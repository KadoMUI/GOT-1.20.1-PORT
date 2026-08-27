#!/usr/bin/env python3
from pathlib import Path
ROOT=Path(__file__).resolve().parents[1]
J=ROOT/'src/main/java/got'
entity=(J/'npc/GOTDothrakiNpcEntity.java').read_text()
svc=(J/'npc/GOTDothrakiMountService.java').read_text()
spawner=(J/'npc/GOTDothrakiNpcSpawnerItem.java').read_text()
inv=(J/'invasion/GOTInvasionNpcFactory.java').read_text()
assert 'case DOTHRAKI_CHIEFTAIN -> true' in entity
assert 'case DOTHRAKI, DOTHRAKI_ARCHER -> random.nextFloat() < 0.85F' in entity
assert 'DothrakiHorsePending' in entity
assert 'GOTEntities.GOT_HORSE.get().create(level)' in svc
assert 'horse.finalizeSpawn(level, difficulty, MobSpawnType.EVENT' in svc
assert 'horse.mountNpc(rider)' in svc
assert 'if (npc.rollWorldMount()) npc.requestDothrakiHorse();' in spawner
assert 'if (mob.rollWorldMount()) mob.requestDothrakiHorse();' in inv
assert entity.count('public void aiStep()') == 1
print('Dothraki mounted-NPC audit passed: persistent mount requests, dominant mounted combatants, creative spawner and invasion integration')
