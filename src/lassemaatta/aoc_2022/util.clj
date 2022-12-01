(ns lassemaatta.aoc-2022.util
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn read-file
  "Given a path to a file, read the contents and return a seq of the lines"
  [filename]
  (some-> filename
          io/resource
          slurp
          str/split-lines))
