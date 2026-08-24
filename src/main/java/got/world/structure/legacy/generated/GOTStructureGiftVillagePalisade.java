package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureGiftVillagePalisade extends LegacyNorthernContext {

   protected GOTStructureGiftVillagePalisade(NorthStructureBuilder builder) {
      super(builder, Style.GIFT);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureGiftVillagePalisade piece = new GOTStructureGiftVillagePalisade(builder);
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
         int i1 = 0;
         int k1 = 0;
         if (!this.isSurface(world, 0, this.getTopBlock(world, i1, 0) - 1, k1)) {
            return false;
         }
      }

      for (int j12 = 1; (j12 >= 0 || !this.isOpaque(world, 0, j12, 0)) && this.getY(j12) >= 0; j12--) {
         this.setBlockAndMetadata(world, 0, j12, 0, this.cobbleBlock, this.cobbleMeta);
         this.setGrassToDirt(world, 0, j12 - 1, 0);
      }

      int height = 5 + random.nextInt(2);

      for (int j13 = 2; j13 <= height; j13++) {
         this.setBlockAndMetadata(world, 0, j13, 0, this.logBlock, this.logMeta);
      }

      return true;
   }

}
