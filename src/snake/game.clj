(ns snake.game
  (:gen-class)
  (:import jline.console.ConsoleReader)
  (:require [snake.snake :as sn]
            [snake.food :as food]
            [snake.common :refer :all]))

(defn create-arena [height width]
  {:height height
   :width width})

(defn snake-food-collision?
  [snake {{:keys [x y]} :food}]
  (sn/snake? snake x y))

(defn create-game-state [height width]
  (let [snake (sn/create height width)
        area (create-arena height width)]
    {:area area
     :snake snake
     :food (food/fullfill nil (sn/get-body snake) area)}))

(defn detech-collisions
  [{:keys [area snake food] :as state}]
  (if (snake-food-collision? snake food)
    (-> state
        (update :snake sn/add-length area)
        (update :food (food/clear)))
    state))

(defn update-food
  [{:keys [area snake food] :as state}]
  (update state :food food/fullfill (sn/get-body snake) area))

(defn- char-to-direction [str]
  (cond
    (= 119 str) :up
    (= 115 str) :down
    (= 97 str) :left
    (= 100 str) :right))

(defn get-direction! []
  (-> (ConsoleReader.) (.readCharacter ) (char-to-direction)))

(defn update-snake
  [{:keys [area snake _] :as state}]
  (when-let [direction (get-direction!)]
    (when-let [new-snake (sn/go-in-direction snake direction area)]
      (update state :snake (fn [_] new-snake)))))

(defn view
  [{:keys [area snake food]}]
  (let [height (:height area)
        width (:width area)]
    (reduce #(str %1 \newline %2)
            \newline
            (for [y (range height)]
              (apply str (for [x (range width)]
                           (cond
                             (sn/snake? snake x y) "s"
                             (food/food? food x y) "x"
                             :else "_")))))))

(defn dirty-view [state]
  (println (view state))
  state)

(defn game-update
  [state]
  (-> state
      (dirty-view)
      (detech-collisions)
      (update-food)
      (update-snake)))

(defn game-loop [state]
  (when state
    (recur (game-update state))))

(defn -main [& args]
  (game-loop (create-game-state 20 20)))
