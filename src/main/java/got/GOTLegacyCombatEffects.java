package got;

import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

/** Shared legacy 1.7.10 hit-effect formulas used by poisoned/fire weapons. */
public final class GOTLegacyCombatEffects {
    private GOTLegacyCombatEffects() {}

    public static void applyStandardPoison(LivingEntity target) {
        int d = target.level().getDifficulty().getId();
        int secondsBase = 1 + d * 2;
        int seconds = secondsBase + target.getRandom().nextInt(Math.max(1, secondsBase));
        target.addEffect(new MobEffectInstance(MobEffects.POISON, seconds * 20));
    }

    public static void applyStandardFire(LivingEntity target) {
        int d = target.level().getDifficulty().getId();
        target.setSecondsOnFire(1 + d * 10);
    }
}
