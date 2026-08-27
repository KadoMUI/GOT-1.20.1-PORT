# Project Thrones 0.9.9 — Shield Pose Fix 4

The screenshots from Fix 3 showed the important remaining failure: when the
player changed arm pose, the shield orbited far above/below the hand.  This was
caused by using vanilla shield JSON translations on a legacy flat shield mesh
that does not have vanilla ShieldModel's part/handle hierarchy.

Fix 4:
- Explicitly returns HumanoidModel.ArmPose.BLOCK while a GOT shield is actively used.
- Keeps the shield's third-person pivot at the hand/forearm instead of 10 model
  units away from it.
- Retains the shield-plane rotations and actual ShieldItem/BLOCK mechanics.
- Leaves first-person transforms and shield textures unchanged.
- Includes Bed UV Fix 2 from the previous package.

Expected third-person behavior:
- idle: shield remains attached immediately beside the hand/forearm;
- blocking: shield follows the BLOCK arm up in front of the torso instead of
  orbiting over the head or down near the ground.
