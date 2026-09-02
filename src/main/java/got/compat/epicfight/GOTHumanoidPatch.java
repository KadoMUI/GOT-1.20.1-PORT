package got.compat.epicfight;

import got.GOTMod;
import net.minecraft.world.entity.PathfinderMob;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.world.capabilities.entitypatch.Factions;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.entity.ai.goal.AnimatedAttackGoal;
import yesman.epicfight.world.entity.ai.goal.TargetChasingGoal;

/**
 * Shared patch for GOT humanoids.
 *
 * super.initAI() is intentionally explicit: Epic Fight installs AnimatedAttackGoal
 * and TargetChasingGoal there. Omitting it produces locomotion animations without
 * Epic Fight combat animations.
 */
public final class GOTHumanoidPatch extends HumanoidMobPatch<PathfinderMob> {
    public GOTHumanoidPatch() {
        super(Factions.NEUTRAL);
    }

    @Override
    protected void initAI() {
        super.initAI();
        verifyEpicFightCombatGoal();
    }

    /**
     * Stage2 regression guard: locomotion alone is not enough.  The final tested
     * addon explicitly verified that Epic Fight replaced vanilla combat with its
     * animated attack/chasing goals after HumanoidMobPatch.initAI().
     */
    private void verifyEpicFightCombatGoal() {
        boolean animatedAttack = original.goalSelector.getAvailableGoals().stream()
                .anyMatch(wrapped -> wrapped.getGoal() instanceof AnimatedAttackGoal);
        boolean targetChasing = original.goalSelector.getAvailableGoals().stream()
                .anyMatch(wrapped -> wrapped.getGoal() instanceof TargetChasingGoal);

        if (!animatedAttack || !targetChasing) {
            GOTMod.LOGGER.warn(
                    "Epic Fight NPC combat goal verification failed for {} (AnimatedAttackGoal={}, TargetChasingGoal={})",
                    original.getType(), animatedAttack, targetChasing);
        }
    }

    @Override
    public void initAnimator(Animator animator) {
        super.initAnimator(animator);
        commonAggresiveMobAnimatorInit(animator);
    }

    @Override
    public void updateMotion(boolean considerInaction) {
        commonAggressiveMobUpdateMotion(considerInaction);
    }
}
