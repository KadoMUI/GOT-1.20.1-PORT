package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureWesterosObelisk extends LegacyNorthernContext {

   protected GOTStructureWesterosObelisk(NorthStructureBuilder builder) {
      super(builder, Style.NORTH);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureWesterosObelisk piece = new GOTStructureWesterosObelisk(builder);
      piece.marketVariant = Math.max(0, variant);
      piece.isAbandoned = variant == 1;
      piece.isTramp = variant == 1;
      piece.isHardhome = variant == 1;
      piece.isBlacksmith = variant == 1;
      piece.generate(piece, piece.legacyRandom(), 0, 0, 0, 0);
   }


   public boolean generate(LegacyNorthernContext world, Random random, int i, int j, int k, int rotation) {
      this.setOriginAndRotation(world, i, j, k, rotation, 4);
      this.setupRandomBlocks(random);
      if (this.restrictions) {
         for (int i1 = -3; i1 <= 3; i1++) {
            for (int k12 = -3; k12 <= 3; k12++) {
               int j1 = this.getTopBlock(world, i1, k12) - 1;
               if (!this.isSurface(world, i1, j1, k12)) {
                  return false;
               }
            }
         }
      }

      for (int i1 = -3; i1 <= 3; i1++) {
         for (int k12 = -3; k12 <= 3; k12++) {
            for (int j1 = 3; (j1 >= 0 || !this.isOpaque(world, i1, j1, k12)) && this.getY(j1) >= 0; j1--) {
               this.placeRandomBrick(world, random, i1, j1, k12);
               this.setGrassToDirt(world, i1, j1 - 1, k12);
            }
         }
      }

      for (int var20 = -2; var20 <= 2; var20++) {
         for (int k12 = -2; k12 <= 2; k12++) {
            for (int j1 = 4; j1 <= 8; j1++) {
               this.setBlockAndMetadata(world, var20, j1, k12, this.rockBlock, this.rockMeta);
            }
         }
      }

      for (int var21 = -3; var21 <= 3; var21++) {
         this.placeRandomStairs(world, random, var21, 4, -3, 2);
         this.placeRandomStairs(world, random, var21, 4, 3, 3);
      }

      for (int k1 = -2; k1 <= 2; k1++) {
         this.placeRandomStairs(world, random, -3, 4, k1, 1);
         this.placeRandomStairs(world, random, 3, 4, k1, 0);
      }

      for (int var22 = -1; var22 <= 1; var22++) {
         for (int k12 = -1; k12 <= 1; k12++) {
            for (int j1 = 9; j1 <= 14; j1++) {
               this.placeRandomBrick(world, random, var22, j1, k12);
            }
         }
      }

      for (int var23 = -2; var23 <= 2; var23++) {
         this.placeRandomStairs(world, random, var23, 9, -2, 2);
         this.placeRandomStairs(world, random, var23, 9, 2, 3);
      }

      for (int var15 = -1; var15 <= 1; var15++) {
         this.placeRandomStairs(world, random, -2, 9, var15, 1);
         this.placeRandomStairs(world, random, 2, 9, var15, 0);
      }

      for (int j12 = 15; j12 <= 18; j12++) {
         this.placeRandomBrick(world, random, 0, j12, 0);
         this.placeRandomBrick(world, random, -1, j12, 0);
         this.placeRandomBrick(world, random, 1, j12, 0);
         this.placeRandomBrick(world, random, 0, j12, -1);
         this.placeRandomBrick(world, random, 0, j12, 1);
      }

      this.placeRandomStairs(world, random, -1, 19, 0, 1);
      this.placeRandomStairs(world, random, 1, 19, 0, 0);
      this.placeRandomStairs(world, random, 0, 19, -1, 2);
      this.placeRandomStairs(world, random, 0, 19, 1, 3);
      this.placeRandomBrick(world, random, 0, 19, 0);
      this.setBlockAndMetadata(world, 0, 20, 0, got("beacon"), 0);
      return true;
   }

   private void placeRandomBrick(LegacyNorthernContext world, Random random, int i, int j, int k) {
      if (random.nextInt(4) == 0) {
         if (random.nextBoolean()) {
            this.setBlockAndMetadata(world, i, j, k, this.brickMossyBlock, this.brickMossyMeta);
         } else {
            this.setBlockAndMetadata(world, i, j, k, this.brickCrackedBlock, this.brickCrackedMeta);
         }
      } else {
         this.setBlockAndMetadata(world, i, j, k, this.brickBlock, this.brickMeta);
      }
   }

   private void placeRandomStairs(LegacyNorthernContext world, Random random, int i, int j, int k, int meta) {
      if (random.nextInt(4) == 0) {
         if (random.nextBoolean()) {
            this.setBlockAndMetadata(world, i, j, k, this.brickMossyStairBlock, meta);
         } else {
            this.setBlockAndMetadata(world, i, j, k, this.brickCrackedStairBlock, meta);
         }
      } else {
         this.setBlockAndMetadata(world, i, j, k, this.brickStairBlock, meta);
      }
   }

}
