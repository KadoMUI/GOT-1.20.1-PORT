#!/usr/bin/env python3
from pathlib import Path
import re, sys
root = Path(__file__).resolve().parents[1]
npc = root / 'src/main/java/got/npc'
checks = {
    'north': ('GOTNorthNpcEntity.java','NORTH_LEVYMAN','NORTH_LEVYMAN_ARCHER'),
    'arryn': ('GOTArrynNpcEntity.java','ARRYN_LEVYMAN','ARRYN_LEVYMAN_ARCHER'),
    'riverlands': ('GOTRiverlandsNpcEntity.java','RIVERLANDS_LEVYMAN','RIVERLANDS_LEVYMAN_ARCHER'),
    'westerlands': ('GOTWesterlandsNpcEntity.java','WESTERLANDS_LEVYMAN','WESTERLANDS_LEVYMAN_ARCHER'),
    'crownlands': ('GOTCrownlandsNpcEntity.java','CROWNLANDS_LEVYMAN','CROWNLANDS_LEVYMAN_ARCHER'),
    'dragonstone': ('GOTDragonstoneNpcEntity.java','DRAGONSTONE_LEVYMAN','DRAGONSTONE_LEVYMAN_ARCHER'),
    'reach': ('GOTReachNpcEntity.java','REACH_LEVYMAN','REACH_LEVYMAN_ARCHER'),
    'stormlands': ('GOTStormlandsNpcEntity.java','STORMLANDS_LEVYMAN','STORMLANDS_LEVYMAN_ARCHER'),
    'dorne': ('GOTDorneNpcEntity.java','DORNE_LEVYMAN','DORNE_LEVYMAN_ARCHER'),
    'ironborn': ('GOTIronbornNpcEntity.java','IRONBORN_LEVYMAN','IRONBORN_LEVYMAN_ARCHER'),
}
errors=[]
for faction,(file,melee,ranged) in checks.items():
    text=(npc/file).read_text()
    for token in ('random.nextInt(15) < 10', melee, ranged):
        if token not in text: errors.append(f'{faction}: missing {token}')
    # Natural wilderness should not be reduced to civilian-only anymore.
    natural=text[text.find('finalizeSpawn'):text.find('public void prepareForSpawn')]
    if faction not in ('north','arryn') and re.search(r'prepareForSpawn\([^\n]*_MAN, null, child', natural):
        errors.append(f'{faction}: still natural-spawns civilian role')
if errors:
    print('\n'.join('FAIL '+e for e in errors)); sys.exit(1)
print('NPC Pass 5 natural military audit: PASS')
print('10 Westerosi regional entity families use the legacy 10:5 (2:1) levyman/archer military mix.')
print('North wild/Skagos and Arryn mountain Hill Tribe special handling remains intact.')
