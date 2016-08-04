# Spring Cloud Event Sourcing Example
original source code is here https://github.com/kbastani/spring-cloud-event-sourcing-example

this project added following features to the original project:
* hystrix using stream(rabbitmq)
* zipkin tracing using HTTP
* can deploy to pcfdev, a lightweight Pivotal Cloud Foundry (PCF) for desktop (https://network.pivotal.io/products/pcfdev)

### this demo uses backend services from PaaS(pcfdev): 
you don't need to install seperately!
* MySQL - RDBMS
* Redis - Key/value Store
* RabbitMQ- messaging

### H/W requirement
* requires 9GB+ memory for pcfdev
* 1G memory for config-server, discovery-server, hystrix-dashboard at local host machine.

### internet connection is mandatory for:
* pcfdev DNS lookup: local.pcfdev.io -> 192.168.11.11
* demo app looks up some javascript from internet.

### screen shot
![hystrix dashboard](hystrix.png)
![zipkin dashboard](zipkin.png)
![eureka dashboard](eureka.png)
![rabbitmq dashboard](rabbitmq.png)


# step by step installation guide

1. setup pcfdev

  you need to setup virtualbox vm with 9GB+ memory, otherwise you might get "Insufficient Resource" error
  ```
  cf dev start -m 9000
  https://console.local.pcfdev.io/2  admin/admin
  ```
1. git clone and compile
  
  ```
  git clone https://github.com/myminseok/spring-cloud-event-sourcing-example
  cd spring-cloud-event-sourcing-example
  git checkout pcfdev
  mvn clean package -DskipTests
  
  ```
  
  checkout config repo
  ```
  git clone https://github.com/myminseok/spring-cloud-event-sourceing-pcf-config
  
  ```
  
1. run config service (localhost)
  
  specify you just cloned 'spring-cloud-event-sourceing-pcf-config''folder's absolute path to 'CONFIG_REPO_PATH' env variable.
  ```
  $ config-service > mvn spring-boot:run -DCONFIG_REPO_PATH=/path/to/spring-cloud-event-sourceing-pcf-config

  * check http://192.168.11.1:8888/application/cloud
    should show something "application.yml, application-cloud.yml#cloud" in the response.
  
  $  cf cups config-service -p '{"uri":"http://192.168.11.1:8888/"}'
  ```
1. discovery service (localhost)

  ```
  $  discovery-service > mvn spring-boot:run

  * check http://192.168.11.1:8761/
  $  cf cups discovery-service -p '{"uri":"http://192.168.11.1:8761/"}'
  ```
1. create service (pcfdev)

  ```
  cf cs p-rabbitmq standard rabbitmq
  * check rabbitmq management dashboard
    apps manager (https://console.local.pcfdev.io/2) > pcfdev-space (left memu)> services tab> rabbitmq > manage click
    https://rabbitmq-management.local.pcfdev.io
  * altenatevely, you can use rabbitmq outside of PCF;
   setup rabbitmq( brew install rabbitmq)
   cf cups rabbitmq -p '{"uri":"amqp://user:pass@192.168.11.1:5672", "host":"192.168.11.1", "username":"user", "password":"pass"}'
   
  ```
  
1. turbine-server (pcfdev)
 
  ``` 
  turbine-server> cf push
  # check http://turbine-server.local.pcfdev.io/
  ```
1. turbine-sample (pcfdev)
 
  ```
  turbine-sample> cf push
  curl http://turbine-sample.local.pcfdev.io/
    => response "ok"
  # go to turbine-server http://turbine-server.local.pcfdev.io/  webpage and data should be displayed (via rabbitmq)
   =>  data: {"rollingCountFallbackFailure":0,"...
  ```
1. hystrix-dashboard (localhost)
 
  ```
  hystrix-dashboard> mvn spring-boot:run
  # goto hystrix webpage:  http://192.168.11.1:6161/hystrix
  # put turbine-server url: http://turbine-server.local.pcfdev.io/ then, monitor => turbine-sample should be monitored.
  ```
1. remove turbine-sample
 
  ```
  cf d turbine-sample
  ```
1. zipkin server
 
  ```
  cf cs p-mysql 512mb  zipkin-db
  zipkin-server> cf push
  # check http://zipkin-server.local.pcfdev.io/
  cf cups zipkin-server -p '{"uri":"http://zipkin-server.local.pcfdev.io/"}'
  # cf cups zipkin-server -p '{"uri":"http://192.168.11.1:9411"}'
  ```
1. zipkin sample
 
  ```
  zipkin-server> cf push
  curl http://zipkin-sample.local.pcfdev.io/
  curl http://zipkin-sample.local.pcfdev.io/call
  # check zipkin server http://zipkin-server.local.pcfdev.io/ to see if 'zipkin-sample' listed.
  ```
1. remove zipkin-sample app
  
  ```
  cf d zipkin-sample
  ```
1. create backend services (pcfdev)
  
  ```
  cf cs p-redis shared-vm catalog-redis
  cf cs p-mysql 512mb account-db
  cf cs p-mysql 512mb catalog-db
  cf cs p-mysql 512mb inventory-db
  cf cs p-mysql 512mb order-db
  cf cs p-mysql 512mb shopping-cart-db
  cf cs p-mysql 512mb user-db
  ```
1. user-service
  
  ```
  user-service> cf push
  cf cups user-service -p '{"uri":"http://user-service.local.pcfdev.io/"}'
  ```
1. edge-service
  
  ```
  edge-service> cf push
  cf cups edge-service -p '{"uri":"http://edge-service.local.pcfdev.io/"}'
  ```
1. deploy other apps
  
  ```
  account service
  catalog service
  inventory service
  order service
  shopping-cart service
  online-store-web
  ```
1. final check
  
  check if all app is registered to discovery-service: http://192.168.11.1:8761/
  go to online-store web http://online-store-web2.local.pcfdev.io/
  login with user/password
1. see if hystrix-dashboard is working
 
  ```
  http://192.168.11.1:6161/hystrix/monitor?stream=http%3A%2F%2Fturbine-server.local.pcfdev.io%2F
  ```
1. see if zipkin-server is working
  
  ```
  http://zipkin-server.local.pcfdev.io/
  ```


# trouble shooting

1. ssh into pcfdev
  
  ```
ssh vcap@local.pcfdev.io  password: vcap
  ```
1. login to pcfdev apps manager
 
  ```
  https://console.local.pcfdev.io/2  admin/admin
  ```
1. spring boot 1.4 might not work in PCF with initalization error.

  bump down spring-boot version to 1.3.6.RELEASE in this case.