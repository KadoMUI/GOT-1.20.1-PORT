package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureWesterosCottage extends LegacyNorthernContext {

   protected GOTStructureWesterosCottage(NorthStructureBuilder builder) {
      super(builder, Style.NORTH);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureWesterosCottage piece = new GOTStructureWesterosCottage(builder);
      piece.marketVariant = Math.max(0, variant);
      piece.isAbandoned = variant == 1;
      piece.isTramp = variant == 1;
      piece.isHardhome = variant == 1;
      piece.isBlacksmith = variant == 1;
      piece.generate(piece, piece.legacyRandom(), 0, 0, 0, 0);
   }


   public boolean generate(LegacyNorthernContext world, Random random, int i, int j, int k, int rotation) {
      this.setOriginAndRotation(world, i, j, k, rotation, 6);
      this.setupRandomBlocks(random);
      if (this.restrictions) {
         int minHeight = 0;
         int maxHeight = 0;

         for (int i14 = -6; i14 <= 6; i14++) {
            for (int k132 = -7; k132 <= 10; k132++) {
               int j12 = this.getTopBlock(world, i14, k132) - 1;
               if (!this.isSurface(world, i14, j12, k132)) {
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

      for (int i12 = -5; i12 <= 5; i12++) {
         for (int k12 = -5; k12 <= 5; k12++) {
            int i2 = Math.abs(i12);
            int k2 = Math.abs(k12);
            if (i2 == 5 && k2 == 5) {
               for (int j12 = 3; (j12 >= 0 || !this.isOpaque(world, i12, j12, k12)) && this.getY(j12) >= 0; j12--) {
                  this.setBlockAndMetadata(world, i12, j12, k12, this.woodBeamBlock, this.woodBeamMeta);
                  this.setGrassToDirt(world, i12, j12 - 1, k12);
               }
            } else if (i2 != 5 && k2 != 5) {
               for (int j12 = 0; (j12 >= 0 || !this.isOpaque(world, i12, j12, k12)) && this.getY(j12) >= 0; j12--) {
                  this.setBlockAndMetadata(world, i12, j12, k12, this.rockBlock, this.rockMeta);
                  this.setGrassToDirt(world, i12, j12 - 1, k12);
               }

               for (int var28 = 1; var28 <= 7; var28++) {
                  this.setAir(world, i12, var28, k12);
               }

               if (random.nextInt(3) != 0) {
                  this.setBlockAndMetadata(world, i12, 1, k12, got("thatchFloor"), 0);
               }

               if (i2 == 4 && k2 == 4) {
                  for (int var29 = 1; var29 <= 4; var29++) {
                     this.setBlockAndMetadata(world, i12, var29, k12, this.woodBeamBlock, this.woodBeamMeta);
                  }
               }
            } else {
               for (int j12 = 1; (j12 >= 0 || !this.isOpaque(world, i12, j12, k12)) && this.getY(j12) >= 0; j12--) {
                  this.setBlockAndMetadata(world, i12, j12, k12, this.brickBlock, this.brickMeta);
                  this.setGrassToDirt(world, i12, j12 - 1, k12);
               }

               this.setBlockAndMetadata(world, i12, 2, k12, this.wallBlock, this.wallMeta);
               this.setBlockAndMetadata(world, i12, 3, k12, this.wallBlock, this.wallMeta);
            }
         }
      }

      for (int var36 = -5; var36 <= 5; var36++) {
         for (int k12 = -7; k12 <= -6; k12++) {
            for (int j13 = 0; (j13 >= 0 || !this.isOpaque(world, var36, j13, k12)) && this.getY(j13) >= 0; j13--) {
               this.setBlockAndMetadata(world, var36, j13, k12, got("dirtPath"), 0);
               this.setGrassToDirt(world, var36, j13 - 1, k12);
            }

            for (int var31 = 1; var31 <= 8; var31++) {
               this.setAir(world, var36, var31, k12);
            }
         }
      }

      for (int var37 = -4; var37 <= 4; var37++) {
         for (int k12 = 6; k12 <= 10; k12++) {
            if (k12 != 10 || Math.abs(var37) < 3) {
               for (int j13 = 0; (j13 >= 0 || !this.isOpaque(world, var37, j13, k12)) && this.getY(j13) >= 0; j13--) {
                  this.setBlockAndMetadata(world, var37, j13, k12, got("dirtPath"), 0);
                  this.setGrassToDirt(world, var37, j13 - 1, k12);
               }

               for (int var33 = 1; var33 <= 8; var33++) {
                  this.setAir(world, var37, var33, k12);
               }
            }
         }
      }

      for (int i15 : new int[]{-5, 5}) {
         this.setBlockAndMetadata(world, i15, 2, -3, this.fenceBlock, this.fenceMeta);
         this.setBlockAndMetadata(world, i15, 2, -2, this.fenceBlock, this.fenceMeta);
         this.setBlockAndMetadata(world, i15, 2, 0, this.fenceBlock, this.fenceMeta);
         this.setBlockAndMetadata(world, i15, 2, 2, this.fenceBlock, this.fenceMeta);
         this.setBlockAndMetadata(world, i15, 2, 3, this.fenceBlock, this.fenceMeta);
      }

      for (int k1321 : new int[]{-5, 5}) {
         for (int i16 = -1; i16 <= 1; i16++) {
            for (int j14 = 2; j14 <= 3; j14++) {
               this.setBlockAndMetadata(world, i16, j14, k1321, this.brickBlock, this.brickMeta);
            }
         }

         for (int var63 = -4; var63 <= 4; var63++) {
            this.setBlockAndMetadata(world, var63, 4, k1321, this.woodBeamBlock, this.woodBeamMeta | 4);
         }
      }

      for (int i17 = -3; i17 <= 3; i17++) {
         if (Math.abs(i17) > 1) {
            this.setBlockAndMetadata(world, i17, 2, -5, this.fenceBlock, this.fenceMeta);
            this.setBlockAndMetadata(world, i17, 3, -5, this.wallBlock, this.wallMeta);
            this.setBlockAndMetadata(world, i17, 2, 5, this.fenceBlock, this.fenceMeta);
            this.setBlockAndMetadata(world, i17, 3, 5, this.wallBlock, this.wallMeta);
         }
      }

      this.setBlockAndMetadata(world, 0, 0, -5, this.rockBlock, this.rockMeta);
      this.setBlockAndMetadata(world, 0, 1, -5, this.doorBlock, 1);
      this.setBlockAndMetadata(world, 0, 2, -5, this.doorBlock, 8);
      this.setBlockAndMetadata(world, 0, 3, -6, vanilla("field_150478_aa"), 4);
      this.setBlockAndMetadata(world, 0, 0, 5, this.rockBlock, this.rockMeta);
      this.setBlockAndMetadata(world, 0, 1, 5, this.doorBlock, 3);
      this.setBlockAndMetadata(world, 0, 2, 5, this.doorBlock, 8);
      this.setBlockAndMetadata(world, 0, 3, 6, vanilla("field_150478_aa"), 3);

      for (int k132 : new int[]{-5, 5}) {
         for (int l2 = 0; l2 <= 2; l2++) {
            for (int i18 = -3 + l2; i18 <= 3 - l2; i18++) {
               this.setBlockAndMetadata(world, i18, 5 + l2, k132, this.wallBlock, this.wallMeta);
            }
         }
      }

      this.setBlockAndMetadata(world, 0, 5, -5, this.wallBlock, this.wallMeta);
      this.setBlockAndMetadata(world, 0, 6, -5, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 0, 7, -5, this.wallBlock, this.wallMeta);
      this.setBlockAndMetadata(world, 0, 5, 5, this.wallBlock, this.wallMeta);
      this.setBlockAndMetadata(world, 0, 6, 5, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 0, 7, 5, this.wallBlock, this.wallMeta);

      for (int k1 = -6; k1 <= 6; k1++) {
         for (int l = 0; l <= 5; l++) {
            this.setBlockAndMetadata(world, -6 + l, 3 + l, k1, this.roofStairBlock, 1);
            this.setBlockAndMetadata(world, 6 - l, 3 + l, k1, this.roofStairBlock, 0);
         }

         this.setBlockAndMetadata(world, 0, 8, k1, this.roofBlock, this.roofMeta);
         this.setBlockAndMetadata(world, 0, 9, k1, this.roofSlabBlock, this.roofSlabMeta);
         if (Math.abs(k1) == 6) {
            for (int var44 = 0; var44 <= 4; var44++) {
               this.setBlockAndMetadata(world, -5 + var44, 3 + var44, k1, this.roofStairBlock, 4);
               this.setBlockAndMetadata(world, 5 - var44, 3 + var44, k1, this.roofStairBlock, 5);
            }
         }
      }

      for (int i1 = -4; i1 <= 4; i1++) {
         this.setBlockAndMetadata(world, i1, 4, 0, this.woodBeamBlock, this.woodBeamMeta | 4);
      }

      for (int var23 = -4; var23 <= 4; var23++) {
         this.setBlockAndMetadata(world, 0, 4, var23, this.woodBeamBlock, this.woodBeamMeta | 8);
      }

      for (int j1 = 1; j1 <= 7; j1++) {
         this.setBlockAndMetadata(world, 0, j1, 0, this.woodBeamBlock, this.woodBeamMeta);
      }

      this.setBlockAndMetadata(world, -1, 3, 0, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 1, 3, 0, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 0, 3, -1, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 0, 3, 1, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, -4, 3, 0, vanilla("field_150478_aa"), 2);
      this.setBlockAndMetadata(world, 4, 3, 0, vanilla("field_150478_aa"), 1);
      this.setBlockAndMetadata(world, 0, 3, -4, vanilla("field_150478_aa"), 3);
      this.setBlockAndMetadata(world, 0, 3, 4, vanilla("field_150478_aa"), 4);
      this.setBlockAndMetadata(world, 0, 5, -4, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 0, 6, -4, vanilla("field_150478_aa"), 5);
      this.setBlockAndMetadata(world, 0, 5, 4, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 0, 6, 4, vanilla("field_150478_aa"), 5);
      this.setBlockAndMetadata(world, -2, 1, -4, this.bedBlock, 3);
      this.setBlockAndMetadata(world, -3, 1, -4, this.bedBlock, 11);
      this.setBlockAndMetadata(world, 2, 1, -4, this.bedBlock, 1);
      this.setBlockAndMetadata(world, 3, 1, -4, this.bedBlock, 9);
      this.setBlockAndMetadata(world, -4, 1, -2, this.bedBlock, 2);
      this.setBlockAndMetadata(world, -4, 1, -3, this.bedBlock, 10);
      this.setBlockAndMetadata(world, 4, 1, -2, this.bedBlock, 2);
      this.setBlockAndMetadata(world, 4, 1, -3, this.bedBlock, 10);
      this.setBlockAndMetadata(world, -4, 1, 0, vanilla("field_150460_al"), 4);
      this.setBlockAndMetadata(world, -4, 1, 1, this.rockSlabDoubleBlock, this.rockSlabDoubleMeta);
      this.placePlateWithCertainty(world, random, -4, 2, 1, this.plateBlock, "food_DEFAULT");
      this.setBlockAndMetadata(world, -4, 1, 2, vanilla("field_150383_bp"), 3);
      this.setBlockAndMetadata(world, -4, 1, 3, this.rockSlabDoubleBlock, this.rockSlabDoubleMeta);
      this.placeMug(world, random, -4, 2, 3, 3, "food_DEFAULT_DRINK");
      this.setBlockAndMetadata(world, -3, 1, 4, this.rockSlabDoubleBlock, this.rockSlabDoubleMeta);
      this.placeFlowerPot(world, -3, 2, 4, this.getRandomFlower(world, random));
      this.setBlockAndMetadata(world, -2, 1, 4, vanilla("field_150407_cf"), 0);
      this.setBlockAndMetadata(world, 4, 1, 0, this.tableBlock, 0);
      this.placeChest(world, random, 4, 1, 1, 5, this.getChestContents());
      this.placeChest(world, random, 4, 1, 2, 5, this.getChestContents());
      this.setBlockAndMetadata(world, 4, 1, 3, vanilla("field_150462_ai"), 0);
      this.setBlockAndMetadata(world, 3, 1, 4, this.rockSlabDoubleBlock, this.rockSlabDoubleMeta);
      this.placeFlowerPot(world, 3, 2, 4, this.getRandomFlower(world, random));
      this.setBlockAndMetadata(world, 2, 1, 4, vanilla("field_150407_cf"), 0);
      this.setBlockAndMetadata(world, -5, 1, -6, got("reedBars"), 0);

      for (int var24 = -5; var24 <= -3; var24++) {
         this.setBlockAndMetadata(world, var24, 1, -7, got("reedBars"), 0);
      }

      this.placeFlowerPot(world, -4, 1, -6, this.getRandomFlower(world, random));
      this.placeFlowerPot(world, -3, 1, -6, this.getRandomFlower(world, random));
      this.placeFlowerPot(world, 2, 1, -6, this.getRandomFlower(world, random));
      this.setBlockAndMetadata(world, 3, 1, -6, vanilla("field_150407_cf"), 0);
      this.setBlockAndMetadata(world, 4, 1, -6, vanilla("field_150407_cf"), 0);
      this.setBlockAndMetadata(world, 5, 1, -6, vanilla("field_150407_cf"), 0);
      this.setBlockAndMetadata(world, 4, 2, -6, vanilla("field_150407_cf"), 0);
      this.setBlockAndMetadata(world, 4, 1, -7, vanilla("field_150407_cf"), 0);

      for (int var25 = 1; var25 <= 2; var25++) {
         for (int var41 = 6; var41 <= 9; var41++) {
            this.setBlockAndMetadata(world, -4, var25, var41, got("reedBars"), 0);
            this.setBlockAndMetadata(world, 4, var25, var41, got("reedBars"), 0);
         }

         this.setBlockAndMetadata(world, -3, var25, 9, got("reedBars"), 0);
         this.setBlockAndMetadata(world, -2, var25, 9, got("reedBars"), 0);
         this.setBlockAndMetadata(world, 2, var25, 9, got("reedBars"), 0);
         this.setBlockAndMetadata(world, 3, var25, 9, got("reedBars"), 0);

         for (int i13 = -2; i13 <= 2; i13++) {
            this.setBlockAndMetadata(world, i13, var25, 10, got("reedBars"), 0);
         }
      }

      int[] j15 = new int[]{-2, 1};
      int i13 = j15.length;

      int i15;
      for (int var35 = 0; var35 < i13; var35++) {
         for (int i2 = i15 = j15[var35]; i2 <= i15 + 1; i2++) {
            for (int k14 = 7; k14 <= 8; k14++) {
               this.setBlockAndMetadata(world, i2, 0, k14, vanilla("field_150458_ak"), 7);
               this.setBlockAndMetadata(world, i2, 1, k14, this.cropBlock, this.cropMeta);
            }
         }
      }

      this.setBlockAndMetadata(world, 0, -1, 9, vanilla("field_150346_d"), 0);
      this.setGrassToDirt(world, 0, -2, 9);
      this.setBlockAndMetadata(world, 0, 0, 9, vanilla("field_150355_j"), 0);
      this.setBlockAndMetadata(world, 0, 1, 9, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 0, 2, 9, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 0, 3, 9, vanilla("field_150407_cf"), 0);
      this.setBlockAndMetadata(world, 0, 4, 9, vanilla("field_150423_aK"), 0);
      LegacyEntity male = this.getMan(world);
      male.getFamilyInfo().setMale(true);
      male.func_70062_b(4, new LegacyItemStack("item_goldRing"));
      this.spawnNPCAndSetHome(male, world, 0, 1, -1, 16);
      LegacyEntity female = this.getMan(world);
      female.getFamilyInfo().setMale(false);
      female.func_70062_b(4, new LegacyItemStack("item_goldRing"));
      this.spawnNPCAndSetHome(female, world, 0, 1, -1, 16);
      LegacyEntity child = this.getMan(world);
      child.getFamilyInfo().setMale(random.nextBoolean());
      child.getFamilyInfo().setChild();
      this.spawnNPCAndSetHome(child, world, 0, 1, -1, 16);
      return true;
   }

   public void setupRandomBlocks(Random random) {
      super.setupRandomBlocks(random);
      this.wallBlock = got("daub");
      this.wallMeta = 0;
      if (random.nextInt(3) == 0) {
         this.roofBlock = this.brick2Block;
         this.roofMeta = this.brick2Meta;
         this.roofSlabBlock = this.brick2SlabBlock;
         this.roofSlabMeta = this.brick2SlabMeta;
         this.roofStairBlock = this.brick2StairBlock;
         this.bedBlock = vanilla("field_150324_C");
      }
   }

}
