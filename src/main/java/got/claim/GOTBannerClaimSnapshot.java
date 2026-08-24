package got.claim;

import got.GOTAbstractBannerEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Complete client-safe view of one banner claim editor session. */
public record GOTBannerClaimSnapshot(int entityId, UUID entityUuid, String bannerName,
                                     String factionId, String ownerName, boolean playerSpecific,
                                     boolean selfProtection, boolean structureProtection,
                                     float alignmentRequired, int range, int defaultPermissions,
                                     List<EntryView> entries, boolean editable) {
    public static GOTBannerClaimSnapshot from(GOTAbstractBannerEntity banner, ServerPlayer viewer) {
        List<EntryView> entries = banner.getClaim().entries().stream()
                .map(entry -> new EntryView(entry.stableKey(), entry.displayName(),
                        entry.kind(), entry.permissions(),
                        entry.kind() == GOTBannerWhitelistEntry.Kind.PLAYER
                                && viewer.getUUID().equals(entry.id())))
                .toList();
        return new GOTBannerClaimSnapshot(banner.getId(), banner.getUUID(),
                banner.getBannerType().name(), banner.getClaimFaction().id(),
                banner.getOwnerName(), banner.getClaim().playerSpecific(),
                banner.getClaim().selfProtection(), banner.getClaim().structureProtection(),
                banner.getClaim().alignmentRequired(), banner.getClaimRange(),
                banner.getClaim().defaultPermissionBits(), entries,
                banner.canPlayerEditClaim(viewer));
    }

    public static void encode(GOTBannerClaimSnapshot value, FriendlyByteBuf buffer) {
        buffer.writeVarInt(value.entityId);
        buffer.writeUUID(value.entityUuid);
        buffer.writeUtf(value.bannerName, 128);
        buffer.writeUtf(value.factionId, 64);
        buffer.writeUtf(value.ownerName, 64);
        buffer.writeBoolean(value.playerSpecific);
        buffer.writeBoolean(value.selfProtection);
        buffer.writeBoolean(value.structureProtection);
        buffer.writeFloat(value.alignmentRequired);
        buffer.writeVarInt(value.range);
        buffer.writeVarInt(value.defaultPermissions);
        buffer.writeVarInt(value.entries.size());
        for (EntryView entry : value.entries) entry.encode(buffer);
        buffer.writeBoolean(value.editable);
    }

    public static GOTBannerClaimSnapshot decode(FriendlyByteBuf buffer) {
        int entityId = buffer.readVarInt();
        UUID entityUuid = buffer.readUUID();
        String bannerName = buffer.readUtf(128);
        String factionId = buffer.readUtf(64);
        String ownerName = buffer.readUtf(64);
        boolean playerSpecific = buffer.readBoolean();
        boolean selfProtection = buffer.readBoolean();
        boolean structureProtection = buffer.readBoolean();
        float alignmentRequired = buffer.readFloat();
        int range = buffer.readVarInt();
        int defaultPermissions = buffer.readVarInt();
        int size = Math.min(GOTBannerClaim.MAX_ENTRIES, buffer.readVarInt());
        List<EntryView> entries = new ArrayList<>(size);
        for (int index = 0; index < size; index++) entries.add(EntryView.decode(buffer));
        boolean editable = buffer.readBoolean();
        return new GOTBannerClaimSnapshot(entityId, entityUuid, bannerName, factionId,
                ownerName, playerSpecific, selfProtection, structureProtection,
                alignmentRequired, range, defaultPermissions, List.copyOf(entries), editable);
    }

    public record EntryView(String key, String name, GOTBannerWhitelistEntry.Kind kind,
                            int permissions, boolean viewer) {
        private void encode(FriendlyByteBuf buffer) {
            buffer.writeUtf(key, 160);
            buffer.writeUtf(name, 80);
            buffer.writeEnum(kind);
            buffer.writeVarInt(permissions);
            buffer.writeBoolean(viewer);
        }

        private static EntryView decode(FriendlyByteBuf buffer) {
            return new EntryView(buffer.readUtf(160), buffer.readUtf(80),
                    buffer.readEnum(GOTBannerWhitelistEntry.Kind.class),
                    buffer.readVarInt(), buffer.readBoolean());
        }
    }
}
