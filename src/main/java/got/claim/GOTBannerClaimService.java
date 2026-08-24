package got.claim;

import com.mojang.authlib.GameProfile;
import got.GOTAbstractBannerEntity;
import got.network.C2SBannerClaimActionPacket;
import got.network.GOTNetwork;
import got.network.S2CBannerClaimDataPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.Locale;

/** Server-authoritative validation and mutation boundary for the claim editor. */
public final class GOTBannerClaimService {
    private static final double EDIT_DISTANCE_SQUARED = 64.0D * 64.0D;

    private GOTBannerClaimService() {}

    public static void open(ServerPlayer player, GOTAbstractBannerEntity banner) {
        send(player, banner, true);
    }

    public static void send(ServerPlayer player, GOTAbstractBannerEntity banner, boolean openGui) {
        GOTNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                new S2CBannerClaimDataPacket(GOTBannerClaimSnapshot.from(banner, player), openGui));
    }

    public static void handle(ServerPlayer player, C2SBannerClaimActionPacket packet) {
        GOTAbstractBannerEntity banner = findEditable(player, packet.entityId());
        if (banner == null) return;
        GOTBannerClaim claim = banner.getClaim();
        if (claim.structureProtection()
                && packet.action() != C2SBannerClaimActionPacket.Action.REFRESH) {
            send(player, banner, false);
            return;
        }
        boolean changed = false;
        switch (packet.action()) {
            case REFRESH -> {
                send(player, banner, false);
                return;
            }
            case SET_MODE -> {
                claim.setPlayerSpecific(packet.number() != 0);
                changed = true;
            }
            case SET_SELF_PROTECTION -> {
                claim.setSelfProtection(packet.number() != 0);
                changed = true;
            }
            case SET_ALIGNMENT -> {
                claim.setAlignmentRequired(packet.decimal());
                changed = true;
            }
            case SET_DEFAULT_PERMISSIONS -> {
                claim.setDefaultPermissions(packet.number());
                changed = true;
            }
            case ADD_ENTRY -> changed = addEntry(player, claim, packet.text());
            case REMOVE_ENTRY -> {
                GOTBannerWhitelistEntry entry = claim.entry(packet.text());
                if (entry != null && !player.getUUID().equals(entry.id())) {
                    changed = claim.removeEntry(packet.text());
                }
            }
            case SET_ENTRY_PERMISSIONS -> {
                GOTBannerWhitelistEntry entry = claim.entry(packet.text());
                if (entry != null && !player.getUUID().equals(entry.id())) {
                    changed = claim.setEntryPermissions(packet.text(), packet.number());
                }
            }
        }
        if (changed) banner.claimChanged();
        send(player, banner, false);
    }

    @Nullable
    private static GOTAbstractBannerEntity findEditable(ServerPlayer player, int entityId) {
        Entity entity = player.level().getEntity(entityId);
        if (!(entity instanceof GOTAbstractBannerEntity banner)
                || !banner.isAlive()
                || player.distanceToSqr(banner) > EDIT_DISTANCE_SQUARED
                || !banner.canPlayerEditClaim(player)) {
            player.displayClientMessage(Component.translatable("got.gui.bannerEdit.noPermission"), true);
            return null;
        }
        return banner;
    }

    private static boolean addEntry(ServerPlayer player, GOTBannerClaim claim, String rawName) {
        String name = rawName == null ? "" : rawName.trim();
        if (name.isBlank() || name.length() > 64) return false;
        if (name.toLowerCase(Locale.ROOT).startsWith("f/")) {
            String groupName = name.substring(2).trim();
            if (groupName.isBlank()) return false;
            boolean added = claim.addEntry(GOTBannerWhitelistEntry.group(null, groupName,
                    GOTBannerPermission.FULL.bit()));
            if (added) player.displayClientMessage(Component.translatable(
                    "got.gui.bannerEdit.groupDeferred", groupName), false);
            return added;
        }

        MinecraftServer server = player.getServer();
        if (server == null) return false;
        ServerPlayer online = server.getPlayerList().getPlayerByName(name);
        GameProfile profile = online == null
                ? server.getProfileCache().get(name).orElse(null)
                : online.getGameProfile();
        if (profile == null || profile.getId() == null || profile.getName() == null) {
            player.displayClientMessage(Component.translatable(
                    "got.gui.bannerEdit.invalidUsername"), false);
            return false;
        }
        if (profile.getId().equals(player.getUUID())) return false;
        return claim.addEntry(GOTBannerWhitelistEntry.player(profile.getId(),
                profile.getName(), GOTBannerPermission.FULL.bit()));
    }
}
