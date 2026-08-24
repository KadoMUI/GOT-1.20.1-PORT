package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureWesterosVillageFarm$Animals extends GOTStructureWesterosVillageFarm {

   protected GOTStructureWesterosVillageFarm$Animals(NorthStructureBuilder builder) {
      super(builder);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureWesterosVillageFarm$Animals piece = new GOTStructureWesterosVillageFarm$Animals(builder);
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

      for (int i1 = -1; i1 <= 1; i1++) {
         this.setBlockAndMetadata(world, i1, 1, -5, this.fenceGateBlock, 0);
         this.setBlockAndMetadata(world, i1, 1, 5, this.fenceGateBlock, 2);
      }

      for (int k1 = -1; k1 <= 1; k1++) {
         this.setBlockAndMetadata(world, -5, 1, k1, this.fenceGateBlock, 1);
         this.setBlockAndMetadata(world, 5, 1, k1, this.fenceGateBlock, 3);
      }

      for (int var13 = -1; var13 <= 1; var13++) {
         for (int k1 = -1; k1 <= 1; k1++) {
            if (random.nextInt(3) == 0) {
               int j1 = 1;
               int j2 = 1;
               if (var13 == 0 && k1 == 0 && random.nextBoolean()) {
                  j2++;
               }

               for (int j3 = j1; j3 <= j2; j3++) {
                  this.setBlockAndMetadata(world, var13, j3, k1, vanilla("field_150407_cf"), 0);
               }
            }
         }
      }

      int animals = 4 + random.nextInt(5);

      for (int l = 0; l < animals; l++) {
         LegacyEntity animal = GOTStructureWesterosBarn.getRandomAnimal(world, random);
         int i12 = 3 * (random.nextBoolean() ? 1 : -1);
         int k1 = 3 * (random.nextBoolean() ? 1 : -1);
         this.spawnNPCAndSetHome(animal, world, i12, 1, k1, 0);
         animal.func_110177_bN();
      }

      return true;
   }

}
