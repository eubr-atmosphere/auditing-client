#!/bin/bash
docker exec -it fogbow-database /bin/bash
result=$(psql -U fogbow -d fns -t -c "select serialized_system_user, assigned_ips, from federated_network_table where provider = 'UPV'")
echo $result