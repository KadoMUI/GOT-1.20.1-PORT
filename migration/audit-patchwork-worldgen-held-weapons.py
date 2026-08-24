#!/usr/bin/env python3
"""Regression audit for the eleven worldgen and held-weapon patchwork fixes."""

import json
import re
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "src/main/java/got"
RESOURCES = ROOT / "src/main/resources/assets/got"


def source(relative):
    return (JAVA / relative).read_text(encoding="utf-8")


major = source("world/structure/major/MajorSchematicStructureGenerator.java")
assert re.search(r'CASTLE_BLACK,\s*"CastleBlack\.schem".*?0,\s*60,\s*0,\s*Rotation\.NONE', major, re.S)
assert re.search(r'QARTH,\s*"Qarth\.schem".*?0,\s*0,\s*21,\s*Rotation\.NONE', major, re.S)
assert re.search(r'VOLANTIS,\s*"Volantis\.schem".*?Rotation\.CLOCKWISE_90', major, re.S)
assert "if (state.isAir()) continue;" in major
assert "state = state.rotate(site.rotation())" in major
assert "terrain.structureAnchorHeight(site.anchorX(), site.anchorZ())" in major

waypoint = source("common/world/map/GOTWaypoint.java")
waypoint_info = source("common/world/map/GOTWaypointInfo.java")
assert "this == VOLANTIS ? -426 : 0" in waypoint
assert "waypoint.getCoordX() + GOTWaypoint.mapToWorldR(shiftX)" in waypoint_info

landmarks = source("world/terrain/PlanetosLandmarkTerrain.java")
for city in ("BRAAVOS", "LYS", "MYR", "TYROSH"):
    assert f"GOTWaypoint.{city}" in landmarks
assert "SEA_LEVEL - 8" in landmarks and "FEATHER = 32" in landmarks

sampler = source("world/terrain/PlanetosTerrainSampler.java")
assert "PlanetosLandmarkTerrain.applySeaCityMask" in sampler
assert "for (int dz = -6; dz <= 6; dz++)" in sampler
assert "for (int dx = -6; dx <= 6; dx++)" in sampler
assert "double deepWater" in sampler and "double inland" in sampler
assert "oceanSampleCount" in sampler

generator = source("world/terrain/PlanetosChunkGenerator.java")
assert "PlanetosLandmarkTerrain.isSeaCityWater(x, z)" in generator

mountains = source("world/terrain/PlanetosMountainAnchors.java")
assert "FootprintAnchor" in mountains
assert re.search(r'addFootprint\(result,\s*GOTWaypoint\.THE_EYRIE,\s*-47,\s*-144,\s*120,\s*165', mountains, re.S)

manifest_path = RESOURCES / "held_weapon_models.txt"
entries = []
for line in manifest_path.read_text(encoding="utf-8").splitlines():
    if not line or line.startswith("#"):
        continue
    item, model, scale = line.split("|")
    entries.append((item, model, int(scale)))
assert len(entries) == 124
assert len({item for item, _, _ in entries}) == 124
assert {scale for _, _, scale in entries} == {2, 3}

registered = set()
for path in (JAVA / "GOTItems.java", JAVA / "GOTEquipment.java"):
    registered.update(re.findall(r'ITEMS\.register\("([a-z0-9_]+)"', path.read_text(encoding="utf-8")))

mapped_registered = 0
for item, model, scale in entries:
    namespace, item_id = item.split(":", 1)
    model_namespace, model_path = model.split(":", 1)
    assert model_namespace == "got" and model_path.startswith("item/held/")
    model_json = RESOURCES / "models" / f"{model_path}.json"
    data = json.loads(model_json.read_text(encoding="utf-8"))
    texture = data["textures"]["layer0"]
    texture_namespace, texture_path = texture.split(":", 1)
    assert texture_namespace == "got"
    assert (RESOURCES / "textures" / f"{texture_path}.png").is_file()
    assert ("large-3x" in texture_path) == (scale == 3)

    if namespace == "got" and item_id in registered:
        mapped_registered += 1
        # The inventory model must remain on its compact texture; only the
        # baked hand context wrapper may point at the large model.
        base = RESOURCES / "models/item" / f"{item_id}.json"
        base_text = base.read_text(encoding="utf-8")
        assert "large-2x" not in base_text and "large-3x" not in base_text
        assert "vlarge-2x" not in base_text and "item/held/" not in base_text

assert mapped_registered == 112
vanilla_items = {item for item, _, _ in entries if item.startswith("minecraft:")}
assert vanilla_items == {
    "minecraft:wooden_sword", "minecraft:stone_sword", "minecraft:iron_sword",
    "minecraft:golden_sword", "minecraft:diamond_sword",
}

held_java = source("client/GOTHeldWeaponModels.java")
for context in (
    "FIRST_PERSON_LEFT_HAND", "FIRST_PERSON_RIGHT_HAND",
    "THIRD_PERSON_LEFT_HAND", "THIRD_PERSON_RIGHT_HAND",
):
    assert context in held_java
assert "return inventory.applyTransform(context, poseStack, leftHand)" in held_java
assert "poseStack.scale(heldScale, heldScale, heldScale)" in held_java

print("patchwork audit passed")
print("- authored major structures: global ignore-air paste policy")
print("- landmark placement: Castle Black +60 south; Volantis -426 west/90 degrees; Qarth +21 y")
print("- terrain: four sea-city exclusions, shaped Eyrie plateau, 48-block coastal shelf")
print(f"- held textures: {len(entries)} baked models; {mapped_registered} registered GOT weapons + 5 vanilla swords")
