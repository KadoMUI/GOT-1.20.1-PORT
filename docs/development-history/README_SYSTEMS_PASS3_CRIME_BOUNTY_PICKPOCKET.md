# Project Thrones — Systems Pass 3: Crime / Bounties / Pickpocketing

## Restored
- Persistent faction bounty ledger backed by SavedData.
- Direct player kills of faction NPCs inside that faction's influence/control territory record bounty-eligible kills.
- Legacy kill record lifetime: 3,456,000 ticks.
- Legacy recently-claimed suppression window: 864,000 ticks.
- Server APIs for bounty target discovery and bounty claiming.
- Bounty Trophy awarded when a valid player bounty is claimed.
- Quest event hook `got:bounty_kill` fired on a valid bounty claim.
- Sneak + right-click NPC pickpocket interaction.
- Pickpocket blocked while the NPC is in combat, while directly watched, on hired NPCs, or after that player has already successfully robbed the same NPC.
- Legacy primary chance rolls: 1/3 chance to find loot; notice roll 1/3 on loot success, 1/4 otherwise.
- Nearby same-faction witnesses can notice based on distance, line-of-sight, and civilian/combatant status.
- Detected theft applies the original -1 faction alignment penalty.
- Successful pickpocket awards the legacy `pickpocket` achievement hook and fires quest event `got:pickpocket`.
- Quest parity ledger no longer marks bounty/pickpocket as blocked by a missing subsystem.

## Modern adaptation
The original pickpocket quest pulled one item from the legacy TREASURE chest-content table. Project Thrones now provides a compact coin-denomination loot pool through the same interaction hook. This keeps the gameplay loop working without reviving the obsolete 1.7.10 chest-content implementation solely for this mechanic; later content passes can replace `createLoot` with a richer datapack loot table without changing the crime API.

## Validation
Run `python migration/audit-systems-pass3-crime.py` for static source checks.
