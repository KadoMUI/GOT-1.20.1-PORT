package got.pact;

import got.claim.GOTClaimGroupResolver;

/** Allows Banner Claims to whitelist a Pact as a group with no BannerMod dependency. */
public final class GOTPactClaimBridge {
    private GOTPactClaimBridge() {}
    public static void bootstrap() {
        GOTClaimGroupResolver.register((server, groupId, groupName, playerId) ->
            GOTPactService.byId(server, groupId).map(p -> p.contains(playerId)).orElse(false)
        );
    }
}
