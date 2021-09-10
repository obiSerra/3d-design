(ns printer3d.orecchini-triangolo
  (:require 
            [scad-clj.model :as mdl]
            [printer3d.common :refer [hook-circle gen-file]]))

(defn triangle [t-side hole]
  (let [half-s (/ t-side 2)
        margin (/ (- 1 hole) 2)
        triangle (mdl/polygon [[0 0] [half-s t-side] [t-side 0]] [[0 1 2]])]
    (mdl/difference
     triangle
     (mdl/translate [(* margin t-side) (* margin half-s) 0] (mdl/scale [hole hole 1] triangle)))))

(defn circle [r hole]
  (let [c (mdl/circle r)]
    (mdl/difference c (mdl/scale [hole hole 1] c))))

(defn earrings [{:keys [side hole height sec-translate]}]
  (let [t (triangle side hole)
        t2 (mdl/translate [0 sec-translate 0] t)
        hc (mdl/translate [(/ side 2) 0 0] (hook-circle))]

    (mdl/extrude-linear {:height height} (mdl/union t t2 hc))))

(defn triangolo-cerchi [{:keys [side hole height sec-translate]}]
  (let [t (triangle side hole)
        c (mdl/translate [ 50 10 0] (circle 20 7/10))
        hc (mdl/translate [(/ side 2) 0 0] (hook-circle))]

    (mdl/extrude-linear {:height height} (mdl/union t c hc))))

(comment
  ;(gen-file "scad-files/triangolo-original.scad" (mdl/render (earrings {:side 200 :hole 7/10 :height 5 :sec-translate 70})))
  (gen-file "scad-files/triangolo-cerchi.scad" (mdl/render (triangolo-cerchi {:side 200 :hole 7/10 :height 5 :sec-translate 70})))
  
  )
