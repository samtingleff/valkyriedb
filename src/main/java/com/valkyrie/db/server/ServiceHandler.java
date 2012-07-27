package com.valkyrie.db.server;

import java.util.List;

import org.apache.thrift.TException;

import com.valkyrie.db.gen.AggregateColumnSpec;
import com.valkyrie.db.gen.Query;
import com.valkyrie.db.gen.QueryResult;
import com.valkyrie.db.gen.QueryService;

public class ServiceHandler implements QueryService.Iface {

	@Override
	public QueryResult select(Query query) throws TException {
		QueryResult result = null;
		List<AggregateColumnSpec> columns = query.getColumns();
		
		return result;
	}
}
