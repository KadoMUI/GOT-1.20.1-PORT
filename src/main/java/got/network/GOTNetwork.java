package got.network;

import got.GOTMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class GOTNetwork {
    private static final String PROTOCOL = "11";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(GOTMod.MOD_ID, "main"),
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals
    );

    private GOTNetwork() {}

    public static void register() {
        int id = 0;
        CHANNEL.registerMessage(id++, C2SRequestOptionsPacket.class,
                C2SRequestOptionsPacket::encode, C2SRequestOptionsPacket::decode, C2SRequestOptionsPacket::handle);
        CHANNEL.registerMessage(id++, C2SToggleOptionPacket.class,
                C2SToggleOptionPacket::encode, C2SToggleOptionPacket::decode, C2SToggleOptionPacket::handle);
        CHANNEL.registerMessage(id++, S2COptionsPacket.class,
                S2COptionsPacket::encode, S2COptionsPacket::decode, S2COptionsPacket::handle);
        CHANNEL.registerMessage(id++, C2SRequestTitlesPacket.class,
                C2SRequestTitlesPacket::encode, C2SRequestTitlesPacket::decode, C2SRequestTitlesPacket::handle);
        CHANNEL.registerMessage(id++, C2SSelectTitlePacket.class,
                C2SSelectTitlePacket::encode, C2SSelectTitlePacket::decode, C2SSelectTitlePacket::handle);
        CHANNEL.registerMessage(id++, S2CTitlesPacket.class,
                S2CTitlesPacket::encode, S2CTitlesPacket::decode, S2CTitlesPacket::handle);
        CHANNEL.registerMessage(id++, C2SEngraveOwnerPacket.class,
                C2SEngraveOwnerPacket::encode,
                C2SEngraveOwnerPacket::decode,
                C2SEngraveOwnerPacket::handle);
        CHANNEL.registerMessage(id++, C2SRenameSmithingItemPacket.class,
                C2SRenameSmithingItemPacket::encode,
                C2SRenameSmithingItemPacket::decode,
                C2SRenameSmithingItemPacket::handle);
        CHANNEL.registerMessage(id++, C2SReforgeItemPacket.class,
                C2SReforgeItemPacket::encode,
                C2SReforgeItemPacket::decode,
                C2SReforgeItemPacket::handle);
        CHANNEL.registerMessage(id++, C2SFastTravelPacket.class,
                C2SFastTravelPacket::encode,
                C2SFastTravelPacket::decode,
                C2SFastTravelPacket::handle);
        CHANNEL.registerMessage(id++, S2CFactionDataPacket.class,
                S2CFactionDataPacket::encode,
                S2CFactionDataPacket::decode,
                S2CFactionDataPacket::handle);
        CHANNEL.registerMessage(id++, C2SRequestFactionDataPacket.class,
                C2SRequestFactionDataPacket::encode,
                C2SRequestFactionDataPacket::decode,
                C2SRequestFactionDataPacket::handle);
        CHANNEL.registerMessage(id++, C2SFactionMembershipPacket.class,
                C2SFactionMembershipPacket::encode,
                C2SFactionMembershipPacket::decode,
                C2SFactionMembershipPacket::handle);
        CHANNEL.registerMessage(id++, S2CQuestDataPacket.class,
                S2CQuestDataPacket::encode,
                S2CQuestDataPacket::decode,
                S2CQuestDataPacket::handle);
        CHANNEL.registerMessage(id++, S2CQuestOfferPacket.class,
                S2CQuestOfferPacket::encode,
                S2CQuestOfferPacket::decode,
                S2CQuestOfferPacket::handle);
        CHANNEL.registerMessage(id++, C2SRequestQuestDataPacket.class,
                C2SRequestQuestDataPacket::encode,
                C2SRequestQuestDataPacket::decode,
                C2SRequestQuestDataPacket::handle);
        CHANNEL.registerMessage(id++, C2SQuestActionPacket.class,
                C2SQuestActionPacket::encode,
                C2SQuestActionPacket::decode,
                C2SQuestActionPacket::handle);
        CHANNEL.registerMessage(id++, S2CBannerClaimDataPacket.class,
                S2CBannerClaimDataPacket::encode,
                S2CBannerClaimDataPacket::decode,
                S2CBannerClaimDataPacket::handle);
        CHANNEL.registerMessage(id++, C2SBannerClaimActionPacket.class,
                C2SBannerClaimActionPacket::encode,
                C2SBannerClaimActionPacket::decode,
                C2SBannerClaimActionPacket::handle);
        CHANNEL.registerMessage(id++, S2CPactDataPacket.class,
                S2CPactDataPacket::encode,
                S2CPactDataPacket::decode,
                S2CPactDataPacket::handle);
        CHANNEL.registerMessage(id++, C2SRequestPactDataPacket.class,
                C2SRequestPactDataPacket::encode,
                C2SRequestPactDataPacket::decode,
                C2SRequestPactDataPacket::handle);
        CHANNEL.registerMessage(id++, C2SPactActionPacket.class,
                C2SPactActionPacket::encode,
                C2SPactActionPacket::decode,
                C2SPactActionPacket::handle);
        CHANNEL.registerMessage(id++, got.network.hiring.C2SRequestHiredNpcGuiPacket.class,
                got.network.hiring.C2SRequestHiredNpcGuiPacket::encode,
                got.network.hiring.C2SRequestHiredNpcGuiPacket::decode,
                got.network.hiring.C2SRequestHiredNpcGuiPacket::handle);
        CHANNEL.registerMessage(id++, got.network.hiring.S2CHiredNpcGuiPacket.class,
                got.network.hiring.S2CHiredNpcGuiPacket::encode,
                got.network.hiring.S2CHiredNpcGuiPacket::decode,
                got.network.hiring.S2CHiredNpcGuiPacket::handle);
        CHANNEL.registerMessage(id++, got.network.hiring.C2SHiredNpcActionPacket.class,
                got.network.hiring.C2SHiredNpcActionPacket::encode,
                got.network.hiring.C2SHiredNpcActionPacket::decode,
                got.network.hiring.C2SHiredNpcActionPacket::handle);
        CHANNEL.registerMessage(id++, got.network.hiring.C2SSetCommandHornModePacket.class,
                got.network.hiring.C2SSetCommandHornModePacket::encode,
                got.network.hiring.C2SSetCommandHornModePacket::decode,
                got.network.hiring.C2SSetCommandHornModePacket::handle);
        CHANNEL.registerMessage(id++, got.network.hiring.C2SSquadronActionPacket.class,
                got.network.hiring.C2SSquadronActionPacket::encode,
                got.network.hiring.C2SSquadronActionPacket::decode,
                got.network.hiring.C2SSquadronActionPacket::handle);
        CHANNEL.registerMessage(id++, got.network.hiring.C2SCopySquadronToCommandToolPacket.class,
                got.network.hiring.C2SCopySquadronToCommandToolPacket::encode,
                got.network.hiring.C2SCopySquadronToCommandToolPacket::decode,
                got.network.hiring.C2SCopySquadronToCommandToolPacket::handle);
        CHANNEL.registerMessage(id++, C2SRequestAchievementDataPacket.class,
                C2SRequestAchievementDataPacket::encode,
                C2SRequestAchievementDataPacket::decode,
                C2SRequestAchievementDataPacket::handle);
        CHANNEL.registerMessage(id++, S2CAchievementDataPacket.class,
                S2CAchievementDataPacket::encode,
                S2CAchievementDataPacket::decode,
                S2CAchievementDataPacket::handle);
        CHANNEL.registerMessage(id++, S2CNpcSpeechPacket.class,
                S2CNpcSpeechPacket::encode,
                S2CNpcSpeechPacket::decode,
                S2CNpcSpeechPacket::handle);
    }
}
