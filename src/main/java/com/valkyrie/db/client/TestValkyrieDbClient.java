package com.valkyrie.db.client;

import com.othersonline.kv.transcoder.ByteArrayTranscoder;
import com.othersonline.kv.transcoder.Transcoder;

public class TestValkyrieDbClient {

	public static void main(String[] args) throws Exception {
		ValkyrieDbClient client = new ValkyrieDbClient();
		client.start();
		Transcoder transcoder = new ByteArrayTranscoder();
		
		int nulls = 0, successes = 0, exceptions = 0;
		int numKeys = Integer.parseInt(args[0]);
		for (int i = 1000; i < numKeys; ++i) {
			try {
				String k = Integer.toString(i);
				Object obj = client.get(k, transcoder);
				if (obj != null)
					successes++;
				else
					nulls++;
			} catch(Exception e) {
				e.printStackTrace();
				exceptions++;
			} finally {}
		}
		System.err.println("Exceptions: " + exceptions);
		System.err.println("Nulls:      " + nulls);
		System.err.println("Successes:  " + successes);
	}
}
