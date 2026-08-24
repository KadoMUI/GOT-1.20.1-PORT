package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureGiftHouse extends LegacyNorthernContext {

   protected GOTStructureGiftHouse(NorthStructureBuilder builder) {
      super(builder, Style.GIFT);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureGiftHouse piece = new GOTStructureGiftHouse(builder);
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

         for (int i1 = -5; i1 <= 6; i1++) {
            for (int k13 = -4; k13 <= 4; k13++) {
               int j1 = this.getTopBlock(world, i1, k13) - 1;
               if (!this.isSurface(world, i1, j1, k13)) {
                  return false;
               }

               if (j1 < minHeight) {
                  minHeight = j1;
               }

               if (j1 > maxHeight) {
                  maxHeight = j1;
               }

               if (maxHeight - minHeight > 6) {
                  return false;
               }
            }
         }
      }

      for (int i1 = -5; i1 <= 5; i1++) {
         for (int k12 = -4; k12 <= 4; k12++) {
            int i2 = Math.abs(i1);
            int k2 = Math.abs(k12);
            if (i2 <= 4 || k2 <= 3) {
               for (int j1 = 0; (j1 >= -3 || !this.isOpaque(world, i1, j1, k12)) && this.getY(j1) >= 0; j1--) {
                  this.setBlockAndMetadata(world, i1, j1, k12, this.brickBlock, this.brickMeta);
                  this.setGrassToDirt(world, i1, j1 - 1, k12);
               }

               for (int var26 = 1; var26 <= 8; var26++) {
                  this.setAir(world, i1, var26, k12);
               }

               if (k2 == 4 || i2 == 5) {
                  boolean beam = k12 == -4 && (i2 == 1 || i2 == 4);
                  if (k12 == 4 && (i2 == 0 || i2 == 4)) {
                     beam = true;
                  }

                  if (i2 == 5 && (k2 == 0 || k2 == 3)) {
                     beam = true;
                  }

                  if (beam) {
                     for (int j14 = 1; j14 <= 3; j14++) {
                        this.setBlockAndMetadata(world, i1, j14, k12, this.woodBeamBlock, this.woodBeamMeta);
                     }
                  } else {
                     for (int j14 = 1; j14 <= 3; j14++) {
                        this.setBlockAndMetadata(world, i1, j14, k12, this.wallBlock, this.wallMeta);
                     }
                  }
               }

               if (k2 <= 3 && i2 <= 4) {
                  this.setBlockAndMetadata(world, i1, 0, k12, this.plankSlabBlock, this.plankSlabMeta | 8);
                  if (random.nextInt(3) == 0) {
                     this.setBlockAndMetadata(world, i1, 1, k12, got("thatchFloor"), 0);
                  }

                  for (int var27 = -2; var27 <= -1; var27++) {
                     this.setAir(world, i1, var27, k12);
                  }
               }
            }
         }
      }

      for (int k131 : new int[]{-4, 4}) {
         for (int i1 = -4; i1 <= 4; i1++) {
            this.setBlockAndMetadata(world, i1, 4, k131, this.woodBeamBlock, this.woodBeamMeta | 4);
         }
      }

      for (int i1 : new int[]{-5, 5}) {
         for (int k14 = -3; k14 <= 3; k14++) {
            int k2 = Math.abs(k14);
            if (k2 == 0) {
               for (int j15 = 4; j15 <= 6; j15++) {
                  this.setBlockAndMetadata(world, i1, j15, k14, this.woodBeamBlock, this.woodBeamMeta);
               }
            } else {
               this.setBlockAndMetadata(world, i1, 4, k14, this.woodBeamBlock, this.woodBeamMeta | 8);
               if (k2 <= 2) {
                  this.setBlockAndMetadata(world, i1, 5, k14, this.wallBlock, this.wallMeta);
               }
            }
         }
      }

      for (int i1 = -5; i1 <= 5; i1++) {
         this.setBlockAndMetadata(world, i1, 5, -3, this.woodBeamBlock, this.woodBeamMeta | 4);
         this.setBlockAndMetadata(world, i1, 6, -1, this.woodBeamBlock, this.woodBeamMeta | 4);
         this.setBlockAndMetadata(world, i1, 7, 0, this.woodBeamBlock, this.woodBeamMeta | 4);
         this.setBlockAndMetadata(world, i1, 6, 1, this.woodBeamBlock, this.woodBeamMeta | 4);
         this.setBlockAndMetadata(world, i1, 5, 3, this.woodBeamBlock, this.woodBeamMeta | 4);
         this.setBlockAndMetadata(world, i1, 5, -4, this.roofStairBlock, 2);
         this.setBlockAndMetadata(world, i1, 6, -3, this.roofSlabBlock, this.roofSlabMeta);
         this.setBlockAndMetadata(world, i1, 6, -2, this.roofBlock, this.roofMeta);
         this.setBlockAndMetadata(world, i1, 7, -1, this.roofSlabBlock, this.roofSlabMeta);
         this.setBlockAndMetadata(world, i1, 7, 1, this.roofSlabBlock, this.roofSlabMeta);
         this.setBlockAndMetadata(world, i1, 6, 2, this.roofBlock, this.roofMeta);
         this.setBlockAndMetadata(world, i1, 6, 3, this.roofSlabBlock, this.roofSlabMeta);
         this.setBlockAndMetadata(world, i1, 5, 4, this.roofStairBlock, 3);
      }

      for (int k15 = -4; k15 <= 4; k15++) {
         this.setBlockAndMetadata(world, 0, 4, k15, this.woodBeamBlock, this.woodBeamMeta | 8);
      }

      this.setBlockAndMetadata(world, 0, 1, -4, this.doorBlock, 1);
      this.setBlockAndMetadata(world, 0, 2, -4, this.doorBlock, 8);
      this.setBlockAndMetadata(world, 0, 4, -5, vanilla("field_150478_aa"), 4);
      this.setBlockAndMetadata(world, -3, 2, -4, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 3, 2, -4, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, -2, 2, 4, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 2, 2, 4, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, -5, 2, -1, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, -5, 2, 1, this.fenceBlock, this.fenceMeta);
      this.setBlockAndMetadata(world, 0, 3, 3, vanilla("field_150478_aa"), 4);
      this.setBlockAndMetadata(world, -4, 4, 0, vanilla("field_150478_aa"), 2);

      for (int i1 : new int[]{-4, 4}) {
         for (int k16 : new int[]{-3, 3}) {
            this.setBlockAndMetadata(world, i1, 1, k16, this.plankBlock, this.plankMeta);

            for (int j13 = 2; j13 <= 4; j13++) {
               this.setBlockAndMetadata(world, i1, j13, k16, this.fenceBlock, this.fenceMeta);
            }
         }
      }

      this.setBlockAndMetadata(world, -2, 1, -3, this.plankBlock, this.plankMeta);
      this.placePlate(world, random, -2, 2, -3, this.plateBlock, "food_DEFAULT");
      this.setBlockAndMetadata(world, -3, 1, -3, this.plankBlock, this.plankMeta);
      this.placePlate(world, random, -3, 2, -3, this.plateBlock, "food_DEFAULT");
      this.setBlockAndMetadata(world, -4, 1, -2, this.plankBlock, this.plankMeta);
      this.placeMug(world, random, -4, 2, -2, 3, "food_DEFAULT_DRINK");
      this.placeChest(world, random, -4, 1, -1, 4, "chest_GIFT");
      this.setBlockAndMetadata(world, -4, 1, 0, vanilla("field_150462_ai"), 0);
      this.placeChest(world, random, -4, 1, 1, 4, "chest_GIFT");
      this.setBlockAndMetadata(world, -4, 1, 2, this.plankBlock, this.plankMeta);
      this.placeBarrel(world, random, -4, 2, 2, 4, "food_DEFAULT_DRINK");
      this.setBlockAndMetadata(world, -3, 1, 3, this.plankBlock, this.plankMeta);
      this.placeBarrel(world, random, -3, 2, 3, 2, "food_DEFAULT_DRINK");
      this.setBlockAndMetadata(world, -2, 1, 3, this.plankBlock, this.plankMeta);
      this.placeMug(world, random, -2, 2, 3, 0, "food_DEFAULT_DRINK");

      for (int k13 : new int[]{-3, 3}) {
         this.setBlockAndMetadata(world, 2, 1, k13, this.bedBlock, 1);
         this.setBlockAndMetadata(world, 3, 1, k13, this.bedBlock, 9);
      }

      this.setBlockAndMetadata(world, 4, 1, -2, this.plankBlock, this.plankMeta);

      for (int i1 = 4; i1 <= 6; i1++) {
         for (int var24 = -1; var24 <= 1; var24++) {
            for (int j16 = 5; (j16 >= 0 || !this.isOpaque(world, i1, j16, var24)) && this.getY(j16) >= 0; j16--) {
               this.setBlockAndMetadata(world, i1, j16, var24, this.brickBlock, this.brickMeta);
               this.setGrassToDirt(world, i1, j16 - 1, var24);
            }
         }
      }

      this.setBlockAndMetadata(world, 4, 6, 0, this.brickBlock, this.brickMeta);
      this.setBlockAndMetadata(world, 6, 5, -1, this.brickStairBlock, 2);
      this.setBlockAndMetadata(world, 6, 5, 1, this.brickStairBlock, 3);
      this.setBlockAndMetadata(world, 6, 6, 0, this.brickStairBlock, 0);

      for (int j12 = 6; j12 <= 8; j12++) {
         this.setBlockAndMetadata(world, 5, j12, 0, this.brickBlock, this.brickMeta);
      }

      for (int var28 = 9; var28 <= 10; var28++) {
         this.setBlockAndMetadata(world, 5, var28, 0, this.brickWallBlock, this.brickWallMeta);
      }

      this.setBlockAndMetadata(world, 5, 0, 0, got("hearth"), 0);
      this.setBlockAndMetadata(world, 5, 1, 0, vanilla("field_150480_ab"), 0);

      for (int var29 = 2; var29 <= 3; var29++) {
         this.setAir(world, 5, var29, 0);
      }

      this.setBlockAndMetadata(world, 4, 1, 0, this.barsBlock, 0);
      this.setBlockAndMetadata(world, 4, 2, 0, vanilla("field_150460_al"), 5);
      this.spawnItemFrame(world, 4, 4, 0, 3, getRandFrameItem(random));
      this.setBlockAndMetadata(world, 4, 1, 2, vanilla("field_150415_aT"), 3);

      for (int var30 = -2; var30 <= 0; var30++) {
         this.setBlockAndMetadata(world, 4, var30, 2, vanilla("field_150468_ap"), 3);
      }

      for (int i1 : new int[]{-4, 4}) {
         for (int k16 : new int[]{-3, 3}) {
            this.setBlockAndMetadata(world, i1, 0, k16, this.plankBlock, this.plankMeta);

            for (int j13 = -2; j13 <= -1; j13++) {
               this.setBlockAndMetadata(world, i1, j13, k16, this.woodBeamBlock, this.woodBeamMeta);
            }
         }
      }

      this.setBlockAndMetadata(world, -3, -1, -3, vanilla("field_150478_aa"), 2);
      this.setBlockAndMetadata(world, 3, -1, -3, vanilla("field_150478_aa"), 1);
      this.setBlockAndMetadata(world, -3, -1, 3, vanilla("field_150478_aa"), 2);
      this.setBlockAndMetadata(world, 3, -1, 3, vanilla("field_150478_aa"), 1);

      for (int i1 : new int[]{-2, 0, 2}) {
         this.setBlockAndMetadata(world, i1, -2, -2, this.bedBlock, 2);
         this.setBlockAndMetadata(world, i1, -2, -3, this.bedBlock, 10);
      }

      for (int k131 : new int[]{-2, 2}) {
         this.placeArmorStand(world, -4, -2, k131, 3, getRandArmorItems(random));
      }

      for (int k131 : new int[]{-1, 1}) {
         this.spawnItemFrame(world, -5, -1, k131, 1, getRandFrameItem(random));
      }

      this.setBlockAndMetadata(world, 0, -2, 3, this.tableBlock, 0);

      for (int i1 : new int[]{-1, 1}) {
         int amount = 2 + random.nextInt(5);
         this.placeChest(world, random, i1, -2, 3, 2, "chest_GIFT", amount);
      }

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
      return true;
   }

}
