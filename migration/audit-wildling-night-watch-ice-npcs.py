#!/usr/bin/env python3
"""Regression audit for Wildlings, Night's Watch, and White Walker/Wight NPCs."""
from __future__ import annotations
import json,re,struct
from pathlib import Path

ROOT=Path(__file__).resolve().parents[1]
JAVA=ROOT/"src/main/java/got"; RES=ROOT/"src/main/resources"
def src(path:str)->str:return (JAVA/path).read_text(encoding="utf-8")
def roles(name:str):return re.findall(r'^    ([A-Z][A-Z0-9_]*)\("([a-z0-9_]+)"',src(f"npc/{name}NpcRole.java").split("public enum Gender",1)[0],re.M)

wild=roles("Wildling"); watch=roles("NightWatch"); ice=roles("WhiteWalker")
assert len(wild)==19 and len(watch)==17 and len(ice)==4,(len(wild),len(watch),len(ice))
for pair in (("THENN_CHIEFTAIN","thenn_chieftain"),("GIANT","giant"),("CRASTER_WIFE","craster_wife")):assert pair in wild
for pair in (("JEOR_MORMONT","jeor_mormont"),("BENJEN_STARK","benjen_stark"),("YOREN","yoren")):assert pair in watch
assert ("NIGHT_KING","night_king") in ice

legendary=RES/"assets/got/textures/entity/legendary"
for texture in ("mance_rayder_1.png","mance_rayder_2.png","tormund_1.png","tormund_2.png","ygritte.png",
                "craster_1.png","craster_2.png","jeor_mormont_1.png","jeor_mormont_2.png","jon_snow_1.png",
                "aemon.png","alliser_thorne_1.png","edd_1.png","samwell_tarly_1.png","cotter_pyke_1.png",
                "harmune.png","denys_mallister_1.png","mullin.png","benjen_stark_1.png","yoren.png","night_king.png"):
    assert (legendary/texture).is_file(),texture
for path,count in (("westeros/wild/male",3),("westeros/thenn/female",4),("westeros/gift/male",3),("westeros/ice/wight/female",8)):
    assert len(list((RES/"assets/got/textures/entity"/path).glob("*.png")))==count,path

for family,entity,biomes in (
    ("wildling","wildling_npc",["got:frozen_shore","got:haunted_forest","got:thenn_land"]),
    ("night_watch","night_watch_npc",["got:gift_new","got:gift_old"]),
    ("white_walker","white_walker_npc",["got:frozen_shore","got:haunted_forest","got:thenn_land"])):
    modifier=json.loads((RES/f"data/got/forge/biome_modifier/{family}_npcs.json").read_text())
    assert modifier["spawners"][0]["type"]==f"got:{entity}"
    tag=json.loads((RES/f"data/got/tags/worldgen/biome/spawns_{family}_npcs.json").read_text())
    assert tag["values"]==biomes
    assert (RES/f"assets/got/models/item/{family}_npc_spawner.json").is_file()

wild_pop=src("npc/GOTWildlingNpcPopulation.java"); watch_pop=src("npc/GOTNightWatchNpcPopulation.java"); ice_pop=src("npc/GOTWhiteWalkerNpcPopulation.java")
for token in ("wildling_warrior_respawner","thenn_ranged_respawner","hardhome_warrior_respawner","mance_rayder","craster_wife"):assert token in wild_pop
for token in ("gift_population_respawner","gift_archer_respawner","jeor_mormont","cotter_pyke","benjen_stark"):assert token in watch_pop
for token in ("barrow_wight_future","night_king","wight_giant"):assert token in ice_pop
respawn=src("npc/GOTNpcRespawnerData.java")
for token in ("SavedData","getChunkNow","RESPAWN_DELAY","PopulationKey","night_watch","wildling"):assert token in respawn

wild_gen=src("world/structure/wildling/PlanetosWildlingStructureGenerator.java")
watch_gen=src("world/structure/nightwatch/PlanetosNightWatchStructureGenerator.java")
north_gen=src("world/structure/north/PlanetosNorthStructureGenerator.java")
assert "GOTWildlingNpcPopulation.spawnMarkers" in wild_gen and "GOTWaypoint.NIGHT_KING" in wild_gen
assert "GOTNightWatchNpcPopulation.spawnMarkers" in watch_gen
assert "GOTWhiteWalkerNpcPopulation.spawnMarkers" in north_gen

entities=src("GOTEntities.java");items=src("GOTItems.java");client=src("GOTClientEvents.java");creative=src("GOTCreativeTabs.java")
for upper,title in (("WILDLING","Wildling"),("NIGHT_WATCH","NightWatch"),("WHITE_WALKER","WhiteWalker")):
    assert f"{upper}_NPC" in entities and f"GOT{title}NpcEntity.createAttributes" in entities
    assert f"{upper}_NPC_SPAWNER" in items and f"GOT{title}NpcRenderer::new" in client and f"{title}NpcRole.spawnerOrder" in creative
for quest in ("wildling_supplies","wildling_border_patrol","night_watch_supplies","night_watch_border_patrol"):
    assert (RES/f"data/got/quests/{quest}.json").is_file()
print("Northern factions NPC audit passed: 40 roles, fixed legends, structure populations, durable respawners, natural spawns, quests, factions, and client assets")
