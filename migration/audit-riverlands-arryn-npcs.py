#!/usr/bin/env python3
"""Regression audit for the paired Riverlands and Vale NPC milestone."""

from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "src/main/java/got"
RES = ROOT / "src/main/resources"


def roles(name: str) -> list[tuple[str, str]]:
    source = (JAVA / f"npc/{name}NpcRole.java").read_text()
    catalogue = source.split("public enum Gender", 1)[0]
    return re.findall(r'^    ([A-Z][A-Z0-9_]*)\("([a-z0-9_]+)"', catalogue, re.M)


riverlands = roles("Riverlands")
arryn = roles("Arryn")
assert len(riverlands) == 31, len(riverlands)
assert len(riverlands[:20]) == 20 and riverlands[20] == ("WILLIAM_MOOTON", "william_mooton")
assert len(arryn) == 32, len(arryn)
assert len(arryn[:21]) == 21 and arryn[21] == ("GEROLD_GRAFTON", "gerold_grafton")
assert ("ARRYN_GUARD", "arryn_guard") in arryn
assert all(role != "RIVERLANDS_GUARD" for role, _ in riverlands)

legendary_dir = RES / "assets/got/textures/entity/legendary"
textures = {
    "william_mooton": ("",), "clement_piper": ("",),
    "tytos_blackwood": ("",), "hoster_tully": ("",),
    "brynden_tully": ("_1", "_2"), "edmure_tully": ("_1", "_2"),
    "jason_mallister": ("",), "jonos_bracken": ("",),
    "black_walder_frey": ("",), "lothar_frey": ("",),
    "walder_frey": ("_1", "_2"), "gerold_grafton": ("",),
    "lyn_corbray": ("",), "harrold_hardyng": ("",),
    "anya_waynwood": ("",), "gilwood_hunter": ("",),
    "symond_templeton": ("",), "horton_redfort": ("",),
    "yohn_royce": ("",), "benedar_belmore": ("_1", "_2"),
    "robin_arryn": ("",), "lysa_arryn": ("",),
    "catelyn_stark": ("",), "rodrik_cassel": ("",),
}
for base, suffixes in textures.items():
    for suffix in suffixes:
        assert (legendary_dir / f"{base}{suffix}.png").is_file(), (base, suffix)

skin = RES / "assets/got/textures/entity/westeros/westeros"
for folder, count in (("male", 10), ("female", 14),
                      ("malechild", 6), ("femalechild", 11)):
    assert len(list((skin / folder).glob("*.png"))) == count, folder

fixed = {
    "GOTRiverlandsNpcPopulation.java": (
        ("MAIDENPOOL", "WILLIAM_MOOTON"), ("PINKMAIDEN_CASTLE", "CLEMENT_PIPER"),
        ("RAVENTREE_HALL", "TYTOS_BLACKWOOD"), ("RIVERRUN", "BRYNDEN_TULLY"),
        ("RIVERRUN", "EDMURE_TULLY"), ("RIVERRUN", "HOSTER_TULLY"),
        ("SEAGARD", "JASON_MALLISTER"), ("STONE_HEDGE", "JONOS_BRACKEN"),
        ("TWINS_LEFT", "BLACK_WALDER_FREY"), ("TWINS_LEFT", "LOTHAR_FREY"),
        ("TWINS_RIGHT", "WALDER_FREY"),
    ),
    "GOTArrynNpcPopulation.java": (
        ("GULLTOWN", "GEROLD_GRAFTON"), ("HEARTS_HOME", "LYN_CORBRAY"),
        ("IRONOAKS", "HARROLD_HARDYNG"), ("IRONOAKS", "ANYA_WAYNWOOD"),
        ("LONGBOW_HALL", "GILWOOD_HUNTER"), ("NINESTARS", "SYMOND_TEMPLETON"),
        ("REDFORT", "HORTON_REDFORT"), ("RUNESTONE", "YOHN_ROYCE"),
        ("STRONGSONG", "BENEDAR_BELMORE"), ("THE_EYRIE", "ROBIN_ARRYN"),
        ("THE_EYRIE", "LYSA_ARRYN"),
    ),
}
for filename, expected in fixed.items():
    source = (JAVA / f"npc/{filename}").read_text()
    for waypoint, role in expected:
        assert f"GOTWaypoint.{waypoint}" in source and f"NpcRole.{role}" in source, (filename, waypoint, role)
    for token in ("queueFixedSites", "getChunkNow(chunkX, chunkZ)",
                  "MAX_DEFERRED_SPAWNS_PER_TICK", "safePosition", "key.server() != server"):
        assert token in source, (filename, token)

river_population = (JAVA / "npc/GOTRiverlandsNpcPopulation.java").read_text()
for role in ("NorthNpcRole.CATELYN_STARK", "NorthNpcRole.RODRIK_CASSEL"):
    assert role in river_population, role

river_loadouts = (JAVA / "npc/GOTRiverlandsNpcLoadouts.java").read_text()
for token in ('GOTBannerType.byName("tully")', '"got:riverlands_chestplate"',
              '"got:reminder"', '"got:tidewings"', '"got:indomitable"',
              '"got:iron_crossbow"'):
    assert token in river_loadouts, token
arryn_loadouts = (JAVA / "npc/GOTArrynNpcLoadouts.java").read_text()
for token in ('GOTBannerType.byName("arryn")', '"got:arryn_chestplate"',
              '"got:arrynguard_chestplate"', '"got:lady_forlorn"',
              '"got:honor"', '"got:lamentation"', '"got:bronze_chainmail_chestplate"'):
    assert token in arryn_loadouts, token

for region, expected_biomes in {
    "riverlands": ["got:riverlands", "got:riverlands_forest"],
    "arryn": ["got:arryn", "got:arryn_forest", "got:arryn_mountains",
              "got:arryn_mountains_foothills", "got:arryn_town"],
}.items():
    modifier = json.loads((RES / f"data/got/forge/biome_modifier/{region}_npcs.json").read_text())
    assert modifier["spawners"][0]["type"] == f"got:{region}_npc"
    tag = json.loads((RES / f"data/got/tags/worldgen/biome/spawns_{region}_npcs.json").read_text())
    assert tag["values"] == expected_biomes
    assert (RES / f"assets/got/models/item/{region}_npc_spawner.json").is_file()
    for suffix in ("supplies", "border_patrol"):
        quest = json.loads((RES / f"data/got/quests/{region}_{suffix}.json").read_text())
        assert quest["giver"]["factions"] == [region]

entities = (JAVA / "GOTEntities.java").read_text()
items = (JAVA / "GOTItems.java").read_text()
client = (JAVA / "GOTClientEvents.java").read_text()
creative = (JAVA / "GOTCreativeTabs.java").read_text()
terrain = (JAVA / "world/terrain/PlanetosChunkGenerator.java").read_text()
factions = (JAVA / "faction/GOTFaction.java").read_text()
for upper, title in (("RIVERLANDS", "Riverlands"), ("ARRYN", "Arryn")):
    assert f"{upper}_NPC" in entities and f"GOT{title}NpcEntity.createAttributes" in entities
    assert f"{upper}_NPC_SPAWNER" in items
    assert f"GOT{title}NpcRenderer::new" in client
    assert f"{title}NpcRole.spawnerOrder" in creative
    assert f"GOT{title}NpcPopulation.queueFixedSites" in terrain
assert 'id.startsWith("riverlands")' in factions and 'id.startsWith("arryn")' in factions

print("Riverlands/Vale NPC audit passed: 65 role variants, exact fixed sites, loadouts, skins, spawning, quests, and faction wiring")
