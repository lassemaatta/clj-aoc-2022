(ns lassemaatta.aoc-2022.day05)

(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)

(defn- find-crates
  "Find entries like \"    [A] [B]\" and return a map of `{5 \"A\" 9 \"B\"}`"
  [s]
  (loop [m   (re-matcher #"\[(\p{Upper})\]" s)
         res {}]
    (if (.find m)
      (recur m (assoc res (inc (.start m)) (.group m 1)))
      (when (seq res)
        res))))

(defn- find-commands
  "Find entries like \"move 1 from 2 to 3\" and return a map of `{:amount 1 :from 1 :to 2}`"
  [s]
  (let [[_ c from to] (re-find #"move (\d+) from (\d+) to (\d+)" s)]
    (when (and c from to)
      {:amount (parse-long c)
       ;; Note we normalize to [0, N]
       :from   (dec ^long (parse-long from))
       :to     (dec ^long (parse-long to))})))

(defn- parse-line
  [line]
  (when-let [content (or (some-> (find-crates line)
                                 (assoc :type :crate))
                         (some-> (find-commands line)
                                 (assoc :type :command)))]
    {:type    (:type content)
     :content (dissoc content :type)}))

(defn- normalize
  [^long idx]
  (-> idx dec (/ 4)))

(defn- normalize-crate-indexes
  "Convert the crate character positions (1, 5, 9..) to logical (0, 1, 2..)"
  [data]
  (if (-> data :type (= :crate))
    (update data :content update-keys normalize)
    data))

(def parse-instructions-xform
  (comp
    (keep parse-line)
    (map normalize-crate-indexes)))

(defmulti update-state (fn [item _ _] (:type item)))

(defmethod update-state :crate
  [{:keys [content]} _ state]
  ;; Append each crate below the earlier (top most) crates
  (reduce-kv
    (fn [m idx letter]
      (update m idx str letter))
    state
    content))

(defmethod update-state :command
  [{{:keys [amount from to]} :content} mode state]
  (let [post-process       (if (= :all-at-once mode)
                             identity
                             (comp (partial apply str) reverse))
        containers-to-move (-> (get state from)
                               (subs 0 amount)
                               post-process)]
    (-> state
        ;; Remove top crate(s) from the source
        (update from subs amount)
        ;; Add new top crate(s) to the destination
        (update to (fn [old] (str containers-to-move old))))))

(defn apply-instructions
  "Returns a reducing function, which applies the sequence of instructions"
  [mode]
  (fn
    ([] {})
    ([state]
     ;; Combine top crate letters
     (->> (range (count state))
          (map #(get state %))
          (map first)
          (apply str)))
    ([result item]
     (update-state item mode result))))

(defn first-problem
  "Move containers one at a time"
  [input]
  (transduce parse-instructions-xform (apply-instructions :one-at-a-time) input))

(defn second-problem
  "Move multiple containers at once"
  [input]
  (transduce parse-instructions-xform (apply-instructions :all-at-once) input))
