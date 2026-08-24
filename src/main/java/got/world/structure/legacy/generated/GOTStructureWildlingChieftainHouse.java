package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureWildlingChieftainHouse extends LegacyNorthernContext {

   protected GOTStructureWildlingChieftainHouse(NorthStructureBuilder builder) {
      super(builder, Style.WILDLING);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureWildlingChieftainHouse piece = new GOTStructureWildlingChieftainHouse(builder);
      piece.marketVariant = Math.max(0, variant);
      piece.isAbandoned = variant == 1;
      piece.isTramp = variant == 1;
      piece.isHardhome = variant == 1;
      piece.isBlacksmith = variant == 1;
      piece.generate(piece, piece.legacyRandom(), 0, 0, 0, 0);
   }


   private boolean isHardhome;

   public boolean generate(LegacyNorthernContext world, Random random, int i, int j, int k, int rotation) {
      this.setOriginAndRotation(world, i, j, k, rotation, 5);
      if (this.restrictions) {
         int minHeight = 0;
         int maxHeight = 0;

         for (int i13 = -5; i13 <= 5; i13++) {
            for (int k15 = -6; k15 <= 6; k15++) {
               int j15 = this.getTopBlock(world, i13, k15);
               this.getBlock(world, i13, j15 - 1, k15);
               if (j15 < minHeight) {
                  minHeight = j15;
               }

               if (j15 > maxHeight) {
                  maxHeight = j15;
               }

               if (maxHeight - minHeight > 4) {
                  return false;
               }
            }
         }
      }

      LegacyBlock woodBlock = vanilla("field_150364_r");
      int woodMeta = 1;
      LegacyBlock plankBlock = vanilla("field_150344_f");
      int plankMeta = 1;
      LegacyBlock slabBlock = vanilla("field_150376_bx");
      int slabMeta = 1;
      LegacyBlock stairBlock = vanilla("field_150485_bF");
      LegacyBlock fenceBlock = vanilla("field_150422_aJ");
      int fenceMeta = 0;
      LegacyBlock doorBlock = got("doorSpruce");
      LegacyBlock floorBlock = vanilla("field_150406_ce");
      int floorMeta = 15;

      for (int i1 = -5; i1 <= 5; i1++) {
         for (int k14 = -6; k14 <= 6; k14++) {
            for (int j14 = 1; j14 <= 10; j14++) {
               this.setAir(world, i1, j14, k14);
            }

            for (int var45 = 0; (var45 == 0 || !this.isOpaque(world, i1, var45, k14)) && this.getY(var45) >= 0; var45--) {
               if (this.getBlock(world, i1, var45 + 1, k14).func_149662_c()) {
                  this.setBlockAndMetadata(world, i1, var45, k14, vanilla("field_150346_d"), 0);
               } else {
                  this.setBlockAndMetadata(world, i1, var45, k14, vanilla("field_150349_c"), 0);
               }

               this.setGrassToDirt(world, i1, var45 - 1, k14);
            }
         }
      }

      for (int var44 = -4; var44 <= 4; var44++) {
         for (int k14 = -5; k14 <= 5; k14++) {
            this.setBlockAndMetadata(world, var44, 0, k14, floorBlock, floorMeta);
            if (random.nextInt(2) == 0) {
               this.setBlockAndMetadata(world, var44, 1, k14, got("thatchFloor"), 0);
            }
         }
      }

      for (int i14 : new int[]{-4, 4}) {
         for (int k13 = -4; k13 <= 4; k13++) {
            this.setBlockAndMetadata(world, i14, 1, k13, woodBlock, woodMeta | 8);
            this.setBlockAndMetadata(world, i14, 4, k13, woodBlock, woodMeta | 8);
         }

         for (int j15 = 1; j15 <= 4; j15++) {
            this.setBlockAndMetadata(world, i14, j15, -5, woodBlock, woodMeta);
            this.setBlockAndMetadata(world, i14, j15, 0, woodBlock, woodMeta);
            this.setBlockAndMetadata(world, i14, j15, 5, woodBlock, woodMeta);
         }
      }

      for (int i14 : new int[]{-3, 3}) {
         for (int k13 = -4; k13 <= 4; k13++) {
            this.setBlockAndMetadata(world, i14, 1, k13, plankBlock, plankMeta);
         }

         for (int j15 = 2; j15 <= 3; j15++) {
            this.setBlockAndMetadata(world, i14, j15, -4, plankBlock, plankMeta);
            this.setBlockAndMetadata(world, i14, j15, -1, plankBlock, plankMeta);
            this.setBlockAndMetadata(world, i14, j15, 1, plankBlock, plankMeta);
            this.setBlockAndMetadata(world, i14, j15, 4, plankBlock, plankMeta);
         }

         this.setBlockAndMetadata(world, i14, 3, -3, stairBlock, 7);
         this.setBlockAndMetadata(world, i14, 3, -2, stairBlock, 6);
         this.setBlockAndMetadata(world, i14, 3, 2, stairBlock, 7);
         this.setBlockAndMetadata(world, i14, 3, 3, stairBlock, 6);

         for (int var49 = 1; var49 <= 5; var49++) {
            this.setBlockAndMetadata(world, i14, var49, 0, woodBlock, woodMeta);
         }

         this.setBlockAndMetadata(world, i14, 1, -5, woodBlock, woodMeta | 4);
         this.setBlockAndMetadata(world, i14, 2, -5, stairBlock, 2);
         this.setBlockAndMetadata(world, i14, 3, -5, stairBlock, 6);
         this.setBlockAndMetadata(world, i14, 4, -5, slabBlock, slabMeta);
      }

      for (int i14 : new int[]{-2, 2}) {
         for (int j15 = 1; j15 <= 3; j15++) {
            this.setBlockAndMetadata(world, i14, j15, -4, plankBlock, plankMeta);
            this.setBlockAndMetadata(world, i14, j15, -5, woodBlock, woodMeta);
         }

         this.setBlockAndMetadata(world, i14, 4, -5, slabBlock, slabMeta);
         this.setBlockAndMetadata(world, i14, 2, -6, vanilla("field_150478_aa"), 4);
         this.setBlockAndMetadata(world, i14, 3, -6, vanilla("field_150465_bP"), 2);
      }

      for (int j1 = 1; j1 <= 3; j1++) {
         this.setBlockAndMetadata(world, -1, j1, -4, woodBlock, woodMeta);
         this.setBlockAndMetadata(world, 1, j1, -4, woodBlock, woodMeta);
      }

      this.setBlockAndMetadata(world, -1, 2, -5, vanilla("field_150478_aa"), 4);
      this.setBlockAndMetadata(world, 1, 2, -5, vanilla("field_150478_aa"), 4);
      this.setBlockAndMetadata(world, -1, 3, -5, stairBlock, 4);
      this.setBlockAndMetadata(world, -1, 4, -5, stairBlock, 1);
      this.setBlockAndMetadata(world, 1, 3, -5, stairBlock, 5);
      this.setBlockAndMetadata(world, 1, 4, -5, stairBlock, 0);
      this.setBlockAndMetadata(world, 0, 1, -4, doorBlock, 1);
      this.setBlockAndMetadata(world, 0, 2, -4, doorBlock, 8);
      this.setBlockAndMetadata(world, 0, 3, -4, plankBlock, plankMeta);
      this.setBlockAndMetadata(world, 0, 3, -5, slabBlock, slabMeta | 8);

      for (int i12 = -3; i12 <= 3; i12++) {
         this.setBlockAndMetadata(world, i12, 4, -4, woodBlock, woodMeta | 4);
         this.setBlockAndMetadata(world, i12, 5, -5, stairBlock, 6);
      }

      this.setBlockAndMetadata(world, -2, 5, -4, vanilla("field_150465_bP"), 3);
      this.setBlockAndMetadata(world, 2, 5, -4, vanilla("field_150465_bP"), 3);

      for (int var55 = -2; var55 <= 2; var55++) {
         this.setBlockAndMetadata(world, var55, 6, -5, woodBlock, woodMeta | 4);
      }

      for (int var56 = -1; var56 <= 1; var56++) {
         this.setBlockAndMetadata(world, var56, 7, -5, woodBlock, woodMeta | 4);
      }

      for (int var41 = 4; var41 <= 9; var41++) {
         this.setBlockAndMetadata(world, 0, var41, -5, woodBlock, woodMeta);
      }

      this.setBlockAndMetadata(world, 0, 9, -4, stairBlock, 7);
      this.setBlockAndMetadata(world, 0, 6, -6, vanilla("field_150478_aa"), 4);
      this.setBlockAndMetadata(world, 0, 5, -4, vanilla("field_150478_aa"), 3);
      this.placeWallBanner(world, 0, 5, -5, "banner_WILDLING", 2);
      this.placeWallBanner(world, -2, 5, -5, "banner_WILDLING", 2);
      this.placeWallBanner(world, 2, 5, -5, "banner_WILDLING", 2);

      for (int var57 = -3; var57 <= 3; var57++) {
         this.setBlockAndMetadata(world, var57, 1, 5, woodBlock, woodMeta | 4);
         this.setBlockAndMetadata(world, var57, 2, 5, stairBlock, 3);
         this.setBlockAndMetadata(world, var57, 3, 5, stairBlock, 7);
         this.setBlockAndMetadata(world, var57, 4, 5, woodBlock, woodMeta | 4);
      }

      this.setBlockAndMetadata(world, -3, 5, 5, plankBlock, plankMeta);
      this.setBlockAndMetadata(world, -2, 5, 5, plankBlock, plankMeta);
      this.setBlockAndMetadata(world, -1, 5, 5, slabBlock, slabMeta | 8);
      this.setBlockAndMetadata(world, 0, 5, 5, plankBlock, plankMeta);
      this.setBlockAndMetadata(world, 1, 5, 5, slabBlock, slabMeta | 8);
      this.setBlockAndMetadata(world, 2, 5, 5, plankBlock, plankMeta);
      this.setBlockAndMetadata(world, 3, 5, 5, plankBlock, plankMeta);

      for (int var58 = -2; var58 <= 2; var58++) {
         for (int j12 = 6; j12 <= 7; j12++) {
            this.setBlockAndMetadata(world, var58, j12, 5, plankBlock, plankMeta);
         }
      }

      for (int i14 : new int[]{-2, 2}) {
         for (int j15 = 1; j15 <= 4; j15++) {
            this.setBlockAndMetadata(world, i14, j15, 4, plankBlock, plankMeta);
         }

         this.setBlockAndMetadata(world, i14, 5, 4, fenceBlock, fenceMeta);
      }

      for (int j13 = 4; j13 <= 5; j13++) {
         this.setBlockAndMetadata(world, -3, j13, 4, plankBlock, plankMeta);
         this.setBlockAndMetadata(world, 3, j13, 4, plankBlock, plankMeta);
      }

      for (int var43 = 7; var43 <= 9; var43++) {
         this.setBlockAndMetadata(world, 0, var43, 5, woodBlock, woodMeta);
      }

      this.setBlockAndMetadata(world, 0, 9, 4, stairBlock, 6);
      this.setBlockAndMetadata(world, 0, 5, 4, vanilla("field_150478_aa"), 4);
      this.placeWallBanner(world, 0, 4, 5, "banner_WILDLING", 2);
      this.setBlockAndMetadata(world, -1, 4, 4, vanilla("field_150465_bP"), 2);
      this.setBlockAndMetadata(world, 1, 4, 4, vanilla("field_150465_bP"), 2);
      this.setBlockAndMetadata(world, 0, 3, 5, plankBlock, plankMeta);
      this.setBlockAndMetadata(world, 0, 3, 6, stairBlock, 7);
      this.setBlockAndMetadata(world, 0, 4, 6, woodBlock, woodMeta);
      this.setBlockAndMetadata(world, 0, 5, 6, woodBlock, woodMeta);
      this.setBlockAndMetadata(world, 0, 6, 6, stairBlock, 3);
      this.setBlockAndMetadata(world, -2, 5, 0, vanilla("field_150478_aa"), 2);
      this.placeWallBanner(world, -3, 3, 0, "banner_WILDLING", 1);
      this.setBlockAndMetadata(world, 2, 5, 0, vanilla("field_150478_aa"), 1);
      this.placeWallBanner(world, 3, 3, 0, "banner_WILDLING", 3);

      for (int k12 = -3; k12 <= -1; k12++) {
         this.setBlockAndMetadata(world, -3, 4, k12, stairBlock, 0);
         this.setBlockAndMetadata(world, 3, 4, k12, stairBlock, 1);
      }

      for (int var38 = -4; var38 <= -1; var38++) {
         this.setBlockAndMetadata(world, -3, 5, var38, stairBlock, 4);
         this.setBlockAndMetadata(world, 3, 5, var38, stairBlock, 5);
      }

      for (int var39 = 1; var39 <= 3; var39++) {
         this.setBlockAndMetadata(world, -3, 4, var39, stairBlock, 0);
         this.setBlockAndMetadata(world, 3, 4, var39, stairBlock, 1);
         this.setBlockAndMetadata(world, -3, 5, var39, stairBlock, 4);
         this.setBlockAndMetadata(world, 3, 5, var39, stairBlock, 5);
      }

      for (int var40 = -6; var40 <= 6; var40++) {
         this.setBlockAndMetadata(world, -5, 4, var40, slabBlock, slabMeta | 8);
         this.setBlockAndMetadata(world, -4, 5, var40, stairBlock, 1);
         this.setBlockAndMetadata(world, -3, 6, var40, stairBlock, 1);
         this.setBlockAndMetadata(world, -2, 7, var40, plankBlock, plankMeta);
         this.setBlockAndMetadata(world, -2, 8, var40, stairBlock, 1);
         this.setBlockAndMetadata(world, -1, 9, var40, plankBlock, plankMeta);
         this.setBlockAndMetadata(world, -1, 10, var40, stairBlock, 1);
         this.setBlockAndMetadata(world, 0, 10, var40, woodBlock, woodMeta | 8);
         this.setBlockAndMetadata(world, 1, 10, var40, stairBlock, 0);
         this.setBlockAndMetadata(world, 1, 9, var40, plankBlock, plankMeta);
         this.setBlockAndMetadata(world, 2, 8, var40, stairBlock, 0);
         this.setBlockAndMetadata(world, 2, 7, var40, plankBlock, plankMeta);
         this.setBlockAndMetadata(world, 3, 6, var40, stairBlock, 0);
         this.setBlockAndMetadata(world, 4, 5, var40, stairBlock, 0);
         this.setBlockAndMetadata(world, 5, 4, var40, slabBlock, slabMeta | 8);
      }

      for (int k15 : new int[]{-6, 6}) {
         this.setBlockAndMetadata(world, -4, 4, k15, slabBlock, slabMeta | 8);
         this.setBlockAndMetadata(world, -3, 5, k15, stairBlock, 4);
         this.setBlockAndMetadata(world, -2, 6, k15, stairBlock, 4);
         this.setBlockAndMetadata(world, -1, 7, k15, stairBlock, 4);
         this.setBlockAndMetadata(world, -1, 8, k15, plankBlock, plankMeta);
         this.setBlockAndMetadata(world, 1, 8, k15, plankBlock, plankMeta);
         this.setBlockAndMetadata(world, 1, 7, k15, stairBlock, 5);
         this.setBlockAndMetadata(world, 2, 6, k15, stairBlock, 5);
         this.setBlockAndMetadata(world, 3, 5, k15, stairBlock, 5);
         this.setBlockAndMetadata(world, 4, 4, k15, slabBlock, slabMeta | 8);
      }

      this.setBlockAndMetadata(world, 0, 11, -6, stairBlock, 3);
      this.setBlockAndMetadata(world, 0, 11, -7, stairBlock, 6);
      this.setBlockAndMetadata(world, 0, 12, -7, stairBlock, 3);
      this.setBlockAndMetadata(world, 0, 11, 6, stairBlock, 2);
      this.setBlockAndMetadata(world, 0, 11, 7, stairBlock, 7);
      this.setBlockAndMetadata(world, 0, 12, 7, stairBlock, 2);

      for (int k1 = -1; k1 <= 1; k1++) {
         this.setBlockAndMetadata(world, -1, 10, k1, plankBlock, plankMeta);
         this.setBlockAndMetadata(world, 1, 10, k1, plankBlock, plankMeta);
         this.setBlockAndMetadata(world, -1, 11, k1, stairBlock, 1);
         this.setBlockAndMetadata(world, 1, 11, k1, stairBlock, 0);
      }

      this.setBlockAndMetadata(world, 0, 11, -1, stairBlock, 2);
      this.setBlockAndMetadata(world, 0, 11, 1, stairBlock, 3);
      this.setAir(world, 0, 10, 0);

      for (int l = 0; l <= 2; l++) {
         int j12 = 4 + l * 2;
         this.setBlockAndMetadata(world, -4 + l, j12, 0, woodBlock, woodMeta);
         this.setBlockAndMetadata(world, -4 + l, j12 + 1, 0, woodBlock, woodMeta);
         this.setBlockAndMetadata(world, -4 + l, j12 + 2, 0, stairBlock, 1);
         this.setBlockAndMetadata(world, 4 - l, j12, 0, woodBlock, woodMeta);
         this.setBlockAndMetadata(world, 4 - l, j12 + 1, 0, woodBlock, woodMeta);
         this.setBlockAndMetadata(world, 4 - l, j12 + 2, 0, stairBlock, 0);
      }

      for (int var36 = -4; var36 <= 4; var36++) {
         this.setBlockAndMetadata(world, -2, 6, var36, stairBlock, 4);
         this.setBlockAndMetadata(world, 2, 6, var36, stairBlock, 5);
      }

      for (int var37 = -3; var37 <= 3; var37++) {
         this.setBlockAndMetadata(world, -1, 8, var37, stairBlock, 4);
         this.setBlockAndMetadata(world, 1, 8, var37, stairBlock, 5);
      }

      for (int i14 : new int[]{-1, 1}) {
         this.setBlockAndMetadata(world, i14, 8, -5, plankBlock, plankMeta);
         this.setBlockAndMetadata(world, i14, 8, -4, plankBlock, plankMeta);
         this.setBlockAndMetadata(world, i14, 8, 4, plankBlock, plankMeta);
         this.setBlockAndMetadata(world, i14, 8, 5, plankBlock, plankMeta);
      }

      this.setBlockAndMetadata(world, -1, 7, -4, stairBlock, 4);
      this.setBlockAndMetadata(world, 1, 7, -4, stairBlock, 5);
      this.setBlockAndMetadata(world, -1, 7, 4, stairBlock, 4);
      this.setBlockAndMetadata(world, 1, 7, 4, stairBlock, 5);

      for (int j16 = 0; j16 >= -5; j16--) {
         for (int i16 = -1; i16 <= 1; i16++) {
            for (int k16 = -1; k16 <= 1; k16++) {
               if (i16 == 0 && k16 == 0) {
                  this.setAir(world, 0, j16, 0);
               } else {
                  this.setBlockAndMetadata(world, i16, j16, k16, vanilla("field_150347_e"), 0);
               }
            }
         }
      }

      this.setBlockAndMetadata(world, 0, -6, 0, got("hearth"), 0);
      this.setBlockAndMetadata(world, 0, -5, 0, vanilla("field_150480_ab"), 0);
      this.setBlockAndMetadata(world, 0, 0, 0, got("bronzeBars"), 0);
      this.setAir(world, 0, 1, 0);
      this.setBlockAndMetadata(world, 0, 1, 3, got("strawBed"), 0);
      this.setBlockAndMetadata(world, 0, 1, 4, got("strawBed"), 8);

      for (int var59 = 1; var59 <= 2; var59++) {
         this.setBlockAndMetadata(world, -1, var59, 4, fenceBlock, fenceMeta);
         this.setBlockAndMetadata(world, 1, var59, 4, fenceBlock, fenceMeta);
      }

      this.setBlockAndMetadata(world, -2, 1, 3, got("tableWildling"), 0);
      this.setBlockAndMetadata(world, -2, 1, 2, vanilla("field_150462_ai"), 0);
      this.setBlockAndMetadata(world, 2, 1, 3, vanilla("field_150460_al"), 5);
      this.placeChest(world, random, 2, 1, 2, 5, "chest_BEYOND_WALL");
      if (this.isHardhome) {
         this.spawnNPCAndSetHome(new LegacyEntity(world), world, 1, 1, 0, 8);
         this.spawnNPCAndSetHome(new LegacyEntity(world), world, -1, 1, 0, 8);
         this.spawnNPCAndSetHome(new LegacyEntity(world), world, -1, 1, -1, 8);
      } else {
         this.spawnNPCAndSetHome(new LegacyEntity(world), world, 0, 1, 0, 8);
      }

      return true;
   }

   public LegacyNorthernContext setIsHardhome() {
      this.isHardhome = true;
      return this;
   }

}
