package com.valkyrie.db.util;

import java.util.List;

public interface KeyPartitioner<K> {

	public int getPartition(K key);

	// used by hadoop
	public int getPartition(K key, int n);

	public List<Integer> getPartitionList(String host)
			throws Exception;

}