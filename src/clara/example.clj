(ns clara.example
  (:require [clara.rules :refer :all]))

(defrecord SupportRequest [client level])

(defrecord ClientRepresentative [name client])

(defrecord Person [name age mama])

(defrecord OkrugliRodjendan [name age])

(defrule is-important
  "Find important support requests."
  [SupportRequest (= :high level)]
  =>
  (println "High support requested!"))

(defrule notify-client-rep
  "Find the client representative and request support."
  [SupportRequest (= ?client client)]
  [ClientRepresentative (= ?client client) (= ?name name)]
  =>
  (println "Notify" ?name "that"
           ?client "has a new support request!"))

(defrule veliki-rodjendan
  [Person (= ?age age) (= ?name name)]
  [:test (= 0 (rem ?age 10))]
  =>
  (insert! (->OkrugliRodjendan ?name ?age)))

(defrule mama-slavi-okrugli
  [Person (= ?mama mama) (= ?name name)]
  [OkrugliRodjendan (= ?mama name) (= ?age age)]
  =>
  (println (str "Mama od " ?name " slavi " ?age " rodjendan "))
  )

(defrule mama-bonus
  [Person (= ?mama mama) (= ?name name) ]
  [:test (= "M" (subs ?mama 0 1))]
  =>
  (println  (str ?name " dobija bonus ")))

(defrule is-older-than
  [Person  (= ?name1 name)  (= ?age1 age)]
  [Person  (= ?name2 name)  (= ?age2 age)]
  [:test  (> ?age1 ?age2)]
  =>
  (println  (str ?name1 " is older than " ?name2)))


(defn run-rules
  []  ((->  (mk-session 'clara.example)
           (insert  (->ClientRepresentative "Alice" "Acme")
                   (->SupportRequest "Acme" :high)
                   (->Person "Iva" 5 "Bilja")
                   (->Person "Masa" 7 "Bilja")
                   (->Person "Vlada" 37 "Ljilja")
                   (->Person "Bilja" 40 "Mira")
                   )
           (fire-rules))))
