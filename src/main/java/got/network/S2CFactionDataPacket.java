package got.network;

import got.client.faction.GOTClientFactionState;
import got.faction.GOTFaction;
import got.faction.GOTFactionPlayerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public record S2CFactionDataPacket(Map<GOTFaction, Float> alignments,
                                   GOTFaction membership,
                                   GOTFaction brokenFaction,
                                   int pledgeBreakCooldown,
                                   int pledgeBreakCooldownStart,
                                   Map<GOTFaction, GOTFactionPlayerData.FactionStats> stats) {
    public S2CFactionDataPacket(GOTFactionPlayerData.Snapshot snapshot) {
        this(snapshot.alignments(), snapshot.membership(), snapshot.brokenFaction(),
                snapshot.pledgeBreakCooldown(), snapshot.pledgeBreakCooldownStart(), snapshot.stats());
    }

    public static void encode(S2CFactionDataPacket message, FriendlyByteBuf buffer) {
        buffer.writeVarInt(GOTFaction.playableFactions().size());
        for (GOTFaction faction : GOTFaction.playableFactions()) {
            buffer.writeEnum(faction);
            buffer.writeFloat(message.alignments.getOrDefault(faction, 0.0F));
            GOTFactionPlayerData.FactionStats stats = message.stats.getOrDefault(faction,
                    new GOTFactionPlayerData.FactionStats(0, 0));
            buffer.writeVarInt(stats.npcKills());
            buffer.writeVarInt(stats.enemyKills());
        }
        buffer.writeEnum(message.membership);
        buffer.writeEnum(message.brokenFaction);
        buffer.writeVarInt(message.pledgeBreakCooldown);
        buffer.writeVarInt(message.pledgeBreakCooldownStart);
    }

    public static S2CFactionDataPacket decode(FriendlyByteBuf buffer) {
        Map<GOTFaction, Float> alignments = new EnumMap<>(GOTFaction.class);
        Map<GOTFaction, GOTFactionPlayerData.FactionStats> stats = new EnumMap<>(GOTFaction.class);
        int count = buffer.readVarInt();
        for (int index = 0; index < count; index++) {
            GOTFaction faction = buffer.readEnum(GOTFaction.class);
            alignments.put(faction, buffer.readFloat());
            stats.put(faction, new GOTFactionPlayerData.FactionStats(buffer.readVarInt(), buffer.readVarInt()));
        }
        return new S2CFactionDataPacket(Map.copyOf(alignments), buffer.readEnum(GOTFaction.class),
                buffer.readEnum(GOTFaction.class), buffer.readVarInt(), buffer.readVarInt(), Map.copyOf(stats));
    }

    public static void handle(S2CFactionDataPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> GOTClientFactionState.accept(message)));
        context.setPacketHandled(true);
    }
}
