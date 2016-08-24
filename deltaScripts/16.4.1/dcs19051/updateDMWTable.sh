#!/bin/bash
#
# DCS #19051 - This script provides drops the existing DMW table
#	from the Postgres DB, and removes the entry from the
#	"plugin_info" table, to allow for the updated dmw table in 16.4.1
#
# Since we aren't working with real winds data until GOES-R goes operational, we can
# just drop the current "dmw" table and remove its entry from "plugin_info"

PSQL="/awips2/psql/bin/psql"
TABLE=dmw

# Drop the database table
echo "Removing the database table '${TABLE}' from database."
${PSQL} -U awips -d metadata -c "DROP TABLE IF EXISTS ${TABLE};"

# Remove any other references to the plugin in the database
echo "Removing '${TABLE} entry from 'plugin_info' table."
${PSQL} -U awips -d metadata -c "DELETE FROM plugin_info WHERE name='${TABLE}';"
