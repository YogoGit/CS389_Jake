#!/bin/bash
echo "host replication all 0.0.0.0/0 trust" >> "$PGDATA/pg_hba.conf"
echo "host all postgres resume-tracker.application_cluster trust" >> "$PGDATA/pg_hba.conf"
set -e
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
CREATE USER $PG_REP_USER REPLICATION LOGIN CONNECTION LIMIT 100 ENCRYPTED PASSWORD '$PG_REP_PASSWORD';
CREATE USER $PG_APP_USER WITH ENCRYPTED PASSWORD '$PG_APP_PW';
GRANT ALL PRIVILEGES ON DATABASE $POSTGRES_DB to $PG_APP_USER;
EOSQL
cat >> "${PGDATA}"/postgresql.conf <<EOF
wal_level = replica
archive_mode = on
archive_command = 'cd .'
max_wal_senders = 8
hot_standby = on
EOF