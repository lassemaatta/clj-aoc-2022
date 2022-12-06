(ns lassemaatta.aoc-2022.day06
  (:import [clojure.lang PersistentQueue]))

(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)

(defn- push-queue
  "Push an item to the `queue.` Pops a value if the given length `n` is exceeded."
  [queue ^long n val]
  (let [size (count queue)]
    (cond-> queue
      (<= n size) pop
      true        (conj val))))

(defn- sliding-window
  "Returns a stateful transducer, which applies a sliding window of size `n` over the input.
  Starting index of the window is stored in the metadata under `:index`."
  [^long n]
  (fn [xf]
    (let [*index  (volatile! 0)
          *window (volatile! PersistentQueue/EMPTY)]
      (fn
        ([] (xf))
        ([result]
         (xf result))
        ([result input]
         (let [index  (vswap! *index (fn [^long idx] (inc idx)))
               window (vswap! *window push-queue n input)]
           (if (= n (count window))
             (xf result (with-meta (vec window) {:index index}))
             result)))))))

(defn unique?
  [window]
  (= (count window)
     (count (set window))))

(defn find-first-unique-pattern
  [n]
  (comp
    (sliding-window n)
    (filter unique?)
    (take 1)
    (map (comp :index meta))))

(defn first-value
  ([] 0)
  ([result] result)
  ([_ value] value))

(defn first-problem
  "Find the first unique substring of length 4"
  [input]
  (-> (transduce
        (find-first-unique-pattern 4)
        first-value
        (first input))))

(defn second-problem
  "Find the first unique substring of length 14"
  [input]
  (-> (transduce
        (find-first-unique-pattern 14)
        first-value
        (first input))))
