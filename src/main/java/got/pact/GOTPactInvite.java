package got.pact;

import net.minecraft.nbt.CompoundTag;
import java.util.UUID;

public record GOTPactInvite(UUID pactId, UUID inviterId, UUID invitedPlayerId, long expiresAtGameTime) {
    public boolean expired(long now) { return expiresAtGameTime >= 0 && now >= expiresAtGameTime; }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("Pact", pactId);
        tag.putUUID("Inviter", inviterId);
        tag.putUUID("Player", invitedPlayerId);
        tag.putLong("Expires", expiresAtGameTime);
        return tag;
    }

    public static GOTPactInvite load(CompoundTag tag) {
        return new GOTPactInvite(tag.getUUID("Pact"), tag.getUUID("Inviter"), tag.getUUID("Player"), tag.getLong("Expires"));
    }
}
