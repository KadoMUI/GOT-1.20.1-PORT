# Project Thrones 1.20.1 — Systems Pass 8: Utility Gameplay

Source-only pass built on Systems Pass 7.

## Pouches
- Restores small / medium / large pouch items as modern separate registry items.
- Legacy capacities: 9 / 18 / 27 slots.
- Inventory is serialized directly into the pouch ItemStack under `GOTPouchData/Items`.
- Pouches cannot be placed inside pouches.
- Dedicated pouch container + screen, shift-click support, death/save-safe item NBT persistence.
- Implements `DyeableLeatherItem`; legacy pouch overlay textures are used and the vanilla leather-dye recipe path can recolor them.
- Restores the 7-leather small-pouch recipe (`X X / X X / XXX`).
- Restores pouch combining through `got:pouch_combine`: smaller pouches can be combined into the next capacity and their contents are retained. Over-capacity combinations are rejected.
- Crafting any pouch awards `CRAFT_POUCH`.

## Sling
- `got:sling` is functional again instead of a durability-only placeholder.
- Consumes `got:pebble` ammunition unless Creative.
- 250 durability, one durability per shot, legacy bow-like shot sound.
- New `got:pebble_projectile` entity deals impact damage.
- Killing a sufficiently large mob with a sling pebble awards `KILL_LARGE_MOB_WITH_SLINGSHOT`.

## Darts / Sarbacane
Legacy darts were Sarbacane ammunition, not hand-thrown weapons. This pass restores that actual loop.
- `got:sarbacane` is now a chargeable ranged weapon instead of a generic SwordItem.
- Minimum useful draw matches the legacy 0.65 threshold; charge curve and maximum scaling follow the old Sarbacane behavior.
- Searches inventory for either `got:dart` or `got:dart_poisoned`, consumes one unless Creative, and damages the Sarbacane.
- New `got:dart_projectile` entity persists its projectile item and damage factor.
- Normal dart: velocity-scaled impact damage.
- Poisoned dart: same impact plus Poison for 100 ticks (5 seconds).
- `USE_SARBACANE` achievement is awarded on a successful shot.
- Sothoryos faction-table recipes for Dart x4 and Dart + Poison remain intact from earlier passes.

## Branding Iron
- Replaces the inert branding-iron Item with `GOTBrandingIronItem`.
- Legacy 100 durability restored.
- Unnamed irons open a compact naming screen; names are trimmed and capped at 64 characters.
- Named cool irons can be heated on active heat sources: lit furnace, lit campfire, active Alloy Forge / Oven, fire, soul fire, and lava.
- Heated model switches to the restored `branding_iron_hot` texture.
- Heated + named iron can brand Animals; legacy behavior is preserved by applying the brand as the animal custom name and storing the branding player's UUID in `GOTBrander` persistent data.
- Branded animals become persistent.
- Iron loses one durability per brand and cools after crossing each five-use durability band, matching the legacy cooling logic.
- Awards `BRAND_ENTITY`.

## Networking / client
- Main network protocol bumped 13 -> 14 for the branding-name packet.
- Adds Pouch menu registration and screen.
- Registers pebble/dart ThrownItem renderers.
- Registers branding hot item-property override and pouch dye tint.

## Validation
`migration/audit-systems-pass8-utilities.py` statically validates all major integration points.
No Gradle build was attempted, per project workflow request.
