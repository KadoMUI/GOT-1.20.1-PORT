#!/usr/bin/env python3
"""Regression audit for the Asshai, Ibben, and Jogos Nhai NPC checkpoint."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "src/main/java/got"
RES = ROOT / "src/main/resources"


def src(path: str) -> str:
    return (JAVA / path).read_text(encoding="utf-8")


def roles(title: str) -> list[tuple[str, str]]:
    text = src(f"npc/{title}NpcRole.java").split("public enum Gender", 1)[0]
    return re.findall(r'^    ([A-Z][A-Z0-9_]*)\("([a-z0-9_]+)"', text, re.MULTILINE)


expected = {"Asshai": 8, "Ibben": 20, "JogosNhai": 5}
for title, count in expected.items():
    assert len(roles(title)) == count, (title, len(roles(title)))

asshai = dict(roles("Asshai"))
ibben = dict(roles("Ibben"))
jogos = dict(roles("JogosNhai"))
for name in ("ASSHAI_MAN", "ASSHAI_WARRIOR", "ASSHAI_SHADOWBINDER",
             "ASSHAI_SPHEREBINDER", "ASSHAI_BANNER_BEARER", "ASSHAI_CAPTAIN",
             "ASSHAI_ALCHEMIST", "ASSHAI_ARCHMAG"):
    assert name in asshai
for name in ("IBBEN_MAN", "IBBEN_LEVYMAN", "IBBEN_LEVYMAN_ARCHER",
             "IBBEN_SOLDIER", "IBBEN_SOLDIER_ARCHER", "IBBEN_BANNER_BEARER",
             "IBBEN_CAPTAIN", "IBBEN_BLACKSMITH", "IBBEN_GOLDSMITH", "IBBEN_FARMER",
             "IBBEN_FARMHAND", "IBBEN_BARTENDER", "IBBEN_MINER", "IBBEN_LUMBERMAN",
             "IBBEN_MASON", "IBBEN_BREWER", "IBBEN_FLORIST", "IBBEN_BUTCHER",
             "IBBEN_FISHMONGER", "IBBEN_BAKER"):
    assert name in ibben
for name in ("JOGOS_NHAI_MAN", "JOGOS_NHAI_ARCHER", "JOGOS_NHAI_CHIEFTAIN",
             "JOGOS_NHAI_SHAMAN", "TUGAR_KHAN"):
    assert name in jogos

for path, count in (
    ("essos/asshai/male", 8), ("essos/asshai/female", 9),
    ("essos/asshai/malechild", 5), ("essos/asshai/femalechild", 9),
    ("essos/ibben/male", 3), ("essos/ibben/female", 4),
    ("essos/ibben/malechild", 3), ("essos/ibben/femalechild", 4),
    ("essos/jogos/male", 4), ("essos/jogos/female", 5),
    ("essos/jogos/malechild", 4), ("essos/jogos/femalechild", 5),
):
    actual = len(list((RES / "assets/got/textures/entity" / path).glob("*.png")))
    assert actual == count, (path, actual)

for texture in ("archmag.png", "tugar_khan.png"):
    assert (RES / "assets/got/textures/entity/legendary" / texture).is_file(), texture
assert (RES / "assets/got/textures/entity/essos/ibben/outfit.png").is_file()

for family, title, upper in (("asshai", "Asshai", "ASSHAI"),
                             ("ibben", "Ibben", "IBBEN"),
                             ("jogos_nhai", "JogosNhai", "JOGOS_NHAI")):
    assert f"{upper}_NPC" in src("GOTEntities.java")
    assert f"GOT{title}NpcEntity.createAttributes" in src("GOTEntities.java")
    assert f"SpawnPlacements.register({upper}_NPC.get()" in src("GOTEntities.java")
    assert f"{upper}_NPC_SPAWNER" in src("GOTItems.java")
    assert f"GOT{title}NpcRenderer::new" in src("GOTClientEvents.java")
    assert f"{title}NpcRole.spawnerOrder" in src("GOTCreativeTabs.java")
    assert f"GOTFaction.{upper}" in src("GOTCommands.java")
    assert (RES / f"assets/got/models/item/{family}_npc_spawner.json").is_file()

faction = src("faction/GOTFaction.java")
assert 'id.startsWith("ibben")' in faction
assert 'id.startsWith("jogos_nhai")' in faction
assert 'id.startsWith("shadow_")' in faction

asshai_entity = src("npc/GOTAsshaiNpcEntity.java")
assert 'metadata.id().equals("shadow_town")' in asshai_entity
assert "random.nextInt(14)" in asshai_entity
assert "ASSHAI_SHADOWBINDER" in asshai_entity and "SmallFireball" in asshai_entity
assert "ASSHAI_ARCHMAG" in asshai_entity and "LargeFireball" in asshai_entity
assert "ASSHAI_SPHEREBINDER" in asshai_entity and "target.push" in asshai_entity
assert "randomAsshai" in asshai_entity

ibben_entity = src("npc/GOTIbbenNpcEntity.java")
assert 'metadata.id().equals("ibben")' in ibben_entity
assert 'metadata.id().equals("ibben_colony")' in ibben_entity
assert "random.nextInt(15)" in ibben_entity and "randomIbben" in ibben_entity
assert "IBBEN_LEVYMAN_ARCHER" in ibben_entity

jogos_entity = src("npc/GOTJogosNhaiNpcEntity.java")
assert '.id().startsWith("yi_ti")' in jogos_entity
assert "random.nextInt(15)" in jogos_entity and "randomJogosNhai" in jogos_entity
assert "MobEffects.POISON" in jogos_entity and "random.nextBoolean()" in jogos_entity

for modifier, tag_name, entity, weight, values in (
    ("asshai_npcs", "spawns_asshai_npcs", "asshai_npc", 10, ["got:shadow_town"]),
    ("ibben_npcs", "spawns_ibben_npcs", "ibben_npc", 10, ["got:ibben", "got:ibben_colony"]),
    ("jogos_nhai_incursions", "spawns_jogos_nhai_incursions", "jogos_nhai_npc", 2,
     ["got:yi_ti", "got:yi_ti_border_zone", "got:yi_ti_marshes", "got:yi_ti_tropical_forest"]),
):
    data = json.loads((RES / f"data/got/forge/biome_modifier/{modifier}.json").read_text())
    assert data["spawners"] == [{"type": f"got:{entity}", "weight": weight,
                                 "minCount": 1, "maxCount": 2}]
    tag = json.loads((RES / f"data/got/tags/worldgen/biome/{tag_name}.json").read_text())
    assert tag == {"replace": False, "values": values}

population = src("npc/GOTFreeCitiesNpcPopulation.java")
assert 'fixed(GOTWaypoint.ASSHAI, City.ASSHAI, "asshai_archmag", 0, 0)' in population
assert 'fixed(GOTWaypoint.HOJDBAATAR, City.JOGOS_NHAI, "tugar_khan", 0, 3)' in population

loadouts = (src("npc/GOTAsshaiNpcLoadouts.java") + src("npc/GOTIbbenNpcLoadouts.java")
            + src("npc/GOTJogosNhaiNpcLoadouts.java"))
for item in ("got:asshai_shadowbinder_staff", "got:asshai_archmag_staff", "got:asshai_mask",
             "got:ibben_chestplate", "got:trident", "got:jogos_nhai_chestplate",
             "got:jogos_nhai_helmet", "got:alloy_steel_scimitar"):
    assert item in loadouts

ibben_renderer = src("client/npc/GOTIbbenNpcRenderer.java")
assert "usesOutfitOverlay" in ibben_renderer and "essos/ibben/outfit.png" in ibben_renderer
assert "1.3F" in src("npc/JogosNhaiNpcRole.java")

lang = json.loads((RES / "assets/got/lang/en_us.json").read_text(encoding="utf-8-sig"))
for family in ("asshai", "ibben", "jogos_nhai"):
    for suffix in ("supplies", "border_patrol"):
        quest = json.loads((RES / f"data/got/quests/{family}_{suffix}.json").read_text())
        assert quest["giver"]["factions"] == [family]
        for key in ("title", "description", "offer", "complete"):
            assert quest[key] in lang
        assert all(objective["label"] in lang for objective in quest["objectives"])

print("Asshai/Ibben/Jogos Nhai NPC audit passed: 31 ordinary roles, 2 fixed characters, exact skins, spawning, magic, poison, loadouts, quests, and wiring")
