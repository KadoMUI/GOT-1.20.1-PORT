#!/usr/bin/env python3
"""Regression audit for the Crownlands, Dragonstone, and Reach NPC pass."""

from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "src/main/java/got"
RES = ROOT / "src/main/resources"


def source(path: str) -> str:
    return (JAVA / path).read_text(encoding="utf-8")


def roles(region: str) -> list[tuple[str, str]]:
    catalogue = source(f"npc/{region}NpcRole.java").split("public enum Gender", 1)[0]
    return re.findall(r'^    ([A-Z][A-Z0-9_]*)\("([a-z0-9_]+)"', catalogue, re.M)


catalogues = {
    "Crownlands": roles("Crownlands"),
    "Dragonstone": roles("Dragonstone"),
    "Reach": roles("Reach"),
}
assert len(catalogues["Crownlands"]) == 43
assert len(catalogues["Dragonstone"]) == 29
assert len(catalogues["Reach"]) == 35
assert catalogues["Crownlands"][20] == ("SANSA_STARK", "sansa_stark")
assert catalogues["Dragonstone"][20] == ("ARDRIAN_CELTIGAR", "ardrian_celtigar")
assert catalogues["Reach"][21] == ("GARLAN_TYRELL", "garlan_tyrell")
assert ("KINGSGUARD", "kingsguard") in catalogues["Crownlands"]
assert ("REACH_GUARD", "reach_guard") in catalogues["Reach"]
assert all(role not in {"CROWNLANDS_SOLDIER", "CROWNLANDS_BANNER_BEARER"}
           for role, _ in catalogues["Crownlands"])

legendary_dir = RES / "assets/got/textures/entity/legendary"
for region in catalogues:
    text = source(f"npc/{region}NpcRole.java").split("public enum Gender", 1)[0]
    entries = re.findall(
        r'^    [A-Z0-9_]+\("[^"]+", "[^"]+", Gender\.[A-Z]+, Combat\.[A-Z]+, '
        r'Trade\.[A-Z]+, \d+, true, "([^"]+)", "([^"]*)", "([^"]*)"', text, re.M)
    for base, base_suffix, overlay_suffix in entries:
        assert (legendary_dir / f"{base}{base_suffix}.png").is_file(), base
        if overlay_suffix:
            assert (legendary_dir / f"{base}{overlay_suffix}.png").is_file(), base

fixed = {
    "GOTCrownlandsNpcPopulation.java": {
        "KINGS_LANDING": ["SANSA_STARK", "SHAE", "YOREN", "SANDOR_CLEGANE",
                          "JOFFREY_BARATHEON", "CERSEI_LANNISTER", "JAIME_LANNISTER",
                          "PYCELLE", "JANOS_SLYNT", "VARYS", "ILYN_PAYNE", "HIGH_SEPTON",
                          "TOMMEN_BARATHEON", "MYRCELLA_BARATHEON", "MERYN_TRANT",
                          "BARRISTAN_SELMY", "PETYR_BAELISH", "TYRION_LANNISTER",
                          "LANCEL_LANNISTER", "BRONN", "PODRICK_PAYNE", "TOBHO_MOTT",
                          "GENDRY_BARATHEON"],
    },
    "GOTDragonstoneNpcPopulation.java": {
        "CLAW_ISLE": ["ARDRIAN_CELTIGAR"],
        "DRAGONSTONE": ["STANNIS_BARATHEON", "DAVOS_SEAWORTH", "MELISANDRA",
                        "SHIREEN_BARATHEON", "SELYSE_BARATHEON", "MATTHOS_SEAWORTH"],
        "DRIFTMARK": ["MONFORD_VELARYON", "AURANE_WATERS"],
    },
    "GOTReachNpcPopulation.java": {
        "BRIGHTWATER_KEEP": ["GARLAN_TYRELL"], "GOLDENGROVE": ["MATHIS_ROWAN"],
        "GREENSHIELD": ["MORIBALD_CHESTER"],
        "HIGHGARDEN": ["MACE_TYRELL", "OLENNA_TYRELL", "MARGAERY_TYRELL", "WILLAS_TYRELL"],
        "HIGHTOWER_LITEHOUSE": ["LEYTON_HIGHTOWER"], "HORN_HILL": ["RANDYLL_TARLY"],
        "LONGTABLE": ["ORTON_MERRYWEATHER"], "OLDTOWN": ["EBROSE"],
        "RING": ["QUENN_ROXTON"], "STARFISH_HARBOR": ["PAXTER_REDWYNE"],
        "STORMS_END": ["LORAS_TYRELL"],
    },
}
for filename, sites in fixed.items():
    text = source(f"npc/{filename}")
    for waypoint, names in sites.items():
        for name in names:
            assert f"GOTWaypoint.{waypoint}" in text and f"NpcRole.{name}" in text, (waypoint, name)
    for token in ("queueFixedSites", "MAX_DEFERRED_SPAWNS_PER_TICK", "safePosition",
                  "getChunkNow(chunkX, chunkZ)", "key.server() != server"):
        assert token in text, (filename, token)

loadout_tokens = {
    "GOTCrownlandsNpcLoadouts.java": ('"got:crownlands_chestplate"', '"got:kingsguard_chestplate"',
                                      '"got:sandor_clegane_sword"', '"got:hearteater"',
                                      '"got:petyr_baelish_dagger"', "case ALCHEMIST"),
    "GOTDragonstoneNpcLoadouts.java": ('GOTBannerType.byName("stannis")',
                                       '"got:dragonstone_chestplate"', '"got:lightbringer"',
                                       '"got:ardrian_celtigar_axe"', '"got:cutwave"'),
    "GOTReachNpcLoadouts.java": ('GOTBannerType.byName("tyrell")', '"got:reach_chestplate"',
                                 '"got:reachguard_chestplate"', '"got:heartsbane"',
                                 '"got:vigilance"', '"got:orphan_maker"'),
}
for filename, tokens in loadout_tokens.items():
    text = source(f"npc/{filename}")
    for token in tokens:
        assert token in text, (filename, token)

for region, expected_biomes in {
    "crownlands": ["got:crownlands", "got:crownlands_forest", "got:crownlands_town",
                   "got:kingswood_north", "got:kingswood_south"],
    "dragonstone": ["got:dragonstone"],
    "reach": ["got:reach", "got:reach_arbor", "got:reach_fire_field", "got:reach_forest",
              "got:reach_hills", "got:reach_town"],
}.items():
    modifier = json.loads((RES / f"data/got/forge/biome_modifier/{region}_npcs.json").read_text())
    assert modifier["spawners"][0]["type"] == f"got:{region}_npc"
    tag = json.loads((RES / f"data/got/tags/worldgen/biome/spawns_{region}_npcs.json").read_text())
    assert tag["values"] == expected_biomes
    assert (RES / f"assets/got/models/item/{region}_npc_spawner.json").is_file()
    for suffix in ("supplies", "border_patrol"):
        quest = json.loads((RES / f"data/got/quests/{region}_{suffix}.json").read_text())
        assert quest["giver"]["factions"] == [region]

entities = source("GOTEntities.java")
items = source("GOTItems.java")
client = source("GOTClientEvents.java")
creative = source("GOTCreativeTabs.java")
terrain = source("world/terrain/PlanetosChunkGenerator.java")
factions = source("faction/GOTFaction.java")
for upper, title in (("CROWNLANDS", "Crownlands"), ("DRAGONSTONE", "Dragonstone"), ("REACH", "Reach")):
    assert f"{upper}_NPC" in entities and f"GOT{title}NpcEntity.createAttributes" in entities
    assert f"{upper}_NPC_SPAWNER" in items
    assert f"GOT{title}NpcRenderer::new" in client
    assert f"{title}NpcRole.spawnerOrder" in creative
    assert f"GOT{title}NpcPopulation.queueFixedSites" in terrain
assert 'id.startsWith("crownlands")' in factions
assert 'id.startsWith("dragonstone")' in factions
assert 'id.startsWith("reach")' in factions

print("Crownlands/Dragonstone/Reach NPC audit passed: 107 roles, 46 fixed characters, loadouts, skins, spawning, quests, and faction wiring")
