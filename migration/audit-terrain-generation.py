#!/usr/bin/env python3
"""Strict source/data audit for the Planetos terrain-generation checkpoint."""

from __future__ import annotations

import csv
import json
import re
import sys
from pathlib import Path


project = Path(__file__).resolve().parents[1]
java = project / "src/main/java/got"
resources = project / "src/main/resources"
errors: list[str] = []


biomes_source = (java / "world/biome/GOTBiomes.java").read_text(encoding="utf-8")
biome_entries = re.findall(
    r'ResourceKey<Biome>\s+[A-Z0-9_]+\s*=\s*register\("([a-z0-9_]+)",\s*GOTBiomePreset\.([A-Z_]+)\);',
    biomes_source,
)
if len(biome_entries) != 187:
    errors.append(f"GOTBiomes contains {len(biome_entries)} entries, expected 187")
biome_ids = [entry[0] for entry in biome_entries]
if len(biome_ids) != len(set(biome_ids)):
    errors.append("duplicate GOT biome registry IDs")

biome_json_root = resources / "data/got/worldgen/biome"
biome_json_ids = {path.stem for path in biome_json_root.glob("*.json")}
if biome_json_ids != set(biome_ids):
    errors.append("biome JSON set does not exactly match the Java catalogue")

with (resources / "data/got/worldgen/legacy_biome_map.csv").open(newline="", encoding="utf-8") as handle:
    map_rows = list(csv.DictReader(handle))
if len(map_rows) != 185:
    errors.append(f"legacy biome color table contains {len(map_rows)} rows, expected 185")
if len({row["rgb"] for row in map_rows}) != len(map_rows):
    errors.append("legacy biome color table contains duplicate RGB values")
unknown_map_ids = sorted({row["registry_id"] for row in map_rows} - set(biome_ids))
if unknown_map_ids:
    errors.append(f"legacy biome map references unknown IDs: {', '.join(unknown_map_ids)}")

for path in (
    resources / "data/got/dimension/planetos.json",
    resources / "data/got/worldgen/world_preset/planetos.json",
):
    data = json.loads(path.read_text(encoding="utf-8"))
    generator = data["generator"] if "generator" in data else data["dimensions"]["minecraft:overworld"]["generator"]
    if generator.get("type") != "got:planetos":
        errors.append(f"{path.relative_to(project)} does not select got:planetos")
    if "settings" in generator:
        errors.append(f"{path.relative_to(project)} still carries minecraft:overworld noise settings")
    source = generator.get("biome_source", {})
    if source.get("type") != "got:planetos":
        errors.append(f"{path.relative_to(project)} lost the atlas biome source")
    listed = [value.removeprefix("got:") for value in source.get("biomes", [])]
    if listed != biome_ids:
        errors.append(f"{path.relative_to(project)} biome order differs from GOTBiomes")

required_tokens = {
    "world/terrain/GOTTerrainRegistries.java": [
        "Registries.CHUNK_GENERATOR", "PlanetosChunkGenerator.CODEC", "CHUNK_GENERATORS.register(bus)",
    ],
    "world/terrain/PlanetosChunkGenerator.java": [
        "fillFromNoise", "MIN_Y = -64", "GEN_DEPTH = 384", "SEA_LEVEL",
        "Blocks.DEEPSLATE", "Blocks.WATER", "Blocks.BEDROCK", "isCave",
        "OCEAN_FLOOR_WG", "WORLD_SURFACE_WG", "PlanetosSurfaceResolver.top",
        "isAquatic(metadata) || PlanetosLandmarkTerrain.isSeaCityWater",
        "GOTLegacyTerrainCatalog.isAquatic(metadata.id())",
    ],
    "world/terrain/PlanetosTerrainSampler.java": [
        "blendBiomes", "dx = -6", "dz = -6", "fractal3D", "surfaceDetail",
        "PlanetosMountainAnchors.heightBoost", "oceanWeight", "smoothStep01",
        "PlanetosTerrainProfile.of(nearby, null)",
        "Secondary variants are deliberately excluded from height",
        "structureAnchorHeight", "PlanetosLandmarkTerrain.applySeaCityMask",
    ],
    "world/terrain/PlanetosMountainAnchors.java": [
        "int[][] kingSpears", "GOTWaypoint.VAES_DOTHRAK", "GOTWaypoint.THE_EYRIE",
        "GOTWaypoint.DRAGONSTONE", "return List.copyOf(result)",
    ],
    "world/biome/GOTLegacyTerrainCatalog.java": [
        '"king_spears"', '"dothraki_sea_hills"', '"riverlands_forest"',
        '"dorne_mesa"', '"dragonstone"', '"stepstones"',
    ],
    "world/biome/DefaultPlanetosGenLayerProvider.java": [
        'id.equals("river")', 'id.equals("lake")', "isBakedLandform",
    ],
    "world/GOTPlanetosSpawnEvents.java": [
        "PlayerLoggedInEvent", "PlanetosChunkGenerator", "GOTWaypoint.WINTERFELL",
        "GOTDimensions.PLANETOS", "setRespawnPosition", "GOTInitialPlanetosSpawn",
    ],
    "common/fasttravel/GOTFastTravelManager.java": [
        "public static BlockPos findSafeDestination",
    ],
}
for relative, tokens in required_tokens.items():
    path = java / relative
    if not path.is_file():
        errors.append(f"missing source {relative}")
        continue
    text = path.read_text(encoding="utf-8")
    for token in tokens:
        if token not in text:
            errors.append(f"{relative} missing integration token {token!r}")

variant_source = (java / "world/biome/DefaultPlanetosGenLayerProvider.java").read_text(encoding="utf-8")
if 'contains("river")' in variant_source:
    errors.append("variant selector still misclassifies Riverlands by substring")

sampler_source = (java / "world/terrain/PlanetosTerrainSampler.java").read_text(encoding="utf-8")
if "PlanetosBiomeManager.getVariant(" in sampler_source:
    errors.append("terrain sampler still lets cell-based secondary variants alter elevation")
if "if (profile.ocean())" in sampler_source:
    errors.append("terrain sampler still applies the center ocean cap as a hard height switch")

generator_source = (java / "world/terrain/PlanetosChunkGenerator.java").read_text(encoding="utf-8")
if re.search(r"if \(y <= PlanetosTerrainSampler\.SEA_LEVEL\)\s*\{", generator_source):
    errors.append("chunk generator still floods low non-aquatic land columns")

mountain_source = (java / "world/terrain/PlanetosMountainAnchors.java").read_text(encoding="utf-8")
king_block = mountain_source.split("int[][] kingSpears = {", 1)[-1].split("};", 1)[0]
king_count = len(re.findall(r"\{\d+,\d+}", king_block))
waypoint_count = len(re.findall(r"addWaypoint\(result,", mountain_source))
literal_map_count = len(re.findall(r"addMap\(result,\s*\d", mountain_source))
footprint_count = len(re.findall(r"addFootprint\(result,\s*GOTWaypoint\.", mountain_source))
mountain_count = king_count + waypoint_count + literal_map_count + footprint_count
if mountain_count != 97:
    errors.append(f"fixed mountain catalogue contains {mountain_count} anchors, expected 97")

corrected_presets = {
    "dothraki_sea": "SAVANNAH",
    "dothraki_sea_forest": "FOREST",
    "dothraki_sea_hills": "SAVANNAH",
    "riverlands": "MIDERATE_PLAINS",
    "riverlands_forest": "FOREST",
    "king_spears": "AQUATIC",
    "dorne_mountains": "MOUNTAINS",
    "arryn_mountains_foothills": "MIDERATE_PLAINS",
}
actual_presets = dict(biome_entries)
for biome_id, expected in corrected_presets.items():
    if actual_presets.get(biome_id) != expected:
        errors.append(f"{biome_id} preset is {actual_presets.get(biome_id)}, expected {expected}")

for path in resources.rglob("*.json"):
    try:
        json.loads(path.read_text(encoding="utf-8"))
    except Exception as exc:
        errors.append(f"invalid JSON {path.relative_to(project)}: {exc}")

if errors:
    print("Terrain-generation audit failed:")
    for error in errors:
        print(f"- {error}")
    sys.exit(1)

print("Terrain-generation audit passed")
print(f"- atlas-backed biomes: {len(biome_ids)}")
print(f"- legacy biome colors: {len(map_rows)}")
print("- generator: got:planetos (custom chunk fill)")
print("- vertical range: -64..319; sea level: 63")
print("- terrain: blended legacy heights + smoothed coastal shelves + caves + mapped water + bedrock")
print("- grid transitions: atlas landforms blend; cell-based variants do not alter height")
print("- hydrology: only authored lake, river and ocean biomes flood to sea level")
print(f"- fixed landmark mountain anchors: {mountain_count}")
print("- known geography corrections: Dothraki Sea, Riverlands, King Spears")
print("- Planetos preset entry: one-time Winterfell spawn in got:planetos")
