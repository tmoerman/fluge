(ns fluge.core
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]
            [fluge.data :as d]))

;.
;;; Fluge
;'

(def fluge-width 1200)
(def fluge-height 300)

(defn deptartures-left-destinations-right
  [[_ _ _ from-long from-lat _ _ _ to-long to-lat _ _ _]]
  (let [size  fluge-width
        y-offset (- (/ size 8) 10)

        from-x (-> (q/map-range from-long -180 180  10 (- (/ size 2) 10)))
        from-y (-> (q/map-range from-lat  -180 180  (- (/ size 2) 10) 10) (- y-offset))
        to-x   (-> (q/map-range to-long   -180 180  (+ (/ size 2) 10) (- size 10)))
        to-y   (-> (q/map-range to-lat    -180 180  (- (/ size 2) 10) 10) (- y-offset))]
    
    [from-x from-y to-x to-y]))

(defn setup-fluge []
  (q/smooth)
  (q/frame-rate 4)
  (q/no-stroke)

  {:mouse [0 0]
   :flights (->> d/flights
                 (map deptartures-left-destinations-right))})

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

(defn draw-fluge [state]
  (q/background 10 10 20)
  (draw-brushed-linked state))

(comment
  (q/defsketch fluge
               :host "fluge"
               :size [fluge-width fluge-height]
               :setup setup-fluge
               :mouse-moved mouse-moved
               :draw draw-fluge
               :middleware [m/fun-mode]))


;.
;;; Fluge 2
;' 

(def fluge-2-width 800)

(defn to-points
  [[_ _ _ from-long from-lat _ _ _ to-long to-lat _ _ _]]
  (let [size     fluge-2-width
        y-offset (- (/ size 4) 20)

        from-x (-> (q/map-range from-long -180 180  10 (- size 10)))
        from-y (-> (q/map-range from-lat  -180 180  (- size 10) 10) (- y-offset))
        to-x   (-> (q/map-range to-long   -180 180  10 (- size 10)))
        to-y   (-> (q/map-range to-lat    -180 180  (- size 10) 10) (- y-offset))]

    [from-x from-y to-x to-y]))

(defn setup-fluge-2 []
  (q/frame-rate 4)
  
  {:mouse [0 0]
   :flights (->> d/flights
                 (map to-points))})

(defn draw-fluge-2 [{:keys [mouse flights]}]
  (q/background 10 10 20)

  (let [in-vicinity (filter (partial departure-in-vicinity? 10 mouse) flights)]

    (q/fill 0 100 255 5)
    (q/no-stroke)
    (draw-departures flights)

    (q/fill 255 255 0 5)
    (doseq [[from-x from-y to-x to-y] in-vicinity]
      
      (q/stroke 255 255 0 10)
      (q/stroke-weight 1)
      (q/line from-x from-y to-x to-y)

      )))

  

(q/defsketch fluge-2
             :host "fluge-2"
             :size [800 400]
             :setup setup-fluge-2
             :mouse-moved mouse-moved
             :draw draw-fluge-2
             :middleware [m/fun-mode])