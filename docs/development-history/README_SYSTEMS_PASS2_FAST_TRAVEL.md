# Project Thrones 1.20.1 — Systems Pass 2: Fast Travel

Built on Systems Pass 1 (NPC biome populations). Source-only pass; Gradle was intentionally not invoked.

## Restored / completed

- Legacy region-based waypoint discovery: entering a Planetos biome unlocks the corresponding fast-travel region.
- Fixed waypoints are server-validated against discovered regions and negative faction alignment.
- Original 200-tick / 10-second travel warmup.
- Warmup cancels on movement, damage, hostile targeting, sleeping, or leaving Planetos.
- Original countdown messaging during the final five seconds.
- Original waypoint cooldown defaults: 60 seconds minimum, 600 seconds maximum.
- Original cooldown formula: `(min + (max-min) * 0.9^useCount) * max(1, distance * 1.2E-5)`, converted to ticks.
- Per-waypoint use counts persist and reduce future cooldowns exactly as the legacy formula expects.
- Safe fixed-waypoint destination resolution.
- Player custom waypoints can be created at the player's current location, renamed, deleted, rendered on the map, and used for fast travel.
- Custom waypoint Y is preserved when the saved position remains safe; otherwise the safe-surface fallback is used.
- Custom waypoints and region unlock state persist through death/clone.
- Mounted travel preserves and remounts the player's current mount.
- Legacy 256-block entourage collection restored for:
  - hired NPCs currently ordered to follow the player,
  - owned non-sitting tameables,
  - mobs leashed directly to the player.
- Mounted hired NPCs travel with their mounts.
- Fast-travel state is synchronized to the client for correct locked/unlocked map markers and custom waypoint markers.
- Added map "Add Waypoint" control and custom waypoint rename/delete controls.
- Network protocol bumped to 12 because the fast-travel packet schema and packet table changed.

## Deliberately not part of this 1.0 pass

- Legacy Fellowship per-waypoint sharing UI for custom waypoints. Project Thrones uses Pacts in place of Fellowships, and the old per-waypoint Fellowship sharing model does not map cleanly onto the current Pact SavedData without a separate sharing design pass.
- The old cinematic/sepia travel-map flyover presentation is presentation polish; travel mechanics no longer depend on it.
- Conquest-based hostile-waypoint unlock exceptions remain tied to the future Command Table / Conquest overhaul. Negative faction alignment currently locks that faction's waypoint instead of consulting the legacy conquest grid.

## Static audit

Run:

`python3 migration/audit-systems-pass2-fast-travel.py`

The audit covers warmup, cooldown formula, movement/damage cancellation, attack gating, region discovery, custom waypoints, client map integration, entourage travel, and use-count persistence.
