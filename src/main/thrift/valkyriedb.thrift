namespace java com.valkyrie.db.gen

const i32 DEFAULT_PORT = 9011

struct Key {
 1: required binary bytes,
 2: optional i32 partition
}

struct GetRequest {
 1: required Key key
}

struct SetRequest {
 1: required Key key,
 2: required binary data
}

struct DeleteRequest {
 1: required Key key
}

struct GetResponse {
 1: required bool exists,
 2: required binary data
}

struct IFunction {
 1: optional string name,
 2: optional string code
}

struct MapReduceRequest {
 1: optional IFunction mapper,
 2: optional IFunction combiner,
 3: optional IFunction reducer,
 4: optional IFunction serializer
}

struct MapReduceResponse {
 1: required bool exists,
 2: optional binary data,
 3: optional map<string, i64> counters
}

service ValkyrieDbService {
 bool exists(1: GetRequest request);

 GetResponse getValue(1: GetRequest request);

 void setValue(1: SetRequest request);

 void deleteValue(1: DeleteRequest request);

 string compile(1: IFunction fn);

 void iterate(1: IFunction fn);

 MapReduceResponse mapreduce(1: MapReduceRequest fns);
}
