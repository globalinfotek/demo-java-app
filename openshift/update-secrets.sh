#!/usr/bin/env bash

############################
#
#   [ INPUTS ]
#
############################
APP_NAME="demo-java-app"        # <-- [ UPDATE THIS ]

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

now() {
    date +'%Y-%m-%d-t-%H-%M-%S'
}

############################
#
#   [ INPUT VALIDATION ]
#
############################
TARGET_ENVIRONMENT=$1
VALID_PROJECTS=("devops")
if [ -z "${TARGET_ENVIRONMENT}" ]
then
    echo "BAD INPUT --> A target environment must be specified."
    echo " > Available options are: [ ${VALID_PROJECTS[*]} ]"
    exit 1
fi
array_contains VALID_PROJECTS ${TARGET_ENVIRONMENT} || invalid_environment ${TARGET_ENVIRONMENT} "${VALID_PROJECTS[*]}"

############################
#
#  [ VARIABLE ASSIGNMENT ]
#
############################
APPLICATION_NAME="${APP_NAME}-${TARGET_ENVIRONMENT}"
PROJECT=usaf-${TARGET_ENVIRONMENT}

PROPS_SECRET_NAME="${APPLICATION_NAME}-properties"
PROPS_COPY_NAME="${PROPS_SECRET_NAME}-$(now)"
echo "Making a copy of the secret [ ${PROPS_SECRET_NAME} ] [ ${PROPS_COPY_NAME} ]."
oc export secret -n ${PROJECT} ${PROPS_SECRET_NAME} -o json | sed "s/${PROPS_SECRET_NAME}/${PROPS_COPY_NAME}/" | oc create -n ${PROJECT} -f -

echo "Deleting the old secret ${PROPS_SECRET_NAME}"
oc delete secret -n ${PROJECT} ${PROPS_SECRET_NAME}

mkdir ../build
echo "[INFO] Creating inline properties secret."
cat "../envs/${TARGET_ENVIRONMENT}.properties.json" | jq -cM '.' > ../build/${TARGET_ENVIRONMENT}.inline.properties
oc secrets new -n ${PROJECT} ${APPLICATION_NAME}-properties property-overrides=../build/${TARGET_ENVIRONMENT}.inline.properties
oc label secret ${APPLICATION_NAME}-properties -n ${PROJECT} app=${APPLICATION_NAME}
