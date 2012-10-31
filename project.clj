
(defproject lewis "0.0.1"
  :description "Web interface for Datomic"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.1"]
                 [ring/ring-jetty-adapter "1.1.2"]
                 [ring/ring-devel "1.1.3"]
                 [hiccup "1.0.1"]
                 [cheshire "4.0.3"]
                 [aleph "0.3.0-beta6"]
                 [com.datomic/datomic-free "0.8.3551"]]
  :main lewis.core)

