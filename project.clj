(defproject snake "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [jline "2.14.2"]
                 [org.clojure/spec.alpha "0.2.176"]
                 [org.clojure/core.match "0.3.0-alpha5"]]
  :repl-options {:init-ns snake.game}
  :main snake.game
  :profiles {:uberjar {:aot :all}})
