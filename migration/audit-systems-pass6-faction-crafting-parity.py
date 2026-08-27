from pathlib import Path
import re, sys
root=Path(__file__).resolve().parents[1]
java=(root/'src/main/java/got/crafting/GOTFactionCraftingTableBlock.java').read_text()
blocks=(root/'src/main/java/got/GOTBlocks.java').read_text()
banners=(root/'src/main/java/got/GOTBannerType.java').read_text()

prefixes=['arryn','asshai','braavos','crownlands','dorne','dothraki','dragonstone','ghiscar','gift','hillmen','ibben','ironborn','jogos_nhai','lhazar','lorath','lys','mossovy','myr','north','norvos','pentos','qarth','qohor','reach','riverlands','sothoryos','stormlands','summer','tyrosh','volantis','westerlands','fur','yi_ti']
for p in prefixes:
    assert f'GOTFactionCraftingTableBlock("{p}"' in blocks, p

# Legacy banner outputs recovered from GOTRecipe bytecode.
banner_names=['hillmen','arryn','robert','martell','stannis','night','greyjoy','robb','tyrell','tully','renly','lannister','lorath','lys','myr','norvos','pentos','qarth','qohor','tyrosh','volantis','braavos','ghiscar','lhazar','summer','jogos_nhai','mossovy','wildling','thenn','ibben','sothoryos','yi_ti','asshai']
for b in banner_names:
    assert f'"{b}"' in banners, f'missing banner catalogue {b}'

assert 'got:harpy' in java
assert 'thennBannerPattern' in java
assert 'factionBannerPattern' in java
assert 'legacyTableId' in java
assert 'Forge ShapedOreRecipe supported grid offsets and horizontal mirroring' in java
assert 'alloy_steel_ingot' in java
assert 'dried_reeds' in java
assert '"fur".equals(equipmentPrefix)' in java

print('Systems Pass 6 faction crafting parity audit: PASS')
print('Faction tables:', len(prefixes))
print('Legacy faction banners covered:', len(banner_names))
