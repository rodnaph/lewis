
(ns lewis.actions
  (:require [lewis.db :as db]
            [ring.util.response :as response]))

(defn connect [{:keys [params]}]
  (let [url (:url params)]
    (db/connect! url)
    (merge (response/redirect "/session")
           {:session {:url url}})))

