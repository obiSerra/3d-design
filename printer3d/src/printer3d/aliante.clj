(ns printer3d.disco-tommi
  (:require [printer3d.utils :as utils]
            [scad-clj.scad :as scad]
            [scad-clj.model :as model]))






(utils/save-file "aliante.scad" [(model/difference base-disc empty-top empty-bottom)])
