#!/bin/bash
docker exec -it fogbow-database /bin/bash
psql -U fogbow -d ras -t -c "select json_agg(json_build_object('instanceId', o.instance_id, 'serializedSystemUser', o.serialized_system_user)) from order_table o inner join compute_order_table c on o.id = c.id where o.instance_id IS NOT NULL AND c.provider = 'UPV';"