package got.world.structure.north;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Faithful 1.20.1 translation of the legacy North Castle (NorthSettleForted).
 *
 * <p>The original castle is a settlement assembled from shared Westerosi
 * fortress, fort-wall, gate, corner, watchtower, stone-house, smithy, stable,
 * crop-farm and well pieces.  Keep this implementation coordinate-for-
 * coordinate with those GPL source classes; it deliberately does not use the
 * simplified reconstruction helpers in {@link NorthStructureTemplates}.</p>
 */
public final class LegacyNorthCastleTemplates {
    private static final Map<String, BlockState> BLOCK_CACHE = new ConcurrentHashMap<>();

    private LegacyNorthCastleTemplates() {}

    public static void castle(NorthStructureBuilder b) {
        fortress(child(b, 0, 12, 2, 1));

        fortGate(child(b, 0, -37, 0, 2));
        fortWall(child(b, -11, -37, 0, 3), true);
        fortWall(child(b, 11, -37, 0, 4), false);
        watchtower(child(b, -23, -33, 2, 5));
        watchtower(child(b, 23, -33, 2, 6));

        fortGate(child(b, -37, 0, 3, 7));
        fortWall(child(b, -37, -11, 3, 8), false);
        fortWall(child(b, -37, 11, 3, 9), true);
        watchtower(child(b, -33, -23, 1, 10));
        watchtower(child(b, -33, 23, 1, 11));

        fortGate(child(b, 0, 37, 2, 12));
        fortWall(child(b, -11, 37, 2, 13), false);
        fortWall(child(b, 11, 37, 2, 14), true);
        watchtower(child(b, -23, 33, 0, 15));
        watchtower(child(b, 23, 33, 0, 16));

        fortGate(child(b, 37, 0, 1, 17));
        fortWall(child(b, 37, -11, 1, 18), true);
        fortWall(child(b, 37, 11, 1, 19), false);
        watchtower(child(b, 33, -23, 3, 20));
        watchtower(child(b, 33, 23, 3, 21));

        fortCorner(child(b, -30, -30, 3, 22));
        fortCorner(child(b, -30, 30, 2, 23));
        fortCorner(child(b, 30, 30, 1, 24));
        fortCorner(child(b, 30, -30, 0, 25));

        stables(child(b, -24, 2, 0, 26));
        stables(child(b, -24, -2, 2, 27));
        smithy(child(b, 24, 1, 0, 28));
        smithy(child(b, 24, -1, 2, 29));
        stoneHouse(child(b, -3, -25, 1, 30));
        stoneHouse(child(b, 3, -25, 3, 31));
        cropFarm(child(b, -18, -21, 1, 32));
        cropFarm(child(b, 18, -21, 3, 33));
        well(child(b, -12, 27, 1, 34));
        well(child(b, 12, 27, 3, 35));

        b.marker("north_commander", 0, 1, 2);
    }

    private static NorthStructureBuilder child(NorthStructureBuilder b, int x, int z,
                                               int rotation, long salt) {
        return b.child(x, 0, z, rotation,
                b.seed() ^ salt * 0x9e3779b97f4a7c15L);
    }

    private static void fortGate(NorthStructureBuilder b) {
        b.clear(-5, 1, -1, 5, 8, 2);
        for (int x = -4; x <= 4; x++) {
            int ax = Math.abs(x);
            if (ax <= 1) {
                ground(b, x, 0, brick());
                b.fill(x, 1, 0, x, 4, 0, gate());
                b.set(x, 5, 0, stair(brickStairs(), x < 0 ? 0 : x > 0 ? 1 : 2));
                b.set(x, 6, 0, basaltBrick());
                if (x == 0) b.set(x, 7, 0, basaltSlab(false));
                b.set(x, 5, 1, andesiteSlab(false));
            } else if (ax == 2) {
                columnToGround(b, x, 0, 5, pillar());
                b.set(x, 6, 0, basaltSlab(false));
                b.set(x, 4, 1, andesiteSlab(true));
            } else {
                columnToGround(b, x, 0, 3, brick());
                b.set(x, 4, 0, stair(brickStairs(), 2));
                b.set(x, 5, 0, basaltWall());
                b.set(x, 4, 1, andesiteSlab(true));
                if (ax == 4) {
                    columnToGround(b, x, 1, 3, brick());
                    b.set(x, 4, 1, smoothAndesite());
                    for (int y = -1; y <= 3; y++) b.set(x, y, 2, ladder(3));
                }
            }
        }
        b.marker("north_gate_guard", -3, 1, 1);
        b.marker("north_gate_guard", 3, 1, 1);
    }

    private static void fortWall(NorthStructureBuilder b, boolean right) {
        int min = right ? -6 : -8;
        int max = right ? 8 : 6;
        for (int x = min; x <= max; x++) {
            boolean post = Math.abs(x) % 3 == 0;
            columnToGround(b, x, 0, post ? 4 : 3, post ? basaltPillar() : brick());
            if (!post) b.set(x, 4, 0, stair(brickStairs(), 2));
            b.set(x, 5, 0, basaltWall());
            if (post) b.set(x, 6, 0, Blocks.TORCH.defaultBlockState());
            b.set(x, 4, 1, andesiteSlab(true));
        }
    }

    private static void fortCorner(NorthStructureBuilder b) {
        for (int l = -8; l <= 8; l++) {
            int x;
            int z;
            if (l >= 0) {
                x = l / 2;
                z = -((l + 1) / 2);
            } else {
                x = -(Math.abs(l) + 1) / 2;
                z = Math.abs(l) / 2;
            }
            boolean post = Math.abs(l) == 3;
            columnToGround(b, x, z, 4, post ? basaltPillar() : brick());
            b.set(x, 5, z, basaltWall());
            if (post) b.set(x, 6, z, Blocks.TORCH.defaultBlockState());
            if (Math.floorMod(l, 2) == 0) {
                if (l >= 0) b.set(x, 4, z + 1, andesiteSlab(true));
                else b.set(x + 1, 4, z, andesiteSlab(true));
            }
        }
    }

    private static void watchtower(NorthStructureBuilder b) {
        b.clear(-5, 1, -5, 5, 15, 5);
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                int ax = Math.abs(x);
                int az = Math.abs(z);
                if (ax == 3 && az == 3) continue;
                ground(b, x, z, brick());
                if ((ax == 3 && az == 2) || (az == 3 && ax == 2)) {
                    b.fill(x, 1, z, x, 9, z, pillar());
                } else if (ax == 3 || az == 3) {
                    b.fill(x, 1, z, x, 9, z, brick());
                } else {
                    b.clear(x, 1, z, x, 5, z);
                    b.set(x, 6, z, b.palette().planks());
                    b.clear(x, 7, z, x, 9, z);
                }
                b.set(x, 10, z, brick());
            }
        }
        for (int x = -3; x <= 3; x++) {
            b.set(x, 10, -4, stair(brickStairs(), 2));
            b.set(x, 10, 4, stair(brickStairs(), 3));
        }
        for (int z = -2; z <= 2; z++) {
            b.set(-4, 10, z, stair(brickStairs(), 1));
            b.set(4, 10, z, stair(brickStairs(), 0));
        }
        cornerStairs(b, -4, -4, Direction.WEST, Direction.NORTH);
        cornerStairs(b, 4, -4, Direction.EAST, Direction.NORTH);
        cornerStairs(b, -4, 4, Direction.WEST, Direction.SOUTH);
        cornerStairs(b, 4, 4, Direction.EAST, Direction.SOUTH);

        b.door(0, 1, -3, Direction.NORTH);
        b.set(-1, 1, -3, carved());
        b.set(-1, 2, -3, carved());
        b.set(1, 1, -3, carved());
        b.set(1, 2, -3, carved());
        for (int y = 1; y <= 9; y++) b.set(0, y, 2, ladder(2));
        b.set(0, 10, 2, trapdoor(9));

        for (int x = -4; x <= 4; x++) {
            for (int z = -4; z <= 4; z++) {
                int ax = Math.abs(x);
                int az = Math.abs(z);
                if (ax == 4 && az == 4) {
                    b.set(x, 11, z, brick());
                    b.set(x, 12, z, brick());
                } else if ((ax == 4 || az == 4) && Math.floorMod(x + z, 2) != 0) {
                    b.set(x, 11, z, brickWall());
                } else if (ax == 4 || az == 4) {
                    b.set(x, 11, z, brick());
                    b.set(x, 12, z, brickSlab(false));
                }
            }
        }
        b.fill(0, 11, 0, 0, 12, 0, pillar());
        b.set(0, 13, 0, carved());
        b.marker("north_watchman", 0, 11, 1);
        b.marker("north_banner_stark", 0, 14, 0);
    }

    private static void fortress(NorthStructureBuilder b) {
        b.clear(-15, 1, -15, 15, 14, 15);
        for (int x = -11; x <= 11; x++) {
            for (int z = -11; z <= 11; z++) {
                int ax = Math.abs(x);
                int az = Math.abs(z);
                boolean crossWall = (ax >= 9 && az <= 5) || (az >= 9 && ax <= 5);
                if (crossWall) {
                    boolean post = (ax == 11 && (az == 2 || az == 5))
                            || (az == 11 && (ax == 2 || ax == 5));
                    columnToGround(b, x, z, 5, post ? pillar() : brick());
                    b.set(x, 6, z, brick());
                    b.clear(x, 7, z, x, 9, z);
                    if (ax == 9 || ax == 11 || az == 9 || az == 11) {
                        b.set(x, 7, z, basaltWall());
                        if (ax == 5 || az == 5) b.set(x, 8, z, Blocks.TORCH.defaultBlockState());
                    }
                } else {
                    ground(b, x, z, brick());
                    b.clear(x, 1, z, x, 9, z);
                }
            }
        }

        for (int cx : new int[]{-10, 10}) {
            for (int cz : new int[]{-10, 10}) {
                for (int x = cx - 4; x <= cx + 4; x++) {
                    for (int z = cz - 4; z <= cz + 4; z++) {
                        int ax = Math.abs(x - cx);
                        int az = Math.abs(z - cz);
                        if ((ax == 4 && az >= 3) || (az == 4 && ax >= 3)) continue;
                        boolean wall = ax == 4 || az == 4 || (ax == 3 && az == 3);
                        if (wall) {
                            boolean post = (ax == 4 && az == 2) || (az == 4 && ax == 2);
                            columnToGround(b, x, z, 5, post ? pillar() : brick());
                            b.set(x, 6, z, brick());
                            if (ax <= 1 || az <= 1) {
                                b.set(x, 7, z, basaltWall());
                            } else {
                                b.set(x, 7, z, basaltBrick());
                                b.set(x, 8, z, basaltSlab(false));
                            }
                        } else {
                            ground(b, x, z, brick());
                            b.clear(x, 1, z, x, 5, z);
                            b.set(x, 6, z, b.palette().planks());
                        }
                    }
                }
                b.fill(cx, 1, cz, cx, 8, cz, b.palette().log());
                b.set(cx, 9, cz, b.palette().woodSlab());
                if (cx < 0) for (int y = 1; y <= 5; y++) b.set(cx + 1, y, cz, ladder(4));
                if (cx > 0) for (int y = 1; y <= 5; y++) b.set(cx - 1, y, cz, ladder(5));
                if (cz < 0) for (int y = 1; y <= 5; y++) b.set(cx, y, cz + 1, ladder(3));
                if (cz > 0) for (int y = 1; y <= 5; y++) b.set(cx, y, cz - 1, ladder(2));
            }
        }

        fortressWindows(b);
        for (int y = 1; y <= 4; y++) {
            b.fill(-1, y, -10, 1, y, -10, gate());
            b.set(-2, y, -9, pillar());
            b.set(2, y, -9, pillar());
        }
        b.fill(-1, -3, -12, 1, 0, -12, brick());

        b.fill(-2, 0, -2, 2, 0, 2, basaltBrick());
        b.fill(-8, 0, 0, 8, 0, 0, basaltBrick());
        b.fill(0, 0, -12, 0, 0, 8, basaltBrick());
        for (int y = 1; y <= 4; y++) {
            b.set(-1, y, -1, brickWall());
            b.set(1, y, -1, brickWall());
            b.set(-1, y, 1, brickWall());
            b.set(1, y, 1, brickWall());
        }
        b.set(-1, 5, -1, stair(brickStairs(), 2));
        b.set(0, 5, -1, stair(brickStairs(), 2));
        b.set(1, 5, -1, stair(brickStairs(), 2));
        b.set(-1, 5, 0, stair(brickStairs(), 1));
        b.set(0, 5, 0, brick());
        b.set(1, 5, 0, stair(brickStairs(), 0));
        b.set(-1, 5, 1, stair(brickStairs(), 3));
        b.set(0, 5, 1, stair(brickStairs(), 3));
        b.set(1, 5, 1, stair(brickStairs(), 3));
        b.fill(0, 6, 0, 0, 9, 0, pillar());
        b.set(0, 10, 0, carved());
        b.set(0, 11, 0, block("beacon", Blocks.BEACON));

        b.marker("north_captain", 0, 1, 0);
        b.marker("north_guard", -10, 7, -10);
        b.marker("north_guard", 10, 7, -10);
        b.marker("north_banner_stark", 0, 9, 0);
    }

    private static void fortressWindows(NorthStructureBuilder b) {
        for (int side : new int[]{-11, 11}) {
            int inner = side - Integer.signum(side);
            for (int z0 : new int[]{-4, 3}) {
                b.set(side, 2, z0, stair(brickStairs(), 3));
                b.set(side, 2, z0 + 1, stair(brickStairs(), 2));
                b.set(side, 4, z0, stair(brickStairs(), 7));
                b.set(side, 4, z0 + 1, stair(brickStairs(), 6));
                b.clear(side, 3, z0, side, 3, z0 + 1);
                b.fill(inner, 3, z0, inner, 3, z0 + 1, carved());
            }
        }
        for (int side : new int[]{-11, 11}) {
            int inner = side - Integer.signum(side);
            for (int x0 : new int[]{-4, 3}) {
                b.set(x0, 2, side, stair(brickStairs(), 0));
                b.set(x0 + 1, 2, side, stair(brickStairs(), 1));
                b.set(x0, 4, side, stair(brickStairs(), 4));
                b.set(x0 + 1, 4, side, stair(brickStairs(), 5));
                b.clear(x0, 3, side, x0 + 1, 3, side);
                b.fill(x0, 3, inner, x0 + 1, 3, inner, carved());
            }
        }
    }

    private static void stoneHouse(NorthStructureBuilder b) {
        b.clear(-6, 1, -8, 6, 14, 7);
        for (int x = -5; x <= 4; x++) {
            for (int z = -7; z <= 5; z++) {
                int ax = Math.abs(x);
                if (x == -5) {
                    ground(b, x, z, Blocks.GRASS_BLOCK.defaultBlockState());
                    continue;
                }
                ground(b, x, z, brick());
                if (z >= -4) {
                    if ((z == -4 || z == 5) && ax == 4) b.fill(x, 1, z, x, 7, z, pillar());
                    else if (z == -4 || z == 5 || ax == 4) b.fill(x, 1, z, x, 7, z, brick());
                    else {
                        b.clear(x, 1, z, x, 3, z);
                        b.set(x, 4, z, b.palette().planks());
                    }
                } else if (z == -7) {
                    if (ax == 4 || ax == 2) b.fill(x, 1, z, x, 3, z, pillar());
                } else if (ax == 4) {
                    b.set(x, 1, z, brick());
                    b.set(x, 3, z, b.palette().fence());
                } else {
                    b.set(x, 0, z, b.palette().planks());
                    b.clear(x, 1, z, x, 3, z);
                }
            }
        }
        for (int x = -5; x <= 5; x++) {
            for (int step = 0; step <= 2; step++) {
                int y = 8 + step;
                b.set(x, y, -5 + step, stair(basaltStairs(), 2));
                b.set(x, y, -4 + step, basaltBrick());
                b.set(x, y, -3 + step, stair(basaltStairs(), 7));
                b.set(x, y, 6 - step, stair(basaltStairs(), 3));
                b.set(x, y, 5 - step, basaltBrick());
                b.set(x, y, 4 - step, stair(basaltStairs(), 6));
            }
            b.fill(x, 10, -2, x, 10, 3, basaltBrick());
        }
        b.fill(-5, 1, -1, -3, 11, 2, brick());
        for (int x = -4; x <= 4; x++) {
            b.set(x, 4, -7, basaltSlab(false));
            b.set(x, 4, -6, basaltBrick());
            b.set(x, 4, -5, basaltBrick());
            b.set(x, 5, -5, basaltSlab(false));
        }
        b.door(0, 1, -4, Direction.NORTH);
        b.marker("north_civilian", 0, 1, 0);
    }

    private static void smithy(NorthStructureBuilder b) {
        b.clear(-5, 1, 0, 5, 8, 12);
        for (int z = 1; z <= 11; z++) {
            for (int x = -4; x <= 4; x++) {
                boolean post = Math.abs(x) == 4 && (z == 1 || z == 11);
                ground(b, x, z, smoothAndesite());
                if (post) b.fill(x, 1, z, x, 4, z, basaltPillar());
                else if (Math.abs(x) == 4 || z == 1 || z == 11) {
                    b.fill(x, 1, z, x, 3, z, brick());
                    b.set(x, 4, z, brickWall());
                }
            }
        }
        b.fill(-1, 1, 1, 1, 3, 1, andesiteRock());
        b.set(0, 1, 1, b.palette().fenceGate());
        for (int z = 2; z <= 10; z++) {
            b.set(-4, 4, z, brickWall());
            b.set(4, 4, z, brickWall());
            b.fill(-3, 5, z, 3, 5, z, basaltBrick());
            b.set(-4, 5, z, stair(basaltStairs(), 1));
            b.set(4, 5, z, stair(basaltStairs(), 0));
        }
        for (int x = -4; x <= 4; x++) {
            b.set(x, 5, 1, stair(basaltStairs(), 2));
            b.set(x, 5, 11, stair(basaltStairs(), 3));
        }
        b.fill(-3, 1, 9, -2, 1, 10, Blocks.LAVA.defaultBlockState());
        b.set(-3, 1, 8, block("alloy_forge", Blocks.BLAST_FURNACE));
        b.set(-2, 1, 8, block("alloy_forge", Blocks.BLAST_FURNACE));
        b.marker("north_blacksmith", 0, 1, 6);
    }

    private static void stables(NorthStructureBuilder b) {
        b.clear(-6, 1, -7, 6, 10, 7);
        for (int x = -4; x <= 4; x++) {
            for (int z = -5; z <= 5; z++) ground(b, x, z, brick());
        }
        for (int x = -5; x <= 5; x++) {
            b.set(x, 4, -6, thatchSlab(false));
            b.set(x, 4, -5, thatchSlab(true));
            b.set(x, 5, -4, thatchSlab(false));
            b.set(x, 5, -3, thatchSlab(true));
            b.set(x, 6, -2, thatchSlab(false));
            b.set(x, 6, -1, thatchSlab(true));
        }
        for (int x = -4; x <= 4; x++) {
            int ax = Math.abs(x);
            if (ax == 4 || x == 0) {
                b.fill(x, 1, -5, x, 3, -5, b.palette().log());
                for (int z = -4; z <= -1; z++) {
                    b.set(x, 1, z, b.palette().planks());
                    b.fill(x, 2, z, x, 3, z, b.palette().fence());
                    b.set(x, 4, z, thatch());
                    if (z >= -3) b.set(x, 5, z, thatch());
                    if (z == -1) b.set(x, 6, z, thatch());
                }
            } else {
                b.set(x, 2, -5, b.palette().fenceGate());
                for (int z = -4; z <= -1; z++) b.set(x, 0, z, andesiteRock());
            }
        }
        for (int side : new int[]{-4, 4}) {
            b.fill(side, 1, 0, side, 6, 0, pillar());
            b.fill(side, 1, 5, side, 5, 5, pillar());
            b.fill(side, 1, 1, side, 6, 4, brick());
            b.clear(side, 1, 2, side, 2, 3);
            b.set(side, 3, 2, stair(brickStairs(), 7));
            b.set(side, 3, 3, stair(brickStairs(), 6));
        }
        b.fill(-3, 1, 0, 3, 6, 0, brick());
        b.fill(-3, 1, 5, 3, 5, 5, brick());
        b.fill(-3, 4, 1, 3, 4, 4, b.palette().planks());
        for (int x = -5; x <= 5; x++) {
            b.set(x, 7, 0, stair(basaltStairs(), 2));
            b.fill(x, 7, 1, x, 7, 3, basaltBrick());
            b.set(x, 7, 4, stair(basaltStairs(), 3));
            b.set(x, 6, 5, stair(basaltStairs(), 3));
            b.set(x, 5, 6, stair(basaltStairs(), 3));
        }
        b.marker("north_stablemaster", 0, 1, 2);
        b.marker("stable_horse", -2, 1, -3);
        b.marker("stable_horse", 2, 1, -3);
    }

    private static void cropFarm(NorthStructureBuilder b) {
        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z <= 5; z++) {
                int ax = Math.abs(x);
                int az = Math.abs(z);
                ground(b, x, z, andesiteRock());
                b.clear(x, 1, z, x, 6, z);
                if (ax == 5 && az == 5) {
                    b.set(x, 1, z, andesiteRock());
                    b.set(x, 2, z, andesiteSlab(false));
                } else if (ax == 5 || az == 5) {
                    b.set(x, 1, z, andesiteWall());
                    if (ax == 3 || az == 3) b.set(x, 2, z, Blocks.TORCH.defaultBlockState());
                    if (x == 0 || z == 0) b.set(x, 1, z, Blocks.AIR.defaultBlockState());
                } else if (ax <= 2 && az <= 2) {
                    if (x == 0 && z == 0) {
                        b.set(x, 0, z, Blocks.WATER.defaultBlockState());
                        b.set(x, 1, z, andesiteRock());
                        b.set(x, 2, z, Blocks.HAY_BLOCK.defaultBlockState());
                        b.set(x, 3, z, b.palette().fence());
                        b.set(x, 4, z, Blocks.HAY_BLOCK.defaultBlockState());
                        b.set(x, 5, z, Blocks.PUMPKIN.defaultBlockState());
                    } else {
                        b.set(x, 0, z, Blocks.FARMLAND.defaultBlockState());
                        b.set(x, 1, z, Math.floorMod(x + z, 3) == 0
                                ? Blocks.CARROTS.defaultBlockState() : Blocks.WHEAT.defaultBlockState());
                    }
                } else {
                    b.set(x, 0, z, Blocks.DIRT_PATH.defaultBlockState());
                }
            }
        }
        b.marker("north_farmer", 0, 1, -1);
    }

    private static void well(NorthStructureBuilder b) {
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                int ax = Math.abs(x);
                int az = Math.abs(z);
                ground(b, x, z, andesiteRock());
                b.clear(x, 1, z, x, 7, z);
                if (ax == 2 && az == 2) {
                    b.fill(x, 1, z, x, 2, z, andesiteRock());
                    b.set(x, 3, z, andesiteWall());
                    b.set(x, 4, z, andesiteRock());
                    b.set(x, 5, z, andesiteSlab(false));
                }
                if (ax <= 2 && az <= 2) {
                    int d = ax + az;
                    if (d == 3) { b.set(x, 4, z, andesiteSlab(true)); b.set(x, 5, z, smoothAndesite()); }
                    if (d == 2) { b.set(x, 5, z, smoothAndesite()); b.set(x, 6, z, andesiteSlab(false)); }
                    if (d == 1) { b.set(x, 5, z, andesiteSlab(true)); b.set(x, 6, z, smoothAndesite()); }
                    if (d == 0) { b.set(x, 6, z, smoothAndesite()); b.set(x, 7, z, andesiteSlab(false)); }
                }
                if ((ax == 2 && az <= 1) || (az == 2 && ax <= 1)) {
                    b.set(x, 0, z, smoothAndesite());
                    b.set(x, 1, z, b.palette().fence());
                }
            }
        }
        b.fill(-1, -3, -1, 1, 0, 1, Blocks.WATER.defaultBlockState());
        b.fill(0, 4, 0, 0, 5, 0, b.palette().fence());
    }

    private static void cornerStairs(NorthStructureBuilder b, int x, int z,
                                     Direction xFacing, Direction zFacing) {
        b.set(x, 10, z + (z < 0 ? 1 : -1), stair(brickStairs(), legacyStairMeta(xFacing)));
        b.set(x + (x < 0 ? 1 : -1), 10, z, stair(brickStairs(), legacyStairMeta(zFacing)));
    }

    private static void ground(NorthStructureBuilder b, int x, int z, BlockState state) {
        b.fill(x, -6, z, x, 0, z, state);
    }

    private static void columnToGround(NorthStructureBuilder b, int x, int z,
                                       int height, BlockState state) {
        b.fill(x, -6, z, x, height, z, state);
    }

    private static BlockState stair(BlockState state, int meta) {
        if (!state.hasProperty(StairBlock.FACING)) return state;
        Direction direction = switch (meta & 3) {
            case 0 -> Direction.EAST;
            case 1 -> Direction.WEST;
            case 2 -> Direction.SOUTH;
            default -> Direction.NORTH;
        };
        state = state.setValue(StairBlock.FACING, direction);
        if (state.hasProperty(StairBlock.HALF)) {
            state = state.setValue(StairBlock.HALF, (meta & 4) != 0 ? Half.TOP : Half.BOTTOM);
        }
        return state;
    }

    private static int legacyStairMeta(Direction direction) {
        return switch (direction) {
            case EAST -> 0;
            case WEST -> 1;
            case SOUTH -> 2;
            default -> 3;
        };
    }

    private static BlockState ladder(int meta) {
        Direction direction = switch (meta) {
            case 2 -> Direction.NORTH;
            case 3 -> Direction.SOUTH;
            case 4 -> Direction.WEST;
            default -> Direction.EAST;
        };
        return Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, direction);
    }

    private static BlockState trapdoor(int meta) {
        BlockState state = Blocks.OAK_TRAPDOOR.defaultBlockState();
        Direction direction = switch (meta & 3) {
            case 0 -> Direction.SOUTH;
            case 1 -> Direction.NORTH;
            case 2 -> Direction.EAST;
            default -> Direction.WEST;
        };
        state = state.setValue(TrapDoorBlock.FACING, direction);
        if ((meta & 8) != 0) state = state.setValue(TrapDoorBlock.HALF, Half.TOP);
        return state;
    }

    private static BlockState brick() { return block("andesite_bricks", Blocks.STONE_BRICKS); }
    private static BlockState brickSlab(boolean top) { return slab(block("andesite_brick_slab", Blocks.STONE_BRICK_SLAB), top); }
    private static BlockState brickStairs() { return block("stairs_andesite_brick", Blocks.STONE_BRICK_STAIRS); }
    private static BlockState brickWall() { return block("andesite_brick_wall", Blocks.STONE_BRICK_WALL); }
    private static BlockState carved() { return block("carved_andesite_bricks", Blocks.CHISELED_STONE_BRICKS); }
    private static BlockState pillar() { return block("andesite_pillar", Blocks.POLISHED_ANDESITE); }
    private static BlockState andesiteRock() { return block("andesite_rock", Blocks.ANDESITE); }
    private static BlockState smoothAndesite() { return block("smooth_andesite", Blocks.POLISHED_ANDESITE); }
    private static BlockState andesiteSlab(boolean top) { return slab(block("andesite_slab", Blocks.STONE_SLAB), top); }
    private static BlockState andesiteWall() { return block("andesite_wall", Blocks.COBBLESTONE_WALL); }
    private static BlockState basaltBrick() { return block("basalt_westeros_bricks", Blocks.POLISHED_BLACKSTONE_BRICKS); }
    private static BlockState basaltSlab(boolean top) { return slab(block("basalt_brick_slab", Blocks.POLISHED_BLACKSTONE_BRICK_SLAB), top); }
    private static BlockState basaltStairs() { return block("stairs_basalt_brick", Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS); }
    private static BlockState basaltWall() { return block("basalt_brick_wall", Blocks.POLISHED_BLACKSTONE_BRICK_WALL); }
    private static BlockState basaltPillar() { return block("basalt_pillar", Blocks.POLISHED_BASALT); }
    private static BlockState gate() { return block("gate_iron_bars", Blocks.IRON_BARS); }
    private static BlockState thatch() { return block("thatch_thatch", Blocks.HAY_BLOCK); }
    private static BlockState thatchSlab(boolean top) { return slab(block("thatch_slab", Blocks.OAK_SLAB), top); }

    private static BlockState slab(BlockState state, boolean top) {
        return state.hasProperty(SlabBlock.TYPE)
                ? state.setValue(SlabBlock.TYPE, top ? SlabType.TOP : SlabType.BOTTOM)
                : state;
    }

    private static BlockState block(String id, Block fallback) {
        return BLOCK_CACHE.computeIfAbsent(id, key -> {
            Block value = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("got", key));
            return value == null || value == Blocks.AIR
                    ? fallback.defaultBlockState() : value.defaultBlockState();
        });
    }
}
