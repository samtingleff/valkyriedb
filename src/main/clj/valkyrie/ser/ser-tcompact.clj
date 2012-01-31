(ns valkyrie.ser
  (:import
    [java.io ByteArrayOutputStream]
    [org.apache.thrift TBase]
    [org.apache.thrift.protocol TCompactProtocol]
    [org.apache.thrift.transport TIOStreamTransport])
  )

(fn [#^TBase v]
  (let [baos (ByteArrayOutputStream.)
        transport (TIOStreamTransport. baos)
        proto (TCompactProtocol. transport)]
    (.write v proto)
    (.toByteArray baos)
    ))