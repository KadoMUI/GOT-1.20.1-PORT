package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureWesterosMarketStall extends LegacyNorthernContext {

   protected GOTStructureWesterosMarketStall(NorthStructureBuilder builder) {
      super(builder, Style.NORTH);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureWesterosMarketStall piece = new GOTStructureWesterosMarketStall(builder);
      piece.marketVariant = Math.max(0, variant);
      piece.isAbandoned = variant == 1;
      piece.isTramp = variant == 1;
      piece.isHardhome = variant == 1;
      piece.isBlacksmith = variant == 1;
      piece.generate(piece, piece.legacyRandom(), 0, 0, 0, 0);
   }


   public boolean generate(LegacyNorthernContext world, Random random, int i, int j, int k, int rotation) {
      this.setOriginAndRotation(world, i, j, k, rotation, 3);
      this.setupRandomBlocks(random);
      if (this.restrictions) {
         int minHeight = 0;
         int maxHeight = 0;

         for (int i1 = -2; i1 <= 2; i1++) {
            for (int k1 = -2; k1 <= 2; k1++) {
               int j1 = this.getTopBlock(world, i1, k1) - 1;
               if (!this.isSurface(world, i1, j1, k1)) {
                  return false;
               }

               if (j1 < minHeight) {
                  minHeight = j1;
               }

               if (j1 > maxHeight) {
                  maxHeight = j1;
               }

               if (maxHeight - minHeight > 5) {
                  return false;
               }
            }
         }
      }

      for (int i1 = -2; i1 <= 2; i1++) {
         for (int k1 = -2; k1 <= 2; k1++) {
            int i2 = Math.abs(i1);
            int k2 = Math.abs(k1);

            for (int j1 = 0; (j1 >= 0 || !this.isOpaque(world, i1, j1, k1)) && this.getY(j1) >= 0; j1--) {
               this.setBlockAndMetadata(world, i1, j1, k1, this.brickBlock, this.brickMeta);
               this.setGrassToDirt(world, i1, j1 - 1, k1);
            }

            for (int var13 = 1; var13 <= 4; var13++) {
               this.setAir(world, i1, var13, k1);
            }

            if (i2 == 2 && k2 == 2) {
               for (int var14 = 1; var14 <= 3; var14++) {
                  this.setBlockAndMetadata(world, i1, var14, k1, this.fenceBlock, this.fenceMeta);
               }
            } else if (i2 == 2 || k2 == 2) {
               this.setBlockAndMetadata(world, i1, 1, k1, this.plankBlock, this.plankMeta);
               this.setBlockAndMetadata(world, i1, 3, k1, this.fenceBlock, this.fenceMeta);
            }

            this.generateRoof(world, i1, 4, k1);
         }
      }

      this.setBlockAndMetadata(world, -2, 1, 0, this.fenceGateBlock, 1);
      this.setBlockAndMetadata(world, -1, 1, -1, this.tableBlock, 0);
      this.setBlockAndMetadata(world, 1, 1, -1, this.getChest(), 3);
      LegacyEntity trader = this.createTrader(world);
      this.spawnNPCAndSetHome(trader, world, 0, 1, 0, 4);
      return true;
   }

}
