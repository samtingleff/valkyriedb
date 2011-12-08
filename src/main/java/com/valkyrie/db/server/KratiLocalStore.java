package com.valkyrie.db.server;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;

import krati.core.segment.ChannelSegmentFactory;
import krati.store.DataStore;
import krati.store.DynamicDataStore;
import krati.util.FnvHashFunction;

public class KratiLocalStore {

	private DataStore<byte[], byte[]> store;

	public KratiLocalStore(File dir,
			int initLevel,
			int batchSize,
			int numSyncBatches,
			int segmentFileSizeMB,
			double segmentCompactFactor,
			double hashLoadFactor) throws Exception {
		store = createDataStore(dir,
				initLevel,
				batchSize,
				numSyncBatches,
				segmentFileSizeMB,
				segmentCompactFactor,
				hashLoadFactor);
	}

	public KratiLocalStore(File dir, Configuration conf) throws Exception {
		int initLevel = conf.getInt("valkyriedb.krati.initLevel", 5);
		int batchSize = conf.getInt("valkyriedb.krati.batchSize", 100);
		int numSyncBatches = conf.getInt("valkyriedb.krati.numSyncBatches", 5);
		int segmentFileSizeMB = conf.getInt("valkyriedb.krati.segmentFileSizeMB", 256);
		double segmentCompactFactor = conf.getFloat("valkyriedb.krati.segmentCompactFactor", .5f);
		double hashLoadFactor = conf.getFloat("valkyriedb.krati.hashLoadFactor", .75f);
		store = createDataStore(dir,
				initLevel,
				batchSize,
				numSyncBatches,
				segmentFileSizeMB,
				segmentCompactFactor,
				hashLoadFactor);
	}

	public KratiLocalStore(File dir, com.mtbaker.client.Configuration conf) throws Exception {
		int initLevel = conf.getInteger("valkyriedb.krati.initLevel", 5);
		int batchSize = conf.getInteger("valkyriedb.krati.batchSize", 100);
		int numSyncBatches = conf.getInteger("valkyriedb.krati.numSyncBatches", 5);
		int segmentFileSizeMB = conf.getInteger("valkyriedb.krati.segmentFileSizeMB", 256);
		double segmentCompactFactor = conf.getDouble("valkyriedb.krati.segmentCompactFactor", .5);
		double hashLoadFactor = conf.getDouble("valkyriedb.krati.hashLoadFactor", .75);
		store = createDataStore(dir,
				initLevel,
				batchSize,
				numSyncBatches,
				segmentFileSizeMB,
				segmentCompactFactor,
				hashLoadFactor);
	}

	public void set(byte[] key, byte[] value) throws Exception {
		store.put(key, value);
	}

	public byte[] get(byte[] key) {
		return store.get(key);
	}

	public void delete(byte[] key) throws Exception {
		store.delete(key);
	}

	public void sync() throws IOException {
		store.sync();
	}

	public void persist() throws IOException {
		store.persist();
	}

	public void clear() throws IOException {
		store.clear();
	}

	public void close() throws IOException {
		store.close();
	}

	protected DataStore<byte[], byte[]> createDataStore(File dir,
			int initLevel,
			int batchSize,
			int numSyncBatches,
			int segmentFileSizeMB,
			double segmentCompactFactor,
			double hashLoadFactor) throws Exception {
		// reasonable dev values:
		// initLevel: 0
		// batchSize: 100
		// numSyncBatches: 5
		// segmentFileSizeMB: 256
		// segmentCompactFactor: .5
		// hashLoadFactor: .75
		// reasonable production values:
		// initLevel: 10
		// batchSize: 10000
		// numSyncBatches: 5
		// segmentFileSizeMB: 256
		// segmentCompactFactor: .5
		// hashLoadFactor: .75
		
		// http://groups.google.com/group/krati/browse_thread/thread/fbc445367da4430f?pli=1
		return new DynamicDataStore(dir,
				initLevel,
				batchSize,
				numSyncBatches,
				segmentFileSizeMB,
				new ChannelSegmentFactory(),
				segmentCompactFactor,
				hashLoadFactor,
				new FnvHashFunction());
	}
}
