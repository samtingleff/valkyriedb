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
 ColumnType = 1,
 IntegerType = 2,
 LongType = 3,
 DoubleType = 4,
 StringType = 5,
 DateType = 6
}

struct Value {
 1: required ColumnType type,
 2: required string value
}

struct AggregateColumnSpec {
 1: required Aggregate aggregate,
 2: required Value value
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
 QueryResult select(1: Query query);
}

