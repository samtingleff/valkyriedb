package com.valkyrie.db.scripting;

public interface ScriptingEngine<F> {

	public F compile(String code);
}
