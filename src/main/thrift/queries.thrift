namespace java com.valkyrie.db.gen

enum Aggregate {
 None = 1,
 Count = 2,
 Sum = 3,
 Distinct = 4
}

enum Operator {
 Equals = 1,
 GreaterThan = 2,
 GreaterThanOrEquals = 3,
 LessThan = 4,
 LessThanOrEquals = 5
}

enum ColumnType {
 IntegerType = 1,
 LongType = 2,
 DoubleType = 3,
 StringType = 4,
 BytesType = 5
}

union ColumnValue {
 1: i32 v_int;
 2: i64 v_long;
 3: double v_double;
 4: string v_string;
 5: binary v_bytes;
}

struct ColumnSpec {
 1: required string column,
 2: required ColumnType type
}

struct TableSpec {
 1: required string name,
 2: required list<ColumnSpec> columns
}

struct ColumnValueList {
 1: list<ColumnValue> values
}

struct AggregateColumnSpec {
 1: required ColumnSpec column,
 2: required Aggregate aggregate
}

struct Value {
 1: required ColumnType type,
 2: required ColumnValue value
}

struct Condition {
 1: required string column,
 2: required Operator operator,
 3: required Value value
}

struct Grouping {
 1: required string column
}

struct Query {
 1: required list<AggregateColumnSpec> columns,
 2: required list<Condition> conditions,
 3: required list<Grouping> groupings
}

struct Row {
 1: required list<Value> values
}

struct QueryResult {
 1: required list<Row> rows
}

service QueryService {
 void createTable(1: TableSpec table);
 void dropTable(1: string table);
 void insert(1: string table, 2: list<string> columns, 3: list<ColumnValueList> values);
 QueryResult select(1: Query query);
}

