# NPC Pass 6 — Professions and Special AI

This pass audits the profession/special-role layer after natural military populations were restored.

## Fixed

Four Merchant-capable NPC engines had their own offer caches but were not driving the shared trader runtime consistently:

- Summer Isles (`summerOffers`)
- Sothoryos (`sothoryosOffers`)
- Mossovy (`offers`)
- Golden Company (`offers`)

They now run `GOTNpcTraderRuntime.tick(...)` and `GOTTraderAdvertisement.tick(...)`, bringing stock lock/decay/refresh and ambient trader advertisement behavior in line with the other regional NPC engines.

## Audited as already implemented

The pass deliberately does not replace functioning systems. The static gate now protects these already-present special behaviors:

- Asshai Shadowbinder ranged fire magic.
- Asshai Spherebinder close-control knockback behavior.
- Asshai Archmag large-fireball attack.
- Mossovy Witcher hybrid ranged/melee behavior and mercenary alignment gate.
- Ghiscari and Qohori Unsullied enhanced health/combat identity.
- Sothoryos blowgunner ranged dart behavior.
- Prostitute unaligned/civilian special handling and speech path.
- Regional profession trade pools (smiths, bakers, brewers, bartenders, farmers, fishmongers, goldsmiths, maesters, unit traders, etc.) remain handled by the existing regional offer builders.

## Scope note

The legacy profession interfaces were primarily trade/unit-trade identities; they were not a vanilla-villager-style job-site AI system. This pass therefore avoids inventing crop-harvesting or workstation AI that was not part of the original NPC behavior.

Run `python migration/audit-npc-pass6-professions-special-ai.py` for the static parity gate.
