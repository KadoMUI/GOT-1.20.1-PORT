package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureWildlingKeep extends LegacyNorthernContext {

   protected GOTStructureWildlingKeep(NorthStructureBuilder builder) {
      super(builder, Style.WILDLING);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureWildlingKeep piece = new GOTStructureWildlingKeep(builder);
      piece.marketVariant = Math.max(0, variant);
      piece.isAbandoned = variant == 1;
      piece.isTramp = variant == 1;
      piece.isHardhome = variant == 1;
      piece.isBlacksmith = variant == 1;
      piece.generate(piece, piece.legacyRandom(), 0, 0, 0, 0);
   }


   public boolean generate(LegacyNorthernContext world, Random random, int i, int j, int k, int rotation) {
      this.setOriginAndRotation(world, i, j, k, rotation, 1);
      this.setupRandomBlocks(random);
      if (this.restrictions) {
         int minHeight = 0;
         int maxHeight = 0;

         for (int i14 = -6; i14 <= 5; i14++) {
            for (int k16 = -10; k16 <= 10; k16++) {
               int j14 = this.getTopBlock(world, i14, k16) - 1;
               if (!this.isSurface(world, i14, j14, k16)) {
                  return false;
               }

               if (j14 < minHeight) {
                  minHeight = j14;
               }

               if (j14 > maxHeight) {
                  maxHeight = j14;
               }

               if (maxHeight - minHeight > 5) {
                  return false;
               }
            }
         }
      }

      for (int i12 = -3; i12 <= 2; i12++) {
         for (int k14 = -5; k14 <= 4; k14++) {
            if (k14 >= -4 || i12 >= -1 && i12 <= 0) {
               for (int j13 = 0; (j13 >= 0 || !this.isOpaque(world, i12, j13, k14)) && this.getY(j13) >= 0; j13--) {
                  this.setBlockAndMetadata(world, i12, j13, k14, this.plankBlock, this.plankMeta);
                  this.setGrassToDirt(world, i12, j13 - 1, k14);
               }
            }
         }
      }

      for (int var36 = -5; var36 <= 4; var36++) {
         for (int k14 = -7; k14 <= 7; k14++) {
            for (int j13 = 1; j13 <= 8; j13++) {
               this.setAir(world, var36, j13, k14);
            }
         }
      }

      for (int var37 = -3; var37 <= 2; var37++) {
         for (int k14 = -4; k14 <= 4; k14++) {
            if (random.nextBoolean()) {
               this.setBlockAndMetadata(world, var37, 1, k14, got("thatchFloor"), 0);
            }
         }
      }

      for (int var38 = -4; var38 <= 3; var38++) {
         for (int k14 = -7; k14 <= 5; k14++) {
            boolean beam = k14 == -7 && (var38 == -4 || var38 == -2 || var38 == 1 || var38 == 3) || Math.abs(k14) == 5 && (var38 == -4 || var38 == 3);
            if (beam) {
               for (int j12 = 3; (j12 >= 1 || !this.isOpaque(world, var38, j12, k14)) && this.getY(j12) >= 0; j12--) {
                  this.setBlockAndMetadata(world, var38, j12, k14, this.woodBeamBlock, this.woodBeamMeta);
                  this.setGrassToDirt(world, var38, j12 - 1, k14);
               }
            } else if (k14 >= -5) {
               if (var38 != -4 && var38 != 3) {
                  if (Math.abs(k14) == 5) {
                     this.setBlockAndMetadata(world, var38, 1, k14, this.plankBlock, this.plankMeta);
                     this.setGrassToDirt(world, var38, 0, k14);

                     for (int j12 = 2; j12 <= 3; j12++) {
                        this.setBlockAndMetadata(world, var38, j12, k14, this.plankBlock, this.plankMeta);
                     }

                     this.setBlockAndMetadata(world, var38, 4, k14, this.woodBeamBlock, this.woodBeamMeta | 4);
                  }
               } else {
                  this.setBlockAndMetadata(world, var38, 1, k14, this.plankBlock, this.plankMeta);
                  this.setGrassToDirt(world, var38, 0, k14);

                  for (int j12 = 2; j12 <= 3; j12++) {
                     this.setBlockAndMetadata(world, var38, j12, k14, this.plankBlock, this.plankMeta);
                  }
               }
            }
         }
      }

      for (int k15 = -7; k15 <= 6; k15++) {
         int roofEdge = k15 != -7 && k15 != 6 ? 0 : 1;

         for (int step = 0; step <= 4; step++) {
            int j12 = 3 + step;
            LegacyBlock stairBlock = this.roofStairBlock;
            if (step == 4 || roofEdge != 0) {
               stairBlock = this.plankStairBlock;
            }

            this.setBlockAndMetadata(world, -5 + step, j12, k15, stairBlock, 1);
            this.setBlockAndMetadata(world, 4 - step, j12, k15, stairBlock, 0);
            if (roofEdge != 0 && step <= 3) {
               this.setBlockAndMetadata(world, -4 + step, j12, k15, stairBlock, 4);
               this.setBlockAndMetadata(world, 3 - step, j12, k15, stairBlock, 5);
            }

            if (k15 >= -4 && k15 <= 4 && step >= 1 && step <= 3) {
               this.setBlockAndMetadata(world, -4 + step, j12, k15, stairBlock, 4);
               this.setBlockAndMetadata(world, 3 - step, j12, k15, stairBlock, 5);
            }
         }
      }

      for (int k161 : new int[]{-6, -5, 5}) {
         this.setBlockAndMetadata(world, -2, 5, k161, this.plankBlock, this.plankMeta);
         this.setBlockAndMetadata(world, -1, 5, k161, this.plankStairBlock, 4);
         this.setBlockAndMetadata(world, 0, 5, k161, this.plankStairBlock, 5);
         this.setBlockAndMetadata(world, 1, 5, k161, this.plankBlock, this.plankMeta);
         this.setBlockAndMetadata(world, -1, 6, k161, this.plankBlock, this.plankMeta);
         this.setBlockAndMetadata(world, 0, 6, k161, this.plankBlock, this.plankMeta);
      }

      for (int k16 : new int[]{-7, 6}) {
         this.setBlockAndMetadata(world, -1, 8, k16, this.plankStairBlock, 0);
         this.setBlockAndMetadata(world, 0, 8, k16, this.plankStairBlock, 1);
      }

      for (int i1 = -4; i1 <= 3; i1++) {
         if (i1 != -4 && i1 != -2 && i1 != 1 && i1 != 3) {
            this.setBlockAndMetadata(world, i1, 3, -7, this.plankSlabBlock, this.plankSlabMeta | 8);
         } else {
            this.setBlockAndMetadata(world, i1, 3, -7, this.plankBlock, this.plankMeta);
         }

         if (i1 >= -3 && i1 <= 2) {
            this.setBlockAndMetadata(world, i1, 3, 6, this.plankSlabBlock, this.plankSlabMeta | 8);
         }
      }

      for (int var32 = -3; var32 <= 2; var32++) {
         this.setBlockAndMetadata(world, var32, 4, -6, this.plankBlock, this.plankMeta);
      }

      this.setBlockAndMetadata(world, -4, 3, -6, this.plankSlabBlock, this.plankSlabMeta | 8);
      this.setBlockAndMetadata(world, 3, 3, -6, this.plankSlabBlock, this.plankSlabMeta | 8);
      this.setBlockAndMetadata(world, -1, 4, -6, this.rockSlabBlock, this.rockSlabMeta);
      this.setBlockAndMetadata(world, 0, 4, -6, this.rockSlabBlock, this.rockSlabMeta);
      this.setBlockAndMetadata(world, -2, 4, -7, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 1, 4, -7, this.fenceBlock, this.fenceMeta);

      for (int var33 = -1; var33 <= 0; var33++) {
         for (int j16 = 1; j16 <= 2; j16++) {
            this.setAir(world, var33, j16, -5);
         }
      }

      this.setBlockAndMetadata(world, -1, 3, -5, this.plankStairBlock, 4);
      this.setBlockAndMetadata(world, 0, 3, -5, this.plankStairBlock, 5);

      for (int i15 : new int[]{-5, 4}) {
         for (int k17 : new int[]{-7, 6}) {
            for (int j1 = 2; (j1 >= 1 || !this.isOpaque(world, i15, j1, k17)) && this.getY(j1) >= 0; j1--) {
               this.setBlockAndMetadata(world, i15, j1, k17, this.fenceBlock, this.fenceMeta);
            }
         }
      }

      for (int i15 : new int[]{-4, 3}) {
         this.setAir(world, i15, 2, -2);
         this.setBlockAndMetadata(world, i15, 3, -2, this.plankSlabBlock, this.plankSlabMeta | 8);
         this.setBlockAndMetadata(world, i15, 4, -2, this.roofBlock, this.roofMeta);
         this.setBlockAndMetadata(world, i15, 2, -3, this.fenceBlock, this.fenceMeta);
         this.setBlockAndMetadata(world, i15, 2, -1, this.fenceBlock, this.fenceMeta);
         this.setBlockAndMetadata(world, i15, 3, -3, this.plankStairBlock, 7);
         this.setBlockAndMetadata(world, i15, 3, -1, this.plankStairBlock, 6);
      }

      for (int i15 : new int[]{-5, 4}) {
         this.setBlockAndMetadata(world, i15, 1, -3, this.plankStairBlock, 7);
         this.setBlockAndMetadata(world, i15, 1, -2, this.plankSlabBlock, this.plankSlabMeta | 8);
         this.setBlockAndMetadata(world, i15, 1, -1, this.plankStairBlock, 6);

         for (int k18 = -3; k18 <= -1; k18++) {
            if (random.nextBoolean()) {
               this.placeFlowerPot(world, i15, 2, k18, this.getRandomFlower(world, random));
            }
         }

         this.setBlockAndMetadata(world, i15, 3, -4, this.roofBlock, this.roofMeta);
         this.setBlockAndMetadata(world, i15, 3, -3, this.roofSlabBlock, this.roofSlabMeta | 8);
         this.setBlockAndMetadata(world, i15, 4, -3, this.roofSlabBlock, this.roofSlabMeta);
         this.setBlockAndMetadata(world, i15, 4, -2, this.roofBlock, this.roofMeta);
         this.setAir(world, i15, 3, -2);
         this.setBlockAndMetadata(world, i15, 3, -1, this.roofSlabBlock, this.roofSlabMeta | 8);
         this.setBlockAndMetadata(world, i15, 4, -1, this.roofSlabBlock, this.roofSlabMeta);
         this.setBlockAndMetadata(world, i15, 3, 0, this.roofBlock, this.roofMeta);

         for (int k17 : new int[]{-4, 0}) {
            for (int j1 = 2; (j1 >= 1 || !this.isOpaque(world, i15, j1, k17)) && this.getY(j1) >= 0; j1--) {
               this.setBlockAndMetadata(world, i15, j1, k17, this.fenceBlock, this.fenceMeta);
            }
         }
      }

      this.setBlockAndMetadata(world, -4, 2, 3, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, -2, 2, 5, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 1, 2, 5, this.fenceBlock, this.fenceMeta);

      for (int k12 = 1; k12 <= 3; k12++) {
         for (int i13 = 2; i13 <= 3; i13++) {
            for (int j13 = 5; (j13 >= 0 || !this.isOpaque(world, i13, j13, k12)) && this.getY(j13) >= 0; j13--) {
               this.setBlockAndMetadata(world, i13, j13, k12, this.brickBlock, this.brickMeta);
            }
         }
      }

      this.setBlockAndMetadata(world, 3, 5, 1, this.brickStairBlock, 2);
      this.setBlockAndMetadata(world, 3, 5, 3, this.brickStairBlock, 3);
      this.setBlockAndMetadata(world, 2, 6, 1, this.brickStairBlock, 2);
      this.setBlockAndMetadata(world, 2, 6, 3, this.brickStairBlock, 3);
      this.setBlockAndMetadata(world, 3, 6, 2, this.brickStairBlock, 0);
      this.setBlockAndMetadata(world, 1, 6, 2, this.brickBlock, this.brickMeta);

      for (int j17 = 6; j17 <= 8; j17++) {
         this.setBlockAndMetadata(world, 2, j17, 2, this.brickBlock, this.brickMeta);
      }

      this.setBlockAndMetadata(world, 2, 9, 2, this.rockSlabBlock, this.rockSlabMeta);

      for (int var30 = 0; var30 <= 4; var30++) {
         this.setBlockAndMetadata(world, 2, 4, var30, this.brickBlock, this.brickMeta);

         for (int step2 = 0; step2 <= 1; step2++) {
            this.setBlockAndMetadata(world, 1 - step2, 5 + step2, var30, this.brickStairBlock, 5);
         }
      }

      for (int k161 : new int[]{0, 4}) {
         for (int j18 = 1; j18 <= 3; j18++) {
            this.setBlockAndMetadata(world, 2, j18, k161, this.rockWallBlock, this.rockWallMeta);
         }
      }

      this.setBlockAndMetadata(world, 2, 0, 2, got("hearth"), 0);
      this.setBlockAndMetadata(world, 2, 1, 2, vanilla("field_150480_ab"), 0);
      this.setBlockAndMetadata(world, 2, 2, 2, vanilla("field_150460_al"), 5);
      this.setBlockAndMetadata(world, 2, 3, 2, this.brickBlock, this.brickMeta);
      this.setBlockAndMetadata(world, 1, 0, 2, this.rockSlabDoubleBlock, this.rockSlabDoubleMeta);
      this.setBlockAndMetadata(world, 1, 1, 1, this.brickBlock, this.brickMeta);
      this.setBlockAndMetadata(world, 1, 1, 2, this.barsBlock, 0);
      this.setBlockAndMetadata(world, 1, 1, 3, this.brickBlock, this.brickMeta);

      for (int k13 = 1; k13 <= 3; k13++) {
         this.setBlockAndMetadata(world, 1, 2, k13, this.rockSlabBlock, this.rockSlabMeta);
      }

      for (int i16 = -2; i16 <= 1; i16++) {
         this.setBlockAndMetadata(world, i16, 5, -4, this.plankSlabBlock, this.plankSlabMeta);
      }

      this.setBlockAndMetadata(world, -3, 1, -4, this.plankStairBlock, 3);
      this.setBlockAndMetadata(world, -3, 1, -3, this.plankStairBlock, 2);
      this.setBlockAndMetadata(world, -3, 1, -2, vanilla("field_150462_ai"), 0);
      this.setBlockAndMetadata(world, -3, 1, -1, got("tableWildling"), 0);
      this.placeChest(world, random, -3, 1, 0, 4, "chest_BEYOND_WALL");
      this.setBlockAndMetadata(world, 2, 1, -4, this.plankStairBlock, 7);
      this.setBlockAndMetadata(world, 2, 1, -3, this.plankSlabBlock, this.plankSlabMeta | 8);
      this.setBlockAndMetadata(world, 2, 1, -2, this.plankStairBlock, 6);
      this.setBlockAndMetadata(world, 2, 1, -1, vanilla("field_150383_bp"), 3);
      this.placeBarrel(world, random, 2, 2, -4, 5, "food_DEFAULT_DRINK");
      this.placeMug(world, random, 2, 2, -3, 1, "food_DEFAULT_DRINK");
      if (random.nextBoolean()) {
         this.placePlateWithCertainty(world, random, 2, 2, -2, this.plateBlock, "food_DEFAULT");
      } else {
         this.setBlockAndMetadata(world, 2, 2, -2, this.plateBlock, 0);
      }

      for (int var31 = 2; var31 <= 3; var31++) {
         this.setBlockAndMetadata(world, -2, 1, var31, this.bedBlock, 3);
         this.setBlockAndMetadata(world, -3, 1, var31, this.bedBlock, 11);
         this.setBlockAndMetadata(world, -3, 3, var31, this.plankSlabBlock, this.plankSlabMeta | 8);
      }

      for (int k161 : new int[]{1, 4}) {
         for (int j19 = 1; j19 <= 2; j19++) {
            this.setBlockAndMetadata(world, -3, j19, k161, this.fenceBlock, this.fenceMeta);
         }

         this.setBlockAndMetadata(world, -3, 3, k161, this.plankBlock, this.plankMeta);
      }

      this.setBlockAndMetadata(world, -3, 3, -4, vanilla("field_150478_aa"), 2);
      this.setBlockAndMetadata(world, 2, 3, -4, vanilla("field_150478_aa"), 1);
      this.setBlockAndMetadata(world, -2, 4, 4, vanilla("field_150478_aa"), 4);
      this.setBlockAndMetadata(world, 1, 4, 4, vanilla("field_150478_aa"), 4);
      if (random.nextInt(3) != 0) {
         boolean hayOrWood = random.nextBoolean();

         for (int i13 = -1; i13 <= 1; i13++) {
            for (int k19 = 6; k19 <= 7; k19++) {
               if (k19 == 6 || i13 == 0) {
                  int j12 = 1;

                  while (!this.isOpaque(world, i13, j12 - 1, k19) && this.getY(j12) >= 0) {
                     j12--;
                  }

                  int j2 = j12;
                  if (i13 == 0 && k19 == 6) {
                     j2++;
                  }

                  for (int j3 = j12; j3 <= j2; j3++) {
                     if (hayOrWood) {
                        this.setBlockAndMetadata(world, i13, j3, k19, vanilla("field_150407_cf"), 0);
                     } else {
                        this.setBlockAndMetadata(world, i13, j3, k19, this.woodBeamBlock, this.woodBeamMeta | 8);
                     }
                  }

                  this.setGrassToDirt(world, i13, j12 - 1, k19);
               }
            }
         }
      }

      if (random.nextBoolean()) {
         int j110 = 2;
         int k14 = 6;
         ArrayList<Integer> chestCoords = new ArrayList();

         for (int i15 = -4; i15 <= 3; i15++) {
            if (!this.isOpaque(world, i15, j110, k14)) {
               chestCoords.add(i15);
            }
         }

         if (!chestCoords.isEmpty()) {
            int var67 = (Integer)chestCoords.get(random.nextInt(chestCoords.size()));

            while (!this.isOpaque(world, var67, j110 - 1, k14) && this.getY(j110) >= 0) {
               j110--;
            }

            this.placeChest(world, random, var67, j110, k14, 3, "chest_BEYOND_WALL");
         }
      }

      for (int i17 = -1; i17 <= 0; i17++) {
         for (int j14 = 1; j14 <= 3; j14++) {
            this.setBlockAndMetadata(world, i17, j14, -5, got("gateWooden"), 2);
         }
      }

      this.spawnNPCAndSetHome(new LegacyEntity(world), world, 0, 1, 0, 16);
      return true;
   }

}
