package com.valkyrie.db.util;

import java.io.IOException;

import com.mtbaker.client.Configuration;

public class KeyPartitioner {
	private HashFunction<byte[]> hash;

	public KeyPartitioner(HashFunction<byte[]> hash) {
		this.hash = hash;
	}

	public int getPartition(Configuration conf, byte[] key) {
		try {
			int numPartitions = conf.getInteger("valkyrie.partitions.count", 1);
			return getPartition(key, numPartitions);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public int getPartition(org.apache.hadoop.conf.Configuration conf, byte[] key) {
		int numPartitions = conf.getInt("valkyrie.partitions.count", 1);
		return getPartition(key, numPartitions);
	}

	public int getPartition(byte[] key, int n) {
		long h = this.hash.hash(key);
		return Math.abs((int) (h % n));
	}

}
