# Project Thrones 0.9.9 — Shield Silhouette Build Fix 1

Fixes the only compile error from the silhouette-extrusion pass.

Minecraft/Forge 1.20.1's mapped NativeImage API does not provide:
    NativeImage.getA(int)

The renderer now extracts the alpha byte directly from getPixelRGBA():

    ((pixel >>> 24) & 0xFF)

No shield geometry, transforms, blocking models, texture-facing logic, or
silhouette extrusion behavior was otherwise changed.
