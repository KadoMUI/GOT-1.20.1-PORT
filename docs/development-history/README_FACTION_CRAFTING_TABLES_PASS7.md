# Project Thrones — Faction Crafting Tables Pass 7 (Yi Ti)

Pass 7 is intentionally Yi Ti-only and builds on Pass 6 (Asshai).

## Legacy source audit

Reconstructed from `GOTRecipe.createYiTiRecipes()` in the supplied 1.7.10 GOT 24.08.29 JAR. Legacy metadata-backed blocks were mapped to the existing split 1.20.1 registry IDs rather than approximated.

## Restored Yi Ti recipes

### Masonry
- 4 Stone -> 4 Yi Ti Bricks
- 2x2 Yi Ti Bricks -> Carved Yi Ti Bricks
- Yi Ti Bricks + Vine -> Mossy Yi Ti Bricks
- Yi Ti Bricks + Orange Chrysanthemum -> Flowery Yi Ti Bricks
- Four Gold Nuggets around Yi Ti Bricks -> Gold-Trimmed Yi Ti Bricks
- 4 Granite Rock -> 4 Granite Yi Ti Bricks
- 2x2 Granite Yi Ti Bricks -> Carved Granite Yi Ti Bricks

### Construction families
The original slab/stair/wall recipes are restored for:
- Yi Ti Bricks
- Mossy Yi Ti Bricks
- Cracked Yi Ti Bricks
- Flowery Yi Ti Bricks
- Granite Yi Ti Bricks

### Pillars
- 3 Stone vertically -> 3 Yi Ti Pillars
- 3 Granite Rock vertically -> 3 Granite Yi Ti Pillars
- 3 Yi Ti Pillars -> 6 Yi Ti Pillar Slabs
- 3 Granite Yi Ti Pillars -> 6 Granite Yi Ti Pillar Slabs

### Armor
- Standard Yi Ti armor from Iron using vanilla armor silhouettes
- Full Yi Ti Bombardier armor from the original Alloy Steel / Iron mixed patterns
- Full Yi Ti Samurai armor from the complementary Alloy Steel / Iron mixed patterns
- Yi Ti Captain Helmet from White Bison Horn + Samurai Helmet + White Bison Horn

Pass 7 corrects the Samurai chestplate and leggings patterns from the earlier Pass 4 approximation after direct bytecode comparison.

### Faction items
- Yi Ti banner using the modern Yi Ti banner type/NBT
- Yi Ti faction crafting table from a 2x2 plank square

## Scope
No Asshai, Sothoryos, or other faction recipe behavior was intentionally changed in this pass.
