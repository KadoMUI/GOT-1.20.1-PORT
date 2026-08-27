# Project Thrones 1.20.1 — Final Functional Sweep

Scope: current post-Pacts source. Structures, Dragons, and Conquest are explicitly excluded.

## Result

No broad missing-system or registry failure was found in the included scope. The port is in a state where further work should be driven primarily by runtime playtesting and future design changes, not by another large legacy-content restoration pass.

## Static integrity checks

- 9,267 JSON resources parsed successfully after cleanup.
- 556 directly registered item IDs were checked; every one has an item model and English display-name coverage.
- 88 registered entity types were checked; every one has a client renderer and attribute registration where applicable.
- GOT datapack item/block references resolve to modeled registry IDs.
- Custom recipe types in the data pack (`banner_copy`, `pouch_combine`) have matching serializers.
- No duplicate direct registry IDs were found in the principal item/entity/menu/block-entity registries.
- Nine registered menus have corresponding client screen registrations where a custom screen is required; storage deliberately uses the vanilla chest screen.
- Pact waypoint sharing, Pact fast-travel knowledge, and Pact claim access are wired into their server-authoritative services and persistence paths.
- White Walker Wight conversion is event-driven and does not depend on structure generation.

## Cleanup performed in this sweep

- Added missing English UI strings for Oven, Fermentation Barrel, generic Storage, Iron Bank feedback, Unsmeltery feedback, poison/drink tooltips, Ulthos Spider alignment feedback, Ulthos creature-spawner text, and old menu labels.
- Updated stale quest audit entries: legendary quests and Jaqen tutorial are no longer marked deferred.
- Updated stale speech audit entries: Jaqen and miniquest lifecycle speech are no longer marked deferred.
- Removed an obsolete hired-inventory comment claiming menu types were placeholders; the registered farmer/warrior menus are already real.

## Systems considered functionally present

- Planetos dimension/world framework and biome registry (structure generation excluded from this sweep).
- Factions, alignment, relations, faction membership, ranks/titles.
- Quest journal, quest offering, HUD tracking, objective progress, rewards, legendary quests, Jaqen tutorial, quest-offer indicators.
- NPC regional role system, hiring, commands, squadrons, hired inventories, mounted NPC control.
- Trading/economy, coins, coin exchange, Iron Bank consolidation, regional/legendary traders.
- Smithing modifiers/scrolls, reforging, naming/engraving, functional machines.
- Pacts: invitations/membership/admin/ownership, friendly-fire rules, map sharing, custom-waypoint sharing, shared fast-travel knowledge, shared claim access.
- Banner claims and permissions.
- Fast travel including custom and Pact-shared waypoints, cooldown/warmup, entourage travel, and region discovery.
- Achievements and Lore systems.
- Calendar/time system.
- Drinks, drunkenness player effects, fermentation, vessels.
- Shields as functional blocking shields with custom faction rendering.
- Mount system and current mount equipment behavior.
- Wildlife, fantasy wildlife, Stone Men, Werewolves, Giants/Wight Giants, ghost Wraiths, White Walker Wights/conversion.
- Bookshelf/chest/basket storage via the modern shared 27-slot storage implementation.

## Deliberate non-blocking deviations / future-polish candidates

These do not prevent 1.0 functionality and should not be treated as blockers:

- Conquest is intentionally excluded and scheduled for replacement.
- Dragons are intentionally excluded and scheduled for Ice and Fire integration.
- Structures are outside this sweep.
- Wyverns and Termites were intentionally skipped.
- Iron Bank uses a simplified direct interaction instead of recreating the legacy dedicated GUI.
- Bookshelf storage uses a standard 27-slot chest-style menu instead of the old bespoke GUI.
- Custom Pact waypoints are creator-owned for rename/delete while visible/travelable by the Pact.
- Drunken NPC speech distortion remains a legacy-fidelity omission; player drunkenness itself works.
- Old audit/helper ledgers may contain other historical wording that does not drive runtime behavior.

## Recommended 1.0 smoke test

Before tagging a release candidate, run one multiplayer test world and verify: new-world Planetos entry; map and fixed/custom fast travel; two-player Pact create/join/leave and shared markers/claims; faction join/alignment; quest offer/accept/track/complete; Jaqen tutorial; hire/follow/halt/inventory/mounted hire; trader purchase and coin exchange; smith scroll application/reforge; Oven/Alloy Forge/Millstone/Fermentation; shield blocking in first/third person; one example of each wildlife family; Stone Man/Werewolf/Giant/Wraith; White Walker kills a human NPC and it rises as a Wight; save/reload and reconnect to confirm persistence.

## 0.9.9 buildfix
- Added correct 1.20.1 RangedAttackMob import to GOTGiantBaseEntity.
- Added OverlayTexture import to GOTMarshWraithBallRenderer.
