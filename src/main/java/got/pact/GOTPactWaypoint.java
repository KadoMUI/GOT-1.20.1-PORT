package got.pact;

import got.common.fasttravel.GOTFastTravelData;
import net.minecraft.nbt.CompoundTag;
import java.util.UUID;

/** One member-owned custom map marker shared with the whole Pact. */
public record GOTPactWaypoint(UUID owner, int id, String name, int x, int y, int z) {
    public CompoundTag save() {
        CompoundTag t = new CompoundTag();
        t.putUUID("Owner", owner); t.putInt("Id", id); t.putString("Name", name);
        t.putInt("X", x); t.putInt("Y", y); t.putInt("Z", z);
        return t;
    }
    public static GOTPactWaypoint load(CompoundTag t) {
        return new GOTPactWaypoint(t.getUUID("Owner"), t.getInt("Id"), t.getString("Name"), t.getInt("X"), t.getInt("Y"), t.getInt("Z"));
    }
    public GOTFastTravelData.Custom asCustom() { return new GOTFastTravelData.Custom(owner, id, name, x, y, z); }
}
