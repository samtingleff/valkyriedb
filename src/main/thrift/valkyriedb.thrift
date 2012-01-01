namespace java com.valkyrie.db.gen

const i32 DEFAULT_PORT = 9011

struct Key {
 1: binary bytes,
 2: i32 partition
}

struct GetRequest {
 1: Key key
}

struct SetRequest {
 1: Key key,
 2: binary data
}

struct DeleteRequest {
 1: Key key
}

struct GetResponse {
 1: bool exists,
 2: binary data
}

service ValkyrieDbService {
 bool exists(1: GetRequest request);

 GetResponse getValue(1: GetRequest request);

 void setValue(1: SetRequest request);

 void deleteValue(1: DeleteRequest request);
}
