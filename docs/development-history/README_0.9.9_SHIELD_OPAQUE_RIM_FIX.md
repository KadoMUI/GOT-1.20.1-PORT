# Project Thrones 0.9.9 — Shield Opaque Rim Fix

The two separated front/back planes and silhouette-derived edge geometry remain.

Root cause of the still-visible gap:
The previous extrusion pass textured the rim using UVs from the far-left edge of
the 64x32 legacy texture. Many shield sheets have transparent padding there.
With entityCutoutNoCull, the side geometry therefore rendered fully transparent.

This pass:
- scans the outward/right-hand 32x32 shield half;
- finds the first guaranteed opaque texel;
- stores that texel's center UV in the cached outline data;
- textures every silhouette side quad with that opaque sample.

This makes the already-generated connector geometry actually visible without
changing the known-good shield pose, blocking models, front/back separation, or
texture-facing assignment.
