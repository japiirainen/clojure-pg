(ns auth-api.db
  (:require [buddy.hashers :refer [encrypt check]]
            [honeysql.core :as h]
            [honeysql.helpers :as hh]
            [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]))

(def db-config
  {:dbtype "postgresql"
   :dbname "auth_db"
   :host "localhost"
   :port 5432
   :user "postgres"
   :password "postgres"})

(def db (jdbc/get-datasource db-config))

(defn db-query [sql]
  (jdbc/execute! db sql
                 {:return-keys true
                  :builder-fn rs/as-unqualified-maps}))

(defn db-query-one [sql]
  (jdbc/execute-one! db sql
                     {:return-keys true
                      :builder-fn rs/as-unqualified-maps}))

(defn sanitize-user [user]
  (dissoc user :password))

(defn create-user [{:keys [email username password]}]
  (let [hashed-pwd (encrypt password)
        created-user (->
                      (hh/insert-into :users)
                      (hh/columns :email :username :password)
                      (hh/values [[email username hashed-pwd]])
                      h/format
                      db-query-one)
        sanitized-user (sanitize-user created-user)]
    sanitized-user))

(defn get-user-by-credentials
  [{:keys [username password]}]
  (let [user (-> (hh/select :*)
                 (hh/from :users)
                 (hh/where := :username username)
                 h/format
                 db-query-one)
        sanitized-user (sanitize-user user)]
    (if (and user (check password (:password user)))
      sanitized-user
      nil)))

(defn get-user-by-payload
  [{:keys [username]}]
  (let [user (-> (hh/select :*)
                 (hh/from :users)
                 (hh/where := :username username)
                 h/format
                 db-query-one)
        sanitized-user (sanitize-user user)]
    (if user
      sanitized-user
      nil)))

(defn get-all-users []
  (let [users (-> (hh/select :*)
                  (hh/from :users)
                  h/format
                  db-query)
        sanitized (map #(sanitize-user %) users)]
    sanitized))

(comment
  db
  (create-user {:username "user" :email "user@mail.com" :password "salasana"})
  (sanitize-user {:username "joona" :password "foobar"})
  (get-user-by-credentials {:username "user" :password "salasana"})
  (get-user-by-payload {:username "user"})
  (get-all-users))
