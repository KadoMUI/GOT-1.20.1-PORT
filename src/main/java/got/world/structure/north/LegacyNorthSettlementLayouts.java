package got.world.structure.north;

import java.util.Random;
import net.minecraft.world.level.block.Blocks;

/** Settlement piece coordinates mechanically recovered from the legacy Instance class. */
public final class LegacyNorthSettlementLayouts {
   private LegacyNorthSettlementLayouts() {}

   public static void village(NorthStructureBuilder b) {
      Random random = new Random(b.seed());

      place(b, "WesterosWell", 0, -4, 0, random);
      place(b, "RESPAWNER", 0, 0, 0, random);
      place(b, "RESPAWNER", 0, 0, 0, random);
      place(b, "NorthHouse", -21, 0, 1, random);
      place(b, "NorthHouse", 0, -21, 2, random);
      place(b, "NorthHouse", 21, 0, 3, random);
      place(b, "NorthTavern", 0, 21, 0, random);
      if (random.nextBoolean()) {
         if (random.nextBoolean()) {
            place(b, "MARKET", -9, -12, 1, random);
         }

         if (random.nextBoolean()) {
            place(b, "MARKET", 9, -12, 3, random);
         }

         if (random.nextBoolean()) {
            place(b, "MARKET", -9, 12, 1, random);
         }

         if (random.nextBoolean()) {
            place(b, "MARKET", 9, 12, 3, random);
         }
      }

      int houses = 20;
      float frac = 1.0F / houses;
      float turn = 0.0F;

      while (turn < 1.0F) {
         float turnR = (float)Math.toRadians((turn += frac) * 360.0F);
         float sin = (float)Math.sin(turnR);
         float cos = (float)Math.cos(turnR);
         int r = 0;
         float turn8 = turn * 8.0F;
         if (turn8 >= 3.0F && turn8 < 5.0F) {
            r = 1;
         } else if (turn8 >= 5.0F && turn8 < 7.0F) {
            r = 2;
         } else if (turn8 >= 7.0F || turn8 < 1.0F) {
            r = 3;
         }

         if (random.nextBoolean()) {
            int l = 61;
            int i = Math.round(l * cos);
            int k = Math.round(l * sin);
            place(b, "RANDOM_HOUSE", i, k, r, random);
         } else if (random.nextInt(3) != 0) {
            int l = 65;
            int i = Math.round(l * cos);
            int k = Math.round(l * sin);
            place(b, "HAY", i, k, r, random);
         }
      }

      int signPos = Math.round(50.0F * (float)Math.cos((float) (Math.PI / 4)));
      int signDisp = Math.round(7.0F * (float)Math.cos((float) (Math.PI / 4)));
      place(b, "WesterosVillageSign", -signPos, -signPos + signDisp, 1, random);
      place(b, "WesterosVillageSign", signPos, -signPos + signDisp, 3, random);
      place(b, "WesterosVillageSign", -signPos, signPos - signDisp, 1, random);
      place(b, "WesterosVillageSign", signPos, signPos - signDisp, 3, random);
      int farmX = 38;
      int farmZ = 17;
      int farmSize = 6;
      if (random.nextBoolean()) {
         place(b, "RANDOM_FARM", -farmX + farmSize, -farmZ, 1, random);
      }

      if (random.nextBoolean()) {
         place(b, "RANDOM_FARM", -farmZ + farmSize, -farmX, 1, random);
      }

      if (random.nextBoolean()) {
         place(b, "RANDOM_FARM", farmX - farmSize, -farmZ, 3, random);
      }

      if (random.nextBoolean()) {
         place(b, "RANDOM_FARM", farmZ - farmSize, -farmX, 3, random);
      }

      if (random.nextBoolean()) {
         place(b, "RANDOM_FARM", -farmX + farmSize, farmZ, 1, random);
      }

      if (random.nextBoolean()) {
         place(b, "RANDOM_FARM", farmX - farmSize, farmZ, 3, random);
      }
   
   }

   public static void hillman(NorthStructureBuilder b) {
      Random random = new Random(b.seed());

      place(b, "RESPAWNER", 0, 0, 0, random);
      place(b, "RESPAWNER", 0, 0, 0, random);
      int pathEnd = 68;
      int pathSide = 7;
      int centreSide = 19;
      place(b, "MossovyWell", 0, -2, 0, random);
      place(b, "NorthHillmanHouse_SPECIAL", 0, -centreSide, 2, random);
      place(b, "NorthHillmanHouse_SPECIAL", -pathEnd, 0, 1, random);
      place(b, "NorthHillmanChieftainHouse", pathEnd, 0, 3, random);
      int rowHouses = 3;

      for (int l = -rowHouses; l <= rowHouses; l++) {
         int i1 = l * 18;
         int k1 = pathSide;
         if (Math.abs(i1) <= 15) {
            k1 += 15 - pathSide;
         }

         if (Math.abs(l) >= 1) {
            place(b, "NorthHillmanHouse", i1, -k1, 2, random);
         }

         place(b, "NorthHillmanHouse", i1, k1, 0, random);
         int k2 = k1 + 20;
         if (l != 0) {
            place(b, "HAY", i1, -k2, 2, random);
         }

         place(b, "HAY", i1, k2, 0, random);
      }
   
   }

   public static void smallTown(NorthStructureBuilder b) {
      Random random = new Random(b.seed());

      boolean outerTavern = random.nextBoolean();
      place(b, "RESPAWNER", 0, 0, 0, random);

      for (int i1 : new int[]{-40, 40}) {
         int[] arrn = new int[]{-40, 40};

         for (int k1 : arrn) {
            place(b, "RESPAWNER", i1, k1, 0, random);
         }
      }

      place(b, "WesterosWell", 0, -4, 0, random);
      int stallPos = 12;

      for (int k1 = -1; k1 <= 1; k1++) {
         int k2 = k1 * stallPos;
         if (random.nextInt(3) != 0) {
            place(b, "MARKET", -stallPos + 3, k2, 1, random);
         }

         if (random.nextInt(3) != 0) {
            place(b, "MARKET", stallPos - 3, k2, 3, random);
         }
      }

      if (random.nextInt(3) != 0) {
         place(b, "MARKET", 0, stallPos - 3, 0, random);
      }

      if (random.nextInt(3) != 0) {
         place(b, "MARKET", 0, -stallPos + 3, 2, random);
      }

      int flowerX = 12;
      int flowerZ = 18;

      for (int i1 : new int[]{-flowerX, flowerX}) {
         place(b, "WesterosTownGarden", i1, flowerZ, 0, random);
         place(b, "WesterosTownGarden", i1, -flowerZ, 2, random);
         place(b, "WesterosTownGarden", -flowerZ, i1, 1, random);
         place(b, "WesterosTownGarden", flowerZ, i1, 3, random);
      }

      int lampZ = 21;

      for (int i1 : new int[]{-1, 1}) {
         int lampX = i1 * 6;
         place(b, "WesterosLampPost", lampX, lampZ, 0, random);
         place(b, "WesterosLampPost", lampX, -lampZ, 2, random);
         if (i1 != -1) {
            place(b, "WesterosLampPost", -lampZ, lampX, 1, random);
         }

         place(b, "WesterosLampPost", lampZ, lampX, 3, random);
      }

      int houseX = 24;

      for (int k1 = -1; k1 <= 1; k1++) {
         int houseZ = k1 * 12;
         if (k1 == 1) {
            place(b, "NorthHouseLarge", -houseX, houseZ, 1, random);
            place(b, "NorthHouseLarge", houseX, houseZ, 3, random);
         }

         if (k1 != 0) {
            place(b, "NorthHouseLarge", houseZ, houseX, 0, random);
            place(b, "NorthHouseLarge", houseZ, -houseX, 2, random);
         }
      }

      place(b, "NorthSmithy", 0, -26, 2, random);
      place(b, "WesterosObelisk", 0, 27, 0, random);
      place(b, "NorthTavern", -houseX, -5, 1, random);
      place(b, "WesterosTownTrees", -47, -13, 2, random);
      place(b, "WesterosTownTrees", -47, 1, 0, random);

      for (int i1 : new int[]{-43, -51}) {
         place(b, "WesterosTownBench", i1, -9, 2, random);
         place(b, "WesterosTownBench", i1, -3, 0, random);
      }

      place(b, "NorthBath", houseX + 2, -6, 3, random);
      place(b, "WesterosTownGarden", 51, -13, 2, random);
      place(b, "WesterosTownGarden", 51, 1, 0, random);
      place(b, "WesterosTownGarden", 52, -6, 3, random);
      int wellX = 22;
      int wellZ = 31;

      for (int i1 : new int[]{-wellX, wellX}) {
         place(b, "WesterosWell", i1, -wellZ, 2, random);
         place(b, "WesterosWell", i1, wellZ, 0, random);
         place(b, "WesterosWell", -wellZ, i1, 1, random);
         place(b, "WesterosWell", wellZ, i1, 3, random);
      }

      int var27 = 54;

      for (int k1 = -2; k1 <= 2; k1++) {
         int houseZ = k1 * 12;
         if (k1 == -2 || k1 >= 1) {
            place(b, "NorthHouseLarge", -var27, houseZ, 3, random);
            place(b, "NorthHouseLarge", var27, houseZ, 1, random);
         }

         place(b, "NorthHouseLarge", houseZ, var27, 2, random);
         place(b, "NorthHouseLarge", houseZ, -var27, 0, random);
      }

      int treeX = 47;
      int treeZ = 35;

      for (int i1 : new int[]{-treeX, treeX}) {
         place(b, "WesterosTownTrees", i1, -treeZ, 0, random);
         place(b, "WesterosTownTrees", i1, treeZ, 2, random);
         place(b, "WesterosTownTrees", -treeZ, i1, 3, random);
         place(b, "WesterosTownTrees", treeZ, i1, 1, random);
      }

      var27 = 64;
      int lampX = 59;

      for (int k1 = -4; k1 <= 4; k1++) {
         int houseZ = k1 * 12;
         boolean treepiece = Math.floorMod(k1, 2) == 1;
         if (treepiece) {
            place(b, "FARM_TREE", -var27 - 2, houseZ, 1, random);
            place(b, "FARM_TREE", var27 + 2, houseZ, 3, random);
         } else {
            place(b, "NorthHouseLarge", -var27, houseZ, 1, random);
            place(b, "NorthHouseLarge", var27, houseZ, 3, random);
         }

         if (treepiece) {
            place(b, "FARM_TREE", houseZ, -var27 - 2, 2, random);
         } else {
            place(b, "NorthHouseLarge", houseZ, -var27, 2, random);
         }

         if (Math.abs(k1) >= 2 && (!outerTavern || k1 <= 2)) {
            if (treepiece) {
               place(b, "FARM_TREE", houseZ, var27 + 2, 0, random);
            } else {
               place(b, "NorthHouseLarge", houseZ, var27, 0, random);
            }
         }

         place(b, "WesterosLampPost", -lampX, houseZ, 1, random);
         place(b, "WesterosLampPost", lampX, houseZ, 3, random);
         place(b, "WesterosLampPost", houseZ, lampX, 0, random);
         place(b, "WesterosLampPost", houseZ, -lampX, 2, random);
      }

      if (outerTavern) {
         place(b, "NorthTavern", 44, var27, 0, random);
      }

      int gardenX = 42;
      int gardenZ = 48;
      place(b, "FARM_TREE", -gardenX, -gardenZ, 1, random);
      place(b, "FARM_TREE", -gardenX, gardenZ, 1, random);
      place(b, "FARM_TREE", gardenX, -gardenZ, 3, random);
      place(b, "FARM_TREE", gardenX, gardenZ, 3, random);
      int obeliskX = 62;
      int obeliskZ = 66;
      place(b, "WesterosObelisk", -obeliskX, -obeliskZ, 1, random);
      place(b, "WesterosObelisk", -obeliskX, obeliskZ, 1, random);
      place(b, "WesterosObelisk", obeliskX, -obeliskZ, 3, random);
      place(b, "WesterosObelisk", obeliskX, obeliskZ, 3, random);
      int var34 = 64;
      int var40 = 57;
      place(b, "WesterosWell", -var34, -var40, 1, random);
      place(b, "WesterosWell", -var34, var40, 1, random);
      place(b, "WesterosWell", var34, -var40, 3, random);
      place(b, "WesterosWell", var34, var40, 3, random);
      place(b, "WesterosWell", -var40, -var34, 2, random);
      place(b, "WesterosWell", var40, -var34, 2, random);
      place(b, "WesterosWell", -var40, var34, 0, random);
      place(b, "WesterosWell", var40, var34, 0, random);
      int var46 = 75;
      int var52 = 61;
      place(b, "WesterosTownTrees", -var46, -var52, 1, random);
      place(b, "WesterosTownTrees", -var46, var52, 1, random);
      place(b, "WesterosTownTrees", var46, -var52, 3, random);
      place(b, "WesterosTownTrees", var46, var52, 3, random);
      place(b, "WesterosTownTrees", -var52, -var46, 2, random);
      place(b, "WesterosTownTrees", var52, -var46, 2, random);
      place(b, "WesterosTownTrees", -var52, var46, 0, random);
      place(b, "WesterosTownTrees", var52, var46, 0, random);
      place(b, "WesterosTownTrees", -14, 71, 1, random);
      place(b, "WesterosTownTrees", 14, 71, 3, random);
   
   }

   public static void town(NorthStructureBuilder b) {
      Random random = new Random(b.seed());

      boolean outerTavern = random.nextBoolean();
      place(b, "RESPAWNER", 0, 0, 0, random);

      for (int i1 : new int[]{-40, 40}) {
         int[] arrn = new int[]{-40, 40};

         for (int k1 : arrn) {
            place(b, "RESPAWNER", i1, k1, 0, random);
         }
      }

      place(b, "WesterosWell", 0, -4, 0, random);
      int stallPos = 12;

      for (int k1 = -1; k1 <= 1; k1++) {
         int k2 = k1 * stallPos;
         if (random.nextInt(3) != 0) {
            place(b, "MARKET", -stallPos + 3, k2, 1, random);
         }

         if (random.nextInt(3) != 0) {
            place(b, "MARKET", stallPos - 3, k2, 3, random);
         }
      }

      if (random.nextInt(3) != 0) {
         place(b, "MARKET", 0, stallPos - 3, 0, random);
      }

      if (random.nextInt(3) != 0) {
         place(b, "MARKET", 0, -stallPos + 3, 2, random);
      }

      int flowerX = 12;
      int flowerZ = 18;

      for (int i1 : new int[]{-flowerX, flowerX}) {
         place(b, "WesterosTownGarden", i1, flowerZ, 0, random);
         place(b, "WesterosTownGarden", i1, -flowerZ, 2, random);
         place(b, "WesterosTownGarden", -flowerZ, i1, 1, random);
         place(b, "WesterosTownGarden", flowerZ, i1, 3, random);
      }

      int lampZ = 21;

      for (int i1 : new int[]{-1, 1}) {
         int lampX = i1 * 6;
         place(b, "WesterosLampPost", lampX, lampZ, 0, random);
         place(b, "WesterosLampPost", lampX, -lampZ, 2, random);
         if (i1 != -1) {
            place(b, "WesterosLampPost", -lampZ, lampX, 1, random);
         }

         place(b, "WesterosLampPost", lampZ, lampX, 3, random);
      }

      int houseX = 24;

      for (int k1 = -1; k1 <= 1; k1++) {
         int houseZ = k1 * 12;
         if (k1 == 1) {
            place(b, "NorthHouseLarge", -houseX, houseZ, 1, random);
            place(b, "NorthHouseLarge", houseX, houseZ, 3, random);
         }

         if (k1 != 0) {
            place(b, "NorthHouseLarge", houseZ, houseX, 0, random);
            place(b, "NorthHouseLarge", houseZ, -houseX, 2, random);
         }
      }

      place(b, "NorthSmithy", 0, -26, 2, random);
      place(b, "WesterosObelisk", 0, 27, 0, random);
      place(b, "NorthTavern", -houseX, -5, 1, random);
      place(b, "WesterosTownTrees", -47, -13, 2, random);
      place(b, "WesterosTownTrees", -47, 1, 0, random);

      for (int i1 : new int[]{-43, -51}) {
         place(b, "WesterosTownBench", i1, -9, 2, random);
         place(b, "WesterosTownBench", i1, -3, 0, random);
      }

      place(b, "NorthBath", houseX + 2, -6, 3, random);
      place(b, "WesterosTownGarden", 51, -13, 2, random);
      place(b, "WesterosTownGarden", 51, 1, 0, random);
      place(b, "WesterosTownGarden", 52, -6, 3, random);
      int wellX = 22;
      int wellZ = 31;

      for (int i1 : new int[]{-wellX, wellX}) {
         place(b, "WesterosWell", i1, -wellZ, 2, random);
         place(b, "WesterosWell", i1, wellZ, 0, random);
         place(b, "WesterosWell", -wellZ, i1, 1, random);
         place(b, "WesterosWell", wellZ, i1, 3, random);
      }

      int var35 = 54;

      for (int k1 = -2; k1 <= 2; k1++) {
         int houseZ = k1 * 12;
         if (k1 == -2 || k1 >= 1) {
            place(b, "NorthHouseLarge", -var35, houseZ, 3, random);
            place(b, "NorthHouseLarge", var35, houseZ, 1, random);
         }

         place(b, "NorthHouseLarge", houseZ, var35, 2, random);
         place(b, "NorthHouseLarge", houseZ, -var35, 0, random);
      }

      int treeX = 47;
      int treeZ = 35;

      for (int i1 : new int[]{-treeX, treeX}) {
         place(b, "WesterosTownTrees", i1, -treeZ, 0, random);
         place(b, "WesterosTownTrees", i1, treeZ, 2, random);
         place(b, "WesterosTownTrees", -treeZ, i1, 3, random);
         place(b, "WesterosTownTrees", treeZ, i1, 1, random);
      }

      var35 = 64;
      int lampX = 59;

      for (int k1 = -4; k1 <= 4; k1++) {
         int houseZ = k1 * 12;
         boolean treepiece = Math.floorMod(k1, 2) == 1;
         if (treepiece) {
            place(b, "FARM_TREE", -var35 - 2, houseZ, 1, random);
            place(b, "FARM_TREE", var35 + 2, houseZ, 3, random);
         } else {
            place(b, "NorthHouseLarge", -var35, houseZ, 1, random);
            place(b, "NorthHouseLarge", var35, houseZ, 3, random);
         }

         if (treepiece) {
            place(b, "FARM_TREE", houseZ, -var35 - 2, 2, random);
         } else {
            place(b, "NorthHouseLarge", houseZ, -var35, 2, random);
         }

         if (Math.abs(k1) >= 2 && (!outerTavern || k1 <= 2)) {
            if (treepiece) {
               place(b, "FARM_TREE", houseZ, var35 + 2, 0, random);
            } else {
               place(b, "NorthHouseLarge", houseZ, var35, 0, random);
            }
         }

         place(b, "WesterosLampPost", -lampX, houseZ, 1, random);
         place(b, "WesterosLampPost", lampX, houseZ, 3, random);
         place(b, "WesterosLampPost", houseZ, lampX, 0, random);
         place(b, "WesterosLampPost", houseZ, -lampX, 2, random);
      }

      if (outerTavern) {
         place(b, "NorthTavern", 44, var35, 0, random);
      }

      int gardenX = 42;
      int gardenZ = 48;
      place(b, "FARM_TREE", -gardenX, -gardenZ, 1, random);
      place(b, "FARM_TREE", -gardenX, gardenZ, 1, random);
      place(b, "FARM_TREE", gardenX, -gardenZ, 3, random);
      place(b, "FARM_TREE", gardenX, gardenZ, 3, random);
      int obeliskX = 62;
      int obeliskZ = 66;
      place(b, "WesterosObelisk", -obeliskX, -obeliskZ, 1, random);
      place(b, "WesterosObelisk", -obeliskX, obeliskZ, 1, random);
      place(b, "WesterosObelisk", obeliskX, -obeliskZ, 3, random);
      place(b, "WesterosObelisk", obeliskX, obeliskZ, 3, random);
      int var42 = 64;
      int var48 = 57;
      place(b, "WesterosWell", -var42, -var48, 1, random);
      place(b, "WesterosWell", -var42, var48, 1, random);
      place(b, "WesterosWell", var42, -var48, 3, random);
      place(b, "WesterosWell", var42, var48, 3, random);
      place(b, "WesterosWell", -var48, -var42, 2, random);
      place(b, "WesterosWell", var48, -var42, 2, random);
      place(b, "WesterosWell", -var48, var42, 0, random);
      place(b, "WesterosWell", var48, var42, 0, random);
      int var54 = 75;
      int var60 = 61;
      place(b, "WesterosTownTrees", -var54, -var60, 1, random);
      place(b, "WesterosTownTrees", -var54, var60, 1, random);
      place(b, "WesterosTownTrees", var54, -var60, 3, random);
      place(b, "WesterosTownTrees", var54, var60, 3, random);
      place(b, "WesterosTownTrees", -var60, -var54, 2, random);
      place(b, "WesterosTownTrees", var60, -var54, 2, random);
      place(b, "WesterosTownTrees", -var60, var54, 0, random);
      place(b, "WesterosTownTrees", var60, var54, 0, random);
      place(b, "WesterosTownTrees", -14, 71, 1, random);
      place(b, "WesterosTownTrees", 14, 71, 3, random);

      for (int k1 : new int[]{67, 75}) {
         place(b, "WesterosTownBench", -10, k1, 1, random);
         place(b, "WesterosTownBench", 10, k1, 3, random);
      }

      place(b, "NorthGatehouse", 0, 84, 2, random);
      place(b, "WesterosLampPost", -4, 73, 0, random);
      place(b, "WesterosLampPost", 4, 73, 0, random);
      int towerX = 78;
      int towerZ = 74;

      for (int i1 : new int[]{-towerX, towerX}) {
         place(b, "NorthWatchtower", i1, -towerZ, 2, random);
         place(b, "NorthWatchtower", i1, towerZ, 0, random);
      }

      int wallZ = 82;
      int wallEndX = 76;

      for (int l = 0; l <= 3; l++) {
         int wallX = 12 + l * 16;
         place(b, "TOWN_WALL_LEFT", -wallX, wallZ, 2, random);
         place(b, "TOWN_WALL_RIGHT", wallX, wallZ, 2, random);
      }

      place(b, "TOWN_WALL_LEFT_END_SHORT", -wallEndX, wallZ, 2, random);
      place(b, "TOWN_WALL_RIGHT_END_SHORT", wallEndX, wallZ, 2, random);
      place(b, "TOWN_WALL_CENTRE", -wallZ, 0, 3, random);
      place(b, "TOWN_WALL_CENTRE", wallZ, 0, 1, random);
      place(b, "TOWN_WALL_CENTRE", 0, -wallZ, 0, random);

      for (int var71 = 0; var71 <= 3; var71++) {
         int wallX = 12 + var71 * 16;
         place(b, "TOWN_WALL_LEFT", -wallZ, -wallX, 3, random);
         place(b, "TOWN_WALL_RIGHT", -wallZ, wallX, 3, random);
         place(b, "TOWN_WALL_LEFT", wallZ, wallX, 1, random);
         place(b, "TOWN_WALL_RIGHT", wallZ, -wallX, 1, random);
         place(b, "TOWN_WALL_LEFT", wallX, -wallZ, 0, random);
         place(b, "TOWN_WALL_RIGHT", -wallX, -wallZ, 0, random);
      }

      place(b, "TOWN_WALL_LEFT_END", -wallZ, -wallEndX, 3, random);
      place(b, "TOWN_WALL_RIGHT_END", -wallZ, wallEndX, 3, random);
      place(b, "TOWN_WALL_LEFT_END", wallZ, wallEndX, 1, random);
      place(b, "TOWN_WALL_RIGHT_END", wallZ, -wallEndX, 1, random);
      place(b, "TOWN_WALL_LEFT_END_SHORT", wallEndX, -wallZ, 0, random);
      place(b, "TOWN_WALL_RIGHT_END_SHORT", -wallEndX, -wallZ, 0, random);
   
   }

   private static void place(NorthStructureBuilder parent, String piece, int x, int z,
                             int rotation, Random random) {
      NorthStructureBuilder b = parent.child(x, 0, z, rotation, random.nextLong());
      switch (piece) {
         case "RESPAWNER" -> b.marker("north_population_respawner", 0, 0, 0);
         case "MARKET" -> got.world.structure.legacy.generated.GOTStructureWesterosMarketStall.place(b, random.nextInt(11));
         case "RANDOM_HOUSE" -> {
            if (random.nextInt(5) == 0) switch (random.nextInt(3)) {
               case 0 -> got.world.structure.legacy.generated.GOTStructureWesterosStables.place(b, 0);
               case 1 -> NorthStructureTemplates.generate(b, NorthStructureType.SMITHY);
               default -> got.world.structure.legacy.generated.GOTStructureWesterosBarn.place(b, 0);
            } else NorthStructureTemplates.generate(b, NorthStructureType.HOUSE_SMALL);
         }
         case "RANDOM_FARM" -> {
            int farm = random.nextBoolean() ? (random.nextBoolean() ? 1 : 0) : 2;
            NorthStructureTemplates.villageFarm(b, farm);
         }
         case "FARM_TREE" -> got.world.structure.legacy.generated.GOTStructureWesterosVillageFarm$Tree.place(b, 0);
         case "FARM_CROPS" -> got.world.structure.legacy.generated.GOTStructureWesterosVillageFarm$Crops.place(b, 0);
         case "HAY" -> b.fill(-2, 0, -2, 2, 2, 2, Blocks.HAY_BLOCK.defaultBlockState());
         case "WesterosWell", "MossovyWell" -> got.world.structure.legacy.generated.GOTStructureWesterosWell.place(b, 0);
         case "WesterosLampPost" -> got.world.structure.legacy.generated.GOTStructureWesterosLampPost.place(b, 0);
         case "WesterosObelisk" -> got.world.structure.legacy.generated.GOTStructureWesterosObelisk.place(b, 0);
         case "WesterosTownBench" -> got.world.structure.legacy.generated.GOTStructureWesterosTownBench.place(b, 0);
         case "WesterosTownGarden" -> got.world.structure.legacy.generated.GOTStructureWesterosTownGarden.place(b, 0);
         case "WesterosTownTrees" -> got.world.structure.legacy.generated.GOTStructureWesterosTownTrees.place(b, 0);
         case "WesterosVillageSign" -> got.world.structure.legacy.generated.GOTStructureWesterosVillageSign.place(b, 0);
         case "NorthHouse" -> NorthStructureTemplates.generate(b, NorthStructureType.HOUSE);
         case "NorthHouseLarge" -> NorthStructureTemplates.generate(b, NorthStructureType.HOUSE_LARGE);
         case "NorthHouseSmall" -> NorthStructureTemplates.generate(b, NorthStructureType.HOUSE_SMALL);
         case "NorthTavern" -> got.world.structure.legacy.generated.GOTStructureWesterosTavern.place(b, 0);
         case "NorthSmithy" -> NorthStructureTemplates.generate(b, NorthStructureType.SMITHY);
         case "NorthBarn" -> got.world.structure.legacy.generated.GOTStructureWesterosBarn.place(b, 0);
         case "NorthBath" -> got.world.structure.legacy.generated.GOTStructureWesterosBath.place(b, 0);
         case "NorthGatehouse" -> got.world.structure.legacy.generated.GOTStructureWesterosGatehouse.place(b, 0);
         case "NorthWatchtower" -> got.world.structure.legacy.generated.GOTStructureWesterosWatchtower.place(b, 0);
         case "NorthHillmanHouse" -> got.world.structure.legacy.generated.GOTStructureNorthHillmanHouse.place(b, 0);
         case "NorthHillmanHouse_SPECIAL" -> got.world.structure.legacy.generated.GOTStructureNorthHillmanHouse.place(b, 1);
         case "NorthHillmanChieftainHouse" -> got.world.structure.legacy.generated.GOTStructureNorthHillmanChieftainHouse.place(b, 0);
         case "TOWN_WALL_CENTRE" -> got.world.structure.legacy.generated.GOTStructureWesterosTownWall.place(b, 0);
         case "TOWN_WALL_LEFT" -> got.world.structure.legacy.generated.GOTStructureWesterosTownWall.place(b, 1);
         case "TOWN_WALL_LEFT_END" -> got.world.structure.legacy.generated.GOTStructureWesterosTownWall.place(b, 2);
         case "TOWN_WALL_LEFT_END_SHORT" -> got.world.structure.legacy.generated.GOTStructureWesterosTownWall.place(b, 3);
         case "TOWN_WALL_RIGHT" -> got.world.structure.legacy.generated.GOTStructureWesterosTownWall.place(b, 4);
         case "TOWN_WALL_RIGHT_END" -> got.world.structure.legacy.generated.GOTStructureWesterosTownWall.place(b, 5);
         case "TOWN_WALL_RIGHT_END_SHORT" -> got.world.structure.legacy.generated.GOTStructureWesterosTownWall.place(b, 6);
         default -> throw new IllegalArgumentException("Unmapped recovered North piece: " + piece);
      }
   }
}
