#!/usr/bin/env bash
# Deploys the online store application to CF

echo '
   ____ _                 _   _   _       _   _
  / ___| | ___  _   _  __| | | \ | | __ _| |_(_)_   _____
 | |   | |/ _ \| | | |/ _` | |  \| |/ _` | __| \ \ / / _ \
 | |___| | (_) | |_| | (_| | | |\  | (_| | |_| |\ V /  __/
  \____|_|\___/ \__,_|\__,_| |_| \_|\__,_|\__|_| \_/ \___|
   ___        _    __ _ _   _
  / _ \ _   _| |_ / _(_) |_| |_ ___ _ __ ___
 | | | | | | | __| |_| | __| __/ _ \ ''__/ __|
 | |_| | |_| | |_|  _| | |_| ||  __/ |  \__ \
  \___/ \__,_|\__|_| |_|\__|\__\___|_|  |___/

'


cf target




cf cs p-mysql 100mb-dev account-db
cf cs p-mysql 100mb-dev  catalog-db
cf cs p-mysql 100mb-dev  inventory-db
cf cs p-mysql 100mb-dev  order-db
cf cs p-mysql 100mb-dev  shopping-cart-db
cf cs p-mysql 100mb-dev  user-db
cf cs p-mysql 100mb-dev  zipkin-db
cf cs p-redis shared-vm catalog-redis
cf cs p-rabbitmq standard rabbitmq



# call and wait
#cf create-service -c '{"git": { "uri": "https://github.com/myminseok/spring-cloud-event-sourceing-pcf-config.git", "repos": { "cook": { "pattern": "cook*", "uri": "https://github.com/myminseok/spring-cloud-event-sourceing-pcf-config.git" } } }, "count": 3 }' p-config-server standard config-service
# wait for initialize=> error
#cf cs p-service-registry standard discovery-service

cf push config-service
cf cups config-service -p '{"uri":"http://config-service.pcfdemo.net"}'

cf push discovery-service
cf cups discovery-service -p '{"uri":"http://discovery-service.pcfdemo.net"}'


#cf cs p-circuit-breaker-dashboard standard hystrix-dashboard


cf push turbine-server

cf push zipkin-server
cf cups zipkin-server -p '{"uri":"http://zipkin-server.pcfdemo.net"}'

cf push user-service
cf cups user-service -p '{"uri":"http://user-service.pcfdemo.net"}'
cf push edge-service
cf cups edge-service -p '{"uri":"http://edge-service.pcfdemo.net"}'
cf push account-service
cf push catalog-service
cf push inventory-service
cf push order-service
cf push shopping-cart-service
cf push online-store-web


