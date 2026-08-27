# Project Thrones — Mounts Pass 2: Legacy Models & Textures

Reconstructed the legacy mount presentation from GOT 24.08.29.

- Restored original entity textures for zebra, boar, camel, rhino and wooly rhino.
- Restored the original saddle/carpet texture assets for the next equipment-layer pass.
- Reconstructed the 1.7.10 box geometry for Rhino/Wooly Rhino, Camel, and Boar, including rhino horns and boar tusks.
- Replaced the temporary HorseRenderer paths for Rhino, Wooly Rhino, Camel and Boar with dedicated legacy-model renderers.
- Kept the GOT horse on the vanilla horse renderer because the legacy GOTRenderHorse itself used ModelHorse rather than a custom GOT model.
- Zebra likewise has no custom legacy model; the original used horse geometry with a fixed zebra skin. Its fixed-skin renderer is the remaining presentation hook to complete alongside saddle/equipment layers.

The original mount saddle/carpet images are now in the modern resource tree, but equipment overlays are intentionally left for the mount-equipment pass rather than baked into the base animal skin.
