package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureWesterosVillageFarm$Crops extends GOTStructureWesterosVillageFarm {

   protected GOTStructureWesterosVillageFarm$Crops(NorthStructureBuilder builder) {
      super(builder);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureWesterosVillageFarm$Crops piece = new GOTStructureWesterosVillageFarm$Crops(builder);
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

      for (int i1 = -4; i1 <= 4; i1++) {
         for (int k1 = -4; k1 <= 4; k1++) {
            int i2 = Math.abs(i1);
            int k2 = Math.abs(k1);
            if (i2 > 2 || k2 > 2) {
               this.setBlockAndMetadata(world, i1, 0, k1, got("dirtPath"), 0);
            } else if (i2 == 0 && k2 == 0) {
               this.setBlockAndMetadata(world, i1, 0, k1, vanilla("field_150355_j"), 0);
               this.setBlockAndMetadata(world, i1, 1, k1, this.rockBlock, this.rockMeta);
               this.setBlockAndMetadata(world, i1, 2, k1, vanilla("field_150407_cf"), 0);
               this.setBlockAndMetadata(world, i1, 3, k1, this.fenceBlock, this.fenceMeta);
               this.setBlockAndMetadata(world, i1, 4, k1, vanilla("field_150407_cf"), 0);
               this.setBlockAndMetadata(world, i1, 5, k1, vanilla("field_150423_aK"), 2);
            } else {
               this.setBlockAndMetadata(world, i1, 0, k1, vanilla("field_150458_ak"), 7);
               this.setBlockAndMetadata(world, i1, 1, k1, this.cropBlock, this.cropMeta);
            }
         }
      }

      this.setBlockAndMetadata(world, 0, 1, -5, this.fenceGateBlock, 0);
      this.setBlockAndMetadata(world, 0, 1, 5, this.fenceGateBlock, 2);
      this.setBlockAndMetadata(world, -5, 1, 0, this.fenceGateBlock, 1);
      this.setBlockAndMetadata(world, 5, 1, 0, this.fenceGateBlock, 3);
      int farmhands = 1 + random.nextInt(2);

      for (int l = 0; l < farmhands; l++) {
         LegacyEntity farmhand = this.getFarmhand(world);
         this.spawnNPCAndSetHome(farmhand, world, 0, 1, -1, 8);
         ((LegacyEntity)farmhand).setSeedsItem(this.seedItem);
      }

      return true;
   }

}
