package com.valkyrie.db.server;

import java.nio.ByteBuffer;

import com.mtbaker.client.Configuration;
import com.othersonline.kv.gen.GetResult;

public class ValkyrieDbS2SClient {
	private Configuration conf;

	public ValkyrieDbS2SClient(Configuration conf) {
		this.conf = conf;
	}

	public void init() { }

	public boolean exists(String key) { return false; }

	public GetResult get(String key) { return null; }

	public void set(String key, ByteBuffer bytes) { }

	public void delete(String key) { }
}
