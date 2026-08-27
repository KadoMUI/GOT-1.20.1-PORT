# Project Thrones — Mounts Pass 6

## Mounted hiring integration

This pass reconnects the 51 mounted unit-trade entries that were deliberately deferred while the mount runtime was incomplete.

### Implemented
- Extended data-driven hire definitions with optional mount type, legacy mount label, and mount armor metadata.
- Generated all 51 mounted definitions from `docs/DEFERRED_MOUNTED_HIRE_ENTRIES.json`.
- Preserved unmounted definitions as distinct choices rather than allowing duplicate role IDs to resolve arbitrarily.
- Added explicit mounted-definition resolution in `GOTHiringRuleService`.
- Added a `Hire Mounted` choice to the hire GUI only for roles with a mounted legacy variant.
- The server independently re-evaluates mounted alignment, pledge and price requirements; the client cannot force an unavailable mounted hire.
- Mounted transactions create the correct Project Thrones mount and pair it through the Pass 5 `mountNpc` contract.
- Supported legacy mounted hire species in the recovered table: Horse (48), Zebra (2), Wooly Rhino (1).
- Restored iron horse armor for the 46 mounted definitions whose legacy trade specifies it.
- Mount labels such as `Rider` are retained as mount custom names.
- If mount creation/pairing fails, the hire is rolled back and the player's coin value is refunded.

### Result
The previously deferred 51 mounted hire entries are now active data rather than audit-only records. Mounted and unmounted variants no longer collide merely because they share the same NPC role ID.

### Build note
`./gradlew compileJava --offline --no-daemon` was attempted, but the wrapper still attempts to fetch Gradle 8.8 from services.gradle.org and this environment cannot resolve outbound DNS. Local Gradle compilation remains required.
