from pathlib import Path
root=Path(__file__).resolve().parents[1]
checks={
'crime data':root/'src/main/java/got/crime/GOTCrimeData.java',
'crime service':root/'src/main/java/got/crime/GOTCrimeService.java',
'crime events':root/'src/main/java/got/crime/GOTCrimeEvents.java'}
for n,p in checks.items(): assert p.exists(),n
s=(root/'src/main/java/got/crime/GOTCrimeData.java').read_text(); assert '3_456_000' in s and '864_000' in s
s=(root/'src/main/java/got/crime/GOTCrimeService.java').read_text(); assert 'random.nextInt(3)==0' in s and 'random.nextInt(lootSuccess?3:4)==0' in s and '-1.0F' in s
q=(root/'src/main/java/got/quest/GOTQuestParityLedger.java').read_text(); assert 'Requires player-bounty/crime subsystem' not in q
print('Systems Pass 3 crime/bounty/pickpocket audit: PASS')
