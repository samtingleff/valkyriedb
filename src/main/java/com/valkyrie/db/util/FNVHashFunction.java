package com.valkyrie.db.util;

/**
 * Taken from http://www.isthe.com/chongo/tech/comp/fnv and Voldemort (voldemort.utils.FnvHashFunction)
 *
 * FNV-1
 * <code>
 *   hash = basis
 *   for each octet_of_data to be hashed
 *       hash = hash * FNV_prime
 *       hash = hash xor octet_of_data
 *   return hash
 * </code>
 * 
 * FNV-1a
 * <code>
 *   hash = basis
 *   for each octet_of_data to be hashed
 *       hash = hash xor octet_of_data
 *       hash = hash * FNV_prime
 *   return hash
 * </code>
 */
public class FNVHashFunction implements HashFunction<byte[]> {

	@Override
	public long hash(byte[] data) {
		long hash = Fnv1Hash32.FNV_BASIS;
        for(int i = 0; i < data.length; i++) {
            hash ^= 0xFF & data[i];
            hash *= Fnv1Hash32.FNV_PRIME;
        }
        
        return (hash == HashFunction.NON_HASH_CODE) ? HashFunction.MAX_HASH_CODE : hash;
	}

}
