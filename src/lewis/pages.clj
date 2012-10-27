
(ns lewis.pages
  (:use [datomic.api :only [q] :as d])
  (:require [lewis.layout :as layout]
            [lewis.form :as form]
            [lewis.results :as results]
            [lewis.db :as db]
            [lewis.history :as history]))

(defn- to-recent-query [tx]
  (let [url (format "/session/data?tx=%s" tx)]
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
          [:p [:i "Filter by namespace?"]]]
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

