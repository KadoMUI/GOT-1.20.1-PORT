# Systems Pass 10 — Texture / Resource Correctness

Source-only visual/resource cleanup against the GOT 1.7.10 asset set.

## Corrected legacy texture migrations

- Restored the original `corn_stalk.png` item icon (the modern singular-path copy was incorrect).
- Restored missing modern-path pouch textures and overlays for Small/Medium/Large Pouches.
- Restored the missing `branding_iron_hot.png` modern-path texture.
- Corrected the five case-sensitive held vanilla sword references (`swordIron`, `swordGold`, `swordWood`, `swordDiamond`, `swordStone`).

## Crop / seed corrections

- Cucumber Crop now uses the four original cucumber growth visuals across the 8 modern age states instead of rendering the mature plant for all ages.
- Pipeweed Crop now uses the four original crop-stage textures instead of rendering the separate `pipeweed_plant` texture at every age.
- Rice no longer uses the vanilla Apple icon; it uses original mature GOT rice art.
- Cucumber Seeds no longer use vanilla Wheat Seeds; a dedicated seed icon is supplied because the legacy asset pack has no standalone cucumber-seed item texture.
- Flour and Dough no longer reuse the Flax texture.
- Raw Tin and Raw Silver no longer reuse their finished ingot textures.

## Animation corrections

The 1.20.1 models use `textures/item` / `textures/block`, while several legacy `.mcmeta` files remained only under the old plural folders. Modern-path animation metadata is now restored for:

- Fire Arrow
- Poisoned Arrow
- Poisoned Crossbow Bolt
- Wild Fire layer 0

All vertical sprite strips in the modern `item` and `block` folders now have animation metadata.

## Crossbow visual states

The original pull textures existed but were not reachable by the modern item models. All seven GOT Crossbow variants now have pull-state model overrides and use their original `_pull_0`, `_pull_1`, and `_pull_2` textures:

- Iron
- Copper
- Gold
- Bronze
- Alloy Steel
- Valyrian
- Joffrey Baratheon

The final pull frame is also used for the charged visual because the legacy set contains no separate charged texture.

## Integrity audit

`migration/audit-texture-resources.py` checks:

- every GOT model texture reference resolves exactly and case-sensitively;
- no GOT model parent is missing;
- animated sprite strips have `.mcmeta` metadata;
- required restored utility textures exist;
- cucumber/pipeweed retain multiple growth visuals;
- all seven Crossbows expose their pull textures/overrides.

At packaging time the audit reports `PASS`.

A direct legacy comparison also found 795 modern `textures/item` resources with direct 1.7.10 counterparts; all 795 are pixel-identical after the fixes.
