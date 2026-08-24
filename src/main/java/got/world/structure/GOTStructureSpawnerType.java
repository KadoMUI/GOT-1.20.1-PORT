package got.world.structure;

import got.world.structure.nightwatch.NightWatchStructureType;
import got.world.structure.north.NorthStructureType;
import got.world.structure.wildling.WildlingStructureType;

/** Shared legacy identity used by every regional structure-spawner catalogue. */
public interface GOTStructureSpawnerType {
    int legacyId();
    String serializedName();
    String displayName();
    int radius();
    boolean settlement();

    static GOTStructureSpawnerType byLegacyId(int id) {
        WildlingStructureType wildling = WildlingStructureType.findByLegacyId(id);
        if (wildling != null) return wildling;
        NightWatchStructureType nightWatch = NightWatchStructureType.findByLegacyId(id);
        if (nightWatch != null) return nightWatch;
        NorthStructureType north = NorthStructureType.findByLegacyId(id);
        return north != null ? north : NorthStructureType.HOUSE;
    }
}
