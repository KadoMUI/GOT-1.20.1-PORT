package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureWesterosTower extends LegacyNorthernContext {

   protected GOTStructureWesterosTower(NorthStructureBuilder builder) {
      super(builder, Style.NORTH);
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureWesterosTower piece = new GOTStructureWesterosTower(builder);
      piece.marketVariant = Math.max(0, variant);
      piece.isAbandoned = variant == 1;
      piece.isTramp = variant == 1;
      piece.isHardhome = variant == 1;
      piece.isBlacksmith = variant == 1;
      piece.generate(piece, piece.legacyRandom(), 0, 0, 0, 0);
   }


   private boolean generateRoom = true;

   public boolean generate(LegacyNorthernContext world, Random random, int i, int j, int k, int rotation) {
      return this.generateAndHeight(world, random, i, j, k, rotation, random.nextInt(4));
   }

   public boolean generateAndHeight(LegacyNorthernContext world, Random random, int i, int j, int k, int rotation, int height) {
      int doorBase = j - 1;
      this.setOriginAndRotation(world, i, j + height, k, rotation, 3);
      doorBase -= this.getY(0);
      this.setupRandomBlocks(random);
      if (this.restrictions) {
         for (int i1 = -3; i1 <= 3; i1++) {
            for (int k1 = -3; k1 <= 3; k1++) {
               int j12 = this.getTopBlock(world, i1, k1) - 1;
               if (!this.isSurface(world, i1, j12, k1)) {
                  return false;
               }
            }
         }
      }

      for (int i1 = -2; i1 <= 2; i1++) {
         for (int k1 = -2; k1 <= 2; k1++) {
            for (int j12 = 9; j12 <= 13; j12++) {
               this.setAir(world, i1, j12, k1);
            }
         }
      }

      for (int var34 = -2; var34 <= 2; var34++) {
         for (int k1 = -2; k1 <= 2; k1++) {
            int i2 = Math.abs(var34);
            int k2 = Math.abs(k1);

            for (int j1 = 8; j1 >= doorBase || !this.isOpaque(world, var34, j1, k1) && this.getY(j1) >= 0; j1--) {
               if (i2 == 2 && k2 == 2) {
                  this.setBlockAndMetadata(world, var34, j1, k1, this.pillarBlock, this.pillarMeta);
               } else {
                  this.setBlockAndMetadata(world, var34, j1, k1, this.brickBlock, this.brickMeta);
               }

               this.setGrassToDirt(world, var34, j1 - 1, k1);
            }

            if (i2 == 2 && k2 == 2) {
               for (int var30 = 9; var30 <= 12; var30++) {
                  this.setBlockAndMetadata(world, var34, var30, k1, this.pillarBlock, this.pillarMeta);
               }
            } else if (i2 == 2 || k2 == 2) {
               this.setBlockAndMetadata(world, var34, 9, k1, this.fenceBlock, this.fenceMeta);
            }
         }
      }

      for (int var35 = -3; var35 <= 3; var35++) {
         for (int k1 = -3; k1 <= 3; k1++) {
            int i2 = Math.abs(var35);
            int k2 = Math.abs(k1);
            if (i2 == 3 && k2 == 1 || k2 == 3 && i2 == 1) {
               for (int j1 = 4; j1 >= 1 || !this.isOpaque(world, var35, j1, k1) && this.getY(j1) >= 0; j1--) {
                  this.setBlockAndMetadata(world, var35, j1, k1, this.brickBlock, this.brickMeta);
                  this.setGrassToDirt(world, var35, j1 - 1, k1);
               }
            }
         }
      }

      for (int i13 : new int[]{-1, 1}) {
         this.setBlockAndMetadata(world, i13, 5, -3, this.brickStairBlock, 2);
         this.setBlockAndMetadata(world, i13, 5, 3, this.brickStairBlock, 3);
      }

      for (int k12 : new int[]{-1, 1}) {
         this.setBlockAndMetadata(world, -3, 5, k12, this.brickStairBlock, 1);
         this.setBlockAndMetadata(world, 3, 5, k12, this.brickStairBlock, 0);
      }

      for (int i12 = -1; i12 <= 1; i12++) {
         for (int var43 = -1; var43 <= 1; var43++) {
            this.setBlockAndMetadata(world, i12, 8, var43, this.rockSlabDoubleBlock, this.rockSlabDoubleMeta);
         }
      }

      this.setBlockAndMetadata(world, 0, 9, 0, this.rockBlock, this.rockMeta);
      this.setBlockAndMetadata(world, 0, 10, 0, got("beacon"), 0);
      this.setBlockAndMetadata(world, -2, 9, 0, this.fenceGateBlock, 3);

      for (int j13 = 8; !this.isOpaque(world, -3, j13, 0) && this.getY(j13) >= 0; j13--) {
         this.setBlockAndMetadata(world, -3, j13, 0, vanilla("field_150468_ap"), 5);
      }

      this.setBlockAndMetadata(world, -2, 12, -1, vanilla("field_150478_aa"), 3);
      this.setBlockAndMetadata(world, 2, 12, -1, vanilla("field_150478_aa"), 3);
      this.setBlockAndMetadata(world, -2, 12, 1, vanilla("field_150478_aa"), 4);
      this.setBlockAndMetadata(world, 2, 12, 1, vanilla("field_150478_aa"), 4);

      for (int var36 = -3; var36 <= 3; var36++) {
         for (int var44 = -3; var44 <= 3; var44++) {
            int var26 = Math.abs(var36);
            int k2 = Math.abs(var44);
            if (var26 == 3 || k2 == 3) {
               this.setBlockAndMetadata(world, var36, 13, var44, this.brickSlabBlock, this.brickSlabMeta | 8);
            } else if (var26 != 2 && k2 != 2) {
               if (var26 == 1 || k2 == 1) {
                  this.setBlockAndMetadata(world, var36, 14, var44, this.brickBlock, this.brickMeta);
               }
            } else {
               if (var26 == 2 && k2 == 2) {
                  this.setBlockAndMetadata(world, var36, 13, var44, this.brickBlock, this.brickMeta);
               } else {
                  this.setBlockAndMetadata(world, var36, 13, var44, this.brickSlabBlock, this.brickSlabMeta | 8);
               }

               this.setBlockAndMetadata(world, var36, 14, var44, this.brickSlabBlock, this.brickSlabMeta);
            }
         }
      }

      for (int i13 : new int[]{-2, 2}) {
         for (int k13 : new int[]{-2, 2}) {
            this.setBlockAndMetadata(world, i13, 13, k13 - 1, this.brickStairBlock, 6);
            this.setBlockAndMetadata(world, i13, 13, k13 + 1, this.brickStairBlock, 7);
            this.setBlockAndMetadata(world, i13 - 1, 13, k13, this.brickStairBlock, 5);
            this.setBlockAndMetadata(world, i13 + 1, 13, k13, this.brickStairBlock, 4);
         }
      }

      if (this.generateRoom) {
         this.setBlockAndMetadata(world, 0, doorBase, -2, this.brickBlock, this.brickMeta);
         this.setBlockAndMetadata(world, 0, doorBase + 1, -2, this.doorBlock, 1);
         this.setBlockAndMetadata(world, 0, doorBase + 2, -2, this.doorBlock, 9);

         for (int i16 = -1; i16 <= 1; i16++) {
            for (int var46 = -1; var46 <= 1; var46++) {
               this.setBlockAndMetadata(world, i16, doorBase, var46, this.brickBlock, this.brickMeta);

               for (int j12 = doorBase + 1; j12 <= doorBase + 4; j12++) {
                  this.setAir(world, i16, j12, var46);
               }
            }
         }

         this.setBlockAndMetadata(world, 0, doorBase + 3, -1, vanilla("field_150478_aa"), 3);
         this.setBlockAndMetadata(world, 1, doorBase + 1, -1, this.tableBlock, 0);
         this.placeWallBanner(world, 2, doorBase + 4, -1, this.bannerType, 3);
         this.placeChest(world, random, -1, doorBase + 1, -1, this.getChest(), 3, this.getChestContents());

         for (int j1 = doorBase + 1; j1 <= doorBase + 4; j1++) {
            this.setBlockAndMetadata(world, 1, j1, 1, this.brickBlock, this.brickMeta);
            this.setBlockAndMetadata(world, 1, j1, 0, vanilla("field_150468_ap"), 2);
         }

         this.setBlockAndMetadata(world, -1, doorBase + 2, 1, this.brickSlabBlock, this.brickSlabMeta | 8);
         this.setBlockAndMetadata(world, 0, doorBase + 2, 1, this.brickSlabBlock, this.brickSlabMeta | 8);

         for (int j14 : new int[]{doorBase + 1, doorBase + 3}) {
            this.setBlockAndMetadata(world, -1, j14, 1, this.bedBlock, 1);
            this.setBlockAndMetadata(world, 0, j14, 1, this.bedBlock, 9);
         }
      }

      int soldiers = 1 + random.nextInt(2);

      for (int l = 0; l < soldiers; l++) {
         LegacyEntity soldier = this.getSoldierArcher(world);
         soldier.setSpawnRidingHorse(false);
         this.spawnNPCAndSetHome(soldier, world, -1, 9, 0, 16);
      }

      LegacyEntity respawner = new LegacyEntity(world);
      respawner.setSpawnClass1(this.getSoldier(world).getClass());
      respawner.setCheckRanges(16, -12, 12, 4);
      respawner.setSpawnRanges(2, -2, 2, 16);
      this.placeNPCRespawner(respawner, world, 0, 9, 0);
      return true;
   }

   public void setGenerateRoom(boolean generateRoom) {
      this.generateRoom = generateRoom;
   }

}
