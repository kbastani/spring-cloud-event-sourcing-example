#!/usr/bin/env bash

cf dev start -m 9000
ssh vcap@local.pcfdev.io  password: vcap
https://console.local.pcfdev.io/2  admin/admin


# config service (localhost)
config-service> mvn spring-boot:run
# check http://192.168.11.1:8888/
cf cups config-service -p '{"uri":"http://192.168.11.1:8888/"}'

# discovery service (localhost)
discovery-service > mvn spring-boot:run
# check http://192.168.11.1:8761/
cf cups discovery-service -p '{"uri":"http://192.168.11.1:8761/"}'


# create service (pcfdev)
cf cs p-rabbitmq standard rabbitmq

## optionally use rabbitmq outside of PCF;
# cf cups rabbitmq -p '{"uri":"amqp://user:pass@192.168.11.1:5672", "host":"192.168.11.1", "username":"user", "password":"pass"}'


# turbine-server (pcfdev)
turbine-server> cf push
# check http://turbine-server.local.pcfdev.io/


# turbine-sample (pcfdev)
turbine-sample> cf push
curl http://turbine-sample.local.pcfdev.io/
# => response "ok"
# go to turbine-server http://turbine-server.local.pcfdev.io/  webpage and data should be displayed (via rabbitmq)
# =>  data: {"rollingCountFallbackFailure":0,"...


# hystrix-dashboard (localhost)
hystrix-dashboard> mvn spring-boot:run
# goto hystrix webpage:  http://192.168.11.1:6161/hystrix
# put turbine-server url: http://turbine-server.local.pcfdev.io/ then, monitor => turbine-sample should be monitored.


# remove turbine-sample
cf d turbine-sample


# zipkin server
cf cs p-mysql 512mb  zipkin-db
zipkin-server> cf push
# check http://zipkin-server.local.pcfdev.io/
cf cups zipkin-server -p '{"uri":"http://zipkin-server.local.pcfdev.io/"}'
# cf cups zipkin-server -p '{"uri":"http://192.168.11.1:9411"}'


# zipkin sample
zipkin-server> cf push
curl http://zipkin-sample.local.pcfdev.io/
curl http://zipkin-sample.local.pcfdev.io/call
# check zipkin server http://zipkin-server.local.pcfdev.io/ to see if 'zipkin-sample' listed.

# remove zipkin-sample app
cf d zipkin-sample




## create backend services (pcfdev)
cf cs p-redis shared-vm catalog-redis
cf cs p-mysql 512mb account-db
cf cs p-mysql 512mb catalog-db
cf cs p-mysql 512mb inventory-db
cf cs p-mysql 512mb order-db
cf cs p-mysql 512mb shopping-cart-db
cf cs p-mysql 512mb user-db


user-service> cf push
cf cups user-service -p '{"uri":"http://user-service.local.pcfdev.io/"}'

edge-service> cf push
cf cups edge-service -p '{"uri":"http://edge-service.local.pcfdev.io/"}'


account service
catalog service
inventory service
order service
shopping-cart service
online-store-web

# check if all app is registered to discovery-service: http://192.168.11.1:8761/

# go to online-store web http://online-store-web2.local.pcfdev.io/
# login with user/password

# see if hystrix-dashboard is working
http://192.168.11.1:6161/hystrix/monitor?stream=http%3A%2F%2Fturbine-server.local.pcfdev.io%2F

see if zipkin-server is working
