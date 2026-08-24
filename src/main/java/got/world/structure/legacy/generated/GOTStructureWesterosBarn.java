package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureWesterosBarn extends LegacyNorthernContext {

   protected GOTStructureWesterosBarn(NorthStructureBuilder builder) {
      super(builder, Style.NORTH);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureWesterosBarn piece = new GOTStructureWesterosBarn(builder);
      piece.marketVariant = Math.max(0, variant);
      piece.isAbandoned = variant == 1;
      piece.isTramp = variant == 1;
      piece.isHardhome = variant == 1;
      piece.isBlacksmith = variant == 1;
      piece.generate(piece, piece.legacyRandom(), 0, 0, 0, 0);
   }


   

   public static LegacyEntity getRandomAnimal(LegacyNorthernContext world, Random random) {
      int animal = random.nextInt(4);
      switch (animal) {
         case 0:
            return new LegacyEntity(world);
         case 1:
            return new LegacyEntity(world);
         case 2:
            return new LegacyEntity(world);
         case 3:
            return new LegacyEntity(world);
         default:
            return null;
      }
   }

   public boolean generate(LegacyNorthernContext world, Random random, int i, int j, int k, int rotation) {
      this.setOriginAndRotation(world, i, j, k, rotation, 1);
      this.setupRandomBlocks(random);
      if (this.restrictions) {
         int minHeight = 0;
         int maxHeight = 0;

         for (int i13 = -12; i13 <= 5; i13++) {
            for (int k122 = -2; k122 <= 15; k122++) {
               int j12 = this.getTopBlock(world, i13, k122) - 1;
               if (!this.isSurface(world, i13, j12, k122)) {
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

      for (int i14 = -12; i14 <= 4; i14++) {
         for (int k13 = -2; k13 <= 15; k13++) {
            this.setBlockAndMetadata(world, i14, 0, k13, vanilla("field_150349_c"), 0);

            for (int j1 = -1; !this.isOpaque(world, i14, j1, k13) && this.getY(j1) >= 0; j1--) {
               this.setBlockAndMetadata(world, i14, j1, k13, vanilla("field_150346_d"), 0);
               this.setGrassToDirt(world, i14, j1 - 1, k13);
            }

            for (int var31 = 1; var31 <= 10; var31++) {
               this.setAir(world, i14, var31, k13);
            }
         }
      }

      for (int i15 : new int[]{-4, 4}) {
         for (int k14 = 0; k14 <= 13; k14++) {
            this.setBlockAndMetadata(world, i15, 1, k14, this.rockBlock, this.rockMeta);
            if (k14 != 0 && k14 != 4 && k14 != 9 && k14 != 13) {
               for (int j13 = 2; j13 <= 5; j13++) {
                  this.setBlockAndMetadata(world, i15, j13, k14, this.plankBlock, this.plankMeta);
               }
            } else {
               for (int j13 = 2; j13 <= 5; j13++) {
                  this.setBlockAndMetadata(world, i15, j13, k14, this.woodBeamBlock, this.woodBeamMeta);
               }
            }
         }

         this.setBlockAndMetadata(world, i15, 4, 1, this.plankStairBlock, 7);
         this.setBlockAndMetadata(world, i15, 4, 2, this.fenceBlock, this.fenceMeta);
         this.setBlockAndMetadata(world, i15, 4, 3, this.plankStairBlock, 6);
         this.setBlockAndMetadata(world, i15, 4, 10, this.plankStairBlock, 7);
         this.setBlockAndMetadata(world, i15, 4, 11, this.fenceBlock, this.fenceMeta);
         this.setBlockAndMetadata(world, i15, 4, 12, this.plankStairBlock, 6);
      }

      for (int k1221 : new int[]{0, 13}) {
         for (int i12 = -3; i12 <= 3; i12++) {
            int i2 = Math.abs(i12);
            if (i2 <= 1) {
               this.setBlockAndMetadata(world, i12, 1, k1221, this.fenceGateBlock, 0);
            } else {
               this.setBlockAndMetadata(world, i12, 1, k1221, this.rockBlock, this.rockMeta);
            }

            if (i2 == 2) {
               for (int j14 = 2; j14 <= 7; j14++) {
                  this.setBlockAndMetadata(world, i12, j14, k1221, this.woodBeamBlock, this.woodBeamMeta);
               }
            }

            if (i2 == 3) {
               for (int j14 = 2; j14 <= 5; j14++) {
                  this.setBlockAndMetadata(world, i12, j14, k1221, this.plankBlock, this.plankMeta);
               }

               for (int var65 = 6; var65 <= 8; var65++) {
                  this.setBlockAndMetadata(world, i12, var65, k1221, this.wallBlock, this.wallMeta);
               }
            }
         }

         this.setBlockAndMetadata(world, -1, 4, k1221, this.plankStairBlock, 4);
         this.setBlockAndMetadata(world, 1, 4, k1221, this.plankStairBlock, 5);

         for (int var39 = -1; var39 <= 1; var39++) {
            this.setBlockAndMetadata(world, var39, 5, k1221, this.plankBlock, this.plankMeta);
            this.setBlockAndMetadata(world, var39, 6, k1221, this.wallBlock, this.wallMeta);
            this.setBlockAndMetadata(world, var39, 8, k1221, this.wallBlock, this.wallMeta);
            this.setBlockAndMetadata(world, var39, 9, k1221, this.wallBlock, this.wallMeta);
         }

         this.setBlockAndMetadata(world, -1, 7, k1221, this.wallBlock, this.wallMeta);
         this.setBlockAndMetadata(world, 0, 7, k1221, this.fenceBlock, this.fenceMeta);
         this.setBlockAndMetadata(world, 1, 7, k1221, this.wallBlock, this.wallMeta);
      }

      for (int k122 : new int[]{-1, 14}) {
         for (int i12 = -3; i12 <= 3; i12++) {
            this.setBlockAndMetadata(world, i12, 6, k122, this.woodBeamBlock, this.woodBeamMeta | 4);
         }
      }

      this.setBlockAndMetadata(world, 0, 5, -1, this.plankStairBlock, 6);
      this.setBlockAndMetadata(world, 0, 5, 14, this.plankStairBlock, 7);

      for (int k1 = -1; k1 <= 14; k1++) {
         this.setBlockAndMetadata(world, -2, 8, k1, this.woodBeamBlock, this.woodBeamMeta | 8);
         this.setBlockAndMetadata(world, 2, 8, k1, this.woodBeamBlock, this.woodBeamMeta | 8);
         this.setBlockAndMetadata(world, 0, 10, k1, this.woodBeamBlock, this.woodBeamMeta | 8);
         this.setBlockAndMetadata(world, -5, 5, k1, this.roofStairBlock, 1);
         this.setBlockAndMetadata(world, -4, 6, k1, this.roofBlock, this.roofMeta);
         this.setBlockAndMetadata(world, -4, 7, k1, this.roofStairBlock, 1);
         this.setBlockAndMetadata(world, -3, 8, k1, this.roofStairBlock, 1);
         this.setBlockAndMetadata(world, -2, 9, k1, this.roofStairBlock, 1);
         this.setBlockAndMetadata(world, -1, 10, k1, this.roofStairBlock, 1);
         this.setBlockAndMetadata(world, 0, 11, k1, this.roofSlabBlock, this.roofSlabMeta);
         this.setBlockAndMetadata(world, 1, 10, k1, this.roofStairBlock, 0);
         this.setBlockAndMetadata(world, 2, 9, k1, this.roofStairBlock, 0);
         this.setBlockAndMetadata(world, 3, 8, k1, this.roofStairBlock, 0);
         this.setBlockAndMetadata(world, 4, 7, k1, this.roofStairBlock, 0);
         this.setBlockAndMetadata(world, 4, 6, k1, this.roofBlock, this.roofMeta);
         this.setBlockAndMetadata(world, 5, 5, k1, this.roofStairBlock, 0);
         if (k1 == -1 || k1 == 14) {
            this.setBlockAndMetadata(world, -4, 5, k1, this.roofStairBlock, 4);
            this.setBlockAndMetadata(world, -3, 7, k1, this.roofStairBlock, 4);
            this.setBlockAndMetadata(world, -1, 9, k1, this.roofStairBlock, 4);
            this.setBlockAndMetadata(world, 1, 9, k1, this.roofStairBlock, 5);
            this.setBlockAndMetadata(world, 3, 7, k1, this.roofStairBlock, 5);
            this.setBlockAndMetadata(world, 4, 5, k1, this.roofStairBlock, 5);
         }
      }

      for (int var20 = 1; var20 <= 3; var20++) {
         this.setBlockAndMetadata(world, -3, 1, var20, vanilla("field_150407_cf"), 0);
      }

      for (int var21 = 1; var21 <= 2; var21++) {
         this.setBlockAndMetadata(world, -3, 2, var21, vanilla("field_150407_cf"), 0);
         this.setBlockAndMetadata(world, -2, 1, var21, vanilla("field_150407_cf"), 0);
      }

      for (int var22 = 10; var22 <= 12; var22++) {
         this.setBlockAndMetadata(world, -3, 1, var22, vanilla("field_150407_cf"), 0);
         this.setBlockAndMetadata(world, 3, 1, var22, vanilla("field_150407_cf"), 0);
      }

      for (int var23 = 11; var23 <= 12; var23++) {
         this.setBlockAndMetadata(world, -3, 2, var23, vanilla("field_150407_cf"), 0);
         this.setBlockAndMetadata(world, -2, 1, var23, vanilla("field_150407_cf"), 0);
         this.setBlockAndMetadata(world, 2, 1, var23, vanilla("field_150407_cf"), 0);
         this.setBlockAndMetadata(world, 3, 2, var23, vanilla("field_150407_cf"), 0);
      }

      this.setBlockAndMetadata(world, -3, 1, 4, vanilla("field_150462_ai"), 0);
      this.setBlockAndMetadata(world, -3, 1, 9, vanilla("field_150462_ai"), 0);
      this.setBlockAndMetadata(world, 3, 1, 9, vanilla("field_150462_ai"), 0);

      for (int j15 = 2; j15 <= 4; j15++) {
         this.setBlockAndMetadata(world, -3, j15, 4, this.fenceBlock, this.fenceMeta);
         this.setBlockAndMetadata(world, -3, j15, 9, this.fenceBlock, this.fenceMeta);
         this.setBlockAndMetadata(world, 3, j15, 9, this.fenceBlock, this.fenceMeta);
      }

      for (int var24 = 1; var24 <= 12; var24++) {
         for (int i16 = -3; i16 <= 3; i16++) {
            this.setBlockAndMetadata(world, i16, 5, var24, this.plankBlock, this.plankMeta);
         }

         this.setBlockAndMetadata(world, -3, 7, var24, this.plankSlabBlock, this.plankSlabMeta | 8);
         this.setBlockAndMetadata(world, -1, 9, var24, this.plankSlabBlock, this.plankSlabMeta | 8);
         this.setBlockAndMetadata(world, 1, 9, var24, this.plankSlabBlock, this.plankSlabMeta | 8);
         this.setBlockAndMetadata(world, 3, 7, var24, this.plankSlabBlock, this.plankSlabMeta | 8);
      }

      this.setBlockAndMetadata(world, 0, 4, 4, got("chandelier"), 2);
      this.setBlockAndMetadata(world, 0, 4, 9, got("chandelier"), 2);

      for (int step = 0; step <= 3; step++) {
         this.setBlockAndMetadata(world, 3, 1 + step, 2 + step, this.plankStairBlock, 2);
         this.setBlockAndMetadata(world, 3, 1 + step, 3 + step, this.plankStairBlock, 7);
      }

      this.setBlockAndMetadata(world, 2, 4, 6, this.plankStairBlock, 5);

      for (int var25 = 3; var25 <= 5; var25++) {
         this.setAir(world, 3, 5, var25);
      }

      this.setAir(world, 3, 5, 6);
      this.setAir(world, 3, 7, 6);
      this.setBlockAndMetadata(world, 2, 5, 6, this.plankStairBlock, 0);
      this.setBlockAndMetadata(world, 3, 6, 2, this.fenceBlock, this.fenceMeta);

      for (int var26 = 2; var26 <= 5; var26++) {
         this.setBlockAndMetadata(world, 2, 6, var26, this.fenceBlock, this.fenceMeta);
      }

      this.setBlockAndMetadata(world, 1, 6, 5, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 1, 6, 6, this.fenceGateBlock, 1);
      this.setBlockAndMetadata(world, 1, 6, 7, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 2, 6, 7, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 3, 6, 7, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 3, 6, 9, this.bedBlock, 2);
      this.setBlockAndMetadata(world, 3, 6, 8, this.bedBlock, 10);
      this.setBlockAndMetadata(world, 2, 6, 12, vanilla("field_150462_ai"), 0);
      this.setBlockAndMetadata(world, 3, 6, 12, this.tableBlock, 0);
      this.placeChest(world, random, 3, 6, 11, 5, this.getChestContents());

      for (int i1 = -3; i1 <= -2; i1++) {
         for (int var36 = 7; var36 <= 8; var36++) {
            this.setBlockAndMetadata(world, i1, 6, var36, this.plankBlock, this.plankMeta);
         }
      }

      this.placeBarrel(world, random, -3, 6, 6, 4, "food_DEFAULT_DRINK");
      this.placeMug(world, random, -2, 7, 7, 3, "food_DEFAULT_DRINK");
      this.placePlateWithCertainty(world, random, -2, 7, 8, this.plateBlock, "food_DEFAULT");
      this.setBlockAndMetadata(world, 0, 9, 4, got("chandelier"), 2);
      this.setBlockAndMetadata(world, 0, 9, 9, got("chandelier"), 2);

      for (int var27 = 1; var27 <= 5; var27++) {
         this.setBlockAndMetadata(world, -3, 6, var27, vanilla("field_150407_cf"), 0);
      }

      for (int var28 = 1; var28 <= 4; var28++) {
         this.setBlockAndMetadata(world, -2, 6, var28, vanilla("field_150407_cf"), 0);
      }

      for (int var29 = 2; var29 <= 3; var29++) {
         this.setBlockAndMetadata(world, -2, 7, var29, vanilla("field_150407_cf"), 0);
         this.setBlockAndMetadata(world, -1, 6, var29, vanilla("field_150407_cf"), 0);
      }

      this.setBlockAndMetadata(world, -3, 6, 11, vanilla("field_150407_cf"), 0);
      this.setBlockAndMetadata(world, -3, 6, 12, vanilla("field_150407_cf"), 0);
      this.setBlockAndMetadata(world, -3, 7, 12, vanilla("field_150407_cf"), 0);

      for (int var30 = 10; var30 <= 12; var30++) {
         this.setBlockAndMetadata(world, -2, 6, var30, vanilla("field_150407_cf"), 0);
         this.setBlockAndMetadata(world, -1, 6, var30, vanilla("field_150407_cf"), 0);
      }

      this.setBlockAndMetadata(world, -2, 7, 11, vanilla("field_150407_cf"), 0);
      this.setBlockAndMetadata(world, -1, 7, 11, vanilla("field_150407_cf"), 0);
      this.setBlockAndMetadata(world, 0, 6, 11, vanilla("field_150407_cf"), 0);
      if (random.nextInt(3) == 0) {
         if (random.nextBoolean()) {
            this.placeChest(world, random, -2, 6, 3, 4, this.getChestContents());
         } else {
            this.placeChest(world, random, -1, 6, 11, 4, this.getChestContents());
         }
      }

      for (int var34 = -4; var34 <= 4; var34++) {
         for (int var37 = 0; var37 <= 13; var37++) {
            if (this.isOpaque(world, var34, 1, var37)) {
               this.setGrassToDirt(world, var34, 0, var37);
            }
         }
      }

      int animals = 3 + random.nextInt(6);

      for (int l = 0; l < animals; l++) {
         LegacyEntity animal = getRandomAnimal(world, random);
         this.spawnNPCAndSetHome(animal, world, 0, 1, 6, 0);
         animal.func_110177_bN();
      }

      for (int var38 = 1; var38 <= 12; var38++) {
         this.setBlockAndMetadata(world, -10, 1, var38, this.rockWallBlock, this.rockWallMeta);
      }

      for (int k14 : new int[]{0, 13}) {
         this.setBlockAndMetadata(world, -10, 1, k14, this.rockWallBlock, this.rockWallMeta);
         this.setBlockAndMetadata(world, -10, 2, k14, vanilla("field_150478_aa"), 5);
         this.setBlockAndMetadata(world, -9, 1, k14, this.fenceGateBlock, 0);

         for (int i17 = -8; i17 <= -5; i17++) {
            this.setBlockAndMetadata(world, i17, 1, k14, this.rockWallBlock, this.rockWallMeta);
         }

         this.setBlockAndMetadata(world, -5, 2, k14, vanilla("field_150478_aa"), 5);
      }

      for (int i18 = -9; i18 <= -5; i18++) {
         for (int k15 = 1; k15 <= 12; k15++) {
            if (i18 == -5 && k15 >= 2 && k15 <= 11) {
               this.setBlockAndMetadata(world, -5, -1, k15, vanilla("field_150346_d"), 0);
               this.setBlockAndMetadata(world, -5, 0, k15, vanilla("field_150355_j"), 0);
               this.setBlockAndMetadata(world, -5, 1, k15, this.rockSlabBlock, this.rockSlabMeta);
            } else if (i18 >= -8 && k15 >= 2 && k15 <= 11) {
               this.setBlockAndMetadata(world, i18, 0, k15, vanilla("field_150458_ak"), 7);
               this.setBlockAndMetadata(world, i18, 1, k15, this.cropBlock, this.cropMeta);
            } else {
               this.setBlockAndMetadata(world, i18, 0, k15, got("dirtPath"), 0);
            }
         }
      }

      this.setBlockAndMetadata(world, -10, 2, 6, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, -10, 3, 6, vanilla("field_150407_cf"), 0);
      this.setBlockAndMetadata(world, -10, 4, 6, vanilla("field_150423_aK"), 3);
      LegacyEntity farmer = this.getFarmer(world);
      this.spawnNPCAndSetHome(farmer, world, 0, 6, 8, 16);
      int farmhands = 1 + random.nextInt(3);

      for (int l = 0; l < farmhands; l++) {
         LegacyEntity farmhand = this.getFarmhand(world);
         this.spawnNPCAndSetHome(farmhand, world, -7, 1, 6, 12);
         ((LegacyEntity)farmhand).setSeedsItem(this.seedItem);
      }

      return true;
   }

}
