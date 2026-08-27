# Project Thrones 0.9.9 — Bed UV Fix 2

The prior bed polish pass rotated legacy GOT bed top textures 270 degrees.
That made the pillow perpendicular to the bed length, but on the wrong end of
the head half.

This pass changes the top/bottom UV rotation to 90 degrees for:
- Fur Bed
- Lion Fur Bed
- Straw Bed

The original PNG textures are unchanged. The right-half pillow region of each
legacy head texture now rotates onto the outer/head end of the bed.
