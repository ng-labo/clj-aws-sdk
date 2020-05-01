# clj-aws-sdk

A Clojure library designed to aws sdk well.

- Amazon SNS
- ec2

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
#object[com.amazonaws.services.ec2.model.StopInstancesResult 0x213bc663 "{StoppingInstances: [{CurrentState: {Code: 80,Name: stopped},InstanceId: i-0079b2025d22c0157,PreviousState: {Code: 80,Name: stopped}}]}"]
clj-aws-sdk.core=> (describe-instances)
({:instance-id "i-0079b2025d22c0157", :state "stopped"})
clj-aws-sdk.core=> (terminate-instance "i-0079b2025d22c0157")
#object[com.amazonaws.services.ec2.model.TerminateInstancesResult 0x4a20061d "{TerminatingInstances: [{CurrentState: {Code: 48,Name: terminated},InstanceId: i-0079b2025d22c0157,PreviousState: {Code: 80,Name: stopped}}]}"]
clj-aws-sdk.core=> (describe-instances)
({:instance-id "i-0079b2025d22c0157", :state "terminated"})

```
