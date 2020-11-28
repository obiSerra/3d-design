(ns printer3d.utils
  (:require [scad-clj.scad :as scad]
            [scad-clj.model :as model]))

(def _base-path "/mnt/c/Users/rober/Documents/3d-designs/")

(defn save-file 
  ([file-name shapes] (save-file file-name shapes false))
  ([file-name shapes with-render]
   (spit (str _base-path file-name) 
         (scad/write-scad
          (if with-render (model/render shapes) shapes)))))

(defn round-edges [round element]
  (model/minkowski 
   (model/sphere round)
   element))
