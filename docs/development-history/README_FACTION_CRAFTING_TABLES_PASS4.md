# Project Thrones — Faction Crafting Tables Pass 4

Pass 4 begins reconstruction of the non-standard legacy faction recipe pools directly from the 1.7.10 `GOTRecipe` bytecode.

Implemented exact table-only recipes:

## Asshai
- Shadowbinder Staff: diagonal stick/stick/ruby legacy pattern.
- Asshai Mask: wooden-plank helmet/mask pattern; accepts the modern plank tag as the old `plankWood` ore-dictionary ingredient did.

## Yi Ti
- Yi Ti Samurai Helmet: iron crown + alloy-steel lower corners.
- Yi Ti Samurai Chestplate: exact mixed iron/alloy-steel legacy pattern.
- Yi Ti Samurai Leggings: exact mixed iron/alloy-steel legacy pattern.
- Yi Ti Samurai Boots: exact mixed iron/alloy-steel legacy pattern.
- Yi Ti Captain Helmet: Samurai Helmet flanked by two White Bison Horns.

These recipes are resolved only while using the appropriate faction table and do not become global vanilla crafting recipes.

The prior Wildling Fur, Dothraki Dried Reeds, faction iron-equipment substitution, and elite alloy-steel armor rules remain intact.

Sothoryos' extended architectural/trap/dart pool is retained in the bytecode audit for the next reconstruction pass because several recipes depend on legacy metadata block variants that need explicit modern registry mappings rather than guessed substitutes.
