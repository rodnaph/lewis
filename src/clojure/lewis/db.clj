
(ns lewis.db
  (:use [datomic.api :only [q db] :as d]))

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

