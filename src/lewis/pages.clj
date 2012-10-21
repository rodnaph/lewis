
(ns lewis.pages
  (:use [datomic.api :only [q] :as d])
  (:require [lewis.layout :as layout]
            [lewis.form :as form]
            [lewis.db :as db]))

(defn entity2row [[id]]
  (let [e (d/entity (db/database) id)]
    [:tr
      [:td (:db/id e)]
      [:td (:db/doc e)]
      [:td (:db/valueType e)]
      [:td (:db/cardinality e)]]))

(defn- do-query [tx & [params]]
  (let [results (q tx (db/database) params)]
    (layout/standard "Schema"
      [:div.row
        [:div.span8
          [:h2 (format "Found %d result(s)" (count results))]
          [:table.table
            [:thead
              [:tr
                [:th "Identifier"]
                [:th "Docs"]
                [:th "ValueType"]
                [:th "Cardinality"]]]
            [:tbody
              (map entity2row results)]]
          [:h3 "Raw Results"]
          [:pre
           (pr-str results)]]
        [:div.span4
          [:h2 "Query"
            [:pre (pr-str tx)]]]])))

;; Public
;; ------

(defn index [req]
  (layout/standard "Connect"
    [:div.row
      [:div.span12
        [:h1 "Connect"]
        [:p "Enter the Datomic URI to connect."]
        (form/connect)]]))

(defn home [req]
  (layout/standard "Home"
    "HOME"))

(defn schema [req]
  (do-query '[:find ?e 
              :where [?e :db/valueType]]))

(defn query-form [req]
  (layout/standard "Query"
    [:div.row
      [:div.span12
        [:h1 "Perform a Query"]
        [:p "Enter your Datalog query below, and click 'Execute'"]
        (form/query)]]))

(defn query [{:keys [params]}]
  (do-query
    (read-string (:query params))))

(defn transact-form [{:keys [session]}]
  (layout/standard "Home"
    [:div.row
      [:div.span12
        [:h1 "Performing a Transaction"]
        [:p "Enter the EDN transaction you'd like to perform, and click 'Execute'"]
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

