
(ns lewis.core
  (:require [ring.adapter.jetty :as jetty]
            [lewis.web :as web]))

(defn -main [& args]
  (jetty/run-jetty web/app {:port 5555}))

