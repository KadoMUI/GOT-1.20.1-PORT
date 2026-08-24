package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureGiftGate extends LegacyNorthernContext {

   protected GOTStructureGiftGate(NorthStructureBuilder builder) {
      super(builder, Style.GIFT);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureGiftGate piece = new GOTStructureGiftGate(builder);
      piece.marketVariant = Math.max(0, variant);
      piece.isAbandoned = variant == 1;
      piece.isTramp = variant == 1;
      piece.isHardhome = variant == 1;
      piece.isBlacksmith = variant == 1;
      piece.generate(piece, piece.legacyRandom(), 0, 0, 0, 0);
   }


   public boolean generate(LegacyNorthernContext world, Random random, int i, int j, int k, int rotation) {
      this.setOriginAndRotation(world, i, j, k, rotation, 0, 0);
      this.setupRandomBlocks(random);
      this.originY++;
      this.originZ -= 7;

      for (int x = -6; x <= 6; x++) {
         for (int y = -6; y <= 6; y++) {
            for (int z = 0; z <= 3; z++) {
               this.setBlockAndMetadata(world, x, z, y, got("brickIce"), 0);
            }
         }
      }

      for (int x = -5; x <= 5; x++) {
         for (int y = -6; y <= 6; y++) {
            for (int z = 4; z <= 7; z++) {
               this.setBlockAndMetadata(world, x, z, y, got("brickIce"), 0);
            }
         }
      }

      for (int x = -4; x <= 4; x++) {
         for (int y = -6; y <= 6; y++) {
            for (int z = 8; z <= 9; z++) {
               this.setBlockAndMetadata(world, x, z, y, got("brickIce"), 0);
            }
         }
      }

      for (int x = -3; x <= 3; x++) {
         for (int y = -6; y <= 6; y++) {
            this.setBlockAndMetadata(world, x, 10, y, got("brickIce"), 0);
         }
      }

      for (int x = -2; x <= 2; x++) {
         for (int y = -6; y <= 6; y++) {
            this.setBlockAndMetadata(world, x, 11, y, got("brickIce"), 0);
         }
      }

      for (int x = -3; x <= 3; x++) {
         for (int y = -6; y <= 6; y++) {
            for (int z = 0; z <= 6; z++) {
               this.setAir(world, x, z, y);
            }
         }
      }

      for (int x = -3; x <= 3; x++) {
         for (int z = 0; z <= 6; z++) {
            this.setBlockAndMetadata(world, x, z, 4, got("gateIronBars"), 2);
            this.setBlockAndMetadata(world, x, z, -4, got("gateWooden"), 2);
         }
      }

      int wallTop = this.getTopBlock(world, 0, 0) - 1;

      for (int z = 0; z <= 6; z++) {
         for (int y = -6; y <= 6; y++) {
            if (y > -4 && y < 4) {
               this.setBlockAndMetadata(world, -4, z, y, got("cobblebrick"), 0);
               this.setBlockAndMetadata(world, 4, z, y, got("cobblebrick"), 0);
            } else {
               this.setBlockAndMetadata(world, -4, z, y, this.woodBeamBlock, this.woodBeamMeta);
               this.setBlockAndMetadata(world, 4, z, y, this.woodBeamBlock, this.woodBeamMeta);
            }
         }
      }

      for (int x = -3; x <= 3; x++) {
         for (int y = -6; y <= 6; y++) {
            if (y > -4 && y < 4) {
               this.setBlockAndMetadata(world, x, 7, y, this.plankBlock, this.plankMeta);
            } else {
               this.setBlockAndMetadata(world, x, 7, y, this.woodBeamBlock, this.woodBeamMeta | 4);
               if (y >= 4) {
                  this.setBlockAndMetadata(world, x, wallTop, y, this.woodBeamBlock, this.woodBeamMeta | 4);
               }
            }

            LegacyBlock block = vanilla("field_150351_n");
            int meta = 0;
            float f = random.nextFloat();
            if (f < 0.5F) {
               block = got("dirtPath");
            } else if (f < 0.8F) {
               block = vanilla("field_150346_d");
               meta = 1;
            }

            this.setBlockAndMetadata(world, x, -1, y, block, meta);
         }
      }

      if (!this.isAbandoned) {
         this.setBlockAndMetadata(world, 3, 3, 1, vanilla("field_150478_aa"), 1);
         this.setBlockAndMetadata(world, 3, 3, -1, vanilla("field_150478_aa"), 1);
         this.setBlockAndMetadata(world, -3, 3, 1, vanilla("field_150478_aa"), 2);
         this.setBlockAndMetadata(world, -3, 3, -1, vanilla("field_150478_aa"), 2);
      }

      for (int z = 0; z <= wallTop; z++) {
         this.setBlockAndMetadata(world, -4, z, 7, got("rope"), 3);
         this.setBlockAndMetadata(world, 4, z, 7, got("rope"), 3);
         this.setBlockAndMetadata(world, -4, z, 6, this.woodBeamBlock, this.woodBeamMeta);
         this.setBlockAndMetadata(world, 4, z, 6, this.woodBeamBlock, this.woodBeamMeta);
      }

      for (int x = -3; x <= 3; x++) {
         for (int z = 8; z <= wallTop - 1; z++) {
            this.setBlockAndMetadata(world, x, z, 6, this.fenceBlock, this.fenceMeta);
         }

         this.setBlockAndMetadata(world, x, wallTop, 6, this.woodBeamBlock, this.woodBeamMeta | 4);
      }

      for (int y = 0; y <= 6; y++) {
         this.setBlockAndMetadata(world, -4, wallTop, y, this.woodBeamBlock, this.woodBeamMeta | 8);
         this.setBlockAndMetadata(world, 4, wallTop, y, this.woodBeamBlock, this.woodBeamMeta | 8);
      }

      return true;
   }

   public LegacyNorthernContext setIsAbandoned() {
      this.isAbandoned = true;
      return this;
   }

}
