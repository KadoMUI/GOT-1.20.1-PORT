# Project Thrones 0.9.9 — Shield Pose Fix

This pass corrects the remaining GOT faction shield attachment issue shown in the 2026-08-26 screenshots.

- The item already uses `ShieldItem` and `UseAnim.BLOCK`; the blocking arm animation was not the problem.
- The custom flat legacy shield renderer was incorrectly using vanilla ShieldModel display offsets.
- Third-person X/Y offsets are now centered on the hand/forearm instead of displaced ~10 model pixels away.
- First-person offsets were adjusted for the same custom-mesh origin mismatch.
- Physical shield proportions are now taller/narrower rather than a square 1x1 card.
- Left-half texture remains the outward/front heraldry.
- No PNG textures were edited.
