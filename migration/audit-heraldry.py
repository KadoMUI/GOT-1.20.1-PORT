#!/usr/bin/env python3
"""Strict source/resource audit for the 1.20.1 Heraldry catch-up."""

from __future__ import annotations

import csv
import json
import re
import sys
from pathlib import Path

from PIL import Image


project = Path(__file__).resolve().parents[1]
catalogue = project / "migration/heraldry-catalog.csv"
java_root = project / "src/main/java/got"
assets = project / "src/main/resources/assets/got"
data = project / "src/main/resources/data/got"
errors: list[str] = []


with catalogue.open(newline="", encoding="utf-8") as handle:
    rows = list(csv.DictReader(handle))

if len(rows) != 605:
    errors.append(f"catalogue contains {len(rows)} rows, expected 605")

ids = [int(row["legacy_id"]) for row in rows]
names = [row["name"] for row in rows]
if len(ids) != len(set(ids)):
    errors.append("duplicate legacy banner IDs")
if len(names) != len(set(names)):
    errors.append("duplicate banner names")
if min(ids, default=-1) != 0 or max(ids, default=-1) != 617:
    errors.append("legacy ID range is not 0..617 with historical gaps")

generated = (java_root / "GOTBannerType.java").read_text(encoding="utf-8")
generated_rows = re.findall(
    r'^\s*add\(types, (\d+), "([^"]+)", "([A-Z0-9_]+)"\);$', generated, re.MULTILINE
)
if len(generated_rows) != len(rows):
    errors.append(f"generated Java contains {len(generated_rows)} rows, expected {len(rows)}")
else:
    expected = [(row["legacy_id"], row["name"], row["faction"]) for row in rows]
    if generated_rows != expected:
        errors.append("generated Java does not preserve catalogue ordering/data")

english = json.loads((assets / "lang/en_us.json").read_text(encoding="utf-8"))
for row in rows:
    name = row["name"]
    legacy = assets / "textures/banner" / f"{name}.png"
    modern = assets / "textures/entity/banner" / f"{name.lower()}.png"
    for texture in (legacy, modern):
        if not texture.is_file():
            errors.append(f"missing texture {texture.relative_to(project)}")
            continue
        try:
            with Image.open(texture) as image:
                image.verify()
        except Exception as exc:
            errors.append(f"invalid PNG {texture.relative_to(project)}: {exc}")
    key = f"item.got:banner.{name}.name"
    if key not in english:
        errors.append(f"missing translation {key}")

for required in (
    assets / "textures/entity/banner/stand.png",
    assets / "textures/item/icon_heraldry.png",
    assets / "models/item/banner.json",
    assets / "models/item/icon_heraldry.json",
    data / "recipes/banner_copy.json",
):
    if not required.is_file():
        errors.append(f"missing resource {required.relative_to(project)}")

for path in list((project / "src/main/resources").rglob("*.json")):
    try:
        json.loads(path.read_text(encoding="utf-8"))
    except Exception as exc:
        errors.append(f"invalid JSON {path.relative_to(project)}: {exc}")

required_tokens = {
    "GOTMod.java": ["GOTEntities.register(modBus)", "GOTRecipes.register(modBus)"],
    "GOTItems.java": ['ITEMS.register("banner"', 'simple("icon_heraldry")'],
    "GOTCreativeTabs.java": ["GOTBannerType.values()", "GOTBannerItem.createStack(type)"],
    "GOTClientEvents.java": [
        "GOTBannerGeometry::standing", "GOTBannerGeometry::wall",
        "GOTStandingBannerRenderer::new", "GOTWallBannerRenderer::new",
    ],
    "GOTBannerItem.java": ["placeStanding", "placeWall", "GOTBannerData"],
    "GOTWallBannerEntity.java": [
        "BANNER_THICKNESS = 1.0D / 16.0D", "WALL_GAP = 0.001D",
        "onSyncedDataUpdated", "refreshWallBoundingBox()", "setBoundingBox(new AABB(",
    ],
    "GOTAbstractBannerEntity.java": ["BannerType", "Owner", "GOTBannerItem.PROTECTION_TAG"],
    "GOTBannerCopyRecipe.java": ["blanks + 1", "setProtectionData(result, null)"],
}
for filename, tokens in required_tokens.items():
    path = java_root / filename
    if not path.is_file():
        errors.append(f"missing source {filename}")
        continue
    text = path.read_text(encoding="utf-8")
    for token in tokens:
        if token not in text:
            errors.append(f"{filename} missing integration token {token!r}")

modern_names = [path.name for path in (assets / "textures/entity/banner").glob("*.png")]
if any(name != name.lower() for name in modern_names):
    errors.append("modern banner texture tree contains uppercase resource paths")
if len(modern_names) != 606:
    errors.append(f"modern banner texture tree contains {len(modern_names)} PNGs, expected 606")

if errors:
    print("Heraldry audit failed:")
    for error in errors:
        print(f"- {error}")
    sys.exit(1)

print("Heraldry audit passed")
print(f"- banner variants: {len(rows)}")
print(f"- stable legacy ID range: {min(ids)}..{max(ids)} ({618 - len(ids)} historical gaps)")
print(f"- design textures: {len(rows)} legacy + {len(rows)} modernized")
print("- placement forms: freestanding + wall-mounted")
print("- wall collision box: direction-aware 1 x 2 x 1/16")
print("- original geometry: world + held/inventory")
print("- protection-data copy/clear recipe: wired")
