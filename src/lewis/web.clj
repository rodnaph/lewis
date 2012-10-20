
(ns lewis.web
  (:use compojure.core
    (ring.middleware [reload :only [wrap-reload]]))
  (:require (compojure [handler :as handler]
                       [route :as route])))

(defroutes app-routes
  (GET "/" [] "Hello, World!")
  (route/resources "/assets")
  (route/not-found "Not found..."))

(def app
  (-> #'app-routes
    (wrap-reload)
    (handler/site :session)))

