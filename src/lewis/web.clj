
(ns lewis.web
  (:use compojure.core
        (ring.middleware reload stacktrace))
  (:require (compojure [handler :as handler]
                       [route :as route])
            (lewis [pages :as pages]
                   [actions :as actions])))

(defroutes app-routes

  (GET "/" [] pages/index)
  (POST "/connect" [] actions/connect)

  (context "/session" []

    (GET "/" [] pages/home)
    (GET "/schema" [] pages/schema)

    (GET "/query" [] pages/query-form)
    (POST "/query" [] pages/query)

    (GET "/transact" [] pages/transact-form)
    (POST "/transact" [] pages/transact))

  (route/resources "/assets")
  (route/not-found "Not found..."))

(def app
  (-> #'app-routes
    (wrap-reload)
    (wrap-stacktrace)
    (handler/site :session)))

