
(ns lewis.schema.core
  (:use [datomic.api :only [q] :as d])
  (:require [lewis.layout :as layout]
            [lewis.form :as form]
            [lewis.results :as results]
            [lewis.db :as db]))

(defn show [req]
  (let [tx '[:find ?e 
             :where [?e :db/valueType]]
        edit-url (format "/session/data?tx=%s" (pr-str tx))]
    (layout/standard "Schema"
      [:div.row
        [:div.span12
          [:pre (pr-str tx)]]
        [:div.span12.edit-query
          [:a {:href edit-url} "Edit query"]]
        [:div.span12
          (results/schema (q tx (db/database)))]])))

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
              (results/success "Transaction executed successfully"))
            (catch Exception e
              (results/error e)))]])))

