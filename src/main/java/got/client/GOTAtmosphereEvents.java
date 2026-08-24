package got.client;

import got.GOTMod;
import got.world.GOTDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Restores the legacy dense-fog overlay in Valyria and Yeen. */
@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class GOTAtmosphereEvents {
    private static final int SAMPLE_RADIUS = 2;
    private static final int SAMPLE_SPACING = 8;

    private GOTAtmosphereEvents() {}

    @SubscribeEvent
    public static void renderFog(ViewportEvent.RenderFog event) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null || !level.dimension().equals(GOTDimensions.PLANETOS)) return;
        if (event.getCamera().getFluidInCamera() != FogType.NONE) return;

        float fogRatio = fogRatio(level, event.getCamera().getBlockPosition());
        if (fogRatio <= 0.0F) return;

        float normalNear = event.getNearPlaneDistance();
        float normalFar = event.getFarPlaneDistance();
        float thickNear = normalFar * 0.05F;
        float thickFar = Math.min(normalFar, 192.0F) * 0.5F;
        event.setNearPlaneDistance(Mth.lerp(fogRatio, normalNear, thickNear));
        event.setFarPlaneDistance(Mth.lerp(fogRatio, normalFar, thickFar));
        event.setCanceled(true);
    }

    private static float fogRatio(ClientLevel level, BlockPos center) {
        int foggy = 0;
        int samples = 0;
        for (int dz = -SAMPLE_RADIUS; dz <= SAMPLE_RADIUS; dz++) {
            for (int dx = -SAMPLE_RADIUS; dx <= SAMPLE_RADIUS; dx++) {
                BlockPos sample = center.offset(dx * SAMPLE_SPACING, 0, dz * SAMPLE_SPACING);
                String biome = level.getBiome(sample).unwrapKey()
                        .map(key -> key.location().getPath()).orElse("");
                if (biome.equals("valyria") || biome.equals("valyria_volcano") || biome.equals("yeen")) foggy++;
                samples++;
            }
        }
        return samples == 0 ? 0.0F : (float)foggy / samples;
    }
}
