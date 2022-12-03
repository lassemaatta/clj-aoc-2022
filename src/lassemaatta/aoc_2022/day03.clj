(ns lassemaatta.aoc-2022.day03
  (:require [clojure.set :as set]))

(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)

(defn- line->split-rucksacks
  [line]
  (when (seq line)
    (let [n (count line)]
      [(subs line 0 (/ n 2))
       (subs line (/ n 2))])))

(defn- find-common-in-split-sacks
  [[a b]]
  (first (set/intersection (set a) (set b))))

(def letter->priority
  (->> (concat (range (int \a) (inc (int \z)))
               (range (int \A) (inc (int \Z))))
       (map char)
       (map-indexed (fn [^long idx letter] [letter (inc idx)]))
       (into {})))

(def split-lines-to-two-sacks
  (comp
    (map line->split-rucksacks)
    (map find-common-in-split-sacks)
    (map letter->priority)))

(defn first-problem
  "Split each line to two sacks, find the common element and it's priority and sum all"
  [input]
  (transduce split-lines-to-two-sacks + 0 input))

(defn- find-common-in-sack-group
  [rucksacks]
  (first (apply set/intersection rucksacks)))

(def group-three-sacks
  (comp
    (map set)
    (partition-all 3)
    (map find-common-in-sack-group)
    (map letter->priority)))

(defn second-problem
  "Group three lines, find the common element and it's priority and sum all "
  [input]
  (transduce group-three-sacks + 0 input))
