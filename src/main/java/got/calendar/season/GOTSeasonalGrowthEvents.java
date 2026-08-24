package got.calendar.season;

import got.calendar.GOTCalendarApi;
import got.world.GOTDimensions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** First gameplay hook from the calendar into crop growth. Rules are opt-in. */
@Mod.EventBusSubscriber(modid = "got", bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTSeasonalGrowthEvents {
    private GOTSeasonalGrowthEvents() {}

    @SubscribeEvent
    public static void onCropGrow(BlockEvent.CropGrowEvent.Pre event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (!level.dimension().equals(GOTDimensions.PLANETOS)) return;

        BlockState state = event.getState();
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        GOTSeasonalGrowthRegistry.Rule rule = GOTSeasonalGrowthRegistry.get(blockId);
        if (rule == null) return; // 1.0 parity: unregistered crops are unaffected.

        GOTCalendarApi.Snapshot snapshot = GOTCalendarApi.snapshot(level.getServer());
        float multiplier = rule.multiplier(snapshot.season());

        if (multiplier <= 0.0F) {
            event.setResult(Event.Result.DENY);
            return;
        }

        // Multipliers below 1.0 slow growth probabilistically without recursive randomTick calls.
        // Values >= 1.0 leave vanilla growth untouched in Pass 3; acceleration belongs in the
        // later climate/agriculture integration where growth can be balanced coherently.
        if (multiplier < 1.0F && level.random.nextFloat() > multiplier) {
            event.setResult(Event.Result.DENY);
        }
    }
}
