(ns lewis.actions
  (:require [lewis.db :as db]
            [ring.util.response :as response]))

;; Public
;; ------

(defn connect [{:keys [params]}]
  (let [url (:url params)]
    (db/connect! url)
    (assoc-in (response/redirect "/session")
              [:session :uri] url)))

(defn disconnect [req]
  (assoc-in (response/redirect "/")
            [:session :uri] nil))

