#!/usr/bin/env python3
from pathlib import Path
import json
import re

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "src/main/java/got"
RES = ROOT / "src/main/resources"

roles_source = (JAVA / "npc/NorthNpcRole.java").read_text()
catalogue = roles_source.split("public enum Gender", 1)[0]
roles = re.findall(r"^\s{4}([A-Z][A-Z0-9_]*)\(\"([a-z0-9_]+)\"", catalogue, re.M)
assert len(roles) == 47, len(roles)
assert len(roles[:roles.index(("BARBREY_DUSTIN", "barbrey_dustin"))]) == 27
assert len(roles[27:]) == 20
role_ids = {role_id for _, role_id in roles}
assert len(role_ids) == 47
assert not any(role_id.startswith(("night_watch", "wildling", "thenn")) for role_id in role_ids)

ordinary = {
    "north_man", "north_levyman", "north_levyman_archer", "north_soldier",
    "north_soldier_archer", "north_guard", "north_banner_bearer", "north_captain",
    "north_blacksmith", "north_goldsmith", "north_farmer", "north_farmhand",
    "north_bartender", "north_miner", "north_lumberman", "north_mason",
    "north_brewer", "north_florist", "north_butcher", "north_fishmonger", "north_baker",
    "north_hillman", "north_hillman_warrior", "north_hillman_archer",
    "north_hillman_axe_thrower", "north_hillman_banner_bearer", "north_hillman_chieftain",
}
assert ordinary <= role_ids

legendary_textures = {
    "barbrey_dustin": 1, "ramsay_bolton": 1, "roose_bolton": 2,
    "howland_reed": 2, "rickard_karstark": 2, "john_umber": 2,
    "maege_mormont": 2, "rodrik_ryswell": 1, "cley_cerwyn": 1,
    "helman_tallhart": 1, "wyman_manderly": 2, "robb_stark": 2,
    "hodor": 1, "arya_stark": 1, "bran_stark": 1, "rickon_stark": 1,
    "luwin": 1, "osha": 2, "catelyn_stark": 1, "rodrik_cassel": 1,
}
legendary_dir = RES / "assets/got/textures/entity/legendary"
for base, count in legendary_textures.items():
    expected = [legendary_dir / f"{base}.png"] if count == 1 else [
        legendary_dir / f"{base}_{index}.png" for index in range(1, count + 1)
    ]
    assert all(path.is_file() for path in expected), (base, expected)

north_skin = RES / "assets/got/textures/entity/westeros/north"
wild_skin = RES / "assets/got/textures/entity/westeros/wild"
for folder, count in (("male", 8), ("female", 7), ("malechild", 3), ("femalechild", 6)):
    assert len(list((north_skin / folder).glob("*.png"))) == count, folder
for folder, count in (("male", 3), ("female", 4), ("malechild", 3), ("femalechild", 4)):
    assert len(list((wild_skin / folder).glob("*.png"))) == count, folder
assert (wild_skin / "outfit.png").is_file()

population = (JAVA / "npc/GOTNorthNpcPopulation.java").read_text()
for token in (
    "north_market_trader_", "north_hillman_axe_thrower", "north_hillman_chieftain_npc",
    "legendary_npc:", "rodrik_ryswel", "getPopulationKey", "safePosition",
    "@Mod.EventBusSubscriber", "enqueue(worldGen.getLevel(), prepare(markers))",
    "TickEvent.ServerTickEvent", "getChunkNow(chunkX, chunkZ)",
    "MAX_DEFERRED_SPAWNS_PER_TICK", "key.server() != server",
):
    assert token in population, token
assert population.index("accessor instanceof ServerLevel") < population.index("accessor instanceof WorldGenLevel")
worldgen_dispatch = population.split("public static int spawnMarkers", 1)[1].split("private static void enqueue", 1)[0]
assert "spawnPrepared(level" not in worldgen_dispatch.split("accessor instanceof WorldGenLevel", 1)[1]
for variant, role in enumerate((
    "NORTH_GOLDSMITH", "NORTH_MINER", "NORTH_LUMBERMAN", "NORTH_MASON",
    "NORTH_BREWER", "NORTH_FLORIST", "NORTH_BUTCHER", "NORTH_FISHMONGER",
    "NORTH_FARMER", "NORTH_BLACKSMITH", "NORTH_BAKER",
)):
    assert f"case {variant} -> NorthNpcRole.{role}" in population, (variant, role)

builder = (JAVA / "world/structure/north/NorthStructureBuilder.java").read_text()
marker_method = builder.split("public void marker", 1)[1].split("public void path", 1)[0]
assert "insideClip(pos)" in marker_method

generator = (JAVA / "world/structure/north/PlanetosNorthStructureGenerator.java").read_text()
assert generator.count("GOTNorthNpcPopulation.spawnMarkers") == 3
for named in (
    "barbrey_dustin", "ramsay_bolton", "roose_bolton", "howland_reed",
    "rickard_karstark", "john_umber", "maege_mormont", "rodrik_ryswel",
    "cley_cerwyn", "helman_tallhart", "wyman_manderly", "robb_stark",
    "hodor", "arya_stark", "bran_stark", "rickon_stark", "maester_luwin", "osha",
):
    assert f'"{named}"' in generator, named

riverlands_population = (JAVA / "npc/GOTRiverlandsNpcPopulation.java").read_text()
for named in ("CATELYN_STARK", "RODRIK_CASSEL"):
    assert f"NorthNpcRole.{named}" in riverlands_population, named

legacy_context = (JAVA / "world/structure/legacy/LegacyNorthernContext.java").read_text()
for token in ("entity.male", "entity.child", 'append("#home=")'):
    assert token in legacy_context, token
for path, prefix in (
    (JAVA / "world/structure/legacy/generated/GOTStructureNorthHillmanHouse.java", "north_hillman"),
    (JAVA / "world/structure/legacy/generated/GOTStructureNorthHillmanChieftainHouse.java", "north_hillman_chieftain"),
):
    assert f'this.markerPrefix = "{prefix}"' in path.read_text()

entity = (JAVA / "npc/GOTNorthNpcEntity.java").read_text()
for token in (
    "implements net.minecraft.world.entity.monster.RangedAttackMob, Merchant",
    "GOTThrownAxeEntity", "MerchantOffers", "canFreeze()", "restrictTo",
    "NORTH_HILLMAN_WARRIOR", "NorthNpcRole.NORTH_MAN",
    "!resolvedPopulationKey.isEmpty()",
):
    assert token in entity, token

spawning = (RES / "data/got/forge/biome_modifier/north_npcs.json")
modifier = json.loads(spawning.read_text())
assert modifier["type"] == "forge:add_spawns"
assert modifier["spawners"][0]["type"] == "got:north_npc"
biome_tag = json.loads((RES / "data/got/tags/worldgen/biome/spawns_north_npcs.json").read_text())
assert "got:north" in biome_tag["values"] and "got:skagos" in biome_tag["values"]

creative = (JAVA / "GOTCreativeTabs.java").read_text()
assert "NorthNpcRole.spawnerOrder" in creative
assert "GOTNorthNpcSpawnerItem::createStack" in creative
assert (RES / "assets/got/models/item/north_npc_spawner.json").is_file()

renderer = (JAVA / "client/npc/GOTNorthNpcRenderer.java").read_text()
model = (JAVA / "client/npc/GOTNorthNpcModel.java").read_text()
assert 'texOffs(0, 32)' in model
assert 'texOffs(32, 0)' not in model
assert 'ArmPose.BOW_AND_ARROW' in model
assert 'role.legendaryLayered() ? "_1" : ""' in renderer
assert 'role.legendaryTexture() + "_2.png"' in renderer
assert renderer.index('addLayer(new LegacySecondSkinLayer') < renderer.index('addLayer(new HumanoidArmorLayer')
assert '0.9375F * entity.getRole().scale()' in renderer
assert 'entity.isBaby() ? 0.55F : 1.0F' not in renderer
assert 'float scale = isBaby() ? 0.5F : 1.0F' in entity
assert 'getRole().scale() * (isBaby()' not in entity
assert 'JOHN_UMBER("john_umber", "John Umber", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "john_umber", 2, 1.2F)' in roles_source

loadouts = (JAVA / "npc/GOTNorthNpcLoadouts.java").read_text()
assert '"got:iron_sword"' not in loadouts
assert '"minecraft:iron_sword"' in loadouts

print("North NPC audit passed: 27 regional roles, 20 named characters, exact skins, marker population and spawning")
