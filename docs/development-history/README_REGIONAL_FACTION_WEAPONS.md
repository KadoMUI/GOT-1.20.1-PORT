# Project Thrones — Regional Faction Weapons Fix

Replaces the incorrect generated per-faction tool/weapon families with the two intended regional weapon families supplied for Project Thrones.

## Crafting table result mapping

Faction armor remains faction-specific.

For faction crafting tables, vanilla iron silhouettes are remapped at the final result stage:

### Westeros tables
- Iron Sword recipe -> Westeros Sword
- Iron Shovel recipe -> Westeros Spear
- Iron Axe recipe -> Westeros Hammer

### Essos / non-Westeros tables
- Iron Sword recipe -> Essos Sword
- Iron Shovel recipe -> Essos Spear
- Iron Axe recipe -> Essos Hammer
- Iron Pickaxe recipe -> Essos Polearm

The polearm currently uses the same reach/thrust profile as the spear because no separate polearm stat profile was specified.

## Weapon profiles
- Regional Sword: iron sword damage, attack speed, and durability.
- Regional Spear: stone sword damage, +3 entity reach, line-thrust multi-target damage.
- Regional Hammer: netherite-axe-equivalent attack damage and axe-speed attack cadence, while retaining an iron durability tier.
- Essos Polearm: same spear/thrust profile pending a distinct requested profile.

## Assets
Uses the user-supplied:
- westeros_sword.png
- westeros_spear.png
- westeros_hammer.png
- essos_sword.png
- essos_spear.png
- essos_hammer.png
- essos_polearm.png
