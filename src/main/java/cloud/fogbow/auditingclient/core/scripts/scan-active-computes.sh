#!/bin/bash
docker exec -it fogbow-database /bin/bash
result=$(psql -U fogbow -d ras -t -c "select serialized_system_user, instance_id, from compute_order_table where provider = 'UPV'")
echo $result
