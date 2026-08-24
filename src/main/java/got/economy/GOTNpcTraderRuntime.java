package got.economy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

/**
 * Persistent per-NPC economy state.
 *
 * This is the live counterpart of the recovered GOTTraderInfo defaults:
 * 200 recent value locks an overused entry, recent value decays every 60 ticks,
 * 5000 total value causes a full offer refresh, and refreshed offers remain
 * unavailable for 6000 ticks.
 */
public final class GOTNpcTraderRuntime {
    private static final String ROOT = "GOTEconomy";
    private static final int LOCK_AT_VALUE = 200;
    private static final int DECAY_EVERY = 60;
    private static final int REFRESH_AT_VALUE = 5000;
    private static final int REFRESH_LOCK_TICKS = 6000;

    private GOTNpcTraderRuntime() {}

    private static CompoundTag data(Entity npc) {
        CompoundTag persistent = npc.getPersistentData();
        if (!persistent.contains(ROOT)) persistent.put(ROOT, new CompoundTag());
        return persistent.getCompound(ROOT);
    }

    private static void save(Entity npc, CompoundTag tag) {
        npc.getPersistentData().put(ROOT, tag);
    }

    public static void onTrade(Entity npc, MerchantOffers offers, MerchantOffer traded) {
        CompoundTag tag = data(npc);
        int index = offers.indexOf(traded);
        if (index < 0) return;

        int value = Math.max(
                GOTCoinValueService.valueOf(traded.getBaseCostA())
                        + GOTCoinValueService.valueOf(traded.getCostB()),
                GOTCoinValueService.valueOf(traded.getResult())
        );

        String key = "Recent_" + index;
        int recent = tag.getInt(key) + Math.max(1, value);
        tag.putInt(key, recent);
        tag.putInt("ValueSinceRefresh", tag.getInt("ValueSinceRefresh") + Math.max(1, value));

        if (recent >= LOCK_AT_VALUE) traded.setToOutOfStock();
        save(npc, tag);
    }

    /**
     * @return true when the caller should discard its current offers and build
     * a newly randomized set.
     */
    public static boolean tick(Entity npc, MerchantOffers offers) {
        CompoundTag tag = data(npc);

        int lock = tag.getInt("RefreshLock");
        if (lock > 0) {
            tag.putInt("RefreshLock", lock - 1);
            for (MerchantOffer offer : offers) offer.setToOutOfStock();
        }

        if (npc.tickCount % DECAY_EVERY == 0) {
            for (int i = 0; i < offers.size(); i++) {
                String key = "Recent_" + i;
                int recent = Math.max(0, tag.getInt(key) - 1);
                tag.putInt(key, recent);
                if (lock <= 0 && recent < LOCK_AT_VALUE && offers.get(i).isOutOfStock()) {
                    offers.get(i).resetUses();
                }
            }
        }

        if (tag.getInt("ValueSinceRefresh") >= REFRESH_AT_VALUE) {
            CompoundTag fresh = new CompoundTag();
            fresh.putInt("RefreshLock", REFRESH_LOCK_TICKS);
            save(npc, fresh);
            return true;
        }

        save(npc, tag);
        return false;
    }

    public static int refreshLock(Entity npc) {
        return data(npc).getInt("RefreshLock");
    }
}
