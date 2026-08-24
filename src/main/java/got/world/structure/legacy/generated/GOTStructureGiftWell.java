package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureGiftWell extends LegacyNorthernContext {

   protected GOTStructureGiftWell(NorthStructureBuilder builder) {
      super(builder, Style.GIFT);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureGiftWell piece = new GOTStructureGiftWell(builder);
      piece.marketVariant = Math.max(0, variant);
      piece.isAbandoned = variant == 1;
      piece.isTramp = variant == 1;
      piece.isHardhome = variant == 1;
      piece.isBlacksmith = variant == 1;
      piece.generate(piece, piece.legacyRandom(), 0, 0, 0, 0);
   }


   public boolean generate(LegacyNorthernContext world, Random random, int i, int j, int k, int rotation) {
      this.setOriginAndRotation(world, i, j, k, rotation, 2);
      this.setupRandomBlocks(random);
      if (this.restrictions) {
         for (int i1 = -2; i1 <= 2; i1++) {
            for (int k1 = -2; k1 <= 2; k1++) {
               int j12 = this.getTopBlock(world, i1, k1) - 1;
               if (!this.isSurface(world, i1, j12, k1)) {
                  return false;
               }
            }
         }
      }

      for (int i1 = -2; i1 <= 2; i1++) {
         for (int k1 = -2; k1 <= 2; k1++) {
            int i2 = Math.abs(i1);
            int k2 = Math.abs(k1);

            for (int j1 = 0; (j1 >= 0 || !this.isOpaque(world, i1, j1, k1)) && this.getY(j1) >= 0; j1--) {
               this.setBlockAndMetadata(world, i1, j1, k1, got("dirtPath"), 0);
               this.setGrassToDirt(world, i1, j1 - 1, k1);
            }

            for (int var19 = 1; var19 <= 5; var19++) {
               this.setAir(world, i1, var19, k1);
            }

            if (i2 == 1 && k2 == 1) {
               this.setBlockAndMetadata(world, i1, 1, k1, this.logBlock, this.logMeta);

               for (int var20 = 2; var20 <= 3; var20++) {
                  this.setBlockAndMetadata(world, i1, var20, k1, this.fenceBlock, this.fenceMeta);
               }

               this.setBlockAndMetadata(world, i1, 4, k1, this.plankSlabBlock, this.plankSlabMeta);
            }

            if (i2 == 0 && k2 == 1 || k2 == 0 && i2 == 1) {
               this.setBlockAndMetadata(world, i1, 4, k1, this.plankSlabBlock, this.plankSlabMeta | 8);
            }

            if (i2 == 0 && k2 == 0) {
               this.setBlockAndMetadata(world, i1, 5, k1, this.plankSlabBlock, this.plankSlabMeta);

               for (int var21 = 3; var21 <= 4; var21++) {
                  this.setBlockAndMetadata(world, i1, var21, k1, this.fenceBlock, this.fenceMeta);
               }
            }
         }
      }

      this.setBlockAndMetadata(world, 0, 0, 0, got("gateWoodenBars"), 0);
      int depth = random.nextInt(2);
      int waterDepth = 2 + random.nextInt(4);
      int wellTop = -1;

      int wellBottom;
      for (int j1 = wellBottom = wellTop - depth - waterDepth - 1; j1 <= wellTop; j1++) {
         for (int i12 = -1; i12 <= 1; i12++) {
            for (int k12 = -1; k12 <= 1; k12++) {
               int i2 = Math.abs(i12);
               int k2 = Math.abs(k12);
               if (j1 == wellBottom) {
                  this.setBlockAndMetadata(world, i12, j1, k12, got("dirtPath"), 0);
               } else if (i2 != 0 || k2 != 0) {
                  this.setBlockAndMetadata(world, i12, j1, k12, got("dirtPath"), 0);
               } else if (j1 <= wellBottom + waterDepth) {
                  this.setBlockAndMetadata(world, i12, j1, k12, vanilla("field_150355_j"), 0);
               } else {
                  this.setAir(world, i12, j1, k12);
               }
            }
         }
      }

      for (int var23 = wellBottom + waterDepth + 1; var23 <= wellTop; var23++) {
         this.setBlockAndMetadata(world, 0, var23, 0, vanilla("field_150468_ap"), 2);
      }

      return true;
   }

}
