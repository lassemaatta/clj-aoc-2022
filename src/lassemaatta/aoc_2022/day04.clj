(ns lassemaatta.aoc-2022.day04)

(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)

(defn- line->pair-ranges
  [line]
  (when (seq line)
    (let [[a b c d] (->> (re-find #"(\d+)-(\d+),(\d+)-(\d+)" line)
                         (drop 1)
                         (map parse-long))]
      [[a b] [c d]])))

(defn- range-contains?
  [[^long a ^long b] [^long c ^long d]]
  (and (<= a c) (<= d b)))

(defn- pairs-contain?
  [[pair-one pair-two]]
  (or (range-contains? pair-one pair-two)
      (range-contains? pair-two pair-one)))

(def containing-assignments-xform
  (comp
    (map line->pair-ranges)
    (filter pairs-contain?)))

(defn first-problem
  "Count the number of assignment pairs where one contains the other"
  [input]
  (count (sequence containing-assignments-xform input)))

(defn- range-overlaps?
  [[[^long a ^long b] [^long c ^long d]]]
  (or (<= a c b)
      (<= a d b)
      (<= c a d)))

(def overlapping-assignments-xform
  (comp
    (map line->pair-ranges)
    (filter range-overlaps?)))

(defn second-problem
  "Count the number of assignment pairs that overlap at least partially"
  [input]
  (count (sequence overlapping-assignments-xform input)))
