# Project Thrones 1.0 Systems Pass 1 — NPC Biome Populations

This pass connects and corrects the natural NPC population layer using the original 1.7.10 biome constructor spawn-list declarations as the source of truth.

## What changed

- Normalized Forge `add_spawns` biome modifiers around exact legacy biome membership instead of broad regional-prefix approximations.
- Corrected Westerosi military tags so ordinary levy populations spawn in the exact legacy base biomes (`north`, `westerlands`, `riverlands`, `crownlands`, `dragonstone`, `reach`, `stormlands`, `dorne`, `iron_islands`).
- Corrected Free Cities / Essos tags to their exact ordinary military biomes.
- Restored overlapping legacy populations:
  - Wildlings in Haunted Forest, both Gifts, Skagos, Stoney Shore, Frozen Shore, North, North Town, North Wild, and Thenn Land.
  - Hill Tribe fighters in Arryn, Arryn Town, and Arryn Mountains Foothills using the shared modern Arryn entity engine.
  - Norvos military in Volantis as well as Norvos.
  - Sothoryos military incursions in the appropriate colonies/Ulthos zones remain intact.
  - Jogos Nhai incursions restricted to Yi Ti proper and Yi Ti Border Zone.
- White Walker ordinary military population is restricted to Haunted Forest, matching `WALKERS_MILITARY` in the legacy biome bytecode.
- Removed the invented ambient Night's Watch biome modifier. Night's Watch remains a fixed/structure population faction.
- Kept Dothraki out of ordinary biome `add_spawns`: the original Dothraki Sea constructors do not register a Dothraki military spawn list. Project Thrones' khalasar/camp/invasion systems remain their population source.
- Added `data/got/npc_biome_population_ledger.json` documenting the recovered map.
- Added `migration/audit-npc-biome-populations.py` as the new regression gate.

## Important behavior notes

The modern port collapses many legacy NPC subclasses into one entity type per faction. The biome modifier chooses the faction entity; `finalizeSpawn` then resolves the appropriate role/loadout for that biome. Arryn is a special overlap: Arryn Town and Mountains Foothills resolve to Hill Tribe fighters, while Arryn proper permits both native levies and Hill Tribe encounters.

No Gradle build was attempted; this pass is source-only for local compilation/testing.
