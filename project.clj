(defproject six-degrees-of-kevin-bacon "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-cljsbuild "1.0.6"]]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.slf4j/slf4j-simple "1.7.12"]
                 [org.clojure/data.json "0.2.6"]
                 [me.raynes/fs "1.4.6"]
                 [org.clojure/clojurescript "0.0-3308"]]
  :main ^:skip-aot six-degrees-of-kevin-bacon.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :cljsbuild {:builds [{:source-paths ["src-cljs"],
    :compiler {:pretty-printer true,
      :output-to "www/js/main.js",
      :optimizations :whitespace}}]})
