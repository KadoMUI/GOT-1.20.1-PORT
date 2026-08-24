package got;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.ArrayList;
import java.util.List;

/** Immutable gameplay definition for one drink type. */
public record GOTDrinkDefinition(
        String id,
        float alcoholicity,
        boolean brewable,
        int nutrition,
        float saturation,
        boolean curesEffects,
        int damage,
        List<EffectEntry> effects
) {
    public GOTDrinkDefinition {
        effects = List.copyOf(effects);
    }

    public List<MobEffectInstance> createEffects(float strength) {
        List<MobEffectInstance> result = new ArrayList<>();
        for (EffectEntry entry : effects) {
            int duration = Math.max(1, Math.round(entry.durationTicks() * Math.max(0.25F, strength)));
            result.add(new MobEffectInstance(entry.effect(), duration, entry.amplifier()));
        }
        return result;
    }

    public record EffectEntry(MobEffect effect, int durationTicks, int amplifier) {}
}
