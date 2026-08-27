# Project Thrones — Faction Crafting Tables Pass 2

## Legacy material fidelity

Pass 2 begins the per-faction recipe reconstruction by correcting the Wildling table's material rules.

### Wildling fur armour
The original 1.7.10 `GOTRecipe` bytecode was audited directly. `furHelmet`, `furChestplate`, `furLeggings`, and `furBoots` use the normal armour silhouettes, but their ingredient is `GOTItems.fur` — not iron.

The 1.20.1 Wildling table now reproduces that rule:
- Fur Helmet: 5x `got:fur`
- Fur Chestplate: 8x `got:fur`
- Fur Leggings: 7x `got:fur`
- Fur Boots: 4x `got:fur`
- Iron placed in a Wildling table no longer transforms into Fur armour.
- Fur is only treated as the armour material for the four exact armour silhouettes; it cannot substitute for iron in unrelated recipes.
- The table continues to use the normal 3x3 crafting interaction and consumes the real Fur stacks when the result is taken.

### Existing faction tables
The Pass 1 iron-equipment substitution remains in place for the other faction tables. This pass intentionally starts the deeper recipe reconstruction with the known incorrect Wildling material rather than inventing modern recipes that were not present in the legacy mod.

## Validation
The legacy recipes were confirmed from `got.common.recipe.GOTRecipe` in Game of Thrones 24.08.29.jar. Source-level checks were performed after the change. Gradle compilation remains dependent on the local environment's Gradle 8.8 availability.
