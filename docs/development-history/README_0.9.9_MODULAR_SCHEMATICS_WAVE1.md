# Project Thrones 0.9.9 — Modular Schematics Wave 1

Implements the first normal settlement-module wave as sparse waypoint satellites.

Generation policy:
- NOT generic world scatter.
- Only ~1/3 of eligible waypoints receive a modular satellite cluster.
- A selected waypoint rolls exactly 1-4 houses.
- At most 1 smithy (40% roll).
- At most 1 store (40% roll).
- Buildings generate 192-360 blocks from their home waypoint.
- A 160-block no-build zone protects the waypoint itself / bespoke location.
- Candidates are rejected if within 128 blocks of any other waypoint.
- Every module reserves an 84x84 padded footprint.
- Reservations are global/deterministic across waypoint clusters, preventing
  modular structures from colliding with each other.
- A module gets 18 placement attempts. If none are valid, it is skipped rather
  than forced into another building.

Enabled first-wave normal modules:
North, Riverlands, Vale/Arryn, Crownlands, Westerlands, Reach, Dorne, Ironborn.

Not enabled as normal settlement modules:
HBW, DragonstoneDance, RuinsLarge/Small, Dothraki tents/Khal tent, Wildling_House.
Those are specialty structures and should receive their own generation rules.
DragonstoneStore and StormlandsSmithy are bundled but held until those regions
have a house module, preventing shop-only/smithy-only satellite settlements.
