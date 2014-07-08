(ns bushou.gui)

(use 'clojure.repl)
(use 'seesaw.core)

(native!)

(defonce f (-> (frame :title "Practice Vocab") pack! show!))
(config! f :size [300 :by 300])

(def j (atom 1))
(def i (atom 0))

(defn apply-interpose [i & s]
  (apply str (interpose i (apply list* s))))

#_(defn split-line [s]
  (apply str (flatten (interpose "\r\n" (partition-all 60 s)))))

(defn get-txt [data offset]
  (apply-interpose " " (+ @i offset) (take @j (nth data (mod @i (count data))))))

(defn refresh! [data offset] (config! f :content (config! (label (get-txt data offset)) :font "ARIAL-32")))

(defn slurp-lines [f]
  (filter #(not (re-find #"^\*+$" %)) (.split (slurp f) "\n")))

(defn prep-line [line]
  (map #(let [
              phrase (.trim %)
              i (.indexOf phrase "_")
              ]
          (if (.endsWith phrase "_")
            (-> phrase (.substring 0 i) .trim)
            phrase))
       (.split line ",")))

(defn -main [& [file num-to-drop]]
  (if file
    (let [
          ix (if num-to-drop (Integer/parseInt num-to-drop) 0)
          data (slurp-lines (str "data/" file))
          data (if num-to-drop (drop ix data) (shuffle data))
          data (map prep-line data)
          capped-inc (fn [x] (min (count (first data)) (inc x)))
          default-j (condp = file
                      "bushou.csv" 4
                      2)
          ]
      (reset! j default-j)
      (refresh! data ix)
      (listen f :key-typed
              (fn [e]
                (condp = (str (.getKeyChar e))
                  "n" (do (swap! i inc) (reset! j default-j) (refresh! data ix))
                  "p" (do (swap! i dec) (reset! j default-j) (refresh! data ix))
                  "h" (do (swap! j capped-inc) (refresh! data ix))
                  "s" (do (reset! i 0) (reset! j default-j) (refresh! data ix))
                  "o" (.exec (Runtime/getRuntime) (format "open data/%s" file))
                  nil))))
    (do
      (println "Usage: file <num-to-drop>")
      (System/exit 0))))
