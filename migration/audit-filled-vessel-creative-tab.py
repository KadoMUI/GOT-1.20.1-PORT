#!/usr/bin/env python3
"""Audit the NBT-backed filled-vessel entries in the Food and Drink tab."""

from pathlib import Path
import re


ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "src/main/java/got"

items_source = (JAVA / "GOTItems.java").read_text(encoding="utf-8")
vessels_source = (JAVA / "GOTDrinkVessel.java").read_text(encoding="utf-8")
tabs_source = (JAVA / "GOTCreativeTabs.java").read_text(encoding="utf-8")

drink_fields = re.findall(
    r"public static final RegistryObject<Item> (MUG_[A-Z0-9_]+) = drink\(",
    items_source,
)

food_list_match = re.search(
    r"public static final List<RegistryObject<\? extends Item>> FOOD_ITEMS = List\.of\((.*?)\n\s*\);",
    items_source,
    re.DOTALL,
)
if not food_list_match:
    raise SystemExit("Could not locate GOTItems.FOOD_ITEMS")

food_list = food_list_match.group(1)
missing_drinks = [field for field in drink_fields if not re.search(rf"\b{field}\b", food_list)]
if missing_drinks:
    raise SystemExit(f"Registered drinks missing from FOOD_ITEMS: {missing_drinks}")

enum_match = re.search(r"public enum GOTDrinkVessel \{(.*?);", vessels_source, re.DOTALL)
if not enum_match:
    raise SystemExit("Could not locate GOTDrinkVessel constants")

vessels = re.findall(r"^\s*([A-Z][A-Z0-9_]*)\(", enum_match.group(1), re.MULTILINE)

required_fragments = (
    "GOTItems.FOOD_ITEMS.forEach(item -> acceptFoodAndDrinkItem(output, item))",
    "if (value instanceof GOTDrinkItem drink)",
    "for (GOTDrinkVessel vessel : GOTDrinkVessel.values())",
    "output.accept(drink.createFilled(vessel, 1.0F))",
)
missing_fragments = [fragment for fragment in required_fragments if fragment not in tabs_source]
if missing_fragments:
    raise SystemExit(f"Creative-tab expansion is incomplete: {missing_fragments}")

if len(drink_fields) != 48:
    raise SystemExit(f"Expected 48 registered drinks, found {len(drink_fields)}")
if len(vessels) != 15:
    raise SystemExit(f"Expected 15 supported vessels, found {len(vessels)}")

print(f"PASS: {len(drink_fields)} drinks x {len(vessels)} vessels = {len(drink_fields) * len(vessels)} filled creative stacks")
