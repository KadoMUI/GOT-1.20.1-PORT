# Capes Pass 1 — Legacy Cape System

Reconstructed from `GOTCapes`, `GOTGuiCapes`, and `GOTRenderCape` in Game of Thrones 24.08.29.

## Legacy requirements retained
All 31 legacy alignment capes retain the original requirement: **+100 alignment with the cape's associated faction**.
This includes alternate capes that share a faction requirement:
- Northguard -> North
- Arrynguard -> Arryn
- Kingsguard 1/2 -> Crownlands
- Unsullied -> Ghiscar
- Yi Ti Bombardier/Samurai -> Yi Ti

## Targaryen change
The old build made Targaryen a developer-exclusive cape. Project Thrones replaces that requirement with:
- +500 Dothraki
- +500 Ghiscar (the modern faction representing the Unsullied/Slaver's Bay power base)
- +500 Dragonstone

All three must be satisfied simultaneously.

## System
- GOT Menu now contains Capes.
- Selection/removal is server-authoritative.
- Selected cape persists per player through death/save.
- Unlocks are evaluated server-side from live alignment.
- Cape selection is synchronized to clients for multiplayer rendering.
- Player renderer gets a cloth cape layer with movement/crouch sway.
- Existing legacy cape textures are reused.
- Network protocol 15 -> 16.
