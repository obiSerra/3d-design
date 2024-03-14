(ns printer3d.picurity-camera.pi-holder
  (:require
   [scad-clj.model :as mdl]
   [printer3d.utils :as utils]))


(def pi-data {:x 68
              :y 33
              :z 16})


(def sd-hole {:x 8 :y 15 :z 12})
(def camera-hole {:x 8 :y 16 :z 3})
(def usb-hole {:x 28 :y 5 :z 5})
(def hdmi-hole {:x 20 :y 5 :z 5})

(def shell-size 2)

(def pi-volume (mdl/cube (-> pi-data :x) (-> pi-data :y) (-> pi-data :z)))

(defn add-shell [value]
  (+ value shell-size))

(def bottom-shell-structure
  (mdl/difference
   (mdl/resize [(-> pi-data :x add-shell) (-> pi-data :y add-shell) (-> pi-data :z)] pi-volume)
;;    (mdl/translate [(-> pi-data :x (/ 2) -) 2 (-> pi-data :z (/ 2) (- shell-size) (- (/ 2 (:z sd-hole))) -)]
;;                   (mdl/cube (-> sd-hole :x) (-> sd-hole :y) (-> sd-hole :z)))
   (mdl/translate [0 0 shell-size] pi-volume)))


(def bottom-shell
  (mdl/difference
   bottom-shell-structure
   (mdl/translate [(-> pi-data :x (/ 2) -) 4 (-> pi-data :z (/ 2) -)]
                  (mdl/cube (-> sd-hole :x) (-> sd-hole :y) (-> sd-hole :z)))
   (mdl/translate [(-> pi-data :x (/ 2)) 0 (-> pi-data :z (/ 2) (- 3.5) -)]
                  (mdl/cube (-> camera-hole :x) (-> camera-hole :y) (-> camera-hole :z)))
   
   (mdl/translate [17 (-> pi-data :y (/ 2) -) (-> pi-data :z (/ 2) (- 4.5) -)]
                  (mdl/cube (-> usb-hole :x) (-> usb-hole :y) (-> usb-hole :z)))
   (mdl/translate [-20 (-> pi-data :y (/ 2) -) (-> pi-data :z (/ 2) (- 4.5) -)]
                  (mdl/cube (-> hdmi-hole :x) (-> hdmi-hole :y) (-> hdmi-hole :z)))
   ))

(utils/save-file "picurity/pi-container" [bottom-shell])