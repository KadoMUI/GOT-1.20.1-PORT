#!/usr/bin/env python3
"""Regression checks for chunk-clipped reads in recovered legacy structures."""

from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "src/main/java/got"

builder = (JAVA / "world/structure/north/NorthStructureBuilder.java").read_text()
context = (JAVA / "world/structure/legacy/LegacyNorthernContext.java").read_text()

assert "public boolean insideClip(BlockPos pos)" in builder
assert "pos.getY() >= level.getMinBuildHeight()" in builder
assert "pos.getY() < level.getMaxBuildHeight()" in builder

for method in (
    "getBlock", "func_147439_a", "func_72921_c", "isOpaque",
    "isAir", "isSurface", "getTopBlock", "setGrassToDirt",
):
    start = context.index(" " + method + "(")
    end = context.index("\n    }", start)
    body = context[start:end]
    assert "insideClip" in body, f"{method} reads or writes without chunk clipping"

print("legacy worldgen clipping audit passed")
