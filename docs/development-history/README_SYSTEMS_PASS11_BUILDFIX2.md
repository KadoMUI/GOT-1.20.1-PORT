# Project Thrones 1.20.1 — Systems Pass 11 Build Fix 2

Source-only compile repair pass based on the user's build(10).log.

## Compile errors repaired

- `GOTCoinExchangeMenu`
  - corrected `MenuProvider` import to `net.minecraft.world.MenuProvider`.
- `GOTZebraRenderer`
  - no longer extends final vanilla `HorseRenderer`.
  - now renders `GOTZebraEntity` through `MobRenderer` + vanilla `HorseModel` geometry and the recovered GOT zebra texture.
- `GOTPouchScreen`
  - corrected stale constructor-local `m` references to the screen's `menu` field.
- `GOTDartEntity`
  - NBT overrides are now public, matching `ThrowableItemProjectile` in 1.20.1.
- `GOTInvasionData`
  - corrected `Difficulty` import to `net.minecraft.world.Difficulty`.
  - corrected nearest-player typing (`getNearestPlayer` returns `Player`; server-player requirement is now checked explicitly).
- `GOTLoreBookService`
  - replaced nonexistent `ServerPlayer.clientInformation()` call with `ServerPlayer.getLanguage()`.
- `GOTMountEntity`
  - added `equipHorseArmor(ItemStack)` bridge using the vanilla horse armor equipment slot.
- `GOTLegacyCavalryService`
  - uses the mount armor bridge instead of nonexistent `getInventory()`.
- `GOTHiringTransactionService`
  - uses the mount armor bridge instead of nonexistent `getInventory()`.
- `GOTHiredMountController`
  - only passes a `PathfinderMob` to `GOTHiredCommandState.halted(...)`.
- `PlanetosChunkGenerator`
  - restored missing `GOTNorthNpcPopulation` import.

The previous Build Fix 1 pebble syntax correction is retained.

No Gradle build was attempted in this environment.
