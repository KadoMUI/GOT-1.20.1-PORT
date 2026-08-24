#!/usr/bin/env python3
from pathlib import Path
import json
import re

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "src/main/java/got"
RES = ROOT / "src/main/resources"

roles_source = (JAVA / "npc/WesterlandsNpcRole.java").read_text()
catalogue = roles_source.split("public enum Gender", 1)[0]
roles = re.findall(r'^\s{4}([A-Z][A-Z0-9_]*)\("([a-z0-9_]+)"', catalogue, re.M)
assert len(roles) == 36, len(roles)
assert roles[21] == ("ADDAM_MARBRAND", "addam_marbrand")
assert len(roles[:21]) == 21
assert len(roles[21:]) == 15
role_ids = {role_id for _, role_id in roles}
assert len(role_ids) == 36

ordinary = {
    "westerlands_man", "westerlands_levyman", "westerlands_levyman_archer",
    "westerlands_soldier", "westerlands_soldier_archer", "westerlands_guard",
    "westerlands_banner_bearer", "westerlands_captain", "westerlands_blacksmith",
    "westerlands_goldsmith", "westerlands_farmer", "westerlands_farmhand",
    "westerlands_bartender", "westerlands_miner", "westerlands_lumberman",
    "westerlands_mason", "westerlands_brewer", "westerlands_florist",
    "westerlands_butcher", "westerlands_fishmonger", "westerlands_baker",
}
assert ordinary <= role_ids

fixed_named = {
    "addam_marbrand", "quenten_banefort", "tywin_lannister", "qyburn",
    "gregor_clegane", "polliver", "harys_swyft", "lyle_crakehall",
    "sebaston_farman", "forley_prester", "leo_lefford", "tytos_brax",
    "kevan_lannister", "daven_lannister", "amory_lorch",
}
assert fixed_named <= role_ids
assert not ({"cersei_lannister", "jaime_lannister", "tyrion_lannister",
             "sandor_clegane", "ilyn_payne", "lancel_lannister",
             "podrick_payne"} & role_ids)

legendary_dir = RES / "assets/got/textures/entity/legendary"
for name in fixed_named - {"gregor_clegane"}:
    assert (legendary_dir / f"{name}.png").is_file(), name
for suffix in (1, 2, 3):
    assert (legendary_dir / f"gregor_clegane_{suffix}.png").is_file(), suffix

skin = RES / "assets/got/textures/entity/westeros/westeros"
for folder, count in (("male", 10), ("female", 14),
                      ("malechild", 6), ("femalechild", 11)):
    assert len(list((skin / folder).glob("*.png"))) == count, folder

entity = (JAVA / "npc/GOTWesterlandsNpcEntity.java").read_text()
for token in (
    "implements net.minecraft.world.entity.monster.RangedAttackMob, Merchant, GOTFactionNpc",
    'getFactionId() { return "westerlands"; }',
    "getRole().alignmentBonus()", "WesterlandsNpcRole.GREGOR_CLEGANE",
    "50.0D", "restrictTo", "canFreeze()", "MerchantOffers",
):
    assert token in entity, token

north = (JAVA / "npc/GOTNorthNpcEntity.java").read_text()
assert "Merchant, GOTFactionNpc" in north
assert 'getFactionId() { return "north"; }' in north

population = (JAVA / "npc/GOTWesterlandsNpcPopulation.java").read_text()
for token in (
    "enqueue(worldGen.getLevel(), prepare(markers))", "queueFixedSites",
    "getChunkNow(chunkX, chunkZ)", "MAX_DEFERRED_SPAWNS_PER_TICK",
    "key.server() != server", "safePosition", "westerlands_market_trader_",
):
    assert token in population, token
for waypoint, role in (
    ("ASHEMARK", "ADDAM_MARBRAND"), ("BANEFORT", "QUENTEN_BANEFORT"),
    ("CASTERLY_ROCK", "TYWIN_LANNISTER"), ("CASTERLY_ROCK", "QYBURN"),
    ("CLEGANES_KEEP", "GREGOR_CLEGANE"), ("CLEGANES_KEEP", "POLLIVER"),
    ("CORNFIELD", "HARYS_SWYFT"), ("CRAKEHALL", "LYLE_CRAKEHALL"),
    ("FAIRCASTLE", "SEBASTON_FARMAN"), ("FEASTFIRES", "FORLEY_PRESTER"),
    ("GOLDEN_TOOTH", "LEO_LEFFORD"), ("HORNVALE", "TYTOS_BRAX"),
    ("LANNISPORT", "KEVAN_LANNISTER"), ("LANNISPORT", "DAVEN_LANNISTER"),
    ("LANNISPORT", "AMORY_LORCH"),
):
    assert f"GOTWaypoint.{waypoint}, WesterlandsNpcRole.{role}" in population, (waypoint, role)

terrain = (JAVA / "world/terrain/PlanetosChunkGenerator.java").read_text()
assert "GOTWesterlandsNpcPopulation.queueFixedSites(level, chunk, seed)" in terrain

loadouts = (JAVA / "npc/GOTWesterlandsNpcLoadouts.java").read_text()
for token in (
    'GOTBannerType.byName("lannister")', '"got:westerlands_chestplate"',
    '"got:westerlandsguard_chestplate"', '"got:bane"',
    '"got:gregor_clegane_sword"', '"got:copper_goblet"',
):
    assert token in loadouts, token

renderer = (JAVA / "client/npc/GOTWesterlandsNpcRenderer.java").read_text()
model = (JAVA / "client/npc/GOTWesterlandsNpcModel.java").read_text()
assert '"textures/entity/westeros/westeros/"' in renderer
assert 'entity.isFemale() ? 14 : 10' in renderer
assert 'entity.isFemale() ? 11 : 6' in renderer
assert "role.legendaryOverlaySuffix()" in renderer
assert 'texOffs(0, 32)' in model and 'texOffs(32, 0)' not in model
assert "ArmPose.BOW_AND_ARROW" in model

modifier = json.loads((RES / "data/got/forge/biome_modifier/westerlands_npcs.json").read_text())
assert modifier["type"] == "forge:add_spawns"
assert modifier["spawners"][0]["type"] == "got:westerlands_npc"
biome_tag = json.loads((RES / "data/got/tags/worldgen/biome/spawns_westerlands_npcs.json").read_text())
assert biome_tag["values"] == [
    "got:westerlands", "got:westerlands_forest",
    "got:westerlands_hills", "got:westerlands_town",
]

entities = (JAVA / "GOTEntities.java").read_text()
items = (JAVA / "GOTItems.java").read_text()
creative = (JAVA / "GOTCreativeTabs.java").read_text()
client = (JAVA / "GOTClientEvents.java").read_text()
assert "WESTERLANDS_NPC" in entities and "GOTWesterlandsNpcEntity.createAttributes" in entities
assert "WESTERLANDS_NPC_SPAWNER" in items
assert "WesterlandsNpcRole.spawnerOrder" in creative
assert "GOTWesterlandsNpcRenderer::new" in client
assert (RES / "assets/got/models/item/westerlands_npc_spawner.json").is_file()

print("Westerlands NPC audit passed: 21 ordinary roles, 15 fixed characters, alignment bridge, skins, spawning and waypoint population")
