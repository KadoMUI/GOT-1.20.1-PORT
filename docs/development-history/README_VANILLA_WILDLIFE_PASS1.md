# Project Thrones 1.20.1 — Vanilla Wildlife Pass 1

This pass restores the legacy wildlife concepts that are better served by modern vanilla Minecraft rather than duplicate GOT entity classes.

## Added Planetos natural spawning

- `minecraft:rabbit`
  - Temperate/cold Westeros, the North/Beyond-the-Wall margins, Ibben/Mossovy, temperate Essos, Dorne/Lhazar, Dothraki/Jogos Nhai grasslands and similar suitable land biomes.
  - Excluded from tropical Sothoryos/Ulthos and the hottest/wettest jungle regions.
- `minecraft:cod`
  - Planetos oceans/seas and selected cool/temperate coastal biomes.
- `minecraft:salmon`
  - Rivers/lakes and cool northern waterways/forested river regions.
- `minecraft:tropical_fish`
  - Warm southern/tropical waters and water pockets in Summer Isles, Sothoryos, southern Yi Ti/Volantis, Ulthos, and Slaver's Bay-style hot regions.
- `minecraft:pufferfish`
  - Rarer subset of the warmest tropical/mangrove/marsh waters.

## Architecture

Implemented entirely through Forge `add_spawns` biome modifiers plus GOT biome tags. No duplicate entity classes or custom tick-based spawner were added. This keeps normal Minecraft population caps, despawning, spawn placement checks, and compatibility behavior.

Files:
- `data/got/forge/biome_modifier/vanilla_rabbits.json`
- `data/got/forge/biome_modifier/vanilla_cod.json`
- `data/got/forge/biome_modifier/vanilla_salmon.json`
- `data/got/forge/biome_modifier/vanilla_tropical_fish.json`
- `data/got/forge/biome_modifier/vanilla_pufferfish.json`
- corresponding `data/got/tags/worldgen/biome/spawns_vanilla_*.json`

The distributions are deliberately data-driven so weights and biome membership can be adjusted later without touching Java.
