package got.npc;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;
import java.util.List;

/**
 * Modern reconstruction of GOTEntityAIDothrakiSkirmish.
 * Adult ordinary Dothraki occasionally fight short, non-factional practice bouts.
 */
final class GOTDothrakiSkirmishGoal extends Goal {
    private final GOTDothrakiNpcEntity mob;
    private GOTDothrakiNpcEntity opponent;

    GOTDothrakiSkirmishGoal(GOTDothrakiNpcEntity mob) {
        this.mob = mob;
        setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (!mob.canDothrakiSkirmish() || mob.isDothrakiSkirmishing()) return false;

        int chance = 20_000;
        AABB area = mob.getBoundingBox().inflate(16.0D, 8.0D, 16.0D);
        List<GOTDothrakiNpcEntity> nearby = mob.level().getEntitiesOfClass(GOTDothrakiNpcEntity.class, area,
                other -> other != mob && other.isDothrakiSkirmishing());
        for (int i = 0; i < nearby.size(); i++) chance = Math.max(40, chance / 10);
        if (mob.getRandom().nextInt(chance) != 0) return false;

        opponent = mob.level().getEntitiesOfClass(GOTDothrakiNpcEntity.class, area, this::validOpponent)
                .stream().min(java.util.Comparator.comparingDouble(mob::distanceToSqr)).orElse(null);
        return opponent != null;
    }

    private boolean validOpponent(GOTDothrakiNpcEntity other) {
        if (other == mob || !other.canDothrakiSkirmish()) return false;
        if (mob.sameKhalasar(other)) return false;
        return !other.isPassenger();
    }

    @Override
    public void start() {
        if (opponent == null) return;
        mob.beginDothrakiSkirmish(opponent);
        opponent.beginDothrakiSkirmish(mob);
    }
}
