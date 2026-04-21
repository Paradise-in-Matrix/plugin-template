(ns build
  (:require ["fs" :as fs]
            ["path" :as path]
            [clojure.edn :as edn]))

(defn build []
  (let [metadata-str (fs/readFileSync "manifest.edn" "utf8")
        metadata     (edn/read-string metadata-str)
        ui-code      (fs/readFileSync "src/ui.cljs" "utf8")
        worker-code  (fs/readFileSync "src/worker.cljs" "utf8")

        payload (assoc metadata
                       :ui-form-str (str "(do\n" ui-code "\n)")
                       :worker-form-str (str "(do\n" worker-code "\n)"))

        dist-dir "dist"
        out-file (path/join dist-dir "plugin.edn")]

    (when-not (fs/existsSync dist-dir)
      (fs/mkdirSync dist-dir))

    (fs/writeFileSync out-file (pr-str payload))
    (println "Plugin packed successfully to" out-file)))

(build)
