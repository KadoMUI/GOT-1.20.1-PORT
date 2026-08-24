package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureWesterosWell extends LegacyNorthernContext {

   protected GOTStructureWesterosWell(NorthStructureBuilder builder) {
      super(builder, Style.NORTH);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureWesterosWell piece = new GOTStructureWesterosWell(builder);
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

         for (int i1 = -3; i1 <= 3; i1++) {
            for (int k1 = -3; k1 <= 3; k1++) {
               int j12 = this.getTopBlock(world, i1, k1) - 1;
               if (!this.isSurface(world, i1, j12, k1)) {
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

      for (int i1 = -3; i1 <= 3; i1++) {
         for (int k1 = -3; k1 <= 3; k1++) {
            int i2 = Math.abs(i1);
            int k2 = Math.abs(k1);

            for (int j12 = 0; (j12 >= 0 || !this.isOpaque(world, i1, j12, k1)) && this.getY(j12) >= 0; j12--) {
               this.setBlockAndMetadata(world, i1, j12, k1, this.rockBlock, this.rockMeta);
               this.setGrassToDirt(world, i1, j12 - 1, k1);
            }

            for (int var17 = 1; var17 <= 6; var17++) {
               this.setAir(world, i1, var17, k1);
            }

            if (i2 == 2 && k2 == 2) {
               this.setBlockAndMetadata(world, i1, 1, k1, this.rockBlock, this.rockMeta);
               this.setBlockAndMetadata(world, i1, 2, k1, this.rockBlock, this.rockMeta);
               this.setBlockAndMetadata(world, i1, 3, k1, this.rockWallBlock, this.rockWallMeta);
               this.setBlockAndMetadata(world, i1, 4, k1, this.rockBlock, this.rockMeta);
               this.setBlockAndMetadata(world, i1, 5, k1, this.rockSlabBlock, this.rockSlabMeta);
            }

            if (i2 <= 2 && k2 <= 2) {
               int d = i2 + k2;
               if (d == 3) {
                  this.setBlockAndMetadata(world, i1, 4, k1, this.rockSlabBlock, this.rockSlabMeta | 8);
                  this.setBlockAndMetadata(world, i1, 5, k1, this.rockSlabDoubleBlock, this.rockSlabDoubleMeta);
               }

               if (d == 2) {
                  this.setBlockAndMetadata(world, i1, 5, k1, this.rockSlabDoubleBlock, this.rockSlabDoubleMeta);
                  this.setBlockAndMetadata(world, i1, 6, k1, this.rockSlabBlock, this.rockSlabMeta);
               }

               if (d == 1) {
                  this.setBlockAndMetadata(world, i1, 5, k1, this.rockSlabBlock, this.rockSlabMeta | 8);
                  this.setBlockAndMetadata(world, i1, 6, k1, this.rockSlabDoubleBlock, this.rockSlabDoubleMeta);
               }

               if (d == 0) {
                  this.setBlockAndMetadata(world, i1, 6, k1, this.rockSlabDoubleBlock, this.rockSlabDoubleMeta);
                  this.setBlockAndMetadata(world, i1, 7, k1, this.rockSlabBlock, this.rockSlabMeta);
               }
            }

            if (i2 == 2 && k2 <= 1 || k2 == 2 && i2 <= 1) {
               this.setBlockAndMetadata(world, i1, 1, k1, this.fenceBlock, this.fenceMeta);
               this.setBlockAndMetadata(world, i1, 0, k1, this.rockSlabDoubleBlock, this.rockSlabDoubleMeta);
            }
         }
      }

      int waterDepth = 1 + random.nextInt(4);
      int depth = waterDepth + 1 + random.nextInt(3);

      for (int j1 = 0; j1 < depth; j1++) {
         int j2 = -j1;
         boolean watery = j1 >= depth - waterDepth;

         for (int i1 = -1; i1 <= 1; i1++) {
            for (int k1 = -1; k1 <= 1; k1++) {
               if (watery) {
                  this.setBlockAndMetadata(world, i1, j2, k1, vanilla("field_150355_j"), 0);
               } else {
                  this.setAir(world, i1, j2, k1);
               }
            }
         }

         if (!watery) {
            this.setBlockAndMetadata(world, 0, j2, -1, vanilla("field_150468_ap"), 3);
            this.setBlockAndMetadata(world, 0, j2, 1, vanilla("field_150468_ap"), 2);
            this.setBlockAndMetadata(world, -1, j2, 0, vanilla("field_150468_ap"), 4);
            this.setBlockAndMetadata(world, 1, j2, 0, vanilla("field_150468_ap"), 5);
         }
      }

      this.setBlockAndMetadata(world, 0, 1, -2, this.fenceGateBlock, 0);
      this.setBlockAndMetadata(world, 0, 1, 2, this.fenceGateBlock, 2);
      this.setBlockAndMetadata(world, -2, 1, 0, this.fenceGateBlock, 1);
      this.setBlockAndMetadata(world, 2, 1, 0, this.fenceGateBlock, 3);

      for (int var15 = 4; var15 <= 5; var15++) {
         this.setBlockAndMetadata(world, 0, var15, 0, this.fenceBlock, this.fenceMeta);
      }

      this.setBlockAndMetadata(world, -3, 5, 0, vanilla("field_150478_aa"), 1);
      this.setBlockAndMetadata(world, 3, 5, 0, vanilla("field_150478_aa"), 2);
      this.setBlockAndMetadata(world, 0, 5, -3, vanilla("field_150478_aa"), 4);
      this.setBlockAndMetadata(world, 0, 5, 3, vanilla("field_150478_aa"), 3);
      return true;
   }

}
