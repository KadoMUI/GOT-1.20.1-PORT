package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureWesterosSmithy extends LegacyNorthernContext {

   protected GOTStructureWesterosSmithy(NorthStructureBuilder builder) {
      super(builder, Style.NORTH);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureWesterosSmithy piece = new GOTStructureWesterosSmithy(builder);
      piece.marketVariant = Math.max(0, variant);
      piece.isAbandoned = variant == 1;
      piece.isTramp = variant == 1;
      piece.isHardhome = variant == 1;
      piece.isBlacksmith = variant == 1;
      piece.generate(piece, piece.legacyRandom(), 0, 0, 0, 0);
   }


   protected boolean isKingsLanding;

   public boolean generate(LegacyNorthernContext world, Random random, int i, int j, int k, int rotation) {
      this.setOriginAndRotation(world, i, j, k, rotation, 0);
      this.setupRandomBlocks(random);
      if (this.restrictions) {
         int minHeight = 0;
         int maxHeight = 0;

         for (int i13 = -4; i13 <= 4; i13++) {
            for (int k12 = 1; k12 <= 11; k12++) {
               int j1 = this.getTopBlock(world, i13, k12) - 1;
               if (!this.isSurface(world, i13, j1, k12)) {
                  return false;
               }

               if (j1 < minHeight) {
                  minHeight = j1;
               }

               if (j1 > maxHeight) {
                  maxHeight = j1;
               }

               if (maxHeight - minHeight > 8) {
                  return false;
               }
            }
         }
      }

      for (int k1 = 1; k1 <= 11; k1++) {
         for (int i1 = -4; i1 <= 4; i1++) {
            boolean pillar = Math.abs(i1) == 4 && (k1 == 1 || k1 == 11);
            if (pillar) {
               for (int j1 = 4; (j1 >= 0 || !this.isOpaque(world, i1, j1, k1)) && this.getY(j1) >= 0; j1--) {
                  this.setBlockAndMetadata(world, i1, j1, k1, this.pillar2Block, this.pillar2Meta);
                  this.setGrassToDirt(world, i1, j1 - 1, k1);
               }
            } else {
               for (int j1 = 0; (j1 >= 0 || !this.isOpaque(world, i1, j1, k1)) && this.getY(j1) >= 0; j1--) {
                  this.setBlockAndMetadata(world, i1, j1, k1, this.rockSlabDoubleBlock, this.rockSlabDoubleMeta);
                  this.setGrassToDirt(world, i1, j1 - 1, k1);
               }

               if (Math.abs(i1) != 4 && k1 != 1 && k1 != 11) {
                  for (int var28 = 1; var28 <= 5; var28++) {
                     this.setAir(world, i1, var28, k1);
                  }
               } else {
                  for (int var27 = 1; var27 <= 3; var27++) {
                     this.setBlockAndMetadata(world, i1, var27, k1, this.brickBlock, this.brickMeta);
                  }

                  this.setBlockAndMetadata(world, i1, 4, k1, this.brickWallBlock, this.brickWallMeta);
               }
            }
         }
      }

      for (int var17 = 3; var17 <= 7; var17 += 4) {
         for (int i1 = -2; i1 <= 1; i1 += 3) {
            for (int k2 = var17; k2 <= var17 + 2; k2++) {
               for (int i2 = i1; i2 <= i1 + 1; i2++) {
                  this.setBlockAndMetadata(world, i2, 0, k2, this.rockBlock, this.rockMeta);
               }
            }
         }
      }

      for (int i12 = -1; i12 <= 1; i12++) {
         for (int j1 = 1; j1 <= 3; j1++) {
            this.setBlockAndMetadata(world, i12, j1, 1, this.rockBlock, this.rockMeta);
         }
      }

      this.setBlockAndMetadata(world, 0, 1, 1, this.fenceGateBlock, 0);
      this.setAir(world, 0, 2, 1);

      for (int var18 = 2; var18 <= 10; var18++) {
         this.setBlockAndMetadata(world, -4, 4, var18, this.brickWallBlock, this.brickWallMeta);
         this.setBlockAndMetadata(world, 4, 4, var18, this.brickWallBlock, this.brickWallMeta);
      }

      for (int var23 = -3; var23 <= 3; var23++) {
         this.setBlockAndMetadata(world, var23, 4, 1, this.brickWallBlock, this.brickWallMeta);
         this.setBlockAndMetadata(world, var23, 4, 11, this.brickWallBlock, this.brickWallMeta);
      }

      for (int var19 = 2; var19 <= 10; var19++) {
         for (int i1 = -3; i1 <= 3; i1++) {
            this.setBlockAndMetadata(world, i1, 5, var19, this.brick2Block, this.brick2Meta);
         }
      }

      for (int var20 = 2; var20 <= 10; var20++) {
         this.setBlockAndMetadata(world, -4, 5, var20, this.brick2StairBlock, 1);
         this.setBlockAndMetadata(world, 4, 5, var20, this.brick2StairBlock, 0);
      }

      for (int var24 = -4; var24 <= 4; var24++) {
         this.setBlockAndMetadata(world, var24, 5, 1, this.brick2StairBlock, 2);
         this.setBlockAndMetadata(world, var24, 5, 11, this.brick2StairBlock, 3);
      }

      this.setBlockAndMetadata(world, -3, 1, 2, vanilla("field_150462_ai"), 0);
      this.setBlockAndMetadata(world, -3, 1, 3, this.tableBlock, 0);
      this.setBlockAndMetadata(world, -3, 1, 4, this.brickBlock, this.brickMeta);
      this.setBlockAndMetadata(world, -3, 2, 4, this.brickWallBlock, this.brickWallMeta);

      for (int var21 = 2; var21 <= 4; var21++) {
         this.setBlockAndMetadata(world, -3, 3, var21, this.brickStairBlock, 0);
      }

      for (int var22 = 2; var22 <= 6; var22 += 2) {
         this.setBlockAndMetadata(world, 3, 1, var22, vanilla("field_150467_bQ"), 0);
      }

      this.placeChest(world, random, 3, 1, 8, this.getChest(), 5, this.getChestContents());
      this.placeChest(world, random, 3, 1, 9, this.getChest(), 5, this.getChestContents());
      this.setBlockAndMetadata(world, -1, 2, 2, vanilla("field_150478_aa"), 3);
      this.setBlockAndMetadata(world, 1, 2, 2, vanilla("field_150478_aa"), 3);
      this.setBlockAndMetadata(world, -3, 2, 6, vanilla("field_150478_aa"), 2);
      this.setBlockAndMetadata(world, 3, 2, 6, vanilla("field_150478_aa"), 1);
      this.setBlockAndMetadata(world, -1, 1, 8, this.rockSlabDoubleBlock, this.rockSlabDoubleMeta);
      this.setBlockAndMetadata(world, -1, 2, 8, this.rockSlabDoubleBlock, this.rockSlabDoubleMeta);
      this.setBlockAndMetadata(world, -3, 1, 9, vanilla("field_150353_l"), 0);
      this.setBlockAndMetadata(world, -2, 1, 9, vanilla("field_150353_l"), 0);
      this.setBlockAndMetadata(world, -3, 1, 10, vanilla("field_150353_l"), 0);
      this.setBlockAndMetadata(world, -2, 1, 10, vanilla("field_150353_l"), 0);
      this.setBlockAndMetadata(world, -3, 3, 8, this.brickStairBlock, 2);
      this.setBlockAndMetadata(world, -2, 3, 8, this.brickStairBlock, 2);
      this.setBlockAndMetadata(world, -1, 3, 8, this.brickStairBlock, 2);
      this.setBlockAndMetadata(world, -1, 3, 9, this.brickStairBlock, 0);
      this.setBlockAndMetadata(world, -1, 3, 10, this.brickStairBlock, 0);
      this.setBlockAndMetadata(world, -3, 1, 8, got("alloyForge"), 2);
      this.setBlockAndMetadata(world, -2, 1, 8, got("alloyForge"), 2);
      this.setBlockAndMetadata(world, -1, 1, 9, got("alloyForge"), 4);
      this.setBlockAndMetadata(world, -1, 1, 10, got("alloyForge"), 4);
      world.func_72921_c(-3, 1, 8, 2, 3);
      world.func_72921_c(-2, 1, 8, 2, 3);
      world.func_72921_c(-1, 1, 9, 5, 3);
      world.func_72921_c(-1, 1, 10, 5, 3);
      this.setBlockAndMetadata(world, -3, 2, 8, this.barsBlock, 0);
      this.setBlockAndMetadata(world, -2, 2, 8, this.barsBlock, 0);
      this.setBlockAndMetadata(world, -1, 2, 9, this.barsBlock, 0);
      this.setBlockAndMetadata(world, -1, 2, 10, this.barsBlock, 0);

      for (int var25 = -1; var25 <= 1; var25++) {
         for (int k13 = -1; k13 <= 1; k13++) {
            if (var25 != 0 || k13 != 0) {
               this.setBlockAndMetadata(world, -3 + var25, 4, 10 + k13, this.rockSlabDoubleBlock, this.rockSlabDoubleMeta);
               this.setBlockAndMetadata(world, -3 + var25, 5, 10 + k13, this.rockSlabDoubleBlock, this.rockSlabDoubleMeta);
               this.setBlockAndMetadata(world, -3 + var25, 6, 10 + k13, this.rockSlabBlock, this.rockSlabMeta);
            }
         }
      }

      this.setAir(world, -3, 5, 10);
      this.setAir(world, -3, 5, 10);
      if (this.isKingsLanding) {
         this.spawnNPCAndSetHome(new LegacyEntity(world), world, 0, 1, 6, 4);
         this.spawnNPCAndSetHome(new LegacyEntity(world), world, 0, 1, 6, 4);
      } else {
         this.spawnNPCAndSetHome(this.getBlacksmith(world), world, 0, 1, 6, 4);
      }

      return true;
   }

}
