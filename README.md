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
clj-aws-sdk.core=> (map #(println (keys %)) (describe-securitygroup))
(:group-id :vpc-id :description)
(:group-id :vpc-id :description)
(nil nil)
clj-aws-sdk.core=> (map #(println (keys %)) (describe-keypairs))
(:name :finger-print)
(nil)
clj-aws-sdk.core=>
```
