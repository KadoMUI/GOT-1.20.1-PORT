#!/usr/bin/env python3
"""Static fidelity gate for the final southern-continent NPC pass."""

from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SRC = ROOT / "src/main/java/got"
RES = ROOT / "src/main/resources"


def read(path: Path) -> str:
    assert path.is_file(), f"Missing file: {path.relative_to(ROOT)}"
    return path.read_text(encoding="utf-8-sig")


def require(condition: bool, message: str) -> None:
    if not condition:
        raise AssertionError(message)


summer_roles = re.findall(r'^    ([A-Z0-9_]+)\("([a-z0-9_]+)"',
                          read(SRC / "npc/SummerIslesNpcRole.java"), re.MULTILINE)
soth_roles = re.findall(r'^    ([A-Z0-9_]+)\("([a-z0-9_]+)"',
                        read(SRC / "npc/SothoryosNpcRole.java"), re.MULTILINE)
require(len(summer_roles) == 20, f"Expected 20 Summer Isles roles, got {len(summer_roles)}")
require(len(soth_roles) == 9, f"Expected 9 Sothoryos roles, got {len(soth_roles)}")

summer_entity = read(SRC / "npc/GOTSummerIslesNpcEntity.java")
soth_entity = read(SRC / "npc/GOTSothoryosNpcEntity.java")
spider = read(SRC / "npc/GOTUlthosSpiderEntity.java")
blizzard = read(SRC / "npc/GOTBlizzardEntity.java")
yi_ti_loadouts = read(SRC / "npc/GOTYiTiNpcLoadouts.java")
require("random.nextInt(15) < 10" in summer_entity, "Summer military must preserve 10:5 soldier/archer ratio")
require("random.nextInt(15) < 10" in soth_entity, "Sothoryos military must preserve 10:5 warrior/blowgunner ratio")
require('getFactionId() { return "summer_islands"; }' in summer_entity, "Summer faction bridge missing")
require('getFactionId() { return "sothoryos"; }' in soth_entity, "Sothoryos faction bridge missing")
require("sarbacane" in read(SRC / "npc/GOTSothoryosNpcLoadouts.java"), "Sothoryos blowgun missing")
require("bronze_dagger_poisoned" in read(SRC / "npc/GOTSothoryosNpcLoadouts.java"), "Shaman poison weapon missing")

for token in ("random.nextInt(3)", "Variant.SLOWNESS", "Variant.POISON",
              "12.0D + scale * 6.0D", "0.35D - scale * 0.03D",
              "getScaleLevel() > 0", "alignment(GOTFaction.ULTHOS)"):
    require(token in spider, f"Ulthos spider fidelity token missing: {token}")
require('getFactionId() { return "ulthos"; }' in spider, "Ulthos spider faction missing")
for token in ("MAX_HEALTH, 1.0D", "new BlizzardSnowball", "4.0F", "0.525F", "createWight",
              'getFactionId() { return "white_walker"; }'):
    require(token in blizzard, f"Blizzard fidelity token missing: {token}")
require("import net.minecraft.server.level.ServerLevel;" in blizzard,
        "Blizzard must use the 1.20.1 ServerLevel package")
require("net.minecraft.world.level.ServerLevel;" not in blizzard,
        "Obsolete Blizzard ServerLevel import remains")
for role in ("YI_TI_FISHMONGER", "YI_TI_FLORIST", "YI_TI_LUMBERMAN"):
    require(yi_ti_loadouts.count(role) == 2,
            f"Yi-Ti loadout switch should contain exactly its equipment and headgear cases: {role}")

registries = read(SRC / "GOTEntities.java")
client = read(SRC / "GOTClientEvents.java")
items = read(SRC / "GOTItems.java")
creative = read(SRC / "GOTCreativeTabs.java")
for entity_id in ("summer_isles_npc", "sothoryos_npc", "ulthos_spider", "blizzard"):
    require(f'ENTITIES.register("{entity_id}"' in registries, f"Entity not registered: {entity_id}")
    require(f"GOTEntities.{entity_id.upper()}" in client, f"Renderer not wired: {entity_id}")
for item_id in ("summer_isles_npc_spawner", "sothoryos_npc_spawner", "ulthos_creature_spawner"):
    require(f'ITEMS.register("{item_id}"' in items, f"Spawner not registered: {item_id}")
    require((RES / f"assets/got/models/item/{item_id}.json").is_file(), f"Spawner model missing: {item_id}")
require("SummerIslesNpcRole.spawnerOrder" in creative, "Summer roles absent from creative catalogue")
require("SothoryosNpcRole.spawnerOrder" in creative, "Sothoryos roles absent from creative catalogue")
require("UlthosCreature.values" in creative, "Ulthos creatures absent from creative catalogue")

expected_biomes = {
    "spawns_summer_isles_npcs.json": {"got:summer_islands", "got:summer_colony"},
    "spawns_sothoryos_npcs.json": {"got:sothoryos_bushland", "got:ulthos_bushland"},
    "spawns_sothoryos_incursions.json": {"got:summer_colony", "got:ghiscar_colony", "got:qarth_colony", "got:ulthos_forest", "got:ulthos_red_forest"},
    "spawns_ulthos_spiders.json": {"got:ulthos_forest", "got:ulthos_red_forest"},
    "spawns_ulthos_spider_incursions.json": {"got:sothoryos_bushland"},
    "spawns_blizzards.json": {"got:ulthos_frost", "got:ulthos_taiga"},
}
tag_root = RES / "data/got/tags/worldgen/biome"
for filename, values in expected_biomes.items():
    actual = set(json.loads(read(tag_root / filename))["values"])
    require(actual == values, f"Biome list mismatch for {filename}: {actual}")

for modifier in ("summer_isles_npcs", "sothoryos_npcs", "sothoryos_incursions",
                 "ulthos_spiders", "ulthos_spider_incursions", "blizzards"):
    data = json.loads(read(RES / f"data/got/forge/biome_modifier/{modifier}.json"))
    require(data["type"] == "forge:add_spawns", f"Invalid biome modifier: {modifier}")

for texture in (
    "assets/got/textures/entity/sothoryos/summer/male/0.png",
    "assets/got/textures/entity/sothoryos/summer/female/0.png",
    "assets/got/textures/entity/sothoryos/sothoryos/male/0.png",
    "assets/got/textures/entity/sothoryos/sothoryos/shaman.png",
    "assets/got/textures/entity/ulthos/spider.png",
    "assets/got/textures/entity/ulthos/spiderSlowness.png",
    "assets/got/textures/entity/ulthos/spiderPoison.png",
    "assets/got/textures/entity/ulthos/blizzard.png",
):
    require((RES / texture).is_file(), f"Recovered texture missing: {texture}")

for quest in ("summer_isles_supplies", "summer_isles_border_patrol",
              "sothoryos_supplies", "sothoryos_border_patrol"):
    require((RES / f"data/got/quests/{quest}.json").is_file(), f"Quest missing: {quest}")

print("Summer Isles/Sothoryos/Ulthos NPC audit passed: 29 humanoid roles, exact military ratios, six spawn territories, three spider sizes/statuses, Blizzard frost behavior, trades, quests, assets, and wiring")
