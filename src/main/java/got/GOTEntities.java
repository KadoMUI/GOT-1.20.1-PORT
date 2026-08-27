package got;

import got.npc.GOTNorthNpcEntity;
import got.mount.*;
import got.wildlife.*;
import got.special.*;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.Parrot;
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
import got.npc.GOTJaqenHgharEntity;
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
import got.npc.GOTCrocodileEntity;
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

    public static final RegistryObject<EntityType<GOTJaqenHgharEntity>> JAQEN_HGHAR =
            ENTITIES.register("jaqen_hghar", () -> EntityType.Builder
                    .of(GOTJaqenHgharEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(12).updateInterval(3)
                    .build("jaqen_hghar"));

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

    public static final RegistryObject<EntityType<GOTCrocodileEntity>> CROCODILE =
            ENTITIES.register("crocodile", () -> EntityType.Builder
                    .of(GOTCrocodileEntity::new, MobCategory.MONSTER)
                    .sized(2.1F, 0.7F).clientTrackingRange(8).updateInterval(3)
                    .build("crocodile"));

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

    public static final RegistryObject<EntityType<GOTPebbleEntity>> PEBBLE_PROJECTILE =
            ENTITIES.register("pebble_projectile", () -> EntityType.Builder.<GOTPebbleEntity>of(GOTPebbleEntity::new, MobCategory.MISC).sized(0.25F,0.25F).clientTrackingRange(4).updateInterval(10).build("pebble_projectile"));
    public static final RegistryObject<EntityType<GOTDartEntity>> DART_PROJECTILE =
            ENTITIES.register("dart_projectile", () -> EntityType.Builder.<GOTDartEntity>of(GOTDartEntity::new, MobCategory.MISC).sized(0.2F,0.2F).clientTrackingRange(4).updateInterval(10).build("dart_projectile"));

    public static final RegistryObject<EntityType<GOTThrownAxeEntity>> THROWN_AXE =
            ENTITIES.register("thrown_axe", () -> EntityType.Builder
                    .<GOTThrownAxeEntity>of(GOTThrownAxeEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("thrown_axe"));

    public static final RegistryObject<EntityType<GOTLegacySpearEntity>> SPEAR_PROJECTILE =
            ENTITIES.register("spear_projectile", () -> EntityType.Builder
                    .<GOTLegacySpearEntity>of(GOTLegacySpearEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(6).updateInterval(10).build("spear_projectile"));

    public static final RegistryObject<EntityType<GOTFirePotEntity>> FIRE_POT_PROJECTILE =
            ENTITIES.register("fire_pot_projectile", () -> EntityType.Builder
                    .<GOTFirePotEntity>of(GOTFirePotEntity::new, MobCategory.MISC)
                    .sized(0.35F, 0.35F).clientTrackingRange(6).updateInterval(10).build("fire_pot_projectile"));

    public static final RegistryObject<EntityType<GOTLegacyArrowEntity>> LEGACY_ARROW_PROJECTILE =
            ENTITIES.register("legacy_arrow_projectile", () -> EntityType.Builder
                    .<GOTLegacyArrowEntity>of(GOTLegacyArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).clientTrackingRange(8).updateInterval(20).build("legacy_arrow_projectile"));

    public static final RegistryObject<EntityType<GOTMountEntity>> GOT_HORSE =
            ENTITIES.register("horse", () -> EntityType.Builder.<GOTMountEntity>of(GOTMountEntity::new, MobCategory.CREATURE).sized(1.3965F, 1.6F).clientTrackingRange(10).build("horse"));
    public static final RegistryObject<EntityType<GOTZebraEntity>> ZEBRA =
            ENTITIES.register("zebra", () -> EntityType.Builder.<GOTZebraEntity>of(GOTZebraEntity::new, MobCategory.CREATURE).sized(1.3965F, 1.6F).clientTrackingRange(10).build("zebra"));
    public static final RegistryObject<EntityType<GOTRhinoEntity>> RHINO =
            ENTITIES.register("rhino", () -> EntityType.Builder.<GOTRhinoEntity>of(GOTRhinoEntity::new, MobCategory.CREATURE).sized(1.7F, 1.8F).clientTrackingRange(10).build("rhino"));
    public static final RegistryObject<EntityType<GOTWoolyRhinoEntity>> WOOLY_RHINO =
            ENTITIES.register("wooly_rhino", () -> EntityType.Builder.<GOTWoolyRhinoEntity>of(GOTWoolyRhinoEntity::new, MobCategory.CREATURE).sized(1.7F, 1.8F).clientTrackingRange(10).build("wooly_rhino"));
    public static final RegistryObject<EntityType<GOTCamelEntity>> CAMEL =
            ENTITIES.register("camel", () -> EntityType.Builder.<GOTCamelEntity>of(GOTCamelEntity::new, MobCategory.CREATURE).sized(1.5F, 2.0F).clientTrackingRange(10).build("camel"));
    public static final RegistryObject<EntityType<GOTBoarEntity>> BOAR =
            ENTITIES.register("boar", () -> EntityType.Builder.<GOTBoarEntity>of(GOTBoarEntity::new, MobCategory.CREATURE).sized(1.2F, 1.1F).clientTrackingRange(10).build("boar"));
    public static final RegistryObject<EntityType<GOTDeerEntity>> DEER =
            ENTITIES.register("deer", () -> EntityType.Builder.<GOTDeerEntity>of(GOTDeerEntity::new, MobCategory.CREATURE).sized(1.1F, 1.6F).clientTrackingRange(10).build("deer"));
    public static final RegistryObject<EntityType<GOTBearEntity>> BEAR =
            ENTITIES.register("bear", () -> EntityType.Builder.<GOTBearEntity>of(GOTBearEntity::new, MobCategory.CREATURE).sized(1.6F, 1.8F).clientTrackingRange(10).build("bear"));
    public static final RegistryObject<EntityType<GOTSnowBearEntity>> SNOW_BEAR =
            ENTITIES.register("snow_bear", () -> EntityType.Builder.<GOTSnowBearEntity>of(GOTSnowBearEntity::new, MobCategory.CREATURE).sized(1.6F, 1.8F).clientTrackingRange(10).build("snow_bear"));
    public static final RegistryObject<EntityType<GOTBisonEntity>> BISON =
            ENTITIES.register("bison", () -> EntityType.Builder.<GOTBisonEntity>of(GOTBisonEntity::new, MobCategory.CREATURE).sized(1.5F, 1.7F).clientTrackingRange(10).build("bison"));
    public static final RegistryObject<EntityType<GOTWhiteBisonEntity>> WHITE_BISON =
            ENTITIES.register("white_bison", () -> EntityType.Builder.<GOTWhiteBisonEntity>of(GOTWhiteBisonEntity::new, MobCategory.CREATURE).sized(1.5F, 1.7F).clientTrackingRange(10).build("white_bison"));
    public static final RegistryObject<EntityType<GOTDirewolfEntity>> DIREWOLF =
            ENTITIES.register("direwolf", () -> EntityType.Builder.<GOTDirewolfEntity>of(GOTDirewolfEntity::new, MobCategory.CREATURE).sized(0.9F, 1.1F).clientTrackingRange(10).build("direwolf"));
    public static final RegistryObject<EntityType<GOTElephantEntity>> ELEPHANT =
            ENTITIES.register("elephant", () -> EntityType.Builder.<GOTElephantEntity>of(GOTElephantEntity::new, MobCategory.CREATURE).sized(2.4F, 2.8F).clientTrackingRange(12).build("elephant"));
    public static final RegistryObject<EntityType<GOTMammothEntity>> MAMMOTH =
            ENTITIES.register("mammoth", () -> EntityType.Builder.<GOTMammothEntity>of(GOTMammothEntity::new, MobCategory.CREATURE).sized(2.6F, 3.2F).clientTrackingRange(12).build("mammoth"));
    public static final RegistryObject<EntityType<GOTGiraffeEntity>> GIRAFFE =
            ENTITIES.register("giraffe", () -> EntityType.Builder.<GOTGiraffeEntity>of(GOTGiraffeEntity::new, MobCategory.CREATURE).sized(1.4F, 3.4F).clientTrackingRange(12).build("giraffe"));
    public static final RegistryObject<EntityType<GOTLionEntity>> LION =
            ENTITIES.register("lion", () -> EntityType.Builder.<GOTLionEntity>of(GOTLionEntity::new, MobCategory.CREATURE).sized(1.2F, 1.2F).clientTrackingRange(10).build("lion"));
    public static final RegistryObject<EntityType<GOTLionessEntity>> LIONESS =
            ENTITIES.register("lioness", () -> EntityType.Builder.<GOTLionessEntity>of(GOTLionessEntity::new, MobCategory.CREATURE).sized(1.15F, 1.15F).clientTrackingRange(10).build("lioness"));
    public static final RegistryObject<EntityType<GOTOryxEntity>> ORYX =
            ENTITIES.register("oryx", () -> EntityType.Builder.<GOTOryxEntity>of(GOTOryxEntity::new, MobCategory.CREATURE).sized(1.0F, 1.45F).clientTrackingRange(10).build("oryx"));
    public static final RegistryObject<EntityType<GOTWhiteOryxEntity>> WHITE_ORYX =
            ENTITIES.register("white_oryx", () -> EntityType.Builder.<GOTWhiteOryxEntity>of(GOTWhiteOryxEntity::new, MobCategory.CREATURE).sized(1.0F, 1.45F).clientTrackingRange(10).build("white_oryx"));
    public static final RegistryObject<EntityType<GOTDikDikEntity>> DIKDIK =
            ENTITIES.register("dik_dik", () -> EntityType.Builder.<GOTDikDikEntity>of(GOTDikDikEntity::new, MobCategory.CREATURE).sized(0.55F, 0.75F).clientTrackingRange(8).build("dik_dik"));
    public static final RegistryObject<EntityType<GOTWalrusEntity>> WALRUS =
            ENTITIES.register("walrus", () -> EntityType.Builder.<GOTWalrusEntity>of(GOTWalrusEntity::new, MobCategory.CREATURE).sized(1.8F, 1.25F).clientTrackingRange(10).build("walrus"));
    public static final RegistryObject<EntityType<GOTBeaverEntity>> BEAVER =
            ENTITIES.register("beaver", () -> EntityType.Builder.<GOTBeaverEntity>of(GOTBeaverEntity::new, MobCategory.CREATURE).sized(0.85F, 0.65F).clientTrackingRange(8).build("beaver"));
    public static final RegistryObject<EntityType<GOTShadowcatEntity>> SHADOWCAT =
            ENTITIES.register("shadowcat", () -> EntityType.Builder.<GOTShadowcatEntity>of(GOTShadowcatEntity::new, MobCategory.CREATURE).sized(1.25F, 1.2F).clientTrackingRange(10).build("shadowcat"));

    public static final RegistryObject<EntityType<GOTBirdEntity>> BIRD = ENTITIES.register("bird", () -> EntityType.Builder.<GOTBirdEntity>of(GOTBirdEntity::new, MobCategory.CREATURE).sized(.5F,.5F).clientTrackingRange(8).build("bird"));
    public static final RegistryObject<EntityType<GOTSeagullEntity>> SEAGULL = ENTITIES.register("seagull", () -> EntityType.Builder.<GOTSeagullEntity>of(GOTSeagullEntity::new, MobCategory.CREATURE).sized(.6F,.6F).clientTrackingRange(8).build("seagull"));
    public static final RegistryObject<EntityType<GOTGorcrowEntity>> GORCROW = ENTITIES.register("gorcrow", () -> EntityType.Builder.<GOTGorcrowEntity>of(GOTGorcrowEntity::new, MobCategory.CREATURE).sized(.7F,.7F).clientTrackingRange(8).build("gorcrow"));
    public static final RegistryObject<EntityType<GOTButterflyEntity>> BUTTERFLY = ENTITIES.register("butterfly", () -> EntityType.Builder.<GOTButterflyEntity>of(GOTButterflyEntity::new, MobCategory.AMBIENT).sized(.5F,.5F).clientTrackingRange(6).build("butterfly"));
    public static final RegistryObject<EntityType<GOTFlamingoEntity>> FLAMINGO = ENTITIES.register("flamingo", () -> EntityType.Builder.<GOTFlamingoEntity>of(GOTFlamingoEntity::new, MobCategory.CREATURE).sized(.6F,1.8F).clientTrackingRange(8).build("flamingo"));
    public static final RegistryObject<EntityType<GOTSwanEntity>> SWAN = ENTITIES.register("swan", () -> EntityType.Builder.<GOTSwanEntity>of(GOTSwanEntity::new, MobCategory.CREATURE).sized(.7F,.8F).clientTrackingRange(8).build("swan"));
    public static final RegistryObject<EntityType<GOTMidgesEntity>> MIDGES = ENTITIES.register("midges", () -> EntityType.Builder.<GOTMidgesEntity>of(GOTMidgesEntity::new, MobCategory.AMBIENT).sized(.5F,.35F).clientTrackingRange(6).build("midges"));
    public static final RegistryObject<EntityType<GOTDesertScorpionEntity>> DESERT_SCORPION = ENTITIES.register("desert_scorpion", () -> EntityType.Builder.<GOTDesertScorpionEntity>of(GOTDesertScorpionEntity::new, MobCategory.MONSTER).sized(.8F,.45F).clientTrackingRange(8).build("desert_scorpion"));
    public static final RegistryObject<EntityType<GOTJungleScorpionEntity>> JUNGLE_SCORPION = ENTITIES.register("jungle_scorpion", () -> EntityType.Builder.<GOTJungleScorpionEntity>of(GOTJungleScorpionEntity::new, MobCategory.MONSTER).sized(.8F,.45F).clientTrackingRange(8).build("jungle_scorpion"));
    public static final RegistryObject<EntityType<GOTRedScorpionEntity>> RED_SCORPION = ENTITIES.register("red_scorpion", () -> EntityType.Builder.<GOTRedScorpionEntity>of(GOTRedScorpionEntity::new, MobCategory.MONSTER).sized(1.0F,.55F).clientTrackingRange(8).build("red_scorpion"));
    public static final RegistryObject<EntityType<GOTManticoreEntity>> MANTICORE = ENTITIES.register("manticore", () -> EntityType.Builder.<GOTManticoreEntity>of(GOTManticoreEntity::new, MobCategory.MONSTER).sized(.45F,.3F).clientTrackingRange(8).build("manticore"));
    public static final RegistryObject<EntityType<GOTStoneManEntity>> STONE_MAN = ENTITIES.register("stone_man", () -> EntityType.Builder.<GOTStoneManEntity>of(GOTStoneManEntity::new, MobCategory.MONSTER).sized(.6F,1.8F).clientTrackingRange(10).updateInterval(3).build("stone_man"));
    public static final RegistryObject<EntityType<GOTWerewolfEntity>> WEREWOLF = ENTITIES.register("werewolf", () -> EntityType.Builder.<GOTWerewolfEntity>of(GOTWerewolfEntity::new, MobCategory.MONSTER).sized(.8F,1.9F).clientTrackingRange(10).updateInterval(3).build("werewolf"));
    public static final RegistryObject<EntityType<GOTGiantEntity>> GIANT = ENTITIES.register("giant", () -> EntityType.Builder.<GOTGiantEntity>of(GOTGiantEntity::new, MobCategory.CREATURE).sized(2.56F,5.12F).clientTrackingRange(16).updateInterval(3).build("giant"));
    public static final RegistryObject<EntityType<GOTWightGiantEntity>> WIGHT_GIANT = ENTITIES.register("wight_giant", () -> EntityType.Builder.<GOTWightGiantEntity>of(GOTWightGiantEntity::new, MobCategory.MONSTER).sized(2.56F,5.12F).clientTrackingRange(16).updateInterval(3).build("wight_giant"));
    public static final RegistryObject<EntityType<GOTThrownRockEntity>> THROWN_ROCK = ENTITIES.register("thrown_rock", () -> EntityType.Builder.<GOTThrownRockEntity>of(GOTThrownRockEntity::new, MobCategory.MISC).sized(.75F,.75F).clientTrackingRange(12).updateInterval(10).build("thrown_rock"));
    public static final RegistryObject<EntityType<GOTBarrowWraithEntity>> BARROW_WRAITH = ENTITIES.register("barrow_wraith", () -> EntityType.Builder.<GOTBarrowWraithEntity>of(GOTBarrowWraithEntity::new, MobCategory.MONSTER).sized(.8F,2.5F).clientTrackingRange(10).updateInterval(3).build("barrow_wraith"));
    public static final RegistryObject<EntityType<GOTMarshWraithEntity>> MARSH_WRAITH = ENTITIES.register("marsh_wraith", () -> EntityType.Builder.<GOTMarshWraithEntity>of(GOTMarshWraithEntity::new, MobCategory.MONSTER).sized(.6F,1.8F).clientTrackingRange(12).updateInterval(3).build("marsh_wraith"));
    public static final RegistryObject<EntityType<GOTMarshWraithBallEntity>> MARSH_WRAITH_BALL = ENTITIES.register("marsh_wraith_ball", () -> EntityType.Builder.<GOTMarshWraithBallEntity>of(GOTMarshWraithBallEntity::new, MobCategory.MISC).sized(.75F,.75F).clientTrackingRange(12).updateInterval(10).build("marsh_wraith_ball"));

    private GOTEntities() {}

    public static void register(IEventBus modBus) {
        ENTITIES.register(modBus);
        modBus.addListener(GOTEntities::createAttributes);
        modBus.addListener(GOTEntities::commonSetup);
    }

    private static void createAttributes(EntityAttributeCreationEvent event) {
        event.put(GOT_HORSE.get(), GOTMountEntity.createGOTMountAttributes().build());
        event.put(ZEBRA.get(), GOTMountEntity.createGOTMountAttributes().build());
        event.put(RHINO.get(), GOTMountEntity.createGOTMountAttributes().build());
        event.put(WOOLY_RHINO.get(), GOTMountEntity.createGOTMountAttributes().build());
        event.put(CAMEL.get(), GOTMountEntity.createGOTMountAttributes().build());
        event.put(BOAR.get(), GOTMountEntity.createGOTMountAttributes().build());
        event.put(DEER.get(), GOTDeerEntity.createAttributes().build());
        event.put(BEAR.get(), GOTBearEntity.createAttributes().build());
        event.put(SNOW_BEAR.get(), GOTSnowBearEntity.createAttributes().build());
        event.put(BISON.get(), GOTBisonEntity.createAttributes().build());
        event.put(WHITE_BISON.get(), GOTBisonEntity.createAttributes().build());
        event.put(DIREWOLF.get(), GOTDirewolfEntity.createAttributes().build());
        event.put(ELEPHANT.get(), GOTElephantEntity.createAttributes().build());
        event.put(MAMMOTH.get(), GOTMammothEntity.createAttributes().build());
        event.put(GIRAFFE.get(), GOTGiraffeEntity.createAttributes().build());
        event.put(LION.get(), GOTLionEntity.createAttributes().build());
        event.put(LIONESS.get(), GOTLionEntity.createAttributes().build());
        event.put(ORYX.get(), GOTOryxEntity.createAttributes().build());
        event.put(WHITE_ORYX.get(), GOTWhiteOryxEntity.createWhiteAttributes().build());
        event.put(DIKDIK.get(), GOTDikDikEntity.createAttributes().build());
        event.put(WALRUS.get(), GOTWalrusEntity.createAttributes().build());
        event.put(BEAVER.get(), GOTBeaverEntity.createAttributes().build());
        event.put(SHADOWCAT.get(), GOTShadowcatEntity.createAttributes().build());
        event.put(BIRD.get(), Parrot.createAttributes().build());
        event.put(SEAGULL.get(), Parrot.createAttributes().build());
        event.put(GORCROW.get(), Parrot.createAttributes().build());
        event.put(BUTTERFLY.get(), GOTButterflyEntity.createAttributes().build());
        event.put(FLAMINGO.get(), GOTFlamingoEntity.createAttributes().build());
        event.put(SWAN.get(), GOTSwanEntity.createAttributes().build());
        event.put(MIDGES.get(), GOTMidgesEntity.createAttributes().build());
        event.put(DESERT_SCORPION.get(), GOTScorpionEntity.createAttributes().build());
        event.put(JUNGLE_SCORPION.get(), GOTScorpionEntity.createAttributes().build());
        event.put(RED_SCORPION.get(), GOTRedScorpionEntity.createRedAttributes().build());
        event.put(MANTICORE.get(), GOTManticoreEntity.createAttributes().build());
        event.put(STONE_MAN.get(), GOTStoneManEntity.createAttributes().build());
        event.put(WEREWOLF.get(), GOTWerewolfEntity.createAttributes().build());
        event.put(GIANT.get(), GOTGiantBaseEntity.giantAttributes().build());
        event.put(WIGHT_GIANT.get(), GOTWightGiantEntity.createWightAttributes().build());
        event.put(BARROW_WRAITH.get(), GOTBarrowWraithEntity.createAttributes().build());
        event.put(MARSH_WRAITH.get(), GOTMarshWraithEntity.createAttributes().build());

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
        event.put(JAQEN_HGHAR.get(), GOTBraavosNpcEntity.createAttributes().build());
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
        event.put(CROCODILE.get(), GOTCrocodileEntity.createAttributes().build());
        event.put(ULTHOS_SPIDER.get(), GOTUlthosSpiderEntity.createAttributes().build());
        event.put(BLIZZARD.get(), GOTBlizzardEntity.createAttributes().build());
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            SpawnPlacements.register(DEER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.animal.Animal::checkAnimalSpawnRules);
            SpawnPlacements.register(BEAR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.animal.Animal::checkAnimalSpawnRules);
            SpawnPlacements.register(SNOW_BEAR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.animal.Animal::checkAnimalSpawnRules);
            SpawnPlacements.register(BISON.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.animal.Animal::checkAnimalSpawnRules);
            SpawnPlacements.register(WHITE_BISON.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.animal.Animal::checkAnimalSpawnRules);
            SpawnPlacements.register(DIREWOLF.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.animal.Animal::checkAnimalSpawnRules);
            SpawnPlacements.register(ELEPHANT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.animal.Animal::checkAnimalSpawnRules);
            SpawnPlacements.register(MAMMOTH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.animal.Animal::checkAnimalSpawnRules);
            SpawnPlacements.register(GIRAFFE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.animal.Animal::checkAnimalSpawnRules);
            SpawnPlacements.register(LION.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.animal.Animal::checkAnimalSpawnRules);
            SpawnPlacements.register(LIONESS.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.animal.Animal::checkAnimalSpawnRules);
            SpawnPlacements.register(ORYX.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.animal.Animal::checkAnimalSpawnRules);
            SpawnPlacements.register(WHITE_ORYX.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.animal.Animal::checkAnimalSpawnRules);
            SpawnPlacements.register(DIKDIK.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.animal.Animal::checkAnimalSpawnRules);
            SpawnPlacements.register(WALRUS.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.animal.Animal::checkAnimalSpawnRules);
            SpawnPlacements.register(BEAVER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.animal.Animal::checkAnimalSpawnRules);
            SpawnPlacements.register(SHADOWCAT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.animal.Animal::checkAnimalSpawnRules);
            SpawnPlacements.register(FLAMINGO.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.animal.Animal::checkAnimalSpawnRules);
            SpawnPlacements.register(SWAN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.animal.Animal::checkAnimalSpawnRules);
            SpawnPlacements.register(DESERT_SCORPION.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.monster.Monster::checkMonsterSpawnRules);
            SpawnPlacements.register(JUNGLE_SCORPION.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.monster.Monster::checkMonsterSpawnRules);
            SpawnPlacements.register(RED_SCORPION.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.monster.Monster::checkMonsterSpawnRules);
            SpawnPlacements.register(MANTICORE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.monster.Monster::checkMonsterSpawnRules);
            SpawnPlacements.register(STONE_MAN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.monster.Monster::checkMonsterSpawnRules);
            SpawnPlacements.register(WEREWOLF.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.monster.Monster::checkMonsterSpawnRules);
            SpawnPlacements.register(GIANT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (type, level, spawnType, pos, random) -> level.getBlockState(pos.below()).isValidSpawn(level,pos.below(),type) && level.getBlockState(pos).getCollisionShape(level,pos).isEmpty());
            SpawnPlacements.register(WIGHT_GIANT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.monster.Monster::checkMonsterSpawnRules);
            SpawnPlacements.register(BARROW_WRAITH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.monster.Monster::checkMonsterSpawnRules);
            SpawnPlacements.register(MARSH_WRAITH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.monster.Monster::checkMonsterSpawnRules);
        });
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
        event.enqueueWork(() -> SpawnPlacements.register(CROCODILE.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                GOTCrocodileEntity::canSpawn));
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
