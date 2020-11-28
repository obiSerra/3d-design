(ns printer3d.disco-tommi
  (:require [printer3d.utils :as utils]
            [scad-clj.scad :as scad]
            [scad-clj.model :as model]))



(def base-disc (model/cylinder 20 2.9))

(def empty-top (->>(model/cylinder (- 20 1.4) 1.3)
                   (model/color [1 0 0])
                   (model/translate [0 0 (- (/ -2.9 2) (/ -1.2 2))])))
(def empty-bottom (->> (model/cylinder (- 20 1.4) 0.12)
                       (model/color [0 1 0])
                       (model/translate [0 0 (- (/ 2.9 2) (/ 0.11 2))]))
  )

;;(utils/save-file "disco-tommi.scad" [(model/difference base-disc empty-top empty-bottom )])


(utils/save-file "disco-tommi.scad" [(model/difference base-disc empty-top empty-bottom)] true)


;;(utils/save-file "disco-tommi.scad" [ empty-bottom empty-top base-disc ])


