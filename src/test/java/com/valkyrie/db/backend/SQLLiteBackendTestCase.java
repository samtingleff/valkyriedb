package com.valkyrie.db.backend;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.valkyrie.db.gen.Aggregate;
import com.valkyrie.db.gen.AggregateColumnSpec;
import com.valkyrie.db.gen.Column;
import com.valkyrie.db.gen.ColumnSpec;
import com.valkyrie.db.gen.ColumnType;
import com.valkyrie.db.gen.ColumnValue;
import com.valkyrie.db.gen.ColumnValueList;
import com.valkyrie.db.gen.Query;
import com.valkyrie.db.gen.QueryResult;
import com.valkyrie.db.gen.Row;
import com.valkyrie.db.gen.TableSpec;
import com.valkyrie.db.gen.Value;
import com.valkyrie.db.meta.TableMetadataServiceFactory;

import junit.framework.TestCase;

public class SQLLiteBackendTestCase extends TestCase {

	private SQLLiteBackend backend;

	public void setUp() throws Exception {
		backend = new SQLLiteBackend(TableMetadataServiceFactory.create());
		backend.init();
	}

	public void tearDown() throws Exception {
		backend.close();
	}

	public void testSimple() throws Exception {
		//TableSpec ts = createTableSpec();
		String table = "foo";
		backend.execute("create table foo ( a_int INT,b_long BIGINT,c_double FLOAT,d_string VARCHAR,e_bytes BLOB )");
		ColumnValue cv = new ColumnValue();
		cv.setV_int(12);
		backend.insert(table,
				Collections.singletonList(new Column(
						new ColumnSpec("a_int", ColumnType.IntegerType),
						cv)),
				Arrays.asList(
						new ColumnValueList(Collections.singletonList(cv))));
		QueryResult qr = backend.select(
				new Query(
						table,
						Arrays.asList(
								new AggregateColumnSpec(
										new ColumnSpec("a_int", ColumnType.IntegerType),
										Aggregate.None),
								new AggregateColumnSpec(
										new ColumnSpec("b_long", ColumnType.LongType),
										Aggregate.None)),
						Collections.EMPTY_LIST,
						Collections.EMPTY_LIST));
		assertNotNull(qr);
		List<Row> rows = qr.getRows();
		assertNotNull(rows);
		assertEquals(rows.size(), 1);
		Row row = rows.get(0);
		List<Value> columns = row.getValues();
		assertNotNull(columns);
		assertEquals(columns.size(), 2);
		assertEquals(columns.get(0).getValue().getV_int(), 12);
		assertEquals(columns.get(1).getValue().getV_long(), 0);
		backend.execute("drop table foo");
	}

	public void testSimpleTable() throws Exception {
		createTable();
		backend.execute("drop table foo");
	}

	private void createTable() throws Exception {
		backend.execute("create table foo ( a_int INT,b_long BIGINT,c_double FLOAT,d_string VARCHAR,e_bytes BLOB )");
	}

	private TableSpec createTableSpec() {
		List<ColumnSpec> columns = Arrays.asList(
				new ColumnSpec("a_int", ColumnType.IntegerType),
				new ColumnSpec("b_long", ColumnType.LongType),
				new ColumnSpec("c_double", ColumnType.DoubleType),
				new ColumnSpec("d_string", ColumnType.StringType),
				new ColumnSpec("e_bytes", ColumnType.BytesType)
		);
		TableSpec ts = new TableSpec("foo", columns);
		return ts;
	}
}
