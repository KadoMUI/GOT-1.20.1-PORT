# Project Thrones — Systems Pass 9: Coin Exchange

This pass reconstructs the legacy GOT Coin Exchange and deliberately does **not** restore the obsolete custom GOT Armor Stand. Vanilla Minecraft's armor stand is the 1.0 replacement.

## Legacy behavior restored

- Eight exact denominations: `1, 4, 16, 64, 256, 1024, 4096, 16384`.
- One coin input slot.
- Left/break-down exchange converts one denomination into four coins of the next lower denomination.
- Right/consolidate exchange converts four coins into one coin of the next higher denomination.
- Break-down output is clamped to the vanilla/legacy 64-item result stack, consuming at most 16 source coins per operation.
- Consolidation consumes only complete groups of four and leaves any remainder in the input slot.
- After an exchange, the menu locks until the produced result is removed, matching the legacy `exchanged` state.
- Closing the menu safely returns any remaining input/output to the player.
- Trader distance/liveness validation closes the menu if the trader is no longer valid.
- Legacy `textures/gui/coin_exchange.png` is used for the reconstructed screen.

## Access

Sneak-right-click a GOT merchant while holding any GOT coin denomination. This action runs before the Crime/Pickpocket handler so holding a coin explicitly means "exchange" rather than "pickpocket".

Normal right-click trading remains unchanged.

## Networking

No custom packet or protocol bump is required. The two exchange controls use Minecraft's standard container `clickMenuButton` packet path and normal slot/data synchronization.

## 1.0 decision

The legacy GOT Armor Stand is intentionally omitted because Minecraft 1.20.1 already provides a native armor stand entity with the required baseline functionality.

Static audit: `migration/audit-systems-pass9-coin-exchange.py`.
