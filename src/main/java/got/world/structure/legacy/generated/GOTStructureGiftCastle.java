package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureGiftCastle extends LegacyNorthernContext {

   protected GOTStructureGiftCastle(NorthStructureBuilder builder) {
      super(builder, Style.GIFT);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureGiftCastle piece = new GOTStructureGiftCastle(builder);
      piece.marketVariant = Math.max(0, variant);
      piece.isAbandoned = variant == 1;
      piece.isTramp = variant == 1;
      piece.isHardhome = variant == 1;
      piece.isBlacksmith = variant == 1;
      piece.generate(piece, piece.legacyRandom(), 0, 0, 0, 0);
   }


   public boolean generate(LegacyNorthernContext world, Random random, int i, int j, int k, int rotation) {
      this.setOriginAndRotation(world, i, j, k, rotation, 13);
      this.setupRandomBlocks(random);
      if (this.restrictions) {
         for (int i15 = -12; i15 <= 12; i15++) {
            for (int k12 = -12; k12 <= 12; k12++) {
               int j12 = this.getTopBlock(world, i15, k12) - 1;
               if (!this.isSurface(world, i15, j12, k12)) {
                  return false;
               }
            }
         }
      }

      for (int i15 = -12; i15 <= 12; i15++) {
         for (int k12 = -12; k12 <= 12; k12++) {
            int i2 = Math.abs(i15);
            int k2 = Math.abs(k12);

            for (int j14 = 1; j14 <= 10; j14++) {
               this.setAir(world, i15, j14, k12);
            }

            for (int var69 = 0; (var69 >= 0 || !this.isOpaque(world, i15, var69, k12)) && this.getY(var69) >= 0; var69--) {
               if ((i2 != 12 || k2 != 12 && k2 != 9 && k2 != 2) && (k2 != 12 || i2 != 9 && i2 != 2)) {
                  if (i2 > 9 || k2 > 9) {
                     this.setBlockAndMetadata(world, i15, var69, k12, this.plankBlock, this.plankMeta);
                  } else if (var69 == 0) {
                     int randomGround = random.nextInt(3);
                     switch (randomGround) {
                        case 0:
                           this.setBlockAndMetadata(world, i15, 0, k12, vanilla("field_150349_c"), 0);
                           break;
                        case 1:
                           this.setBlockAndMetadata(world, i15, 0, k12, vanilla("field_150346_d"), 1);
                           break;
                        case 2:
                           this.setBlockAndMetadata(world, i15, 0, k12, got("dirtPath"), 0);
                     }

                     if (random.nextInt(3) == 0) {
                        this.setBlockAndMetadata(world, i15, 1, k12, got("thatchFloor"), 0);
                     }
                  } else {
                     this.setBlockAndMetadata(world, i15, var69, k12, vanilla("field_150346_d"), 0);
                  }
               } else {
                  this.setBlockAndMetadata(world, i15, var69, k12, this.woodBeamBlock, this.woodBeamMeta);
               }

               this.setGrassToDirt(world, i15, var69 - 1, k12);
            }
         }
      }

      for (int var65 = -12; var65 <= 12; var65++) {
         for (int k12 = -12; k12 <= 12; k12++) {
            int i2 = Math.abs(var65);
            int k2 = Math.abs(k12);
            int yBoost = 0;
            if (k12 < 8 && i2 < 7) {
               yBoost = 1;
            }

            if (i2 != 9 && i2 != 12 || k2 != 9 && k2 != 12) {
               if (i2 != 12 && k2 != 12 || k2 != 2 && i2 != 2) {
                  if (i2 != 12 && k2 != 12) {
                     if (i2 <= 9 && k2 <= 9) {
                        if (i2 == 9 || k2 == 9) {
                           this.setBlockAndMetadata(world, var65, 5 + yBoost, k12, this.fenceBlock, this.fenceMeta);
                           if ((i2 == 9 && Math.floorMod(k12, 3) == 0 || k2 == 9 && Math.floorMod(var65, 3) == 0) && !this.isAbandoned) {
                              this.setBlockAndMetadata(world, var65, 6 + yBoost, k12, vanilla("field_150478_aa"), 5);
                           }

                           if (k12 == -9) {
                              this.setBlockAndMetadata(world, var65, 4 + yBoost, -9, this.plankStairBlock, 7);
                           } else if (k12 == 9) {
                              this.setBlockAndMetadata(world, var65, 4 + yBoost, 9, this.plankStairBlock, 6);
                           } else if (var65 == -9) {
                              this.setBlockAndMetadata(world, -9, 4 + yBoost, k12, this.plankStairBlock, 4);
                           } else if (var65 == 9) {
                              this.setBlockAndMetadata(world, var65, 4 + yBoost, k12, this.plankStairBlock, 5);
                           }
                        }
                     } else {
                        for (int j15 = 1; j15 <= 4 + yBoost; j15++) {
                           this.setBlockAndMetadata(world, var65, j15, k12, this.plankBlock, this.plankMeta);
                        }
                     }
                  } else {
                     for (int j15 = 1; j15 <= 5 + yBoost; j15++) {
                        this.setBlockAndMetadata(world, var65, j15, k12, this.plankBlock, this.plankMeta);
                     }

                     if ((i2 != 12 || k2 < 10 || k2 > 11) && (k2 != 12 || i2 < 10 || i2 > 11)) {
                        if (Math.floorMod(i2 + k2, 2) == 0) {
                           this.setBlockAndMetadata(world, var65, 6 + yBoost, k12, this.plankBlock, this.plankMeta);
                        } else {
                           this.setBlockAndMetadata(world, var65, 6 + yBoost, k12, this.plankSlabBlock, this.plankSlabMeta);
                        }
                     } else {
                        this.setBlockAndMetadata(world, var65, 5 + yBoost, k12, this.fenceBlock, this.fenceMeta);
                     }
                  }
               } else {
                  for (int j15 = 1; j15 <= 6 + yBoost; j15++) {
                     this.setBlockAndMetadata(world, var65, j15, k12, this.woodBeamBlock, this.woodBeamMeta);
                  }
               }
            } else {
               for (int j15 = 1; j15 <= 8; j15++) {
                  this.setBlockAndMetadata(world, var65, j15, k12, this.woodBeamBlock, this.woodBeamMeta);
               }
            }
         }
      }

      for (int i13 : new int[]{-12, 9}) {
         for (int k14 : new int[]{-12, 9}) {
            this.setBlockAndMetadata(world, i13 + 1, 8, k14, this.plankStairBlock, 4);
            this.setBlockAndMetadata(world, i13 + 2, 8, k14, this.plankStairBlock, 5);
            this.setBlockAndMetadata(world, i13 + 1, 8, k14 + 3, this.plankStairBlock, 4);
            this.setBlockAndMetadata(world, i13 + 2, 8, k14 + 3, this.plankStairBlock, 5);
            this.setBlockAndMetadata(world, i13, 8, k14 + 1, this.plankStairBlock, 7);
            this.setBlockAndMetadata(world, i13, 8, k14 + 2, this.plankStairBlock, 6);
            this.setBlockAndMetadata(world, i13 + 3, 8, k14 + 1, this.plankStairBlock, 7);
            this.setBlockAndMetadata(world, i13 + 3, 8, k14 + 2, this.plankStairBlock, 6);

            for (int i22 = i13; i22 <= i13 + 3; i22++) {
               this.setBlockAndMetadata(world, i22, 9, k14 - 1, this.roofSlabBlock, this.roofSlabMeta);
               this.setBlockAndMetadata(world, i22, 9, k14 + 4, this.roofSlabBlock, this.roofSlabMeta);
            }

            for (int k22 = k14; k22 <= k14 + 3; k22++) {
               this.setBlockAndMetadata(world, i13 - 1, 9, k22, this.roofSlabBlock, this.roofSlabMeta);
               this.setBlockAndMetadata(world, i13 + 4, 9, k22, this.roofSlabBlock, this.roofSlabMeta);
            }

            for (int var93 = i13; var93 <= i13 + 3; var93++) {
               for (int k23 = k14; k23 <= k14 + 3; k23++) {
                  if (var93 >= i13 + 1 && var93 <= i13 + 2 && k23 >= k14 + 1 && k23 <= k14 + 2) {
                     this.setBlockAndMetadata(world, var93, 9, k23, this.roofSlabBlock, this.roofSlabMeta | 8);
                     this.setBlockAndMetadata(world, var93, 10, k23, this.roofSlabBlock, this.roofSlabMeta);
                  } else {
                     this.setBlockAndMetadata(world, var93, 9, k23, this.roofBlock, this.roofMeta);
                  }
               }
            }
         }
      }

      for (int k1 = -12; k1 <= 12; k1++) {
         int k24 = Math.abs(k1);
         if (k24 >= 10 && k24 <= 11 || k24 >= 3 && k24 <= 8) {
            this.setBlockAndMetadata(world, -12, 1, k1, this.plankStairBlock, 1);

            for (int j12 = 2; j12 <= 3; j12++) {
               this.setAir(world, -12, j12, k1);
            }

            this.setBlockAndMetadata(world, -12, 4, k1, this.plankStairBlock, 5);
            this.setBlockAndMetadata(world, 12, 1, k1, this.plankStairBlock, 0);

            for (int var57 = 2; var57 <= 3; var57++) {
               this.setAir(world, 12, var57, k1);
            }

            this.setBlockAndMetadata(world, 12, 4, k1, this.plankStairBlock, 4);
         }

         if (k24 == 12 && k1 > 0) {
            for (int i12 = -1; i12 <= 1; i12++) {
               this.setBlockAndMetadata(world, i12, 1, k1, this.plankBlock, this.plankMeta);
               this.setBlockAndMetadata(world, i12, 4, k1, this.plankBlock, this.plankMeta);
               this.setBlockAndMetadata(world, i12, 5, k1, this.woodBeamBlock, this.woodBeamMeta | 4);
               this.setBlockAndMetadata(world, i12, 6, k1, this.fenceBlock, this.fenceMeta);
               this.setBlockAndMetadata(world, i12, 5, k1 - Integer.signum(k1), this.plankSlabBlock, this.plankSlabMeta);
            }

            this.setBlockAndMetadata(world, -2, 7, k1, this.plankSlabBlock, this.plankSlabMeta);
            this.setBlockAndMetadata(world, 2, 7, k1, this.plankSlabBlock, this.plankSlabMeta);
            this.setBlockAndMetadata(world, -1, 2, k1, this.plankStairBlock, 0);
            this.setAir(world, 0, 2, k1);
            this.setBlockAndMetadata(world, 1, 2, k1, this.plankStairBlock, 1);
            this.setBlockAndMetadata(world, -1, 3, k1, this.plankStairBlock, 4);
            this.setAir(world, 0, 3, k1);
            this.setBlockAndMetadata(world, 1, 3, k1, this.plankStairBlock, 5);
         }
      }

      for (int i14 = -12; i14 <= 12; i14++) {
         int i23 = Math.abs(i14);
         if (i23 >= 10 && i23 <= 11 || i23 >= 3 && i23 <= 8) {
            this.setBlockAndMetadata(world, i14, 1, -12, this.plankStairBlock, 2);

            for (int j12 = 2; j12 <= 3; j12++) {
               this.setAir(world, i14, j12, -12);
            }

            this.setBlockAndMetadata(world, i14, 4, -12, this.plankStairBlock, 6);
            this.setBlockAndMetadata(world, i14, 1, 12, this.plankStairBlock, 3);

            for (int var59 = 2; var59 <= 3; var59++) {
               this.setAir(world, i14, var59, 12);
            }

            this.setBlockAndMetadata(world, i14, 4, 12, this.plankStairBlock, 7);
         }

         if (i23 == 12) {
            for (int k13 = -1; k13 <= 1; k13++) {
               this.setBlockAndMetadata(world, i14, 1, k13, this.plankBlock, this.plankMeta);
               this.setBlockAndMetadata(world, i14, 4, k13, this.plankBlock, this.plankMeta);
               this.setBlockAndMetadata(world, i14, 5, k13, this.woodBeamBlock, this.woodBeamMeta | 8);
               this.setBlockAndMetadata(world, i14, 6, k13, this.fenceBlock, this.fenceMeta);
               this.setBlockAndMetadata(world, i14 - Integer.signum(i14), 5, k13, this.plankSlabBlock, this.plankSlabMeta);
            }

            this.setBlockAndMetadata(world, i14, 7, -2, this.plankSlabBlock, this.plankSlabMeta);
            this.setBlockAndMetadata(world, i14, 7, 2, this.plankSlabBlock, this.plankSlabMeta);
            this.setBlockAndMetadata(world, i14, 2, -1, this.plankStairBlock, 3);
            this.setAir(world, i14, 2, 0);
            this.setBlockAndMetadata(world, i14, 2, 1, this.plankStairBlock, 2);
            this.setBlockAndMetadata(world, i14, 3, -1, this.plankStairBlock, 7);
            this.setAir(world, i14, 3, 0);
            this.setBlockAndMetadata(world, i14, 3, 1, this.plankStairBlock, 6);
         }
      }

      for (int var27 = -11; var27 <= -10; var27++) {
         this.setAir(world, -6, 5, var27);
         this.setBlockAndMetadata(world, -5, 5, var27, this.plankStairBlock, 1);
         this.setBlockAndMetadata(world, 5, 5, var27, this.plankStairBlock, 0);
         this.setAir(world, 6, 5, var27);
      }

      for (int j1 = 4; j1 <= 7; j1++) {
         this.setBlockAndMetadata(world, -6, j1, -9, this.woodBeamBlock, this.woodBeamMeta);
         this.setBlockAndMetadata(world, 6, j1, -9, this.woodBeamBlock, this.woodBeamMeta);
      }

      if (!this.isAbandoned) {
         this.setBlockAndMetadata(world, -6, 8, -9, vanilla("field_150478_aa"), 5);
         this.setBlockAndMetadata(world, 6, 8, -9, vanilla("field_150478_aa"), 5);
      }

      for (int var28 = -12; var28 <= -10; var28++) {
         for (int j13 = 1; j13 <= 4; j13++) {
            for (int i12 = -1; i12 <= 1; i12++) {
               this.setAir(world, i12, j13, var28);
            }

            this.setBlockAndMetadata(world, -2, j13, var28, this.woodBeamBlock, this.woodBeamMeta);
            this.setBlockAndMetadata(world, 2, j13, var28, this.woodBeamBlock, this.woodBeamMeta);
         }

         for (int i1 = -1; i1 <= 1; i1++) {
            this.setBlockAndMetadata(world, i1, 4, var28, this.woodBeamBlock, this.woodBeamMeta | 4);
         }
      }

      for (int var53 = 1; var53 <= 3; var53++) {
         for (int i1 = -1; i1 <= 1; i1++) {
            this.setBlockAndMetadata(world, i1, var53, -11, this.gateBlock, 3);
         }
      }

      for (int var63 = -1; var63 <= 1; var63++) {
         this.setBlockAndMetadata(world, var63, 4, -12, this.plankStairBlock, 6);
         this.setBlockAndMetadata(world, var63, 5, -12, this.woodBeamBlock, this.woodBeamMeta | 4);
         this.setBlockAndMetadata(world, var63, 6, -12, this.woodBeamBlock, this.woodBeamMeta | 4);
         this.setBlockAndMetadata(world, var63, 6, -11, this.plankSlabBlock, this.plankSlabMeta);
      }

      for (int var54 = 6; var54 <= 8; var54++) {
         this.setBlockAndMetadata(world, 0, var54, -12, this.woodBeamBlock, this.woodBeamMeta);
      }

      this.setBlockAndMetadata(world, -2, 8, -12, this.plankStairBlock, 1);
      this.setBlockAndMetadata(world, 0, 9, -12, this.plankSlabBlock, this.plankSlabMeta);
      this.setBlockAndMetadata(world, 2, 8, -12, this.plankStairBlock, 0);
      this.setBlockAndMetadata(world, -1, 7, -12, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 1, 7, -12, this.fenceBlock, this.fenceMeta);
      if (!this.isAbandoned) {
         this.placeWallBanner(world, -2, 6, -12, this.bannerType, 2);
         this.placeWallBanner(world, 0, 7, -12, this.bannerType, 2);
         this.placeWallBanner(world, 2, 6, -12, this.bannerType, 2);
         this.setBlockAndMetadata(world, -2, 3, -13, vanilla("field_150478_aa"), 4);
         this.setBlockAndMetadata(world, 2, 3, -13, vanilla("field_150478_aa"), 4);
         this.setBlockAndMetadata(world, -2, 3, -9, vanilla("field_150478_aa"), 3);
         this.setBlockAndMetadata(world, 2, 3, -9, vanilla("field_150478_aa"), 3);
      }

      for (int var29 = -13; var29 <= 9; var29++) {
         for (int i1 = -1; i1 <= 1; i1++) {
            for (int j12 = 0; (j12 >= 0 || !this.isOpaque(world, i1, j12, var29)) && this.getY(j12) >= 0; j12--) {
               this.setBlockAndMetadata(world, i1, j12, var29, this.brickBlock, this.brickMeta);
               this.setGrassToDirt(world, i1, j12 - 1, var29);
            }
         }

         if (var29 > -10) {
            this.setBlockAndMetadata(world, -2, 0, var29, this.brickBlock, this.brickMeta);
            this.setBlockAndMetadata(world, 2, 0, var29, this.brickBlock, this.brickMeta);
            if (Math.floorMod(var29, 4) == 2) {
               this.setBlockAndMetadata(world, -2, 1, var29, this.brickWallBlock, this.brickWallMeta);
               if (!this.isAbandoned) {
                  this.setBlockAndMetadata(world, -2, 2, var29, vanilla("field_150478_aa"), 5);
               }

               this.setBlockAndMetadata(world, 2, 1, var29, this.brickWallBlock, this.brickWallMeta);
               if (!this.isAbandoned) {
                  this.setBlockAndMetadata(world, 2, 2, var29, vanilla("field_150478_aa"), 5);
               }
            }
         }
      }

      for (int var55 = 1; var55 <= 3; var55++) {
         this.setBlockAndMetadata(world, -2, var55, 10, this.woodBeamBlock, this.woodBeamMeta);
         this.setBlockAndMetadata(world, 2, var55, 10, this.woodBeamBlock, this.woodBeamMeta);
      }

      if (!this.isAbandoned) {
         this.setBlockAndMetadata(world, -2, 3, 9, vanilla("field_150478_aa"), 4);
         this.setBlockAndMetadata(world, 2, 3, 9, vanilla("field_150478_aa"), 4);
      }

      for (int i13 : new int[]{-7, 7}) {
         this.setBlockAndMetadata(world, i13, 1, 0, this.fenceBlock, this.fenceMeta);
         this.setBlockAndMetadata(world, i13, 2, 0, this.fenceBlock, this.fenceMeta);
         if (!this.isAbandoned) {
            this.setBlockAndMetadata(world, i13, 3, 0, vanilla("field_150478_aa"), 5);

            for (int l = 0; l < 2; l++) {
               LegacyEntity horse = new LegacyEntity(world);
               this.spawnNPCAndSetHome(horse, world, i13 - Integer.signum(i13) * 3, 1, 0, 0);
               horse.func_110214_p(0);
               horse.saddleMountForWorldGen();
               horse.func_110177_bN();
               this.leashEntityTo(horse, world, i13, 2, 0);
            }
         }
      }

      for (int k15 = -9; k15 <= -5; k15++) {
         for (int var34 = -9; var34 <= -5; var34++) {
            this.setBlockAndMetadata(world, var34, 3, k15, this.plankSlabBlock, this.plankSlabMeta);
         }
      }

      this.setBlockAndMetadata(world, -9, 3, -9, this.plankBlock, this.plankMeta);
      this.setBlockAndMetadata(world, -6, 3, -9, this.plankBlock, this.plankMeta);

      for (int j17 = 1; j17 <= 2; j17++) {
         if (j17 == 1) {
            this.setBlockAndMetadata(world, -7, 1, -9, vanilla("field_150460_al"), 3);
            this.setBlockAndMetadata(world, -9, 1, -7, vanilla("field_150460_al"), 4);
         } else {
            this.setBlockAndMetadata(world, -7, j17, -9, got("alloyForge"), 3);
            this.setBlockAndMetadata(world, -9, j17, -7, got("alloyForge"), 4);
         }

         this.setBlockAndMetadata(world, -8, j17, -9, this.plankBlock, this.plankMeta);
         this.setBlockAndMetadata(world, -9, j17, -9, this.plankBlock, this.plankMeta);
         this.setBlockAndMetadata(world, -9, j17, -8, this.plankBlock, this.plankMeta);
         this.setBlockAndMetadata(world, -5, j17, -5, this.fenceBlock, this.fenceMeta);
      }

      this.setBlockAndMetadata(world, -5, 1, -9, this.plankBlock, this.plankMeta);
      this.setBlockAndMetadata(world, -5, 2, -9, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, -6, 1, -9, this.plankBlock, this.plankMeta);
      if (!this.isAbandoned) {
         this.setBlockAndMetadata(world, -6, 2, -9, vanilla("field_150478_aa"), 3);
      }

      this.setBlockAndMetadata(world, -9, 1, -6, this.plankBlock, this.plankMeta);
      if (!this.isAbandoned) {
         this.setBlockAndMetadata(world, -9, 2, -6, vanilla("field_150478_aa"), 2);
      }

      this.setBlockAndMetadata(world, -9, 1, -5, this.plankBlock, this.plankMeta);
      this.setBlockAndMetadata(world, -9, 2, -5, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, -6, 1, -5, vanilla("field_150467_bQ"), 1);
      this.setBlockAndMetadata(world, -5, 1, -6, vanilla("field_150383_bp"), 3);
      if (!this.isAbandoned) {
         LegacyEntity blacksmith = new LegacyEntity(world);
         this.spawnNPCAndSetHome(blacksmith, world, -4, 1, -4, 8);
      }

      for (int var47 = 5; var47 <= 9; var47++) {
         for (int i12 = -9; i12 <= -5; i12++) {
            this.setBlockAndMetadata(world, i12, 3, var47, this.plankSlabBlock, this.plankSlabMeta);
         }
      }

      this.setBlockAndMetadata(world, -9, 3, 9, this.plankBlock, this.plankMeta);

      for (int j13 = 1; j13 <= 2; j13++) {
         this.setBlockAndMetadata(world, -9, j13, 9, this.plankBlock, this.plankMeta);
         this.setBlockAndMetadata(world, -5, j13, 5, this.fenceBlock, this.fenceMeta);
      }

      this.setBlockAndMetadata(world, -5, 1, 9, this.plankBlock, this.plankMeta);
      this.setBlockAndMetadata(world, -5, 2, 9, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, -6, 1, 9, this.plankBlock, this.plankMeta);
      if (this.isAbandoned) {
         this.placeChest(world, random, -7, 1, 9, 2, "chest_TREASURE");
         this.placeChest(world, random, -8, 1, 9, 2, "chest_TREASURE");
      } else {
         this.setBlockAndMetadata(world, -6, 2, 9, vanilla("field_150478_aa"), 4);
         this.placeChest(world, random, -7, 1, 9, 2, "chest_GIFT");
         this.placeChest(world, random, -8, 1, 9, 2, "chest_GIFT");
      }

      this.setBlockAndMetadata(world, -9, 1, 8, vanilla("field_150462_ai"), 0);
      this.setBlockAndMetadata(world, -9, 1, 7, this.tableBlock, 0);
      this.setBlockAndMetadata(world, -9, 1, 6, this.plankBlock, this.plankMeta);
      if (!this.isAbandoned) {
         this.setBlockAndMetadata(world, -9, 2, 6, vanilla("field_150478_aa"), 2);
      }

      this.setBlockAndMetadata(world, -9, 1, 5, this.plankBlock, this.plankMeta);
      this.setBlockAndMetadata(world, -9, 2, 5, this.fenceBlock, this.fenceMeta);

      for (int var48 = 5; var48 <= 10; var48++) {
         for (int i12 = 5; i12 <= 10; i12++) {
            this.setBlockAndMetadata(world, i12, 0, var48, this.plankBlock, this.plankMeta);
            this.setAir(world, i12, 1, var48);
            this.setAir(world, i12, 2, var48);
            this.setBlockAndMetadata(world, i12, 3, var48, this.plankBlock, this.plankMeta);
         }
      }

      for (int var49 = 4; var49 <= 9; var49++) {
         this.setBlockAndMetadata(world, 4, 3, var49, this.plankStairBlock, 1);
      }

      for (int var35 = 5; var35 <= 9; var35++) {
         this.setBlockAndMetadata(world, var35, 3, 4, this.plankStairBlock, 2);
      }

      for (int var50 = 5; var50 <= 10; var50++) {
         this.setBlockAndMetadata(world, 5, 1, var50, this.plankBlock, this.plankMeta);
         this.setBlockAndMetadata(world, 5, 2, var50, this.plankBlock, this.plankMeta);
      }

      for (int var36 = 6; var36 <= 10; var36++) {
         this.setBlockAndMetadata(world, var36, 1, 5, this.plankBlock, this.plankMeta);
         this.setBlockAndMetadata(world, var36, 2, 5, this.plankBlock, this.plankMeta);
      }

      this.setBlockAndMetadata(world, 5, 0, 8, this.plankBlock, this.plankMeta);
      this.setBlockAndMetadata(world, 5, 1, 8, this.doorBlock, 2);
      this.setBlockAndMetadata(world, 5, 2, 8, this.doorBlock, 8);
      this.setBlockAndMetadata(world, 5, 1, 5, this.woodBeamBlock, this.woodBeamMeta);
      this.setBlockAndMetadata(world, 5, 2, 5, this.woodBeamBlock, this.woodBeamMeta);

      for (int var37 = 6; var37 <= 10; var37++) {
         if (Math.floorMod(var37, 2) == 0 && !this.isAbandoned) {
            this.setBlockAndMetadata(world, var37, 2, 6, vanilla("field_150478_aa"), 3);
            this.setBlockAndMetadata(world, var37, 2, 10, vanilla("field_150478_aa"), 4);
         }

         for (int k13 = 6; k13 <= 10; k13++) {
            if (random.nextBoolean()) {
               this.setBlockAndMetadata(world, var37, 1, k13, got("thatchFloor"), 0);
            }
         }
      }

      for (int k16 : new int[]{6, 10}) {
         this.setBlockAndMetadata(world, 7, 1, k16, this.bedBlock, 3);
         this.setBlockAndMetadata(world, 6, 1, k16, this.bedBlock, 11);
         this.setBlockAndMetadata(world, 9, 1, k16, this.bedBlock, 1);
         this.setBlockAndMetadata(world, 10, 1, k16, this.bedBlock, 9);
      }

      if (this.isAbandoned) {
         this.placeChest(world, random, 8, 1, 6, 3, "chest_TREASURE");
         this.placeChest(world, random, 8, 1, 10, 2, "chest_TREASURE");
      } else {
         this.placeChest(world, random, 8, 1, 6, 3, "chest_GIFT");
         this.placeChest(world, random, 8, 1, 10, 2, "chest_GIFT");
      }

      this.setBlockAndMetadata(world, 10, 1, 8, this.plankBlock, this.plankMeta);
      if (!this.isAbandoned) {
         this.placeBarrel(world, random, 10, 2, 8, 5, "food_DEFAULT_DRINK");
      }

      for (int j18 = 1; j18 <= 4; j18++) {
         this.setBlockAndMetadata(world, 6, j18, -9, this.woodBeamBlock, this.woodBeamMeta);
         this.setBlockAndMetadata(world, 7, j18, -9, this.plankBlock, this.plankMeta);
         this.setBlockAndMetadata(world, 8, j18, -9, this.plankBlock, this.plankMeta);
         this.setBlockAndMetadata(world, 9, j18, -9, this.woodBeamBlock, this.woodBeamMeta);
      }

      for (int var30 = -8; var30 <= -7; var30++) {
         for (int i12 = 6; i12 <= 9; i12++) {
            int stairHeight = i12 - 5;

            for (int j19 = 0; j19 < stairHeight; j19++) {
               this.setBlockAndMetadata(world, i12, j19, var30, this.plankBlock, this.plankMeta);
            }

            this.setBlockAndMetadata(world, i12, stairHeight, var30, this.plankStairBlock, 1);
         }

         this.setAir(world, 9, 5, var30);
      }

      if (!this.isAbandoned) {
         this.placeWallBanner(world, -10, 3, 0, this.bannerType, 1);
         this.placeWallBanner(world, 10, 3, 0, this.bannerType, 3);
      }

      for (int var38 = -1; var38 <= 1; var38++) {
         this.setBlockAndMetadata(world, var38, 0, 10, this.brickBlock, this.brickMeta);

         for (int var62 = 1; var62 <= 3; var62++) {
            this.setAir(world, var38, var62, 10);
         }
      }

      if (!this.isAbandoned) {
         this.setBlockAndMetadata(world, 0, 1, 9, got("commandTable"), 0);
         this.placeWallBanner(world, 0, 3, 11, this.bannerType, 2);
         this.spawnLegendaryMobs(world);

         for (int l = 0; l < 8; l++) {
            this.spawnNPCAndSetHome(new LegacyEntity(world), world, 0, 1, 0, 20);
         }

         LegacyEntity respawner = new LegacyEntity(world);
         respawner.setSpawnClass1(LegacyEntity.class);
         respawner.setCheckRanges(16, -8, 10, 12);
         respawner.setSpawnRanges(11, 1, 6, 20);
         this.placeNPCRespawner(respawner, world, 0, 0, 0);
      }

      return true;
   }

}
