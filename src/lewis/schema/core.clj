(ns lewis.schema.core
  (:require [cheshire.core :as json]
            [datomic.api :as d :refer [q]]
            [hiccup.form :refer :all]
            [lewis.db :as db]
            [lewis.form :as form]
            [lewis.layout :as layout]
            [lewis.results :as results]
            [ring.util.response :as response]))

(def schema-tx '[:find ?e 
                 :where [?e :db/valueType]])

(def valueTypes [ "string"
                  "boolean"
                  "long"
                  "bigint"
                  "float"
                  "double"
                  "bigdec"
                  "ref"
                  "instant"
                  "uuid"
                  "uri"
                  "bytes" ])

(def cardinalityValues [ "one" 
                         "many" ])

(def uniqueValues [ ""
                    "value"
                    "identity" ])

(defn- get-schema []
  (q schema-tx (db/database)))

(defn- matches-term? [term e]
  (let [ident (str (:db/ident e))]
    (> (.indexOf ident term) -1)))

;; Public
;; ------

(defn update [req]
  (layout/standard "Update Schema"
    [:h1 "Updating the Schema"]
    [:p "The form below allows you to alter the database schema.  As you "
        "use it you will see the EDN that will be used in the transaction."]
      [:div.form-horizontal.schema-update
        (form/row "Identifier"
                  (text-field "ident"))
        (form/row "Value Type" 
                  (drop-down "valueType" valueTypes))
        (form/row "Cardinality"
                  (drop-down "cardinality" cardinalityValues))
        (form/row "Unique"
                  (drop-down "unique" uniqueValues))
        (form/row "Documentation"
                  (text-field "doc"))
        (form/row "Fulltext?"
                  (check-box "fulltext"))
        (form/row "No history?"
                  (check-box "noHistory"))
        (form-to [:post "/session/schema/transact"]
          (text-area "tx")
          (form/submit "Transact"))]))

(defn show [req]
  (let [edit-url (format "/session/data?tx=%s" (pr-str schema-tx))]
    (layout/standard "Schema"
      [:div.row
        [:div.span12
          [:pre (pr-str schema-tx)]]
        [:div.span12.edit-query
          [:a {:href edit-url} "Edit query"]]
        [:div.span12.schema-filter]
        [:div.span12
          (results/schema (get-schema))]])))

(defn json [{:keys [params]}]
  (let [term (get params :term "")
        res (->> (get-schema)
                 (map results/result2entity)
                 (filter (partial matches-term? term)))]
    (response/content-type
      {:body (json/generate-string res)}
      "application/json")))

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

