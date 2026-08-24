package got;

import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModLoadingContext;
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
        LOGGER.info("Game of Thrones 1.20.1 worldgen, regional NPCs, factions, quests, calendar, Pacts, and hiring systems initialized");
    }
}
