(ns lewis.core
  (:require [lewis.web :as web]
            [ring.adapter.jetty :as jetty]))

(defn -main [& args]
  (jetty/run-jetty 
    web/app 
    {:port 5555}))

