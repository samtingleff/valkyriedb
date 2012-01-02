package com.valkyrie.db.util;

public class KeyPartitionerFactory {

	public static KeyPartitioner createKeyPartitioner() {
		return new KeyPartitioner(new Murmur3HashFunction());
	}
}
