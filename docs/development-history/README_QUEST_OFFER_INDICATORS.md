# Quest Offer Indicators — Pass 1

Restores the 1.7.10 floating quest-offer exclamation mark for 1.20.1 NPCs.

- Server-authoritative and per-player: markers only appear when that player actually has an eligible quest available from the NPC.
- Uses the recovered legacy `textures/item/quest_offer.png` icon.
- Marker is billboarded above the NPC, visible within 16 blocks, fullbright, and tinted to the NPC's faction color as in the legacy offer-color behavior.
- Marker is suppressed while the NPC is speaking, matching the old rendering priority.
- Active quests from that giver do not show the offer marker.
- Jaqen's initial tutorial offer participates even though his special tutorial flow intentionally bypasses the generic `canOfferQuests()` system.
- Indicator state is refreshed once per second with a short client TTL, avoiding stale markers when eligibility changes or an entity leaves range.
- Network protocol bumped from 17 to 18.
