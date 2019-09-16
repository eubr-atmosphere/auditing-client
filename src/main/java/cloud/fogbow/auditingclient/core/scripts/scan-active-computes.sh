#!/bin/bash
docker exec -it fogbow-database /bin/bash
result=$(psql -U fogbow -d ras -t -c "select serialized_system_user, instance_id from order_table inner join compute_order_table on order_table.id = compute_order_table.id where provider = 'UPV'")
echo $result
