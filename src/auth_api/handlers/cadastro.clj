(ns auth-api.handlers.cadastro
  (:require
   [buddy.hashers :as hashers]
   [clojure.string :as str]
   [auth-api.db :as db]
   [auth-api.util :as util]))

(def ^:private PEPPER "ABACATE")

(defn email-liberado? [email]
  (empty? (db/select-email email)))

(defn ->credenciais [email senha]
  (let [senha-pepper (str senha PEPPER)
        senha-secreta (hashers/derive senha-pepper)]
    {:email email
     :senha-secreta senha-secreta}))

(defn ->dados-usuario [email]
  (let [nome (first (str/split email #"@"))]
    {:nome nome
     :email email}))

(defn cria-usuario [email senha]
  ;; Criar o hash da senha, usar ENV como PEPPER
  ;; Salvar, em algum lugar, e email e a senha hasheada
  (let [credenciais (->credenciais email senha)
        dados-usuario (->dados-usuario email)]
    (db/insert-email email)
    (db/insert-credenciais credenciais)
    (db/insert-usuario dados-usuario)
    {:status 201
     :body {:success true
            :data {:nome (:nome dados-usuario)
                   :email email}}}))

(defn cadastro-handler [request]
  (let [{:keys [email senha senha-confirmacao]} (:body request)]

    (cond
      (or (nil? email) (nil? senha) (nil? senha-confirmacao))
      {:status 400
       :body {:success false
              :error {:code "missing_field"
                      :message "Os campos 'email', 'senha' e 'senha-confirmacao' são obrigatórios."}}}

      (not (email-liberado? email))
      {:status 409
       :body {:success false
              :error {:code "email_already_exists"
                      :message "O email informado já está em uso."}}}

      (not= senha senha-confirmacao)
      {:status 400
       :body {:success false
              :error {:code "password_mismatch"
                      :message "As senhas não conferem."}}}

      (not (util/email-valido? email))
      {:status 400
       :body {:success false
              :error {:code "email_invalid"
                      :message "Email inválido."}}}

      (not (util/senha-valida? senha))
      {:status 400
       :body {:success false
              :error {:code "password_invalid"
                      :message "Senha inválida."}}}

      :else
      (cria-usuario email senha))))
