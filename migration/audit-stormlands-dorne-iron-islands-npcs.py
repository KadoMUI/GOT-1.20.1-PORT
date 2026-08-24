#!/usr/bin/env python3
"""Regression audit for the Stormlands, Dorne, and Iron Islands NPC pass."""

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
    "Stormlands": roles("Stormlands"),
    "Dorne": roles("Dorne"),
    "Ironborn": roles("Ironborn"),
}
assert len(catalogues["Stormlands"]) == 25
assert len(catalogues["Dorne"]) == 34
assert len(catalogues["Ironborn"]) == 37
assert catalogues["Stormlands"][20] == ("SELWYN_TARTH", "selwyn_tarth")
assert catalogues["Dorne"][20] == ("HARMEN_ULLER", "harmen_uller")
assert catalogues["Ironborn"][21] == ("BAELOR_BLACKTYDE", "baelor_blacktyde")
assert ("IRONBORN_PRIEST", "ironborn_priest") in catalogues["Ironborn"]

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
assert (legendary_dir / "victarion_greyjoy_cape.png").is_file()

fixed = {
    "GOTStormlandsNpcPopulation.java": {
        "EVENFALL_HALL": ["SELWYN_TARTH"], "GREENSTONE": ["ELDON_ESTERMONT"],
        "STONEHELM": ["GULIAN_SWANN"],
        "STORMS_END": ["RENLY_BARATHEON", "BRIENNE_TARTH"],
    },
    "GOTDorneNpcPopulation.java": {
        "HELLHOLT": ["HARMEN_ULLER"], "HIGH_HERMITAGE": ["GEROLD_DAYNE"],
        "SANDSTONE": ["QUENTYN_QORGYLE"], "SKYREACH": ["FRANKLYN_FOWLER"],
        "STARFALL": ["BERIC_DAYNE"],
        "SUNSPEAR": ["OBERYN_MARTELL", "DORAN_MARTELL", "ELLARYA_SAND",
                     "AREO_HOTAH", "TRYSTANE_MARTELL", "ARIANNE_MARTELL",
                     "MANFREY_MARTELL"],
        "YRONWOOD": ["QUENTYN_MARTELL", "ANDERS_YRONWOOD"],
    },
    "GOTIronbornNpcPopulation.java": {
        "BLACKTYDE": ["BAELOR_BLACKTYDE"],
        "DRUMM_CASTLE": ["DUNSTAN_DRUMM", "ANDRIK_THE_UNSMILING"],
        "GREY_GARDEN": ["HARRAS_HARLAW"], "HAMMERHORN": ["GOROLD_GOODBROTHER"],
        "LONELY_LIGHT": ["GYLBERT_FARWYND"], "LORDSPORT": ["DAGMER"],
        "NAGGAS_HILL": ["AERON_GREYJOY"],
        "PYKE": ["BALON_GREYJOY", "YARA_GREYJOY", "THEON_GREYJOY"],
        "RED_HAVEN": ["ERIK_IRONMAKER"], "TEN_TOWERS": ["RODRIK_HARLAW"],
        "VOLMARK": ["MARON_VOLMARK"], "EURON": ["EURON_GREYJOY"],
        "VICTARION_LANDING": ["VICTARION_GREYJOY"],
    },
}
fixed_count = 0
for filename, sites in fixed.items():
    text = source(f"npc/{filename}")
    for waypoint, names in sites.items():
        for name in names:
            assert f"GOTWaypoint.{waypoint}" in text and f"NpcRole.{name}" in text, (waypoint, name)
            fixed_count += 1
    for token in ("queueFixedSites", "MAX_DEFERRED_SPAWNS_PER_TICK", "safePosition",
                  "getChunkNow(chunkX, chunkZ)", "key.server() != server"):
        assert token in text, (filename, token)
assert fixed_count == 35

loadout_tokens = {
    "GOTStormlandsNpcLoadouts.java": ('GOTBannerType.byName("renly")',
                                       '"got:stormlands_chestplate"', '"got:just_maid"'),
    "GOTDorneNpcLoadouts.java": ('GOTBannerType.byName("martell")', '"got:dorne_chestplate"',
                                  '"got:darkstar"', '"got:dawn"', '"got:sunspear"'),
    "GOTIronbornNpcLoadouts.java": ('GOTBannerType.byName("greyjoy")',
                                     '"got:ironborn_chestplate"', '"got:red_rain"',
                                     '"got:nightfall"', '"got:valyrian_chainmail_chestplate"',
                                     "case PRIEST"),
}
for filename, tokens in loadout_tokens.items():
    text = source(f"npc/{filename}")
    for token in tokens:
        assert token in text, (filename, token)

expected = {
    "stormlands": ["got:stormlands", "got:stormlands_forest", "got:stormlands_tarth",
                    "got:stormlands_tarth_forest", "got:stormlands_town"],
    "dorne": ["got:dorne", "got:dorne_desert", "got:dorne_forest",
              "got:dorne_mesa", "got:dorne_mountains"],
    "ironborn": ["got:iron_islands", "got:iron_islands_forest", "got:iron_islands_hills"],
}
for region, expected_biomes in expected.items():
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
commands = source("GOTCommands.java")
for upper, title in (("STORMLANDS", "Stormlands"), ("DORNE", "Dorne"),
                     ("IRONBORN", "Ironborn")):
    assert f"{upper}_NPC" in entities and f"GOT{title}NpcEntity.createAttributes" in entities
    assert f"{upper}_NPC_SPAWNER" in items
    assert f"GOT{title}NpcRenderer::new" in client
    assert f"{title}NpcRole.spawnerOrder" in creative
    assert f"GOT{title}NpcPopulation.queueFixedSites" in terrain
assert 'id.startsWith("stormlands")' in factions
assert 'id.startsWith("dorne")' in factions
assert 'id.startsWith("iron_islands")' in factions
for label in ("Stormlands", "Dorne", "Iron Islands"):
    assert f'" | {label} "' in commands

print("Stormlands/Dorne/Iron Islands NPC audit passed: 96 roles, 35 fixed characters, loadouts, skins, spawning, quests, and faction wiring")
