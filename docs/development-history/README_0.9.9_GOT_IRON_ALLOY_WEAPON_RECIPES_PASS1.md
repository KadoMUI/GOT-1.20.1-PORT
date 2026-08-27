# Project Thrones 0.9.9 — GOT Iron + Alloy Steel Weapon Recipes Pass 1

Implemented the 1.0 weapon recipe set on GOT faction crafting tables.

## Regional sword rule
- Westeros tables: iron recipe -> `minecraft:iron_sword`; alloy recipe -> `got:alloy_steel_sword`
- Essos tables: iron recipe -> `got:iron_scimitar`; alloy recipe -> `got:alloy_steel_scimitar`

## Recipes implemented for both Iron and Alloy Steel
- Sword / Scimitar
- Hammer
- Spear
- Longsword
- Greatsword (requires the matching-material Longsword)
- Polearm
- Pike
- Dagger
- Poisoned Dagger (direct shaped recipe)

Existing shapeless dagger + `got:bottle_poison` recipes remain valid for Iron and Alloy Steel (and the other legacy dagger materials already supported by the mod).

## Missing item repair
The source did not contain `got:iron_polearm` or `got:alloy_steel_polearm`, so both were registered. No dedicated source textures existed for these two items; their item models currently reference the existing Essos polearm texture so they render functionally pending dedicated art.

## Validation note
A Gradle build was attempted, but this execution environment could not reach `services.gradle.org` to download Gradle 8.8. The failure was environmental (`UnknownHostException`), not a compile error returned by the project.
