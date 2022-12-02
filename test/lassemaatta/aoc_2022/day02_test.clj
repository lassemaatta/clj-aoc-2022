(ns lassemaatta.aoc-2022.day02-test
  (:require [clojure.test :refer [deftest is]]
            [lassemaatta.aoc-2022.day02 :as day02]
            [lassemaatta.aoc-2022.util :as util]))

(deftest first-small-test
  (is (= 15
         (-> "day02/small-sample.txt"
             util/read-file
             day02/first-problem))))

(deftest first-large-test
  (is (= 11666
         (-> "day02/large-sample.txt"
             util/read-file
             day02/first-problem))))

(deftest second-small-test
  (is (= 12
         (-> "day02/small-sample.txt"
             util/read-file
             day02/second-problem))))

(deftest second-large-test
  (is (= 12767
         (-> "day02/large-sample.txt"
             util/read-file
             day02/second-problem))))
