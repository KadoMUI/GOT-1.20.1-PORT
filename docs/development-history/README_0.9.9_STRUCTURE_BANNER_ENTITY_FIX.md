# Project Thrones 0.9.9 — Structure Banner Entity Fix

GOT standing and wall banners are entities, not blocks. The schematic placers previously restored only blocks and block entities, so entity-backed GOT banners were silently omitted during structure worldgen.

This pass adds Sponge-v2 `Entities` support for GOT standing/wall banners to both:
- major waypoint schematic placement
- reusable/modular authored schematic placement

Banner type/NBT is restored, standing-banner yaw follows schematic rotation, and wall-banner direction/support anchor is reconstructed at the generated location.

## Important for existing `.schem` files
A schematic can only restore a GOT banner if the file actually contains entities. WorldEdit copies/saves often omit entities unless the selection is copied with entity inclusion (for example, `//copy -e` before saving, depending on the WorldEdit build).

Existing `.schem` files that were saved without entities contain no GOT banner data to recover. They do not need to be rebuilt: reopen/paste the source build, re-copy/save with entities included, and replace the schematic file.
