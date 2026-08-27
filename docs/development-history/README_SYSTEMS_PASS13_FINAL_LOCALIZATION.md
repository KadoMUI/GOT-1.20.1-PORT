# Systems Pass 13 — Final Localization Cleanup

## GOT menu
- Removed the standalone Localization button from `GOTGuiMenu` permanently.
- Localization is now available from `GOT Menu -> Settings -> Localization...`.
- The Settings localization button opens Minecraft 1.20.1's native `LanguageSelectScreen`, so the selected language remains the same global Minecraft language used by GOT lore and all other translatable UI.

## Localization cleanup
- Added the outstanding faction membership/join/leave messages.
- Added banner-claim command and GUI strings; the banner-claim static audit now passes again.
- Added quest offer/progress/ready/abandon strings.
- Added hired-unit ownership/alignment requirement strings.
- Added faction page navigation and remaining hostile/unaligned subtitle fallbacks.
- Added explicit player-visible entity names for regional NPC families and utility projectiles that could otherwise expose registry IDs.
- Preserved the world preset label exactly as `PLANETOS`.

## Audit
`migration/audit-systems-pass13-localization.py` verifies:
- every literal/static `got.*` Component.translatable key resolves in `en_us.json`;
- the standalone Localization main-menu button is gone;
- Settings contains the native Minecraft language selector path;
- the Planetos world preset is exactly `PLANETOS`;
- core NPC/projectile/banner entity translation keys exist.

Result: PASS.

No Gradle build was attempted.
