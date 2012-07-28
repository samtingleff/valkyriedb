package com.valkyrie.db.meta;

import java.util.Arrays;
import java.util.List;

import com.valkyrie.db.gen.ColumnSpec;
import com.valkyrie.db.gen.ColumnType;
import com.valkyrie.db.gen.TableSpec;

import junit.framework.TestCase;

public class TableMetadataServiceTestCase extends TestCase {

	private TableMetadataService service;

	public void setUp() throws Exception {
		service = new MemoryTableMetadataService();
	}

	public void testCreateTable() throws Exception {
		TableSpec ts = createTableSpec();
		service.create(ts);
		TableSpec ts2 = service.read(ts.getName());
		assertNotNull(ts2);
		assertEquals(ts, ts2);

		assertEquals(service.getColumnType(ts.getName(), "a_int"), ColumnType.IntegerType);
		assertEquals(service.getColumnType(ts.getName(), "b_long"), ColumnType.LongType);
		assertEquals(service.getColumnType(ts.getName(), "c_double"), ColumnType.DoubleType);
		assertEquals(service.getColumnType(ts.getName(), "d_string"), ColumnType.StringType);
		assertEquals(service.getColumnType(ts.getName(), "e_bytes"), ColumnType.BytesType);
	}

	private TableSpec createTableSpec() {
		List<ColumnSpec> columns = Arrays.asList(
				new ColumnSpec("a_int", ColumnType.IntegerType),
				new ColumnSpec("b_long", ColumnType.LongType),
				new ColumnSpec("c_double", ColumnType.DoubleType),
				new ColumnSpec("d_string", ColumnType.StringType),
				new ColumnSpec("e_bytes", ColumnType.BytesType)
		);
		TableSpec ts = new TableSpec("foo", columns);
		return ts;
	}
}
