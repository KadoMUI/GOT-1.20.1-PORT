package got.client.claim;

import got.claim.GOTBannerClaimSnapshot;
import got.client.gui.GOTGuiBannerClaim;
import net.minecraft.client.Minecraft;

/** Applies authoritative claim snapshots and keeps an open editor synchronized. */
public final class GOTClientBannerClaimState {
    private GOTClientBannerClaimState() {}

    public static void accept(GOTBannerClaimSnapshot snapshot, boolean openGui) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen instanceof GOTGuiBannerClaim screen
                && screen.entityUuid().equals(snapshot.entityUuid())) {
            screen.accept(snapshot);
        } else if (openGui) {
            minecraft.setScreen(new GOTGuiBannerClaim(snapshot));
        }
    }
}
