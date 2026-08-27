# Project Thrones — Faction Crafting Tables Pass 5 (Sothoryos)

Pass 5 is intentionally Sothoryos-only. It reconstructs the Sothoryos faction-table recipe pool from the 1.7.10 `GOTRecipe.createSothoryosRecipes()` bytecode and maps legacy metadata blocks to their modern 1.20.1 registry entries.

Implemented table-exclusive families:
- Sothoryos brick, mossy, cracked, gold and obsidian architectural families: blocks, slabs, stairs and walls.
- Sothoryos stone, gold and obsidian pillars plus pillar slabs.
- Sarbacane, standard/gold/obsidian sarbacane traps.
- Darts (4) and poison-dart conversion using Bottle of Poison.
- Sothoryos double torches.
- Sothoryos iron armour and the Flame-of-East + helmet Chieftain Helmet upgrade.
- Sothoryos crafting table from any modern plank-tag wood.

The recipes remain faction-table-only and do not become global vanilla recipes. Pass 4 Asshai/Yi Ti behavior and all earlier faction material rules are preserved unchanged.

The legacy Sothoryos banner recipe was not forced into this pass because the modern banner implementation needs its faction/banner-state mapping handled separately rather than silently returning a generic banner.
