#!/usr/bin/env python3
"""Audit the normalized Catch-up 2 construction catalogue and resources."""

from __future__ import annotations

import csv
import json
import sys
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
RESOURCES = ROOT / "src/main/resources"
ASSETS = RESOURCES / "assets/got"
DATA = RESOURCES / "data/got"
LANG = ASSETS / "lang/en_us.json"
BASE_CSV = DATA / "construction/base_blocks.csv"
FAMILY_CSV = DATA / "construction/families.csv"

PART_ID_COLUMNS = {
    "stairs": "stairs_id",
    "slab": "slab_id",
    "wall": "wall_id",
    "fence": "fence_id",
    "fence_gate": "fence_gate_id",
    "door": "door_id",
    "trapdoor": "trapdoor_id",
    "beam": "beam_id",
}

EXPECTED_BASES = 38
EXPECTED_FAMILIES = 143
EXPECTED_VARIANTS = 501


def rows(path: Path) -> list[dict[str, str]]:
    with path.open(encoding="utf-8", newline="") as handle:
        return list(csv.DictReader(handle))


def json_file(path: Path, errors: list[str]):
    try:
        return json.loads(path.read_text(encoding="utf-8"))
    except (OSError, json.JSONDecodeError) as exc:
        errors.append(f"invalid JSON {path.relative_to(ROOT)}: {exc}")
        return None


def collect_references(value, models: set[str], textures: set[str]) -> None:
    if isinstance(value, dict):
        for key, child in value.items():
            if key in {"model", "parent"} and isinstance(child, str):
                models.add(child)
            elif key == "textures" and isinstance(child, dict):
                for texture in child.values():
                    if isinstance(texture, str) and not texture.startswith("#"):
                        textures.add(texture)
            collect_references(child, models, textures)
    elif isinstance(value, list):
        for child in value:
            collect_references(child, models, textures)


def got_resource(reference: str, kind: str) -> Path | None:
    if reference.startswith("minecraft:"):
        return None
    namespace, separator, path = reference.partition(":")
    if not separator:
        namespace, path = "minecraft", reference
    if namespace != "got":
        return None
    return ASSETS / kind / f"{path}.json" if kind == "models" else ASSETS / kind / f"{path}.png"


def main() -> int:
    errors: list[str] = []
    bases = rows(BASE_CSV)
    families = rows(FAMILY_CSV)

    if len(bases) != EXPECTED_BASES:
        errors.append(f"expected {EXPECTED_BASES} base rows, found {len(bases)}")
    if len(families) != EXPECTED_FAMILIES:
        errors.append(f"expected {EXPECTED_FAMILIES} family rows, found {len(families)}")

    base_ids = [row["id"].strip() for row in bases]
    if len(base_ids) != len(set(base_ids)):
        errors.append("duplicate base block id")

    variant_ids: list[str] = []
    for family in families:
        parts = {part.strip() for part in family["parts"].split(";") if part.strip()}
        unknown = parts - PART_ID_COLUMNS.keys()
        if unknown:
            errors.append(f"{family['family_id']}: unknown parts {sorted(unknown)}")
        for part in parts:
            block_id = family[PART_ID_COLUMNS[part]].strip()
            if not block_id:
                errors.append(f"{family['family_id']}: missing id for {part}")
            else:
                variant_ids.append(block_id)

    if len(variant_ids) != EXPECTED_VARIANTS:
        errors.append(f"expected {EXPECTED_VARIANTS} variants, found {len(variant_ids)}")
    if len(variant_ids) != len(set(variant_ids)):
        errors.append("duplicate generated variant id")
    overlap = set(base_ids) & set(variant_ids)
    if overlap:
        errors.append(f"base/variant id overlap: {sorted(overlap)}")

    translations = json_file(LANG, errors) or {}
    construction_ids = base_ids + variant_ids
    referenced_models: set[str] = set()
    referenced_textures: set[str] = set()

    for block_id in construction_ids:
        required = [
            ASSETS / f"blockstates/{block_id}.json",
            ASSETS / f"models/item/{block_id}.json",
            DATA / f"loot_tables/blocks/{block_id}.json",
        ]
        for path in required:
            if not path.is_file():
                errors.append(f"missing {path.relative_to(ROOT)}")
                continue
            parsed = json_file(path, errors)
            if parsed is not None:
                collect_references(parsed, referenced_models, referenced_textures)
        if f"block.got.{block_id}" not in translations:
            errors.append(f"missing translation block.got.{block_id}")

    visited: set[str] = set()
    pending = list(referenced_models)
    while pending:
        reference = pending.pop()
        if reference in visited:
            continue
        visited.add(reference)
        path = got_resource(reference, "models")
        if path is None:
            continue
        if not path.is_file():
            errors.append(f"missing model {reference}")
            continue
        parsed = json_file(path, errors)
        if parsed is None:
            continue
        child_models: set[str] = set()
        collect_references(parsed, child_models, referenced_textures)
        pending.extend(child_models - visited)

    for reference in sorted(referenced_textures):
        if reference.startswith(("got:blocks/", "got:items/", "got:model/")):
            errors.append(f"legacy texture path is not atlas-visible in 1.20.1: {reference}")
            continue
        path = got_resource(reference, "textures")
        if path is not None and not path.is_file():
            errors.append(f"missing texture {reference}")

    if errors:
        for error in errors:
            print(f"ERROR: {error}", file=sys.stderr)
        print(f"Construction audit failed with {len(errors)} error(s).", file=sys.stderr)
        return 1

    print(
        "Construction audit OK: "
        f"{len(bases)} bases, {len(families)} families, "
        f"{len(variant_ids)} variants, {len(construction_ids)} total blocks"
    )
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
