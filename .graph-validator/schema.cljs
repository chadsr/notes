(ns schema
  "Enforce schema constraints on page types"
  (:require [clojure.test :refer [deftest is]]
            [logseq.graph-validator.state :as state]
            [logseq.db.rules :as rules]
            [datascript.core :as d]))

(defn- get-property-names-for-type [type']
  (->> (d/q '[:find (pull ?b [:block/properties])
              :in $ ?type %
              :where (page-property ?b :type ?type)]
            @state/db-conn
            type'
            (vals rules/query-dsl-rules))
       (map (comp :block/properties first))
       (mapcat keys)
       set
       ;; Remove built ins
       ((fn [x] (apply disj x [:id :title :alias :tags :type])))))
(defn- get-properties-for-type [type']
  (->> (d/q '[:find (pull ?b [:block/properties])
              :in $ ?type %
              :where (page-property ?b :type ?type)]
            @state/db-conn
            type'
            (vals rules/query-dsl-rules))
       (map (comp :block/properties first))))

(deftest class-schema
  (let [props (get-properties-for-type "Class")]
    (is (empty? (remove :url props))
        "All classes should have a :url property")))

(deftest note-schema
  (let [props (get-properties-for-type "Note")]
    (is (empty? (remove :description props))
        "All notes should have a :description property")
    (is (empty? (remove :datecreated props))
        "All notes should have a :datecreated property")
    (is (empty? (remove :topic props))
        "All notes should have a :topic property")))

(deftest journal-schema
  (let [props (get-properties-for-type "Journal")]
    (is (empty? (remove :datecreated props))
        "All journals should have a :datecreated property")))