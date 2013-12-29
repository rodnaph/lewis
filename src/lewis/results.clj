(ns lewis.results
  (:require [datomic.api :as d :refer [q]]
            [lewis.db :as db]))

(declare id2entity)

(defn- entity2schema [e]
  [:tr
    [:td (str (:db/ident e))]
    [:td (:db/doc e)]
    [:td (:db/valueType e)]
    [:td (:db/cardinality e)]])

(defn- to-th [col]
  [:th col])

(defn- to-td [e col]
  [:td (get e col)])

(defn- to-tr [cols e]
  [:tr
    (map (partial to-td e) cols)])

(defn- cols4results [res]
  (let [id (ffirst res)
        e (id2entity id)]
    (keys e)))

(defn- table [res]
  (let [cols (cols4results res)]
    [:table.table
      [:thead
        [:tr
          (map to-th cols)]]
      [:tbody
        (->> res
             (map (comp id2entity first))
             (map (partial to-tr cols)))]]))

(defn- res-empty []
  "No results...")

(defn- res-show [res]
  [:span
    [:h2 (format "Found %d result(s)" (count res))]
    (table res)])

;; Public
;; ------

(defn id2entity [id]
  (d/entity (db/database) id))

(def result2entity (comp id2entity first))

(defn schema [results]
  [:h2 (format "Found %d result(s)" (count results))]
  [:table.table.schema-table
    [:thead
      [:tr
        [:th "Identifier"]
        [:th "Docs"]
        [:th "ValueType"]
        [:th "Cardinality"]]]
    [:tbody
      (->> results
        (map (comp id2entity first))
        (map entity2schema))]])

(defn success [msg]
  [:div.alert.alert-success msg])

(defn error [e]
  [:div.alert.alert-error 
    "Error: " (.getMessage e)])

(defn render [tx]
  (try
    (let [res (q (read-string tx) (db/database))]
      (if (empty? res)
        (res-empty)
        (res-show res)))
    (catch Exception e
      (error e))))

