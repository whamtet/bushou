(ns bushou.core
  )

;;transfer data from old to new format

(defn split [s]
  (let [
        s (vec (.split s ","))
        ]
    (if (= 5 (count s)) (conj s "") s)))

(defn slurp-csv [f]
  (map split (.split (slurp f) "\n")))

(defn apply-interpose [i s]
  (apply str (interpose i s)))

(defn spit-csv [f s]
  (spit f (apply-interpose "\n" (map #(apply-interpose "," %) s))))

(defn rearrange [[char alt trad pinyin meaning hint]]
  [char alt trad pinyin
   (if (= "" hint) "no hint" hint) meaning])

;(spit-csv "data/bushou.csv" (map rearrange (slurp-csv "csv/data.csv")))
