package got;

import got.achievement.GOTAchievementHooks;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

/** Legacy spear/harpoon: one point below sword melee damage and charge-to-throw. */
public final class GOTLegacySpearItem extends SwordItem {
    private final Tier tier;

    public GOTLegacySpearItem(Tier tier, Properties props) {
        super(tier, 2, -2.8F, props);
        this.tier = tier;
    }

    @Override public UseAnim getUseAnimation(ItemStack stack) { return UseAnim.BOW; }
    @Override public int getUseDuration(ItemStack stack) { return 72000; }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity user, int timeLeft) {
        if (!(user instanceof Player player)) return;
        int charge = getUseDuration(stack) - timeLeft;
        float f = charge / 20.0F;
        if (f < 0.1F) return;
        f = Math.min(1.0F, (f * f + f * 2.0F) / 3.0F);

        if (!level.isClientSide) {
            ItemStack thrown = stack.copy();
            thrown.setCount(1);
            float legacyWeaponDamage = tier.getAttackDamageBonus() + 3.0F;
            float damage = legacyWeaponDamage * 0.7F * Math.max(0.75F, f);
            GOTLegacySpearEntity spear = new GOTLegacySpearEntity(level, player, thrown, damage);
            spear.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 2.0F, 1.0F);
            level.addFreshEntity(spear);
            level.playSound(null, player.blockPosition(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 0.75F + f * 0.35F);
            if (player instanceof ServerPlayer sp) GOTAchievementHooks.award(sp, "USE_SPEAR");
        }
        if (!player.getAbilities().instabuild) stack.shrink(1);
    }
}
