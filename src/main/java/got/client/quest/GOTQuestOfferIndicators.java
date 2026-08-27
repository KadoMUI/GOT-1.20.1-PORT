package got.client.quest;

import java.util.HashMap;
import java.util.Map;

/** Short-lived client cache of server-authoritative quest-offer markers. */
public final class GOTQuestOfferIndicators {
    private static final long TTL_MS = 2500L;
    private static final Map<Integer, Entry> STATES = new HashMap<>();

    private GOTQuestOfferIndicators() {}

    public static void update(int entityId, boolean offering, int color) {
        if (!offering) {
            STATES.remove(entityId);
            return;
        }
        STATES.put(entityId, new Entry(color, System.currentTimeMillis() + TTL_MS));
    }

    public static int color(int entityId) {
        Entry entry = STATES.get(entityId);
        if (entry == null) return -1;
        if (entry.expiresAt < System.currentTimeMillis()) {
            STATES.remove(entityId);
            return -1;
        }
        return entry.color;
    }

    public static void clear() { STATES.clear(); }

    private record Entry(int color, long expiresAt) {}
}
