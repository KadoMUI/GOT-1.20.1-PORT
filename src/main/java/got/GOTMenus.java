package got;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class GOTMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, GOTMod.MOD_ID);

    public static final RegistryObject<MenuType<OvenMenu>> OVEN = MENUS.register("oven", () -> IForgeMenuType.create(OvenMenu::new));
    public static final RegistryObject<MenuType<AlloyForgeMenu>> ALLOY_FORGE = MENUS.register("alloy_forge", () -> IForgeMenuType.create(AlloyForgeMenu::new));
    public static final RegistryObject<MenuType<MillstoneMenu>> MILLSTONE = MENUS.register("millstone", () -> IForgeMenuType.create(MillstoneMenu::new));
    public static final RegistryObject<MenuType<FermentationBarrelMenu>> FERMENTATION_BARREL = MENUS.register("fermentation_barrel", () -> IForgeMenuType.create(FermentationBarrelMenu::new));
    public static final RegistryObject<MenuType<got.menu.hiring.GOTContainerHiredWarriorInventory>> HIRED_WARRIOR_INVENTORY = MENUS.register("hired_warrior_inventory", () -> IForgeMenuType.create(got.menu.hiring.GOTContainerHiredWarriorInventory::new));
    public static final RegistryObject<MenuType<got.menu.hiring.GOTContainerHiredFarmerInventory>> HIRED_FARMER_INVENTORY = MENUS.register("hired_farmer_inventory", () -> IForgeMenuType.create(got.menu.hiring.GOTContainerHiredFarmerInventory::new));

    public static final RegistryObject<MenuType<GOTSmithingMenu>> GOT_SMITHING = MENUS.register("got_smithing", () -> IForgeMenuType.create(GOTSmithingMenu::new));
    public static final RegistryObject<MenuType<GOTPouchMenu>> POUCH = MENUS.register("pouch", () -> IForgeMenuType.create(GOTPouchMenu::new));
    public static final RegistryObject<MenuType<GOTCoinExchangeMenu>> COIN_EXCHANGE = MENUS.register("coin_exchange", () -> IForgeMenuType.create(GOTCoinExchangeMenu::new));

    private GOTMenus() {}

    public static void register(IEventBus bus) {
        MENUS.register(bus);
    }
}
