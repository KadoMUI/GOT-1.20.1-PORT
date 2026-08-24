package got;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import java.util.*;

public record FermentationRecipe(String outputId, List<String> ingredients) {
    public boolean matches(List<ItemStack> stacks) {
        List<String> remaining = new ArrayList<>(ingredients);
        for (ItemStack stack : stacks) {
            if (stack.isEmpty()) return false;
            ResourceLocation key = BuiltInRegistries.ITEM.getKey(stack.getItem());
            String id = key.toString();
            int found = remaining.indexOf(id);
            if (found < 0) return false;
            remaining.remove(found);
        }
        return remaining.isEmpty();
    }

    public ItemStack output() {
        var item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(outputId));
        return new ItemStack(item, 16);
    }
}
