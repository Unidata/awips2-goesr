#! /bin/bash
# Run on dx1
# Adds indexes dmw table to improve query performance
/awips2/psql/bin/psql -U awipsadmin -d metadata -c "
DROP INDEX IF EXISTS dmw_orbitalslot_scene_reftime_idx;
CREATE INDEX dmw_orbitalslot_scene_reftime_idx
  ON dmw USING btree (orbitalslot, scene, reftime);

DROP INDEX IF EXISTS dmw_scene_reftime_idx;
CREATE INDEX dmw_scene_reftime_idx
  ON dmw USING btree (scene, reftime);"

