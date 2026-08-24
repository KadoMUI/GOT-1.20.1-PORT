package got.world.structure.nightwatch;

import got.GOTMod;
import got.world.structure.north.NorthStructureBuilder;
import got.world.structure.north.NorthStructurePalette;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

/** Routes the Gift/Night's Watch family to the recovered legacy procedures. */
public final class NightWatchStructureTemplates {
    public enum CastleKind { ABANDONED, CASTLE_BLACK, EASTWATCH, SHADOW_TOWER }

    private NightWatchStructureTemplates() {}

    public static void generate(NorthStructureBuilder b, NightWatchStructureType type) {
        switch (type) {
            case HOUSE_SMALL -> got.world.structure.legacy.generated.GOTStructureGiftHouseSmall.place(b, 0);
            case HOUSE -> got.world.structure.legacy.generated.GOTStructureGiftHouse.place(b, 0);
            case STABLES -> got.world.structure.legacy.generated.GOTStructureGiftStables.place(b, 0);
            case SMITHY -> got.world.structure.legacy.generated.GOTStructureGiftSmithy.place(b, 0);
            case VILLAGE -> village(b);
        }
    }

    public static void village(NorthStructureBuilder b) {
        Random random = new Random(b.seed());
        b.marker("gift_population_respawner", 0, 0, 0);
        b.marker("gift_archer_respawner", 0, 0, 0);
        got.world.structure.legacy.generated.GOTStructureGiftWell.place(
                b.child(0, 0, -2, 0, mix(b.seed(), 0, -2)), 0);
        for (int x : new int[]{-8, 8}) {
            for (int z : new int[]{-8, 8})
                got.world.structure.legacy.generated.GOTStructureGiftVillageLight.place(
                        b.child(x, 0, z, 0, mix(b.seed(), x, z)), 0);
        }

        for (int index = 0; index < 20; index++) {
            double fraction = (index + 1) / 20.0D;
            double turn = fraction * Math.PI * 2.0D;
            double sin = Math.sin(turn);
            double cos = Math.cos(turn);
            if (sin < 0.0D && Math.abs(cos) <= 0.5D) continue;
            int rotation = fraction * 8.0D >= 3.0D && fraction * 8.0D < 5.0D ? 1
                    : fraction * 8.0D >= 5.0D && fraction * 8.0D < 7.0D ? 2
                    : fraction * 8.0D >= 7.0D || fraction * 8.0D < 1.0D ? 3 : 0;
            if (random.nextInt(3) != 0) {
                int radius = random.nextInt(3) == 0 ? 34 : 22;
                int x = (int)Math.round(radius * cos);
                int z = (int)Math.round(radius * sin);
                NorthStructureBuilder child = b.child(x, 0, z, rotation, random.nextLong());
                if (random.nextInt(3) == 0) switch (random.nextInt(3)) {
                    case 0 -> got.world.structure.legacy.generated.GOTStructureGiftSmithy.place(child, 0);
                    case 1 -> got.world.structure.legacy.generated.GOTStructureGiftStables.place(child, 0);
                    default -> got.world.structure.legacy.generated.GOTStructureGiftHouse.place(child, 0);
                } else got.world.structure.legacy.generated.GOTStructureGiftHouseSmall.place(child, 0);
            } else if (random.nextInt(4) == 0) {
                    int radius = random.nextInt(3) == 0 ? 36 : 24;
                    hayBales(b.child((int)Math.round(radius * cos), 0,
                            (int)Math.round(radius * sin), rotation, random.nextLong()));
            }
        }

        // Legacy 47-block circular palisade, omitting the southern gate.
        int radius = 47;
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                int distanceSq = x * x + z * z;
                if ((Math.abs(x) <= 5 && z < 0)
                        || distanceSq < radius * radius
                        || distanceSq >= (radius + 1) * (radius + 1)) continue;
                got.world.structure.legacy.generated.GOTStructureGiftVillagePalisade.place(
                        b.child(x, 0, z, 0, mix(b.seed(), x, z)), 0);
            }
        }
        for (int x = -22; x <= 22; x++) for (int z = -47; z <= 22; z++) {
            int distanceSq = x * x + z * z;
            if (distanceSq < 20 * 20 || (z < 0 && z > -47 && Math.abs(x) <= 3)) {
                b.set(x, -1, z, Blocks.COARSE_DIRT.defaultBlockState());
                b.clear(x, 0, z, x, 2, z);
            }
        }
        b.marker("gift_guard", -4, 1, -42);
        b.marker("gift_guard", 4, 1, -42);
    }

    public static void fixedCastle(NorthStructureBuilder b, CastleKind kind) {
        switch (kind) {
            case CASTLE_BLACK -> {
                got.world.structure.legacy.generated.GOTStructureGiftGate.place(
                        b.child(0, 0, 7, 0, mix(b.seed(), 0, 7)), 0);
                got.world.structure.legacy.generated.GOTStructureGiftCastle.place(
                        b.child(-4, 0, 25, 1, mix(b.seed(), -4, 25)), 0);
                legendaryMarkers(b, kind);
            }
            case SHADOW_TOWER -> {
                got.world.structure.legacy.generated.GOTStructureGiftGate.place(
                        b.child(0, 0, 7, 0, mix(b.seed(), 0, 7)), 0);
                got.world.structure.legacy.generated.GOTStructureGiftCastle.place(
                        b.child(0, 0, 20, 0, mix(b.seed(), 0, 20)), 0);
                legendaryMarkers(b, kind);
            }
            case EASTWATCH -> {
                got.world.structure.legacy.generated.GOTStructureGiftCastle.place(
                        b.child(0, 0, 50, 0, mix(b.seed(), 0, 50)), 0);
                legendaryMarkers(b, kind);
            }
            case ABANDONED -> {
                got.world.structure.legacy.generated.GOTStructureGiftGate.place(
                        b.child(0, 0, 7, 0, mix(b.seed(), 0, 7)), 1);
                got.world.structure.legacy.generated.GOTStructureGiftCastle.place(
                        b.child(0, 0, 20, 0, mix(b.seed(), 0, 20)), 1);
            }
        }
    }

    private static void houseSmall(NorthStructureBuilder b) {
        NorthStructurePalette p = b.palette();
        int w = 4, d = 5, h = 4;
        b.foundation(-w, -d, w, d, p.stone());
        b.hollowBox(-w, 0, -d, w, h, d, p.daub(), p.planks());
        b.timberFrame(-w, -d, w, d, h);
        b.door(0, 1, -d, Direction.NORTH);
        windows(b, w, d, 2);
        b.gabledRoof(w, -d, d, h);
        b.set(-2, 1, 2, block("table_gift", Blocks.CRAFTING_TABLE));
        b.set(2, 1, 2, Blocks.FURNACE.defaultBlockState());
        b.set(0, 1, 3, block("straw_bed", Blocks.BLACK_BED));
        b.chest(-2, 1, -2, Direction.SOUTH, "gift");
        b.marker("gift_civilian", 0, 1, 1);
        b.marker("gift_civilian", 1, 1, 1);
    }

    private static void house(NorthStructureBuilder b) {
        NorthStructurePalette p = b.palette();
        int w = 5, d = 7, h = 5;
        b.foundation(-w, -d, w, d, p.stone());
        b.hollowBox(-w, 0, -d, w, h, d, p.daub(), p.planks());
        b.timberFrame(-w, -d, w, d, h);
        b.fill(-w + 1, 3, 0, w - 1, 3, 0, p.log());
        b.door(0, 1, -d, Direction.NORTH);
        windows(b, w, d, 2);
        b.gabledRoof(w, -d, d, h);
        b.set(-3, 1, 4, block("table_gift", Blocks.CRAFTING_TABLE));
        b.set(3, 1, 4, Blocks.FURNACE.defaultBlockState());
        b.set(-3, 1, -1, block("straw_bed", Blocks.BLACK_BED));
        b.set(3, 1, -1, block("straw_bed", Blocks.BLACK_BED));
        b.chest(-1, 4, 3, Direction.NORTH, "gift");
        b.chest(1, 4, 3, Direction.NORTH, "gift");
        b.marker("gift_civilian", -1, 1, 2);
        b.marker("gift_civilian", 1, 1, 2);
        b.marker("gift_child", 0, 1, 1);
    }

    private static void stables(NorthStructureBuilder b) {
        NorthStructurePalette p = b.palette();
        int w = 6, d = 8, h = 5;
        b.foundation(-w, -d, w, d, Blocks.COBBLESTONE.defaultBlockState());
        b.hollowBox(-w, 0, -d, w, h, d, p.planks(), Blocks.COARSE_DIRT.defaultBlockState());
        b.timberFrame(-w, -d, w, d, h);
        b.clear(-2, 1, -d, 2, 3, -d);
        b.gabledRoof(w, -d, d, h);
        for (int x : new int[]{-4, 0, 4}) b.fill(x, 1, -1, x, 2, 6, p.fence());
        b.set(-3, 1, 5, Blocks.HAY_BLOCK.defaultBlockState());
        b.set(3, 1, 5, Blocks.HAY_BLOCK.defaultBlockState());
        b.set(-3, 5, 4, block("straw_bed", Blocks.BLACK_BED));
        b.chest(-3, 5, 6, Direction.SOUTH, "gift");
        b.marker("gift_stablemaster", 0, 1, 2);
        b.marker("gift_horse", -2, 1, 6);
        b.marker("gift_horse", 2, 1, 6);
    }

    private static void smithy(NorthStructureBuilder b) {
        NorthStructurePalette p = b.palette();
        int w = 6, d = 6, h = 5;
        b.foundation(-w, -d, w, d, p.stone());
        b.hollowBox(-w, 0, -d, w, h, d, p.stone(), p.planks());
        b.door(0, 1, -d, Direction.NORTH);
        b.gabledRoof(w, -d, d, h);
        b.set(-4, 1, 0, Blocks.BLAST_FURNACE.defaultBlockState());
        b.set(-3, 1, 1, block("alloy_forge", Blocks.FURNACE));
        b.set(-5, 1, -1, block("unsmeltery", Blocks.BLAST_FURNACE));
        b.set(-2, 1, 2, Blocks.ANVIL.defaultBlockState());
        b.set(-4, 1, 1, Blocks.LAVA.defaultBlockState());
        b.fill(-4, 2, 1, -4, 7, 1, p.stone());
        b.chest(3, 1, 3, Direction.WEST, "gift");
        b.marker("gift_blacksmith", 0, 1, 0);
    }

    private static void castle(NorthStructureBuilder b, CastleKind kind) {
        boolean abandoned = kind == CastleKind.ABANDONED;
        NorthStructurePalette p = b.palette();
        int r = 12;
        b.foundation(-r, -r, r, r, p.stone());
        b.clear(-r, 0, -r, r, 12, r);
        for (int z = -r; z <= r; z++) {
            for (int x = -r; x <= r; x++) {
                BlockState ground = Math.floorMod(mix(b.seed(), x, z), 3L) == 0L
                        ? Blocks.COARSE_DIRT.defaultBlockState() : Blocks.DIRT_PATH.defaultBlockState();
                b.set(x, 0, z, ground);
            }
        }

        // Original 25 x 25 raised timber curtain with four 4 x 4 corner towers.
        for (int i = -r; i <= r; i++) {
            for (int y = 1; y <= 6; y++) {
                b.set(i, y, -r, p.planks());
                b.set(i, y, r, p.planks());
                b.set(-r, y, i, p.planks());
                b.set(r, y, i, p.planks());
            }
            if (Math.floorMod(i, 2) == 0) {
                b.set(i, 7, -r, p.woodSlab());
                b.set(i, 7, r, p.woodSlab());
                b.set(-r, 7, i, p.woodSlab());
                b.set(r, 7, i, p.woodSlab());
            }
        }
        for (int x : new int[]{-11, 11}) {
            for (int z : new int[]{-11, 11}) {
                b.fill(x - 1, 1, z - 1, x + 1, 8, z + 1, p.log());
                b.fill(x - 2, 9, z - 2, x + 2, 9, z + 2, p.roofSlab());
            }
        }

        // Main south gate and stone approach.
        b.clear(-2, 1, -r, 2, 4, -r);
        b.fill(-2, 1, -r - 1, -2, 7, -r + 2, p.log());
        b.fill(2, 1, -r - 1, 2, 7, -r + 2, p.log());
        b.fill(-2, 6, -r - 1, 2, 6, -r + 2, p.log());
        b.path(-1, -r - 2, 1, 10);

        // Smithy in the northwest, storehouse in the southwest, and two-tier
        // barracks in the southeast match the original compound roles.
        b.fill(-9, 1, -9, -5, 3, -5, p.planks());
        b.clear(-8, 1, -8, -6, 2, -6);
        b.set(-7, 1, -7, block("alloy_forge", Blocks.BLAST_FURNACE));
        b.set(-6, 1, -6, Blocks.ANVIL.defaultBlockState());
        b.marker(abandoned ? "abandoned_smithy" : "gift_blacksmith", -4, 1, -4);

        b.fill(-9, 1, 5, -5, 3, 9, p.planks());
        b.clear(-8, 1, 6, -6, 2, 8);
        b.chest(-7, 1, 9, Direction.NORTH, abandoned ? "gift_treasure" : "gift");
        b.chest(-8, 1, 9, Direction.NORTH, abandoned ? "gift_treasure" : "gift");
        b.set(-9, 1, 7, block("table_gift", Blocks.CRAFTING_TABLE));

        b.fill(5, 1, 5, 10, 3, 10, p.planks());
        b.clear(6, 1, 6, 9, 2, 9);
        for (int z : new int[]{6, 10}) {
            b.set(7, 1, z, abandoned ? block("fur_bed", Blocks.GRAY_BED)
                    : block("straw_bed", Blocks.BLACK_BED));
            b.set(9, 1, z, abandoned ? block("fur_bed", Blocks.GRAY_BED)
                    : block("straw_bed", Blocks.BLACK_BED));
        }
        b.chest(8, 1, 6, Direction.SOUTH, abandoned ? "gift_treasure" : "gift");
        b.chest(8, 1, 10, Direction.NORTH, abandoned ? "gift_treasure" : "gift");
        b.fill(6, 1, -9, 9, 4, -9, p.log());
        for (int step = 0; step < 4; step++) {
            b.fill(6 + step, 0, -8, 6 + step, step, -7, p.planks());
            b.set(6 + step, step + 1, -8, p.woodStairs());
        }

        if (!abandoned) {
            b.marker("gift_banner_night", -10, 3, 0);
            b.marker("gift_banner_night", 10, 3, 0);
            b.marker("gift_banner_night", 0, 3, 11);
            for (int i = 0; i < 8; i++) {
                int x = (i % 4) * 4 - 6;
                int z = (i / 4) * 5 - 2;
                b.marker("gift_guard", x, 1, z);
            }
            legendaryMarkers(b, kind);
        }
    }

    private static void legendaryMarkers(NorthStructureBuilder b, CastleKind kind) {
        switch (kind) {
            case CASTLE_BLACK -> {
                b.marker("jeor_mormont", -3, 1, -3);
                b.marker("jon_snow", 3, 1, 0);
                b.marker("aemon_targaryen", 3, 1, 3);
                b.marker("alliser_thorne", -3, 1, 3);
                b.marker("edd", 3, 1, -3);
                b.marker("samwell_tarly", 0, 1, 3);
            }
            case EASTWATCH -> {
                b.marker("cotter_pyke", 0, 1, 0);
                b.marker("harmune", 0, 1, 1);
            }
            case SHADOW_TOWER -> {
                b.marker("denys_mallister", 0, 1, 0);
                b.marker("mullin", 0, 1, 1);
            }
            case ABANDONED -> { }
        }
    }

    private static void wallGate(NorthStructureBuilder b, boolean abandoned) {
        NorthStructurePalette p = b.palette();
        BlockState iceBrick = block("brick_ice_bricks", Blocks.PACKED_ICE);
        // Stepped 13 x 13 gatehouse from the original Gift gate.
        for (int z = -6; z <= 6; z++) {
            b.fill(-6, 0, z, 6, 3, z, iceBrick);
            b.fill(-5, 4, z, 5, 7, z, iceBrick);
            b.fill(-4, 8, z, 4, 9, z, iceBrick);
            b.fill(-3, 10, z, 3, 10, z, iceBrick);
            b.fill(-2, 11, z, 2, 11, z, iceBrick);
        }
        b.clear(-3, 0, -6, 3, 6, 6);
        BlockState bars = abandoned ? block("gate_wooden_bars", Blocks.SPRUCE_FENCE)
                : block("gate_iron_bars", Blocks.IRON_BARS);
        b.fill(-3, 0, -4, 3, 6, -4, bars);
        b.fill(-3, 0, 4, 3, 6, 4, bars);
        b.fill(-4, 0, -3, -4, 7, 3, p.log());
        b.fill(4, 0, -3, 4, 7, 3, p.log());
        b.fill(-3, 7, -3, 3, 7, 3, p.planks());
        b.path(-3, -6, 3, 6);

        int liftTop = Math.max(12, PlanetosNightWatchStructureGenerator.WALL_TOP - b.originY());
        for (int y = 8; y <= liftTop; y++) {
            b.set(-4, y, 6, p.log());
            b.set(4, y, 6, p.log());
            b.set(-4, y, 7, block("rope", Blocks.CHAIN));
            b.set(4, y, 7, block("rope", Blocks.CHAIN));
            if (y < liftTop) {
                for (int x = -3; x <= 3; x++) b.set(x, y, 6, p.fence());
            }
        }
        b.fill(-4, liftTop, 0, 4, liftTop, 7, p.log());
    }

    private static void well(NorthStructureBuilder b) {
        NorthStructurePalette p = b.palette();
        b.foundation(-2, -2, 2, 2, Blocks.DIRT_PATH.defaultBlockState());
        b.fill(-1, -5, -1, 1, 0, 1, p.stone());
        b.fill(0, -4, 0, 0, -1, 0, Blocks.WATER.defaultBlockState());
        for (int x : new int[]{-1, 1}) for (int z : new int[]{-1, 1}) {
            b.fill(x, 1, z, x, 3, z, p.fence());
            b.set(x, 4, z, p.woodSlab());
        }
        b.fill(-1, 4, 0, 1, 4, 0, p.woodSlab());
        b.fill(0, 3, -1, 0, 4, 1, p.fence());
    }

    private static void lamp(NorthStructureBuilder b) {
        b.fill(0, -1, 0, 0, 2, 0, b.palette().log());
        b.set(0, 3, 0, b.palette().planks());
        b.set(0, 4, 0, b.palette().fence());
        BlockState lantern = Blocks.LANTERN.defaultBlockState();
        if (lantern.hasProperty(LanternBlock.HANGING)) {
            lantern = lantern.setValue(LanternBlock.HANGING, true);
        }
        b.set(0, 5, 0, lantern);
    }

    private static void palisadePost(NorthStructureBuilder b, int x, int z, long salt) {
        b.set(x, -1, z, Blocks.COBBLESTONE.defaultBlockState());
        int height = 5 + (int)Math.floorMod(salt, 2L);
        b.fill(x, 0, z, x, height, z, b.palette().log());
    }

    private static void hayBales(NorthStructureBuilder b) {
        b.fill(-2, 0, -2, 2, 1, 2, Blocks.HAY_BLOCK.defaultBlockState());
        b.fill(-1, 2, -1, 1, 2, 1, Blocks.HAY_BLOCK.defaultBlockState());
    }

    private static void windows(NorthStructureBuilder b, int w, int d, int y) {
        b.set(-w, y, -1, Blocks.IRON_BARS.defaultBlockState());
        b.set(w, y, -1, Blocks.IRON_BARS.defaultBlockState());
        b.set(-2, y, d, Blocks.IRON_BARS.defaultBlockState());
        b.set(2, y, d, Blocks.IRON_BARS.defaultBlockState());
    }

    private static int rotation(double radians) {
        return Math.floorMod((int)Math.round(radians / (Math.PI / 2.0D)) + 1, 4);
    }

    private static long mix(long seed, long x, long z) {
        long value = seed ^ x * 341873128712L ^ z * 132897987541L;
        value ^= value >>> 33;
        value *= 0xff51afd7ed558ccdL;
        value ^= value >>> 33;
        value *= 0xc4ceb9fe1a85ec53L;
        return value ^ value >>> 33;
    }

    private static BlockState block(String id, Block fallback) {
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(GOTMod.MOD_ID, id));
        return block == null || block == Blocks.AIR ? fallback.defaultBlockState() : block.defaultBlockState();
    }
}
