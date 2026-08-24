#!/usr/bin/env python3
"""Validate construction families and merge their English display names.

The Forge data providers generate blockstates, models, loot tables, recipes and
tags.  This companion deliberately owns the one resource Forge cannot safely
merge with the port's existing hand-maintained file: assets/got/lang/en_us.json.
"""

from __future__ import annotations

import argparse
import csv
import json
import re
import sys
from dataclasses import dataclass
from pathlib import Path


PARTS = {
    "stairs": "Stairs",
    "slab": "Slab",
    "wall": "Wall",
    "fence": "Fence",
    "fence_gate": "Fence Gate",
    "door": "Door",
    "trapdoor": "Trapdoor",
    "beam": "Beam",
}
MATERIALS = {"masonry", "wood", "thatch", "earth", "misc"}
WOOD_ONLY = {"fence_gate", "door", "trapdoor"}
ID_PATTERN = re.compile(r"^[a-z0-9_.-]+$")
LOCATION_PATTERN = re.compile(r"^[a-z0-9_.-]+:[a-z0-9_./-]+$")

REQUIRED_COLUMNS = [
    "family_id",
    "base_block",
    "display_name",
    "material",
    "parts",
    "base_texture",
    "base_model",
    "stairs_id",
    "slab_id",
    "wall_id",
    "fence_id",
    "fence_gate_id",
    "door_id",
    "trapdoor_id",
    "beam_id",
    "stairs_name",
    "slab_name",
    "wall_name",
    "fence_name",
    "fence_gate_name",
    "door_name",
    "trapdoor_name",
    "beam_name",
    "door_bottom_texture",
    "door_top_texture",
    "door_item_texture",
    "trapdoor_texture",
    "beam_side_texture",
    "beam_end_texture",
]


@dataclass(frozen=True)
class Variant:
    family_id: str
    part: str
    block_id: str
    display_name: str


@dataclass(frozen=True)
class Family:
    family_id: str
    base_block: str
    display_name: str
    material: str
    parts: tuple[str, ...]
    variants: tuple[Variant, ...]
    textures: tuple[str, ...]


def meaningful_lines(path: Path) -> list[str]:
    return [
        line
        for line in path.read_text(encoding="utf-8").splitlines()
        if line.strip() and not line.lstrip().startswith("#")
    ]


def load_catalogue(path: Path) -> list[Family]:
    lines = meaningful_lines(path)
    if not lines:
        raise ValueError(f"{path}: catalogue has no CSV header")

    reader = csv.DictReader(lines)
    columns = reader.fieldnames or []
    missing = [column for column in REQUIRED_COLUMNS if column not in columns]
    extra = [column for column in columns if column not in REQUIRED_COLUMNS]
    if missing or extra:
        raise ValueError(f"{path}: missing columns={missing}; unexpected columns={extra}")

    families: list[Family] = []
    family_ids: set[str] = set()
    variant_ids: set[str] = set()
    for source_line, row in enumerate(reader, start=2):
        family_id = require_id(row["family_id"], path, source_line, "family_id")
        if family_id in family_ids:
            raise ValueError(f"{path}:{source_line}: duplicate family_id {family_id}")
        family_ids.add(family_id)

        base_block = require_location(row["base_block"], path, source_line, "base_block")
        display_name = required(row["display_name"], path, source_line, "display_name")
        material = required(row["material"], path, source_line, "material").lower()
        if material not in MATERIALS:
            raise ValueError(f"{path}:{source_line}: unknown material {material}")

        raw_parts = tuple(part.strip().lower() for part in row["parts"].split(";") if part.strip())
        if len(set(raw_parts)) != len(raw_parts):
            raise ValueError(f"{path}:{source_line}: duplicate parts are not allowed")
        parts = raw_parts
        if not parts:
            raise ValueError(f"{path}:{source_line}: parts cannot be empty")
        unknown_parts = sorted(set(parts) - PARTS.keys())
        if unknown_parts:
            raise ValueError(f"{path}:{source_line}: unknown parts {unknown_parts}")
        if material not in {"wood", "thatch"} and set(parts) & WOOD_ONLY:
            raise ValueError(
                f"{path}:{source_line}: {material} family uses wood-only parts "
                f"{sorted(set(parts) & WOOD_ONLY)}"
            )

        variants: list[Variant] = []
        for part in parts:
            override = row[f"{part}_id"].strip()
            block_id = require_id(override or f"{family_id}_{part}", path, source_line, f"{part}_id")
            if block_id in variant_ids:
                raise ValueError(f"{path}:{source_line}: duplicate generated block id got:{block_id}")
            variant_ids.add(block_id)
            display_override = row[f"{part}_name"].strip()
            variants.append(Variant(
                family_id,
                part,
                block_id,
                display_override or f"{display_name} {PARTS[part]}",
            ))

        texture_fields = (
            "base_texture",
            "door_bottom_texture",
            "door_top_texture",
            "door_item_texture",
            "trapdoor_texture",
            "beam_side_texture",
            "beam_end_texture",
        )
        textures = tuple(
            require_location(row[field], path, source_line, field)
            for field in texture_fields
            if row[field].strip()
        )
        if row["base_model"].strip():
            require_location(row["base_model"], path, source_line, "base_model")

        families.append(
            Family(family_id, base_block, display_name, material, parts, tuple(variants), textures)
        )
    return families


def validate_project(project: Path, families: list[Family]) -> list[str]:
    errors: list[str] = []
    resources = project / "src/main/resources"
    blockstates = resources / "assets/got/blockstates"
    existing_ids = {path.stem for path in blockstates.glob("*.json")}
    explicit_ids: set[str] = set()
    registration_pattern = re.compile(r'(?:register(?:[A-Za-z]+)?|simple|food|drink|seed)\(\s*"([a-z0-9_.-]+)"')
    for java_file in (project / "src/main/java").rglob("*.java"):
        if "construction" in java_file.parts:
            continue
        explicit_ids.update(registration_pattern.findall(java_file.read_text(encoding="utf-8")))

    for family in families:
        namespace, base_path = family.base_block.split(":", 1)
        if namespace == "got" and base_path not in existing_ids:
            errors.append(f"{family.family_id}: base block {family.base_block} has no GOT blockstate")

        for variant in family.variants:
            if variant.block_id in explicit_ids:
                errors.append(
                    f"{family.family_id}: got:{variant.block_id} collides with an explicit Java registration"
                )

        for texture in family.textures:
            namespace, texture_path = texture.split(":", 1)
            texture_file = resources / f"assets/{namespace}/textures/{texture_path}.png"
            if namespace == "got" and not texture_file.is_file():
                errors.append(f"{family.family_id}: missing texture {texture} ({texture_file})")
    return errors


def sync_translations(project: Path, families: list[Family]) -> int:
    language_path = project / "src/main/resources/assets/got/lang/en_us.json"
    translations = json.loads(language_path.read_text(encoding="utf-8"))
    added = 0
    for family in families:
        for variant in family.variants:
            key = f"block.got.{variant.block_id}"
            if translations.get(key) != variant.display_name:
                translations[key] = variant.display_name
                added += 1
    ordered = {key: translations[key] for key in sorted(translations)}
    language_path.write_text(
        json.dumps(ordered, ensure_ascii=False, indent=2) + "\n", encoding="utf-8"
    )
    return added


def required(value: str, path: Path, line: int, field: str) -> str:
    value = value.strip()
    if not value:
        raise ValueError(f"{path}:{line}: {field} cannot be empty")
    return value


def require_id(value: str, path: Path, line: int, field: str) -> str:
    value = required(value, path, line, field).lower()
    if not ID_PATTERN.fullmatch(value):
        raise ValueError(f"{path}:{line}: invalid {field} {value!r}")
    return value


def require_location(value: str, path: Path, line: int, field: str) -> str:
    value = required(value, path, line, field).lower()
    if not LOCATION_PATTERN.fullmatch(value):
        raise ValueError(f"{path}:{line}: invalid {field} {value!r}")
    return value


def main() -> int:
    default_project = Path(__file__).resolve().parents[1]
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--project", type=Path, default=default_project)
    parser.add_argument("--catalogue", type=Path)
    parser.add_argument(
        "--sync-translations",
        action="store_true",
        help="merge generated block names into the existing en_us.json",
    )
    args = parser.parse_args()

    project = args.project.resolve()
    catalogue = args.catalogue or project / "src/main/resources/data/got/construction/families.csv"
    try:
        families = load_catalogue(catalogue)
        errors = validate_project(project, families)
        if errors:
            for error in errors:
                print(f"ERROR: {error}", file=sys.stderr)
            return 1
        translation_changes = sync_translations(project, families) if args.sync_translations else 0
    except (OSError, ValueError, json.JSONDecodeError) as error:
        print(f"ERROR: {error}", file=sys.stderr)
        return 1

    variants = sum(len(family.variants) for family in families)
    print(
        f"Construction catalogue OK: {len(families)} families, {variants} variants, "
        f"{translation_changes} translation changes"
    )
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
