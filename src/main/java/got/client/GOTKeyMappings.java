package got.client;

import com.mojang.blaze3d.platform.InputConstants;
import got.GOTMod;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class GOTKeyMappings {
    public static final KeyMapping OPEN_MENU = new KeyMapping("key.got.open_menu", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_L, "key.categories.got");
    private GOTKeyMappings() {}
    @SubscribeEvent public static void register(RegisterKeyMappingsEvent event) { event.register(OPEN_MENU); }
}
