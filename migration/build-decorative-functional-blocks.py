#!/usr/bin/env python3
"""Generate Catch-up 3 resources from the Java decorative/functional catalogue."""

from __future__ import annotations

import json
import shutil
from pathlib import Path

from normalize_texture_layout import normalize_textures

ROOT = Path(__file__).resolve().parents[1]
RES = ROOT / "src/main/resources"
ASSETS = RES / "assets/got"
DATA = RES / "data"

COLORS = ["white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray",
          "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"]
BARS = ["asshai_bars", "copper_bars", "bronze_bars", "silver_bars", "gold_bars", "valyrian_bars", "reed_bars"]
GLASS = ["fine_glass"] + [f"{c}_fine_glass" for c in COLORS]
PANES = ["fine_glass_pane"] + [f"{c}_fine_glass_pane" for c in COLORS]
GATES = ["gate_copper_bars", "gate_bronze_bars", "gate_gold_bars", "gate_iron_bars",
         "gate_silver_bars", "gate_valyrian_bars", "gate_wooden", "gate_wooden_bars"]
CONTROLS = [f"{s}_{kind}" for s in ["andesite", "basalt", "chalk", "diorite", "granite", "labradorite", "rhyolite"]
            for kind in ["button", "pressure_plate"]]
BEDS = ["fur_bed", "lion_fur_bed", "straw_bed"]
RUGS = ["bear_rug_black", "bear_rug_dark", "bear_rug_light", "giraffe_rug", "lion_rug", "lioness_rug"]
TREASURE = ["treasure_copper", "treasure_gold", "treasure_silver", "treasure_valyrian"]
STALACTITES = ["stalactite", "stalactite_ice", "stalactite_obsidian"]
FOODS = ["apple_crumble", "banana_cake", "berry_pie", "cherry_pie", "lemon_cake", "pastry", "marzipan_block"]
SMALL_PLANTS = ["blackroot", "bluebell", "marigold", "asshai_moss", "asshai_thorn", "clover",
                "four_leaf_clover", "chrysanthemum_blue", "chrysanthemum_pink", "chrysanthemum_yellow",
                "chrysanthemum_white", "flax_plant", "tall_grass_short", "tall_grass_flowery",
                "tall_grass_wheat", "tall_grass_thistle", "tall_grass_nettles", "tall_grass_jungle_sprout",
                "red_sand_gem", "yellow_sand_gem", "southern_daisy", "southern_eastbells", "kelp"]
TALL_PLANTS = ["black_iris", "yellow_iris", "hibiscus", "flame_of_east"]
VINES = ["oldwood_vines", "willow_vines"]
CROPS = ["leek_crop", "turnip_crop", "yam_crop"]
WOODS = ["oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "ibbinia", "catalpa", "ulthos",
         "ulthos_red", "aramant", "beech", "holly", "banana", "maple", "larch", "date_palm", "mangrove",
         "chestnut", "baobab", "cedar", "fir", "pine", "lemon", "orange", "lime", "mahogany", "willow",
         "cypress", "olive", "aspen", "green_oak", "fotinia", "almond", "plum", "redwood", "pomegranate",
         "palm", "dragon", "kanuka", "weirwood"]
FALLEN = [f"{w}_fallen_leaves" for w in WOODS]
STORAGE = ["reed_basket", "sandstone_chest", "stone_chest", "bookshelf_storage"]
MACHINES = ["weapon_rack", "iron_bank", "unsmeltery", "beacon", "wild_fire", "wild_fire_jar",
            "kebab_stand", "kebab_stand_sand"]
LIGHTING = ["asshai_torch", "asshai_wall_torch", "sothoryos_double_torch", "fuse", "marsh_lights"]
BOMBS = ["bomb", "bomb_fire", "bomb_double", "bomb_fire_double", "bomb_triple", "bomb_fire_triple"]
FRUIT_BLOCKS = ["banana", "date"]
TRAPS = ["sarbacane_trap", "sarbacane_trap_gold", "sarbacane_trap_obsidian"]
CARVED_SIGNS = ["sign_carved", "sign_carved_glowing"]
MOUNDS_AND_WASTE = ["termite_mound", "infested_termite_mound", "waste_block"]
ALL = BARS + GLASS + PANES + GATES + CONTROLS + BEDS + RUGS + TREASURE + STALACTITES + FOODS + SMALL_PLANTS + \
      TALL_PLANTS + VINES + CROPS + FALLEN + STORAGE + MACHINES + LIGHTING + ["coral_reef"] + BOMBS + \
      FRUIT_BLOCKS + TRAPS + CARVED_SIGNS + MOUNDS_AND_WASTE
NO_ITEM = {"asshai_wall_torch", "wild_fire", "marsh_lights", *CROPS}


def write(path: Path, data: object) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=2, sort_keys=False) + "\n", encoding="utf-8")


def blockstate(block: str, data: object) -> None:
    write(ASSETS / "blockstates" / f"{block}.json", data)


def model(name: str, data: object) -> None:
    write(ASSETS / "models/block" / f"{name}.json", data)


def item_model(name: str, parent: str | None = None, texture: str | None = None) -> None:
    path = ASSETS / "models/item" / f"{name}.json"
    if path.exists():
        return
    if texture:
        write(path, {"parent": "minecraft:item/generated", "textures": {"layer0": texture}})
    else:
        write(path, {"parent": parent or f"got:block/{name}"})


def cube(block: str, texture: str, *, oriented: bool = False, cutout: bool = False) -> None:
    data = {"parent": "minecraft:block/cube_all", "textures": {"all": texture}}
    if cutout:
        data["render_type"] = "cutout"
    model(block, data)
    if oriented:
        blockstate(block, {"variants": {
            "facing=north": {"model": f"got:block/{block}"},
            "facing=east": {"model": f"got:block/{block}", "y": 90},
            "facing=south": {"model": f"got:block/{block}", "y": 180},
            "facing=west": {"model": f"got:block/{block}", "y": 270}}})
    else:
        blockstate(block, {"variants": {"": {"model": f"got:block/{block}"}}})


def horizontal_state(block: str, variants: dict[str, str]) -> None:
    values = {}
    for state, suffix in variants.items():
        for facing, rotation in (("north", 0), ("east", 90), ("south", 180), ("west", 270)):
            entry = {"model": f"got:block/{block}{suffix}"}
            if rotation:
                entry["y"] = rotation
            values[f"facing={facing},{state}" if state else f"facing={facing}"] = entry
    blockstate(block, {"variants": values})


def pane(block: str, texture: str, *, translucent: bool) -> None:
    render = "translucent" if translucent else "cutout"
    for suffix, parent in (("_post", "template_glass_pane_post"), ("_side", "template_glass_pane_side"),
                           ("_side_alt", "template_glass_pane_side_alt"), ("_noside", "template_glass_pane_noside"),
                           ("_noside_alt", "template_glass_pane_noside_alt")):
        model(block + suffix, {"parent": f"minecraft:block/{parent}", "render_type": render,
                               "textures": {"pane": texture, "edge": texture}})
    blockstate(block, {"multipart": [
        {"apply": {"model": f"got:block/{block}_post"}},
        {"when": {"north": "true"}, "apply": {"model": f"got:block/{block}_side"}},
        {"when": {"east": "true"}, "apply": {"model": f"got:block/{block}_side", "y": 90}},
        {"when": {"south": "true"}, "apply": {"model": f"got:block/{block}_side_alt"}},
        {"when": {"west": "true"}, "apply": {"model": f"got:block/{block}_side_alt", "y": 90}},
        {"when": {"north": "false"}, "apply": {"model": f"got:block/{block}_noside"}},
        {"when": {"east": "false"}, "apply": {"model": f"got:block/{block}_noside_alt"}},
        {"when": {"south": "false"}, "apply": {"model": f"got:block/{block}_noside_alt", "y": 90}},
        {"when": {"west": "false"}, "apply": {"model": f"got:block/{block}_noside", "y": 270}}]})
    item_model(block, parent=f"got:block/{block}_post")


def element_model(texture: str, start: list[int], end: list[int], *, cutout: bool = False) -> dict:
    data = {"textures": {"particle": texture, "all": texture}, "elements": [{"from": start, "to": end,
            "faces": {face: {"texture": "#all"} for face in ["down", "up", "north", "south", "west", "east"]}}]}
    if cutout:
        data["render_type"] = "cutout"
    return data


def plate_model(base: str, top: str) -> dict:
    """Exact two-tier shape used by GOTRenderBlocks.renderPlate in 1.7.10."""
    faces = lambda: {
        "down": {"texture": "#base"}, "up": {"texture": "#top"},
        "north": {"texture": "#base"}, "south": {"texture": "#base"},
        "west": {"texture": "#base"}, "east": {"texture": "#base"},
    }
    return {
        "textures": {"particle": base, "base": base, "top": top},
        "elements": [
            {"from": [3, 0, 3], "to": [13, 1, 13], "faces": faces()},
            {"from": [2, 1, 2], "to": [14, 2, 14], "faces": faces()},
        ],
    }


def add_original_plate_assets() -> None:
    texture_names = [
        "wood_plate_base", "wood_plate_top", "plate_base", "plate_top",
        "ceramic_plate_base", "ceramic_plate_top",
    ]
    for name in texture_names:
        source = ASSETS / "textures/blocks" / f"{name}.png"
        target = ASSETS / "textures/block" / f"{name}.png"
        if not source.is_file():
            raise FileNotFoundError(f"missing original plate texture {source}")
        target.parent.mkdir(parents=True, exist_ok=True)
        shutil.copy2(source, target)

    for block, legacy in (("wooden_plate", "wood_plate"),
                          ("metal_plate", "plate"),
                          ("ceramic_plate", "ceramic_plate")):
        model(block, plate_model(f"got:block/{legacy}_base", f"got:block/{legacy}_top"))
        blockstate(block, {"variants": {"": {"model": f"got:block/{block}"}}})


def named_texture(block: str) -> str:
    aliases = {
        "tall_grass_flowery": "tall_grass_flower", "tall_grass_nettles": "tall_grass_nettle",
        "tall_grass_jungle_sprout": "tall_grass_fernsprout", "four_leaf_clover": "clover_petal",
        "clover": "clover_petal", "red_sand_gem": "southern_flower_red",
        "yellow_sand_gem": "southern_flower_yellow", "southern_daisy": "southern_flower_daisy",
        "southern_eastbells": "southern_flower_pink", "oldwood_vines": "mirk_vines",
        "flax_plant": "flax_plant", "kelp": "plant_kelp"}
    return f"got:block/{aliases.get(block, block)}"


def add_block_assets() -> None:
    for block in BARS:
        pane(block, f"got:block/{block}", translucent=False)
    cube("fine_glass", "got:block/glass")
    pane("fine_glass_pane", "got:block/glass", translucent=True)
    for color in COLORS:
        legacy = "silver" if color == "light_gray" else color
        texture = f"got:block/stained_glass_{legacy}"
        cube(f"{color}_fine_glass", texture)
        pane(f"{color}_fine_glass_pane", texture, translucent=True)

    for block in GATES:
        texture = f"got:block/{block if block != 'gate_wooden' else 'gate_wooden_base'}"
        model(block, element_model(texture, [0, 0, 6], [16, 16, 10], cutout=True))
        model(block + "_open", element_model(texture, [0, 14, 6], [16, 16, 10], cutout=True))
        horizontal_state(block, {"open=false,powered=false": "", "open=false,powered=true": "",
                                 "open=true,powered=false": "_open", "open=true,powered=true": "_open"})

    stone_textures = {"andesite": "minecraft:block/andesite", "diorite": "minecraft:block/diorite",
                      "granite": "minecraft:block/granite", "basalt": "got:block/rock_basalt",
                      "chalk": "got:block/smooth_stone_chalk_side", "labradorite": "got:block/rock_labradorite",
                      "rhyolite": "got:block/rock_rhyolite"}
    for block in CONTROLS:
        stone, kind = block.rsplit("_", 1)
        if kind == "plate":
            stone = block.removesuffix("_pressure_plate")
            texture = stone_textures[stone]
            model(block, {"parent": "minecraft:block/pressure_plate_up", "textures": {"texture": texture}})
            model(block + "_down", {"parent": "minecraft:block/pressure_plate_down", "textures": {"texture": texture}})
            blockstate(block, {"variants": {"powered=false": {"model": f"got:block/{block}"},
                                             "powered=true": {"model": f"got:block/{block}_down"}}})
        else:
            stone = block.removesuffix("_button")
            texture = stone_textures[stone]
            for suffix, parent in (("", "button"), ("_pressed", "button_pressed"), ("_inventory", "button_inventory")):
                model(block + suffix, {"parent": f"minecraft:block/{parent}", "textures": {"texture": texture}})
            # Vanilla's own model handles the full face/facing matrix; this compact state is safe for every state.
            blockstate(block, {"variants": {"": {"model": f"got:block/{block}"}}})
            item_model(block, parent=f"got:block/{block}_inventory")

    for bed in BEDS:
        for part, prefix in (("foot", "feet"), ("head", "head")):
            model(f"{bed}_{part}", {"textures": {"particle": f"got:block/{bed}_{prefix}_top",
                  "top": f"got:block/{bed}_{prefix}_top", "side": f"got:block/{bed}_{prefix}_side",
                  "end": f"got:block/{bed}_{prefix}_end"}, "elements": [{"from": [0, 0, 0], "to": [16, 9, 16],
                  "faces": {"down": {"texture": "#top"}, "up": {"texture": "#top"},
                  "north": {"texture": "#end"}, "south": {"texture": "#end"},
                  "west": {"texture": "#side"}, "east": {"texture": "#side"}}}]})
        variants = {}
        for part in ("foot", "head"):
            for occupied in ("false", "true"):
                for facing, rot in (("north", 0), ("east", 90), ("south", 180), ("west", 270)):
                    entry = {"model": f"got:block/{bed}_{part}"}
                    if rot: entry["y"] = rot
                    variants[f"facing={facing},occupied={occupied},part={part}"] = entry
        blockstate(bed, {"variants": variants})
        item_model(bed, texture=f"got:item/{bed}")

    rug_textures = {"bear_rug_black": "bear_rug_black", "bear_rug_dark": "bear_rug_dark",
                    "bear_rug_light": "bear_rug_light", "giraffe_rug": "giraffe_rug_giraffe",
                    "lion_rug": "lion_rug_lion", "lioness_rug": "lion_rug_lioness"}
    for rug, icon in rug_textures.items():
        model(rug + "_foot", element_model(f"got:item/{icon}", [0, 0, 0], [16, 1, 16], cutout=True))
        model(rug + "_head", element_model(f"got:item/{icon}", [0, 0, 0], [16, 1, 16], cutout=True))
        horizontal_state(rug, {"part=foot": "_foot", "part=head": "_head"})
        item_model(rug, texture=f"got:item/{icon}")

    for treasure in TREASURE:
        for layers in range(1, 9):
            model(f"{treasure}_{layers}", {"textures": {"particle": f"got:block/{treasure}",
                  "top": f"got:block/{treasure}", "side": f"got:block/{treasure}_side"},
                  "elements": [{"from": [0, 0, 0], "to": [16, layers * 2, 16], "faces": {
                  "down": {"texture": "#top"}, "up": {"texture": "#top"}, "north": {"texture": "#side"},
                  "south": {"texture": "#side"}, "west": {"texture": "#side"}, "east": {"texture": "#side"}}}]})
        blockstate(treasure, {"variants": {f"layers={i}": {"model": f"got:block/{treasure}_{i}"} for i in range(1, 9)}})
        item_model(treasure, parent=f"got:block/{treasure}_1")

    stalactite_textures = {"stalactite": "minecraft:block/stone", "stalactite_ice": "minecraft:block/packed_ice",
                          "stalactite_obsidian": "minecraft:block/obsidian"}
    for stalactite, texture in stalactite_textures.items():
        model(stalactite, element_model(texture, [4, 0, 4], [12, 16, 12]))
        blockstate(stalactite, {"variants": {"vertical_direction=down": {"model": f"got:block/{stalactite}"},
                                             "vertical_direction=up": {"model": f"got:block/{stalactite}", "x": 180}}})

    for food in FOODS:
        prefix = food
        for bites in range(7):
            width = max(2, 14 - bites * 2)
            model(f"{food}_{bites}", {"textures": {"particle": f"got:block/{prefix}_side",
                  "bottom": f"got:block/{prefix}_bottom", "top": f"got:block/{prefix}_top",
                  "side": f"got:block/{prefix}_side", "inside": f"got:block/{prefix}_inner"},
                  "elements": [{"from": [1, 0, 1], "to": [1 + width, 8, 15], "faces": {
                  "down": {"texture": "#bottom"}, "up": {"texture": "#top"},
                  "north": {"texture": "#side"}, "south": {"texture": "#side"},
                  "west": {"texture": "#inside" if bites else "#side"}, "east": {"texture": "#side"}}}]})
        blockstate(food, {"variants": {f"bites={i}": {"model": f"got:block/{food}_{i}"} for i in range(7)}})
        item_model(food, parent=f"got:block/{food}_0")

    # Storage and one-off machines retain their original textures but use modern JSON geometry.
    cube("reed_basket", "got:block/thatch_reed", oriented=True)
    cube("sandstone_chest", "got:block/sandstone_bricks", oriented=True)
    cube("stone_chest", "minecraft:block/stone", oriented=True)
    cube("bookshelf_storage", "minecraft:block/bookshelf", oriented=True)
    model("weapon_rack", element_model("got:block/model/weapon_rack", [1, 0, 5], [15, 14, 11], cutout=True))
    horizontal_state("weapon_rack", {"": ""})
    item_model("weapon_rack", texture="got:item/weapon_rack")
    cube("iron_bank", "got:block/iron_bank_side")
    for lit, texture in ((False, "got:block/model/unsmeltery/idle"), (True, "got:block/model/unsmeltery/active")):
        model("unsmeltery" + ("_lit" if lit else ""), {"parent": "minecraft:block/orientable",
              "textures": {"top": texture, "front": texture, "side": texture}})
    horizontal_state("unsmeltery", {"lit=false": "", "lit=true": "_lit"})
    for lit, texture in ((False, "got:block/model/beacon"), (True, "got:block/model/beacon")):
        model("beacon" + ("_lit" if lit else ""), element_model(texture, [0, 0, 0], [16, 13, 16], cutout=True))
    blockstate("beacon", {"variants": {"lit=false": {"model": "got:block/beacon"},
                                         "lit=true": {"model": "got:block/beacon_lit"}}})
    model("wild_fire", {"parent": "minecraft:block/cross", "render_type": "cutout",
                         "textures": {"cross": "got:block/wild_fire_layer_0"}})
    blockstate("wild_fire", {"variants": {"": {"model": "got:block/wild_fire"}}})
    for lit in (False, True):
        texture = "got:block/wild_fire_layer_0" if lit else "got:block/wild_fire_jar_base_side"
        model("wild_fire_jar" + ("_lit" if lit else ""), element_model(texture, [3, 0, 3], [13, 15, 13], cutout=True))
    blockstate("wild_fire_jar", {"variants": {"lit=false": {"model": "got:block/wild_fire_jar"},
                                                "lit=true": {"model": "got:block/wild_fire_jar_lit"}}})
    for stand in ("kebab_stand", "kebab_stand_sand"):
        base = "stand_sand" if stand.endswith("sand") else "stand"
        for state, texture in (("", f"got:block/model/kebab/{base}"), ("_raw", "got:block/model/kebab/raw"),
                               ("_cooked", "got:block/model/kebab/cooked")):
            model(stand + state, element_model(texture, [1, 0, 1], [15, 14, 15], cutout=True))
        variants = {}
        for meat in range(4):
            for cooked in ("false", "true"):
                suffix = "" if meat == 0 else "_cooked" if cooked == "true" else "_raw"
                for facing, rot in (("north", 0), ("east", 90), ("south", 180), ("west", 270)):
                    entry = {"model": f"got:block/{stand}{suffix}"}
                    if rot: entry["y"] = rot
                    variants[f"cooked={cooked},facing={facing},meat={meat}"] = entry
        blockstate(stand, {"variants": variants})
        item_model(stand, texture=f"got:item/{stand}")

    model("asshai_torch", {"parent": "minecraft:block/template_torch", "render_type": "cutout",
                            "textures": {"torch": "got:block/asshai_torch"}})
    model("asshai_wall_torch", {"parent": "minecraft:block/template_torch_wall", "render_type": "cutout",
                                 "textures": {"torch": "got:block/asshai_torch"}})
    blockstate("asshai_torch", {"variants": {"": {"model": "got:block/asshai_torch"}}})
    blockstate("asshai_wall_torch", {"variants": {
        "facing=north": {"model": "got:block/asshai_wall_torch"},
        "facing=east": {"model": "got:block/asshai_wall_torch", "y": 90},
        "facing=south": {"model": "got:block/asshai_wall_torch", "y": 180},
        "facing=west": {"model": "got:block/asshai_wall_torch", "y": 270}}})
    item_model("asshai_torch", parent="got:block/asshai_torch")
    for block in ("sothoryos_double_torch", "fuse"):
        for half in ("lower", "upper"):
            texture = f"got:block/{'sothoryos_double_torch_' + ('bottom' if half == 'lower' else 'top') if block.startswith('sothoryos') else 'fuse_' + ('bottom' if half == 'lower' else 'top')}"
            model(f"{block}_{half}", element_model(texture, [7, 0, 7], [9, 16 if half == "lower" else 9, 9], cutout=True))
        blockstate(block, {"variants": {"half=lower": {"model": f"got:block/{block}_lower"},
                                         "half=upper": {"model": f"got:block/{block}_upper"}}})
    item_model("sothoryos_double_torch", texture="got:item/sothoryos_double_torch")
    write(ASSETS / "models/item/fuse.json", {"parent": "got:block/fuse_lower"})

    for bomb in BOMBS:
        fire = "_fire" in bomb
        side = "got:block/bomb_fire_side" if fire else "got:block/bomb_side"
        top = "got:block/bomb_fire_top" if fire else "got:block/bomb_top"
        for lit in (False, True):
            suffix = "_lit" if lit else ""
            model(bomb + suffix, {"render_type": "cutout", "textures": {"particle": side, "side": side,
                  "top": top, "handle": "got:block/bomb_handle"}, "elements": [
                  {"from": [2, 0, 2], "to": [14, 12, 14], "faces": {
                  "down": {"texture": "#top"}, "up": {"texture": "#top"}, "north": {"texture": "#side"},
                  "south": {"texture": "#side"}, "west": {"texture": "#side"}, "east": {"texture": "#side"}}},
                  {"from": [6, 12, 6], "to": [10, 16, 10], "faces": {
                  "down": {"texture": "#handle"}, "up": {"texture": "#handle"}, "north": {"texture": "#handle"},
                  "south": {"texture": "#handle"}, "west": {"texture": "#handle"}, "east": {"texture": "#handle"}}}]})
        blockstate(bomb, {"variants": {"lit=false": {"model": f"got:block/{bomb}"},
                                        "lit=true": {"model": f"got:block/{bomb}_lit"}}})

    for fruit in FRUIT_BLOCKS:
        model(fruit, {"parent": "minecraft:block/cube_bottom_top", "render_type": "cutout",
                      "textures": {"side": f"got:block/{fruit}_side", "top": f"got:block/{fruit}_top",
                                   "bottom": f"got:block/{fruit}_bottom"}})
        blockstate(fruit, {"variants": {"": {"model": f"got:block/{fruit}"}}})

    trap_textures = {"sarbacane_trap": "sarbacane_trap_face", "sarbacane_trap_gold": "sarbacane_trap_gold_face",
                     "sarbacane_trap_obsidian": "sarbacane_trap_obsidian_face"}
    for trap, front in trap_textures.items():
        base = "minecraft:block/obsidian" if trap.endswith("obsidian") else "minecraft:block/gold_block" if trap.endswith("gold") else "minecraft:block/stone"
        for triggered in (False, True):
            suffix = "_triggered" if triggered else ""
            model(trap + suffix, {"parent": "minecraft:block/orientable",
                  "textures": {"top": base, "front": f"got:block/{front}", "side": base}})
        horizontal_state(trap, {"triggered=false": "", "triggered=true": "_triggered"})

    for sign in CARVED_SIGNS:
        texture = "got:block/brick4_basalt_westeros_carved" if sign.endswith("glowing") else "got:block/weirwood_planks"
        model(sign, element_model(texture, [0, 1, 6], [16, 15, 10], cutout=True))
        horizontal_state(sign, {"": ""})

    cube("termite_mound", "got:block/termite_mound")
    cube("infested_termite_mound", "got:block/termite_mound")
    for index in range(8):
        model(f"waste_block_{index}", {"parent": "minecraft:block/cube_all",
                                      "textures": {"all": f"got:block/waste_block_var{index}"}})
    blockstate("waste_block", {"variants": {"": [{"model": f"got:block/waste_block_{i}"} for i in range(8)]}})
    item_model("waste_block", parent="got:block/waste_block_0")

    for plant in SMALL_PLANTS:
        model(plant, {"parent": "minecraft:block/cross", "render_type": "cutout", "textures": {"cross": named_texture(plant)}})
        blockstate(plant, {"variants": {"": {"model": f"got:block/{plant}"}}})
    tall_texture = {"black_iris": ("double_flower_black_iris_bottom", "double_flower_black_iris_top"),
                    "yellow_iris": ("double_flower_yellow_iris_bottom", "double_flower_yellow_iris_top"),
                    "hibiscus": ("double_flower_pink_bottom", "double_flower_pink_top"),
                    "flame_of_east": ("double_flower_red_bottom", "double_flower_red_top")}
    for plant, (lower, upper) in tall_texture.items():
        for half, texture in (("lower", lower), ("upper", upper)):
            model(f"{plant}_{half}", {"parent": "minecraft:block/cross", "render_type": "cutout",
                                      "textures": {"cross": f"got:block/{texture}"}})
        blockstate(plant, {"variants": {"half=lower": {"model": f"got:block/{plant}_lower"},
                                         "half=upper": {"model": f"got:block/{plant}_upper"}}})
        write(ASSETS / "models/item" / f"{plant}.json", {"parent": f"got:block/{plant}_lower"})
    for vine in VINES:
        model(vine, {"parent": "minecraft:block/cross", "render_type": "cutout", "textures": {"cross": named_texture(vine)}})
        blockstate(vine, {"variants": {"": {"model": f"got:block/{vine}"}}})
    model("coral_reef", element_model("got:block/coral_reef", [0, 0, 0], [16, 1, 16], cutout=True))
    blockstate("coral_reef", {"variants": {"": {"model": "got:block/coral_reef"}}})
    model("marsh_lights", {"parent": "minecraft:block/cross", "render_type": "cutout",
                            "textures": {"cross": "got:block/dead_marsh_plant"}})
    blockstate("marsh_lights", {"variants": {"": {"model": "got:block/marsh_lights"}}})

    for crop in CROPS:
        base = crop.removesuffix("_crop")
        variants = {}
        for age in range(8):
            stage = min(3, age // 2)
            name = f"{crop}_{age}"
            model(name, {"parent": "minecraft:block/crop", "render_type": "cutout",
                         "textures": {"crop": f"got:block/{base}_{stage}"}})
            variants[f"age={age}"] = {"model": f"got:block/{name}"}
        blockstate(crop, {"variants": variants})

    vanilla_woods = {"oak", "spruce", "birch", "jungle", "acacia", "dark_oak"}
    for block, wood in zip(FALLEN, WOODS):
        texture = f"minecraft:block/{wood}_leaves" if wood in vanilla_woods else f"got:block/{wood}_leaves"
        model(block, element_model(texture, [0, 0, 0], [16, 1, 16], cutout=True))
        blockstate(block, {"variants": {"": {"model": f"got:block/{block}"}}})

    for block in ALL:
        if block not in NO_ITEM:
            item_model(block)

    for machine in ("beacon", "unsmeltery"):
        write(ASSETS / "models/item" / f"{machine}.json", {"parent": "builtin/entity"})

    add_original_plate_assets()


def loot_self(block: str, conditions: list[dict] | None = None) -> dict:
    entry = {"type": "minecraft:item", "name": f"got:{block}"}
    if conditions:
        entry["conditions"] = conditions
    return {"type": "minecraft:block", "pools": [{"rolls": 1, "entries": [entry],
            "conditions": [{"condition": "minecraft:survives_explosion"}]}]}


def state_condition(block: str, properties: dict[str, str]) -> dict:
    return {"condition": "minecraft:block_state_property", "block": f"got:{block}", "properties": properties}


def add_data() -> None:
    for block in ALL:
        path = DATA / "got/loot_tables/blocks" / f"{block}.json"
        if block in NO_ITEM:
            write(path, {"type": "minecraft:block", "pools": []})
        elif block in BEDS or block in RUGS:
            write(path, loot_self(block, [state_condition(block, {"part": "foot"})]))
        elif block in ("sothoryos_double_torch", "fuse"):
            write(path, loot_self(block, [state_condition(block, {"half": "lower"})]))
        elif block in FOODS:
            write(path, loot_self(block, [state_condition(block, {"bites": "0"})]))
        else:
            write(path, loot_self(block))

    # Crops drop their edible root only at maturity; immature plants intentionally drop nothing.
    for crop in CROPS:
        item = crop.removesuffix("_crop")
        write(DATA / "got/loot_tables/blocks" / f"{crop}.json", {"type": "minecraft:block", "pools": [{"rolls": 1,
              "entries": [{"type": "minecraft:item", "name": f"got:{item}",
              "conditions": [state_condition(crop, {"age": "7"})]}]}]})

    recipes = {
        "reed_basket": {"pattern": ["R R", "R R", "RRR"], "key": {"R": {"item": "minecraft:sugar_cane"}}},
        "sandstone_chest": {"pattern": ["SSS", "S S", "SSS"], "key": {"S": {"item": "minecraft:sandstone"}}},
        "stone_chest": {"pattern": ["SSS", "S S", "SSS"], "key": {"S": {"item": "minecraft:stone"}}},
        "bookshelf_storage": {"pattern": ["PPP", "BBB", "PPP"], "key": {"P": {"item": "minecraft:oak_planks"}, "B": {"item": "minecraft:book"}}},
        "weapon_rack": {"pattern": ["S S", "PPP", " S "], "key": {"S": {"item": "minecraft:stick"}, "P": {"item": "minecraft:oak_planks"}}},
        "fur_bed": {"pattern": ["FFF", "PPP"], "key": {"F": {"item": "got:fur"}, "P": {"item": "minecraft:oak_planks"}}},
        "lion_fur_bed": {"pattern": ["FFF", "PPP"], "key": {"F": {"item": "got:lion_fur"}, "P": {"item": "minecraft:oak_planks"}}},
        "straw_bed": {"pattern": ["WWW", "PPP"], "key": {"W": {"item": "minecraft:wheat"}, "P": {"item": "minecraft:oak_planks"}}},
        "wild_fire_jar": {"pattern": [" G ", "GWG", " G "], "key": {"G": {"item": "minecraft:glass"}, "W": {"item": "got:mug_wild_fire"}}},
        "iron_bank": {"pattern": ["III", "ICI", "III"], "key": {"I": {"item": "minecraft:iron_ingot"}, "C": {"item": "got:coin_1"}}},
        "unsmeltery": {"pattern": ["III", "IFI", "III"], "key": {"I": {"item": "minecraft:iron_ingot"}, "F": {"item": "minecraft:furnace"}}},
        "kebab_stand": {"pattern": ["S S", "SSS", " S "], "key": {"S": {"item": "minecraft:stick"}}},
        "kebab_stand_sand": {"pattern": ["S S", "SSS", " S "], "key": {"S": {"item": "minecraft:sandstone"}}},
    }
    for block, recipe in recipes.items():
        write(DATA / "got/recipes" / f"{block}.json", {"type": "minecraft:crafting_shaped", **recipe,
                                                        "result": {"item": f"got:{block}", "count": 1}})

    bar_materials = {"copper_bars": "minecraft:copper_ingot", "bronze_bars": "got:bronze_ingot",
                     "silver_bars": "got:silver_ingot", "gold_bars": "minecraft:gold_ingot",
                     "valyrian_bars": "got:valyrian_steel_ingot"}
    for block, ingredient in bar_materials.items():
        write(DATA / "got/recipes" / f"{block}.json", {"type": "minecraft:crafting_shaped", "pattern": ["III", "III"],
              "key": {"I": {"item": ingredient}}, "result": {"item": f"got:{block}", "count": 16}})

    pickaxe = BARS + GLASS + PANES + [g for g in GATES if "wooden" not in g] + CONTROLS + TREASURE + STALACTITES + \
              ["sandstone_chest", "stone_chest", "iron_bank", "unsmeltery"] + TRAPS
    axe = [g for g in GATES if "wooden" in g] + BEDS + RUGS + ["reed_basket", "bookshelf_storage", "weapon_rack",
                                                                "beacon", "kebab_stand"]
    write(DATA / "minecraft/tags/blocks/mineable/pickaxe.json", {"replace": False, "values": [f"got:{x}" for x in pickaxe]})
    write(DATA / "minecraft/tags/blocks/mineable/axe.json", {"replace": False, "values": [f"got:{x}" for x in axe]})
    write(DATA / "minecraft/tags/blocks/beds.json", {"replace": False, "values": [f"got:{x}" for x in BEDS]})
    write(DATA / "minecraft/tags/blocks/climbable.json", {"replace": False, "values": [f"got:{x}" for x in VINES]})
    write(DATA / "got/tags/items/kebab_meats.json", {"replace": False, "values": ["minecraft:beef", "minecraft:porkchop",
          "minecraft:chicken", "minecraft:mutton", "minecraft:rabbit", "got:deer_raw", "got:camel_raw", "got:lion_raw"]})


def title(block: str) -> str:
    special = {"asshai": "Asshai", "sothoryos": "Sothoryos", "valyrian": "Valyrian", "yi": "Yi", "ti": "Ti",
               "ibbinia": "Ibbinian", "ulthos": "Ulthos"}
    return " ".join(special.get(word, word.capitalize()) for word in block.split("_"))


def add_translations() -> None:
    path = ASSETS / "lang/en_us.json"
    language = json.loads(path.read_text(encoding="utf-8"))
    for block in ALL:
        language[f"block.got.{block}"] = title(block)
    language["item.got.asshai_torch"] = "Asshai Torch"
    language["item.got.leek"] = "Leek"
    language["container.got.storage"] = "Storage"
    language["message.got.iron_bank_balance"] = "Consolidated %,d copper pennies"
    language["message.got.unsmeltery_invalid"] = "That item cannot be unsmelted"
    write(path, dict(sorted(language.items())))
    item_model("leek", texture="got:item/leek")


def main() -> None:
    assert len(ALL) == len(set(ALL)), "duplicate Catch-up 3 registry IDs"
    add_block_assets()
    add_data()
    add_translations()
    copied = normalize_textures(ROOT)
    catalogue = DATA / "got/decorative_functional/blocks.json"
    write(catalogue, {"segment": "Catch-up 3", "registered_blocks": len(ALL), "no_block_item": sorted(NO_ITEM),
                      "categories": {"bars": BARS, "glass": GLASS + PANES, "gates": GATES, "controls": CONTROLS,
                                     "beds_and_rugs": BEDS + RUGS, "storage": STORAGE, "machines": MACHINES,
                                     "lighting": LIGHTING, "food": FOODS + FRUIT_BLOCKS, "bombs_and_traps": BOMBS + TRAPS,
                                     "signs": CARVED_SIGNS, "mounds_and_waste": MOUNDS_AND_WASTE,
                                     "treasure_and_stalactites": TREASURE + STALACTITES,
                                     "plants": SMALL_PLANTS + TALL_PLANTS + VINES + CROPS + FALLEN + ["coral_reef"]}})
    print(f"Generated Catch-up 3 resources for {len(ALL)} blocks "
          f"({len(ALL) - len(NO_ITEM)} block items); normalized {copied} texture asset(s).")


if __name__ == "__main__":
    main()
