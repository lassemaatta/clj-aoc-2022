(ns lassemaatta.aoc-2022.day01-test
  (:require [clojure.test :refer [are deftest]]
            [lassemaatta.aoc-2022.day01 :as day01]
            [lassemaatta.aoc-2022.util :as util]))

(deftest day01-test
  (are [problem filename result] (= result (-> filename util/read-file problem))
    day01/first-problem  "day01/small-sample.txt" 24000
    day01/first-problem  "day01/large-sample.txt" 70720
    day01/second-problem "day01/small-sample.txt" 45000
    day01/second-problem "day01/large-sample.txt" 207148))
