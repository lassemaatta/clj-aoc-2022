(ns lassemaatta.aoc-2022.day03-test
  (:require [clojure.test :refer [are deftest]]
            [lassemaatta.aoc-2022.day03 :as day03]
            [lassemaatta.aoc-2022.util :as util]))

(deftest day03-test
  (are [problem filename result] (= result (-> filename util/read-file problem))
    day03/first-problem  "day03/small-sample.txt" 157
    day03/first-problem  "day03/large-sample.txt" 7766
    day03/second-problem "day03/small-sample.txt" 70
    day03/second-problem "day03/large-sample.txt" 2415))
