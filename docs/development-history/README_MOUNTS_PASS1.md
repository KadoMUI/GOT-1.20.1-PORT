# Project Thrones — Mounts Pass 1

This pass starts the dedicated mount restoration before the final NPC pass.

Implemented:
- GOTMountEntity modern base class
- persistent `GOTBelongsToNPC` ownership state
- helper for tame/saddled/persistent NPC mounts
- registered GOT horse, zebra, rhino, wooly rhino, camel, and boar entity types
- registered base horse attributes
- client renderer registration so the entities have a safe renderer path
- legacy mount/hiring audit documenting the 51 deferred mounted hires

Important: this is intentionally Mounts Pass 1, not a claim that mounts are finished. The 1.7.10 JAR confirms custom rendering/models and special mount behavior that still need restoration. The current hiring resolver also cannot safely expose mounted and unmounted variants of the same role yet.

Build could not be run here because the Gradle wrapper attempted to download Gradle 8.8 and outbound DNS is unavailable.
