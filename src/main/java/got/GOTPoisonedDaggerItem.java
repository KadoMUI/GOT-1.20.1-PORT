package got;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public final class GOTPoisonedDaggerItem extends SwordItem {
    public GOTPoisonedDaggerItem(Tier tier, Properties props) {
        // Legacy dagger = normal sword damage minus 3.
        super(tier, 0, -1.7F, props);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.hurtEnemy(stack, target, attacker);
        if (!target.level().isClientSide) GOTLegacyCombatEffects.applyStandardPoison(target);
        return result;
    }
}
