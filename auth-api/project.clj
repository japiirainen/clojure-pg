(defproject auth-api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.postgresql/postgresql "42.2.2"]
                 [seancorfield/next.jdbc "1.1.613"]
                 [buddy/buddy-auth "2.2.0"]
                 [buddy/buddy-hashers "1.6.0"]
                 [http-kit/http-kit "2.4.0"]
                 [metosin/reitit "0.5.5"]
                 [honeysql/honeysql "1.0.444"]]
  :repl-options {:init-ns auth-api.core})
