
(ns lewis.pages
  (:use [datomic.api :only [q] :as d])
  (:require [lewis.layout :as layout]
            [lewis.form :as form]
            [lewis.db :as db]))

(defn- connection [session]
  [:div.row
    [:div.span12 (format "Connected to: %s" (:url session))]])

;; Public
;; ------

(defn index [req]
  (layout/standard "Connect"
    [:div.row
      [:div.span12
        [:p "Enter the Datomic details:"]
        (form/connect)]]))

(defn home [req]
  (layout/standard "Home"
    "HOME"))

(defn query-form [req]
  (layout/standard "Query"
    "Make a query"))

(defn query [req]
  "Do query...")

(defn schema [req]
  (let [query '[:find ?attr
                :in $
                :where [?e db/valueType]]
        results (q query (db/database))]
    (layout/standard "Schema"
      [:div.row
        [:div.span8
          [:h2 (format "Found %d result(s)" (count results))]]
        [:div.span4
          [:h2 "Query"
            [:pre (pr-str query)]]]])))

(defn transact-form [{:keys [session]}]
  (layout/standard "Home"
    (connection session)
    [:div.row
      [:div.span12
        (form/transact)]]))

(defn transact [{:keys [params]}]
  (try
    (let [schema-tx (read-string (:query params))]
      (d/transact (db/connection) schema-tx)
      "Transaction success! See console for details")
    (catch Exception e
      (println "Error: " (.getMessage e))
      (.printStackTrace e)
      "Invalid transaction...")))


