package com.valkyrie.db.scripting;

import java.io.InputStream;
import java.io.Reader;

public interface ScriptingEngine<F> {

	public F compile(InputStream code);

	public F compile(Reader code);

	public F compile(String code);
}
