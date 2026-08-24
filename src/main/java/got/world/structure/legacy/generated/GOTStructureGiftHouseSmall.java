package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureGiftHouseSmall extends LegacyNorthernContext {

   protected GOTStructureGiftHouseSmall(NorthStructureBuilder builder) {
      super(builder, Style.GIFT);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureGiftHouseSmall piece = new GOTStructureGiftHouseSmall(builder);
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
         int minHeight = 0;
         int maxHeight = 0;

         for (int i14 = -6; i14 <= 7; i14++) {
            for (int k15 = -4; k15 <= 4; k15++) {
               int j1 = this.getTopBlock(world, i14, k15) - 1;
               if (!this.isSurface(world, i14, j1, k15)) {
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

      for (int i15 = -2; i15 <= 7; i15++) {
         for (int k13 = -3; k13 <= 3; k13++) {
            int k2 = Math.abs(k13);

            for (int j12 = 1; j12 <= 8; j12++) {
               this.setAir(world, i15, j12, k13);
            }

            if (i15 >= 5 && k2 <= 1) {
               for (int var49 = 5; (var49 >= 0 || !this.isOpaque(world, i15, var49, k13)) && this.getY(var49) >= 0; var49--) {
                  this.setBlockAndMetadata(world, i15, var49, k13, this.brickBlock, this.brickMeta);
                  this.setGrassToDirt(world, i15, var49 - 1, k13);
               }
            } else if (i15 == 6 && k2 == 2) {
               for (int var48 = 4; (var48 >= 0 || !this.isOpaque(world, i15, var48, k13)) && this.getY(var48) >= 0; var48--) {
                  this.setBlockAndMetadata(world, i15, var48, k13, this.woodBeamBlock, this.woodBeamMeta);
                  this.setGrassToDirt(world, i15, var48 - 1, k13);
               }
            } else if (i15 <= 5) {
               boolean beam = false;
               boolean wall = false;
               if (i15 == -2 && Math.floorMod(k13, 3) == 0) {
                  beam = true;
               }

               if (k2 == 3 && (i15 == 2 || i15 == 5)) {
                  beam = true;
               }

               if (i15 == -2 || k2 == 3) {
                  wall = true;
               }

               if (beam) {
                  for (int j13 = 4; (j13 >= 0 || !this.isOpaque(world, i15, j13, k13)) && this.getY(j13) >= 0; j13--) {
                     this.setBlockAndMetadata(world, i15, j13, k13, this.woodBeamBlock, this.woodBeamMeta);
                     this.setGrassToDirt(world, i15, j13 - 1, k13);
                  }
               } else if (wall) {
                  for (int j13 = 1; j13 <= 4; j13++) {
                     this.setBlockAndMetadata(world, i15, j13, k13, this.wallBlock, this.wallMeta);
                  }

                  for (int var58 = 0; (var58 >= 0 || !this.isOpaque(world, i15, var58, k13)) && this.getY(var58) >= 0; var58--) {
                     this.setBlockAndMetadata(world, i15, var58, k13, this.plankBlock, this.plankMeta);
                     this.setGrassToDirt(world, i15, var58 - 1, k13);
                  }
               } else {
                  for (int j13 = 0; (j13 >= 0 || !this.isOpaque(world, i15, j13, k13)) && this.getY(j13) >= 0; j13--) {
                     this.setBlockAndMetadata(world, i15, j13, k13, this.plankBlock, this.plankMeta);
                     this.setGrassToDirt(world, i15, j13 - 1, k13);
                  }

                  if (random.nextInt(3) == 0) {
                     this.setBlockAndMetadata(world, i15, 1, k13, got("thatchFloor"), 0);
                  }
               }
            }
         }
      }

      this.setBlockAndMetadata(world, 0, 0, -3, this.brickBlock, this.brickMeta);
      this.setBlockAndMetadata(world, 0, 1, -3, this.doorBlock, 1);
      this.setBlockAndMetadata(world, 0, 2, -3, this.doorBlock, 8);
      this.setBlockAndMetadata(world, -2, 2, -1, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, -2, 2, 1, this.fenceBlock, this.fenceMeta);

      for (int k15 : new int[]{-3, 3}) {
         if (k15 >= 0) {
            this.setBlockAndMetadata(world, 0, 2, k15, this.fenceBlock, this.fenceMeta);
         }

         this.setBlockAndMetadata(world, 3, 2, k15, this.fenceBlock, this.fenceMeta);
      }

      for (int i13 = -2; i13 <= 5; i13++) {
         this.setBlockAndMetadata(world, i13, 4, -3, this.woodBeamBlock, this.woodBeamMeta | 4);
         this.setBlockAndMetadata(world, i13, 4, 3, this.woodBeamBlock, this.woodBeamMeta | 4);
         if (i13 <= 4) {
            this.setBlockAndMetadata(world, i13, 4, 0, this.woodBeamBlock, this.woodBeamMeta | 4);
         }
      }

      for (int var37 = -2; var37 <= 5; var37++) {
         this.setBlockAndMetadata(world, var37, 4, -4, this.roofStairBlock, 2);
         this.setBlockAndMetadata(world, var37, 4, 4, this.roofStairBlock, 3);
         this.setBlockAndMetadata(world, var37, 5, -3, this.roofStairBlock, 2);
         this.setBlockAndMetadata(world, var37, 5, 3, this.roofStairBlock, 3);
      }

      for (int k14 = -3; k14 <= 3; k14++) {
         int k22 = Math.abs(k14);
         this.setBlockAndMetadata(world, -3, 4, k14, this.roofStairBlock, 1);
         if (k22 <= 2) {
            this.setBlockAndMetadata(world, -2, 5, k14, this.roofStairBlock, 1);
         }

         if (k22 >= 2) {
            this.setBlockAndMetadata(world, 6, 4, k14, this.roofStairBlock, 0);
         }

         if (k22 == 2) {
            this.setBlockAndMetadata(world, 5, 5, k14, this.roofStairBlock, 0);
         }
      }

      for (int var38 = -1; var38 <= 4; var38++) {
         for (int var33 = -2; var33 <= 2; var33++) {
            int var42 = Math.abs(var33);
            if (var42 <= 1 && var38 >= 0 && var38 <= 3) {
               this.setBlockAndMetadata(world, var38, 6, var33, this.roofBlock, this.roofMeta);
            } else {
               this.setBlockAndMetadata(world, var38, 6, var33, this.roofSlabBlock, this.roofSlabMeta);
            }
         }
      }

      for (int k151 : new int[]{-2, 2}) {
         for (int i1 = -1; i1 <= 4; i1++) {
            this.setBlockAndMetadata(world, i1, 5, k151, this.roofBlock, this.roofMeta);
         }
      }

      for (int i1221 : new int[]{-1, 4}) {
         for (int k12 = -1; k12 <= 1; k12++) {
            this.setBlockAndMetadata(world, i1221, 5, k12, this.fenceBlock, this.fenceMeta);
         }
      }

      this.setBlockAndMetadata(world, 6, 5, -1, this.brickStairBlock, 2);
      this.setBlockAndMetadata(world, 7, 5, -1, this.brickStairBlock, 2);
      this.setBlockAndMetadata(world, 7, 5, 0, this.brickStairBlock, 0);
      this.setBlockAndMetadata(world, 7, 5, 1, this.brickStairBlock, 3);
      this.setBlockAndMetadata(world, 6, 5, 1, this.brickStairBlock, 3);

      for (int j14 = 6; j14 <= 7; j14++) {
         this.setBlockAndMetadata(world, 6, j14, 0, this.brickBlock, this.brickMeta);
      }

      this.setBlockAndMetadata(world, 6, 8, 0, this.brickWallBlock, this.brickWallMeta);
      this.setBlockAndMetadata(world, 2, 2, -4, vanilla("field_150478_aa"), 4);
      this.setBlockAndMetadata(world, 2, 2, 4, vanilla("field_150478_aa"), 3);
      this.setBlockAndMetadata(world, 0, 3, -2, vanilla("field_150478_aa"), 3);
      this.setBlockAndMetadata(world, -1, 1, -1, this.tableBlock, 0);
      this.setBlockAndMetadata(world, -1, 1, 0, this.plankBlock, this.plankMeta);
      this.placePlateWithCertainty(world, random, -1, 2, 0, this.plateBlock, "food_DEFAULT");
      this.setBlockAndMetadata(world, -1, 1, 1, vanilla("field_150462_ai"), 0);
      this.setBlockAndMetadata(world, -1, 1, 2, this.plankBlock, this.plankMeta);
      this.placeMug(world, random, -1, 2, 2, random.nextInt(3), "food_DEFAULT_DRINK");
      this.placeChest(world, random, 0, 1, 2, 2, "chest_GIFT");
      this.setBlockAndMetadata(world, 1, 1, 2, this.plankBlock, this.plankMeta);
      this.placeBarrel(world, random, 1, 2, 2, 2, "food_DEFAULT_DRINK");

      for (int k151 : new int[]{-2, 2}) {
         this.setBlockAndMetadata(world, 3, 1, k151, this.bedBlock, 1);
         this.setBlockAndMetadata(world, 4, 1, k151, this.bedBlock, 9);
         this.setBlockAndMetadata(world, 5, 1, k151, this.plankBlock, this.plankMeta);

         for (int j1 = 2; j1 <= 4; j1++) {
            this.setBlockAndMetadata(world, 5, j1, k151, this.fenceBlock, this.fenceMeta);
         }
      }

      this.setBlockAndMetadata(world, 6, 0, 0, got("hearth"), 0);
      this.setBlockAndMetadata(world, 6, 1, 0, vanilla("field_150480_ab"), 0);

      for (int j15 = 2; j15 <= 3; j15++) {
         this.setAir(world, 6, j15, 0);
      }

      this.setBlockAndMetadata(world, 5, 1, 0, this.barsBlock, 0);
      this.setBlockAndMetadata(world, 5, 2, 0, vanilla("field_150460_al"), 5);
      this.spawnItemFrame(world, 5, 3, 0, 3, getRandFrameItem(random));
      int gateX = 0;
      int gateZ = 0;
      int gateMeta = -1;
      int i122 = -5;

      label275:
      while (true) {
         if (i122 <= -5) {
            int k12 = -4;
            if (this.isValidGatePos(world, i122, 1, k12)) {
               gateX = i122;
               gateZ = k12 + 1;
               gateMeta = 0;
               break;
            }

            int var29 = 4;
            if (!this.isValidGatePos(world, i122, 1, var29)) {
               i122++;
               continue;
            }

            gateX = i122;
            gateZ = var29 - 1;
            gateMeta = 2;
            break;
         }

         int k15 = -2;

         while (true) {
            if (k15 > 2) {
               break label275;
            }

            int i1 = -7;
            if (this.isValidGatePos(world, i1, 1, k15)) {
               gateX = i1 + 1;
               gateZ = k15;
               gateMeta = 3;
               break label275;
            }

            k15++;
         }
      }

      if (gateMeta != -1) {
         for (int var31 = -6; var31 <= -3; var31++) {
            for (int k12 = -3; k12 <= 3; k12++) {
               int k2 = Math.abs(k12);

               for (int j1 = 1; j1 <= 3; j1++) {
                  this.setAir(world, var31, j1, k12);
               }

               for (int var35 = 0; (var35 >= 0 || !this.isOpaque(world, var31, var35, k12)) && this.getY(var35) >= 0; var35--) {
                  if (var35 == 0) {
                     this.setBlockAndMetadata(world, var31, 0, k12, vanilla("field_150349_c"), 0);
                  } else {
                     this.setBlockAndMetadata(world, var31, var35, k12, vanilla("field_150346_d"), 0);
                  }

                  this.setGrassToDirt(world, var31, var35 - 1, k12);
               }

               if (var31 == -6 || k2 == 3) {
                  this.setBlockAndMetadata(world, var31, 1, k12, this.fenceBlock, this.fenceMeta);
               }
            }
         }

         this.setBlockAndMetadata(world, gateX, 1, gateZ, this.fenceGateBlock, gateMeta);
         this.setBlockAndMetadata(world, gateX, 0, gateZ, got("dirtPath"), 0);

         for (int k15 = -2; k15 <= 2; k15++) {
            this.setBlockAndMetadata(world, -5, 0, k15, got("dirtPath"), 0);

            for (int i1 = -4; i1 <= -3; i1++) {
               if (k15 != 0 || i1 != -3) {
                  this.setBlockAndMetadata(world, i1, 0, k15, vanilla("field_150458_ak"), 7);
                  this.setBlockAndMetadata(world, i1, 1, k15, this.cropBlock, this.cropMeta);
               }
            }
         }

         this.setBlockAndMetadata(world, -3, -1, 0, vanilla("field_150346_d"), 0);
         this.setGrassToDirt(world, -3, -2, 0);
         this.setBlockAndMetadata(world, -3, 0, 0, vanilla("field_150355_j"), 0);
         this.setBlockAndMetadata(world, -3, 1, 0, this.fenceBlock, this.fenceMeta);
         this.setBlockAndMetadata(world, -3, 2, 0, vanilla("field_150478_aa"), 1);
         this.setBlockAndMetadata(world, -6, 2, 0, this.fenceBlock, this.fenceMeta);
         this.setBlockAndMetadata(world, -6, 3, 0, vanilla("field_150407_cf"), 0);
         this.setBlockAndMetadata(world, -6, 4, 0, vanilla("field_150423_aK"), 3);
      }

      LegacyEntity male = new LegacyEntity(world);
      male.getFamilyInfo().setMale(true);
      male.func_70062_b(4, new LegacyItemStack("item_goldRing"));
      this.spawnNPCAndSetHome(male, world, 2, 1, 0, 16);
      LegacyEntity female = new LegacyEntity(world);
      female.getFamilyInfo().setMale(false);
      female.func_70062_b(4, new LegacyItemStack("item_goldRing"));
      this.spawnNPCAndSetHome(female, world, 2, 1, 0, 16);
      LegacyEntity child = new LegacyEntity(world);
      child.getFamilyInfo().setMale(random.nextBoolean());
      child.getFamilyInfo().setChild();
      this.spawnNPCAndSetHome(child, world, 2, 1, 0, 16);
      return true;
   }

   private boolean isValidGatePos(LegacyNorthernContext world, int i, int j, int k) {
      return this.isOpaque(world, i, j - 1, k) && this.isAir(world, i, j, k) && this.isAir(world, i, j + 1, k);
   }

}
