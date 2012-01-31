(ns valkyrie.ser
  (:import com.valkyrie.db.util.Serializers)
  )

(fn [v] (Serializers/longToBytes v))