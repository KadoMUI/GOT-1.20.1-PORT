# Project Thrones 0.9.9 — Shield Reference Rebuild

This pass replaces the previous guessed transform approach with the architecture
verified in both attached reference mods.

## LOTR Renewed 1.16
- LOTRShieldItem extends ShieldItem.
- Each shield model has a `blocking` predicate override.
- Blocking selects a separate `<shield>_blocking.json`.
- Idle and blocking models use different hand transforms.

## Epic Knights 1.20.1
- MedievalShieldItem extends ShieldItem.
- Registers a `blocking` item model property:
  living.isUsingItem() && living.getUseItem() == stack.
- Uses `builtin/entity` with a custom BEWLR.
- Uses separate normal and `_blocking` JSON transforms.
- Its heater shield plate is ~31x31 model pixels around a central handle pivot.

## Project Thrones changes
- Added the `minecraft:blocking` model predicate for every GOTFactionShieldItem.
- Added `_blocking.json` models for all 40 GOT shields.
- Adopted Epic Knights' 1.20.1 normal/blocking display transforms.
- Removed the custom IClientItemExtensions arm-pose override.
- Enlarged the legacy GOT shield render plate to ~31x32 model pixels so it is
  physically comparable to the working Epic Knights heater shield.
- Preserved the original GOT 64x32 textures and front/back half convention.
