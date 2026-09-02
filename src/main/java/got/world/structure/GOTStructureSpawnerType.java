package got.world.structure;

import got.world.structure.nightwatch.NightWatchStructureType;
import got.world.structure.north.NorthStructureType;
import got.world.structure.wildling.WildlingStructureType;
import got.world.structure.schematic.AuthoredStructureType;

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
        if (north != null) return north;
        AuthoredStructureType authored = AuthoredStructureType.findByLegacyId(id);
        return authored != null ? authored : NorthStructureType.HOUSE;
    }
}
