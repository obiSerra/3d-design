(ns printer3d.picurity-camera.picamera-holder
  (:require
   [scad-clj.model :as mdl]
   [printer3d.utils :as utils]))

(def cover-data {:center {:x 34 :y 34}
                 :sides {:x 14 :y 23}})
(def light-data {:r 10
                 :small {:r 5 :pos {:x 6 :y 10}}})

(def camera-data {:r 10
                  :small {:r 5 :pos {:x 12 :y 12}}})

(def center-sq (mdl/square (-> cover-data :center :x) (-> cover-data :center :y)))


(def side (->> (mdl/circle (-> cover-data :sides :y (/ 2)))
               (mdl/translate [(-> cover-data :sides :x (/ 2)) 0 0])
               (mdl/union
                (mdl/square (-> cover-data :sides :x) (-> cover-data :sides :y)))))



(def cover-shape
  (mdl/union
   (mdl/translate [0 -2 0] center-sq)
   (mdl/translate [(+ (-> cover-data :sides :x (/ 2)) (-> cover-data :center :x (/ 2))) 0 0] side)
   (mdl/translate [(+ (-> cover-data :sides :x (/ 2) -) (-> cover-data :center :x (/ 2) -)) 0 0]
                  (mdl/rotatec [0 0 (mdl/deg->rad 180)] side))))

(defn make-light [data]
  (binding [mdl/*fn* 200] (mdl/union (mdl/cylinder (-> data :r) 10)
                                     (mdl/translate [(-> data :small :pos :x) (-> data :small :pos :y) 0] (mdl/cylinder (-> data :small :r) 10)))))

(def light
  (make-light light-data))

(def cover-front
  (let [bottom-h 3
        spacers-h 3]
    (mdl/difference
     (mdl/union
      (mdl/extrude-linear {:height bottom-h} cover-shape)
      (mdl/translate [0 -15 spacers-h] (mdl/cylinder 2 8))
      (mdl/translate [19 -8 spacers-h] (mdl/cylinder 2 8))
      (mdl/translate [-19 8 spacers-h] (mdl/cylinder 2 8)))
     (mdl/translate [-13 -17 0] (mdl/cube 10 10 20))
     (mdl/translate [31 0 0] (mdl/rotatec [0 0 (mdl/deg->rad 90)] light))
     (mdl/translate [-30 0 0] (mdl/rotatec [0 0 (mdl/deg->rad 270)] light))
     (mdl/translate [0 -2 0] (mdl/rotatec [0 0 (mdl/deg->rad 270)] (make-light camera-data))))))

(def cover-back
  (mdl/scale [1.05 1.05 1]
             (let [bottom-h 1
                   border-w 1
                   border-h 20]
               (mdl/difference
                (mdl/extrude-linear {:height border-h} (utils/round-edges-flat border-w cover-shape))
                (mdl/translate [0 0 bottom-h] (mdl/extrude-linear {:height border-h} cover-shape))
                (mdl/translate [0 -16 bottom-h] (mdl/cube 21 21 border-h))))))

(utils/save-file "picurity/camera-older" [cover-front
                                          ;; => (:difference
                                          ;;     (:union
                                          ;;      (:extrude-linear
                                          ;;       {:scale nil, :twist nil, :center true, :convexity nil, :height 2}
                                          ;;       (:union
                                          ;;        (:translate [0 -2 0] (:square {:x 34, :y 34, :center true}))
                                          ;;        (:translate [24 0 0] (:union (:square {:x 14, :y 23, :center true}) (:translate [7 0 0] (:circle {:r 23/2}))))
                                          ;;        (:translate
                                          ;;         [-24 0 0]
                                          ;;         (:rotatec
                                          ;;          [0 0 3.141592653589793]
                                          ;;          (:union (:square {:x 14, :y 23, :center true}) (:translate [7 0 0] (:circle {:r 23/2})))))))
                                          ;;      (:translate [0 -15 4] (:cylinder {:h 5, :r 2, :center true}))
                                          ;;      (:translate [19 -8 4] (:cylinder {:h 5, :r 2, :center true}))
                                          ;;      (:translate [-19 8 4] (:cylinder {:h 5, :r 2, :center true})))
                                          ;;     (:translate [-13 -17 0] (:cube {:x 10, :y 10, :z 20, :center true}))
                                          ;;     (:translate
                                          ;;      [31 0 0]
                                          ;;      (:rotatec
                                          ;;       [0 0 1.5707963267948966]
                                          ;;       (:union
                                          ;;        (:cylinder {:fn 200, :h 10, :r 10, :center true})
                                          ;;        (:translate [6 10 0] (:cylinder {:fn 200, :h 10, :r 5, :center true})))))
                                          ;;     (:translate
                                          ;;      [-30 0 0]
                                          ;;      (:rotatec
                                          ;;       [0 0 4.71238898038469]
                                          ;;       (:union
                                          ;;        (:cylinder {:fn 200, :h 10, :r 10, :center true})
                                          ;;        (:translate [6 10 0] (:cylinder {:fn 200, :h 10, :r 5, :center true})))))
                                          ;;     (:translate
                                          ;;      [0 -2 0]
                                          ;;      (:rotatec
                                          ;;       [0 0 4.71238898038469]
                                          ;;       (:union
                                          ;;        (:cylinder {:fn 200, :h 10, :r 10, :center true})
                                          ;;        (:translate [12 12 0] (:cylinder {:fn 200, :h 10, :r 5, :center true}))))))
                                          ])
