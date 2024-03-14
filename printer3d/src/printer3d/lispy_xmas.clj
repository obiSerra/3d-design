(ns printer3d.lispy-xmas
  (:require [printer3d.utils :as utils]
            [scad-clj.scad :as scad]
            [scad-clj.model :as model]))

;; Measurements




;; Utils


;; Primitives

(defn lambda-symbol []
  (let [z 5
        x 5
        main-rod (model/cube x 100 z :center false)
        second-rod (->> (model/cube x 70 z :center false)
                        (model/translate [20 28 0])
                        (model/rotatec [0 0 (model/deg->rad 35)]))
        third-rod (->> (model/cube 10 5 z :center false)
                       (model/translate [x 0 0]))
        lambda (model/translate [0 12 0]
     (utils/round-edges 2 (model/union main-rod second-rod third-rod)))] 
    
    [lambda
     
     (->> lambda
          (model/scale [2/3 2/3 2/3])
          (model/translate [0 0 0])
          (model/rotatec [0 0 (model/deg->rad 60)])
          )]
    ))

(defn center-element [] (->> (model/difference
                              (model/cylinder 15 5)
                              (model/cylinder 10 30))
                             (model/translate [0 0 3])))

(defn make-star []
  
  (loop [els []
         ang 0]
    (if (> ang 360)
      els
      (recur (cons (model/rotatec [0 0 (model/deg->rad ang)] (lambda-symbol)) els) (+ 45 ang))
      )

    )
)

(comment
  (make-star)
)

(utils/save-file "lispy-xmas.scad" [(center-element) 
                                    (make-star)
                                    ])

