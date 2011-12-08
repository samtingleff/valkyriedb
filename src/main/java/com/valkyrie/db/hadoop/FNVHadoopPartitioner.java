package com.valkyrie.db.hadoop;

import org.apache.hadoop.mapreduce.Partitioner;

import com.valkyrie.db.util.FNVHashFunction;
import com.valkyrie.db.util.KeyPartitioner;

public class FNVHadoopPartitioner<K, V> extends Partitioner<K, V> {
	private KeyPartitioner partitioner = new KeyPartitioner(new FNVHashFunction());

	@Override
	public int getPartition(K key, V value, int numPartitions) {
		byte[] keyBytes = key.toString().getBytes();
		return partitioner.getPartition(keyBytes, numPartitions);
	}

}
