(ns auth-api.core
  (:require [auth-api.routes :refer [ping-route auth-routes]]
            [muuntaja.core :as m]
            [org.httpkit.server :refer [run-server]]
            [reitit.coercion.schema]
            [reitit.ring :as ring]
            [reitit.ring.coercion :refer [coerce-exceptions-middleware
                                          coerce-request-middleware
                                          coerce-response-middleware]]
            [reitit.ring.middleware.exception :refer [exception-middleware]]
            [reitit.ring.middleware.muuntaja :refer [format-request-middleware
                                                     format-response-middleware
                                                     format-negotiate-middleware]]))

(defonce server
  (atom nil))

(def app
  (ring/ring-handler
   (ring/router
    [["/api"
      ping-route
      auth-routes]]
    {:data {:coercion reitit.coercion.schema/coercion
            :muuntaja m/instance
            :middleware [format-negotiate-middleware
                         format-response-middleware
                         exception-middleware
                         format-request-middleware
                         coerce-exceptions-middleware
                         coerce-request-middleware
                         coerce-response-middleware]}})
   (ring/routes
    (ring/redirect-trailing-slash-handler)
    (ring/create-default-handler
     {:not-found (constantly {:status 404 :body {:error "Route not found"}})}))))

(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)))

(defn -main []
  (println "Server running on localhost:3333")
  (reset! server (run-server app {:port 3333})))

(comment
  (+ 1 1)
  (stop-server)
  (-main))
