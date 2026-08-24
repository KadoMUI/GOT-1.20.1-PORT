package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureWesterosStables extends LegacyNorthernContext {

   protected GOTStructureWesterosStables(NorthStructureBuilder builder) {
      super(builder, Style.NORTH);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureWesterosStables piece = new GOTStructureWesterosStables(builder);
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

         for (int i14 = -5; i14 <= 5; i14++) {
            for (int k13 = -6; k13 <= 6; k13++) {
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

      for (int i13 = -4; i13 <= 4; i13++) {
         for (int k12 = -5; k12 <= 5; k12++) {
            for (int j12 = 0; (j12 == 0 || !this.isOpaque(world, i13, j12, k12)) && this.getY(j12) >= 0; j12--) {
               this.setBlockAndMetadata(world, i13, j12, k12, this.brickBlock, this.brickMeta);
               this.setGrassToDirt(world, i13, j12 - 1, k12);
            }

            for (int var32 = 1; var32 <= 7; var32++) {
               this.setAir(world, i13, var32, k12);
            }
         }
      }

      for (int var36 = -5; var36 <= 5; var36++) {
         this.setBlockAndMetadata(world, var36, 4, -6, this.roofSlabBlock, this.roofSlabMeta);
         this.setBlockAndMetadata(world, var36, 4, -5, this.roofSlabBlock, this.roofSlabMeta | 8);
         this.setBlockAndMetadata(world, var36, 5, -4, this.roofSlabBlock, this.roofSlabMeta);
         this.setBlockAndMetadata(world, var36, 5, -3, this.roofSlabBlock, this.roofSlabMeta | 8);
         this.setBlockAndMetadata(world, var36, 6, -2, this.roofSlabBlock, this.roofSlabMeta);
         this.setBlockAndMetadata(world, var36, 6, -1, this.roofSlabBlock, this.roofSlabMeta | 8);
      }

      for (int var37 = -4; var37 <= 4; var37++) {
         int i2 = Math.abs(var37);
         if (i2 != 4 && i2 != 0) {
            this.setBlockAndMetadata(world, var37, 2, -5, this.fenceGateBlock, 2);

            for (int k1 = -4; k1 <= -1; k1++) {
               this.setBlockAndMetadata(world, var37, 0, k1, this.rockBlock, this.rockMeta);
               if (random.nextInt(3) != 0) {
                  this.setBlockAndMetadata(world, var37, 1, k1, got("thatchFloor"), 0);
               }
            }
         } else {
            for (int j14 = 1; j14 <= 3; j14++) {
               this.setBlockAndMetadata(world, var37, j14, -5, this.woodBeamBlock, this.woodBeamMeta);
            }

            this.setBlockAndMetadata(world, var37, 3, -6, this.plankStairBlock, 6);

            for (int k1 = -4; k1 <= -1; k1++) {
               this.setBlockAndMetadata(world, var37, 1, k1, this.plankBlock, this.plankMeta);
               this.setBlockAndMetadata(world, var37, 2, k1, this.fenceBlock, this.fenceMeta);
               this.setBlockAndMetadata(world, var37, 3, k1, this.fenceBlock, this.fenceMeta);
            }

            for (int var24 = -5; var24 <= -1; var24++) {
               this.setBlockAndMetadata(world, var37, 4, var24, this.roofBlock, this.roofMeta);
               if (var24 >= -3) {
                  this.setBlockAndMetadata(world, var37, 5, var24, this.roofBlock, this.roofMeta);
               }

               if (var24 >= -1) {
                  this.setBlockAndMetadata(world, var37, 6, var24, this.roofBlock, this.roofMeta);
               }
            }
         }
      }

      for (int i122 : new int[]{-4, 4}) {
         for (int j12 = 1; j12 <= 6; j12++) {
            this.setBlockAndMetadata(world, i122, j12, 0, this.pillarBlock, this.pillarMeta);
         }

         for (int var34 = 1; var34 <= 5; var34++) {
            this.setBlockAndMetadata(world, i122, var34, 5, this.pillarBlock, this.pillarMeta);
         }

         for (int k14 = 1; k14 <= 4; k14++) {
            for (int j15 = 1; j15 <= 6; j15++) {
               this.setBlockAndMetadata(world, i122, j15, k14, this.brickBlock, this.brickMeta);
            }
         }

         for (int var47 = 2; var47 <= 3; var47++) {
            for (int j15 = 1; j15 <= 2; j15++) {
               this.setAir(world, i122, j15, var47);
            }
         }

         this.setBlockAndMetadata(world, i122, 3, 2, this.brickStairBlock, 7);
         this.setBlockAndMetadata(world, i122, 3, 3, this.brickStairBlock, 6);

         for (int var48 = 1; var48 <= 4; var48++) {
            this.setBlockAndMetadata(world, i122, 4, var48, this.woodBeamBlock, this.woodBeamMeta | 8);
         }
      }

      for (int i16 = -3; i16 <= 3; i16++) {
         for (int j1 = 1; j1 <= 6; j1++) {
            if (j1 >= 2 && j1 <= 4) {
               this.setBlockAndMetadata(world, i16, j1, 0, this.plankBlock, this.plankMeta);
            } else {
               this.setBlockAndMetadata(world, i16, j1, 0, this.brickBlock, this.brickMeta);
            }
         }

         for (int var22 = 1; var22 <= 5; var22++) {
            this.setBlockAndMetadata(world, i16, var22, 5, this.brickBlock, this.brickMeta);
         }
      }

      for (int i122 : new int[]{-2, 2}) {
         this.setBlockAndMetadata(world, i122, 1, -1, vanilla("field_150407_cf"), 0);
         this.setBlockAndMetadata(world, i122, 4, -1, vanilla("field_150478_aa"), 4);
         LegacyEntity horse = new LegacyEntity(world);
         this.spawnNPCAndSetHome(horse, world, i122, 1, -3, 0);
         horse.func_110214_p(0);
         horse.saddleMountForWorldGen();
         horse.func_110177_bN();
      }

      for (int i1 = -3; i1 <= 3; i1++) {
         for (int k12 = 1; k12 <= 4; k12++) {
            this.setBlockAndMetadata(world, i1, 4, k12, this.plankBlock, this.plankMeta);
         }
      }

      for (int var28 = -5; var28 <= 5; var28++) {
         this.setBlockAndMetadata(world, var28, 7, 0, this.brick2StairBlock, 2);

         for (int k12 = 1; k12 <= 3; k12++) {
            this.setBlockAndMetadata(world, var28, 7, k12, this.brick2Block, this.brick2Meta);
         }

         this.setBlockAndMetadata(world, var28, 7, 4, this.brick2StairBlock, 3);
         this.setBlockAndMetadata(world, var28, 6, 5, this.brick2StairBlock, 3);
         this.setBlockAndMetadata(world, var28, 5, 6, this.brick2StairBlock, 3);
         if (Math.abs(var28) == 5) {
            this.setBlockAndMetadata(world, var28, 6, 4, this.brick2StairBlock, 6);
            this.setBlockAndMetadata(world, var28, 5, 5, this.brick2StairBlock, 6);
         }
      }

      for (int i1221 : new int[]{-3, 1}) {
         this.setBlockAndMetadata(world, i1221, 2, 5, this.brickStairBlock, 0);
         this.setBlockAndMetadata(world, i1221, 3, 5, this.brickStairBlock, 4);
         this.setBlockAndMetadata(world, i1221 + 1, 2, 5, this.brickWallBlock, this.brickWallMeta);
         this.setBlockAndMetadata(world, i1221 + 1, 3, 5, this.brickWallBlock, this.brickWallMeta);
         this.setBlockAndMetadata(world, i1221 + 2, 2, 5, this.brickStairBlock, 1);
         this.setBlockAndMetadata(world, i1221 + 2, 3, 5, this.brickStairBlock, 5);
      }

      this.setBlockAndMetadata(world, 0, 2, 5, this.brickCarved, this.brickCarvedMeta);
      this.setBlockAndMetadata(world, -3, 1, 4, this.plankBlock, this.plankMeta);
      this.placeFlowerPot(world, -3, 2, 4, this.getRandomFlower(world, random));
      this.setBlockAndMetadata(world, -2, 1, 4, this.plankSlabBlock, this.plankSlabMeta | 8);
      this.placeMug(world, random, -2, 2, 4, 0, "food_DEFAULT_DRINK");
      this.setBlockAndMetadata(world, -1, 1, 4, vanilla("field_150383_bp"), 3);
      this.setBlockAndMetadata(world, 0, 1, 4, this.plankSlabBlock, this.plankSlabMeta | 8);
      this.placePlateWithCertainty(world, random, 0, 2, 4, this.plateBlock, "food_DEFAULT");
      this.setBlockAndMetadata(world, 1, 1, 4, vanilla("field_150460_al"), 2);
      this.setBlockAndMetadata(world, 2, 1, 4, this.plankSlabBlock, this.plankSlabMeta | 8);
      this.placeMug(world, random, 2, 2, 4, 0, "food_DEFAULT_DRINK");
      this.setBlockAndMetadata(world, 3, 1, 4, this.plankBlock, this.plankMeta);
      this.placeFlowerPot(world, 3, 2, 4, this.getRandomFlower(world, random));
      this.setBlockAndMetadata(world, -3, 1, 1, vanilla("field_150462_ai"), 0);
      this.setBlockAndMetadata(world, -2, 1, 1, this.tableBlock, 0);
      this.setBlockAndMetadata(world, 3, 1, 1, this.plankBlock, this.plankMeta);
      this.placeMug(world, random, 3, 2, 1, 2, "food_DEFAULT_DRINK");
      this.setBlockAndMetadata(world, 0, 3, 3, got("chandelier"), 2);

      for (int j13 = 1; j13 <= 6; j13++) {
         this.setBlockAndMetadata(world, 0, j13, 0, this.pillarBlock, this.pillarMeta);
      }

      for (int var35 = 1; var35 <= 4; var35++) {
         this.setBlockAndMetadata(world, 0, var35, 1, vanilla("field_150468_ap"), 3);
      }

      for (int i17 = -3; i17 <= 3; i17++) {
         this.setBlockAndMetadata(world, i17, 6, 4, this.brick2StairBlock, 6);
      }

      this.setBlockAndMetadata(world, 3, 5, 1, vanilla("field_150407_cf"), 0);
      this.setBlockAndMetadata(world, 3, 5, 2, vanilla("field_150407_cf"), 0);
      this.setBlockAndMetadata(world, 3, 5, 3, vanilla("field_150407_cf"), 0);
      this.setBlockAndMetadata(world, 3, 6, 1, vanilla("field_150407_cf"), 0);
      this.setBlockAndMetadata(world, 2, 5, 1, vanilla("field_150407_cf"), 0);
      this.setBlockAndMetadata(world, 2, 5, 2, vanilla("field_150407_cf"), 0);
      if (random.nextInt(3) == 0) {
         this.placeChest(world, random, 3, 5, 1, 5, this.getChestContents());
      }

      this.setBlockAndMetadata(world, 3, 6, 2, vanilla("field_150478_aa"), 1);
      this.setBlockAndMetadata(world, -2, 5, 3, this.bedBlock, 3);
      this.setBlockAndMetadata(world, -3, 5, 3, this.bedBlock, 11);
      this.placeChest(world, random, -3, 5, 1, 3, this.getChestContents());
      this.placeBarrel(world, random, -2, 5, 1, 3, "food_DEFAULT_DRINK");
      LegacyEntity westerosman = this.getMan(world);
      this.spawnNPCAndSetHome(westerosman, world, 0, 1, 2, 8);
      return true;
   }

   public void setupRandomBlocks(Random random) {
      super.setupRandomBlocks(random);
      this.bedBlock = vanilla("field_150324_C");
   }

}
