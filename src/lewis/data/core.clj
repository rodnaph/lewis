
(ns lewis.data.core
  (:require (lewis [results :as results]
                   [layout :as layout]
                   [form :as form]
                   [history :as history])))

;; Public
;; ------

(defn insert-form [req]
  "Insert")

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

