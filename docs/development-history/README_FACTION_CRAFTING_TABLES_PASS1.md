# Project Thrones — Faction Crafting Tables Pass 1

## Goal
Restore functional faction crafting tables instead of treating the registered faction tables as vanilla CraftingTableBlock instances.

## Implementation
- Added `got.crafting.GOTFactionCraftingTableBlock`.
- Converted all 33 registered faction tables to the faction-aware block.
- The table retains the vanilla 3x3 crafting UI and normal recipe behavior.
- When a vanilla iron equipment recipe resolves, the result slot substitutes the matching registered faction equipment for that table.
- Armor silhouettes supported: helmet, chestplate, leggings, boots.
- Tool/weapon silhouettes supported by the resolver: sword, pickaxe, axe, shovel, hoe. These automatically resolve whenever `<faction>_<type>` exists in the item registry.
- Hill Tribes correctly resolve through the existing `hillmen_*` equipment IDs.
- Wildling correctly resolves through the existing `fur_*` equipment IDs.
- Tables whose catalogue intentionally lacks a particular slot (for example current Ibben/Mossovy helmet registration) simply have no faction substitution for that missing item.

## Examples
- Iron helmet recipe in North table -> `got:north_helmet`
- Iron chestplate recipe in Westerlands table -> `got:westerlands_chestplate`
- Iron leggings recipe in Braavos table -> `got:braavos_leggings`

## Legacy reference
The 1.7.10 JAR was checked before implementation. Legacy `GOTContainerCraftingTable$North`, etc. each point at a faction-specific `GOTRecipe` list. The legacy North recipe bytecode confirms the standard iron armor silhouettes with iron ingots output `northHelmet`, `northChestplate`, `northLeggings`, and `northBoots`; alloy steel similarly produced North Guard equipment. This pass restores the table-sensitive iron-equipment behavior requested while preserving the modern vanilla crafting UI.

## Validation
Source-level audit completed for all 33 table registrations. A Gradle compile could not be run in the sandbox because the wrapper attempts to download Gradle 8.8 from services.gradle.org and outbound DNS is unavailable.
