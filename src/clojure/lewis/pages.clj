
(ns lewis.pages
  (:use [net.cgrand.enlive-html :only [deftemplate defsnippet content do-> set-attr]] 
        [datomic.api :only [q] :as d])
  (:require [lewis.layout :as layout]
            [lewis.form :as form]
            [lewis.results :as results]
            [lewis.db :as db]
            [lewis.history :as history]))

(defn- query-url-for [tx]
  (format "/session/data?tx=%s" tx))

(defsnippet connect-snippet "connect.html" [:body :> :*] [])

(defsnippet recent-query-snippet "home.html" [:.recent-queries :li]
  [recent-query]
  [:a] (do-> (content recent-query)
             (set-attr :href (query-url-for recent-query)))) 

(defsnippet home-snippet "home.html" [:body :> :*]
  [recent-queries]
  [:.recent-queries] (content
                       (map recent-query-snippet recent-queries)))

(deftemplate connect-template "index.html"
  []
  [:title] (content "Lewis - Connect")
  [:.loggedin] nil
  [:.content] (content (connect-snippet)))

(deftemplate home-template "index.html"
  [recent-queries]
  [:title] (content "Lewis - Home")
  [:.content] (content (home-snippet recent-queries)))

;; Public
;; ------

(defn index [req]
  (connect-template))

(defn home [req]
  (home-template
    (history/queries)))

