# Project Thrones — Mounts Pass 3: Equipment Layers

## Implemented
- Dedicated zebra renderer using the original GOT zebra texture while retaining vanilla horse geometry and its native saddle/horse-armor render layers.
- Legacy saddle render layers for rhino, wooly rhino, camel, and boar using the recovered original saddle textures.
- Saddles render conditionally from the actual mount saddle state; unsaddled mounts remain visually unsaddled.
- Camel carpet presentation restored as a conditional two-pass layer using the original `carpet_base.png` and `carpet_overlay.png` assets.
- Camel carpet color is persistent in NBT (`GOTCamelCarpet`) and supports the 16 vanilla dye colors.
- Equipment layers use separate baked layer definitions and slight shell separation to avoid z-fighting, mirroring the legacy renderer's inflated equipment-model approach.
- Existing Horse and Zebra mounts continue to use the modern vanilla horse equipment pipeline, including iron/gold/diamond and Project Thrones Valyrian horse armor.

## Legacy parity notes
The 1.7.10 render audit confirms:
- `GOTRenderRhino` used `rhino/saddle.png` as a separate render pass.
- `GOTRenderBoar` used `boar/saddle.png` as a separate render pass.
- `GOTRenderCamel` used a separate saddle model plus dynamically colored carpet generated from `carpet_base.png` + `carpet_overlay.png`.
- `GOTRenderZebra` was simply horse geometry with `animal/zebra.png`, so the 1.20.1 zebra deliberately retains the vanilla horse model/equipment layers.

## Still intentionally deferred
- Gameplay interaction for equipping/removing camel carpets.
- Species-specific stats and movement.
- Mounted combat/control AI.
- Mounted NPC hiring activation.

## Build note
A Gradle compile could not be executed in the sandbox because the wrapper attempts to download Gradle 8.8 and outbound DNS/network access is unavailable.
