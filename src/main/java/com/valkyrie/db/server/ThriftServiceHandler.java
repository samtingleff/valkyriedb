package com.valkyrie.db.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;

import com.mtbaker.client.Configuration;
import com.othersonline.kv.gen.GetResult;
import com.othersonline.kv.gen.KeyValueService;
import com.othersonline.kv.gen.KeyValueStoreException;
import com.othersonline.kv.gen.KeyValueStoreIOException;
import com.valkyrie.db.util.KeyPartitioner;
import com.valkyrie.db.util.KeyPartitionerFactory;

public class ThriftServiceHandler implements KeyValueService.Iface {
	private static final ByteBuffer EMPTY_BYTE_BUFFER = ByteBuffer.wrap(new byte[0]);

	private final Log log = LogFactory.getLog(getClass());

	private Configuration conf;

	private KeyPartitioner keyPartitioner;

	private PartitionedLocalStore localStorage;

	private ValkyrieDbS2SClient s2sClient;

	public ThriftServiceHandler(Configuration conf, PartitionedLocalStore localStorage) {
		this.conf = conf;
		this.localStorage = localStorage;
	}

	public void init() throws Exception {
		this.keyPartitioner = KeyPartitionerFactory.createKeyPartitioner();
		initS2SClient();
	}

	public void close() throws IOException {
	}

	@Override
	public boolean exists(String key) throws KeyValueStoreIOException,
			KeyValueStoreException, TException {
		log.trace("exists()");
		Integer partition = keyPartitioner.getPartition(conf, key.getBytes());
		KratiLocalStore ls = localStorage.getPartition(partition);
		if (ls == null) {
			if (log.isDebugEnabled())
				log.debug("Local partition # " + partition
						+ " is null for key " + key);
			try {
				if (enableServerToServer()) {
					if (log.isDebugEnabled())
						log.debug("Fetching over s2s for key " + key);
					return s2sClient.exists(key);
				}
				else {
					log.debug("s2s disabled");
					return false;
				}
			} catch (IOException e) {
				log.warn("IOException over s2s exists()", e);
				throw new KeyValueStoreIOException();
			}
		} else {
			byte[] bytes = ls.get(key.getBytes());
			if (log.isDebugEnabled()) {
				log.debug("Got bytes " + bytes + " for key " + key);
				if (bytes != null)
					log.debug("Size " + bytes.length + " for key " + key);
			}
			return (bytes != null);
		}
	}

	@Override
	public GetResult getValue(String key) throws KeyValueStoreIOException,
			KeyValueStoreException, TException {
		log.trace("getValue()");
		Integer partition = keyPartitioner.getPartition(conf, key.getBytes());
		KratiLocalStore ls = localStorage.getPartition(partition);
		if (ls == null) {
			if (log.isDebugEnabled())
				log.debug("Local partition # " + partition
						+ " is null for key " + key);
			try {
				if (enableServerToServer()) {
					if (log.isDebugEnabled())
						log.debug("Fetching over s2s for key " + key);
					return s2sClient.get(key);
				}
				else {
					log.debug("s2s disabled");
					return new GetResult(false, EMPTY_BYTE_BUFFER);
				}
			} catch (IOException e) {
				log.warn("IOException over s2s get()", e);
				throw new KeyValueStoreIOException();
			}
		} else {
			byte[] bytes = ls.get(key.getBytes());
			if (log.isDebugEnabled()) {
				log.debug("Got bytes " + bytes + " for key " + key);
				if (bytes != null)
					log.debug("Size " + bytes.length + " for key " + key);
			}
			return (bytes != null)
					? (new GetResult(true, ByteBuffer.wrap(bytes)))
					: (new GetResult(false, EMPTY_BYTE_BUFFER));
		}
	}

	@Override
	public Map<String, GetResult> getBulk(List<String> keys)
			throws KeyValueStoreIOException, KeyValueStoreException, TException {
		log.trace("getBulk()");
		Map<String, GetResult> results = new HashMap<String, GetResult>(keys
				.size());
		for (String key : keys) {
			GetResult gr = getValue(key);
			results.put(key, gr);
		}
		return results;
	}

	@Override
	public void setValue(String key, ByteBuffer b)
			throws KeyValueStoreIOException, KeyValueStoreException, TException {
		log.trace("setValue()");
		Integer partition = keyPartitioner.getPartition(conf, key.getBytes());
		KratiLocalStore ls = localStorage.getPartition(partition);
		if (ls == null) {
			try {
				if (log.isDebugEnabled())
					log.debug("Local partition # " + partition
							+ " is null for key " + key);
				if (enableServerToServer()) {
					if (log.isDebugEnabled())
						log.debug("Saving over s2s for key " + key);
					s2sClient.set(key, b);
				} else {
					log.debug("Skipping s2s for key " + key);
				}
			} catch (IOException e) {
				log.warn("IOException over s2s setValue()", e);
				throw new KeyValueStoreIOException();
			}
		} else {
			byte[] bytes = b.array();
			if (bytes == null) {
				if (log.isDebugEnabled())
					log.debug("Null bytes on set(). Deleting key " + key);
				try {
					ls.delete(key.getBytes());
				} catch (Exception e) {
					log.warn("Exception calling delete()", e);
					throw new KeyValueStoreIOException();
				}
			} else {
				if (log.isDebugEnabled()) {
					if (bytes != null)
						log.debug("Size " + bytes.length + " for key " + key);
				}
				try {
					ls.set(key.getBytes(), b.array());
				} catch (Exception e) {
					log.warn("Exception calling set()", e);
					throw new KeyValueStoreIOException();
				}
			}
		}
	}

	@Override
	public void deleteValue(String key) throws KeyValueStoreIOException,
			KeyValueStoreException, TException {
		log.trace("deleteValue()");
		Integer partition = keyPartitioner.getPartition(conf, key.getBytes());
		KratiLocalStore ls = localStorage.getPartition(partition);
		if (ls == null) {
			try {
				if (log.isDebugEnabled())
					log.debug("Local partition # " + partition
							+ " is null for key " + key);
				if (enableServerToServer()) {
					if (log.isDebugEnabled())
						log.debug("Deleting over s2s for key " + key);
					s2sClient.delete(key);
				} else {
					log.debug("Skipping s2s for key " + key);
				}
			} catch (IOException e) {
				log.warn("IOException over s2s setValue()", e);
				throw new KeyValueStoreIOException();
			}
		} else {
			if (log.isDebugEnabled())
				log.debug("Deleting key " + key);
			try {
				ls.delete(key.getBytes());
			} catch (Exception e) {
				log.warn("Exception calling delete()", e);
				throw new KeyValueStoreIOException();
			}
		}
	}

	protected boolean enableServerToServer() throws IOException {
		return conf.getBoolean("valkyrie.server.s2s", false);
	}

	protected void initS2SClient() throws IOException {
		log.trace("initS2SClient()");
		s2sClient = new ValkyrieDbS2SClient(conf);
		s2sClient.init();
	}
}
