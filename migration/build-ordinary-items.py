#!/usr/bin/env python3
"""Generate Catch-up 4 ordinary-item resources and the Dirt Path Slab fix."""

from __future__ import annotations

import json
import shutil
from pathlib import Path

from PIL import Image


ROOT = Path(__file__).resolve().parents[1]
RESOURCES = ROOT / "src/main/resources"
ASSETS = RESOURCES / "assets/got"
DATA = RESOURCES / "data/got"

ITEMS = {
    "beaver_tail": "Beaver Tail",
    "blood_of_true_kings": "Blood of True Kings",
    "bottle_poison": "Bottle of Poison",
    "bounty_trophy": "Headhunter's Trophy",
    "clay_plate": "Clay Plate",
    "copper_nugget": "Copper Nugget",
    "leather_hat": "Leather Hat",
    "mystery_web": "Mystery Web",
    "salted_flesh": "Suspicious Meat",
}


def write_json(path: Path, value: object) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(value, indent=2) + "\n", encoding="utf-8")


def ingredient(item: str) -> dict[str, str]:
    return {"item": item}


def shapeless(result: str, ingredients: list[str], count: int = 1) -> dict:
    output: dict[str, object] = {"item": result}
    if count != 1:
        output["count"] = count
    return {
        "type": "minecraft:crafting_shapeless",
        "category": "misc",
        "ingredients": [ingredient(item) for item in ingredients],
        "result": output,
    }


def shaped(result: str, pattern: list[str], keys: dict[str, str], count: int = 1,
           category: str = "misc") -> dict:
    output: dict[str, object] = {"item": result}
    if count != 1:
        output["count"] = count
    return {
        "type": "minecraft:crafting_shaped",
        "category": category,
        "pattern": pattern,
        "key": {key: ingredient(item) for key, item in keys.items()},
        "result": output,
    }


def build_item_assets() -> None:
    legacy = ASSETS / "textures/items"
    modern = ASSETS / "textures/item"
    models = ASSETS / "models/item"
    modern.mkdir(parents=True, exist_ok=True)
    models.mkdir(parents=True, exist_ok=True)

    for item_id in ITEMS:
        source = legacy / f"{item_id}.png"
        if not source.is_file():
            raise FileNotFoundError(source)
        shutil.copy2(source, modern / source.name)
        write_json(models / f"{item_id}.json", {
            "parent": "minecraft:item/generated",
            "textures": {"layer0": f"got:item/{item_id}"},
        })

    # The armor material name "got:hat" resolves to these vanilla armor-layer
    # locations. A transparent overlay lets DyeableArmorItem tint the original
    # legacy base texture without requesting a nonexistent sprite at runtime.
    armor = ASSETS / "textures/models/armor"
    armor.mkdir(parents=True, exist_ok=True)
    shutil.copy2(ASSETS / "textures/armor/hat.png", armor / "hat_layer_1.png")
    Image.new("RGBA", (64, 32), (0, 0, 0, 0)).save(armor / "hat_layer_1_overlay.png")


def build_recipes() -> None:
    recipes = DATA / "recipes"
    write_json(recipes / "bottle_poison.json",
               shapeless("got:bottle_poison", ["minecraft:glass_bottle", "got:wildberry"]))
    write_json(recipes / "clay_plate.json",
               shaped("got:clay_plate", ["XX"], {"X": "minecraft:clay_ball"}, 2))
    write_json(recipes / "ceramic_plate_from_smelting.json", {
        "type": "minecraft:smelting",
        "category": "misc",
        "cookingtime": 200,
        "experience": 0.3,
        "ingredient": ingredient("got:clay_plate"),
        "result": "got:ceramic_plate",
    })
    write_json(recipes / "copper_nugget.json",
               shapeless("got:copper_nugget", ["minecraft:copper_ingot"], 9))
    write_json(recipes / "copper_ingot_from_nuggets.json",
               shaped("minecraft:copper_ingot", ["XXX", "XXX", "XXX"],
                      {"X": "got:copper_nugget"}))
    write_json(recipes / "leather_hat.json",
               shaped("got:leather_hat", [" X ", "XXX"], {"X": "minecraft:leather"}))
    write_json(recipes / "salted_flesh.json",
               shapeless("got:salted_flesh", ["minecraft:rotten_flesh", "got:salt"]))

    for base in [
        "alloy_steel_dagger", "bronze_dagger", "copper_dagger", "gold_dagger",
        "iron_dagger", "obsidian_dagger", "stone_dagger", "valyrian_dagger",
        "wood_dagger", "dart",
    ]:
        write_json(recipes / f"{base}_poisoned.json",
                   shapeless(f"got:{base}_poisoned", [f"got:{base}", "got:bottle_poison"]))

    write_json(recipes / "arrow_poisoned.json",
               shapeless("got:arrow_poisoned",
                          ["minecraft:arrow"] * 4 + ["got:bottle_poison"], 4))
    write_json(recipes / "crossbow_bolt_poisoned.json",
               shapeless("got:crossbow_bolt_poisoned",
                          ["got:crossbow_bolt"] * 4 + ["got:bottle_poison"], 4))


def build_tags() -> None:
    write_json(RESOURCES / "data/forge/tags/items/nuggets/copper.json", {
        "replace": False,
        "values": ["got:copper_nugget"],
    })


def build_language() -> None:
    path = ASSETS / "lang/en_us.json"
    values = json.loads(path.read_text(encoding="utf-8"))
    for item_id, display_name in ITEMS.items():
        values[f"item.got.{item_id}"] = display_name
    values["tooltip.got.bottle_poison"] = "Poison a drink in your other hand or a placed filled vessel"
    write_json(path, dict(sorted(values.items())))


def fix_dirt_path_slab() -> None:
    models = ASSETS / "models/block"
    textures = {
        "bottom": "minecraft:block/dirt",
        "side": "minecraft:block/dirt_path_side",
        "top": "minecraft:block/dirt_path_top",
    }
    write_json(models / "dirt_path_slab.json", {
        "parent": "minecraft:block/slab",
        "textures": textures,
    })
    write_json(models / "dirt_path_slab_top.json", {
        "parent": "minecraft:block/slab_top",
        "textures": textures,
    })


def main() -> None:
    build_item_assets()
    build_recipes()
    build_tags()
    build_language()
    fix_dirt_path_slab()


if __name__ == "__main__":
    main()
