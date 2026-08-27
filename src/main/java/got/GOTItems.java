package got;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

public final class GOTItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, GOTMod.MOD_ID);

    public static final RegistryObject<Item> ALLOY_STEEL_INGOT = simple("alloy_steel_ingot");
    public static final RegistryObject<Item> ALLOY_STEEL_NUGGET = simple("alloy_steel_nugget");
    public static final RegistryObject<Item> BRONZE_INGOT = simple("bronze_ingot");
    public static final RegistryObject<Item> BRONZE_NUGGET = simple("bronze_nugget");
    public static final RegistryObject<Item> TIN_INGOT = simple("tin_ingot");
    public static final RegistryObject<Item> RAW_TIN = simple("raw_tin");
    public static final RegistryObject<Item> SILVER_INGOT = simple("silver_ingot");
    public static final RegistryObject<Item> SILVER_NUGGET = simple("silver_nugget");
    public static final RegistryObject<Item> RAW_SILVER = simple("raw_silver");
    public static final RegistryObject<Item> OBSIDIAN_SHARD = simple("obsidian_shard");
    public static final RegistryObject<Item> VALYRIAN_STEEL_INGOT = simple("valyrian_steel_ingot");
    public static final RegistryObject<Item> VALYRIAN_STEEL_NUGGET = simple("valyrian_steel_nugget");
    public static final RegistryObject<Item> SALT = simple("salt");

    // Milestone 4.5.3 missing materials and miscellaneous crafting components
    public static final RegistryObject<Item> AMBER = simple("amber");
    public static final RegistryObject<Item> AMETHYST = simple("amethyst");
    public static final RegistryObject<Item> BRONZE_RING = simple("bronze_ring");
    public static final RegistryObject<Item> COBALT_BLUE = simple("cobalt_blue");
    public static final RegistryObject<Item> COPPER_RING = simple("copper_ring");
    public static final RegistryObject<Item> CORAL = simple("coral");
    public static final RegistryObject<Item> DIAMOND = simple("diamond");
    public static final RegistryObject<Item> EMERALD = simple("emerald");
    public static final RegistryObject<Item> FEATHER_DYED = simple("feather_dyed");
    public static final RegistryObject<Item> FUR = simple("fur");
    public static final RegistryObject<Item> GATE_GEAR = simple("gate_gear");
    public static final RegistryObject<Item> GOLD_RING = simple("gold_ring");
    public static final RegistryObject<Item> HORN = simple("horn");
    public static final RegistryObject<Item> ICE_SHARD = simple("ice_shard");
    public static final RegistryObject<Item> LION_FUR = simple("lion_fur");
    public static final RegistryObject<Item> MUG = ITEMS.register("mug", () -> new GOTVesselItem(GOTDrinkVessel.MUG, new Item.Properties()));
    public static final RegistryObject<Item> OPAL = simple("opal");
    public static final RegistryObject<Item> ORYX_HIDE = simple("oryx_hide");
    public static final RegistryObject<Item> ORYX_HORN = simple("oryx_horn");
    public static final RegistryObject<Item> PEBBLE = simple("pebble");
    public static final RegistryObject<Item> POUCH_SMALL = ITEMS.register("pouch_small", () -> new GOTPouchItem(9, new Item.Properties()));
    public static final RegistryObject<Item> POUCH_MEDIUM = ITEMS.register("pouch_medium", () -> new GOTPouchItem(18, new Item.Properties()));
    public static final RegistryObject<Item> POUCH_LARGE = ITEMS.register("pouch_large", () -> new GOTPouchItem(27, new Item.Properties()));
    public static final RegistryObject<Item> PIPE = simple("pipe");
    public static final RegistryObject<Item> RED_CLAY_BALL = simple("red_clay_ball");
    public static final RegistryObject<Item> RHINO_HORN = simple("rhino_horn");
    public static final RegistryObject<Item> RUBY = simple("ruby");
    public static final RegistryObject<Item> SALTPETER = simple("saltpeter");
    public static final RegistryObject<Item> SAPPHIRE = simple("sapphire");
    public static final RegistryObject<Item> SILVER_RING = simple("silver_ring");
    public static final RegistryObject<Item> SULFUR = simple("sulfur");
    public static final RegistryObject<Item> SULFUR_MATCH = simple("sulfur_match");
    public static final RegistryObject<Item> SWAN_FEATHER = simple("swan_feather");
    public static final RegistryObject<Item> TOPAZ = simple("topaz");
    public static final RegistryObject<Item> VALYRIAN_RING = simple("valyrian_ring");
    public static final RegistryObject<Item> WATERSKIN = simple("waterskin");
    public static final RegistryObject<Item> WHEEL = simple("wheel");
    public static final RegistryObject<Item> WHITE_BISON_HORN = simple("white_bison_horn");

    // Catch-up 4: remaining ordinary legacy items. Entity-backed systems,
    // books, pouches, and carts remain intentionally out
    // of this catalogue.
    public static final RegistryObject<Item> BEAVER_TAIL = simple("beaver_tail");
    public static final RegistryObject<Item> BLOOD_OF_TRUE_KINGS = simple("blood_of_true_kings");
    public static final RegistryObject<Item> BOTTLE_POISON = ITEMS.register("bottle_poison",
            () -> new GOTPoisonBottleItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BOUNTY_TROPHY = ITEMS.register("bounty_trophy",
            () -> new GOTSpecialEnchantmentItem(GOTSmithingModifier.HEADHUNTING,
                    new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> CLAY_PLATE = simple("clay_plate");
    public static final RegistryObject<Item> COPPER_NUGGET = simple("copper_nugget");
    public static final RegistryObject<Item> LEATHER_HAT = ITEMS.register("leather_hat",
            () -> new DyeableArmorItem(GOTArmorMaterials.of("hat"), ArmorItem.Type.HELMET,
                    new Item.Properties()));
    public static final RegistryObject<Item> MYSTERY_WEB = simple("mystery_web");
    public static final RegistryObject<Item> SALTED_FLESH = food("salted_flesh", 6, 0.6F);

    // Heraldry uses one NBT-backed item for all 605 legacy designs.
    public static final RegistryObject<Item> BANNER = ITEMS.register("banner",
            () -> new GOTBannerItem(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> ICON_HERALDRY = simple("icon_heraldry");

    // One NBT-backed item preserves the original North structure spawner IDs.
    public static final RegistryObject<Item> STRUCTURE_SPAWNER = ITEMS.register("structure_spawner",
            () -> new GOTStructureSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> NORTH_NPC_SPAWNER = ITEMS.register("north_npc_spawner",
            () -> new got.npc.GOTNorthNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> WESTERLANDS_NPC_SPAWNER = ITEMS.register("westerlands_npc_spawner",
            () -> new got.npc.GOTWesterlandsNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> RIVERLANDS_NPC_SPAWNER = ITEMS.register("riverlands_npc_spawner",
            () -> new got.npc.GOTRiverlandsNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ARRYN_NPC_SPAWNER = ITEMS.register("arryn_npc_spawner",
            () -> new got.npc.GOTArrynNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CROWNLANDS_NPC_SPAWNER = ITEMS.register("crownlands_npc_spawner",
            () -> new got.npc.GOTCrownlandsNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> DRAGONSTONE_NPC_SPAWNER = ITEMS.register("dragonstone_npc_spawner",
            () -> new got.npc.GOTDragonstoneNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> REACH_NPC_SPAWNER = ITEMS.register("reach_npc_spawner",
            () -> new got.npc.GOTReachNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> STORMLANDS_NPC_SPAWNER = ITEMS.register("stormlands_npc_spawner",
            () -> new got.npc.GOTStormlandsNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> DORNE_NPC_SPAWNER = ITEMS.register("dorne_npc_spawner",
            () -> new got.npc.GOTDorneNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> IRONBORN_NPC_SPAWNER = ITEMS.register("ironborn_npc_spawner",
            () -> new got.npc.GOTIronbornNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> WILDLING_NPC_SPAWNER = ITEMS.register("wildling_npc_spawner",
            () -> new got.npc.GOTWildlingNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> NIGHT_WATCH_NPC_SPAWNER = ITEMS.register("night_watch_npc_spawner",
            () -> new got.npc.GOTNightWatchNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> WHITE_WALKER_NPC_SPAWNER = ITEMS.register("white_walker_npc_spawner",
            () -> new got.npc.GOTWhiteWalkerNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BRAAVOS_NPC_SPAWNER = ITEMS.register("braavos_npc_spawner",
            () -> new got.npc.GOTBraavosNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> PENTOS_NPC_SPAWNER = ITEMS.register("pentos_npc_spawner",
            () -> new got.npc.GOTPentosNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> VOLANTIS_NPC_SPAWNER = ITEMS.register("volantis_npc_spawner",
            () -> new got.npc.GOTVolantisNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> LYS_NPC_SPAWNER = ITEMS.register("lys_npc_spawner",
            () -> new got.npc.GOTLysNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MYR_NPC_SPAWNER = ITEMS.register("myr_npc_spawner",
            () -> new got.npc.GOTMyrNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> TYROSH_NPC_SPAWNER = ITEMS.register("tyrosh_npc_spawner",
            () -> new got.npc.GOTTyroshNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> GHISCAR_NPC_SPAWNER = ITEMS.register("ghiscar_npc_spawner",
            () -> new got.npc.GOTGhiscarNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> DOTHRAKI_NPC_SPAWNER = ITEMS.register("dothraki_npc_spawner",
            () -> new got.npc.GOTDothrakiNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> YI_TI_NPC_SPAWNER = ITEMS.register("yi_ti_npc_spawner",
            () -> new got.npc.GOTYiTiNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ASSHAI_NPC_SPAWNER = ITEMS.register("asshai_npc_spawner",
            () -> new got.npc.GOTAsshaiNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> IBBEN_NPC_SPAWNER = ITEMS.register("ibben_npc_spawner",
            () -> new got.npc.GOTIbbenNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> JOGOS_NHAI_NPC_SPAWNER = ITEMS.register("jogos_nhai_npc_spawner",
            () -> new got.npc.GOTJogosNhaiNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> QARTH_NPC_SPAWNER = ITEMS.register("qarth_npc_spawner",
            () -> new got.npc.GOTQarthNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> LORATH_NPC_SPAWNER = ITEMS.register("lorath_npc_spawner",
            () -> new got.npc.GOTLorathNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> QOHOR_NPC_SPAWNER = ITEMS.register("qohor_npc_spawner",
            () -> new got.npc.GOTQohorNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> LHAZAR_NPC_SPAWNER = ITEMS.register("lhazar_npc_spawner",
            () -> new got.npc.GOTLhazarNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> NORVOS_NPC_SPAWNER = ITEMS.register("norvos_npc_spawner",
            () -> new got.npc.GOTNorvosNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MOSSOVY_NPC_SPAWNER = ITEMS.register("mossovy_npc_spawner",
            () -> new got.npc.GOTMossovyNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> GOLDEN_COMPANY_NPC_SPAWNER = ITEMS.register("golden_company_npc_spawner",
            () -> new got.npc.GOTGoldenCompanyNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SUMMER_ISLES_NPC_SPAWNER = ITEMS.register("summer_isles_npc_spawner",
            () -> new got.npc.GOTSummerIslesNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SOTHORYOS_NPC_SPAWNER = ITEMS.register("sothoryos_npc_spawner",
            () -> new got.npc.GOTSothoryosNpcSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ULTHOS_CREATURE_SPAWNER = ITEMS.register("ulthos_creature_spawner",
            () -> new got.npc.GOTUlthosCreatureSpawnerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> QUEST_BOOK = ITEMS.register("quest_book",
            () -> new got.quest.GOTQuestBookItem(new Item.Properties().stacksTo(1)));
    // Original NBT-backed smithing template item. Each stack stores ScrollModifier.
    public static final RegistryObject<Item> SMITH_SCROLL = ITEMS.register("smith_scroll",
            () -> new GOTSmithScrollItem(new Item.Properties().stacksTo(1)));


    // Milestone 4.5.4 consumables
    public static final RegistryObject<Item> APPLE_CRUMBLE = placeableFood("apple_crumble", 6, 0.6F,
            () -> GOTDecorativeFunctionalBlocks.APPLE_CRUMBLE.get());
    public static final RegistryObject<Item> BANANA_CAKE = placeableFood("banana_cake", 6, 0.6F,
            () -> GOTDecorativeFunctionalBlocks.BANANA_CAKE.get());
    public static final RegistryObject<Item> BEAVER_COOKED = food("beaver_cooked", 7, 0.7F);
    public static final RegistryObject<Item> BEAVER_RAW = food("beaver_raw", 2, 0.2F);
    public static final RegistryObject<Item> BERRY_PIE = placeableFood("berry_pie", 6, 0.6F,
            () -> GOTDecorativeFunctionalBlocks.BERRY_PIE.get());
    public static final RegistryObject<Item> CAMEL_COOKED = food("camel_cooked", 7, 0.7F);
    public static final RegistryObject<Item> CAMEL_RAW = food("camel_raw", 2, 0.2F);
    public static final RegistryObject<Item> CHERRY_PIE = placeableFood("cherry_pie", 6, 0.6F,
            () -> GOTDecorativeFunctionalBlocks.CHERRY_PIE.get());
    public static final RegistryObject<Item> DATE = placeableFood("date", 2, 0.2F,
            () -> GOTDecorativeFunctionalBlocks.DATE.get());
    public static final RegistryObject<Item> DEER_COOKED = food("deer_cooked", 7, 0.7F);
    public static final RegistryObject<Item> DEER_RAW = food("deer_raw", 2, 0.2F);
    public static final RegistryObject<Item> ELEPHANT_COOKED = food("elephant_cooked", 7, 0.7F);
    public static final RegistryObject<Item> ELEPHANT_RAW = food("elephant_raw", 2, 0.2F);
    public static final RegistryObject<Item> GAMMON = food("gammon", 7, 0.7F);
    public static final RegistryObject<Item> GINGERBREAD = food("gingerbread", 6, 0.6F);
    public static final RegistryObject<Item> KEBAB = food("kebab", 7, 0.7F);
    public static final RegistryObject<Item> LEEK = food("leek", 2, 0.2F);
    public static final RegistryObject<Item> LEEK_SOUP = food("leek_soup", 6, 0.6F);
    public static final RegistryObject<Item> LEMON = food("lemon", 2, 0.2F);
    public static final RegistryObject<Item> LEMON_CAKE = placeableFood("lemon_cake", 6, 0.6F,
            () -> GOTDecorativeFunctionalBlocks.LEMON_CAKE.get());
    public static final RegistryObject<Item> LIME = food("lime", 2, 0.2F);
    public static final RegistryObject<Item> LION_COOKED = food("lion_cooked", 7, 0.7F);
    public static final RegistryObject<Item> LION_RAW = food("lion_raw", 2, 0.2F);
    public static final RegistryObject<Item> MANGO = food("mango", 2, 0.2F);
    public static final RegistryObject<Item> MAPLE_SYRUP = food("maple_syrup", 4, 0.4F);
    public static final RegistryObject<Item> MARZIPAN = food("marzipan", 6, 0.6F);
    public static final RegistryObject<Item> MARZIPAN_CHOCOLATE = food("marzipan_chocolate", 6, 0.6F);
    public static final RegistryObject<Item> MELON_SOUP = food("melon_soup", 6, 0.6F);
    public static final RegistryObject<Item> MUSHROOM_PIE = food("mushroom_pie", 6, 0.6F);
    public static final RegistryObject<Item> MUTTON_COOKED = food("mutton_cooked", 7, 0.7F);
    public static final RegistryObject<Item> MUTTON_RAW = food("mutton_raw", 2, 0.2F);
    public static final RegistryObject<Item> OLIVE = food("olive", 2, 0.2F);
    public static final RegistryObject<Item> OLIVE_BREAD = food("olive_bread", 2, 0.2F);
    public static final RegistryObject<Item> ORANGE = food("orange", 2, 0.2F);
    public static final RegistryObject<Item> PANCAKE = food("pancake", 6, 0.6F);
    public static final RegistryObject<Item> PANCAKE_MAPLE_SYRUP = food("pancake_maple_syrup", 6, 0.6F);
    public static final RegistryObject<Item> PASTRY = placeableFood("pastry", 6, 0.6F,
            () -> GOTDecorativeFunctionalBlocks.PASTRY.get());
    public static final RegistryObject<Item> PEAR = food("pear", 2, 0.2F);
    public static final RegistryObject<Item> PEARL = food("pearl", 2, 0.2F);
    public static final RegistryObject<Item> PLUM = food("plum", 2, 0.2F);
    public static final RegistryObject<Item> POMEGRANATE = food("pomegranate", 2, 0.2F);
    public static final RegistryObject<Item> RABBIT_COOKED = food("rabbit_cooked", 7, 0.7F);
    public static final RegistryObject<Item> RABBIT_RAW = food("rabbit_raw", 2, 0.2F);
    public static final RegistryObject<Item> RAISINS = food("raisins", 2, 0.2F);
    public static final RegistryObject<Item> RHINO_COOKED = food("rhino_cooked", 7, 0.7F);
    public static final RegistryObject<Item> RHINO_RAW = food("rhino_raw", 2, 0.2F);
    public static final RegistryObject<Item> RICE = food("rice", 2, 0.2F);
    public static final RegistryObject<Item> SHISH_KEBAB = food("shish_kebab", 7, 0.7F);
    public static final RegistryObject<Item> TURNIP = food("turnip", 2, 0.2F);
    public static final RegistryObject<Item> TURNIP_COOKED = food("turnip_cooked", 2, 0.2F);
    public static final RegistryObject<Item> WALRUS_LARD_COOKED = food("walrus_lard_cooked", 7, 0.7F);
    public static final RegistryObject<Item> WALRUS_LARD_RAW = food("walrus_lard_raw", 2, 0.2F);
    public static final RegistryObject<Item> YAM = food("yam", 2, 0.2F);
    public static final RegistryObject<Item> YAM_ROAST = food("yam_roast", 2, 0.2F);
    public static final RegistryObject<Item> ZEBRA_COOKED = food("zebra_cooked", 7, 0.7F);
    public static final RegistryObject<Item> ZEBRA_RAW = food("zebra_raw", 2, 0.2F);
    public static final RegistryObject<Item> MUG_ALE = drink("mug_ale");
    public static final RegistryObject<Item> MUG_APPLE_JUICE = drink("mug_apple_juice");
    public static final RegistryObject<Item> MUG_ARAQ = drink("mug_araq");
    public static final RegistryObject<Item> MUG_BANANA_BEER = drink("mug_banana_beer");
    public static final RegistryObject<Item> MUG_BLACKBERRY_JUICE = drink("mug_blackberry_juice");
    public static final RegistryObject<Item> MUG_BLUEBERRY_JUICE = drink("mug_blueberry_juice");
    public static final RegistryObject<Item> MUG_BRANDY = drink("mug_brandy");
    public static final RegistryObject<Item> MUG_CACTUS_LIQUEUR = drink("mug_cactus_liqueur");
    public static final RegistryObject<Item> MUG_CARROT_WINE = drink("mug_carrot_wine");
    public static final RegistryObject<Item> MUG_CHERRY_LIQUEUR = drink("mug_cherry_liqueur");
    public static final RegistryObject<Item> MUG_CHOCOLATE = drink("mug_chocolate");
    public static final RegistryObject<Item> MUG_CIDER = drink("mug_cider");
    public static final RegistryObject<Item> MUG_COCOA = drink("mug_cocoa");
    public static final RegistryObject<Item> MUG_CORN_LIQUOR = drink("mug_corn_liquor");
    public static final RegistryObject<Item> MUG_CRANBERRY_JUICE = drink("mug_cranberry_juice");
    public static final RegistryObject<Item> MUG_ELDERBERRY_JUICE = drink("mug_elderberry_juice");
    public static final RegistryObject<Item> MUG_ETHANOL = drink("mug_ethanol");
    public static final RegistryObject<Item> MUG_GIN = drink("mug_gin");
    public static final RegistryObject<Item> MUG_LEMON_LIQUEUR = drink("mug_lemon_liqueur");
    public static final RegistryObject<Item> MUG_LEMONADE = drink("mug_lemonade");
    public static final RegistryObject<Item> MUG_LIME_LIQUEUR = drink("mug_lime_liqueur");
    public static final RegistryObject<Item> MUG_MANGO_JUICE = drink("mug_mango_juice");
    public static final RegistryObject<Item> MUG_MAPLE_BEER = drink("mug_maple_beer");
    public static final RegistryObject<Item> MUG_MEAD = drink("mug_mead");
    public static final RegistryObject<Item> MUG_MELON_LIQUEUR = drink("mug_melon_liqueur");
    public static final RegistryObject<Item> MUG_MILK = drink("mug_milk");
    public static final RegistryObject<Item> MUG_ORANGE_JUICE = drink("mug_orange_juice");
    public static final RegistryObject<Item> MUG_PERRY = drink("mug_perry");
    public static final RegistryObject<Item> MUG_PLANTAIN_BREW = drink("mug_plantain_brew");
    public static final RegistryObject<Item> MUG_PLUM_KVASS = drink("mug_plum_kvass");
    public static final RegistryObject<Item> MUG_POMEGRANATE_JUICE = drink("mug_pomegranate_juice");
    public static final RegistryObject<Item> MUG_POMEGRANATE_WINE = drink("mug_pomegranate_wine");
    public static final RegistryObject<Item> MUG_POPPY_MILK = drink("mug_poppy_milk");
    public static final RegistryObject<Item> MUG_RASPBERRY_JUICE = drink("mug_raspberry_juice");
    public static final RegistryObject<Item> MUG_RED_GRAPE_JUICE = drink("mug_red_grape_juice");
    public static final RegistryObject<Item> MUG_RED_WINE = drink("mug_red_wine");
    public static final RegistryObject<Item> MUG_RUM = drink("mug_rum");
    public static final RegistryObject<Item> MUG_SAMBUCA = drink("mug_sambuca");
    public static final RegistryObject<Item> MUG_SHADE_EVENING = drink("mug_shade_evening");
    public static final RegistryObject<Item> MUG_SOUR_MILK = drink("mug_sour_milk");
    public static final RegistryObject<Item> MUG_TERMITE_TEQUILA = drink("mug_termite_tequila");
    public static final RegistryObject<Item> MUG_UNSULLIED_TONIC = drink("mug_unsullied_tonic");
    public static final RegistryObject<Item> MUG_VODKA = drink("mug_vodka");
    public static final RegistryObject<Item> MUG_WATER = drink("mug_water");
    public static final RegistryObject<Item> MUG_WHISKY = drink("mug_whisky");
    public static final RegistryObject<Item> MUG_WHITE_GRAPE_JUICE = drink("mug_white_grape_juice");
    public static final RegistryObject<Item> MUG_WHITE_WINE = drink("mug_white_wine");
    public static final RegistryObject<Item> MUG_WILD_FIRE = drink("mug_wild_fire");

    public static final RegistryObject<Item> COIN_1 = ITEMS.register("coin_1", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> COIN_4 = ITEMS.register("coin_4", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> COIN_16 = ITEMS.register("coin_16", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> COIN_64 = ITEMS.register("coin_64", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> COIN_256 = ITEMS.register("coin_256", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> COIN_1024 = ITEMS.register("coin_1024", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> COIN_4096 = ITEMS.register("coin_4096", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> COIN_16384 = ITEMS.register("coin_16384", () -> new Item(new Item.Properties().stacksTo(64)));


    // Hiring / command system tools
    // Command Sword/Horn are registered once through GOTEquipment.
    public static final RegistryObject<Item> SQUADRON_ITEM = ITEMS.register("squadron_item",
            () -> new GOTSquadronItem(new Item.Properties()));

    public static final RegistryObject<Item> ALMOND = ITEMS.register("almond", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.2F).build())));
    public static final RegistryObject<Item> APPLE_GREEN = ITEMS.register("apple_green", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3F).build())));
    public static final RegistryObject<Item> BANANA = placeableFood("banana", 4, 0.3F,
            () -> GOTDecorativeFunctionalBlocks.BANANA.get());
    public static final RegistryObject<Item> BANANA_BREAD = ITEMS.register("banana_bread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.6F).build())));
    public static final RegistryObject<Item> BLACKBERRY = ITEMS.register("blackberry", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.2F).fast().build())));
    public static final RegistryObject<Item> BLUEBERRY = ITEMS.register("blueberry", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.2F).fast().build())));
    public static final RegistryObject<Item> CHERRY = ITEMS.register("cherry", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.2F).fast().build())));
    public static final RegistryObject<Item> CHESTNUT = ITEMS.register("chestnut", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.2F).build())));
    public static final RegistryObject<Item> CHESTNUT_ROAST = ITEMS.register("chestnut_roast", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.5F).build())));
    public static final RegistryObject<Item> CORN_BREAD = ITEMS.register("corn_bread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.6F).build())));
    public static final RegistryObject<Item> CORN_COOKED = ITEMS.register("corn_cooked", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.4F).build())));
    public static final RegistryObject<Item> CRANBERRY = ITEMS.register("cranberry", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.2F).fast().build())));



    // Milestone 4.3 plants and agriculture
    public static final RegistryObject<Item> CORN = food("corn", 3, 0.3F);
    public static final RegistryObject<Item> CORN_STALK = seed("corn_stalk", "corn_crop");
    public static final RegistryObject<Item> FLAX = simple("flax");
    public static final RegistryObject<Item> FLAX_SEEDS = seed("flax_seeds", "flax_crop");
    public static final RegistryObject<Item> LETTUCE = ITEMS.register("lettuce", () -> new GOTSeedItem("lettuce_crop", new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.2F).build())));
    public static final RegistryObject<Item> CUCUMBER = food("cucumber", 2, 0.2F);
    public static final RegistryObject<Item> CUCUMBER_SEEDS = seed("cucumber_seeds", "cucumber_crop");
    public static final RegistryObject<Item> PIPEWEED = simple("pipeweed");
    public static final RegistryObject<Item> PIPEWEED_LEAF = simple("pipeweed_leaf");
    public static final RegistryObject<Item> PIPEWEED_SEEDS = seed("pipeweed_seeds", "pipeweed_crop");
    public static final RegistryObject<Item> ELDERBERRY = food("elderberry", 2, 0.2F);
    public static final RegistryObject<Item> RASPBERRY = food("raspberry", 2, 0.2F);
    public static final RegistryObject<Item> WILDBERRY = food("wildberry", 2, 0.2F);
    public static final RegistryObject<Item> GRAPE_RED = food("grape_red", 2, 0.2F);
    public static final RegistryObject<Item> GRAPE_WHITE = food("grape_white", 2, 0.2F);
    public static final RegistryObject<Item> SEEDS_GRAPE_RED = simple("seeds_grape_red");
    public static final RegistryObject<Item> SEEDS_GRAPE_WHITE = simple("seeds_grape_white");
    public static final RegistryObject<Item> FLOUR = simple("flour");
    public static final RegistryObject<Item> DOUGH = simple("dough");

    public static final RegistryObject<Item> BRONZE_DAGGER = ITEMS.register("bronze_dagger", () -> new SwordItem(Tiers.IRON, 2, -1.7F, new Item.Properties()));
    public static final RegistryObject<Item> BRONZE_LONGSWORD = ITEMS.register("bronze_longsword", () -> new SwordItem(Tiers.IRON, 4, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> BRONZE_GREATSWORD = ITEMS.register("bronze_greatsword", () -> new SwordItem(Tiers.IRON, 6, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> ALLOY_STEEL_DAGGER = ITEMS.register("alloy_steel_dagger", () -> new SwordItem(Tiers.DIAMOND, 2, -1.7F, new Item.Properties()));
    public static final RegistryObject<Item> ALLOY_STEEL_LONGSWORD = ITEMS.register("alloy_steel_longsword", () -> new SwordItem(Tiers.DIAMOND, 4, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> ALLOY_STEEL_GREATSWORD = ITEMS.register("alloy_steel_greatsword", () -> new SwordItem(Tiers.DIAMOND, 6, -3.0F, new Item.Properties()));

    public static final List<RegistryObject<? extends Item>> MATERIAL_ITEMS = List.of(
            ALLOY_STEEL_INGOT,
            ALLOY_STEEL_NUGGET,
            BRONZE_INGOT,
            BRONZE_NUGGET,
            TIN_INGOT,
            RAW_TIN,
            SILVER_INGOT,
            SILVER_NUGGET,
            RAW_SILVER,
            OBSIDIAN_SHARD,
            VALYRIAN_STEEL_INGOT,
            VALYRIAN_STEEL_NUGGET,
            SALT,
            COIN_1,
            COIN_4,
            COIN_16,
            COIN_64,
            COIN_256,
            COIN_1024,
            COIN_4096,
            COIN_16384,
            FLAX,
            PIPEWEED_LEAF,
            FLOUR,
            DOUGH,
            AMBER, AMETHYST, BRONZE_RING, COBALT_BLUE, COPPER_RING, CORAL, DIAMOND,
            EMERALD, FEATHER_DYED, FUR, GATE_GEAR, GOLD_RING, HORN, ICE_SHARD,
            LION_FUR, OPAL, ORYX_HIDE, ORYX_HORN, PEBBLE, RED_CLAY_BALL, RHINO_HORN,
            RUBY, SALTPETER, SAPPHIRE, SILVER_RING, SULFUR, SWAN_FEATHER, TOPAZ,
            VALYRIAN_RING, WHITE_BISON_HORN,
            BEAVER_TAIL, BOUNTY_TROPHY, COPPER_NUGGET
    );

    public static final List<RegistryObject<? extends Item>> MISC_ITEMS = List.of(
            MUG, PIPE, SULFUR_MATCH, WATERSKIN, WHEEL,
            BLOOD_OF_TRUE_KINGS, BOTTLE_POISON, LEATHER_HAT, MYSTERY_WEB, QUEST_BOOK,
            POUCH_SMALL, POUCH_MEDIUM, POUCH_LARGE,
            GOTEquipment.COMMAND_HORN, SQUADRON_ITEM
    );

    public static final List<RegistryObject<? extends Item>> FOOD_ITEMS = List.of(
            ALMOND,
            APPLE_GREEN,
            BANANA,
            BANANA_BREAD,
            BLACKBERRY,
            BLUEBERRY,
            CHERRY,
            CHESTNUT,
            CHESTNUT_ROAST,
            CORN_BREAD,
            CORN_COOKED,
            CRANBERRY,
            CORN,
            LETTUCE,
            CUCUMBER,
            ELDERBERRY,
            RASPBERRY,
            WILDBERRY,
            GRAPE_RED,
            GRAPE_WHITE,
            APPLE_CRUMBLE, BANANA_CAKE, BEAVER_COOKED, BEAVER_RAW, BERRY_PIE, CAMEL_COOKED, CAMEL_RAW, CHERRY_PIE, CLAY_PLATE, DATE, DEER_COOKED, DEER_RAW, ELEPHANT_COOKED, ELEPHANT_RAW, GAMMON, GINGERBREAD, KEBAB, LEEK, LEEK_SOUP, LEMON, LEMON_CAKE, LIME, LION_COOKED, LION_RAW, MANGO, MAPLE_SYRUP, MARZIPAN, MARZIPAN_CHOCOLATE, MELON_SOUP, MUG_ALE, MUG_APPLE_JUICE, MUG_ARAQ, MUG_BANANA_BEER, MUG_BLACKBERRY_JUICE, MUG_BLUEBERRY_JUICE, MUG_BRANDY, MUG_CACTUS_LIQUEUR, MUG_CARROT_WINE, MUG_CHERRY_LIQUEUR, MUG_CHOCOLATE, MUG_CIDER, MUG_COCOA, MUG_CORN_LIQUOR, MUG_CRANBERRY_JUICE, MUG_ELDERBERRY_JUICE, MUG_ETHANOL, MUG_GIN, MUG_LEMON_LIQUEUR, MUG_LEMONADE, MUG_LIME_LIQUEUR, MUG_MANGO_JUICE, MUG_MAPLE_BEER, MUG_MEAD, MUG_MELON_LIQUEUR, MUG_MILK, MUG_ORANGE_JUICE, MUG_PERRY, MUG_PLANTAIN_BREW, MUG_PLUM_KVASS, MUG_POMEGRANATE_JUICE, MUG_POMEGRANATE_WINE, MUG_POPPY_MILK, MUG_RASPBERRY_JUICE, MUG_RED_GRAPE_JUICE, MUG_RED_WINE, MUG_RUM, MUG_SAMBUCA, MUG_SHADE_EVENING, MUG_SOUR_MILK, MUG_TERMITE_TEQUILA, MUG_UNSULLIED_TONIC, MUG_VODKA, MUG_WATER, MUG_WHISKY, MUG_WHITE_GRAPE_JUICE, MUG_WHITE_WINE, MUG_WILD_FIRE, MUSHROOM_PIE, MUTTON_COOKED, MUTTON_RAW, OLIVE, OLIVE_BREAD, ORANGE, PANCAKE, PANCAKE_MAPLE_SYRUP, PASTRY, PEAR, PEARL, PLUM, POMEGRANATE, RABBIT_COOKED, RABBIT_RAW, RAISINS, RHINO_COOKED, RHINO_RAW, RICE, SALTED_FLESH, SHISH_KEBAB, TURNIP, TURNIP_COOKED, WALRUS_LARD_COOKED, WALRUS_LARD_RAW, YAM, YAM_ROAST, ZEBRA_COOKED, ZEBRA_RAW
    );

    public static final List<RegistryObject<? extends Item>> NATURE_ITEMS = List.of(
            CORN_STALK,
            FLAX_SEEDS,
            CUCUMBER_SEEDS,
            PIPEWEED,
            PIPEWEED_SEEDS,
            SEEDS_GRAPE_RED,
            SEEDS_GRAPE_WHITE
    );

    public static final List<RegistryObject<? extends Item>> BASIC_EQUIPMENT = List.of(
            BRONZE_DAGGER,
            BRONZE_LONGSWORD,
            BRONZE_GREATSWORD,
            ALLOY_STEEL_DAGGER,
            ALLOY_STEEL_LONGSWORD,
            ALLOY_STEEL_GREATSWORD,
            GOTEquipment.COMMAND_SWORD
    );

    /**
     * Legacy aggregate retained for compatibility with any code that still expects the old list.
     * New creative tabs use the category-specific lists above.
     */
    public static final List<RegistryObject<? extends Item>> MIGRATED_CONTENT = List.of(
            ALLOY_STEEL_INGOT, ALLOY_STEEL_NUGGET, BRONZE_INGOT, BRONZE_NUGGET,
            TIN_INGOT, RAW_TIN, SILVER_INGOT, SILVER_NUGGET, RAW_SILVER,
            OBSIDIAN_SHARD, VALYRIAN_STEEL_INGOT, VALYRIAN_STEEL_NUGGET, SALT,
            COIN_1, COIN_4, COIN_16, COIN_64, COIN_256, COIN_1024, COIN_4096, COIN_16384,
            ALMOND, APPLE_GREEN, BANANA, BANANA_BREAD, BLACKBERRY, BLUEBERRY, CHERRY,
            CHESTNUT, CHESTNUT_ROAST, CORN_BREAD, CORN_COOKED, CRANBERRY,
            BRONZE_DAGGER, BRONZE_LONGSWORD, BRONZE_GREATSWORD,
            ALLOY_STEEL_DAGGER, ALLOY_STEEL_LONGSWORD, ALLOY_STEEL_GREATSWORD
    );

    private static RegistryObject<Item> seed(String id, String cropId) {
        return ITEMS.register(id, () -> new GOTSeedItem(cropId, new Item.Properties()));
    }

    private static RegistryObject<Item> food(String id, int nutrition, float saturation) {
        return ITEMS.register(id, () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(nutrition).saturationMod(saturation).build())));
    }

    /** Keeps legacy cakes and pies edible in-hand while making the same inventory item place its block. */
    private static RegistryObject<Item> placeableFood(String id, int nutrition, float saturation,
                                                       Supplier<? extends Block> block) {
        return ITEMS.register(id, () -> new BlockItem(block.get(), new Item.Properties().food(
                new FoodProperties.Builder().nutrition(nutrition).saturationMod(saturation).build())));
    }


    private static RegistryObject<Item> drink(String id) {
        return ITEMS.register(id, () -> new GOTDrinkItem(GOTDrinkDefinitions.get(id), new Item.Properties().stacksTo(16)));
    }

    private static RegistryObject<Item> simple(String id) {
        return ITEMS.register(id, () -> new Item(new Item.Properties()));
    }

    private GOTItems() {}

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }
}
