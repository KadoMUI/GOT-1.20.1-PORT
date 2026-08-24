package got.npc.hiring;

import got.faction.GOTFaction;
import got.faction.GOTFactionPlayerData;
import net.minecraft.server.level.ServerPlayer;

/**
 * Direct bridge to the faction/alignment API that exists in got-1.20.1-0.6.0.
 */
public final class GOTHiringPlayerRules {
    private GOTHiringPlayerRules() {}

    public static float alignment(ServerPlayer player, String factionId) {
        return GOTFaction.byId(factionId)
            .map(faction -> GOTFactionPlayerData.get(player).alignment(faction))
            .orElse(0.0F);
    }

    public static boolean pledgedTo(ServerPlayer player, String factionId) {
        return GOTFaction.byId(factionId)
            .map(faction -> GOTFactionPlayerData.get(player).membership() == faction)
            .orElse(false);
    }
}
