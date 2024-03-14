(ns printer3d.utils
  (:require [scad-clj.scad :as scad]
            [scad-clj.model :as model]))

(def _base-path "scad-files/")

(defn save-file
  ([file-name shapes] (save-file file-name shapes false))
  ([file-name shapes with-render]
   (spit (str _base-path (str file-name ".scad"))
         (scad/write-scad
          (if with-render (model/render shapes) shapes)))))

(defn round-edges
  ([round element]
   (round-edges round 200 element))
  ([round angles element]
   (model/minkowski
    (binding [model/*fn* angles] (model/sphere round))
    element)))

(defn round-edges-flat [round element]
  (model/minkowski
   (binding [model/*fn* 200] (model/circle round))
   element))
