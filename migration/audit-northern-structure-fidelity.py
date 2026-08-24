#!/usr/bin/env python3
from pathlib import Path
import re

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "src/main/java/got"
GENERATED = JAVA / "world/structure/legacy/generated"

expected = {
    "GOTStructureWesterosBarn", "GOTStructureWesterosBath", "GOTStructureWesterosCottage",
    "GOTStructureWesterosFortress", "GOTStructureWesterosGatehouse", "GOTStructureWesterosHouse",
    "GOTStructureWesterosLampPost", "GOTStructureWesterosMarketStall", "GOTStructureWesterosObelisk",
    "GOTStructureWesterosSmithy", "GOTStructureWesterosStables", "GOTStructureWesterosStoneHouse",
    "GOTStructureWesterosTavern", "GOTStructureWesterosTower", "GOTStructureWesterosTownBench",
    "GOTStructureWesterosTownGarden", "GOTStructureWesterosTownTrees", "GOTStructureWesterosTownWall",
    "GOTStructureWesterosVillageFarm",
    "GOTStructureWesterosVillageFarm$Animals", "GOTStructureWesterosVillageFarm$Crops",
    "GOTStructureWesterosVillageFarm$Tree", "GOTStructureWesterosVillageSign",
    "GOTStructureWesterosWatchfort", "GOTStructureWesterosWatchtower", "GOTStructureWesterosWell",
    "GOTStructureGiftCastle", "GOTStructureGiftGate", "GOTStructureGiftHouse",
    "GOTStructureGiftHouseSmall", "GOTStructureGiftSmithy", "GOTStructureGiftStables",
    "GOTStructureGiftVillageLight", "GOTStructureGiftVillagePalisade", "GOTStructureGiftWell",
    "GOTStructureWildlingBarn", "GOTStructureWildlingChieftainHouse", "GOTStructureWildlingHouse",
    "GOTStructureWildlingKeep", "GOTStructureThennChieftainHouse", "GOTStructureThennHouse",
    "GOTStructureNorthHillmanChieftainHouse", "GOTStructureNorthHillmanHouse",
}
actual = {path.stem for path in GENERATED.glob("*.java")}
assert actual == expected, (sorted(expected - actual), sorted(actual - expected))

for name in expected:
    source = (GENERATED / f"{name}.java").read_text()
    assert "Exact coordinate body mechanically recovered" in source, name
    assert "public static void place" in source, name
    assert "setBlockAndMetadata" in source or "super.generate" in source, name

fingerprints = {
    "GOTStructureWesterosTavern": ["-8; i16 <= 12", "tavernNameSign", "placeMugOrPlate"],
    "GOTStructureWesterosFortress": ["-14; i11 <= 14", "commandTable", "brickCarved"],
    "GOTStructureGiftCastle": ["-12; i15 <= 12", "spawnLegendaryMobs", "isAbandoned"],
    "GOTStructureGiftGate": ["originZ -= 7", "brickIce", "wallTop"],
    "GOTStructureWildlingHouse": ["-4; i1 <= 4", "thatchFloor", "isTramp"],
    "GOTStructureWildlingChieftainHouse": ["isHardhome", "bronzeBars", "hearth"],
    "GOTStructureWildlingKeep": ["chestCoords", "gateWooden", "BEYOND_WALL"],
    "GOTStructureThennHouse": ["isBlacksmith", "banner_THENN", "goldRing"],
    "GOTStructureWesterosTownWall": ["xMinInner", "Math.floorMod(i1, 4)", "rockWallBlock"],
}
for name, tokens in fingerprints.items():
    source = (GENERATED / f"{name}.java").read_text()
    for token in tokens:
        assert token in source, (name, token)

north = (JAVA / "world/structure/north/NorthStructureTemplates.java").read_text()
night = (JAVA / "world/structure/nightwatch/NightWatchStructureTemplates.java").read_text()
wild = (JAVA / "world/structure/wildling/WildlingStructureTemplates.java").read_text()
for source, required in (
    (north, 14), (night, 4), (wild, 4),
):
    assert source.count("legacy.generated.") >= required

layouts = (JAVA / "world/structure/north/LegacyNorthSettlementLayouts.java").read_text()
for token in ("setupVillage", "setupHillman", "setupSmallTown", "setupTown"):
    assert token not in layouts  # modern method names only; generated from these exact bodies
for token in ("village(", "hillman(", "smallTown(", "town(", "TOWN_WALL_LEFT_END_SHORT", "RANDOM_FARM"):
    assert token in layouts, token
assert "fortificationRing" not in layouts
assert layouts.count("GOTStructureWesterosTownWall.place") == 7

context = (JAVA / "world/structure/legacy/LegacyNorthernContext.java").read_text()
for token in ("carved_basalt_westeros_bricks", "brick_ice_bricks", "rottenLog",
              "Math.floorMod(marketVariant, 11)", "isAbandoned"):
    assert token in context, token

assert not any("reconstruction" in (GENERATED / f"{name}.java").read_text().lower() for name in expected)
print(f"Northern fidelity audit passed: {len(expected)} recovered procedural pieces and exact regional routing")
