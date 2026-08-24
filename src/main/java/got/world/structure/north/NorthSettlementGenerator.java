package got.world.structure.north;

import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Random;

/** Complete layouts for the five legacy North settlement forms. */
public final class NorthSettlementGenerator {
    private NorthSettlementGenerator() {}

    public static void generate(NorthStructureBuilder b, NorthSettlementKind kind,
                                String siteName, List<String> legendaryRoles) {
        switch (kind) {
            case VILLAGE -> LegacyNorthSettlementLayouts.village(b);
            case SMALL_TOWN -> LegacyNorthSettlementLayouts.smallTown(b);
            case TOWN -> LegacyNorthSettlementLayouts.town(b);
            case FORT -> fort(b);
            case HILLMAN -> LegacyNorthSettlementLayouts.hillman(b);
        }
        b.marker("settlement_center:" + siteName, 0, 1, 0);
        for (int i = 0; i < legendaryRoles.size(); i++) {
            int angle = Math.floorMod(i, 8);
            int x = switch (angle) { case 0, 1, 7 -> 4; case 3, 4, 5 -> -4; default -> 0; };
            int z = switch (angle) { case 1, 2, 3 -> 4; case 5, 6, 7 -> -4; default -> 0; };
            b.marker("legendary_npc:" + legendaryRoles.get(i), x, 1, z);
        }
    }

    private static void village(NorthStructureBuilder b) {
        Random random = new Random(b.seed());
        // Original dirty centre, outer ring, and diagonal approaches.
        for (int x = -65; x <= 65; x++) for (int z = -65; z <= 65; z++) {
            int distanceSq = x * x + z * z;
            boolean path = distanceSq < 22 * 22
                    || (distanceSq > 53 * 53 && distanceSq < 62 * 62)
                    || (distanceSq < 53 * 53 && Math.abs(Math.abs(x) - Math.abs(z)) <= 3);
            if (path) {
                b.set(x, -1, z, Blocks.COARSE_DIRT.defaultBlockState());
                b.clear(x, 0, z, x, 2, z);
            }
        }
        NorthStructureTemplates.well(b.child(0, 0, -4, 0, b.seed() ^ 0x57454c4cL));
        piece(b, NorthStructureType.HOUSE, -21, 0, 1);
        piece(b, NorthStructureType.HOUSE, 0, -21, 2);
        piece(b, NorthStructureType.HOUSE, 21, 0, 3);
        piece(b, NorthStructureType.TAVERN, 0, 21, 0);
        if (random.nextBoolean()) {
            int[][] stalls = {{-9,-12,1},{9,-12,3},{-9,12,1},{9,12,3}};
            for (int[] stall : stalls) if (random.nextBoolean()) {
                NorthStructureTemplates.marketStall(b.child(stall[0], 0, stall[1], stall[2],
                        random.nextLong()), random.nextInt(11));
            }
        }
        for (int i = 0; i < 20; i++) {
            double angle = (i + 1) * Math.PI * 2.0D / 20.0D;
            int x = (int)Math.round(61.0D * Math.cos(angle));
            int z = (int)Math.round(61.0D * Math.sin(angle));
            double turn = (i + 1) / 20.0D;
            double turn8 = turn * 8.0D;
            int rotation = turn8 >= 3.0D && turn8 < 5.0D ? 1
                    : turn8 >= 5.0D && turn8 < 7.0D ? 2
                    : turn8 >= 7.0D || turn8 < 1.0D ? 3 : 0;
            if (random.nextBoolean()) {
                NorthStructureType type = NorthStructureType.HOUSE_SMALL;
                if (random.nextInt(5) == 0) type = switch (random.nextInt(3)) {
                    case 0 -> NorthStructureType.STABLES;
                    case 1 -> NorthStructureType.SMITHY;
                    default -> NorthStructureType.BARN;
                };
                piece(b, type, x, z, rotation);
            } else if (random.nextInt(3) != 0) {
                b.fill(x - 2, 0, z - 2, x + 2, 2, z + 2,
                        Blocks.HAY_BLOCK.defaultBlockState());
            }
        }

        int signPos = (int)Math.round(50.0D * Math.cos(Math.PI / 4.0D));
        int signDisp = (int)Math.round(7.0D * Math.cos(Math.PI / 4.0D));
        for (int[] sign : new int[][]{{-signPos,-signPos+signDisp,1},{signPos,-signPos+signDisp,3},
                {-signPos,signPos-signDisp,1},{signPos,signPos-signDisp,3}}) {
            got.world.structure.legacy.generated.GOTStructureWesterosVillageSign.place(
                    b.child(sign[0], 0, sign[1], sign[2], random.nextLong()), 0);
        }

        int[][] farms = {{-32,-17,1},{-11,-38,1},{32,-17,3},{11,-38,3},{-32,17,1},{32,17,3}};
        for (int i = 0; i < farms.length; i++) {
            if (!random.nextBoolean()) continue;
            int[] farm = farms[i];
            NorthStructureTemplates.villageFarm(b.child(farm[0], 0, farm[1], farm[2],
                    random.nextLong()), random.nextBoolean() ? (random.nextBoolean() ? 1 : 0) : 2);
        }
    }

    private static void smallTown(NorthStructureBuilder b) {
        b.path(-3, -76, 3, 76);
        b.path(-76, -3, 76, 3);
        NorthStructureTemplates.well(b.child(0, 0, -4, 0, b.seed() ^ 0x57454c4cL));
        for (int i = 0; i < 8; i++) {
            double angle = i * Math.PI / 4.0D;
            int x = (int)Math.round(Math.cos(angle) * 24.0D);
            int z = (int)Math.round(Math.sin(angle) * 24.0D);
            piece(b, i == 5 ? NorthStructureType.TAVERN
                    : i == 6 ? NorthStructureType.SMITHY
                    : NorthStructureType.HOUSE_LARGE, x, z, Math.floorMod(i + 2, 4));
        }
        for (int i = 0; i < 8; i++) {
            int x = i < 4 ? -9 : 9;
            int z = -18 + (i % 4) * 12;
            NorthStructureTemplates.marketStall(b.child(x, 0, z, x < 0 ? 1 : 3,
                    b.seed() ^ 0x4d41524b4554L ^ i), i);
        }
        for (int i = -4; i <= 4; i++) {
            int offset = i * 12;
            NorthStructureType side = Math.floorMod(i, 2) == 0
                    ? NorthStructureType.HOUSE_LARGE : NorthStructureType.HOUSE_SMALL;
            piece(b, side, -64, offset, 1);
            piece(b, side, 64, offset, 3);
            if (Math.abs(i) >= 2) {
                piece(b, side, offset, -64, 2);
                piece(b, side, offset, 64, 0);
            }
            NorthStructureTemplates.lampPost(b.child(-59, 0, offset, 1, b.seed() ^ offset));
            NorthStructureTemplates.lampPost(b.child(59, 0, offset, 3, b.seed() ^ offset ^ 1));
        }
        piece(b, NorthStructureType.BATH, 26, -6, 3);
    }

    private static void town(NorthStructureBuilder b) {
        int r = 82;
        NorthStructureTemplates.fortificationRing(b, r, 5, true, true, false, false);
        b.path(-3, -r, 3, r);
        b.path(-r, -3, r, 3);

        piece(b, NorthStructureType.TAVERN, -21, -25, 1);
        piece(b, NorthStructureType.STABLES, 23, -24, 3);
        piece(b, NorthStructureType.SMITHY, -25, 25, 1);
        piece(b, NorthStructureType.BATH, 24, 25, 3);
        piece(b, NorthStructureType.HOUSE_LARGE, -52, -28, 1);
        piece(b, NorthStructureType.HOUSE_LARGE, 51, -28, 3);
        piece(b, NorthStructureType.HOUSE_LARGE, -51, 31, 1);
        piece(b, NorthStructureType.HOUSE_LARGE, 51, 31, 3);
        piece(b, NorthStructureType.BARN, -47, 55, 2);
        piece(b, NorthStructureType.HOUSE, 48, 56, 2);
        NorthStructureTemplates.well(b.child(0, 0, 0, 0, b.seed() ^ 0x544f574e57454c4cL));

        for (int i = 0; i < 11; i++) {
            int row = i / 6;
            int col = i % 6;
            int x = -25 + col * 10;
            int z = row == 0 ? 9 : 18;
            NorthStructureTemplates.marketStall(b.child(x, 0, z, 2,
                    b.seed() ^ 0x4d41524b4554L ^ i), i);
        }
        for (int x = -60; x <= 60; x += 12) {
            NorthStructureTemplates.lampPost(b.child(x, 0, -7, 0, b.seed() ^ x));
            NorthStructureTemplates.lampPost(b.child(x, 0, 7, 0, b.seed() ^ x ^ 1));
        }
        b.marker("north_market_master", 0, 1, 14);
        b.marker("north_sheriff", 0, 1, -10);
        for (int[] tower : new int[][]{{-78,-74},{-78,74},{78,-74},{78,74}}) {
            piece(b, NorthStructureType.WATCHTOWER, tower[0], tower[1], tower[0] < 0 ? 1 : 3);
        }
    }

    private static void fort(NorthStructureBuilder b) {
        got.world.structure.schematic.AuthoredSchematicTemplate.place(
                b, "WesterosCastleSmall.schem");
    }

    private static void hillman(NorthStructureBuilder b) {
        b.path(-68, -3, 68, 3);
        NorthStructureTemplates.well(b.child(0, 0, -2, 0, b.seed() ^ 0x57454c4cL));
        piece(b, NorthStructureType.HILLMAN_HOUSE, 0, -19, 2);
        piece(b, NorthStructureType.HILLMAN_HOUSE, -68, 0, 1);
        piece(b, NorthStructureType.HILLMAN_CHIEFTAIN_HOUSE, 68, 0, 3);
        for (int l = -3; l <= 3; l++) {
            int x = l * 18;
            int z = Math.abs(x) <= 15 ? 22 : 7;
            if (Math.abs(l) >= 1) piece(b, NorthStructureType.HILLMAN_HOUSE, x, -z, 2);
            piece(b, NorthStructureType.HILLMAN_HOUSE, x, z, 0);
            int hayZ = z + 20;
            if (l != 0) b.fill(x - 2, 0, -hayZ - 2, x + 2, 2, -hayZ + 2, Blocks.HAY_BLOCK.defaultBlockState());
            b.fill(x - 2, 0, hayZ - 2, x + 2, 2, hayZ + 2, Blocks.HAY_BLOCK.defaultBlockState());
        }
        b.marker("north_hillman_chieftain", 0, 1, -20);
        b.marker("north_hillman", 0, 1, 4);
    }

    private static void piece(NorthStructureBuilder parent, NorthStructureType type,
                              int x, int z, int rotation) {
        NorthStructureTemplates.generate(parent.child(x, 0, z, rotation,
                parent.seed() ^ x * 341873128712L ^ z * 132897987541L), type);
    }
}
