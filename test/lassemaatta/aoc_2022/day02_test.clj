(ns lassemaatta.aoc-2022.day02-test
  (:require [clojure.test :refer [are deftest]]
            [lassemaatta.aoc-2022.day02 :as day02]
            [lassemaatta.aoc-2022.util :as util]))

(deftest day02-test
  (are [problem filename result] (= result (-> filename util/read-file problem))
    day02/first-problem  "day02/small-sample.txt" 15
    day02/first-problem  "day02/large-sample.txt" 11666
    day02/second-problem "day02/small-sample.txt" 12
    day02/second-problem "day02/large-sample.txt" 12767))
