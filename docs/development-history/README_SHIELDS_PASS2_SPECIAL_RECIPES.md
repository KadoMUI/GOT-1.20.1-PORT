# Project Thrones — Shields Pass 2: Special Shield Recipes

This pass extends the real-item shield implementation with the three special shield recipes requested for 1.0.

## Alcoholic Shield
Uses the ordinary vanilla shield silhouette with planks in the six wooden positions. The top-middle iron-ingot position accepts **any alcoholic GOT drink** (`GOTDrinkItem.definition().alcoholicity() > 0`) regardless of vessel NBT, so mugs, goblets, horns, bottles, etc. all qualify when they contain an alcoholic beverage.

## Golden Company Shield
Uses the vanilla shield silhouette but replaces all six plank positions with **Gold Ingots** and the top-middle Iron Ingot with **Alloy Steel**.

## Targaryen Shield
Uses the ordinary plank body and replaces the top-middle Iron Ingot with **Valyrian Steel**.

These three special patterns are recognized in any faction crafting table, because Golden Company and Targaryen do not have dedicated faction crafting-table blocks and the Alcoholic Shield is intentionally universal.

The remaining achievement shields stay registered as real shield items but remain recipe-less until their unlock/crafting rules are decided.
