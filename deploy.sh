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

filter_arr() {
    arr=($@)
    arr=(${arr[@]:1})
    filtered_dirs=($(for i in ${arr[@]}
        do echo $i
    done | grep -v $1))
}

include_arr() {
    arr=($@)
    arr=(${arr[@]:1})
    included_dirs=($(for i in ${arr[@]}
        do echo $i
    done | grep $1))
}

# Get manifest files for app deployments and services
declare -a dirs
i=1
for d in `find */* -name manifest.yml -type f`
do
    dirs[i++]=$(echo "${d%/}" | sed -e "s/\/manifest.yml//g")
done

# This is the list of backing services for the online store
backing_services='discovery-service\|config-service\|user-service\|edge-service'
service_instances=(discovery-service config-service user-service edge-service)

# Filter out microservices
include_arr $backing_services ${dirs[@]}

# Filter out backing services
filter_arr $backing_services ${dirs[@]}

echo 'Deploying backing services...\n---'

PROJECT_DIR=$PWD

for D in ${service_instances[@]}
do
    # Find the existing app
    app=$(echo $(cf app ${D} --guid) | grep -v 'FAILED')
    cd "$PROJECT_DIR/${D}"

    if [ -z "${app}" ]
    then
        echo "Creating new app '${D}'..."
        cf push
    else
        # Be sure to use the already existing route
        echo "Getting existing route for '${D}'..."
        route=$(cf curl /v2/apps/$(cf app ${D} --guid)/routes | jq -r '.resources[0].entity.host')
        echo "Found route '${route}'\n"
        echo "Pushing ${D}..."
        cf push -n $route
    fi

    # Retrieve full url for backing service
    host=$(cf curl /v2/apps/$(cf app ${D} --guid)/routes | jq -r '.resources[0].entity.host')
    domain=$(cf curl $(cf curl /v2/apps/$(cf app ${D} --guid)/routes | jq -r '.resources[0].entity.domain_url') | jq -r '.entity.name')

    # Check if service instance already exists
    service=$(echo $(cf service ${D}) | grep -v 'FAILED')
    service_credentials=$(echo '{"uri":"http://host.domain"}' | sed -e s/host/$host/g | sed -e s/domain/$domain/g)

    if [ -z "${service}" ]
    then
        echo "Creating new service instance for '${D}'..."
        # Create a user-provided service for this backing service
        cf cups ${D} -p $service_credentials
    else
        echo "Updating service instance credentials for '${D}'..."
        cf uups ${D} -p $service_credentials
    fi
done

echo 'Deploying microservices...\n---'

for D in ${filtered_dirs[@]}
do
    app=$(echo $(cf app ${D} --guid) | grep -v 'FAILED')
    cd "$PROJECT_DIR/${D}"

    if [ -z "${app}" ]
    then
        echo "Creating new app '${D}'..."
        cf push
    else
        # Be sure to use the already existing route
        echo "Getting existing route for '${D}'..."
        route=$(cf curl /v2/apps/$(cf app ${D} --guid)/routes | jq -r '.resources[0].entity.host')
        echo "Found route '${route}'\n"
        echo "Pushing ${D}..."
        cf push -n $route
    fi
done