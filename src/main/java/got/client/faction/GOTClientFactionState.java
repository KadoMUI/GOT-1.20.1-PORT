package got.client.faction;

import got.faction.GOTFaction;
import got.faction.GOTFactionPlayerData;
import got.network.S2CFactionDataPacket;

import java.util.EnumMap;
import java.util.Map;

public final class GOTClientFactionState {
    private static final Map<GOTFaction, Float> ALIGNMENTS = new EnumMap<>(GOTFaction.class);
    private static final Map<GOTFaction, GOTFactionPlayerData.FactionStats> STATS = new EnumMap<>(GOTFaction.class);
    private static GOTFaction membership = GOTFaction.UNALIGNED;
    private static GOTFaction brokenFaction = GOTFaction.UNALIGNED;
    private static int pledgeBreakCooldown;
    private static int pledgeBreakCooldownStart;
    private static boolean initialized;

    private GOTClientFactionState() {}

    public static void accept(S2CFactionDataPacket packet) {
        ALIGNMENTS.clear();
        ALIGNMENTS.putAll(packet.alignments());
        STATS.clear();
        STATS.putAll(packet.stats());
        membership = packet.membership();
        brokenFaction = packet.brokenFaction();
        pledgeBreakCooldown = packet.pledgeBreakCooldown();
        pledgeBreakCooldownStart = packet.pledgeBreakCooldownStart();
        initialized = true;
    }

    public static void tick() {
        if (pledgeBreakCooldown > 0) pledgeBreakCooldown--;
    }

    public static void reset() {
        ALIGNMENTS.clear();
        STATS.clear();
        membership = GOTFaction.UNALIGNED;
        brokenFaction = GOTFaction.UNALIGNED;
        pledgeBreakCooldown = 0;
        pledgeBreakCooldownStart = 0;
        initialized = false;
    }

    public static boolean initialized() { return initialized; }
    public static float alignment(GOTFaction faction) { return ALIGNMENTS.getOrDefault(faction, 0.0F); }
    public static GOTFaction membership() { return membership; }
    public static GOTFaction brokenFaction() { return brokenFaction; }
    public static int pledgeBreakCooldown() { return pledgeBreakCooldown; }
    public static int pledgeBreakCooldownStart() { return pledgeBreakCooldownStart; }
    public static GOTFactionPlayerData.FactionStats stats(GOTFaction faction) {
        return STATS.getOrDefault(faction, new GOTFactionPlayerData.FactionStats(0, 0));
    }
}
