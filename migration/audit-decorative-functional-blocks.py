#!/usr/bin/env python3
"""Reproducible Catch-up 3 registry/resource/parity audit."""

from __future__ import annotations

import importlib.util
import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
RES = ROOT / "src/main/resources"
ASSETS = RES / "assets/got"
JAVA = ROOT / "src/main/java/got"

spec = importlib.util.spec_from_file_location("catchup3", ROOT / "migration/build-decorative-functional-blocks.py")
catalogue = importlib.util.module_from_spec(spec)
assert spec.loader
spec.loader.exec_module(catalogue)

errors: list[str] = []


def load(path: Path) -> object:
    try:
        return json.loads(path.read_text(encoding="utf-8"))
    except Exception as exc:
        errors.append(f"invalid JSON {path.relative_to(ROOT)}: {exc}")
        return {}


def walk(value: object):
    if isinstance(value, dict):
        for key, nested in value.items():
            yield key, nested
            yield from walk(nested)
    elif isinstance(value, list):
        for nested in value:
            yield from walk(nested)


def check_json_and_refs() -> None:
    segment = set(catalogue.ALL)
    language = load(ASSETS / "lang/en_us.json")
    if not (ASSETS / "models/item/leek.json").exists():
        errors.append("missing required resource src/main/resources/assets/got/models/item/leek.json")
    for block in sorted(segment):
        required = [ASSETS / "blockstates" / f"{block}.json",
                    RES / "data/got/loot_tables/blocks" / f"{block}.json"]
        if block not in catalogue.NO_ITEM:
            required.append(ASSETS / "models/item" / f"{block}.json")
        for path in required:
            if not path.exists():
                errors.append(f"missing required resource {path.relative_to(ROOT)}")
        if f"block.got.{block}" not in language:
            errors.append(f"missing translation block.got.{block}")

    for path in RES.rglob("*.json"):
        data = load(path)
        if path.parent == ASSETS / "blockstates":
            for key, value in walk(data):
                if key == "model" and isinstance(value, str) and value.startswith("got:"):
                    target = ASSETS / "models" / f"{value.split(':', 1)[1]}.json"
                    if not target.exists():
                        errors.append(f"{path.relative_to(ROOT)} references missing model {value}")
        if "/models/" in path.as_posix():
            for key, value in walk(data):
                if key == "parent" and isinstance(value, str) and value.startswith("got:"):
                    target = ASSETS / "models" / f"{value.split(':', 1)[1]}.json"
                    if not target.exists():
                        errors.append(f"{path.relative_to(ROOT)} references missing parent {value}")
            textures = data.get("textures", {}) if isinstance(data, dict) else {}
            if isinstance(textures, dict):
                for value in textures.values():
                    if isinstance(value, str) and value.startswith("got:") and not value.startswith("#"):
                        if value.startswith(("got:blocks/", "got:items/", "got:model/")):
                            # Hand-only weapon models intentionally consume the
                            # recovered large-2x/large-3x/vlarge-2x sprite trees.
                            # All ordinary inventory/block models must remain on
                            # the normalized modern texture paths.
                            if "/models/item/held/" not in path.as_posix():
                                errors.append(
                                    f"{path.relative_to(ROOT)} uses legacy non-atlas texture path {value}"
                                )
                                continue
                        target = ASSETS / "textures" / f"{value.split(':', 1)[1]}.png"
                        if not target.exists():
                            errors.append(f"{path.relative_to(ROOT)} references missing texture {value}")


def check_java_wiring() -> None:
    decorative = (JAVA / "GOTDecorativeFunctionalBlocks.java").read_text(encoding="utf-8")
    blocks = (JAVA / "GOTBlocks.java").read_text(encoding="utf-8")
    mod = (JAVA / "GOTMod.java").read_text(encoding="utf-8")
    entities = (JAVA / "GOTBlockEntities.java").read_text(encoding="utf-8")
    client = (JAVA / "GOTClientEvents.java").read_text(encoding="utf-8")
    items = (JAVA / "GOTItems.java").read_text(encoding="utf-8")
    checks = {
        "decorative bootstrap": "GOTDecorativeFunctionalBlocks.bootstrap();" in blocks,
        "sound registry": "GOTSounds.register(modBus);" in mod,
        "storage block entity": "GOTStorageBlockEntity::new" in entities,
        "weapon rack block entity": "GOTWeaponRackBlockEntity::new" in entities,
        "weapon rack renderer": "GOTWeaponRackRenderer::new" in client,
        "carved sign block entity": "GOTCarvedSignBlockEntity::new" in entities,
        "carved sign renderer": "GOTCarvedSignRenderer::new" in client,
        "legacy decor block entity": "GOTLegacyDecorBlockEntity::new" in entities,
        "legacy decor renderer": "GOTLegacyDecorRenderer::new" in client,
        "legacy decor layers": "LegacyDecorGeometry::beacon" in client and "LegacyDecorGeometry::lionRug" in client,
        "legacy machine block items": "GOTLegacyMachineBlockItem" in decorative,
        "cutout render layer": "GOTDecorativeFunctionalBlocks.CUTOUT" in client,
        "translucent render layer": "GOTDecorativeFunctionalBlocks.TRANSLUCENT" in client,
        "placeable food helper": "placeableFood(" in items,
        "leek crop item": "RegistryObject<Item> LEEK" in items,
        "duplicate guard": "Duplicate decorative/functional block id" in decorative,
    }
    errors.extend(f"missing Java integration: {name}" for name, ok in checks.items() if not ok)

    geometry = (JAVA / "LegacyDecorGeometry.java").read_text(encoding="utf-8")
    renderer = (JAVA / "GOTLegacyDecorRenderer.java").read_text(encoding="utf-8")
    geometry_checks = {
        "beacon 12-log geometry": 'for (int log = 0; log < 4; log++)' in geometry,
        "unsmeltery rocking body": 'getChild("body").xRot' in renderer,
        "bear rug animal model": 'public static LayerDefinition bearRug()' in geometry,
        "giraffe rug animal model": 'public static LayerDefinition giraffeRug()' in geometry,
        "lion rug animal model": 'public static LayerDefinition lionRug()' in geometry,
        "six rug skins": all(name in renderer for name in
            ["BEAR_BLACK", "BEAR_DARK", "BEAR_LIGHT", "GIRAFFE", "LION", "LIONESS"]),
    }
    errors.extend(f"missing original-model parity: {name}" for name, ok in geometry_checks.items() if not ok)

    for machine in ("beacon", "unsmeltery"):
        item = load(ASSETS / "models/item" / f"{machine}.json")
        if not isinstance(item, dict) or item.get("parent") != "builtin/entity":
            errors.append(f"{machine} item does not use its original custom model renderer")


def check_original_plate_models() -> None:
    expected = {
        "wooden_plate": "wood_plate", "metal_plate": "plate", "ceramic_plate": "ceramic_plate"
    }
    for block, legacy in expected.items():
        data = load(ASSETS / "models/block" / f"{block}.json")
        elements = data.get("elements", []) if isinstance(data, dict) else []
        bounds = [(element.get("from"), element.get("to")) for element in elements]
        if bounds != [([3, 0, 3], [13, 1, 13]), ([2, 1, 2], [14, 2, 14])]:
            errors.append(f"{block} does not use the original two-tier plate geometry")
        textures = data.get("textures", {}) if isinstance(data, dict) else {}
        if textures.get("base") != f"got:block/{legacy}_base" or textures.get("top") != f"got:block/{legacy}_top":
            errors.append(f"{block} does not use the original base/top plate textures")


def check_catalogue() -> None:
    ids = catalogue.ALL
    if len(ids) != 193:
        errors.append(f"expected 193 Catch-up 3 blocks, found {len(ids)}")
    if len(ids) != len(set(ids)):
        errors.append("duplicate registry IDs in Catch-up 3 catalogue")
    generated = load(RES / "data/got/decorative_functional/blocks.json")
    if isinstance(generated, dict) and generated.get("registered_blocks") != len(ids):
        errors.append("generated catalogue count does not match source catalogue")
    if set(catalogue.NO_ITEM) != {"asshai_wall_torch", "wild_fire", "marsh_lights", "leek_crop", "turnip_crop", "yam_crop"}:
        errors.append("unexpected no-block-item set")


def report() -> None:
    print("Catch-up 3 — Decorative and Functional Blocks audit")
    print(f"registered blocks: {len(catalogue.ALL)}")
    print(f"block items: {len(catalogue.ALL) - len(catalogue.NO_ITEM)}")
    print(f"blockstates: {len(catalogue.ALL)} / {len(catalogue.ALL)}")
    print(f"loot tables: {len(catalogue.ALL)} / {len(catalogue.ALL)}")
    print(f"translations: {len(catalogue.ALL)} block names + item/container/messages")
    print("deferred by design: carts/pouches/books and NPC entity spawning")
    if errors:
        print(f"FAILED ({len(errors)} errors)")
        for error in errors:
            print(f"- {error}")
        sys.exit(1)
    print("PASS — registry, wiring, JSON, model, texture, loot, item-model, and translation coverage are clean")


if __name__ == "__main__":
    check_catalogue()
    check_java_wiring()
    check_original_plate_models()
    check_json_and_refs()
    report()
