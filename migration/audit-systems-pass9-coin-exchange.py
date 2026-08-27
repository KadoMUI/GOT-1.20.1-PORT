from pathlib import Path
root=Path(__file__).resolve().parents[1]
checks={
 'menu': root/'src/main/java/got/GOTCoinExchangeMenu.java',
 'screen': root/'src/main/java/got/client/GOTCoinExchangeScreen.java',
 'event': root/'src/main/java/got/economy/GOTCoinExchangeEvents.java',
 'texture': root/'src/main/resources/assets/got/textures/gui/coin_exchange.png',
}
for name,p in checks.items():
    assert p.exists(), f'missing {name}: {p}'
menu=checks['menu'].read_text()
assert 'computeBreakDown' in menu and 'computeConsolidate' in menu
assert 'consume * 4' in menu and 'in.getCount() / 4' in menu
assert 'Math.min(in.getCount(), 16)' in menu
assert 'clearContainer(player, input)' in menu and 'clearContainer(player, results)' in menu
event=checks['event'].read_text()
assert 'EventPriority.HIGHEST' in event and 'isShiftKeyDown' in event
assert 'GOTCoinValueService.valueOf(player.getMainHandItem())' in event
menus=(root/'src/main/java/got/GOTMenus.java').read_text(); assert 'COIN_EXCHANGE' in menus
client=(root/'src/main/java/got/GOTClientEvents.java').read_text(); assert 'GOTCoinExchangeScreen' in client
lang=(root/'src/main/resources/assets/got/lang/en_us.json').read_text(); assert 'got.container.coinExchange' in lang
print('SYSTEMS PASS 9 COIN EXCHANGE: PASS')
