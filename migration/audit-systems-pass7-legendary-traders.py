from pathlib import Path
import json
root=Path(__file__).resolve().parents[1]
checks={
 'helper': root/'src/main/java/got/economy/GOTLegendaryTraderOffers.java',
 'catalog': root/'src/main/resources/data/got/economy/legendary_trader_parity_audit.json',
 'roles': root/'src/main/resources/data/got/economy/role_trade_pool_map.json',
}
for k,p in checks.items():
    assert p.exists(), f'missing {k}: {p}'
text=checks['helper'].read_text()
for token in ['petyr_baelish','davos_seaworth','tobho_mott','harmune','mullin','craster','aeron_greyjoy']:
    assert token in text, token
role_files='\n'.join(p.read_text(errors='ignore') for p in (root/'src/main/java/got/npc').glob('*NpcRole.java'))
for token in ['hot_pie','moqorro','xaro_xhoan_daxos']:
    assert token in role_files, token
pop=(root/'src/main/java/got/npc/GOTFreeCitiesNpcPopulation.java').read_text()
assert 'fixed(GOTWaypoint.VOLANTIS, City.ASSHAI, "moqorro", -1, 0)' in pop
assert 'fixed(GOTWaypoint.QARTH, City.QARTH, "xaro_xhoan_daxos", 3, 0)' in pop
mapping=json.loads(checks['roles'].read_text())['special']
assert mapping['davos_seaworth']['buys']=='MAESTER_BUYS'
audit=json.loads(checks['catalog'].read_text())
assert audit['count'] >= 19
print('Systems Pass 7 legendary trader audit: PASS')
