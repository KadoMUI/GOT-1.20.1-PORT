# Special Mobs Pass 3 — Ghost Wraiths

Restores the non-White-Walker spectral undead from the final 1.7.10 JAR.

## Barrow Wraith
- 20 HP, 5 melee damage, 0.22 movement speed.
- Undead hostile mob.
- Successful melee hits apply Slowness, Mining Fatigue, and Weakness for the legacy difficulty-scaled duration.
- Uses `textures/entity/westeros/shadow.png`.
- Natural spawning restricted to `north_barrows`.

## Marsh Wraith
- 20 HP, 0.22 movement speed.
- Ranged spectral hostile mob.
- Fires every ~40 ticks at up to 12 blocks.
- Reuses the legacy Mossovy Marsh Wraith texture.
- Natural spawning in Mossovy/Essos marshes.

## Marsh Wraith Ball
- Dedicated projectile entity.
- No gravity, ~0.5 block/tick launch speed, 200 tick lifetime.
- 5 direct damage and 5 seconds Slowness on its intended target.
- Reuses `marshWraith_ball.png`.

White Walker Wights are intentionally NOT part of this pass.
