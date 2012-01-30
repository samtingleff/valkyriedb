from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol

from valkyriedb import *
from valkyriedb.ttypes import *
from valkyriedb.constants import DEFAULT_PORT

class ValkyrieDbClient(object):
    def __init__(self, host='localhost', port=DEFAULT_PORT):
        self.host = host
        self.port = port

    def connect(self):
        self.socket = TSocket.TSocket(self.host, self.port)
        self.transport = TTransport.TFramedTransport(self.socket)
        self.protocol = TBinaryProtocol.TBinaryProtocol(self.transport)
        self.client = ValkyrieDbService.Client(self.protocol)
        self.transport.open()

    def disconnect(self):
        self.transport.close()

    def exists(self, key):
        return self.client.exists(GetRequest(Key(bytes=key)))

    def get(self, key):
        gr = self.client.getValue(GetRequest(Key(bytes=key)))
        if gr.exists: return gr.data
        else: return None

    def set(self, key, value):
        return self.client.setValue(SetRequest(key=Key(bytes=key), data=value))

    def delete(self, key):
        return self.client.deleteValue(DeleteRequest(key=Key(bytes=key)))

    def compile(self, name=None, code=None):
        return self.client.compile(IFunction(name=name, code=code))

    def iterate(self, name=None, code=None):
        return self.client.iterate(IFunction(name=name, code=code))

    def mapreduce(self, mapper, combiner, reducer, serializer):
        return self.client.mapreduce(MapReduceRequest(
                IFunction(name=mapper),
                IFunction(name=combiner),
                IFunction(name=reducer),
                IFunction(name=serializer)))

def main():
    client = ValkyrieDbClient(host='localhost', port=9012)
    client.connect()
    print client.exists("foo")
    print client.get("123")
    print client.get("123")
    print client.set("123", "987")
    print client.get("123")
    print client.delete("123")
    print client.get("bar")
    print client.set("123", "987")
    print client.set("456", "13")
    id = client.compile(code="(fn [context, k, v] (str (String. k)))")
    print client.iterate(name=id)

    mapper = client.compile(code="(fn [context k v] (.collect context 1 (Integer/parseInt (String. v))))")
    print mapper
    combiner = client.compile(code="(fn [context k values] (.collect context k (reduce + values)))")
    print combiner
    reducer = client.compile(code="(fn [context values] (.collect context 1 (reduce + values)))")
    print reducer
    serializer = client.compile(code="(fn [v] (.getBytes (Long/toString v)))")
    print serializer
    print client.mapreduce(mapper, combiner, reducer, serializer)

    client.disconnect()

main()
