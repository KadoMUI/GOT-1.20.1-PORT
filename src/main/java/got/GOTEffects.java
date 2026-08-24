package got;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class GOTEffects {
    private static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, GOTMod.MOD_ID);

    public static final RegistryObject<MobEffect> DRUNKENNESS = EFFECTS.register(
            "drunkenness",
            () -> new GOTSimpleMobEffect(MobEffectCategory.HARMFUL, 0x8A5A2B)
    );

    private GOTEffects() {}

    public static void register(IEventBus bus) {
        EFFECTS.register(bus);
    }
}
