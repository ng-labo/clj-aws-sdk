# clj-aws-sdk

A Clojure library designed to aws sdk well.

- Amazon SNS

## Usage

example

```clojure
clj-aws-sdk.core=> (clj-aws-sdk.example.sns/send-message "+8190********" "Hello world")
#object[com.amazonaws.services.sns.model.PublishResult 0x13dd0f69 {MessageId: 78560646-e71b-58f4-9db3-51599c867779}]
nil
clj-aws-sdk.core=>
```

