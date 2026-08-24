package got;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * The vessel portion of a filled legacy drink. 1.7.10 encoded this in item
 * damage; the 1.20.1 port stores the stable serialized name in stack NBT.
 */
public enum GOTDrinkVessel {
    MUG("mug", true),
    CLAY_MUG("clay_mug", true),
    CERAMIC_MUG("ceramic_mug", true),
    GOLD_GOBLET("gold_goblet", true),
    SILVER_GOBLET("silver_goblet", true),
    COPPER_GOBLET("copper_goblet", true),
    WOODEN_GOBLET("wooden_goblet", true),
    SKULL_CUP("skull_cup", true),
    WINE_GLASS("wine_glass", true),
    BOTTLE("bottle", true),
    WATERSKIN("waterskin", false),
    DRINKING_HORN("drinking_horn", true),
    GOLD_DRINKING_HORN("gold_drinking_horn", true),
    BRONZE_GOBLET("bronze_goblet", true),
    VALYRIAN_GOBLET("valyrian_goblet", true);

    private final String serializedName;
    private final boolean placeable;

    /** CustomModelData value used by the item model override. */
    public int modelData() {
        return ordinal();
    }

    GOTDrinkVessel(String serializedName, boolean placeable) {
        this.serializedName = serializedName;
        this.placeable = placeable;
    }

    public String serializedName() {
        return serializedName;
    }

    public boolean isPlaceable() {
        return placeable;
    }

    public Component displayName() {
        return Component.translatable("drink_vessel.got." + serializedName);
    }

    public ItemStack emptyStack() {
        Item item = switch (this) {
            case MUG -> GOTItems.MUG.get();
            case CLAY_MUG -> GOTBlocks.CLAY_MUG.get().asItem();
            case CERAMIC_MUG -> GOTBlocks.CERAMIC_MUG.get().asItem();
            case GOLD_GOBLET -> GOTBlocks.GOLD_GOBLET.get().asItem();
            case SILVER_GOBLET -> GOTBlocks.SILVER_GOBLET.get().asItem();
            case COPPER_GOBLET -> GOTBlocks.COPPER_GOBLET.get().asItem();
            case WOODEN_GOBLET -> GOTBlocks.WOODEN_GOBLET.get().asItem();
            case SKULL_CUP -> GOTBlocks.SKULL_CUP.get().asItem();
            // The dedicated wine-glass block is not ported yet; a bottle is a safe temporary return.
            case WINE_GLASS -> GOTBlocks.WINE_GLASS.get().asItem();
            case BOTTLE -> Items.GLASS_BOTTLE;
            case WATERSKIN -> GOTItems.WATERSKIN.get();
            case DRINKING_HORN -> GOTBlocks.ALE_HORN.get().asItem();
            case GOLD_DRINKING_HORN -> GOTBlocks.GOLDEN_ALE_HORN.get().asItem();
            case BRONZE_GOBLET -> GOTBlocks.BRONZE_GOBLET.get().asItem();
            case VALYRIAN_GOBLET -> GOTBlocks.VALYRIAN_GOBLET.get().asItem();
        };
        return new ItemStack(item);
    }

    public static GOTDrinkVessel byName(String name) {
        for (GOTDrinkVessel vessel : values()) {
            if (vessel.serializedName.equals(name)) {
                return vessel;
            }
        }
        return MUG;
    }
}
