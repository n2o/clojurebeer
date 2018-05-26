(ns clojurebeer.core
  (:require [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary :include-macros true]
            [accountant.core :as accountant]
            [clojure.string :as str]))

;; -------------------------
;; Views

(def song
  "Say you, say me
  Say it for always
  That's the way it should be
  Say you, say me
  Say it together
  Naturally

  I had a dream I had an awesome dream
  People in the park playing games in the dark
  And what they played was a masquerade

  And from behind of walls of doubt a voice was crying out

  Say you, say me
  Say it for always
  That's the way it should be
  Say you, say me

  Say it together
  Naturally

  As we go down life's lonesome highway
  Seems the hardest thing to do is to find a friend or two


  A helping hand - Some one who understands
  That when you feel you've lost your way
  You've got some one there to say \"I'll show you\"

  Say you, say me
  Say it for always

  That's the way it should be
  Say you, say me
  Say it together
  Naturally

  So you think you know the answers - Oh no
  'Couse the whole world has got you dancing
  That's right - I'm telling you
  It's time to start believing - Oh yes
  Believing who you are: You are a shining star
  Say you, say me
  Say it for always
  That's the way it should be
  Say you, say m
  Say it together
  Naturally
  Say it together... naturally")

(def slider (atom 0))
(def non-empty-words
  (remove empty?
          (str/split song #"\s")))

(def unique-words
  (set
   (map str/lower-case non-empty-words)))

(def mapped-words
  (into {} (for [w unique-words]
             [w (rand-int 100)])))

(defn beerify []
  (for [w non-empty-words]
    (let [u (str/lower-case w)
          r (mapped-words u)]
      (if (< r @slider)
        (if (= u w) "🍺" "🍻")
        w))))

(defn home-page []
  [:div
   [:h2 "Welcome to clojurebeer"]
   [:input {:type "range"
            :min 0
            :max 100
            :onChange #(reset! slider (.. % -target -value))}]
   [:p (str/join " " (beerify))]
   [:img {:src "img/richie.jpg"}]])


;; -------------------------
;; Routes

(defonce page (atom #'home-page))

(defn current-page []
  [:div [@page]])

(secretary/defroute "/" []
  (reset! page #'home-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
   {:nav-handler
    (fn [path]
      (secretary/dispatch! path))
    :path-exists?
    (fn [path]
      (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
