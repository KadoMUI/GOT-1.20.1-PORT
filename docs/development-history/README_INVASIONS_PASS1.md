# Invasions Pass 1 — Core Runtime

Restores the 1.7.10 invasion runtime around the modern NPC system.

- **27 original invasion types** are registered.
- Original weighted compositions are restored from `GOTInvasions.preInit()`.
- Default invasion size is the original **30–70**.
- A maximum of **16 invasion NPCs** remain active around the spawner center.
- While a player is within **80 blocks**, the system rolls once per tick at the original **1/160** spawn cadence and attempts **1–6** NPCs per successful wave.
- Spawn placement makes up to **40 attempts** within approximately six blocks of the invasion center.
- **16 consecutive failed waves** abort the invasion, matching the old fail-safe.
- Spawned NPCs carry a persistent invasion UUID and are tracked across saves/reloads.
- Player kills contribute to invasion completion and the original **DEFEAT_INVASION / Blitzkrieg** achievement.
- Operator command restored: `/invasion <type> [x y z] [size]`.

This pass restores the core spawner/runtime/command architecture. The original automatic **biome event invasion tables / warhorn trigger layer** should be handled in the next invasion pass, because those depend on reconstructing the old biome-specific EventChance registrations rather than inventing frequencies.
