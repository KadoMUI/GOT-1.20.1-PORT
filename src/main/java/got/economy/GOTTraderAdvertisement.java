package got.economy;

import got.speech.GOTSpeechBehaviorData;
import got.speech.GOTSpeechService;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.AABB;

import java.util.List;

/**
 * Recovered GOTTraderInfo advertisement behavior:
 * - trader must be alive and not fighting
 * - no trade for >600 ticks
 * - scan players in a 10-block expanded box
 * - ignore creative players
 * - 1-in-3 chance per eligible player
 * - after an advertisement, wait 20 * random(5..20) ticks
 */
public final class GOTTraderAdvertisement {
    private static final String ROOT = "GOTEconomy";
    private GOTTraderAdvertisement() {}

    public static void tick(PathfinderMob trader) {
        if (trader.level().isClientSide || !trader.isAlive() || trader.getTarget() != null) return;

        CompoundTag data = trader.getPersistentData().getCompound(ROOT);
        int ad = data.getInt("AdvertisementCooldown");
        if (ad > 0) {
            data.putInt("AdvertisementCooldown", ad - 1);
            trader.getPersistentData().put(ROOT, data);
            return;
        }

        int since = data.getInt("TimeSinceTrade") + 1;
        data.putInt("TimeSinceTrade", since);
        trader.getPersistentData().put(ROOT, data);
        if (since <= 600) return;

        List<ServerPlayer> players = trader.level().getEntitiesOfClass(
                ServerPlayer.class, trader.getBoundingBox().inflate(10.0D),
                p -> p.isAlive() && !p.getAbilities().instabuild);

        boolean spoke = false;
        for (ServerPlayer player : players) {
            if (trader.getRandom().nextInt(3) != 0) continue;
            if (!GOTSpeechBehaviorData.canSpeak(trader)) continue;
            GOTSpeechService.speakDefault(trader, player);
            spoke = true;
        }

        if (spoke) {
            data = trader.getPersistentData().getCompound(ROOT);
            data.putInt("AdvertisementCooldown", 20 * (5 + trader.getRandom().nextInt(16)));
            trader.getPersistentData().put(ROOT, data);
        }
    }

    public static void markTrade(PathfinderMob trader) {
        CompoundTag data = trader.getPersistentData().getCompound(ROOT);
        data.putInt("TimeSinceTrade", 0);
        trader.getPersistentData().put(ROOT, data);
    }
}
