(ns bushou.translate
  (:require [clj-http.client :as client]
            [clojure.pprint :as pprint]
  ))


(def token (atom nil))
(defn set-token []
  (->> (client/post "https://datamarket.accesscontrol.windows.net/v2/OAuth2-13"
             {:form-params {:client_id (slurp "client-id.key")
                            :client_secret (slurp "client-secret.key")
                            :scope "http://api.microsofttranslator.com/"
                            :grant_type "client_credentials"}
                :as :json})
       :body :access_token (reset! token)))

(defn simp->trad [simp]
  (->> (client/get
             (str "http://api.microsofttranslator.com/v2/Http.svc/Translate?Text=%27" simp "%27&To=zh-CHT")
              {:headers {"Authorization" (str "Bearer " @token)}})
       :body
       (re-seq #"\p{script=Han}+")
       (interpose "; ")
       (apply str)
       ))


(defn update-line [line]
  (let [cols (.split line ",") l (count cols)]
    (if (or (= 1 l) (= 4 l))
      (apply str (interpose "," (list* (first cols) (simp->trad (first cols)) (rest cols))))
      line)))

(defn slurp-lines [f]
  (.split (slurp f) "\n"))

(defn spit-lines [f s]
  (spit f (apply str (interpose "\n" s))))

(defn add-traditional-chars [f]
  (set-token)
  (spit-lines f (pmap update-line (slurp-lines f)))
  (.exec (Runtime/getRuntime) (str "open " f))
  )

;(add-traditional-chars "csv/c.csv")
