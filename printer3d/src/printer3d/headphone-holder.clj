(ns printer3d.headphone-holder
  (:require [printer3d.utils :as utils]
            [scad-clj.scad :as scad]
            [scad-clj.model :as model]))

;; Measurements

(def width 13)
(def thick 6)
(def main-length 76)
(def last-part-length 10)
(def l-length (+ 25 (* 2 thick))) 

(def socket-r 3.2)
(def socket-h 3.5)

;; Utils

(defn  element-end [element-length second-element-length]
  (* 0.5 (+ element-length second-element-length)) )


(defn round-edges [round element]
  (model/minkowski 
   (model/sphere round)
   element))

;; Primitives

(defn magnet-socket []
  (->> (model/cylinder socket-r socket-h)
       (model/translate [0 0 (- (- thick socket-h))])))

(def main-branch (model/difference (->>
                                    (model/cube width main-length thick)                  
                                    (round-edges 1)) 
                                   
                                   (model/translate [0 (/ main-length 3)] (magnet-socket))
                                   (model/translate [0 (- (/ main-length 3))] (magnet-socket))))

(def l-rod (->> (model/cube width l-length thick)              
                (model/rotatec [(model/deg->rad 90) 0 0])
                (model/color [1 0 0])
                (model/translate [0 (+ (- (element-end main-length l-length) (* 0.5 l-length)) (* 0.5 thick))
                                  (- (* l-length 0.5) 
                                     (* 0.5 thick ))])))

(def last-part (->>            
                (model/cube width last-part-length thick)
                (model/color [0 1 0])
                (model/translate [0 (- (element-end main-length last-part-length) last-part-length 0)
                                  (- l-length thick)]))) 



(utils/save-file "headphone-holder.scad" [main-branch (round-edges 1 (model/union l-rod last-part))] true)
