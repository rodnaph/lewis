
(ns lewis.pages
  (:use [datomic.api :only [q] :as d])
  (:require [lewis.layout :as layout]
            [lewis.form :as form]
            [lewis.db :as db]
            [lewis.history :as history]))

(defn entity2schema [e]
  [:tr
    [:td (str (:db/ident e))]
    [:td (:db/doc e)]
    [:td (:db/valueType e)]
    [:td (:db/cardinality e)]])

(defn- to-entity [id]
  (d/entity (db/database) id))

(defn- query-table [tx & [params]]
  (let [results (q tx (db/database) params)]
    [:h2 (format "Found %d result(s)" (count results))]
    [:table.table
      [:thead
        [:tr
          [:th "Identifier"]
          [:th "Docs"]
          [:th "ValueType"]
          [:th "Cardinality"]]]
      [:tbody
        (->> results
          (map (comp to-entity first))
          (map entity2schema))]]))

(defn- cols4results [res]
  (let [id (ffirst res)
        e (to-entity id)]
    (keys e)))

(defn- to-th [col]
  [:th col])

(defn to-td [e col]
  [:td (get e col)])

(defn- to-tr [cols e]
  [:tr
    (map (partial to-td e) cols)])

(defn- table-for [cols res]
  [:table.table
    [:thead
      [:tr
        (map to-th cols)]]
    [:tbody
      (->> res
           (map (comp to-entity first))
           (map (partial to-tr cols)))]])

(defn- results-table [tx]
  (let [res (q (read-string tx) (db/database))]
    (if (empty? res)
      "No Results..."
      (let [cols (cols4results res)]
        [:span
          [:h2 (format "Found %d result(s)" (count res))]
          (table-for cols res)]))))

(defn- to-recent-query [tx]
  (let [url (format "/session/query?tx=%s" tx)]
    [:li
      [:a {:href url} tx]]))

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
    [:div.row
      [:div.span8
        [:h1 "Home"]
        [:p "Use the links on the top navbar to move around the app."]
        [:h2 "Statistics"]
        [:p "@todo if makes sense"]]
      [:div.span4
        [:h2 "Recent Queries"]
        [:ul
          (map to-recent-query (history/queries))]]]))

(defn schema [req]
  (let [tx '[:find ?e 
             :where [?e :db/valueType]]]
    (layout/standard "Schema"
      [:div.row
        [:div.span12
          [:pre (pr-str tx)]]
        [:div.span12
          [:input]
          [:p "Filter by namespace?"]]
        [:div.span12
          (query-table tx)]])))

(defn query [{:keys [params]}]
  (let [tx (:tx params)]
    (history/add-query! tx)
    (layout/standard "Query"
      [:div.row
        [:div.span12
          (if (not tx)
              [:h1 "Perform a Query"])
          [:p "Enter your "
            [:a {:href "http://richhickey.github.com/clojure-contrib/doc/datalog.html"} "Datalog"]
            " query below, and click 'Execute'"]
          (form/query tx)]
        (if tx 
          [:div.span12
            (results-table tx)])])))

(defn transact-form [{:keys [session]}]
  (layout/standard "Home"
    [:div.row
      [:div.span12
        [:h1 "Performing a Transaction"]
        [:p "Enter the "
          [:a {:href "https://github.com/richhickey/edn"} "EDN"]
          " transaction you'd like to perform, and click 'Execute'"]
        (form/transact)]]))

(defn transact [{:keys [params]}]
  (let [tx (:tx params)]
    (layout/standard "Transaction Results"
      [:div.row
        [:div.span12
          (form/transact tx)]
        [:div.span12
          (try
            (let [schema-tx (read-string tx)
                  result-tx (d/transact (db/connection) schema-tx)]
              [:div.alert.alert-success 
                "Transaction executed successfully"])
            (catch Exception e
              [:div.alert.alert-error 
                "Error: " (.getMessage e)]))]])))

