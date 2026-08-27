# Project Thrones — NPC Pass 5: Natural Military Populations

This pass repairs the ordinary regional military population layer after NPC Pass 4 completed fixed/named population anchors.

## Legacy behavior recovered

The final 1.7.10 `GOTSpawnList` defines the ordinary Westerosi military lists as a weight-10 melee levyman plus a weight-5 levy archer, each spawning in groups of 1–2. Regional biome constructors then add that `*_MILITARY` list with the legacy population chance. Because Project Thrones 1.20.1 collapses the old individual NPC subclasses into one entity type per faction, those class weights have to be reproduced inside `finalizeSpawn`.

Pass 5 restores that exact **10:5 / 2:1 role mix** for:

- North
- Arryn / Vale
- Riverlands
- Westerlands
- Crownlands
- Dragonstone
- Reach
- Stormlands
- Dorne
- Iron Islands

Natural regional NPCs in those ordinary faction biomes now become levymen about 66.7% of the time and levy archers about 33.3% of the time instead of incorrectly becoming wilderness civilians.

## Special regional behavior retained

- `north_wild` / Skagos continue to use the Northern Hillman fighter selector.
- Arryn Mountains continue to use the Hill Tribe fighter selector.
- Civilians remain structure/settlement population NPCs. This matches the legacy division between biome military populations and settlement inhabitants.
- Professional Soldiers, Guards, Banner Bearers and Captains remain structure/conquest/invasion/hiring roles rather than being incorrectly substituted into the ordinary wilderness military list.
- Existing cavalry behavior is unchanged. The legacy 10% horse roll belongs to professional Soldier/Banner Bearer classes, not the levyman military spawn list, so Pass 5 does not invent mounted wilderness levymen.

## Existing Essos populations

The existing modern Free Cities / Yi Ti / Ghiscar / Qarth / Lhazar / Qohor and other regional entity implementations already select their military roles internally rather than defaulting to civilians, so this pass does not rewrite those working role distributions.

## Validation

`migration/audit-npc-pass5-natural-military.py` checks all ten repaired regional entity families for the restored 10:5 selector and guards against regression to civilian-only wilderness spawns.

A machine-readable summary is included at `data/got/npc_natural_population_audit.json`.

Per request, no Gradle build was attempted for this pass. This archive is source-only for local compilation/testing.
