# Project Thrones — Invasions Pass 4

Final presentation/parity pass against GOT 24.08.29 (1.7.10).

## Added
- Modern S2C invasion-watch packet/state, replacing `GOTPacketInvasionWatch`'s entity-id transport with persistent UUID/type/center/size/remaining data.
- Legacy watcher semantics: a newly announced invasion can override the watched invasion; normal updates only refresh the same watched invasion; stale client watches expire after 600 ticks.
- Server refreshes watched values for hostile nearby players and explicitly clears the watch when the invasion ends.
- Restored the 1.7.10-style invasion boss bar: 182px wide, 5px high, faction-colored health/progress, centered invasion title, shifted down when vanilla boss UI is present.
- Restored the floating invasion-spawner presentation as the original rotating **iron hammer**, scale 1.5, rendered at the invasion center. The original renderer used an equipped iron-hammer ItemStack rather than a bespoke model.
- Presentation is driven by SavedData/network state, so no disposable physical spawner entity is required in 1.20.1.

## Legacy evidence
`GOTRenderInvasionSpawner` renders `GOTItems.ironHammer`, rotates it by spawner spin, and scales it to 1.5. `GOTInvasionStatus` stores the watched invasion and expires it after 600 ticks. `GOTTickHandlerClient` renders a 182x5 faction-colored invasion bar and invasion title in the boss-health HUD layer.

## Validation still required locally
Run `gradlew build`, then test natural and Warhorn starts, progress, victory/failure, Peaceful cancellation, save/reload, two simultaneous invasions, and multiplayer watcher selection. All 27 invasion types should receive at least one forced-spawn smoke test before declaring the subsystem release-ready.
