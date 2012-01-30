package com.valkyrie.db.mr;

import java.util.HashMap;
import java.util.Map;

import com.valkyrie.db.server.KratiLocalStore;

import clojure.lang.IFn;

public class MemoryExecutionContext implements ExecutionContext {

	private Map<Object, MemoryCollector<Object>> collector;

	private KratiLocalStore ks;

	private Map<String, IFn> funcs;

	public MemoryExecutionContext(KratiLocalStore ks, Map<String, IFn> funcs, Map<Object, MemoryCollector<Object>> collector) {
		this.ks = ks;
		this.funcs = funcs;
		this.collector = collector;
	}

	@Override
	public void collect(Object key, Object value) {
		MemoryCollector<Object> list = collector.get(key);
		if (list == null) {
			list = new MemoryCollector<Object>();
			collector.put(key, list);
		}
		list.collect(value);
	}

	@Override
	public void registerIFn(String name, IFn fn) {
		funcs.put(name, fn);
	}

	@Override
	public IFn getIFn(String name) {
		return funcs.get(name);
	}

	@Override
	public void set(byte[] key, byte[] value) throws Exception {
		ks.set(key, value);
	}

	@Override
	public byte[] get(byte[] key) {
		return ks.get(key);
	}

	@Override
	public void delete(byte[] key) throws Exception {
		ks.delete(key);
	}
}
