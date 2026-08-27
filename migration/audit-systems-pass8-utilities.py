from pathlib import Path
root=Path(__file__).resolve().parents[1]
def read(p): return (root/p).read_text()
checks={
'pouch_items':'POUCH_SMALL' in read('src/main/java/got/GOTItems.java') and 'POUCH_MEDIUM' in read('src/main/java/got/GOTItems.java') and 'POUCH_LARGE' in read('src/main/java/got/GOTItems.java'),
'pouch_menu':'class GOTPouchMenu' in read('src/main/java/got/GOTPouchMenu.java'),
'pouch_combine':'POUCH_COMBINE' in read('src/main/java/got/GOTRecipes.java'),
'sling_projectile':'class GOTPebbleEntity' in read('src/main/java/got/GOTPebbleEntity.java'),
'sarbacane':'class GOTSarbacaneItem' in read('src/main/java/got/GOTSarbacaneItem.java'),
'dart_projectile':'class GOTDartEntity' in read('src/main/java/got/GOTDartEntity.java'),
'poisoned_dart':'MobEffects.POISON' in read('src/main/java/got/GOTDartEntity.java'),
'branding':'class GOTBrandingIronItem' in read('src/main/java/got/GOTBrandingIronItem.java') and 'BRAND_ENTITY' in read('src/main/java/got/GOTBrandingIronItem.java'),
'branding_packet':'C2SBrandingNamePacket' in read('src/main/java/got/network/GOTNetwork.java'),
'protocol14':'PROTOCOL = "14"' in read('src/main/java/got/network/GOTNetwork.java'),
}
for k,v in checks.items(): print(('PASS' if v else 'FAIL'),k)
assert all(checks.values())
