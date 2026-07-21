#!/usr/bin/env bash
set -e

SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do # resolve $SOURCE until the file is no longer a symlink
  DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
  SOURCE="$(readlink "$SOURCE")"
  [[ $SOURCE != /* ]] && SOURCE="$DIR/$SOURCE" # if $SOURCE was a relative symlink, we need to resolve it relative to the path where the symlink file was located
done
THIS_DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
echo "This Dir: $THIS_DIR"
source $THIS_DIR/common.sh

# TOKEN=$(./get_token.sh ${GFS_ACCOUNT_USERNAME} ${GFS_ACCOUNT_PASSWORD} | jq -r '.["access_token"]')
# # echo "TOKEN: ${TOKEN}"

# http -A bearer -a "${TOKEN}" POST http://${GFS_APP_API_HOST}:${GFS_APP_API_PORT}/${GFS_APP_API_PATH}/api/chat/stream prompt="Please list the versions of FreeBSD covered in the documents." 
http -A bearer -a "${TOKEN}" POST http://${KBADM_APP_API_HOST}:${KBADM_APP_API_PORT}/${KBADM_APP_API_PATH}/api/chat/stream prompt="Please list the versions of FreeBSD covered in the documents." 

