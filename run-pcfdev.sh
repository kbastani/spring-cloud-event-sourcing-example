#!/usr/bin/env bash

cf dev start -m 9000
ssh vcap@local.pcfdev.io  password: vcap
https://console.local.pcfdev.io/2  admin/admin



brew install jq

#dnsmasq https://docs.pivotal.io/pcf-dev/work-offline.html
brew install dnsmasq
echo "address=/.local.pcfdev.io/192.168.11.11" >> /usr/local/etc/dnsmasq.conf
sudo brew services start dnsmasq
sudo launchctl stop homebrew.mxcl.dnsmasq
sudo launchctl start homebrew.mxcl.dnsmasq

# turbine-server
# zipkin-server

# config-service
config-service> mvn spring-boot:run
cf cups config-service -p '{"uri":"http://192.168.11.1:8888"}'
cf cups rabbitmq -p '{"uri":"amqp://user:pass@192.168.11.1:5672", "host":"192.168.11.1", "username":"user", "password":"pass"}'
cf cups zipkin-server -p '{"uri":"http://192.168.11.1:9411"}'

# push discovery-service

# create service
cf cs p-mysql 512mb account-db
cf cs p-mysql 512mb catalog-db
cf cs p-mysql 512mb inventory-db
cf cs p-mysql 512mb order-db
cf cs p-mysql 512mb shopping-cart-db
cf cs p-mysql 512mb user-db
cf cs p-redis shared-vm catalog-redis
cf cs p-rabbitmq standard rabbitmq



#
user-service
account service
catalog service
inventory service
order service
shopping-cart service
online-store-web

