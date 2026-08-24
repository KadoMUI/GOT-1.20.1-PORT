package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureWesterosTownGarden extends LegacyNorthernContext {

   protected GOTStructureWesterosTownGarden(NorthStructureBuilder builder) {
      super(builder, Style.NORTH);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureWesterosTownGarden piece = new GOTStructureWesterosTownGarden(builder);
      piece.marketVariant = Math.max(0, variant);
      piece.isAbandoned = variant == 1;
      piece.isTramp = variant == 1;
      piece.isHardhome = variant == 1;
      piece.isBlacksmith = variant == 1;
      piece.generate(piece, piece.legacyRandom(), 0, 0, 0, 0);
   }


   public boolean generate(LegacyNorthernContext world, Random random, int i, int j, int k, int rotation) {
      this.setOriginAndRotation(world, i, j, k, rotation, 0);
      this.setupRandomBlocks(random);
      if (this.restrictions) {
         for (int i1 = -3; i1 <= 3; i1++) {
            for (int k1 = 0; k1 <= 3; k1++) {
               int j1 = this.getTopBlock(world, i1, k1) - 1;
               if (!this.isSurface(world, i1, j1, k1)) {
                  return false;
               }
            }
         }
      }

      for (int i1 = -3; i1 <= 3; i1++) {
         for (int k1 = 0; k1 <= 3; k1++) {
            int i2 = Math.abs(i1);

            for (int j1 = 0; (j1 >= 0 || !this.isOpaque(world, i1, j1, k1)) && this.getY(j1) >= 0; j1--) {
               this.setBlockAndMetadata(world, i1, j1, k1, this.rockSlabDoubleBlock, this.rockSlabDoubleMeta);
               this.setGrassToDirt(world, i1, j1 - 1, k1);
            }

            for (int var15 = 1; var15 <= 3; var15++) {
               this.setAir(world, i1, var15, k1);
            }

            if (i2 <= 2 && k1 >= 1 && k1 <= 2) {
               this.setBlockAndMetadata(world, i1, 0, k1, vanilla("field_150349_c"), 0);
            }

            if (i2 == 3 && (k1 == 0 || k1 == 3)) {
               this.setBlockAndMetadata(world, i1, 1, k1, this.rockWallBlock, this.rockWallMeta);
               this.setBlockAndMetadata(world, i1, 2, k1, vanilla("field_150478_aa"), 5);
            }
         }
      }

      for (int k12 = 1; k12 <= 2; k12++) {
         LegacyBlock flower = this.getRandomFlower(world, random);

         for (int i12 = -2; i12 <= 2; i12++) {
            this.setBlockAndMetadata(world, i12, 1, k12, flower, 0);
         }
      }

      return true;
   }

}
