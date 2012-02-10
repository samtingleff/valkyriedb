package com.valkyrie.db.hadoop;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Partitioner;

import com.valkyrie.db.util.KeyPartitioner;
import com.valkyrie.db.util.Murmur3HashFunction;

public class Murmur3HadoopPartitioner<K, V> extends Partitioner<K, V> {
	private KeyPartitioner partitioner;

	public Murmur3HadoopPartitioner() {
		try {
			this.partitioner = new KeyPartitioner(null, new Murmur3HashFunction());
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int getPartition(K key, V value, int numPartitions) {
		byte[] keyBytes = key.toString().getBytes();
		return partitioner.getPartition(keyBytes, numPartitions);
	}

}
