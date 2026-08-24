package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureWesterosFortress extends LegacyNorthernContext {

   protected GOTStructureWesterosFortress(NorthStructureBuilder builder) {
      super(builder, Style.NORTH);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureWesterosFortress piece = new GOTStructureWesterosFortress(builder);
      piece.marketVariant = Math.max(0, variant);
      piece.isAbandoned = variant == 1;
      piece.isTramp = variant == 1;
      piece.isHardhome = variant == 1;
      piece.isBlacksmith = variant == 1;
      piece.generate(piece, piece.legacyRandom(), 0, 0, 0, 0);
   }


   public boolean generate(LegacyNorthernContext world, Random random, int i, int j, int k, int rotation) {
      this.setOriginAndRotation(world, i, j, k, rotation, 12);
      this.setupRandomBlocks(random);
      if (this.restrictions) {
         int minHeight = 0;
         int maxHeight = 0;

         for (int i11 = -14; i11 <= 14; i11++) {
            for (int i12 = -14; i12 <= 14; i12++) {
               int i13 = this.getTopBlock(world, i11, i12) - 1;
               if (!this.isSurface(world, i11, i13, i12)) {
                  return false;
               }

               if (i13 < minHeight) {
                  minHeight = i13;
               }

               if (i13 > maxHeight) {
                  maxHeight = i13;
               }

               if (maxHeight - minHeight > 8) {
                  return false;
               }
            }
         }
      }

      for (int i10 = -11; i10 <= 11; i10++) {
         for (int i11 = -11; i11 <= 11; i11++) {
            int i12 = Math.abs(i10);
            int k2 = Math.abs(i11);
            if ((i12 < 9 || k2 > 5) && (k2 < 9 || i12 > 5)) {
               for (int i13 = 0; (i13 == 0 || !this.isOpaque(world, i10, i13, i11)) && this.getY(i13) >= 0; i13--) {
                  this.setBlockAndMetadata(world, i10, i13, i11, this.brickBlock, this.brickMeta);
                  this.setGrassToDirt(world, i10, i13 - 1, i11);
               }

               for (int var90 = 1; var90 <= 9; var90++) {
                  this.setAir(world, i10, var90, i11);
               }
            } else {
               boolean pillar = false;
               if (i12 == 11) {
                  pillar = k2 == 2 || k2 == 5;
               } else if (k2 == 11) {
                  pillar = i12 == 2 || i12 == 5;
               }

               for (int i13 = 5; (i13 >= 0 || !this.isOpaque(world, i10, i13, i11)) && this.getY(i13) >= 0; i13--) {
                  if (pillar && i13 >= 1) {
                     this.setBlockAndMetadata(world, i10, i13, i11, this.pillarBlock, this.pillarMeta);
                  } else {
                     this.setBlockAndMetadata(world, i10, i13, i11, this.brickBlock, this.brickMeta);
                  }

                  this.setGrassToDirt(world, i10, i13 - 1, i11);
               }

               this.setBlockAndMetadata(world, i10, 6, i11, this.brickBlock, this.brickMeta);

               for (int var101 = 7; var101 <= 9; var101++) {
                  this.setAir(world, i10, var101, i11);
               }

               if (i12 == 9 || i12 == 11 || k2 == 9 || k2 == 11) {
                  this.setBlockAndMetadata(world, i10, 7, i11, this.brick2WallBlock, this.brick2WallMeta);
                  if (i12 == 5 || k2 == 5) {
                     this.setBlockAndMetadata(world, i10, 8, i11, vanilla("field_150478_aa"), 5);
                  }
               }
            }
         }
      }

      for (int i11 : new int[]{-10, 10}) {
         for (int i12 : new int[]{-10, 10}) {
            for (int i14 = i11 - 4; i14 <= i11 + 4; i14++) {
               for (int k2 = i12 - 4; k2 <= i12 + 4; k2++) {
                  int i15 = Math.abs(i14 - i11);
                  int k3 = Math.abs(k2 - i12);
                  int i16 = Math.abs(i14);
                  int k4 = Math.abs(k2);
                  if ((i15 != 4 || k3 < 3) && (k3 != 4 || i15 < 3)) {
                     if (i15 == 4 || k3 == 4 || i15 == 3 && k3 == 3) {
                        boolean pillar = false;
                        if (i15 == 4) {
                           pillar = k3 == 2;
                        } else if (k3 == 4) {
                           pillar = i15 == 2;
                        }

                        for (int i17 = 5; (i17 >= 0 || !this.isOpaque(world, i14, i17, k2)) && this.getY(i17) >= 0; i17--) {
                           if (pillar && i17 >= 1) {
                              this.setBlockAndMetadata(world, i14, i17, k2, this.pillarBlock, this.pillarMeta);
                           } else {
                              this.setBlockAndMetadata(world, i14, i17, k2, this.brickBlock, this.brickMeta);
                           }

                           this.setGrassToDirt(world, i14, i17 - 1, k2);
                        }

                        this.setBlockAndMetadata(world, i14, 6, k2, this.brickBlock, this.brickMeta);

                        for (int var140 = 7; var140 <= 9; var140++) {
                           this.setAir(world, i14, var140, k2);
                        }

                        if (i15 > 1 && k3 > 1) {
                           this.setBlockAndMetadata(world, i14, 7, k2, this.brick2Block, this.brick2Meta);
                           this.setBlockAndMetadata(world, i14, 8, k2, this.brick2SlabBlock, this.brick2SlabMeta);
                        } else {
                           this.setBlockAndMetadata(world, i14, 7, k2, this.brick2WallBlock, this.brick2WallMeta);
                           if (i16 == 10 || k4 == 10) {
                              if (i16 <= 10 && k4 <= 10) {
                                 this.setAir(world, i14, 7, k2);
                              } else {
                                 this.setBlockAndMetadata(world, i14, 8, k2, vanilla("field_150478_aa"), 5);
                              }
                           }
                        }
                     } else {
                        for (int i17 = 0; (i17 == 0 || !this.isOpaque(world, i14, i17, k2)) && this.getY(i17) >= 0; i17--) {
                           this.setBlockAndMetadata(world, i14, i17, k2, this.brickBlock, this.brickMeta);
                           this.setGrassToDirt(world, i14, i17 - 1, k2);
                        }

                        for (int var138 = 1; var138 <= 9; var138++) {
                           this.setAir(world, i14, var138, k2);
                        }

                        this.setBlockAndMetadata(world, i14, 6, k2, this.plankBlock, this.plankMeta);
                        if (i16 == 9 && k4 == 9 || i16 == 11 && k4 == 11) {
                           this.setBlockAndMetadata(world, i14, 5, k2, got("chandelier"), 2);
                        }
                     }
                  }
               }
            }

            for (int i13 = 1; i13 <= 8; i13++) {
               this.setBlockAndMetadata(world, i11, i13, i12, this.woodBeamBlock, this.woodBeamMeta);
            }

            this.setBlockAndMetadata(world, i11, 9, i12, this.plankSlabBlock, this.plankSlabMeta);
            this.setBlockAndMetadata(world, i11, 8, i12 - 1, vanilla("field_150478_aa"), 4);
            this.setBlockAndMetadata(world, i11, 8, i12 + 1, vanilla("field_150478_aa"), 3);
            this.setBlockAndMetadata(world, i11 - 1, 8, i12, vanilla("field_150478_aa"), 1);
            this.setBlockAndMetadata(world, i11 + 1, 8, i12, vanilla("field_150478_aa"), 2);
            if (i11 < 0) {
               for (int var128 = 1; var128 <= 5; var128++) {
                  this.setBlockAndMetadata(world, i11 + 1, var128, i12, vanilla("field_150468_ap"), 4);
               }

               this.setBlockAndMetadata(world, i11 + 1, 6, i12, this.trapdoorBlock, 11);
            }

            if (i11 > 0) {
               for (int var129 = 1; var129 <= 5; var129++) {
                  this.setBlockAndMetadata(world, i11 - 1, var129, i12, vanilla("field_150468_ap"), 5);
               }

               this.setBlockAndMetadata(world, i11 - 1, 6, i12, this.trapdoorBlock, 10);
            }

            if (i12 < 0) {
               for (int var130 = 1; var130 <= 5; var130++) {
                  this.setBlockAndMetadata(world, i11, var130, i12 + 1, vanilla("field_150468_ap"), 3);
               }

               this.setBlockAndMetadata(world, i11, 6, i12 + 1, this.trapdoorBlock, 8);
            }

            if (i12 > 0) {
               for (int var131 = 1; var131 <= 5; var131++) {
                  this.setBlockAndMetadata(world, i11, var131, i12 - 1, vanilla("field_150468_ap"), 2);
               }

               this.setBlockAndMetadata(world, i11, 6, i12 - 1, this.trapdoorBlock, 9);
            }
         }
      }

      for (int i11 : new int[]{-11, 11}) {
         int i12 = i11 + Integer.signum(i11) * -1;

         for (int i13 : new int[]{-4, 3}) {
            this.setBlockAndMetadata(world, i11, 2, i13, this.brickStairBlock, 3);
            this.setBlockAndMetadata(world, i11, 2, i13 + 1, this.brickStairBlock, 2);
            this.setBlockAndMetadata(world, i11, 4, i13, this.brickStairBlock, 7);
            this.setBlockAndMetadata(world, i11, 4, i13 + 1, this.brickStairBlock, 6);

            for (int i14 = i13; i14 <= i13 + 1; i14++) {
               this.setAir(world, i11, 3, i14);
               this.setBlockAndMetadata(world, i12, 3, i14, this.brickCarved, this.brickCarvedMeta);
            }
         }

         this.setBlockAndMetadata(world, i11, 2, -1, this.brickStairBlock, 3);
         this.setBlockAndMetadata(world, i11, 2, 0, this.brickSlabBlock, this.brickSlabMeta);
         this.setBlockAndMetadata(world, i11, 2, 1, this.brickStairBlock, 2);
         this.setBlockAndMetadata(world, i11, 4, -1, this.brickStairBlock, 7);
         this.setBlockAndMetadata(world, i11, 4, 0, this.brickSlabBlock, this.brickSlabMeta | 8);
         this.setBlockAndMetadata(world, i11, 4, 1, this.brickStairBlock, 6);

         for (int k2 = -1; k2 <= 1; k2++) {
            this.setAir(world, i11, 3, k2);
            this.setBlockAndMetadata(world, i12, 3, k2, this.brickCarved, this.brickCarvedMeta);
         }
      }

      for (int i11 : new int[]{-11, 11}) {
         int k2 = i11 + Integer.signum(i11) * -1;

         for (int i12 : new int[]{-4, 3}) {
            this.setBlockAndMetadata(world, i12, 2, i11, this.brickStairBlock, 0);
            this.setBlockAndMetadata(world, i12 + 1, 2, i11, this.brickStairBlock, 1);
            this.setBlockAndMetadata(world, i12, 4, i11, this.brickStairBlock, 4);
            this.setBlockAndMetadata(world, i12 + 1, 4, i11, this.brickStairBlock, 5);

            for (int i13 = i12; i13 <= i12 + 1; i13++) {
               this.setAir(world, i13, 3, i11);
               this.setBlockAndMetadata(world, i13, 3, k2, this.brickCarved, this.brickCarvedMeta);
            }
         }

         if (i11 > 0) {
            this.setBlockAndMetadata(world, -1, 2, i11, this.brickStairBlock, 0);
            this.setBlockAndMetadata(world, 0, 2, i11, this.brickSlabBlock, this.brickSlabMeta);
            this.setBlockAndMetadata(world, 1, 2, i11, this.brickStairBlock, 1);
            this.setBlockAndMetadata(world, -1, 4, i11, this.brickStairBlock, 4);
            this.setBlockAndMetadata(world, 0, 4, i11, this.brickSlabBlock, this.brickSlabMeta | 8);
            this.setBlockAndMetadata(world, 1, 4, i11, this.brickStairBlock, 5);

            for (int i12 = -1; i12 <= 1; i12++) {
               this.setAir(world, i12, 3, i11);
               this.setBlockAndMetadata(world, i12, 3, k2, this.brickCarved, this.brickCarvedMeta);
            }
         }
      }

      for (int i11 : new int[]{-14, 14}) {
         int k2 = i11 + Integer.signum(i11) * -1;

         for (int i12 : new int[]{-10, 10}) {
            this.setBlockAndMetadata(world, i12 - 1, 3, i11, this.brickStairBlock, 0);
            this.setBlockAndMetadata(world, i12, 3, i11, this.brick2WallBlock, this.brick2WallMeta);
            this.setBlockAndMetadata(world, i12 + 1, 3, i11, this.brickStairBlock, 1);
            this.setBlockAndMetadata(world, i12 - 1, 4, i11, this.brickStairBlock, 4);
            this.setBlockAndMetadata(world, i12, 4, i11, this.brick2WallBlock, this.brick2WallMeta);
            this.setBlockAndMetadata(world, i12 + 1, 4, i11, this.brickStairBlock, 5);
            this.setBlockAndMetadata(world, i12 - 1, 1, k2, this.brickStairBlock, 1);
            this.setBlockAndMetadata(world, i12, 1, k2, this.brickBlock, this.brickMeta);
            this.setBlockAndMetadata(world, i12 + 1, 1, k2, this.brickStairBlock, 0);
            this.setBlockAndMetadata(world, i12, 2, k2, this.brickSlabBlock, this.brickSlabMeta);
         }
      }

      for (int i11 : new int[]{-14, 14}) {
         int i12 = i11 + Integer.signum(i11) * -1;

         for (int i13 : new int[]{-10, 10}) {
            this.setBlockAndMetadata(world, i11, 3, i13 - 1, this.brickStairBlock, 3);
            this.setBlockAndMetadata(world, i11, 3, i13, this.brick2WallBlock, this.brick2WallMeta);
            this.setBlockAndMetadata(world, i11, 3, i13 + 1, this.brickStairBlock, 2);
            this.setBlockAndMetadata(world, i11, 4, i13 - 1, this.brickStairBlock, 7);
            this.setBlockAndMetadata(world, i11, 4, i13, this.brick2WallBlock, this.brick2WallMeta);
            this.setBlockAndMetadata(world, i11, 4, i13 + 1, this.brickStairBlock, 6);
            this.setBlockAndMetadata(world, i12, 1, i13 - 1, this.brickStairBlock, 2);
            this.setBlockAndMetadata(world, i12, 1, i13, this.brickBlock, this.brickMeta);
            this.setBlockAndMetadata(world, i12, 1, i13 + 1, this.brickStairBlock, 3);
            this.setBlockAndMetadata(world, i12, 2, i13, this.brickSlabBlock, this.brickSlabMeta);
         }
      }

      for (int i11 : new int[]{-10, 10}) {
         for (int i12 : new int[]{i11 - 2, i11 + 2}) {
            this.placeArmorStand(world, i12, 1, -7, 0, getRandArmorItems(random));
            this.placeArmorStand(world, i12, 1, 7, 2, getRandArmorItems(random));
         }

         this.placeChest(world, random, i11, 1, -6, this.getChest(), 2, this.getChestContents(), 1);
         this.setAir(world, i11, 2, -6);
         this.spawnItemFrame(world, i11, 3, -6, 2, getRandFrameItem(random));
         this.placeChest(world, random, i11, 1, 6, this.getChest(), 3, this.getChestContents(), 1);
         this.setAir(world, i11, 2, 6);
         this.spawnItemFrame(world, i11, 3, 6, 0, getRandFrameItem(random));
      }

      for (int i11 : new int[]{-10, 10}) {
         for (int k2 : new int[]{i11 - 2, i11 + 2}) {
            this.placeArmorStand(world, -7, 1, k2, 1, getRandArmorItems(random));
            this.placeArmorStand(world, 7, 1, k2, 3, getRandArmorItems(random));
         }

         this.placeChest(world, random, -6, 1, i11, this.getChest(), 5, this.getChestContents(), 1);
         this.setAir(world, -6, 2, i11);
         this.spawnItemFrame(world, -6, 3, i11, 3, getRandFrameItem(random));
         this.placeChest(world, random, 6, 1, i11, this.getChest(), 4, this.getChestContents(), 1);
         this.setAir(world, 6, 2, i11);
         this.spawnItemFrame(world, 6, 3, i11, 1, getRandFrameItem(random));
      }

      for (int i9 = 1; i9 <= 4; i9++) {
         for (int i11 = -1; i11 <= 1; i11++) {
            this.setBlockAndMetadata(world, i11, i9, -10, this.gateBlock, 2);
            this.setAir(world, i11, i9, -9);
            this.setAir(world, i11, i9, -11);
         }

         this.setBlockAndMetadata(world, -2, i9, -9, this.pillarBlock, this.pillarMeta);
         this.setBlockAndMetadata(world, 2, i9, -9, this.pillarBlock, this.pillarMeta);
      }

      for (int i8 = -1; i8 <= 1; i8++) {
         int i11 = -12;

         for (int i12 = 0; (i12 <= 0 || !this.isOpaque(world, i8, i12, i11)) && this.getY(i12) >= 0; i12--) {
            this.setBlockAndMetadata(world, i8, i12, i11, this.brickBlock, this.brickMeta);
            this.setGrassToDirt(world, i8, i12 - 1, i11);
         }
      }

      this.placeWallBanner(world, -2, 4, -11, this.bannerType, 2);
      this.placeWallBanner(world, 2, 4, -11, this.bannerType, 2);

      for (int var33 = -2; var33 <= 2; var33++) {
         for (int i11 = -2; i11 <= 2; i11++) {
            this.setBlockAndMetadata(world, var33, 0, i11, this.brick2Block, this.brick2Meta);
         }
      }

      for (int var34 = -8; var34 <= 8; var34++) {
         this.setBlockAndMetadata(world, var34, 0, 0, this.brick2Block, this.brick2Meta);
      }

      for (int i7 = -12; i7 <= 8; i7++) {
         this.setBlockAndMetadata(world, 0, 0, i7, this.brick2Block, this.brick2Meta);
      }

      this.setBlockAndMetadata(world, 0, 0, 0, got("brick4"), 6);

      for (int i6 = 1; i6 <= 4; i6++) {
         this.setBlockAndMetadata(world, -1, i6, -1, this.brickWallBlock, this.brickWallMeta);
         this.setBlockAndMetadata(world, 1, i6, -1, this.brickWallBlock, this.brickWallMeta);
         this.setBlockAndMetadata(world, -1, i6, 1, this.brickWallBlock, this.brickWallMeta);
         this.setBlockAndMetadata(world, 1, i6, 1, this.brickWallBlock, this.brickWallMeta);
      }

      this.setBlockAndMetadata(world, -1, 5, -1, this.brickStairBlock, 2);
      this.setBlockAndMetadata(world, 0, 5, -1, this.brickStairBlock, 2);
      this.setBlockAndMetadata(world, 1, 5, -1, this.brickStairBlock, 2);
      this.setBlockAndMetadata(world, -1, 5, 0, this.brickStairBlock, 1);
      this.setBlockAndMetadata(world, 0, 5, 0, this.brickBlock, this.brickMeta);
      this.setBlockAndMetadata(world, 1, 5, 0, this.brickStairBlock, 0);
      this.setBlockAndMetadata(world, -1, 5, 1, this.brickStairBlock, 3);
      this.setBlockAndMetadata(world, 0, 5, 1, this.brickStairBlock, 3);
      this.setBlockAndMetadata(world, 1, 5, 1, this.brickStairBlock, 3);

      for (int var48 = 6; var48 <= 9; var48++) {
         this.setBlockAndMetadata(world, 0, var48, 0, this.pillarBlock, this.pillarMeta);
      }

      this.setBlockAndMetadata(world, 0, 10, 0, this.brickCarved, this.brickCarvedMeta);
      this.setBlockAndMetadata(world, 0, 11, 0, got("beacon"), 0);
      this.placeWallBanner(world, 0, 9, 0, this.bannerType, 0);
      this.placeWallBanner(world, 0, 9, 0, this.bannerType, 1);
      this.placeWallBanner(world, 0, 9, 0, this.bannerType, 2);
      this.placeWallBanner(world, 0, 9, 0, this.bannerType, 3);
      this.setBlockAndMetadata(world, 0, 4, 0, got("chandelier"), 2);
      this.setBlockAndMetadata(world, -3, 3, -8, this.brickWallBlock, this.brickWallMeta);
      this.setBlockAndMetadata(world, -3, 4, -8, vanilla("field_150478_aa"), 5);
      this.setBlockAndMetadata(world, 3, 3, -8, this.brickWallBlock, this.brickWallMeta);
      this.setBlockAndMetadata(world, 3, 4, -8, vanilla("field_150478_aa"), 5);
      this.setBlockAndMetadata(world, -8, 3, -3, this.brickWallBlock, this.brickWallMeta);
      this.setBlockAndMetadata(world, -8, 4, -3, vanilla("field_150478_aa"), 5);
      this.setBlockAndMetadata(world, 8, 3, -3, this.brickWallBlock, this.brickWallMeta);
      this.setBlockAndMetadata(world, 8, 4, -3, vanilla("field_150478_aa"), 5);

      for (int i5 = -7; i5 <= 7; i5++) {
         int i11 = Math.abs(i5);
         if (i11 >= 2) {
            for (int i12 = -7; i12 <= -2; i12++) {
               int k2 = i12 + 9;
               int d = Math.abs(i11 - k2);
               if (d == 0 && (i11 == 2 || i11 == 7)) {
                  d = 2;
               }

               if (d <= 2) {
                  this.setBlockAndMetadata(world, i5, 0, i12, vanilla("field_150349_c"), 0);
                  switch (d) {
                     case 0:
                        this.setBlockAndMetadata(world, i5, 1, i12, vanilla("field_150398_cm"), 4);
                        this.setBlockAndMetadata(world, i5, 2, i12, vanilla("field_150398_cm"), 8);
                        break;
                     case 1:
                        this.setBlockAndMetadata(world, i5, 1, i12, vanilla("field_150328_O"), 6);
                        break;
                     case 2:
                        this.setBlockAndMetadata(world, i5, 1, i12, vanilla("field_150328_O"), 4);
                  }
               }
            }
         }
      }

      this.setBlockAndMetadata(world, -7, 0, 1, this.brick2Block, this.brick2Meta);

      for (int i4 = 0; i4 <= 2; i4++) {
         this.setBlockAndMetadata(world, -7, 1 + i4, 2 + i4, this.brickStairBlock, 2);

         for (int i11 = 1; i11 < 1 + i4; i11++) {
            this.setBlockAndMetadata(world, -7, i11, 2 + i4, this.brickBlock, this.brickMeta);
         }
      }

      for (int i3 = 1; i3 <= 3; i3++) {
         this.setBlockAndMetadata(world, -7, i3, 5, this.brickBlock, this.brickMeta);
         this.setBlockAndMetadata(world, -7, i3, 6, this.brickBlock, this.brickMeta);
         this.setBlockAndMetadata(world, -8, i3, 4, this.brickBlock, this.brickMeta);
         this.setBlockAndMetadata(world, -8, i3, 5, this.brickBlock, this.brickMeta);
      }

      for (int step = 0; step <= 2; step++) {
         this.setBlockAndMetadata(world, -8, 4 + step, 3 - step, this.brickStairBlock, 3);

         for (int i11 = 1; i11 < 4 + step; i11++) {
            this.setBlockAndMetadata(world, -8, i11, 3 - step, this.brickBlock, this.brickMeta);
         }
      }

      for (int i2 = -1; i2 <= 0; i2++) {
         this.setBlockAndMetadata(world, -8, 5, i2, this.brickStairBlock, 4);
         this.setBlockAndMetadata(world, -8, 6, i2, this.brickBlock, this.brickMeta);
      }

      this.setAir(world, -9, 7, 0);
      this.setAir(world, -9, 7, 1);
      this.setBlockAndMetadata(world, -8, 7, -1, this.brick2WallBlock, this.brick2WallMeta);

      for (int n = 6; n <= 8; n++) {
         for (int i11 = -1; i11 <= 3; i11++) {
            this.setBlockAndMetadata(world, n, 0, i11, this.brick2Block, this.brick2Meta);
            if (n >= 7 && i11 >= 0 && i11 <= 2) {
               this.setBlockAndMetadata(world, n, 4, i11, this.plankSlabBlock, this.plankSlabMeta | 8);
            } else {
               this.setBlockAndMetadata(world, n, 4, i11, this.plankSlabBlock, this.plankSlabMeta);
            }
         }
      }

      for (int j1 = 1; j1 <= 3; j1++) {
         this.setBlockAndMetadata(world, 6, j1, -1, this.fenceBlock, this.fenceMeta);
         this.setBlockAndMetadata(world, 6, j1, 3, this.fenceBlock, this.fenceMeta);
      }

      for (int m = 7; m <= 8; m++) {
         this.setBlockAndMetadata(world, m, 3, -1, this.fenceBlock, this.fenceMeta);
         this.setBlockAndMetadata(world, m, 3, 3, this.fenceBlock, this.fenceMeta);
      }

      for (int k1 = 0; k1 <= 2; k1++) {
         this.setBlockAndMetadata(world, 6, 3, k1, this.fenceBlock, this.fenceMeta);
      }

      this.setBlockAndMetadata(world, 8, 1, -1, got("alloyForge"), 5);
      this.setBlockAndMetadata(world, 8, 2, -1, vanilla("field_150460_al"), 5);
      this.setBlockAndMetadata(world, 8, 1, 1, this.tableBlock, 0);
      this.placeChest(world, random, 8, 1, 2, this.getChest(), 5, this.getChestContents());
      this.setBlockAndMetadata(world, 8, 1, 3, vanilla("field_150462_ai"), 0);
      this.spawnItemFrame(world, 9, 2, 1, 3, getRandFrameItem(random));
      this.spawnItemFrame(world, 9, 2, 2, 3, getRandFrameItem(random));
      this.setBlockAndMetadata(world, 6, 1, 1, vanilla("field_150467_bQ"), 0);
      this.setBlockAndMetadata(world, 8, 3, 1, vanilla("field_150478_aa"), 1);

      for (int i1 = -5; i1 <= 5; i1++) {
         for (int i11 = 4; i11 <= 8; i11++) {
            int i12 = Math.abs(i1);
            if (i12 != 5 || i11 != 4) {
               if (i12 <= 4 && i11 >= 5) {
                  this.setBlockAndMetadata(world, i1, 0, i11, this.plankBlock, this.plankMeta);
                  this.setBlockAndMetadata(world, i1, 4, i11, this.brickBlock, this.brickMeta);
               } else {
                  if (i12 != 1 && i12 != 4 && i11 != 5) {
                     this.setBlockAndMetadata(world, i1, 1, i11, this.brickBlock, this.brickMeta);

                     for (int i13 = 2; i13 <= 3; i13++) {
                        this.setBlockAndMetadata(world, i1, i13, i11, this.plankBlock, this.plankMeta);
                     }
                  } else {
                     for (int i13 = 1; i13 <= 3; i13++) {
                        this.setBlockAndMetadata(world, i1, i13, i11, this.woodBeamBlock, this.woodBeamMeta);
                     }
                  }

                  this.setBlockAndMetadata(world, i1, 4, i11, this.brickSlabBlock, this.brickSlabMeta);
               }
            }
         }
      }

      this.setBlockAndMetadata(world, 0, 0, 4, this.plankBlock, this.plankMeta);
      this.setBlockAndMetadata(world, 0, 1, 4, this.doorBlock, 1);
      this.setBlockAndMetadata(world, 0, 2, 4, this.doorBlock, 8);

      for (int var68 = -4; var68 <= 4; var68++) {
         if (Math.floorMod(var68, 2) == 0) {
            this.setBlockAndMetadata(world, var68, 1, 7, this.bedBlock, 0);
            this.setBlockAndMetadata(world, var68, 1, 8, this.bedBlock, 8);
         } else {
            this.placeChest(world, random, var68, 1, 8, this.getChest(), 2, this.getChestContents());
         }
      }

      this.placeWallBanner(world, -2, 3, 9, this.bannerType, 2);
      this.placeWallBanner(world, 2, 3, 9, this.bannerType, 2);
      this.setBlockAndMetadata(world, -4, 1, 5, this.plankBlock, this.plankMeta);
      this.setBlockAndMetadata(world, -3, 1, 5, this.plankBlock, this.plankMeta);
      this.placeBarrel(world, random, -4, 2, 5, 3, "food_DEFAULT_DRINK");
      this.placeMug(world, random, -3, 2, 5, 2, "food_DEFAULT_DRINK");
      this.setBlockAndMetadata(world, 3, 1, 5, this.plankBlock, this.plankMeta);
      this.setBlockAndMetadata(world, 4, 1, 5, this.plankBlock, this.plankMeta);
      this.placePlateWithCertainty(world, random, 3, 2, 5, this.plateBlock, "food_DEFAULT");
      this.placePlateWithCertainty(world, random, 4, 2, 5, this.plateBlock, "food_DEFAULT");
      this.setBlockAndMetadata(world, -3, 3, 6, got("chandelier"), 2);
      this.setBlockAndMetadata(world, 3, 3, 6, got("chandelier"), 2);
      this.setBlockAndMetadata(world, -5, 1, 2, got("commandTable"), 0);
      if (this.hasMaester()) {
         LegacyEntity maester = new LegacyEntity(world);
         maester.setSpawnRidingHorse(false);
         this.spawnNPCAndSetHome(maester, world, 0, 1, 0, 12);
      }

      if (this.hasSepton()) {
         LegacyEntity septon = new LegacyEntity(world);
         septon.setSpawnRidingHorse(false);
         this.spawnNPCAndSetHome(septon, world, 0, 1, 0, 12);
      }

      if (this.kingdom == Kingdom.DRAGONSTONE) {
         LegacyEntity priest = new LegacyEntity(world);
         priest.setSpawnRidingHorse(false);
         this.spawnNPCAndSetHome(priest, world, 0, 1, 0, 12);
      } else if (this.kingdom == Kingdom.IRONBORN) {
         LegacyEntity priest = new LegacyEntity(world);
         priest.setSpawnRidingHorse(false);
         this.spawnNPCAndSetHome(priest, world, 0, 1, 0, 12);
      } else if (this.kingdom == Kingdom.CROWNLANDS_RED) {
         this.spawnLegendaryNPC(new LegacyEntity(world), world, -2, 1, -2);
         this.spawnLegendaryNPC(new LegacyEntity(world), world, 2, 1, -2);
         this.spawnLegendaryNPC(new LegacyEntity(world), world, 2, 1, 2);
         this.spawnLegendaryNPC(new LegacyEntity(world), world, -2, 1, 2);
         this.spawnLegendaryNPC(new LegacyEntity(world), world, 2, 1, 0);
         this.spawnLegendaryNPC(new LegacyEntity(world), world, -2, 1, 0);
         this.spawnLegendaryNPC(new LegacyEntity(world), world, 0, 1, 2);
         this.spawnLegendaryNPC(new LegacyEntity(world), world, 0, 1, -2);
         this.spawnLegendaryNPC(new LegacyEntity(world), world, -1, 1, 2);
         this.spawnLegendaryNPC(new LegacyEntity(world), world, -2, 1, 1);
         this.spawnLegendaryNPC(new LegacyEntity(world), world, -1, 1, -2);
         this.spawnLegendaryNPC(new LegacyEntity(world), world, -1, 1, 1);
         this.spawnLegendaryNPC(new LegacyEntity(world), world, -1, 1, 0);
      } else {
         LegacyEntity captain = this.getCaptain(world);
         captain.setSpawnRidingHorse(false);
         this.spawnNPCAndSetHome(captain, world, 0, 1, 0, 12);
         LegacyEntity soldier = this.getSoldier(world);
         soldier.setSpawnRidingHorse(false);
         this.spawnNPCAndSetHome(soldier, world, 0, 1, 0, 16);
      }

      LegacyEntity respawner = new LegacyEntity(world);
      respawner.setSpawnClass1(this.getSoldier(world).getClass());
      respawner.setSpawnClass2(this.getSoldierArcher(world).getClass());
      respawner.setCheckRanges(20, -8, 12, 12);
      respawner.setSpawnRanges(10, 0, 8, 16);
      this.placeNPCRespawner(respawner, world, 0, 0, 0);
      return true;
   }

   public void setupRandomBlocks(Random random) {
      super.setupRandomBlocks(random);
      this.bedBlock = vanilla("field_150324_C");
   }

}
