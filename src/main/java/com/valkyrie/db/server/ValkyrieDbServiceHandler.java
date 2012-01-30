package com.valkyrie.db.server;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;

import clojure.lang.IFn;

import com.valkyrie.db.gen.DeleteRequest;
import com.valkyrie.db.gen.GetRequest;
import com.valkyrie.db.gen.GetResponse;
import com.valkyrie.db.gen.IFunction;
import com.valkyrie.db.gen.SetRequest;
import com.valkyrie.db.gen.ValkyrieDbService;
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
		IFn func = null;
		if (fn.getCode() == null) {
			func = funcs.get(fn.getName());
		} else
			func = scripting.compile(fn.getCode());

		if (func == null)
			throw new TException("No matching function registered");

		Iterator<KratiLocalStore> partitionIterator = localStorage.iterator();
		while (partitionIterator.hasNext()) {
			KratiLocalStore ks = partitionIterator.next();
			Iterator<Map.Entry<byte[], byte[]>> iter = ks.iterator();
			while (iter.hasNext()) {
				Map.Entry<byte[], byte[]> e = iter.next();
				func.invoke(e.getKey(), e.getValue());
			}
		}
	}
}
