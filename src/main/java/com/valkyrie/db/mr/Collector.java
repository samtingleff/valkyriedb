package com.valkyrie.db.mr;

public interface Collector<T> extends Iterable<T> {

	public void collect(T obj);
}
