package got.world.structure.north;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/** Dispatches every standalone North spawner to its recovered legacy geometry. */
public final class NorthStructureTemplates {
    private NorthStructureTemplates() {}

    public static void generate(NorthStructureBuilder b, NorthStructureType type) {
        switch (type) {
            case HILLMAN_HOUSE -> got.world.structure.legacy.generated.GOTStructureNorthHillmanHouse.place(b, 0);
            case HILLMAN_CHIEFTAIN_HOUSE -> got.world.structure.legacy.generated.GOTStructureNorthHillmanChieftainHouse.place(b, 0);
            case BARN -> got.world.structure.legacy.generated.GOTStructureWesterosBarn.place(b, 0);
            case BATH -> got.world.structure.legacy.generated.GOTStructureWesterosBath.place(b, 0);
            case FORTRESS -> got.world.structure.schematic.AuthoredSchematicTemplate.place(b, "WesterosCastleSmall.schem");
            case HOUSE -> got.world.structure.legacy.generated.GOTStructureWesterosCottage.place(b, 0);
            case HOUSE_LARGE -> got.world.structure.legacy.generated.GOTStructureWesterosStoneHouse.place(b, 0);
            case HOUSE_SMALL -> got.world.structure.legacy.generated.GOTStructureWesterosHouse.place(b, 0);
            case SMITHY -> got.world.structure.legacy.generated.GOTStructureWesterosSmithy.place(b, 0);
            case STABLES -> got.world.structure.legacy.generated.GOTStructureWesterosStables.place(b, 0);
            case TAVERN -> got.world.structure.legacy.generated.GOTStructureWesterosTavern.place(b, 0);
            case TOWER -> got.world.structure.legacy.generated.GOTStructureWesterosTower.place(b, 0);
            case WATCHFORT -> got.world.structure.legacy.generated.GOTStructureWesterosWatchfort.place(b, 0);
            case WATCHTOWER -> got.world.structure.legacy.generated.GOTStructureWesterosWatchtower.place(b, 0);
            case HILLMAN_SETTLEMENT, VILLAGE, FORT_SETTLEMENT, TOWN ->
                    throw new IllegalArgumentException("Settlement type requires NorthSettlementGenerator: " + type);
        }
    }

    private static void house(NorthStructureBuilder b, int halfWidth, int halfDepth,
                              int wallHeight, String loot) {
        NorthStructurePalette p = b.palette();
        b.foundation(-halfWidth, -halfDepth, halfWidth, halfDepth, p.stone());
        b.hollowBox(-halfWidth, 0, -halfDepth, halfWidth, wallHeight, halfDepth, p.daub(), p.planks());
        b.timberFrame(-halfWidth, -halfDepth, halfWidth, halfDepth, wallHeight);
        b.door(0, 1, -halfDepth, Direction.NORTH);
        windows(b, halfWidth, halfDepth, 2);
        b.gabledRoof(halfWidth, -halfDepth, halfDepth, wallHeight);
        b.set(-halfWidth + 1, 1, halfDepth - 1, Blocks.CRAFTING_TABLE.defaultBlockState());
        b.set(halfWidth - 1, 1, halfDepth - 1, Blocks.FURNACE.defaultBlockState());
        b.set(halfWidth - 1, 2, halfDepth - 1, Blocks.LANTERN.defaultBlockState());
        b.chest(-halfWidth + 1, 1, -halfDepth + 2, Direction.SOUTH, loot);
        b.marker("north_civilian", 0, 1, 1);
    }

    private static void houseLarge(NorthStructureBuilder b) {
        NorthStructurePalette p = b.palette();
        int w = 5, d = 7, h = 5;
        b.foundation(-w, -d, w, d, p.stone());
        b.hollowBox(-w, 0, -d, w, h, d, p.daub(), p.planks());
        b.timberFrame(-w, -d, w, d, h);
        b.fill(-w + 1, 3, 0, w - 1, 3, 0, p.log());
        b.door(0, 1, -d, Direction.NORTH);
        windows(b, w, d, 2);
        b.gabledRoof(w, -d, d, h);
        b.set(-3, 1, 4, Blocks.CRAFTING_TABLE.defaultBlockState());
        b.set(3, 1, 4, Blocks.FURNACE.defaultBlockState());
        b.set(0, 1, 0, Blocks.SPRUCE_STAIRS.defaultBlockState());
        b.chest(-3, 1, -4, Direction.SOUTH, "north_house");
        b.chest(3, 4, 4, Direction.NORTH, "north_house");
        b.marker("north_civilian", -1, 1, 2);
        b.marker("north_civilian", 2, 4, -2);
    }

    private static void barn(NorthStructureBuilder b) {
        NorthStructurePalette p = b.palette();
        int w = 5, d = 7, h = 5;
        b.foundation(-w, -d, w, d, Blocks.COBBLESTONE.defaultBlockState());
        b.hollowBox(-w, 0, -d, w, h, d, p.planks(), Blocks.COARSE_DIRT.defaultBlockState());
        b.timberFrame(-w, -d, w, d, h);
        b.clear(-1, 1, -d, 1, 3, -d);
        b.set(-2, 1, -d, p.fenceGate());
        b.set(2, 1, -d, p.fenceGate());
        b.gabledRoof(w, -d, d, h);
        b.fill(-4, 1, 2, -2, 2, 5, Blocks.HAY_BLOCK.defaultBlockState());
        b.fill(2, 1, 3, 4, 1, 5, Blocks.HAY_BLOCK.defaultBlockState());
        b.fill(-4, 1, -4, -4, 2, 0, p.fence());
        b.fill(4, 1, -4, 4, 2, 0, p.fence());
        b.marker("north_farmer", 0, 1, 1);
        b.marker("stable_animal", -3, 1, -2);
        b.marker("stable_animal", 3, 1, -2);
    }

    private static void bath(NorthStructureBuilder b) {
        NorthStructurePalette p = b.palette();
        int w = 4, d = 5, h = 4;
        b.foundation(-w, -d, w, d, p.stone());
        b.hollowBox(-w, 0, -d, w, h, d, p.stone(), p.stone());
        b.door(0, 1, -d, Direction.NORTH);
        b.fill(-2, 0, -1, 2, 0, 3, Blocks.WATER.defaultBlockState());
        b.fill(-3, 0, -2, 3, 0, -2, p.stoneSlab());
        b.fill(-3, 0, 4, 3, 0, 4, p.stoneSlab());
        b.gabledRoof(w, -d, d, h);
        b.set(-3, 2, 0, Blocks.LANTERN.defaultBlockState());
        b.set(3, 2, 0, Blocks.LANTERN.defaultBlockState());
        b.marker("north_bath_attendant", 0, 1, -3);
    }

    private static void smithy(NorthStructureBuilder b) {
        NorthStructurePalette p = b.palette();
        int w = 5, d = 6, h = 5;
        b.foundation(-w, -d, w, d, p.stone());
        b.hollowBox(-w, 0, -d, w, h, d, p.stone(), p.stone());
        b.door(0, 1, -d, Direction.NORTH);
        b.clear(-w, 1, -2, -w, 3, 2);
        b.fill(-w, 1, -2, -w, 1, 2, p.fence());
        b.gabledRoof(w, -d, d, h);
        b.set(3, 1, 3, Blocks.BLAST_FURNACE.defaultBlockState());
        b.set(3, 1, 2, Blocks.FURNACE.defaultBlockState());
        b.set(1, 1, 3, Blocks.ANVIL.defaultBlockState());
        b.set(-3, 1, 3, litCampfire());
        b.fill(-3, 2, 3, -3, 7, 3, Blocks.BRICKS.defaultBlockState());
        b.chest(-3, 1, -3, Direction.SOUTH, "north_smithy");
        b.marker("north_blacksmith", 0, 1, 1);
    }

    private static void stables(NorthStructureBuilder b) {
        NorthStructurePalette p = b.palette();
        int w = 6, d = 7, h = 5;
        b.foundation(-w, -d, w, d, Blocks.COBBLESTONE.defaultBlockState());
        b.hollowBox(-w, 0, -d, w, h, d, p.planks(), Blocks.COARSE_DIRT.defaultBlockState());
        b.timberFrame(-w, -d, w, d, h);
        b.clear(-2, 1, -d, 2, 3, -d);
        b.gabledRoof(w, -d, d, h);
        for (int x : new int[]{-4, 0, 4}) {
            b.fill(x, 1, -2, x, 2, 5, p.fence());
            b.set(x + (x < 4 ? 1 : -1), 1, 3, Blocks.HAY_BLOCK.defaultBlockState());
        }
        b.marker("north_stablemaster", 0, 1, -3);
        b.marker("stable_horse", -3, 1, 2);
        b.marker("stable_horse", 2, 1, 2);
    }

    private static void tavern(NorthStructureBuilder b) {
        NorthStructurePalette p = b.palette();
        int w = 6, d = 8, h = 6;
        b.foundation(-w, -d, w, d, p.stone());
        b.hollowBox(-w, 0, -d, w, h, d, p.daub(), p.planks());
        b.timberFrame(-w, -d, w, d, h);
        b.door(0, 1, -d, Direction.NORTH);
        windows(b, w, d, 2);
        b.gabledRoof(w, -d, d, h);
        b.fill(-4, 1, 4, 4, 1, 4, p.planks());
        for (int z : new int[]{-3, 1, 5}) {
            b.set(-3, 1, z, Blocks.SPRUCE_FENCE.defaultBlockState());
            b.set(3, 1, z, Blocks.SPRUCE_FENCE.defaultBlockState());
            b.set(-3, 2, z, Blocks.SPRUCE_PRESSURE_PLATE.defaultBlockState());
            b.set(3, 2, z, Blocks.SPRUCE_PRESSURE_PLATE.defaultBlockState());
        }
        b.set(5, 1, 5, Blocks.FURNACE.defaultBlockState());
        b.chest(-5, 1, 6, Direction.EAST, "north_tavern");
        b.marker("north_bartender", 0, 1, 5);
        b.marker("north_patron", -2, 1, 0);
        b.marker("north_patron", 2, 1, 2);
    }

    private static void tower(NorthStructureBuilder b, boolean watchtower) {
        NorthStructurePalette p = b.palette();
        int w = watchtower ? 3 : 4;
        int height = watchtower ? 15 : 13;
        b.foundation(-w, -w, w, w, p.stone());
        b.hollowBox(-w, 0, -w, w, height, w, p.stone(), p.stone());
        b.door(0, 1, -w, Direction.NORTH);
        for (int y = 3; y < height; y += 4) {
            b.fill(-w + 1, y, -w + 1, w - 1, y, w - 1, p.planks());
            b.clear(0, y, 0, 0, y, 0);
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                int x = dir.getStepX() * w;
                int z = dir.getStepZ() * w;
                b.set(x, y + 1, z, Blocks.IRON_BARS.defaultBlockState());
            }
        }
        b.fill(-w - 1, height + 1, -w - 1, w + 1, height + 1, w + 1, p.stone());
        b.fill(-w, height + 2, -w, w, height + 2, w, p.stoneSlab());
        for (int x = -w - 1; x <= w + 1; x += 2) {
            b.set(x, height + 2, -w - 1, p.stoneWall());
            b.set(x, height + 2, w + 1, p.stoneWall());
        }
        for (int z = -w + 1; z <= w - 1; z += 2) {
            b.set(-w - 1, height + 2, z, p.stoneWall());
            b.set(w + 1, height + 2, z, p.stoneWall());
        }
        b.marker(watchtower ? "north_watchman" : "north_guard", 0, height + 2, 0);
    }

    private static void fortress(NorthStructureBuilder b) {
        NorthStructurePalette p = b.palette();
        int r = 17;
        b.foundation(-r, -r, r, r, p.stone());
        b.clear(-r, 0, -r, r, 18, r);
        b.fill(-r, 0, -r, r, 0, r, Blocks.COARSE_DIRT.defaultBlockState());
        fortWalls(b, r, 7);
        gatehouse(b, 0, -r, Direction.NORTH);
        piece(b, NorthStructureType.HOUSE_LARGE, 0, 3, 0);
        piece(b, NorthStructureType.SMITHY, -10, 5, 1);
        piece(b, NorthStructureType.STABLES, 10, 5, 3);
        b.path(-2, -r, 2, 3);
        b.marker("north_captain", 0, 1, 2);
        b.marker("north_guard", -12, 8, -12);
        b.marker("north_guard", 12, 8, -12);
        b.marker("north_banner_stark", 0, 8, -r + 1);
    }

    private static void watchfort(NorthStructureBuilder b) {
        NorthStructurePalette p = b.palette();
        int r = 11;
        b.foundation(-r, -r, r, r, p.stone());
        b.clear(-r, 0, -r, r, 16, r);
        b.fill(-r, 0, -r, r, 0, r, Blocks.COARSE_DIRT.defaultBlockState());
        fortWalls(b, r, 5);
        gatehouse(b, 0, -r, Direction.NORTH);
        piece(b, NorthStructureType.HOUSE_SMALL, 0, 3, 0);
        b.set(-6, 1, 4, Blocks.HAY_BLOCK.defaultBlockState());
        b.chest(6, 1, 5, Direction.WEST, "north_fort");
        b.marker("north_watchman", 0, 1, 2);
        b.marker("north_watchman", -8, 6, -8);
    }

    private static void hillmanHouse(NorthStructureBuilder b, boolean chieftain) {
        NorthStructurePalette p = b.palette();
        int w = chieftain ? 5 : 4;
        int d = 6;
        int h = chieftain ? 5 : 4;
        b.foundation(-w, -d, w, d, Blocks.COBBLESTONE.defaultBlockState());
        b.hollowBox(-w, 0, -d, w, h, d, Blocks.COBBLESTONE.defaultBlockState(),
                Blocks.COARSE_DIRT.defaultBlockState());
        b.fill(-w + 1, h, -d + 1, w - 1, h, d - 1, p.log());
        b.door(0, 1, -d, Direction.NORTH);
        b.gabledRoof(w, -d, d, h);
        b.set(0, 1, 2, litCampfire());
        b.set(-w + 1, 1, d - 1, Blocks.HAY_BLOCK.defaultBlockState());
        b.chest(w - 1, 1, d - 1, Direction.WEST,
                chieftain ? "north_fort" : "north_house");
        b.marker(chieftain ? "north_hillman_chieftain" : "north_hillman", 0, 1, 0);
    }

    public static void marketStall(NorthStructureBuilder b, int variant) {
        got.world.structure.legacy.generated.GOTStructureWesterosMarketStall.place(b, variant);
    }

    public static void villageFarm(NorthStructureBuilder b, int variant) {
        switch (Math.floorMod(variant, 3)) {
            case 0 -> got.world.structure.legacy.generated.GOTStructureWesterosVillageFarm$Crops.place(b, 0);
            case 1 -> got.world.structure.legacy.generated.GOTStructureWesterosVillageFarm$Animals.place(b, 0);
            default -> got.world.structure.legacy.generated.GOTStructureWesterosVillageFarm$Tree.place(b, 0);
        }
    }

    public static void barrow(NorthStructureBuilder b) {
        int r = 7;
        b.foundation(-r, -r, r, r, Blocks.STONE.defaultBlockState());
        b.clear(-r, 0, -r, r, 7, r);
        for (int y = 0; y <= 5; y++) {
            int layer = r - y;
            for (int z = -layer; z <= layer; z++) for (int x = -layer; x <= layer; x++) {
                if (x * x + z * z <= layer * layer) b.set(x, y, z,
                        y <= 1 ? Blocks.STONE.defaultBlockState() : Blocks.DIRT.defaultBlockState());
            }
        }
        b.clear(-2, 1, -r, 2, 3, 2);
        b.fill(-2, 0, -5, 2, 0, 2, b.palette().stone());
        b.fill(-2, 1, -5, -2, 3, 2, b.palette().stone());
        b.fill(2, 1, -5, 2, 3, 2, b.palette().stone());
        b.fill(-2, 4, -5, 2, 4, 2, b.palette().stone());
        b.chest(0, 1, 1, Direction.NORTH, "north_barrow");
        b.marker("barrow_wight_future", 0, 1, 0);
    }

    public static void well(NorthStructureBuilder b) {
        got.world.structure.legacy.generated.GOTStructureWesterosWell.place(b, 0);
    }

    public static void lampPost(NorthStructureBuilder b) {
        got.world.structure.legacy.generated.GOTStructureWesterosLampPost.place(b, 0);
    }

    public static void gatehouse(NorthStructureBuilder b, int x, int z, Direction facing) {
        NorthStructurePalette p = b.palette();
        // Used directly at the caller's origin; x/z parameters allow the shared
        // fortress layouts to retain the original gatehouse sub-piece role.
        int horizontal = facing.getAxis() == Direction.Axis.Z ? 1 : 0;
        for (int side : new int[]{-4, 4}) {
            int sx = x + (horizontal == 1 ? side : 0);
            int sz = z + (horizontal == 0 ? side : 0);
            b.fill(sx - 2, 0, sz - 2, sx + 2, 7, sz + 2, p.stone());
            b.clear(sx - 1, 1, sz - 1, sx + 1, 5, sz + 1);
            b.fill(sx - 2, 8, sz - 2, sx + 2, 8, sz + 2, p.stoneSlab());
        }
        if (horizontal == 1) b.clear(x - 2, 1, z - 1, x + 2, 5, z + 1);
        else b.clear(x - 1, 1, z - 2, x + 1, 5, z + 2);
        b.marker("north_gate_guard", x - (horizontal == 1 ? 4 : 0), 1, z - (horizontal == 0 ? 4 : 0));
        b.marker("north_gate_guard", x + (horizontal == 1 ? 4 : 0), 1, z + (horizontal == 0 ? 4 : 0));
    }

    public static void fortificationRing(NorthStructureBuilder b, int radius, int height,
                                         boolean northGate, boolean southGate,
                                         boolean westGate, boolean eastGate) {
        fortWalls(b, radius, height);
        if (northGate) { b.clear(-3, 1, -radius, 3, 5, -radius); gatehouse(b, 0, -radius, Direction.NORTH); }
        if (southGate) { b.clear(-3, 1, radius, 3, 5, radius); gatehouse(b, 0, radius, Direction.SOUTH); }
        if (westGate) { b.clear(-radius, 1, -3, -radius, 5, 3); gatehouse(b, -radius, 0, Direction.WEST); }
        if (eastGate) { b.clear(radius, 1, -3, radius, 5, 3); gatehouse(b, radius, 0, Direction.EAST); }
    }

    private static void fortWalls(NorthStructureBuilder b, int r, int height) {
        NorthStructurePalette p = b.palette();
        for (int i = -r; i <= r; i++) {
            for (int y = 0; y <= height; y++) {
                b.set(i, y, -r, p.variedStone(b.seed() + i * 31L + y));
                b.set(i, y, r, p.variedStone(b.seed() + i * 47L + y));
                b.set(-r, y, i, p.variedStone(b.seed() + i * 61L + y));
                b.set(r, y, i, p.variedStone(b.seed() + i * 73L + y));
            }
            if (Math.floorMod(i, 2) == 0) {
                b.set(i, height + 1, -r, p.stoneWall());
                b.set(i, height + 1, r, p.stoneWall());
                b.set(-r, height + 1, i, p.stoneWall());
                b.set(r, height + 1, i, p.stoneWall());
            }
        }
        for (int cx : new int[]{-r, r}) for (int cz : new int[]{-r, r}) {
            b.fill(cx - 2, 0, cz - 2, cx + 2, height + 2, cz + 2, p.stone());
            b.fill(cx - 3, height + 3, cz - 3, cx + 3, height + 3, cz + 3, p.stoneSlab());
        }
    }

    private static void piece(NorthStructureBuilder parent, NorthStructureType type,
                              int x, int z, int rotation) {
        NorthStructureBuilder child = parent.child(x, 0, z, rotation,
                parent.seed() ^ x * 31L ^ z * 47L);
        generate(child, type);
    }

    private static void windows(NorthStructureBuilder b, int w, int d, int y) {
        for (int x : new int[]{-w, w}) {
            b.set(x, y, -2, Blocks.GLASS_PANE.defaultBlockState());
            b.set(x, y, 2, Blocks.GLASS_PANE.defaultBlockState());
        }
        b.set(-2, y, d, Blocks.GLASS_PANE.defaultBlockState());
        b.set(2, y, d, Blocks.GLASS_PANE.defaultBlockState());
    }

    private static BlockState litCampfire() {
        BlockState state = Blocks.CAMPFIRE.defaultBlockState();
        return state.hasProperty(CampfireBlock.LIT) ? state.setValue(CampfireBlock.LIT, true) : state;
    }
}
