
(ns lewis.data.core
  (:use (hiccup form)
        [datomic.api :only [q] :as d])
  (:require (lewis [db :as db]
                   [results :as results]
                   [layout :as layout]
                   [form :as form]
                   [history :as history])))

(defn- param-vector [params param-name]
  (let [values (get params param-name)]
    (if (vector? values)
        (map read-string values)
        [(read-string values)])))

(defn- params2tx [params]
  (let [names (param-vector params "name")
        values (param-vector params "value")]
    (println names)
    (merge {:db/id #db/id[:db.part/user -1]}
      (apply merge
        (map hash-map names values)))))

;; Public
;; ------

(defn insert-form [req]
  (layout/standard "Inserting Data"
    [:div.row
      [:div.span8
        [:h1 "Inserting Data"]
        [:p "To insert some data, enter the names and values into the fields "
            "below and then click insert."]
        (form-to {:class "form-insert"}
                 [:post "/session/data/insert"]
          [:div.fields
            [:div.field
              [:div.name
                (text-field {:disabled "disabled"} "name" ":db/id")]
              [:div.value
                (text-field {:disabled "disabled"} "value" "#db/id[:db.part/user]")]]]
          [:div.field.submit
            (form/submit "Insert Value")])]]))

(defn insert [{:keys [form-params]}]
  (layout/standard "Inserting Data"
    (try
      (let [data-tx (params2tx form-params)
            result-tx (d/transact (db/connection) [data-tx])]
        (results/success "Data inserted!"))
      (catch Exception e
        (println (params2tx form-params))
        (results/error e)))))

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
            (results/render tx)])])))

