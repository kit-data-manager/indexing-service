#!/bin/bash
################################################################################
# Management for metastore framework
# - Managing the following instances 
#    - elasticsearch
#    - indexing-service
# Usage:
# bash manageIndexingFramework.sh [init|start|stop]
################################################################################

################################################################################
# Define default values for variables
################################################################################
DOCKER_NETWORK=network4indexing
DOCKER_ELASTIC=elastic4indexing
DOCKER_INDEXING=indexing4docker
DOCKER_METASTORE=metastore4indexing
DOCKER_RABBIT=rabbitmq4indexing
################################################################################
# START DECLARATION FUNCTIONS
################################################################################

################################################################################
function usage {
################################################################################
  echo "Script for managing indexing service."
  echo "USAGE:"
  echo "  $0 [init|start|test|stop]"
  echo " "
  echo "  init  - Initialize/Reset the whole framework"
  echo "  start - Start stopped framework"
  echo "  test  - Start elasticsearch only"
  echo "  stop  - Stop framework"
  exit 1
}

################################################################################
function checkParameters {
################################################################################
  # Check no of parameters.
  if [ "$#" -ne 1 ]; then
    echo "Illegal number of parameters!"
    usage
  fi
}

################################################################################
function initFramework {
################################################################################
printInfo "Setup Framework"

echo "Setup configuration directories for metaStore and indexing-Service"
#mkdir -p "$ACTUAL_DIR/settings/metastore"
mkdir -p "$ACTUAL_DIR/settings/indexing"

echo "Setup network for docker..."
docker network create $DOCKER_NETWORK

#echo "Start RabbitMQ server..."
#deleteDockerContainer $DOCKER_RABBIT
#docker run -d --hostname rabbitmq --net $DOCKER_NETWORK --name $DOCKER_RABBIT -p 5672:5672 -p 15672:15672 rabbitmq:3-management

#echo "Start metaStore2..."
#deleteDockerContainer $DOCKER_METASTORE
#docker run -d -v "$ACTUAL_DIR/settings/metastore":/spring/metastore2/config --net $DOCKER_NETWORK --name $DOCKER_METASTORE -p8040:8040 kitdm/metastore2:latest

echo "Start elasticsearch server..."
deleteDockerContainer $DOCKER_ELASTIC
docker run -d --net $DOCKER_NETWORK --name $DOCKER_ELASTIC  -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:7.9.3

echo "Start Indexing-Service..."
deleteDockerContainer $DOCKER_INDEXING
docker run -d -v "$ACTUAL_DIR/settings/metastore":/spring/indexing-service/config --net $DOCKER_NETWORK --name $DOCKER_INDEXING  -p 8050:8050 indexing-service:latest

printInfo "Ready to use metastore"
}

################################################################################
function initTest {
################################################################################
printInfo "Setup Test Framework"

echo "Setup network for docker..."
docker network create $DOCKER_NETWORK

echo "Start elasticsearch server..."
deleteDockerContainer $DOCKER_ELASTIC
docker run -d --net $DOCKER_NETWORK --name $DOCKER_ELASTIC  -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:7.9.3

printInfo "Ready to use indexing service"
}

################################################################################
function startFramework {
################################################################################
printInfo "(Re)start metastore and all linked services..."

#echo "Start RabbitMQ server..."
#docker start $DOCKER_RABBIT 

#echo "Start metastore2..."
#docker start $DOCKER_METASTORE 

echo "Start elasticsearch server..."
docker start $DOCKER_ELASTIC 

echo "Start Indexing-Service..."
docker start $DOCKER_INDEXING

printInfo "Framework started!"
}

################################################################################
function stopFramework {
################################################################################
printInfo "Shutdown indexing service and all linked services..."

echo "Stop Indexing-Service..."
docker stop $DOCKER_INDEXING

echo "Stop elasticsearch server..."
docker stop $DOCKER_ELASTIC 

#echo "Stop metastore2..."
#docker stop $DOCKER_METASTORE 

#echo "Stop RabbitMQ server..."
#docker stop $DOCKER_RABBIT 

printInfo "Framework stopped!"
}

################################################################################
function deleteDockerContainer {
################################################################################
printInfo "Delete docker image '$1'"

if docker ps | grep -q "$1"; then
    echo "Docker container '$1' still running -> Stop docker container"
    docker stop $1
fi

if docker ps -a | grep -q "$1"; then
    echo "Docker container '$1' exists -> Remove docker container"
    docker rm $1
fi
}

################################################################################
function printInfo {
################################################################################
echo "---------------------------------------------------------------------------"
echo "$*"
echo "---------------------------------------------------------------------------"
}

################################################################################
# END DECLARATION FUNCTIONS / START OF SCRIPT
################################################################################

################################################################################
# Test for commands used in this script
################################################################################
testForCommands="type echo grep mkdir docker"

for command in $testForCommands
do 
  if ! type $command >> /dev/null; then
    echo "Error: command '$command' is not installed!"
    exit 1
  fi
done

################################################################################
# Determine directory of script. 
################################################################################
ACTUAL_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

################################################################################
# Check parameters
################################################################################
checkParameters "$*"

################################################################################
# Manage framework
################################################################################

case "$1" in
  init) initFramework
     ;;
  start) startFramework
     ;;
  test) initTest
     ;;
  stop) stopFramework
      ;;
  *) usage
     ;;
esac
