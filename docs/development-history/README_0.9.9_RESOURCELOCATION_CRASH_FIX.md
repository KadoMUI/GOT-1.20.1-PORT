# Project Thrones 0.9.9 — ResourceLocation Crash Fix

The older crash was caused by an uppercase character in a Minecraft 1.20.1
ResourceLocation:

    got:textures/map/mapScreen.png

Minecraft 1.20.1 only accepts lowercase characters in ResourceLocation paths.

The current source still contained that exact path, plus several other client
texture literals with the same hazard. This pass lowercases both the Java
references and their matching resource filenames/directories for:

- mapScreen.png -> mapscreen.png
- mapOverlay.png -> mapoverlay.png
- questBook.png -> questbook.png
- hiredFarmer.png -> hiredfarmer.png
- hiredWarrior.png -> hiredwarrior.png
- squadronItem.png -> squadronitem.png
- marshWraith.png -> marshwraith.png
- marshWraith_ball.png -> marshwraith_ball.png
- whiteOryx/ -> whiteoryx/

A post-patch scan found no remaining uppercase literal paths in direct
ResourceLocation constructor/fromNamespaceAndPath calls.
