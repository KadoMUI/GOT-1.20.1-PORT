# Wildlife Pass 3 - Build Fix 1

Fixes the seven compile errors reported by the 2026-08-25 clean build:

- Corrects `LanguageSelectScreen` import for Minecraft 1.20.1.
- Corrects the cape unlock alignment variable typo (`f` -> `faction`).
- Adds the missing `OverlayTexture` import to the cape layer.
- Adds a correctly typed `GOTJaqenHgharModel` and uses it in Jaqen's renderer, resolving both humanoid generic errors.
- Makes defensive wildlife `anger(...)` protected so adult herd members can be invoked safely through the captured entity type.

The `ResourceLocation(String,String)` messages in the supplied build log are deprecation warnings and are not compile failures.
