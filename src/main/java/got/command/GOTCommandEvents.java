package got.command;

import got.GOTMod;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTCommandEvents {
    private GOTCommandEvents() {}

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        GOTLegacyCommands.register(event.getDispatcher());
    }
}
