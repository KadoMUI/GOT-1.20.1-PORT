#!/usr/bin/env python3
"""Regression audit for the Lys, Myr, and Tyrosh NPC checkpoint."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "src/main/java/got"
RES = ROOT / "src/main/resources"


def src(path: str) -> str:
    return (JAVA / path).read_text(encoding="utf-8")


def role_pairs(title: str) -> list[tuple[str, str]]:
    text = src(f"npc/{title}NpcRole.java").split("public enum Gender", 1)[0]
    return re.findall(r'^    ([A-Z][A-Z0-9_]*)\("([a-z0-9_]+)"', text, re.MULTILINE)


ordinary_suffixes = (
    "MAN", "LEVYMAN", "LEVYMAN_ARCHER", "SOLDIER", "SOLDIER_ARCHER",
    "BANNER_BEARER", "CAPTAIN", "BLACKSMITH", "GOLDSMITH", "SLAVER",
    "SLAVE", "BARTENDER", "MINER", "LUMBERMAN", "MASON", "BREWER",
    "FLORIST", "BUTCHER", "FISHMONGER", "BAKER",
)
expected_counts = {"Lys": 21, "Myr": 20, "Tyrosh": 22}
for title, expected_count in expected_counts.items():
    family = title.lower()
    roles = role_pairs(title)
    assert len(roles) == expected_count, (title, len(roles))
    role_names = {name for name, _ in roles}
    role_ids = {role_id for _, role_id in roles}
    assert {f"{title.upper()}_{suffix}" for suffix in ordinary_suffixes} <= role_names
    assert {f"{family}_{suffix.lower()}" for suffix in ordinary_suffixes} <= role_ids
    assert f"{title.upper()}_FARMER" not in role_names
    assert f"{title.upper()}_FARMHAND" not in role_names

assert ("SALLADHOR_SAAN", "salladhor_saan") in role_pairs("Lys")
assert ("JON_CONNINGTON", "jon_connington") in role_pairs("Tyrosh")
assert ("YOUNG_GRIFF", "young_griff") in role_pairs("Tyrosh")

for family, biomes in (
    ("lys", ["got:lys"]),
    ("myr", ["got:myr", "got:myr_forest"]),
    ("tyrosh", ["got:tyrosh"]),
):
    modifier = json.loads((RES / f"data/got/forge/biome_modifier/{family}_npcs.json").read_text())
    assert modifier == {
        "type": "forge:add_spawns",
        "biomes": f"#got:spawns_{family}_npcs",
        "spawners": [{"type": f"got:{family}_npc", "weight": 10,
                      "minCount": 1, "maxCount": 2}],
    }
    tag = json.loads((RES / f"data/got/tags/worldgen/biome/spawns_{family}_npcs.json").read_text())
    assert tag == {"replace": False, "values": biomes}
    assert (RES / f"assets/got/models/item/{family}_npc_spawner.json").is_file()

for path, count in (
    ("essos/violet/male", 12), ("essos/violet/female", 10),
    ("essos/violet/malechild", 6), ("essos/violet/femalechild", 10),
    ("essos/free/male", 10), ("essos/free/female", 9),
    ("essos/free/malechild", 5), ("essos/free/femalechild", 9),
    ("essos/colored/male", 10), ("essos/colored/female", 9),
    ("essos/colored/malechild", 5), ("essos/colored/femalechild", 9),
    ("essos/slave/male", 4), ("essos/slave/female", 3),
):
    actual = len(list((RES / "assets/got/textures/entity" / path).glob("*.png")))
    assert actual == count, (path, actual)

entities = src("GOTEntities.java")
items = src("GOTItems.java")
client = src("GOTClientEvents.java")
creative = src("GOTCreativeTabs.java")
for family, title in (("lys", "Lys"), ("myr", "Myr"), ("tyrosh", "Tyrosh")):
    upper = family.upper()
    assert f"{upper}_NPC" in entities
    assert f"GOT{title}NpcEntity.createAttributes" in entities
    assert f"SpawnPlacements.register({upper}_NPC.get()" in entities
    assert f"{upper}_NPC_SPAWNER" in items
    assert f"GOT{title}NpcRenderer::new" in client
    assert f"{title}NpcRole.spawnerOrder" in creative
    assert (RES / f"assets/got/models/item/{family}_npc_spawner.json").is_file()

    entity = src(f"npc/GOT{title}NpcEntity.java")
    loadouts = src(f"npc/GOT{title}NpcLoadouts.java")
    renderer = src(f"client/npc/GOT{title}NpcRenderer.java")
    spawner = src(f"npc/GOT{title}NpcSpawnerItem.java")
    assert "GOTNpcNames.randomEssos" in entity
    assert f"{title}NpcRole.{upper}_LEVYMAN" in entity
    assert f"{title}NpcRole.{upper}_LEVYMAN_ARCHER" in entity
    assert "random.nextInt(15) < 10" in entity
    assert f'GOTBannerType.byName("{family}")' in loadouts
    assert f'got:{family}_helmet' in loadouts
    assert f'got:{family}_chestplate' in loadouts
    assert "case SLAVER" in loadouts and f"{upper}_SLAVER" in loadouts
    assert 'got:robes_helmet' in loadouts and 'got:leather_hat' not in loadouts
    assert f'return "{family}"' in entity and "GOTQuestGiver" in entity
    assert 'textures/entity/essos/' in renderer
    assert 'textures/entity/essos/slave/' in renderer
    assert f'0x' in spawner and f'item.got.{family}_npc_spawner.legendary' in spawner

assert 'textures/entity/essos/violet/' in src("client/npc/GOTLysNpcRenderer.java")
assert 'textures/entity/essos/free/' in src("client/npc/GOTMyrNpcRenderer.java")
assert 'textures/entity/essos/colored/' in src("client/npc/GOTTyroshNpcRenderer.java")
assert "case SALLADHOR_SAAN" in src("npc/GOTLysNpcLoadouts.java")
assert 'stack("got:truth")' in src("npc/GOTLysNpcLoadouts.java")
assert 'spawnAtLocation(GOTLysNpcLoadouts.stack("got:truth"))' in src("npc/GOTLysNpcEntity.java")
assert "case JON_CONNINGTON, YOUNG_GRIFF" in src("npc/GOTTyroshNpcLoadouts.java")

for texture in ("salladhor_saan.png", "jon_connington.png", "young_griff.png"):
    assert (RES / "assets/got/textures/entity/legendary" / texture).is_file(), texture

population = src("npc/GOTFreeCitiesNpcPopulation.java")
for declaration in (
    'fixed(GOTWaypoint.BRAAVOS, City.BRAAVOS, "tycho_nestoris", 0, 1)',
    'fixed(GOTWaypoint.PENTOS, City.PENTOS, "illyrio_mopatis", 3, 0)',
    'fixed(GOTWaypoint.LYS, City.LYS, "salladhor_saan", 0, -1)',
    'fixed(GOTWaypoint.TYROSH, City.TYROSH, "jon_connington", 0, -1)',
    'fixed(GOTWaypoint.TYROSH, City.TYROSH, "young_griff", 1, -1)',
):
    assert declaration in population, declaration
assert "populationKey" in population and "level.noCollision(npc)" in population
assert "GOTFreeCitiesNpcPopulation.queueFixedSites" in src("world/terrain/PlanetosChunkGenerator.java")
major = src("world/structure/major/MajorSchematicStructureGenerator.java")
for upper, schematic in (("LYS", "Lys.schem"), ("MYR", "Myr.schem"), ("TYROSH", "Tyrosh.schem")):
    assert f'site(GOTWaypoint.{upper}, "{schematic}"' in major

factions = src("faction/GOTFaction.java")
commands = src("GOTCommands.java")
lang = json.loads((RES / "assets/got/lang/en_us.json").read_text(encoding="utf-8-sig"))
for family, upper in (("lys", "LYS"), ("myr", "MYR"), ("tyrosh", "TYROSH")):
    assert f'id.startsWith("{family}")' in factions
    assert f"GOTFaction.{upper}" in commands
    for suffix in ("supplies", "border_patrol"):
        quest_path = RES / f"data/got/quests/{family}_{suffix}.json"
        quest = json.loads(quest_path.read_text())
        assert quest["giver"]["factions"] == [family]
        for key in ("title", "description", "offer", "complete"):
            assert quest[key] in lang
        for objective in quest["objectives"]:
            assert objective["label"] in lang

print("Lys/Myr/Tyrosh NPC audit passed: 60 ordinary roles, 3 fixed characters, 2 corrected diplomats, exact skins, military spawning, loadouts, quests, and wiring")
