package got.faction;

import net.minecraft.network.chat.Component;

public record GOTFactionRank(float alignment, String key) {
    public Component displayName(GOTFaction faction) {
        if (key.equals("neutral") || key.equals("enemy")) {
            return Component.translatable("got.rank." + key);
        }
        return Component.translatable("got.rank." + key)
                .append(" ")
                .append(Component.translatable("got.rank." + faction.legacyName()));
    }
}
