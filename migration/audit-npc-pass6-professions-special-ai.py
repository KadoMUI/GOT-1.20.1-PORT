from pathlib import Path
ROOT=Path(__file__).resolve().parents[1]/'src/main/java/got/npc'

def req(file,*needles):
    s=(ROOT/file).read_text()
    miss=[n for n in needles if n not in s]
    if miss: raise SystemExit(f'{file}: missing {miss}')

# Merchant engines that previously skipped the shared stock-lock/decay runtime.
req('GOTSummerIslesNpcEntity.java','GOTNpcTraderRuntime.tick(this, summerOffers)','GOTTraderAdvertisement.tick(this)')
req('GOTSothoryosNpcEntity.java','GOTNpcTraderRuntime.tick(this, sothoryosOffers)','GOTTraderAdvertisement.tick(this)')
req('GOTMossovyNpcEntity.java','GOTNpcTraderRuntime.tick(this, offers)','GOTTraderAdvertisement.tick(this)')
req('GOTGoldenCompanyNpcEntity.java','GOTNpcTraderRuntime.tick(this, offers)','GOTTraderAdvertisement.tick(this)')

# Special-role behavior already restored and protected by this parity gate.
req('GOTAsshaiNpcEntity.java','ASSHAI_SHADOWBINDER','SmallFireball','ASSHAI_SPHEREBINDER','target.push','ASSHAI_ARCHMAG','LargeFireball')
req('GOTMossovyNpcEntity.java','MOSSOVY_WITCHER','Combat.HYBRID','distanceToSqr(target) > 36.0D','requires_alignment')
req('GOTGhiscarNpcEntity.java','GHISCAR_UNSULLIED -> 50.0D','ghiscar_astapor')
req('GOTQohorNpcEntity.java','QOHOR_UNSULLIED','50.0D')
req('GOTSothoryosNpcEntity.java','SOTHORYOS_BLOWGUNNER','performRangedAttack','dart.setBaseDamage')
req('GOTArrynNpcEntity.java','getRole().prostitute()','return "unaligned"')
print('NPC Pass 6 profession/special-AI audit: PASS')
