package got;

import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(GOTMod.MOD_ID)
public final class GOTMod {
    public static final String MOD_ID = "got";
    public static final Logger LOGGER = LogUtils.getLogger();

    public GOTMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON,
                got.claim.GOTBannerClaimConfig.SPEC, "got-banner-claims.toml");
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        got.world.terrain.GOTTerrainRegistries.register(modBus);
        GOTSounds.register(modBus);
        GOTBlocks.register(modBus);
        GOTEffects.register(modBus);
        GOTEntities.register(modBus);
        GOTBlockEntities.register(modBus);
        GOTMenus.register(modBus);
        GOTItems.register(modBus);
        GOTEquipment.register(modBus);
        GOTRecipes.register(modBus);
        GOTCreativeTabs.register(modBus);
        got.network.GOTNetwork.register();
        got.calendar.network.GOTCalendarNetwork.register();
        got.pact.GOTPactClaimBridge.bootstrap();

        // Epic Fight is deliberately OPTIONAL. Keep all Epic Fight-linked classes behind
        // this reflective boundary so a normal Conquest of Westeros install never asks
        // the JVM to resolve yesman.epicfight.* when Epic Fight is not installed.
        if (ModList.get().isLoaded("epicfight")) {
            try {
                Class<?> compatBootstrap = Class.forName("got.compat.epicfight.GOTEpicFightCompat");
                compatBootstrap.getMethod("bootstrap", IEventBus.class).invoke(null, modBus);
                LOGGER.info("Epic Fight detected; Conquest of Westeros compatibility enabled");
            } catch (ReflectiveOperationException | LinkageError exception) {
                // Epic Fight is optional. A compatibility bridge failure must never
                // prevent base Conquest of Westeros from loading.
                LOGGER.error("Epic Fight is installed, but Conquest of Westeros could not initialize its optional compatibility layer; continuing without Epic Fight compatibility", exception);
            }
        } else {
            LOGGER.info("Epic Fight not detected; Conquest of Westeros compatibility layer remains disabled");
        }

        // Better Combat compatibility is deliberately data-only. Its weapon_attributes
        // resources are ignored by vanilla Minecraft and are consumed only when Better
        // Combat itself is installed, so no Better Combat API class is linked here.
        boolean betterCombatLoaded = ModList.get().isLoaded("bettercombat");
        if (betterCombatLoaded) {
            LOGGER.info("Better Combat detected; Conquest of Westeros weapon compatibility enabled");
        } else {
            LOGGER.info("Better Combat not detected; Conquest of Westeros Better Combat data remains inert");
        }

        if (betterCombatLoaded && ModList.get().isLoaded("epicfight")) {
            LOGGER.warn("Epic Fight and Better Combat are both installed. Conquest exposes compatibility for both, but the two combat overhauls can compete for player animation/combat control; use an EF/Better Combat switching bridge or select one combat system if conflicts appear.");
        }

        LOGGER.info("Game of Thrones 1.20.1 worldgen, regional NPCs, factions, quests, calendar, Pacts, Conquest foundation, and hiring systems initialized");
    }
}
