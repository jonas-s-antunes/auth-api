(ns auth-api.core
  (:require
   [ring.adapter.jetty :refer [run-jetty]]
   [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
   [compojure.core :refer [defroutes GET POST]]
   [compojure.route :as route]
   [buddy.hashers :as hashers]
   [clojure.string :as str]
   [auth-api.db :as db]))

(def ^:private PEPPER "ABACATE")

(defn email-liberado? [email]
  (empty? (db/select-email email)))

(defn email-valido? [email]
  ;; ^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$
  (boolean (re-matches #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$" email)))

(defn senha-valida? [senha]
  ;; ^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[$*&@#])[0-9a-zA-Z$*&@#]{6,}
  (boolean (re-matches #"^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[$*&@#])[0-9a-zA-Z$*&@#]{6,}" senha)))


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

      (not (email-valido? email))
      {:status 400
       :body {:success false
              :error {:code "email_invalid"
                      :message "Email inválido."}}}

      (not (senha-valida? senha))
      {:status 400
       :body {:success false
              :error {:code "password_invalid"
                      :message "Senha inválida."}}}

      :else
      (cria-usuario email senha))))

(defn usuario-handler [request]
  (prn "Consultando os Usuarios.")
  (db/select-usuarios)
  {:status 200
   :body {:message "Algo foi consultado!"}})

(defroutes app-routes
  (GET "/health" [] "API OK")
  (GET "/" [] "API OK")
  (POST   "/api/cadastro" request (cadastro-handler request))
  (POST   "/api/login" [] "login OK")
  (GET    "/api/usuario" request (usuario-handler request))
  (POST   "/api/refresh" [] "refresh OK")
  (POST   "/api/logout" [] "logout OK")

  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-json-body {:keywords? true})
      wrap-json-response))

(defn -main []
  (run-jetty app {:port 3000 :join? false}))
