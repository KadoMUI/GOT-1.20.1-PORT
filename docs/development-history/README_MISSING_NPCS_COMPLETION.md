# Missing NPC completion pass

The six unresolved hiring-role entries from the parity audit are now real
modern roles:

- Hillman Warrior
- Hillman Archer
- Hillman Axe Thrower
- Hillman Banner Bearer
- Hillman Berserker
- Prostitute

The five Vale Hillman roles belong to `HILL_TRIBES`, not `ARRYN`. They reuse the
existing humanoid Arryn entity implementation but dynamically report the correct
faction, keeping the faction/alignment system accurate without duplicating an
entire regional NPC engine.

Legacy combat details restored include longbow archers, thrown-axe AI, Hillmen
armor, the Hill Tribes banner, and the Berserker's 40 max health / iron
battleaxe.

The Prostitute role is female, passive, unaffiliated, and carries the original
`PROSTITUTE` hired task. No settlement spawning was invented for it; authored
markers can use `prostitute`, and the hiring catalog can resolve it.

The six exact legacy hiring definitions from the previous unresolved ledger are
now installed under `data/got/hiring`, bringing that unresolved-role ledger to
zero.
