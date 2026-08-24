package got.npc.hiring.command;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Legacy-compatible squadron tag carried by Command Sword/Horn/Squadron items.
 *
 * Original NBT key: GOTSquadron
 * Original maximum accepted length: 200.
 */
public final class GOTCommandToolSquadron {
    public static final String NBT_KEY = "GOTSquadron";
    public static final int MAX_LENGTH = 200;

    private GOTCommandToolSquadron() {}

    @Nullable
    public static String get(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(NBT_KEY)) return null;
        return tag.getString(NBT_KEY);
    }

    public static void set(ItemStack stack, @Nullable String squadron) {
        CompoundTag tag = stack.getOrCreateTag();
        if (squadron == null) {
            tag.remove(NBT_KEY);
            return;
        }
        String value = squadron.length() > MAX_LENGTH ? squadron.substring(0, MAX_LENGTH) : squadron;
        tag.putString(NBT_KEY, value);
    }

    public static boolean compatible(String npcSquadron, ItemStack commandItem) {
        String itemSquadron = get(commandItem);
        boolean npcEmpty = npcSquadron == null || npcSquadron.isBlank();
        boolean itemEmpty = itemSquadron == null || itemSquadron.isBlank();
        if (npcEmpty) return itemEmpty;
        return itemSquadron != null && npcSquadron.equalsIgnoreCase(itemSquadron);
    }
}
