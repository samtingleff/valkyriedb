package com.valkyrie.db.util;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.mtbaker.client.Configuration;
import com.othersonline.kv.gen.Constants;

public class HashBytePartitioner implements SimpleKeyPartitioner<byte[]> {
	private Configuration conf;

	private HashFunction<byte[]> hash;

	private List<String> servers;

	private int numPartitions;

	public HashBytePartitioner(Configuration conf, HashFunction<byte[]> hash) throws IOException {
		this.hash = hash;
		this.conf = conf;
		if (conf != null) {
			this.numPartitions = conf.getInteger("partitions.count", 1);
			this.servers = conf.getStringList("servers",
					Collections.singletonList("localhost:" + Constants.DEFAULT_PORT));
		}
	}

	/* (non-Javadoc)
	 * @see com.valkyrie.db.util.SimpleKeyPartitioner#getPartition(byte[])
	 */
	public int getPartition(byte[] key) {
		return getPartition(key, numPartitions);
	}

	// used by hadoop
	/* (non-Javadoc)
	 * @see com.valkyrie.db.util.SimpleKeyPartitioner#getPartition(byte[], int)
	 */
	public int getPartition(byte[] key, int n) {
		long h = this.hash.hash(key);
		return Math.abs((int) (h % n));
	}

	/* (non-Javadoc)
	 * @see com.valkyrie.db.util.SimpleKeyPartitioner#getPartitionList(java.lang.String)
	 */
	public List<Integer> getPartitionList(String host) throws Exception {
		if (host == null)
			host = InetAddress.getLocalHost().getHostName();
		int myhostid = 0, index = 0;
		for (String s : this.servers) {
			String[] split = s.split(":");
			if (split[0].equals(host)) {
				myhostid = index;
				break;
			}
			++index;
		}
		List<Integer> result = new LinkedList<Integer>();
		for (int i = 0; i < this.numPartitions; ++i) {
			int hostid = i % servers.size();
			if (hostid == myhostid)
				result.add(new Integer(i));
		}
		return result;
	}

}
