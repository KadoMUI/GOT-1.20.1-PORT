#!/usr/bin/env python3
"""Audit Catch-up 4 item registration, assets, recipes, and texture paths."""

from __future__ import annotations

import json
import re
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
RESOURCES = ROOT / "src/main/resources"
JAVA = ROOT / "src/main/java/got/GOTItems.java"
ITEMS = {
    "beaver_tail", "blood_of_true_kings", "bottle_poison", "bounty_trophy",
    "clay_plate", "copper_nugget", "leather_hat", "mystery_web", "salted_flesh",
}
REQUIRED_RECIPES = {
    "bottle_poison", "clay_plate", "ceramic_plate_from_smelting", "copper_nugget",
    "copper_ingot_from_nuggets", "leather_hat", "salted_flesh",
    "alloy_steel_dagger_poisoned", "bronze_dagger_poisoned",
    "copper_dagger_poisoned", "gold_dagger_poisoned", "iron_dagger_poisoned",
    "obsidian_dagger_poisoned", "stone_dagger_poisoned",
    "valyrian_dagger_poisoned", "wood_dagger_poisoned", "dart_poisoned",
    "arrow_poisoned", "crossbow_bolt_poisoned",
}


def load(path: Path) -> object:
    return json.loads(path.read_text(encoding="utf-8"))


def main() -> None:
    errors: list[str] = []
    java = JAVA.read_text(encoding="utf-8")
    lang = load(RESOURCES / "assets/got/lang/en_us.json")

    registered = set(re.findall(r'(?:simple|food)\("([a-z0-9_]+)"', java))
    registered.update(re.findall(r'ITEMS\.register\("([a-z0-9_]+)"', java))

    for item_id in sorted(ITEMS):
        if item_id not in registered:
            errors.append(f"unregistered item: {item_id}")
        model = RESOURCES / f"assets/got/models/item/{item_id}.json"
        texture = RESOURCES / f"assets/got/textures/item/{item_id}.png"
        if not model.is_file():
            errors.append(f"missing model: {item_id}")
        if not texture.is_file():
            errors.append(f"missing texture: {item_id}")
        if f"item.got.{item_id}" not in lang:
            errors.append(f"missing translation: {item_id}")
        if model.is_file():
            layer = load(model).get("textures", {}).get("layer0")
            if layer != f"got:item/{item_id}":
                errors.append(f"invalid model texture for {item_id}: {layer}")

    for recipe in sorted(REQUIRED_RECIPES):
        path = RESOURCES / f"data/got/recipes/{recipe}.json"
        if not path.is_file():
            errors.append(f"missing recipe: {recipe}")
        else:
            load(path)

    for name in ("dirt_path_slab", "dirt_path_slab_top"):
        path = RESOURCES / f"assets/got/models/block/{name}.json"
        model = load(path)
        textures = model.get("textures", {})
        expected = {
            "bottom": "minecraft:block/dirt",
            "side": "minecraft:block/dirt_path_side",
            "top": "minecraft:block/dirt_path_top",
        }
        if textures != expected:
            errors.append(f"invalid Dirt Path Slab textures in {name}: {textures}")

    for path in [
        RESOURCES / "assets/got/textures/models/armor/hat_layer_1.png",
        RESOURCES / "assets/got/textures/models/armor/hat_layer_1_overlay.png",
        RESOURCES / "data/forge/tags/items/nuggets/copper.json",
    ]:
        if not path.is_file():
            errors.append(f"missing supporting resource: {path.relative_to(ROOT)}")

    if errors:
        raise SystemExit("\n".join(errors))
    print(f"Catch-up 4 audit passed: {len(ITEMS)} items, "
          f"{len(REQUIRED_RECIPES)} recipes, Dirt Path Slab fixed")


if __name__ == "__main__":
    main()
