# clj-aws-sdk

A Clojure library designed to aws sdk well.

- Amazon SNS
- ec2
- Amazon SQS
- CostExplorer

## Usage

SMS

```clojure
clj-aws-sdk.core=> (clj-aws-sdk.example.sns/send-message "+8190********" "Hello world")
#object[com.amazonaws.services.sns.model.PublishResult 0x13dd0f69 {MessageId: 78560646-e71b-58f4-9db3-51599c867779}]
nil
clj-aws-sdk.core=>
```

ec2

```clojure
clj-aws-sdk.core=> (use 'clj-aws-sdk.example.ec2)
nil
clj-aws-sdk.core=> (map #(get % :group-name) (describe-securitygroup))
("launch-wizard-1" "default")
clj-aws-sdk.core=> (map #(get % :name) (describe-keypairs))
("my-keypair-1")
clj-aws-sdk.core=> (run-instance image-id "my-keypair-1" "launch-wizard-1")
"i-0079b2025d22c0157"
clj-aws-sdk.core=> (describe-instances)
({:instance-id "i-0079b2025d22c0157", :state "pending"})
clj-aws-sdk.core=> (stop-instance "i-0079b2025d22c0157")
{:instance-id "i-0079b2025d22c0157", :state "stopped"}
clj-aws-sdk.core=> (describe-instances)
({:instance-id "i-0079b2025d22c0157", :state "stopped"})
clj-aws-sdk.core=> (terminate-instance "i-0079b2025d22c0157")
{:instance-id "i-0079b2025d22c0157", :state "terminated"}
clj-aws-sdk.core=> (describe-instances)
({:instance-id "i-0079b2025d22c0157", :state "terminated"})

```

SQS

```clojure
clj-aws-sdk.core=> (use 'clj-aws-sdk.example.sqs)
nil
clj-aws-sdk.core=> (def queue-name "My-Test-Queue-1")
#'clj-aws-sdk.core/queue-name
clj-aws-sdk.core=> (create-queue queue-name)
nil
clj-aws-sdk.core=> (list-queue)
["https://sqs.ap-northeast-1.amazonaws.com/389561742621/My-Test-Queue-1"]
clj-aws-sdk.core=> (send-queue-message queue-name "Hello SQS!")
#object[com.amazonaws.services.sqs.model.SendMessageResult 0x3adc41b9 "{MD5OfMessageBody: 62ff2d262678b73fdc2e47c7ea60f87f,MessageId: 97f7f04f-1af4-4d40-9a4$-39c4c8e2d1c8,}"]
clj-aws-sdk.core=> (receive-message queue-name)
("Hello SQS!")
```

CostExplorer

```clojjure
clj-aws-sdk.core=> (use 'clj-aws-sdk.example.costexplorer)
nil
clj-aws-sdk.core=> (map #(println (get % :time-period) (get % :total)) (query "2020-04-26" "2020-05-03"))
2020-04-26 {:amount 0.3936429385, :unit USD}
2020-04-27 {:amount 0, :unit USD}
2020-04-28 {:amount 0, :unit USD}
2020-04-29 {:amount 0, :unit USD}
2020-04-30 {:amount 0.0196199884, :unit USD}
2020-05-01 {:amount 0.0408245384, :unit USD}
2020-05-02 {:amount 0.41, :unit USD}
(nil nil nil nil nil nil nil)

```
