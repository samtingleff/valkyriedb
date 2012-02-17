package com.valkyrie.db.hadoop;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Partitioner;

import com.valkyrie.db.util.KeyPartitionerFactory;
import com.valkyrie.db.util.SimpleKeyPartitioner;

public class Murmur3HadoopPartitioner<K, V> extends Partitioner<K, V> {
	private SimpleKeyPartitioner partitioner;

	public Murmur3HadoopPartitioner() {
		try {
			this.partitioner = KeyPartitionerFactory.createKeyPartitioner(null);
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
