
(ns lewis.form
  (:use (hiccup core form)))

(defn- control [text & content]
  [:div.control-group
    (if (> 0 (count text))
      [:label {:class "control-label"} (format "%s:" text)])
    [:div.controls
      content]])

(defn- form [to submit & content]
  (form-to {:class "form-horizontal"} to
    content
    (control "" (submit-button submit))))

;; Public
;; ------

(defn connect []
  (form [:post "/connect"] "Connect"
    (control "URL" (text-field "url"))))

(defn transact []
  (form [:post "/session/transact"] "Execute"
    (control "Command" (text-area "query"))))

