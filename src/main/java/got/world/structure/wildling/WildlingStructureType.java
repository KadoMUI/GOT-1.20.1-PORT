package got.world.structure.wildling;

import got.world.structure.GOTStructureSpawnerType;

import javax.annotation.Nullable;
import java.util.Arrays;

/** Complete Free Folk/Thenn subset of the original structure-spawner registry. */
public enum WildlingStructureType implements GOTStructureSpawnerType {
    HOUSE(7, "wildling_house", "Wildling House", 10, false),
    CHIEFTAIN_HOUSE(8, "wildling_chieftain_house", "Wildling Chieftain House", 12, false),
    THENN_HOUSE(9, "thenn_house", "Thenn House", 10, false),
    THENN_CHIEFTAIN_HOUSE(10, "thenn_chieftain_house", "Thenn Chieftain House", 12, false),
    SETTLEMENT(306, "wildling_settlement", "Wildling Village", 82, true),
    THENN_SETTLEMENT(307, "thenn_settlement", "Thenn Village", 82, true);

    private final int legacyId;
    private final String serializedName;
    private final String displayName;
    private final int radius;
    private final boolean settlement;

    WildlingStructureType(int legacyId, String serializedName, String displayName,
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
    public static WildlingStructureType findByLegacyId(int id) {
        return Arrays.stream(values()).filter(type -> type.legacyId == id).findFirst().orElse(null);
    }
}
