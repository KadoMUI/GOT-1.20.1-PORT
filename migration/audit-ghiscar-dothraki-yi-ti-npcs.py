#!/usr/bin/env python3
"""Regression audit for the Ghiscar, Dothraki, and Yi-Ti NPC checkpoint."""
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

expected = {"Ghiscar": 29, "Dothraki": 6, "YiTi": 24}
for title, count in expected.items():
    assert len(roles(title)) == count, (title, len(roles(title)))

ghis = dict(roles("Ghiscar")); doth = dict(roles("Dothraki")); yiti = dict(roles("YiTi"))
for name in ("GHISCAR_GLADIATOR", "GHISCAR_HARPY", "GHISCAR_UNSULLIED",
             "KRAZNYS_MO_NAKLOZ", "MISSANDEI", "GREY_WORM", "HIZDAHR_ZO_LORAQ",
             "DAARIO_NAHARIS", "RAZDAL_MO_ERAZ"):
    assert name in ghis
for name in ("DOTHRAKI", "DOTHRAKI_ARCHER", "DOTHRAKI_CHIEFTAIN", "DOTHRAKI_SHAMAN",
             "DAENERYS_TARGARYEN", "JORAH_MORMONT"):
    assert name in doth
for name in ("YI_TI_LEVYMAN_CROSSBOWER", "YI_TI_SOLDIER_CROSSBOWER", "YI_TI_SAMURAI",
             "YI_TI_SAMURAI_FLAMETHROWER", "YI_TI_BOMBARDIER", "BU_GAI"):
    assert name in yiti

for path, count in (
    ("essos/ghiscar/male", 5), ("essos/ghiscar/female", 3),
    ("essos/ghiscar/malechild", 3), ("essos/ghiscar/femalechild", 3),
    ("essos/slave/male", 4), ("essos/slave/female", 3), ("essos/unsullied", 6),
    ("essos/nomad/male", 6), ("essos/nomad/female", 7),
    ("essos/nomad/malechild", 2), ("essos/nomad/femalechild", 7),
    ("essos/yi_ti/male", 4), ("essos/yi_ti/female", 5),
    ("essos/yi_ti/malechild", 4), ("essos/yi_ti/femalechild", 5),
):
    actual = len(list((RES / "assets/got/textures/entity" / path).glob("*.png")))
    assert actual == count, (path, actual)

for texture in ("kraznys_mo_nakloz_1.png", "kraznys_mo_nakloz_2.png", "missandei.png",
                "grey_worm.png", "hizdahr_zo_loraq.png", "daario_naharis.png",
                "razdal_mo_eraz.png", "daenerys_targaryen.png", "jorah_mormont.png",
                "bu_gai_1.png", "bu_gai_2.png"):
    assert (RES / "assets/got/textures/entity/legendary" / texture).is_file(), texture

for family, title, upper in (("ghiscar", "Ghiscar", "GHISCAR"),
                             ("dothraki", "Dothraki", "DOTHRAKI"),
                             ("yi_ti", "YiTi", "YI_TI")):
    assert f"{upper}_NPC" in src("GOTEntities.java")
    assert f"GOT{title}NpcEntity.createAttributes" in src("GOTEntities.java")
    assert f"SpawnPlacements.register({upper}_NPC.get()" in src("GOTEntities.java")
    assert f"{upper}_NPC_SPAWNER" in src("GOTItems.java")
    assert f"GOT{title}NpcRenderer::new" in src("GOTClientEvents.java")
    assert f"{title}NpcRole.spawnerOrder" in src("GOTCreativeTabs.java")
    assert f'id.startsWith("{family}")' in src("faction/GOTFaction.java")
    assert f"GOTFaction.{upper}" in src("GOTCommands.java")
    assert (RES / f"assets/got/models/item/{family}_npc_spawner.json").is_file()

ghis_entity = src("npc/GOTGhiscarNpcEntity.java")
assert all(b in ghis_entity for b in ("ghiscar_meereen", "ghiscar_astapor", "ghiscar_yunkai", "ghiscar_new_ghis"))
assert "GHISCAR_HARPY" in ghis_entity and "GHISCAR_UNSULLIED" in ghis_entity
assert "random.nextInt(15) < 10" in ghis_entity and "randomGhiscar" in ghis_entity
yiti_entity = src("npc/GOTYiTiNpcEntity.java")
assert "random.nextInt(23)" in yiti_entity and "LargeFireball" in yiti_entity and "SmallFireball" in yiti_entity
assert "randomYiTi" in yiti_entity
doth_entity = src("npc/GOTDothrakiNpcEntity.java")
assert "not a biome spawn list" in doth_entity and "randomDothraki" in doth_entity
assert not (RES / "data/got/forge/biome_modifier/dothraki_npcs.json").exists()

for family, biomes in (
    ("ghiscar", ["got:ghiscar_meereen", "got:ghiscar_astapor", "got:ghiscar_yunkai", "got:ghiscar_new_ghis"]),
    ("yi_ti", ["got:yi_ti", "got:yi_ti_border_zone", "got:yi_ti_marshes", "got:yi_ti_tropical_forest"]),
):
    modifier = json.loads((RES / f"data/got/forge/biome_modifier/{family}_npcs.json").read_text())
    assert modifier["spawners"] == [{"type": f"got:{family}_npc", "weight": 10, "minCount": 1, "maxCount": 2}]
    tag = json.loads((RES / f"data/got/tags/worldgen/biome/spawns_{family}_npcs.json").read_text())
    assert tag == {"replace": False, "values": biomes}

population = src("npc/GOTFreeCitiesNpcPopulation.java")
for declaration in (
    'fixed(GOTWaypoint.ASTAPOR, City.GHISCAR, "kraznys_mo_nakloz", -1, 0)',
    'fixed(GOTWaypoint.ASTAPOR, City.GHISCAR, "missandei", -1, -1)',
    'fixed(GOTWaypoint.ASTAPOR, City.GHISCAR, "grey_worm", -1, 1)',
    'fixed(GOTWaypoint.MEEREEN, City.GHISCAR, "hizdahr_zo_loraq", -1, -1)',
    'fixed(GOTWaypoint.YUNKAI, City.GHISCAR, "daario_naharis", -1, 0)',
    'fixed(GOTWaypoint.YUNKAI, City.GHISCAR, "razdal_mo_eraz", -1, 1)',
    'fixed(GOTWaypoint.VAES_EFE, City.DOTHRAKI, "daenerys_targaryen", 0, 3)',
    'fixed(GOTWaypoint.VAES_EFE, City.DOTHRAKI, "jorah_mormont", 1, 3)',
    'fixed(GOTWaypoint.YIN, City.YI_TI, "bu_gai", 12, 0)',
):
    assert declaration in population, declaration

loadouts = src("npc/GOTGhiscarNpcLoadouts.java") + src("npc/GOTDothrakiNpcLoadouts.java") + src("npc/GOTYiTiNpcLoadouts.java")
for item in ("got:unsullied_chestplate", "got:harpy", "got:dothraki_chestplate",
             "got:yi_ti_samurai_chestplate", "got:yi_ti_bombardier_chestplate", "got:katana"):
    assert item in loadouts

lang = json.loads((RES / "assets/got/lang/en_us.json").read_text(encoding="utf-8-sig"))
for family in ("ghiscar", "dothraki", "yi_ti"):
    for suffix in ("supplies", "border_patrol"):
        quest = json.loads((RES / f"data/got/quests/{family}_{suffix}.json").read_text())
        assert quest["giver"]["factions"] == [family]
        for key in ("title", "description", "offer", "complete"):
            assert quest[key] in lang
        assert all(objective["label"] in lang for objective in quest["objectives"])

print("Ghiscar/Dothraki/Yi-Ti NPC audit passed: 50 ordinary roles, 9 fixed characters, exact skins, spawning, loadouts, quests, and wiring")
