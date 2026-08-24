#!/usr/bin/env python3
"""Generate the data-pack biome entries consumed by Minecraft 1.20.1.

GOTBiomes is the authoritative catalogue for IDs and preset families.  Biomes
are a dynamic worldgen registry in modern Minecraft, so ResourceKey constants
alone cannot bind them during world creation; each key needs a JSON entry.
"""

from __future__ import annotations

import json
import math
import re
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
CATALOGUE = ROOT / "src/main/java/got/world/biome/GOTBiomes.java"
OUTPUT = ROOT / "src/main/resources/data/got/worldgen/biome"

ENTRY = re.compile(
    r'ResourceKey<Biome>\s+[A-Z0-9_]+\s*=\s*register\("([a-z0-9_]+)",\s*GOTBiomePreset\.([A-Z_]+)\);'
)

PRESETS: dict[str, tuple[float, float, bool]] = {
    "DESERT": (1.8, 0.0, False),
    "DESERT_COLD": (0.25, 0.05, True),
    "FROST": (-0.5, 0.5, True),
    "POLAR": (-0.5, 0.5, True),
    "TAIGA": (0.2, 0.8, True),
    "SAVANNAH": (1.2, 0.15, True),
    "BUSHLAND": (1.2, 0.15, True),
    "JUNGLE": (1.0, 1.0, True),
    "MOUNTAINS": (0.35, 0.5, True),
    "MARSHES": (0.8, 0.9, True),
    "FOREST": (0.7, 0.8, True),
    "NORTHERN_PLAINS": (0.35, 0.6, True),
    "SOUTHERN_PLAINS": (1.0, 0.45, True),
    "AQUATIC": (0.5, 0.5, True),
    "MIDERATE_PLAINS": (0.7, 0.6, True),
}


def java_hsb_to_rgb(hue: float, saturation: float, brightness: float) -> int:
    if saturation == 0.0:
        channel = int(brightness * 255.0 + 0.5)
        red = green = blue = channel
    else:
        h = (hue - math.floor(hue)) * 6.0
        fraction = h - math.floor(h)
        p = brightness * (1.0 - saturation)
        q = brightness * (1.0 - saturation * fraction)
        t = brightness * (1.0 - saturation * (1.0 - fraction))
        sector = int(h)
        red_f, green_f, blue_f = {
            0: (brightness, t, p),
            1: (q, brightness, p),
            2: (p, brightness, t),
            3: (p, q, brightness),
            4: (t, p, brightness),
            5: (brightness, p, q),
        }[sector]
        red = int(red_f * 255.0 + 0.5)
        green = int(green_f * 255.0 + 0.5)
        blue = int(blue_f * 255.0 + 0.5)
    return (red << 16) | (green << 8) | blue


def sky_color(temperature: float) -> int:
    value = max(-1.0, min(1.0, temperature / 3.0))
    return java_hsb_to_rgb(0.62222224 - value * 0.05, 0.5 + value * 0.1, 1.0)


def biome_json(biome_id: str, temperature: float, downfall: float, precipitation: bool) -> dict:
    effects = {
        "fog_color": 0xC0D8FF,
        "sky_color": sky_color(temperature),
        "water_color": 0x3F76E4,
        "water_fog_color": 0x050533,
    }
    if biome_id in {"valyria", "valyria_volcano"}:
        effects.update({
            "fog_color": 0x808080,
            "sky_color": 0x808080,
            "water_color": 0x808080,
            "water_fog_color": 0x404040,
            "grass_color": 0x808080,
            "foliage_color": 0x808080,
        })
    elif biome_id.startswith("shadow_"):
        effects.update({
            "fog_color": 0x000000,
            "sky_color": 0x000000,
            "water_color": 0x000000,
            "water_fog_color": 0x000000,
        })
    return {
        "has_precipitation": precipitation,
        "temperature": temperature,
        "downfall": downfall,
        "effects": effects,
        "spawners": {},
        "spawn_costs": {},
        "carvers": {},
        "features": [],
    }


def main() -> None:
    entries = ENTRY.findall(CATALOGUE.read_text(encoding="utf-8"))
    if len(entries) != 187:
        raise SystemExit(f"Expected 187 GOT biome entries, found {len(entries)}")
    unknown = sorted({preset for _, preset in entries if preset not in PRESETS})
    if unknown:
        raise SystemExit(f"Missing preset definitions: {', '.join(unknown)}")

    OUTPUT.mkdir(parents=True, exist_ok=True)
    expected = set()
    for biome_id, preset in entries:
        expected.add(f"{biome_id}.json")
        temperature, downfall, precipitation = PRESETS[preset]
        path = OUTPUT / f"{biome_id}.json"
        path.write_text(
            json.dumps(biome_json(biome_id, temperature, downfall, precipitation), indent=2) + "\n",
            encoding="utf-8",
        )

    for stale in OUTPUT.glob("*.json"):
        if stale.name not in expected:
            stale.unlink()
    print(f"Generated {len(entries)} biome JSON files in {OUTPUT.relative_to(ROOT)}")


if __name__ == "__main__":
    main()
