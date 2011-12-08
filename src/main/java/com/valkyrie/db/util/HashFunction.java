package com.valkyrie.db.util;

/**
 * Partially copied from krati
 * 
 * https://github.com/jingwei/krati/blob/master/src/main/java/krati/util/HashFunction.java
 * 
 * @author stingleff
 *
 * @param <K>
 */
public interface HashFunction<K> {
	public static final long NON_HASH_CODE = 0;

	public static final long MIN_HASH_CODE = Long.MIN_VALUE;

	public static final long MAX_HASH_CODE = Long.MAX_VALUE;

	public long hash(K data);
}
