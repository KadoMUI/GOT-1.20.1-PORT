# Project Thrones — Shields Pass 1

Replaces the legacy shield-selector concept with real item-based shields.

## Mechanics
- `GOTFactionShieldItem` extends vanilla `ShieldItem`.
- Durability is 336, matching the vanilla Shield.
- Vanilla shield blocking/use behavior is retained.
- Legacy 64x32 shield sheets are rendered as physical two-sided shields:
  - left half = outside/front face
  - right half = inside/back face held toward the player

## Faction-table recipes
The normal shield silhouette uses planks + an Iron Ingot and resolves to the table faction's base shield.
Factions with a recovered elite/alternate shield can use the same silhouette with an Alloy Steel Ingot:
- North -> White Harbour Guard shield
- Arryn -> Gulltown Guard shield
- Reach -> Oldtown Guard shield
- Westerlands -> Westerlands Guard shield
- Ghiscar -> Unsullied shield
- Qohor -> Unsullied shield (matching the table's Unsullied elite equipment path)
- Yi Ti -> Yi Ti Samurai shield

Tables without a legacy faction shield texture do not silently produce the generic vanilla shield.

## Registered item shields
All preserved legacy shield textures are registered as actual items, including faction, guard, Yi Ti specialty, Golden Company, Targaryen, Alcoholic, and achievement shields. Only faction-table shields receive recipes in this pass.
