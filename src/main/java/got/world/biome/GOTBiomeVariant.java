package got.world.biome;
public record GOTBiomeVariant(int id,String registryName,String displayName,Category category,float heightBoost,float treeMultiplier){
 public enum Category{STANDARD,HILLS,MOUNTAINS,FOREST,DENSE_FOREST,SPARSE_FOREST,ORCHARD,LAKE,RIVER,MARSH,ROCKY}
}
