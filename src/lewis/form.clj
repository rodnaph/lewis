
(ns lewis.form
  (:use (hiccup core form)))

(declare submit)
                  
(defn- query-to [to & [tx]]
  (form-to to
    (text-area "tx" tx)
    (submit "Execute")))


;; Public
;; ------

(defn submit [text]
  (submit-button
    {:class "btn btn-primary"}
    text))

 (defn connect []
  (form-to [:post "/connect"]
    [:div.input-append
      (text-field "url")
      (submit "Connect")]))

(defn query [& [tx]]
  (query-to [:get "/session/data"] tx))

(defn transact [& [tx]]
  (query-to [:post "/session/schema/transact"] tx))

