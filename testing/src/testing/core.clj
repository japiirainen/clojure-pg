(ns testing.core
  (:require [malli.core :as m]))

(m/validate int? 1)