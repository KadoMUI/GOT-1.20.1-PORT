from pathlib import Path
p=Path(__file__).resolve().parents[1]/'src/main/java/got/GOTEquipment.java'
s=p.read_text()
fail=[]
checks={
 'fire arrow functional':'GOTLegacyArrowEntity.Kind.FIRE_ARROW',
 'poison arrow functional':'GOTLegacyArrowEntity.Kind.POISON_ARROW',
 'bolt ammo functional':'GOTLegacyArrowEntity.Kind.BOLT',
 'poison bolt functional':'GOTLegacyArrowEntity.Kind.POISON_BOLT',
 'fire pot functional':'new GOTFirePotItem',
 'harpoon spear':'HARPOON = ITEMS.register("harpoon", () -> new GOTLegacySpearItem',
 'iron spear functional':'IRON_SPEAR = ITEMS.register("iron_spear", () -> new GOTLegacySpearItem',
 'iron pike functional':'IRON_PIKE = ITEMS.register("iron_pike", () -> new GOTLegacyPikeItem',
 'iron throwing axe functional':'IRON_THROWING_AXE = ITEMS.register("iron_throwing_axe", () -> new GOTThrowingAxeItem',
 'iron poison dagger':'IRON_DAGGER_POISONED = ITEMS.register("iron_dagger_poisoned", () -> new GOTPoisonedDaggerItem',
}
for name,needle in checks.items():
    if needle not in s: fail.append(name)
for bad in ['crossbow_bolt", () -> new CrossbowItem','arrow_fire", () -> new Item','fire_pot", () -> new Item']:
    if bad in s: fail.append('placeholder remains: '+bad)
print('PASS' if not fail else 'FAIL')
for f in fail: print('-',f)
raise SystemExit(bool(fail))
