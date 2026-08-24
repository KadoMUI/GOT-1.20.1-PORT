package got.faction;

import got.npc.GOTFactionNpc;
import got.world.GOTDimensions;
import got.world.biome.GOTBiomeMetadata;
import got.world.biome.PlanetosBiomeManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public final class GOTFactionControl {
    private GOTFactionControl() {}

    /**
     * Original alignment areas counted defined territory or a same-faction NPC
     * within 24 blocks. Biome ownership is the modern defined-territory source.
     */
    public static boolean isInInfluence(ServerPlayer player, GOTFaction faction) {
        boolean nearby = !player.level().getEntitiesOfClass(
                LivingEntity.class,
                player.getBoundingBox().inflate(24.0D),
                entity -> entity instanceof GOTFactionNpc npc && npc.getFaction() == faction
        ).isEmpty();
        if (nearby) return true;
        if (!player.level().dimension().equals(GOTDimensions.PLANETOS)) return false;
        GOTBiomeMetadata metadata = PlanetosBiomeManager.getMetadata(player.blockPosition().getX(), player.blockPosition().getZ());
        return metadata != null && GOTFaction.fromBiomeId(metadata.id()) == faction;
    }
}
