(ns ^:figwheel-no-load clojurebeer.dev
  (:require
    [clojurebeer.core :as core]
    [devtools.core :as devtools]))

(devtools/install!)

(enable-console-print!)

(core/init!)
