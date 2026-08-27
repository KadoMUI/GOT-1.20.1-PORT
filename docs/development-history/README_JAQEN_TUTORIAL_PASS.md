# Project Thrones — Jaqen H'Ghar Tutorial Pass

This pass restores the legacy 1.7.10 Jaqen H'Ghar tutorial flow in the 1.20.1 port.

Implemented:
- Dedicated `got:jaqen_hghar` legendary NPC using the original legacy texture.
- Lorath faction identity, 300 alignment value, persistent legendary naming, alloy-steel sword loadout.
- Legacy arrival/departure/friendly/welcome/quest speech banks.
- Automatic legendary tutorial spawning near eligible players in Planetos, using the legacy 2400-tick retry cadence, 4–16 block radius, 32 placement attempts, and y > 62 ground checks.
- Original 15-stage `GOTMiniQuestWelcome` progression retained numerically for save/debug readability.
- Quest Book -> Map -> Alignment -> regional/faction teaching -> Factions -> final Jaqen conversations.
- Modern compatibility bridge: because the current Alignment GUI no longer has the old region-cycle keybinds, the first Factions-screen visit after Alignment fulfills the legacy regional-alignment-cycle stage; reopening Factions after Jaqen's explanation fulfills the original ViewFactions stage.
- Original reward payload: two small pouches plus one Valyrian dagger, once per player.
- Original final farewell and Jaqen departure effect.
- Network protocol bumped to 17 for the added tutorial packets.

Validation:
- Static source/resource audit passed.
- `en_us.json` parses successfully.
- Full Gradle compile could not execute in the sandbox because the wrapper requires downloading Gradle 8.8 from services.gradle.org and the sandbox has no outbound network. Run `gradlew compileJava` or `gradlew build` locally as the definitive compile check.

Useful test path:
1. Enter Planetos with a player who has never completed the Jaqen tutorial.
2. For immediate testing use `/summon got:jaqen_hghar` rather than waiting for the 2400-tick tracker cadence.
3. Right-click Jaqen, accept, then continue interacting/opening the prompted GOT interfaces.
4. Confirm Quest Book acquisition, stage gating, two pouches + Valyrian dagger, farewell, and disappearance.
