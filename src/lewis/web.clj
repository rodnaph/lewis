
(ns lewis.web
  (:use compojure.core
        (ring.middleware reload stacktrace))
  (:require (compojure [handler :as handler]
                       [route :as route])
            [ring.util.response :as response]
            (lewis [pages :as pages]
                   [session :as session]
                   [actions :as actions])))

(defroutes session-routes
  (GET "/" [] pages/home)
  (GET "/disconnect" [] actions/disconnect)
  (GET "/schema" [] pages/schema)

  (GET "/query" [] pages/query-form)
  (POST "/query" [] pages/query)

  (GET "/transact" [] pages/transact-form)
  (POST "/transact" [] pages/transact))

(defroutes app-routes
  (GET "/" [] pages/index)
  (POST "/connect" [] actions/connect)
  (context "/session" []
    (session/check session-routes))
  (route/resources "/assets")
  (route/not-found "Not found..."))

(def app
  (-> #'app-routes
    (session/wrap)
    (wrap-reload)
    (wrap-stacktrace)
    (handler/site :session)))

