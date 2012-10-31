
(ns lewis.ws
  (:use lamina.core))

(def broadcast-ch (channel))

(defn message [msg]
  (enqueue broadcast-ch (pr-str msg)))

(defn app [ch req]
  (receive ch
    (fn [name]
      (siphon broadcast-ch ch))))

