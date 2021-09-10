(ns printer3d.mato.common
  (:require [scad-clj.model :as mdl]
            [scad-clj.scad :as scd]))

(defn gen-file [filename data]
  (spit filename (scd/write-scad data)))

(defn hook-circle-full []
  (mdl/difference (mdl/circle 25) (mdl/circle 15)))

(defn hook-circle
  ([] (hook-circle 40 20 10))
  ([w h t]
     (mdl/difference (hook-circle-full) (mdl/translate [0 t 0] (mdl/square w h)))))

