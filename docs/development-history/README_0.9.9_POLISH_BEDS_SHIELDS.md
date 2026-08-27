# Project Thrones 0.9.9 — Beds/Shields Polish Fix

## Beds
- Corrected top-face UV rotation for Fur Bed, Lion Fur Bed, and Straw Bed.
- Legacy textures are unchanged.
- Both foot/head block models rotate the top UV so the pillow crosses the head of the bed instead of running lengthwise.

## Shields
- GOTFactionShieldItem remains a real ShieldItem with BLOCK use animation.
- Replaced generic/custom hand transforms with vanilla shield-style transforms for first person, third person, GUI, fixed, and ground contexts.
- Removed negative-Y scales that vertically mirrored/upended the shield art.
- Left-half legacy texture remains the outward/front face; right-half remains the inside/back face.
