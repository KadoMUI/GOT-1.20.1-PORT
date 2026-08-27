from pathlib import Path
import re, json
root=Path(__file__).resolve().parents[1]
cat=(root/'src/main/java/got/achievement/GOTAchievementCatalog.java').read_text()
codes=re.findall(r'new Entry\("[^"]+", "([^"]+)"',cat)
assert len(codes)==390, len(codes)
ev=(root/'src/main/java/got/achievement/GOTAchievementEvents.java').read_text()
for token in ['ENTER_','WEAR_FULL_','KILL_','RIDE_','TRAVEL','CRAFT_','DRINK_','USE_','GET_']:
    assert token in ev, token
hooks=(root/'src/main/java/got/achievement/GOTAchievementHooks.java').read_text()
assert 'canonical' in hooks and 'copyPersisted' in hooks and 'sync(player)' in hooks
lore_files=list((root/'src/main/resources/data/got/lore_legacy/texts').rglob('*.txt'))
assert len(lore_files)==502, len(lore_files)
reg=(root/'src/main/java/got/lore/GOTLoreRegistry.java').read_text()
assert 's.startsWith("#")' in reg and 'uFEFF' in reg
for f in ['GOTLoreBookService.java','GOTLoreEvents.java','GOTLoreReloadEvents.java']:
    assert (root/'src/main/java/got/lore'/f).exists(), f
net=(root/'src/main/java/got/network/GOTNetwork.java').read_text()
assert 'C2SRequestLoreDataPacket' in net and 'S2CLoreDataPacket' in net
quest=(root/'src/main/java/got/quest/GOTQuestService.java').read_text()
assert 'nextInt(10)' in quest and 'GOTLoreBookService.randomForFaction' in quest
print(json.dumps({'achievement_catalog':len(codes),'lore_resources':len(lore_files),'status':'PASS'},indent=2))
