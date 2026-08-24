package got;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GOTItemOwnership {
    public static final String CURRENT_OWNER = "GOTCurrentOwner";
    public static final String PREVIOUS_OWNERS = "GOTPrevOwnerList";
    private static final int MAX_PREVIOUS = 3;

    private GOTItemOwnership() {}

    public static String getCurrentOwner(ItemStack stack) {
        if (stack == null || stack.isEmpty() || !stack.hasTag()) return null;
        String owner = stack.getTag().getString(CURRENT_OWNER);
        return owner.isBlank() ? null : owner;
    }

    public static List<String> getPreviousOwners(ItemStack stack) {
        if (stack == null || stack.isEmpty() || !stack.hasTag()) return List.of();
        ListTag list = stack.getTag().getList(PREVIOUS_OWNERS, Tag.TAG_STRING);
        List<String> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) result.add(list.getString(i));
        return Collections.unmodifiableList(result);
    }

    public static boolean canEngraveNewOwner(ItemStack stack, String playerName) {
        String current = getCurrentOwner(stack);
        return current == null || !current.equals(playerName);
    }

    public static void setCurrentOwner(ItemStack stack, String owner) {
        String current = getCurrentOwner(stack);
        if (current != null) addPreviousOwner(stack, current);
        stack.getOrCreateTag().putString(CURRENT_OWNER, owner);
    }

    public static void addPreviousOwner(ItemStack stack, String owner) {
        List<String> old = new ArrayList<>(getPreviousOwners(stack));
        old.remove(owner);
        old.add(0, owner);
        while (old.size() > MAX_PREVIOUS) old.remove(old.size() - 1);

        ListTag tag = new ListTag();
        for (String value : old) tag.add(StringTag.valueOf(value));
        stack.getOrCreateTag().put(PREVIOUS_OWNERS, tag);
    }
}
