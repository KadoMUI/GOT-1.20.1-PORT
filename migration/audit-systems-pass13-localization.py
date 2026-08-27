#!/usr/bin/env python3
from pathlib import Path
import json,re,sys
root=Path(__file__).resolve().parents[1]
java_root=root/'src/main/java'
lang=json.loads((root/'src/main/resources/assets/got/lang/en_us.json').read_text(encoding='utf-8'))
errors=[]
java='\n'.join(p.read_text(encoding='utf-8',errors='ignore') for p in java_root.rglob('*.java'))
# Every literal GOT translation key must resolve. Deliberate dynamic prefixes end in '.'.
used=set(re.findall(r'Component\.translatable\("([^"]+)"',java))
missing=sorted(k for k in used if k.startswith('got.') and not k.endswith('.') and k not in lang)
if missing: errors += [f'missing static translation: {k}' for k in missing]
# Standalone localization button must be gone from GOTGuiMenu.
menu=(java_root/'got/client/gui/GOTGuiMenu.java').read_text()
if 'got.gui.languages' in menu: errors.append('standalone Localization button still present in GOTGuiMenu')
settings=(java_root/'got/client/gui/GOTGuiSettings.java').read_text()
for token in ('got.gui.settings.localization','LanguageSelectScreen'):
    if token not in settings: errors.append(f'Settings localization integration missing: {token}')
# Player-visible entity registry translations that were previously leaking raw IDs.
entity_ids=set()
for p in java_root.rglob('*.java'):
    txt=p.read_text(encoding='utf-8',errors='ignore')
    entity_ids.update(re.findall(r'ENTIT(?:Y|IES)[A-Z_]*\.register\("([a-z0-9_]+)"',txt))
for i in entity_ids:
    # Some hits are block-entity registries. Require translation only if key was intentionally added or it is an NPC/projectile/banner.
    if any(x in i for x in ('_npc','projectile','banner','thrown_axe')) or i in {'spear_projectile','pebble_projectile'}:
        if f'entity.got.{i}' not in lang: errors.append(f'missing entity translation: {i}')
for k in ('generator.got.planetos','got.gui.settings.localization','got.command.claim.info','got.quest.accepted'):
    if k not in lang: errors.append(f'missing required localization: {k}')
if lang.get('generator.got.planetos')!='PLANETOS': errors.append('Planetos preset label is not exactly PLANETOS')
if errors:
    print('FAIL')
    print('\n'.join(errors))
    sys.exit(1)
print('PASS — Systems Pass 13 localization')
print(f'English keys: {len(lang)}')
print(f'Static GOT translation calls checked: {len([k for k in used if k.startswith("got.")])}')
print('Standalone Localization menu button: removed')
print('Settings -> Localization: wired to Minecraft language selector')
print('Planetos preset: PLANETOS')
