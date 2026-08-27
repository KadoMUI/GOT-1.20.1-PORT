# Project Thrones 0.9.9 — Shield Rim Fix

The shield pose, blocking models, size, and front/back texture assignment are unchanged.

This patch adds the missing four edge faces between the front and back planes:
left, right, top, and bottom. The rim samples a narrow strip from the existing
inside-half texture, so no PNGs are changed.

Result: the shield renders as one thin solid object rather than two disconnected
planes floating a small distance apart.
