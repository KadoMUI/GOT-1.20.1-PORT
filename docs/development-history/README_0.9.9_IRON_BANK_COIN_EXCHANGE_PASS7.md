# Project Thrones 0.9.9 — Iron Bank Coin Exchange Pass 7

- Reuses the existing `got:iron_bank` block as the craftable Coin Exchange.
- Right-clicking the placed block now opens the existing Coin Exchange GUI/menu.
- Block-backed access uses trader entity id `-1`, so it does not require an NPC.
- Replaces the old iron-ingot recipe with the requested furnace silhouette:
  - 8 Cobblestone around the outside
  - any GOT coin in the center
- Adds `got:coins` item tag containing every currently registered denomination.
- Does not create a duplicate Coin Exchange block or duplicate GUI.
