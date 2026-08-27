# Project Thrones 1.0.0 — Regional Sword Recipe Collision Fix

Adds a unique mixed-material recipe for the regional swords:

    Alloy Steel Ingot
    Iron Ingot
    Stick

On Westeros faction tables this produces `got:westeros_sword`.
On Essos faction tables this produces `got:essos_sword`.

The existing pure-Iron recipe remains unchanged:
- Westeros tables -> Iron Sword
- Essos tables -> Iron Scimitar

This prevents the regional sword family from overlapping/shadowing the GOT Iron family.
