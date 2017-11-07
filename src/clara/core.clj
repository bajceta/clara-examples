(ns clara.core
  (:gen-class)
  (:require clara.example) 
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
   (clara.example/run-rules)
  )
