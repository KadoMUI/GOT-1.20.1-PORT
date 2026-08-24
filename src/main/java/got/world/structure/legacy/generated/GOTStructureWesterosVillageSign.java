package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureWesterosVillageSign extends LegacyNorthernContext {

   protected GOTStructureWesterosVillageSign(NorthStructureBuilder builder) {
      super(builder, Style.NORTH);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureWesterosVillageSign piece = new GOTStructureWesterosVillageSign(builder);
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

      for (int j12 = 0; (j12 >= 0 || !this.isOpaque(world, 0, j12, 0)) && this.getY(j12) >= 0; j12--) {
         this.setBlockAndMetadata(world, 0, j12, 0, this.woodBeamBlock, this.woodBeamMeta);
         this.setGrassToDirt(world, 0, j12 - 1, 0);
      }

      this.setBlockAndMetadata(world, 0, 1, 0, this.woodBeamBlock, this.woodBeamMeta);
      this.setBlockAndMetadata(world, 0, 2, 0, this.plankBlock, this.plankMeta);
      this.setBlockAndMetadata(world, 0, 3, 0, this.woodBeamBlock, this.woodBeamMeta);
      this.setBlockAndMetadata(world, 0, 4, 0, this.plankSlabBlock, this.plankSlabMeta);
      this.setBlockAndMetadata(world, -1, 3, 0, vanilla("field_150478_aa"), 1);
      this.setBlockAndMetadata(world, 1, 3, 0, vanilla("field_150478_aa"), 2);
      this.setBlockAndMetadata(world, 0, 3, -1, vanilla("field_150478_aa"), 4);
      this.setBlockAndMetadata(world, 0, 3, 1, vanilla("field_150478_aa"), 3);
      return true;
   }

}
