#!/bin/bash

############################
#
#   [ INPUTS ]
#
############################
APP_NAME="demo-java-app"        # <-- [ UPDATE THIS ]
DOCKER_TAG="latest"             # <-- [ UPDATE THIS ]
DOCKER_HOST=52.61.202.137       # <-- [ UPDATE THIS ]
DOCKER_PORT=8100                # <-- [ UPDATE THIS ]

############################
#
#      [ FUNCTIONS ]
#
############################
array_contains () {
    local array="$1[@]"
    local seeking=$2
    local in=1
    for element in "${!array}"; do
        if [[ $element == $seeking ]]; then
            in=0
            break
        fi
    done
    return $in
}

invalid_environment() {
    echo "BAD INPUT --> The target environment [ $1 ] does not exist."
    echo "          > The environment must correspond to an OCP project."
    echo "          > Available options are: [ $2 ]"
    exit 1
}

invalid_action() {
    echo "BAD INPUT --> The action [ $1 ] is invalid."
    echo "          > Available options are: [ $2 ]"
    exit 1
}

############################
#
#   [ INPUT VALIDATION ]
#
############################
TARGET_ENVIRONMENT=$1
VALID_PROJECTS=("dt" "devops")
if [ -z "${TARGET_ENVIRONMENT}" ]
then
    echo "BAD INPUT --> A target environment must be specified."
    echo " > Available options are: [ ${VALID_PROJECTS[*]} ]"
    exit 1
fi
array_contains VALID_PROJECTS ${TARGET_ENVIRONMENT} || invalid_environment ${TARGET_ENVIRONMENT} "${VALID_PROJECTS[*]}"

ACTION=$2
VALID_ACTIONS=("create" "replace")
if [ -z "${ACTION}" ]
then
    echo "BAD INPUT --> An action must be specified."
    echo " > Available options are: [ ${VALID_ACTIONS[*]} ]"
    exit 1
fi
array_contains VALID_ACTIONS ${ACTION} || invalid_action ${ACTION} "${VALID_ACTIONS[*]}"
echo "[INFO] Action = ${ACTION}"

TAG=$3
if [ -z "${TAG}" ]
then
    echo "The default docker tag will be used."
else
    DOCKER_TAG=${TAG}
    echo "The docker tag ${DOCKER_TAG} will be used."
fi

############################
#
#  [ VARIABLE ASSIGNMENT ]
#
############################
APPLICATION_NAME="${APP_NAME}-${TARGET_ENVIRONMENT}"
DOCKER_REGISTRY="${DOCKER_HOST}:${DOCKER_PORT}"
PROJECT=usaf-${TARGET_ENVIRONMENT}

echo "[INFO] Deploying to project [ ${PROJECT} ] with application name [ ${APPLICATION_NAME} ]"

############################
#
#   [ DELETE THE OLD ]
#
############################
if [ "${TARGET_ENVIRONMENT}" != "prod" ] && [ "${ACTION}" == "create" ]
then
    echo "[INFO] Deleting existing OCP resources for ${APPLICATION_NAME}"
    oc delete all -n ${PROJECT} -l app=${APPLICATION_NAME}
    oc delete secrets -n ${PROJECT} -l app=${APPLICATION_NAME}
    oc delete serviceaccounts -n ${PROJECT} -l app=${APPLICATION_NAME}
fi

############################
#
#  [ CREATE THE NEW ]
#
############################
if [ "${ACTION}" == "create" ]
then
	mkdir ../build
	echo "[INFO] Creating inline properties secret."
	cat "../envs/${TARGET_ENVIRONMENT}.properties.json" | jq -cM '.' > ../build/${TARGET_ENVIRONMENT}.inline.properties
	oc secrets new -n ${PROJECT} ${APPLICATION_NAME}-properties property-overrides=../build/${TARGET_ENVIRONMENT}.inline.properties
    oc label secret ${APPLICATION_NAME}-properties -n ${PROJECT} app=${APPLICATION_NAME}
fi

oc process -f app-template.yaml \
    -p APP_NAME=${APPLICATION_NAME}\
    -p DOCKER_IMAGE="${DOCKER_REGISTRY}/${APP_NAME}:${DOCKER_TAG}"\
    | oc ${ACTION} -n ${PROJECT} -f -
