package got;

import got.construction.GOTConstructionFamilies;
import got.construction.GOTConstructionBases;
import got.crafting.GOTFactionCraftingTableBlock;
import got.world.flora.GOTSaplingBlock;
import got.world.flora.GOTTreeSpecies;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.StonecutterBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class GOTBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, GOTMod.MOD_ID);
    public static final List<RegistryObject<Block>> BUILDING_BLOCKS = new ArrayList<>();
    public static final List<RegistryObject<Block>> UTILITY_BLOCKS = new ArrayList<>();
    public static final List<RegistryObject<Block>> NATURE_BLOCKS = new ArrayList<>();

    public static final RegistryObject<Block> TIN_ORE = registerBlock("tin_ore",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_ORE).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> DEEPSLATE_TIN_ORE = registerBlock("deepslate_tin_ore",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> TIN_BLOCK = registerBlock("tin_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> BRONZE_BLOCK = registerBlock("bronze_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> SILVER_ORE = registerBlock("silver_ore",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.GOLD_ORE).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> DEEPSLATE_SILVER_ORE = registerBlock("deepslate_silver_ore",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_GOLD_ORE).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> SILVER_BLOCK = registerBlock("silver_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> ALLOY_STEEL_BLOCK = registerBlock("alloy_steel_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> VALYRIAN_STEEL_BLOCK = registerBlock("valyrian_steel_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).requiresCorrectToolForDrops()));

    // 0.5I: legacy underground resources. The original mod used one stone-backed
    // block for each of these deposits, so the port deliberately keeps one ID
    // instead of inventing separate deepslate variants for legacy-only ores.
    public static final RegistryObject<Block> SULFUR_ORE = registerBlock("sulfur_ore",
            () -> experienceOre(Blocks.COAL_ORE, 0, 2));
    public static final RegistryObject<Block> SALTPETER_ORE = registerBlock("saltpeter_ore",
            () -> experienceOre(Blocks.COAL_ORE, 0, 2));
    public static final RegistryObject<Block> SALT_ORE = registerBlock("salt_ore",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_ORE).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> GLOWSTONE_ORE = registerBlock("glowstone_ore",
            () -> experienceOre(Blocks.GOLD_ORE, 0, 2));
    public static final RegistryObject<Block> COBALT_ORE = registerBlock("cobalt_ore",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.GOLD_ORE).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> VALYRIAN_ORE = registerBlock("valyrian_ore",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE).strength(10.0F, 12.0F)
                    .requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> TOPAZ_ORE = registerBlock("topaz_ore",
            () -> experienceOre(Blocks.DIAMOND_ORE, 0, 2));
    public static final RegistryObject<Block> AMETHYST_ORE = registerBlock("amethyst_ore",
            () -> experienceOre(Blocks.DIAMOND_ORE, 0, 2));
    public static final RegistryObject<Block> SAPPHIRE_ORE = registerBlock("sapphire_ore",
            () -> experienceOre(Blocks.DIAMOND_ORE, 0, 2));
    public static final RegistryObject<Block> RUBY_ORE = registerBlock("ruby_ore",
            () -> experienceOre(Blocks.DIAMOND_ORE, 0, 2));
    public static final RegistryObject<Block> AMBER_ORE = registerBlock("amber_ore",
            () -> experienceOre(Blocks.DIAMOND_ORE, 0, 2));
    public static final RegistryObject<Block> DIAMOND_VEIN = registerBlock("diamond_vein",
            () -> experienceOre(Blocks.DIAMOND_ORE, 0, 2));
    public static final RegistryObject<Block> OPAL_ORE = registerBlock("opal_ore",
            () -> experienceOre(Blocks.DIAMOND_ORE, 0, 2));
    public static final RegistryObject<Block> EMERALD_VEIN = registerBlock("emerald_vein",
            () -> experienceOre(Blocks.EMERALD_ORE, 0, 2));

    public static final RegistryObject<Block> ANDESITE_BRICKS = registerBuildingBlock("andesite_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CARVED_ANDESITE_BRICKS = registerBuildingBlock("carved_andesite_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CRACKED_ANDESITE_BRICKS = registerBuildingBlock("cracked_andesite_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> MOSSY_ANDESITE_BRICKS = registerBuildingBlock("mossy_andesite_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> BASALT_BRICKS = registerBuildingBlock("basalt_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CRACKED_BASALT_BRICKS = registerBuildingBlock("cracked_basalt_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> DIORITE_BRICKS = registerBuildingBlock("diorite_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> RHYOLITE_BRICKS = registerBuildingBlock("rhyolite_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> SANDSTONE_BRICKS = registerBuildingBlock("sandstone_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CARVED_BASALT_BRICKS = registerBuildingBlock("carved_basalt_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> BASALT_WESTEROS_BRICKS = registerBuildingBlock("basalt_westeros_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> GRANITE_BRICKS = registerBuildingBlock("granite_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CARVED_DIORITE_BRICKS = registerBuildingBlock("carved_diorite_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CARVED_GRANITE_BRICKS = registerBuildingBlock("carved_granite_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> LHAZAR_BRICKS = registerBuildingBlock("lhazar_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CARVED_SANDSTONE_BRICKS = registerBuildingBlock("carved_sandstone_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CRACKED_SANDSTONE_BRICKS = registerBuildingBlock("cracked_sandstone_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> SANDSTONE_RED_BRICKS = registerBuildingBlock("sandstone_red_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CARVED_SANDSTONE_RED_BRICKS = registerBuildingBlock("carved_sandstone_red_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CRACKED_SANDSTONE_RED_BRICKS = registerBuildingBlock("cracked_sandstone_red_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CARVED_BASALT_WESTEROS_BRICKS = registerBuildingBlock("carved_basalt_westeros_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CHALK_BRICKS = registerBuildingBlock("chalk_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> SANDSTONE_LAPIS_BRICKS = registerBuildingBlock("sandstone_lapis_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> SOTHORYOS_BRICKS = registerBuildingBlock("sothoryos_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CRACKED_SOTHORYOS_BRICKS = registerBuildingBlock("cracked_sothoryos_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> SOTHORYOS_GOLD_BRICKS = registerBuildingBlock("sothoryos_gold_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> MOSSY_SOTHORYOS_BRICKS = registerBuildingBlock("mossy_sothoryos_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> SOTHORYOS_OBSIDIAN_BRICKS = registerBuildingBlock("sothoryos_obsidian_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> MUD_BRICKS = registerBuildingBlock("mud_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CARVED_RHYOLITE_BRICKS = registerBuildingBlock("carved_rhyolite_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> YI_TI_BRICKS = registerBuildingBlock("yi_ti_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CARVED_YI_TI_BRICKS = registerBuildingBlock("carved_yi_ti_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CRACKED_YI_TI_BRICKS = registerBuildingBlock("cracked_yi_ti_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> YI_TI_FLOWERS_BRICKS = registerBuildingBlock("yi_ti_flowers_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> MOSSY_YI_TI_BRICKS = registerBuildingBlock("mossy_yi_ti_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> LABRADORITE_BRICKS = registerBuildingBlock("labradorite_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CARVED_LABRADORITE_BRICKS = registerBuildingBlock("carved_labradorite_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CRACKED_LABRADORITE_BRICKS = registerBuildingBlock("cracked_labradorite_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> MOSSY_LABRADORITE_BRICKS = registerBuildingBlock("mossy_labradorite_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> SWORD_BRICKS = registerBuildingBlock("sword_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> YI_TI_GOLD_BRICKS = registerBuildingBlock("yi_ti_gold_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> YI_TI_GRANITE_BRICKS = registerBuildingBlock("yi_ti_granite_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CARVED_YI_TI_GRANITE_BRICKS = registerBuildingBlock("carved_yi_ti_granite_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> BRICK_ICE_BRICKS = registerBuildingBlock("brick_ice_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> TERRACOTTA_ROOF_TILES = registerBuildingBlock("terracotta_roof_tiles", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> BLACK_TERRACOTTA_ROOF_TILES = registerBuildingBlock("black_terracotta_roof_tiles", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> BLUE_TERRACOTTA_ROOF_TILES = registerBuildingBlock("blue_terracotta_roof_tiles", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> BROWN_TERRACOTTA_ROOF_TILES = registerBuildingBlock("brown_terracotta_roof_tiles", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CYAN_TERRACOTTA_ROOF_TILES = registerBuildingBlock("cyan_terracotta_roof_tiles", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> GRAY_TERRACOTTA_ROOF_TILES = registerBuildingBlock("gray_terracotta_roof_tiles", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> GREEN_TERRACOTTA_ROOF_TILES = registerBuildingBlock("green_terracotta_roof_tiles", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> LIGHT_BLUE_TERRACOTTA_ROOF_TILES = registerBuildingBlock("light_blue_terracotta_roof_tiles", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> LIME_TERRACOTTA_ROOF_TILES = registerBuildingBlock("lime_terracotta_roof_tiles", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> MAGENTA_TERRACOTTA_ROOF_TILES = registerBuildingBlock("magenta_terracotta_roof_tiles", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> ORANGE_TERRACOTTA_ROOF_TILES = registerBuildingBlock("orange_terracotta_roof_tiles", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> PINK_TERRACOTTA_ROOF_TILES = registerBuildingBlock("pink_terracotta_roof_tiles", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> PURPLE_TERRACOTTA_ROOF_TILES = registerBuildingBlock("purple_terracotta_roof_tiles", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> RED_TERRACOTTA_ROOF_TILES = registerBuildingBlock("red_terracotta_roof_tiles", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> SILVER_TERRACOTTA_ROOF_TILES = registerBuildingBlock("silver_terracotta_roof_tiles", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> WHITE_TERRACOTTA_ROOF_TILES = registerBuildingBlock("white_terracotta_roof_tiles", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> YELLOW_TERRACOTTA_ROOF_TILES = registerBuildingBlock("yellow_terracotta_roof_tiles", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> COBBLESTONE_BRICKS = registerBuildingBlock("cobblestone_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CONCRETE_BLACK = registerBuildingBlock("concrete_black", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CONCRETE_BLUE = registerBuildingBlock("concrete_blue", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CONCRETE_BROWN = registerBuildingBlock("concrete_brown", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CONCRETE_CYAN = registerBuildingBlock("concrete_cyan", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CONCRETE_GRAY = registerBuildingBlock("concrete_gray", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CONCRETE_GREEN = registerBuildingBlock("concrete_green", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CONCRETE_LIGHT_BLUE = registerBuildingBlock("concrete_light_blue", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CONCRETE_LIME = registerBuildingBlock("concrete_lime", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CONCRETE_MAGENTA = registerBuildingBlock("concrete_magenta", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CONCRETE_ORANGE = registerBuildingBlock("concrete_orange", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CONCRETE_PINK = registerBuildingBlock("concrete_pink", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CONCRETE_POWDER_BLACK = registerBuildingBlock("concrete_powder_black", () -> new FallingBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_CONCRETE_POWDER)));
    public static final RegistryObject<Block> CONCRETE_POWDER_BLUE = registerBuildingBlock("concrete_powder_blue", () -> new FallingBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_CONCRETE_POWDER)));
    public static final RegistryObject<Block> CONCRETE_POWDER_BROWN = registerBuildingBlock("concrete_powder_brown", () -> new FallingBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_CONCRETE_POWDER)));
    public static final RegistryObject<Block> CONCRETE_POWDER_CYAN = registerBuildingBlock("concrete_powder_cyan", () -> new FallingBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_CONCRETE_POWDER)));
    public static final RegistryObject<Block> CONCRETE_POWDER_GRAY = registerBuildingBlock("concrete_powder_gray", () -> new FallingBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_CONCRETE_POWDER)));
    public static final RegistryObject<Block> CONCRETE_POWDER_GREEN = registerBuildingBlock("concrete_powder_green", () -> new FallingBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_CONCRETE_POWDER)));
    public static final RegistryObject<Block> CONCRETE_POWDER_LIGHT_BLUE = registerBuildingBlock("concrete_powder_light_blue", () -> new FallingBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_CONCRETE_POWDER)));
    public static final RegistryObject<Block> CONCRETE_POWDER_LIME = registerBuildingBlock("concrete_powder_lime", () -> new FallingBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_CONCRETE_POWDER)));
    public static final RegistryObject<Block> CONCRETE_POWDER_MAGENTA = registerBuildingBlock("concrete_powder_magenta", () -> new FallingBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_CONCRETE_POWDER)));
    public static final RegistryObject<Block> CONCRETE_POWDER_ORANGE = registerBuildingBlock("concrete_powder_orange", () -> new FallingBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_CONCRETE_POWDER)));
    public static final RegistryObject<Block> CONCRETE_POWDER_PINK = registerBuildingBlock("concrete_powder_pink", () -> new FallingBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_CONCRETE_POWDER)));
    public static final RegistryObject<Block> CONCRETE_POWDER_PURPLE = registerBuildingBlock("concrete_powder_purple", () -> new FallingBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_CONCRETE_POWDER)));
    public static final RegistryObject<Block> CONCRETE_POWDER_RED = registerBuildingBlock("concrete_powder_red", () -> new FallingBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_CONCRETE_POWDER)));
    public static final RegistryObject<Block> CONCRETE_POWDER_SILVER = registerBuildingBlock("concrete_powder_silver", () -> new FallingBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_CONCRETE_POWDER)));
    public static final RegistryObject<Block> CONCRETE_POWDER_WHITE = registerBuildingBlock("concrete_powder_white", () -> new FallingBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_CONCRETE_POWDER)));
    public static final RegistryObject<Block> CONCRETE_POWDER_YELLOW = registerBuildingBlock("concrete_powder_yellow", () -> new FallingBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_CONCRETE_POWDER)));
    public static final RegistryObject<Block> CONCRETE_PURPLE = registerBuildingBlock("concrete_purple", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CONCRETE_RED = registerBuildingBlock("concrete_red", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CONCRETE_SILVER = registerBuildingBlock("concrete_silver", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CONCRETE_WHITE = registerBuildingBlock("concrete_white", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CONCRETE_YELLOW = registerBuildingBlock("concrete_yellow", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> DAUB = registerBuildingBlock("daub", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> APPLE_PLANKS = registerBuildingBlock("apple_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> ARAMANT_PLANKS = registerBuildingBlock("aramant_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> BANANA_PLANKS = registerBuildingBlock("banana_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> BEECH_PLANKS = registerBuildingBlock("beech_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> CATALPA_PLANKS = registerBuildingBlock("catalpa_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> CHARRED_PLANKS = registerBuildingBlock("charred_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> CHERRY_PLANKS = registerBuildingBlock("cherry_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> DATE_PALM_PLANKS = registerBuildingBlock("date_palm_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> HOLLY_PLANKS = registerBuildingBlock("holly_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> IBBINIA_PLANKS = registerBuildingBlock("ibbinia_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> LARCH_PLANKS = registerBuildingBlock("larch_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> MANGO_PLANKS = registerBuildingBlock("mango_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> MANGROVE_PLANKS = registerBuildingBlock("mangrove_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> MAPLE_PLANKS = registerBuildingBlock("maple_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> PEAR_PLANKS = registerBuildingBlock("pear_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> ULTHOS_PLANKS = registerBuildingBlock("ulthos_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> ALMOND_PLANKS = registerBuildingBlock("almond_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> ASPEN_PLANKS = registerBuildingBlock("aspen_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> BAOBAB_PLANKS = registerBuildingBlock("baobab_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> CEDAR_PLANKS = registerBuildingBlock("cedar_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> CHESTNUT_PLANKS = registerBuildingBlock("chestnut_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> CYPRESS_PLANKS = registerBuildingBlock("cypress_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> FIR_PLANKS = registerBuildingBlock("fir_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> FOTINIA_PLANKS = registerBuildingBlock("fotinia_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> GREEN_OAK_PLANKS = registerBuildingBlock("green_oak_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> LEMON_PLANKS = registerBuildingBlock("lemon_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> LIME_PLANKS = registerBuildingBlock("lime_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> MAHOGANY_PLANKS = registerBuildingBlock("mahogany_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> OLIVE_PLANKS = registerBuildingBlock("olive_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> ORANGE_PLANKS = registerBuildingBlock("orange_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> PINE_PLANKS = registerBuildingBlock("pine_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> WILLOW_PLANKS = registerBuildingBlock("willow_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> DRAGON_PLANKS = registerBuildingBlock("dragon_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> KANUKA_PLANKS = registerBuildingBlock("kanuka_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> PALM_PLANKS = registerBuildingBlock("palm_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> PLUM_PLANKS = registerBuildingBlock("plum_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> POMEGRANATE_PLANKS = registerBuildingBlock("pomegranate_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> REDWOOD_PLANKS = registerBuildingBlock("redwood_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> WEIRWOOD_PLANKS = registerBuildingBlock("weirwood_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> ROTTEN_PLANKS = registerBuildingBlock("rotten_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> CRACKED_RED_BRICKS = registerBuildingBlock("cracked_red_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> MOSSY_RED_BRICKS = registerBuildingBlock("mossy_red_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> THATCH_FLOOR = registerBuildingBlock("thatch_floor", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> THATCH_REED = registerBuildingBlock("thatch_reed", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> THATCH_THATCH = registerBuildingBlock("thatch_thatch", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));


    public static final List<RegistryObject<Block>> DECORATION_BLOCKS = new ArrayList<>();

    public static final RegistryObject<Block> WOODEN_PLATE = registerDecorationBlock("wooden_plate", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(0.5F).noOcclusion(), DecorationBlock.Shape.PLATE));
    public static final RegistryObject<Block> METAL_PLATE = registerDecorationBlock("metal_plate", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(0.5F).noOcclusion(), DecorationBlock.Shape.PLATE));
    public static final RegistryObject<Block> CERAMIC_PLATE = registerDecorationBlock("ceramic_plate", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(0.5F).noOcclusion(), DecorationBlock.Shape.PLATE));
    public static final RegistryObject<Block> WOODEN_GOBLET = registerDecorationBlock("wooden_goblet", got.common.block.GOTBlockGoblet.Wood::new);
    public static final RegistryObject<Block> COPPER_GOBLET = registerDecorationBlock("copper_goblet", got.common.block.GOTBlockGoblet.Copper::new);
    public static final RegistryObject<Block> BRONZE_GOBLET = registerDecorationBlock("bronze_goblet", got.common.block.GOTBlockGoblet.Bronze::new);
    public static final RegistryObject<Block> SILVER_GOBLET = registerDecorationBlock("silver_goblet", got.common.block.GOTBlockGoblet.Silver::new);
    public static final RegistryObject<Block> GOLD_GOBLET = registerDecorationBlock("gold_goblet", got.common.block.GOTBlockGoblet.Gold::new);
    public static final RegistryObject<Block> VALYRIAN_GOBLET = registerDecorationBlock("valyrian_goblet", got.common.block.GOTBlockGoblet.Valyrian::new);
    public static final RegistryObject<Block> WOODEN_MUG = registerDecorationBlock("wooden_mug", got.common.block.GOTBlockWoodenMug::new);
    public static final RegistryObject<Block> CLAY_MUG = registerDecorationBlock("clay_mug", got.common.block.GOTBlockClayMug::new);
    public static final RegistryObject<Block> CERAMIC_MUG = registerDecorationBlock("ceramic_mug", got.common.block.GOTBlockCeramicMug::new);
    public static final RegistryObject<Block> ALE_HORN = registerDecorationBlock("ale_horn", () -> new got.common.block.GOTBlockAleHorn(GOTDrinkVessel.DRINKING_HORN));
    public static final RegistryObject<Block> GOLDEN_ALE_HORN = registerDecorationBlock("golden_ale_horn", () -> new got.common.block.GOTBlockAleHorn(GOTDrinkVessel.GOLD_DRINKING_HORN));
    public static final RegistryObject<Block> SKULL_CUP = registerDecorationBlock("skull_cup", got.common.block.GOTBlockSkullCup::new);
    public static final RegistryObject<Block> WINE_GLASS = registerDecorationBlock("wine_glass", got.common.block.GOTBlockWineGlass::new);
    public static final RegistryObject<Block> GLASS_BOTTLE_VESSEL = BLOCKS.register("glass_bottle_vessel", got.common.block.GOTBlockGlassBottle::new);
    public static final RegistryObject<Block> WOOD_BIRD_CAGE = registerDecorationBlock("wood_bird_cage", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(0.5F).noOcclusion(), DecorationBlock.Shape.CAGE));
    public static final RegistryObject<Block> COPPER_BIRD_CAGE = registerDecorationBlock("copper_bird_cage", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS).strength(1.5F).noOcclusion(), DecorationBlock.Shape.CAGE));
    public static final RegistryObject<Block> BRONZE_BIRD_CAGE = registerDecorationBlock("bronze_bird_cage", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS).strength(1.5F).noOcclusion(), DecorationBlock.Shape.CAGE));
    public static final RegistryObject<Block> IRON_BIRD_CAGE = registerDecorationBlock("iron_bird_cage", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS).strength(1.5F).noOcclusion(), DecorationBlock.Shape.CAGE));
    public static final RegistryObject<Block> SILVER_BIRD_CAGE = registerDecorationBlock("silver_bird_cage", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS).strength(1.5F).noOcclusion(), DecorationBlock.Shape.CAGE));
    public static final RegistryObject<Block> GOLD_BIRD_CAGE = registerDecorationBlock("gold_bird_cage", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS).strength(1.5F).noOcclusion(), DecorationBlock.Shape.CAGE));
    public static final RegistryObject<Block> VALYRIAN_BIRD_CAGE = registerDecorationBlock("valyrian_bird_cage", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS).strength(1.5F).noOcclusion(), DecorationBlock.Shape.CAGE));
    public static final RegistryObject<Block> IRON_CHANDELIER = registerDecorationBlock("iron_chandelier", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS).strength(1.5F).noOcclusion().lightLevel(state -> 12), DecorationBlock.Shape.CHANDELIER));
    public static final RegistryObject<Block> COPPER_CHANDELIER = registerDecorationBlock("copper_chandelier", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS).strength(1.5F).noOcclusion().lightLevel(state -> 12), DecorationBlock.Shape.CHANDELIER));
    public static final RegistryObject<Block> BRONZE_CHANDELIER = registerDecorationBlock("bronze_chandelier", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS).strength(1.5F).noOcclusion().lightLevel(state -> 12), DecorationBlock.Shape.CHANDELIER));
    public static final RegistryObject<Block> SILVER_CHANDELIER = registerDecorationBlock("silver_chandelier", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS).strength(1.5F).noOcclusion().lightLevel(state -> 12), DecorationBlock.Shape.CHANDELIER));
    public static final RegistryObject<Block> GOLD_CHANDELIER = registerDecorationBlock("gold_chandelier", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS).strength(1.5F).noOcclusion().lightLevel(state -> 12), DecorationBlock.Shape.CHANDELIER));
    public static final RegistryObject<Block> VALYRIAN_CHANDELIER = registerDecorationBlock("valyrian_chandelier", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS).strength(1.5F).noOcclusion().lightLevel(state -> 12), DecorationBlock.Shape.CHANDELIER));
    public static final RegistryObject<Block> ASSHAI_CHANDELIER = registerDecorationBlock("asshai_chandelier", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS).strength(1.5F).noOcclusion().lightLevel(state -> 12), DecorationBlock.Shape.CHANDELIER));
    public static final RegistryObject<Block> HANGING_CHAIN = registerDecorationBlock("hanging_chain", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS).strength(1.5F).noOcclusion(), DecorationBlock.Shape.CHAIN));
    public static final RegistryObject<Block> ROPE = registerDecorationBlock("rope", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(0.5F).noOcclusion(), DecorationBlock.Shape.ROPE));
    public static final RegistryObject<Block> BUTTERFLY_JAR = registerDecorationBlock("butterfly_jar", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).strength(0.3F).noOcclusion(), DecorationBlock.Shape.JAR));
    public static final RegistryObject<Block> HEARTH = registerDecorationBlock("hearth", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(0.5F).noOcclusion().lightLevel(state -> 13), DecorationBlock.Shape.HEARTH));
    public static final RegistryObject<Block> KEBAB_SPIT = registerDecorationBlock("kebab_spit", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(0.5F).noOcclusion(), DecorationBlock.Shape.KEBAB));
    public static final RegistryObject<Block> ANDESITE_ROCK = registerDecorationBlock("andesite_rock", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(0.5F).noOcclusion(), DecorationBlock.Shape.ROCK));
    public static final RegistryObject<Block> BASALT_ROCK = registerDecorationBlock("basalt_rock", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(0.5F).noOcclusion(), DecorationBlock.Shape.ROCK));
    public static final RegistryObject<Block> BASALT_MOSS_ROCK = registerDecorationBlock("basalt_moss_rock", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(0.5F).noOcclusion(), DecorationBlock.Shape.ROCK));
    public static final RegistryObject<Block> CHALK_ROCK = registerDecorationBlock("chalk_rock", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(0.5F).noOcclusion(), DecorationBlock.Shape.ROCK));
    public static final RegistryObject<Block> DIORITE_ROCK = registerDecorationBlock("diorite_rock", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(0.5F).noOcclusion(), DecorationBlock.Shape.ROCK));
    public static final RegistryObject<Block> GRANITE_ROCK = registerDecorationBlock("granite_rock", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(0.5F).noOcclusion(), DecorationBlock.Shape.ROCK));
    public static final RegistryObject<Block> LABRADORITE_ROCK = registerDecorationBlock("labradorite_rock", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(0.5F).noOcclusion(), DecorationBlock.Shape.ROCK));
    public static final RegistryObject<Block> RHYOLITE_ROCK = registerDecorationBlock("rhyolite_rock", () -> new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(0.5F).noOcclusion(), DecorationBlock.Shape.ROCK));



    // Milestone 4.3 plants and agriculture
    public static final RegistryObject<Block> CORN_CROP = registerNatureBlockNoItem("corn_crop",
            () -> new GOTCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noCollission().randomTicks(), GOTItems.CORN_STALK));
    public static final RegistryObject<Block> FLAX_CROP = registerNatureBlockNoItem("flax_crop",
            () -> new GOTCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noCollission().randomTicks(), GOTItems.FLAX_SEEDS));
    public static final RegistryObject<Block> LETTUCE_CROP = registerNatureBlockNoItem("lettuce_crop",
            () -> new GOTCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noCollission().randomTicks(), GOTItems.LETTUCE));
    public static final RegistryObject<Block> CUCUMBER_CROP = registerNatureBlockNoItem("cucumber_crop",
            () -> new GOTCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noCollission().randomTicks(), GOTItems.CUCUMBER_SEEDS));
    public static final RegistryObject<Block> PIPEWEED_CROP = registerNatureBlockNoItem("pipeweed_crop",
            () -> new GOTCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noCollission().randomTicks(), GOTItems.PIPEWEED_SEEDS));

    public static final RegistryObject<Block> BLACKBERRY_BUSH = registerNatureBlock("blackberry_bush",
            () -> new GOTBerryBushBlock(BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).noCollission().randomTicks(), GOTItems.BLACKBERRY));
    public static final RegistryObject<Block> BLUEBERRY_BUSH = registerNatureBlock("blueberry_bush",
            () -> new GOTBerryBushBlock(BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).noCollission().randomTicks(), GOTItems.BLUEBERRY));
    public static final RegistryObject<Block> CRANBERRY_BUSH = registerNatureBlock("cranberry_bush",
            () -> new GOTBerryBushBlock(BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).noCollission().randomTicks(), GOTItems.CRANBERRY));
    public static final RegistryObject<Block> ELDERBERRY_BUSH = registerNatureBlock("elderberry_bush",
            () -> new GOTBerryBushBlock(BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).noCollission().randomTicks(), GOTItems.ELDERBERRY));
    public static final RegistryObject<Block> RASPBERRY_BUSH = registerNatureBlock("raspberry_bush",
            () -> new GOTBerryBushBlock(BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).noCollission().randomTicks(), GOTItems.RASPBERRY));
    public static final RegistryObject<Block> WILDBERRY_BUSH = registerNatureBlock("wildberry_bush",
            () -> new GOTBerryBushBlock(BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).noCollission().randomTicks(), GOTItems.WILDBERRY));

    public static final RegistryObject<Block> RED_GRAPEVINE = registerNatureBlock("red_grapevine",
            () -> new BushBlock(BlockBehaviour.Properties.copy(Blocks.VINE).noCollission().randomTicks()));
    public static final RegistryObject<Block> WHITE_GRAPEVINE = registerNatureBlock("white_grapevine",
            () -> new BushBlock(BlockBehaviour.Properties.copy(Blocks.VINE).noCollission().randomTicks()));

    public static final RegistryObject<Block> ASSHAI_FLOWER = registerNatureBlock("asshai_flower", () -> new BushBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).noCollission()));
    public static final RegistryObject<Block> CHRYSANTHEMUM_ORANGE = registerNatureBlock("chrysanthemum_orange", () -> new BushBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).noCollission()));
    public static final RegistryObject<Block> DEAD_MARSH_PLANT = registerNatureBlock("dead_marsh_plant", () -> new BushBlock(BlockBehaviour.Properties.copy(Blocks.DEAD_BUSH).noCollission()));
    public static final RegistryObject<Block> PLANTAIN = registerNatureBlock("plantain", () -> new BushBlock(BlockBehaviour.Properties.copy(Blocks.FERN).noCollission()));
    public static final RegistryObject<Block> ARID_GRASS = registerNatureBlock("arid_grass", () -> new BushBlock(BlockBehaviour.Properties.copy(Blocks.GRASS).noCollission()));
    public static final RegistryObject<Block> ASSHAI_GRASS = registerNatureBlock("asshai_grass", () -> new BushBlock(BlockBehaviour.Properties.copy(Blocks.GRASS).noCollission()));
    public static final RegistryObject<Block> QUENDITE_GRASS = registerNatureBlock("quendite_grass", () -> new BushBlock(BlockBehaviour.Properties.copy(Blocks.GRASS).noCollission()));
    public static final RegistryObject<Block> REEDS = registerNatureBlock("reeds", () -> new BushBlock(BlockBehaviour.Properties.copy(Blocks.SUGAR_CANE).noCollission()));
    public static final RegistryObject<Block> DRIED_REEDS = registerNatureBlock("dried_reeds", () -> new BushBlock(BlockBehaviour.Properties.copy(Blocks.DEAD_BUSH).noCollission()));


    // Legacy tree species used by Planetos biome decoration and sapling growth.
    public static final RegistryObject<Block> CATALPA_LOG = registerNatureBlock("catalpa_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> CATALPA_LEAVES = registerNatureBlock("catalpa_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> CATALPA_SAPLING = registerNatureBlock("catalpa_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.CATALPA, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> IBBINIA_LOG = registerNatureBlock("ibbinia_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> IBBINIA_LEAVES = registerNatureBlock("ibbinia_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> IBBINIA_SAPLING = registerNatureBlock("ibbinia_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.IBBINIA, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> ULTHOS_LOG = registerNatureBlock("ulthos_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> ULTHOS_LEAVES = registerNatureBlock("ulthos_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> ULTHOS_SAPLING = registerNatureBlock("ulthos_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.ULTHOS, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> ULTHOS_RED_LOG = registerNatureBlock("ulthos_red_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> ULTHOS_RED_LEAVES = registerNatureBlock("ulthos_red_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> ULTHOS_RED_SAPLING = registerNatureBlock("ulthos_red_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.ULTHOS_RED, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> ARAMANT_LOG = registerNatureBlock("aramant_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> ARAMANT_LEAVES = registerNatureBlock("aramant_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> ARAMANT_SAPLING = registerNatureBlock("aramant_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.ARAMANT, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> BANANA_LOG = registerNatureBlock("banana_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> BANANA_LEAVES = registerNatureBlock("banana_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> BANANA_SAPLING = registerNatureBlock("banana_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.BANANA, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> BEECH_LOG = registerNatureBlock("beech_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> BEECH_LEAVES = registerNatureBlock("beech_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> BEECH_SAPLING = registerNatureBlock("beech_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.BEECH, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> HOLLY_LOG = registerNatureBlock("holly_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> HOLLY_LEAVES = registerNatureBlock("holly_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> HOLLY_SAPLING = registerNatureBlock("holly_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.HOLLY, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> DATE_PALM_LOG = registerNatureBlock("date_palm_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> DATE_PALM_LEAVES = registerNatureBlock("date_palm_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> DATE_PALM_SAPLING = registerNatureBlock("date_palm_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.DATE_PALM, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> LARCH_LOG = registerNatureBlock("larch_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> LARCH_LEAVES = registerNatureBlock("larch_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> LARCH_SAPLING = registerNatureBlock("larch_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.LARCH, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> MANGROVE_LOG = registerNatureBlock("mangrove_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> MANGROVE_LEAVES = registerNatureBlock("mangrove_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> MANGROVE_SAPLING = registerNatureBlock("mangrove_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.MANGROVE, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> MAPLE_LOG = registerNatureBlock("maple_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> MAPLE_LEAVES = registerNatureBlock("maple_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> MAPLE_SAPLING = registerNatureBlock("maple_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.MAPLE, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> BAOBAB_LOG = registerNatureBlock("baobab_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> BAOBAB_LEAVES = registerNatureBlock("baobab_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> BAOBAB_SAPLING = registerNatureBlock("baobab_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.BAOBAB, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> CEDAR_LOG = registerNatureBlock("cedar_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> CEDAR_LEAVES = registerNatureBlock("cedar_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> CEDAR_SAPLING = registerNatureBlock("cedar_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.CEDAR, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> CHESTNUT_LOG = registerNatureBlock("chestnut_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> CHESTNUT_LEAVES = registerNatureBlock("chestnut_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> CHESTNUT_SAPLING = registerNatureBlock("chestnut_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.CHESTNUT, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> FIR_LOG = registerNatureBlock("fir_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> FIR_LEAVES = registerNatureBlock("fir_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> FIR_SAPLING = registerNatureBlock("fir_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.FIR, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> LEMON_LOG = registerNatureBlock("lemon_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> LEMON_LEAVES = registerNatureBlock("lemon_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> LEMON_SAPLING = registerNatureBlock("lemon_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.LEMON, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> LIME_LOG = registerNatureBlock("lime_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> LIME_LEAVES = registerNatureBlock("lime_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> LIME_SAPLING = registerNatureBlock("lime_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.LIME, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> ORANGE_LOG = registerNatureBlock("orange_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> ORANGE_LEAVES = registerNatureBlock("orange_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> ORANGE_SAPLING = registerNatureBlock("orange_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.ORANGE, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> PINE_LOG = registerNatureBlock("pine_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> PINE_LEAVES = registerNatureBlock("pine_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> PINE_SAPLING = registerNatureBlock("pine_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.PINE, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> CYPRESS_LOG = registerNatureBlock("cypress_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> CYPRESS_LEAVES = registerNatureBlock("cypress_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> CYPRESS_SAPLING = registerNatureBlock("cypress_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.CYPRESS, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> MAHOGANY_LOG = registerNatureBlock("mahogany_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> MAHOGANY_LEAVES = registerNatureBlock("mahogany_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> MAHOGANY_SAPLING = registerNatureBlock("mahogany_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.MAHOGANY, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> OLIVE_LOG = registerNatureBlock("olive_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> OLIVE_LEAVES = registerNatureBlock("olive_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> OLIVE_SAPLING = registerNatureBlock("olive_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.OLIVE, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> WILLOW_LOG = registerNatureBlock("willow_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> WILLOW_LEAVES = registerNatureBlock("willow_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> WILLOW_SAPLING = registerNatureBlock("willow_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.WILLOW, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> ALMOND_LOG = registerNatureBlock("almond_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> ALMOND_LEAVES = registerNatureBlock("almond_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> ALMOND_SAPLING = registerNatureBlock("almond_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.ALMOND, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> ASPEN_LOG = registerNatureBlock("aspen_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> ASPEN_LEAVES = registerNatureBlock("aspen_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> ASPEN_SAPLING = registerNatureBlock("aspen_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.ASPEN, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> FOTINIA_LOG = registerNatureBlock("fotinia_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> FOTINIA_LEAVES = registerNatureBlock("fotinia_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> FOTINIA_SAPLING = registerNatureBlock("fotinia_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.FOTINIA, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> GREEN_OAK_LOG = registerNatureBlock("green_oak_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> GREEN_OAK_LEAVES = registerNatureBlock("green_oak_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> GREEN_OAK_SAPLING = registerNatureBlock("green_oak_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.GREEN_OAK, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> PALM_LOG = registerNatureBlock("palm_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> PALM_LEAVES = registerNatureBlock("palm_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> PALM_SAPLING = registerNatureBlock("palm_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.PALM, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> PLUM_LOG = registerNatureBlock("plum_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> PLUM_LEAVES = registerNatureBlock("plum_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> PLUM_SAPLING = registerNatureBlock("plum_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.PLUM, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> POMEGRANATE_LOG = registerNatureBlock("pomegranate_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> POMEGRANATE_LEAVES = registerNatureBlock("pomegranate_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> POMEGRANATE_SAPLING = registerNatureBlock("pomegranate_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.POMEGRANATE, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> REDWOOD_LOG = registerNatureBlock("redwood_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> REDWOOD_LEAVES = registerNatureBlock("redwood_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> REDWOOD_SAPLING = registerNatureBlock("redwood_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.REDWOOD, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> DRAGON_LOG = registerNatureBlock("dragon_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> DRAGON_LEAVES = registerNatureBlock("dragon_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> DRAGON_SAPLING = registerNatureBlock("dragon_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.DRAGON, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> KANUKA_LOG = registerNatureBlock("kanuka_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> KANUKA_LEAVES = registerNatureBlock("kanuka_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> KANUKA_SAPLING = registerNatureBlock("kanuka_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.KANUKA, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> OAK_LOG = registerNatureBlock("oak_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> OAK_LEAVES = registerNatureBlock("oak_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> OAK_SAPLING = registerNatureBlock("oak_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.OAK, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));
    public static final RegistryObject<Block> WEIRWOOD_LOG = registerNatureBlock("weirwood_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> WEIRWOOD_LEAVES = registerNatureBlock("weirwood_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noOcclusion()));
    public static final RegistryObject<Block> WEIRWOOD_SAPLING = registerNatureBlock("weirwood_sapling", () -> new GOTSaplingBlock(GOTTreeSpecies.WEIRWOOD, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noCollission().randomTicks()));

    // Milestone 4.2 utility blocks
    public static final RegistryObject<Block> COMMAND_TABLE = registerUtilityBlock("command_table", () -> new GOTCommandTableBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(2.5F).noOcclusion()));
    public static final RegistryObject<Block> ALLOY_FORGE = registerUtilityBlock("alloy_forge", () -> new AlloyForgeBlock(BlockBehaviour.Properties.copy(Blocks.BLAST_FURNACE).strength(3.5F).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> OVEN = registerUtilityBlock("oven", () -> new OvenBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).strength(3.5F).requiresCorrectToolForDrops().lightLevel(state -> state.getValue(OvenBlock.LIT) ? 13 : 0)));
    public static final RegistryObject<Block> FERMENTATION_BARREL = registerUtilityBlock("fermentation_barrel", () -> new FermentationBarrelBlock(BlockBehaviour.Properties.copy(Blocks.BARREL).strength(2.5F)));
    public static final RegistryObject<Block> PLACED_DRINK_VESSEL = BLOCKS.register("placed_drink_vessel", () -> new PlacedDrinkVesselBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON).noOcclusion().strength(0.3F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> MILLSTONE = registerUtilityBlock("millstone", () -> new MillstoneBlock(BlockBehaviour.Properties.copy(Blocks.STONECUTTER).strength(3.5F).requiresCorrectToolForDrops()));
    // Legendary weapon conversion station; intentionally reuses the vanilla Stonecutter menu/GUI.
    public static final RegistryObject<Block> VALYRIAN_BLADE_CUTTER = registerUtilityBlock("valyrian_blade_cutter", () -> new StonecutterBlock(BlockBehaviour.Properties.copy(Blocks.STONECUTTER).strength(3.5F).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> TABLE_ARRYN = registerUtilityBlock("table_arryn", () -> new GOTFactionCraftingTableBlock("arryn", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_ASSHAI = registerUtilityBlock("table_asshai", () -> new GOTFactionCraftingTableBlock("asshai", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_BRAAVOS = registerUtilityBlock("table_braavos", () -> new GOTFactionCraftingTableBlock("braavos", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_CROWNLANDS = registerUtilityBlock("table_crownlands", () -> new GOTFactionCraftingTableBlock("crownlands", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_DORNE = registerUtilityBlock("table_dorne", () -> new GOTFactionCraftingTableBlock("dorne", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_DOTHRAKI = registerUtilityBlock("table_dothraki", () -> new GOTFactionCraftingTableBlock("dothraki", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_DRAGONSTONE = registerUtilityBlock("table_dragonstone", () -> new GOTFactionCraftingTableBlock("dragonstone", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_GHISCAR = registerUtilityBlock("table_ghiscar", () -> new GOTFactionCraftingTableBlock("ghiscar", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_GIFT = registerUtilityBlock("table_gift", () -> new GOTFactionCraftingTableBlock("gift", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_HILL_TRIBES = registerUtilityBlock("table_hill_tribes", () -> new GOTFactionCraftingTableBlock("hillmen", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_IBBEN = registerUtilityBlock("table_ibben", () -> new GOTFactionCraftingTableBlock("ibben", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_IRONBORN = registerUtilityBlock("table_ironborn", () -> new GOTFactionCraftingTableBlock("ironborn", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_JOGOS_NHAI = registerUtilityBlock("table_jogos_nhai", () -> new GOTFactionCraftingTableBlock("jogos_nhai", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_LHAZAR = registerUtilityBlock("table_lhazar", () -> new GOTFactionCraftingTableBlock("lhazar", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_LORATH = registerUtilityBlock("table_lorath", () -> new GOTFactionCraftingTableBlock("lorath", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_LYS = registerUtilityBlock("table_lys", () -> new GOTFactionCraftingTableBlock("lys", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_MOSSOVY = registerUtilityBlock("table_mossovy", () -> new GOTFactionCraftingTableBlock("mossovy", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_MYR = registerUtilityBlock("table_myr", () -> new GOTFactionCraftingTableBlock("myr", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_NORTH = registerUtilityBlock("table_north", () -> new GOTFactionCraftingTableBlock("north", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_NORVOS = registerUtilityBlock("table_norvos", () -> new GOTFactionCraftingTableBlock("norvos", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_PENTOS = registerUtilityBlock("table_pentos", () -> new GOTFactionCraftingTableBlock("pentos", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_QARTH = registerUtilityBlock("table_qarth", () -> new GOTFactionCraftingTableBlock("qarth", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_QOHOR = registerUtilityBlock("table_qohor", () -> new GOTFactionCraftingTableBlock("qohor", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_REACH = registerUtilityBlock("table_reach", () -> new GOTFactionCraftingTableBlock("reach", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_RIVERLANDS = registerUtilityBlock("table_riverlands", () -> new GOTFactionCraftingTableBlock("riverlands", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_SOTHORYOS = registerUtilityBlock("table_sothoryos", () -> new GOTFactionCraftingTableBlock("sothoryos", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_STORMLANDS = registerUtilityBlock("table_stormlands", () -> new GOTFactionCraftingTableBlock("stormlands", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_SUMMER = registerUtilityBlock("table_summer", () -> new GOTFactionCraftingTableBlock("summer", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_TYROSH = registerUtilityBlock("table_tyrosh", () -> new GOTFactionCraftingTableBlock("tyrosh", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_VOLANTIS = registerUtilityBlock("table_volantis", () -> new GOTFactionCraftingTableBlock("volantis", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_WESTERLANDS = registerUtilityBlock("table_westerlands", () -> new GOTFactionCraftingTableBlock("westerlands", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_WILDLING = registerUtilityBlock("table_wildling", () -> new GOTFactionCraftingTableBlock("fur", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> TABLE_YI_TI = registerUtilityBlock("table_yi_ti", () -> new GOTFactionCraftingTableBlock("yi_ti", BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));



    private static RegistryObject<Block> registerNatureBlock(String id, Supplier<Block> supplier) {
        RegistryObject<Block> block = registerBlock(id, supplier);
        NATURE_BLOCKS.add(block);
        return block;
    }

    private static RegistryObject<Block> registerNatureBlockNoItem(String id, Supplier<Block> supplier) {
        RegistryObject<Block> block = BLOCKS.register(id, supplier);
        NATURE_BLOCKS.add(block);
        return block;
    }

    private static RegistryObject<Block> registerDecorationBlock(String id, Supplier<Block> supplier) {
        RegistryObject<Block> block = registerBlock(id, supplier);
        DECORATION_BLOCKS.add(block);
        return block;
    }

    public static final List<RegistryObject<Block>> MIGRATED_BLOCKS = List.of(
            TIN_ORE, DEEPSLATE_TIN_ORE, TIN_BLOCK, BRONZE_BLOCK,
            SILVER_ORE, DEEPSLATE_SILVER_ORE, SILVER_BLOCK,
            ALLOY_STEEL_BLOCK, VALYRIAN_STEEL_BLOCK,
            SULFUR_ORE, SALTPETER_ORE, SALT_ORE, GLOWSTONE_ORE,
            COBALT_ORE, VALYRIAN_ORE, TOPAZ_ORE, AMETHYST_ORE,
            SAPPHIRE_ORE, RUBY_ORE, AMBER_ORE, DIAMOND_VEIN,
            OPAL_ORE, EMERALD_VEIN);

    private static Block experienceOre(Block template, int minimum, int maximum) {
        return new DropExperienceBlock(BlockBehaviour.Properties.copy(template)
                .requiresCorrectToolForDrops(), UniformInt.of(minimum, maximum));
    }

    private static RegistryObject<Block> registerBlock(String id, Supplier<Block> supplier) {
        RegistryObject<Block> block = BLOCKS.register(id, supplier);
        GOTItems.ITEMS.register(id, () -> {
            Block registered = block.get();
            if (registered instanceof PlacedDrinkVesselBlock vesselBlock) {
                return new GOTVesselBlockItem(registered, new Item.Properties(), vesselBlock.defaultVessel());
            }
            return new BlockItem(registered, new Item.Properties());
        });
        return block;
    }

    private static RegistryObject<Block> registerUtilityBlock(String id, Supplier<Block> supplier) {
        RegistryObject<Block> block = registerBlock(id, supplier);
        UTILITY_BLOCKS.add(block);
        return block;
    }

    private static RegistryObject<Block> registerBuildingBlock(String id, Supplier<Block> supplier) {
        RegistryObject<Block> block = registerBlock(id, supplier);
        BUILDING_BLOCKS.add(block);
        return block;
    }

    private GOTBlocks() {}

    public static void register(IEventBus modBus) {
        // Dynamic full blocks must exist before their stair/slab/etc. families.
        // Both catalogues must expand before the DeferredRegister is attached.
        GOTConstructionBases.bootstrap();
        GOTConstructionFamilies.bootstrap();
        GOTDecorativeFunctionalBlocks.bootstrap();
        BLOCKS.register(modBus);
    }
}
