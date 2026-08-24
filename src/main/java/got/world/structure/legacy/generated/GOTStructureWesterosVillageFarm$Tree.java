package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureWesterosVillageFarm$Tree extends GOTStructureWesterosVillageFarm {

   protected GOTStructureWesterosVillageFarm$Tree(NorthStructureBuilder builder) {
      super(builder);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureWesterosVillageFarm$Tree piece = new GOTStructureWesterosVillageFarm$Tree(builder);
      piece.marketVariant = Math.max(0, variant);
      piece.isAbandoned = variant == 1;
      piece.isTramp = variant == 1;
      piece.isHardhome = variant == 1;
      piece.isBlacksmith = variant == 1;
      piece.generate(piece, piece.legacyRandom(), 0, 0, 0, 0);
   }


   public boolean generate(LegacyNorthernContext world, Random random, int i, int j, int k, int rotation) {
      if (!super.generate(world, random, i, j, k, rotation)) {
         return false;
      }

      for (int i1 = -5; i1 <= 5; i1++) {
         for (int k1 = -5; k1 <= 5; k1++) {
            int i2 = Math.abs(i1);
            int k2 = Math.abs(k1);
            if (i2 == 5 && k2 == 5) {
               this.setBlockAndMetadata(world, i1, 2, k1, this.rockWallBlock, this.rockWallMeta);
               this.setBlockAndMetadata(world, i1, 3, k1, vanilla("field_150362_t"), 4);
            }
         }
      }
      this.placeFarmTree(world, random);
for (int var12 = -4; var12 <= 4; var12++) {
         for (int k1 = -4; k1 <= 4; k1++) {
            int j1 = 1;
            if (!this.isOpaque(world, var12, j1, k1) && random.nextInt(8) == 0) {
               this.plantFlower(world, random, var12, j1, k1);
            }
         }
      }

      return true;
   }

}
