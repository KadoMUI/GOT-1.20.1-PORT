# NPC Pass 3 - Legacy Cavalry

This pass restores the ordinary mounted-cavalry roll used by the final 1.7.10 mod for non-Dothraki factions.

## Natural/invasion cavalry

The following professional melee soldier + banner-bearer roles now receive the original 1-in-10 (10%) mounted roll:
North, Arryn, Riverlands, Westerlands, Reach, Stormlands, Dorne, Dragonstone, Ironborn, Braavos, Pentos, Myr, Tyrosh, Lys, Lorath, Norvos, Qohor, Volantis, Qarth, Ghiscar, and Yi Ti.

Their mount is a GOT Horse with iron horse armor.

Ranged soldier subclasses are intentionally excluded, matching the legacy classes which explicitly disabled their inherited horse roll.

## Jogos Nhai

Jogos Nhai warriors and archers both receive the legacy 10% mounted roll and ride GOT Zebras without horse armor.

## Spawn-context parity

The cavalry roll is made once and persisted on the NPC. Reloading a world therefore cannot reroll a foot soldier into cavalry.

Fixed/persistent structure and respawner NPCs are kept on foot, preserving the legacy pattern where structures explicitly suppressed riding. Invasion mobs remain eligible for the legacy constructor cavalry roll.

Creative-spawner/persistent test NPCs are kept on foot unless an explicit mounted creation path is used.

## Dothraki

No Dothraki behavior was changed. Project Thrones keeps the intentional 85% mounted warrior/archer and 100% mounted chieftain rules from NPC Passes 1-2 rather than restoring the legacy 10% ratio.
