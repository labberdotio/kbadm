#!/usr/bin/env bash
set -e

# Container Specific Versions

# Images

# IPs
export DOCKER_IP_SUBNET="10.89.0"
export DOCKER_IP_SUBNET_RANGE="10.89.0.0/24"

## Docker Globals
# export LOG_LEVEL="DEBUG"
export LOG_LEVEL="INFO"
export API_LOG_LEVEL=$LOG_LEVEL
# export THISHOST="controller"
# export THISHOST="10.88.88.60"
export THISHOST="10.88.88.180"
export COMMON_PLATFORM="linux"
# export COMMON_NETWORK="labber"
export COMMON_NETWORK="podman"
export GFS_VOLUME="gfs"
export RABBITMQ_HOST="rabbitmq"
export ORIENTDB_HOST="orientdb"
export GREMLIN_HOST="gremlin-server"
# docker network create $COMMON_NETWORK || true

export BASE_BOX="debian/buster64"

# GFS
export GREMLIN_HOST="${DOCKER_IP_gremlin}"
export GREMLIN_PORT=8182
export GREMLIN_USERNAME="root"
export GREMLIN_PASSWORD="root"

export KAFKA_HOST="broker"
export KAFKA_PORT=9092
export KAFKA_USERNAME="kafka"
export KAFKA_PASSWORD="kafka"

export GFS_API_HOST="${DOCKER_IP_gfsapi}"
export GFS_API_PORT=5000
export GFS_API_USERNAME="root"
export GFS_API_PASSWORD="root"

export GFS_UI_HOST="${DOCKER_IP_gfsui}"
# export GFS_UI_PORT=8888
export GFS_UI_PORT=3000
export GFS_UI_USERNAME="root"
export GFS_UI_PASSWORD="root"
export GFS_UI_API_NAMESPACE="gfs1"
export GFS_UI_API_HOST=$THISHOST
export GFS_UI_API_PORT=$GFS_API_PORT
export GFS_UI_WS_HOST=$THISHOST
export GFS_UI_WS_PORT=5002

export GFS_APP_HOST="${DOCKER_IP_gfsapp}"
# export GFS_APP_PORT=8888
export GFS_APP_PORT=3001
export GFS_APP_USERNAME="root"
export GFS_APP_PASSWORD="root"
export GFS_APP_API_NAMESPACE="gfs1"
# export GFS_APP_API_HOST=$THISHOST
export GFS_APP_API_HOST="localhost"
# export GFS_APP_API_PORT=$GFS_API_PORT
export GFS_APP_API_PORT=8080
# export GFS_APP_API_PATH="api/"
export GFS_APP_API_PATH=""
export GFS_APP_WS_HOST=$THISHOST
export GFS_APP_WS_PORT=5002

export GFS_PUSHER_HOST="${DOCKER_IP_gfspusher}"
export GFS_PUSHER_PORT=5002

export GFS_RENDERER_HOST="${DOCKER_IP_gfrenderer}"
export GFS_RENDERER_PORT=5003

export GFS_RIPPLER_HOST="${DOCKER_IP_gfsrippler}"
export GFS_RIPPLER_PORT=5001

export GFS_PULSINATOR_HOST=$THISHOST
export GFS_PULSINATOR_PORT=5005

export GFS_SUBSCRIBER_HOST=$THISHOST
export GFS_SUBSCRIBER_PORT=5004

# export GFS_RENDER_URI="http://10.88.88.60:5003"
# export GFS_RENDER_URI="http://10.88.88.60:5002"
export GFS_RENDER_URI="http://${GFS_RENDERER_HOST}:${GFS_RENDERER_PORT}"

# export GFS_DHCP_RENDER_URI="${GFS_RENDER_URI}/dhcp"
export GFS_DHCP_RENDER_URI="${GFS_RENDER_URI}/ipxedhcp"
export GFS_DNSMASQ_RENDER_URI="${GFS_RENDER_URI}/dns"
export GFS_HOSTS_RENDER_URI="${GFS_RENDER_URI}/hosts"
export GFS_BINDCONF_RENDER_URI="${GFS_RENDER_URI}/bindconf"
export GFS_BINDZONE_RENDER_URI="${GFS_RENDER_URI}/bindzone"
export GFS_PORTAL_RENDER_URI="${GFS_RENDER_URI}/http"

export GFS_ACCOUNT_USERNAME="whoopsjohnnie"
export GFS_ACCOUNT_PASSWORD="welcome1"
# export GFS_DEFAULT_NAMESPACE="testingns"
# export GFS_DEFAULT_NAMESPACE="copyright2"
export GFS_DEFAULT_NAMESPACE="copyright3"

export KBADM_APP_API_HOST="localhost"
export KBADM_APP_API_PORT=8080
export KBADM_APP_API_PATH="llm"

export IMPORTCSV="./server/src/test/resources/composes/"
export IMPORTCOMPOSE="./server/src/test/resources/composes/"


