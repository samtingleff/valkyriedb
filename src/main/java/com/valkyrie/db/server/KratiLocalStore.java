package com.valkyrie.db.server;

import java.io.File;
import java.io.IOException;

import com.mtbaker.client.annotations.Configurable;
import com.mtbaker.client.annotations.ConfigurableField;

import krati.core.segment.ChannelSegmentFactory;
import krati.store.DataStore;
import krati.store.DynamicDataStore;
import krati.util.FnvHashFunction;

@Configurable("krati")
public class KratiLocalStore {

	private DataStore<byte[], byte[]> store;

	@ConfigurableField("dir")
	private String dir;

	@ConfigurableField("initLevel")
	private int initLevel = 5;

	@ConfigurableField("batchSize")
	private int batchSize = 100;

	@ConfigurableField("numSyncBatches")
	private int numSyncBatches = 5;

	@ConfigurableField("segmentFileSizeMB")
	private int segmentFileSizeMB = 64;

	@ConfigurableField("segmentCompactFactor")
	private double segmentCompactFactor = 0.5;

	@ConfigurableField("hashLoadFactor")
	private double hashLoadFactor = 0.75;

	public KratiLocalStore(String dir,
			int initLevel,
			int batchSize,
			int numSyncBatches,
			int segmentFileSizeMB,
			double segmentCompactFactor,
			double hashLoadFactor) throws Exception {
		this.dir = dir;
		this.initLevel = initLevel;
		this.batchSize = batchSize;
		this.numSyncBatches = numSyncBatches;
		this.segmentFileSizeMB = segmentFileSizeMB;
		this.segmentCompactFactor = segmentCompactFactor;
		this.hashLoadFactor = hashLoadFactor;
	}

	public KratiLocalStore() {
	}

	public void init() throws Exception {
		store = createDataStore(new File(dir),
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

	public void setDir(String dir) {
		this.dir = dir;
	}

	public void setInitLevel(int initLevel) {
		this.initLevel = initLevel;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	public void setNumSyncBatches(int numSyncBatches) {
		this.numSyncBatches = numSyncBatches;
	}

	public void setSegmentFileSizeMB(int segmentFileSizeMB) {
		this.segmentFileSizeMB = segmentFileSizeMB;
	}

	public void setSegmentCompactFactor(double segmentCompactFactor) {
		this.segmentCompactFactor = segmentCompactFactor;
	}

	public void setHashLoadFactor(double hashLoadFactor) {
		this.hashLoadFactor = hashLoadFactor;
	}
}
