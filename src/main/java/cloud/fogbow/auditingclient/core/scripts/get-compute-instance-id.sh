#!/bin/bash
docker exec -it fogbow-database /bin/bash
psql -h 10.11.16.23 -p 5432 -U fogbow -d ras -t -c "select json_build_object('instanceId', instance_id) from order_table where id = '$1'"
