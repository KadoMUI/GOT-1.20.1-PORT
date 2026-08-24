package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureWesterosStoneHouse extends LegacyNorthernContext {

   protected GOTStructureWesterosStoneHouse(NorthStructureBuilder builder) {
      super(builder, Style.NORTH);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureWesterosStoneHouse piece = new GOTStructureWesterosStoneHouse(builder);
      piece.marketVariant = Math.max(0, variant);
      piece.isAbandoned = variant == 1;
      piece.isTramp = variant == 1;
      piece.isHardhome = variant == 1;
      piece.isBlacksmith = variant == 1;
      piece.generate(piece, piece.legacyRandom(), 0, 0, 0, 0);
   }


   public boolean generate(LegacyNorthernContext world, Random random, int i, int j, int k, int rotation) {
      this.setOriginAndRotation(world, i, j, k, rotation, 8);
      this.setupRandomBlocks(random);
      if (this.restrictions) {
         int minHeight = 0;
         int maxHeight = 0;

         for (int i12 = -5; i12 <= 5; i12++) {
            for (int k132 = -7; k132 <= 6; k132++) {
               int j13 = this.getTopBlock(world, i12, k132) - 1;
               if (!this.isSurface(world, i12, j13, k132)) {
                  return false;
               }

               if (j13 < minHeight) {
                  minHeight = j13;
               }

               if (j13 > maxHeight) {
                  maxHeight = j13;
               }

               if (maxHeight - minHeight > 5) {
                  return false;
               }
            }
         }
      }

      for (int i1 = -5; i1 <= 4; i1++) {
         for (int k14 = -7; k14 <= 5; k14++) {
            int i2 = Math.abs(i1);
            if (i1 == -5) {
               for (int j132 = 1; j132 <= 8; j132++) {
                  this.setAir(world, -5, j132, k14);
               }

               this.setBlockAndMetadata(world, -5, 0, k14, vanilla("field_150349_c"), 0);

               for (int j13 = -1; !this.isOpaque(world, i1, j13, k14) && this.getY(j13) >= 0; j13--) {
                  this.setBlockAndMetadata(world, i1, j13, k14, vanilla("field_150346_d"), 0);
                  this.setGrassToDirt(world, i1, j13 - 1, k14);
               }
            } else {
               for (int j13 = 0; (j13 == 0 || !this.isOpaque(world, i1, j13, k14)) && this.getY(j13) >= 0; j13--) {
                  this.setBlockAndMetadata(world, i1, j13, k14, this.brickBlock, this.brickMeta);
                  this.setGrassToDirt(world, i1, j13 - 1, k14);
               }

               if (k14 >= -4) {
                  if ((k14 == -4 || k14 == 5) && i2 == 4) {
                     for (int var37 = 1; var37 <= 7; var37++) {
                        this.setBlockAndMetadata(world, i1, var37, k14, this.pillarBlock, this.pillarMeta);
                     }
                  } else if (k14 != -4 && k14 != 5 && i2 != 4) {
                     if (i2 <= 3) {
                        for (int var35 = 1; var35 <= 3; var35++) {
                           this.setAir(world, i1, var35, k14);
                        }

                        this.setBlockAndMetadata(world, i1, 4, k14, this.plankBlock, this.plankMeta);

                        for (int var36 = 5; var36 <= 9; var36++) {
                           this.setAir(world, i1, var36, k14);
                        }
                     }
                  } else {
                     for (int var34 = 1; var34 <= 7; var34++) {
                        this.setBlockAndMetadata(world, i1, var34, k14, this.brickBlock, this.brickMeta);
                     }
                  }
               }

               if (k14 <= -5) {
                  if (k14 == -7) {
                     if (i2 != 4 && i2 != 2) {
                        for (int var40 = 1; var40 <= 3; var40++) {
                           this.setAir(world, i1, var40, -7);
                        }
                     } else {
                        for (int var39 = 1; var39 <= 3; var39++) {
                           this.setBlockAndMetadata(world, i1, var39, -7, this.pillarBlock, this.pillarMeta);
                        }
                     }
                  } else if (i2 == 4) {
                     this.setBlockAndMetadata(world, i1, 1, k14, this.brickBlock, this.brickMeta);
                     this.placeFlowerPot(world, i1, 2, k14, this.getRandomFlower(world, random));
                     this.setBlockAndMetadata(world, i1, 3, k14, this.fenceBlock, this.fenceMeta);
                  } else {
                     this.setBlockAndMetadata(world, i1, 0, k14, this.plankBlock, this.plankMeta);

                     for (int var38 = 1; var38 <= 3; var38++) {
                        this.setAir(world, i1, var38, k14);
                     }
                  }
               }
            }
         }
      }

      for (int var28 = -5; var28 <= 5; var28++) {
         int i2 = Math.abs(var28);

         for (int step = 0; step <= 2; step++) {
            int j14 = 8 + step;
            this.setBlockAndMetadata(world, var28, j14, -5 + step, this.brick2StairBlock, 2);
            this.setBlockAndMetadata(world, var28, j14, -4 + step, this.brick2Block, this.brick2Meta);
            this.setBlockAndMetadata(world, var28, j14, -3 + step, this.brick2StairBlock, 7);
            this.setBlockAndMetadata(world, var28, j14, 6 - step, this.brick2StairBlock, 3);
            this.setBlockAndMetadata(world, var28, j14, 5 - step, this.brick2Block, this.brick2Meta);
            this.setBlockAndMetadata(world, var28, j14, 4 - step, this.brick2StairBlock, 6);
            if (i2 == 4) {
               for (int j2 = 8; j2 <= j14 - 1; j2++) {
                  this.setBlockAndMetadata(world, var28, j2, -5 + step, this.brickBlock, this.brickMeta);
                  this.setBlockAndMetadata(world, var28, j2, 6 - step, this.brickBlock, this.brickMeta);
               }
            }
         }

         for (int k15 = -2; k15 <= 3; k15++) {
            int j14 = 10;
            this.setBlockAndMetadata(world, var28, j14, k15, this.brick2Block, this.brick2Meta);
            if (i2 == 4) {
               for (int j2 = 8; j2 <= j14 - 1; j2++) {
                  this.setBlockAndMetadata(world, var28, j2, k15, this.brickBlock, this.brickMeta);
               }
            }
         }
      }

      for (int var29 = -5; var29 <= -3; var29++) {
         for (int k12 = -1; k12 <= 2; k12++) {
            for (int j12 = 1; j12 <= 11; j12++) {
               this.setBlockAndMetadata(world, var29, j12, k12, this.brickBlock, this.brickMeta);
            }

            this.setGrassToDirt(world, var29, 0, k12);
         }
      }

      for (int var30 = -4; var30 <= 4; var30++) {
         this.setBlockAndMetadata(world, var30, 4, -7, this.brick2SlabBlock, this.brick2SlabMeta);
         this.setBlockAndMetadata(world, var30, 4, -6, this.brick2Block, this.brick2Meta);
         this.setBlockAndMetadata(world, var30, 4, -5, this.brick2Block, this.brick2Meta);
         this.setBlockAndMetadata(world, var30, 5, -5, this.brick2SlabBlock, this.brick2SlabMeta);
      }

      this.setBlockAndMetadata(world, 0, 3, -6, got("chandelier"), 2);
      this.setBlockAndMetadata(world, 0, 1, -4, this.doorBlock, 1);
      this.setBlockAndMetadata(world, 0, 2, -4, this.doorBlock, 8);
      this.setBlockAndMetadata(world, -2, 2, -3, vanilla("field_150478_aa"), 3);
      this.setBlockAndMetadata(world, 2, 2, -3, vanilla("field_150478_aa"), 3);

      for (int var31 = -1; var31 <= 1; var31++) {
         for (int k12 = -1; k12 <= 2; k12++) {
            this.setBlockAndMetadata(world, var31, 1, k12, vanilla("field_150404_cg"), 15);
         }
      }

      if (random.nextInt(4) == 0) {
         this.placeChest(world, random, 0, 0, 1, this.getChest(), 2, this.getChestContents());
      }

      this.setBlockAndMetadata(world, 3, 2, 4, vanilla("field_150478_aa"), 1);
      this.setBlockAndMetadata(world, 0, 3, 1, got("chandelier"), 2);

      for (int k1 = 0; k1 <= 1; k1++) {
         this.setBlockAndMetadata(world, -3, 1, k1, vanilla("field_150411_aY"), 0);
         this.setBlockAndMetadata(world, -3, 2, k1, vanilla("field_150460_al"), 4);
         this.setBlockAndMetadata(world, -4, 0, k1, got("hearth"), 0);
         this.setBlockAndMetadata(world, -4, 1, k1, vanilla("field_150480_ab"), 0);

         for (int j1 = 2; j1 <= 10; j1++) {
            this.setAir(world, -4, j1, k1);
         }
      }

      for (int var21 = -3; var21 <= -2; var21++) {
         this.setBlockAndMetadata(world, -3, 1, var21, this.plankBlock, this.plankMeta);
         this.placeMug(world, random, -3, 2, var21, 3, "food_DEFAULT_DRINK");
         this.setBlockAndMetadata(world, -3, 3, var21, this.plankStairBlock, 4);
      }

      for (int var22 = 3; var22 <= 4; var22++) {
         this.setBlockAndMetadata(world, -3, 3, var22, this.plankStairBlock, 4);
      }

      this.setBlockAndMetadata(world, -3, 1, 3, vanilla("field_150383_bp"), 3);
      this.setBlockAndMetadata(world, -3, 1, 4, this.plankBlock, this.plankMeta);
      this.placePlateWithCertainty(world, random, -3, 2, 4, this.plateBlock, "food_DEFAULT");
      this.setBlockAndMetadata(world, -2, 1, 4, vanilla("field_150462_ai"), 0);

      for (int var23 = 0; var23 <= 3; var23++) {
         this.setAir(world, 3, 4, var23);
      }

      for (int step = 0; step <= 3; step++) {
         this.setBlockAndMetadata(world, 3, 1 + step, 2 - step, this.plankStairBlock, 3);
      }

      this.setBlockAndMetadata(world, 3, 1, 1, this.plankBlock, this.plankMeta);
      this.setBlockAndMetadata(world, 3, 1, 0, this.plankBlock, this.plankMeta);
      this.setBlockAndMetadata(world, 3, 2, 0, this.plankStairBlock, 6);
      this.placeChest(world, random, 3, 1, -1, 5, this.getChestContents());
      this.setBlockAndMetadata(world, 3, 1, -2, this.tableBlock, 0);
      this.setBlockAndMetadata(world, 3, 1, -3, this.plankBlock, this.plankMeta);
      this.setBlockAndMetadata(world, 3, 2, -3, this.fenceBlock, this.fenceMeta);

      for (int var24 = -3; var24 <= -1; var24++) {
         this.setBlockAndMetadata(world, 3, 3, var24, this.plankBlock, this.plankMeta);
      }

      this.spawnItemFrame(world, 3, 3, -1, 3, getRandFrameItem(random));

      for (int j15 = 1; j15 <= 3; j15++) {
         this.setBlockAndMetadata(world, 0, j15, 5, this.pillarBlock, this.pillarMeta);
      }

      this.placeWallBanner(world, 0, 3, 5, this.bannerType, 2);

      for (int i13 : new int[]{-3, 1}) {
         this.setBlockAndMetadata(world, i13, 2, 5, this.brickStairBlock, 0);
         this.setBlockAndMetadata(world, i13, 3, 5, this.brickStairBlock, 4);
         this.setBlockAndMetadata(world, i13 + 1, 2, 5, this.brickWallBlock, this.brickWallMeta);
         this.setBlockAndMetadata(world, i13 + 1, 3, 5, this.brickWallBlock, this.brickWallMeta);
         this.setBlockAndMetadata(world, i13 + 2, 2, 5, this.brickStairBlock, 1);
         this.setBlockAndMetadata(world, i13 + 2, 3, 5, this.brickStairBlock, 5);
      }

      for (int k13 : new int[]{-4, 5}) {
         for (int i14 : new int[]{-3, 1}) {
            this.setBlockAndMetadata(world, i14, 6, k13, this.brickStairBlock, 0);
            this.setBlockAndMetadata(world, i14, 7, k13, this.brickStairBlock, 4);
            this.setBlockAndMetadata(world, i14 + 1, 6, k13, this.brickWallBlock, this.brickWallMeta);
            this.setBlockAndMetadata(world, i14 + 1, 7, k13, this.brickWallBlock, this.brickWallMeta);
            this.setBlockAndMetadata(world, i14 + 1, 8, k13, this.brickBlock, this.brickMeta);
            this.setBlockAndMetadata(world, i14 + 2, 6, k13, this.brickStairBlock, 1);
            this.setBlockAndMetadata(world, i14 + 2, 7, k13, this.brickStairBlock, 5);
         }

         this.setBlockAndMetadata(world, 0, 6, k13, this.brickCarved, this.brickCarvedMeta);
      }

      this.setBlockAndMetadata(world, -2, 5, 0, this.plankBlock, this.plankMeta);
      this.setBlockAndMetadata(world, -2, 6, 0, got("plate"), 0);
      this.setBlockAndMetadata(world, -2, 5, 1, this.plankBlock, this.plankMeta);
      this.placeMug(world, random, -2, 6, 1, 3, "food_DEFAULT_DRINK");

      for (int k13 : new int[]{-1, 2}) {
         this.setBlockAndMetadata(world, -2, 5, k13, this.bedBlock, 11);
         this.setBlockAndMetadata(world, -1, 5, k13, this.bedBlock, 3);
         this.spawnItemFrame(world, -3, 7, k13, 1, getRandFrameItem(random));
      }

      for (int k122 = 0; k122 <= 1; k122++) {
         for (int var26 = 7; var26 <= 8; var26++) {
            this.setBlockAndMetadata(world, -3, var26, k122, this.pillarBlock, this.pillarMeta);
         }
      }

      this.placeChest(world, random, -3, 5, -3, 4, this.getChestContents());
      this.setBlockAndMetadata(world, -3, 5, -2, this.plankBlock, this.plankMeta);
      this.setBlockAndMetadata(world, -3, 5, 3, this.plankBlock, this.plankMeta);
      this.placeChest(world, random, -3, 5, 4, 4, this.getChestContents());
      this.setBlockAndMetadata(world, 0, 9, -2, this.brick2Block, this.brick2Meta);
      this.setBlockAndMetadata(world, 0, 8, -2, got("chandelier"), 2);
      this.setBlockAndMetadata(world, 0, 9, 3, this.brick2Block, this.brick2Meta);
      this.setBlockAndMetadata(world, 0, 8, 3, got("chandelier"), 2);
      this.setBlockAndMetadata(world, -3, 7, -2, vanilla("field_150478_aa"), 2);
      this.setBlockAndMetadata(world, -3, 7, 3, vanilla("field_150478_aa"), 2);
      this.setBlockAndMetadata(world, 3, 7, -2, vanilla("field_150478_aa"), 1);
      this.setBlockAndMetadata(world, 3, 7, 3, vanilla("field_150478_aa"), 1);

      for (int k14 = -1; k14 <= 2; k14++) {
         this.setBlockAndMetadata(world, -5, 12, k14, this.brickStairBlock, 1);
         this.setBlockAndMetadata(world, -3, 12, k14, this.brickStairBlock, 0);
      }

      this.setBlockAndMetadata(world, -4, 12, -1, this.brickStairBlock, 2);
      this.setBlockAndMetadata(world, -4, 12, 0, this.brickBlock, this.brickMeta);
      this.setBlockAndMetadata(world, -4, 12, 1, this.brickBlock, this.brickMeta);
      this.setBlockAndMetadata(world, -4, 12, 2, this.brickStairBlock, 3);
      this.setBlockAndMetadata(world, -4, 13, 0, this.brickWallBlock, this.brickWallMeta);
      this.setBlockAndMetadata(world, -4, 13, 1, this.brickWallBlock, this.brickWallMeta);
      LegacyEntity male = this.getMan(world);
      male.getFamilyInfo().setMale(true);
      male.func_70062_b(4, new LegacyItemStack("item_goldRing"));
      this.spawnNPCAndSetHome(male, world, 0, 1, 0, 16);
      LegacyEntity female = this.getMan(world);
      female.getFamilyInfo().setMale(false);
      female.func_70062_b(4, new LegacyItemStack("item_goldRing"));
      this.spawnNPCAndSetHome(female, world, 0, 1, 0, 16);
      LegacyEntity child = this.getMan(world);
      child.getFamilyInfo().setMale(random.nextBoolean());
      child.getFamilyInfo().setChild();
      this.spawnNPCAndSetHome(child, world, 0, 1, 0, 16);
      return true;
   }

   public void setupRandomBlocks(Random random) {
      super.setupRandomBlocks(random);
      if (random.nextBoolean()) {
         this.doorBlock = got("doorAramant");
      }

      this.bedBlock = vanilla("field_150324_C");
      this.plateBlock = random.nextBoolean() ? got("plate") : got("ceramicPlate");
   }

}
