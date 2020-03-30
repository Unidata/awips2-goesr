#! /bin/bash
# This script should be run on dx1
echo "Running delta script ${0} for RODO DR 8082"
/awips2/psql/bin/psql --user=awips --db=metadata --command="UPDATE satellite SET units='µm' WHERE units='microm';"
echo "Delta script ${0} complete"
