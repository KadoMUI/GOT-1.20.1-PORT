package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureWesterosBath extends LegacyNorthernContext {

   protected GOTStructureWesterosBath(NorthStructureBuilder builder) {
      super(builder, Style.NORTH);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureWesterosBath piece = new GOTStructureWesterosBath(builder);
      piece.marketVariant = Math.max(0, variant);
      piece.isAbandoned = variant == 1;
      piece.isTramp = variant == 1;
      piece.isHardhome = variant == 1;
      piece.isBlacksmith = variant == 1;
      piece.generate(piece, piece.legacyRandom(), 0, 0, 0, 0);
   }


   public boolean generate(LegacyNorthernContext world, Random random, int i, int j, int k, int rotation) {
      this.setOriginAndRotation(world, i, j, k, rotation, 10);
      this.setupRandomBlocks(random);
      if (this.restrictions) {
         int minHeight = 0;
         int maxHeight = 0;

         for (int i12 = -11; i12 <= 11; i12++) {
            for (int k12 = -9; k12 <= 9; k12++) {
               int j1 = this.getTopBlock(world, i12, k12) - 1;
               if (!this.isSurface(world, i12, j1, k12)) {
                  return false;
               }

               if (j1 < minHeight) {
                  minHeight = j1;
               }

               if (j1 > maxHeight) {
                  maxHeight = j1;
               }

               if (maxHeight - minHeight > 6) {
                  return false;
               }
            }
         }
      }

      for (int i13 = -11; i13 <= 11; i13++) {
         for (int k1 = -9; k1 <= 9; k1++) {
            int i2 = Math.abs(i13);
            int k2 = Math.abs(k1);

            for (int j1 = 0; (j1 >= -1 || !this.isOpaque(world, i13, j1, k1)) && this.getY(j1) >= 0; j1--) {
               this.setBlockAndMetadata(world, i13, j1, k1, this.brickBlock, this.brickMeta);
               this.setGrassToDirt(world, i13, j1 - 1, k1);
            }

            for (int var25 = 1; var25 <= 8; var25++) {
               this.setAir(world, i13, var25, k1);
            }

            if (i2 <= 6 && k2 <= 4 && i2 + k2 <= 8) {
               this.setBlockAndMetadata(world, i13, 0, k1, vanilla("field_150355_j"), 0);
            }
         }
      }

      for (int i14 = -10; i14 <= 10; i14++) {
         for (int k1 = -8; k1 <= 8; k1++) {
            int i2 = Math.abs(i14);
            int k2 = Math.abs(k1);
            if (i2 == 10 && k2 % 4 == 0 || k2 == 8 && i2 % 4 == 2) {
               for (int j1 = 1; j1 <= 4; j1++) {
                  this.setBlockAndMetadata(world, i14, j1, k1, this.pillarBlock, this.pillarMeta);
               }

               this.setBlockAndMetadata(world, i14 - 1, 1, k1, this.brickStairBlock, 1);
               this.setBlockAndMetadata(world, i14 + 1, 1, k1, this.brickStairBlock, 0);
               this.setBlockAndMetadata(world, i14, 1, k1 - 1, this.brickStairBlock, 2);
               this.setBlockAndMetadata(world, i14, 1, k1 + 1, this.brickStairBlock, 3);
               this.setBlockAndMetadata(world, i14 - 1, 4, k1, this.brickStairBlock, 5);
               this.setBlockAndMetadata(world, i14 + 1, 4, k1, this.brickStairBlock, 4);
               this.setBlockAndMetadata(world, i14, 4, k1 - 1, this.brickStairBlock, 6);
               this.setBlockAndMetadata(world, i14, 4, k1 + 1, this.brickStairBlock, 7);
            }

            if (i2 == 10 || k2 == 8) {
               this.setBlockAndMetadata(world, i14, 5, k1, this.brickBlock, this.brickMeta);
            }
         }
      }

      for (int i15 : new int[]{-6, 6}) {
         for (int k13 : new int[]{-4, 4}) {
            for (int j12 = 1; j12 <= 7; j12++) {
               this.setBlockAndMetadata(world, i15, j12, k13, this.pillarBlock, this.pillarMeta);
            }

            this.setBlockAndMetadata(world, i15 - 1, 1, k13, this.brickStairBlock, 1);
            this.setBlockAndMetadata(world, i15 + 1, 1, k13, this.brickStairBlock, 0);
            this.setBlockAndMetadata(world, i15, 1, k13 - 1, this.brickStairBlock, 2);
            this.setBlockAndMetadata(world, i15, 1, k13 + 1, this.brickStairBlock, 3);
            this.setBlockAndMetadata(world, i15 - 1, 7, k13, this.brickStairBlock, 5);
            this.setBlockAndMetadata(world, i15 + 1, 7, k13, this.brickStairBlock, 4);
            this.setBlockAndMetadata(world, i15, 7, k13 - 1, this.brickStairBlock, 6);
            this.setBlockAndMetadata(world, i15, 7, k13 + 1, this.brickStairBlock, 7);
            this.setBlockAndMetadata(world, i15 - 1, 4, k13, vanilla("field_150478_aa"), 1);
            this.setBlockAndMetadata(world, i15 + 1, 4, k13, vanilla("field_150478_aa"), 2);
            this.setBlockAndMetadata(world, i15, 4, k13 - 1, vanilla("field_150478_aa"), 4);
            this.setBlockAndMetadata(world, i15, 4, k13 + 1, vanilla("field_150478_aa"), 3);
         }
      }

      for (int step = 0; step <= 3; step++) {
         int j13 = 5 + step;
         int i12 = 11 - step;
         int k12 = 9 - step;

         for (int i22 = -i12; i22 <= i12; i22++) {
            this.setBlockAndMetadata(world, i22, j13, -k12, this.brick2StairBlock, 2);
            this.setBlockAndMetadata(world, i22, j13, k12, this.brick2StairBlock, 3);
         }

         for (int k22 = -k12 + 1; k22 <= k12 - 1; k22++) {
            this.setBlockAndMetadata(world, -i12, j13, k22, this.brick2StairBlock, 1);
            this.setBlockAndMetadata(world, i12, j13, k22, this.brick2StairBlock, 0);
         }

         if (step >= 2) {
            for (int i22 = -i12 + 1; i22 <= i12 - 1; i22++) {
               this.setBlockAndMetadata(world, i22, j13 - 1, -k12, this.brick2StairBlock, 7);
               this.setBlockAndMetadata(world, i22, j13 - 1, k12, this.brick2StairBlock, 6);
            }

            for (int k22 = -k12; k22 <= k12; k22++) {
               this.setBlockAndMetadata(world, -i12, j13 - 1, k22, this.brick2StairBlock, 4);
               this.setBlockAndMetadata(world, i12, j13 - 1, k22, this.brick2StairBlock, 5);
            }
         }
      }

      for (int i1 = -7; i1 <= 7; i1++) {
         for (int var21 = -5; var21 <= 5; var21++) {
            this.setBlockAndMetadata(world, i1, 8, var21, this.brick2Block, this.brick2Meta);
         }
      }

      for (int var23 = -9; var23 <= 9; var23++) {
         for (int var22 = -7; var22 <= 7; var22++) {
            int var29 = Math.abs(var23);
            int k2 = Math.abs(var22);
            if (var29 == 9 && k2 % 4 == 0 || k2 == 7 && var29 % 4 == 2) {
               for (int j14 = 5; j14 <= 6; j14++) {
                  this.setBlockAndMetadata(world, var23, j14, var22, this.brickBlock, this.brickMeta);
               }
            }
         }
      }

      int bathers = 2 + random.nextInt(4);

      for (int l = 0; l < bathers; l++) {
         LegacyEntity man = this.getMan(world);
         this.spawnNPCAndSetHome(man, world, 0, 0, 0, 16);
      }

      return true;
   }

}
