package got;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class GOTWasteBlock extends Block {
    public GOTWasteBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        entity.makeStuckInBlock(state, new net.minecraft.world.phys.Vec3(0.7D, 0.35D, 0.7D));
        if (!level.isClientSide && entity instanceof LivingEntity living && level.getGameTime() % 20 == 0) {
            living.addEffect(new MobEffectInstance(MobEffects.POISON, 40, 0));
        }
    }
}
