package got;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/** Special recipes retained from the original mod. */
public final class GOTRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, GOTMod.MOD_ID);

    public static final RegistryObject<RecipeSerializer<GOTBannerCopyRecipe>> BANNER_COPY =
            SERIALIZERS.register("banner_copy",
                    () -> new SimpleCraftingRecipeSerializer<>(GOTBannerCopyRecipe::new));

    private GOTRecipes() {}

    public static void register(IEventBus modBus) {
        SERIALIZERS.register(modBus);
    }
}
