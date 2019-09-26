#!/bin/bash
#docker exec -it fogbow-database /bin/bash
#result=$(psql -U fogbow -d fns -t -c "select serialized_system_user, ip, compute_id, id from federated_network_table inner join
#  federated_network_order_assigned_ips on federated_network_order_assigned_ips.federated_network_order_id = federated_network_table.id where provider = 'UPV'")
docker exec -it fogbow-database /bin/bash
psql -h 10.11.16.23 -p 5432 -U fogbow -d fns -t -c "select serialized_system_user, ip, compute_id, id from federated_network_table inner join
  federated_network_order_assigned_ips on federated_network_order_assigned_ips.federated_network_order_id = federated_network_table.id where provider = 'UPV'"