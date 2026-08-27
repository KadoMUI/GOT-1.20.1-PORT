# Project Thrones 0.9.9 — NPC Shields Pass 1

Adds faction shields to appropriate humanoid military NPC loadouts.

## Shield-bearing roles
- Levymen (melee)
- Soldiers (melee)
- Guards
- Captains
- Warriors where the faction has a shield
- Unsullied / Grey Worm
- Yi Ti Samurai
- Yi Ti Bombardiers

## Intentionally shieldless
- Archers / crossbowmen
- Spearmen / pikemen
- Banner bearers (their offhand remains the faction banner)
- Flamethrower units
- Priests, craftsmen, traders, farmers and civilians
- Factions with no dedicated shield asset (Night's Watch, Dothraki, Wildlings,
  Jogos Nhai, Ibben, Lhazar, Mossovy)

## Special mappings
- North Guard -> Northguard Shield
- Arryn Guard -> Arrynguard Shield
- Westerlands Guard -> Westerlandsguard Shield
- Reach Guard -> Reachguard Shield
- Hillmen warriors -> Hillmen Shield
- Unsullied / Grey Worm -> Unsullied Shield
- Yi Ti Samurai -> Yi Ti Samurai Shield
- Yi Ti Bombardier -> Yi Ti Bombardier Shield

The helper refuses to overwrite an occupied OFFHAND slot, preserving banners
and any future special offhand equipment.
