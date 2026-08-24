package got.faction;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

public final class GOTFactionHurtByTargetGoal extends HurtByTargetGoal {
    private final PathfinderMob factionMob;

    public GOTFactionHurtByTargetGoal(PathfinderMob mob) {
        super(mob);
        this.factionMob = mob;
    }

    @Override
    public boolean canUse() {
        LivingEntity attacker = factionMob.getLastHurtByMob();
        return GOTFactionRules.canNpcRetaliate(factionMob, attacker) && super.canUse();
    }
}
