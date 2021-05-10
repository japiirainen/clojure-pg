(ns tutorial.core
  (:require [clojure.string :as str])
  (:gen-class))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (def str1 "This is my 2nd string")
  (str/includes? str1 "my"))
