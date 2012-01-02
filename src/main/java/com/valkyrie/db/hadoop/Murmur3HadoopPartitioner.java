package com.valkyrie.db.hadoop;

import org.apache.hadoop.mapreduce.Partitioner;

import com.valkyrie.db.util.KeyPartitioner;
import com.valkyrie.db.util.Murmur3HashFunction;

public class Murmur3HadoopPartitioner<K, V> extends Partitioner<K, V> {
	private KeyPartitioner partitioner = new KeyPartitioner(new Murmur3HashFunction());

	@Override
	public int getPartition(K key, V value, int numPartitions) {
		byte[] keyBytes = key.toString().getBytes();
		return partitioner.getPartition(keyBytes, numPartitions);
	}

}
