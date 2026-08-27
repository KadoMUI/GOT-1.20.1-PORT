# Special Mobs Pass 4 — White Walker Wights

Restores the legacy White-Walker Wight combat/conversion behavior on top of the existing modern White Walker roster.

## Wights
- Uses the existing `WhiteWalkerNpcRole.WIGHT` and legacy male/female/child Wight texture pool.
- Legacy Wight health restored to 1 HP behind the White-Walker damage gate.
- Ordinary damage is rejected; fire and Valyrian/dragonglass/obsidian-named weapons can damage Wights/Walkers/Night King.
- Successful melee hits apply the shared frost slowdown effect.
- Existing natural Haunted Forest Wight spawning and scavenged iron-dagger/fur loadout remain intact.

## Corpse conversion
`GOTWightConversionEvents` replaces legacy `IceUtils.createNewWight`:
- Human GOT faction NPC killed by a Wight, White Walker, or Wight Giant -> ordinary Wight.
- Human GOT faction NPC personally killed by the Night King -> White Walker (legacy exception).
- Living GOT Giant killed by the White-Walker side -> Wight Giant.
- Non-human faction mobs such as Blizzard and Ulthos Spider are explicitly excluded from the human path.
- Converted Wights rise at the corpse location and inherit carried/equipped items with zero equipment drop chance.

White Walker entity implementation itself is not redesigned in this pass; this pass completes the Wight side and shared conversion behavior.
