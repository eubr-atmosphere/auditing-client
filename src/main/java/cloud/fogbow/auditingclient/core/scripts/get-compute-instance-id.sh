#!/bin/bash
docker exec fogbow-database psql -U fogbow -d ras -t -c "select json_build_object('instanceId', instance_id) from order_table where id = '$1'"
