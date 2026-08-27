# Invasions Pass 2 — Natural Events + Warhorn

Pass 2 restores the two trigger layers intentionally deferred by Pass 1.

## Natural biome invasions
- Recovered all **64 original biome→invasion registrations across 28 legacy biome constructors** from the 24.08.29 jar.
- Restored the original event tiers: **RARE = 10%/hour**, **UNCOMMON = 30%/hour**, **COMMON = 90%/hour**, converted to the original per-second probability.
- Modern sub-biomes inherit the invasion table of their recovered legacy parent family.
- Natural invasions only trigger when a living non-spectator player within 48 blocks has **negative alignment** with the invading faction, matching the old selector.
- Natural spawn placement retains the old surface/height intent and prevents duplicate same-faction invasions from stacking locally.

## Warhorn
- Replaced the placeholder warhorn with a functional faction-bound invasion horn.
- Horns store their invasion type in NBT (`InvasionType`), preserving the old item design.
- Requires **500 alignment** with the horn faction, as in 1.7.10.
- Uses the old 40-tick bow/use animation and consumes the horn on successful use outside creative mode.
- Starts a persistent invasion above the user and respects hostile banner protection.
- Restored the original per-invasion horn names and faction-color overlay tint.

Pass 1's invasion runtime, weighted NPC compositions, persistence, kill tracking, achievement hook, and `/invasion` command remain unchanged.
