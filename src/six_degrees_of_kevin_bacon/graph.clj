(ns six-degrees-of-kevin-bacon.graph
  (:require [clojure.set :as set]
            [clojure.core.reducers :as r]
            [clojure.data.json :as json]
            [clojure.java.io :as io]
            [clojure.set :as set]
            ;[six-degrees-of-kevin-bacon.util :as u]
            ))

(defrecord Graph
  [neighbors data])

(def empty-graph (Graph. {} {}))

(defn update-conj [s x]
  (conj (if (nil? s) #{} s) x))

(defn add
  ([g x y] (add g x y false))
  ([g x y bidirectional?]
      ((if bidirectional? #(add % y x false) identity)
        (update-in g [:neighbors x] #(update-conj % y)))))

(defn delete
  ([g x y] (delete g x y false))
  ([g x y bidirectional?]
   ((if bidirectional? #(delete % y x false) identity)
      (update-in g [:neighbors x] #(disj % y)))))

(defn merge-graphs [a b]
  (Graph. (merge-with set/union (:neighbors a) (:neighbors b))
          (merge (:data a) (:data b))))

(defn get-value
  ([g x] ((:data g) x))
  ([g x k] ((get-value g x) k)))

(defn set-value
  ([g x v] (assoc-in g [:data x] v))
  ([g x k v] (set-value g x (assoc (get-value g x) k v))))

(defn update-value
  ([g x f] (set-value g x (f (get-value g x))))
  ([g x k f] (set-value g x k (f (get-value g x k)))))

(defn get-vertices [graph]
  (reduce set/union (set (keys (:neighbors graph)))
          (vals (:neighbors graph))))

(defn get-edges [graph]
  (let [pair-edges (fn [[v neighbors]]
                     (map #(vector v %) neighbors))]
    (mapcat pair-edges (:neighbors graph))))

(defn bf-seq
  ([get-neighbors a]
   (bf-seq
     get-neighbors
     (conj clojure.lang.PersistentQueue/EMPTY [a])
     #{a}))
  ([get-neighbors q seen]
   (lazy-seq
     (when-not (empty? q)
       (let [current (first q)
             nbors (remove seen (get-neighbors (last current)))]
         (cons current
               (bf-seq get-neighbors
                       (into (pop q)
                             (map #(conj current %) nbors))
                       (into seen nbors))))))))

(defn breadth-first [graph a]
  (bf-seq (:neighbors graph) a))

(defn bfs [graph a b]
  (first (filter #(= (last %) b) (breadth-first graph a))))
