package got.npc;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

/** Keeps generated khalasar members coordinated around their chieftain. */
final class GOTDothrakiKhalasarGoal extends Goal {
    private final GOTDothrakiNpcEntity mob;
    private GOTDothrakiNpcEntity leader;

    GOTDothrakiKhalasarGoal(GOTDothrakiNpcEntity mob) {
        this.mob = mob;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override public boolean canUse() {
        if (mob.getKhalasarLeaderUuid() == null || mob.getTarget() != null || mob.isDothrakiSkirmishing()) return false;
        leader = mob.getKhalasarLeader();
        return leader != null && leader.isAlive() && mob.distanceToSqr(leader) > 49.0D;
    }

    @Override public boolean canContinueToUse() {
        return leader != null && leader.isAlive() && mob.getTarget() == null
                && mob.distanceToSqr(leader) > 16.0D;
    }

    @Override public void tick() {
        if (leader == null) return;
        LivingEntity leaderTarget = leader.getTarget();
        if (leaderTarget != null && leaderTarget.isAlive() && mob.getRole().activeCombatant()) {
            mob.setTarget(leaderTarget);
            return;
        }
        if (!mob.isPassenger()) mob.getNavigation().moveTo(leader, 1.15D);
        mob.getLookControl().setLookAt(leader, 20.0F, 20.0F);
    }

    @Override public void stop() {
        if (!mob.isPassenger()) mob.getNavigation().stop();
        leader = null;
    }
}
