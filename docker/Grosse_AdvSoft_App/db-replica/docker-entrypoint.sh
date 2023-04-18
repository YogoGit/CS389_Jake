#!/bin/bash
if [ ! -s "$PGDATA/PG_VERSION" ]; then
echo "*:*:*:$PG_REP_USER:$PG_REP_PASSWORD" > ~/.pgpass
chmod 0600 ~/.pgpass
until ping -c 1 -W 1 db_primary
do
echo "Replica waiting for primary to ping..."
sleep 3s
done
until pg_basebackup -h db_primary -D ${PGDATA} -U ${PG_REP_USER} -X stream -v -R
do
echo "Waiting for primary to connect..."
sleep 3s
done
echo "host replication all 0.0.0.0/0 trust" >> "$PGDATA/pg_hba.conf"
set -e
cat >> ${PGDATA}/postgresql.conf <<EOF
primary_conninfo = 'host=localhost port=5432 user=$PG_REP_USER password=$PG_REP_PASSWORD'
promote_trigger_file = '/tmp/touch_me_to_promote_to_me_primary'
EOF
chown postgres. ${PGDATA} -R
chmod 700 ${PGDATA} -R
fi
exec "$@"