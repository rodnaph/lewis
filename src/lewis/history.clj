(ns lewis.history)

(def qs (ref []))

;; Public
;; ------

(defn add-query! [q]
  (dosync
    (alter qs conj q)))

(defn queries
  ([] (queries 10))
  ([limit]
    (take limit @qs)))

