(ns auth-api.handlers.usuario
  (:require
   [auth-api.db :as db]))

(defn usuario-handler [request]
  (prn "Consultando os Usuarios.")
  (db/select-usuarios)
  {:status 200
   :body {:message "Algo foi consultado!"}})
