package got.faction;

import net.minecraft.network.chat.Component;

public enum GOTFactionRelation {
    ALLY,
    FRIEND,
    NEUTRAL,
    ENEMY,
    MORTAL_ENEMY;

    public boolean isGood() {
        return this == ALLY || this == FRIEND;
    }

    public boolean isBad() {
        return this == ENEMY || this == MORTAL_ENEMY;
    }

    public Component displayName() {
        return Component.translatable("got.faction.rel." + name());
    }
}
