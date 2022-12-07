(ns lassemaatta.aoc-2022.day07-test
  (:require [clojure.test :refer [are deftest]]
            [lassemaatta.aoc-2022.day07 :as day07]
            [lassemaatta.aoc-2022.util :as util]))

(deftest day07-test
  (are [problem filename result] (= result (-> filename util/read-file problem))
    day07/first-problem  "day07/small-sample.txt" 95437
    day07/first-problem  "day07/large-sample.txt" 1778099
    day07/second-problem "day07/small-sample.txt" 24933642
    day07/second-problem "day07/large-sample.txt" 1623571))
