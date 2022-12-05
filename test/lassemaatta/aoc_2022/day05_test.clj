(ns lassemaatta.aoc-2022.day05-test
  (:require [clojure.test :refer [are deftest]]
            [lassemaatta.aoc-2022.day05 :as day05]
            [lassemaatta.aoc-2022.util :as util]))

(deftest day05-test
  (are [problem filename result] (= result (-> filename util/read-file problem))
    day05/first-problem  "day05/small-sample.txt" "CMZ"
    day05/first-problem  "day05/large-sample.txt" "ZRLJGSCTR"
    day05/second-problem "day05/small-sample.txt" "MCD"
    day05/second-problem "day05/large-sample.txt" "PRTTGRFPB"))
