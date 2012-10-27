
(ns lewis.web
  (:use compojure.core
        (ring.middleware reload stacktrace))
  (:require (compojure [handler :as handler]
                       [route :as route])
            [ring.util.response :as response]
            (lewis [pages :as pages]
                   [session :as session]
                   [actions :as actions])
            [lewis.data.core :as data]
            [lewis.schema.core :as schema]))

(defroutes session-routes
  (GET "/" [] pages/home)
  (GET "/disconnect" [] actions/disconnect)

  (GET "/data" [] data/query)
  (GET "/data/insert" [] data/insert-form)
  (POST "/data/insert" [] data/insert)

  (GET "/schema" [] schema/show)
  (GET "/schema.json" [] schema/json)
  (GET "/schema/update" [] schema/update)
  (GET "/schema/transact" [] schema/transact-form)
  (POST "/schema/transact" [] schema/transact))

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

