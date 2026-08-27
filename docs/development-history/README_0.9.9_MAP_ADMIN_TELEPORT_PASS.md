# Project Thrones 0.9.9 — Map Admin Teleport Pass

Adds the legacy-style admin map teleport shortcut.

## Behavior
- Open the GOT map.
- Move the cursor anywhere inside the map viewport.
- Press **F**.
- The cursor's atlas position is converted to Planetos world X/Z.
- The server resolves a safe terrain Y using MOTION_BLOCKING_NO_LEAVES.
- The player is teleported to that position in Planetos.

## Permission
The server accepts the packet only when `ServerPlayer.hasPermissions(2)` is true.

This covers:
- singleplayer worlds with cheats enabled;
- dedicated/LAN server operators with command permission level 2+.

A non-OP client cannot bypass this by crafting the packet manually.

## Safety
- Destination must be inside the Planetos world border.
- Y is clamped to the dimension build range.
- Riding is stopped before teleport.
- Fall distance is reset.

## Networking
Protocol bumped from 19 -> 20 because a new C2S packet was added.
