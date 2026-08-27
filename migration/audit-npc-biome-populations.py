#!/usr/bin/env python3
from pathlib import Path
import json
R=Path(__file__).resolve().parents[1]
T=R/'src/main/resources/data/got/tags/worldgen/biome'
M=R/'src/main/resources/data/got/forge/biome_modifier'
def vals(name): return json.loads((T/name).read_text())['values']
assert vals('spawns_north_npcs.json') == ['got:north']
assert vals('spawns_westerlands_npcs.json') == ['got:westerlands']
assert vals('spawns_riverlands_npcs.json') == ['got:riverlands']
assert vals('spawns_reach_npcs.json') == ['got:reach']
assert vals('spawns_dorne_npcs.json') == ['got:dorne']
assert vals('spawns_wildling_npcs.json') == ['got:haunted_forest','got:gift_new','got:skagos','got:stoney_shore','got:frozen_shore','got:north','got:north_town','got:gift_old','got:north_wild','got:thenn_land']
assert vals('spawns_white_walker_npcs.json') == ['got:haunted_forest']
assert vals('spawns_yi_ti_npcs.json') == ['got:yi_ti']
assert vals('spawns_jogos_nhai_incursions.json') == ['got:yi_ti','got:yi_ti_border_zone']
assert vals('spawns_norvos_npcs.json') == ['got:norvos','got:volantis']
assert not (M/'night_watch_npcs.json').exists()
assert not (M/'dothraki_npcs.json').exists()
a=(R/'src/main/java/got/npc/GOTArrynNpcEntity.java').read_text()
assert 'arryn_mountains_foothills' in a and 'randomHillmanFighter' in a
w=(R/'src/main/java/got/npc/GOTWildlingNpcEntity.java').read_text()
assert 'isLegacyWildlingBiome' in w and 'stoney_shore' in w
ww=(R/'src/main/java/got/npc/GOTWhiteWalkerNpcEntity.java').read_text()
assert 'metadata.id().equals("haunted_forest")' in ww
n=(R/'src/main/java/got/npc/GOTNorvosNpcEntity.java').read_text()
assert 'metadata.id().equals("volantis")' in n
print('NPC biome population integration audit passed')
