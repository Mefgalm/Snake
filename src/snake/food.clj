(ns snake.food
  (:require [snake.common :refer :all]))

(defn- generatate-food-point
  [exclude-points height width]
  (point (rand-int width) (rand-int height)))

(defn clear []
  {:food nil})

(defn fullfill 
  [{food :food :as food-generator} exclude-points {:keys [height width]}]
  (if (nil? food)
    {:food (generatate-food-point exclude-points height width)}
    food-generator))

(defn food? [{{:keys [x y]} :food} cx cy]
  (and (= x cx)
       (= y cy)))