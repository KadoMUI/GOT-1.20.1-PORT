package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureWesterosWatchfort extends LegacyNorthernContext {

   protected GOTStructureWesterosWatchfort(NorthStructureBuilder builder) {
      super(builder, Style.NORTH);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureWesterosWatchfort piece = new GOTStructureWesterosWatchfort(builder);
      piece.marketVariant = Math.max(0, variant);
      piece.isAbandoned = variant == 1;
      piece.isTramp = variant == 1;
      piece.isHardhome = variant == 1;
      piece.isBlacksmith = variant == 1;
      piece.generate(piece, piece.legacyRandom(), 0, 0, 0, 0);
   }


   public boolean generate(LegacyNorthernContext world, Random random, int i, int j, int k, int rotation) {
      this.setOriginAndRotation(world, i, j, k, rotation, 9);
      this.setupRandomBlocks(random);
      if (this.restrictions) {
         int x1 = -6;
         int x2 = 6;
         int z1 = -6;
         int z2 = 34;

         for (int i18 = x1; i18 <= x2; i18++) {
            for (int k14 = z1; k14 <= z2; k14++) {
               int j15 = this.getTopBlock(world, i18, k14) - 1;
               if (!this.isSurface(world, i18, j15, k14)) {
                  return false;
               }
            }
         }
      }

      for (int i17 = -5; i17 <= 5; i17++) {
         for (int j13 = 1; j13 <= 11; j13++) {
            for (int k13 = -5; k13 <= 5; k13++) {
               if (Math.abs(i17) == 5 && Math.abs(k13) == 5) {
                  this.setBlockAndMetadata(world, i17, j13, k13, this.pillar2Block, this.pillar2Meta);
               } else {
                  this.placeRandomBrick(world, random, i17, j13, k13);
               }
            }
         }
      }

      for (int var81 = -6; var81 <= 6; var81++) {
         this.setBlockAndMetadata(world, var81, 1, -6, this.brick2StairBlock, 2);
         this.setBlockAndMetadata(world, var81, 1, 6, this.brick2StairBlock, 3);
      }

      for (int k15 = -5; k15 <= 5; k15++) {
         this.setBlockAndMetadata(world, -6, 1, k15, this.brick2StairBlock, 1);
         this.setBlockAndMetadata(world, 6, 1, k15, this.brick2StairBlock, 0);
      }

      for (int var82 = -6; var82 <= 6; var82++) {
         for (int k12 = -6; k12 <= 6; k12++) {
            for (int j1 = 0; !this.isOpaque(world, var82, j1, k12) && this.getY(j1) >= 0; j1--) {
               this.placeRandomBrick(world, random, var82, j1, k12);
               this.setGrassToDirt(world, var82, j1 - 1, k12);
            }
         }
      }

      for (int var83 = -4; var83 <= 4; var83++) {
         for (int k12 = -4; k12 <= 4; k12++) {
            for (int j1 = 2; j1 <= 5; j1++) {
               this.setAir(world, var83, j1, k12);
            }

            for (int var50 = 7; var50 <= 10; var50++) {
               this.setAir(world, var83, var50, k12);
            }
         }
      }

      for (int j12 : new int[]{4, 9}) {
         this.setBlockAndMetadata(world, -4, j12, -2, vanilla("field_150478_aa"), 2);
         this.setBlockAndMetadata(world, -4, j12, 2, vanilla("field_150478_aa"), 2);
         this.setBlockAndMetadata(world, 4, j12, -2, vanilla("field_150478_aa"), 1);
         this.setBlockAndMetadata(world, 4, j12, 2, vanilla("field_150478_aa"), 1);
         this.setBlockAndMetadata(world, -2, j12, -4, vanilla("field_150478_aa"), 3);
         this.setBlockAndMetadata(world, 2, j12, -4, vanilla("field_150478_aa"), 3);
         this.setBlockAndMetadata(world, -2, j12, 4, vanilla("field_150478_aa"), 4);
         this.setBlockAndMetadata(world, 2, j12, 4, vanilla("field_150478_aa"), 4);
      }

      for (int i15 = -4; i15 <= 4; i15++) {
         for (int j13 = 12; j13 <= 16; j13++) {
            for (int k13 = -4; k13 <= 4; k13++) {
               if (Math.abs(i15) == 4 && Math.abs(k13) == 4) {
                  this.setBlockAndMetadata(world, i15, j13, k13, this.pillar2Block, this.pillar2Meta);
               } else {
                  this.placeRandomBrick(world, random, i15, j13, k13);
               }
            }
         }
      }

      for (int var75 = -5; var75 <= 5; var75++) {
         this.setBlockAndMetadata(world, var75, 12, -5, this.brick2StairBlock, 2);
         this.setBlockAndMetadata(world, var75, 12, 5, this.brick2StairBlock, 3);
      }

      for (int k1 = -4; k1 <= 4; k1++) {
         this.setBlockAndMetadata(world, -5, 12, k1, this.brick2StairBlock, 1);
         this.setBlockAndMetadata(world, 5, 12, k1, this.brick2StairBlock, 0);
      }

      for (int var76 = -3; var76 <= 3; var76++) {
         for (int var42 = -3; var42 <= 3; var42++) {
            for (int var52 = 12; var52 <= 15; var52++) {
               this.setAir(world, var76, var52, var42);
            }
         }
      }

      this.setBlockAndMetadata(world, -3, 14, -2, vanilla("field_150478_aa"), 2);
      this.setBlockAndMetadata(world, -3, 14, 2, vanilla("field_150478_aa"), 2);
      this.setBlockAndMetadata(world, 3, 14, -2, vanilla("field_150478_aa"), 1);
      this.setBlockAndMetadata(world, 3, 14, 2, vanilla("field_150478_aa"), 1);
      this.setBlockAndMetadata(world, -2, 14, -3, vanilla("field_150478_aa"), 3);
      this.setBlockAndMetadata(world, 2, 14, -3, vanilla("field_150478_aa"), 3);
      this.setBlockAndMetadata(world, -2, 14, 3, vanilla("field_150478_aa"), 4);
      this.setBlockAndMetadata(world, 2, 14, 3, vanilla("field_150478_aa"), 4);

      for (int var77 = -4; var77 <= 4; var77++) {
         this.placeRandomWall(world, random, var77, 17, -4);
         this.placeRandomWall(world, random, var77, 17, 4);
      }

      for (int var39 = -4; var39 <= 4; var39++) {
         this.placeRandomWall(world, random, -4, 17, var39);
         this.placeRandomWall(world, random, 4, 17, var39);
      }

      for (int i162 : new int[]{-4, 4}) {
         for (int k17 : new int[]{-4, 4}) {
            for (int j16 = 17; j16 <= 19; j16++) {
               this.setBlockAndMetadata(world, i162, j16, k17, this.pillar2Block, this.pillar2Meta);
            }

            this.placeRandomBrick(world, random, i162, 20, k17);
         }
      }

      this.setBlockAndMetadata(world, -4, 19, -3, vanilla("field_150478_aa"), 3);
      this.setBlockAndMetadata(world, 4, 19, -3, vanilla("field_150478_aa"), 3);
      this.setBlockAndMetadata(world, -4, 19, 3, vanilla("field_150478_aa"), 4);
      this.setBlockAndMetadata(world, 4, 19, 3, vanilla("field_150478_aa"), 4);

      for (int i12 = -2; i12 <= 2; i12++) {
         for (int var44 = -2; var44 <= 2; var44++) {
            this.setBlockAndMetadata(world, i12, 21, var44, this.brick2Block, this.brick2Meta);
            if (Math.abs(i12) <= 1 && Math.abs(var44) <= 1) {
               this.setBlockAndMetadata(world, i12, 22, var44, this.brick2Block, this.brick2Meta);
            } else {
               this.setBlockAndMetadata(world, i12, 22, var44, this.brick2SlabBlock, this.brick2SlabMeta);
            }
         }
      }

      for (int var72 = -5; var72 <= 5; var72++) {
         for (int var45 = -5; var45 <= 5; var45++) {
            this.setBlockAndMetadata(world, var72, 21, var45, this.brick2SlabBlock, this.brick2SlabMeta);
         }
      }

      for (int i162 : new int[]{-4, 4}) {
         for (int k17 : new int[]{-4, 4}) {
            this.setBlockAndMetadata(world, i162, 20, k17 - 1, this.brick2StairBlock, 6);
            this.setBlockAndMetadata(world, i162, 20, k17 + 1, this.brick2StairBlock, 7);

            for (int k2 = k17 - 1; k2 <= k17 + 1; k2++) {
               this.setBlockAndMetadata(world, i162 - 1, 20, k2, this.brick2StairBlock, 5);
               this.setBlockAndMetadata(world, i162 + 1, 20, k2, this.brick2StairBlock, 4);
            }
         }
      }

      this.setBlockAndMetadata(world, -4, 21, -4, this.brick2SlabBlock, this.brick2SlabMeta);
      this.setBlockAndMetadata(world, -4, 21, -3, this.brick2SlabBlock, this.brick2SlabMeta);
      this.setBlockAndMetadata(world, -4, 21, -2, this.brick2StairBlock, 1);
      this.setBlockAndMetadata(world, -4, 21, -1, this.brick2StairBlock, 3);
      this.setBlockAndMetadata(world, -4, 21, 0, this.brick2SlabBlock, this.brick2SlabMeta);
      this.setBlockAndMetadata(world, -4, 21, 1, this.brick2StairBlock, 2);
      this.setBlockAndMetadata(world, -4, 21, 2, this.brick2StairBlock, 1);
      this.setBlockAndMetadata(world, -4, 21, 3, this.brick2SlabBlock, this.brick2SlabMeta);
      this.setBlockAndMetadata(world, -4, 21, 4, this.brick2SlabBlock, this.brick2SlabMeta);
      this.setBlockAndMetadata(world, -3, 21, -4, this.brick2SlabBlock, this.brick2SlabMeta);
      this.setBlockAndMetadata(world, -3, 21, -3, this.brick2Block, this.brick2Meta);
      this.setBlockAndMetadata(world, -3, 21, -2, this.brick2Block, this.brick2Meta);
      this.setBlockAndMetadata(world, -3, 21, -1, this.brick2StairBlock, 1);
      this.setBlockAndMetadata(world, -3, 21, 0, this.brick2StairBlock, 1);
      this.setBlockAndMetadata(world, -3, 21, 1, this.brick2StairBlock, 1);
      this.setBlockAndMetadata(world, -3, 21, 2, this.brick2Block, this.brick2Meta);
      this.setBlockAndMetadata(world, -3, 21, 3, this.brick2Block, this.brick2Meta);
      this.setBlockAndMetadata(world, -3, 21, 4, this.brick2SlabBlock, this.brick2SlabMeta);
      this.setBlockAndMetadata(world, 4, 21, -4, this.brick2SlabBlock, this.brick2SlabMeta);
      this.setBlockAndMetadata(world, 4, 21, -3, this.brick2SlabBlock, this.brick2SlabMeta);
      this.setBlockAndMetadata(world, 4, 21, -2, this.brick2StairBlock, 0);
      this.setBlockAndMetadata(world, 4, 21, -1, this.brick2StairBlock, 3);
      this.setBlockAndMetadata(world, 4, 21, 0, this.brick2SlabBlock, this.brick2SlabMeta);
      this.setBlockAndMetadata(world, 4, 21, 1, this.brick2StairBlock, 2);
      this.setBlockAndMetadata(world, 4, 21, 2, this.brick2StairBlock, 0);
      this.setBlockAndMetadata(world, 4, 21, 3, this.brick2SlabBlock, this.brick2SlabMeta);
      this.setBlockAndMetadata(world, 4, 21, 4, this.brick2SlabBlock, this.brick2SlabMeta);
      this.setBlockAndMetadata(world, 3, 21, -4, this.brick2SlabBlock, this.brick2SlabMeta);
      this.setBlockAndMetadata(world, 3, 21, -3, this.brick2Block, this.brick2Meta);
      this.setBlockAndMetadata(world, 3, 21, -2, this.brick2Block, this.brick2Meta);
      this.setBlockAndMetadata(world, 3, 21, -1, this.brick2StairBlock, 0);
      this.setBlockAndMetadata(world, 3, 21, 0, this.brick2StairBlock, 0);
      this.setBlockAndMetadata(world, 3, 21, 1, this.brick2StairBlock, 0);
      this.setBlockAndMetadata(world, 3, 21, 2, this.brick2Block, this.brick2Meta);
      this.setBlockAndMetadata(world, 3, 21, 3, this.brick2Block, this.brick2Meta);
      this.setBlockAndMetadata(world, 3, 21, 4, this.brick2SlabBlock, this.brick2SlabMeta);
      this.setBlockAndMetadata(world, -2, 21, 4, this.brick2StairBlock, 3);
      this.setBlockAndMetadata(world, -1, 21, 4, this.brick2StairBlock, 0);
      this.setBlockAndMetadata(world, 0, 21, 4, this.brick2SlabBlock, this.brick2SlabMeta);
      this.setBlockAndMetadata(world, 1, 21, 4, this.brick2StairBlock, 1);
      this.setBlockAndMetadata(world, 2, 21, 4, this.brick2StairBlock, 3);
      this.setBlockAndMetadata(world, -2, 21, 3, this.brick2Block, this.brick2Meta);
      this.setBlockAndMetadata(world, -1, 21, 3, this.brick2StairBlock, 3);
      this.setBlockAndMetadata(world, 0, 21, 3, this.brick2StairBlock, 3);
      this.setBlockAndMetadata(world, 1, 21, 3, this.brick2StairBlock, 3);
      this.setBlockAndMetadata(world, 2, 21, 3, this.brick2Block, this.brick2Meta);
      this.setBlockAndMetadata(world, -2, 21, -4, this.brick2StairBlock, 2);
      this.setBlockAndMetadata(world, -1, 21, -4, this.brick2StairBlock, 0);
      this.setBlockAndMetadata(world, 0, 21, -4, this.brick2SlabBlock, this.brick2SlabMeta);
      this.setBlockAndMetadata(world, 1, 21, -4, this.brick2StairBlock, 1);
      this.setBlockAndMetadata(world, 2, 21, -4, this.brick2StairBlock, 2);
      this.setBlockAndMetadata(world, -2, 21, -3, this.brick2Block, this.brick2Meta);
      this.setBlockAndMetadata(world, -1, 21, -3, this.brick2StairBlock, 2);
      this.setBlockAndMetadata(world, 0, 21, -3, this.brick2StairBlock, 2);
      this.setBlockAndMetadata(world, 1, 21, -3, this.brick2StairBlock, 2);
      this.setBlockAndMetadata(world, 2, 21, -3, this.brick2Block, this.brick2Meta);
      this.placeBarredWindowOnZ(world, -5, 3, 0);
      this.placeBarredWindowOnZ(world, 5, 3, 0);
      this.placeBarredWindowOnX(world, 0, 3, -5);
      this.placeBarredWindowOnX(world, 0, 3, 5);
      this.placeBarredWindowOnZ(world, -5, 8, 0);
      this.placeBarredWindowOnZ(world, 5, 8, 0);
      this.placeBarredWindowOnX(world, 0, 8, -5);
      this.placeBarredWindowOnX(world, 0, 8, 5);
      this.placeBarredWindowOnZ(world, -4, 13, 0);
      this.placeBarredWindowOnZ(world, 4, 13, 0);
      this.placeBarredWindowOnX(world, 0, 13, -4);
      this.placeBarredWindowOnX(world, 0, 13, 4);

      for (int i1 = -2; i1 <= 2; i1++) {
         for (int var47 = -8; var47 <= -7; var47++) {
            for (int var55 = 0; !this.isOpaque(world, i1, var55, var47) && this.getY(var55) >= 0; var55--) {
               this.placeRandomBrick(world, random, i1, var55, var47);
               this.setGrassToDirt(world, i1, var55 - 1, var47);
            }
         }
      }

      for (int var68 = -2; var68 <= 2; var68++) {
         if (Math.abs(var68) == 2) {
            for (int j13 = 1; j13 <= 4; j13++) {
               this.setBlockAndMetadata(world, var68, j13, -6, this.pillarBlock, this.pillarMeta);
            }
         }

         this.setBlockAndMetadata(world, var68, 5, -6, this.brick2SlabBlock, this.brick2SlabMeta);
         this.placeRandomStairs(world, random, var68, 1, -8, 2);
      }

      this.placeWallBanner(world, 0, 7, -5, this.bannerType, 2);

      for (int var69 = -1; var69 <= 1; var69++) {
         for (int var48 = -7; var48 <= -6; var48++) {
            this.placeRandomBrick(world, random, var69, 1, var48);
         }

         this.placeRandomBrick(world, random, var69, 1, -5);
         this.setAir(world, var69, 2, -5);
         this.setAir(world, var69, 3, -5);
         this.setAir(world, var69, 4, -5);
      }

      this.placeRandomStairs(world, random, -2, 1, -7, 1);
      this.placeRandomStairs(world, random, 2, 1, -7, 0);

      for (int var70 = -1; var70 <= 1; var70++) {
         for (int j13 = 2; j13 <= 4; j13++) {
            this.setBlockAndMetadata(world, var70, j13, -6, this.gateBlock, 3);
         }
      }

      this.placeRandomSlab(world, random, -4, 2, -4, true);
      this.placeBarrel(world, random, -4, 3, -4, 4, "food_DEFAULT_DRINK");
      this.placeRandomSlab(world, random, -4, 2, -3, true);
      this.placeBarrel(world, random, -4, 3, -3, 4, "food_DEFAULT_DRINK");
      this.placeChest(world, random, -4, 2, -2, this.getChest(), 4, this.getChestContents());
      this.placeRandomSlab(world, random, 4, 2, -4, true);
      this.placeBarrel(world, random, 4, 3, -4, 5, "food_DEFAULT_DRINK");
      this.placeRandomSlab(world, random, 4, 2, -3, true);
      this.placeBarrel(world, random, 4, 3, -3, 5, "food_DEFAULT_DRINK");
      this.placeChest(world, random, 4, 2, -2, this.getChest(), 5, this.getChestContents());
      this.setBlockAndMetadata(world, -4, 2, 4, vanilla("field_150462_ai"), 0);
      this.setBlockAndMetadata(world, 4, 2, 4, this.tableBlock, 0);

      for (int var71 = -1; var71 <= 1; var71++) {
         for (int step = 0; step <= 3; step++) {
            int k13 = -1 + step;
            int j12 = 2 + step;
            this.setAir(world, var71, 6, k13);

            for (int j2 = 2; j2 < j12; j2++) {
               this.placeRandomBrick(world, random, var71, j2, k13);
            }

            this.placeRandomStairs(world, random, var71, j12, k13, 2);
         }

         this.placeRandomStairs(world, random, var71, 6, 3, 2);
      }

      this.placeChest(world, random, 0, 2, 2, this.getChest(), 3, this.getChestContents());
      this.setAir(world, 0, 3, 2);
      this.setBlockAndMetadata(world, 0, 7, -4, got("commandTable"), 0);

      for (int i162 : new int[]{-3, 3}) {
         for (int step2 = 0; step2 <= 4; step2++) {
            int k14 = 2 - step2;
            int j15 = 7 + step2;
            this.setAir(world, i162, 11, k14);

            for (int j22 = 7; j22 < j15; j22++) {
               this.placeRandomBrick(world, random, i162, j22, k14);
            }

            this.placeRandomStairs(world, random, i162, j15, k14, 3);
         }
      }

      for (int i14 = -1; i14 <= 1; i14++) {
         for (int var38 = 0; var38 <= 3; var38++) {
            int var59 = -2 + var38;
            int j12 = 12 + var38;
            this.setAir(world, i14, 16, var59);

            for (int j2 = 12; j2 < j12; j2++) {
               this.placeRandomBrick(world, random, i14, j2, var59);
            }

            this.placeRandomStairs(world, random, i14, j12, var59, 2);
         }

         this.placeRandomStairs(world, random, i14, 16, 2, 2);
      }

      for (int k18 = 5; k18 <= 28; k18++) {
         for (int j13 = 12; j13 <= 15; j13++) {
            for (int i112 = -2; i112 <= 2; i112++) {
               this.setAir(world, i112, j13, k18);
            }
         }
      }

      for (int var74 = -1; var74 <= 1; var74++) {
         this.placeRandomBrick(world, random, var74, 13, 4);
         this.placeRandomBrick(world, random, var74, 14, 4);
      }

      for (int i1621 : new int[]{-2, 2}) {
         this.placeRandomBrick(world, random, i1621, 12, 5);
         this.placeRandomBrick(world, random, i1621, 13, 5);
         this.setBlockAndMetadata(world, i1621, 14, 5, this.brick2WallBlock, this.brick2WallMeta);
         this.setBlockAndMetadata(world, i1621, 15, 5, vanilla("field_150478_aa"), 5);
      }

      this.setBlockAndMetadata(world, 0, 12, 4, this.doorBlock, 3);
      this.setBlockAndMetadata(world, 0, 13, 4, this.doorBlock, 8);

      for (int k19 = 6; k19 <= 28; k19++) {
         for (int i113 = -1; i113 <= 1; i113++) {
            this.placeRandomBrick(world, random, i113, 11, k19);
         }

         this.placeRandomWall(world, random, -2, 12, k19);
         this.placeRandomWall(world, random, 2, 12, k19);
         this.placeRandomStairs(world, random, -2, 11, k19, 5);
         this.placeRandomStairs(world, random, 2, 11, k19, 4);
      }

      for (int i13 = -1; i13 <= 1; i13++) {
         this.placeRandomStairs(world, random, i13, 10, 6, 7);
         this.placeRandomStairs(world, random, i13, 10, 16, 6);
         this.placeRandomStairs(world, random, i13, 10, 18, 7);
         this.placeRandomStairs(world, random, i13, 10, 28, 6);

         for (int j13 = 10; !this.isOpaque(world, i13, j13, 17) && this.getY(j13) >= 0; j13--) {
            this.placeRandomBrick(world, random, i13, j13, 17);
            this.setGrassToDirt(world, i13, j13 - 1, 17);
         }
      }

      for (int j14 = 12; j14 <= 13; j14++) {
         this.placeRandomBrick(world, random, -2, j14, 11);
         this.placeRandomBrick(world, random, 2, j14, 11);
         this.placeRandomBrick(world, random, -2, j14, 23);
         this.placeRandomBrick(world, random, 2, j14, 23);
      }

      this.setBlockAndMetadata(world, -1, 13, 11, vanilla("field_150478_aa"), 2);
      this.setBlockAndMetadata(world, 1, 13, 11, vanilla("field_150478_aa"), 1);
      this.setBlockAndMetadata(world, -1, 13, 23, vanilla("field_150478_aa"), 2);
      this.setBlockAndMetadata(world, 1, 13, 23, vanilla("field_150478_aa"), 1);
      this.placeBanner(world, -2, 14, 11, this.bannerType, 3);
      this.placeBanner(world, 2, 14, 11, this.bannerType, 1);
      this.placeBanner(world, -2, 14, 23, this.bannerType, 3);
      this.placeBanner(world, 2, 14, 23, this.bannerType, 1);

      for (int var78 = 12; var78 <= 14; var78++) {
         this.placeRandomBrick(world, random, -2, var78, 17);
         this.placeRandomBrick(world, random, 2, var78, 17);
      }

      this.placeRandomBrick(world, random, -2, 15, 17);
      this.placeRandomBrick(world, random, 2, 15, 17);
      this.placeRandomStairs(world, random, -1, 15, 17, 4);
      this.placeRandomStairs(world, random, 1, 15, 17, 5);
      this.placeRandomSlab(world, random, 0, 15, 17, true);

      for (int var73 = -1; var73 <= 1; var73++) {
         this.setBlockAndMetadata(world, var73, 16, 17, this.brick2Block, this.brick2Meta);
      }

      this.setBlockAndMetadata(world, -2, 16, 17, this.brick2SlabBlock, this.brick2SlabMeta);
      this.setBlockAndMetadata(world, 2, 16, 17, this.brick2SlabBlock, this.brick2SlabMeta);
      this.setBlockAndMetadata(world, 0, 17, 17, this.brick2SlabBlock, this.brick2SlabMeta);
      this.setBlockAndMetadata(world, -2, 14, 16, vanilla("field_150478_aa"), 4);
      this.setBlockAndMetadata(world, 2, 14, 16, vanilla("field_150478_aa"), 4);
      this.setBlockAndMetadata(world, -2, 14, 18, vanilla("field_150478_aa"), 3);
      this.setBlockAndMetadata(world, 2, 14, 18, vanilla("field_150478_aa"), 3);
      this.placeBeaconTower(-4, 17, 9, 0, 5);
this.setAir(world, -1, 12, 29);
      this.setAir(world, 0, 12, 29);
      this.setAir(world, 1, 12, 29);
      LegacyEntity soldier = this.getSoldier(world);
      soldier.setSpawnRidingHorse(false);
      this.spawnNPCAndSetHome(soldier, world, 0, 2, -3, 32);
      LegacyEntity captain = this.getCaptain(world);
      captain.setSpawnRidingHorse(false);
      this.spawnNPCAndSetHome(captain, world, 0, 15, 0, 8);
      LegacyEntity respawner = new LegacyEntity(world);
      respawner.setSpawnClass1(this.getSoldier(world).getClass());
      respawner.setSpawnClass2(this.getSoldierArcher(world).getClass());
      respawner.setCheckRanges(24, -8, 18, 12);
      respawner.setSpawnRanges(4, 2, 17, 32);
      this.placeNPCRespawner(respawner, world, 0, 2, 0);
      return true;
   }

   private void placeBarredWindowOnX(LegacyNorthernContext world, int i, int j, int k) {
      for (int i1 = -1; i1 <= 1; i1++) {
         for (int j1 = 0; j1 <= 1; j1++) {
            this.setBlockAndMetadata(world, i + i1, j + j1, k, this.barsBlock, 0);
         }
      }
   }

   private void placeBarredWindowOnZ(LegacyNorthernContext world, int i, int j, int k) {
      for (int k1 = -1; k1 <= 1; k1++) {
         for (int j1 = 0; j1 <= 1; j1++) {
            this.setBlockAndMetadata(world, i, j + j1, k + k1, this.barsBlock, 0);
         }
      }
   }

   private void placeRandomBrick(LegacyNorthernContext world, Random random, int i, int j, int k) {
      if (random.nextInt(10) == 0) {
         if (random.nextBoolean()) {
            this.setBlockAndMetadata(world, i, j, k, this.brickMossyBlock, this.brickMossyMeta);
         } else {
            this.setBlockAndMetadata(world, i, j, k, this.brickCrackedBlock, this.brickCrackedMeta);
         }
      } else {
         this.setBlockAndMetadata(world, i, j, k, this.brickBlock, this.brickMeta);
      }
   }

   private void placeRandomSlab(LegacyNorthernContext world, Random random, int i, int j, int k, boolean inverted) {
      int flag = inverted ? 8 : 0;
      if (random.nextInt(10) == 0) {
         if (random.nextBoolean()) {
            this.setBlockAndMetadata(world, i, j, k, this.brickMossySlabBlock, this.brickMossySlabMeta | flag);
         } else {
            this.setBlockAndMetadata(world, i, j, k, this.brickCrackedSlabBlock, this.brickCrackedSlabMeta | flag);
         }
      } else {
         this.setBlockAndMetadata(world, i, j, k, this.brickSlabBlock, this.brickSlabMeta | flag);
      }
   }

   private void placeRandomStairs(LegacyNorthernContext world, Random random, int i, int j, int k, int meta) {
      if (random.nextInt(10) == 0) {
         if (random.nextBoolean()) {
            this.setBlockAndMetadata(world, i, j, k, this.brickMossyStairBlock, meta);
         } else {
            this.setBlockAndMetadata(world, i, j, k, this.brickCrackedStairBlock, meta);
         }
      } else {
         this.setBlockAndMetadata(world, i, j, k, this.brickStairBlock, meta);
      }
   }

   private void placeRandomWall(LegacyNorthernContext world, Random random, int i, int j, int k) {
      if (random.nextInt(10) == 0) {
         if (random.nextBoolean()) {
            this.setBlockAndMetadata(world, i, j, k, this.brickMossyWallBlock, this.brickMossyWallMeta);
         } else {
            this.setBlockAndMetadata(world, i, j, k, this.brickCrackedWallBlock, this.brickCrackedWallMeta);
         }
      } else {
         this.setBlockAndMetadata(world, i, j, k, this.brickWallBlock, this.brickWallMeta);
      }
   }

   public void setupRandomBlocks(Random random) {
      super.setupRandomBlocks(random);
      this.barsBlock = vanilla("field_150411_aY");
   }

}
