(ns lassemaatta.aoc-2022.day01
  (:import [java.util PriorityQueue]))

(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)

(defn- line->number
  [line]
  (when (seq line)
    (parse-long line)))

(def ^:private calories-per-elf-xform
  (comp
    (map line->number)
    (partition-by nil?)
    (remove #(= [nil] %))
    (map (fn [calories] (reduce + 0 calories)))))

(defn first-problem
  "Find the elf carrying the most calories and calculate the total amount of calories"
  [input]
  (transduce calories-per-elf-xform max 0 input))

(defn- max-n-key
  "Returns a stateful transducer, which returns n largest values of (f x)."
  [f ^long n]
  (fn [xf]
    (let [best (PriorityQueue. (inc n))]
      (fn
        ([] (xf))
        ([result]
         (let [v (if (.isEmpty best)
                   []
                   (let [v (vec (.toArray best))]
                     (reduce xf result v)))]
           (xf v)))
        ([result input]
         (let [^long value (f input)]
           (cond
             ;; Queue is still filling up?
             (< (.size best) n)
             (.add best value)
             ;; New value is better than the least element of the queue?
             (< (long (.peek best)) value)
             (doto best
               (.offer value)
               (.poll))))
         result)))))

(def ^:private top-three-elves-xform
  (comp
    calories-per-elf-xform
    (max-n-key identity 3)))

(defn second-problem
  "Find the three elves carrying the most calories and return the sum of all the calories."
  [input]
  (transduce top-three-elves-xform + 0 input))
