(defproject auth-api "0.1.0-SNAPSHOT"
  :description "API simples de autenticacao"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [ring/ring-core "1.11.0"]
                 [ring/ring-json "0.5.1"]
                 [ring/ring-jetty-adapter "1.11.0"]
                 [buddy/buddy-hashers "1.4.0"]
                 [compojure "1.7.0"]
                 [clj-http "3.12.3"]]
  :main auth-api.core)
