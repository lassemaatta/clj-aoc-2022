(ns lassemaatta.aoc-2022.day04-test
  (:require [clojure.test :refer [are deftest]]
            [lassemaatta.aoc-2022.day04 :as day04]
            [lassemaatta.aoc-2022.util :as util]))

(deftest day04-test
  (are [problem filename result] (= result (-> filename util/read-file problem))
    day04/first-problem  "day04/small-sample.txt" 2
    day04/first-problem  "day04/large-sample.txt" 464
    day04/second-problem "day04/small-sample.txt" 4
    day04/second-problem "day04/large-sample.txt" 770))
