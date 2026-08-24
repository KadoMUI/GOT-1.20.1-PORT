package got.calendar.season;

import got.calendar.GOTAegonCalendar;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Calendar hook for seasonal crop growth.
 *
 * IMPORTANT: no crops are restricted by default in Pass 3. That preserves 1.0 gameplay parity.
 * Farmer's Delight/Homeostatic and future Project Thrones crop seasons can register rules later.
 */
public final class GOTSeasonalGrowthRegistry {
    private static final Map<ResourceLocation, Rule> RULES = new HashMap<>();

    private GOTSeasonalGrowthRegistry() {}

    public static void register(ResourceLocation blockId, Set<GOTAegonCalendar.Season> growingSeasons) {
        register(blockId, growingSeasons, 1.0F, 0.0F);
    }

    public static void register(ResourceLocation blockId,
                                Set<GOTAegonCalendar.Season> growingSeasons,
                                float inSeasonMultiplier,
                                float outOfSeasonMultiplier) {
        EnumSet<GOTAegonCalendar.Season> seasons = growingSeasons.isEmpty()
                ? EnumSet.noneOf(GOTAegonCalendar.Season.class)
                : EnumSet.copyOf(growingSeasons);
        RULES.put(blockId, new Rule(Collections.unmodifiableSet(seasons),
                Math.max(0.0F, inSeasonMultiplier), Math.max(0.0F, outOfSeasonMultiplier)));
    }

    public static Rule get(ResourceLocation blockId) {
        return RULES.get(blockId);
    }

    public static boolean hasRule(ResourceLocation blockId) {
        return RULES.containsKey(blockId);
    }

    public record Rule(Set<GOTAegonCalendar.Season> growingSeasons,
                       float inSeasonMultiplier,
                       float outOfSeasonMultiplier) {
        public float multiplier(GOTAegonCalendar.Season season) {
            return growingSeasons.contains(season) ? inSeasonMultiplier : outOfSeasonMultiplier;
        }
    }
}
