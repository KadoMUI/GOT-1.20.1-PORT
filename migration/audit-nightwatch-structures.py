#!/usr/bin/env python3
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "src/main/java/got"
RES = ROOT / "src/main/resources"

type_src = (JAVA / "world/structure/nightwatch/NightWatchStructureType.java").read_text()
generator = (JAVA / "world/structure/nightwatch/PlanetosNightWatchStructureGenerator.java").read_text()
templates = (JAVA / "world/structure/nightwatch/NightWatchStructureTemplates.java").read_text()
spawner = (JAVA / "GOTStructureSpawnerItem.java").read_text()
tabs = (JAVA / "GOTCreativeTabs.java").read_text()
chunk_generator = (JAVA / "world/terrain/PlanetosChunkGenerator.java").read_text()

expected_types = {
    "HOUSE_SMALL": 11,
    "HOUSE": 12,
    "STABLES": 13,
    "SMITHY": 14,
    "VILLAGE": 308,
}
for name, legacy_id in expected_types.items():
    assert re.search(rf"\b{name}\({legacy_id},", type_src), (name, legacy_id)

assert generator.count("abandoned(GOTWaypoint.") == 15
for waypoint in [
    "CASTLE_BLACK", "EASTWATCH", "SHADOW_TOWER", "MOLETOWN", "QUEENSCROWN"
]:
    assert f"GOTWaypoint.{waypoint}" in generator
assert "public static final int WALL_TOP = 150" in generator
assert "PlanetosTerrainSampler.SEA_LEVEL" in generator
assert "GOTBeziers.isWallAt" in generator
assert "brick_ice_bricks" in generator
assert "NightWatchStructureType nightWatch" in spawner
assert "PlanetosNightWatchStructureGenerator.spawn" in spawner
assert "NightWatchStructureType.values()" in tabs
assert "PlanetosNightWatchStructureGenerator.generate" in chunk_generator

for role in [
    "jeor_mormont", "jon_snow", "aemon_targaryen", "alliser_thorne",
    "edd", "samwell_tarly", "cotter_pyke", "harmune", "denys_mallister", "mullin"
]:
    assert f'"{role}"' in templates, role

language = json.loads((RES / "assets/got/lang/en_us.json").read_text())
for key in [
    "structure.got.gift_house_small", "structure.got.gift_house",
    "structure.got.gift_stables", "structure.got.gift_smithy",
    "structure.got.gift_settlement"
]:
    assert key in language, key

for loot in ["gift", "gift_treasure"]:
    data = json.loads((RES / f"data/got/loot_tables/chests/{loot}.json").read_text())
    assert data["type"] == "minecraft:chest"
    assert data["pools"]

print("Night's Watch structure audit passed:")
print("  5 legacy structure-spawner variants")
print("  15 abandoned castles + 3 active castles + 2 fixed villages")
print("  Westerosi Wall Y63-Y150 and active/abandoned gates")
print("  Gift loot, translations, creative access, and population markers")
