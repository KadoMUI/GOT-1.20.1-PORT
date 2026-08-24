package got;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class GOTDrunkennessClientEvents {
    private static final Random RANDOM = new Random();
    private static float targetYawOffset;
    private static float targetPitchOffset;
    private static float currentYawVelocity;
    private static float currentPitchVelocity;
    private static int retargetTicks;

    private GOTDrunkennessClientEvents() {}

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.isPaused() || !player.hasEffect(GOTEffects.DRUNKENNESS.get())) {
            reset();
            return;
        }

        int amplifier = player.getEffect(GOTEffects.DRUNKENNESS.get()).getAmplifier();
        float intensity = 1.0F + amplifier * 0.35F;

        if (--retargetTicks <= 0) {
            targetYawOffset = (RANDOM.nextFloat() * 2.0F - 1.0F) * 0.34F * intensity;
            targetPitchOffset = (RANDOM.nextFloat() * 2.0F - 1.0F) * 0.18F * intensity;
            retargetTicks = 22 + RANDOM.nextInt(35);
        }

        currentYawVelocity = Mth.lerp(0.035F, currentYawVelocity, targetYawOffset);
        currentPitchVelocity = Mth.lerp(0.035F, currentPitchVelocity, targetPitchOffset);

        player.setYRot(player.getYRot() + currentYawVelocity);
        player.setXRot(Mth.clamp(player.getXRot() + currentPitchVelocity, -89.5F, 89.5F));
        player.yRotO += currentYawVelocity;
        player.xRotO = Mth.clamp(player.xRotO + currentPitchVelocity, -89.5F, 89.5F);
    }

    private static void reset() {
        targetYawOffset = 0.0F;
        targetPitchOffset = 0.0F;
        currentYawVelocity = 0.0F;
        currentPitchVelocity = 0.0F;
        retargetTicks = 0;
    }
}
