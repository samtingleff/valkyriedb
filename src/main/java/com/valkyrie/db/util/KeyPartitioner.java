package com.valkyrie.db.util;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mtbaker.client.Configuration;
import com.othersonline.kv.gen.Constants;

public class KeyPartitioner {
	private HashFunction<byte[]> hash;

	private List<String> servers;

	private List<Host> hosts;

	private int numPartitions;

	public KeyPartitioner(Configuration conf, HashFunction<byte[]> hash) throws IOException {
		this.hash = hash;
		if (conf != null) {
			this.numPartitions = conf.getInteger("valkyrie.partitions.count", 1);
			this.servers = conf.getStringList("servers",
					Collections.singletonList("localhost:" + Constants.DEFAULT_PORT));
			this.hosts = new ArrayList<Host>(servers.size());
			for (String s : servers) {
				String[] split = s.split(":");
				InetAddress ia = InetAddress.getByName(split[0]);
				int port = Integer.parseInt(split[1]);
				this.hosts.add(new Host(ia, port));
			}
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

	private static class Host {
		private InetAddress ia;
		private int port;
		public Host(InetAddress ia, int port) {
			this.ia = ia;
			this.port = port;
		}
	}

}
