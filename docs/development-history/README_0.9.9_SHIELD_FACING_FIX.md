# Project Thrones 0.9.9 — Shield Facing Fix

The reference-derived idle/blocking architecture is unchanged.

This pass only reverses the rendered plate facing:
- LEFT half of the legacy 64x32 texture remains the heraldic/front face.
- RIGHT half remains the inside/back face.
- Front now faces outward from the player's forearm.
- Back now faces inward toward the player.
- No item-model transforms, blocking predicates, or shield mechanics changed.
