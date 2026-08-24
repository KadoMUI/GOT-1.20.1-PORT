package got.world.structure.wildling;

import got.world.structure.north.NorthStructureBuilder;
import net.minecraft.world.level.block.Blocks;

/** Rebuilds the original default, Thenn, Hardhome, and Craster settlement layouts. */
public final class WildlingSettlementGenerator {
    private WildlingSettlementGenerator() {}

    public static void generate(NorthStructureBuilder b, WildlingSettlementKind kind) {
        switch (kind) {
            case DEFAULT -> village(b, false);
            case THENN -> village(b, true);
            case HARDHOME -> hardhome(b);
            case CRASTER -> craster(b);
        }
    }

    private static void village(NorthStructureBuilder b, boolean thenn) {
        b.marker(thenn ? "thenn_warrior_respawner" : "wildling_warrior_respawner", 0, 0, 0);
        b.marker(thenn ? "thenn_ranged_respawner" : "wildling_ranged_respawner", 0, 0, 0);
        dirtyCross(b, 68, 3);
        WildlingStructureTemplates.well(b.child(0, 0, -2, 0, mix(b.seed(), 0, -2)));
        legacyHouse(b.child(0, 0, -19, 2, mix(b.seed(), 0, -19)), thenn, true);
        legacyHouse(b.child(-68, 0, 0, 1, mix(b.seed(), -68, 0)), thenn, true);
        legacyChieftain(b.child(68, 0, 0, 3, mix(b.seed(), 68, 0)), thenn, false);

        for (int row = -3; row <= 3; row++) {
            int x = row * 18;
            int z = Math.abs(x) <= 15 ? 15 : 7;
            long salt = mix(b.seed(), x, z);
            if (Math.abs(row) >= 1) legacyHouse(b.child(x, 0, -z, 2, salt), thenn, false);
            legacyHouse(b.child(x, 0, z, 0, salt ^ 0x51L), thenn, false);
            int outer = z + 20;
            if (row != 0) WildlingStructureTemplates.hayBales(b.child(x, 0, -outer, 2, salt ^ 0xA2L));
            WildlingStructureTemplates.hayBales(b.child(x, 0, outer, 0, salt ^ 0xF3L));
        }
    }

    private static void hardhome(NorthStructureBuilder b) {
        final int[][] houses = {
                {-8,-10,0},{13,-8,1},{-22,5,3},{24,8,2},{2,19,2},
                {-35,-18,1},{37,-20,3},{-40,15,0},{42,19,2},{-17,37,1},
                {18,39,3},{-57,-4,2},{58,2,0},{-4,-52,1},{5,56,3}
        };
        for (int i=0;i<houses.length;i++) {
            int x=houses[i][0], z=houses[i][1], rotation=houses[i][2];
            NorthStructureBuilder house=b.child(x,0,z,rotation,mix(b.seed(),x,z));
            got.world.structure.schematic.AuthoredSchematicTemplate.place(house,"Wildling_House.schem");
        }
    }

    private static void craster(NorthStructureBuilder b) {
        b.path(-10, -2, 12, 2);
        got.world.structure.legacy.generated.GOTStructureWildlingKeep.place(
                b.child(-7, 0, 0, 2, mix(b.seed(), -7, 0)), 0);
        got.world.structure.legacy.generated.GOTStructureWildlingBarn.place(
                b.child(7, 0, 6, 2, mix(b.seed(), 7, 6)), 0);
    }

    private static void house(NorthStructureBuilder b, int x, int z, int rotation, boolean tramp) {
        legacyHouse(b.child(x, 0, z, rotation, mix(b.seed(), x, z)), false, tramp);
    }

    private static void legacyHouse(NorthStructureBuilder b, boolean thenn, boolean special) {
        if (thenn) got.world.structure.legacy.generated.GOTStructureThennHouse.place(b, special ? 1 : 0);
        else got.world.structure.legacy.generated.GOTStructureWildlingHouse.place(b, special ? 1 : 0);
    }

    private static void legacyChieftain(NorthStructureBuilder b, boolean thenn, boolean hardhome) {
        if (thenn) got.world.structure.legacy.generated.GOTStructureThennChieftainHouse.place(b, hardhome ? 1 : 0);
        else got.world.structure.legacy.generated.GOTStructureWildlingChieftainHouse.place(b, hardhome ? 1 : 0);
    }

    private static void dirtyCross(NorthStructureBuilder b, int length, int halfWidth) {
        b.path(-length, -halfWidth, length, halfWidth);
        b.path(-halfWidth, -length, halfWidth, length);
        for (int x = -18; x <= 18; x++) for (int z = -18; z <= 18; z++) {
            if (x * x + z * z <= 18 * 18) b.set(x, -1, z, Blocks.COARSE_DIRT.defaultBlockState());
        }
    }

    private static void hardhomePaths(NorthStructureBuilder b) {
        // 36-block inner square, 132-block outer square, and four axial roads.
        // Splitting the rings into bands keeps the original shape while avoiding
        // a full 173 x 173 scan in every intersecting chunk.
        b.path(-18, -18, 18, -12);
        b.path(-18, 12, 18, 18);
        b.path(-18, -11, -12, 11);
        b.path(12, -11, 18, 11);
        b.path(-66, -66, 66, -60);
        b.path(-66, 60, 66, 66);
        b.path(-66, -59, -60, 59);
        b.path(60, -59, 66, 59);
        b.path(-3, -86, 3, -18);
        b.path(-3, 18, 3, 86);
        b.path(-86, -3, -18, 3);
        b.path(18, -3, 86, 3);
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
