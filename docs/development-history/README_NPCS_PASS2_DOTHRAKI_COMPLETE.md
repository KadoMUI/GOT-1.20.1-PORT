# Project Thrones — NPC Pass 2: Dothraki Completion

Built on `Project-Thrones-npcs-pass1-dothraki-mounted.zip`.

## Scope

This pass finishes the Dothraki NPC behavior/population layer before moving to the wider NPC audit. It deliberately does **not** implement or reposition Dothraki world structures; the new population API is designed to plug into those structures when the WorldGen stabilization pass reaches them.

## Added

### Khalasar groups
- Added `GOTDothrakiKhalasarPopulation` as the common group-spawn entry point for future Dothraki settlements/camps and scripted encounters.
- A khalasar has one chieftain leader plus a configurable 4–32 fighters.
- Fighters are a 3:1 melee/archer mix.
- The chieftain is always mounted under Project Thrones' Dothraki mount policy; ordinary fighters use the existing 85% mounted world-spawn policy.
- Each group receives a persistent UUID and each follower stores the chieftain UUID.
- Group membership and leadership survive save/reload.

### Mounted formation/patrol behavior
- Added `GOTDothrakiKhalasarGoal`.
- Followers regroup around their chieftain when idle rather than wandering away independently.
- Followers inherit the chieftain's active target so a khalasar engages as one group.
- `GOTMountEntity` now understands an idle Dothraki rider's khalasar leader and moves the horse to regroup with that leader. This closes the gap where a passenger NPC could know it should follow the group but could not directly steer its mount while idle.

### Legacy Dothraki skirmishing
- Reconstructed the important behavior of legacy `GOTEntityAIDothrakiSkirmish`.
- Adult male ordinary Dothraki can rarely enter a short 160-tick practice fight with another eligible Dothraki.
- The legacy base chance is 1/20,000 and is accelerated by nearby ongoing skirmishes, down to a 1/40 floor.
- Chieftains, shamans, women, mounted riders, and special characters do not initiate skirmishes.
- Members of the same khalasar never skirmish each other.
- Skirmish state persists through save/reload and terminates without permanently making the Dothraki faction hostile to itself.

### Population/respawn integration
- `GOTNpcRespawnerData` now supports the `dothraki` family.
- Respawned Dothraki regain the appropriate world-mounted roll automatically.
- Existing Free Cities/eastern population placement now applies Dothraki mount policy to any ordinary Dothraki role routed through it.
- Spawner items, invasions, mounted hiring, and the new khalasar factory all continue using the same central horse creation service.

## Intentional Project Thrones divergence

The final 1.7.10 ordinary Dothraki constructor only rolled a riding horse at roughly 10%. Project Thrones intentionally uses the stronger policy chosen for the modern port: chieftains always mounted and warriors/archers 85% mounted for world-generated groups. Dothraki horse culture is therefore visibly dominant even if other factions receive less mounted population later.

## Legacy sources audited
- `GOTEntityDothraki`
- `GOTEntityDothrakiArcher`
- `GOTEntityDothrakiChieftain`
- `GOTEntityDothrakiShaman`
- `GOTEntityAIDothrakiSkirmish`
- `GOTStructureDothrakiSettlement$Instance`
- all four Dothraki settlement NPC respawners
- `GOTStructureDothrakiChieftainTent`

## Validation

Static source audit completed. `./gradlew compileJava` could not execute because the wrapper attempts to download Gradle 8.8 from `services.gradle.org`, which is unreachable from this environment.

Recommended in-game smoke tests:
1. Spawn normal Dothraki/archers/chieftains from spawner items and verify mount distribution.
2. Trigger a Dothraki invasion and verify mounted combat.
3. Spawn a khalasar through a test hook/structure marker and verify regrouping, leader target propagation, persistence, and mounted following.
4. Kill a respawner-backed Dothraki and confirm the replacement can mount again after the respawn delay.
5. Leave a cluster of eligible foot Dothraki alive long enough to observe a temporary skirmish; verify same-khalasar members do not participate.
