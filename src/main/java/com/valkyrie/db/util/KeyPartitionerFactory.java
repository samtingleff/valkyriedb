package com.valkyrie.db.util;

import java.io.IOException;

import com.mtbaker.client.Configuration;

public class KeyPartitionerFactory {

	public static KeyPartitioner<byte[]> defaultKeyPartitioner(Configuration conf) throws IOException {
		return new HashBytePartitioner(conf, new Murmur3HashFunction());
	}
}
