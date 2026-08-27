# Project Thrones — NPC Pass 4: Named NPC / Population Repair

Built on NPC Pass 3 (legacy cavalry).

## Missing named NPCs restored

The legacy GOTFixer bytecode and the modern role/population catalogues were cross-audited. Three North legendary roles existed in 1.20.1 but had no population path:

- Barbrey Dustin — Barrowtown, legacy SpawnInfo offset (0, +3)
- Howland Reed — Greywater Watch, legacy SpawnInfo offset (0, +5)
- Wyman Manderly — White Harbour, legacy SpawnInfo offset (0, +5)

`GOTNorthNpcPopulation.queueFixedSites` now queues those characters chunk-safely using waypoint coordinates and the Planetos terrain surface, and `PlanetosChunkGenerator` invokes that fixed-site queue alongside the other regional population queues.

## Durable named NPC respawning

Added `GOTNamedNpcRespawnData`, a SavedData-backed named-character anchor system.

- Any legendary GOT NPC with a non-empty population key is registered when it enters a ServerLevel.
- The original entity NBT is snapshotted so role, equipment, gender, quest/trade state, home data and other persistent state can be restored without maintaining a giant family-specific respawn switch.
- UUIDs are stripped before cloning to avoid duplicate UUID conflicts.
- Anchors persist with the dimension.
- The system only checks loaded home chunks.
- Missing named NPCs respawn after a five-minute delay.
- Existing named NPCs suppress duplicate spawns by population key.
- Per-level checks are budgeted to avoid scanning every anchor every tick.

This closes the previous one-shot-worldgen failure mode where a fixed legendary NPC could permanently disappear after death.

## Validation

- Updated and passed `migration/audit-north-npcs.py`.
- Static named-population audit passed.
- Cross-catalogue reference audit found no remaining legendary role with no population/structure reference.

The Gradle wrapper still requires an external Gradle 8.8 download in this environment, so a local `gradlew build` remains the compile/runtime validation step.
