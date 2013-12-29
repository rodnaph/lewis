(ns lewis.layout
  (:require [hiccup.page :refer :all]
            [lewis.session :as session]))

(defn standard [title & content]
  (let [home-url (if (session/exists) "/session" "/")]
    (html5
      [:head
        [:title (format "Lewis - %s" title)]
        (include-css "/assets/bootstrap-2.0/css/bootstrap.css"
                     "/assets/codemirror-2.34/codemirror.css"
                     "/assets/select2-3.2/select2.css"
                     "/assets/css/main.css")]
      [:body
        [:div.container
          [:div.navbar.navbar-inverse.navbar-fixed-top
            [:div.navbar-inner
              [:div.container
                [:a.brand {:href home-url} "Lewis"]
                (if (session/exists)
                  [:span
                    [:ul.nav
                      [:li.dropdown
                        [:a.dropdown-toggle {:data-toggle "dropdown" :href "/session/data"} "Data " [:b.caret]]
                        [:ul.dropdown-menu
                          [:li [:a {:href "/session/data"} "Query"]]
                          [:li [:a {:href "/session/data/insert"} "Insert"]]]]
                      [:li.dropdown
                        [:a.dropdown-toggle {:data-toggle "dropdown" :href "/session/schema"} "Schema " [:b.caret]]
                        [:ul.dropdown-menu
                          [:li [:a {:href "/session/schema"} "Browse"]]
                          [:li [:a {:href "/session/schema/update"} "Update"]]
                          [:li [:a {:href "/session/schema/transact"} "Transact"]]]]
                      [:li
                        [:a {:href "/session/disconnect" :class "disconnect"} "Disconnect"]]]
                     [:div.pull-right.uri
                       (session/uri)]])]]]
          content
          [:div.row
            [:div.span12.footer
              "Lewis"]]
          (include-js "/assets/js/3rdparty/jquery-1.8.2.js"
                      "/assets/select2-3.2/select2.min.js"
                      "/assets/bootstrap-2.0/js/bootstrap.js"
                      "/assets/codemirror-2.34/codemirror.js"
                      "/assets/js/3rdparty/underscore-1.4.2.js"
                      "/assets/js/3rdparty/codemirror/clojure.js"
                      "/assets/js/main.js")]])))

