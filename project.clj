(defproject lewis "0.0.1"
  :description "Web interface for Datomic"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.1"]
                 [ring/ring-jetty-adapter "1.1.2"]
                 [ring/ring-devel "1.1.3"]
                 [hiccup "1.0.1"]
                 [cheshire "4.0.3"]
                 [com.datomic/datomic-free "0.8.3551"]]

  :main lewis.core

  :plugins [[lein-ring "0.8.8"]]

  :ring {:handler lewis.web/app
         :port 5555
         :auto-refresh true
         :nrepl {:start? true}})
