# Project Thrones 0.9.9 — Shoreline Smoothing Pass 1

## Problem
Planetos terrain height already blended land/ocean approaches, but the final sea-fill decision still used the raw authored biome at the exact block coordinate. Atlas boundaries therefore survived as rectangular coastlines and 90-degree river/coast corners.

## Fix
- Added a radial weighted ocean/land kernel to `PlanetosTerrainSampler`.
- Added low-frequency seeded coastal distortion only inside the transition band.
- Deep inland and deep ocean remain unchanged.
- Terrain height and sea flooding now consume the same smoothed water-column decision.
- Smoothed-water columns use sand/gravel seabed tops even when the authored biome directly under the adjusted shoreline is technically a land biome.
- Water state is calculated once per X/Z column during chunk generation rather than once per vertical block.

## Expected result
- Coastlines round through atlas corners rather than preserving right angles.
- Riverbanks receive the same smoothing.
- Coastal elevation still ramps through the existing shelf/beach transition.
- Continental geography remains anchored to the authored Planetos map; this only modifies the local shore edge.

## Testing note
This is world generation. Existing generated chunks are not rewritten. Test in a new world or delete/regenerate the relevant region/chunk files before comparing coastlines.
