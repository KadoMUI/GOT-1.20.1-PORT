package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureWesterosTownBench extends LegacyNorthernContext {

   protected GOTStructureWesterosTownBench(NorthStructureBuilder builder) {
      super(builder, Style.NORTH);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureWesterosTownBench piece = new GOTStructureWesterosTownBench(builder);
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
         for (int i12 = -2; i12 <= 2; i12++) {
            for (int k1 = 0; k1 <= 2; k1++) {
               int j1 = this.getTopBlock(world, i12, k1) - 1;
               if (!this.isSurface(world, i12, j1, k1)) {
                  return false;
               }
            }
         }
      }

      int k1 = 0;
      int j1 = this.getTopBlock(world, 0, k1);
      this.setBlockAndMetadata(world, 0, j1, k1, this.rockSlabBlock, this.rockSlabMeta);
      this.setBlockAndMetadata(world, -1, j1, k1, this.rockStairBlock, 0);
      this.setBlockAndMetadata(world, 1, j1, k1, this.rockStairBlock, 1);

      for (int i1 = -1; i1 <= 1; i1++) {
         this.setGrassToDirt(world, i1, j1 - 1, k1);
         this.layFoundation(world, i1, j1 - 1, k1);
      }

      int var16 = 2;
      j1 = this.getTopBlock(world, 0, var16);

      for (int var14 = -1; var14 <= 1; var14++) {
         this.setBlockAndMetadata(world, var14, j1, var16, this.rockSlabBlock, this.rockSlabMeta | 8);
      }

      for (int i13 : new int[]{-2, 2}) {
         this.setBlockAndMetadata(world, i13, j1, var16, this.rockSlabDoubleBlock, this.rockSlabDoubleMeta);
         this.setGrassToDirt(world, i13, j1 - 1, var16);
         this.layFoundation(world, i13, j1 - 1, var16);
      }

      return true;
   }

   private void layFoundation(LegacyNorthernContext world, int i, int j, int k) {
      for (int j1 = j; (j1 >= j || !this.isOpaque(world, i, j1, k)) && this.getY(j1) >= 0; j1--) {
         this.setBlockAndMetadata(world, i, j1, k, this.rockSlabDoubleBlock, this.rockSlabDoubleMeta);
         this.setGrassToDirt(world, i, j1 - 1, k);
      }
   }

}
