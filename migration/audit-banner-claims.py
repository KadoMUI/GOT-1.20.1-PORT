#!/usr/bin/env python3
"""Regression audit for the complete Forge 1.20.1 banner-claim checkpoint."""

from __future__ import annotations

import hashlib
import json
import re
import struct
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
RES = ROOT / "src/main/resources"


def require(condition: bool, message: str) -> None:
    if not condition:
        raise AssertionError(message)


def text(path: str) -> str:
    return (ROOT / path).read_text(encoding="utf-8-sig")


core = {
    "src/main/java/got/claim/GOTBannerClaim.java",
    "src/main/java/got/claim/GOTBannerPermission.java",
    "src/main/java/got/claim/GOTBannerWhitelistEntry.java",
    "src/main/java/got/claim/GOTBannerProtection.java",
    "src/main/java/got/claim/GOTBannerProtectionEvents.java",
    "src/main/java/got/claim/GOTBannerClaimService.java",
    "src/main/java/got/claim/GOTBannerClaimSnapshot.java",
    "src/main/java/got/claim/GOTClaimGroupResolver.java",
    "src/main/java/got/client/gui/GOTGuiBannerClaim.java",
    "src/main/java/got/network/C2SBannerClaimActionPacket.java",
    "src/main/java/got/network/S2CBannerClaimDataPacket.java",
}
for name in core:
    require((ROOT / name).is_file(), f"Missing banner-claim source: {name}")

claim = text("src/main/java/got/claim/GOTBannerClaim.java")
for constant in ("MIN_ALIGNMENT = 1.0F", "MAX_ALIGNMENT = 10_000.0F",
                 "DEFAULT_LIST_SIZE = 16", "MAX_ENTRIES = 4_000", "MAX_RANGE = 64"):
    require(constant in claim, f"Claim limit drifted: {constant}")
for legacy_key in ("PlayerProtection", "StructureProtection", "SelfProtection",
                   "AlignProtectF", "AllowedPlayers", "DefaultPerms", "WhitelistLength"):
    require(legacy_key in claim, f"Legacy NBT compatibility key missing: {legacy_key}")

permissions = text("src/main/java/got/claim/GOTBannerPermission.java")
expected_permissions = {
    "FULL", "DOORS", "TABLES", "CONTAINERS", "PERSONAL_CONTAINERS",
    "FOOD", "BEDS", "SWITCHES",
}
enum_body = re.search(r"enum GOTBannerPermission\s*\{(.*?)\;", permissions, re.S)
require(enum_body is not None, "Cannot read permission enum")
actual_permissions = set(re.findall(r"\b[A-Z][A-Z_]+\b", enum_body.group(1)))
require(actual_permissions == expected_permissions,
        f"Permission set drifted: {sorted(actual_permissions)}")

protection = text("src/main/java/got/claim/GOTBannerProtection.java")
for tier in ("BRONZE_RANGE = 8", "SILVER_RANGE = 16", "GOLD_RANGE = 32",
             "VALYRIAN_RANGE = 64"):
    require(tier in protection, f"Support tier drifted: {tier}")
require("GOTFactionPlayerData.get(player).alignment" in protection,
        "Faction/alignment authorization is disconnected")
require("GOTClaimGroupResolver.isMember" in protection,
        "Pacts/group integration seam is disconnected")

events = text("src/main/java/got/claim/GOTBannerProtectionEvents.java")
for event_hook in ("BlockEvent.BreakEvent", "BlockEvent.EntityPlaceEvent",
                   "PlayerInteractEvent.RightClickBlock", "FillBucketEvent",
                   "PistonEvent.Pre", "ExplosionEvent.Detonate",
                   "ProjectileImpactEvent", "MobSpawnEvent.FinalizeSpawn"):
    require(event_hook in events, f"Protection event hook missing: {event_hook}")

entity = text("src/main/java/got/GOTAbstractBannerEntity.java")
for behavior in ("getClaimRange", "isClaimActive", "canPlayerEditClaim",
                 "createDropStack", "GOTBannerClaimService.open"):
    require(behavior in entity, f"Banner entity behavior missing: {behavior}")
item = text("src/main/java/got/GOTBannerItem.java")
require("canCreateClaim" in item and "alignment < GOTBannerClaim.MIN_ALIGNMENT" in item,
        "Placement authorization is incomplete")
require("shouldKeepOriginalOwner" not in item or "isShiftKeyDown" in item,
        "Creative owner-retention behavior is missing")

network = text("src/main/java/got/network/GOTNetwork.java")
for packet in ("S2CBannerClaimDataPacket", "C2SBannerClaimActionPacket"):
    require(packet in network, f"Banner claim packet not registered: {packet}")

commands = text("src/main/java/got/GOTCommands.java")
for command in ('literal("claim")', 'literal("setrange")', 'literal("structure")',
                'literal("transfer")'):
    require(command in commands, f"Claim command missing: {command}")

texture = RES / "assets/got/textures/gui/banner_edit.png"
data = texture.read_bytes()
require(data[:8] == b"\x89PNG\r\n\x1a\n", "banner_edit.png is not a PNG")
require(struct.unpack(">II", data[16:24]) == (256, 256),
        "banner_edit.png dimensions drifted")
require(hashlib.sha256(data).hexdigest()
        == "6cfcf5d394c0992f25da6b1cf794d6c8458df226b9dcc2910dda699971857140",
        "banner_edit.png no longer matches the recovered legacy asset")

lang = json.loads((RES / "assets/got/lang/en_us.json").read_text(encoding="utf-8-sig"))
for permission in expected_permissions:
    require(f"got.gui.bannerEdit.perm.{permission}" in lang,
            f"Missing permission translation: {permission}")
for key in ("got.gui.bannerEdit.title", "got.chat.protectedLand",
            "got.command.claim.info", "got.command.claim.transfer"):
    require(key in lang, f"Missing banner-claim translation: {key}")

print(f"Banner claim audit passed: {len(core)} core sources, 4 support tiers, "
      f"{len(expected_permissions)} permissions, legacy GUI, persistence, networking, and enforcement")
