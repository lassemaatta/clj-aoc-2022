(ns lassemaatta.aoc-2022.day02)

(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)

(def abc->move
  {\A :rock
   \B :paper
   \C :scissors})

(def xyz->move
  {\X :rock
   \Y :paper
   \Z :scissors})

(def xyz->result
  {\X :lose
   \Y :draw
   \Z :win})

(def move->score
  {:rock     1
   :paper    2
   :scissors 3})

(defn- parse-line
  [parse-abc parse-xyz]
  (fn [^String line]
    (when (seq line)
      [(parse-abc (.charAt line 0))
       (parse-xyz (.charAt line 2))])))

(defn- decide-my-move
  [[opponent-move result]]
  [opponent-move
   (if (= result :draw)
     opponent-move
     (let [win? (= result :win)]
       (case opponent-move
         :rock     (if win? :paper :scissors)
         :paper    (if win? :scissors :rock)
         :scissors (if win? :rock :paper))))])

(defn- play
  [[opponent-move my-move]]
  (+ ^int (move->score my-move)
     ^int (if (= opponent-move my-move)
            3
            (case opponent-move
              :rock     (if (= my-move :scissors) 0 6)
              :paper    (if (= my-move :rock) 0 6)
              :scissors (if (= my-move :paper) 0 6)))))

(def letter-matches-best-move-xform
  (comp
    (map (parse-line abc->move xyz->move))
    (map play)))

(defn first-problem
  "Play a set of rock-paper-scissors. Assume xyz describes the optimal move."
  [input]
  (transduce letter-matches-best-move-xform + 0 input))

(def letter-matches-target-result
  (comp
    (map (parse-line abc->move xyz->result))
    (map decide-my-move)
    (map play)))

(defn second-problem
  "Play a set of rock-paper-scissors. Assume xyz describes the desired game result."
  [input]
  (transduce letter-matches-target-result + 0 input))
