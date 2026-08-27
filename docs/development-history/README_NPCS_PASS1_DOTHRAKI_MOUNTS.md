# Project Thrones NPC Pass 1 — Dothraki Mounted NPCs

This pass begins the NPC completion phase on top of Faction Crafting Tables Pass 7.

## Dothraki mount integration
- Added `GOTDothrakiMountService`, a single horse-spawn path for Dothraki NPC population systems.
- Chieftains are always mounted when using world/population mount selection.
- Ordinary Dothraki warriors and archers are mounted 85% of the time, leaving a small dismounted camp/foot contingent.
- Shamans, Daenerys and Jorah are not auto-mounted by this rule.
- Dothraki NPC spawner items now use the mounted distribution.
- Dothraki invasion units now use the mounted distribution, turning Dothraki invasions into actual mounted hordes.
- Mount requests persist through NBT and retry if the first horse spawn is temporarily obstructed.
- Dynamically created horses run `finalizeSpawn` so legacy GOT horse stat setup and vanilla horse variants are applied.
- The mount is tamed, saddled, persistent, NPC-owned and attached through the Mounts Pass 5/6 `mountNpc` contract.

## Hiring behavior
Explicit hiring remains unchanged. The player can still choose the ordinary foot hire or the mounted Dothraki hire restored in Mounts Pass 6. Auto-mounting is not applied to an explicitly purchased foot unit.

## Future population hooks
Future khalasar/camp/settlement population code only needs to call `rollWorldMount()` followed by `requestDothrakiHorse()` after preparing a combat NPC; the actual horse creation is deferred safely until the NPC is in the server level.
