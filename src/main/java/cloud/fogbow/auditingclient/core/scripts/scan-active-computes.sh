#!/bin/bash
docker exec fogbow-database psql -U fogbow -d ras -t -c "select
  json_agg(
    json_build_object(
      'instanceId', o.instance_id,
      'serializedSystemUser', o.serialized_system_user,
      'cloudName', o.cloud_name
    )
  )
  from order_table o inner join compute_order_table c
  on o.id = c.id
  where o.instance_id IS NOT NULL;"