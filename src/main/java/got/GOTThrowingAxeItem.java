package got;

import got.achievement.GOTAchievementHooks;
import got.npc.GOTThrownAxeEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public final class GOTThrowingAxeItem extends Item {
    private final Tier tier;
    public GOTThrowingAxeItem(Tier tier, Properties props) { super(props.stacksTo(1).durability(tier.getUses())); this.tier = tier; }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            ItemStack thrown = stack.copy(); thrown.setCount(1);
            float damage = tier.getAttackDamageBonus() + 4.0F;
            GOTThrownAxeEntity axe = new GOTThrownAxeEntity(level, player, thrown, damage);
            axe.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.0F, 1.0F);
            level.addFreshEntity(axe);
            level.playSound(null, player.blockPosition(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
            if (player instanceof ServerPlayer sp) GOTAchievementHooks.award(sp, "USE_THROWING_AXE");
        }
        if (!player.getAbilities().instabuild) stack.shrink(1);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
