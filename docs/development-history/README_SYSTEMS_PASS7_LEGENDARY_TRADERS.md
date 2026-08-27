# Project Thrones — Systems Pass 7: Legendary / Special Trader Parity

Source-only pass built from Systems Pass 6.

## Scope
Audited all 19 `legendary/trader` entity classes from the final 1.7.10 JAR and reconciled their legacy sell/buy pool identity with the 1.20.1 NPC roster.

## Unique pools restored
- Petyr Baelish: BAELISH_SELLS / EMPTY_BUYS. One-time Petyr dagger sale at legacy value 4096. Sale state persists; if he dies before selling it, the dagger drops alongside his brooch.
- Davos Seaworth: DAVOS_SELLS / MAESTER_BUYS (corrected from EMPTY_BUYS). Leek, legacy leek-crop equivalent, and leek soup at legacy value 5.
- Tobho Mott: TOBHO_SELLS / EMPTY_BUYS. Valyrian dagger/poisoned dagger/hammer/spear/sword, plate armor and chainmail at recovered legacy prices.

## Pool identity corrections
- Harmune: MAESTER_SELLS / MAESTER_BUYS (was being treated as Blacksmith)
- Mullin: MAESTER_SELLS / MAESTER_BUYS (was being treated as Blacksmith)
- Craster: BUTCHER_SELLS / BUTCHER_BUYS (replaces hand-authored mini-shop)
- Aeron Greyjoy: ALCHEMIST_SELLS / ALCHEMIST_BUYS (replaces modern Priest approximation)

The remaining named traders already matched their legacy profession pools: Aemon, Ebrose, Gendry, High Septon, Luwin, Pycelle, Qyburn, Illyrio, Tycho, plus the restored Hot Pie, Moqorro and Xaro.

## Restored missing legendary traders
- Hot Pie — Riverlands, Baker pool, 1 alignment, 0.9 scale. Legacy spawn remains tied to the Crossroads Tavern structure, so the role/trading/rendering is ready while structure generation is intentionally outside this systems pass.
- Moqorro — Asshai faction, Alchemist pool, 100 alignment, Skull Staff. Fixed legacy Volantis settlement offset (-1, 0).
- Xaro Xhoan Daxos — Qarth faction, Goldsmith pool, 500 alignment, poisoned Alloy Steel dagger. Fixed legacy Qarth settlement offset (+3, 0).

Their original `_1`/`_2` legendary textures are wired through the modern layered legendary renderer.

## Other recovered special behavior
- Petyr requires non-negative Crownlands alignment to access his unique sale and cannot sell the dagger twice.
- Gendry now restores his Blood of True Kings death drop.
- Pycelle now restores his Bottle of Poison death drop.
- The legendary-trader role→pool ledger now records all 19 legacy traders.

## Validation
Run:
`python migration/audit-systems-pass7-legendary-traders.py`

Expected: `Systems Pass 7 legendary trader audit: PASS`

No Gradle build was attempted per project workflow.
