package com.valkyrie.db.mr;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MemoryCollector<T> implements Collector<T> {

	private List<T> list = new LinkedList<T>();

	public MemoryCollector() {
	}

	@Override
	public void collect(T obj) {
		list.add(obj);
	}

	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}

	@Override
	public String toString() {
		return list.toString();
	}

	public List<T> values() {
		return list;
	}
}
