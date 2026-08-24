#!/usr/bin/env python3
from pathlib import Path
import sys

root = Path(sys.argv[1] if len(sys.argv) > 1 else ".")
java = root / "src/main/java"

faction = (java / "got/faction/GOTFaction.java").read_text()
service = (java / "got/faction/GOTFactionService.java").read_text()
events = (java / "got/faction/GOTFactionEvents.java").read_text()
network = (java / "got/network/GOTNetwork.java").read_text()
screen = (java / "got/client/gui/GOTGuiFactions.java").read_text()
menu = (java / "got/client/gui/GOTGuiMenu.java").read_text()
north = (java / "got/npc/GOTNorthNpcEntity.java").read_text()
west = (java / "got/npc/GOTWesterlandsNpcEntity.java").read_text()

legacy_factions = [
    "WHITE_WALKER", "WILDLING", "NIGHT_WATCH", "NORTH", "IRONBORN",
    "WESTERLANDS", "RIVERLANDS", "HILL_TRIBES", "ARRYN", "DRAGONSTONE",
    "CROWNLANDS", "STORMLANDS", "REACH", "DORNE", "BRAAVOS", "VOLANTIS",
    "PENTOS", "NORVOS", "LORATH", "QOHOR", "MYR", "LYS", "TYROSH",
    "GHISCAR", "QARTH", "LHAZAR", "DOTHRAKI", "IBBEN", "JOGOS_NHAI",
    "MOSSOVY", "YI_TI", "ASSHAI", "SUMMER_ISLANDS", "SOTHORYOS", "ULTHOS",
    "HOSTILE", "UNALIGNED"
]
for name in legacy_factions:
    assert name in faction, f"missing faction {name}"

assert faction.count("put(map,") >= 85, "legacy relation matrix is incomplete"
for relation in [
    "put(map, NORTH, WESTERLANDS, GOTFactionRelation.ENEMY)",
    "put(map, NIGHT_WATCH, NORTH, GOTFactionRelation.ALLY)",
    "put(map, CROWNLANDS, WESTERLANDS, GOTFactionRelation.ALLY)",
]:
    assert relation in faction, f"missing relation {relation}"

assert "new GOTFactionTargetGoal(this)" in north and "new GOTFactionTargetGoal(this)" in west
assert "new GOTFactionHurtByTargetGoal(this)" in north and "new GOTFactionHurtByTargetGoal(this)" in west
assert "isActiveCombatant()" in north and "isActiveCombatant()" in west
assert "!getRole().legendary() && !getRole().activeCombatant()" in north
assert "!getRole().legendary() && !getRole().activeCombatant()" in west
assert "scalePenalty(-base" in service and "alignment / 50.0F" in service
assert "GOTFactionControl.isInInfluence" in service
assert "approvesCivilianEnemyKills" in service
assert "pledgeAlignment()" in service and "36_000" in service and "180_000" in service
assert "LivingDeathEvent" in events and "LivingAttackEvent" in events
assert "PlayerEvent.Clone" in events and "PlayerLoggedInEvent" in events
assert "S2CFactionDataPacket" in network and "C2SFactionMembershipPacket" in network
assert "FACTIONS_TEXTURE" in screen and "FACTIONS_TEXTURE_FULL" in screen
assert "GOTGuiButtonPledge" in screen and "GOTGuiFactions::new, true" in menu

for texture in ["factions.png", "factions_full.png", "alignment.png"]:
    assert (root / "src/main/resources/assets/got/textures/gui" / texture).is_file(), texture

print("Faction/alignment audit passed: 37 legacy factions, full relations, NPC hostility, kill reputation, persistent membership, networking, and GUI")
