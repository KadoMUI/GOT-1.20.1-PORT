# Helmet Models Pass 1 — North + Reach

Reconstructed directly from `GOTModelNorthHelmet` and `GOTModelReachHelmet`
in Game of Thrones 24.08.29.

## North
Preserves the legacy 8x8 shell, longitudinal crown strips, front plate/nasal
details and rear plate details using the exact recovered ModelRenderer box
coordinates and texture offsets.

## Reach
Preserves the legacy helmet shell, tall central crest housing and three
zero-thickness horsehair/mane planes. The mane planes are slightly fanned to
reproduce the original silhouette.

Only `north_helmet` and `reach_helmet` are changed to custom armor-model items.
Their armor materials/stats, recipes, inventory textures and armor textures are
unchanged. NPCs wearing the registered items receive the same worn model
through Forge's humanoid armor-model hook.
