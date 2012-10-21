
(ns lewis.layout
  (:use (hiccup core page))
  (:require [lewis.session :as session]))

(defn standard [title & content]
  (let [home-url (if (session/exists) "/session" "/")]
    (html
      [:head
        [:title (format "Lewis - %s" title)]
        (include-css "/assets/bootstrap/css/bootstrap.css")
        (include-css "/assets/css/main.css")]
      [:body
        [:div.container
          [:div.navbar.navbar-inverse.navbar-fixed-top
            [:div.navbar-inner
              [:div.container
                [:a.brand {:href home-url} "Lewis"]
                (if (session/exists)
                  [:span
                    [:ul.nav
                      [:li
                        [:a {:href "/session/schema"} "Schema"]]
                      [:li
                        [:a {:href "/session/query"} "Query"]]
                      [:li
                        [:a {:href "/session/transact"} "Transact"]]
                      [:li
                        [:a {:href "/session/disconnect"} "Disconnect"]]]
                     [:div.pull-right.uri
                       (session/uri)]])]]]
          content
          [:div.row
            [:div.span12.footer
              "Lewis"]]]])))

