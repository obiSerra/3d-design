(ns printer3d.ring
  (:require
   [scad-clj.model :as mdl]
   [scad-clj.scad :as scd]
   [printer3d.utils :as utils]))


(def ring-height 7)
(def inner-r 10.3)
(def thickness 1.8)

(def out-det 360)
(def in-det 360)

(def rounded true)

;; Internal d 18.45


(defn cut-edges [sph]
  (let [to-remove (mdl/cube 50 50 50)]
    (mdl/difference
     sph
     (mdl/translate [0 0 (+ 25 (/ ring-height 2))] to-remove)
     (mdl/translate [0 0 (+ -25 (/ ring-height -2))] to-remove))))

(def inner (binding [mdl/*fn* in-det] (mdl/cylinder inner-r 50)))
(def outer (binding [mdl/*fn* out-det] (mdl/sphere (+ inner-r thickness))))


(def ring (let [main (cut-edges (mdl/difference outer inner))]
            (if rounded (utils/round-edges 1 main) main)))



(utils/save-file "ring" [(mdl/color [1 0 0]ring)])