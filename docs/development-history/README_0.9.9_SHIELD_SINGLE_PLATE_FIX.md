# Project Thrones 0.9.9 — Shield Single-Plate Fix

The previous rim pass did not visibly solve the issue because the legacy shield
textures are alpha-cut silhouettes inside a larger rectangular image. The rim
was connecting the rectangle edges, not the visible heraldic silhouette.

This pass changes the geometry model instead:

- front and back faces now occupy the exact same physical Z plane;
- front and back still use opposite winding, so each side shows the correct half
  of the legacy 64x32 texture;
- fake thickness/rim geometry is removed;
- idle/blocking model predicates and Epic Knights-derived transforms are unchanged;
- texture-facing fix is unchanged.

This treats the legacy GOT shield art as one double-sided physical plate, which
matches the actual 2D source asset and removes the visible gap between halves.
