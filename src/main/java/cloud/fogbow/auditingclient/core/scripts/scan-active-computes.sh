#!/bin/bash
docker exec -it fogbow-database /bin/bash
#psql -h 10.11.16.23 -p 5432 -U fogbow -d ras -t -c "select serialized_system_user, instance_id from order_table inner join compute_order_table on order_table.id = compute_order_table.id WHERE instance_id IS NOT NULL AND provider = 'UPV'"
psql -h 10.11.16.23 -p 5432 -U fogbow -d ras -t -c "select json_agg(json_build_object('instanceId', o.instance_id, 'serializedSystemUser', o.serialized_system_user)) from order_table o inner join compute_order_table c on o.id = c.id where o.instance_id IS NOT NULL;"