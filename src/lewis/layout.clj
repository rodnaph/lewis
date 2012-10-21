
(ns lewis.layout
  (:use (hiccup core page)))

(defn standard [title & content]
  (html
    [:head
      [:title (format "Lewis - %s" title)]
      (include-css "/assets/bootstrap/css/bootstrap.css")]
    [:body
      [:div.container
        [:div.row
          [:div.span12
            [:h1 "Lewis"]
            [:ul
              [:li
                [:a {:href "/"} "Connect"]]
              [:li
                [:a {:href "/session"} "Home"]]
              [:li
                [:a {:href "/session/schema"} "Schema"]]
              [:li
                [:a {:href "/session/query"} "Query"]]
              [:li
                [:a {:href "/session/transact"} "Transact"]]]]]
        content
        [:div.row
          [:div.span12.footer
            "Lewis"]]]]))

