(ns auth-api.handlers
  (:require [auth-api.db :as db]
            [auth-api.jwt :refer [create-token]]))

(defn ping [_]
  {:status 200
   :body {:hello "world"
          :ping "pong"}})

(defn get-all-users [_]
  {:status 200
   :body {:users (db/get-all-users)}})

(defn register
  [{:keys [parameters]}]
  (let [data (:body parameters)
        user (db/create-user data)]
    {:status 201
     :body {:user user
            :token (create-token user)}}))

(defn login
  [{:keys [parameters]}]
  (let [data (:body parameters)
        user (db/get-user-by-credentials data)]
    (if (nil? user)
      {:status 404
       :body {:error "Invalid credentials"}}
      {:status 200
       :body {:user user
              :token (create-token user)}})))

(defn me
  [{:keys [parameters]}]
  (let [payload (:identity parameters)
        user (db/get-user-by-payload payload)]
    (if (nil? user)
      {:status 404
       :body {:error "Invalid credentials"}}
      {:status 200
       :body {:user user
              :token (create-token user)}})))
