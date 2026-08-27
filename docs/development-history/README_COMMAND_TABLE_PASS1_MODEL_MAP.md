# Command Table Pass 1 — Model + Map Interaction

Scope intentionally stops before the planned post-1.0 Command Table/Conquest overhaul.

## Implemented
- Replaced the placeholder full cube with a custom table model: thick tabletop + four legs.
- Added matching non-full collision/selection shape and no-occlusion block properties.
- Right-click is server-authoritative and opens the existing `GOTGuiMap`, exactly the same map class used by GOT Menu -> Map.
- Closing the map returns to the GOT menu.
- No inventory/menu, conquest UI, squadron logic, hovering world-map renderer, or zoom-on-table behavior is added in this pass.
- Network protocol bumped 14 -> 15 for the map-open packet.

## Deliberately deferred
The legacy hovering 3D map, table zoom state, squadron interaction and new waypoint/regional Conquest system remain reserved for the later Command Table overhaul.
