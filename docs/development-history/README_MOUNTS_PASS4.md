# Project Thrones — Mounts Pass 4: Legacy Stats

Reconstructs legacy mount stat rules from GOT 1.7.10 bytecode.

## Base horse / zebra
- Health clamp: 12–48
- Jump clamp: 0.30–1.00
- Speed clamp: 0.08–0.45
- Zebra inherits the legacy GOT horse stat profile.

## Rhino / Wooly Rhino
- Health clamp: 20–50, then legacy spawn transform: x1.5 with minimum 40
- Speed clamp: 0.12–0.42, then x1.2
- Jump clamp: 0.20–0.80, then x0.5
- Attack damage: 4

## Camel
- Health clamp: 12–36
- Speed clamp: 0.10–0.35
- Jump clamp: 0.10–0.60, then x0.5

## Boar
- Health clamp: 10–30, then capped at 25 on spawn
- Speed clamp: 0.08–0.35
- Attack damage: 3

Species transformations run from finalizeSpawn so they are applied to newly spawned mounts and persist naturally through entity NBT rather than being multiplied again after reload.

Attack attributes are now registered for GOT mounts. The hostile mount combat AI itself is intentionally left for the behavior pass.

Build was attempted, but the Gradle wrapper cannot download Gradle 8.8 in this environment due unavailable outbound DNS/network access.
