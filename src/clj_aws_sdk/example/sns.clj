(ns clj-aws-sdk.example.sns
  (:import [com.amazonaws.services.sns AmazonSNSClient])
  (:import [com.amazonaws.services.sns.model PublishRequest])
  (:import [com.amazonaws.services.sqs.model MessageAttributeValue])
  (:import [java.util HashMap])
)

(defn send-message
  [phone-number message]
  (let [snsClient (AmazonSNSClient.)]
    (let [result (.publish snsClient 
      (doto (PublishRequest.)
            (.withMessage message)
            (.withPhoneNumber phone-number)
            (.withMessageAttributes(HashMap.))))]
      (println result))))


