(ns lassemaatta.aoc-2022.day07
  (:require [clojure.set :as set]))

(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)

;; Input parsing

(defn- command-cd
  [line]
  (or (when-let [[_ dirname] (re-find #"\$ cd ([\p{Lower}]+)" line)]
        [:into dirname])
      (when (= "$ cd /" line)
        [:goto-root])
      (when (= "$ cd .." line)
        [:up])))

(defn- ls-output
  [line]
  (or (when-let [[_ num-bytes filename] (re-find #"(\d+) ([\w\.]+)" line)]
        [:file filename (parse-long num-bytes)])
      (when-let [[_ dirname] (re-find #"dir (\w+)" line)]
        [:dir dirname])))

(defn- parse-line
  [line]
  (or (command-cd line)
      (ls-output line)))

;; Transducer shenanigans

(defmulti on-entry (fn [_state entry] (first entry)))

(defn- parent-paths
  "Given path `[a b c]`, return `([a b c] [a b] [a])`"
  [path]
  (take (count path) (iterate pop path)))

(defn- calculate-total-size
  "Once a directory is processed, traverse up and check if the parent size is now known."
  [{:keys [children dirs] :as state} path]
  (->> path
       parent-paths
       (reduce
         (fn [{:keys [dirs-total] :as state} path]
           (let [my-children (get children path)]
             ;; Check if we know the total size of all my sub-directories
             (if (every? dirs-total my-children)
               (let [^long local-size (or (get dirs path) 0)
                     ^long child-size (->> (select-keys dirs-total my-children)
                                           vals
                                           (reduce + 0))
                     total-size       (+ local-size child-size)]
                 (-> state
                     (update :dirs-total assoc path total-size)
                     ;; This directory is now done, so let's create a proper entry for it
                     (assoc-in [:dirs-details path] {:dirname    (last path)
                                                     :path       (pop path)
                                                     :local-size local-size
                                                     :total-size total-size})))
               state)))
         state)))

(defn- finish-collecting
  "Calculate the size of the local files in a directory"
  [{:keys [collecting path] :as state}]
  (if (seq collecting)
    (let [local-size (reduce + 0 collecting)]
      (-> state
          (assoc :collecting [])
          (update :dirs conj [path local-size])
          (calculate-total-size path)))
    state))

(defmethod on-entry :into
  [state [_ dirname]]
  (-> state
      finish-collecting
      (update :path conj dirname)))

(defmethod on-entry :up
  [state _]
  (-> state
      finish-collecting
      (update :path pop)))

(defmethod on-entry :goto-root
  [state _]
  (-> state
      finish-collecting
      (assoc :path ["/"])))

(defmethod on-entry :dir
  [{:keys [path] :as state} [_ dirname]]
  (let [dirpath (conj path dirname)]
    (-> state
        ;; Mark ourselves as the child of our parent path
        (update-in [:children path] (fnil conj #{}) dirpath))))

(defmethod on-entry :file
  [{:keys [path] :as state} [_ filename num-bytes]]
  (let [file-path (conj path filename)
        entry     {:filename filename
                   :path     file-path
                   :size     num-bytes}]
    (-> state
        (update :collecting conj num-bytes)
        (update :files conj [file-path entry]))))

(defn- submit-changes-for-key
  "Find new entries by comparing the states and submit them downstream"
  [result xf old-state new-state k]
  (let [old-items     (-> old-state k keys set)
        current-items (-> new-state k keys set)
        new-items     (set/difference current-items old-items)]
    (reduce (fn [r item] (xf r (get-in new-state [k item])))
            result
            new-items)))

(defn- submit-changes
  "Check if we've discovered any new files or directories"
  [result xf old-state new-state]
  (-> result
      (submit-changes-for-key xf old-state new-state :files)
      (submit-changes-for-key xf old-state new-state :dirs-details)))

(defn- filesystem-analyzer
  "Returns a stateful transducer, which produces values describing files and directories"
  [xf]
  (let [*state (volatile! {:path         [] ; Current path
                           :collecting   [] ; File sizes of discovered files in current path
                           :children     {} ; parent path -> set of children
                           :dirs         {} ; dir path -> local size
                           :dirs-total   {} ; dir path -> total size
                           :dirs-details {} ; dir path -> dir name, path, local size, total size
                           :files        {}})] ; file path -> filename, path, size
    (fn
      ([] (xf))
      ([result]
       ;; Remember to flush any pending changes
       (let [old-state (-> *state deref)
             new-state (-> old-state finish-collecting)]
         (xf (submit-changes result xf old-state new-state))))
      ([result entry]
       (let [old-state (-> *state deref)
             state     (vswap! *state on-entry entry)]
         (submit-changes result xf old-state state))))))

(def find-directories-xform
  (comp
    (keep parse-line)
    filesystem-analyzer
    ;; Just keep the directories, ignore entries describing individual files
    (filter :dirname)))

(defn- find-directories-by-size-xform
  [^long limit]
  (comp
    find-directories-xform
    (filter (fn [{:keys [^long total-size]}]
              (< total-size limit)))
    (map :total-size)))

(defn first-problem
  "Find the sum of directories which are smaller than 100000"
  [input]
  (transduce (find-directories-by-size-xform 100000) + 0 input))

(def find-directory-sizes-xform
  (comp
    find-directories-xform
    (map :total-size)))

(defn second-problem
  "Find the smallest directory to delete which releases the necessary amount of space"
  [input]
  (let [fs-size             70000000
        target-unused-space 30000000
        all-dirs            (->> input
                                 (sequence find-directory-sizes-xform)
                                 sort
                                 vec)
        ^long used-space    (peek all-dirs) ; The last entry corresponds to the root directory
        unused-space        (- fs-size used-space)
        space-to-free       (- target-unused-space unused-space)]
    (->> all-dirs
         (drop-while (fn [^long size] (< size space-to-free)))
         (take 1)
         first)))
