package got.world.structure.wildling;

import got.GOTMod;
import got.world.structure.north.NorthStructureBuilder;
import got.world.structure.north.NorthStructurePalette;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

/** Routes Wildling and Thenn spawners to the recovered legacy procedures. */
public final class WildlingStructureTemplates {
    private WildlingStructureTemplates() {}

    public static void generate(NorthStructureBuilder b, WildlingStructureType type) {
        switch (type) {
            case HOUSE -> got.world.structure.schematic.AuthoredSchematicTemplate.placePreservingTerrain(b, "Wildling_House.schem");
            case CHIEFTAIN_HOUSE -> got.world.structure.legacy.generated.GOTStructureWildlingChieftainHouse.place(b, 0);
            case THENN_HOUSE -> got.world.structure.legacy.generated.GOTStructureThennHouse.place(b, 0);
            case THENN_CHIEFTAIN_HOUSE -> got.world.structure.legacy.generated.GOTStructureThennChieftainHouse.place(b, 0);
            case SETTLEMENT, THENN_SETTLEMENT ->
                    throw new IllegalArgumentException("Settlement type requires WildlingSettlementGenerator: " + type);
        }
    }

    public static void house(NorthStructureBuilder b, boolean thenn, boolean trampOrBlacksmith) {
        NorthStructurePalette p = b.palette();
        int w = 4, d = 6, h = 4;
        b.foundation(-w, -d, w, d, Blocks.PODZOL.defaultBlockState());
        b.clear(-w, 0, -d, w, 10, d);
        b.fill(-3, 0, -5, 3, 0, 5, block("thatch_floor", Blocks.BROWN_TERRACOTTA));

        // Spruce frame and bowed plank walls from the original longhouse.
        for (int x : new int[]{-3, 3}) {
            for (int z : new int[]{-5, 0, 5}) b.fill(x, 1, z, x, h, z, p.log());
            b.fill(x, 1, -4, x, h, 4, p.planks());
        }
        b.fill(-2, 1, -5, 2, h, -5, p.planks());
        b.fill(-2, 1, 5, 2, h, 5, p.planks());
        b.door(0, 1, -5, Direction.NORTH);
        for (int x : new int[]{-2, 2}) {
            b.set(x, 2, -5, Blocks.IRON_BARS.defaultBlockState());
            b.set(x, 2, 5, Blocks.IRON_BARS.defaultBlockState());
        }
        woodenRoof(b, 4, -6, 6, h);

        b.set(0, 1, 3, block("straw_bed", Blocks.BROWN_BED));
        b.set(-2, 1, 3, block("table_wildling", Blocks.CRAFTING_TABLE));
        b.set(2, 1, 3, Blocks.FURNACE.defaultBlockState());
        b.set(-1, 1, 4, Blocks.CAULDRON.defaultBlockState());
        b.chest(1, 1, 4, Direction.NORTH, "beyond_wall");
        b.marker(thenn ? "thenn_banner" : "wildling_banner", 0, 4, 5);

        if (trampOrBlacksmith) {
            b.marker(thenn ? "thenn_blacksmith" : "hardhome_tramp", 0, 1, 0);
        } else {
            String prefix = thenn ? "thenn" : "wildling";
            b.marker(prefix + "_civilian_male", -1, 1, 0);
            b.marker(prefix + "_civilian_female", 1, 1, 0);
            b.marker(prefix + "_child", 0, 1, 1);
        }
    }

    public static void chieftainHouse(NorthStructureBuilder b, boolean thenn, boolean hardhome) {
        NorthStructurePalette p = b.palette();
        int w = 5, d = 6, h = 4;
        b.foundation(-w, -d, w, d, Blocks.PODZOL.defaultBlockState());
        b.clear(-w, 0, -d - 1, w, 13, d + 1);
        b.fill(-4, 0, -5, 4, 0, 5, block("thatch_floor", Blocks.BROWN_TERRACOTTA));

        for (int x : new int[]{-4, 4}) {
            for (int z : new int[]{-5, 0, 5}) b.fill(x, 1, z, x, h, z, p.log());
            b.fill(x, 1, -4, x, h, 4, p.planks());
        }
        b.fill(-3, 1, -5, 3, h, -5, p.planks());
        b.fill(-3, 1, 5, 3, h, 5, p.planks());
        b.door(0, 1, -5, Direction.NORTH);
        woodenRoof(b, 5, -6, 6, h);

        // The original hall is built around a deep central hearth and smoke vent.
        b.fill(-1, -5, -1, 1, 0, 1, Blocks.STONE.defaultBlockState());
        b.clear(0, -5, 0, 0, 10, 0);
        b.set(0, -6, 0, block("hearth", Blocks.CAMPFIRE));
        b.set(0, -5, 0, litCampfire());
        b.set(0, 0, 0, block("bronze_bars", Blocks.IRON_BARS));

        b.set(0, 1, 3, block("straw_bed", Blocks.BROWN_BED));
        b.set(-2, 1, 3, block("table_wildling", Blocks.CRAFTING_TABLE));
        b.set(2, 1, 3, Blocks.FURNACE.defaultBlockState());
        b.set(-2, 1, 2, Blocks.CAULDRON.defaultBlockState());
        b.chest(2, 1, 2, Direction.WEST, "beyond_wall");
        b.marker(thenn ? "thenn_banner" : "wildling_banner", 0, 5, -5);
        b.marker(thenn ? "thenn_banner" : "wildling_banner", -3, 3, 0);
        b.marker(thenn ? "thenn_banner" : "wildling_banner", 3, 3, 0);

        if (hardhome) {
            b.marker("mance_rayder", -1, 1, -1);
            b.marker("tormund", -1, 1, 0);
            b.marker("ygritte", 1, 1, 0);
        } else {
            b.marker(thenn ? "thenn_chieftain" : "wildling_chieftain", 0, 1, 0);
        }
    }

    public static void crastersKeep(NorthStructureBuilder b) {
        NorthStructurePalette p = b.palette();
        int w = 6, d = 10;
        b.foundation(-w, -d, w, d, Blocks.COARSE_DIRT.defaultBlockState());
        b.clear(-w, 0, -d, w, 11, d);

        // Craster's irregular timber keep, with an offset porch and stone chimney.
        b.fill(-3, 0, -5, 2, 0, 4, p.planks());
        b.hollowBox(-4, 0, -5, 3, 4, 5, p.planks(), p.planks());
        for (int x : new int[]{-4, 3}) for (int z : new int[]{-5, 5}) b.fill(x, 0, z, x, 4, z, p.log());
        b.clear(-1, 1, -5, 0, 2, -5);
        woodenRoof(b.child(0, 0, 0, 0, mix(b.seed(), 1, 0)), 5, -7, 6, 3);
        b.fill(-4, 0, -8, 3, 0, -6, p.planks());
        for (int x : new int[]{-4, -2, 1, 3}) b.fill(x, 0, -8, x, 3, -8, p.log());

        b.fill(1, 0, 1, 3, 4, 3, p.stone());
        b.clear(1, 1, 2, 1, 2, 2);
        b.set(1, 0, 2, block("hearth", Blocks.CAMPFIRE));
        b.set(1, 1, 2, litCampfire());
        b.fill(2, 5, 2, 2, 8, 2, p.stone());

        b.set(-3, 1, -1, block("table_wildling", Blocks.CRAFTING_TABLE));
        b.set(-3, 1, -2, Blocks.CAULDRON.defaultBlockState());
        b.set(-2, 1, 3, block("fur_bed", Blocks.GRAY_BED));
        b.set(-2, 1, 2, block("fur_bed", Blocks.GRAY_BED));
        b.chest(-3, 1, 0, Direction.EAST, "beyond_wall");
        b.chest(2, 1, -3, Direction.WEST, "beyond_wall");
        b.marker("craster", 0, 1, 0);
    }

    public static void crastersBarn(NorthStructureBuilder b) {
        NorthStructurePalette p = b.palette();
        int w = 7, minZ = -1, maxZ = 16, h = 5;
        b.foundation(-w, minZ, w, maxZ, Blocks.COARSE_DIRT.defaultBlockState());
        b.clear(-w, 0, minZ, w, 12, maxZ);
        b.fill(-5, 0, 0, 5, 0, 15, Blocks.COARSE_DIRT.defaultBlockState());

        for (int x : new int[]{-5, 5}) {
            for (int z = 0; z <= 15; z += 3) b.fill(x, 1, z, x, h, z, p.log());
            b.fill(x, 1, 0, x, h, 15, p.planks());
        }
        b.fill(-4, 1, 0, 4, h, 0, p.planks());
        b.fill(-4, 1, 15, 4, h, 15, p.planks());
        b.clear(-1, 1, 0, 1, 4, 0);
        b.fill(-1, 1, 0, 1, 4, 0, block("gate_iron_bars", Blocks.IRON_BARS));
        woodenRoof(b.child(0, 3, 7, 0, mix(b.seed(), 2, 0)), 7, -9, 9, 2);

        for (int z : new int[]{4, 8, 12}) {
            b.fill(-4, 1, z, -2, 2, z, p.fence());
            b.fill(2, 1, z, 4, 2, z, p.fence());
            b.set(-3, 1, z + 1, Blocks.HAY_BLOCK.defaultBlockState());
            b.set(3, 1, z + 1, Blocks.HAY_BLOCK.defaultBlockState());
            b.marker("craster_livestock", -3, 1, z - 1);
            b.marker("craster_livestock", 3, 1, z - 1);
        }
        b.chest(-4, 1, 13, Direction.EAST, "beyond_wall");
        b.chest(-4, 1, 14, Direction.EAST, "beyond_wall");
        b.set(4, 1, 14, block("table_wildling", Blocks.CRAFTING_TABLE));
        for (int i = 0; i < 11; i++) b.marker("craster_wife", (i % 5) - 2, 1, 5 + i / 5);
    }

    public static void well(NorthStructureBuilder b) {
        NorthStructurePalette p = b.palette();
        b.foundation(-2, -2, 2, 2, Blocks.COARSE_DIRT.defaultBlockState());
        b.fill(-1, -4, -1, 1, 0, 1, p.stone());
        b.fill(0, -3, 0, 0, -1, 0, Blocks.WATER.defaultBlockState());
        for (int x : new int[]{-1, 1}) for (int z : new int[]{-1, 1}) b.fill(x, 1, z, x, 3, z, p.fence());
        b.fill(-1, 4, -1, 1, 4, 1, p.woodSlab());
    }

    public static void hayBales(NorthStructureBuilder b) {
        b.fill(-2, 0, -2, 2, 1, 2, Blocks.HAY_BLOCK.defaultBlockState());
        b.fill(-1, 2, -1, 1, 2, 1, Blocks.HAY_BLOCK.defaultBlockState());
    }

    public static void whitetree(NorthStructureBuilder b) {
        BlockState log = block("weirwood_log", Blocks.BIRCH_LOG);
        BlockState leaves = block("weirwood_leaves", Blocks.BIRCH_LEAVES);
        b.foundation(-3, -3, 3, 3, Blocks.ROOTED_DIRT.defaultBlockState());
        b.clear(-8, 0, -8, 8, 22, 8);
        b.fill(-1, 0, -1, 1, 14, 1, log);
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            for (int step = 1; step <= 6; step++) {
                int x = direction.getStepX() * step;
                int z = direction.getStepZ() * step;
                b.set(x, Math.max(-1, 2 - step / 2), z, log);
            }
            for (int step = 1; step <= 5; step++) {
                int x = direction.getStepX() * step;
                int z = direction.getStepZ() * step;
                b.set(x, 11 + step / 2, z, log);
            }
        }
        for (int y = 9; y <= 17; y++) {
            int radius = y < 12 ? 5 : y < 16 ? 7 : 4;
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + z * z <= radius * radius + 2 && Math.floorMod(mix(b.seed() + y, x, z), 7L) != 0L) {
                        b.set(x, y, z, leaves);
                    }
                }
            }
        }
        b.marker("benjen_stark", 2, 1, 2);
    }

    private static void woodenRoof(NorthStructureBuilder b, int halfWidth, int minZ, int maxZ, int wallHeight) {
        NorthStructurePalette p = b.palette();
        for (int step = 0; step <= halfWidth; step++) {
            int y = wallHeight + step;
            int left = -halfWidth - 1 + step;
            int right = halfWidth + 1 - step;
            b.fill(left, y, minZ, left, y, maxZ,
                    NorthStructureBuilder.facing(p.woodStairs(), Direction.WEST));
            if (right != left) {
                b.fill(right, y, minZ, right, y, maxZ,
                        NorthStructureBuilder.facing(p.woodStairs(), Direction.EAST));
            }
        }
        b.fill(0, wallHeight + halfWidth + 1, minZ, 0, wallHeight + halfWidth + 1,
                maxZ, NorthStructureBuilder.topSlab(p.woodSlab()));
    }

    private static BlockState litCampfire() {
        BlockState state = Blocks.CAMPFIRE.defaultBlockState();
        return state.hasProperty(CampfireBlock.LIT) ? state.setValue(CampfireBlock.LIT, true) : state;
    }

    private static BlockState block(String id, Block fallback) {
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(GOTMod.MOD_ID, id));
        return block == null || block == Blocks.AIR ? fallback.defaultBlockState() : block.defaultBlockState();
    }

    private static long mix(long seed, long x, long z) {
        long value = seed ^ x * 341873128712L ^ z * 132897987541L;
        value ^= value >>> 33;
        value *= 0xff51afd7ed558ccdL;
        value ^= value >>> 33;
        value *= 0xc4ceb9fe1a85ec53L;
        return value ^ value >>> 33;
    }
}
