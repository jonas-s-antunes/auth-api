(ns auth-api.handlers.auth
  (:require
   [auth-api.db :as db]
   [auth-api.util :as util]))


(defn auth-handler [request]
  (let [{:keys [email senha]} (:body request)]

    (cond
      (or (nil? email) (nil? senha))
      {:status 400
       :body {:success false
              :error {:code "missing_field"
                      :message "Os campos 'email' e 'senha' são obrigatórios."}}}

      (or (not (util/email-valido? email)) (not (util/senha-valida? senha)))
      {:status 400
       :body {:success false
              :error {:code "email_password_invalid"
                      :message "Email ou Password inválidos."}}}

      ;
      )))
