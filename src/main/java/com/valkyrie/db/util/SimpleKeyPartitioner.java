package com.valkyrie.db.util;

import java.util.List;

public interface SimpleKeyPartitioner<K> {

	public abstract int getPartition(K key);

	// used by hadoop
	public abstract int getPartition(K key, int n);

	public abstract List<Integer> getPartitionList(String host)
			throws Exception;

}