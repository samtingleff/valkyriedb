package com.valkyrie.db.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.mtbaker.client.Configuration;
import com.mtbaker.client.provider.properties.PropertiesConfigurationClient;
import com.valkyrie.db.server.KratiLocalStore;

public class TestLocalValkyrieDbClient {

	public static void main(String[] args) throws Exception {
		TestLocalValkyrieDbClient client = new TestLocalValkyrieDbClient();
		client.run();
	}

	private KratiLocalStore store;

	private Configuration conf;

	private void run() throws Exception {
		initConfiguration();
		store = new KratiLocalStore(
				new File("/Users/stingleff/tmp/valkyriedb/1/0/index.current"),
				conf);
		/*
		DataStore<byte[], byte[]> ds = store.store;
		IndexedIterator<Entry<byte[], byte[]>> iter = ds.iterator();
		while (iter.hasNext()) {
			Entry<byte[], byte[]> e = iter.next();
			//System.out.println(new String(e.getKey()));
			//System.out.println(new String(e.getValue()));
			if ("0".equals(new String(e.getKey())))
				System.out.println(new String(e.getValue()));
		}
		KeyPartitioner kp = new KeyPartitioner(new FNVHashFunction());
		System.out.println(kp.getPartition("21".getBytes(), 12));
		*/
	}
	private void initConfiguration() throws IOException {
		Properties props = new Properties();
		InputStream is = new FileInputStream("/etc/valkyrie.server.properties");
		try {
			props.load(is);
			PropertiesConfigurationClient client = new PropertiesConfigurationClient(
					props);
			conf = client.getConfiguration(null, 10);
		} finally {
			is.close();
		}
	}
}
