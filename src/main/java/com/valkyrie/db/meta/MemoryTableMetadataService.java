package com.valkyrie.db.meta;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.valkyrie.db.gen.ColumnSpec;
import com.valkyrie.db.gen.ColumnType;
import com.valkyrie.db.gen.TableSpec;

public class MemoryTableMetadataService implements TableMetadataService {

	private Map<String, TableSpec> tables = new ConcurrentHashMap<String, TableSpec>();

	@Override
	public void create(TableSpec table) throws TableMetadataError {
		tables.put(table.getName(), table);
	}

	@Override
	public TableSpec read(String table) throws TableMetadataError {
		return tables.get(table);
	}

	@Override
	public ColumnType getColumnType(String table, String column)
			throws TableMetadataError {
		TableSpec ts = tables.get(table);
		if (ts == null)
			throw new TableMetadataError();
		List<ColumnSpec> columns = ts.getColumns();
		for (ColumnSpec cs : columns) {
			if (cs.getColumn().equals(column))
				return cs.getType();
		}
		throw new TableMetadataError();
	}

}
