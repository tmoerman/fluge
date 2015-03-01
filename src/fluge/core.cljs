(ns fluge.core
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]
            [fluge.data :as d]))

(defn to-points
  [[_ _ _ from-long from-lat _ _ _ to-long to-lat _ _ _]]
  (let [size (q/width)
        y-offset (- (/ size 8) 10)

        from-x (-> (q/map-range from-long -180 180  10 (- (/ size 2) 10)))
        from-y (-> (q/map-range from-lat  -180 180  (- (/ size 2) 10) 10) (- y-offset))
        to-x   (-> (q/map-range to-long   -180 180  (+ (/ size 2) 10) (- size 10)))
        to-y   (-> (q/map-range to-lat    -180 180  (- (/ size 2) 10) 10) (- y-offset))]
    
    [from-x from-y to-x to-y]))

(defn setup []
  (q/frame-rate 4)
  (q/no-stroke)

  {:mouse [0 0]
   :flights (->> d/flights
                 (map to-points))})

(defn mouse-moved [state {x :x y :y}]
  (assoc state :mouse [x y]))

(defn departure-in-vicinity?
  [distance [mouse-x mouse-y] [from-x from-y _ _]]
  (< (q/dist from-x from-y mouse-x mouse-y) distance))

(def dot-size 2)
(defn draw-departures [flights] (doseq [[x y _ _] flights] (q/ellipse x y dot-size dot-size)))
(defn draw-arrivals   [flights] (doseq [[_ _ x y] flights] (q/ellipse x y dot-size dot-size)))

(defn draw-brushed-linked
  [{mouse :mouse flights :flights}]
  (let [{in-vicinity true not-in-vicinity false} (group-by (partial departure-in-vicinity? 10 mouse) flights)]
    
    (q/fill 0 100 255 20)
    (draw-departures not-in-vicinity)

    (q/fill 0 100 255 5)
    (draw-arrivals not-in-vicinity)

    (q/fill 255 255 0 5)
    (draw-departures in-vicinity)

    (q/fill 255 255 0 30)
    (draw-arrivals in-vicinity)))

(defn draw [state]
  (q/background 10 10 20)
  (draw-brushed-linked state))

(q/defsketch fluge
  :host        "fluge"
  :size        [1200 300]
  :setup       setup
  :mouse-moved mouse-moved
  :draw        draw
  :middleware  [m/fun-mode])
