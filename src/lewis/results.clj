
(ns lewis.results
  (:use [datomic.api :only [q] :as d])
  (:require [lewis.db :as db]))

(defn entity2schema [e]
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

(defn- to-entity [id]
  (d/entity (db/database) id))

(defn- cols4results [res]
  (let [id (ffirst res)
        e (to-entity id)]
    (keys e)))

(defn- table [res]
  (let [cols (cols4results res)]
    [:table.table
      [:thead
        [:tr
          (map to-th cols)]]
      [:tbody
        (->> res
             (map (comp to-entity first))
             (map (partial to-tr cols)))]]))

;; Public
;; ------

(defn schema [results]
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
        (map entity2schema))]])

(defn render [tx]
  (let [res (q (read-string tx) (db/database))]
    (if (empty? res)
      "No Results..."
      [:span
        [:h2 (format "Found %d result(s)" (count res))]
        (table res)])))


