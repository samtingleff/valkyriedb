package com.valkyrie.db.scripting;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import clojure.lang.Compiler;
import clojure.lang.IFn;

public class ClojureEngine implements ScriptingEngine<IFn> {

	@Override
	public IFn compile(InputStream code) {
		return (IFn) Compiler.load(new InputStreamReader(code));
	}

	@Override
	public IFn compile(Reader code) {
		return (IFn) Compiler.load(code);
	}

	@Override
	public IFn compile(String code) {
		return compile(new StringReader(code));
	}
}
