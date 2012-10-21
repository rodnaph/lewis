
(ns lewis.form
  (:use (hiccup core form)))

(defn- submit [text]
  (submit-button
    {:class "btn btn-primary"}
    text))
                   
(defn- query-to [url]
  (form-to [:post url]
    (text-area "query")
    (submit "Execute")))


;; Public
;; ------

(defn connect []
  (form-to [:post "/connect"]
    [:div.input-append
      (text-field "url")
      (submit "Connect")]))

(defn query []
  (query-to "/session/query"))

(defn transact []
  (query-to "/session/transact"))

