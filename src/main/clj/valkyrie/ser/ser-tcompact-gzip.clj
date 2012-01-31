(ns valkyrie.ser
  (:import
    [java.io ByteArrayOutputStream]
    [java.util.zip GZIPOutputStream]
    [org.apache.thrift TBase]
    [org.apache.thrift.protocol TCompactProtocol]
    [org.apache.thrift.transport TIOStreamTransport])
  )

(fn [#^TBase v]
  (let [baos (ByteArrayOutputStream.)
        gzip (GZIPOutputStream. baos)
        transport (TIOStreamTransport. gzip)
        proto (TCompactProtocol. transport)]
    (.write v proto)
    (.close gzip)
    (.toByteArray baos)
    ))