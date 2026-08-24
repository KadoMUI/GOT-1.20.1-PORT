#!/usr/bin/env python3
"""Copy legacy texture assets into the 1.20.1 atlas-visible layout.

Minecraft 1.20.1's block atlas discovers mod sprites beneath the singular
``textures/block`` and ``textures/item`` directories.  The original 1.7.10
archive uses plural ``blocks``/``items`` directories, plus a ``model`` tree.
Generated models must reference the modern resource locations, and the PNGs
they name must exist at those locations in a clean source checkout.
"""

from __future__ import annotations

import json
import shutil
from pathlib import Path


def texture_references(value: object):
    if isinstance(value, dict):
        for key, nested in value.items():
            if key == "textures" and isinstance(nested, dict):
                for reference in nested.values():
                    if isinstance(reference, str) and reference.startswith("got:") \
                            and not reference.startswith("#"):
                        yield reference
            yield from texture_references(nested)
    elif isinstance(value, list):
        for nested in value:
            yield from texture_references(nested)


def source_candidates(textures: Path, modern_path: str) -> list[Path]:
    if modern_path.startswith("block/model/"):
        return [textures / "model" / f"{modern_path.removeprefix('block/model/')}.png"]
    if modern_path.startswith("block/"):
        relative = modern_path.removeprefix("block/")
        return [textures / "blocks" / f"{relative}.png"]
    if modern_path.startswith("item/"):
        relative = modern_path.removeprefix("item/")
        return [textures / "items" / f"{relative}.png"]
    return []


def normalize_reference(value: str) -> str:
    replacements = {
        "got:blocks/": "got:block/",
        "got:items/": "got:item/",
        "got:model/": "got:block/model/",
    }
    for legacy, modern in replacements.items():
        if value.startswith(legacy):
            return modern + value.removeprefix(legacy)
    return value


def normalize_value(value: object) -> object:
    if isinstance(value, dict):
        return {key: normalize_value(nested) for key, nested in value.items()}
    if isinstance(value, list):
        return [normalize_value(nested) for nested in value]
    if isinstance(value, str):
        return normalize_reference(value)
    return value


def normalize_textures(project: Path) -> int:
    assets = project / "src/main/resources/assets/got"
    textures = assets / "textures"
    copied = 0
    references: set[str] = set()
    for model in (assets / "models").rglob("*.json"):
        data = json.loads(model.read_text(encoding="utf-8"))
        normalized = normalize_value(data)
        if normalized != data:
            model.write_text(json.dumps(normalized, indent=2) + "\n", encoding="utf-8")
        references.update(texture_references(normalized))

    for reference in sorted(references):
        modern_path = reference.removeprefix("got:")
        if not modern_path.startswith(("block/", "item/")):
            continue
        target = textures / f"{modern_path}.png"
        if target.is_file():
            continue
        source = next((candidate for candidate in source_candidates(textures, modern_path)
                       if candidate.is_file()), None)
        if source is None:
            continue
        target.parent.mkdir(parents=True, exist_ok=True)
        shutil.copy2(source, target)
        copied += 1
    return copied


def main() -> None:
    project = Path(__file__).resolve().parents[1]
    copied = normalize_textures(project)
    print(f"Normalized {copied} legacy texture asset(s) into the 1.20.1 layout.")


if __name__ == "__main__":
    main()
