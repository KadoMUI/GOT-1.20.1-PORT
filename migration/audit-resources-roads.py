#!/usr/bin/env python3
"""Repeatable source/resource audit for milestone 0.5I."""

from pathlib import Path
import json
import re

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "src/main/java"
RES = ROOT / "src/main/resources"

ORE_IDS = (
    "sulfur_ore", "saltpeter_ore", "salt_ore", "glowstone_ore",
    "cobalt_ore", "valyrian_ore", "topaz_ore", "amethyst_ore",
    "sapphire_ore", "ruby_ore", "amber_ore", "diamond_vein",
    "opal_ore", "emerald_vein",
)


def require(condition: bool, message: str) -> None:
    if not condition:
        raise SystemExit(f"FAIL: {message}")


blocks = (JAVA / "got/GOTBlocks.java").read_text(encoding="utf-8")
resources = (JAVA / "got/world/resource/PlanetosResourceGenerator.java").read_text(encoding="utf-8")
roads = (JAVA / "got/world/road/PlanetosRoadGenerator.java").read_text(encoding="utf-8")
chunk = (JAVA / "got/world/terrain/PlanetosChunkGenerator.java").read_text(encoding="utf-8")
beziers = (JAVA / "got/common/world/map/GOTBezierData.java").read_text(encoding="utf-8")
lang = json.loads((RES / "assets/got/lang/en_us.json").read_text(encoding="utf-8"))

require("PlanetosResourceGenerator.generate(level, chunk, seed);" in chunk,
        "resource pass is not wired into Planetos decoration")
require("PlanetosBiomeDecorator.decorate(level, chunk, seed);" in chunk,
        "flora pass was displaced")
require("PlanetosRoadGenerator.generate(level, chunk, seed);" in chunk,
        "road pass is not wired after flora")
require(chunk.index("PlanetosResourceGenerator.generate") < chunk.index("PlanetosBiomeDecorator.decorate")
        < chunk.index("PlanetosRoadGenerator.generate"), "decoration order must be resources, flora, roads")

for ore in ORE_IDS:
    constant = ore.upper()
    require(f'("{ore}"' in blocks, f"missing block registration for got:{ore}")
    for relative in (
        f"assets/got/blockstates/{ore}.json",
        f"assets/got/models/block/{ore}.json",
        f"assets/got/models/item/{ore}.json",
        f"assets/got/textures/block/{ore}.png",
        f"data/got/loot_tables/blocks/{ore}.json",
    ):
        path = RES / relative
        require(path.is_file(), f"missing {relative}")
        if path.suffix == ".json":
            json.loads(path.read_text(encoding="utf-8"))
    require(f"block.got.{ore}" in lang, f"missing translation for got:{ore}")

for token in (
    "40.0F, 32", "15.0F * factors.ore(), 8", "8.0F * factors.ore(), 4",
    "GOTBlocks.SULFUR_ORE", "GOTBlocks.SALTPETER_ORE", "GOTBlocks.SALT_ORE",
    "GOTBlocks.COBALT_ORE", "GOTBlocks.VALYRIAN_ORE", "stormlands_tarth",
    "sothoryos_", "ulthos_", "valyria_volcano",
):
    require(token in resources, f"resource catalogue lost required rule {token}")

road_routes = len(re.findall(r"Type\.ROAD", beziers))
wall_routes = len(re.findall(r"Type\.WALL", beziers))
linkers = len(re.findall(r"registerLinker(?:Auto|AutoInv)?\(", beziers))
require(road_routes == 170, f"expected 170 authored road routes, found {road_routes}")
require(wall_routes == 5, f"expected 5 authored wall routes, found {wall_routes}")
require(linkers == 112, f"expected 112 authored linkers, found {linkers}")

for style in ("DIRTY", "PAVING", "SANDY", "COBBLE", "ASSHAI", "SNOWY", "SOTHORYOS"):
    require(style in roads, f"missing road style {style}")
for behavior in ("isRouteAt", "OAK_PLANKS", "OAK_FENCE", "OCEAN_FLOOR_WG", "clearAbove"):
    require(behavior in roads, f"missing road/bridge behavior {behavior}")
require("surfaceHeight" not in roads, "road pass must not alter or resample terrain elevation")

print("Resources/Roads audit passed")
print(f"- legacy ore/gem blocks: {len(ORE_IDS)}")
print("- common deposits: soils, seven country rocks, metals, chemicals, and eight gems")
print("- regional deposits: mountains, Tarth, oceans, Sothoryos, Ulthos, Valyria")
print(f"- authored map network: {road_routes} roads + {linkers} linkers ({wall_routes} walls preserved)")
print("- road styles: dirty, paved, sandy, cobbled, Asshai, snowy, Sothoryosi")
print("- water crossings: deck, edge rail, and support generation")
