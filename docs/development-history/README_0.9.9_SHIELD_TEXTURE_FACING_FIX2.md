# Project Thrones 0.9.9 — Shield Texture Facing Fix 2

The reference-derived pose/blocking system was already correct. The prior facing
fix broke it by reversing the shield plate geometry/winding.

This pass restores the exact working geometry from the reference rebuild and
changes only texture assignment:

- outward/front physical face uses the opposite 32px half of the legacy 64x32 sheet;
- inward/back physical face uses the other half;
- no idle/blocking JSON transforms changed;
- no blocking predicate changed;
- no plate dimensions or winding changed from the working reference build.

The ResourceLocation lowercase crash fixes from the latest source are retained.
