package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureThennHouse extends LegacyNorthernContext {

   protected GOTStructureThennHouse(NorthStructureBuilder builder) {
      super(builder, Style.WILDLING);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureThennHouse piece = new GOTStructureThennHouse(builder);
      piece.marketVariant = Math.max(0, variant);
      piece.isAbandoned = variant == 1;
      piece.isTramp = variant == 1;
      piece.isHardhome = variant == 1;
      piece.isBlacksmith = variant == 1;
      piece.generate(piece, piece.legacyRandom(), 0, 0, 0, 0);
   }


   private boolean isBlacksmith;

   public boolean generate(LegacyNorthernContext world, Random random, int i, int j, int k, int rotation) {
      this.setOriginAndRotation(world, i, j, k, rotation, 5);
      if (this.restrictions) {
         int minHeight = 0;
         int maxHeight = 0;

         for (int i13 = -4; i13 <= 4; i13++) {
            for (int k12 = -6; k12 <= 6; k12++) {
               int j13 = this.getTopBlock(world, i13, k12);
               this.getBlock(world, i13, j13 - 1, k12);
               if (j13 < minHeight) {
                  minHeight = j13;
               }

               if (j13 > maxHeight) {
                  maxHeight = j13;
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
      LegacyBlock doorBlock = got("doorSpruce");
      LegacyBlock floorBlock = vanilla("field_150406_ce");
      int floorMeta = 15;

      for (int i1 = -4; i1 <= 4; i1++) {
         for (int k1 = -6; k1 <= 6; k1++) {
            for (int j1 = 1; j1 <= 7; j1++) {
               this.setAir(world, i1, j1, k1);
            }

            for (int var27 = 0; (var27 == 0 || !this.isOpaque(world, i1, var27, k1)) && this.getY(var27) >= 0; var27--) {
               if (this.getBlock(world, i1, var27 + 1, k1).func_149662_c()) {
                  this.setBlockAndMetadata(world, i1, var27, k1, vanilla("field_150346_d"), 0);
               } else {
                  this.setBlockAndMetadata(world, i1, var27, k1, vanilla("field_150349_c"), 0);
               }

               this.setGrassToDirt(world, i1, var27 - 1, k1);
            }
         }
      }

      for (int var29 = -3; var29 <= 3; var29++) {
         for (int k1 = -5; k1 <= 5; k1++) {
            this.setBlockAndMetadata(world, var29, 0, k1, floorBlock, floorMeta);
            if (random.nextInt(2) == 0) {
               this.setBlockAndMetadata(world, var29, 1, k1, got("thatchFloor"), 0);
            }
         }
      }

      for (int j14 = 1; j14 <= 4; j14++) {
         this.setBlockAndMetadata(world, -3, j14, -5, woodBlock, woodMeta);
         this.setBlockAndMetadata(world, 3, j14, -5, woodBlock, woodMeta);
         this.setBlockAndMetadata(world, -3, j14, 5, woodBlock, woodMeta);
         this.setBlockAndMetadata(world, 3, j14, 5, woodBlock, woodMeta);
      }

      for (int j15 : new int[]{1, 4}) {
         for (int i12 = -2; i12 <= 2; i12++) {
            this.setBlockAndMetadata(world, i12, j15, -5, woodBlock, woodMeta | 4);
            this.setBlockAndMetadata(world, i12, j15, 5, woodBlock, woodMeta | 4);
         }

         for (int k13 = -4; k13 <= 4; k13++) {
            this.setBlockAndMetadata(world, -3, j15, k13, woodBlock, woodMeta | 8);
            this.setBlockAndMetadata(world, 3, j15, k13, woodBlock, woodMeta | 8);
         }
      }

      for (int k14 = -4; k14 <= 4; k14++) {
         this.setBlockAndMetadata(world, -3, 2, k14, stairBlock, 1);
         this.setBlockAndMetadata(world, 3, 2, k14, stairBlock, 0);
         this.setBlockAndMetadata(world, -3, 3, k14, stairBlock, 5);
         this.setBlockAndMetadata(world, 3, 3, k14, stairBlock, 4);
      }

      for (int i14 : new int[]{-3, 3}) {
         this.setBlockAndMetadata(world, i14, 2, 0, slabBlock, slabMeta);
         this.setBlockAndMetadata(world, i14, 3, 0, slabBlock, slabMeta | 8);
      }

      for (int k15 = -5; k15 <= 5; k15++) {
         for (int l = 0; l <= 3; l++) {
            this.setBlockAndMetadata(world, -4 + l, 4 + l, k15, stairBlock, 1);
            this.setBlockAndMetadata(world, 4 - l, 4 + l, k15, stairBlock, 0);
         }

         this.setBlockAndMetadata(world, 0, 7, k15, plankBlock, plankMeta);
      }

      for (int k12 : new int[]{-5, 5}) {
         for (int i12 = -2; i12 <= 2; i12++) {
            this.setBlockAndMetadata(world, i12, 4, k12, woodBlock, woodMeta | 4);
            this.setBlockAndMetadata(world, i12, 5, k12, woodBlock, woodMeta | 4);
         }

         for (int var31 = -1; var31 <= 1; var31++) {
            this.setBlockAndMetadata(world, var31, 6, k12, woodBlock, woodMeta | 4);
         }
      }

      for (int i15 = -2; i15 <= 2; i15++) {
         this.setBlockAndMetadata(world, i15, 2, 5, stairBlock, 3);
         this.setBlockAndMetadata(world, i15, 3, 5, stairBlock, 7);
      }

      this.setBlockAndMetadata(world, 0, 3, 5, plankBlock, plankMeta);
      this.setBlockAndMetadata(world, 0, 3, 6, stairBlock, 7);

      for (int j12 = 4; j12 <= 8; j12++) {
         this.setBlockAndMetadata(world, 0, j12, 6, woodBlock, woodMeta);
      }

      this.setBlockAndMetadata(world, 0, 8, 5, stairBlock, 2);
      this.setBlockAndMetadata(world, 0, 9, 6, stairBlock, 2);
      this.setBlockAndMetadata(world, 0, 1, -5, doorBlock, 1);
      this.setBlockAndMetadata(world, 0, 2, -5, doorBlock, 8);
      this.setBlockAndMetadata(world, 0, 3, -5, plankBlock, plankMeta);
      this.setBlockAndMetadata(world, -2, 2, -5, stairBlock, 2);
      this.setBlockAndMetadata(world, -2, 3, -5, stairBlock, 6);
      this.setBlockAndMetadata(world, -1, 2, -5, stairBlock, 1);
      this.setBlockAndMetadata(world, -1, 3, -5, stairBlock, 5);
      this.setBlockAndMetadata(world, 1, 2, -5, stairBlock, 0);
      this.setBlockAndMetadata(world, 1, 3, -5, stairBlock, 4);
      this.setBlockAndMetadata(world, 2, 2, -5, stairBlock, 2);
      this.setBlockAndMetadata(world, 2, 3, -5, stairBlock, 6);
      this.setBlockAndMetadata(world, 0, 3, -6, stairBlock, 6);

      for (int var34 = 4; var34 <= 8; var34++) {
         this.setBlockAndMetadata(world, 0, var34, -6, woodBlock, woodMeta);
      }

      this.setBlockAndMetadata(world, 0, 8, -5, stairBlock, 3);
      this.setBlockAndMetadata(world, 0, 9, -6, stairBlock, 3);
      this.setBlockAndMetadata(world, -1, 5, -6, vanilla("field_150478_aa"), 1);
      this.setBlockAndMetadata(world, 1, 5, -6, vanilla("field_150478_aa"), 2);

      for (int k16 = -4; k16 <= 4; k16++) {
         this.setBlockAndMetadata(world, -2, 1, k16, slabBlock, slabMeta | 8);
         this.setBlockAndMetadata(world, 2, 1, k16, slabBlock, slabMeta | 8);
      }

      this.setBlockAndMetadata(world, -2, 3, -4, vanilla("field_150478_aa"), 3);
      this.setBlockAndMetadata(world, 2, 3, -4, vanilla("field_150478_aa"), 3);
      this.setBlockAndMetadata(world, -2, 3, 4, vanilla("field_150478_aa"), 4);
      this.setBlockAndMetadata(world, 2, 3, 4, vanilla("field_150478_aa"), 4);
      this.setBlockAndMetadata(world, 0, 1, 3, got("strawBed"), 0);
      this.setBlockAndMetadata(world, 0, 1, 4, got("strawBed"), 8);
      this.setBlockAndMetadata(world, -1, 1, 4, vanilla("field_150462_ai"), 0);
      this.placeChest(world, random, 1, 1, 4, 2, "chest_BEYOND_WALL");
      this.placeWallBanner(world, 0, 4, 5, "banner_THENN", 2);
      this.setBlockAndMetadata(world, -1, 3, 4, vanilla("field_150465_bP"), 2);
      this.setBlockAndMetadata(world, 1, 3, 4, vanilla("field_150465_bP"), 2);
      if (this.isBlacksmith) {
         this.spawnNPCAndSetHome(new LegacyEntity(world), world, 0, 1, 0, 8);
      } else {
         LegacyEntity male = new LegacyEntity(world);
         male.getFamilyInfo().setMale(true);
         male.func_70062_b(4, new LegacyItemStack("item_goldRing"));
         this.spawnNPCAndSetHome(male, world, 0, 1, 0, 16);
         LegacyEntity female = new LegacyEntity(world);
         female.getFamilyInfo().setMale(false);
         female.func_70062_b(4, new LegacyItemStack("item_goldRing"));
         this.spawnNPCAndSetHome(female, world, 0, 1, 0, 16);
         LegacyEntity child = new LegacyEntity(world);
         child.getFamilyInfo().setMale(random.nextBoolean());
         child.getFamilyInfo().setChild();
         this.spawnNPCAndSetHome(child, world, 0, 1, 0, 16);
      }

      return true;
   }

   public LegacyNorthernContext setIsBlacksmith() {
      this.isBlacksmith = true;
      return this;
   }

}
