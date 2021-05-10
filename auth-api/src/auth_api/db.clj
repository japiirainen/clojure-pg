(ns auth-api.db
  (:require [buddy.hashers :refer [encrypt check]]
            [honeysql.core :as h]
            [honeysql.helpers :as hh]
            [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]))

(def db-config
  {:dbtype "postgresql"
   :dbname "auth-api"
   :host "localhost"
   :user "postgres"
   :password "postgres"})

(def db (jdbc/get-datasource db-config))