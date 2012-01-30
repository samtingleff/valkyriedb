package com.valkyrie.db.mr;

import clojure.lang.IFn;

public interface ExecutionContext {

	public void collect(Object key, Object value);

	public void registerIFn(String name, IFn fn);

	public IFn getIFn(String name);

	public void set(byte[] key, byte[] value) throws Exception;

	public byte[] get(byte[] key) throws Exception;

	public void delete(byte[] key) throws Exception;
}