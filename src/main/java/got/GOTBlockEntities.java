package got;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class GOTBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, GOTMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<OvenBlockEntity>> OVEN = BLOCK_ENTITIES.register("oven",
            () -> BlockEntityType.Builder.of(OvenBlockEntity::new, GOTBlocks.OVEN.get()).build(null));

    public static final RegistryObject<BlockEntityType<FermentationBarrelBlockEntity>> FERMENTATION_BARREL = BLOCK_ENTITIES.register("fermentation_barrel",
            () -> BlockEntityType.Builder.of(FermentationBarrelBlockEntity::new, GOTBlocks.FERMENTATION_BARREL.get()).build(null));

    public static final RegistryObject<BlockEntityType<PlacedDrinkVesselBlockEntity>> PLACED_DRINK_VESSEL = BLOCK_ENTITIES.register("placed_drink_vessel",
            () -> BlockEntityType.Builder.of(PlacedDrinkVesselBlockEntity::new,
                    GOTBlocks.PLACED_DRINK_VESSEL.get(),
                    GOTBlocks.WOODEN_MUG.get(), GOTBlocks.CLAY_MUG.get(), GOTBlocks.CERAMIC_MUG.get(),
                    GOTBlocks.GOLD_GOBLET.get(), GOTBlocks.SILVER_GOBLET.get(), GOTBlocks.COPPER_GOBLET.get(),
                    GOTBlocks.WOODEN_GOBLET.get(), GOTBlocks.BRONZE_GOBLET.get(), GOTBlocks.VALYRIAN_GOBLET.get(),
                    GOTBlocks.ALE_HORN.get(), GOTBlocks.GOLDEN_ALE_HORN.get(), GOTBlocks.SKULL_CUP.get(),
                    GOTBlocks.WINE_GLASS.get(), GOTBlocks.GLASS_BOTTLE_VESSEL.get())
                    .build(null));

    public static final RegistryObject<BlockEntityType<GOTStorageBlockEntity>> STORAGE = BLOCK_ENTITIES.register("storage",
            () -> BlockEntityType.Builder.of(GOTStorageBlockEntity::new,
                    GOTDecorativeFunctionalBlocks.REED_BASKET.get(),
                    GOTDecorativeFunctionalBlocks.SANDSTONE_CHEST.get(),
                    GOTDecorativeFunctionalBlocks.STONE_CHEST.get(),
                    GOTDecorativeFunctionalBlocks.BOOKSHELF_STORAGE.get()).build(null));

    public static final RegistryObject<BlockEntityType<GOTWeaponRackBlockEntity>> WEAPON_RACK = BLOCK_ENTITIES.register("weapon_rack",
            () -> BlockEntityType.Builder.of(GOTWeaponRackBlockEntity::new,
                    GOTDecorativeFunctionalBlocks.WEAPON_RACK.get()).build(null));

    public static final RegistryObject<BlockEntityType<GOTCarvedSignBlockEntity>> CARVED_SIGN = BLOCK_ENTITIES.register("carved_sign",
            () -> BlockEntityType.Builder.of(GOTCarvedSignBlockEntity::new,
                    GOTDecorativeFunctionalBlocks.SIGN_CARVED.get(),
                    GOTDecorativeFunctionalBlocks.SIGN_CARVED_GLOWING.get()).build(null));

    public static final RegistryObject<BlockEntityType<GOTLegacyDecorBlockEntity>> LEGACY_DECOR = BLOCK_ENTITIES.register("legacy_decor",
            () -> BlockEntityType.Builder.of(GOTLegacyDecorBlockEntity::new,
                    GOTDecorativeFunctionalBlocks.BEACON.get(),
                    GOTDecorativeFunctionalBlocks.UNSMELTERY.get(),
                    GOTDecorativeFunctionalBlocks.ALL.get("bear_rug_black").get(),
                    GOTDecorativeFunctionalBlocks.ALL.get("bear_rug_dark").get(),
                    GOTDecorativeFunctionalBlocks.ALL.get("bear_rug_light").get(),
                    GOTDecorativeFunctionalBlocks.ALL.get("giraffe_rug").get(),
                    GOTDecorativeFunctionalBlocks.ALL.get("lion_rug").get(),
                    GOTDecorativeFunctionalBlocks.ALL.get("lioness_rug").get()).build(null));

    private GOTBlockEntities() {}

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
