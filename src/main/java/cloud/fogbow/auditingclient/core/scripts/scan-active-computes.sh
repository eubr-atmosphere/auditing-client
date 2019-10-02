
#!/bin/bash
docker exec -it fogbow-database /bin/bash
#psql -h 10.11.16.23 -p 5432 -U fogbow -d ras -t -c "select serialized_system_user, instance_id from order_table inner join compute_order_table on order_table.id = compute_order_table.id WHERE instance_id IS NOT NULL AND provider = 'UPV'"
psql -h 10.11.16.23 -p 5432 -U fogbow -d ras -t -c "select serialized_system_user, instance_id from order_table inner join compute_order_table on order_table.id = compute_order_table.id WHERE instance_id IS NOT NULL"