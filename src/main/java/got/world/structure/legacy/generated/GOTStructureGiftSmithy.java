package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureGiftSmithy extends LegacyNorthernContext {

   protected GOTStructureGiftSmithy(NorthStructureBuilder builder) {
      super(builder, Style.GIFT);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureGiftSmithy piece = new GOTStructureGiftSmithy(builder);
      piece.marketVariant = Math.max(0, variant);
      piece.isAbandoned = variant == 1;
      piece.isTramp = variant == 1;
      piece.isHardhome = variant == 1;
      piece.isBlacksmith = variant == 1;
      piece.generate(piece, piece.legacyRandom(), 0, 0, 0, 0);
   }


   public boolean generate(LegacyNorthernContext world, Random random, int i, int j, int k, int rotation) {
      this.setOriginAndRotation(world, i, j, k, rotation, 5);
      this.setupRandomBlocks(random);
      if (this.restrictions) {
         int minHeight = 0;
         int maxHeight = 0;

         for (int i1 = -7; i1 <= 9; i1++) {
            for (int k15 = -5; k15 <= 5; k15++) {
               int j13 = this.getTopBlock(world, i1, k15) - 1;
               if (!this.isSurface(world, i1, j13, k15)) {
                  return false;
               }

               if (j13 < minHeight) {
                  minHeight = j13;
               }

               if (j13 > maxHeight) {
                  maxHeight = j13;
               }

               if (maxHeight - minHeight > 6) {
                  return false;
               }
            }
         }
      }

      for (int i12 = -7; i12 <= 8; i12++) {
         for (int k1 = -4; k1 <= 4; k1++) {
            int k2 = Math.abs(k1);

            for (int j14 = 1; j14 <= 8; j14++) {
               this.setAir(world, i12, j14, k1);
            }

            if (i12 > 1 && (i12 != 2 || k2 != 4)) {
               if ((i12 == 2 || i12 == 8) && k2 <= 3 || i12 >= 3 && i12 <= 7 && k2 <= 4) {
                  if (i12 != 2 && i12 != 8 && k2 != 4) {
                     for (int var51 = 0; (var51 >= 0 || !this.isOpaque(world, i12, var51, k1)) && this.getY(var51) >= 0; var51--) {
                        this.setBlockAndMetadata(world, i12, var51, k1, this.plankBlock, this.plankMeta);
                        this.setGrassToDirt(world, i12, var51 - 1, k1);
                     }

                     if (random.nextInt(3) == 0) {
                        this.setBlockAndMetadata(world, i12, 1, k1, got("thatchFloor"), 0);
                     }
                  } else {
                     boolean beam = (i12 == 2 || i12 == 8) && k2 == 3;
                     if (i12 == 3 || i12 == 7) {
                        beam = true;
                     }

                     if (beam) {
                        for (int j13 = 4; (j13 >= 0 || !this.isOpaque(world, i12, j13, k1)) && this.getY(j13) >= 0; j13--) {
                           this.setBlockAndMetadata(world, i12, j13, k1, this.woodBeamBlock, this.woodBeamMeta);
                           this.setGrassToDirt(world, i12, j13 - 1, k1);
                        }
                     } else {
                        for (int j13 = 1; j13 <= 3; j13++) {
                           this.setBlockAndMetadata(world, i12, j13, k1, this.wallBlock, this.wallMeta);
                        }

                        for (int var40 = 0; (var40 >= 0 || !this.isOpaque(world, i12, var40, k1)) && this.getY(var40) >= 0; var40--) {
                           this.setBlockAndMetadata(world, i12, var40, k1, this.plankBlock, this.plankMeta);
                           this.setGrassToDirt(world, i12, var40 - 1, k1);
                        }
                     }
                  }
               }
            } else {
               for (int var50 = 0; (var50 >= 0 || !this.isOpaque(world, i12, var50, k1)) && this.getY(var50) >= 0; var50--) {
                  this.setBlockAndMetadata(world, i12, var50, k1, this.cobbleBlock, this.cobbleMeta);
                  this.setGrassToDirt(world, i12, var50 - 1, k1);
               }
            }
         }
      }

      for (int var33 = 4; var33 <= 6; var33++) {
         this.setBlockAndMetadata(world, var33, 4, -4, this.woodBeamBlock, this.woodBeamMeta | 4);
         this.setBlockAndMetadata(world, var33, 4, 4, this.woodBeamBlock, this.woodBeamMeta | 4);
      }

      for (int k13 = -2; k13 <= 2; k13++) {
         this.setBlockAndMetadata(world, 2, 4, k13, this.woodBeamBlock, this.woodBeamMeta | 8);
         this.setBlockAndMetadata(world, 8, 4, k13, this.woodBeamBlock, this.woodBeamMeta | 8);
      }

      this.setBlockAndMetadata(world, 5, 2, -4, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 5, 2, 4, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 2, 2, -2, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 2, 2, 2, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 2, 1, 0, this.doorBlock, 2);
      this.setBlockAndMetadata(world, 2, 2, 0, this.doorBlock, 8);
      this.setBlockAndMetadata(world, 3, 3, 0, vanilla("field_150478_aa"), 2);

      for (int l = 0; l <= 2; l++) {
         int j1 = 4 + l;

         for (int i1 = 2 + l; i1 <= 8 - l; i1++) {
            this.setBlockAndMetadata(world, i1, j1, -5 + l, this.roofStairBlock, 2);
            this.setBlockAndMetadata(world, i1, j1, 5 - l, this.roofStairBlock, 3);
         }

         for (int i14 : new int[]{1 + l, 9 - l}) {
            this.setBlockAndMetadata(world, i14, j1, -4 + l, this.roofStairBlock, 2);
            this.setBlockAndMetadata(world, i14, j1, 4 - l, this.roofStairBlock, 3);
         }

         for (int k16 = -3 + l; k16 <= 3 - l; k16++) {
            this.setBlockAndMetadata(world, 1 + l, j1, k16, this.roofStairBlock, 1);
            this.setBlockAndMetadata(world, 9 - l, j1, k16, this.roofStairBlock, 0);
         }

         for (int k17 : new int[]{-4 + l, 4 - l}) {
            this.setBlockAndMetadata(world, 2 + l, j1, k17, this.roofStairBlock, 1);
            this.setBlockAndMetadata(world, 8 - l, j1, k17, this.roofStairBlock, 0);
         }
      }

      for (int var44 = -1; var44 <= 1; var44++) {
         this.setBlockAndMetadata(world, 5, 7, var44, this.roofBlock, this.roofMeta);
         this.setBlockAndMetadata(world, 4, 7, var44, this.roofSlabBlock, this.roofSlabMeta);
         this.setBlockAndMetadata(world, 6, 7, var44, this.roofSlabBlock, this.roofSlabMeta);
      }

      this.setBlockAndMetadata(world, 5, 7, -2, this.roofSlabBlock, this.roofSlabMeta);
      this.setBlockAndMetadata(world, 5, 7, 2, this.roofSlabBlock, this.roofSlabMeta);

      for (int var32 = 0; var32 <= 1; var32++) {
         int j1 = 5 + var32;

         for (int i1 = 4 + var32; i1 <= 6 - var32; i1++) {
            this.setBlockAndMetadata(world, i1, j1, -3 + var32, this.roofSlabBlock, this.roofSlabMeta | 8);
            this.setBlockAndMetadata(world, i1, j1, 3 - var32, this.roofSlabBlock, this.roofSlabMeta | 8);
         }

         for (int k12 = -2 + var32; k12 <= 2 - var32; k12++) {
            this.setBlockAndMetadata(world, 3 + var32, j1, k12, this.roofSlabBlock, this.roofSlabMeta | 8);
            this.setBlockAndMetadata(world, 7 - var32, j1, k12, this.roofSlabBlock, this.roofSlabMeta | 8);
         }
      }

      for (int var34 = 7; var34 <= 9; var34++) {
         for (int k1 = -1; k1 <= 1; k1++) {
            for (int j15 = 5; (j15 >= 0 || !this.isOpaque(world, var34, j15, k1)) && this.getY(j15) >= 0; j15--) {
               this.setBlockAndMetadata(world, var34, j15, k1, this.brickBlock, this.brickMeta);
               this.setGrassToDirt(world, var34, j15 - 1, k1);
            }
         }
      }

      for (int var45 = -1; var45 <= 1; var45++) {
         this.setBlockAndMetadata(world, 9, 5, var45, this.brickStairBlock, 0);
      }

      this.setBlockAndMetadata(world, 8, 5, -1, this.brickStairBlock, 2);
      this.setBlockAndMetadata(world, 8, 5, 1, this.brickStairBlock, 3);

      for (int j16 = 6; j16 <= 7; j16++) {
         this.setBlockAndMetadata(world, 8, j16, 0, this.brickBlock, this.brickMeta);
      }

      this.setBlockAndMetadata(world, 8, 8, 0, this.brickWallBlock, this.brickWallMeta);

      for (int var46 = -3; var46 <= 3; var46++) {
         this.setBlockAndMetadata(world, 5, 4, var46, this.woodBeamBlock, this.woodBeamMeta | 8);
      }

      for (int i15 : new int[]{3, 7}) {
         for (int k18 : new int[]{-3, 3}) {
            this.setBlockAndMetadata(world, i15, 1, k18, this.plankBlock, this.plankMeta);

            for (int j17 = 2; j17 <= 4; j17++) {
               this.setBlockAndMetadata(world, i15, j17, k18, this.fenceBlock, this.fenceMeta);
            }
         }
      }

      this.setBlockAndMetadata(world, 3, 1, -2, this.plankBlock, this.plankMeta);
      this.placePlate(world, random, 3, 2, -2, this.plateBlock, "food_DEFAULT");
      this.setBlockAndMetadata(world, 4, 1, -3, this.plankBlock, this.plankMeta);
      this.placePlate(world, random, 4, 2, -3, this.plateBlock, "food_DEFAULT");
      this.setBlockAndMetadata(world, 5, 1, -3, vanilla("field_150462_ai"), 0);
      this.setBlockAndMetadata(world, 6, 1, -3, this.plankBlock, this.plankMeta);
      this.placeMug(world, random, 6, 2, -3, 2, "food_DEFAULT_DRINK");
      this.setBlockAndMetadata(world, 7, 1, -2, this.plankBlock, this.plankMeta);
      this.placeBarrel(world, random, 7, 2, -2, 5, "food_DEFAULT_DRINK");
      this.setBlockAndMetadata(world, 3, 1, 2, this.plankBlock, this.plankMeta);
      this.placeMug(world, random, 3, 2, 2, 3, "food_DEFAULT_DRINK");
      this.setBlockAndMetadata(world, 5, 1, 3, this.bedBlock, 1);
      this.setBlockAndMetadata(world, 6, 1, 3, this.bedBlock, 9);
      this.placeChest(world, random, 7, 1, 2, 5, "chest_GIFT");
      this.setBlockAndMetadata(world, 8, 0, 0, got("hearth"), 0);
      this.setBlockAndMetadata(world, 8, 1, 0, vanilla("field_150480_ab"), 0);

      for (int j12 = 2; j12 <= 3; j12++) {
         this.setAir(world, 8, j12, 0);
      }

      this.setBlockAndMetadata(world, 7, 1, 0, this.barsBlock, 0);
      this.setBlockAndMetadata(world, 7, 2, 0, vanilla("field_150460_al"), 5);
      this.spawnItemFrame(world, 7, 3, 0, 3, getRandFrameItem(random));
      this.placeChest(world, random, 1, 1, 2, 5, "chest_GIFT");
      this.setBlockAndMetadata(world, 1, 1, -2, this.tableBlock, 0);
      this.setBlockAndMetadata(world, 1, 1, -3, vanilla("field_150462_ai"), 0);

      for (int var35 = 1; var35 <= 3; var35++) {
         for (int i13 = -6; i13 <= -3; i13++) {
            for (int k12 = 0; k12 <= 3; k12++) {
               this.setBlockAndMetadata(world, i13, var35, k12, this.brickBlock, this.brickMeta);
            }
         }

         this.setBlockAndMetadata(world, -2, var35, 3, this.brickBlock, this.brickMeta);
      }

      for (int var36 = 1; var36 <= 3; var36++) {
         this.setBlockAndMetadata(world, -6, var36, -3, this.fenceBlock, this.fenceMeta);
         this.setBlockAndMetadata(world, -2, var36, -3, this.fenceBlock, this.fenceMeta);
      }

      for (int l2 = 0; l2 <= 1; l2++) {
         int j1 = 4 + l2;

         for (int i1 = -6 + l2; i1 <= -2 - l2; i1++) {
            this.setBlockAndMetadata(world, i1, j1, -3 + l2, this.brickStairBlock, 2);
            this.setBlockAndMetadata(world, i1, j1, 3 - l2, this.brickStairBlock, 3);
         }

         for (int k12 = -2 + l2; k12 <= 2 - l2; k12++) {
            this.setBlockAndMetadata(world, -6 + l2, j1, k12, this.brickStairBlock, 1);
            this.setBlockAndMetadata(world, -2 - l2, j1, k12, this.brickStairBlock, 0);
         }
      }

      for (int k14 = -2; k14 <= 2; k14++) {
         for (int i13 = -5; i13 <= -3; i13++) {
            this.setBlockAndMetadata(world, i13, 4, k14, this.brickBlock, this.brickMeta);
         }
      }

      for (int var47 = -1; var47 <= 1; var47++) {
         this.setBlockAndMetadata(world, -4, 5, var47, this.brickBlock, this.brickMeta);
      }

      this.setBlockAndMetadata(world, -4, 1, 0, vanilla("field_150460_al"), 2);
      this.setBlockAndMetadata(world, -3, 1, 0, this.barsBlock, 0);
      this.setBlockAndMetadata(world, -3, 1, 1, got("alloyForge"), 4);
      this.setBlockAndMetadata(world, -4, 2, 0, this.barsBlock, 0);
      this.setBlockAndMetadata(world, -3, 2, 0, this.barsBlock, 0);
      this.setBlockAndMetadata(world, -3, 2, 1, this.barsBlock, 0);
      this.setBlockAndMetadata(world, -4, 1, 1, vanilla("field_150353_l"), 0);

      for (int var37 = 2; var37 <= 5; var37++) {
         this.setAir(world, -4, var37, 1);
      }

      this.setBlockAndMetadata(world, -2, 1, 2, vanilla("field_150383_bp"), 3);
      this.setBlockAndMetadata(world, -5, 1, -1, got("unsmeltery"), 4);
      this.setBlockAndMetadata(world, -5, 1, -3, vanilla("field_150467_bQ"), 1);
      this.setBlockAndMetadata(world, -3, 1, -3, vanilla("field_150467_bQ"), 1);
      LegacyEntity blacksmith = new LegacyEntity(world);
      this.spawnNPCAndSetHome(blacksmith, world, 0, 1, 0, 8);
      return true;
   }

}
