package com.valkyrie.db.meta;

public class TableMetadataServiceFactory {

	public static TableMetadataService create() {
		return new MemoryTableMetadataService();
	}
}
