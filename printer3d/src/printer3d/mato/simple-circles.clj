(ns printer3d.mato.utils
  (:require
   [scad-clj.model :as mdl]
   [printer3d.mato.common :refer [gen-file]]
   [printer3d.utils :refer [save-file]]
   ))

(defn circle [r hole]
  (let [c (mdl/circle r)]
    (mdl/scale [1 1.2 1] (mdl/difference c (mdl/scale [hole hole 1] c)))))


(defn circle-earrings [{:keys [radius hole height sec-translate]}]
  
  (let [s 10
        c (binding [mdl/*fn* s] (circle radius hole))
        c2 (mdl/translate [0 sec-translate 0] (binding [mdl/*fn* s] (circle radius hole)))
        hc (mdl/translate [0 (- (* 1.2 radius)) 0] (binding [mdl/*fn* 360] (mdl/difference (mdl/circle 25/10) (mdl/circle 15/10))))]

    (mdl/extrude-linear {:height height} (mdl/union c c2 hc))))

(save-file "simple-circle.scad" (mdl/render (circle-earrings {:radius 20 :hole 87/100 :height 32/10 :sec-translate 12})))