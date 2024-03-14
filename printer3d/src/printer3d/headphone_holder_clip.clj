(ns printer3d.headphone-holder-clip
  (:require [printer3d.utils :as utils]
            [scad-clj.scad :as scad]
            [scad-clj.model :as model]))

;; Measurements

(def width 13)
(def thick 6)
(def main-length 76)
(def last-part-length 10)
(def l-length (+ 25 (* 2 thick))) 

(def clip-height 27)
(def clip-length 30)


;; Utils

(defn  element-end [element-length second-element-length]
  (* 0.5 (+ element-length second-element-length)) )


(defn round-edges [round element]
  (model/minkowski 
   (model/sphere round)
   element))

;; Primitives



(def main-branch (model/difference (->>
                                    (model/cube width main-length thick)                  
                                    (round-edges 1))))

(def l-rod (->> (model/cube width l-length thick)              
                (model/rotatec [(model/deg->rad 90) 0 0])
                (model/color [1 0 0])
                (model/translate [0 (+ (- (element-end main-length l-length) (* 0.5 l-length)) 
                                       (* 0.5 thick))
                                  (- (* l-length 0.5) 
                                     (* 0.5 thick ))])))

(def last-part (->>            
                (model/cube width last-part-length thick)
                (model/color [0 1 0])
                (model/translate [0 (- (element-end main-length last-part-length) last-part-length 0)
                                  (- l-length thick)]))) 

(def clip-y-pos (- 0 (- (- (element-end main-length l-length) 
                           (* 0.5 l-length)) 
                        (* 0.5 thick))))

(def clip-top (->>
               (model/cube width thick clip-length)
               (model/color [0 0.7 0])
               (model/translate [0 clip-y-pos (- 0 (/ clip-length 2))])))

(def clip-bottom (->>
                  (model/cube width thick clip-length)
                  (model/color [0.7 0 0])
                  (model/translate [0 (+ clip-y-pos clip-length thick) (- 0 (/ clip-length 2))])))


(def ruler (->>
              (model/cube 2 clip-length 2)
                  (model/color [0.2 0 0.4])
                  (model/translate [10 -17 -20]
                   )
                  ))

(utils/save-file "headphone-holder-clip.scad" [main-branch 
                                               (round-edges 1 (model/union l-rod last-part))
                                               (round-edges 1 clip-top)
                                               (round-edges 1 clip-bottom)] false)
