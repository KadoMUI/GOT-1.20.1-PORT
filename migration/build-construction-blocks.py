#!/usr/bin/env python3
"""Build the complete source/resource catalogue for Catch-up 2 construction blocks."""

from __future__ import annotations

import csv
import json
from dataclasses import dataclass, field
from pathlib import Path

from normalize_texture_layout import normalize_textures


PARTS = ("stairs", "slab", "wall", "fence", "fence_gate", "door", "trapdoor", "beam")
FAMILY_COLUMNS = [
    "family_id", "base_block", "display_name", "material", "parts",
    "base_texture", "base_model",
    *(f"{part}_id" for part in PARTS),
    *(f"{part}_name" for part in PARTS),
    "door_bottom_texture", "door_top_texture", "door_item_texture",
    "trapdoor_texture", "beam_side_texture", "beam_end_texture",
]
BASE_COLUMNS = [
    "id", "display_name", "material", "kind", "copy_block",
    "side_texture", "top_texture", "bottom_texture", "face_texture",
    "middle_texture", "top_segment_texture", "bottom_segment_texture",
]


@dataclass(frozen=True)
class BaseSpec:
    block_id: str
    display_name: str
    material: str
    kind: str
    copy_block: str
    side: str
    top: str = ""
    bottom: str = ""
    face: str = ""
    middle: str = ""
    top_segment: str = ""
    bottom_segment: str = ""

    def textures(self) -> tuple[str, str, str]:
        top = self.top or self.face or self.side
        return self.side, top, self.bottom or top


@dataclass
class VariantSpec:
    block_id: str
    display_name: str


@dataclass
class FamilySpec:
    family_id: str
    base_block: str
    display_name: str
    material: str
    base_texture: str
    base_model: str
    variants: dict[str, VariantSpec] = field(default_factory=dict)
    special_textures: dict[str, str] = field(default_factory=dict)


def legacy_texture(path: str) -> str:
    return f"got:block/{path}"


def normalized_texture(block_id: str) -> str:
    namespace, path = block_id.split(":", 1)
    return f"{namespace}:block/{path}"


def normalized_model(block_id: str) -> str:
    namespace, path = block_id.split(":", 1)
    return f"{namespace}:block/{path}"


def make_bases() -> list[BaseSpec]:
    bases = [
        BaseSpec("basalt", "Basalt", "masonry", "column", "minecraft:stone",
                 legacy_texture("rock_basalt_side"), legacy_texture("rock_basalt")),
        BaseSpec("rhyolite", "Rhyolite", "masonry", "cube", "minecraft:stone",
                 legacy_texture("rock_rhyolite")),
        BaseSpec("marble", "Marble", "masonry", "cube", "minecraft:stone",
                 legacy_texture("rock_chalk")),
        BaseSpec("labradorite", "Labradorite", "masonry", "cube", "minecraft:stone",
                 legacy_texture("rock_labradorite")),
        BaseSpec("white_sandstone", "White Sandstone", "masonry", "column", "minecraft:sandstone",
                 legacy_texture("white_sandstone"), legacy_texture("white_sandstone_top"),
                 legacy_texture("white_sandstone_bottom")),
        BaseSpec("white_sand", "White Sand", "earth", "falling", "minecraft:sand",
                 legacy_texture("white_sand")),
        BaseSpec("scorched_stone", "Scorched Stone", "masonry", "cube", "minecraft:stone",
                 legacy_texture("scorched_stone")),
        BaseSpec("bone_block", "Bone Block", "masonry", "cube", "minecraft:bone_block",
                 legacy_texture("bone_block")),
        BaseSpec("jungle_mud", "Jungle Mud", "earth", "cube", "minecraft:mud",
                 legacy_texture("mud")),
        BaseSpec("asshai_dirt", "Asshai Dirt", "earth", "cube", "minecraft:dirt",
                 legacy_texture("asshai_dirt")),
        BaseSpec("jungle_mud_path", "Jungle Mud Path", "earth", "cube", "minecraft:dirt_path",
                 legacy_texture("dirt_path_mud")),
        BaseSpec("bruschatka", "Bruschatka", "earth", "cube", "minecraft:dirt_path",
                 legacy_texture("dirt_path_brus")),
        BaseSpec("basalt_gravel", "Asshai Gravel", "earth", "falling", "minecraft:gravel",
                 legacy_texture("basalt_gravel")),
        BaseSpec("obsidian_gravel", "Obsidian Gravel", "earth", "falling", "minecraft:gravel",
                 legacy_texture("obsidian_gravel")),
    ]

    for name, display, texture in [
        ("basalt", "Smooth Basalt", "basalt"),
        ("andesite", "Smooth Andesite", "andesite"),
        ("rhyolite", "Smooth Rhyolite", "rhyolite"),
        ("diorite", "Smooth Diorite", "diorite"),
        ("granite", "Smooth Granite", "granite"),
        ("marble", "Smooth Marble", "chalk"),
        ("labradorite", "Smooth Labradorite", "labradorite"),
    ]:
        bases.append(BaseSpec(
            f"smooth_{name}", display, "masonry", "column", "minecraft:smooth_stone",
            legacy_texture(f"smooth_stone_{texture}_side"),
            legacy_texture(f"smooth_stone_{texture}_top")))

    for block_id, display, prefix in [
        ("diorite_pillar", "Diorite Pillar", "pillar1_diorite"),
        ("granite_pillar", "Granite Pillar", "pillar1_granite"),
        ("sandstone_pillar", "Sandstone Pillar", "pillar1_sandstone"),
        ("andesite_pillar", "Andesite Pillar", "pillar1_andesite"),
        ("asshai_basalt_pillar", "Asshai Basalt Pillar", "pillar1_basalt"),
        ("rhyolite_pillar", "Rhyolite Pillar", "pillar1_rhyolite"),
        ("basalt_pillar", "Basalt Pillar", "pillar1_basalt_westeros"),
        ("sothoryos_pillar", "Sothoryos Pillar", "pillar1_sothoryos"),
        ("red_sandstone_pillar", "Red Sandstone Pillar", "pillar1_sandstone_red"),
        ("labradorite_pillar", "Labradorite Pillar", "pillar2_labradorite"),
        ("marble_pillar", "Marble Pillar", "pillar2_chalk"),
        ("stone_pillar", "Stone Pillar", "pillar2_stone"),
        ("brick_pillar", "Brick Pillar", "pillar2_brick"),
        ("yi_ti_pillar", "Yi Ti Pillar", "pillar2_yi_ti"),
        ("yi_ti_granite_pillar", "Granite Yi Ti Pillar", "pillar2_yi_ti_red"),
        ("sothoryos_gold_pillar", "Golden Sothoryos Pillar", "pillar2_sothoryos_gold"),
        ("sothoryos_obsidian_pillar", "Obsidian Sothoryos Pillar", "pillar2_sothoryos_obsidian"),
    ]:
        bases.append(BaseSpec(
            block_id, display, "masonry", "pillar", "minecraft:stone",
            legacy_texture(f"{prefix}_side"), face=legacy_texture(f"{prefix}_face"),
            middle=legacy_texture(f"{prefix}_side_middle"),
            top_segment=legacy_texture(f"{prefix}_side_top"),
            bottom_segment=legacy_texture(f"{prefix}_side_bottom")))

    assert len(bases) == 38
    return bases


def add_family(
        families: list[FamilySpec],
        family_id: str,
        base_block: str,
        display_name: str,
        material: str,
        parts: dict[str, tuple[str, str]],
        base_texture: str | None = None,
        base_model: str | None = None,
        special_textures: dict[str, str] | None = None) -> None:
    families.append(FamilySpec(
        family_id,
        base_block,
        display_name,
        material,
        base_texture or normalized_texture(base_block),
        base_model or normalized_model(base_block),
        {part: VariantSpec(*values) for part, values in parts.items()},
        special_textures or {},
    ))


def make_families(bases: list[BaseSpec]) -> list[FamilySpec]:
    base_by_id = {base.block_id: base for base in bases}
    families: list[FamilySpec] = []

    wood_types = [
        ("ibbinia", "Ibbinia", "wood_beam1_ibbinia"),
        ("catalpa", "Catalpa", "wood_beam1_catalpa"),
        ("ulthos", "Black-oak", "wood_beam1_ulthos"),
        ("charred", "Charred", "wood_beam1_charred"),
        ("apple", "Apple", "wood_beam_fruit_apple"),
        ("pear", "Pear", "wood_beam_fruit_pear"),
        ("cherry", "Cherry", "wood_beam_fruit_cherry"),
        ("mango", "Mango", "wood_beam_fruit_mango"),
        ("aramant", "Aramant", "wood_beam2_aramant"),
        ("beech", "Beech", "wood_beam2_beech"),
        ("holly", "Holly", "wood_beam2_holly"),
        ("banana", "Banana", "wood_beam2_banana"),
        ("maple", "Maple", "wood_beam3_maple"),
        ("larch", "Larch", "wood_beam3_larch"),
        ("date_palm", "Date Palm", "wood_beam3_date_palm"),
        ("mangrove", "Mangrove", "wood_beam3_mangrove"),
        ("chestnut", "Chestnut", "wood_beam4_chestnut"),
        ("baobab", "Baobab", "wood_beam4_baobab"),
        ("cedar", "Cedar", "wood_beam4_cedar"),
        ("fir", "Fir", "wood_beam4_fir"),
        ("pine", "Pine", "wood_beam5_pine"),
        ("lemon", "Lemon", "wood_beam5_lemon"),
        ("orange", "Orange", "wood_beam5_orange"),
        ("lime", "Lime", "wood_beam5_lime"),
        ("mahogany", "Mahogany", "wood_beam6_mahogany"),
        ("willow", "Willow", "wood_beam6_willow"),
        ("cypress", "Cypress", "wood_beam6_cypress"),
        ("olive", "Olive", "wood_beam6_olive"),
        ("aspen", "Aspen", "wood_beam7_aspen"),
        ("green_oak", "Green-oak", "wood_beam7_green_oak"),
        ("fotinia", "Fotinia", "wood_beam7_fotinia"),
        ("almond", "Almond", "wood_beam7_almond"),
        ("plum", "Plum", "wood_beam8_plum"),
        ("redwood", "Ironwood", "wood_beam8_redwood"),
        ("pomegranate", "Pomegranate", "wood_beam8_pomegranate"),
        ("palm", "Palm", "wood_beam8_palm"),
        ("dragon", "Dragonblood", "wood_beam9_dragon"),
        ("kanuka", "Kanuka", "wood_beam9_kanuka"),
        ("weirwood", "Weirwood", "wood_beam9_weirwood"),
        ("rotten", "Rotten", "wood_beam_rotten_rotten"),
    ]
    for token, display, beam in wood_types:
        wood_label = "" if token == "weirwood" else " Wood"
        parts = {
            "stairs": (f"stairs_{token}", f"{display}{wood_label} Stairs"),
            "slab": (f"wood_slab_{token}", f"{display}{wood_label} Slab"),
            "fence": (f"fence_{token}", f"{display} Fence"),
            "fence_gate": (f"fence_gate_{token}", f"{display} Fence Gate"),
            "door": (f"door_{token}", f"{display} Door"),
            "trapdoor": (f"trapdoor_{token}", f"{display} Trapdoor"),
            "beam": (f"wood_beam_{token}", f"{display}{wood_label} Beam"),
        }
        add_family(
            families, token, f"got:{token}_planks", display, "wood", parts,
            special_textures={
                "door_bottom_texture": legacy_texture(f"door_{token}_lower"),
                "door_top_texture": legacy_texture(f"door_{token}_upper"),
                "door_item_texture": f"got:item/door_{token}",
                "trapdoor_texture": legacy_texture(f"trapdoor_{token}"),
                "beam_side_texture": legacy_texture(f"{beam}_side"),
                "beam_end_texture": legacy_texture(f"{beam}_top"),
            })

    for token, display, beam in [
        ("oak", "Oak", "wood_beam_v1_oak"),
        ("spruce", "Spruce", "wood_beam_v1_spruce"),
        ("birch", "Birch", "wood_beam_v1_birch"),
        ("jungle", "Jungle", "wood_beam_v1_jungle"),
        ("acacia", "Acacia", "wood_beam_v2_acacia"),
        ("dark_oak", "Dark Oak", "wood_beam_v2_dark_oak"),
    ]:
        add_family(
            families, f"vanilla_{token}", f"minecraft:{token}_planks", display, "wood",
            {"beam": (f"wood_beam_{token}", f"{display} Wood Beam")},
            special_textures={
                "beam_side_texture": legacy_texture(f"{beam}_side"),
                "beam_end_texture": legacy_texture(f"{beam}_top"),
            })

    brick_families = [
        ("andesite_brick", "got:andesite_bricks", "Andesite Brick", "stairs_andesite_brick"),
        ("cracked_andesite_brick", "got:cracked_andesite_bricks", "Cracked Andesite Brick", "stairs_andesite_brick_cracked"),
        ("mossy_andesite_brick", "got:mossy_andesite_bricks", "Mossy Andesite Brick", "stairs_andesite_brick_mossy"),
        ("basalt_brick", "got:basalt_westeros_bricks", "Basalt Brick", "stairs_basalt_brick"),
        ("asshai_basalt_brick", "got:basalt_bricks", "Asshai Basalt Brick", "stairs_basalt_brick_asshai"),
        ("asshai_cracked_basalt_brick", "got:cracked_basalt_bricks", "Asshai Cracked Basalt Brick", "stairs_basalt_brick_asshai_cracked"),
        ("diorite_brick", "got:diorite_bricks", "Diorite Brick", "stairs_diorite_brick"),
        ("granite_brick", "got:granite_bricks", "Granite Brick", "stairs_granite_brick"),
        ("labradorite_brick", "got:labradorite_bricks", "Labradorite Brick", "stairs_labradorite_brick"),
        ("cracked_labradorite_brick", "got:cracked_labradorite_bricks", "Cracked Labradorite Brick", "stairs_labradorite_brick_cracked"),
        ("mossy_labradorite_brick", "got:mossy_labradorite_bricks", "Mossy Labradorite Brick", "stairs_labradorite_brick_mossy"),
        ("marble_brick", "got:chalk_bricks", "Marble Brick", "stairs_chalk_brick"),
        ("mud_brick", "got:mud_bricks", "Mud Brick", "stairs_mud_brick"),
        ("rhyolite_brick", "got:rhyolite_bricks", "Rhyolite Brick", "stairs_rhyolite_brick"),
        ("sandstone_brick", "got:sandstone_bricks", "Sandstone Brick", "stairs_sandstone_brick"),
        ("cracked_sandstone_brick", "got:cracked_sandstone_bricks", "Cracked Sandstone Brick", "stairs_sandstone_brick_cracked"),
        ("red_sandstone_brick", "got:sandstone_red_bricks", "Red Sandstone Brick", "stairs_sandstone_brick_red"),
        ("cracked_red_sandstone_brick", "got:cracked_sandstone_red_bricks", "Cracked Red Sandstone Brick", "stairs_sandstone_brick_red_cracked"),
        ("sothoryos_brick", "got:sothoryos_bricks", "Sothoryos Brick", "stairs_sothoryos_brick"),
        ("cracked_sothoryos_brick", "got:cracked_sothoryos_bricks", "Cracked Sothoryos Brick", "stairs_sothoryos_brick_cracked"),
        ("sothoryos_gold_brick", "got:sothoryos_gold_bricks", "Golden Sothoryos Brick", "stairs_sothoryos_brick_gold"),
        ("mossy_sothoryos_brick", "got:mossy_sothoryos_bricks", "Mossy Sothoryos Brick", "stairs_sothoryos_brick_mossy"),
        ("sothoryos_obsidian_brick", "got:sothoryos_obsidian_bricks", "Obsidian Sothoryos Brick", "stairs_sothoryos_brick_obsidian"),
        ("yi_ti_brick", "got:yi_ti_bricks", "Yi Ti Brick", "stairs_yi_ti_brick"),
        ("cracked_yi_ti_brick", "got:cracked_yi_ti_bricks", "Cracked Yi Ti Brick", "stairs_yi_ti_brick_cracked"),
        ("flowery_yi_ti_brick", "got:yi_ti_flowers_bricks", "Flowery Yi Ti Brick", "stairs_yi_ti_brick_flowers"),
        ("mossy_yi_ti_brick", "got:mossy_yi_ti_bricks", "Mossy Yi Ti Brick", "stairs_yi_ti_brick_mossy"),
        ("granite_yi_ti_brick", "got:yi_ti_granite_bricks", "Granite Yi Ti Brick", "stairs_yi_ti_brick_red"),
        ("cracked_brick", "got:cracked_red_bricks", "Cracked Brick", "stairs_brick_cracked"),
        ("mossy_brick", "got:mossy_red_bricks", "Mossy Brick", "stairs_brick_mossy"),
        ("cracked_stone_brick", "minecraft:cracked_stone_bricks", "Cracked Stone Brick", "stairs_stone_brick_cracked"),
    ]
    for stem, base, display, stairs_id in brick_families:
        add_family(families, stem, base, display, "masonry", {
            "stairs": (stairs_id, f"{display} Stairs"),
            "slab": (f"{stem}_slab", f"{display} Slab"),
            "wall": (f"{stem}_wall", f"{display} Wall"),
        })

    raw_families = [
        ("basalt", "got:basalt", "Basalt", "stairs_basalt"),
        ("andesite", "minecraft:andesite", "Andesite", "stairs_andesite"),
        ("rhyolite", "got:rhyolite", "Rhyolite", "stairs_rhyolite"),
        ("diorite", "minecraft:diorite", "Diorite", "stairs_diorite"),
        ("granite", "minecraft:granite", "Granite", "stairs_granite"),
        ("marble", "got:marble", "Marble", "stairs_chalk"),
        ("labradorite", "got:labradorite", "Labradorite", "stairs_labradorite"),
    ]
    for stem, base, display, stairs_id in raw_families:
        add_family(families, f"raw_{stem}", base, display, "masonry", {
            "stairs": (stairs_id, f"{display} Stairs"),
            "slab": (f"{stem}_slab", f"{display} Slab"),
            "wall": (f"{stem}_wall", f"{display} Wall"),
        })

    for stem, base, display, stairs_id, wall_id in [
        ("bone", "got:bone_block", "Bone", "stairs_bone", "bone_wall"),
        ("scorched_stone", "got:scorched_stone", "Scorched Stone", "stairs_scorched_stone", "scorched_wall"),
        ("white_sandstone", "got:white_sandstone", "White Sandstone", "stairs_white_sandstone", "white_sandstone_wall"),
    ]:
        add_family(families, stem, base, display, "masonry", {
            "stairs": (stairs_id, f"{display} Stairs"),
            "slab": (f"{stem}_slab", f"{display} Slab"),
            "wall": (wall_id, f"{display} Wall"),
        })

    colors = [
        ("", "", "Clay Tiling", "terracotta_roof_tiles"),
        ("white", "white", "White Clay Tiling", "white_terracotta_roof_tiles"),
        ("orange", "orange", "Orange Clay Tiling", "orange_terracotta_roof_tiles"),
        ("magenta", "magenta", "Magenta Clay Tiling", "magenta_terracotta_roof_tiles"),
        ("light_blue", "light_blue", "Light Blue Clay Tiling", "light_blue_terracotta_roof_tiles"),
        ("yellow", "yellow", "Yellow Clay Tiling", "yellow_terracotta_roof_tiles"),
        ("lime", "lime", "Lime Clay Tiling", "lime_terracotta_roof_tiles"),
        ("pink", "pink", "Pink Clay Tiling", "pink_terracotta_roof_tiles"),
        ("gray", "gray", "Gray Clay Tiling", "gray_terracotta_roof_tiles"),
        ("light_gray", "light_gray", "Light Gray Clay Tiling", "silver_terracotta_roof_tiles"),
        ("cyan", "cyan", "Cyan Clay Tiling", "cyan_terracotta_roof_tiles"),
        ("purple", "purple", "Purple Clay Tiling", "purple_terracotta_roof_tiles"),
        ("blue", "blue", "Blue Clay Tiling", "blue_terracotta_roof_tiles"),
        ("brown", "brown", "Brown Clay Tiling", "brown_terracotta_roof_tiles"),
        ("green", "green", "Green Clay Tiling", "green_terracotta_roof_tiles"),
        ("red", "red", "Red Clay Tiling", "red_terracotta_roof_tiles"),
        ("black", "black", "Black Clay Tiling", "black_terracotta_roof_tiles"),
    ]
    for family_suffix, legacy_suffix, display, base_id in colors:
        stem = f"{family_suffix}_clay_tile" if family_suffix else "clay_tile"
        stairs_id = "stairs_clay_tile" + (f"_dyed_{legacy_suffix}" if legacy_suffix else "")
        add_family(families, stem, f"got:{base_id}", display, "masonry", {
            "stairs": (stairs_id, f"{display} Stairs"),
            "slab": (f"{stem}_slab", f"{display} Slab"),
            "wall": (f"{stem}_wall", f"{display} Wall"),
        })

    for stem, base, display, stairs_id, material in [
        ("thatch", "got:thatch_thatch", "Thatch", "stairs_thatch", "thatch"),
        ("reed", "got:thatch_reed", "Reed", "stairs_reed", "thatch"),
    ]:
        add_family(families, stem, base, display, material, {
            "stairs": (stairs_id, f"{display} Stairs"),
            "slab": (f"{stem}_slab", f"{display} Slab"),
        })

    for block_id, display in [
        ("smooth_basalt", "Smooth Basalt"),
        ("smooth_andesite", "Smooth Andesite"),
        ("smooth_rhyolite", "Smooth Rhyolite"),
        ("smooth_diorite", "Smooth Diorite"),
        ("smooth_granite", "Smooth Granite"),
        ("smooth_marble", "Smooth Marble"),
        ("smooth_labradorite", "Smooth Labradorite"),
    ]:
        add_family(families, block_id, f"got:{block_id}", display, "masonry", {
            "slab": (f"{block_id}_slab", f"{display} Slab"),
        })

    pillar_ids = [
        "diorite_pillar", "granite_pillar", "sandstone_pillar", "andesite_pillar",
        "asshai_basalt_pillar", "rhyolite_pillar", "basalt_pillar", "sothoryos_pillar",
        "red_sandstone_pillar", "labradorite_pillar", "marble_pillar", "stone_pillar",
        "brick_pillar", "yi_ti_pillar", "yi_ti_granite_pillar",
        "sothoryos_gold_pillar", "sothoryos_obsidian_pillar",
    ]
    for block_id in pillar_ids:
        display = base_by_id[block_id].display_name
        add_family(families, block_id, f"got:{block_id}", display, "masonry", {
            "slab": (f"{block_id}_slab", f"{display} Slab"),
        })

    for stem, base, display in [
        ("dirt", "minecraft:dirt", "Dirt"),
        ("dirt_path", "minecraft:dirt_path", "Dirt Path"),
        ("jungle_mud", "got:jungle_mud", "Jungle Mud"),
        ("asshai_dirt", "got:asshai_dirt", "Asshai Dirt"),
        ("jungle_mud_path", "got:jungle_mud_path", "Jungle Mud Path"),
        ("bruschatka", "got:bruschatka", "Bruschatka"),
        ("gravel", "minecraft:gravel", "Gravel"),
        ("asshai_gravel", "got:basalt_gravel", "Asshai Gravel"),
        ("obsidian_gravel", "got:obsidian_gravel", "Obsidian Gravel"),
        ("sand", "minecraft:sand", "Sand"),
        ("red_sand", "minecraft:red_sand", "Red Sand"),
        ("white_sand", "got:white_sand", "White Sand"),
    ]:
        add_family(families, stem, base, display, "earth", {
            "slab": (f"{stem}_slab", f"{display} Slab"),
        })

    add_family(families, "stone", "minecraft:stone", "Stone", "masonry", {
        "wall": ("stone_wall", "Stone Wall"),
    })

    # Dynamic bases retain the legacy filenames, but use the modern singular
    # texture directory required by Minecraft 1.20.1's block atlas.
    for family in families:
        namespace, path = family.base_block.split(":", 1)
        if namespace == "got" and path in base_by_id:
            family.base_texture = base_by_id[path].side

    assert len(families) == 143, len(families)
    assert sum(len(family.variants) for family in families) == 501
    return families


def write_json(path: Path, value: object) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(value, indent=2) + "\n", encoding="utf-8")


def model_ref(block_id: str) -> str:
    return f"got:block/{block_id}"


def texture_triple(family: FamilySpec, base_by_id: dict[str, BaseSpec]) -> tuple[str, str, str]:
    # dirt_path is a block-model ID, not a sprite ID. Vanilla exposes three
    # atlas sprites for its slab faces; treating minecraft:block/dirt_path as
    # one texture produced the final purple/black construction block.
    if family.base_block == "minecraft:dirt_path":
        return (
            "minecraft:block/dirt_path_side",
            "minecraft:block/dirt_path_top",
            "minecraft:block/dirt",
        )
    namespace, path = family.base_block.split(":", 1)
    if namespace == "got" and path in base_by_id:
        return base_by_id[path].textures()
    return family.base_texture, family.base_texture, family.base_texture


def stairs_state(block_id: str) -> dict:
    variants = {}
    bottom = {
        "east": {"inner_left": 270, "inner_right": 0, "outer_left": 270, "outer_right": 0, "straight": 0},
        "north": {"inner_left": 180, "inner_right": 270, "outer_left": 180, "outer_right": 270, "straight": 270},
        "south": {"inner_left": 0, "inner_right": 90, "outer_left": 0, "outer_right": 90, "straight": 90},
        "west": {"inner_left": 90, "inner_right": 180, "outer_left": 90, "outer_right": 180, "straight": 180},
    }
    top = {
        "east": {"inner_left": 0, "inner_right": 90, "outer_left": 0, "outer_right": 90, "straight": 0},
        "north": {"inner_left": 270, "inner_right": 0, "outer_left": 270, "outer_right": 0, "straight": 270},
        "south": {"inner_left": 90, "inner_right": 180, "outer_left": 90, "outer_right": 180, "straight": 90},
        "west": {"inner_left": 180, "inner_right": 270, "outer_left": 180, "outer_right": 270, "straight": 180},
    }
    for half, rotations in (("bottom", bottom), ("top", top)):
        for facing, shapes in rotations.items():
            for shape, rotation in shapes.items():
                suffix = "_inner" if shape.startswith("inner") else "_outer" if shape.startswith("outer") else ""
                entry = {"model": model_ref(block_id + suffix)}
                if half == "top":
                    entry["x"] = 180
                if rotation:
                    entry["y"] = rotation
                if half == "top" or rotation:
                    entry["uvlock"] = True
                variants[f"facing={facing},half={half},shape={shape}"] = entry
    return {"variants": variants}


def wall_state(block_id: str) -> dict:
    multipart = [{"when": {"up": "true"}, "apply": {"model": model_ref(block_id + "_post")}}]
    for state, suffix in (("low", "_side"), ("tall", "_side_tall")):
        for direction, rotation in (("north", 0), ("east", 90), ("south", 180), ("west", 270)):
            apply = {"model": model_ref(block_id + suffix), "uvlock": True}
            if rotation:
                apply["y"] = rotation
            multipart.append({"when": {direction: state}, "apply": apply})
    return {"multipart": multipart}


def fence_state(block_id: str) -> dict:
    multipart = [{"apply": {"model": model_ref(block_id + "_post")}}]
    for direction, rotation in (("north", 0), ("east", 90), ("south", 180), ("west", 270)):
        apply = {"model": model_ref(block_id + "_side"), "uvlock": True}
        if rotation:
            apply["y"] = rotation
        multipart.append({"when": {direction: "true"}, "apply": apply})
    return {"multipart": multipart}


def fence_gate_state(block_id: str) -> dict:
    variants = {}
    for facing, rotation in (("south", 0), ("west", 90), ("north", 180), ("east", 270)):
        for in_wall in (False, True):
            for opened in (False, True):
                suffix = "_wall" if in_wall else ""
                if opened:
                    suffix += "_open"
                entry = {"model": model_ref(block_id + suffix), "uvlock": True}
                if rotation:
                    entry["y"] = rotation
                key = f"facing={facing},in_wall={str(in_wall).lower()},open={str(opened).lower()}"
                variants[key] = entry
    return {"variants": variants}


def door_state(block_id: str) -> dict:
    variants = {}
    closed_rotation = {"east": 0, "south": 90, "west": 180, "north": 270}
    left_open = {"east": 90, "south": 180, "west": 270, "north": 0}
    right_open = {"east": 270, "south": 0, "west": 90, "north": 180}
    for facing in ("east", "north", "south", "west"):
        for half in ("lower", "upper"):
            level = "bottom" if half == "lower" else "top"
            for hinge in ("left", "right"):
                for opened in (False, True):
                    suffix = f"_{level}_{hinge}" + ("_open" if opened else "")
                    entry = {"model": model_ref(block_id + suffix)}
                    rotation = (left_open if hinge == "left" else right_open)[facing] if opened else closed_rotation[facing]
                    if rotation:
                        entry["y"] = rotation
                    key = f"facing={facing},half={half},hinge={hinge},open={str(opened).lower()}"
                    variants[key] = entry
    return {"variants": variants}


def trapdoor_state(block_id: str) -> dict:
    variants = {}
    rotations = {"north": 0, "east": 90, "south": 180, "west": 270}
    for facing, rotation in rotations.items():
        for half in ("bottom", "top"):
            for opened in (False, True):
                suffix = "_open" if opened else f"_{half}"
                entry = {"model": model_ref(block_id + suffix)}
                if opened and rotation:
                    entry["y"] = rotation
                key = f"facing={facing},half={half},open={str(opened).lower()}"
                variants[key] = entry
    return {"variants": variants}


def self_loot(block_id: str) -> dict:
    return {"type": "minecraft:block", "pools": [{
        "bonus_rolls": 0.0,
        "conditions": [{"condition": "minecraft:survives_explosion"}],
        "entries": [{"type": "minecraft:item", "name": f"got:{block_id}"}],
        "rolls": 1.0,
    }]}


def slab_loot(block_id: str) -> dict:
    return {"type": "minecraft:block", "pools": [{
        "bonus_rolls": 0.0,
        "entries": [{
            "type": "minecraft:item", "name": f"got:{block_id}",
            "functions": [
                {"function": "minecraft:set_count", "count": 2.0,
                 "conditions": [{"condition": "minecraft:block_state_property",
                                  "block": f"got:{block_id}", "properties": {"type": "double"}}]},
                {"function": "minecraft:explosion_decay"},
            ],
        }],
        "rolls": 1.0,
    }]}


def door_loot(block_id: str) -> dict:
    return {"type": "minecraft:block", "pools": [{
        "bonus_rolls": 0.0,
        "conditions": [
            {"condition": "minecraft:block_state_property", "block": f"got:{block_id}",
             "properties": {"half": "lower"}},
            {"condition": "minecraft:survives_explosion"},
        ],
        "entries": [{"type": "minecraft:item", "name": f"got:{block_id}"}],
        "rolls": 1.0,
    }]}


def recipe_for(family: FamilySpec, part: str, block_id: str) -> dict:
    base = {"item": family.base_block}
    recipes = {
        "stairs": (4, ["#  ", "## ", "###"], {"#": base}),
        "slab": (6, ["###"], {"#": base}),
        "wall": (6, ["###", "###"], {"#": base}),
        "fence": (3, ["#S#", "#S#"], {"#": base, "S": {"item": "minecraft:stick"}}),
        "fence_gate": (1, ["S#S", "S#S"], {"#": base, "S": {"item": "minecraft:stick"}}),
        "door": (3, ["##", "##", "##"], {"#": base}),
        "trapdoor": (2, ["###", "###"], {"#": base}),
        "beam": (3, ["#", "#", "#"], {"#": base}),
    }
    count, pattern, keys = recipes[part]
    result = {"item": f"got:{block_id}"}
    if count != 1:
        result["count"] = count
    return {"type": "minecraft:crafting_shaped", "pattern": pattern, "key": keys, "result": result}


def add_tag(resources: Path, namespace: str, registry: str, tag: str, values: list[str]) -> None:
    path = resources / f"data/{namespace}/tags/{registry}/{tag}.json"
    existing = {"replace": False, "values": []}
    if path.is_file():
        existing = json.loads(path.read_text(encoding="utf-8"))
    merged = list(dict.fromkeys([*existing.get("values", []), *values]))
    write_json(path, {"replace": False, "values": merged})


def generate_resources(project: Path, bases: list[BaseSpec], families: list[FamilySpec]) -> None:
    resources = project / "src/main/resources"
    blockstates = resources / "assets/got/blockstates"
    block_models = resources / "assets/got/models/block"
    item_models = resources / "assets/got/models/item"
    loot = resources / "data/got/loot_tables/blocks"
    recipes = resources / "data/got/recipes"
    base_by_id = {base.block_id: base for base in bases}

    block_tags: dict[str, list[str]] = {}
    item_tags: dict[str, list[str]] = {}

    def tag(name: str, value: str, item: bool = False) -> None:
        (item_tags if item else block_tags).setdefault(name, []).append(value)

    for base in bases:
        block_id = base.block_id
        side, top, bottom = base.textures()
        if base.kind == "pillar":
            states = {
                "down=false,up=false": model_ref(block_id),
                "down=false,up=true": model_ref(block_id + "_bottom"),
                "down=true,up=false": model_ref(block_id + "_top"),
                "down=true,up=true": model_ref(block_id + "_middle"),
            }
            write_json(blockstates / f"{block_id}.json",
                       {"variants": {state: {"model": model} for state, model in states.items()}})
            for suffix, texture in [
                ("", side), ("_middle", base.middle),
                ("_top", base.top_segment), ("_bottom", base.bottom_segment),
            ]:
                write_json(block_models / f"{block_id}{suffix}.json", {
                    "parent": "minecraft:block/cube_column",
                    "textures": {"end": base.face, "side": texture},
                })
        elif base.kind == "column":
            write_json(blockstates / f"{block_id}.json",
                       {"variants": {"": {"model": model_ref(block_id)}}})
            write_json(block_models / f"{block_id}.json", {
                "parent": "minecraft:block/cube_bottom_top",
                "textures": {"side": side, "top": top, "bottom": bottom},
            })
        else:
            write_json(blockstates / f"{block_id}.json",
                       {"variants": {"": {"model": model_ref(block_id)}}})
            write_json(block_models / f"{block_id}.json", {
                "parent": "minecraft:block/cube_all", "textures": {"all": side},
            })
        write_json(item_models / f"{block_id}.json", {"parent": model_ref(block_id)})
        write_json(loot / f"{block_id}.json", self_loot(block_id))
        tool = {"masonry": "mineable/pickaxe", "wood": "mineable/axe",
                "thatch": "mineable/hoe", "earth": "mineable/shovel"}.get(base.material)
        if tool:
            tag(tool, f"got:{block_id}")

    for family in families:
        side, top, bottom = texture_triple(family, base_by_id)
        for part, variant in family.variants.items():
            block_id = variant.block_id
            texture = family.base_texture
            if part == "stairs":
                write_json(blockstates / f"{block_id}.json", stairs_state(block_id))
                for suffix, parent in (("", "stairs"), ("_inner", "inner_stairs"), ("_outer", "outer_stairs")):
                    write_json(block_models / f"{block_id}{suffix}.json", {
                        "parent": f"minecraft:block/{parent}",
                        "textures": {"bottom": bottom, "side": side, "top": top},
                    })
                tag("stairs", f"got:{block_id}")
                tag("stairs", f"got:{block_id}", True)
                if family.material == "wood":
                    tag("wooden_stairs", f"got:{block_id}")
                    tag("wooden_stairs", f"got:{block_id}", True)
            elif part == "slab":
                write_json(blockstates / f"{block_id}.json", {"variants": {
                    "type=bottom": {"model": model_ref(block_id)},
                    "type=double": {"model": family.base_model},
                    "type=top": {"model": model_ref(block_id + "_top")},
                }})
                for suffix, parent in (("", "slab"), ("_top", "slab_top")):
                    write_json(block_models / f"{block_id}{suffix}.json", {
                        "parent": f"minecraft:block/{parent}",
                        "textures": {"bottom": bottom, "side": side, "top": top},
                    })
                tag("slabs", f"got:{block_id}")
                tag("slabs", f"got:{block_id}", True)
                if family.material == "wood":
                    tag("wooden_slabs", f"got:{block_id}")
                    tag("wooden_slabs", f"got:{block_id}", True)
            elif part == "wall":
                write_json(blockstates / f"{block_id}.json", wall_state(block_id))
                for suffix, parent in (("_post", "template_wall_post"),
                                       ("_side", "template_wall_side"),
                                       ("_side_tall", "template_wall_side_tall"),
                                       ("_inventory", "wall_inventory")):
                    write_json(block_models / f"{block_id}{suffix}.json", {
                        "parent": f"minecraft:block/{parent}", "textures": {"wall": side},
                    })
                tag("walls", f"got:{block_id}")
                tag("walls", f"got:{block_id}", True)
            elif part == "fence":
                write_json(blockstates / f"{block_id}.json", fence_state(block_id))
                for suffix, parent in (("_post", "fence_post"), ("_side", "fence_side"),
                                       ("_inventory", "fence_inventory")):
                    write_json(block_models / f"{block_id}{suffix}.json", {
                        "parent": f"minecraft:block/{parent}", "textures": {"texture": texture},
                    })
                tag("fences", f"got:{block_id}")
                tag("wooden_fences", f"got:{block_id}")
                tag("fences", f"got:{block_id}", True)
                tag("wooden_fences", f"got:{block_id}", True)
            elif part == "fence_gate":
                write_json(blockstates / f"{block_id}.json", fence_gate_state(block_id))
                for suffix, parent in (("", "template_fence_gate"),
                                       ("_open", "template_fence_gate_open"),
                                       ("_wall", "template_fence_gate_wall"),
                                       ("_wall_open", "template_fence_gate_wall_open")):
                    write_json(block_models / f"{block_id}{suffix}.json", {
                        "parent": f"minecraft:block/{parent}", "textures": {"texture": texture},
                    })
                tag("fence_gates", f"got:{block_id}")
                tag("fence_gates", f"got:{block_id}", True)
            elif part == "door":
                write_json(blockstates / f"{block_id}.json", door_state(block_id))
                door_bottom = family.special_textures["door_bottom_texture"]
                door_top = family.special_textures["door_top_texture"]
                for suffix, parent in [
                    ("_bottom_left", "door_bottom_left"),
                    ("_bottom_left_open", "door_bottom_left_open"),
                    ("_bottom_right", "door_bottom_right"),
                    ("_bottom_right_open", "door_bottom_right_open"),
                    ("_top_left", "door_top_left"),
                    ("_top_left_open", "door_top_left_open"),
                    ("_top_right", "door_top_right"),
                    ("_top_right_open", "door_top_right_open"),
                ]:
                    write_json(block_models / f"{block_id}{suffix}.json", {
                        "parent": f"minecraft:block/{parent}", "render_type": "cutout",
                        "textures": {"bottom": door_bottom, "top": door_top},
                    })
                write_json(item_models / f"{block_id}.json", {
                    "parent": "minecraft:item/generated",
                    "textures": {"layer0": family.special_textures["door_item_texture"]},
                })
                tag("wooden_doors", f"got:{block_id}")
                tag("wooden_doors", f"got:{block_id}", True)
            elif part == "trapdoor":
                write_json(blockstates / f"{block_id}.json", trapdoor_state(block_id))
                trap_texture = family.special_textures["trapdoor_texture"]
                for suffix, parent in (("_bottom", "template_trapdoor_bottom"),
                                       ("_top", "template_trapdoor_top"),
                                       ("_open", "template_trapdoor_open")):
                    write_json(block_models / f"{block_id}{suffix}.json", {
                        "parent": f"minecraft:block/{parent}", "render_type": "cutout",
                        "textures": {"texture": trap_texture},
                    })
                tag("wooden_trapdoors", f"got:{block_id}")
                tag("wooden_trapdoors", f"got:{block_id}", True)
            elif part == "beam":
                write_json(blockstates / f"{block_id}.json", {"variants": {
                    "axis=x": {"model": model_ref(block_id + "_horizontal"), "x": 90, "y": 90},
                    "axis=y": {"model": model_ref(block_id)},
                    "axis=z": {"model": model_ref(block_id + "_horizontal"), "x": 90},
                }})
                beam_textures = {"end": family.special_textures["beam_end_texture"],
                                 "side": family.special_textures["beam_side_texture"]}
                write_json(block_models / f"{block_id}.json", {
                    "parent": "minecraft:block/cube_column", "textures": beam_textures})
                write_json(block_models / f"{block_id}_horizontal.json", {
                    "parent": "minecraft:block/cube_column_horizontal", "textures": beam_textures})

            if part not in {"door"}:
                item_parent = block_id
                if part == "wall":
                    item_parent += "_inventory"
                elif part == "fence":
                    item_parent += "_inventory"
                elif part == "trapdoor":
                    item_parent += "_bottom"
                write_json(item_models / f"{block_id}.json", {"parent": model_ref(item_parent)})

            write_json(loot / f"{block_id}.json",
                       slab_loot(block_id) if part == "slab" else
                       door_loot(block_id) if part == "door" else self_loot(block_id))
            write_json(recipes / f"{block_id}.json", recipe_for(family, part, block_id))
            if family.material == "masonry" and part in {"stairs", "slab", "wall"}:
                count = 2 if part == "slab" else 1
                result = {"item": f"got:{block_id}"}
                if count != 1:
                    result["count"] = count
                write_json(recipes / f"{block_id}_from_stonecutting.json", {
                    "type": "minecraft:stonecutting",
                    "ingredient": {"item": family.base_block},
                    "result": f"got:{block_id}",
                    "count": count,
                })

            tool = {"masonry": "mineable/pickaxe", "wood": "mineable/axe",
                    "thatch": "mineable/hoe", "earth": "mineable/shovel"}.get(family.material)
            if tool:
                tag(tool, f"got:{block_id}")

    for name, values in block_tags.items():
        add_tag(resources, "minecraft", "blocks", name, values)
    for name, values in item_tags.items():
        add_tag(resources, "minecraft", "items", name, values)


def write_catalogues(project: Path, bases: list[BaseSpec], families: list[FamilySpec]) -> None:
    directory = project / "src/main/resources/data/got/construction"
    directory.mkdir(parents=True, exist_ok=True)
    with (directory / "base_blocks.csv").open("w", encoding="utf-8", newline="") as handle:
        writer = csv.DictWriter(handle, BASE_COLUMNS)
        writer.writeheader()
        for base in bases:
            writer.writerow({
                "id": base.block_id, "display_name": base.display_name,
                "material": base.material, "kind": base.kind, "copy_block": base.copy_block,
                "side_texture": base.side, "top_texture": base.top,
                "bottom_texture": base.bottom, "face_texture": base.face,
                "middle_texture": base.middle, "top_segment_texture": base.top_segment,
                "bottom_segment_texture": base.bottom_segment,
            })

    with (directory / "families.csv").open("w", encoding="utf-8", newline="") as handle:
        writer = csv.DictWriter(handle, FAMILY_COLUMNS)
        writer.writeheader()
        for family in families:
            row = {column: "" for column in FAMILY_COLUMNS}
            row.update({
                "family_id": family.family_id, "base_block": family.base_block,
                "display_name": family.display_name, "material": family.material,
                "parts": ";".join(family.variants), "base_texture": family.base_texture,
                "base_model": family.base_model,
            })
            for part, variant in family.variants.items():
                row[f"{part}_id"] = variant.block_id
                row[f"{part}_name"] = variant.display_name
            row.update(family.special_textures)
            writer.writerow(row)


def sync_translations(project: Path, bases: list[BaseSpec], families: list[FamilySpec]) -> None:
    path = project / "src/main/resources/assets/got/lang/en_us.json"
    translations = json.loads(path.read_text(encoding="utf-8"))
    for base in bases:
        translations[f"block.got.{base.block_id}"] = base.display_name
    for family in families:
        for variant in family.variants.values():
            translations[f"block.got.{variant.block_id}"] = variant.display_name
    path.write_text(json.dumps(dict(sorted(translations.items())), indent=2) + "\n", encoding="utf-8")


def write_audit(project: Path, bases: list[BaseSpec], families: list[FamilySpec]) -> None:
    counts = {part: 0 for part in PARTS}
    for family in families:
        for part in family.variants:
            counts[part] += 1
    lines = [
        "# Catch-up 2 Construction Coverage",
        "",
        f"- New full construction blocks: **{len(bases)}**",
        f"- Catalogue families: **{len(families)}**",
        f"- New shaped variants: **{sum(counts.values())}**",
        f"- Total new registered blocks: **{len(bases) + sum(counts.values())}**",
        "",
        "| Shape | Added |",
        "|---|---:|",
        *[f"| {part.replace('_', ' ').title()} | {counts[part]} |" for part in PARTS],
        "",
        "Vanilla 1.20.1 replacements are deliberately not duplicated: five non-oak wood",
        "doors/gates/trapdoors, six vanilla fences, stone/red-sandstone/mossy-stone",
        "stairs and slabs, and the vanilla stone-brick/sandstone/brick wall families.",
        "",
        "Heraldry, vessels, furniture, storage, gates/portcullises, plants and utility",
        "blocks remain outside this segment.",
    ]
    (project / "MILESTONE_CONSTRUCTION_BLOCKS.md").write_text("\n".join(lines) + "\n", encoding="utf-8")


def main() -> int:
    project = Path(__file__).resolve().parents[1]
    bases = make_bases()
    families = make_families(bases)
    write_catalogues(project, bases, families)
    generate_resources(project, bases, families)
    copied = normalize_textures(project)
    sync_translations(project, bases, families)
    write_audit(project, bases, families)
    variants = sum(len(family.variants) for family in families)
    print(f"Generated {len(bases)} bases, {len(families)} families and {variants} variants; "
          f"normalized {copied} texture asset(s)")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
