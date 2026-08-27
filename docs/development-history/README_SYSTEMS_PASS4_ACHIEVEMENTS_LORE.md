# Project Thrones — Systems Pass 4: Achievements + Lore Integration

## Achievements
- Keeps the recovered 390-entry legacy achievement catalog and existing parchment GUI.
- Normalizes legacy fields/lowercase hooks to canonical catalog IDs (fixes e.g. `pickpocket` vs `PICKPOCKET`).
- Copies achievement and achievement-stat persistence across player death.
- Immediately syncs newly awarded state to the client and displays an unlock actionbar message.
- Adds catalog-driven event integration for:
  - entering Planetos biomes (including legendary enter achievements),
  - Traveller I–V at 20/40/60/80/100 unique Planetos biomes,
  - killing ordinary/special entities and exact consolidated NPC roles,
  - legendary NPC kills + KILL_LEGENDARY_NPC,
  - four-piece armor set achievements,
  - riding supported mount entity types,
  - crafting, item use, drinks and key material pickups.
- Direct subsystem hooks now also cover quest completion, faction pledge, 100 hires, Golden Company hiring and bounty retaliation, in addition to pre-existing trade/invasion/pickpocket/smithing/rank hooks.
- Achievements whose actual legacy gameplay system is still absent remain dependency-blocked rather than being faked (notably Alloy Forge, Millstone, carts/pouches and similar utility mechanics).

## Lore
- Fixes the legacy parser so `#title`, `#author`, `#types` metadata and UTF-8 BOM-prefixed files are actually parsed instead of leaking into book body text.
- Registers the 502-file lore corpus with the server resource reload lifecycle.
- Restores the original written-book delivery format. Generated books carry `GOTLoreId`, title, author and paginated text.
- Reading a lore book permanently discovers that entry; discovered state survives death.
- Restores the original miniquest extra-reward chance: 1 in 10 completed quests can award a lore book.
- Lore selection respects the player's client language when that translation exists, falls back to English, and filters by faction/type category.
- Adds a discovered-lore browser to the main GOT menu so previously read lore can be revisited without retaining the physical book.
- Adds client/server lore-data packets; network protocol is now 13.

## Validation
`migration/audit-systems-pass4-achievements-lore.py` validates the 390 achievement catalog entries, 502 lore resources, trigger integration surface, parser repair, lore-book integration and network registration.

No Gradle build was attempted per request; this archive is source-only for local compilation/testing.
