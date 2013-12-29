(ns lewis.form
  (:require [hiccup.form :refer :all]))

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

(defn row [label control]
  [:div.control-group
    [:label.control-label label]
    [:div.controls control]])

 (defn connect []
  (form-to [:post "/connect"]
    [:div.input-append
      (text-field "url")
      (submit "Connect")]))

(defn query [& [tx]]
  (query-to [:get "/session/data"] tx))

(defn transact [& [tx]]
  (query-to [:post "/session/schema/transact"] tx))

