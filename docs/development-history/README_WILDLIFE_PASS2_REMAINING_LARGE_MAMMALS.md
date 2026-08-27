# Project Thrones 1.20.1 — Wildlife Pass 2: Remaining Large Mammals

Restores the remaining legacy large-mammal wildlife family from the 1.7.10 GOT build, excluding creatures already completed in Wildlife Pass 1.

## Entities restored
- Elephant — rideable GOT mount family, heavy knockback attack, elephant meat drops
- Mammoth — cold-region rideable GOT mount family, stronger heavy attack, fur/meat drops, freeze immune
- Giraffe — rideable GOT mount family
- Lion / Lioness — predatory cats using the original separate skins
- Oryx / White Oryx — defensive herd animals; White Oryx uses three legacy skins and is rarer
- Dik-dik — small passive herd animal using three legacy skins
- Walrus — defensive cold-coast herd animal, fur + walrus lard drops, freeze immune
- Beaver — defensive forest/river animal, beaver tail + meat drops
- Shadowcat — large neutral/defensive mountain predator, legacy 50 HP / 5 damage baseline

## Spawning
All entities use Forge biome modifiers and dedicated Planetos biome tags. Mammoths and walruses are northern/cold fauna; Shadowcats favor mountains; Elephants, Giraffes, Lions, Oryx and Dik-dik populate warm Essosi/Sothoryosi biomes; Beavers favor forested river-country.

## Rendering
Uses the existing legacy texture corpus and the reusable GOT wildlife model/renderer framework. White Oryx and Dik-dik deterministically choose one of their three legacy skins.

## Build note
JSON resources validate successfully. A definitive Gradle compile could not be performed in the sandbox because the Gradle 8.8 wrapper distribution is not cached and outbound access to services.gradle.org is unavailable.
