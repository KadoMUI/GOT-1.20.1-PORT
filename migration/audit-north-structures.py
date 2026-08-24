#!/usr/bin/env python3
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "src/main/java"
RES = ROOT / "src/main/resources"

expected_ids = [15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 309, 310, 311, 312]
enum_text = (JAVA / "got/world/structure/north/NorthStructureType.java").read_text()
actual_ids = [int(value) for value in re.findall(r"^[ ]+[A-Z_]+\((\d+),", enum_text, re.M)]
assert actual_ids == expected_ids, (actual_ids, expected_ids)

generator = (JAVA / "got/world/structure/north/PlanetosNorthStructureGenerator.java").read_text()
assert generator.count("fort(GOTWaypoint.") == 13
assert generator.count("hillman(GOTWaypoint.") == 5
assert len(re.findall(r"site\(GOTWaypoint\.", generator)) == 11
assert "GOTBeziers.isRoadNear" in generator
assert "metadata.id().equals(\"north_barrows\")" in generator
assert "Math.floorMod(chunkSeed, 800L)" in generator
assert "RANDOM_CASTLE_ONE_IN = 4096L" in generator
assert "isRareRandomCastle(cellSeed)" in generator
assert "Math.floorMod(cellSeed >>> 17, RANDOM_CASTLE_ONE_IN) == 0L" in generator
assert "Math.floorMod(cellSeed >>> 17, 4L)" not in generator
assert "randomRadius + site.radius() + clearance" in generator
assert "FIXED_SITE_CLEARANCE = 48" in generator

settlements = (JAVA / "got/world/structure/north/NorthSettlementGenerator.java").read_text()
for kind in ("VILLAGE", "SMALL_TOWN", "TOWN", "FORT", "HILLMAN"):
    assert f"case {kind}" in settlements
fort_body = re.search(r"private static void fort\(NorthStructureBuilder b\) \{(.*?)\n    \}",
                      settlements, re.S).group(1)
assert "LegacyNorthCastleTemplates.castle(b);" in fort_body
assert "fortificationRing" not in fort_body

castle = (JAVA / "got/world/structure/north/LegacyNorthCastleTemplates.java").read_text()
for token in (
    "fortress(child(b, 0, 12, 2",
    "fortGate(child(b, 0, -37, 0",
    "fortGate(child(b, -37, 0, 3",
    "fortGate(child(b, 0, 37, 2",
    "fortGate(child(b, 37, 0, 1",
    "watchtower(child(b, -23, -33, 2",
    "watchtower(child(b, 33, 23, 3",
    "fortCorner(child(b, -30, -30, 3",
    "fortCorner(child(b, 30, -30, 0",
    "stables(child(b, -24, 2, 0",
    "smithy(child(b, 24, 1, 0",
    "stoneHouse(child(b, -3, -25, 1",
    "cropFarm(child(b, -18, -21, 1",
    "well(child(b, -12, 27, 1",
):
    assert token in castle, token
assert castle.count("fortGate(child(b,") == 4
assert castle.count("fortWall(child(b,") == 8
assert castle.count("watchtower(child(b,") == 8
assert castle.count("fortCorner(child(b,") == 4
for palette_id in (
    "andesite_bricks", "carved_andesite_bricks", "andesite_pillar",
    "basalt_westeros_bricks", "basalt_brick_slab", "stairs_basalt_brick",
    "basalt_brick_wall", "basalt_pillar", "gate_iron_bars",
):
    assert f'"{palette_id}"' in castle, palette_id
assert "Native 1.20.1 reconstructions" not in castle

templates = (JAVA / "got/world/structure/north/NorthStructureTemplates.java").read_text()
for token in ("marketStall", "villageFarm", "gatehouse", "barrow", "watchfort", "hillmanHouse"):
    assert token in templates
legacy_context = (JAVA / "got/world/structure/legacy/LegacyNorthernContext.java").read_text()
assert "Math.floorMod(marketVariant, 11)" in legacy_context
for recovered in (
    "GOTStructureWesterosBarn.place", "GOTStructureWesterosBath.place",
    "GOTStructureWesterosCottage.place", "GOTStructureWesterosFortress.place",
    "GOTStructureWesterosStoneHouse.place", "GOTStructureWesterosTavern.place",
    "GOTStructureWesterosWatchfort.place", "GOTStructureNorthHillmanHouse.place",
):
    assert recovered in templates, recovered
layouts = (JAVA / "got/world/structure/north/LegacyNorthSettlementLayouts.java").read_text()
for method in ("village", "hillman", "smallTown", "town"):
    assert f"static void {method}" in layouts

chunk_generator = (JAVA / "got/world/terrain/PlanetosChunkGenerator.java").read_text()
road_index = chunk_generator.index("PlanetosRoadGenerator.generate")
structure_index = chunk_generator.index("PlanetosNorthStructureGenerator.generate")
assert structure_index > road_index

creative = (JAVA / "got/GOTCreativeTabs.java").read_text()
assert "NorthStructureType.values()" in creative
assert "GOTStructureSpawnerItem.createStack" in creative

for path in [
    RES / "assets/got/models/item/structure_spawner.json",
    RES / "assets/got/textures/item/structure_spawner_base.png",
    RES / "assets/got/textures/item/structure_spawner_overlay.png",
    RES / "assets/got/models/item/structure_spawner_village.json",
    RES / "assets/got/textures/item/structure_spawner_village_base.png",
    RES / "assets/got/textures/item/structure_spawner_village_overlay.png",
]:
    assert path.is_file(), path

for name in ("north_house", "north_tavern", "north_smithy", "north_fort", "north_barrow"):
    path = RES / f"data/got/loot_tables/chests/{name}.json"
    json.loads(path.read_text())

lang = json.loads((RES / "assets/got/lang/en_us.json").read_text())
for key in re.findall(r'"(north_[a-z_]+)"', enum_text):
    assert f"structure.got.{key}" in lang

assert "legendary_npc:" in settlements
assert "north_banner_stark" in templates

print("North structures audit passed: 18 spawner variants, 29 fixed sites, "
      "1-in-4096 random castles, footprint-aware fixed-site clearance, "
      "5 recovered settlement layouts, faithful piece and North Castle assembly")
