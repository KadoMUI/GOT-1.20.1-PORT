# Systems Pass 12 — Legacy Combat / Projectile Parity

Source-only pass. No Gradle build attempted.

## Restored legacy behavior

- `arrow_fire` is real ArrowItem ammunition and applies the legacy difficulty-scaled burn effect.
- `arrow_poisoned` is real ArrowItem ammunition and applies the legacy standard poison effect.
- `crossbow_bolt` / `crossbow_bolt_poisoned` are ammunition again, not CrossbowItem placeholders. Bolts use a 2x arrow base-damage profile; poisoned bolts apply standard poison.
- GOT crossbows retain vanilla 1.20.1 crossbow mechanics, accept the restored ArrowItem ammunition, and award `USE_CROSSBOW` when fired.
- Material Throwing Axes are one-item throwable weapons again. Their projectile preserves the thrown item, uses material-derived damage, can be recovered, and awards `USE_THROWING_AXE`.
- Material Spears are melee + charge-to-throw weapons again. Legacy damage is one point below the material sword, thrown damage uses the old 0.7 ranged multiplier model, the thrown item is recoverable, and throwing awards `USE_SPEAR`.
- `harpoon` is restored as the legacy Iron Spear alias and uses the same throwing behavior.
- Material Pikes are melee-only sword-class weapons; no throw action is added.
- Poisoned Daggers now apply the legacy standard poison effect on melee hit and use the legacy dagger damage reduction (sword damage -3).
- Normal material Daggers that exist in the registry also use the recovered dagger damage profile.
- `fire_pot` is a stack-of-4 throwable again. Impact deals 3 damage to the direct target, 1 to nearby living targets in a 3-block radius, lights successful targets for 2–4 seconds plus an extra 2–4 seconds on direct hit, awards `USE_FIRE_POT`, and can detonate a Wild Fire Jar.

## Explicitly separate

The newer Westeros/Essos regional Sword/Spear/Hammer/Polearm family is not replaced by this pass. Those are Project Thrones faction-table weapons; this pass restores the older material weapon families.

## Legacy findings

Recovered from the final 1.7.10 classes:
- GOTItemPike is melee-only.
- GOTItemSpear is sword damage -1, chargeable/throwable, and uses a 0.7 ranged multiplier.
- Harpoon is instantiated as GOTItemSpear(IRON).
- GOTItemThrowingAxe is a one-item throwable with material-derived damage.
- GOTItemFirePot stacks to 4 and uses a 3-block impact radius.
- GOTItemDagger subtracts 3 damage from the material sword profile.
- Standard poison duration is difficulty-scaled: `(1 + difficulty*2) + random(0..base-1)` seconds.
- Standard fire duration is `1 + difficulty*10` seconds.
