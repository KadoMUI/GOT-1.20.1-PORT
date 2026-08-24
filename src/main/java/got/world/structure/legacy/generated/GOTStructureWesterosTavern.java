package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureWesterosTavern extends LegacyNorthernContext {

   protected GOTStructureWesterosTavern(NorthStructureBuilder builder) {
      super(builder, Style.NORTH);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureWesterosTavern piece = new GOTStructureWesterosTavern(builder);
      piece.marketVariant = Math.max(0, variant);
      piece.isAbandoned = variant == 1;
      piece.isTramp = variant == 1;
      piece.isHardhome = variant == 1;
      piece.isBlacksmith = variant == 1;
      piece.generate(piece, piece.legacyRandom(), 0, 0, 0, 0);
   }


   protected boolean isKingsLanding;
   protected boolean isCrossroads;
   private String[] tavernNameSign;

   public boolean generate(LegacyNorthernContext world, Random random, int i, int j, int k, int rotation) {
      this.setOriginAndRotation(world, i, j, k, rotation, 1);
      this.setupRandomBlocks(random);
      if (this.restrictions) {
         int minHeight = 0;
         int maxHeight = 0;

         for (int i12 = -9; i12 <= 13; i12++) {
            for (int k142 = -2; k142 <= 16; k142++) {
               int j13 = this.getTopBlock(world, i12, k142) - 1;
               if (!this.isSurface(world, i12, j13, k142)) {
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

      int oppHeight;
      if (this.restrictions && (oppHeight = this.getTopBlock(world, 0, 15) - 1) > 0) {
         this.originY = this.getY(oppHeight);
      }

      for (int i15 = -7; i15 <= 11; i15++) {
         for (int k1 = 0; k1 <= 14; k1++) {
            if (i15 != -7 && i15 != 11 || k1 != 0 && k1 != 14) {
               int beam = 0;
               if (i15 == -7 || i15 == 11) {
                  beam = Math.floorMod(k1, 4) == 1 ? 1 : 0;
               } else if (k1 == 0 || k1 == 14) {
                  beam = Math.floorMod(i15, 4) == 2 ? 1 : 0;
               }

               if (beam != 0) {
                  for (int j12 = 4; (j12 >= 0 || !this.isOpaque(world, i15, j12, k1)) && this.getY(j12) >= 0; j12--) {
                     this.setBlockAndMetadata(world, i15, j12, k1, this.woodBeamBlock, this.woodBeamMeta);
                     this.setGrassToDirt(world, i15, j12 - 1, k1);
                  }
               } else if (i15 != -7 && i15 != 11 && k1 != 0 && k1 != 14) {
                  for (int j12 = 0; (j12 >= 0 || !this.isOpaque(world, i15, j12, k1)) && this.getY(j12) >= 0; j12--) {
                     this.setBlockAndMetadata(world, i15, j12, k1, this.plankBlock, this.plankMeta);
                     this.setGrassToDirt(world, i15, j12 - 1, k1);
                  }

                  for (int var72 = 1; var72 <= 4; var72++) {
                     this.setAir(world, i15, var72, k1);
                  }
               } else {
                  for (int j12 = 0; (j12 >= 0 || !this.isOpaque(world, i15, j12, k1)) && this.getY(j12) >= 0; j12--) {
                     this.setBlockAndMetadata(world, i15, j12, k1, this.rockBlock, this.rockMeta);
                     this.setGrassToDirt(world, i15, j12 - 1, k1);
                  }

                  for (int var70 = 1; var70 <= 4; var70++) {
                     this.setBlockAndMetadata(world, i15, var70, k1, this.wallBlock, this.wallMeta);
                  }
               }
            }
         }
      }

      for (int k1421 : new int[]{0, 14}) {
         for (int i1 = -4; i1 <= 8; i1++) {
            if (Math.floorMod(i1, 4) == 0 && i1 != 0) {
               this.setBlockAndMetadata(world, i1, 2, k1421, got("glassPane"), 0);
               this.setBlockAndMetadata(world, i1, 3, k1421, got("glassPane"), 0);
            }
         }
      }

      for (int i1421 : new int[]{-7, 11}) {
         for (int k132 = 3; k132 <= 11; k132++) {
            if (Math.floorMod(k132, 4) == 3 && (i1421 != -7 || k132 != 7)) {
               this.setBlockAndMetadata(world, i1421, 2, k132, got("glassPane"), 0);
               this.setBlockAndMetadata(world, i1421, 3, k132, got("glassPane"), 0);
            }
         }
      }

      this.setBlockAndMetadata(world, 0, 0, 0, this.plankBlock, this.plankMeta);
      this.setBlockAndMetadata(world, 0, 1, 0, this.doorBlock, 1);
      this.setBlockAndMetadata(world, 0, 2, 0, this.doorBlock, 8);
      this.setBlockAndMetadata(world, 0, 0, 14, this.plankBlock, this.plankMeta);
      this.setBlockAndMetadata(world, 0, 1, 14, this.doorBlock, 3);
      this.setBlockAndMetadata(world, 0, 2, 14, this.doorBlock, 8);
      int[] i15 = new int[]{-1, 15};
      int k1 = i15.length;

      for (int beam = 0; beam < k1; beam++) {
         int i1 = 0;
         int k142 = i15[beam];
         int doorHeight = this.getTopBlock(world, i1, k142) - 1;
         if (doorHeight < 0) {
            for (int j14 = 0; (j14 == 0 || !this.isOpaque(world, i1, j14, k142)) && this.getY(j14) >= 0; j14--) {
               this.setBlockAndMetadata(world, i1, j14, k142, this.plankBlock, this.plankMeta);
               this.setGrassToDirt(world, i1, j14 - 1, k142);
            }

            i1++;

            for (int var126 = 0; !this.isOpaque(world, i1, var126, k142) && this.getY(var126) >= 0; var126--) {
               this.setBlockAndMetadata(world, i1, var126, k142, this.plankStairBlock, 0);
               this.setGrassToDirt(world, i1, var126 - 1, k142);

               for (int j2 = var126 - 1; !this.isOpaque(world, i1, j2, k142) && this.getY(j2) >= 0; j2--) {
                  this.setBlockAndMetadata(world, i1, j2, k142, this.plankBlock, this.plankMeta);
                  this.setGrassToDirt(world, i1, j2 - 1, k142);
               }

               if (++i1 >= 15) {
                  break;
               }
            }
         }
      }

      this.setBlockAndMetadata(world, -2, 3, -1, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, -2, 4, -1, vanilla("field_150478_aa"), 5);
      this.setBlockAndMetadata(world, 2, 3, -1, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 2, 4, -1, vanilla("field_150478_aa"), 5);
      this.setBlockAndMetadata(world, 0, 4, -1, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 0, 4, -2, this.plankBlock, this.plankMeta);
      this.placeSign(world, -1, 4, -2, vanilla("field_150444_as"), 5, this.tavernNameSign);
      this.placeSign(world, 1, 4, -2, vanilla("field_150444_as"), 4, this.tavernNameSign);
      this.placeSign(world, 0, 4, -3, vanilla("field_150444_as"), 2, this.tavernNameSign);
      this.setBlockAndMetadata(world, -2, 3, 15, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, -2, 4, 15, vanilla("field_150478_aa"), 5);
      this.setBlockAndMetadata(world, 2, 3, 15, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 2, 4, 15, vanilla("field_150478_aa"), 5);
      this.setBlockAndMetadata(world, 0, 4, 15, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 0, 4, 16, this.plankBlock, this.plankMeta);
      this.placeSign(world, -1, 4, 16, vanilla("field_150444_as"), 5, this.tavernNameSign);
      this.placeSign(world, 1, 4, 16, vanilla("field_150444_as"), 4, this.tavernNameSign);
      this.placeSign(world, 0, 4, 17, vanilla("field_150444_as"), 3, this.tavernNameSign);

      for (int i16 = -8; i16 <= 12; i16++) {
         for (int var31 = -1; var31 <= 15; var31++) {
            if (i16 > -7 && i16 < 11 || var31 > 0 && var31 < 14) {
               boolean var53 = false;
               if (i16 == -8 || i16 == 12) {
                  var53 = Math.floorMod(var31, 4) == 1;
               } else if (var31 == -1 || var31 == 15) {
                  var53 = Math.floorMod(i16, 4) == 2;
               }

               if (var53) {
                  if (i16 == -8 || i16 == 12) {
                     this.setBlockAndMetadata(world, i16, 5, var31, this.woodBeamBlock, this.woodBeamMeta | 4);
                  }

                  if (var31 == -1 || var31 == 15) {
                     this.setBlockAndMetadata(world, i16, 5, var31, this.woodBeamBlock, this.woodBeamMeta | 8);
                  }

                  for (int j12 = 6; j12 <= 8; j12++) {
                     this.setBlockAndMetadata(world, i16, j12, var31, this.woodBeamBlock, this.woodBeamMeta);
                  }
               } else if (i16 != -8 && i16 != 12 && var31 != -1 && var31 != 15) {
                  if ((i16 == -7 || i16 == 11) && (var31 == 1 || var31 == 13) || (i16 == -6 || i16 == 10) && (var31 == 0 || var31 == 14)) {
                     if (i16 == -7 || i16 == 11) {
                        this.setBlockAndMetadata(world, i16, 5, var31, this.woodBeamBlock, this.woodBeamMeta | 4);
                     }

                     if (var31 == 0 || var31 == 14) {
                        this.setBlockAndMetadata(world, i16, 5, var31, this.woodBeamBlock, this.woodBeamMeta | 8);
                     }

                     for (int j12 = 6; j12 <= 8; j12++) {
                        this.setBlockAndMetadata(world, i16, j12, var31, this.wallBlock, this.wallMeta);
                     }
                  } else {
                     this.setBlockAndMetadata(world, i16, 5, var31, this.plankBlock, this.plankMeta);

                     for (int j12 = 6; j12 <= 11; j12++) {
                        this.setAir(world, i16, j12, var31);
                     }
                  }
               } else {
                  if (i16 == -8 || i16 == 12) {
                     this.setBlockAndMetadata(world, i16, 5, var31, this.woodBeamBlock, this.woodBeamMeta | 8);
                  }

                  if (var31 == -1 || var31 == 15) {
                     this.setBlockAndMetadata(world, i16, 5, var31, this.woodBeamBlock, this.woodBeamMeta | 4);
                  }

                  for (int j12 = 6; j12 <= 8; j12++) {
                     this.setBlockAndMetadata(world, i16, j12, var31, this.wallBlock, this.wallMeta);
                  }
               }
            }
         }
      }

      for (int k1421 : new int[]{-1, 15}) {
         for (int i1 = -4; i1 <= 8; i1++) {
            if (Math.floorMod(i1, 4) == 0) {
               this.setBlockAndMetadata(world, i1, 7, k1421, got("glassPane"), 0);
            }
         }
      }

      for (int i142 : new int[]{-8, 12}) {
         for (int k132 = 3; k132 <= 11; k132++) {
            if (Math.floorMod(k132, 4) == 3) {
               this.setBlockAndMetadata(world, i142, 7, k132, got("glassPane"), 0);
            }
         }
      }

      for (int step2 = 0; step2 <= 2; step2++) {
         for (int i17 = -9; i17 <= 13; i17++) {
            if (i17 >= -7 + step2 && i17 <= 11 - step2) {
               this.setBlockAndMetadata(world, i17, 8 + step2, -2 + step2, this.roofStairBlock, 2);
               this.setBlockAndMetadata(world, i17, 8 + step2, 16 - step2, this.roofStairBlock, 3);
            }

            if (i17 <= -7 + step2 || i17 >= 11 - step2) {
               this.setBlockAndMetadata(world, i17, 8 + step2, step2, this.roofStairBlock, 2);
               this.setBlockAndMetadata(world, i17, 8 + step2, 14 - step2, this.roofStairBlock, 3);
            }
         }

         this.setBlockAndMetadata(world, -7 + step2, 8 + step2, -1 + step2, this.roofStairBlock, 1);
         this.setBlockAndMetadata(world, 11 - step2, 8 + step2, -1 + step2, this.roofStairBlock, 0);
         this.setBlockAndMetadata(world, -7 + step2, 8 + step2, 15 - step2, this.roofStairBlock, 1);
         this.setBlockAndMetadata(world, 11 - step2, 8 + step2, 15 - step2, this.roofStairBlock, 0);
      }

      for (int i18 = -9; i18 <= 13; i18++) {
         this.setBlockAndMetadata(world, i18, 11, 4, this.roofBlock, this.roofMeta);
         this.setBlockAndMetadata(world, i18, 12, 5, this.roofSlabBlock, this.roofSlabMeta);
         this.setBlockAndMetadata(world, i18, 12, 6, this.roofBlock, this.roofMeta);
         this.setBlockAndMetadata(world, i18, 12, 7, this.woodBeamBlock, this.woodBeamMeta | 4);
         this.setBlockAndMetadata(world, i18, 13, 7, this.roofSlabBlock, this.roofSlabMeta);
         this.setBlockAndMetadata(world, i18, 12, 8, this.roofBlock, this.roofMeta);
         this.setBlockAndMetadata(world, i18, 12, 9, this.roofSlabBlock, this.roofSlabMeta);
         this.setBlockAndMetadata(world, i18, 11, 10, this.roofBlock, this.roofMeta);
         if (i18 >= -3 && i18 <= 7) {
            this.setBlockAndMetadata(world, i18, 11, 1, this.roofSlabBlock, this.roofSlabMeta);
            this.setBlockAndMetadata(world, i18, 11, 2, this.roofBlock, this.roofMeta);
            this.setBlockAndMetadata(world, i18, 11, 3, this.roofBlock, this.roofMeta);
            this.setBlockAndMetadata(world, i18, 11, 11, this.roofBlock, this.roofMeta);
            this.setBlockAndMetadata(world, i18, 11, 12, this.roofBlock, this.roofMeta);
            this.setBlockAndMetadata(world, i18, 11, 13, this.roofSlabBlock, this.roofSlabMeta);
         } else {
            this.setBlockAndMetadata(world, i18, 11, 3, this.roofSlabBlock, this.roofSlabMeta);
            this.setBlockAndMetadata(world, i18, 11, 11, this.roofSlabBlock, this.roofSlabMeta);
         }

         if (i18 == -4 || i18 == 8) {
            this.setBlockAndMetadata(world, i18, 11, 1, this.roofSlabBlock, this.roofSlabMeta);
            this.setBlockAndMetadata(world, i18, 11, 2, this.roofSlabBlock, this.roofSlabMeta);
            this.setBlockAndMetadata(world, i18, 11, 12, this.roofSlabBlock, this.roofSlabMeta);
            this.setBlockAndMetadata(world, i18, 11, 13, this.roofSlabBlock, this.roofSlabMeta);
         }

         if (i18 == -9 || i18 == 13) {
            this.setBlockAndMetadata(world, i18, 8, 1, this.roofStairBlock, 7);
            this.setBlockAndMetadata(world, i18, 9, 2, this.roofStairBlock, 7);
            this.setBlockAndMetadata(world, i18, 10, 3, this.roofStairBlock, 7);
            this.setBlockAndMetadata(world, i18, 11, 5, this.roofSlabBlock, this.roofSlabMeta | 8);
            this.setBlockAndMetadata(world, i18, 11, 9, this.roofSlabBlock, this.roofSlabMeta | 8);
            this.setBlockAndMetadata(world, i18, 10, 11, this.roofStairBlock, 6);
            this.setBlockAndMetadata(world, i18, 9, 12, this.roofStairBlock, 6);
            this.setBlockAndMetadata(world, i18, 8, 13, this.roofStairBlock, 6);
         }
      }

      for (int i1421 : new int[]{-8, 12}) {
         for (int k132 = 2; k132 <= 12; k132++) {
            this.setBlockAndMetadata(world, i1421, 9, k132, this.woodBeamBlock, this.woodBeamMeta | 8);
         }

         for (int var109 = 3; var109 <= 11; var109++) {
            this.setBlockAndMetadata(world, i1421, 10, var109, this.wallBlock, this.wallMeta);
         }

         for (int var110 = 5; var110 <= 9; var110++) {
            this.setBlockAndMetadata(world, i1421, 11, var110, this.wallBlock, this.wallMeta);
         }
      }

      for (int i19 = 3; i19 <= 5; i19++) {
         for (int var33 = 6; var33 <= 8; var33++) {
            for (int j1 = 0; j1 <= 13; j1++) {
               if (i19 == 4 && var33 == 7) {
                  this.setAir(world, 4, j1, 7);
               } else {
                  this.setBlockAndMetadata(world, i19, j1, var33, this.brickBlock, this.brickMeta);
               }
            }
         }

         this.setBlockAndMetadata(world, i19, 14, 6, this.brickStairBlock, 2);
         this.setBlockAndMetadata(world, i19, 14, 8, this.brickStairBlock, 3);
      }

      this.setBlockAndMetadata(world, 3, 14, 7, this.brickStairBlock, 1);
      this.setBlockAndMetadata(world, 5, 14, 7, this.brickStairBlock, 0);
      this.setBlockAndMetadata(world, 4, 15, 7, this.brickBlock, this.brickMeta);
      this.setBlockAndMetadata(world, 4, 16, 7, this.brickBlock, this.brickMeta);
      this.setBlockAndMetadata(world, 4, 17, 7, this.brickWallBlock, this.brickWallMeta);
      this.setBlockAndMetadata(world, 4, 18, 7, this.brickWallBlock, this.brickWallMeta);
      this.setBlockAndMetadata(world, 4, 0, 7, got("hearth"), 0);
      this.setBlockAndMetadata(world, 4, 1, 7, vanilla("field_150480_ab"), 0);
      this.setBlockAndMetadata(world, 4, 1, 6, vanilla("field_150411_aY"), 0);
      this.setBlockAndMetadata(world, 4, 1, 8, vanilla("field_150411_aY"), 0);
      this.setBlockAndMetadata(world, 3, 1, 7, vanilla("field_150411_aY"), 0);
      this.setBlockAndMetadata(world, 5, 1, 7, vanilla("field_150411_aY"), 0);
      this.setBlockAndMetadata(world, 4, 2, 6, vanilla("field_150460_al"), 2);
      this.setBlockAndMetadata(world, 4, 2, 8, vanilla("field_150460_al"), 3);
      this.setBlockAndMetadata(world, 3, 2, 7, vanilla("field_150460_al"), 5);
      this.setBlockAndMetadata(world, 5, 2, 7, vanilla("field_150460_al"), 4);
      this.setBlockAndMetadata(world, 0, 4, 3, got("chandelier"), 2);
      this.setBlockAndMetadata(world, 0, 4, 11, got("chandelier"), 2);
      this.setBlockAndMetadata(world, 8, 4, 3, got("chandelier"), 2);
      this.setBlockAndMetadata(world, 8, 4, 11, got("chandelier"), 2);

      for (int k1421 : new int[]{1, 2}) {
         this.setBlockAndMetadata(world, -4, 1, k1421, this.plankBlock, this.plankMeta);
         this.placeMugOrPlate(world, random, -4, 2, k1421);
         this.setBlockAndMetadata(world, -6, 1, k1421, this.plankStairBlock, 0);
         this.setBlockAndMetadata(world, -2, 1, k1421, this.plankStairBlock, 1);
      }

      for (int k142 : new int[]{1, 2, 12, 13}) {
         this.setBlockAndMetadata(world, 2, 1, k142, this.plankBlock, this.plankMeta);
         this.placeMugOrPlate(world, random, 2, 2, k142);
         this.setBlockAndMetadata(world, 3, 1, k142, this.plankBlock, this.plankMeta);
         this.placeMugOrPlate(world, random, 3, 2, k142);
         this.setBlockAndMetadata(world, 5, 1, k142, this.plankStairBlock, 1);
      }

      for (int k12 = 6; k12 <= 8; k12++) {
         this.setBlockAndMetadata(world, 8, 1, k12, this.plankBlock, this.plankMeta);
         this.placeMugOrPlate(world, random, 8, 2, k12);
         this.setBlockAndMetadata(world, 10, 1, k12, this.plankStairBlock, 1);
      }

      for (int i13 = 7; i13 <= 10; i13++) {
         this.setBlockAndMetadata(world, i13, 1, 1, this.plankStairBlock, 3);
         this.setBlockAndMetadata(world, i13, 1, 13, this.plankStairBlock, 2);
      }

      for (int var65 = 2; var65 <= 4; var65++) {
         this.setBlockAndMetadata(world, 10, 1, var65, this.plankStairBlock, 1);
      }

      for (int var66 = 10; var66 <= 12; var66++) {
         this.setBlockAndMetadata(world, 10, 1, var66, this.plankStairBlock, 1);
      }

      for (int var84 = 7; var84 <= 8; var84++) {
         for (int k132 : new int[]{3, 4, 10, 11}) {
            this.setBlockAndMetadata(world, var84, 1, k132, this.plankBlock, this.plankMeta);
            this.placeMugOrPlate(world, random, var84, 2, k132);
         }
      }

      for (int j15 = 1; j15 <= 4; j15++) {
         this.setBlockAndMetadata(world, -2, j15, 5, this.woodBeamBlock, this.woodBeamMeta);
         this.setBlockAndMetadata(world, -2, j15, 9, this.woodBeamBlock, this.woodBeamMeta);
      }

      for (int var85 = -6; var85 <= -3; var85++) {
         this.setBlockAndMetadata(world, var85, 1, 5, this.plankBlock, this.plankMeta);
         this.setBlockAndMetadata(world, var85, 3, 5, this.fenceBlock, this.fenceMeta);
         this.setBlockAndMetadata(world, var85, 4, 5, this.woodBeamBlock, this.woodBeamMeta | 4);
         this.setBlockAndMetadata(world, var85, 1, 9, this.plankBlock, this.plankMeta);
         this.setBlockAndMetadata(world, var85, 3, 9, this.fenceBlock, this.fenceMeta);
         this.setBlockAndMetadata(world, var85, 4, 9, this.woodBeamBlock, this.woodBeamMeta | 4);
      }

      for (int var67 = 6; var67 <= 8; var67++) {
         this.setBlockAndMetadata(world, -2, 1, var67, this.plankBlock, this.plankMeta);
         this.setBlockAndMetadata(world, -2, 3, var67, this.fenceBlock, this.fenceMeta);
         this.setBlockAndMetadata(world, -2, 4, var67, this.woodBeamBlock, this.woodBeamMeta | 8);
      }

      this.setBlockAndMetadata(world, -4, 1, 5, this.fenceGateBlock, 0);
      this.placeBarrel(world, random, -6, 2, 5, 3, "food_DEFAULT_DRINK");
      this.placeMug(world, random, -5, 2, 5, 2, "food_DEFAULT_DRINK");
      this.placeMug(world, random, -3, 2, 5, 2, "food_DEFAULT_DRINK");
      this.setBlockAndMetadata(world, -4, 1, 9, this.fenceGateBlock, 2);
      this.placeBarrel(world, random, -6, 2, 9, 2, "food_DEFAULT_DRINK");
      this.placeMug(world, random, -5, 2, 9, 0, "food_DEFAULT_DRINK");
      this.placeMug(world, random, -3, 2, 9, 0, "food_DEFAULT_DRINK");
      this.placeBarrel(world, random, -2, 2, 8, 5, "food_DEFAULT_DRINK");
      this.placeMug(world, random, -2, 2, 6, 1, "food_DEFAULT_DRINK");
      this.setBlockAndMetadata(world, -6, 1, 6, this.plankStairBlock, 4);
      this.placePlateWithCertainty(world, random, -6, 2, 6, this.plateBlock, "food_DEFAULT");
      this.setBlockAndMetadata(world, -6, 1, 7, vanilla("field_150460_al"), 4);
      this.setBlockAndMetadata(world, -6, 1, 8, vanilla("field_150383_bp"), 3);
      this.placeChest(world, random, -3, 0, 8, 5, this.getChestContents());

      for (int var68 = 6; var68 <= 8; var68++) {
         this.setBlockAndMetadata(world, -6, 3, var68, this.plankStairBlock, 4);
         this.placeBarrel(world, random, -6, 4, var68, 4, "food_DEFAULT_DRINK");
      }

      this.setBlockAndMetadata(world, -4, 4, 7, got("chandelier"), 2);

      for (int step = 0; step <= 2; step++) {
         this.setBlockAndMetadata(world, -3 - step, 1 + step, 13, this.plankStairBlock, 0);
         this.setBlockAndMetadata(world, -4 - step, 1 + step, 13, this.plankStairBlock, 5);
      }

      this.setBlockAndMetadata(world, -6, 3, 13, this.plankBlock, this.plankMeta);

      for (int var64 = 0; var64 <= 1; var64++) {
         this.setBlockAndMetadata(world, -6, 4 + var64, 12 - var64, this.plankStairBlock, 3);
         this.setBlockAndMetadata(world, -6, 3 + var64, 12 - var64, this.plankStairBlock, 6);
      }

      for (int var86 = -6; var86 <= -4; var86++) {
         this.setAir(world, var86, 5, 13);
      }

      this.setAir(world, -6, 5, 12);

      for (int var87 = -5; var87 <= -3; var87++) {
         this.setBlockAndMetadata(world, var87, 6, 14, this.fenceBlock, this.fenceMeta);
         this.setBlockAndMetadata(world, var87, 6, 12, this.fenceBlock, this.fenceMeta);
      }

      this.setBlockAndMetadata(world, -3, 6, 13, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, -7, 6, 12, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, -7, 6, 11, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, -5, 6, 11, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, -5, 7, 12, vanilla("field_150478_aa"), 5);

      for (int var88 = -7; var88 <= -3; var88++) {
         for (int var35 = 10; var35 <= 14; var35++) {
            if (var88 != -3 || var35 != 10) {
               if ((var88 >= -4 || var35 <= 11) && var35 <= 13) {
                  this.setBlockAndMetadata(world, var88, 10, var35, this.wallBlock, this.wallMeta);
               }

               if (var88 >= -5 || var35 <= 12) {
                  this.setBlockAndMetadata(world, var88, 9, var35, this.roofSlabBlock, this.roofSlabMeta | 8);
               }
            }
         }
      }

      this.setBlockAndMetadata(world, 4, 7, 6, vanilla("field_150411_aY"), 0);
      this.setBlockAndMetadata(world, 4, 7, 8, vanilla("field_150411_aY"), 0);
      this.setBlockAndMetadata(world, 3, 7, 7, vanilla("field_150411_aY"), 0);
      this.setBlockAndMetadata(world, 5, 7, 7, vanilla("field_150411_aY"), 0);
      this.spawnItemFrame(world, 3, 10, 7, 3, getRandFrameItem(random));

      for (int var89 = -2; var89 <= 1; var89++) {
         for (int var36 = 5; var36 <= 9; var36++) {
            this.setBlockAndMetadata(world, var89, 6, var36, vanilla("field_150404_cg"), 12);
         }
      }

      for (int var90 = -2; var90 <= 6; var90++) {
         int i2 = Math.floorMod(var90, 4);
         if (i2 == 2) {
            for (int var58 = 6; var58 <= 8; var58++) {
               this.setBlockAndMetadata(world, var90, var58, 3, this.woodBeamBlock, this.woodBeamMeta);

               for (int k142 = 0; k142 <= 2; k142++) {
                  this.setBlockAndMetadata(world, var90, var58, k142, this.wallBlock, this.wallMeta);
               }

               this.setBlockAndMetadata(world, var90, var58, 11, this.woodBeamBlock, this.woodBeamMeta);

               for (int var118 = 12; var118 <= 14; var118++) {
                  this.setBlockAndMetadata(world, var90, var58, var118, this.wallBlock, this.wallMeta);
               }
            }

            for (int k16 = 0; k16 <= 3; k16++) {
               this.setBlockAndMetadata(world, var90, 9, k16, this.woodBeamBlock, this.woodBeamMeta | 8);
            }

            for (int var151 = 11; var151 <= 14; var151++) {
               this.setBlockAndMetadata(world, var90, 9, var151, this.woodBeamBlock, this.woodBeamMeta | 8);
            }
         } else {
            for (int var57 = 6; var57 <= 8; var57++) {
               this.setBlockAndMetadata(world, var90, var57, 3, this.wallBlock, this.wallMeta);
               this.setBlockAndMetadata(world, var90, var57, 11, this.wallBlock, this.wallMeta);
            }

            this.setBlockAndMetadata(world, var90, 9, 3, this.woodBeamBlock, this.woodBeamMeta | 4);
            this.setBlockAndMetadata(world, var90, 9, 11, this.woodBeamBlock, this.woodBeamMeta | 4);

            for (int k16 = 0; k16 <= 2; k16++) {
               this.setBlockAndMetadata(world, var90, 9, k16, this.roofSlabBlock, this.roofSlabMeta | 8);
            }

            for (int var149 = 12; var149 <= 14; var149++) {
               this.setBlockAndMetadata(world, var90, 9, var149, this.roofSlabBlock, this.roofSlabMeta | 8);
            }
         }

         if (i2 == 0) {
            this.setBlockAndMetadata(world, var90, 6, 3, this.doorBlock, 3);
            this.setBlockAndMetadata(world, var90, 7, 3, this.doorBlock, 8);
            this.setBlockAndMetadata(world, var90, 8, 2, vanilla("field_150478_aa"), 4);
            this.setBlockAndMetadata(world, var90, 6, 11, this.doorBlock, 1);
            this.setBlockAndMetadata(world, var90, 7, 11, this.doorBlock, 8);
            this.setBlockAndMetadata(world, var90, 8, 12, vanilla("field_150478_aa"), 3);
         }

         if (i2 == 3) {
            this.setBlockAndMetadata(world, var90, 6, 1, this.bedBlock, 0);
            this.setBlockAndMetadata(world, var90, 6, 2, this.bedBlock, 8);
            this.setBlockAndMetadata(world, var90, 6, 0, this.getChest(), 4);
            this.setBlockAndMetadata(world, var90, 6, 13, this.bedBlock, 2);
            this.setBlockAndMetadata(world, var90, 6, 12, this.bedBlock, 10);
            this.setBlockAndMetadata(world, var90, 6, 14, this.getChest(), 4);
         }

         if (i2 == 1) {
            this.setBlockAndMetadata(world, var90, 6, 2, this.plankStairBlock, 2);
            this.setBlockAndMetadata(world, var90, 6, 0, this.plankBlock, this.plankMeta);
            this.placeMug(world, random, var90, 7, 0, 2, "food_DEFAULT_DRINK");
            this.setBlockAndMetadata(world, var90, 6, 12, this.plankStairBlock, 3);
            this.setBlockAndMetadata(world, var90, 6, 14, this.plankBlock, this.plankMeta);
            this.placeMug(world, random, var90, 7, 14, 0, "food_DEFAULT_DRINK");
         }

         for (int var152 = 1; var152 <= 3; var152++) {
            this.setBlockAndMetadata(world, var90, 10, var152, this.wallBlock, this.wallMeta);
         }

         for (int var153 = 11; var153 <= 13; var153++) {
            this.setBlockAndMetadata(world, var90, 10, var153, this.wallBlock, this.wallMeta);
         }
      }

      for (int var69 = 5; var69 <= 9; var69++) {
         int k2 = Math.floorMod(var69, 4);
         if (k2 == 1) {
            for (int var60 = 6; var60 <= 8; var60++) {
               this.setBlockAndMetadata(world, -4, var60, var69, this.woodBeamBlock, this.woodBeamMeta);

               for (int i142 = -7; i142 <= -5; i142++) {
                  this.setBlockAndMetadata(world, i142, var60, var69, this.wallBlock, this.wallMeta);
               }

               this.setBlockAndMetadata(world, 8, var60, var69, this.woodBeamBlock, this.woodBeamMeta);

               for (int var113 = 9; var113 <= 11; var113++) {
                  this.setBlockAndMetadata(world, var113, var60, var69, this.wallBlock, this.wallMeta);
               }
            }

            for (int i12 = -7; i12 <= -4; i12++) {
               this.setBlockAndMetadata(world, i12, 9, var69, this.woodBeamBlock, this.woodBeamMeta | 4);
            }

            for (int var81 = 8; var81 <= 11; var81++) {
               this.setBlockAndMetadata(world, var81, 9, var69, this.woodBeamBlock, this.woodBeamMeta | 4);
            }
         } else {
            for (int var59 = 6; var59 <= 8; var59++) {
               this.setBlockAndMetadata(world, -4, var59, var69, this.wallBlock, this.wallMeta);
               this.setBlockAndMetadata(world, 8, var59, var69, this.wallBlock, this.wallMeta);
            }

            this.setBlockAndMetadata(world, -4, 9, var69, this.woodBeamBlock, this.woodBeamMeta | 8);
            this.setBlockAndMetadata(world, 8, 9, var69, this.woodBeamBlock, this.woodBeamMeta | 8);

            for (int i12 = -7; i12 <= -5; i12++) {
               this.setBlockAndMetadata(world, i12, 9, var69, this.roofSlabBlock, this.roofSlabMeta | 8);
            }

            for (int var79 = 9; var79 <= 11; var79++) {
               this.setBlockAndMetadata(world, var79, 9, var69, this.roofSlabBlock, this.roofSlabMeta | 8);
            }
         }

         if (k2 == 3) {
            this.setBlockAndMetadata(world, -4, 6, var69, this.doorBlock, 0);
            this.setBlockAndMetadata(world, -4, 7, var69, this.doorBlock, 8);
            this.setBlockAndMetadata(world, -5, 8, var69, vanilla("field_150478_aa"), 1);
            this.setBlockAndMetadata(world, 8, 6, var69, this.doorBlock, 2);
            this.setBlockAndMetadata(world, 8, 7, var69, this.doorBlock, 8);
            this.setBlockAndMetadata(world, 9, 8, var69, vanilla("field_150478_aa"), 2);
         }

         if (k2 == 0) {
            this.setBlockAndMetadata(world, -6, 6, var69, this.bedBlock, 1);
            this.setBlockAndMetadata(world, -5, 6, var69, this.bedBlock, 9);
            this.setBlockAndMetadata(world, -7, 6, var69, this.getChest(), 2);
            this.setBlockAndMetadata(world, 10, 6, var69, this.bedBlock, 3);
            this.setBlockAndMetadata(world, 9, 6, var69, this.bedBlock, 11);
            this.setBlockAndMetadata(world, 11, 6, var69, this.getChest(), 2);
         }

         if (k2 == 2) {
            this.setBlockAndMetadata(world, -5, 6, var69, this.plankStairBlock, 1);
            this.setBlockAndMetadata(world, -7, 6, var69, this.plankBlock, this.plankMeta);
            this.placeMug(world, random, -7, 7, var69, 3, "food_DEFAULT_DRINK");
            this.setBlockAndMetadata(world, 9, 6, var69, this.plankStairBlock, 0);
            this.setBlockAndMetadata(world, 11, 6, var69, this.plankBlock, this.plankMeta);
            this.placeMug(world, random, 11, 7, var69, 1, "food_DEFAULT_DRINK");
         }

         for (int var82 = -7; var82 <= -4; var82++) {
            this.setBlockAndMetadata(world, var82, 10, var69, this.wallBlock, this.wallMeta);
            this.setBlockAndMetadata(world, var82, 11, var69, this.wallBlock, this.wallMeta);
         }

         for (int var83 = 8; var83 <= 11; var83++) {
            this.setBlockAndMetadata(world, var83, 10, var69, this.wallBlock, this.wallMeta);
            this.setBlockAndMetadata(world, var83, 11, var69, this.wallBlock, this.wallMeta);
         }
      }

      for (int var91 = 7; var91 <= 8; var91++) {
         for (int var37 = 10; var37 <= 11; var37++) {
            if (var91 != 7 || var37 != 10) {
               for (int var61 = 6; var61 <= 8; var61++) {
                  this.setBlockAndMetadata(world, var91, var61, var37, this.wallBlock, this.wallMeta);
               }

               this.setBlockAndMetadata(world, var91, 9, var37, this.woodBeamBlock, this.woodBeamMeta | (var91 == 8 ? 8 : 4));
               this.setBlockAndMetadata(world, var91, 10, var37, this.wallBlock, this.wallMeta);
            }
         }
      }

      this.setBlockAndMetadata(world, 8, 6, 10, this.doorBlock, 2);
      this.setBlockAndMetadata(world, 8, 7, 10, this.doorBlock, 8);
      this.setBlockAndMetadata(world, 9, 8, 10, vanilla("field_150478_aa"), 2);
      this.setBlockAndMetadata(world, 7, 8, 13, vanilla("field_150478_aa"), 2);

      for (int var92 = 7; var92 <= 8; var92++) {
         for (int var38 = 12; var38 <= 13; var38++) {
            this.setBlockAndMetadata(world, var92, 10, var38, this.wallBlock, this.wallMeta);
         }
      }

      for (int var93 = 9; var93 <= 11; var93++) {
         for (int var39 = 10; var39 <= 11; var39++) {
            this.setBlockAndMetadata(world, var93, 10, var39, this.wallBlock, this.wallMeta);
         }
      }

      for (int var94 = 7; var94 <= 9; var94++) {
         for (int var40 = 12; var40 <= 14; var40++) {
            this.setBlockAndMetadata(world, var94, 9, var40, this.roofSlabBlock, this.roofSlabMeta | 8);
         }
      }

      for (int var95 = 9; var95 <= 11; var95++) {
         for (int var41 = 10; var41 <= 12; var41++) {
            this.setBlockAndMetadata(world, var95, 9, var41, this.roofSlabBlock, this.roofSlabMeta | 8);
         }
      }

      this.setBlockAndMetadata(world, 11, 6, 11, this.bedBlock, 0);
      this.setBlockAndMetadata(world, 11, 6, 12, this.bedBlock, 8);
      this.setBlockAndMetadata(world, 11, 6, 10, this.getChest(), 5);
      this.setBlockAndMetadata(world, 7, 6, 13, this.bedBlock, 2);
      this.setBlockAndMetadata(world, 7, 6, 12, this.bedBlock, 10);
      this.setBlockAndMetadata(world, 7, 6, 14, this.getChest(), 4);
      this.setBlockAndMetadata(world, 9, 6, 14, this.plankBlock, this.plankMeta);
      this.placeMug(world, random, 9, 7, 14, 0, "food_DEFAULT_DRINK");
      this.setBlockAndMetadata(world, 10, 6, 13, this.plankBlock, this.plankMeta);
      this.placeMug(world, random, 10, 7, 13, 1, "food_DEFAULT_DRINK");

      for (int var96 = 7; var96 <= 8; var96++) {
         for (int var42 = 3; var42 <= 4; var42++) {
            if (var96 != 7 || var42 != 4) {
               for (int var62 = 6; var62 <= 8; var62++) {
                  this.setBlockAndMetadata(world, var96, var62, var42, this.wallBlock, this.wallMeta);
               }

               this.setBlockAndMetadata(world, var96, 9, var42, this.woodBeamBlock, this.woodBeamMeta | (var96 == 8 ? 8 : 4));
               this.setBlockAndMetadata(world, var96, 10, var42, this.wallBlock, this.wallMeta);
            }
         }
      }

      this.setBlockAndMetadata(world, 8, 6, 4, this.doorBlock, 2);
      this.setBlockAndMetadata(world, 8, 7, 4, this.doorBlock, 8);
      this.setBlockAndMetadata(world, 9, 8, 4, vanilla("field_150478_aa"), 2);
      this.setBlockAndMetadata(world, 7, 8, 1, vanilla("field_150478_aa"), 2);

      for (int var97 = 7; var97 <= 8; var97++) {
         for (int var43 = 1; var43 <= 2; var43++) {
            this.setBlockAndMetadata(world, var97, 10, var43, this.wallBlock, this.wallMeta);
         }
      }

      for (int var98 = 9; var98 <= 11; var98++) {
         for (int var44 = 3; var44 <= 4; var44++) {
            this.setBlockAndMetadata(world, var98, 10, var44, this.wallBlock, this.wallMeta);
         }
      }

      for (int var99 = 7; var99 <= 9; var99++) {
         for (int var45 = 0; var45 <= 2; var45++) {
            this.setBlockAndMetadata(world, var99, 9, var45, this.roofSlabBlock, this.roofSlabMeta | 8);
         }
      }

      for (int var100 = 9; var100 <= 11; var100++) {
         for (int var46 = 2; var46 <= 4; var46++) {
            this.setBlockAndMetadata(world, var100, 9, var46, this.roofSlabBlock, this.roofSlabMeta | 8);
         }
      }

      this.setBlockAndMetadata(world, 11, 6, 3, this.bedBlock, 2);
      this.setBlockAndMetadata(world, 11, 6, 2, this.bedBlock, 10);
      this.setBlockAndMetadata(world, 11, 6, 4, this.getChest(), 5);
      this.setBlockAndMetadata(world, 7, 6, 1, this.bedBlock, 0);
      this.setBlockAndMetadata(world, 7, 6, 2, this.bedBlock, 8);
      this.setBlockAndMetadata(world, 7, 6, 0, this.getChest(), 4);
      this.setBlockAndMetadata(world, 9, 6, 0, this.plankBlock, this.plankMeta);
      this.placeMug(world, random, 9, 7, 0, 2, "food_DEFAULT_DRINK");
      this.setBlockAndMetadata(world, 10, 6, 1, this.plankBlock, this.plankMeta);
      this.placeMug(world, random, 10, 7, 1, 1, "food_DEFAULT_DRINK");

      for (int var101 = -4; var101 <= -3; var101++) {
         for (int var47 = 3; var47 <= 4; var47++) {
            if (var101 != -3 || var47 != 4) {
               for (int var63 = 6; var63 <= 8; var63++) {
                  this.setBlockAndMetadata(world, var101, var63, var47, this.wallBlock, this.wallMeta);
               }

               this.setBlockAndMetadata(world, var101, 9, var47, this.woodBeamBlock, this.woodBeamMeta | (var101 == -4 ? 8 : 4));
               this.setBlockAndMetadata(world, var101, 10, var47, this.wallBlock, this.wallMeta);
            }
         }
      }

      this.setBlockAndMetadata(world, -4, 6, 4, this.doorBlock, 0);
      this.setBlockAndMetadata(world, -4, 7, 4, this.doorBlock, 8);
      this.setBlockAndMetadata(world, -5, 8, 4, vanilla("field_150478_aa"), 1);
      this.setBlockAndMetadata(world, -3, 8, 1, vanilla("field_150478_aa"), 1);

      for (int var102 = -4; var102 <= -3; var102++) {
         for (int var48 = 1; var48 <= 2; var48++) {
            this.setBlockAndMetadata(world, var102, 10, var48, this.wallBlock, this.wallMeta);
         }
      }

      for (int var103 = -7; var103 <= -5; var103++) {
         for (int var49 = 3; var49 <= 4; var49++) {
            this.setBlockAndMetadata(world, var103, 10, var49, this.wallBlock, this.wallMeta);
         }
      }

      for (int var104 = -5; var104 <= -3; var104++) {
         for (int var50 = 0; var50 <= 2; var50++) {
            this.setBlockAndMetadata(world, var104, 9, var50, this.roofSlabBlock, this.roofSlabMeta | 8);
         }
      }

      for (int var105 = -7; var105 <= -5; var105++) {
         for (int var51 = 2; var51 <= 4; var51++) {
            this.setBlockAndMetadata(world, var105, 9, var51, this.roofSlabBlock, this.roofSlabMeta | 8);
         }
      }

      this.setBlockAndMetadata(world, -7, 6, 3, this.bedBlock, 2);
      this.setBlockAndMetadata(world, -7, 6, 2, this.bedBlock, 10);
      this.setBlockAndMetadata(world, -7, 6, 4, this.getChest(), 4);
      this.setBlockAndMetadata(world, -3, 6, 1, this.bedBlock, 0);
      this.setBlockAndMetadata(world, -3, 6, 2, this.bedBlock, 8);
      this.setBlockAndMetadata(world, -3, 6, 0, this.getChest(), 5);
      this.setBlockAndMetadata(world, -5, 6, 0, this.plankBlock, this.plankMeta);
      this.placeMug(world, random, -5, 7, 0, 2, "food_DEFAULT_DRINK");
      this.setBlockAndMetadata(world, -6, 6, 1, this.plankBlock, this.plankMeta);
      this.placeMug(world, random, -6, 7, 1, 3, "food_DEFAULT_DRINK");

      for (int var106 = -3; var106 <= 7; var106++) {
         for (int k1321 : new int[]{5, 9}) {
            this.setBlockAndMetadata(world, var106, 11, k1321, this.roofSlabBlock, this.roofSlabMeta | 8);
         }
      }

      this.setBlockAndMetadata(world, -1, 11, 7, got("chandelier"), 2);
      this.setBlockAndMetadata(world, 7, 11, 7, got("chandelier"), 2);
      if (this.isKingsLanding) {
         this.spawnNPCAndSetHome(new LegacyEntity(world), world, -4, 1, 7, 2);
         this.spawnNPCAndSetHome(new LegacyEntity(world), world, 2, 1, 7, 16);
         this.spawnNPCAndSetHome(new LegacyEntity(world), world, 2, 1, 7, 16);
         this.spawnNPCAndSetHome(new LegacyEntity(world), world, 2, 1, 7, 16);
         this.spawnNPCAndSetHome(new LegacyEntity(world), world, 2, 1, 7, 16);
      } else if (this.isCrossroads) {
         this.spawnNPCAndSetHome(new LegacyEntity(world), world, -4, 1, 7, 2);
         this.spawnNPCAndSetHome(new LegacyEntity(world), world, 2, 1, 7, 16);
      } else {
         this.spawnNPCAndSetHome(this.getBartender(world), world, -4, 1, 7, 2);
      }

      if (this.hasDarkSkinPeople()) {
         this.spawnNPCAndSetHome(new LegacyEntity(world), world, 2, 1, 7, 16);
         this.spawnNPCAndSetHome(new LegacyEntity(world), world, 2, 1, 7, 16);
      } else {
         this.spawnNPCAndSetHome(new LegacyEntity(world), world, 2, 1, 7, 16);
         this.spawnNPCAndSetHome(new LegacyEntity(world), world, 2, 1, 7, 16);
      }

      this.spawnNPCAndSetHome(new LegacyEntity(world), world, 2, 1, 7, 16);

      for (int l = 0; l < 5; l++) {
         this.spawnNPCAndSetHome(new LegacyEntity(world), world, 2, 1, 7, 16);
      }

      return true;
   }

   private void placeMugOrPlate(LegacyNorthernContext world, Random random, int i, int j, int k) {
      if (random.nextBoolean()) {
         this.placeMug(world, random, i, j, k, random.nextInt(4), "food_DEFAULT_DRINK");
      } else {
         this.placePlate(world, random, i, j, k, this.plateBlock, "food_DEFAULT");
      }
   }

   public void setupRandomBlocks(Random random) {
      super.setupRandomBlocks(random);
      this.bedBlock = vanilla("field_150324_C");
      String[] tavernName = getTavernName(random);
      this.tavernNameSign = new String[]{"", tavernName[0], tavernName[1], ""};
   }

}
