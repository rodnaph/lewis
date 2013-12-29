(ns lewis.data.core
  (:require [hiccup.form :refer :all]
            [lewis.form :as form]
            [lewis.history :as history]
            [lewis.layout :as layout]
            [lewis.results :as results]))

;; Public
;; ------

(defn insert-form [req]
  (layout/standard "Inserting Data"
    [:div.row
      [:div.span8
        [:h1 "Inserting a Datom"]
        [:p "To insert a datom, enter the attributes and values below, the EDN will be generated for you."]
        [:div.data-insert
          [:div.fields
            [:div.field
              [:div.name
                (text-field {:disabled "disabled"} "name" ":db/id")]
              [:div.value
                (text-field {:disabled "disabled"} "value" "#db/id[:db.part/user]")]]]
          (form-to [:post "/session/schema/transact"]
            (text-area "tx")
            [:div.submit
              (form/submit "Insert Datom")])]]]))

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

