
(ns lewis.core
  (:use lamina.core
        aleph.http)
  (:require [ring.adapter.jetty :as jetty]
            [lewis.web :as web]
            [lewis.ws :as ws]))

(defn -main [& args]
  (start-http-server
    ws/app
    {:port 5556 :websocket true})
  (jetty/run-jetty 
    web/app 
    {:port 5555}))

