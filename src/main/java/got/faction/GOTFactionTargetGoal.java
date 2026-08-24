package got.faction;

import got.npc.GOTFactionNpc;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

/** Legacy-style nearest hostile faction/player target selection. */
public final class GOTFactionTargetGoal extends NearestAttackableTargetGoal<LivingEntity> {
    public GOTFactionTargetGoal(PathfinderMob mob) {
        super(mob, LivingEntity.class, 10, true, false,
                target -> target != null && GOTFactionRules.shouldNpcTarget(mob, target));
    }

    @Override
    public boolean canUse() {
        return mob instanceof GOTFactionNpc npc && npc.isActiveCombatant() && super.canUse();
    }
}
