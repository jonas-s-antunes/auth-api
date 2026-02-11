(ns auth-api.db)

;; Simulando um banco de dados

#_{:emails [{:email "email@email.com"}]
   :credenciais [{:email "email@email.com"
                  :senha-secreta "@BLA%123"}]
   :usuarios [{:email "email@email.com"
               :nome "email"}]}

(def ^:private DB (atom {:emails []
                         :credenciais []
                         :usuarios []}))

(defn select-email [email]
  (filter #(= email (:email %)) (:emails @DB)))

(defn select-usuarios []
  (prn @DB)
  (:usuarios @DB))

(defn insert-email [email]
  (swap! DB #(update % :emails merge {:email email})))

(defn insert-credenciais [credenciais]
  (swap! DB #(update % :credenciais merge credenciais)))

(defn insert-usuario [dados-usuario]
  (swap! DB #(update % :usuarios merge dados-usuario)))

;;;
