package got.client;

import got.GOTMod;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class GOTClientForgeEvents {
    private GOTClientForgeEvents() {}
    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            got.client.faction.GOTClientFactionState.reset();
            got.client.quest.GOTClientQuestState.reset();
        } else got.client.faction.GOTClientFactionState.tick();
        while (GOTKeyMappings.OPEN_MENU.consumeClick()) {
            if (minecraft.screen == null) minecraft.setScreen(got.client.gui.GOTGuiMenu.openMenu());
        }
    }

    @SubscribeEvent
    public static void renderQuestTracker(RenderGuiEvent.Post event) {
        got.client.quest.GOTClientQuestState.renderTracker(Minecraft.getInstance(), event.getGuiGraphics());
    }
}
