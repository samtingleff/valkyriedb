package com.valkyrie.db.scripting;

import java.io.StringReader;

import clojure.lang.Compiler;
import clojure.lang.IFn;

public class ClojureEngine implements ScriptingEngine<IFn> {

	@Override
	public IFn compile(String code) {
		return (IFn) Compiler.load(new StringReader(code));
	}
}
