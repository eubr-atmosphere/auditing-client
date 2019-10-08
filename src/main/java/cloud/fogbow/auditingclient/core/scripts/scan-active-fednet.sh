#!/bin/bash
docker exec fogbow-database psql -U fogbow -d fns -t -c "select json_agg(json_build_object(
  'serializedSystemUser', serialized_system_user,
  'ip', ip,
  'computeId', compute_id,
  'id', id)) from federated_network_table inner join
  federated_network_order_assigned_ips on federated_network_order_assigned_ips.federated_network_order_id = federated_network_table.id"
