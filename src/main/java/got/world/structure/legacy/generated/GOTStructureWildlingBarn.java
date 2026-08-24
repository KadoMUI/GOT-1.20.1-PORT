package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureWildlingBarn extends LegacyNorthernContext {

   protected GOTStructureWildlingBarn(NorthStructureBuilder builder) {
      super(builder, Style.WILDLING);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureWildlingBarn piece = new GOTStructureWildlingBarn(builder);
      piece.marketVariant = Math.max(0, variant);
      piece.isAbandoned = variant == 1;
      piece.isTramp = variant == 1;
      piece.isHardhome = variant == 1;
      piece.isBlacksmith = variant == 1;
      piece.generate(piece, piece.legacyRandom(), 0, 0, 0, 0);
   }


   

   private static LegacyEntity getRandomAnimal(LegacyNorthernContext world, Random random) {
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

         for (int i12 = -7; i12 <= 7; i12++) {
            for (int k122 = -1; k122 <= 16; k122++) {
               int j12 = this.getTopBlock(world, i12, k122) - 1;
               if (!this.isSurface(world, i12, j12, k122)) {
                  return false;
               }

               if (j12 < minHeight) {
                  minHeight = j12;
               }

               if (j12 > maxHeight) {
                  maxHeight = j12;
               }

               if (maxHeight - minHeight > 6) {
                  return false;
               }
            }
         }
      }

      for (int i14 = -5; i14 <= 5; i14++) {
         for (int k13 = 0; k13 <= 15; k13++) {
            int i23 = Math.abs(i14);
            int k22 = Math.floorMod(k13, 3);

            for (int j12 = 0; (j12 >= 0 || !this.isOpaque(world, i14, j12, k13)) && this.getY(j12) >= 0; j12--) {
               this.setBlockAndMetadata(world, i14, j12, k13, this.brickBlock, this.brickMeta);
               this.setGrassToDirt(world, i14, j12 - 1, k13);
            }

            for (int var45 = 1; var45 <= 11; var45++) {
               this.setAir(world, i14, var45, k13);
            }

            int beam = 0;
            if (i23 == 5 && k22 == 0) {
               beam = 1;
            }

            if ((k13 == 0 || k13 == 15) && i23 == 2) {
               beam = 1;
            }

            if (beam != 0) {
               for (int j1 = 1; j1 <= 5; j1++) {
                  this.setBlockAndMetadata(world, i14, j1, k13, this.woodBeamBlock, this.woodBeamMeta);
               }

               if (k13 == 0 || k13 == 15) {
                  for (int var36 = 6; var36 <= 7; var36++) {
                     this.setBlockAndMetadata(world, i14, var36, k13, this.woodBeamBlock, this.woodBeamMeta);
                  }
               }
            } else if (i23 == 5 || k13 == 0 || k13 == 15) {
               this.setBlockAndMetadata(world, i14, 1, k13, this.plankBlock, this.plankMeta);

               for (int j1 = 2; j1 <= 5; j1++) {
                  this.setBlockAndMetadata(world, i14, j1, k13, this.plankBlock, this.plankMeta);
               }

               if (k13 == 0 || k13 == 15) {
                  for (int var34 = 6; var34 <= 7; var34++) {
                     this.setBlockAndMetadata(world, i14, var34, k13, this.plankBlock, this.plankMeta);
                  }
               }

               this.setBlockAndMetadata(world, i14, 5, k13, this.woodBeamBlock, this.woodBeamMeta | 4);
               this.setBlockAndMetadata(world, i14, 8, k13, this.woodBeamBlock, this.woodBeamMeta | 4);
            }

            if (i23 <= 4 && k13 >= 1 && k13 <= 14) {
               if (k13 >= 3 && k13 <= 12) {
                  this.setBlockAndMetadata(world, i14, 0, k13, vanilla("field_150346_d"), 1);
               }

               if (random.nextBoolean()) {
                  this.setBlockAndMetadata(world, i14, 1, k13, got("thatchFloor"), 0);
               }

               if (i23 >= 2 || k13 <= 3) {
                  this.setBlockAndMetadata(world, i14, 5, k13, this.plankBlock, this.plankMeta);
                  if (random.nextBoolean()) {
                     this.setBlockAndMetadata(world, i14, 6, k13, got("thatchFloor"), 0);
                  }
               }
            }
         }
      }

      for (int var47 = -5; var47 <= 5; var47++) {
         int i22 = Math.abs(var47);
         if (i22 != 2 && i22 != 5) {
            for (int k15 : new int[]{0, 15}) {
               this.setBlockAndMetadata(world, var47, 3, k15, this.plankSlabBlock, this.plankSlabMeta);
               if (var47 == -4 || var47 == 3) {
                  this.setBlockAndMetadata(world, var47, 4, k15, this.plankStairBlock, 4);
               } else if (var47 == -3 || var47 == 4) {
                  this.setBlockAndMetadata(world, var47, 4, k15, this.plankStairBlock, 5);
               }

               switch (var47) {
                  case -1:
                     this.setBlockAndMetadata(world, var47, 4, k15, this.plankStairBlock, 4);
                     break;
                  case 0:
                     this.setBlockAndMetadata(world, var47, 4, k15, this.plankSlabBlock, this.plankSlabMeta | 8);
                     break;
                  case 1:
                     this.setBlockAndMetadata(world, var47, 4, k15, this.plankStairBlock, 5);
               }

               this.setBlockAndMetadata(world, var47, 7, k15, this.fenceBlock, this.fenceMeta);
            }

            for (int k15 : new int[]{-1, 16}) {
               if (i22 >= 3 || k15 != -1) {
                  this.setBlockAndMetadata(world, var47, 1, k15, this.plankSlabBlock, this.plankSlabMeta | 8);
               }

               this.setBlockAndMetadata(world, var47, 5, k15, this.plankSlabBlock, this.plankSlabMeta | 8);
               this.setBlockAndMetadata(world, var47, 8, k15, this.plankSlabBlock, this.plankSlabMeta | 8);
            }
         } else {
            for (int k14 = -1; k14 <= 16; k14++) {
               this.setBlockAndMetadata(world, var47, 5, k14, this.woodBeamBlock, this.woodBeamMeta | 8);
               this.setBlockAndMetadata(world, var47, 8, k14, this.woodBeamBlock, this.woodBeamMeta | 8);
               if (k14 == -1 || k14 == 16) {
                  this.setBlockAndMetadata(world, var47, 1, k14, this.woodBeamBlock, this.woodBeamMeta | 8);
                  this.setGrassToDirt(world, var47, 0, k14);

                  for (int j13 = 2; j13 <= 4; j13++) {
                     this.setBlockAndMetadata(world, var47, j13, k14, this.fenceBlock, this.fenceMeta);
                  }

                  for (int var57 = 6; var57 <= 7; var57++) {
                     this.setBlockAndMetadata(world, var47, var57, k14, this.fenceBlock, this.fenceMeta);
                  }
               }
            }
         }
      }

      for (int k16 = 0; k16 <= 15; k16++) {
         int k2 = Math.floorMod(k16, 3);
         if (k2 == 0) {
            for (int i12 = -7; i12 <= 7; i12++) {
               int i2 = Math.abs(i12);
               if (i2 == 6) {
                  this.setBlockAndMetadata(world, i12, 1, k16, this.woodBeamBlock, this.woodBeamMeta | 4);
                  this.setGrassToDirt(world, i12, 0, k16);

                  for (int j12 = 2; j12 <= 4; j12++) {
                     this.setBlockAndMetadata(world, i12, j12, k16, this.fenceBlock, this.fenceMeta);
                  }
               }

               if (i2 >= 6) {
                  this.setBlockAndMetadata(world, i12, 5, k16, this.woodBeamBlock, this.woodBeamMeta | 4);
               }
            }
         } else {
            for (int i16 : new int[]{-6, 6}) {
               this.setBlockAndMetadata(world, i16, 1, k16, this.plankSlabBlock, this.plankSlabMeta | 8);
            }

            this.setBlockAndMetadata(world, -7, 5, k16, this.plankStairBlock, 1);
            this.setBlockAndMetadata(world, -6, 5, k16, this.plankStairBlock, 4);
            this.setBlockAndMetadata(world, 6, 5, k16, this.plankStairBlock, 5);
            this.setBlockAndMetadata(world, 7, 5, k16, this.plankStairBlock, 0);
            if (k16 >= 3) {
               for (int i16 : new int[]{-5, 5}) {
                  this.setBlockAndMetadata(world, i16, 3, k16, this.plankSlabBlock, this.plankSlabMeta);
                  if (k2 == 1) {
                     this.setBlockAndMetadata(world, i16, 4, k16, this.plankStairBlock, 7);
                  } else if (k2 == 2) {
                     this.setBlockAndMetadata(world, i16, 4, k16, this.plankStairBlock, 6);
                  }
               }
            }
         }
      }

      for (int k122 : new int[]{-1, 16}) {
         this.setBlockAndMetadata(world, -7, 5, k122, this.plankStairBlock, 1);
         this.setBlockAndMetadata(world, -6, 5, k122, this.plankBlock, this.plankMeta);
         this.setBlockAndMetadata(world, 6, 5, k122, this.plankBlock, this.plankMeta);
         this.setBlockAndMetadata(world, 7, 5, k122, this.plankStairBlock, 0);
      }

      for (int i17 = -1; i17 <= 1; i17++) {
         for (int j14 = 1; j14 <= 4; j14++) {
            this.setBlockAndMetadata(world, i17, j14, 0, got("gateIronBars"), 2);
         }
      }

      this.setBlockAndMetadata(world, 0, 3, 0, got("gateIronBars"), 2);

      for (int k17 = 1; k17 <= 14; k17++) {
         if (Math.floorMod(k17, 3) == 0) {
            this.setBlockAndMetadata(world, -6, 6, k17, this.plankBlock, this.plankMeta);
            this.setBlockAndMetadata(world, -6, 7, k17, this.plankBlock, this.plankMeta);
            this.setBlockAndMetadata(world, -6, 8, k17, this.plankStairBlock, 1);
            this.setBlockAndMetadata(world, -5, 9, k17, this.plankStairBlock, 1);
            this.setBlockAndMetadata(world, -4, 9, k17, this.plankSlabBlock, this.plankSlabMeta | 8);
            this.setBlockAndMetadata(world, -3, 10, k17, this.plankSlabBlock, this.plankSlabMeta);

            for (int i15 = -2; i15 <= 2; i15++) {
               this.setBlockAndMetadata(world, i15, 10, k17, this.plankSlabBlock, this.plankSlabMeta | 8);
            }

            this.setBlockAndMetadata(world, 3, 10, k17, this.plankSlabBlock, this.plankSlabMeta);
            this.setBlockAndMetadata(world, 4, 9, k17, this.plankSlabBlock, this.plankSlabMeta | 8);
            this.setBlockAndMetadata(world, 5, 9, k17, this.plankStairBlock, 0);
            this.setBlockAndMetadata(world, 6, 8, k17, this.plankStairBlock, 0);
            this.setBlockAndMetadata(world, 6, 6, k17, this.plankBlock, this.plankMeta);
            this.setBlockAndMetadata(world, 6, 7, k17, this.plankBlock, this.plankMeta);
         } else {
            this.setBlockAndMetadata(world, -6, 6, k17, this.roofBlock, this.roofMeta);
            this.setBlockAndMetadata(world, -6, 7, k17, this.roofBlock, this.roofMeta);
            this.setBlockAndMetadata(world, -6, 8, k17, this.roofStairBlock, 1);
            this.setBlockAndMetadata(world, -5, 9, k17, this.roofStairBlock, 1);
            this.setBlockAndMetadata(world, -4, 9, k17, this.roofSlabBlock, this.roofSlabMeta | 8);
            this.setBlockAndMetadata(world, -3, 10, k17, this.roofSlabBlock, this.roofSlabMeta);

            for (int i15 = -2; i15 <= 2; i15++) {
               this.setBlockAndMetadata(world, i15, 10, k17, this.roofSlabBlock, this.roofSlabMeta | 8);
            }

            this.setBlockAndMetadata(world, 3, 10, k17, this.roofSlabBlock, this.roofSlabMeta);
            this.setBlockAndMetadata(world, 4, 9, k17, this.roofSlabBlock, this.roofSlabMeta | 8);
            this.setBlockAndMetadata(world, 5, 9, k17, this.roofStairBlock, 0);
            this.setBlockAndMetadata(world, 6, 8, k17, this.roofStairBlock, 0);
            this.setBlockAndMetadata(world, 6, 6, k17, this.roofBlock, this.roofMeta);
            this.setBlockAndMetadata(world, 6, 7, k17, this.roofBlock, this.roofMeta);
         }
      }

      for (int k1221 : new int[]{0, 15}) {
         this.setBlockAndMetadata(world, -6, 6, k1221, this.plankBlock, this.plankMeta);
         this.setBlockAndMetadata(world, -6, 7, k1221, this.plankBlock, this.plankMeta);
         this.setBlockAndMetadata(world, -6, 8, k1221, this.plankStairBlock, 1);
         this.setBlockAndMetadata(world, -5, 9, k1221, this.plankStairBlock, 1);
         this.setBlockAndMetadata(world, -4, 9, k1221, this.plankBlock, this.plankMeta);
         this.setBlockAndMetadata(world, -3, 9, k1221, this.plankBlock, this.plankMeta);
         this.setBlockAndMetadata(world, -3, 10, k1221, this.plankSlabBlock, this.plankSlabMeta);
         this.setBlockAndMetadata(world, -2, 9, k1221, this.plankSlabBlock, this.plankSlabMeta | 8);
         this.setBlockAndMetadata(world, -2, 10, k1221, this.plankBlock, this.plankMeta);

         for (int i18 = -1; i18 <= 1; i18++) {
            this.setBlockAndMetadata(world, i18, 10, k1221, this.plankBlock, this.plankMeta);
         }

         this.setBlockAndMetadata(world, 2, 9, k1221, this.plankSlabBlock, this.plankSlabMeta | 8);
         this.setBlockAndMetadata(world, 2, 10, k1221, this.plankBlock, this.plankMeta);
         this.setBlockAndMetadata(world, 3, 9, k1221, this.plankBlock, this.plankMeta);
         this.setBlockAndMetadata(world, 3, 10, k1221, this.plankSlabBlock, this.plankSlabMeta);
         this.setBlockAndMetadata(world, 4, 9, k1221, this.plankBlock, this.plankMeta);
         this.setBlockAndMetadata(world, 5, 9, k1221, this.plankStairBlock, 0);
         this.setBlockAndMetadata(world, 6, 8, k1221, this.plankStairBlock, 0);
         this.setBlockAndMetadata(world, 6, 6, k1221, this.plankBlock, this.plankMeta);
         this.setBlockAndMetadata(world, 6, 7, k1221, this.plankBlock, this.plankMeta);
      }

      for (int k122 : new int[]{-1, 16}) {
         this.setBlockAndMetadata(world, -6, 8, k122, this.plankStairBlock, 1);
         this.setBlockAndMetadata(world, -5, 9, k122, this.plankStairBlock, 1);
         this.setBlockAndMetadata(world, -4, 9, k122, this.plankSlabBlock, this.plankSlabMeta | 8);
         this.setBlockAndMetadata(world, -3, 9, k122, this.plankSlabBlock, this.plankSlabMeta | 8);
         this.setBlockAndMetadata(world, -3, 10, k122, this.plankSlabBlock, this.plankSlabMeta);
         this.setBlockAndMetadata(world, -2, 10, k122, this.plankBlock, this.plankMeta);
         this.setBlockAndMetadata(world, -1, 10, k122, this.plankSlabBlock, this.plankSlabMeta | 8);
         this.setBlockAndMetadata(world, -1, 11, k122, this.plankStairBlock, 5);
         this.setBlockAndMetadata(world, 0, 11, k122, this.plankSlabBlock, this.plankSlabMeta);
         this.setBlockAndMetadata(world, 1, 10, k122, this.plankSlabBlock, this.plankSlabMeta | 8);
         this.setBlockAndMetadata(world, 1, 11, k122, this.plankStairBlock, 4);
         this.setBlockAndMetadata(world, 2, 10, k122, this.plankBlock, this.plankMeta);
         this.setBlockAndMetadata(world, 3, 9, k122, this.plankSlabBlock, this.plankSlabMeta | 8);
         this.setBlockAndMetadata(world, 3, 10, k122, this.plankSlabBlock, this.plankSlabMeta);
         this.setBlockAndMetadata(world, 4, 9, k122, this.plankSlabBlock, this.plankSlabMeta | 8);
         this.setBlockAndMetadata(world, 5, 9, k122, this.plankStairBlock, 0);
         this.setBlockAndMetadata(world, 6, 8, k122, this.plankStairBlock, 0);
      }

      for (int k1 = 0; k1 <= 15; k1++) {
         this.setBlockAndMetadata(world, 0, 11, k1, this.plankSlabBlock, this.plankSlabMeta);
      }

      this.setBlockAndMetadata(world, -4, 1, 1, vanilla("field_150407_cf"), 0);
      this.setBlockAndMetadata(world, -3, 1, 1, vanilla("field_150407_cf"), 0);
      this.setBlockAndMetadata(world, 3, 1, 1, vanilla("field_150407_cf"), 0);
      this.setBlockAndMetadata(world, 4, 1, 1, vanilla("field_150407_cf"), 0);

      for (int j15 = 1; j15 <= 7; j15++) {
         if (j15 >= 6) {
            this.setBlockAndMetadata(world, -5, j15, 2, this.plankBlock, this.plankMeta);
            this.setBlockAndMetadata(world, 5, j15, 2, this.plankBlock, this.plankMeta);
         }

         this.setBlockAndMetadata(world, -4, j15, 2, vanilla("field_150468_ap"), 4);
         this.setBlockAndMetadata(world, 4, j15, 2, vanilla("field_150468_ap"), 5);
      }

      for (int var32 = 3; var32 <= 12; var32++) {
         int var53 = Math.floorMod(var32, 3);

         for (int var41 = -4; var41 <= 4; var41++) {
            int i2 = Math.abs(var41);
            if (var53 == 0) {
               if (i2 >= 2) {
                  this.setBlockAndMetadata(world, var41, 1, var32, this.fenceBlock, this.fenceMeta);
                  this.setBlockAndMetadata(world, var41, 2, var32, this.fenceBlock, this.fenceMeta);
               }

               if (i2 == 2) {
                  this.setBlockAndMetadata(world, var41, 3, var32, this.fenceBlock, this.fenceMeta);
                  this.setBlockAndMetadata(world, var41, 4, var32, this.fenceBlock, this.fenceMeta);
               }
            }

            if (var53 == 1) {
               if (i2 == 2) {
                  this.setBlockAndMetadata(world, var41, 1, var32, this.fenceBlock, this.fenceMeta);
               }

               if (i2 == 4) {
                  this.setBlockAndMetadata(world, var41, 1, var32, vanilla("field_150407_cf"), 0);
                  this.setBlockAndMetadata(world, var41, 2, var32, this.fenceBlock, this.fenceMeta);
               }
            }

            if (var53 == 2) {
               if (i2 == 2) {
                  this.setBlockAndMetadata(world, var41, 1, var32, this.fenceGateBlock, var41 > 0 ? 3 : 1);
               }

               if (i2 == 4) {
                  this.setBlockAndMetadata(world, var41, 1, var32, vanilla("field_150383_bp"), 3);
                  this.setBlockAndMetadata(world, var41, 2, var32, this.fenceBlock, this.fenceMeta);
               }

               if (i2 == 3) {
                  LegacyEntity animal = getRandomAnimal(world, random);
                  this.spawnNPCAndSetHome(animal, world, var41, 1, var32, 0);
                  animal.func_110177_bN();
               }
            }

            if (i2 == 4) {
               this.setBlockAndMetadata(world, var41, 3, var32, this.plankSlabBlock, this.plankSlabMeta);
            }
         }
      }

      for (int i13 = -1; i13 <= 1; i13++) {
         int hayHeight = 1 + random.nextInt(2);

         for (int j16 = 1; j16 <= hayHeight; j16++) {
            this.setBlockAndMetadata(world, i13, j16, 14, vanilla("field_150407_cf"), 0);
         }
      }

      this.placeChest(world, random, -4, 1, 13, 4, "chest_BEYOND_WALL");
      this.placeChest(world, random, -4, 1, 14, 4, "chest_BEYOND_WALL");
      this.setBlockAndMetadata(world, 4, 1, 13, vanilla("field_150462_ai"), 0);
      this.setBlockAndMetadata(world, 4, 1, 14, got("tableWildling"), 0);
      this.setBlockAndMetadata(world, -2, 3, 1, vanilla("field_150478_aa"), 3);
      this.setBlockAndMetadata(world, 2, 3, 1, vanilla("field_150478_aa"), 3);
      this.setBlockAndMetadata(world, -2, 3, 14, vanilla("field_150478_aa"), 4);
      this.setBlockAndMetadata(world, 2, 3, 14, vanilla("field_150478_aa"), 4);

      for (int var33 = 3; var33 <= 14; var33++) {
         this.setBlockAndMetadata(world, -2, 6, var33, this.fenceBlock, this.fenceMeta);
         this.setBlockAndMetadata(world, 2, 6, var33, this.fenceBlock, this.fenceMeta);
      }

      for (int var43 = -1; var43 <= 1; var43++) {
         this.setBlockAndMetadata(world, var43, 6, 3, this.fenceBlock, this.fenceMeta);
      }

      this.setBlockAndMetadata(world, -2, 6, 1, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 2, 6, 1, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, -2, 7, 1, vanilla("field_150478_aa"), 3);
      this.setBlockAndMetadata(world, 2, 7, 1, vanilla("field_150478_aa"), 3);
      this.setBlockAndMetadata(world, -2, 7, 14, vanilla("field_150478_aa"), 4);
      this.setBlockAndMetadata(world, 2, 7, 14, vanilla("field_150478_aa"), 4);

      for (int k1221 : new int[]{1, 14}) {
         for (int i18 = -4; i18 <= 4; i18++) {
            int i24 = Math.abs(i18);
            if (i24 != 2) {
               this.setBlockAndMetadata(world, i18, 8, k1221, this.plankSlabBlock, this.plankSlabMeta | 8);
            }
         }
      }

      for (int k18 = 1; k18 <= 14; k18++) {
         if (k18 != 1 && Math.floorMod(k18, 3) != 0) {
            if (k18 != 2) {
               for (int i181 : new int[]{-5, 5}) {
                  int j1 = 6;
                  if (random.nextBoolean()) {
                     int j2 = j1;
                     if (random.nextBoolean()) {
                        j2++;
                     }

                     for (int j3 = j1; j3 <= j2; j3++) {
                        this.setBlockAndMetadata(world, i181, j3, k18, vanilla("field_150407_cf"), 0);
                     }

                     if (j2 >= j1 + 1 && random.nextBoolean()) {
                        int i25 = (Math.abs(i181) - 1) * Integer.signum(i181);
                        j2 = j1;
                        if (random.nextBoolean()) {
                           j2++;
                        }

                        for (int j3 = j1; j3 <= j2; j3++) {
                           this.setBlockAndMetadata(world, i25, j3, k18, vanilla("field_150407_cf"), 0);
                        }
                     }
                  }
               }
            }
         } else {
            for (int i181 : new int[]{-5, 5}) {
               this.setBlockAndMetadata(world, i181, 6, k18, this.fenceBlock, this.fenceMeta);
               this.setBlockAndMetadata(world, i181, 7, k18, this.fenceBlock, this.fenceMeta);
            }
         }
      }

      for (int i19 = -4; i19 <= 4; i19++) {
         int i22 = Math.abs(i19);
         if (i22 != 2 && random.nextBoolean()) {
            this.setBlockAndMetadata(world, i19, 6, 1, vanilla("field_150407_cf"), 0);
         }
      }

      for (int l = 0; l <= 10; l++) {
         LegacyEntity wife = new LegacyEntity(world);
         if (random.nextBoolean()) {
            wife.getFamilyInfo().setChild();
         }

         this.spawnNPCAndSetHome(wife, world, 0, 1, 5, 16);
      }

      return true;
   }

}
