# Project Thrones — Faction Crafting Tables Pass 6: Asshai

This pass is intentionally Asshai-only and builds directly on Pass 5 (Sothoryos).

## Legacy source audit
`GOTRecipe.createAsshaiRecipes()` was recovered from the authoritative 1.7.10 JAR bytecode. Legacy metadata blocks were mapped to the existing 1.20.1 split registry IDs rather than approximated.

### Restored Asshai table pool
- 4 Basalt Bricks from a 2x2 of Basalt Rocks
- 3 Asshai Basalt Pillars from a vertical column of Basalt Rocks
- Asshai Basalt Brick Slabs x6
- Asshai Basalt Brick Stairs x4
- Asshai Basalt Brick Walls x6
- Asshai Cracked Basalt Brick Slabs x6
- Asshai Cracked Basalt Brick Stairs x4
- Asshai Cracked Basalt Brick Walls x6
- Asshai Chandelier x2 (stick + two fuses + iron ingot)
- Asshai Basalt Pillar Slabs x6
- Carved Basalt Bricks from a 2x2 of Basalt Bricks
- Asshai Torch (coal + stick)
- Asshai Bars x16 from six iron ingots
- Asshai iron armor via the faction-table iron equipment resolver
- Asshai Shadowbinder Staff (Ruby + sticks)
- Asshai Mask (planks)
- Asshai faction Banner with modern GOT banner NBT/type data
- Asshai Crafting Table from the legacy 2x2 plank recipe

## Metadata mappings
- `brick1:0` -> `got:basalt_bricks`
- `brick1:7` -> `got:cracked_basalt_bricks`
- `pillar1:7` -> `got:asshai_basalt_pillar`
- legacy Asshai slab/wall/stair metadata -> the existing `asshai_*` / `stairs_basalt_brick_asshai*` registry entries
- `brick2:10` -> `got:carved_basalt_bricks`
- `chandelier:6` -> `got:asshai_chandelier`

## Notes
Pass 5 Sothoryos behavior and all earlier faction-table material rules remain unchanged.
