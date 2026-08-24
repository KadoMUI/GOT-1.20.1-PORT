# FINAL MERGE HOOKS — Hiring + Squadrons

These hooks close the Pass 1–6 subsystem against the current 0.6.0 API.

## 1. Faction/alignment bridge

Replace the old TODO implementation of `GOTHiringPlayerRules` with the file in this patch.

It binds directly to:

- `GOTFaction.byId(String)`
- `GOTFactionPlayerData.get(Player)`
- `GOTFactionPlayerData.alignment(GOTFaction)`
- `GOTFactionPlayerData.membership()`

No duplicate faction state is created.

## 2. Exact legacy price calculation

Replace the Pass-2 fixed-item-cost path with:

```java
int price = GOTHiringLegacyPriceService.price(
    player,
    factionNpc.getFactionId(),
    definition.initialCost(),
    definition.requiredAlignment()
);
```

This is the exact `GOTUnitTradeEntry#getCost` formula recovered from 24.08.29.

Important: the old mod did NOT simply charge the table's initialCost.
Unpledged players paid double before alignment discounting.

## 3. Coin payment

Replace the temporary fixed `silver_coin` cost service with `GOTHiringCoinService`.

The current port already has:

- COIN_1
- COIN_4
- COIN_16
- COIN_64
- COIN_256
- COIN_1024
- COIN_4096
- COIN_16384

Hiring price is a total coin VALUE and may be paid using any combination of denominations.

## 4. Hire action packet

In `C2SHiredNpcActionPacket`, replace the HIRE body with:

```java
case HIRE -> {
    GOTHiringTransactionService.hire(player, entity);
}
```

Then rebuild/send the ordinary hired-NPC GUI snapshot.

Do not separately charge the old cost list; `GOTHiringTransactionService` performs the full server-side transaction.

## 5. Pass-6 AI hook

In the shared hired movement tick, after confirming the entity is hired:

```java
if (GOTHiringRuntimeHooks.beforeHiredMovement(mob)) {
    return;
}
```

Before ordinary target acquisition, keep the Pass-6 combat-policy validation:

```java
if (!GOTHiredCombatPolicy.mayAcquireTarget(mob, candidate)) {
    candidate = null;
}
```

At the end of the hired server tick:

```java
GOTHiringRuntimeHooks.periodicEquipmentSafetySync(mob);
```

## 6. Equipment slot hook

Inside `GOTSlotHiredReplaceItem#set(...)`, after `super.set(stack)`:

```java
if (npc instanceof LivingEntity living) {
    GOTHiredEquipmentSlotHook.changed(living, getSlotIndex());
}
```

Also call the same hook after shift-click changes to warrior equipment slots 0..4.

## 7. Resource reload

Call:

```java
GOTHiringCatalog.reload(resourceManager);
```

from the mod's existing server-data reload listener.

The supplied catalog contains 184 immediately applicable unmounted entries.

## 8. What is intentionally NOT active

The full original audit contains 241 entries.

- 184: active and mapped to current 0.6.0 role IDs
- 51: mounted unit-trade variants, intentionally deferred
- 6: current role missing
  - Hillman Warrior
  - Hillman Archer
  - Hillman Axe Thrower
  - Hillman Banner Bearer
  - Hillman Berserker
  - Prostitute

Those six are preserved in `docs/UNRESOLVED_CURRENT_ROLE_ENTRIES.json`.
Nothing is silently discarded.

## 9. Legacy tasks

The original Task enum contained exactly:

- WARRIOR
- FARMER
- PROSTITUTE

`GOTHiredTask` is updated accordingly even though the Prostitute NPC role is not currently implemented.

## 10. Price sanity examples

For an entry with:
- initialCost = 10
- requiredAlignment = 60

At exactly 60 alignment:
- unpledged price = 20
- pledged price = 10

At 1560 alignment:
- pledged excess = 1500
- maximum 50% discount reached
- pledged price = 5

At 2060 alignment:
- unpledged excess = 2000
- maximum 50% discount reached after doubling
- unpledged price = 10

This is intentional legacy behavior.
