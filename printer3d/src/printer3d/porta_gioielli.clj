(ns printer3d.porta-gioielli
  (:require
   [scad-clj.model :as mdl]
   [scad-clj.scad :as scd]
   [printer3d.utils :as utils]))


;; Dimensions

(def roundness 0.5)

(def basic-r 60)
(def basic-z 10)
(def basic-thick 8)

(def base-x 120)
(def base-y 180)
(def base-z 10)
(def base-thick 20)

(def tr-up  (- (* basic-r 2) 4.2))

(def up-inc 2)
(def up-thick 2)


(def hook-r 5)
(def hook-h 5)
(def hook-t 3)

;; Forms

(defn exagon [r h]  (binding [mdl/*fn* 6] (mdl/cylinder r h)))
(defn cave-exagon [r h t]  (mdl/difference (exagon r h)
                                           (exagon (- r t) (* 2 h))))


(def hook (mdl/rotatec
           [(mdl/deg->rad 90) (mdl/deg->rad 0) (mdl/deg->rad 90)]
           (mdl/difference (binding [mdl/*fn* 6] (mdl/cylinder hook-r hook-h))
                           (binding [mdl/*fn* 6] (mdl/cylinder (- hook-r hook-t) (* hook-h 2)))
                           (mdl/translate [hook-r (- 1.7 hook-r) 0]
                                          (mdl/cube (* 2 hook-r) (* 2 hook-r) (* hook-h 2))))))

(defn lateral-hooks [tr-z] (mdl/union (mdl/translate [16 (- (* basic-r 2) tr-z) (- basic-thick 1)] hook)
                                      (mdl/translate [30 (- (* basic-r 2) tr-z) (- basic-thick 1)] hook)
                                      (mdl/translate [44 (- (* basic-r 2) tr-z) (- basic-thick 1)] hook)

                                      (mdl/translate [(- 16) (- (* basic-r 2) tr-z) (- basic-thick 1)] hook)
                                      (mdl/translate [(- 30) (- (* basic-r 2) tr-z) (- basic-thick 1)] hook)
                                      (mdl/translate [(- 44) (- (* basic-r 2) tr-z) (- basic-thick 1)] hook)))

(defn central-hooks [tr-z] (mdl/union (mdl/translate [(- 1 (/ basic-r 2)) (- (* basic-r 2) tr-z) (- basic-thick 1)] hook)
                                      (mdl/translate [(- 15 (/ basic-r 2)) (- (* basic-r 2) tr-z) (- basic-thick 1)] hook)
                                      (mdl/translate [(- 29 (/ basic-r 2)) (- (* basic-r 2) tr-z) (- basic-thick 1)] hook)))


(def full-top-right (mdl/translate [basic-r basic-r 0] (cave-exagon basic-r basic-z basic-thick)))
(def full-central (cave-exagon basic-r basic-z basic-thick))
(def full-top-left (mdl/translate [(- 0 basic-r) basic-r 0] (cave-exagon basic-r basic-z basic-thick)))

(def full-bottom-left (mdl/translate [(- 0 basic-r) (- 0 basic-r) 0] (cave-exagon basic-r basic-z basic-thick)))
(def full-bottom-right (mdl/translate [basic-r (- 0 basic-r) 0] (cave-exagon basic-r basic-z basic-thick)))

(def final-central
  (mdl/difference
   full-central
   (mdl/translate [0 0 (- (/ basic-z 2))] full-top-right)
   (mdl/translate [0 0 (- (/ basic-z 2))] full-top-left)
   (mdl/translate [0 0 (- (/ basic-z 2))] full-bottom-right)
   (mdl/translate [0 0 (- (/ basic-z 2))] full-bottom-left)))

(def final-top-right
  (mdl/union
   (mdl/difference
    full-top-right
    (mdl/translate [0 0 (/ basic-z 2)] full-central))
   (let [z 11]
     (mdl/union
      (mdl/translate [32 (- (* basic-r 2) z) (- basic-thick 1.5)] hook)
      (mdl/translate [60 (- (* basic-r 2) z) (- basic-thick 1.5)] hook)
      (mdl/translate [88 (- (* basic-r 2) z) (- basic-thick 1)] hook)))
   (let [z 107]
     (mdl/union
      (mdl/translate [32 (- (* basic-r 2) z) (- basic-thick 1.5)] hook)
      (mdl/translate [60 (- (* basic-r 2) z) (- basic-thick 1.5)] hook)
      (mdl/translate [88 (- (* basic-r 2) z) (- basic-thick 1)] hook)))
   (let [z 58]
     (mdl/union
      (mdl/translate [5 (- (* basic-r 2) z) (- basic-thick 1.5)] hook)
      (mdl/translate [115 (- (* basic-r 2) z) (- basic-thick 1)] hook)))))


(def final-top-left
  (mdl/union
   (mdl/difference
    full-top-left
    (mdl/translate [0 0 (/ basic-z 2)] full-central))
   (let [z 11]
     (mdl/union
      (mdl/translate [-32 (- (* basic-r 2) z) (- basic-thick 1.5)] hook)
      (mdl/translate [-60 (- (* basic-r 2) z) (- basic-thick 1.5)] hook)
      (mdl/translate [-88 (- (* basic-r 2) z) (- basic-thick 1)] hook)))
   (let [z 107]
     (mdl/union
      (mdl/translate [-32 (- (* basic-r 2) z) (- basic-thick 1.5)] hook)
      (mdl/translate [-60 (- (* basic-r 2) z) (- basic-thick 1.5)] hook)
      (mdl/translate [-88 (- (* basic-r 2) z) (- basic-thick 1)] hook)))
   (let [z 58]
     (mdl/union
      (mdl/translate [-5 (- (* basic-r 2) z) (- basic-thick 1.5)] hook)
      (mdl/translate [-115 (- (* basic-r 2) z) (- basic-thick 1)] hook)))))

(def final-bottom-left
  (mdl/union
   (mdl/difference
    full-bottom-left
    (mdl/translate [0 0 (/ basic-z 2)] full-central))
   (let [z 131]
     (mdl/union
      (mdl/translate [-32 (- (* basic-r 2) z) (- basic-thick 1.5)] hook)
      (mdl/translate [-60 (- (* basic-r 2) z) (- basic-thick 1.5)] hook)
      (mdl/translate [-88 (- (* basic-r 2) z) (- basic-thick 1)] hook)))
   (let [z 177]
     (mdl/union
      (mdl/translate [-5 (- (* basic-r 2) z) (- basic-thick 1.5)] hook)
      (mdl/translate [-115 (- (* basic-r 2) z) (- basic-thick 1)] hook)))))

(def final-bottom-right
  (mdl/union
   (mdl/difference
    full-bottom-right
    (mdl/translate [0 0 (/ basic-z 2)] full-central))
   (let [z 131]
     (mdl/union
      (mdl/translate [32 (- (* basic-r 2) z) (- basic-thick 1.5)] hook)
      (mdl/translate [60 (- (* basic-r 2) z) (- basic-thick 1.5)] hook)
      (mdl/translate [88 (- (* basic-r 2) z) (- basic-thick 1)] hook)))
   (let [z 177]
     (mdl/union
      (mdl/translate [5 (- (* basic-r 2) z) (- basic-thick 1.5)] hook)
      (mdl/translate [115 (- (* basic-r 2) z) (- basic-thick 1)] hook)))))




(def top
  (mdl/translate [0 0 tr-up]
                 (mdl/rotatec
                  [(mdl/deg->rad 90) 0 0]
                  (mdl/union
                   final-central
                   (mdl/color [1 0 0] final-top-right)
                   (mdl/color [0 0 1] final-top-left)
                   (mdl/color [0 0 1] final-bottom-right)
                   (mdl/color [1 0 0] final-bottom-left)))))


(def base (mdl/difference
           (mdl/difference
            (utils/round-edges 2 (mdl/difference
                                  (mdl/cube base-x base-y base-z)
                                  (mdl/cube (- base-x base-thick) (- base-y base-thick) (* base-z 2))))
            (mdl/translate [0 0 -3] top))
           (mdl/translate [0 0 (- base-z)] (mdl/cube (* base-x 2) (* base-y 2) base-z))))

(comment
  (utils/save-file "porta-gioielli-top" [top])
  (utils/save-file "porta-gioielli-base" [base])

  (utils/save-file "porta-gioielli-top-right" [final-top-right] true)


  (doall
   (map
    #(utils/save-file (str "porta-gioielli-" (clojure.string/replace % #"final-" "")) [(eval (symbol %))] true)
    ["final-central" "final-top-right" "final-top-left" "final-bottom-right" "final-bottom-left" "base"]))
  ;; => (nil nil nil nil nil nil)





  ;; => final-central


  ;; => nil

  ;; => nil
  )
;; => nil



(utils/save-file "porta-gioielli" [(eval (symbol "base"))])
;; => nil

;; => Execution error (IllegalArgumentException) at scad-clj.scad/eval10876$fn (scad.clj:9).
;;    Don't know how to create ISeq from: clojure.lang.Symbol
