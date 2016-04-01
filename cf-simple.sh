#!/usr/bin/env bash

cf cups config-service -p '{"uri":"http://config-service-coercible-intercrop.cfapps.io"}'
cf cups discovery-service -p '{"uri":"http://discovery-service-unschematised-estheticism.cfapps.io"}'
cf cups user-service -p '{"uri":"http://user-service-unconducted-shindig.cfapps.io"}'
cf cups edge-service -p '{"uri":"http://edge-service-colorable-praseodymium.cfapps.io"}'
