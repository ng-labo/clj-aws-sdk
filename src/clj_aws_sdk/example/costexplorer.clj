(ns clj-aws-sdk.example.costexplorer
  (:import [com.amazonaws.services.costexplorer AWSCostExplorerClientBuilder])
  (:import [com.amazonaws.services.costexplorer.model
                                                GetCostAndUsageRequest
                                                DateInterval
                                                GroupDefinition
                                                Granularity])
  )

(def METRICS "BlendedCost")

(defn- parse-metricvalue [total]
  (if (some? total)
      {:amount (.getAmount total)
       :unit (.getUnit total)})
  )

(defn- parse-groups [groups]
  (map (fn[group] {:keys (vec (.getKeys group))
                   :metrics (parse-metricvalue
                              (.get (.getMetrics group) METRICS))
                   }) groups))

(defn- results-bytime [results]
  (map (fn[result]
         {:time-period (.getStart (.getTimePeriod result))
          :total (parse-metricvalue (.get (.getTotal result) METRICS))
          :group (parse-groups (.getGroups result))
         })
  results
  ))

(defn query [start-date end-date & options]
  (:pre (and (re-find (re-pattern "\\d\\d\\d\\d-\\d\\d-\\d\\d") start-date)
             (re-find (re-pattern "\\d\\d\\d\\d-\\d\\d-\\d\\d") end-date)))
  (let [acli (AWSCostExplorerClientBuilder/defaultClient)]
    (let [req (GetCostAndUsageRequest.)]
      (.setTimePeriod req (doto (DateInterval.)
                                (.withStart start-date)
                                (.withEnd end-date)))
      (.setGranularity req (.toString Granularity/DAILY))
      (.setMetrics req [METRICS])
      (if (get (set options) :usage-type)
          (.setGroupBy req [(doto (GroupDefinition.)
                                  (.withType "DIMENSION")
                                  (.withKey "USAGE_TYPE"))]))
      (results-bytime (.getResultsByTime (.getCostAndUsage acli req))))))
