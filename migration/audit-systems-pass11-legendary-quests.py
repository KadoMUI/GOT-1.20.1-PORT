#!/usr/bin/env python3
from pathlib import Path
import glob, json, sys
root=Path(__file__).resolve().parents[1]
java='\n'.join(p.read_text(errors='ignore') for p in (root/'src/main/java').rglob('*.java'))
resources='\n'.join(p.read_text(errors='ignore') for p in (root/'src/main/resources').rglob('*.json'))
quests=[]; errors=[]
for f in glob.glob(str(root/'src/main/resources/data/got/quests/*.json')):
    d=json.load(open(f,encoding='utf-8'))
    if not d.get('legendary'): continue
    quests.append((f,d))
    for role in d.get('giver',{}).get('roles',[]):
        if f'"{role}"' not in java: errors.append(f'{Path(f).name}: missing giver role {role}')
    for o in d.get('objectives',[]):
        target=o.get('target',''); role=o.get('role',''); typ=o.get('type','')
        if role and f'"{role}"' not in java: errors.append(f'{Path(f).name}: missing target role {role}')
        if typ=='KILL_ENTITY' and target.startswith('got:') and f'"{target.split(":",1)[1]}"' not in java:
            errors.append(f'{Path(f).name}: missing entity {target}')
        if typ=='COLLECT' and target.startswith('got:'):
            rid=target.split(':',1)[1]
            if f'"{rid}"' not in java and f'got:{rid}' not in resources:
                errors.append(f'{Path(f).name}: missing item {target}')
bind=json.load(open(root/'src/main/resources/data/got/quests_legacy/legendary_bindings.json',encoding='utf-8'))
raw=json.dumps(bind)
if 'content_audit_pending' in raw: errors.append('legendary_bindings.json still contains content_audit_pending')
if len(quests)!=25: errors.append(f'expected 25 final-build legendary quests, found {len(quests)}')
required=['GOTCrocodileEntity','"crocodile"','MELLARIO("mellario"','GOTWaypoint.NORVOS','"mellario", 0, 1']
for needle in required:
    if needle not in java: errors.append(f'missing integration marker: {needle}')
if errors:
    print('FAIL')
    for e in errors: print(' -',e)
    sys.exit(1)
print('PASS')
print('Legendary quests:',len(quests))
print('Crocodile + Mellario dependencies: present')
print('Pending legendary bindings: 0')
