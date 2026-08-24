package got.world.structure.nightwatch;

import got.world.structure.GOTStructureSpawnerType;

import javax.annotation.Nullable;
import java.util.Arrays;

/** The complete Night's Watch subset of the original structure-spawner registry. */
public enum NightWatchStructureType implements GOTStructureSpawnerType {
    HOUSE_SMALL(11, "gift_house_small", "Night's Watch Shack", 9, false),
    HOUSE(12, "gift_house", "Night's Watch House", 11, false),
    STABLES(13, "gift_stables", "Night's Watch Stables", 13, false),
    SMITHY(14, "gift_smithy", "Night's Watch Smithy", 12, false),
    VILLAGE(308, "gift_settlement", "Night's Watch Village", 54, true);

    private final int legacyId;
    private final String serializedName;
    private final String displayName;
    private final int radius;
    private final boolean settlement;

    NightWatchStructureType(int legacyId, String serializedName, String displayName,
                            int radius, boolean settlement) {
        this.legacyId = legacyId;
        this.serializedName = serializedName;
        this.displayName = displayName;
        this.radius = radius;
        this.settlement = settlement;
    }

    @Override public int legacyId() { return legacyId; }
    @Override public String serializedName() { return serializedName; }
    @Override public String displayName() { return displayName; }
    @Override public int radius() { return radius; }
    @Override public boolean settlement() { return settlement; }

    @Nullable
    public static NightWatchStructureType findByLegacyId(int id) {
        return Arrays.stream(values()).filter(type -> type.legacyId == id).findFirst().orElse(null);
    }
}
