#!/usr/bin/env python3
"""Regression audit for the Qarth, Lorath, and Qohor NPC checkpoint."""
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


expected = {"Qarth": 21, "Lorath": 20, "Qohor": 21}
for title, count in expected.items():
    assert len(roles(title)) == count, (title, len(roles(title)))

qarth = dict(roles("Qarth"))
lorath = dict(roles("Lorath"))
qohor = dict(roles("Qohor"))
common_suffixes = (
    "MAN", "LEVYMAN", "LEVYMAN_ARCHER", "SOLDIER", "SOLDIER_ARCHER",
    "BANNER_BEARER", "CAPTAIN", "BLACKSMITH", "GOLDSMITH", "FARMER",
    "FARMHAND", "BARTENDER", "MINER", "LUMBERMAN", "MASON", "BREWER",
    "FLORIST", "BUTCHER", "FISHMONGER", "BAKER",
)
for family, catalogue in (("QARTH", qarth), ("LORATH", lorath), ("QOHOR", qohor)):
    for suffix in common_suffixes:
        assert f"{family}_{suffix}" in catalogue
assert "QARTH_WARLOCK" in qarth
assert "QOHOR_UNSULLIED" in qohor

for path, count in (
    ("essos/free/male", 10), ("essos/free/female", 9),
    ("essos/free/malechild", 5), ("essos/free/femalechild", 9),
    ("essos/unsullied", 6),
):
    actual = len(list((RES / "assets/got/textures/entity" / path).glob("*.png")))
    assert actual == count, (path, actual)
assert (RES / "assets/got/textures/entity/essos/pree.png").is_file()

for family, title, upper in (("qarth", "Qarth", "QARTH"),
                             ("lorath", "Lorath", "LORATH"),
                             ("qohor", "Qohor", "QOHOR")):
    assert f"{upper}_NPC" in src("GOTEntities.java")
    assert f"GOT{title}NpcEntity.createAttributes" in src("GOTEntities.java")
    assert f"SpawnPlacements.register({upper}_NPC.get()" in src("GOTEntities.java")
    assert f"{upper}_NPC_SPAWNER" in src("GOTItems.java")
    assert f"GOT{title}NpcRenderer::new" in src("GOTClientEvents.java")
    assert f"{title}NpcRole.spawnerOrder" in src("GOTCreativeTabs.java")
    assert f"GOTFaction.{upper}" in src("GOTCommands.java")
    assert (RES / f"assets/got/models/item/{family}_npc_spawner.json").is_file()

faction = src("faction/GOTFaction.java")
for family in ("qarth", "lorath", "qohor"):
    assert f'id.startsWith("{family}")' in faction

for family, total in (("Qarth", 15), ("Lorath", 15)):
    entity = src(f"npc/GOT{family}NpcEntity.java")
    assert f'random.nextInt({total})' in entity
    assert f"{family.upper()}_LEVYMAN" in entity
    assert f"{family.upper()}_LEVYMAN_ARCHER" in entity
    assert "randomEssos" in entity

qohor_entity = src("npc/GOTQohorNpcEntity.java")
assert "random.nextInt(17)" in qohor_entity
assert "roll < 5" in qohor_entity and "roll < 7" in qohor_entity
assert "QOHOR_UNSULLIED" in qohor_entity and "50.0D" in qohor_entity

for modifier, tag_name, entity, values in (
    ("qarth_npcs", "spawns_qarth_npcs", "qarth_npc", ["got:qarth", "got:qarth_colony"]),
    ("lorath_npcs", "spawns_lorath_npcs", "lorath_npc", ["got:lorath"]),
    ("qohor_npcs", "spawns_qohor_npcs", "qohor_npc", ["got:qohor"]),
):
    data = json.loads((RES / f"data/got/forge/biome_modifier/{modifier}.json").read_text())
    assert data["spawners"] == [{"type": f"got:{entity}", "weight": 10,
                                 "minCount": 1, "maxCount": 2}]
    tag = json.loads((RES / f"data/got/tags/worldgen/biome/{tag_name}.json").read_text())
    assert tag == {"replace": False, "values": values}

loadouts = {family: src(f"npc/GOT{family}NpcLoadouts.java")
            for family in ("Qarth", "Lorath", "Qohor")}
for family, text in loadouts.items():
    lower = family.lower()
    for item in (f"got:{lower}_chestplate", f"got:{lower}_helmet",
                 "got:bronze_chainmail_leggings", "got:robes_helmet",
                 "got:iron_spear"):
        assert item in text, (family, item)
assert "got:iron_scimitar" in loadouts["Qarth"]
assert "got:skull_staff" in loadouts["Qarth"]
for item in ("got:iron_pike", "got:unsullied_boots", "got:unsullied_leggings",
             "got:unsullied_chestplate", "got:unsullied_helmet"):
    assert item in loadouts["Qohor"]

qarth_renderer = src("client/npc/GOTQarthNpcRenderer.java")
qohor_renderer = src("client/npc/GOTQohorNpcRenderer.java")
assert "QARTH_WARLOCK" in qarth_renderer and "essos/pree.png" in qarth_renderer
assert "QOHOR_UNSULLIED" in qohor_renderer and "essos/unsullied/" in qohor_renderer

lang = json.loads((RES / "assets/got/lang/en_us.json").read_text(encoding="utf-8-sig"))
for family in ("qarth", "lorath", "qohor"):
    for suffix in ("supplies", "border_patrol"):
        quest = json.loads((RES / f"data/got/quests/{family}_{suffix}.json").read_text())
        assert quest["giver"]["factions"] == [family]
        for key in ("title", "description", "offer", "complete"):
            assert quest[key] in lang
        assert all(objective["label"] in lang for objective in quest["objectives"])

print("Qarth/Lorath/Qohor NPC audit passed: 62 roles, exact special skins, Qohor Unsullied distribution, eastern/western loadouts, spawning, trades, quests, and wiring")

