# Project Thrones — Systems Pass 5: Alloy Forge + Millstone

This source pass replaces the inert 1.20.1 placeholders with dedicated legacy-style machine implementations.

## Alloy Forge
- Replaces `UtilityFacingBlock` with a block entity machine.
- 13-slot legacy inventory: 4 catalyst/alloy slots, 4 primary smelting slots, 4 paired output slots, 1 fuel slot.
- 200-tick processing cycle and ordinary furnace fuel values.
- All four valid lanes process together at the end of a cycle, matching the legacy forge.
- Recovered legacy special pair families:
  - Copper + Tin -> Bronze Ingot (ore/raw/ingot equivalents accepted for modern 1.20.1 resources).
  - Cobalt / Cobalt Blue + Iron -> Alloy Steel Ingot.
  - Widow's Wail + Oathkeeper -> Ice.
- Primary slots can also use vanilla/Project Thrones smelting recipes when no valid alloy pair is present.
- Sided inventory behavior, comparator output, persistent inventory/progress, lit blockstate and active front texture.
- Dedicated menu/screen using the preserved forge GUI texture.
- Taking an output awards `USE_ALLOY_FORGE`.

## Millstone
- Replaces the temporary vanilla Stonecutter implementation.
- Two-slot legacy inventory (input/output).
- Requires a redstone signal to operate.
- 200-tick milling cycle; progress resets when redstone turns off or the recipe becomes invalid.
- Active blockstate uses the preserved animated active top/side textures.
- Restored core legacy milling recipes and chance semantics, including:
  - Stone -> Cobblestone (100%)
  - Cobblestone -> Gravel (75%)
  - Gravel -> Flint (25%)
  - Sandstone -> 2 Sand
  - Red Sandstone -> 2 Red Sand
  - White Sandstone -> 2 White Sand when registered
  - Basalt Rock -> Basalt Gravel (75%) when registered
  - Basalt Gravel -> Flint (25%) when registered
  - Obsidian Gravel -> Obsidian Shard
  - Salt Ore -> Salt
  - legacy brick -> cracked-brick conversions for mapped modern block variants
- Sided automation, comparator output, persistence and dedicated menu/screen using the preserved millstone GUI.
- Taking output awards `USE_MILLSTONE`.

## Notes
- Source-only pass. No Gradle build was attempted per project workflow.
- Modern recipe lookup is intentionally used for the Alloy Forge's ordinary single-input smelting branch; the alloy-pair logic remains explicit and legacy-derived.
