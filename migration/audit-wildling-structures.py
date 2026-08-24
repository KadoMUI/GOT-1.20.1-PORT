#!/usr/bin/env python3
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "src/main/java/got"
RES = ROOT / "src/main/resources"

type_src = (JAVA / "world/structure/wildling/WildlingStructureType.java").read_text()
expected = {
    "HOUSE": 7,
    "CHIEFTAIN_HOUSE": 8,
    "THENN_HOUSE": 9,
    "THENN_CHIEFTAIN_HOUSE": 10,
    "SETTLEMENT": 306,
    "THENN_SETTLEMENT": 307,
}
for name, legacy_id in expected.items():
    assert re.search(rf"\b{name}\({legacy_id},", type_src), (name, legacy_id)

templates = (JAVA / "world/structure/wildling/WildlingStructureTemplates.java").read_text()
settlements = (JAVA / "world/structure/wildling/WildlingSettlementGenerator.java").read_text()
generator = (JAVA / "world/structure/wildling/PlanetosWildlingStructureGenerator.java").read_text()
spawner = (JAVA / "GOTStructureSpawnerItem.java").read_text()
tabs = (JAVA / "GOTCreativeTabs.java").read_text()
chunk = (JAVA / "world/terrain/PlanetosChunkGenerator.java").read_text()

for method in ("crastersKeep", "crastersBarn", "whitetree", "chieftainHouse", "house"):
    assert f" {method}(" in templates, method
for kind in ("DEFAULT", "HARDHOME", "CRASTER", "THENN"):
    assert f"case {kind}" in settlements, kind
for waypoint in ("CRASTERS_KEEP", "HARDHOME", "WHITETREE"):
    assert f"GOTWaypoint.{waypoint}" in generator, waypoint
for biome in ("frozen_shore", "haunted_forest", "thenn_land"):
    assert f'"{biome}"' in generator, biome
for role in ("mance_rayder", "tormund", "ygritte", "craster", "craster_wife", "benjen_stark"):
    assert f'"{role}"' in templates, role
for role in ("wildling_warrior_respawner", "wildling_ranged_respawner",
             "thenn_warrior_respawner", "thenn_ranged_respawner"):
    assert f'"{role}"' in settlements, role

assert "WildlingStructureType wildling" in spawner
assert "PlanetosWildlingStructureGenerator.spawn" in spawner
assert "WildlingStructureType.values()" in tabs
assert "PlanetosWildlingStructureGenerator.generate" in chunk

language = json.loads((RES / "assets/got/lang/en_us.json").read_text())
for key in re.findall(r'"([a-z_]+)", "(?:Wildling|Thenn)', type_src):
    assert f"structure.got.{key}" in language, key
loot = json.loads((RES / "data/got/loot_tables/chests/beyond_wall.json").read_text())
assert loot["type"] == "minecraft:chest" and loot["pools"]

print("Wildling structure audit passed:")
print("  6 legacy structure-spawner variants")
print("  Wildling and Thenn random settlements")
print("  Hardhome, Craster's Keep, and Whitetree fixed sites")
print("  Beyond-the-Wall loot and future population markers")
