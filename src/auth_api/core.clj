(ns auth-api.core
  (:require
   [ring.adapter.jetty :refer [run-jetty]]
   [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
   [compojure.core :refer [defroutes GET POST]]
   [compojure.route :as route]
   [auth-api.handlers.cadastro :as cadastro]
   [auth-api.handlers.auth :as auth]
   [auth-api.handlers.usuario :as usuario]))

(defroutes app-routes
  (GET "/health" [] "API OK")
  (GET "/" [] "API OK")
  (POST   "/api/cadastro" request (cadastro/cadastro-handler request))
  (POST   "/api/login"    request (auth/auth-handler request))
  (GET    "/api/usuario"  request (usuario/usuario-handler request))
  (POST   "/api/refresh" [] "refresh OK")
  (POST   "/api/logout"  [] "logout OK")

  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-json-body {:keywords? true})
      wrap-json-response))

(defn -main []
  (run-jetty app {:port 3000 :join? false}))
