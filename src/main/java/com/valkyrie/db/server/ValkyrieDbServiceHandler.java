package com.valkyrie.db.server;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;

import clojure.lang.IFn;
import clojure.lang.IteratorSeq;

import com.valkyrie.db.gen.DeleteRequest;
import com.valkyrie.db.gen.GetRequest;
import com.valkyrie.db.gen.GetResponse;
import com.valkyrie.db.gen.IFunction;
import com.valkyrie.db.gen.MapReduceRequest;
import com.valkyrie.db.gen.MapReduceResponse;
import com.valkyrie.db.gen.SetRequest;
import com.valkyrie.db.gen.ValkyrieDbService;
import com.valkyrie.db.mr.Collector;
import com.valkyrie.db.mr.ExecutionContext;
import com.valkyrie.db.mr.MemoryCollector;
import com.valkyrie.db.mr.MemoryExecutionContext;
import com.valkyrie.db.scripting.ClojureEngine;

public class ValkyrieDbServiceHandler implements ValkyrieDbService.Iface {
	private static final ByteBuffer EMPTY_BYTE_BUFFER = ByteBuffer.wrap(new byte[0]);

	private Log log = LogFactory.getLog(getClass());

	private Map<String, IFn> funcs = new ConcurrentHashMap<String, IFn>();

	private ClojureEngine scripting = new ClojureEngine();

	private PartitionedLocalStore localStorage;

	public ValkyrieDbServiceHandler(PartitionedLocalStore localStorage) {
		this.localStorage = localStorage;
	}

	public void init() {
		// read clojure defs
		loadScript("serializers.double", "/valkyrie/ser/ser-doubles.clj");
		loadScript("serializers.integer", "/valkyrie/ser/ser-integers.clj");
		loadScript("serializers.long", "/valkyrie/ser/ser-longs.clj");
	}

	private void loadScript(String name, String resource) {
		try {
			InputStream is = getClass().getResourceAsStream(resource);
			IFn fn = scripting.compile(is);
			is.close();
			funcs.put(name, fn);
		} catch(Exception e) {
			log.error("Exception", e);
			throw new RuntimeException(e);
		}
	}
	@Override
	public boolean exists(GetRequest request) throws TException {
		log.trace("exists()");
		KratiLocalStore ls = localStorage.getPartition(new Integer(request.getKey().getPartition()));
		if (ls == null)
			return false;
		else {
			byte[] bytes = ls.get(request.getKey().getBytes());
			if (log.isDebugEnabled()) {
				if (bytes != null)
					log.debug("Size " + bytes.length);
			}
			return (bytes != null);
		}
	}

	@Override
	public GetResponse getValue(GetRequest request) throws TException {
		log.trace("getValue()");
		KratiLocalStore ls = localStorage.getPartition(new Integer(request.getKey().getPartition()));
		if (ls == null)
			return new GetResponse(false, EMPTY_BYTE_BUFFER);
		else {
			byte[] bytes = ls.get(request.getKey().getBytes());
			if (log.isDebugEnabled()) {
				if (bytes != null)
					log.debug("Size " + bytes.length);
			}
			return (bytes == null)
					? new GetResponse(false, EMPTY_BYTE_BUFFER)
					: new GetResponse(true, ByteBuffer.wrap(bytes));
		}
	}

	@Override
	public void setValue(SetRequest request) throws TException {
		log.trace("setValue()");
		KratiLocalStore ls = localStorage.getPartition(new Integer(request.getKey().getPartition()));
		if (ls == null)
			return;
		try {
			ls.set(request.getKey().getBytes(), request.getData());
		} catch (Exception e) {
			log.trace("Exception calling set()", e);
			throw new TException(e);
		}
	}

	@Override
	public void deleteValue(DeleteRequest request) throws TException {
		log.trace("deleteValue()");
		KratiLocalStore ls = localStorage.getPartition(new Integer(request.getKey().getPartition()));
		if (ls == null)
			return;
		try {
			ls.delete(request.getKey().getBytes());
		} catch (Exception e) {
			log.trace("Exception calling delete()", e);
			throw new TException(e);
		}
	}

	@Override
	public String compile(IFunction fn) throws TException {
		log.trace("compile()");
		IFn func = scripting.compile(fn.getCode());
		String id = (fn.getName() == null)
				? UUID.randomUUID().toString()
				: fn.getName();
		funcs.put(id, func);
		return id;
	}

	@Override
	public void iterate(IFunction fn) throws TException {
		log.trace("iterate()");
		IFn func = (fn.getCode() == null)
				? funcs.get(fn.getName())
				: scripting.compile(fn.getCode());

		if (func == null)
			throw new TException("No matching function registered");

		Iterator<KratiLocalStore> partitionIterator = localStorage.iterator();
		while (partitionIterator.hasNext()) {
			KratiLocalStore ks = partitionIterator.next();
			ExecutionContext ctx = new MemoryExecutionContext(this, funcs, null);
			Iterator<Map.Entry<byte[], byte[]>> iter = ks.iterator();
			while (iter.hasNext()) {
				Map.Entry<byte[], byte[]> e = iter.next();
				func.invoke(ctx, e.getKey(), e.getValue());
			}
		}
	}

	@Override
	public MapReduceResponse mapreduce(MapReduceRequest request)
			throws TException {
		log.trace("mapreduce()");

		try {
			IFn map = getIFn(request.getMapper());
			IFn combine = getIFn(request.getCombiner());
			IFn reduce = getIFn(request.getReducer());
			IFn serializer = getIFn(request.getSerializer());
			if (serializer == null)
				serializer = defaultSerializer();

			Map<String, Long> counters = new HashMap<String, Long>();

			Iterator<KratiLocalStore> partitionIterator = localStorage.iterator();
			Map<Object, MemoryCollector<Object>> mapCollector = new HashMap<Object, MemoryCollector<Object>>();
			while (partitionIterator.hasNext()) {
				KratiLocalStore ks = partitionIterator.next();
				Iterator<Map.Entry<byte[], byte[]>> iter = ks.iterator();
				ExecutionContext ctx = new MemoryExecutionContext(this, funcs,
						mapCollector);
				while (iter.hasNext()) {
					Map.Entry<byte[], byte[]> e = iter.next();
					try {
						map.invoke(ctx, e.getKey(), e.getValue());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}

			Map<Object, MemoryCollector<Object>> combineCollector = new HashMap<Object, MemoryCollector<Object>>();
			for (Map.Entry<Object, MemoryCollector<Object>> e : mapCollector
					.entrySet()) {
				ExecutionContext ctx = new MemoryExecutionContext(this, funcs, combineCollector);
				IteratorSeq values = IteratorSeq.create(e.getValue().iterator());
				combine.invoke(ctx, e.getKey(), values);
			}

			Map<Object, MemoryCollector<Object>> reduceCollector = new HashMap<Object, MemoryCollector<Object>>();
			List<Object> values = new ArrayList<Object>();
			for (final Map.Entry<Object, MemoryCollector<Object>> e : combineCollector
					.entrySet()) {
				for (final Object obj : e.getValue())
					values.add(obj);
			}

			ExecutionContext ctx = new MemoryExecutionContext(this, funcs, reduceCollector);
			IteratorSeq seq = IteratorSeq.create(values.iterator());
			reduce.invoke(ctx, seq);
			MapReduceResponse response = new MapReduceResponse();
			byte[] bytes = new byte[] {};
			if (reduceCollector.entrySet().size() > 0) {
				Map.Entry<Object, Collector<Object>> e = (Entry<Object, Collector<Object>>) reduceCollector
						.entrySet().toArray()[0];
				Collector<Object> coll = e.getValue();
				Iterator<Object> iter = coll.iterator();
				if (iter.hasNext()) {
					bytes = (byte[]) serializer.invoke(iter.next());
					response.setExists(true);
				}
			}
			response.setCounters(counters);
			response.setData(bytes);
			return response;
		} catch (Exception e) {
			log.warn("Exception", e);
			throw new TException(e);
		}
	}

	private IFn getIFn(IFunction code) {
		if ((code == null) || ((code.getName() == null) && (code.getCode() == null)))
			return null;
		IFn fn = (code.getCode() == null)
				? ((funcs.get(code.getName()) != null)
						? funcs.get(code.getName())
						: null)
				: scripting.compile(code.getCode());
		return fn;
	}

	private IFn defaultSerializer() {
		IFn fn = (funcs.get("default.serializer") != null)
				? funcs.get("default.serializer")
				: scripting.compile("(fn [v] (.getBytes (.toString (str v))))");
		return fn;
	}
}
