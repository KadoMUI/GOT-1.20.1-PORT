# Project Thrones — Shields Pass 3: Held Pose + Texture Orientation Fix

## Fixes
- GOT faction shields remain `ShieldItem` instances and now explicitly advertise/use the vanilla `UseAnim.BLOCK` contract.
- Right-click starts using the shield for the full vanilla shield duration, allowing Minecraft's first/third-person blocking arm poses to drive presentation.
- Removed the custom renderer's old +/-90 degree third-person Y rotations that made shields look like generic handheld items / edge-on props.
- Reduced first-person custom yaw to a small presentation correction so Minecraft's own shield blocking transform remains authoritative.
- Corrected legacy 64x32 shield-sheet orientation:
  - RIGHT half = outward/front heraldic face.
  - LEFT half = inside/back face.
- GUI/fixed/ground rendering remains supported by the existing BEWLR.

## Scope
Applies automatically to every `GOTFactionShieldItem`, including all regional, elite, achievement, Targaryen, Golden Company, and alcoholic shield variants.
