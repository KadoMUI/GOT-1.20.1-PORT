from pathlib import Path
root=Path(__file__).resolve().parents[1]
checks={
 'alloy block entity':'class AlloyForgeBlockEntity' in (root/'src/main/java/got/AlloyForgeBlockEntity.java').read_text(),
 'alloy 13 slots':'SLOT_COUNT=13' in (root/'src/main/java/got/AlloyForgeBlockEntity.java').read_text(),
 'alloy 200 ticks':'COOK_TOTAL=200' in (root/'src/main/java/got/AlloyForgeBlockEntity.java').read_text(),
 'bronze pair':'got:bronze_ingot' in (root/'src/main/java/got/GOTMachineRecipes.java').read_text(),
 'alloy steel pair':'got:alloy_steel_ingot' in (root/'src/main/java/got/GOTMachineRecipes.java').read_text(),
 'ice reforging pair':'got:widow_wail' in (root/'src/main/java/got/GOTMachineRecipes.java').read_text() and 'got:oathkeeper' in (root/'src/main/java/got/GOTMachineRecipes.java').read_text(),
 'millstone block entity':'class MillstoneBlockEntity' in (root/'src/main/java/got/MillstoneBlockEntity.java').read_text(),
 'millstone redstone':'MillstoneBlock.POWERED' in (root/'src/main/java/got/MillstoneBlockEntity.java').read_text(),
 'millstone 200 ticks':'COOK_TOTAL=200' in (root/'src/main/java/got/MillstoneBlockEntity.java').read_text(),
 'millstone chance':'.25F' in (root/'src/main/java/got/GOTMachineRecipes.java').read_text() and '.75F' in (root/'src/main/java/got/GOTMachineRecipes.java').read_text(),
 'achievement alloy':'USE_ALLOY_FORGE' in (root/'src/main/java/got/AlloyForgeMenu.java').read_text(),
 'achievement mill':'USE_MILLSTONE' in (root/'src/main/java/got/MillstoneMenu.java').read_text(),
 'menus registered':'GOTMenus.ALLOY_FORGE' in (root/'src/main/java/got/GOTClientEvents.java').read_text() and 'GOTMenus.MILLSTONE' in (root/'src/main/java/got/GOTClientEvents.java').read_text(),
}
for k,v in checks.items(): print(('PASS' if v else 'FAIL'),k)
raise SystemExit(0 if all(checks.values()) else 1)
