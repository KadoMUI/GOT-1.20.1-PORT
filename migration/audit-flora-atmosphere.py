#!/usr/bin/env python3
"""Strict source/resource audit for the flora and atmosphere checkpoint."""

from __future__ import annotations

import json
import re
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "src/main/java"
RESOURCES = ROOT / "src/main/resources"


def require(condition: bool, message: str) -> None:
    if not condition:
        raise SystemExit(message)


blocks_source = (JAVA / "got/GOTBlocks.java").read_text(encoding="utf-8")
species_source = (JAVA / "got/world/flora/GOTTreeSpecies.java").read_text(encoding="utf-8")
catalog_source = (JAVA / "got/world/flora/GOTBiomeFloraCatalog.java").read_text(encoding="utf-8")
generator_source = (JAVA / "got/world/flora/GOTTreeGenerator.java").read_text(encoding="utf-8")
decorator_source = (JAVA / "got/world/flora/PlanetosBiomeDecorator.java").read_text(encoding="utf-8")
chunk_source = (JAVA / "got/world/terrain/PlanetosChunkGenerator.java").read_text(encoding="utf-8")
atmosphere_source = (JAVA / "got/client/GOTAtmosphereEvents.java").read_text(encoding="utf-8")

tree_ids = re.findall(r'RegistryObject<Block> ([A-Z0-9_]+)_SAPLING = .*?"([a-z0-9_]+)_sapling"', blocks_source)
require(len(tree_ids) == 36, f"Expected 36 custom saplings, found {len(tree_ids)}")
ids = [resource_id for _, resource_id in tree_ids]
require(len(set(ids)) == 36, "Duplicate custom sapling IDs")

sapling_bindings = re.findall(
    r'new GOTSaplingBlock\(GOTTreeSpecies\.([A-Z0-9_]+),', blocks_source
)
require(len(sapling_bindings) == 36, f"Expected 36 species-aware saplings, found {len(sapling_bindings)}")
require("new SaplingBlock(new GOTTreeGrower()" not in blocks_source,
        "Vanilla-oak sapling placeholder remains in GOTBlocks")
for constant, resource_id in tree_ids:
    require(constant in sapling_bindings, f"Sapling {resource_id} is not bound to {constant}")
    for suffix in ("LOG", "LEAVES"):
        require(f"{constant}_{suffix}" in blocks_source, f"Missing {constant}_{suffix}")
    require(re.search(rf'\b{constant}\("{resource_id}"', species_source) is not None,
            f"Missing GOTTreeSpecies entry for {resource_id}")

for tag_kind in ("blocks", "items"):
    for filename, suffix in (("logs.json", "log"), ("logs_that_burn.json", "log"),
                             ("leaves.json", "leaves"), ("saplings.json", "sapling")):
        path = RESOURCES / f"data/minecraft/tags/{tag_kind}/{filename}"
        require(path.is_file(), f"Missing {tag_kind} tag {filename}")
        values = set(json.loads(path.read_text(encoding="utf-8"))["values"])
        expected = {f"got:{tree_id}_{suffix}" for tree_id in ids}
        require(expected <= values, f"{tag_kind}/{filename} misses {sorted(expected - values)}")

for token in ("FOREST", "TAIGA", "JUNGLE", "SAVANNAH", "MARSHES", "AQUATIC"):
    require(f"case {token}" in catalog_source, f"Flora catalogue lacks {token} profile")
for token in ("WILLOW", "MANGROVE", "REDWOOD", "BAOBAB", "WEIRWOOD", "CHARRED"):
    require(token in catalog_source and token in species_source, f"Tree catalogue lacks {token}")
for shape in ("willow", "mangrove", "redwood", "giant", "dead"):
    require(f"{shape}(level" in generator_source, f"Tree generator lacks {shape} geometry")
for token in ("fallenLogs", "fallenLeaves", "berryBushes", "groundPlants", "aquaticPlants", "snowCover"):
    require(token in decorator_source, f"Decorator lacks {token}")
require("PlanetosBiomeDecorator.decorate(level, chunk, seed)" in chunk_source,
        "Planetos chunk generator does not invoke flora decoration")

biome_dir = RESOURCES / "data/got/worldgen/biome"
biomes = sorted(biome_dir.glob("*.json"))
require(len(biomes) == 187, f"Expected 187 biome JSON files, found {len(biomes)}")
for path in biomes:
    data = json.loads(path.read_text(encoding="utf-8"))
    require("effects" in data and isinstance(data.get("features"), list), f"Malformed biome {path.name}")

for biome_id in ("valyria", "valyria_volcano"):
    effects = json.loads((biome_dir / f"{biome_id}.json").read_text(encoding="utf-8"))["effects"]
    require(effects.get("fog_color") == 0x808080, f"{biome_id} fog is not legacy gray")
    require(effects.get("grass_color") == 0x808080, f"{biome_id} grass is not legacy gray")
for biome_id in ("shadow_land", "shadow_town", "shadow_mountains"):
    effects = json.loads((biome_dir / f"{biome_id}.json").read_text(encoding="utf-8"))["effects"]
    require(effects.get("fog_color") == 0 and effects.get("sky_color") == 0,
            f"{biome_id} atmosphere is not legacy black")
for token in ('equals("valyria")', 'equals("valyria_volcano")', 'equals("yeen")', "setNearPlaneDistance", "setFarPlaneDistance", "setCanceled(true)"):
    require(token in atmosphere_source, f"Atmosphere handler lacks {token}")

print(f"Flora/atmosphere audit passed: {len(ids)} tree species, {len(biomes)} biomes, 8 complete vanilla tags")
