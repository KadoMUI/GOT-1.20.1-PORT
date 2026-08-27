# Project Thrones 0.9.9 — Shield Silhouette Extrusion

This replaces both unsuccessful thickness approaches.

## Why the previous fixes failed
- Single-plane front/back: caused Z-fighting/flickering.
- Rectangular rim: legacy shield PNGs are alpha-cut silhouettes, so the visible
  shield does not reach the rectangular texture bounds. The rim existed, but
  mostly around transparent pixels and therefore appeared to do nothing.

## This implementation
- Restores two physically separated front/back planes.
- Keeps all known-good Epic Knights-derived idle/blocking transforms.
- Keeps the corrected front/back texture-half assignment.
- Reads each shield's 64x32 PNG alpha channel once and caches its outline.
- Uses the visible heraldic/right-hand 32x32 half as the silhouette.
- For every opaque pixel boundary adjacent to transparency, renders a thin quad
  connecting zFront to zBack.
- The resulting side geometry follows the exact pixel-art outline of every GOT
  shield, including pointed, round, heater, oval, and irregular silhouettes.
- No shield PNG needs to be modified.

If a resource cannot be read, the renderer safely falls back to the two main
faces instead of crashing.
