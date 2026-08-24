package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class GOTStructureWesterosTownWall extends LegacyNorthernContext {

   protected GOTStructureWesterosTownWall(NorthStructureBuilder builder, int variant) {
      super(builder, Style.NORTH);
      int x0;
      int x1;
      int xi0;
      int xi1;
      switch (variant) {
         case 1 -> { x0 = -9; x1 = 6; xi0 = x0; xi1 = x1; }
         case 2 -> { x0 = -6; x1 = 6; xi0 = -5; xi1 = 6; }
         case 3 -> { x0 = -5; x1 = 6; xi0 = x0; xi1 = x1; }
         case 4 -> { x0 = -6; x1 = 9; xi0 = x0; xi1 = x1; }
         case 5 -> { x0 = -6; x1 = 6; xi0 = -6; xi1 = 5; }
         case 6 -> { x0 = -6; x1 = 5; xi0 = x0; xi1 = x1; }
         default -> { x0 = -5; x1 = 5; xi0 = x0; xi1 = x1; }
      }
      this.xMin = x0;
      this.xMax = x1;
      this.xMinInner = xi0;
      this.xMaxInner = xi1;
   }


   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureWesterosTownWall piece = new GOTStructureWesterosTownWall(builder, variant);
      piece.generate(piece, piece.legacyRandom(), 0, 0, 0, 0);
   }


   private final int xMin;
   private final int xMax;
   private final int xMinInner;
   private final int xMaxInner;

   public boolean generate(LegacyNorthernContext world, Random random, int i, int j, int k, int rotation) {
      this.setOriginAndRotation(world, i, j, k, rotation, 0);
      this.setupRandomBlocks(random);

      for (int i1 = this.xMin; i1 <= this.xMax; i1++) {
         int k1 = 0;
         this.findSurface(world, i1, k1);

         for (int j1 = 1; (j1 >= 0 || !this.isOpaque(world, i1, j1, k1)) && this.getY(j1) >= 0; j1--) {
            this.setBlockAndMetadata(world, i1, j1, k1, this.rockSlabDoubleBlock, this.rockSlabDoubleMeta);
            this.setGrassToDirt(world, i1, j1 - 1, k1);
         }

         for (int var12 = 2; var12 <= 3; var12++) {
            this.setBlockAndMetadata(world, i1, var12, k1, this.brickBlock, this.brickMeta);
         }

         this.setBlockAndMetadata(world, i1, 4, k1, this.brick2Block, this.brick2Meta);
         int i3 = Math.floorMod(i1, 4);
         if (i3 == 2) {
            this.setBlockAndMetadata(world, i1, 5, k1, this.rockWallBlock, this.rockWallMeta);
         } else {
            this.setBlockAndMetadata(world, i1, 5, k1, this.brickBlock, this.brickMeta);
            switch (i3) {
               case 0:
                  this.setBlockAndMetadata(world, i1, 6, k1, this.brickBlock, this.brickMeta);
                  break;
               case 1:
                  this.setBlockAndMetadata(world, i1, 6, k1, this.brickStairBlock, 0);
               case 2:
               default:
                  break;
               case 3:
                  this.setBlockAndMetadata(world, i1, 6, k1, this.brickStairBlock, 1);
            }
         }

         if (i1 >= this.xMinInner && i1 <= this.xMaxInner) {
            for (int var13 = 1; var13 <= 1; var13++) {
               for (int j12 = 4; (j12 >= 0 || !this.isOpaque(world, i1, j12, var13)) && this.getY(j12) >= 0; j12--) {
                  this.setBlockAndMetadata(world, i1, j12, var13, this.brickBlock, this.brickMeta);
                  this.setGrassToDirt(world, i1, j12 - 1, var13);
               }
            }
         }
      }

      return true;
   }

}
