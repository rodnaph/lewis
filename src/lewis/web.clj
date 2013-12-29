(ns lewis.web
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [lewis.actions :as actions]
            [lewis.data.core :as data]
            [lewis.pages :as pages]
            [lewis.schema.core :as schema]
            [lewis.session :as session]
            [ring.middleware.reload :refer :all]
            [ring.middleware.stacktrace :refer :all]))

(defroutes session-routes
  (GET "/" [] pages/home)
  (GET "/disconnect" [] actions/disconnect)

  (GET "/data" [] data/query)
  (GET "/data/insert" [] data/insert-form)

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

