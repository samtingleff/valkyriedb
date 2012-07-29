package com.valkyrie.db.backend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.thrift.TException;

import com.almworks.sqlite4java.SQLParts;
import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import com.valkyrie.db.gen.AggregateColumnSpec;
import com.valkyrie.db.gen.ColumnSpec;
import com.valkyrie.db.gen.ColumnType;
import com.valkyrie.db.gen.ColumnValue;
import com.valkyrie.db.gen.ColumnValueList;
import com.valkyrie.db.gen.Query;
import com.valkyrie.db.gen.QueryResult;
import com.valkyrie.db.gen.Row;
import com.valkyrie.db.gen.TableSpec;
import com.valkyrie.db.gen.Value;
import com.valkyrie.db.meta.TableMetadataError;
import com.valkyrie.db.meta.TableMetadataService;

public class SQLLiteBackend implements StorageBackend {

	private Map<ColumnType, String> sqlTypes = new HashMap<ColumnType, String>() {
		{
			put(ColumnType.IntegerType, "INT");
			put(ColumnType.LongType, "BIGINT");
			put(ColumnType.DoubleType, "FLOAT");
			put(ColumnType.StringType, "VARCHAR");
			put(ColumnType.BytesType, "BLOB");
		}
	};

	private TableMetadataService tables;

	private SQLiteConnection db;

	public SQLLiteBackend(TableMetadataService tables) {
		this.tables = tables;
	}

	@Override
	public void init() throws IOException {
		db = new SQLiteConnection();
		try {
			db.open(true);
		} catch (SQLiteException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void close() {
		db.dispose();
	}

	@Override
	public void createTable(TableSpec table) throws TException {
		String stmt = compileTableSpec(table);
		try {
			System.out.println(stmt);
			execute(stmt);
		tables.create(table);
		} catch (SQLiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TableMetadataError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally { }
	}

	@Override
	public void dropTable(String table) throws TException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insert(String table, List<String> columns,
			List<ColumnValueList> values) throws TException {
		try {
		SQLiteStatement stmt = prepareInsert(table, columns, values);
		try {
		for (ColumnValueList row : values) {
			List<ColumnValue> cvs = row.getValues();
			int index = 0;
			for (ColumnValue cv : cvs) {
				String column = columns.get(index);
				ColumnType ct = tables.getColumnType(table, column);
				bind(stmt, index + 1, ct, cv);
				++index;
			}
			stmt.step();
			stmt.clearBindings();
		}
		} finally {
		stmt.dispose();
		}
		} catch (SQLiteException e) {
			throw new TException(e);
		} catch (TableMetadataError e) {
			throw new TException(e);
		} finally { }
	}

	@Override
	public QueryResult select(Query query) throws TException {
		try {
			List<Row> rows = new ArrayList<Row>();
			List<AggregateColumnSpec> columns = query.getColumns();
			SQLiteStatement st = prepareSelect(query.getTable(), columns);
			while (st.step()) {
				Row row = new Row();
				int count = st.columnCount();
				for (int i = 0; i < count; ++i) {
					Value v = new Value();
					ColumnSpec cs = columns.get(i).getColumn();
					v.setType(cs.getType());
					ColumnValue cv = new ColumnValue();
					setColumnValue(st, cs.getType(), i, cv);
					v.setValue(cv);
					row.addToValues(v);
				}
				rows.add(row);
			}
			return new QueryResult(rows);
		} catch (SQLiteException e) {
			throw new TException(e);
		} catch (TableMetadataError e) {
			throw new TException(e);
		}
	}

	private void setColumnValue(SQLiteStatement stmt, ColumnType ct, int index, ColumnValue v) throws SQLiteException {
		switch (ct) {
		case IntegerType:
			v.setV_int(stmt.columnInt(index));
			break;
		case LongType:
			v.setV_long(stmt.columnLong(index));
			break;
		case DoubleType:
			v.setV_double(stmt.columnDouble(index));
			break;
		case StringType:
			v.setV_string(stmt.columnString(index));
			break;
		case BytesType:
			v.setV_bytes(stmt.columnBlob(index));
			break;
		default:
			break;
		}
	}

	private void bind(SQLiteStatement stmt, int index, ColumnType ct, ColumnValue cv) throws SQLiteException {
		switch (ct) {
			case IntegerType:
				stmt.bind(index, cv.getV_int());
				break;
			case LongType:
				stmt.bind(index, cv.getV_long());
				break;
			case DoubleType:
				stmt.bind(index, cv.getV_double());
				break;
			case StringType:
				stmt.bind(index, cv.getV_string());
				break;
			case BytesType:
				stmt.bind(index, cv.getV_bytes());
				break;
			default:
				break;
		}
	}

	private String compileTableSpec(TableSpec table) {
		StringBuffer sb = new StringBuffer();
		sb.append("create table ");
		sb.append(table.getName());
		sb.append(" ( ");
		int index = 0;
		for (ColumnSpec column : table.getColumns()) {
			if (index != 0)
				sb.append(',');
			sb.append(column.getColumn());
			sb.append(' ');
			sb.append(getSQLType(column.getType()));
			++index;
		}
		sb.append(" )");
		return sb.toString();
	}

	private SQLiteStatement prepareSelect(String table, List<AggregateColumnSpec> columns) throws SQLiteException, TableMetadataError {
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		int index = 0;
		for (AggregateColumnSpec column : columns) {
			if (index != 0)
				sb.append(',');
			sb.append(column.getColumn().getColumn());
			++index;
		}
		sb.append(" from ");
		sb.append(table);
		/*sb.append(" (");
		sb.append(") values (");
		for (int i = 0 ; i < index; ++i) {
			if (i != 0)
				sb.append(',');
			sb.append('?');
		}
		sb.append(")");*/
		String sql = sb.toString();
		System.out.println(sql);
		SQLiteStatement ps = db.prepare(sql);
		return ps;
	}

	private SQLiteStatement prepareInsert(String table, List<String> columns,
			List<ColumnValueList> values) throws SQLiteException, TableMetadataError {
		StringBuffer sb = new StringBuffer();
		sb.append("insert into ");
		sb.append(table);
		sb.append(" (");
		int index = 0;
		for (String column : columns) {
			if (index != 0)
				sb.append(',');
			sb.append(column);
			++index;
		}
		sb.append(") values (");
		for (int i = 0 ; i < index; ++i) {
			if (i != 0)
				sb.append(',');
			sb.append('?');
		}
		sb.append(")");
		String sql = sb.toString();
		System.out.println(sql);
		SQLiteStatement ps = db.prepare(sql);
		return ps;
	}

	private String getSQLType(ColumnType type) {
		return sqlTypes.get(type);
	}

	private void execute(String stmt) throws SQLiteException {
		db.exec(stmt);
	}
}
