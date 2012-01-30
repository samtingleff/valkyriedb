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

def main():
    client = ValkyrieDbClient(host='localhost', port=9012)
    client.connect()
    print client.exists("foo")
    print client.get("foo")
    print client.get("bar")
    print client.set("bar", "987")
    print client.get("bar")
    client.disconnect()

main()
