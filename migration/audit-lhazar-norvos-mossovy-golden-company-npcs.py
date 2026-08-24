#!/usr/bin/env python3
"""Regression audit for the Lhazar, Norvos, Mossovy, and Golden Company pass."""
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
    if title == "GoldenCompany":
        text = src("npc/GoldenCompanyNpcRole.java").split("public enum Combat", 1)[0]
    return re.findall(r'^    ([A-Z][A-Z0-9_]*)\("([a-z0-9_]+)"', text, re.MULTILINE)


expected = {"Lhazar": 20, "Norvos": 20, "Mossovy": 5, "GoldenCompany": 5}
for title, count in expected.items():
    assert len(roles(title)) == count, (title, len(roles(title)))

lhazar_entity = src("npc/GOTLhazarNpcEntity.java")
assert "random.nextInt(21)" in lhazar_entity
for marker in ("roll < 10", "roll < 15", "roll < 19", "LHAZAR_SOLDIER_ARCHER"):
    assert marker in lhazar_entity, marker
assert "randomLhazar" in lhazar_entity

norvos_entity = src("npc/GOTNorvosNpcEntity.java")
assert "random.nextInt(15) < 10" in norvos_entity
assert "NORVOS_SOLDIER" in norvos_entity and "NORVOS_SOLDIER_ARCHER" in norvos_entity

mossovy_entity = src("npc/GOTMossovyNpcEntity.java")
for marker in ('metadata.id().equals("mossovy")', "MOSSOVY_WITCHER", "50.0D",
               "randomMossovy", "alignment(GOTFaction.MOSSOVY) < 50.0F",
               "tickHiredBehavior", "HiredOwner", "isHiredAlly(attacker)"):
    assert marker in mossovy_entity, marker

golden_entity = src("npc/GOTGoldenCompanyNpcEntity.java")
for marker in ('getFactionId() { return "unaligned"; }',
               'metadata.id().equals("disputed_lands")', "random.nextInt(15) < 10",
               "GOLDEN_COMPANY_WARRIOR", "GOLDEN_COMPANY_SPEARMAN",
               "tickHiredBehavior", "HiredOwner", "isHiredAlly(attacker)"):
    assert marker in golden_entity, marker

for family, title, upper in (
    ("lhazar", "Lhazar", "LHAZAR"),
    ("norvos", "Norvos", "NORVOS"),
    ("mossovy", "Mossovy", "MOSSOVY"),
    ("golden_company", "GoldenCompany", "GOLDEN_COMPANY"),
):
    assert f"{upper}_NPC" in src("GOTEntities.java")
    assert f"GOT{title}NpcEntity.createAttributes" in src("GOTEntities.java")
    assert f"SpawnPlacements.register({upper}_NPC.get()" in src("GOTEntities.java")
    assert f"{upper}_NPC_SPAWNER" in src("GOTItems.java")
    assert f"GOT{title}NpcRenderer::new" in src("GOTClientEvents.java")
    assert f"{title}NpcRole.spawnerOrder" in src("GOTCreativeTabs.java")
    assert (RES / f"assets/got/models/item/{family}_npc_spawner.json").is_file()

faction = src("faction/GOTFaction.java")
for family in ("lhazar", "norvos", "mossovy"):
    assert f'id.startsWith("{family}")' in faction
assert "golden_company" not in faction.split("fromBiomeId", 1)[1].split("buildIndex", 1)[0]

for bank in ("lhazar_male", "lhazar_female", "mossovy_male", "mossovy_female"):
    path = RES / f"assets/got/texts/en/names/{bank}.txt"
    assert path.is_file() and len(path.read_text(encoding="utf-8-sig").splitlines()) >= 90

for path, count in (
    ("essos/nomad/male", 6), ("essos/nomad/female", 7),
    ("essos/nomad/malechild", 2), ("essos/nomad/femalechild", 7),
    ("essos/free/male", 10), ("essos/mossovy/male", 3),
    ("essos/mossovy/female", 3), ("essos/mossovy/malechild", 3),
    ("essos/mossovy/femalechild", 3),
):
    actual = len(list((RES / "assets/got/textures/entity" / path).glob("*.png")))
    assert actual == count, (path, actual)
for name in ("harry_strickland_1.png", "harry_strickland_2.png"):
    assert (RES / "assets/got/textures/entity/legendary" / name).is_file()

for modifier, tag_name, entity, values, maximum in (
    ("lhazar_npcs", "spawns_lhazar_npcs", "lhazar_npc", ["got:lhazar"], 2),
    ("norvos_npcs", "spawns_norvos_npcs", "norvos_npc", ["got:norvos"], 2),
    ("mossovy_npcs", "spawns_mossovy_npcs", "mossovy_npc", ["got:mossovy"], 1),
    ("golden_company_npcs", "spawns_golden_company_npcs", "golden_company_npc",
     ["got:disputed_lands"], 2),
):
    data = json.loads((RES / f"data/got/forge/biome_modifier/{modifier}.json").read_text())
    assert data["spawners"] == [{"type": f"got:{entity}", "weight": 10,
                                 "minCount": 1, "maxCount": maximum}]
    tag = json.loads((RES / f"data/got/tags/worldgen/biome/{tag_name}.json").read_text())
    assert tag == {"replace": False, "values": values}

lhazar_loadout = src("npc/GOTLhazarNpcLoadouts.java")
for item in ("got:iron_scimitar", "got:iron_battleaxe", "got:iron_hammer",
             "got:iron_spear", "got:lhazar_chestplate"):
    assert item in lhazar_loadout
norvos_loadout = src("npc/GOTNorvosNpcLoadouts.java")
for item in ("minecraft", "got:iron_battleaxe", "got:iron_hammer", "got:iron_pike",
             "got:iron_spear", "got:norvos_chestplate"):
    assert item in norvos_loadout
mossovy_loadout = src("npc/GOTMossovyNpcLoadouts.java")
for item in ("got:iron_crossbow", "got:mossovy_chestplate", "createContract", "50"):
    assert item in mossovy_loadout
golden_loadout = src("npc/GOTGoldenCompanyNpcLoadouts.java")
for item in ("got:golden_company_chestplate", "got:iron_pike", "got:valyrian_sword",
             "golden_company", "createContract", "coin_1", "10"):
    assert item in golden_loadout

population = src("npc/GOTFreeCitiesNpcPopulation.java")
for marker in ("GOTWaypoint.MYR", "City.GOLDEN_COMPANY", '"harry_strickland", -1, -1',
               "spawnGoldenCompany"):
    assert marker in population

lang = json.loads((RES / "assets/got/lang/en_us.json").read_text(encoding="utf-8-sig"))
for family in ("lhazar", "norvos", "mossovy"):
    for suffix in ("supplies", "border_patrol"):
        quest = json.loads((RES / f"data/got/quests/{family}_{suffix}.json").read_text())
        assert quest["giver"]["factions"] == [family]
        for key in ("title", "description", "offer", "complete"):
            assert quest[key] in lang
        assert all(objective["label"] in lang for objective in quest["objectives"])
for key in ("item.got.hired_contract.named", "item.got.hired_contract.tooltip",
            "got.hired.following", "got.hired.guarding", "got.hired.requires_alignment"):
    assert key in lang

print("Lhazar/Norvos/Mossovy/Golden Company audit passed: 50 roles, exact regional spawns and skins, Witcher and Golden Company contracts, persistent follow/guard ownership, and fixed Harry Strickland")
