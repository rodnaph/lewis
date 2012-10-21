
(ns lewis.pages
  (:use [datomic.api :only [q] :as d])
  (:require [lewis.layout :as layout]
            [lewis.form :as form]
            [lewis.db :as db]))

(defn- connection [session]
  [:div.row
    [:div.span12 (format "Connected to: %s" (:url session))]])

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
        [:p "Enter the Datomic details:"]
        (form/connect)]]))

(defn home [req]
  (layout/standard "Home"
    "HOME"))

(defn query-form [req]
  (layout/standard "Query"
    "Make a query"))

(defn query [{:keys [params]}]
  (do-query
    (read-string (:query params))))

(defn schema [req]
  (let [q1 '[:find ?e :where [?e :db/valueType]]
        q2 '[:find ?attr
             :where
             [?e :db/valueType]
             [?e :db/ident ?attr]
             [(datomic.Util/namespace ?attr) ?ns]]]
    (do-query q1)))

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

