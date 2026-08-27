# Project Thrones — Faction Crafting Tables Pass 3

## Legacy material + elite armour parity

Pass 3 audits every faction recipe constructor in the 1.7.10 `got.common.recipe.GOTRecipe` bytecode and expands the table-sensitive recipe resolver without making faction recipes globally craftable.

### Restored elite alloy-steel sets
The legacy tables use the normal armour silhouettes with `alloySteelIngot` for their elite sets. These now work in their correct faction tables:

- North -> Northguard armour
- Arryn -> Arrynguard armour
- Crownlands -> Kingsguard armour
- Reach -> Reachguard armour
- Westerlands -> Westerlands Guard armour
- Qohor -> Unsullied armour
- Ghiscar -> Unsullied armour

All four armour pieces are supported. The grid contains and consumes real `got:alloy_steel_ingot`; iron is used only as a temporary internal recipe-match mirror and is restored before the player can take the result.

### Dothraki material fidelity
The original Dothraki armour recipes do **not** use iron. `GOTRecipe.createDothrakiRecipes()` uses `GOTBlocks.driedReeds` in the standard armour silhouettes. The Dothraki table now reproduces this:

- Dothraki Helmet: 5 Dried Reeds
- Dothraki Chestplate: 8 Dried Reeds
- Dothraki Leggings: 7 Dried Reeds
- Dothraki Boots: 4 Dried Reeds

Iron can no longer be transformed into Dothraki armour.

### Wildling regression protection
Pass 2's Fur rule remains intact: Wildling Fur armour requires real `got:fur` and cannot be made from iron.

### Legacy recipe-pool audit
`data/got/faction_crafting_legacy_pool_audit.json` records the GOTItems/GOTBlocks fields referenced by each legacy faction recipe constructor. This identifies the larger special pools still requiring dedicated shaped-recipe handling, particularly Sothoryos, Yi Ti and Asshai, without guessing at recipes.

## Next pass
Pass 4 can use the audit to reconstruct non-armour faction-table-only recipes (Asshai Shadowbinder Staff/Mask, Sothoryos traps/darts/architecture, Yi Ti architecture/equipment variants, banners and other table-local recipes) with exact legacy shapes and ingredients.
