
(ns lewis.web
  (:use compojure.core
        (ring.middleware reload stacktrace))
  (:require (compojure [handler :as handler]
                       [route :as route])
            [ring.util.response :as response]
            (lewis [pages :as pages]
                   [session :as session]
                   [actions :as actions])
            [lewis.data.core :as data]))

(defroutes session-routes
  (GET "/" [] pages/home)
  (GET "/disconnect" [] actions/disconnect)

  (GET "/data" [] data/query)
  (GET "/data/insert" [] data/insert-form)
  (POST "/data/insert" [] data/insert)

  (GET "/schema" [] pages/schema)
  (GET "/schema/transact" [] pages/transact-form)
  (POST "/schema/transact" [] pages/transact))

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

