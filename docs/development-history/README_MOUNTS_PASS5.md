# Project Thrones — Mounts Pass 5: Behavior + Mounted Combat

Builds the server-side behavior layer on top of Pass 4's reconstructed legacy mount stats.

## Implemented
- Preserves vanilla saddle/player riding controls inherited from `Horse` for GOT Horse/Zebra and the custom legacy mount subclasses.
- Adds an explicit `mountNpc(Mob)` contract to pair a GOT mount with an NPC rider.
- Persists the NPC rider UUID with the mount and safely clears stale identity after a rider is gone.
- NPC-owned mounts are automatically tamed, saddled and made persistent.
- Mounted NPC steering follows the rider's combat target, faces the target, advances to melee range, and holds distance for bow users.
- Idle NPC mounts stop rather than wandering out from under their rider.
- Rhino/Wooly Rhino gain mount-assisted melee using their reconstructed 4 damage stat.
- Boars gain mount-assisted melee using their reconstructed 3 damage stat and a slightly faster attack interval.
- Mount attacks inherit faction safety from the NPC rider: allied/same-friendly faction NPCs are rejected as mount attack targets.
- Species movement personalities are separated for mounted AI: rhinos are deliberately heavier/slower to steer, camels moderate, boars more aggressive.
- Existing species jump/fall handling continues to derive from the reconstructed per-species jump-strength rules in Pass 4.
- Normal Minecraft passenger lifecycle handles dismount/death; GOT rider identity is no longer kept indefinitely when the rider disappears.

## Integration boundary
This pass deliberately does **not** activate the 51 deferred mounted hiring definitions. The mount-side API is now present for that work: creation code can spawn a mount and call `mount.mountNpc(npc)`.

Pass 6 should resolve mounted-vs-unmounted hiring selection and activate those legacy mounted definitions. That pass is also the right point to wire regional population spawners to the same mount API.

## Build note
`./gradlew compileJava --offline --no-daemon` was attempted. The wrapper still tries to fetch Gradle 8.8 from services.gradle.org and this environment has no outbound DNS, so compilation could not be performed here.
