package com.valkyrie.db.util;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.mtbaker.client.Configuration;
import com.othersonline.kv.gen.Constants;

public class KeyPartitioner {
	private Configuration conf;

	private HashFunction<byte[]> hash;

	private List<String> servers;

	private int numPartitions;

	public KeyPartitioner(Configuration conf, HashFunction<byte[]> hash) throws IOException {
		this.hash = hash;
		this.conf = conf;
		if (conf != null) {
			this.numPartitions = conf.getInteger("valkyrie.partitions.count", 1);
			this.servers = conf.getStringList("servers",
					Collections.singletonList("localhost:" + Constants.DEFAULT_PORT));
		}
	}

	public int getPartition(byte[] key) {
		return getPartition(key, numPartitions);
	}

	// used by hadoop
	public int getPartition(byte[] key, int n) {
		long h = this.hash.hash(key);
		return Math.abs((int) (h % n));
	}

	public List<Integer> getPartitionList(String host) throws Exception {
		List<String> servers = conf.getStringList("valkyrie.servers", Collections.EMPTY_LIST);
		int numpartitions = conf.getInteger("valkyrie.partitions.count", 1);
		if (host == null)
			host = InetAddress.getLocalHost().getHostName();
		int myhostid = 0, index = 0;
		for (String s : servers) {
			String[] split = s.split(":");
			if (split[0].equals(host)) {
				myhostid = index;
				break;
			}
			++index;
		}
		List<Integer> result = new LinkedList<Integer>();
		for (int i = 0; i < numpartitions; ++i) {
			int hostid = i % servers.size();
			if (hostid == myhostid)
				result.add(new Integer(i));
		}
		return result;
	}

}
