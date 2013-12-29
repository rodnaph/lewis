(ns lewis.session
  (:require [ring.util.response :as response]))

(def ^{:dynamic true :private true} *uri*)

;; Public
;; ------

(defn uri []
  *uri*)

(defn exists []
  (not (nil? *uri*)))

(defn wrap [handler]
  (fn [req]
    (binding [*uri* (get-in req [:session :uri])]
      (handler req))))

(defn check [handler]
  (fn [req]
    (if (exists)
        (handler req)
        (response/redirect "/"))))

