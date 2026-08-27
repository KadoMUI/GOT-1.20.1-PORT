# NPC Pass 7 - Hiring / Orders / Respawn Lifecycle Integration

This source-only pass closes cross-system lifecycle gaps between hiring, mounts,
orders/squadrons and durable population respawners.

## Changes

- Added `GOTHiredMountController` so mounted hired units execute FOLLOW, HOLD,
  PATROL and WANDER movement through the mount rather than the passenger NPC's
  navigation.
- Mounted FOLLOW honors automatic teleport and moves rider+mount together.
- Command Horn SUMMON now moves the mounted unit as one object instead of
  potentially stranding the horse.
- HALT gets first refusal in mount steering, preventing a stale combat target
  from making a halted cavalry unit keep charging.
- Mounted PATROL supports explicit persisted route points and the same local
  guard-point fallback used by foot units.
- HOLD/PATROL/WANDER continue operating when the hiring player is offline;
  only owner-driven FOLLOW/target-sharing requires an online owner.
- Durable respawner anchors no longer count hired population members as filling
  their old settlement slot. The hired unit can leave while the population
  anchor replenishes its guard/population role.
- NPC-owned mounts are removed when their NPC rider dies, preventing persistent
  orphan cavalry mounts from accumulating.
- Existing hired state remains entity-NBT backed, so owner, task, order,
  squadron, guard point/range, teleport preference, XP, hired inventory,
  command state and patrol routes remain save/reload persistent.

## Validation

Run `python migration/audit-npc-pass7-lifecycle.py` for static source checks.
No Gradle build was attempted per project workflow; compile/runtime validation
is intentionally left to the local development environment.
