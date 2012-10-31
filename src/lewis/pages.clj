
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
        [:div.stats]]
      [:div.span4
        [:h2 "Recent Queries"]
        [:ul
          (map to-recent-query (history/queries))]]]))

