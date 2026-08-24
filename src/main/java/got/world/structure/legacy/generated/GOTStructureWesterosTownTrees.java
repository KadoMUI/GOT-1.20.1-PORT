package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureWesterosTownTrees extends LegacyNorthernContext {

   protected GOTStructureWesterosTownTrees(NorthStructureBuilder builder) {
      super(builder, Style.NORTH);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureWesterosTownTrees piece = new GOTStructureWesterosTownTrees(builder);
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
         for (int i1 = -6; i1 <= 6; i1++) {
            for (int k1 = -2; k1 <= 2; k1++) {
               int j12 = this.getTopBlock(world, i1, k1) - 1;
               if (!this.isSurface(world, i1, j12, k1)) {
                  return false;
               }
            }
         }
      }

      for (int i1 = -6; i1 <= 6; i1++) {
         for (int k1 = -2; k1 <= 2; k1++) {
            int i2 = Math.abs(i1);
            int k2 = Math.abs(k1);

            for (int j1 = 0; (j1 >= 0 || !this.isOpaque(world, i1, j1, k1)) && this.getY(j1) >= 0; j1--) {
               this.setBlockAndMetadata(world, i1, j1, k1, this.rockSlabDoubleBlock, this.rockSlabDoubleMeta);
               this.setGrassToDirt(world, i1, j1 - 1, k1);
            }

            for (int var13 = 1; var13 <= 10; var13++) {
               this.setAir(world, i1, var13, k1);
            }

            if (i2 % 4 != 2 && k2 <= 1) {
               this.setBlockAndMetadata(world, i1, 0, k1, vanilla("field_150349_c"), 0);
            }

            if (i2 % 4 == 2 && k2 == 2) {
               this.setBlockAndMetadata(world, i1, 1, k1, this.rockWallBlock, this.rockWallMeta);
               this.setBlockAndMetadata(world, i1, 2, k1, vanilla("field_150478_aa"), 5);
            }
         }
      }
      this.placeFarmTree(world, random);
return true;
   }

}
