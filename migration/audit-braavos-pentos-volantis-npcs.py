#!/usr/bin/env python3
"""Regression audit for the first three Free Cities NPC catalogues."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "src/main/java/got"
RES = ROOT / "src/main/resources"


def src(path: str) -> str:
    return (JAVA / path).read_text(encoding="utf-8")


def roles(name: str) -> list[tuple[str, str]]:
    text = src(f"npc/{name}NpcRole.java").split("public enum Gender", 1)[0]
    return re.findall(r'^    ([A-Z][A-Z0-9_]*)\("([a-z0-9_]+)"', text, re.MULTILINE)


braavos = roles("Braavos")
pentos = roles("Pentos")
volantis = roles("Volantis")
assert len(braavos) == 21
assert len(pentos) == 21
assert len(volantis) == 20
assert ("BRAAVOS_SOLDIER_ARCHER", "braavos_soldier_archer") in braavos
assert ("PENTOS_BANNER_BEARER", "pentos_banner_bearer") in pentos
assert ("VOLANTIS_SLAVE", "volantis_slave") in volantis
assert ("VOLANTIS_SLAVER", "volantis_slaver") in volantis
assert ("TYCHO_NESTORIS", "tycho_nestoris") in braavos
assert ("ILLYRIO_MOPATIS", "illyrio_mopatis") in pentos
assert all("VOLANTIS_FARMER" != role[0] and "VOLANTIS_FARMHAND" != role[0] for role in volantis)

for family, biomes in (
    ("braavos", ["got:braavos", "got:braavos_forest", "got:braavos_hills"]),
    ("pentos", ["got:pentos", "got:pentos_forest", "got:pentos_hills"]),
    ("volantis", ["got:volantis", "got:volantis_forest", "got:volantis_marshes", "got:volantis_orange_forest"]),
):
    modifier = json.loads((RES / f"data/got/forge/biome_modifier/{family}_npcs.json").read_text())
    assert modifier["spawners"] == [{"type": f"got:{family}_npc", "weight": 10, "minCount": 1, "maxCount": 2}]
    tag = json.loads((RES / f"data/got/tags/worldgen/biome/spawns_{family}_npcs.json").read_text())
    assert tag["values"] == biomes
    assert (RES / f"assets/got/models/item/{family}_npc_spawner.json").is_file()

for path, count in (
    ("essos/free/male", 10), ("essos/free/female", 9),
    ("essos/free/malechild", 5), ("essos/free/femalechild", 9),
    ("essos/violet/male", 12), ("essos/violet/female", 10),
    ("essos/violet/malechild", 6), ("essos/violet/femalechild", 10),
    ("essos/slave/male", 4), ("essos/slave/female", 3),
):
    assert len(list((RES / "assets/got/textures/entity" / path).glob("*.png"))) == count, path

entities = src("GOTEntities.java")
items = src("GOTItems.java")
client = src("GOTClientEvents.java")
creative = src("GOTCreativeTabs.java")
for upper, title in (("BRAAVOS", "Braavos"), ("PENTOS", "Pentos"), ("VOLANTIS", "Volantis")):
    assert f"{upper}_NPC" in entities and f"GOT{title}NpcEntity.createAttributes" in entities
    assert f"SpawnPlacements.register({upper}_NPC.get()" in entities
    assert f"{upper}_NPC_SPAWNER" in items
    assert f"GOT{title}NpcRenderer::new" in client
    assert f"{title}NpcRole.spawnerOrder" in creative

for family, title in (("braavos", "Braavos"), ("pentos", "Pentos"), ("volantis", "Volantis")):
    entity = src(f"npc/GOT{title}NpcEntity.java")
    loadouts = src(f"npc/GOT{title}NpcLoadouts.java")
    renderer = src(f"client/npc/GOT{title}NpcRenderer.java")
    assert "GOTNpcNames.randomEssos" in entity
    assert f"{title.upper()}_LEVYMAN_ARCHER" in entity and "random.nextInt(15) < 10" in entity
    assert f'GOTBannerType.byName("{family}")' in loadouts
    assert f'got:{family}_helmet' in loadouts and f'got:{family}_chestplate' in loadouts
    assert 'got:robes_helmet' in loadouts and 'got:leather_hat' not in loadouts
    assert "GOTQuestGiver" in entity and f'return "{family}"' in entity
    if family in ("braavos", "pentos"):
        assert 'textures/entity/essos/free/' in renderer
    else:
        assert 'textures/entity/essos/violet/' in renderer
        assert 'textures/entity/essos/slave/' in renderer
        assert "case VOLANTIS_SLAVER" in loadouts and "case SLAVER" in loadouts
        assert "VOLANTIS_MINER, VOLANTIS_SLAVER" in loadouts

factions = src("faction/GOTFaction.java")
commands = src("GOTCommands.java")
for family, upper in (("braavos", "BRAAVOS"), ("pentos", "PENTOS"), ("volantis", "VOLANTIS")):
    assert f'id.startsWith("{family}")' in factions
    assert f"GOTFaction.{upper}" in commands
    for suffix in ("supplies", "border_patrol"):
        assert (RES / f"data/got/quests/{family}_{suffix}.json").is_file()

population = src("npc/GOTFreeCitiesNpcPopulation.java")
assert 'fixed(GOTWaypoint.BRAAVOS, City.BRAAVOS, "tycho_nestoris", 0, 1)' in population
assert 'fixed(GOTWaypoint.PENTOS, City.PENTOS, "illyrio_mopatis", 3, 0)' in population
assert '"textures/entity/legendary/"' in src("client/npc/GOTBraavosNpcRenderer.java")
assert '"textures/entity/legendary/"' in src("client/npc/GOTPentosNpcRenderer.java")
assert (RES / "assets/got/textures/entity/legendary/tycho_nestoris.png").is_file()
assert (RES / "assets/got/textures/entity/legendary/illyrio_mopatis_1.png").is_file()
assert (RES / "assets/got/textures/entity/legendary/illyrio_mopatis_2.png").is_file()

print("Braavos/Pentos/Volantis NPC audit passed: 60 ordinary roles, 2 fixed characters, exact skins, legacy military spawning, loadouts, trades, quests, factions, and client wiring")
