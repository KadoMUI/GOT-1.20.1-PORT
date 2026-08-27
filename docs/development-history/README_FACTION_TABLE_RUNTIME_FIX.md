# Faction Crafting Table Runtime Fix

- Removes the unguarded recursive material-mirror update that caused StackOverflowError when Alloy Steel armour patterns were placed in faction tables.
- Material mirror writes are now performed while the menu's re-entry guard is active, so TransientCraftingContainer changes cannot recursively call the same conversion path.
- Adds the previously missing faction sword/pickaxe/axe/shovel/hoe registry outputs for all 33 faction-table equipment prefixes.
- Adds models and English names for those outputs. With no legacy regional tool art available, these new Project Thrones extension items intentionally inherit vanilla iron tool textures while retaining distinct registry IDs/names and faction-table outputs.
