import fs from "node:fs";
import os from "node:os";
import path from "node:path";
import { spawnSync } from "node:child_process";

const project = path.resolve(import.meta.dirname, "..");
const temp = fs.mkdtempSync(path.join(os.tmpdir(), "legacy-northern-check-"));
const contextDir = path.join(temp, "got/world/structure/legacy");
const builderDir = path.join(temp, "got/world/structure/north");
fs.mkdirSync(contextDir, { recursive: true });
fs.mkdirSync(builderDir, { recursive: true });

fs.writeFileSync(path.join(builderDir, "NorthStructureBuilder.java"), `
package got.world.structure.north;
public class NorthStructureBuilder { public long seed(){ return 1L; } public Object palette(){ return null; } }
`);

fs.writeFileSync(path.join(contextDir, "LegacyNorthernContext.java"), `
package got.world.structure.legacy;
import got.world.structure.north.NorthStructureBuilder;
import java.util.Random;
public class LegacyNorthernContext {
 public enum Style { NORTH, GIFT, WILDLING }
 protected enum Kingdom { NORTH, DRAGONSTONE, IRONBORN, CROWNLANDS_RED }
 protected Kingdom kingdom=Kingdom.NORTH;
 protected NorthStructureBuilder builder;
 protected boolean restrictions,notifyChanges,isAbandoned,isTramp,isHardhome,isBlacksmith;
 protected int originX,originY,originZ,rockMeta,rockSlabMeta,rockSlabDoubleMeta,rockWallMeta,brickMeta,
  brickSlabMeta,brickWallMeta,brickMossyMeta,brickMossySlabMeta,brickMossyWallMeta,brickCrackedMeta,
  brickCrackedSlabMeta,brickCrackedWallMeta,pillarMeta,brick2Meta,brick2SlabMeta,brick2WallMeta,
  pillar2Meta,cobbleMeta,cobbleSlabMeta,plankMeta,plankSlabMeta,fenceMeta,woodBeamMeta,logMeta,
  wallMeta,roofMeta,roofSlabMeta,cropMeta,brickCarvedMeta,marketVariant;
 protected LegacyBlock rockBlock,rockSlabBlock,rockSlabDoubleBlock,rockStairBlock,rockWallBlock,
  brickBlock,brickSlabBlock,brickStairBlock,brickWallBlock,brickMossyBlock,brickMossySlabBlock,
  brickMossyStairBlock,brickMossyWallBlock,brickCrackedBlock,brickCrackedSlabBlock,
  brickCrackedStairBlock,brickCrackedWallBlock,pillarBlock,brick2Block,brick2SlabBlock,
  brick2StairBlock,brick2WallBlock,pillar2Block,cobbleBlock,cobbleSlabBlock,cobbleStairBlock,
  plankBlock,plankSlabBlock,plankStairBlock,fenceBlock,fenceGateBlock,woodBeamBlock,logBlock,
  doorBlock,wallBlock,roofBlock,roofSlabBlock,roofStairBlock,barsBlock,bedBlock,gateBlock,
  plateBlock,cropBlock,tableBlock,trapdoorBlock,brickCarved;
 protected Object seedItem,bannerType;
 protected String markerPrefix;
 protected LegacyNorthernContext(NorthStructureBuilder b,Style s){builder=b;}
 protected Random legacyRandom(){return new Random();}
 public LegacyBlock got(String s){return new LegacyBlock();} public LegacyBlock vanilla(String s){return new LegacyBlock();}
 public void setOriginAndRotation(Object... a){} public void setupRandomBlocks(Random r){}
 public int getX(int a,int b){return 0;} public int getY(int a){return 0;} public int getZ(int a,int b){return 0;}
 public int getTopBlock(Object... a){return 0;} public boolean isSurface(Object... a){return true;}
 public boolean isOpaque(Object... a){return true;} public boolean isAir(Object... a){return true;}
 public LegacyBlock getBlock(Object... a){return new LegacyBlock();} public LegacyBlock getChest(){return new LegacyBlock();}
 public Object getChestContents(){return null;} public void setBlockAndMetadata(Object... a){}
 public void setAir(Object... a){} public void setGrassToDirt(Object... a){} public void findSurface(Object... a){}
 public void placeChest(Object... a){} public void placeWallBanner(Object... a){} public void placeBanner(Object... a){}
 public void placeBarrel(Object... a){} public void placeMug(Object... a){} public void placePlate(Object... a){}
 public void placePlateWithCertainty(Object... a){} public void placeFlowerPot(Object... a){} public void placeSign(Object... a){}
 public void spawnItemFrame(Object... a){} public void placeArmorStand(Object... a){} public void plantFlower(Object... a){}
 public LegacyBlock getRandomFlower(Random r){return new LegacyBlock();} public void spawnNPCAndSetHome(Object... a){}
 public void placeNPCRespawner(Object... a){} public void spawnLegendaryNPC(Object... a){} protected void spawnLegendaryMobs(Object... a){}
 protected LegacyItemStack getRandFrameItem(Random r){return new LegacyItemStack();} protected LegacyItemStack[] getRandArmorItems(Random r){return new LegacyItemStack[0];}
 public LegacyBlock getRandomFlower(LegacyNorthernContext w,Random r){return new LegacyBlock();} public void placeFlowerPot(LegacyNorthernContext w,int x,int y,int z,LegacyBlock b){}
 protected LegacyEntity getBartender(Object... a){return new LegacyEntity();} protected LegacyEntity getBlacksmith(Object... a){return new LegacyEntity();}
 protected LegacyEntity getCaptain(Object... a){return new LegacyEntity();} protected LegacyEntity getFarmer(Object... a){return new LegacyEntity();}
 protected LegacyEntity getFarmhand(Object... a){return new LegacyEntity();} protected LegacyEntity getMan(Object... a){return new LegacyEntity();}
 protected LegacyEntity getSoldier(Object... a){return new LegacyEntity();} protected LegacyEntity getSoldierArcher(Object... a){return new LegacyEntity();}
 protected LegacyEntity createTrader(Object... a){return new LegacyEntity();} protected void generateRoof(Object... a){}
 protected boolean hasDarkSkinPeople(){return false;} protected boolean hasMaester(){return true;} protected boolean hasSepton(){return false;}
 protected boolean hasNorthernWood(){return true;} protected boolean hasSouthernWood(){return false;}
 protected void leashEntityTo(Object... a){} protected void placeBeaconTower(Object... a){} protected void placeFarmTree(Object... a){}
 protected String[] getTavernName(Random r){return new String[]{"a","b"};}
 public void func_72921_c(Object... a){} public LegacyBlock func_147439_a(Object... a){return new LegacyBlock();}
 public static class LegacyBlock { public boolean func_149662_c(){return true;} }
 public static class LegacyItemStack { public LegacyItemStack(Object... a){} public LegacyItemStack copy(){return this;} public void setStackDisplayName(String s){} }
 public static class LegacyEntity {
  public LegacyEntity(Object... a){} public LegacyEntity getFamilyInfo(){return this;} public LegacyEntity getNPCItemsInv(){return this;}
  public LegacyEntity getHiredNPCInfo(){return this;} public LegacyEntity getRNG(){return this;} public void setMale(boolean b){}
  public void setChild(){} public void setMeleeWeapon(Object o){} public void setRangedWeapon(Object o){}
  public void setSpawnRidingHorse(boolean b){} public void setHomeArea(int... a){} public void setCurrentItemOrArmor(int i,Object o){}
  public void func_70062_b(int i,Object o){} public void func_110177_bN(){} public void func_110214_p(int i){}
  public void saddleMountForWorldGen(){} public void setSpawnClass1(Class<?> c){} public void setSpawnClass2(Class<?> c){}
  public void setCheckRanges(int... a){} public void setSpawnRanges(int... a){} public void setSpawnAmounts(int... a){}
  public void setUnitTrade(boolean b){} public void setSeedsItem(Object o){} public boolean nextBoolean(){return false;}
  public int nextInt(int i){return 0;}
 }
}
`);

const generated = path.join(project, "src/main/java/got/world/structure/legacy/generated");
const sources = [
  path.join(builderDir, "NorthStructureBuilder.java"),
  path.join(contextDir, "LegacyNorthernContext.java"),
  ...fs.readdirSync(generated).filter(f => f.endsWith(".java")).map(f => path.join(generated, f))
];
const result = spawnSync("java", ["-m", "jdk.compiler/com.sun.tools.javac.Main", "-proc:none", "-d", path.join(temp,"classes"), ...sources], { encoding:"utf8" });
if (result.stdout) process.stdout.write(result.stdout);
if (result.stderr) process.stderr.write(result.stderr);
if (result.status !== 0) process.exit(result.status ?? 1);
console.log(`Semantic compile passed for ${sources.length - 2} generated pieces.`);
