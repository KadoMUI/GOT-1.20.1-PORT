package got;

import net.minecraft.world.item.ItemStack;
import java.util.*;

/** The legacy barrel's six-slot shapeless recipes. */
public final class FermentationRecipes {
    private static FermentationRecipe r(String out, String... in) { return new FermentationRecipe("got:"+out, List.of(in)); }
    public static final List<FermentationRecipe> ALL = List.of(
        r("mug_ale", "minecraft:wheat","minecraft:wheat","minecraft:wheat","minecraft:wheat","minecraft:wheat","minecraft:wheat"),
        r("mug_unsullied_tonic", "minecraft:bone","minecraft:bone","minecraft:bone","minecraft:bone","minecraft:bone","minecraft:bone"),
        r("mug_mead", "minecraft:honey_bottle","minecraft:honey_bottle","minecraft:honey_bottle","minecraft:honey_bottle","minecraft:honey_bottle","minecraft:honey_bottle"),
        r("mug_cider", "minecraft:apple","minecraft:apple","minecraft:apple","minecraft:apple","minecraft:apple","minecraft:apple"),
        r("mug_perry", "got:pear","got:pear","got:pear","got:pear","got:pear","got:pear"),
        r("mug_red_wine", "got:red_grape","got:red_grape","got:red_grape","got:red_grape","got:red_grape","got:red_grape"),
        r("mug_white_wine", "got:white_grape","got:white_grape","got:white_grape","got:white_grape","got:white_grape","got:white_grape"),
        r("mug_pomegranate_wine", "got:pomegranate","got:pomegranate","got:pomegranate","got:pomegranate","got:pomegranate","got:pomegranate"),
        r("mug_carrot_wine", "minecraft:carrot","minecraft:carrot","minecraft:carrot","minecraft:carrot","minecraft:carrot","minecraft:carrot"),
        r("mug_banana_beer", "got:banana","got:banana","got:banana","got:banana","got:banana","got:banana"),
        r("mug_maple_beer", "got:maple_syrup","got:maple_syrup","got:maple_syrup","minecraft:wheat","minecraft:wheat","minecraft:wheat"),
        r("mug_plantain_brew", "got:plantain","got:plantain","got:plantain","got:plantain","got:plantain","got:plantain"),
        r("mug_plum_kvass", "got:plum","got:plum","got:plum","minecraft:bread","minecraft:bread","minecraft:bread"),
        r("mug_araq", "got:date","got:date","got:date","got:date","got:date","got:date"),
        r("mug_rum", "minecraft:sugar_cane","minecraft:sugar_cane","minecraft:sugar_cane","minecraft:sugar_cane","minecraft:sugar_cane","minecraft:sugar_cane"),
        r("mug_vodka", "minecraft:potato","minecraft:potato","minecraft:potato","minecraft:potato","minecraft:potato","minecraft:potato"),
        r("mug_whisky", "minecraft:wheat","minecraft:wheat","minecraft:wheat","minecraft:barley","minecraft:barley","minecraft:barley"),
        r("mug_corn_liquor", "got:corn_stalk","got:corn_stalk","got:corn_stalk","got:corn_stalk","got:corn_stalk","got:corn_stalk"),
        r("mug_gin", "minecraft:sweet_berries","minecraft:sweet_berries","minecraft:sweet_berries","minecraft:wheat_seeds","minecraft:wheat_seeds","minecraft:wheat_seeds"),
        r("mug_sambuca", "minecraft:sugar","minecraft:sugar","minecraft:sugar","minecraft:fermented_spider_eye","minecraft:fermented_spider_eye","minecraft:fermented_spider_eye"),
        r("mug_brandy", "got:red_grape","got:red_grape","got:red_grape","minecraft:sugar","minecraft:sugar","minecraft:sugar"),
        r("mug_cactus_liqueur", "minecraft:cactus","minecraft:cactus","minecraft:cactus","minecraft:sugar","minecraft:sugar","minecraft:sugar"),
        r("mug_cherry_liqueur", "got:cherry","got:cherry","got:cherry","minecraft:sugar","minecraft:sugar","minecraft:sugar"),
        r("mug_lemon_liqueur", "got:lemon","got:lemon","got:lemon","minecraft:sugar","minecraft:sugar","minecraft:sugar"),
        r("mug_lime_liqueur", "got:lime","got:lime","got:lime","minecraft:sugar","minecraft:sugar","minecraft:sugar"),
        r("mug_melon_liqueur", "minecraft:melon_slice","minecraft:melon_slice","minecraft:melon_slice","minecraft:sugar","minecraft:sugar","minecraft:sugar"),
        r("mug_termite_tequila", "minecraft:cactus","minecraft:cactus","minecraft:sugar","minecraft:sugar","minecraft:spider_eye","minecraft:spider_eye"),
        r("mug_sour_milk", "minecraft:milk_bucket","minecraft:milk_bucket","minecraft:milk_bucket","minecraft:fermented_spider_eye","minecraft:fermented_spider_eye","minecraft:fermented_spider_eye"),
        r("mug_ethanol", "minecraft:sugar","minecraft:sugar","minecraft:sugar","minecraft:potato","minecraft:potato","minecraft:potato"),
        r("mug_shade_evening", "minecraft:chorus_fruit","minecraft:chorus_fruit","minecraft:chorus_fruit","minecraft:glow_berries","minecraft:glow_berries","minecraft:glow_berries"),
        r("mug_poppy_milk", "minecraft:poppy","minecraft:poppy","minecraft:poppy","minecraft:milk_bucket","minecraft:milk_bucket","minecraft:milk_bucket"),
        r("mug_chocolate", "minecraft:cocoa_beans","minecraft:cocoa_beans","minecraft:cocoa_beans","minecraft:sugar","minecraft:sugar","minecraft:milk_bucket")
    );
    public static FermentationRecipe find(List<ItemStack> stacks) {
        for (FermentationRecipe recipe : ALL) if (recipe.matches(stacks)) return recipe;
        return null;
    }
    private FermentationRecipes() {}
}
