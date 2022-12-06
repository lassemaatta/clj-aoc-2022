(ns lassemaatta.aoc-2022.day06-test
  (:require [clojure.test :refer [are deftest]]
            [lassemaatta.aoc-2022.day06 :as day06]
            [lassemaatta.aoc-2022.util :as util]))

(deftest day05-test
  (are [problem filename result] (= result (-> filename util/read-file problem))
    day06/first-problem  "day06/small-sample-0.txt" 7
    day06/first-problem  "day06/small-sample-1.txt" 5
    day06/first-problem  "day06/small-sample-2.txt" 6
    day06/first-problem  "day06/small-sample-3.txt" 10
    day06/first-problem  "day06/small-sample-4.txt" 11
    day06/first-problem  "day06/large-sample.txt"   1480
    day06/second-problem "day06/small-sample-0.txt" 19
    day06/second-problem "day06/small-sample-1.txt" 23
    day06/second-problem "day06/small-sample-2.txt" 23
    day06/second-problem "day06/small-sample-3.txt" 29
    day06/second-problem "day06/small-sample-4.txt" 26
    day06/second-problem "day06/large-sample.txt"   2746))
