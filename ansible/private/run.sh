#!/bin/bash

image=eubraatmosphere/auditing-client:$1
sudo docker pull $image
container_id=`sudo docker run --name auditing-client -idt -v /var/run/docker.sock:/var/run/docker.sock -v /usr/bin/docker:/usr/bin/docker $image`

DIR_PATH="`dirname \"$0\"`"              # relative
DIR_PATH="`( cd \"$DIR_PATH\" && pwd )`"  # absolutized and normalized
if [ -z "$DIR_PATH" ] ; then
  # error; for some reason, the path is not accessible
  # to the script (e.g. permissions re-evaled after suid)
  exit 1  # fail
fi

sudo docker exec $container_id /bin/bash -c "mkdir src/main/resources/private"
client_id=$(grep ^client_id $DIR_PATH/auditing-client.conf | awk -F "=" '{print $2}')
sudo bash $DIR_PATH/generate-ssh-key-pair $client_id 2048
mv $DIR_PATH/$client_id $DIR_PATH/private.key
sudo docker cp $DIR_PATH/private.key $container_id:/root/auditing-client/src/main/resources/private
sudo docker cp $DIR_PATH/$client_id $container_id:/root/auditing-client/src/main/resources/private
sudo docker cp $DIR_PATH/auditing-client.conf $container_id:/root/auditing-client/src/main/resources/private
sudo docker exec $container_id /bin/bash -c "chmod +x src/main/java/cloud/fogbow/auditingclient/core/scripts/scan*"
