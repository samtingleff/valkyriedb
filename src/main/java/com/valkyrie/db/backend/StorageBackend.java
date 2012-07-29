package com.valkyrie.db.backend;

import java.io.IOException;

import com.valkyrie.db.gen.QueryService;

public interface StorageBackend extends QueryService.Iface {
	public void init() throws IOException;

	public void close();
}
