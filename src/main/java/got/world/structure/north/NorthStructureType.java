package got.world.structure.north;

import got.world.structure.GOTStructureSpawnerType;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * The complete North subset of the legacy structure-spawner catalogue.
 * Legacy IDs are deliberately retained so old stacks can be migrated without
 * remapping their SpawnedID value.
 */
public enum NorthStructureType implements GOTStructureSpawnerType {
    HILLMAN_HOUSE(15, "north_hillman_house", "North Hillman House", 9),
    HILLMAN_CHIEFTAIN_HOUSE(16, "north_hillman_chieftain_house", "North Hillman Chieftain House", 13),
    BARN(17, "north_barn", "North Barn", 12),
    BATH(18, "north_bath", "North Bath", 10),
    FORTRESS(19, "north_fortress", "North Fortress", 22),
    HOUSE(20, "north_house", "North House", 10),
    HOUSE_LARGE(21, "north_house_large", "Large North House", 13),
    HOUSE_SMALL(22, "north_house_small", "Small North House", 8),
    SMITHY(23, "north_smithy", "North Smithy", 11),
    STABLES(24, "north_stables", "North Stables", 13),
    TAVERN(25, "north_tavern", "North Tavern", 15),
    TOWER(26, "north_tower", "North Tower", 8),
    WATCHFORT(27, "north_watchfort", "North Watchfort", 16),
    WATCHTOWER(28, "north_watchtower", "North Watchtower", 7),
    HILLMAN_SETTLEMENT(309, "north_hillman_settlement", "North Hillman Settlement", 80),
    VILLAGE(310, "north_village", "North Village", 72),
    FORT_SETTLEMENT(311, "north_fortified_settlement", "North Fortified Settlement", 70),
    TOWN(312, "north_town", "North Town", 94);

    private final int legacyId;
    private final String serializedName;
    private final String displayName;
    private final int radius;

    NorthStructureType(int legacyId, String serializedName, String displayName, int radius) {
        this.legacyId = legacyId;
        this.serializedName = serializedName;
        this.displayName = displayName;
        this.radius = radius;
    }

    public int legacyId() { return legacyId; }
    public String serializedName() { return serializedName; }
    public String displayName() { return displayName; }
    public int radius() { return radius; }
    public boolean settlement() { return legacyId >= 309; }

    @Nullable
    public static NorthStructureType findByLegacyId(int id) {
        return Arrays.stream(values()).filter(type -> type.legacyId == id).findFirst().orElse(null);
    }

    public static NorthStructureType byLegacyId(int id) {
        NorthStructureType type = findByLegacyId(id);
        return type != null ? type : HOUSE;
    }
}
