(ns snake.snake
  (:require [snake.common :refer :all]))

(defn create [height width]
  {:body [(point (rand-int width) (rand-int height))]
   :direction :up})

(defn- move
  [{snake-body :body :as snake} new-position]
  (let [state (reduce (fn [{body :body next :next} next-step]
                        {:body (conj body next) :next next-step})
                      {:body [] :next new-position} snake-body)]
    (update snake :body (fn [_] (:body state)))))

(defn- reverse-direction [direction]
  (cond
    (= direction :up) :down
    (= direction :left) :right
    (= direction :right) :left
    (= direction :down) :up))

(defn- possible-way?
  [{:keys [body direction]} new-direction]
  (or (= (count body) 1)
       (not= (reverse-direction direction) new-direction)))

(defn fix-value [x range operation]
  (mod (operation x) range))

(defn- shift-in-direction
  [{:keys [x y] :as point} direction height width]
  (cond
    (= direction :up) (update point :y fix-value height dec)
    (= direction :down) (update point :y fix-value height inc)
    (= direction :left) (update point :x fix-value width dec)
    (= direction :right) (update point :x fix-value width inc)))

(defn- bite-tale?
  [{[head & tail] :body}]
  (some #(= head %) tail))

(defn go-in-direction
  [{[head & tail] :body snake-direction :direction :as snake} new-direction {:keys [height width]}]
  (let [direction (if (possible-way? snake new-direction) new-direction snake-direction)
        new-snake (-> snake
                      (move (shift-in-direction head direction height width))
                      (update :direction (fn [_] direction)))]
    (when-not (bite-tale? new-snake)
      new-snake)))

(defn add-length
  [{:keys [body direction] :as snake} {:keys [height width]}]
  (let [new-last-block (shift-in-direction (last body) (reverse-direction direction) height width)]
    (update snake :body conj new-last-block)))

(defn get-body [{body :body}]
  body)

(defn snake? [{body :body} x y]
  (let [point (point x y)]
    (some #(= % point) body)))