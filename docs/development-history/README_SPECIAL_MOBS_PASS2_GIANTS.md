# Special Mobs Pass 2 — Giants / Wight Giants

Restores the legacy Giant combat family for 1.20.1.

- `got:giant`: 2.56 x 5.12 collision box, 100 HP, 7 melee damage, 24 follow range, 0.22 speed, giant knockback, 10-damage thrown rocks, two legacy giant skins, natural Beyond-the-Wall/Wildling-region spawning, legacy ten-fur death drop when `got:fur` is present.
- `got:wight_giant`: same giant chassis and rock attack, two legacy ice giant skins, darkness spawning, no loot, frost/slowness melee effect. Legacy 1.7.10's unusual 1 HP is preserved together with a modern White-Walker damage gate: fire and Valyrian/dragonglass/obsidian-named GOT weapons can damage it; ordinary damage is ignored.
- `got:thrown_rock`: real projectile restoring the Giant's ranged attack rather than faking ranged damage.
- Giant renderer scales the humanoid giant model to legacy-like proportions and uses the original resource textures.

Deferred to the broader Wight pass: conversion of slain victims into Wights and full shared White Walker damage/material service integration.
