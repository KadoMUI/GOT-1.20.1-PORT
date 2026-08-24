#!/usr/bin/env python3
import gzip
import re
import struct
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
RESOURCE_ROOT = ROOT / "src/main/resources/data/got/structures"
JAVA = ROOT / "src/main/java/got/world/structure/major/MajorSchematicStructureGenerator.java"
CHUNK_GENERATOR = ROOT / "src/main/java/got/world/terrain/PlanetosChunkGenerator.java"

EXPECTED = {
    "Braavos.schem": (146, 71, 136, -23, -7, -49),
    "CasterlyRock.schem": (221, 146, 220, -42, -1, -1),
    "CastleBlack.schem": (51, 30, 35, 0, -1, -24),
    "CrastersKeep.schem": (10, 15, 13, -9, -1, -7),
    "Dragonstone.schem": (86, 79, 72, -32, -22, -30),
    "Dreadfort.schem": (34, 28, 29, -14, -1, 1),
    "Eyrie.schem": (120, 251, 165, -47, -13, -144),
    "Harrenhall.schem": (76, 63, 65, -75, -1, -30),
    "Highgarden.schem": (96, 65, 59, 0, -6, -29),
    "KingsLanding.schem": (144, 71, 144, -81, 0, -75),
    "Lys.schem": (155, 64, 156, -4, -7, -82),
    "Myr.schem": (101, 66, 102, -5, -7, -57),
    "Qarth.schem": (309, 127, 433, -156, -2, -420),
    "Riverrun.schem": (74, 65, 63, -8, -9, -33),
    "StormsEnd.schem": (71, 74, 74, -35, -8, -7),
    "Sunspear.schem": (146, 117, 94, -9, -19, -48),
    "Tyrosh.schem": (92, 68, 82, -40, -7, -20),
    "Volantis.schem": (195, 81, 117, -27, -7, -29),
    "Winterfell.schem": (101, 50, 78, -51, -1, -75),
}

TEMPLATES = {
    "DragonstoneStore.schem",
    "IronbornStore.schem",
    "WesterosCastleSmall.schem",
    "Wildling_House.schem",
}


class Reader:
    def __init__(self, data):
        self.data = data
        self.pos = 0

    def take(self, size):
        result = self.data[self.pos:self.pos + size]
        assert len(result) == size, "truncated NBT"
        self.pos += size
        return result

    def unpack(self, fmt):
        return struct.unpack(">" + fmt, self.take(struct.calcsize(">" + fmt)))[0]

    def string(self):
        return self.take(self.unpack("H")).decode("utf-8")

    def payload(self, tag):
        if tag == 1: return self.unpack("b")
        if tag == 2: return self.unpack("h")
        if tag == 3: return self.unpack("i")
        if tag == 4: return self.unpack("q")
        if tag == 5: return self.unpack("f")
        if tag == 6: return self.unpack("d")
        if tag == 7: return self.take(self.unpack("i"))
        if tag == 8: return self.string()
        if tag == 9:
            child = self.unpack("b")
            return [self.payload(child) for _ in range(self.unpack("i"))]
        if tag == 10:
            result = {}
            while True:
                child = self.unpack("b")
                if child == 0: return result
                name = self.string()
                result[name] = self.payload(child)
        if tag == 11: return [self.unpack("i") for _ in range(self.unpack("i"))]
        if tag == 12: return [self.unpack("q") for _ in range(self.unpack("i"))]
        raise AssertionError(f"unsupported NBT tag {tag}")

    def root(self):
        assert self.unpack("b") == 10
        self.string()
        return self.payload(10)


def load_schematic(path):
    with gzip.open(path, "rb") as stream:
        return Reader(stream.read()).root()


def decode_varints(data):
    values = []
    value = shift = 0
    for byte in data:
        value |= (byte & 0x7f) << shift
        if byte & 0x80:
            shift += 7
            assert shift <= 28, "oversized palette varint"
        else:
            values.append(value)
            value = shift = 0
    assert shift == 0, "truncated palette varint"
    return values


def validate_schematic(path, expected, require_blocks=True):
    root = load_schematic(path)
    assert root["Version"] == 2, f"{path.name}: not Sponge v2"
    dimensions = (root["Width"], root["Height"], root["Length"])
    metadata = root["Metadata"]
    offset = (metadata["WEOffsetX"], metadata["WEOffsetY"], metadata["WEOffsetZ"])
    assert dimensions + offset == expected, f"{path.name}: metadata mismatch"
    values = decode_varints(root["BlockData"])
    assert len(values) == dimensions[0] * dimensions[1] * dimensions[2], f"{path.name}: wrong block count"
    inverse = {value: state for state, value in root["Palette"].items()}
    non_air = sum(1 for value in values if inverse[value].split("[", 1)[0]
                  not in {"minecraft:air", "minecraft:cave_air", "minecraft:void_air"})
    if require_blocks:
        assert non_air > 0, f"{path.name}: contains no structure blocks"
    return root, non_air


java = JAVA.read_text(encoding="utf-8")
site_count = len(re.findall(r"(?:site|adjustedSite)\(GOTWaypoint\.", java))
assert site_count == len(EXPECTED), "wrong Java site count"
assert "maximumAnchorY = level.getMaxBuildHeight() - site.offsetY() - site.height()" in java
for filename, expected in EXPECTED.items():
    path = RESOURCE_ROOT / "major" / filename
    assert path.is_file(), f"missing {filename}"
    root, _ = validate_schematic(path, expected)
    assert f'"{filename}"' in java, f"{filename} is not registered"
    for block_state in root["Palette"]:
        block = block_state.split("[", 1)[0]
        if block.startswith("got:"):
            asset = ROOT / "src/main/resources/assets/got/blockstates" / (block[4:] + ".json")
            assert asset.is_file(), f"{filename}: missing blockstate asset {block}"

template_names = {path.name for path in (RESOURCE_ROOT / "templates").glob("*.schem")}
assert template_names == TEMPLATES, "reusable template set changed"

pentos = RESOURCE_ROOT / "pending/Pentos.schem"
root = load_schematic(pentos)
values = decode_varints(root["BlockData"])
assert (root["Width"], root["Height"], root["Length"]) == (175, 31, 201)
assert len(root["Palette"]) == 1 and set(root["Palette"]) == {"minecraft:air"}
assert len(values) == 1_090_425 and set(values) == {0}
assert '"Pentos.schem"' not in java, "empty Pentos export must not be registered"

chunk_source = CHUNK_GENERATOR.read_text(encoding="utf-8")
order = [
    "PlanetosNorthStructureGenerator.generate",
    "PlanetosNightWatchStructureGenerator.generate",
    "PlanetosWildlingStructureGenerator.generate",
    "MajorSchematicStructureGenerator.generate",
]
positions = [chunk_source.index(token) for token in order]
assert positions == sorted(positions), "major schematics must run after regional structures"

for relative in [
    "src/main/java/got/world/structure/north/PlanetosNorthStructureGenerator.java",
    "src/main/java/got/world/structure/nightwatch/PlanetosNightWatchStructureGenerator.java",
    "src/main/java/got/world/structure/wildling/PlanetosWildlingStructureGenerator.java",
]:
    source = (ROOT / relative).read_text(encoding="utf-8")
    assert "MajorSchematicStructureGenerator.replaces(site.waypoint())" in source

assert re.search(r"siteCount\(\).*?SITES\.size", java, re.S)
print(f"major schematic audit passed: {len(EXPECTED)} fixed sites, {len(TEMPLATES)} templates, Pentos safely pending")
