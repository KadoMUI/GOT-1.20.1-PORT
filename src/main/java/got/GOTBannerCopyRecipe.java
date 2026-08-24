package got;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

/**
 * Copies legacy GOTBannerData from one banner to blank banners of the same
 * design, or clears that data when a protected banner is crafted by itself.
 */
public final class GOTBannerCopyRecipe extends CustomRecipe {
    public GOTBannerCopyRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer inventory, Level level) {
        return assembleInternal(inventory) != null;
    }

    @Override
    public ItemStack assemble(CraftingContainer inventory, RegistryAccess access) {
        ItemStack result = assembleInternal(inventory);
        return result == null ? ItemStack.EMPTY : result;
    }

    @Nullable
    private static ItemStack assembleInternal(CraftingContainer inventory) {
        ItemStack source = ItemStack.EMPTY;
        GOTBannerType type = null;
        int blanks = 0;
        int banners = 0;

        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (stack.isEmpty()) continue;
            if (!(stack.getItem() instanceof GOTBannerItem)) return null;
            banners++;
            GOTBannerType stackType = GOTBannerItem.getBannerType(stack);
            if (type == null) type = stackType;
            if (stackType != type) return null;
            if (GOTBannerItem.getProtectionData(stack) != null) {
                if (!source.isEmpty()) return null;
                source = stack;
            } else {
                blanks++;
            }
        }

        if (banners == 0 || source.isEmpty()) return null;
        ItemStack result = source.copy();
        if (blanks == 0) {
            result.setCount(1);
            GOTBannerItem.setProtectionData(result, null);
        } else {
            result.setCount(blanks + 1);
        }
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 1;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return GOTRecipes.BANNER_COPY.get();
    }
}
