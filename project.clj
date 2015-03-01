(defproject fluge "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0-alpha5"]
                 [org.clojure/clojurescript "0.0-2913"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [quil "2.2.5"]
                 [org.clojure/data.csv "0.1.2"]]

  :plugins [[lein-cljsbuild "1.0.4"]]
  :hooks [leiningen.cljsbuild]

  :cljsbuild {:builds [{:source-paths ["src"]
                        :compiler {:output-to "js/main.js"
                                   :output-dir "out"
                                   :main "fluge.core"
                                   :optimizations :none
                                   :pretty-print true}}]})
