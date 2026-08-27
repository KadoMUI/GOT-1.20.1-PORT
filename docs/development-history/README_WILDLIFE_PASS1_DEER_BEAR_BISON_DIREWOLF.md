# Project Thrones 1.20.1 — Wildlife Pass 1

Restores the first legacy GOT wildlife group from the 24.08.29 1.7.10 JAR.

Implemented entity IDs:
- `got:deer`
- `got:bear`
- `got:snow_bear`
- `got:bison`
- `got:white_bison`
- `got:direwolf`

## Legacy behavior restored
- Deer remains in the GOT horse/mount family and can use modern horse taming/riding infrastructure.
- Deer uses the two legacy deer skins and drops leather plus raw/cooked venison.
- Bears use the three legacy bear skins, 40 HP / 4 attack baseline, retaliatory neutral AI, breeding and fur drops.
- Snow Bears use the legacy polar-bear texture, 50 HP / 5 attack, freeze immunity and bear retaliation behavior.
- Bison use four legacy skins, 30 HP / 4 attack, retaliatory melee behavior, knockback, leather/beef and horn drops.
- White Bison use two legacy skins and drop `got:white_bison_horn` instead of the ordinary horn.
- Direwolves use the legacy texture, 50 HP / 5 attack, neutral retaliation, adult defense of nearby young, breeding and fur drops.

## Natural spawning
Forge biome modifiers and GOT biome tags were added for all six entities. Spawn weights are based on recovered 1.7.10 preset values where available: deer 30, forest/northern bison 15–20, bears 10–15, frost bears, and Haunted Forest direwolves 20. Modern tags extend the same ecological rules to equivalent Planetos biomes.

## Rendering
A reusable modern quadruped model/renderer framework was added for Deer, Bear, Bison and Direwolf silhouettes. Existing 1.7.10 texture assets are reused directly; no replacement texture art was generated.

## Build note
The sandbox cannot run the definitive Gradle compile because the wrapper requires Gradle 8.8 from services.gradle.org and outbound network is unavailable. JSON resources were parsed successfully and the source received a static integration audit. Run `gradlew build` locally for the definitive compile/runtime check.
