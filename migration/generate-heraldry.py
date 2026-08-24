#!/usr/bin/env python3
"""Import, generate, and validate the 1.7.10 heraldry catalogue."""

from __future__ import annotations

import argparse
import csv
import json
import re
import shutil
from pathlib import Path


ROW = re.compile(
    r'^\s*([A-Z0-9_]+)\((\d+),\s*"([^"]+)",\s*GOTFaction\.([A-Z0-9_]+)\)[,;]$',
    re.MULTILINE,
)


def import_legacy_enum(source: Path, catalogue: Path) -> None:
    rows = ROW.findall(source.read_text(encoding="utf-8"))
    if not rows:
        raise SystemExit(f"No banner rows found in {source}")
    catalogue.parent.mkdir(parents=True, exist_ok=True)
    with catalogue.open("w", newline="", encoding="utf-8") as handle:
        writer = csv.writer(handle)
        writer.writerow(("constant", "legacy_id", "name", "faction"))
        writer.writerows(rows)


def read_catalogue(catalogue: Path) -> list[dict[str, str]]:
    with catalogue.open(newline="", encoding="utf-8") as handle:
        rows = list(csv.DictReader(handle))
    required = {"constant", "legacy_id", "name", "faction"}
    if not rows or set(rows[0]) != required:
        raise SystemExit(f"Unexpected heraldry catalogue schema in {catalogue}")
    return rows


def java_source(rows: list[dict[str, str]]) -> str:
    additions = "\n".join(
        f'        add(types, {row["legacy_id"]}, "{row["name"]}", "{row["faction"]}");'
        for row in rows
    )
    return f'''package got;

import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The complete legacy heraldry catalogue. IDs and ordering match the original
 * GPL-licensed 1.7.10 mod so commands, structures, and future NPC data can use
 * the same stable values without registering 605 separate items.
 */
public record GOTBannerType(int legacyId, String name, String factionCode) {{
    private static final List<GOTBannerType> VALUES = createTypes();
    private static final Map<Integer, GOTBannerType> BY_ID = indexById();
    private static final Map<String, GOTBannerType> BY_NAME = indexByName();

    public static final GOTBannerType DEFAULT = byLegacyId(0);
    public static final GOTBannerType NULL = byName("null");

    public GOTBannerType {{
        if (legacyId < 0 || name.isBlank() || factionCode.isBlank()) {{
            throw new IllegalArgumentException("Invalid banner catalogue row");
        }}
    }}

    public static List<GOTBannerType> values() {{
        return VALUES;
    }}

    public static GOTBannerType byLegacyId(int legacyId) {{
        GOTBannerType type = BY_ID.get(legacyId);
        if (type != null) return type;
        GOTBannerType fallback = BY_NAME.get("null");
        return fallback != null ? fallback : VALUES.get(0);
    }}

    public static GOTBannerType byName(String name) {{
        if (name == null) return byLegacyId(-1);
        GOTBannerType type = BY_NAME.get(name);
        return type != null ? type : byLegacyId(-1);
    }}

    public String translationKey() {{
        return "item.got:banner." + name + ".name";
    }}

    public ResourceLocation texture() {{
        return ResourceLocation.fromNamespaceAndPath(
                GOTMod.MOD_ID, "textures/entity/banner/" + name.toLowerCase(Locale.ROOT) + ".png");
    }}

    private static List<GOTBannerType> createTypes() {{
        ArrayList<GOTBannerType> types = new ArrayList<>({len(rows)});
{additions}
        return List.copyOf(types);
    }}

    private static void add(List<GOTBannerType> types, int legacyId, String name, String faction) {{
        types.add(new GOTBannerType(legacyId, name, faction));
    }}

    private static Map<Integer, GOTBannerType> indexById() {{
        LinkedHashMap<Integer, GOTBannerType> result = new LinkedHashMap<>();
        for (GOTBannerType type : VALUES) {{
            if (result.put(type.legacyId, type) != null) {{
                throw new IllegalStateException("Duplicate legacy banner ID " + type.legacyId);
            }}
        }}
        return Map.copyOf(result);
    }}

    private static Map<String, GOTBannerType> indexByName() {{
        LinkedHashMap<String, GOTBannerType> result = new LinkedHashMap<>();
        for (GOTBannerType type : VALUES) {{
            if (result.put(type.name, type) != null) {{
                throw new IllegalStateException("Duplicate banner name " + type.name);
            }}
        }}
        return Map.copyOf(result);
    }}
}}
'''


def generate(project: Path, catalogue: Path) -> None:
    rows = read_catalogue(catalogue)
    resource_root = project / "src/main/resources/assets/got"
    legacy_textures = resource_root / "textures/banner"
    lang = json.loads((resource_root / "lang/en_us.json").read_text(encoding="utf-8"))

    ids: set[int] = set()
    names: set[str] = set()
    errors: list[str] = []
    for row in rows:
        legacy_id = int(row["legacy_id"])
        name = row["name"]
        if legacy_id in ids:
            errors.append(f"duplicate ID {legacy_id}")
        if name in names:
            errors.append(f"duplicate name {name}")
        ids.add(legacy_id)
        names.add(name)
        if not (legacy_textures / f"{name}.png").is_file():
            errors.append(f"missing texture {name}.png")
        key = f"item.got:banner.{name}.name"
        if key not in lang:
            errors.append(f"missing English translation {key}")

    if len(rows) != 605:
        errors.append(f"expected 605 catalogue rows, found {len(rows)}")
    if errors:
        raise SystemExit("Heraldry generation failed:\n- " + "\n- ".join(errors))

    java = project / "src/main/java/got/GOTBannerType.java"
    java.write_text(java_source(rows), encoding="utf-8")

    modern_textures = resource_root / "textures/entity/banner"
    modern_textures.mkdir(parents=True, exist_ok=True)
    for row in rows:
        name = row["name"]
        shutil.copy2(legacy_textures / f"{name}.png", modern_textures / f"{name.lower()}.png")
    shutil.copy2(legacy_textures / "stand.png", modern_textures / "stand.png")

    item_models = resource_root / "models/item"
    item_models.mkdir(parents=True, exist_ok=True)
    (item_models / "banner.json").write_text(
        json.dumps({"parent": "minecraft:builtin/entity"}, indent=2) + "\n", encoding="utf-8"
    )

    modern_items = resource_root / "textures/item"
    modern_items.mkdir(parents=True, exist_ok=True)
    shutil.copy2(resource_root / "textures/items/icon_heraldry.png", modern_items / "icon_heraldry.png")
    (item_models / "icon_heraldry.json").write_text(
        json.dumps(
            {"parent": "minecraft:item/generated", "textures": {"layer0": "got:item/icon_heraldry"}},
            indent=2,
        )
        + "\n",
        encoding="utf-8",
    )

    print(
        f"Generated {len(rows)} banner variants, {len(rows) + 1} modern entity textures, "
        "and two item models"
    )


def main() -> None:
    here = Path(__file__).resolve()
    default_project = here.parents[1]
    parser = argparse.ArgumentParser()
    parser.add_argument("--project", type=Path, default=default_project)
    parser.add_argument("--catalogue", type=Path)
    parser.add_argument("--import-legacy-enum", type=Path)
    args = parser.parse_args()
    project = args.project.resolve()
    catalogue = (args.catalogue or project / "migration/heraldry-catalog.csv").resolve()
    if args.import_legacy_enum:
        import_legacy_enum(args.import_legacy_enum.resolve(), catalogue)
    generate(project, catalogue)


if __name__ == "__main__":
    main()
