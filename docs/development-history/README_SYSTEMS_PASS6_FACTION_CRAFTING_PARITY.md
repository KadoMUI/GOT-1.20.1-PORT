# Systems Pass 6 — Final Faction Crafting Table Parity

This pass closes the remaining legacy `GOTRecipe.create*Recipes()` faction-table pools against the 1.7.10 JAR.

## What was already complete

Earlier passes restored the faction armor/material substitution layer, Wildling Fur armor, Dothraki dried-reed armor, elite Alloy Steel armor, and the large specialty pools for Sothoryos, Asshai, and Yi Ti.

## Final parity additions

- Restored the legacy faction-banner recipe to the remaining faction tables.
- Restored the correct primary legacy banner output per table (Robb for North, Robert for Crownlands, Renly for Stormlands, etc.).
- Restored Sothoryos' faction banner, which had intentionally been deferred in Pass 5.
- Restored the second Wildling-table banner recipe for the **Thenn** banner.
- Restored each simple faction table's 2x2 plank self-crafting recipe.
- Restored the Ghiscari **Harpy** craft: five Gold Ingots in the legacy helmet silhouette.
- Preserved Dothraki's legacy behavior: it has a faction table recipe but no faction-banner recipe in `createDothrakiRecipes()`.

## Shaped recipe fidelity

The custom recipe matcher now reproduces legacy Forge `ShapedOreRecipe` placement behavior more closely:

- shaped patterns can be shifted within the 3x3 grid;
- horizontally mirrored patterns are accepted;
- 2x2 faction-table recipes can occupy any valid 2x2 location;
- 1x3 faction banners can occupy any grid column;
- the 2x3 Thenn banner pattern can occupy either horizontal position.

This also improves the previously reconstructed Sothoryos/Asshai/Yi Ti specialty recipes without changing their ingredients or outputs.

## Legacy pool conclusion

A bytecode field-reference sweep of every non-special `create*Recipes()` method found no additional faction-exclusive outputs beyond:

- the faction armor sets;
- elite Alloy Steel armor where applicable;
- banners;
- faction crafting tables;
- Ghiscari Harpy;
- Wildling/Thenn dual banners;
- Dothraki dried-reed armor.

The three exceptional large pools remain Sothoryos, Yi Ti, and Asshai, all reconstructed in prior passes.

`migration/audit-systems-pass6-faction-crafting-parity.py` provides a static regression gate.
