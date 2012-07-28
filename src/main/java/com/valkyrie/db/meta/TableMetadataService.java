package com.valkyrie.db.meta;

import com.valkyrie.db.gen.ColumnType;
import com.valkyrie.db.gen.TableSpec;

public interface TableMetadataService {

	public void create(TableSpec table) throws TableMetadataError;

	public TableSpec read(String table) throws TableMetadataError;

	public ColumnType getColumnType(String table, String column) throws TableMetadataError;
}
