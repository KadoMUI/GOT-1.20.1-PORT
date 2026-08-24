#!/usr/bin/env python3
"""Regression audit for the reusable 1.20.1 quest checkpoint."""

from __future__ import annotations

import json
import re
import struct
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "src/main/java"
RES = ROOT / "src/main/resources"


def require(condition: bool, message: str) -> None:
    if not condition:
        raise AssertionError(message)


def text(path: str) -> str:
    return (ROOT / path).read_text(encoding="utf-8-sig")


def png_size(path: Path) -> tuple[int, int]:
    data = path.read_bytes()
    require(data[:8] == b"\x89PNG\r\n\x1a\n", f"Not a PNG: {path}")
    return struct.unpack(">II", data[16:24])


core = {
    "src/main/java/got/quest/GOTQuestDefinition.java",
    "src/main/java/got/quest/GOTQuestDefinitionManager.java",
    "src/main/java/got/quest/GOTQuestInstance.java",
    "src/main/java/got/quest/GOTQuestPlayerData.java",
    "src/main/java/got/quest/GOTQuestService.java",
    "src/main/java/got/quest/GOTQuestEvents.java",
    "src/main/java/got/quest/GOTQuestGiver.java",
    "src/main/java/got/client/gui/GOTGuiQuestOffer.java",
    "src/main/java/got/client/gui/GOTGuiQuestBook.java",
    "src/main/java/got/client/quest/GOTClientQuestState.java",
    "src/main/java/got/quest/GOTQuestBookItem.java",
}
for name in core:
    require((ROOT / name).is_file(), f"Missing quest source {name}")
require((ROOT / "QUEST_DATA_FORMAT.md").is_file(), "Missing reusable quest data documentation")

service = text("src/main/java/got/quest/GOTQuestService.java")
for hook in ("triggerEvent", "onKill", "onPlayerTick", "onQuestGiverDeath", "acceptOffer", "abandon"):
    require(hook in service, f"Missing quest lifecycle hook {hook}")
for objective in ("COLLECT", "KILL_ENTITY", "KILL_FACTION", "TALK_TO_NPC", "VISIT_LOCATION", "EVENT"):
    require(objective in service or objective in text("src/main/java/got/quest/GOTQuestObjectiveType.java"),
            f"Missing objective family {objective}")
require("if (!objective.consume())" in service,
        "Non-consuming collect objectives can regress into repeat-credit exploits")

player_data = text("src/main/java/got/quest/GOTQuestPlayerData.java")
require("Player.PERSISTED_NBT_TAG" in player_data, "Quest journal is not death-persistent")
require("copyPersisted" in player_data, "Quest clone persistence is missing")

network = text("src/main/java/got/network/GOTNetwork.java")
for packet in ("S2CQuestDataPacket", "S2CQuestOfferPacket", "C2SRequestQuestDataPacket", "C2SQuestActionPacket"):
    require(packet in network, f"Quest packet not registered: {packet}")

for entity in ("GOTNorthNpcEntity.java", "GOTWesterlandsNpcEntity.java",
               "GOTRiverlandsNpcEntity.java", "GOTArrynNpcEntity.java",
               "GOTCrownlandsNpcEntity.java", "GOTDragonstoneNpcEntity.java",
               "GOTReachNpcEntity.java", "GOTStormlandsNpcEntity.java",
               "GOTDorneNpcEntity.java", "GOTIronbornNpcEntity.java",
               "GOTWildlingNpcEntity.java", "GOTNightWatchNpcEntity.java",
               "GOTBraavosNpcEntity.java", "GOTPentosNpcEntity.java",
               "GOTVolantisNpcEntity.java", "GOTLysNpcEntity.java",
               "GOTMyrNpcEntity.java", "GOTTyroshNpcEntity.java",
               "GOTGhiscarNpcEntity.java", "GOTDothrakiNpcEntity.java",
               "GOTYiTiNpcEntity.java", "GOTAsshaiNpcEntity.java",
               "GOTIbbenNpcEntity.java", "GOTJogosNhaiNpcEntity.java",
               "GOTQarthNpcEntity.java", "GOTLorathNpcEntity.java",
               "GOTQohorNpcEntity.java", "GOTLhazarNpcEntity.java",
               "GOTNorvosNpcEntity.java", "GOTMossovyNpcEntity.java",
               "GOTSummerIslesNpcEntity.java", "GOTSothoryosNpcEntity.java"):
    source = text(f"src/main/java/got/npc/{entity}")
    require(("GOTQuestGiver" in source or "extends GOTNorvosNpcEntity" in source)
            and "getQuestRoleId" in source,
            f"Regional NPC is not quest-capable: {entity}")

roles: set[str] = set()
for enum_name in ("NorthNpcRole.java", "WesterlandsNpcRole.java",
                  "RiverlandsNpcRole.java", "ArrynNpcRole.java",
                  "CrownlandsNpcRole.java", "DragonstoneNpcRole.java",
                  "ReachNpcRole.java", "StormlandsNpcRole.java",
                  "DorneNpcRole.java", "IronbornNpcRole.java",
                  "WildlingNpcRole.java", "NightWatchNpcRole.java",
                  "BraavosNpcRole.java", "PentosNpcRole.java",
                  "VolantisNpcRole.java", "LysNpcRole.java",
                  "MyrNpcRole.java", "TyroshNpcRole.java",
                  "GhiscarNpcRole.java", "DothrakiNpcRole.java",
                  "YiTiNpcRole.java", "AsshaiNpcRole.java",
                  "IbbenNpcRole.java", "JogosNhaiNpcRole.java",
                  "QarthNpcRole.java", "LorathNpcRole.java",
                  "QohorNpcRole.java", "LhazarNpcRole.java",
                  "NorvosNpcRole.java", "MossovyNpcRole.java",
                  "SummerIslesNpcRole.java", "SothoryosNpcRole.java"):
    source = text(f"src/main/java/got/npc/{enum_name}")
    roles.update(re.findall(r'^[ ]{4}[A-Z0-9_]+\("([a-z0-9_]+)"', source, flags=re.MULTILINE))

definitions = sorted((RES / "data/got/quests").glob("*.json"))
require(len(definitions) >= 66, "Expected regional quest definitions through the Summer Isles and Sothoryos")
lang = json.loads((RES / "assets/got/lang/en_us.json").read_text(encoding="utf-8-sig"))
seen_ids: set[str] = set()
objective_types: set[str] = set()
for path in definitions:
    data = json.loads(path.read_text(encoding="utf-8"))
    quest_id = f"got:{path.stem}"
    require(quest_id not in seen_ids, f"Duplicate quest id {quest_id}")
    seen_ids.add(quest_id)
    require(data.get("objectives"), f"Quest has no objectives: {path.name}")
    for key in ("title", "description", "offer", "progress", "complete"):
        require(data[key] in lang, f"Missing English quest translation {data[key]}")
    for role in data.get("giver", {}).get("roles", []):
        require(role in roles, f"Unknown quest giver role {role} in {path.name}")
    for objective in data["objectives"]:
        objective_types.add(objective["type"])
        require(objective["label"] in lang, f"Missing objective label {objective['label']}")

require("kill_entity" in objective_types and "kill_faction" in objective_types and "collect" in objective_types,
        "Initial content does not exercise the legacy objective families")
daven = json.loads((RES / "data/got/quests/daven_vengeance.json").read_text())
require(daven["giver"]["roles"] == ["daven_lannister"], "Daven quest giver drifted")
require(daven["objectives"][0]["role"] == "rickard_karstark", "Daven target drifted")

assets = {
    RES / "assets/got/textures/gui/quest/miniquest.png": (256, 256),
    RES / "assets/got/textures/gui/quest/questBook.png": (512, 512),
    RES / "assets/got/textures/gui/quest/tracker.png": (256, 256),
    RES / "assets/got/textures/item/quest_book.png": (16, 16),
    RES / "assets/got/textures/item/quest_offer.png": (32, 32),
}
for path, expected in assets.items():
    require(path.is_file(), f"Missing legacy quest asset {path}")
    require(png_size(path) == expected, f"Unexpected quest asset dimensions: {path}")

recipe = json.loads((RES / "data/got/recipes/quest_book.json").read_text())
require(recipe["result"]["item"] == "got:quest_book", "Quest Book recipe output drifted")
require((RES / "assets/got/models/item/quest_book.json").is_file(), "Quest Book model missing")

print(f"Quest audit passed: {len(definitions)} definitions, {len(core)} core sources, {len(assets)} legacy assets")
