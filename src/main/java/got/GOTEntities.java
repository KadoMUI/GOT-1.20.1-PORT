package got;

import got.npc.GOTNorthNpcEntity;
import got.npc.GOTRiverlandsNpcEntity;
import got.npc.GOTThrownAxeEntity;
import got.npc.GOTWesterlandsNpcEntity;
import got.npc.GOTArrynNpcEntity;
import got.npc.GOTCrownlandsNpcEntity;
import got.npc.GOTDragonstoneNpcEntity;
import got.npc.GOTReachNpcEntity;
import got.npc.GOTStormlandsNpcEntity;
import got.npc.GOTDorneNpcEntity;
import got.npc.GOTIronbornNpcEntity;
import got.npc.GOTWildlingNpcEntity;
import got.npc.GOTNightWatchNpcEntity;
import got.npc.GOTWhiteWalkerNpcEntity;
import got.npc.GOTBraavosNpcEntity;
import got.npc.GOTPentosNpcEntity;
import got.npc.GOTVolantisNpcEntity;
import got.npc.GOTLysNpcEntity;
import got.npc.GOTMyrNpcEntity;
import got.npc.GOTTyroshNpcEntity;
import got.npc.GOTGhiscarNpcEntity;
import got.npc.GOTDothrakiNpcEntity;
import got.npc.GOTYiTiNpcEntity;
import got.npc.GOTAsshaiNpcEntity;
import got.npc.GOTIbbenNpcEntity;
import got.npc.GOTJogosNhaiNpcEntity;
import got.npc.GOTQarthNpcEntity;
import got.npc.GOTLorathNpcEntity;
import got.npc.GOTQohorNpcEntity;
import got.npc.GOTLhazarNpcEntity;
import got.npc.GOTNorvosNpcEntity;
import got.npc.GOTMossovyNpcEntity;
import got.npc.GOTGoldenCompanyNpcEntity;
import got.npc.GOTSummerIslesNpcEntity;
import got.npc.GOTSothoryosNpcEntity;
import got.npc.GOTUlthosSpiderEntity;
import got.npc.GOTBlizzardEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/** Entity registrations needed by the player-placeable heraldry system. */
public final class GOTEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, GOTMod.MOD_ID);

    public static final RegistryObject<EntityType<GOTStandingBannerEntity>> STANDING_BANNER =
            ENTITIES.register("standing_banner", () -> EntityType.Builder
                    .<GOTStandingBannerEntity>of(GOTStandingBannerEntity::new, MobCategory.MISC)
                    .sized(1.0F, 3.0F)
                    .clientTrackingRange(10)
                    .updateInterval(20)
                    .build("standing_banner"));

    public static final RegistryObject<EntityType<GOTWallBannerEntity>> WALL_BANNER =
            ENTITIES.register("wall_banner", () -> EntityType.Builder
                    .<GOTWallBannerEntity>of(GOTWallBannerEntity::new, MobCategory.MISC)
                    .sized(1.0F, 2.0F)
                    .clientTrackingRange(10)
                    .updateInterval(20)
                    .build("wall_banner"));

    public static final RegistryObject<EntityType<GOTNorthNpcEntity>> NORTH_NPC =
            ENTITIES.register("north_npc", () -> EntityType.Builder
                    .of(GOTNorthNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F)
                    .clientTrackingRange(10)
                    .updateInterval(3)
                    .build("north_npc"));

    public static final RegistryObject<EntityType<GOTWesterlandsNpcEntity>> WESTERLANDS_NPC =
            ENTITIES.register("westerlands_npc", () -> EntityType.Builder
                    .of(GOTWesterlandsNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F)
                    .clientTrackingRange(10)
                    .updateInterval(3)
                    .build("westerlands_npc"));

    public static final RegistryObject<EntityType<GOTRiverlandsNpcEntity>> RIVERLANDS_NPC =
            ENTITIES.register("riverlands_npc", () -> EntityType.Builder
                    .of(GOTRiverlandsNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F)
                    .clientTrackingRange(10)
                    .updateInterval(3)
                    .build("riverlands_npc"));

    public static final RegistryObject<EntityType<GOTArrynNpcEntity>> ARRYN_NPC =
            ENTITIES.register("arryn_npc", () -> EntityType.Builder
                    .of(GOTArrynNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F)
                    .clientTrackingRange(10)
                    .updateInterval(3)
                    .build("arryn_npc"));

    public static final RegistryObject<EntityType<GOTCrownlandsNpcEntity>> CROWNLANDS_NPC =
            ENTITIES.register("crownlands_npc", () -> EntityType.Builder
                    .of(GOTCrownlandsNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(10).updateInterval(3)
                    .build("crownlands_npc"));

    public static final RegistryObject<EntityType<GOTDragonstoneNpcEntity>> DRAGONSTONE_NPC =
            ENTITIES.register("dragonstone_npc", () -> EntityType.Builder
                    .of(GOTDragonstoneNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(10).updateInterval(3)
                    .build("dragonstone_npc"));

    public static final RegistryObject<EntityType<GOTReachNpcEntity>> REACH_NPC =
            ENTITIES.register("reach_npc", () -> EntityType.Builder
                    .of(GOTReachNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(10).updateInterval(3)
                    .build("reach_npc"));

    public static final RegistryObject<EntityType<GOTStormlandsNpcEntity>> STORMLANDS_NPC =
            ENTITIES.register("stormlands_npc", () -> EntityType.Builder
                    .of(GOTStormlandsNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(10).updateInterval(3)
                    .build("stormlands_npc"));

    public static final RegistryObject<EntityType<GOTDorneNpcEntity>> DORNE_NPC =
            ENTITIES.register("dorne_npc", () -> EntityType.Builder
                    .of(GOTDorneNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(10).updateInterval(3)
                    .build("dorne_npc"));

    public static final RegistryObject<EntityType<GOTIronbornNpcEntity>> IRONBORN_NPC =
            ENTITIES.register("ironborn_npc", () -> EntityType.Builder
                    .of(GOTIronbornNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(10).updateInterval(3)
                    .build("ironborn_npc"));

    public static final RegistryObject<EntityType<GOTWildlingNpcEntity>> WILDLING_NPC =
            ENTITIES.register("wildling_npc", () -> EntityType.Builder
                    .of(GOTWildlingNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(12).updateInterval(3)
                    .build("wildling_npc"));

    public static final RegistryObject<EntityType<GOTNightWatchNpcEntity>> NIGHT_WATCH_NPC =
            ENTITIES.register("night_watch_npc", () -> EntityType.Builder
                    .of(GOTNightWatchNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(10).updateInterval(3)
                    .build("night_watch_npc"));

    public static final RegistryObject<EntityType<GOTWhiteWalkerNpcEntity>> WHITE_WALKER_NPC =
            ENTITIES.register("white_walker_npc", () -> EntityType.Builder
                    .of(GOTWhiteWalkerNpcEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.8F).clientTrackingRange(12).updateInterval(3)
                    .build("white_walker_npc"));

    public static final RegistryObject<EntityType<GOTBraavosNpcEntity>> BRAAVOS_NPC =
            ENTITIES.register("braavos_npc", () -> EntityType.Builder
                    .of(GOTBraavosNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(10).updateInterval(3)
                    .build("braavos_npc"));

    public static final RegistryObject<EntityType<GOTPentosNpcEntity>> PENTOS_NPC =
            ENTITIES.register("pentos_npc", () -> EntityType.Builder
                    .of(GOTPentosNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(10).updateInterval(3)
                    .build("pentos_npc"));

    public static final RegistryObject<EntityType<GOTVolantisNpcEntity>> VOLANTIS_NPC =
            ENTITIES.register("volantis_npc", () -> EntityType.Builder
                    .of(GOTVolantisNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(10).updateInterval(3)
                    .build("volantis_npc"));

    public static final RegistryObject<EntityType<GOTLysNpcEntity>> LYS_NPC =
            ENTITIES.register("lys_npc", () -> EntityType.Builder
                    .of(GOTLysNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(10).updateInterval(3)
                    .build("lys_npc"));

    public static final RegistryObject<EntityType<GOTMyrNpcEntity>> MYR_NPC =
            ENTITIES.register("myr_npc", () -> EntityType.Builder
                    .of(GOTMyrNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(10).updateInterval(3)
                    .build("myr_npc"));

    public static final RegistryObject<EntityType<GOTTyroshNpcEntity>> TYROSH_NPC =
            ENTITIES.register("tyrosh_npc", () -> EntityType.Builder
                    .of(GOTTyroshNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(10).updateInterval(3)
                    .build("tyrosh_npc"));

    public static final RegistryObject<EntityType<GOTGhiscarNpcEntity>> GHISCAR_NPC =
            ENTITIES.register("ghiscar_npc", () -> EntityType.Builder
                    .of(GOTGhiscarNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(10).updateInterval(3)
                    .build("ghiscar_npc"));

    public static final RegistryObject<EntityType<GOTDothrakiNpcEntity>> DOTHRAKI_NPC =
            ENTITIES.register("dothraki_npc", () -> EntityType.Builder
                    .of(GOTDothrakiNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(10).updateInterval(3)
                    .build("dothraki_npc"));

    public static final RegistryObject<EntityType<GOTYiTiNpcEntity>> YI_TI_NPC =
            ENTITIES.register("yi_ti_npc", () -> EntityType.Builder
                    .of(GOTYiTiNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(10).updateInterval(3)
                    .build("yi_ti_npc"));

    public static final RegistryObject<EntityType<GOTAsshaiNpcEntity>> ASSHAI_NPC =
            ENTITIES.register("asshai_npc", () -> EntityType.Builder
                    .of(GOTAsshaiNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(10).updateInterval(3)
                    .build("asshai_npc"));

    public static final RegistryObject<EntityType<GOTIbbenNpcEntity>> IBBEN_NPC =
            ENTITIES.register("ibben_npc", () -> EntityType.Builder
                    .of(GOTIbbenNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(10).updateInterval(3)
                    .build("ibben_npc"));

    public static final RegistryObject<EntityType<GOTJogosNhaiNpcEntity>> JOGOS_NHAI_NPC =
            ENTITIES.register("jogos_nhai_npc", () -> EntityType.Builder
                    .of(GOTJogosNhaiNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(10).updateInterval(3)
                    .build("jogos_nhai_npc"));

    public static final RegistryObject<EntityType<GOTQarthNpcEntity>> QARTH_NPC =
            ENTITIES.register("qarth_npc", () -> EntityType.Builder
                    .of(GOTQarthNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(10).updateInterval(3)
                    .build("qarth_npc"));

    public static final RegistryObject<EntityType<GOTLorathNpcEntity>> LORATH_NPC =
            ENTITIES.register("lorath_npc", () -> EntityType.Builder
                    .of(GOTLorathNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(10).updateInterval(3)
                    .build("lorath_npc"));

    public static final RegistryObject<EntityType<GOTQohorNpcEntity>> QOHOR_NPC =
            ENTITIES.register("qohor_npc", () -> EntityType.Builder
                    .of(GOTQohorNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(10).updateInterval(3)
                    .build("qohor_npc"));

    public static final RegistryObject<EntityType<GOTLhazarNpcEntity>> LHAZAR_NPC =
            ENTITIES.register("lhazar_npc", () -> EntityType.Builder
                    .of(GOTLhazarNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(10).updateInterval(3)
                    .build("lhazar_npc"));

    public static final RegistryObject<EntityType<GOTNorvosNpcEntity>> NORVOS_NPC =
            ENTITIES.register("norvos_npc", () -> EntityType.Builder
                    .of(GOTNorvosNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(10).updateInterval(3)
                    .build("norvos_npc"));

    public static final RegistryObject<EntityType<GOTMossovyNpcEntity>> MOSSOVY_NPC =
            ENTITIES.register("mossovy_npc", () -> EntityType.Builder
                    .of(GOTMossovyNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(10).updateInterval(3)
                    .build("mossovy_npc"));

    public static final RegistryObject<EntityType<GOTGoldenCompanyNpcEntity>> GOLDEN_COMPANY_NPC =
            ENTITIES.register("golden_company_npc", () -> EntityType.Builder
                    .of(GOTGoldenCompanyNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(10).updateInterval(3)
                    .build("golden_company_npc"));

    public static final RegistryObject<EntityType<GOTSummerIslesNpcEntity>> SUMMER_ISLES_NPC =
            ENTITIES.register("summer_isles_npc", () -> EntityType.Builder
                    .of(GOTSummerIslesNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(10).updateInterval(3)
                    .build("summer_isles_npc"));

    public static final RegistryObject<EntityType<GOTSothoryosNpcEntity>> SOTHORYOS_NPC =
            ENTITIES.register("sothoryos_npc", () -> EntityType.Builder
                    .of(GOTSothoryosNpcEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(10).updateInterval(3)
                    .build("sothoryos_npc"));

    public static final RegistryObject<EntityType<GOTUlthosSpiderEntity>> ULTHOS_SPIDER =
            ENTITIES.register("ulthos_spider", () -> EntityType.Builder
                    .of(GOTUlthosSpiderEntity::new, MobCategory.MONSTER)
                    .sized(0.7F, 0.5F).clientTrackingRange(8).updateInterval(3)
                    .build("ulthos_spider"));

    public static final RegistryObject<EntityType<GOTBlizzardEntity>> BLIZZARD =
            ENTITIES.register("blizzard", () -> EntityType.Builder
                    .of(GOTBlizzardEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.8F).fireImmune().clientTrackingRange(10).updateInterval(3)
                    .build("blizzard"));

    public static final RegistryObject<EntityType<GOTThrownAxeEntity>> THROWN_AXE =
            ENTITIES.register("thrown_axe", () -> EntityType.Builder
                    .<GOTThrownAxeEntity>of(GOTThrownAxeEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("thrown_axe"));

    private GOTEntities() {}

    public static void register(IEventBus modBus) {
        ENTITIES.register(modBus);
        modBus.addListener(GOTEntities::createAttributes);
        modBus.addListener(GOTEntities::commonSetup);
    }

    private static void createAttributes(EntityAttributeCreationEvent event) {
        event.put(NORTH_NPC.get(), GOTNorthNpcEntity.createAttributes().build());
        event.put(WESTERLANDS_NPC.get(), GOTWesterlandsNpcEntity.createAttributes().build());
        event.put(RIVERLANDS_NPC.get(), GOTRiverlandsNpcEntity.createAttributes().build());
        event.put(ARRYN_NPC.get(), GOTArrynNpcEntity.createAttributes().build());
        event.put(CROWNLANDS_NPC.get(), GOTCrownlandsNpcEntity.createAttributes().build());
        event.put(DRAGONSTONE_NPC.get(), GOTDragonstoneNpcEntity.createAttributes().build());
        event.put(REACH_NPC.get(), GOTReachNpcEntity.createAttributes().build());
        event.put(STORMLANDS_NPC.get(), GOTStormlandsNpcEntity.createAttributes().build());
        event.put(DORNE_NPC.get(), GOTDorneNpcEntity.createAttributes().build());
        event.put(IRONBORN_NPC.get(), GOTIronbornNpcEntity.createAttributes().build());
        event.put(WILDLING_NPC.get(), GOTWildlingNpcEntity.createAttributes().build());
        event.put(NIGHT_WATCH_NPC.get(), GOTNightWatchNpcEntity.createAttributes().build());
        event.put(WHITE_WALKER_NPC.get(), GOTWhiteWalkerNpcEntity.createAttributes().build());
        event.put(BRAAVOS_NPC.get(), GOTBraavosNpcEntity.createAttributes().build());
        event.put(PENTOS_NPC.get(), GOTPentosNpcEntity.createAttributes().build());
        event.put(VOLANTIS_NPC.get(), GOTVolantisNpcEntity.createAttributes().build());
        event.put(LYS_NPC.get(), GOTLysNpcEntity.createAttributes().build());
        event.put(MYR_NPC.get(), GOTMyrNpcEntity.createAttributes().build());
        event.put(TYROSH_NPC.get(), GOTTyroshNpcEntity.createAttributes().build());
        event.put(GHISCAR_NPC.get(), GOTGhiscarNpcEntity.createAttributes().build());
        event.put(DOTHRAKI_NPC.get(), GOTDothrakiNpcEntity.createAttributes().build());
        event.put(YI_TI_NPC.get(), GOTYiTiNpcEntity.createAttributes().build());
        event.put(ASSHAI_NPC.get(), GOTAsshaiNpcEntity.createAttributes().build());
        event.put(IBBEN_NPC.get(), GOTIbbenNpcEntity.createAttributes().build());
        event.put(JOGOS_NHAI_NPC.get(), GOTJogosNhaiNpcEntity.createAttributes().build());
        event.put(QARTH_NPC.get(), GOTQarthNpcEntity.createAttributes().build());
        event.put(LORATH_NPC.get(), GOTLorathNpcEntity.createAttributes().build());
        event.put(QOHOR_NPC.get(), GOTQohorNpcEntity.createAttributes().build());
        event.put(LHAZAR_NPC.get(), GOTLhazarNpcEntity.createAttributes().build());
        event.put(NORVOS_NPC.get(), GOTNorvosNpcEntity.createAttributes().build());
        event.put(MOSSOVY_NPC.get(), GOTMossovyNpcEntity.createAttributes().build());
        event.put(GOLDEN_COMPANY_NPC.get(), GOTGoldenCompanyNpcEntity.createAttributes().build());
        event.put(SUMMER_ISLES_NPC.get(), GOTSummerIslesNpcEntity.createAttributes().build());
        event.put(SOTHORYOS_NPC.get(), GOTSothoryosNpcEntity.createAttributes().build());
        event.put(ULTHOS_SPIDER.get(), GOTUlthosSpiderEntity.createAttributes().build());
        event.put(BLIZZARD.get(), GOTBlizzardEntity.createAttributes().build());
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> SpawnPlacements.register(NORTH_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(WESTERLANDS_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(RIVERLANDS_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(ARRYN_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(CROWNLANDS_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(DRAGONSTONE_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(REACH_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(STORMLANDS_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(DORNE_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(IRONBORN_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(WILDLING_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(NIGHT_WATCH_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(WHITE_WALKER_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) -> level.getRawBrightness(pos, 0) <= 7
                        && level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                        && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                        && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(BRAAVOS_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(PENTOS_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(VOLANTIS_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(LYS_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(MYR_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(TYROSH_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(GHISCAR_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(DOTHRAKI_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(YI_TI_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(ASSHAI_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(IBBEN_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(JOGOS_NHAI_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(QARTH_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(LORATH_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(QOHOR_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(LHAZAR_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(NORVOS_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(MOSSOVY_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(GOLDEN_COMPANY_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(SUMMER_ISLES_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(SOTHORYOS_NPC.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
        event.enqueueWork(() -> SpawnPlacements.register(ULTHOS_SPIDER.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                net.minecraft.world.entity.monster.Monster::checkMonsterSpawnRules));
        event.enqueueWork(() -> SpawnPlacements.register(BLIZZARD.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type)
                                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()));
    }
}
