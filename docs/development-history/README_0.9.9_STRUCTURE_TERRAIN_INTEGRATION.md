# Project Thrones 0.9.9 — Structure Terrain Integration

This pass changes structure placement so structures are seated into Planetos terrain instead of being pasted as rigid floating/terrain-cutting boxes.

## Procedural / legacy-template buildings
- `NorthStructureBuilder.foundation(...)` now terraces the actual foundation footprint into the local surface.
- A 3-block smoothstep apron blends the terrace back into untouched terrain.
- Uphill terrain/vegetation is cut only when it is natural terrain; already-placed structural blocks are protected.
- Downhill gaps are filled naturally before the structure foundation is placed.
- Foundation support can extend as far as 24 blocks downward instead of stopping after 8.

## Reusable authored `.schem` templates
- The loader detects the schematic's dominant ground layer.
- The template footprint is fitted to that ground layer before paste.
- Existing WorldEdit offsets are respected.

## Bespoke / waypoint schematics
- The loader analyzes each schematic and detects its dominant ground/foundation layer plus a ground-contact mask.
- Vertical placement uses the median natural terrain height under actual ground-contact columns, instead of only sampling the waypoint center.
- Only columns within 6 blocks of the detected footprint are terraformed.
- Terrain is feathered toward the structure rather than cut vertically.
- Open-water columns are not filled, preserving harbors, bridges, docks, and river crossings.
- Banner-entity restoration from the previous pass remains intact.

## Testing
This changes world generation and therefore requires newly generated chunks / a fresh Planetos test world. Existing generated structures and terrain will not be retrofitted.
