package got.world.structure.legacy;

import got.GOTMod;
import got.world.structure.north.NorthStructureBuilder;
import got.world.structure.north.NorthStructurePalette;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;
import java.util.Random;

/**
 * Compatibility writer for procedural structures recovered from the GPL
 * 1.7.10 code.  The recovered methods keep their original coordinates and
 * metadata; this class translates those old placement operations to 1.20.1
 * block states and records population-only operations as future markers.
 */
public class LegacyNorthernContext {
    public enum Style { NORTH, GIFT, WILDLING }
    protected enum Kingdom { NORTH, DRAGONSTONE, IRONBORN, CROWNLANDS_RED }

    protected final NorthStructureBuilder builder;
    protected final Style style;
    protected final Random structureRandom;
    protected boolean restrictions;
    protected boolean notifyChanges;
    protected int originX;
    protected int originY;
    protected int originZ;

    protected LegacyBlock rockBlock;
    protected int rockMeta;
    protected LegacyBlock rockSlabBlock;
    protected int rockSlabMeta;
    protected LegacyBlock rockSlabDoubleBlock;
    protected int rockSlabDoubleMeta;
    protected LegacyBlock rockStairBlock;
    protected LegacyBlock rockWallBlock;
    protected int rockWallMeta;
    protected LegacyBlock brickBlock;
    protected int brickMeta;
    protected LegacyBlock brickSlabBlock;
    protected int brickSlabMeta;
    protected LegacyBlock brickStairBlock;
    protected LegacyBlock brickWallBlock;
    protected int brickWallMeta;
    protected LegacyBlock brickMossyBlock;
    protected int brickMossyMeta;
    protected LegacyBlock brickMossySlabBlock;
    protected int brickMossySlabMeta;
    protected LegacyBlock brickMossyStairBlock;
    protected LegacyBlock brickMossyWallBlock;
    protected int brickMossyWallMeta;
    protected LegacyBlock brickCrackedBlock;
    protected int brickCrackedMeta;
    protected LegacyBlock brickCrackedSlabBlock;
    protected int brickCrackedSlabMeta;
    protected LegacyBlock brickCrackedStairBlock;
    protected LegacyBlock brickCrackedWallBlock;
    protected int brickCrackedWallMeta;
    protected LegacyBlock pillarBlock;
    protected int pillarMeta;
    protected LegacyBlock brick2Block;
    protected int brick2Meta;
    protected LegacyBlock brick2SlabBlock;
    protected int brick2SlabMeta;
    protected LegacyBlock brick2StairBlock;
    protected LegacyBlock brick2WallBlock;
    protected int brick2WallMeta;
    protected LegacyBlock pillar2Block;
    protected int pillar2Meta;
    protected LegacyBlock cobbleBlock;
    protected int cobbleMeta;
    protected LegacyBlock cobbleSlabBlock;
    protected int cobbleSlabMeta;
    protected LegacyBlock cobbleStairBlock;
    protected LegacyBlock plankBlock;
    protected int plankMeta;
    protected LegacyBlock plankSlabBlock;
    protected int plankSlabMeta;
    protected LegacyBlock plankStairBlock;
    protected LegacyBlock fenceBlock;
    protected int fenceMeta;
    protected LegacyBlock fenceGateBlock;
    protected LegacyBlock woodBeamBlock;
    protected int woodBeamMeta;
    protected LegacyBlock logBlock;
    protected int logMeta;
    protected LegacyBlock doorBlock;
    protected LegacyBlock wallBlock;
    protected int wallMeta;
    protected LegacyBlock roofBlock;
    protected int roofMeta;
    protected LegacyBlock roofSlabBlock;
    protected int roofSlabMeta;
    protected LegacyBlock roofStairBlock;
    protected LegacyBlock barsBlock;
    protected LegacyBlock bedBlock;
    protected LegacyBlock gateBlock;
    protected LegacyBlock plateBlock;
    protected LegacyBlock cropBlock;
    protected int cropMeta;
    protected Object seedItem;
    protected LegacyBlock tableBlock;
    protected LegacyBlock trapdoorBlock;
    protected LegacyBlock brickCarved;
    protected int brickCarvedMeta;
    protected Object bannerType;
    protected int marketVariant;
    protected String markerPrefix;
    protected boolean isAbandoned;
    protected boolean isTramp;
    protected boolean isHardhome;
    protected boolean isBlacksmith;
    protected Kingdom kingdom = Kingdom.NORTH;

    protected LegacyNorthernContext(NorthStructureBuilder builder, Style style) {
        this.builder = builder;
        this.style = style;
        this.structureRandom = new Random(builder.seed());
        this.originY = builder.originY();
        this.markerPrefix = switch (style) {
            case GIFT -> "gift";
            case WILDLING -> "wildling";
            default -> "north";
        };
    }

    protected Random legacyRandom() {
        return structureRandom;
    }

    public void setRestrictions(boolean value) {
        restrictions = value;
    }

    public void setOriginAndRotation(LegacyNorthernContext world, int i, int j, int k,
                                     int rotation, int offset) {
        originX = 0;
        originY = builder.originY();
        originZ = 0;
    }

    public void setOriginAndRotation(LegacyNorthernContext world, int i, int j, int k,
                                     int rotation, int offsetX, int offsetZ) {
        setOriginAndRotation(world, i, j, k, rotation, 0);
        originX += offsetX;
        originZ += offsetZ;
    }

    public int getRotationMode() {
        return 0;
    }

    public int getX(int x, int z) {
        return builder.worldPos(originX + x, 0, originZ + z).getX();
    }

    public int getY(int y) {
        return originY + y;
    }

    public int getZ(int x, int z) {
        return builder.worldPos(originX + x, 0, originZ + z).getZ();
    }

    public void setupRandomBlocks(Random random) {
        NorthStructurePalette p = style == Style.GIFT
                ? NorthStructurePalette.gift(isAbandoned)
                : NorthStructurePalette.create(builder.seed());

        rockBlock = got("rock");
        rockMeta = 1;
        rockSlabBlock = got("slabSingle1");
        rockSlabMeta = 2;
        rockSlabDoubleBlock = got("slabDouble1");
        rockSlabDoubleMeta = 2;
        rockStairBlock = got("stairsAndesite");
        rockWallBlock = got("wallStone1");
        rockWallMeta = 2;
        brickBlock = got("brick1");
        brickMeta = 1;
        brickSlabBlock = got("slabSingle1");
        brickSlabMeta = 3;
        brickStairBlock = got("stairsAndesiteBrick");
        brickWallBlock = got("wallStone1");
        brickWallMeta = 3;
        brickMossyBlock = got("brick1");
        brickMossyMeta = 2;
        brickMossySlabBlock = got("slabSingle1");
        brickMossySlabMeta = 4;
        brickMossyStairBlock = got("stairsAndesiteBrickMossy");
        brickMossyWallBlock = got("wallStone1");
        brickMossyWallMeta = 4;
        brickCrackedBlock = got("brick1");
        brickCrackedMeta = 3;
        brickCrackedSlabBlock = got("slabSingle1");
        brickCrackedSlabMeta = 5;
        brickCrackedStairBlock = got("stairsAndesiteBrickCracked");
        brickCrackedWallBlock = got("wallStone1");
        brickCrackedWallMeta = 5;
        brickCarved = got("brick1");
        brickCarvedMeta = 5;
        pillarBlock = got("pillar1");
        pillarMeta = 6;
        brick2Block = got("brick2");
        brick2Meta = 11;
        brick2SlabBlock = got("slabSingle5");
        brick2SlabMeta = 3;
        brick2StairBlock = got("stairsBasaltBrick");
        brick2WallBlock = got("wallStone2");
        brick2WallMeta = 10;
        pillar2Block = got("pillar1");
        pillar2Meta = 9;
        cobbleBlock = vanilla("field_150347_e");
        cobbleMeta = 0;
        cobbleSlabBlock = vanilla("field_150333_U");
        cobbleSlabMeta = 0;
        cobbleStairBlock = vanilla("field_150372_bz");

        plankBlock = palette("planks", p.planks());
        plankMeta = 0;
        plankSlabBlock = palette("wood_slab", p.woodSlab());
        plankSlabMeta = 0;
        plankStairBlock = palette("wood_stairs", p.woodStairs());
        fenceBlock = palette("fence", p.fence());
        fenceMeta = 0;
        fenceGateBlock = palette("fence_gate", p.fenceGate());
        woodBeamBlock = palette("wood_beam", p.log());
        woodBeamMeta = 0;
        doorBlock = palette("door", p.door());
        trapdoorBlock = vanilla("field_150415_aT");
        wallBlock = random.nextBoolean() ? got("daub") : plankBlock;
        wallMeta = 0;
        roofBlock = got("thatch");
        roofMeta = 0;
        roofSlabBlock = got("slabSingleThatch");
        roofSlabMeta = 0;
        roofStairBlock = got("stairsThatch");
        barsBlock = vanilla("field_150411_aY");
        bedBlock = got("strawBed");
        gateBlock = got("gateIronBars");
        plateBlock = got("ceramicPlate");
        tableBlock = got(style == Style.GIFT ? "tableGift"
                : style == Style.WILDLING ? "tableWildling" : "tableNorth");
        cropBlock = switch (random.nextInt(4)) {
            case 0 -> vanilla("field_150459_bM");
            case 1 -> vanilla("field_150469_bN");
            case 2 -> got("lettuceCrop");
            default -> vanilla("field_150464_aj");
        };
        cropMeta = 7;
        bannerType = style == Style.GIFT ? "night" : style == Style.WILDLING ? "wildling" : "robb";

        if (style == Style.GIFT) {
            brickBlock = got("cobblebrick");
            brickMeta = 0;
            brickStairBlock = vanilla("field_150390_bg");
            brickWallBlock = got("wallStoneV");
            brickWallMeta = 1;
            cobbleBlock = vanilla("field_150347_e");
            logBlock = vanilla("field_150364_r");
            logMeta = 1;
            plankBlock = palette("gift_planks", p.planks());
            plankMeta = 1;
            plankSlabBlock = palette("gift_slab", p.woodSlab());
            plankSlabMeta = 1;
            plankStairBlock = palette("gift_stairs", p.woodStairs());
            fenceBlock = palette("gift_fence", p.fence());
            fenceMeta = 1;
            fenceGateBlock = palette("gift_gate", p.fenceGate());
            woodBeamBlock = isAbandoned ? got("rottenLog") : got("woodBeamV1");
            woodBeamMeta = isAbandoned ? 0 : 1;
            doorBlock = palette("gift_door", p.door());
            wallBlock = random.nextBoolean() ? got("daub") : plankBlock;
            wallMeta = wallBlock == plankBlock ? plankMeta : 0;
            roofBlock = got("thatch");
            roofSlabBlock = got("slabSingleThatch");
            roofStairBlock = got("stairsThatch");
            barsBlock = random.nextBoolean() ? vanilla("field_150411_aY") : got("bronzeBars");
            tableBlock = got("tableGift");
            bedBlock = got("strawBed");
            plateBlock = random.nextBoolean() ? got("woodPlate") : got("ceramicPlate");
            bannerType = "night";
            if (isAbandoned) {
                plankBlock = got("planksRotten");
                plankMeta = 0;
                plankSlabBlock = got("rottenSlabSingle");
                plankSlabMeta = 0;
                fenceBlock = got("fenceRotten");
                fenceMeta = 0;
                plankStairBlock = got("stairsRotten");
                roofBlock = vanilla("field_150350_a");
                roofSlabBlock = vanilla("field_150350_a");
                bedBlock = got("furBed");
            }
        }
    }

    protected boolean hasNorthernWood() { return style == Style.NORTH; }
    protected boolean hasSouthernWood() { return false; }
    protected boolean hasDarkSkinPeople() { return false; }
    protected boolean hasMaester() { return style == Style.NORTH; }
    protected boolean hasSepton() { return false; }

    protected LegacyBlock getChest() { return vanilla("chest"); }
    protected Object getChestContents() { return markerPrefix; }
    protected LegacyBlock getTable() { return tableBlock; }
    protected Object getBanner() { return bannerType; }

    public void setBlockAndMetadata(LegacyNorthernContext world, int x, int y, int z,
                                    LegacyBlock block, int metadata) {
        builder.set(originX + x, originY - builder.originY() + y, originZ + z, block.state(metadata));
    }

    public void setAir(LegacyNorthernContext world, int x, int y, int z) {
        builder.set(originX + x, originY - builder.originY() + y, originZ + z,
                net.minecraft.world.level.block.Blocks.AIR.defaultBlockState());
    }

    public LegacyBlock getBlock(LegacyNorthernContext world, int x, int y, int z) {
        BlockPos pos = localPos(x, y, z);
        return existing(builder.insideClip(pos)
                ? builder.level().getBlockState(pos)
                : net.minecraft.world.level.block.Blocks.STONE.defaultBlockState());
    }

    public LegacyBlock func_147439_a(int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        return existing(builder.insideClip(pos)
                ? builder.level().getBlockState(pos)
                : net.minecraft.world.level.block.Blocks.STONE.defaultBlockState());
    }

    public void func_72921_c(int x, int y, int z, int metadata, int flags) {
        BlockPos worldPos = new BlockPos(x, y, z);
        if (!builder.insideClip(worldPos)) return;
        BlockState existing = builder.level().getBlockState(worldPos);
        builder.level().setBlock(worldPos, applyMetadata(existing, metadata), flags);
    }

    public boolean isOpaque(LegacyNorthernContext world, int x, int y, int z) {
        BlockPos pos = localPos(x, y, z);
        if (!builder.insideClip(pos)) return true;
        BlockState state = builder.level().getBlockState(pos);
        return !state.isAir() && state.getFluidState().isEmpty() && state.canOcclude();
    }

    public boolean isAir(LegacyNorthernContext world, int x, int y, int z) {
        BlockPos pos = localPos(x, y, z);
        return builder.insideClip(pos) && builder.level().getBlockState(pos).isAir();
    }

    public boolean isSurface(LegacyNorthernContext world, int x, int y, int z) {
        BlockPos pos = localPos(x, y, z);
        // Surface-validation loops cover a piece's whole footprint even when
        // this invocation owns only one intersecting chunk. Out-of-clip cells
        // are validated when their own chunk slice is generated.
        if (!builder.insideClip(pos)) return true;
        BlockState state = builder.level().getBlockState(pos);
        return !state.isAir() && state.getFluidState().isEmpty();
    }

    public int getTopBlock(LegacyNorthernContext world, int x, int z) {
        BlockPos horizontal = builder.worldPos(originX + x, 0, originZ + z);
        if (!builder.insideClip(new BlockPos(horizontal.getX(), originY, horizontal.getZ()))) {
            // The settlement anchor is one block above its sampled surface, so
            // zero is the correct local top for a neighboring chunk slice.
            return 0;
        }
        int top = builder.level().getMaxBuildHeight() - 1;
        int bottom = builder.level().getMinBuildHeight();
        for (int y = top; y >= bottom; y--) {
            BlockState state = builder.level().getBlockState(new BlockPos(horizontal.getX(), y, horizontal.getZ()));
            if (!state.isAir() && state.getFluidState().isEmpty()) return y - originY + 1;
        }
        return 0;
    }

    public void findSurface(LegacyNorthernContext world, int x, int z) {
        // Legacy pieces use this only before downward foundation loops.
    }

    public void setGrassToDirt(LegacyNorthernContext world, int x, int y, int z) {
        int localY = originY - builder.originY() + y;
        BlockPos pos = builder.worldPos(originX + x, localY, originZ + z);
        if (!builder.insideClip(pos)) return;
        BlockState state = builder.level().getBlockState(pos);
        if (state.is(net.minecraft.world.level.block.Blocks.GRASS_BLOCK)
                || state.is(net.minecraft.world.level.block.Blocks.DIRT_PATH)
                || state.is(net.minecraft.world.level.block.Blocks.PODZOL)) {
            builder.set(originX + x, localY, originZ + z,
                    net.minecraft.world.level.block.Blocks.DIRT.defaultBlockState());
        }
    }

    private BlockPos localPos(int x, int y, int z) {
        return builder.worldPos(originX + x, originY - builder.originY() + y, originZ + z);
    }

    public void placeChest(LegacyNorthernContext world, Random random, int x, int y, int z,
                           int metadata, Object loot) {
        builder.chest(originX + x, originY - builder.originY() + y, originZ + z,
                legacyFacing(metadata), lootName(loot));
    }

    public void placeChest(LegacyNorthernContext world, Random random, int x, int y, int z,
                           int metadata, Object loot, int min, int max) {
        placeChest(world, random, x, y, z, metadata, loot);
    }

    public void placeChest(LegacyNorthernContext world, Random random, int x, int y, int z,
                           int metadata, Object loot, int amount) {
        placeChest(world, random, x, y, z, metadata, loot);
    }

    public void placeChest(LegacyNorthernContext world, Random random, int x, int y, int z,
                           LegacyBlock chest, int metadata, Object loot) {
        placeChest(world, random, x, y, z, metadata, loot);
    }

    public void placeChest(LegacyNorthernContext world, Random random, int x, int y, int z,
                           LegacyBlock chest, int metadata, Object loot, int amount) {
        placeChest(world, random, x, y, z, metadata, loot);
    }

    public void placeWallBanner(LegacyNorthernContext world, int x, int y, int z,
                                Object banner, int metadata) {
        marker("banner:" + String.valueOf(banner).toLowerCase(Locale.ROOT), x, y, z);
    }

    public void placeBanner(LegacyNorthernContext world, int x, int y, int z,
                            Object banner, int rotation) {
        marker("banner:" + String.valueOf(banner).toLowerCase(Locale.ROOT), x, y, z);
    }

    public void placeBarrel(LegacyNorthernContext world, Random random, int x, int y, int z,
                            int metadata, Object drink) {
        setBlockAndMetadata(world, x, y, z, got("barrel"), metadata);
    }

    public void placeMug(LegacyNorthernContext world, Random random, int x, int y, int z,
                         int metadata, Object drink) {
        marker("placed_drink", x, y, z);
    }

    public void placePlate(LegacyNorthernContext world, Random random, int x, int y, int z,
                           LegacyBlock plate, Object food) {
        setBlockAndMetadata(world, x, y, z, plate, 0);
    }

    public void placePlateWithCertainty(LegacyNorthernContext world, Random random,
                                        int x, int y, int z, LegacyBlock plate,
                                        Object food, float chance) {
        if (random.nextFloat() < chance) placePlate(world, random, x, y, z, plate, food);
    }

    public void placePlateWithCertainty(LegacyNorthernContext world, Random random,
                                        int x, int y, int z, LegacyBlock plate, Object food) {
        placePlateWithCertainty(world, random, x, y, z, plate, food, 0.5F);
    }

    public void placeFlowerPot(LegacyNorthernContext world, int x, int y, int z, LegacyBlock flower, int meta) {
        setBlockAndMetadata(world, x, y, z, vanilla("flower_pot"), 0);
    }

    public void placeSign(LegacyNorthernContext world, int x, int y, int z, LegacyBlock sign,
                          int metadata, String[] text) {
        setBlockAndMetadata(world, x, y, z, vanilla("oak_wall_sign"), metadata);
    }

    public void spawnItemFrame(LegacyNorthernContext world, int x, int y, int z,
                               int metadata, Object stack) {
        marker("item_frame", x, y, z);
    }

    public void placeArmorStand(LegacyNorthernContext world, int x, int y, int z,
                                int rotation, LegacyItemStack... equipment) {
        marker("armor_stand", x, y, z);
    }

    public void plantFlower(LegacyNorthernContext world, Random random, int x, int y, int z) {
        LegacyBlock flower = random.nextBoolean() ? vanilla("field_150328_O") : vanilla("yellow_flower");
        setBlockAndMetadata(world, x, y, z, flower, random.nextInt(4));
    }

    public LegacyBlock getRandomFlower(Random random) {
        return random.nextBoolean() ? vanilla("field_150328_O") : vanilla("yellow_flower");
    }

    public LegacyBlock getRandomFlower(LegacyNorthernContext world, Random random) {
        return getRandomFlower(random);
    }

    public void placeFlowerPot(LegacyNorthernContext world, int x, int y, int z, LegacyBlock flower) {
        placeFlowerPot(world, x, y, z, flower, 0);
    }

    protected LegacyItemStack getRandFrameItem(Random random) {
        return new LegacyItemStack("regional_display_item");
    }

    protected LegacyItemStack[] getRandArmorItems(Random random) {
        return new LegacyItemStack[]{new LegacyItemStack("regional_armor")};
    }

    public void spawnNPCAndSetHome(LegacyEntity entity, LegacyNorthernContext world,
                                   int x, int y, int z, int radius) {
        StringBuilder role = new StringBuilder(entity.role == null ? markerPrefix + "_npc" : entity.role);
        if (entity.male != null) role.append(entity.male ? "#male" : "#female");
        if (entity.child) role.append("#child");
        role.append("#home=").append(radius);
        marker(role.toString(), x, y, z);
    }

    public void placeNPCRespawner(LegacyEntity entity, LegacyNorthernContext world, int x, int y, int z) {
        marker(markerPrefix + "_respawner", x, y, z);
    }

    public void spawnLegendaryNPC(Class<?> type, LegacyNorthernContext world,
                                  int x, int y, int z, int radius) {
        marker("legendary_npc", x, y, z);
    }

    protected void spawnLegendaryMobs(LegacyNorthernContext world) {
        marker("legendary_population", 0, 1, 0);
    }

    protected LegacyEntity getBartender(LegacyNorthernContext world) { return entity("north_bartender"); }
    protected LegacyEntity getBlacksmith(LegacyNorthernContext world) { return entity("north_blacksmith"); }
    protected LegacyEntity getCaptain(LegacyNorthernContext world) { return entity("north_captain"); }
    protected LegacyEntity getFarmer(LegacyNorthernContext world) { return entity("north_farmer"); }
    protected LegacyEntity getFarmhand(LegacyNorthernContext world) { return entity("north_farmhand"); }
    protected LegacyEntity getMan(LegacyNorthernContext world) { return entity(markerPrefix + "_civilian"); }
    protected LegacyEntity getSoldier(LegacyNorthernContext world) { return entity(markerPrefix + "_soldier"); }
    protected LegacyEntity getSoldierArcher(LegacyNorthernContext world) { return entity(markerPrefix + "_archer"); }
    protected LegacyEntity createTrader(LegacyNorthernContext world) { return entity("north_market_trader_" + marketVariant); }

    protected void generateRoof(LegacyNorthernContext world, int x, int y, int z) {
        int ax = Math.abs(x);
        int az = Math.abs(z);
        int color = switch (Math.floorMod(marketVariant, 11)) {
            case 0 -> ax == az ? 15 : 7;                                      // Goldsmith
            case 1 -> Math.floorMod(ax + az, 2) == 0 ? 14 : 5;                // Miner
            case 2 -> (ax == 2 || az == 2) && Math.floorMod(ax + az, 2) == 0 ? 13 : 12;
            case 3 -> ax != 2 && az != 2 && (ax == 1 || az == 1) ? 8 : 7;
            case 4 -> Math.floorMod(ax, 2) == 0 ? 12 : 4;
            case 5 -> ax == az ? 4 : 13;
            case 6 -> ax == 2 || az == 2 ? 6 : ax == 1 || az == 1 ? 14 : 0;
            case 7 -> Math.floorMod(ax, 2) == 0 ? (az == 2 ? 0 : 3) : 11;
            case 8 -> Math.floorMod(ax + az, 2) == 0
                    ? (Integer.signum(x) != -Integer.signum(z) && ax + az == 2 ? 4 : 13) : 12;
            case 9 -> ax == az ? 15 : 7;
            default -> Math.floorMod(az, 2) == 0 ? 1 : 12;                    // Baker
        };
        setBlockAndMetadata(world, x, y, z, vanilla("wool"), color);
    }

    protected String[] getTavernName(Random random) {
        return new String[]{"The", style == Style.GIFT ? "Black Cloak" : "Direwolf"};
    }

    protected void placeBeaconTower(int x, int y, int z, int rotation, int height) {
        got.world.structure.legacy.generated.GOTStructureWesterosTower.place(
                builder.child(x, y, z, rotation, builder.seed() ^ 0x4f1bbcdcL), height);
    }

    protected void placeFarmTree(LegacyNorthernContext world, Random random) {
        LegacyBlock log = palette("farm_tree_log", builder.palette().log());
        LegacyBlock leaves = vanilla("field_150362_t");
        for (int y = 1; y <= 6; y++) setBlockAndMetadata(world, 0, y, 0, log, 0);
        for (int y = 4; y <= 8; y++) {
            int radius = y == 8 ? 1 : y >= 6 ? 2 : 3;
            for (int x = -radius; x <= radius; x++) for (int z = -radius; z <= radius; z++) {
                if (x * x + z * z <= radius * radius + 1 && !(x == 0 && z == 0 && y <= 6))
                    setBlockAndMetadata(world, x, y, z, leaves, 4);
            }
        }
    }

    public void spawnLegendaryNPC(LegacyEntity entity, LegacyNorthernContext world,
                                  int x, int y, int z) {
        marker("legendary_npc", x, y, z);
    }

    protected void leashEntityTo(LegacyEntity entity, LegacyNorthernContext world, int x, int y, int z) { }

    protected LegacyEntity entity(String role) {
        return new LegacyEntity(role);
    }

    protected LegacyBlock palette(String key, BlockState state) {
        return new LegacyBlock("palette:" + key, state);
    }

    protected void marker(String role, int x, int y, int z) {
        builder.marker(role, originX + x, originY - builder.originY() + y, originZ + z);
    }

    public LegacyBlock vanilla(String key) {
        return new LegacyBlock("vanilla:" + key, vanillaBase(key));
    }

    public LegacyBlock got(String key) {
        return new LegacyBlock("got:" + key, gotBase(key, 0));
    }

    private String lootName(Object loot) {
        if (loot == null) return markerPrefix;
        String name = String.valueOf(loot).toLowerCase(Locale.ROOT);
        if (name.contains("beyond")) return "beyond_wall";
        if (name.contains("gift")) return "gift";
        if (name.contains("north")) return "north_house";
        return markerPrefix;
    }

    private static Direction legacyFacing(int metadata) {
        return switch (metadata & 3) {
            case 0 -> Direction.EAST;
            case 1 -> Direction.WEST;
            case 2 -> Direction.SOUTH;
            default -> Direction.NORTH;
        };
    }

    private static BlockState applyMetadata(BlockState state, int metadata) {
        if (state.getBlock() instanceof StairBlock && state.hasProperty(StairBlock.FACING)) {
            state = state.setValue(StairBlock.FACING, legacyFacing(metadata));
            if (state.hasProperty(StairBlock.HALF)) {
                state = state.setValue(StairBlock.HALF,
                        (metadata & 4) != 0 ? Half.TOP : Half.BOTTOM);
            }
        } else if (state.getBlock() instanceof SlabBlock && state.hasProperty(SlabBlock.TYPE)) {
            state = state.setValue(SlabBlock.TYPE, (metadata & 8) != 0 ? SlabType.TOP : SlabType.BOTTOM);
        } else if (state.hasProperty(BlockStateProperties.AXIS)) {
            int axis = metadata & 12;
            state = state.setValue(BlockStateProperties.AXIS,
                    axis == 4 ? Direction.Axis.X : axis == 8 ? Direction.Axis.Z : Direction.Axis.Y);
        }
        if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)
                && !(state.getBlock() instanceof StairBlock)) {
            state = state.setValue(BlockStateProperties.HORIZONTAL_FACING, legacyFacing(metadata));
        }
        if (state.getBlock() instanceof DoorBlock) {
            if (state.hasProperty(DoorBlock.FACING)) state = state.setValue(DoorBlock.FACING, legacyFacing(metadata));
            if (state.hasProperty(DoorBlock.HALF)) state = state.setValue(DoorBlock.HALF,
                    (metadata & 8) != 0 ? DoubleBlockHalf.UPPER : DoubleBlockHalf.LOWER);
        }
        if (state.getBlock() instanceof BedBlock) {
            if (state.hasProperty(BedBlock.FACING)) state = state.setValue(BedBlock.FACING, legacyFacing(metadata));
            if (state.hasProperty(BedBlock.PART)) state = state.setValue(BedBlock.PART,
                    (metadata & 8) != 0 ? BedPart.HEAD : BedPart.FOOT);
        }
        if (state.getBlock() instanceof CropBlock crop && state.hasProperty(BlockStateProperties.AGE_7)) {
            state = state.setValue(BlockStateProperties.AGE_7, Math.min(crop.getMaxAge(), metadata & 7));
        }
        return state;
    }

    private static BlockState vanillaBase(String key) {
        Block block = switch (key) {
            case "field_150322_A" -> net.minecraft.world.level.block.Blocks.SANDSTONE;
            case "field_150324_C" -> net.minecraft.world.level.block.Blocks.RED_BED;
            case "field_150328_O" -> net.minecraft.world.level.block.Blocks.POPPY;
            case "field_150333_U" -> net.minecraft.world.level.block.Blocks.SMOOTH_STONE;
            case "field_150344_f" -> net.minecraft.world.level.block.Blocks.OAK_PLANKS;
            case "field_150346_d" -> net.minecraft.world.level.block.Blocks.DIRT;
            case "field_150347_e" -> net.minecraft.world.level.block.Blocks.COBBLESTONE;
            case "field_150349_c" -> net.minecraft.world.level.block.Blocks.GRASS_BLOCK;
            case "field_150350_a" -> net.minecraft.world.level.block.Blocks.AIR;
            case "field_150351_n" -> net.minecraft.world.level.block.Blocks.GRAVEL;
            case "field_150353_l" -> net.minecraft.world.level.block.Blocks.LAVA;
            case "field_150355_j" -> net.minecraft.world.level.block.Blocks.WATER;
            case "field_150362_t" -> net.minecraft.world.level.block.Blocks.OAK_LEAVES;
            case "field_150364_r" -> net.minecraft.world.level.block.Blocks.OAK_LOG;
            case "field_150372_bz" -> net.minecraft.world.level.block.Blocks.COBBLESTONE_STAIRS;
            case "field_150376_bx" -> net.minecraft.world.level.block.Blocks.OAK_SLAB;
            case "field_150383_bp" -> net.minecraft.world.level.block.Blocks.CAULDRON;
            case "field_150390_bg" -> net.minecraft.world.level.block.Blocks.BRICK_STAIRS;
            case "field_150396_be" -> net.minecraft.world.level.block.Blocks.OAK_FENCE_GATE;
            case "field_150398_cm" -> net.minecraft.world.level.block.Blocks.ROSE_BUSH;
            case "field_150404_cg" -> net.minecraft.world.level.block.Blocks.WHITE_CARPET;
            case "field_150406_ce" -> net.minecraft.world.level.block.Blocks.WHITE_TERRACOTTA;
            case "field_150407_cf" -> net.minecraft.world.level.block.Blocks.HAY_BLOCK;
            case "field_150411_aY" -> net.minecraft.world.level.block.Blocks.IRON_BARS;
            case "field_150415_aT" -> net.minecraft.world.level.block.Blocks.OAK_TRAPDOOR;
            case "field_150422_aJ" -> net.minecraft.world.level.block.Blocks.OAK_FENCE;
            case "field_150423_aK", "flower_pot" -> net.minecraft.world.level.block.Blocks.FLOWER_POT;
            case "field_150432_aD" -> net.minecraft.world.level.block.Blocks.ICE;
            case "field_150433_aE" -> net.minecraft.world.level.block.Blocks.SNOW_BLOCK;
            case "field_150442_at" -> net.minecraft.world.level.block.Blocks.LEVER;
            case "field_150444_as", "oak_wall_sign" -> net.minecraft.world.level.block.Blocks.OAK_WALL_SIGN;
            case "field_150446_ar" -> net.minecraft.world.level.block.Blocks.STONE_STAIRS;
            case "field_150458_ak" -> net.minecraft.world.level.block.Blocks.FARMLAND;
            case "field_150459_bM" -> net.minecraft.world.level.block.Blocks.CARROTS;
            case "field_150460_al" -> net.minecraft.world.level.block.Blocks.FURNACE;
            case "field_150462_ai" -> net.minecraft.world.level.block.Blocks.CRAFTING_TABLE;
            case "field_150464_aj" -> net.minecraft.world.level.block.Blocks.WHEAT;
            case "field_150465_bP" -> net.minecraft.world.level.block.Blocks.SKELETON_WALL_SKULL;
            case "field_150466_ao" -> net.minecraft.world.level.block.Blocks.OAK_DOOR;
            case "field_150467_bQ" -> net.minecraft.world.level.block.Blocks.ANVIL;
            case "field_150468_ap" -> net.minecraft.world.level.block.Blocks.LADDER;
            case "field_150469_bN" -> net.minecraft.world.level.block.Blocks.POTATOES;
            case "field_150476_ad" -> net.minecraft.world.level.block.Blocks.OAK_STAIRS;
            case "field_150478_aa" -> net.minecraft.world.level.block.Blocks.TORCH;
            case "field_150480_ab" -> net.minecraft.world.level.block.Blocks.FIRE;
            case "field_150485_bF" -> net.minecraft.world.level.block.Blocks.SPRUCE_STAIRS;
            case "field_150487_bG" -> net.minecraft.world.level.block.Blocks.BIRCH_STAIRS;
            case "wool" -> net.minecraft.world.level.block.Blocks.WHITE_WOOL;
            case "chest" -> net.minecraft.world.level.block.Blocks.CHEST;
            case "yellow_flower" -> net.minecraft.world.level.block.Blocks.DANDELION;
            default -> net.minecraft.world.level.block.Blocks.STONE;
        };
        return block.defaultBlockState();
    }

    private static BlockState gotBase(String key, int metadata) {
        String id = switch (key) {
            case "brick1" -> switch (metadata) {
                case 2 -> "mossy_andesite_bricks";
                case 3 -> "cracked_andesite_bricks";
                case 5 -> "carved_andesite_bricks";
                default -> "andesite_bricks";
            };
            case "brick2" -> "basalt_bricks";
            case "brick4" -> metadata == 6 ? "carved_basalt_westeros_bricks" : "basalt_westeros_bricks";
            case "slabSingle1" -> switch (metadata & 7) {
                case 3 -> "andesite_brick_slab";
                case 4 -> "mossy_andesite_brick_slab";
                case 5 -> "cracked_andesite_brick_slab";
                default -> "andesite_slab";
            };
            case "slabDouble1" -> "smooth_andesite";
            case "slabSingle5" -> "basalt_brick_slab";
            case "wallStone1" -> switch (metadata) {
                case 3 -> "andesite_brick_wall";
                case 4 -> "mossy_andesite_brick_wall";
                case 5 -> "cracked_andesite_brick_wall";
                default -> "andesite_wall";
            };
            case "wallStone2" -> "basalt_brick_wall";
            case "wallStoneV" -> "andesite_brick_wall";
            case "pillar1" -> metadata == 9 ? "basalt_pillar" : "andesite_pillar";
            case "rock" -> "andesite_rock";
            case "stairsAndesite" -> "stairs_andesite";
            case "stairsAndesiteBrick" -> "stairs_andesite_brick";
            case "stairsAndesiteBrickMossy" -> "stairs_andesite_brick_mossy";
            case "stairsAndesiteBrickCracked" -> "stairs_andesite_brick_cracked";
            case "stairsBasaltBrick" -> "stairs_basalt_brick";
            case "cobblebrick" -> "cobblestone_bricks";
            case "brickIce" -> "brick_ice_bricks";
            case "thatch" -> "thatch_thatch";
            case "slabSingleThatch" -> "thatch_slab";
            case "stairsThatch" -> "stairs_thatch";
            case "planksRotten" -> "rotten_planks";
            case "rottenLog" -> "wood_beam_rotten";
            case "rottenSlabSingle" -> "wood_slab_rotten";
            case "stairsRotten" -> "stairs_rotten";
            case "fenceRotten" -> "fence_rotten";
            case "doorSpruce" -> "door_spruce";
            case "fenceGateSpruce" -> "fence_gate_spruce";
            case "woodBeamV1" -> "wood_beam_spruce";
            case "chandelier" -> "iron_chandelier";
            case "glassPane" -> "fine_glass_pane";
            case "plate" -> "wooden_plate";
            default -> camelToSnake(key);
        };
        Block fallback = fallbackFor(id);
        Block found = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(GOTMod.MOD_ID, id));
        return (found == null || found == net.minecraft.world.level.block.Blocks.AIR
                ? fallback : found).defaultBlockState();
    }

    private static Block fallbackFor(String id) {
        if (id.equals("dirt_path")) return net.minecraft.world.level.block.Blocks.DIRT_PATH;
        if (id.equals("armor_stand")) return net.minecraft.world.level.block.Blocks.IRON_BARS;
        if (id.contains("stairs")) return net.minecraft.world.level.block.Blocks.SPRUCE_STAIRS;
        if (id.contains("slab")) return net.minecraft.world.level.block.Blocks.STONE_SLAB;
        if (id.contains("wall")) return net.minecraft.world.level.block.Blocks.COBBLESTONE_WALL;
        if (id.contains("fence_gate")) return net.minecraft.world.level.block.Blocks.SPRUCE_FENCE_GATE;
        if (id.contains("fence")) return net.minecraft.world.level.block.Blocks.SPRUCE_FENCE;
        if (id.contains("door")) return net.minecraft.world.level.block.Blocks.SPRUCE_DOOR;
        if (id.contains("bed")) return net.minecraft.world.level.block.Blocks.GRAY_BED;
        if (id.contains("bars") || id.startsWith("gate_")) return net.minecraft.world.level.block.Blocks.IRON_BARS;
        if (id.contains("table")) return net.minecraft.world.level.block.Blocks.CRAFTING_TABLE;
        if (id.contains("crop")) return net.minecraft.world.level.block.Blocks.WHEAT;
        if (id.contains("thatch")) return net.minecraft.world.level.block.Blocks.HAY_BLOCK;
        if (id.contains("planks")) return net.minecraft.world.level.block.Blocks.SPRUCE_PLANKS;
        if (id.contains("log") || id.contains("beam") || id.contains("pillar")) return net.minecraft.world.level.block.Blocks.SPRUCE_LOG;
        return net.minecraft.world.level.block.Blocks.STONE;
    }

    private static String camelToSnake(String value) {
        return value.replaceAll("([a-z0-9])([A-Z])", "$1_$2").toLowerCase(Locale.ROOT);
    }

    public final class LegacyBlock {
        private final String key;
        private final BlockState base;

        private LegacyBlock(String key, BlockState base) {
            this.key = key;
            this.base = base;
        }

        public boolean func_149662_c() { return base.canOcclude(); }
        public BlockState state(int metadata) {
            BlockState selected = key.startsWith("got:")
                    ? gotBase(key.substring(4), metadata) : base;
            if (key.equals("vanilla:field_150364_r") && (metadata & 3) == 1)
                selected = net.minecraft.world.level.block.Blocks.SPRUCE_LOG.defaultBlockState();
            if (key.equals("vanilla:field_150344_f") && (metadata & 3) == 1)
                selected = net.minecraft.world.level.block.Blocks.SPRUCE_PLANKS.defaultBlockState();
            if (key.equals("vanilla:field_150376_bx") && (metadata & 7) == 1)
                selected = net.minecraft.world.level.block.Blocks.SPRUCE_SLAB.defaultBlockState();
            if (key.equals("vanilla:field_150422_aJ") && (metadata & 3) == 1)
                selected = net.minecraft.world.level.block.Blocks.SPRUCE_FENCE.defaultBlockState();
            if (key.equals("vanilla:wool")) selected = wool(metadata);
            if (key.equals("vanilla:field_150404_cg")) selected = carpet(metadata);
            if (key.equals("vanilla:field_150406_ce")) selected = terracotta(metadata);
            if (key.equals("vanilla:field_150478_aa") && metadata >= 1 && metadata <= 4) {
                selected = net.minecraft.world.level.block.Blocks.WALL_TORCH.defaultBlockState()
                        .setValue(WallTorchBlock.FACING, legacyFacing(metadata));
            }
            return applyMetadata(selected, metadata);
        }
    }

    private LegacyBlock existing(BlockState state) {
        return new LegacyBlock("existing", state);
    }

    private static BlockState wool(int metadata) {
        Block[] colors = {
                net.minecraft.world.level.block.Blocks.WHITE_WOOL, net.minecraft.world.level.block.Blocks.ORANGE_WOOL,
                net.minecraft.world.level.block.Blocks.MAGENTA_WOOL, net.minecraft.world.level.block.Blocks.LIGHT_BLUE_WOOL,
                net.minecraft.world.level.block.Blocks.YELLOW_WOOL, net.minecraft.world.level.block.Blocks.LIME_WOOL,
                net.minecraft.world.level.block.Blocks.PINK_WOOL, net.minecraft.world.level.block.Blocks.GRAY_WOOL,
                net.minecraft.world.level.block.Blocks.LIGHT_GRAY_WOOL, net.minecraft.world.level.block.Blocks.CYAN_WOOL,
                net.minecraft.world.level.block.Blocks.PURPLE_WOOL, net.minecraft.world.level.block.Blocks.BLUE_WOOL,
                net.minecraft.world.level.block.Blocks.BROWN_WOOL, net.minecraft.world.level.block.Blocks.GREEN_WOOL,
                net.minecraft.world.level.block.Blocks.RED_WOOL, net.minecraft.world.level.block.Blocks.BLACK_WOOL
        };
        return colors[Math.floorMod(metadata, colors.length)].defaultBlockState();
    }

    private static BlockState carpet(int metadata) {
        Block[] colors = {
                net.minecraft.world.level.block.Blocks.WHITE_CARPET, net.minecraft.world.level.block.Blocks.ORANGE_CARPET,
                net.minecraft.world.level.block.Blocks.MAGENTA_CARPET, net.minecraft.world.level.block.Blocks.LIGHT_BLUE_CARPET,
                net.minecraft.world.level.block.Blocks.YELLOW_CARPET, net.minecraft.world.level.block.Blocks.LIME_CARPET,
                net.minecraft.world.level.block.Blocks.PINK_CARPET, net.minecraft.world.level.block.Blocks.GRAY_CARPET,
                net.minecraft.world.level.block.Blocks.LIGHT_GRAY_CARPET, net.minecraft.world.level.block.Blocks.CYAN_CARPET,
                net.minecraft.world.level.block.Blocks.PURPLE_CARPET, net.minecraft.world.level.block.Blocks.BLUE_CARPET,
                net.minecraft.world.level.block.Blocks.BROWN_CARPET, net.minecraft.world.level.block.Blocks.GREEN_CARPET,
                net.minecraft.world.level.block.Blocks.RED_CARPET, net.minecraft.world.level.block.Blocks.BLACK_CARPET
        };
        return colors[Math.floorMod(metadata, colors.length)].defaultBlockState();
    }

    private static BlockState terracotta(int metadata) {
        Block[] colors = {
                net.minecraft.world.level.block.Blocks.WHITE_TERRACOTTA, net.minecraft.world.level.block.Blocks.ORANGE_TERRACOTTA,
                net.minecraft.world.level.block.Blocks.MAGENTA_TERRACOTTA, net.minecraft.world.level.block.Blocks.LIGHT_BLUE_TERRACOTTA,
                net.minecraft.world.level.block.Blocks.YELLOW_TERRACOTTA, net.minecraft.world.level.block.Blocks.LIME_TERRACOTTA,
                net.minecraft.world.level.block.Blocks.PINK_TERRACOTTA, net.minecraft.world.level.block.Blocks.GRAY_TERRACOTTA,
                net.minecraft.world.level.block.Blocks.LIGHT_GRAY_TERRACOTTA, net.minecraft.world.level.block.Blocks.CYAN_TERRACOTTA,
                net.minecraft.world.level.block.Blocks.PURPLE_TERRACOTTA, net.minecraft.world.level.block.Blocks.BLUE_TERRACOTTA,
                net.minecraft.world.level.block.Blocks.BROWN_TERRACOTTA, net.minecraft.world.level.block.Blocks.GREEN_TERRACOTTA,
                net.minecraft.world.level.block.Blocks.RED_TERRACOTTA, net.minecraft.world.level.block.Blocks.BLACK_TERRACOTTA
        };
        return colors[Math.floorMod(metadata, colors.length)].defaultBlockState();
    }

    public static class LegacyEntity {
        private String role;
        private Boolean male;
        private boolean child;
        public LegacyEntity(Object... ignored) { }
        public LegacyEntity(String role) { this.role = role; }
        public LegacyEntity getFamilyInfo() { return this; }
        public LegacyEntity getNPCItemsInv() { return this; }
        public LegacyEntity getHiredNPCInfo() { return this; }
        public LegacyEntity getRNG() { return this; }
        public void setMale(boolean value) { this.male = value; }
        public void setChild() { this.child = true; }
        public void setMeleeWeapon(Object value) { }
        public void setRangedWeapon(Object value) { }
        public void setSpawnRidingHorse(boolean value) { }
        public void setHomeArea(int x, int y, int z, int radius) { }
        public void setCurrentItemOrArmor(int slot, Object stack) { }
        public void func_70062_b(int slot, Object stack) { }
        public void func_110177_bN() { }
        public void func_110214_p(int value) { }
        public void saddleMountForWorldGen() { }
        public void setSpawnClass1(Class<?> value) { }
        public void setSpawnClass2(Class<?> value) { }
        public void setCheckRanges(int... value) { }
        public void setSpawnRanges(int... value) { }
        public void setSpawnAmounts(int... value) { }
        public void setUnitTrade(boolean value) { }
        public void setSeedsItem(Object value) { }
        public boolean nextBoolean() { return false; }
        public int nextInt(int bound) { return 0; }
    }

    public static class LegacyItemStack {
        public LegacyItemStack(Object... ignored) { }
        public LegacyItemStack copy() { return this; }
        public void setStackDisplayName(String value) { }
    }
}
