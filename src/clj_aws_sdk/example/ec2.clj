(ns clj-aws-sdk.example.ec2
  (:import [com.amazonaws.services.ec2.model
              CreateSecurityGroupRequest
              DescribeSecurityGroupsRequest
              DeleteSecurityGroupRequest
              Filter
              AuthorizeSecurityGroupEgressRequest
              AuthorizeSecurityGroupIngressRequest
              IpPermission
              IpRange
              DescribeKeyPairsRequest
              RunInstancesRequest
              DescribeInstancesRequest
              StopInstancesRequest
              TerminateInstancesRequest
              InstanceType])
  (:import [com.amazonaws.services.ec2 AmazonEC2ClientBuilder])
)

; Ubuntu Server 20.04 LTS (HVM), SSD Volume Type -(64bit x86) 
(def image-id "ami-0c1ac8728ef7f87a4")

(defn create-securitygroup [group-name description]
  (let [acli (AmazonEC2ClientBuilder/defaultClient)
        scg-result (.createSecurityGroup acli 
                     (doto (CreateSecurityGroupRequest.)
                           (.withGroupName group-name)
                           (.withDescription description)))
        iprange (IpRange.)]
    (.setCidrIp iprange "0.0.0.0/0")
    (let [ip-permission (doto (IpPermission.)
                              (.setIpv4Ranges [iprange]) 
                              (.withIpProtocol "tcp")
                              (.withFromPort (int 22))
                              (.withToPort (int 22)))]
      (let [asg-ingress (doto (AuthorizeSecurityGroupIngressRequest.)
                              (.withGroupName group-name)
                              (.setIpPermissions
                                [ip-permission ]))]
        (.authorizeSecurityGroupIngress acli asg-ingress)
))))
                       
(defn describe-securitygroup []
  (let [acli (AmazonEC2ClientBuilder/defaultClient)
        req (DescribeSecurityGroupsRequest.)]
    (map (fn [sg]
           { :group-id (.getGroupId sg)
             :group-name (.getGroupName sg)
             :vpc-id (.getVpcId sg)
             :description (.getDescription sg) })
         (.getSecurityGroups (.describeSecurityGroups acli req)))))

(defn delete-securitygroup [group-id]
  (let [acli (AmazonEC2ClientBuilder/defaultClient)
        req (doto (DeleteSecurityGroupRequest.)
                  (.withGroupId group-id))]
    (.deleteSecurityGroup acli req)))

(defn describe-keypairs []
  (let [acli (AmazonEC2ClientBuilder/defaultClient)
        req (DescribeKeyPairsRequest.)]
    (map (fn [kpi]
          { :name (.getKeyName kpi)
            :finger-print (.getKeyFingerprint kpi)
          })
         (.getKeyPairs (.describeKeyPairs acli req)))))

(defn- read-reservations [response]
    (map
      (fn [reservation]
         (reduce conj (map (fn [instance]
                { :instance-id (.getInstanceId instance)
                  :public-ip (.getPublicIpAddress instance)
                  :state (.getName (.getState instance))
                })
              (.getInstances reservation))))
      (.getReservations response)))

(defn run-instance [image-id keypair-name group-name]
  (let [acli (AmazonEC2ClientBuilder/defaultClient)
        req (doto (RunInstancesRequest.)
                  (.withImageId image-id)
                  (.withInstanceType "t1.micro") ;TODO lookup from T1Micro directly
                  (.withMinCount (int 1))
                  (.withMaxCount (int 1))
                  (.withKeyName keypair-name)
                  (.setSecurityGroups [group-name]))]
    (let [response (.runInstances acli req)]
      (.getInstanceId (first (.getInstances (.getReservation response)))))))

(defn describe-instances []
  (let [acli (AmazonEC2ClientBuilder/defaultClient)
        req (DescribeInstancesRequest.)]
    (let [response (.describeInstances acli req)]
      (read-reservations response))))

(defn stop-instance [instance-id]
  (let [acli (AmazonEC2ClientBuilder/defaultClient)
        req (doto (StopInstancesRequest.)
                  (.setInstanceIds [instance-id]))]
    (let [response (.stopInstances acli req)]
      response)))

(defn terminate-instance [instance-id]
  (let [acli (AmazonEC2ClientBuilder/defaultClient)
        req (doto (TerminateInstancesRequest.)
                  (.setInstanceIds [instance-id]))]
    (let [response (.terminateInstances acli req)]
      response)))
