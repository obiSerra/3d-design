(ns printer3d.doccia
  (:require
   [scad-clj.model :as mdl]
   [scad-clj.scad :as scd]
   [printer3d.utils :as utils]))


(def filename "manopola-doccia")

(defn gen-file [filename data]
  (spit filename (scd/write-scad data)))


;;7.26

;; Helpers
(defn r [d] (/ d 2))


;; Measures
(def cylinder-height 20)
(def hole-r (r 7.26))
(def outer-r (r 34))

;; Elements
(def out-cyl (mdl/cylinder outer-r cylinder-height))
(def hole (mdl/cylinder hole-r cylinder-height))
(def holder (mdl/difference out-cyl hole))

(def indicator (->> (mdl/cube 5 5 (- cylinder-height 2) :center false)
                    (mdl/translate [(- outer-r 2) 0 (+ (/ cylinder-height -2) 1)])
                    (utils/round-edges 1)))

(comment)
(gen-file (str "scad-files/" filename ".scad") (mdl/render [holder indicator]))