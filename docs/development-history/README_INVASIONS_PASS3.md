# Project Thrones — Invasions Pass 3

Pass 3 is the behavioral-fidelity pass against `Game of Thrones 24.08.29` (1.7.10).

## Restored from the original invasion spawner

- **Kill-goal semantics:** `invasionRemaining` now means player kills still required. Spawning a mob no longer consumes the invasion objective. Only a player kill advances the objective.
- **Original pacing:** max 16 live invasion NPCs within the runtime, 1/160 spawn-roll per tick, batches of 1–6, 40 placement attempts, and failure shutdown after 16 consecutive failed batches.
- **Original spawn volume:** horizontal placement remains ±6 blocks and vertical attempts use the original -8..+4 band around the invasion center, with modern collision/air validation.
- **Player-progress timeout:** natural invasions that receive no player progress for 6000 ticks regain one required kill every 1200 ticks, capped at the original invasion size. Warhorn invasions are exempt, matching 1.7.10.
- **Contributor expiry:** player contributors now remain eligible for completion rewards for 2400 ticks after their most recent invasion kill, matching the original `recentPlayerContributors` map.
- **Peaceful handling:** active invasions terminate without victory when the level is switched to Peaceful.
- **Horn feedback:** the original `got:item.horn` sound is played at invasion start and after successful spawn batches at volume 4.0 and pitch 0.65–0.75.
- **Start visibility:** natural invasion start notices are restricted to nearby players hostile to the invading faction; a Warhorn initiator is always notified.
- **Completion eligibility:** defeat-invasion achievement eligibility uses the original <= 0 alignment check and 100-block contributor range.
- **Pledged-faction victory bonus:** the original 50-point victory pool is restored and divided among recent nearby contributors pledged to factions hostile to the invader.
- **End effect:** restores the zero-strength invasion-spawner end explosion effect without terrain damage.
- **Persistence:** Warhorn/natural source, idle-progress timer, and contributor TTLs are persisted in SavedData.

## Important Pass 2 correction

Pass 2 treated `remaining` as a spawn budget. The 1.7.10 bytecode proves it is instead a **player-kill objective**: `addPlayerKill` decrements it, while successful spawning does not. Pass 3 corrects this fundamental lifecycle behavior.

## Still intentionally deferred

- Client-side 1.7.10 invasion watcher HUD/spawner renderer parity (`GOTPacketInvasionWatch`, `GOTRenderInvasionSpawner`). The modern SavedData runtime has no physical spawner entity yet.
- Exact visual representation of the floating invasion spawner.
- Exhaustive multiplayer/gameplay validation of all 27 invasion types.

## Build note

A compile was attempted in the artifact environment, but the Gradle wrapper could not download Gradle 8.8 because outbound network/DNS access is unavailable. Run `gradlew build` locally as the authoritative compile test.
