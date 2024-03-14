(ns printer3d.porta-asciugamani
  (:require
   [scad-clj.model :as mdl]
   [scad-clj.scad :as scd]
   [printer3d.utils :as utils]))


(def hook-data {:hook-internal-r 14
                :hook-h 20
                :length 50
                :hook-tr 15.5})

(def hook-circle (mdl/difference
                  (binding [mdl/*fn* 200]
                    (mdl/difference
                     (mdl/cylinder
                      (-> hook-data :hook-internal-r (+ 3)) (-> hook-data :hook-h))
                     (mdl/cylinder
                      (:hook-internal-r hook-data) (-> hook-data :hook-h (* 2)))
                     (mdl/translate [(:hook-internal-r hook-data) 0 0]
                                    (mdl/cube (-> hook-data :hook-internal-r  (* 2))
                                              (-> hook-data :hook-internal-r (* 2))
                                              (-> hook-data :hook-h (* 2))))))))

(def hook (mdl/union hook-circle
                     (mdl/translate [(-> hook-data :length (/ 2)) (-> hook-data :hook-tr) 0]
                                    (mdl/cube (-> hook-data :length) 3 (-> hook-data :hook-h)))
                     (mdl/translate [(-> hook-data :length) (-> hook-data :hook-tr (* 2)) 0]
                                    (mdl/rotatec [0 (mdl/deg->rad 180) 0] hook-circle))))


(utils/save-file "porta-asciugamani" [(utils/round-edges 1 20 hook)])