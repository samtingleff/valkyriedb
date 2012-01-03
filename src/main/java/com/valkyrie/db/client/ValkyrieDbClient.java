package com.valkyrie.db.client;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;

import com.mtbaker.client.Configuration;
import com.mtbaker.client.ConfigurationClient;
import com.mtbaker.client.provider.io.FileInputStreamSource;
import com.mtbaker.client.provider.xml.XmlConfigurationClient;
import com.othersonline.kv.BaseKeyValueStore;
import com.othersonline.kv.KeyValueStore;
import com.othersonline.kv.KeyValueStoreException;
import com.othersonline.kv.gen.Constants;
import com.othersonline.kv.gen.GetResult;
import com.othersonline.kv.transcoder.Transcoder;
import com.valkyrie.db.gen.GetRequest;
import com.valkyrie.db.gen.GetResponse;
import com.valkyrie.db.gen.Key;
import com.valkyrie.db.gen.ValkyrieDbService;
import com.valkyrie.db.util.KeyPartitioner;
import com.valkyrie.db.util.KeyPartitionerFactory;

public class ValkyrieDbClient extends BaseKeyValueStore implements KeyValueStore {

	public static void main(String[] args) throws Exception {
		ValkyrieDbClient client = new ValkyrieDbClient();
		client.start();
		boolean b = client.exists("foobaz");
		byte[] bytes1 = (byte[]) client.get("foobar");
		byte[] bytes2 = (byte[]) client.get("foobaz");
		System.err.println(b);
		System.err.println(bytes2);

		System.err.println(client.exists("0"));
		System.err.println(client.exists("1"));
		String[] keys = new String[] { "jonah", "neal", "sarah", "foo", "sam" };
		for (String k : keys) {
			System.err.println(client.exists(k));
			if (client.exists(k)) {
				System.err.println(new String((byte[]) client.get(k)));
				String v = new String((byte[]) client.get(k));
				System.err.println(v.equals("1"));
			}
		}
	}

	protected Log log = LogFactory.getLog(getClass());

	protected ConfigurationClient conf;

	protected Configuration serverConf;

	protected GenericKeyedObjectPool pool;

	protected KeyPartitioner keyPartitioner;

	public ValkyrieDbClient() {
	}

	@Override
	public String getIdentifier() {
		return "valkyrie-db";
	}

	@Override
	public void start() throws IOException {
		log.trace("start()");
		initConfiguration();
		initKeyPartitioner();
		initConnectionPool();
		super.start();
	}

	@Override
	public void stop() {
		log.trace("stop()");
		super.stop();
		try {
			pool.close();
		} catch (Exception e) {
		}
	}

	@Override
	public boolean exists(String key) throws KeyValueStoreException,
			IOException {
		log.trace("exists()");
		assertReadable();
		TConnection tc = null;
		try {
			tc = getConnection(key);
			int partition = keyPartitioner.getPartition(serverConf, key.getBytes());
			Key k = new Key(ByteBuffer.wrap(key.getBytes()));
			k.setPartition(partition);
			GetRequest gr = new GetRequest(k);
			return tc.kv.exists(gr);
		} catch (Exception e) {
			e.printStackTrace();
			throw new KeyValueStoreException();
		} finally {
			returnConnection(tc);
		}
	}

	@Override
	public Object get(String key) throws KeyValueStoreException, IOException {
		log.trace("get()");
		assertReadable();
		try {
			GetResult g = getValue(key);
			Object obj = (g.isExists()) ? g.getData() : null;
			return obj;
		} catch (Exception e) {
			throw new KeyValueStoreException(e);
		}
	}

	@Override
	public Object get(String key, Transcoder transcoder)
			throws KeyValueStoreException, IOException {
		log.trace("get()");
		assertReadable();
		try {
			GetResult g = getValue(key);
			Object result = null;
			if (g.isExists()) {
				byte[] data = g.getData();
				result = transcoder.decode(data);
			}
			return result;
		} catch (Exception e) {
			throw new KeyValueStoreException(e);
		}
	}

	@Override
	public Map<String, Object> getBulk(String... keys)
			throws KeyValueStoreException, IOException {
		log.trace("getBulk()");
		assertReadable();
		try {
			Map<String, Object> results = new HashMap<String, Object>(keys.length);
			for (String key : keys) {
				GetResult g = getValue(key);
				if (g.isExists())
					results.put(key, g.getData());
			}
			return results;
		} catch (Exception e) {
			throw new KeyValueStoreException(e);
		}
	}

	@Override
	public Map<String, Object> getBulk(List<String> keys)
			throws KeyValueStoreException, IOException {
		log.trace("getBulk()");
		assertReadable();
		try {
			Map<String, Object> results = new HashMap<String, Object>(keys.size());
			for (String key : keys) {
				GetResult g = getValue(key);
				if (g.isExists())
					results.put(key, g.getData());
			}
			return results;
		} catch (Exception e) {
			throw new KeyValueStoreException(e);
		}
	}

	@Override
	public Map<String, Object> getBulk(List<String> keys, Transcoder transcoder)
			throws KeyValueStoreException, IOException {
		log.trace("getBulk()");
		assertReadable();
		try {
			Map<String, Object> results = new HashMap<String, Object>(keys.size());
			for (String key : keys) {
				GetResult g = getValue(key);
				if (g.isExists()) {
					byte[] bytes = g.getData();
					Object obj = transcoder.decode(bytes);
					results.put(key, obj);
				}
			}
			return results;
		} catch (Exception e) {
			throw new KeyValueStoreException(e);
		}
	}

	@Override
	public void set(String key, Object value) throws KeyValueStoreException,
			IOException {
	}

	@Override
	public void set(String key, Object value, Transcoder transcoder)
			throws KeyValueStoreException, IOException {
	}

	@Override
	public void delete(String key) throws KeyValueStoreException, IOException {
	}

	protected GetResult getValue(String key) throws Exception {
		TConnection tc = getConnection(key);
		try {
			int partition = keyPartitioner.getPartition(serverConf, key.getBytes());
			Key k = new Key(ByteBuffer.wrap(key.getBytes()));
			k.setPartition(partition);
			GetRequest request = new GetRequest(k);
			GetResponse r = tc.kv.getValue(request);
			GetResult gr = new GetResult(r.isExists(), r.bufferForData());
			return gr;
		} finally {
			returnConnection(tc);
		}
	}

	protected void initConfiguration() throws IOException {
		File f = new File("/etc/valkyriedb.xml");
		conf = new XmlConfigurationClient(new FileInputStreamSource(f));
		serverConf = conf.getConfiguration("server", 1000*60*10);
	}

	protected void initKeyPartitioner() throws IOException {
		this.keyPartitioner = KeyPartitionerFactory.createKeyPartitioner();
	}

	protected void initConnectionPool() {
		pool = new GenericKeyedObjectPool(new TConnectionFactory());
	}

	protected TConnection getConnection(String key) throws Exception {
		log.trace("getConnection()");
		int partition = keyPartitioner.getPartition(serverConf, key.getBytes());
		if (log.isDebugEnabled())
			log.debug("Got partition " + partition + " for key " + key);
		String server = getBackend(partition);
		if (log.isDebugEnabled())
			log.debug("Got server " + server + " for partition " + partition);
		TConnection tc = (TConnection) pool.borrowObject(server);
		if (log.isDebugEnabled())
			log.debug("Got connection");
		return tc;
	}

	protected void returnConnection(TConnection tc) {
		try {
			pool.returnObject(tc.server, tc);
		} catch (Exception e) {
		}
	}

	protected String getBackend(int partition) throws IOException {
		List<String> servers = serverConf.getStringList("servers",
				Collections.singletonList("localhost:" + Constants.DEFAULT_PORT));
		int host = Math.abs(partition % servers.size());
		return servers.get(host);
	}

	private class TConnection {
		private String server;

		private TSocket socket;

		private TFramedTransport transport;

		private TProtocol protocol;

		private ValkyrieDbService.Iface kv;

		public TConnection(String server) {
			this.server = server;
			String[] split = server.split(":");
			socket = new TSocket(split[0], Integer.parseInt(split[1]));
			transport = new TFramedTransport(socket);
			protocol = new TBinaryProtocol(transport);
			kv = new ValkyrieDbService.Client(protocol);
		}

		public void open() throws TTransportException {
			transport.open();
		}

		public void close() {
			transport.close();
		}
	}

	private class TConnectionFactory implements KeyedPoolableObjectFactory {

		@Override
		public Object makeObject(Object key) throws Exception {
			// called whenever a new instance is needed
			TConnection tc = new TConnection((String) key);
			tc.open();
			return tc;
		}

		@Override
		public void activateObject(Object key, Object obj) throws Exception {
			// invoked on every instance that has been passivated before it is borrowed from the pool
		}

		@Override
		public boolean validateObject(Object key, Object obj) {
			// invoked on activated instances to make sure they can be borrowed from the pool
			TConnection tc = (TConnection) obj;
			boolean result = false;
			try {
				byte[] k = "foo".getBytes();
				int partition = keyPartitioner.getPartition(serverConf, k);
				Key keyK = new Key(ByteBuffer.wrap(k));
				keyK.setPartition(partition);
				GetRequest request = new GetRequest(keyK);
				boolean b = tc.kv.exists(request);
				result = true;
			} catch(Exception e) {
			}
			return result;
		}

		@Override
		public void passivateObject(Object key, Object obj) throws Exception {
			// is invoked on every instance when it is returned to the pool
		}

		@Override
		public void destroyObject(Object key, Object obj) throws Exception {
			// invoked on every instance when it is being "dropped" from the pool
			((TConnection) obj).close();
		}
		
	}
}
