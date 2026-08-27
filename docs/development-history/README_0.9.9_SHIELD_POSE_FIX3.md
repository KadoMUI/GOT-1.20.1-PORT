# Project Thrones 0.9.9 — Shield Pose Fix 3

The previous pass was still wrong because the custom BEWLR was manually applying
hand transforms inside the renderer. Vanilla shields do not do that: their hand,
GUI, fixed, and ground transforms live in the item model JSON, while ShieldRenderer
only renders the shield model in shield model-space.

This pass:
- Adds the vanilla `minecraft:shield` display transform set to all 40 GOT shield item models.
- Removes all first/third-person transform logic from GOTFactionShieldItemRenderer.
- Uses the vanilla shield plate dimensions/origin.
- Uses the vanilla ShieldRenderer model-space Y/Z flip.
- Corrects legacy 64x32 front/back UV V orientation after that model-space flip.
- Leaves BLOCK use animation and ShieldItem mechanics untouched.

This is intended to make GOT shields inherit the same hand/forearm placement path
as vanilla shields instead of approximating it with custom translations.
