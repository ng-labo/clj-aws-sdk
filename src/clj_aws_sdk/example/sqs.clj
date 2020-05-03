(ns clj-aws-sdk.example.sqs
  (:import [com.amazonaws.services.sqs AmazonSQSClientBuilder])
  (:import [com.amazonaws.services.sqs.model AmazonSQSException
                                             CreateQueueRequest
                                             SendMessageRequest])
  )

(def test-queue-name "My-Test-Queue")

(defn- call-api [acli req]
  (try
    (let [result (.createQueue acli req)])
    (catch AmazonSQSException e
      (if (not= (.getErrorCode e) "QueueAlreadyExists") (.getErrorCode e)))))

(defn create-queue [queue-name]
  (let [acli (AmazonSQSClientBuilder/defaultClient)]
    (let [req (doto (CreateQueueRequest. queue-name)
                    (.addAttributesEntry "DelaySeconds" "60")
                    (.addAttributesEntry "MessageRetentionPeriod" "86400"))]
      (call-api acli req)
  )))

(defn list-queue []
  (let [acli (AmazonSQSClientBuilder/defaultClient)]
    (let [lq-result (.listQueues acli)]
      (.getQueueUrls lq-result))))

(defn get-queue-url [queue-name]
  (let [acli (AmazonSQSClientBuilder/defaultClient)]
    (.getQueueUrl (.getQueueUrl acli queue-name))))

(defn delete-queue [queue-name]
  (let [acli (AmazonSQSClientBuilder/defaultClient)]
    (.deleteQueue acli queue-name)))
  
(defn send-queue-message [queue-name message]
  (let [acli (AmazonSQSClientBuilder/defaultClient)]
    (let [req (doto (SendMessageRequest.)
                    (.withQueueUrl(get-queue-url queue-name))
                    (.withMessageBody message)
                    (.withDelaySeconds (int 5)))]
      (.sendMessage acli req))))

(defn delete-messages [acli queue-url messages]
  (loop [msg messages]
    (if (some? (first msg))
        (let [m (first msg)]
          (.deleteMessage acli queue-url (.getReceiptHandle m)))
          (recur (next msg)))))

(defn receive-message [queue-name]
  (let [acli (AmazonSQSClientBuilder/defaultClient)
        q-url (get-queue-url queue-name)]
    (let [messages (.getMessages (.receiveMessage acli q-url))]
      (delete-messages acli q-url messages)
      (map #(.getBody %) messages))))

