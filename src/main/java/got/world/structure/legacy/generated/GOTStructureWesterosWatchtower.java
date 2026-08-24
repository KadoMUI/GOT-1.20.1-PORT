package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureWesterosWatchtower extends LegacyNorthernContext {

   protected GOTStructureWesterosWatchtower(NorthStructureBuilder builder) {
      super(builder, Style.NORTH);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureWesterosWatchtower piece = new GOTStructureWesterosWatchtower(builder);
      piece.marketVariant = Math.max(0, variant);
      piece.isAbandoned = variant == 1;
      piece.isTramp = variant == 1;
      piece.isHardhome = variant == 1;
      piece.isBlacksmith = variant == 1;
      piece.generate(piece, piece.legacyRandom(), 0, 0, 0, 0);
   }


   public boolean generate(LegacyNorthernContext world, Random random, int i, int j, int k, int rotation) {
      this.setOriginAndRotation(world, i, j, k, rotation, 4);
      this.setupRandomBlocks(random);
      if (this.restrictions) {
         int minHeight = 0;
         int maxHeight = 0;

         for (int i2 = -4; i2 <= 4; i2++) {
            for (int k2 = -4; k2 <= 4; k2++) {
               int j2 = this.getTopBlock(world, i2, k2) - 1;
               if (!this.isSurface(world, i2, j2, k2)) {
                  return false;
               }

               if (j2 < minHeight) {
                  minHeight = j2;
               }

               if (j2 > maxHeight) {
                  maxHeight = j2;
               }

               if (maxHeight - minHeight > 8) {
                  return false;
               }
            }
         }
      }

      for (int i3 = -3; i3 <= 3; i3++) {
         for (int k3 = -3; k3 <= 3; k3++) {
            int i4 = Math.abs(i3);
            int k4 = Math.abs(k3);
            if (i4 != 3 || k4 != 3) {
               for (int j2 = 0; !this.isOpaque(world, i3, j2, k3) && this.getY(j2) >= 0; j2--) {
                  this.setBlockAndMetadata(world, i3, j2, k3, this.brickBlock, this.brickMeta);
                  this.setGrassToDirt(world, i3, j2 - 1, k3);
               }

               if ((i4 != 3 || k4 != 2) && (k4 != 3 || i4 != 2)) {
                  if (i4 != 3 && k4 != 3) {
                     this.setBlockAndMetadata(world, i3, 0, k3, this.brickBlock, this.brickMeta);

                     for (int j2 = 1; j2 <= 5; j2++) {
                        this.setAir(world, i3, j2, k3);
                     }

                     this.setBlockAndMetadata(world, i3, 6, k3, this.plankBlock, this.plankMeta);

                     for (int j2 = 7; j2 <= 9; j2++) {
                        this.setAir(world, i3, j2, k3);
                     }
                  } else {
                     for (int j2 = 1; j2 <= 9; j2++) {
                        this.setBlockAndMetadata(world, i3, j2, k3, this.brickBlock, this.brickMeta);
                     }
                  }
               } else {
                  for (int j2 = 1; j2 <= 9; j2++) {
                     this.setBlockAndMetadata(world, i3, j2, k3, this.pillarBlock, this.pillarMeta);
                  }
               }
            }
         }
      }

      for (int i3 = -3; i3 <= 3; i3++) {
         for (int k3 = -3; k3 <= 3; k3++) {
            this.setBlockAndMetadata(world, i3, 10, k3, this.brickBlock, this.brickMeta);
         }
      }

      for (int i3 = -3; i3 <= 3; i3++) {
         this.setBlockAndMetadata(world, i3, 10, -4, this.brickStairBlock, 6);
         this.setBlockAndMetadata(world, i3, 10, 4, this.brickStairBlock, 7);
      }

      for (int k5 = -2; k5 <= 2; k5++) {
         this.setBlockAndMetadata(world, -4, 10, k5, this.brickStairBlock, 5);
         this.setBlockAndMetadata(world, 4, 10, k5, this.brickStairBlock, 4);
      }

      this.setBlockAndMetadata(world, -3, 10, -3, this.brickStairBlock, 5);
      this.setBlockAndMetadata(world, -4, 10, -3, this.brickStairBlock, 6);
      this.setBlockAndMetadata(world, 4, 10, -3, this.brickStairBlock, 6);
      this.setBlockAndMetadata(world, 3, 10, -3, this.brickStairBlock, 4);
      this.setBlockAndMetadata(world, -3, 10, 3, this.brickStairBlock, 5);
      this.setBlockAndMetadata(world, -4, 10, 3, this.brickStairBlock, 7);
      this.setBlockAndMetadata(world, 4, 10, 3, this.brickStairBlock, 7);
      this.setBlockAndMetadata(world, 3, 10, 3, this.brickStairBlock, 4);
      this.setBlockAndMetadata(world, 0, 0, -3, this.brickBlock, this.brickMeta);
      this.setBlockAndMetadata(world, 0, 1, -3, this.doorBlock, 1);
      this.setBlockAndMetadata(world, 0, 2, -3, this.doorBlock, 8);

      for (int j3 = 1; j3 <= 2; j3++) {
         this.setBlockAndMetadata(world, -1, j3, -3, this.brickCarved, this.brickCarvedMeta);
         this.setBlockAndMetadata(world, 1, j3, -3, this.brickCarved, this.brickCarvedMeta);
      }

      this.setBlockAndMetadata(world, -1, 3, -4, vanilla("field_150478_aa"), 4);
      this.setBlockAndMetadata(world, 1, 3, -4, vanilla("field_150478_aa"), 4);
      this.setBlockAndMetadata(world, 0, 6, -3, this.brickCarved, this.brickCarvedMeta);
      this.setBlockAndMetadata(world, 0, 6, 3, this.brickCarved, this.brickCarvedMeta);
      this.setBlockAndMetadata(world, -3, 6, 0, this.brickCarved, this.brickCarvedMeta);
      this.setBlockAndMetadata(world, 3, 6, 0, this.brickCarved, this.brickCarvedMeta);
      this.placeWallBanner(world, 0, 5, -3, this.bannerType, 2);

      for (int j3 = 1; j3 <= 9; j3++) {
         this.setBlockAndMetadata(world, 0, j3, 2, vanilla("field_150468_ap"), 2);
      }

      this.setBlockAndMetadata(world, 0, 10, 2, this.trapdoorBlock, 9);

      for (int k5 = -2; k5 <= 2; k5++) {
         if (Math.floorMod(k5, 2) == 0) {
            this.placeChest(world, random, -2, 1, k5, 4, this.getChestContents());
            this.placeChest(world, random, 2, 1, k5, 5, this.getChestContents());
         } else {
            this.setBlockAndMetadata(world, -1, 1, k5, this.bedBlock, 3);
            this.setBlockAndMetadata(world, -2, 1, k5, this.bedBlock, 11);
            this.setBlockAndMetadata(world, 1, 1, k5, this.bedBlock, 1);
            this.setBlockAndMetadata(world, 2, 1, k5, this.bedBlock, 9);
         }
      }

      this.setBlockAndMetadata(world, -2, 3, 0, vanilla("field_150478_aa"), 2);
      this.setBlockAndMetadata(world, 2, 3, 0, vanilla("field_150478_aa"), 1);
      this.setBlockAndMetadata(world, 0, 5, 0, got("chandelier"), 2);
      this.placeChest(world, random, -2, 7, -2, this.getChest(), 4, this.getChestContents());
      this.setBlockAndMetadata(world, -2, 7, 0, got("armorStand"), 3);
      this.setBlockAndMetadata(world, -2, 8, 0, got("armorStand"), 7);
      this.setBlockAndMetadata(world, -2, 7, 2, vanilla("field_150467_bQ"), 0);
      this.spawnItemFrame(world, -3, 8, -1, 1, getRandFrameItem(random));
      this.spawnItemFrame(world, -3, 8, 1, 1, getRandFrameItem(random));
      this.setBlockAndMetadata(world, 2, 7, -2, this.tableBlock, 0);
      this.setBlockAndMetadata(world, 2, 7, -1, vanilla("field_150462_ai"), 0);
      this.setBlockAndMetadata(world, 2, 7, 0, this.cobbleSlabBlock, this.cobbleSlabMeta | 8);
      this.setBlockAndMetadata(world, 2, 7, 1, this.cobbleSlabBlock, this.cobbleSlabMeta | 8);
      this.setBlockAndMetadata(world, 2, 7, 2, this.cobbleSlabBlock, this.cobbleSlabMeta | 8);
      this.placeMug(world, random, 2, 8, 0, 1, "food_DEFAULT_DRINK");
      this.placePlateWithCertainty(world, random, 2, 8, 1, this.plateBlock, "food_DEFAULT");
      this.placeBarrel(world, random, 2, 8, 2, 5, "food_DEFAULT_DRINK");
      this.setBlockAndMetadata(world, 0, 9, 0, got("chandelier"), 2);

      for (int i3 = -4; i3 <= 4; i3++) {
         for (int k3 = -4; k3 <= 4; k3++) {
            int i4 = Math.abs(i3);
            int k4 = Math.abs(k3);
            if (i4 == 4 && k4 == 4) {
               this.setBlockAndMetadata(world, i3, 11, k3, this.brickBlock, this.brickMeta);
               this.setBlockAndMetadata(world, i3, 12, k3, this.brickBlock, this.brickMeta);
            } else if (i4 == 4 || k4 == 4) {
               if (Math.floorMod(i3 + k3, 2) == 1) {
                  this.setBlockAndMetadata(world, i3, 11, k3, this.brickWallBlock, this.brickWallMeta);
               } else {
                  this.setBlockAndMetadata(world, i3, 11, k3, this.brickBlock, this.brickMeta);
                  this.setBlockAndMetadata(world, i3, 12, k3, this.brickSlabBlock, this.brickSlabMeta);
               }
            }
         }
      }

      this.setBlockAndMetadata(world, 0, 11, 0, this.pillarBlock, this.pillarMeta);
      this.setBlockAndMetadata(world, 0, 12, 0, this.pillarBlock, this.pillarMeta);
      this.setBlockAndMetadata(world, 0, 13, 0, this.brickCarved, this.brickCarvedMeta);
      this.placeBanner(world, 0, 14, 0, this.bannerType, 2);
      this.setBlockAndMetadata(world, 0, 11, -3, vanilla("field_150478_aa"), 3);
      this.setBlockAndMetadata(world, 0, 11, 3, vanilla("field_150478_aa"), 4);
      this.setBlockAndMetadata(world, -3, 11, 0, vanilla("field_150478_aa"), 2);
      this.setBlockAndMetadata(world, 3, 11, 0, vanilla("field_150478_aa"), 1);
      LegacyEntity soldier = this.getSoldier(world);
      soldier.setSpawnRidingHorse(false);
      this.spawnNPCAndSetHome(soldier, world, 0, 1, 0, 16);
      LegacyEntity levyman = this.getSoldier(world);
      this.spawnNPCAndSetHome(levyman, world, 0, 11, 1, 16);
      LegacyEntity respawner = new LegacyEntity(world);
      respawner.setSpawnClass1(this.getSoldier(world).getClass());
      respawner.setCheckRanges(16, -12, 8, 6);
      respawner.setSpawnRanges(3, -6, 6, 16);
      this.placeNPCRespawner(respawner, world, 0, 6, 0);
      return true;
   }

}
