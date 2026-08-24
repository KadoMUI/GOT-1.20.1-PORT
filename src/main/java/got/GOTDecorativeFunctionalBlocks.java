package got;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/** Catch-up 3 catalogue. Kept separate so legacy one-off blocks do not bury the construction catalogue. */
public final class GOTDecorativeFunctionalBlocks {
    public static final Map<String, RegistryObject<Block>> ALL = new LinkedHashMap<>();
    public static final List<RegistryObject<Block>> CUTOUT = new ArrayList<>();
    public static final List<RegistryObject<Block>> TRANSLUCENT = new ArrayList<>();

    public static RegistryObject<Block> REED_BASKET;
    public static RegistryObject<Block> SANDSTONE_CHEST;
    public static RegistryObject<Block> STONE_CHEST;
    public static RegistryObject<Block> BOOKSHELF_STORAGE;
    public static RegistryObject<Block> WEAPON_RACK;
    public static RegistryObject<Block> FUR_BED;
    public static RegistryObject<Block> LION_FUR_BED;
    public static RegistryObject<Block> STRAW_BED;
    public static RegistryObject<Block> WILD_FIRE;
    public static RegistryObject<Block> WILD_FIRE_JAR;
    public static RegistryObject<Block> ASSHAI_TORCH;
    public static RegistryObject<Block> ASSHAI_WALL_TORCH;
    public static RegistryObject<Block> SOTHORYOS_DOUBLE_TORCH;
    public static RegistryObject<Block> FUSE;
    public static RegistryObject<Block> IRON_BANK;
    public static RegistryObject<Block> UNSMELTERY;
    public static RegistryObject<Block> KEBAB_STAND;
    public static RegistryObject<Block> KEBAB_STAND_SAND;
    public static RegistryObject<Block> BEACON;
    public static RegistryObject<Block> BANANA;
    public static RegistryObject<Block> DATE;
    public static RegistryObject<Block> SIGN_CARVED;
    public static RegistryObject<Block> SIGN_CARVED_GLOWING;

    public static RegistryObject<Block> APPLE_CRUMBLE;
    public static RegistryObject<Block> BANANA_CAKE;
    public static RegistryObject<Block> BERRY_PIE;
    public static RegistryObject<Block> CHERRY_PIE;
    public static RegistryObject<Block> LEMON_CAKE;
    public static RegistryObject<Block> PASTRY;

    private static boolean bootstrapped;

    public static synchronized void bootstrap() {
        if (bootstrapped) return;
        bootstrapped = true;

        registerBarsAndGlass();
        registerLightingAndFurniture();
        registerStorageAndMachines();
        registerGatesAndControls();
        registerPlaceableFoods();
        registerLegacyParityRemainder();
        registerPlants();
    }

    private static void registerBarsAndGlass() {
        for (String id : List.of("asshai_bars", "copper_bars", "bronze_bars", "silver_bars", "gold_bars", "valyrian_bars")) {
            cutout(id, () -> new IronBarsBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS)
                    .strength(5.0F, 10.0F).requiresCorrectToolForDrops()), false);
        }
        cutout("reed_bars", () -> new IronBarsBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS)
                .strength(0.6F).sound(SoundType.GRASS)), false);

        translucent("fine_glass", () -> new GlassBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion()), false);
        translucent("fine_glass_pane", () -> new IronBarsBlock(BlockBehaviour.Properties.copy(Blocks.GLASS_PANE).noOcclusion()), false);
        for (String color : colors()) {
            translucent(color + "_fine_glass", () -> new GlassBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion()), false);
            translucent(color + "_fine_glass_pane", () -> new IronBarsBlock(BlockBehaviour.Properties.copy(Blocks.GLASS_PANE).noOcclusion()), false);
        }
    }

    private static void registerLightingAndFurniture() {
        ASSHAI_TORCH = noItem("asshai_torch", () -> new TorchBlock(
                BlockBehaviour.Properties.copy(Blocks.SOUL_TORCH).lightLevel(state -> 14), ParticleTypes.SOUL_FIRE_FLAME));
        ASSHAI_WALL_TORCH = noItem("asshai_wall_torch", () -> new WallTorchBlock(
                BlockBehaviour.Properties.copy(Blocks.SOUL_WALL_TORCH).lightLevel(state -> 14),
                ParticleTypes.SOUL_FIRE_FLAME));
        GOTItems.ITEMS.register("asshai_torch", () -> new StandingAndWallBlockItem(
                ASSHAI_TORCH.get(), ASSHAI_WALL_TORCH.get(), new Item.Properties(), Direction.DOWN));
        GOTBlocks.DECORATION_BLOCKS.add(ASSHAI_TORCH);
        CUTOUT.add(ASSHAI_TORCH);
        CUTOUT.add(ASSHAI_WALL_TORCH);

        SOTHORYOS_DOUBLE_TORCH = decoration("sothoryos_double_torch", () -> new GOTDoubleTorchBlock(
                BlockBehaviour.Properties.copy(Blocks.TORCH).noCollission().lightLevel(
                        state -> state.getValue(GOTDoubleTorchBlock.HALF) == net.minecraft.world.level.block.state.properties.DoubleBlockHalf.UPPER ? 14 : 0)));
        CUTOUT.add(SOTHORYOS_DOUBLE_TORCH);
        FUSE = decoration("fuse", () -> new GOTDoubleTorchBlock(
                BlockBehaviour.Properties.copy(Blocks.TORCH).noCollission().lightLevel(state -> 0)));
        CUTOUT.add(FUSE);

        FUR_BED = decoration("fur_bed", () -> new GOTBedBlock(BlockBehaviour.Properties.copy(Blocks.BROWN_BED).noOcclusion()));
        LION_FUR_BED = decoration("lion_fur_bed", () -> new GOTBedBlock(BlockBehaviour.Properties.copy(Blocks.BROWN_BED).noOcclusion()));
        STRAW_BED = decoration("straw_bed", () -> new GOTBedBlock(BlockBehaviour.Properties.copy(Blocks.YELLOW_BED).noOcclusion()));
        CUTOUT.addAll(List.of(FUR_BED, LION_FUR_BED, STRAW_BED));

        registerRug("bear_rug_black");
        registerRug("bear_rug_dark");
        registerRug("bear_rug_light");
        registerRug("giraffe_rug");
        registerRug("lion_rug");
        registerRug("lioness_rug");

        KEBAB_STAND = decoration("kebab_stand", () -> new GOTKebabStandBlock(
                BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion().strength(1.0F)));
        KEBAB_STAND_SAND = decoration("kebab_stand_sand", () -> new GOTKebabStandBlock(
                BlockBehaviour.Properties.copy(Blocks.SANDSTONE).noOcclusion().strength(1.5F)));
        CUTOUT.addAll(List.of(KEBAB_STAND, KEBAB_STAND_SAND));

        for (String id : List.of("stalactite", "stalactite_ice", "stalactite_obsidian")) {
            RegistryObject<Block> block = decoration(id, () -> new GOTStalactiteBlock(
                    BlockBehaviour.Properties.copy(id.endsWith("obsidian") ? Blocks.OBSIDIAN
                            : id.endsWith("ice") ? Blocks.PACKED_ICE : Blocks.STONE).noOcclusion()));
            CUTOUT.add(block);
        }
        for (String id : List.of("treasure_copper", "treasure_gold", "treasure_silver", "treasure_valyrian")) {
            decoration(id, () -> new GOTTreasurePileBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK)
                    .strength(1.0F).noOcclusion().sound(SoundType.METAL)));
        }
    }

    private static void registerStorageAndMachines() {
        REED_BASKET = utility("reed_basket", () -> new GOTStorageBlock(
                BlockBehaviour.Properties.copy(Blocks.BARREL).strength(0.6F).sound(SoundType.GRASS)));
        SANDSTONE_CHEST = utility("sandstone_chest", () -> new GOTStorageBlock(
                BlockBehaviour.Properties.copy(Blocks.SANDSTONE).strength(3.0F).requiresCorrectToolForDrops()));
        STONE_CHEST = utility("stone_chest", () -> new GOTStorageBlock(
                BlockBehaviour.Properties.copy(Blocks.STONE).strength(3.0F).requiresCorrectToolForDrops()));
        BOOKSHELF_STORAGE = utility("bookshelf_storage", () -> new GOTStorageBlock(
                BlockBehaviour.Properties.copy(Blocks.BOOKSHELF)));
        WEAPON_RACK = decoration("weapon_rack", () -> new GOTWeaponRackBlock(
                BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion().strength(0.5F)));
        CUTOUT.add(WEAPON_RACK);

        IRON_BANK = utility("iron_bank", () -> new GOTIronBankBlock(
                BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(4.0F).requiresCorrectToolForDrops()));
        UNSMELTERY = utility("unsmeltery", () -> new GOTUnsmelteryBlock(
                BlockBehaviour.Properties.copy(Blocks.BLAST_FURNACE).strength(3.5F).requiresCorrectToolForDrops()
                        .lightLevel(state -> state.getValue(GOTUnsmelteryBlock.LIT) ? 13 : 0)));
        BEACON = utility("beacon", () -> new GOTBeaconBlock(
                BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(1.0F).noOcclusion()
                        .lightLevel(state -> state.getValue(GOTBeaconBlock.LIT) ? 15 : 0)));

        WILD_FIRE = noItem("wild_fire", () -> new GOTWildFireBlock(
                BlockBehaviour.Properties.copy(Blocks.FIRE).noCollission().instabreak().lightLevel(state -> 15).noLootTable()));
        WILD_FIRE_JAR = decoration("wild_fire_jar", () -> new GOTWildFireJarBlock(
                BlockBehaviour.Properties.copy(Blocks.GLASS).strength(0.5F).noOcclusion()
                        .lightLevel(state -> state.getValue(GOTWildFireJarBlock.LIT) ? 15 : 8)));
        CUTOUT.addAll(List.of(WILD_FIRE, WILD_FIRE_JAR));
    }

    private static void registerGatesAndControls() {
        for (String id : List.of("gate_copper_bars", "gate_bronze_bars", "gate_gold_bars",
                "gate_iron_bars", "gate_silver_bars", "gate_valyrian_bars")) {
            RegistryObject<Block> gate = utility(id, () -> new GOTConnectedGateBlock(
                    BlockBehaviour.Properties.copy(Blocks.IRON_BARS).strength(4.0F, 10.0F).noOcclusion(),
                    GOTSounds.STONE_GATE_OPEN, GOTSounds.STONE_GATE_CLOSE));
            CUTOUT.add(gate);
        }
        for (String id : List.of("gate_wooden", "gate_wooden_bars")) {
            RegistryObject<Block> gate = utility(id, () -> new GOTConnectedGateBlock(
                    BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(4.0F, 5.0F).noOcclusion(),
                    GOTSounds.GATE_OPEN, GOTSounds.GATE_CLOSE));
            CUTOUT.add(gate);
        }

        for (String stone : List.of("andesite", "basalt", "chalk", "diorite", "granite", "labradorite", "rhyolite")) {
            RegistryObject<Block> button = misc(stone + "_button", () -> new ButtonBlock(
                    BlockBehaviour.Properties.copy(Blocks.STONE_BUTTON), BlockSetType.STONE, 20, false));
            RegistryObject<Block> pressure = misc(stone + "_pressure_plate", () -> new PressurePlateBlock(
                    PressurePlateBlock.Sensitivity.EVERYTHING,
                    BlockBehaviour.Properties.copy(Blocks.STONE_PRESSURE_PLATE), BlockSetType.STONE));
            CUTOUT.add(button);
            CUTOUT.add(pressure);
        }
    }

    private static void registerPlaceableFoods() {
        APPLE_CRUMBLE = noItem("apple_crumble", () -> new CakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));
        BANANA_CAKE = noItem("banana_cake", () -> new CakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));
        BERRY_PIE = noItem("berry_pie", () -> new CakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));
        CHERRY_PIE = noItem("cherry_pie", () -> new CakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));
        LEMON_CAKE = noItem("lemon_cake", () -> new CakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));
        PASTRY = noItem("pastry", () -> new CakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));
        RegistryObject<Block> marzipan = decoration("marzipan_block", () -> new CakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));
        CUTOUT.addAll(List.of(APPLE_CRUMBLE, BANANA_CAKE, BERRY_PIE, CHERRY_PIE, LEMON_CAKE, PASTRY, marzipan));
    }

    /** The final sixteen named legacy variants left after the construction and main functional catalogues. */
    private static void registerLegacyParityRemainder() {
        for (int strength = 1; strength <= 3; strength++) {
            for (boolean fire : List.of(false, true)) {
                String id = "bomb" + (fire ? "_fire" : "") + (strength == 1 ? "" : strength == 2 ? "_double" : "_triple");
                float power = strength == 1 ? 4.0F : strength == 2 ? 7.0F : 10.0F;
                decoration(id, () -> new GOTBombBlock(power, fire,
                        BlockBehaviour.Properties.copy(Blocks.TNT).strength(3.0F, 0.0F).noOcclusion()));
            }
        }

        BANANA = noItem("banana", () -> new GOTHangingFruitBlock(
                BlockBehaviour.Properties.copy(Blocks.COCOA).noCollission().strength(0.2F).sound(SoundType.WOOD)));
        DATE = noItem("date", () -> new GOTHangingFruitBlock(
                BlockBehaviour.Properties.copy(Blocks.COCOA).noCollission().strength(0.2F).sound(SoundType.WOOD)));
        GOTBlocks.NATURE_BLOCKS.addAll(List.of(BANANA, DATE));
        CUTOUT.addAll(List.of(BANANA, DATE));

        utility("sarbacane_trap", () -> new GOTSarbacaneTrapBlock(4.0F,
                BlockBehaviour.Properties.copy(Blocks.DISPENSER).strength(2.5F).requiresCorrectToolForDrops()));
        utility("sarbacane_trap_gold", () -> new GOTSarbacaneTrapBlock(6.0F,
                BlockBehaviour.Properties.copy(Blocks.DISPENSER).strength(3.0F).requiresCorrectToolForDrops()));
        utility("sarbacane_trap_obsidian", () -> new GOTSarbacaneTrapBlock(8.0F,
                BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).strength(5.0F).requiresCorrectToolForDrops()));

        SIGN_CARVED = decoration("sign_carved", () -> new GOTCarvedSignBlock(false,
                BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(1.0F).noOcclusion()));
        SIGN_CARVED_GLOWING = decoration("sign_carved_glowing", () -> new GOTCarvedSignBlock(true,
                BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(1.0F).noOcclusion().lightLevel(state -> 7)));
        CUTOUT.addAll(List.of(SIGN_CARVED, SIGN_CARVED_GLOWING));

        nature("termite_mound", () -> new GOTTermiteMoundBlock(false,
                BlockBehaviour.Properties.copy(Blocks.PACKED_MUD).strength(0.8F).sound(SoundType.GRAVEL)));
        nature("infested_termite_mound", () -> new GOTTermiteMoundBlock(true,
                BlockBehaviour.Properties.copy(Blocks.PACKED_MUD).strength(0.8F).sound(SoundType.GRAVEL)));
        nature("waste_block", () -> new GOTWasteBlock(
                BlockBehaviour.Properties.copy(Blocks.SOUL_SAND).strength(0.6F).sound(SoundType.MUD)));
    }

    private static void registerPlants() {
        for (String id : List.of("blackroot", "bluebell", "marigold", "asshai_moss", "asshai_thorn",
                "clover", "four_leaf_clover", "chrysanthemum_blue", "chrysanthemum_pink",
                "chrysanthemum_yellow", "chrysanthemum_white", "flax_plant",
                "tall_grass_short", "tall_grass_flowery", "tall_grass_wheat", "tall_grass_thistle",
                "tall_grass_nettles", "tall_grass_jungle_sprout")) {
            plant(id, () -> new BushBlock(BlockBehaviour.Properties.copy(Blocks.FERN).noCollission().instabreak().sound(SoundType.GRASS)));
        }
        for (String id : List.of("black_iris", "yellow_iris", "hibiscus", "flame_of_east")) {
            plant(id, () -> new TallFlowerBlock(BlockBehaviour.Properties.copy(Blocks.ROSE_BUSH).noCollission()));
        }
        for (String id : List.of("red_sand_gem", "yellow_sand_gem", "southern_daisy", "southern_eastbells")) {
            plant(id, () -> new BushBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).noCollission()));
        }
        for (String id : List.of("oldwood_vines", "willow_vines")) {
            plant(id, () -> new VineBlock(BlockBehaviour.Properties.copy(Blocks.VINE).noCollission().randomTicks()));
        }
        plant("kelp", () -> new BushBlock(BlockBehaviour.Properties.copy(Blocks.KELP).noCollission()));
        plant("coral_reef", () -> new CarpetBlock(BlockBehaviour.Properties.copy(Blocks.MOSS_CARPET).noCollission()));
        noItemNature("marsh_lights", () -> new BushBlock(BlockBehaviour.Properties.copy(Blocks.TORCH)
                .noCollission().instabreak().lightLevel(state -> 12).noLootTable()));

        noItemNature("leek_crop", () -> new GOTCropBlock(
                BlockBehaviour.Properties.copy(Blocks.WHEAT).noCollission().randomTicks(), GOTItems.LEEK));
        noItemNature("turnip_crop", () -> new GOTCropBlock(
                BlockBehaviour.Properties.copy(Blocks.POTATOES).noCollission().randomTicks(), GOTItems.TURNIP));
        noItemNature("yam_crop", () -> new GOTCropBlock(
                BlockBehaviour.Properties.copy(Blocks.POTATOES).noCollission().randomTicks(), GOTItems.YAM));

        for (String wood : List.of("oak", "spruce", "birch", "jungle", "acacia", "dark_oak",
                "ibbinia", "catalpa", "ulthos", "ulthos_red", "aramant", "beech", "holly", "banana",
                "maple", "larch", "date_palm", "mangrove", "chestnut", "baobab", "cedar", "fir",
                "pine", "lemon", "orange", "lime", "mahogany", "willow", "cypress", "olive",
                "aspen", "green_oak", "fotinia", "almond", "plum", "redwood", "pomegranate", "palm",
                "dragon", "kanuka", "weirwood")) {
            RegistryObject<Block> leaves = nature(wood + "_fallen_leaves", () -> new CarpetBlock(
                    BlockBehaviour.Properties.copy(Blocks.MOSS_CARPET).strength(0.1F).sound(SoundType.GRASS).noOcclusion()));
            CUTOUT.add(leaves);
        }
    }

    private static void registerRug(String id) {
        RegistryObject<Block> rug = decoration(id, () -> new GOTRugBlock(
                BlockBehaviour.Properties.copy(Blocks.BROWN_CARPET).strength(0.1F).sound(SoundType.WOOL).noOcclusion()));
        CUTOUT.add(rug);
    }

    private static RegistryObject<Block> decoration(String id, Supplier<Block> supplier) {
        RegistryObject<Block> block = withItem(id, supplier);
        GOTBlocks.DECORATION_BLOCKS.add(block);
        return block;
    }

    private static RegistryObject<Block> utility(String id, Supplier<Block> supplier) {
        RegistryObject<Block> block = withItem(id, supplier);
        GOTBlocks.UTILITY_BLOCKS.add(block);
        return block;
    }

    private static RegistryObject<Block> misc(String id, Supplier<Block> supplier) {
        RegistryObject<Block> block = withItem(id, supplier);
        GOTBlocks.BUILDING_BLOCKS.add(block);
        return block;
    }

    private static RegistryObject<Block> nature(String id, Supplier<Block> supplier) {
        RegistryObject<Block> block = withItem(id, supplier);
        GOTBlocks.NATURE_BLOCKS.add(block);
        return block;
    }

    private static RegistryObject<Block> plant(String id, Supplier<Block> supplier) {
        RegistryObject<Block> block = nature(id, supplier);
        CUTOUT.add(block);
        return block;
    }

    private static RegistryObject<Block> noItemNature(String id, Supplier<Block> supplier) {
        RegistryObject<Block> block = noItem(id, supplier);
        GOTBlocks.NATURE_BLOCKS.add(block);
        CUTOUT.add(block);
        return block;
    }

    private static RegistryObject<Block> cutout(String id, Supplier<Block> supplier, boolean nature) {
        RegistryObject<Block> block = nature ? nature(id, supplier) : decoration(id, supplier);
        CUTOUT.add(block);
        return block;
    }

    private static RegistryObject<Block> translucent(String id, Supplier<Block> supplier, boolean nature) {
        RegistryObject<Block> block = nature ? nature(id, supplier) : decoration(id, supplier);
        TRANSLUCENT.add(block);
        return block;
    }

    private static RegistryObject<Block> withItem(String id, Supplier<Block> supplier) {
        RegistryObject<Block> block = noItem(id, supplier);
        GOTItems.ITEMS.register(id, () -> {
            if (id.equals("beacon")) {
                return new GOTLegacyMachineBlockItem(block.get(), new Item.Properties(),
                        GOTLegacyMachineBlockItem.Kind.BEACON);
            }
            if (id.equals("unsmeltery")) {
                return new GOTLegacyMachineBlockItem(block.get(), new Item.Properties(),
                        GOTLegacyMachineBlockItem.Kind.UNSMELTERY);
            }
            return new BlockItem(block.get(), new Item.Properties());
        });
        return block;
    }

    private static RegistryObject<Block> noItem(String id, Supplier<Block> supplier) {
        if (ALL.containsKey(id)) {
            throw new IllegalStateException("Duplicate decorative/functional block id " + id);
        }
        RegistryObject<Block> block = GOTBlocks.BLOCKS.register(id, supplier);
        ALL.put(id, block);
        return block;
    }

    private static List<String> colors() {
        return List.of("white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray",
                "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black");
    }

    private GOTDecorativeFunctionalBlocks() {}
}
