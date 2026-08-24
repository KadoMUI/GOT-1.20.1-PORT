package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureGiftVillageLight extends LegacyNorthernContext {

   protected GOTStructureGiftVillageLight(NorthStructureBuilder builder) {
      super(builder, Style.GIFT);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureGiftVillageLight piece = new GOTStructureGiftVillageLight(builder);
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

      for (int j1 = 0; (j1 >= 0 || !this.isOpaque(world, 0, j1, 0)) && this.getY(j1) >= 0; j1--) {
         this.setBlockAndMetadata(world, 0, j1, 0, this.logBlock, this.logMeta);
         this.setGrassToDirt(world, 0, j1 - 1, 0);
      }

      for (int var10 = 1; var10 <= 2; var10++) {
         this.setBlockAndMetadata(world, 0, var10, 0, this.logBlock, this.logMeta);
      }

      this.setBlockAndMetadata(world, 0, 3, 0, this.plankBlock, this.plankMeta);
      this.setBlockAndMetadata(world, 0, 4, 0, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 0, 5, 0, vanilla("field_150478_aa"), 5);
      return true;
   }

}
