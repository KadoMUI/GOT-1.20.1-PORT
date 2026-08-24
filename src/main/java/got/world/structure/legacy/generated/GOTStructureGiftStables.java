package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureGiftStables extends LegacyNorthernContext {

   protected GOTStructureGiftStables(NorthStructureBuilder builder) {
      super(builder, Style.GIFT);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureGiftStables piece = new GOTStructureGiftStables(builder);
      piece.marketVariant = Math.max(0, variant);
      piece.isAbandoned = variant == 1;
      piece.isTramp = variant == 1;
      piece.isHardhome = variant == 1;
      piece.isBlacksmith = variant == 1;
      piece.generate(piece, piece.legacyRandom(), 0, 0, 0, 0);
   }


   public boolean generate(LegacyNorthernContext world, Random random, int i, int j, int k, int rotation) {
      this.setOriginAndRotation(world, i, j, k, rotation, 1, -2);
      this.setupRandomBlocks(random);
      if (this.restrictions) {
         int minHeight = 0;
         int maxHeight = 0;

         for (int i14 = -5; i14 <= 5; i14++) {
            for (int k13 = -1; k13 <= 10; k13++) {
               int j12 = this.getTopBlock(world, i14, k13) - 1;
               if (!this.isSurface(world, i14, j12, k13)) {
                  return false;
               }

               if (j12 < minHeight) {
                  minHeight = j12;
               }

               if (j12 > maxHeight) {
                  maxHeight = j12;
               }

               if (maxHeight - minHeight > 5) {
                  return false;
               }
            }
         }
      }

      for (int i15 = -4; i15 <= 4; i15++) {
         for (int k1 = 0; k1 <= 9; k1++) {
            int i2 = Math.abs(i15);

            for (int j1 = 0; (j1 >= 0 || !this.isOpaque(world, i15, j1, k1)) && this.getY(j1) >= 0; j1--) {
               this.setBlockAndMetadata(world, i15, j1, k1, this.brickBlock, this.brickMeta);
               this.setGrassToDirt(world, i15, j1 - 1, k1);
            }

            for (int var31 = 1; var31 <= 8; var31++) {
               this.setAir(world, i15, var31, k1);
            }

            if (k1 <= 4 && (k1 == 0 || k1 == 4 || i2 == 4)) {
               boolean beam = (k1 == 0 || k1 == 4) && (i2 == 0 || i2 == 4);
               if (beam) {
                  for (int j12 = 1; j12 <= 3; j12++) {
                     this.setBlockAndMetadata(world, i15, j12, k1, this.woodBeamBlock, this.woodBeamMeta);
                  }
               } else if (k1 == 4) {
                  for (int j12 = 1; j12 <= 3; j12++) {
                     this.setBlockAndMetadata(world, i15, j12, 4, this.plankBlock, this.plankMeta);
                  }
               } else {
                  for (int j12 = 1; j12 <= 3; j12++) {
                     this.setBlockAndMetadata(world, i15, j12, k1, this.wallBlock, this.wallMeta);
                  }
               }
            }
         }
      }

      for (int k13 : new int[]{0, 4}) {
         for (int i16 = -3; i16 <= 3; i16++) {
            if (i16 != 0) {
               this.setBlockAndMetadata(world, i16, 3, k13, this.woodBeamBlock, this.woodBeamMeta | 4);
            }
         }
      }

      for (int i1321 : new int[]{-4, 0, 4}) {
         for (int k12 = 0; k12 <= 3; k12++) {
            this.setBlockAndMetadata(world, i1321, 4, k12, this.woodBeamBlock, this.woodBeamMeta | 8);
         }

         for (int j12 = 4; j12 <= 6; j12++) {
            this.setBlockAndMetadata(world, i1321, j12, 4, this.woodBeamBlock, this.woodBeamMeta);
         }
      }

      for (int i17 = -4; i17 <= 4; i17++) {
         this.setBlockAndMetadata(world, i17, 7, 4, this.woodBeamBlock, this.woodBeamMeta | 4);
      }

      for (int i132 : new int[]{-4, 4}) {
         this.setBlockAndMetadata(world, i132, 5, 2, this.wallBlock, this.wallMeta);
         this.setBlockAndMetadata(world, i132, 5, 3, this.wallBlock, this.wallMeta);
         this.setBlockAndMetadata(world, i132, 6, 3, this.wallBlock, this.wallMeta);
      }

      this.setBlockAndMetadata(world, -2, 1, 0, this.doorBlock, 1);
      this.setBlockAndMetadata(world, -2, 2, 0, this.doorBlock, 8);
      this.setBlockAndMetadata(world, 2, 2, 0, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, -4, 2, 2, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 4, 2, 2, this.fenceBlock, this.fenceMeta);

      for (int i18 = -3; i18 <= 3; i18++) {
         for (int var27 = 1; var27 <= 3; var27++) {
            this.setBlockAndMetadata(world, i18, 0, var27, this.plankBlock, this.plankMeta);
         }

         if (i18 != 0) {
            for (int var28 = 1; var28 <= 3; var28++) {
               this.setBlockAndMetadata(world, i18, 4, var28, this.plankSlabBlock, this.plankSlabMeta | 8);
            }

            this.setBlockAndMetadata(world, i18, 4, 4, this.plankBlock, this.plankMeta);
         }
      }

      for (int i1321 : new int[]{-4, 0, 4}) {
         for (int j12 = 1; j12 <= 3; j12++) {
            this.setBlockAndMetadata(world, i1321, j12, 9, this.woodBeamBlock, this.woodBeamMeta);
         }

         for (int k12 = 5; k12 <= 9; k12++) {
            this.setBlockAndMetadata(world, i1321, 4, k12, this.woodBeamBlock, this.woodBeamMeta | 8);
         }

         for (int var39 = 5; var39 <= 8; var39++) {
            this.setBlockAndMetadata(world, i1321, 1, var39, this.fenceBlock, this.fenceMeta);
            this.setBlockAndMetadata(world, i1321, 3, var39, this.fenceBlock, this.fenceMeta);
         }

         this.setBlockAndMetadata(world, i1321, 2, 5, this.fenceBlock, this.fenceMeta);
         this.setBlockAndMetadata(world, i1321, 2, 8, this.fenceBlock, this.fenceMeta);
      }

      for (int i132 : new int[]{-4, 4}) {
         this.setBlockAndMetadata(world, i132, 5, 5, this.wallBlock, this.wallMeta);
         this.setBlockAndMetadata(world, i132, 5, 6, this.wallBlock, this.wallMeta);
         this.setBlockAndMetadata(world, i132, 5, 7, this.wallBlock, this.wallMeta);
         this.setBlockAndMetadata(world, i132, 6, 5, this.wallBlock, this.wallMeta);
         this.setBlockAndMetadata(world, i132, 6, 6, this.wallBlock, this.wallMeta);
         this.setBlockAndMetadata(world, i132, 7, 5, this.wallBlock, this.wallMeta);
      }

      for (int i1 = -3; i1 <= 3; i1++) {
         if (i1 != 0) {
            this.setBlockAndMetadata(world, i1, 1, 9, this.fenceGateBlock, 2);

            for (int var30 = 5; var30 <= 8; var30++) {
               int randomFloor = random.nextInt(3);
               switch (randomFloor) {
                  case 0:
                     this.setBlockAndMetadata(world, i1, 0, var30, vanilla("field_150349_c"), 0);
                     break;
                  case 1:
                     this.setBlockAndMetadata(world, i1, 0, var30, vanilla("field_150346_d"), 1);
                     break;
                  case 2:
                     this.setBlockAndMetadata(world, i1, 0, var30, got("dirtPath"), 0);
               }

               if (random.nextBoolean()) {
                  this.setBlockAndMetadata(world, i1, 1, var30, got("thatchFloor"), 0);
               }
            }

            this.setBlockAndMetadata(world, i1, 4, 5, this.plankStairBlock, 7);
            this.setBlockAndMetadata(world, i1, 4, 6, this.plankSlabBlock, this.plankSlabMeta | 8);
         }
      }

      this.setBlockAndMetadata(world, -3, 3, 9, this.plankStairBlock, 4);
      this.setBlockAndMetadata(world, -1, 3, 9, this.plankStairBlock, 5);
      this.setBlockAndMetadata(world, 1, 3, 9, this.plankStairBlock, 4);
      this.setBlockAndMetadata(world, 3, 3, 9, this.plankStairBlock, 5);

      for (int var24 = -5; var24 <= 5; var24++) {
         int avoidBeam = Math.floorMod(var24, 4) == 0 ? 1 : 0;
         if (avoidBeam == 0) {
            this.setBlockAndMetadata(world, var24, 4, 0, this.roofStairBlock, 2);
            this.setBlockAndMetadata(world, var24, 4, 9, this.roofStairBlock, 3);
         }

         for (int l = 0; l <= 2; l++) {
            this.setBlockAndMetadata(world, var24, 5 + l, 1 + l, this.roofStairBlock, 2);
            this.setBlockAndMetadata(world, var24, 5 + l, 8 - l, this.roofStairBlock, 3);
         }

         for (int k14 = 4; k14 <= 5; k14++) {
            this.setBlockAndMetadata(world, var24, 8, k14, this.roofSlabBlock, this.roofSlabMeta);
         }

         if (Math.abs(var24) == 5) {
            for (int var60 = 0; var60 <= 3; var60++) {
               this.setBlockAndMetadata(world, var24, 4 + var60, 1 + var60, this.roofStairBlock, 7);
               this.setBlockAndMetadata(world, var24, 4 + var60, 8 - var60, this.roofStairBlock, 6);
            }
         }
      }

      for (int i1321 : new int[]{-4, 0, 4}) {
         this.setBlockAndMetadata(world, i1321, 3, -1, vanilla("field_150478_aa"), 4);
         this.setBlockAndMetadata(world, i1321, 3, 10, vanilla("field_150478_aa"), 3);
      }

      this.setBlockAndMetadata(world, -5, 3, 4, vanilla("field_150478_aa"), 1);
      this.setBlockAndMetadata(world, 5, 3, 4, vanilla("field_150478_aa"), 2);
      this.setBlockAndMetadata(world, -3, 1, 3, vanilla("field_150462_ai"), 0);
      this.placeChest(world, random, -2, 1, 3, 2, "chest_GIFT");
      this.setBlockAndMetadata(world, -1, 1, 3, this.plankBlock, this.plankMeta);
      this.placePlateWithCertainty(world, random, -1, 2, 3, this.plateBlock, "food_DEFAULT");
      this.setBlockAndMetadata(world, 0, 1, 3, this.plankBlock, this.plankMeta);
      this.placeBarrel(world, random, 0, 2, 3, 2, "food_DEFAULT_DRINK");
      this.setBlockAndMetadata(world, 1, 1, 3, this.plankBlock, this.plankMeta);
      this.placeMug(world, random, 1, 2, 3, 0, "food_DEFAULT_DRINK");
      this.setBlockAndMetadata(world, 3, 1, 3, this.plankBlock, this.plankMeta);
      this.placeMug(world, random, 3, 2, 3, 1, "food_DEFAULT_DRINK");
      this.setBlockAndMetadata(world, 2, 1, 1, this.bedBlock, 1);
      this.setBlockAndMetadata(world, 3, 1, 1, this.bedBlock, 9);
      this.setBlockAndMetadata(world, -3, 3, 2, vanilla("field_150478_aa"), 2);
      this.setBlockAndMetadata(world, 3, 3, 2, vanilla("field_150478_aa"), 1);

      for (int j13 = 1; j13 <= 4; j13++) {
         this.setBlockAndMetadata(world, 2, j13, 3, vanilla("field_150468_ap"), 2);
      }

      this.setBlockAndMetadata(world, 0, 7, 5, this.plankStairBlock, 7);
      this.setBlockAndMetadata(world, -3, 7, 5, this.plankStairBlock, 4);
      this.setBlockAndMetadata(world, 3, 7, 5, this.plankStairBlock, 5);

      for (int i12 = -3; i12 <= 3; i12++) {
         if (random.nextInt(3) != 0) {
            this.setBlockAndMetadata(world, i12, 5, 2, vanilla("field_150407_cf"), 0);
         }
      }

      for (int var25 = -3; var25 <= 3; var25++) {
         if (random.nextInt(3) != 0) {
            int h = 5;
            int h1 = h + random.nextInt(2);

            for (int j1 = h; j1 <= h1; j1++) {
               this.setBlockAndMetadata(world, var25, j1, 6, vanilla("field_150407_cf"), 0);
            }
         }
      }

      for (int i1321 : new int[]{-3, 3}) {
         for (int k12 = 3; k12 <= 5; k12++) {
            if (random.nextInt(3) != 0) {
               int h = 5;
               int h1 = h + random.nextInt(2);

               for (int j14 = h; j14 <= h1; j14++) {
                  this.setBlockAndMetadata(world, i1321, j14, k12, vanilla("field_150407_cf"), 0);
               }
            }
         }
      }

      this.setBlockAndMetadata(world, -2, 5, 3, this.bedBlock, 3);
      this.setBlockAndMetadata(world, -3, 5, 3, this.bedBlock, 11);
      this.placeChest(world, random, -3, 5, 5, 4, "chest_GIFT");
      this.setBlockAndMetadata(world, -3, 6, 4, vanilla("field_150478_aa"), 2);
      this.setBlockAndMetadata(world, 3, 6, 4, vanilla("field_150478_aa"), 1);
      int men = 1;

      for (int l = 0; l < men; l++) {
         LegacyEntity man = new LegacyEntity(world);
         this.spawnNPCAndSetHome(man, world, 0, 1, 2, 8);
      }

      for (int i16 : new int[]{-2, 2}) {
         LegacyEntity horse = new LegacyEntity(world);
         this.spawnNPCAndSetHome(horse, world, i16, 1, 7, 0);
         horse.func_110214_p(0);
         horse.saddleMountForWorldGen();
         horse.func_110177_bN();
      }

      return true;
   }

}
