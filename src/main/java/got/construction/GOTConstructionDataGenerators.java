package got.construction;

import got.GOTMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/** Data providers shared by every catalogue-driven construction family. */
@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class GOTConstructionDataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper files = event.getExistingFileHelper();

        generator.addProvider(event.includeClient(),
                new ConstructionBlockStateProvider(output, files));
        generator.addProvider(event.includeServer(),
                new ConstructionRecipeProvider(output));
        generator.addProvider(event.includeServer(),
                new ConstructionBlockTagsProvider(output, event.getLookupProvider(), files));
        generator.addProvider(event.includeServer(),
                new LootTableProvider(
                        output,
                        Set.of(),
                        List.of(new LootTableProvider.SubProviderEntry(
                                ConstructionBlockLoot::new,
                                LootContextParamSets.BLOCK))));
    }

    private static final class ConstructionBlockStateProvider extends BlockStateProvider {
        private ConstructionBlockStateProvider(PackOutput output, ExistingFileHelper files) {
            super(output, GOTMod.MOD_ID, files);
        }

        @Override
        protected void registerStatesAndModels() {
            for (ConstructionFamily family : GOTConstructionFamilies.families()) {
                for (ConstructionFamily.Variant variant : family.variants().values()) {
                    Block block = variant.block().get();
                    ResourceLocation texture = variant.textures().get("base");
                    switch (variant.part()) {
                        case STAIRS -> {
                            stairsBlock((StairBlock) block, texture);
                            simpleBlockItem(block, models().getExistingFile(blockModel(variant.id())));
                        }
                        case SLAB -> {
                            slabBlock((SlabBlock) block, family.baseModel(), texture);
                            simpleBlockItem(block, models().getExistingFile(blockModel(variant.id())));
                        }
                        case WALL -> {
                            wallBlock((WallBlock) block, texture);
                            simpleBlockItem(block,
                                    models().getExistingFile(blockModel(variant.id() + "_inventory")));
                        }
                        case FENCE -> {
                            fenceBlock((FenceBlock) block, texture);
                            simpleBlockItem(block,
                                    models().getExistingFile(blockModel(variant.id() + "_inventory")));
                        }
                        case FENCE_GATE -> {
                            fenceGateBlock((FenceGateBlock) block, texture);
                            simpleBlockItem(block, models().getExistingFile(blockModel(variant.id())));
                        }
                        case DOOR -> {
                            doorBlockWithRenderType(
                                    (DoorBlock) block,
                                    variant.textures().get("bottom"),
                                    variant.textures().get("top"),
                                    "cutout");
                            itemModels().singleTexture(
                                    variant.id(),
                                    mcLoc("item/generated"),
                                    "layer0",
                                    variant.textures().get("item"));
                        }
                        case TRAPDOOR -> {
                            trapdoorBlockWithRenderType(
                                    (TrapDoorBlock) block,
                                    variant.textures().get("trapdoor"),
                                    true,
                                    "cutout");
                            simpleBlockItem(block,
                                    models().getExistingFile(blockModel(variant.id() + "_bottom")));
                        }
                        case BEAM -> {
                            axisBlock(
                                    (RotatedPillarBlock) block,
                                    variant.textures().get("side"),
                                    variant.textures().get("end"));
                            simpleBlockItem(block, models().getExistingFile(blockModel(variant.id())));
                        }
                    }
                }
            }
        }

        private ResourceLocation blockModel(String id) {
            return modLoc("block/" + id);
        }
    }

    private static final class ConstructionBlockLoot extends BlockLootSubProvider {
        private ConstructionBlockLoot() {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected void generate() {
            for (ConstructionFamily.Variant variant : GOTConstructionFamilies.variants().values()) {
                Block block = variant.block().get();
                switch (variant.part()) {
                    case SLAB -> add(block, createSlabItemTable(block));
                    case DOOR -> add(block, createDoorTable(block));
                    default -> dropSelf(block);
                }
            }
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return GOTConstructionFamilies.variants().values().stream()
                    .map(variant -> variant.block().get())
                    .toList();
        }
    }

    private static final class ConstructionRecipeProvider extends RecipeProvider {
        private ConstructionRecipeProvider(PackOutput output) {
            super(output);
        }

        @Override
        protected void buildRecipes(Consumer<FinishedRecipe> output) {
            for (ConstructionFamily family : GOTConstructionFamilies.families()) {
                Block base = resolveBase(family.baseBlock());
                for (ConstructionFamily.Variant variant : family.variants().values()) {
                    Block result = variant.block().get();
                    String unlock = "has_" + family.baseBlock().getPath();
                    switch (variant.part()) {
                        case STAIRS -> {
                            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 4)
                                    .define('#', base)
                                    .pattern("#  ")
                                    .pattern("## ")
                                    .pattern("###")
                                    .unlockedBy(unlock, has(base))
                                    .save(output);
                            if (family.material() == ConstructionMaterial.MASONRY) {
                                stonecut(output, base, result, 1, variant.id());
                            }
                        }
                        case SLAB -> {
                            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 6)
                                    .define('#', base)
                                    .pattern("###")
                                    .unlockedBy(unlock, has(base))
                                    .save(output);
                            if (family.material() == ConstructionMaterial.MASONRY) {
                                stonecut(output, base, result, 2, variant.id());
                            }
                        }
                        case WALL -> {
                            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 6)
                                    .define('#', base)
                                    .pattern("###")
                                    .pattern("###")
                                    .unlockedBy(unlock, has(base))
                                    .save(output);
                            if (family.material() == ConstructionMaterial.MASONRY) {
                                stonecut(output, base, result, 1, variant.id());
                            }
                        }
                        case FENCE -> ShapedRecipeBuilder
                                .shaped(RecipeCategory.DECORATIONS, result, 3)
                                .define('#', base)
                                .define('S', Items.STICK)
                                .pattern("#S#")
                                .pattern("#S#")
                                .unlockedBy(unlock, has(base))
                                .save(output);
                        case FENCE_GATE -> ShapedRecipeBuilder
                                .shaped(RecipeCategory.REDSTONE, result)
                                .define('#', base)
                                .define('S', Items.STICK)
                                .pattern("S#S")
                                .pattern("S#S")
                                .unlockedBy(unlock, has(base))
                                .save(output);
                        case DOOR -> ShapedRecipeBuilder
                                .shaped(RecipeCategory.REDSTONE, result, 3)
                                .define('#', base)
                                .pattern("##")
                                .pattern("##")
                                .pattern("##")
                                .unlockedBy(unlock, has(base))
                                .save(output);
                        case TRAPDOOR -> ShapedRecipeBuilder
                                .shaped(RecipeCategory.REDSTONE, result, 2)
                                .define('#', base)
                                .pattern("###")
                                .pattern("###")
                                .unlockedBy(unlock, has(base))
                                .save(output);
                        case BEAM -> ShapedRecipeBuilder
                                .shaped(RecipeCategory.BUILDING_BLOCKS, result, 3)
                                .define('#', base)
                                .pattern("#")
                                .pattern("#")
                                .pattern("#")
                                .unlockedBy(unlock, has(base))
                                .save(output);
                    }
                }
            }
        }

        private static void stonecut(
                Consumer<FinishedRecipe> output,
                Block base,
                Block result,
                int count,
                String id) {
            SingleItemRecipeBuilder.stonecutting(
                            Ingredient.of(base),
                            RecipeCategory.BUILDING_BLOCKS,
                            result,
                            count)
                    .unlockedBy("has_" + id, has(base))
                    .save(output, new ResourceLocation(
                            GOTMod.MOD_ID, id + "_from_stonecutting"));
        }

        private static Block resolveBase(ResourceLocation id) {
            Block block = net.minecraftforge.registries.ForgeRegistries.BLOCKS.getValue(id);
            if (block == null) {
                throw new IllegalStateException("Missing construction recipe base " + id);
            }
            return block;
        }
    }

    private static final class ConstructionBlockTagsProvider extends BlockTagsProvider {
        private ConstructionBlockTagsProvider(
                PackOutput output,
                CompletableFuture<HolderLookup.Provider> lookup,
                ExistingFileHelper files) {
            super(output, lookup, GOTMod.MOD_ID, files);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            for (ConstructionFamily family : GOTConstructionFamilies.families()) {
                for (ConstructionFamily.Variant variant : family.variants().values()) {
                    Block block = variant.block().get();
                    switch (family.material()) {
                        case MASONRY -> tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block);
                        case WOOD -> tag(BlockTags.MINEABLE_WITH_AXE).add(block);
                        case THATCH -> tag(BlockTags.MINEABLE_WITH_HOE).add(block);
                        case EARTH -> tag(BlockTags.MINEABLE_WITH_SHOVEL).add(block);
                        case MISC -> {
                            // Intentionally unclassified; catalogue authors must opt in.
                        }
                    }

                    switch (variant.part()) {
                        case STAIRS -> {
                            tag(BlockTags.STAIRS).add(block);
                            if (family.material().isWoodLike()) {
                                tag(BlockTags.WOODEN_STAIRS).add(block);
                            }
                        }
                        case SLAB -> {
                            tag(BlockTags.SLABS).add(block);
                            if (family.material().isWoodLike()) {
                                tag(BlockTags.WOODEN_SLABS).add(block);
                            }
                        }
                        case WALL -> tag(BlockTags.WALLS).add(block);
                        case FENCE -> {
                            tag(BlockTags.FENCES).add(block);
                            if (family.material().isWoodLike()) {
                                tag(BlockTags.WOODEN_FENCES).add(block);
                            }
                        }
                        case FENCE_GATE -> tag(BlockTags.FENCE_GATES).add(block);
                        case DOOR -> tag(BlockTags.WOODEN_DOORS).add(block);
                        case TRAPDOOR -> tag(BlockTags.WOODEN_TRAPDOORS).add(block);
                        case BEAM -> {
                            // Beams intentionally are not placed in the log tags; doing so
                            // would silently make them valid fuel/recipe substitutes.
                        }
                    }
                }
            }
        }
    }

    private GOTConstructionDataGenerators() {
    }
}
