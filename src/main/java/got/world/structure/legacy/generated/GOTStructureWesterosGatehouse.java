package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureWesterosGatehouse extends LegacyNorthernContext {

   protected GOTStructureWesterosGatehouse(NorthStructureBuilder builder) {
      super(builder, Style.NORTH);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureWesterosGatehouse piece = new GOTStructureWesterosGatehouse(builder);
      piece.marketVariant = Math.max(0, variant);
      piece.isAbandoned = variant == 1;
      piece.isTramp = variant == 1;
      piece.isHardhome = variant == 1;
      piece.isBlacksmith = variant == 1;
      piece.generate(piece, piece.legacyRandom(), 0, 0, 0, 0);
   }


   public boolean generate(LegacyNorthernContext world, Random random, int i, int j, int k, int rotation) {
      this.setOriginAndRotation(world, i, j, k, rotation, 4);
      this.setupRandomBlocks(random);
      if (this.restrictions) {
         int minHeight = 0;
         int maxHeight = 0;

         for (int i15 = -6; i15 <= 6; i15++) {
            for (int k14 = -5; k14 <= 5; k14++) {
               int j12 = this.getTopBlock(world, i15, k14) - 1;
               if (!this.isSurface(world, i15, j12, k14)) {
                  return false;
               }

               if (j12 < minHeight) {
                  minHeight = j12;
               }

               if (j12 > maxHeight) {
                  maxHeight = j12;
               }

               if (maxHeight - minHeight > 8) {
                  return false;
               }
            }
         }
      }

      for (int i14 = -3; i14 <= 3; i14++) {
         for (int k13 = -3; k13 <= 3; k13++) {
            int i2 = Math.abs(i14);
            int k2 = Math.abs(k13);

            for (int j12 = 0; (j12 >= 0 || !this.isOpaque(world, i14, j12, k13)) && this.getY(j12) >= 0; j12--) {
               this.setBlockAndMetadata(world, i14, j12, k13, this.cobbleBlock, this.cobbleMeta);
               this.setGrassToDirt(world, i14, j12 - 1, k13);
            }

            for (int var46 = 1; var46 <= 14; var46++) {
               this.setAir(world, i14, var46, k13);
            }

            if (i2 == 3 && k2 == 3) {
               for (int var48 = 1; var48 <= 10; var48++) {
                  this.setBlockAndMetadata(world, i14, var48, k13, this.pillarBlock, this.pillarMeta);
               }
            } else {
               if (i2 == 3) {
                  for (int var47 = 1; var47 <= 6; var47++) {
                     this.setBlockAndMetadata(world, i14, var47, k13, this.brickBlock, this.brickMeta);
                  }
               }

               this.setBlockAndMetadata(world, i14, 7, k13, this.brickBlock, this.brickMeta);
            }

            if (i2 <= 3 && k2 <= 3) {
               if (i2 != 3 && k2 != 3) {
                  this.setBlockAndMetadata(world, i14, 11, k13, this.rockSlabBlock, this.rockSlabMeta | 8);
               } else {
                  this.setBlockAndMetadata(world, i14, 11, k13, this.rockSlabDoubleBlock, this.rockSlabDoubleMeta);
               }
            }
         }
      }

      for (int var54 = -2; var54 <= 2; var54++) {
         this.setBlockAndMetadata(world, var54, 0, -1, this.cobbleStairBlock, 3);
         this.setBlockAndMetadata(world, var54, 0, 1, this.cobbleStairBlock, 2);
         this.setBlockAndMetadata(world, var54, -1, 0, this.cobbleBlock, this.cobbleMeta);
         this.setGrassToDirt(world, var54, -2, 0);
         this.setAir(world, var54, 0, 0);
      }

      for (int var55 = -2; var55 <= 2; var55++) {
         for (int j13 = 1; j13 <= 7; j13++) {
            if (j13 <= 6 || var55 == 0) {
               this.setBlockAndMetadata(world, var55, j13, -1, this.gateBlock, 2);
               this.setBlockAndMetadata(world, var55, j13, 1, got("gateIronBars"), 2);
            }
         }
      }

      for (int k14 : new int[]{-3, 3}) {
         this.setBlockAndMetadata(world, -2, 6, k14, this.brickStairBlock, 4);
         this.setBlockAndMetadata(world, 2, 6, k14, this.brickStairBlock, 5);
         this.setBlockAndMetadata(world, -2, 5, k14, vanilla("field_150478_aa"), 2);
         this.setBlockAndMetadata(world, 2, 5, k14, vanilla("field_150478_aa"), 1);

         for (int i16 = -2; i16 <= 2; i16++) {
            int i23 = Math.abs(i16);
            this.setBlockAndMetadata(world, i16, 8, k14, this.rockSlabDoubleBlock, this.rockSlabDoubleMeta);
            if (i23 % 2 == 0) {
               this.setBlockAndMetadata(world, i16, 9, k14, got("gateIronBars"), 2);
            } else {
               this.setBlockAndMetadata(world, i16, 9, k14, this.brickBlock, this.brickMeta);
            }

            if (i23 == 0) {
               this.setBlockAndMetadata(world, i16, 10, k14, this.brickCarved, this.brickCarvedMeta);
            } else {
               this.setBlockAndMetadata(world, i16, 10, k14, this.brickBlock, this.brickMeta);
            }
         }
      }

      for (int i122 : new int[]{-3, 3}) {
         for (int k15 : new int[]{-2, 2}) {
            this.setBlockAndMetadata(world, i122, 8, k15, this.rockSlabDoubleBlock, this.rockSlabDoubleMeta);
            this.setBlockAndMetadata(world, i122, 9, k15, this.brickBlock, this.brickMeta);
            this.setBlockAndMetadata(world, i122, 10, k15, this.brickBlock, this.brickMeta);
         }

         this.setBlockAndMetadata(world, i122, 10, -1, this.brickStairBlock, 7);
         this.setBlockAndMetadata(world, i122, 10, 1, this.brickStairBlock, 6);
      }

      for (int i13 = -3; i13 <= 3; i13++) {
         this.setBlockAndMetadata(world, i13, 11, -4, this.brickStairBlock, 6);
         this.setBlockAndMetadata(world, i13, 11, 4, this.brickStairBlock, 7);
      }

      for (int k16 = -3; k16 <= 3; k16++) {
         this.setBlockAndMetadata(world, -4, 11, k16, this.brickStairBlock, 5);
         this.setBlockAndMetadata(world, 4, 11, k16, this.brickStairBlock, 4);
      }

      for (int var53 = -4; var53 <= 4; var53++) {
         for (int k13 = -4; k13 <= 4; k13++) {
            int var28 = Math.abs(var53);
            int k2 = Math.abs(k13);
            if ((var28 <= 3 || k2 <= 3) && (var28 == 4 || k2 == 4)) {
               if ((var28 + k2) % 2 != 0) {
                  this.setBlockAndMetadata(world, var53, 12, k13, this.brickBlock, this.brickMeta);
                  this.setBlockAndMetadata(world, var53, 13, k13, this.brickSlabBlock, this.brickSlabMeta);
               } else {
                  this.setBlockAndMetadata(world, var53, 12, k13, this.brickWallBlock, this.brickWallMeta);
               }
            }
         }
      }

      this.setBlockAndMetadata(world, 0, 8, -1, this.plankBlock, this.plankMeta);
      this.setBlockAndMetadata(world, 0, 8, 0, this.fenceBlock, this.fenceMeta);
      this.setAir(world, 0, 7, 0);
      this.setBlockAndMetadata(world, 0, 8, 1, this.plankBlock, this.plankMeta);
      this.setBlockAndMetadata(world, 0, 9, -1, vanilla("field_150442_at"), 14);
      this.setBlockAndMetadata(world, 0, 9, 1, vanilla("field_150442_at"), 14);

      for (int i1221 : new int[]{-1, 1}) {
         for (int j12 = 8; j12 <= 11; j12++) {
            this.setBlockAndMetadata(world, i1221, j12, 2, vanilla("field_150468_ap"), 2);
         }
      }

      this.setBlockAndMetadata(world, -2, 10, -2, vanilla("field_150478_aa"), 2);
      this.setBlockAndMetadata(world, 2, 10, -2, vanilla("field_150478_aa"), 1);
      this.setBlockAndMetadata(world, -2, 10, 2, vanilla("field_150478_aa"), 2);
      this.setBlockAndMetadata(world, 2, 10, 2, vanilla("field_150478_aa"), 1);
      this.placeWallBanner(world, 1, 10, -3, this.bannerType, 0);
      this.placeWallBanner(world, -1, 10, -3, this.bannerType, 0);
      this.setBlockAndMetadata(world, -3, 12, -3, vanilla("field_150478_aa"), 3);
      this.setBlockAndMetadata(world, 3, 12, -3, vanilla("field_150478_aa"), 3);
      this.setBlockAndMetadata(world, -3, 12, 3, vanilla("field_150478_aa"), 4);
      this.setBlockAndMetadata(world, 3, 12, 3, vanilla("field_150478_aa"), 4);
      this.placeWallBanner(world, -3, 7, -3, this.bannerType, 2);
      this.placeWallBanner(world, 3, 7, -3, this.bannerType, 2);
      this.placeWallBanner(world, 3, 7, 3, this.bannerType, 0);
      this.placeWallBanner(world, -3, 7, 3, this.bannerType, 0);

      for (int i1 = -5; i1 <= 5; i1++) {
         int i22 = Math.abs(i1);
         if (i22 >= 4) {
            for (int k12 = -1; k12 <= 1; k12++) {
               for (int j1 = 4; (j1 >= 0 || !this.isOpaque(world, i1, j1, k12)) && this.getY(j1) >= 0; j1--) {
                  this.setBlockAndMetadata(world, i1, j1, k12, this.brickBlock, this.brickMeta);
                  this.setGrassToDirt(world, i1, j1 - 1, k12);
               }
            }

            int var36 = -2;

            for (int j1 = 7; (j1 >= 0 || !this.isOpaque(world, i1, j1, var36)) && this.getY(j1) >= 0; j1--) {
               this.setBlockAndMetadata(world, i1, j1, var36, this.brickBlock, this.brickMeta);
               this.setGrassToDirt(world, i1, j1 - 1, var36);
            }

            this.setBlockAndMetadata(world, i1, 4, var36, this.brick2Block, this.brick2Meta);
            this.setBlockAndMetadata(world, i1, 8, var36, this.rockWallBlock, this.rockWallMeta);
         }

         if (i22 == 5) {
            int k12 = -3;

            for (int j1 = 7; (j1 >= 0 || !this.isOpaque(world, i1, j1, k12)) && this.getY(j1) >= 0; j1--) {
               this.setBlockAndMetadata(world, i1, j1, k12, this.pillarBlock, this.pillarMeta);
               this.setGrassToDirt(world, i1, j1 - 1, k12);
            }

            this.setBlockAndMetadata(world, i1, 8, k12, this.rockWallBlock, this.rockWallMeta);
         }

         if (i22 == 4) {
            int k12 = -3;
            this.setBlockAndMetadata(world, i1, 5, k12, this.brickStairBlock, 6);
            this.setBlockAndMetadata(world, i1, 6, k12, this.rockWallBlock, this.rockWallMeta);
         }
      }

      for (int k1 = -1; k1 <= 1; k1++) {
         this.setBlockAndMetadata(world, -3, 7, k1, this.brickStairBlock, 1);
         this.setBlockAndMetadata(world, -4, 6, k1, this.brickStairBlock, 1);
         this.setBlockAndMetadata(world, -5, 5, k1, this.brickStairBlock, 1);
         this.setBlockAndMetadata(world, -4, 5, k1, this.brickBlock, this.brickMeta);
         this.setBlockAndMetadata(world, 3, 7, k1, this.brickStairBlock, 0);
         this.setBlockAndMetadata(world, 4, 6, k1, this.brickStairBlock, 0);
         this.setBlockAndMetadata(world, 5, 5, k1, this.brickStairBlock, 0);
         this.setBlockAndMetadata(world, 4, 5, k1, this.brickBlock, this.brickMeta);
      }

      for (int var41 = -8; var41 <= 8; var41++) {
         int i22 = Math.abs(var41);
         if (i22 >= 6) {
            for (int k12 = 0; k12 <= 1; k12++) {
               for (int j1 = 4; (j1 >= 0 || !this.isOpaque(world, var41, j1, k12)) && this.getY(j1) >= 0; j1--) {
                  this.setBlockAndMetadata(world, var41, j1, k12, this.brickBlock, this.brickMeta);
                  this.setGrassToDirt(world, var41, j1 - 1, k12);
               }
            }
         }
      }

      for (int var29 = 0; var29 <= 1; var29++) {
         int maxStep = 12;

         int i122;
         int j12;
         for (int step = 0; step < maxStep && !this.isOpaque(world, i122 = -9 - step, j12 = 4 - step, var29); step++) {
            this.setBlockAndMetadata(world, i122, j12, var29, this.brickStairBlock, 1);
            this.setGrassToDirt(world, i122, j12 - 1, var29);

            for (int j2 = j12 - 1; !this.isOpaque(world, i122, j2, var29) && this.getY(j2) >= 0; j2--) {
               this.setBlockAndMetadata(world, i122, j2, var29, this.brickBlock, this.brickMeta);
               this.setGrassToDirt(world, i122, j2 - 1, var29);
            }
         }

         for (int var70 = 0; var70 < maxStep && !this.isOpaque(world, i122 = 9 + var70, j12 = 4 - var70, var29); var70++) {
            this.setBlockAndMetadata(world, i122, j12, var29, this.brickStairBlock, 0);
            this.setGrassToDirt(world, i122, j12 - 1, var29);

            for (int j2 = j12 - 1; !this.isOpaque(world, i122, j2, var29) && this.getY(j2) >= 0; j2--) {
               this.setBlockAndMetadata(world, i122, j2, var29, this.brickBlock, this.brickMeta);
               this.setGrassToDirt(world, i122, j2 - 1, var29);
            }
         }
      }

      for (int var42 = -9; var42 <= 9; var42++) {
         int i22 = Math.abs(var42);
         if (i22 == 5 || i22 == 8) {
            this.setBlockAndMetadata(world, var42, 3, 1, this.brickCarved, this.brickCarvedMeta);
         } else if (i22 >= 4) {
            this.setBlockAndMetadata(world, var42, 3, 1, this.brickStairBlock, 7);
         }
      }

      for (int i1221 : new int[]{-1, 1}) {
         int j12 = 8;
         int k17 = 0;
         LegacyEntity levyman = this.getSoldier(world);
         levyman.setSpawnRidingHorse(false);
         this.spawnNPCAndSetHome(levyman, world, i1221, j12, k17, 8);
      }

      return true;
   }

}
