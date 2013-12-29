(ns lewis.db
  (:require [datomic.api :as d :refer [db]]))

(def ^:dynamic cnn (atom nil)) 

;; Public
;; ------

(defn database []
  (db @cnn))

(defn connection []
  @cnn)

(defn connect! [uri]
  (d/create-database uri)
  (reset! cnn (d/connect uri)))

