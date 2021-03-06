package com.valkyrie.db.util;

import java.io.IOException;

import com.mtbaker.client.Configuration;

public class KeyPartitionerFactory {

	public static KeyPartitioner createKeyPartitioner(Configuration conf) throws IOException {
		return new KeyPartitioner(conf, new Murmur3HashFunction());
	}
}
