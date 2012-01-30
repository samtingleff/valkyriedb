package com.valkyrie.db.mr;

import java.nio.ByteBuffer;
import java.util.Map;

import com.valkyrie.db.gen.DeleteRequest;
import com.valkyrie.db.gen.GetRequest;
import com.valkyrie.db.gen.GetResponse;
import com.valkyrie.db.gen.Key;
import com.valkyrie.db.gen.SetRequest;
import com.valkyrie.db.gen.ValkyrieDbService;

import clojure.lang.IFn;

public class MemoryExecutionContext implements ExecutionContext {

	private ValkyrieDbService.Iface valkyrie;

	private Map<Object, MemoryCollector<Object>> collector;

	private Map<String, IFn> funcs;

	public MemoryExecutionContext(ValkyrieDbService.Iface valkyrie, Map<String, IFn> funcs, Map<Object, MemoryCollector<Object>> collector) {
		this.valkyrie = valkyrie;
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
		valkyrie.setValue(new SetRequest(new Key(ByteBuffer.wrap(key)), ByteBuffer.wrap(value)));
	}

	@Override
	public byte[] get(byte[] key) throws Exception {
		GetResponse gr = valkyrie.getValue(new GetRequest(new Key(ByteBuffer.wrap(key))));
		return (gr.isExists() ? gr.getData() : null);
	}

	@Override
	public void delete(byte[] key) throws Exception {
		valkyrie.deleteValue(new DeleteRequest(new Key(ByteBuffer.wrap(key))));
	}
}
