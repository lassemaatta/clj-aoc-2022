(ns lassemaatta.aoc-2022.day01-test
  (:require [clojure.test :refer [deftest is]]
            [lassemaatta.aoc-2022.day01 :as day01]
            [lassemaatta.aoc-2022.util :as util]))

(deftest first-small-test
  (is (= 24000
         (-> "day01/small-sample.txt"
             util/read-file
             day01/first-problem))))

(deftest first-large-test
  (is (= 70720
         (-> "day01/large-sample.txt"
             util/read-file
             day01/first-problem))))

(deftest second-small-test
  (is (= 45000
         (-> "day01/small-sample.txt"
             util/read-file
             day01/second-problem))))

(deftest second-large-test
  (is (= 207148
         (-> "day01/large-sample.txt"
             util/read-file
             day01/second-problem))))
