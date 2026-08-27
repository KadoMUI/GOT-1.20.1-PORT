#!/usr/bin/env python3
from pathlib import Path
from PIL import Image
import json, hashlib, sys

root = Path(__file__).resolve().parents[1] / 'src/main/resources/assets/got'
errors=[]
allfiles={str(p.relative_to(root)).lower():p.relative_to(root) for p in root.rglob('*') if p.is_file()}

for p in (root/'models').rglob('*.json'):
    try: data=json.loads(p.read_text())
    except Exception as exc:
        errors.append(f'Invalid JSON {p}: {exc}')
        continue
    textures=data.get('textures') or {}
    if isinstance(textures,dict):
        for ref in textures.values():
            if not isinstance(ref,str) or ref.startswith('#') or ref.startswith('minecraft:'):
                continue
            ns,path=('got',ref)
            if ':' in ref:
                ns,path=ref.split(':',1)
            if ns!='got':
                continue
            rel=Path('textures')/(path+'.png')
            if not (root/rel).exists():
                if str(rel).lower() in allfiles:
                    errors.append(f'Case mismatch: {p.relative_to(root)} -> {ref} (actual {allfiles[str(rel).lower()]})')
                else:
                    errors.append(f'Missing texture: {p.relative_to(root)} -> {ref}')

for sub in ('item','block'):
    for p in (root/'textures'/sub).rglob('*.png'):
        try: w,h=Image.open(p).size
        except Exception:
            continue
        if h>w and h%w==0 and h//w>1 and not Path(str(p)+'.mcmeta').exists():
            errors.append(f'Animated strip missing .mcmeta: {p.relative_to(root)} ({w}x{h})')

required=[
    'textures/item/arrow_fire.png.mcmeta',
    'textures/item/arrow_poisoned.png.mcmeta',
    'textures/item/crossbow_bolt_poisoned.png.mcmeta',
    'textures/block/wild_fire_layer_0.png.mcmeta',
    'textures/item/pouch_small.png','textures/item/pouch_small_overlay.png',
    'textures/item/pouch_medium.png','textures/item/pouch_medium_overlay.png',
    'textures/item/pouch_large.png','textures/item/pouch_large_overlay.png',
    'textures/item/branding_iron_hot.png',
]
for rel in required:
    if not (root/rel).exists(): errors.append(f'Missing required resource: {rel}')

for base,minimum in [('cucumber_crop',4),('pipeweed_crop',4)]:
    hashes=[]
    for i in range(8):
        p=root/'textures/block'/f'{base}_{i}.png'
        if p.exists():
            im=Image.open(p).convert('RGBA')
            hashes.append(hashlib.sha1(im.tobytes()).hexdigest())
    if len(set(hashes)) < minimum:
        errors.append(f'{base} has only {len(set(hashes))} distinct visual stages; expected at least {minimum}')

for base in ['iron','copper','gold','bronze','alloy_steel','valyrian','joffrey_baratheon']:
    item=f'{base}_crossbow'
    model=root/'models/item'/f'{item}.json'
    if not model.exists():
        errors.append(f'Missing crossbow model: {item}')
        continue
    data=json.loads(model.read_text())
    if len(data.get('overrides',[])) < 4:
        errors.append(f'Crossbow pull overrides missing/incomplete: {item}')
    for i in range(3):
        if not (root/'textures/item'/f'{item}_pull_{i}.png').exists():
            errors.append(f'Missing pull texture: {item}_pull_{i}.png')

if errors:
    print('TEXTURE RESOURCE AUDIT: FAIL')
    for e in errors: print(' -',e)
    sys.exit(1)
print('TEXTURE RESOURCE AUDIT: PASS')
