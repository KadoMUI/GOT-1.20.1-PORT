package got.npc.hiring.command;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.PathfinderMob;

/**
 * State that existed separately from guard mode in 1.7.10.
 *
 * HALT/READY changes "canMove"; it is NOT the same as guard mode.
 * The command-sword target flag lets an empty sword command cancel only a
 * target that was assigned by the command sword.
 */
public final class GOTHiredCommandState {
    private static final String ROOT = "GOTHiredCommand";
    private static final String HALTED = "Halted";
    private static final String SWORD_TARGET = "TargetFromCommandSword";

    private GOTHiredCommandState() {}

    private static CompoundTag data(PathfinderMob mob) {
        CompoundTag persistent = mob.getPersistentData();
        if (!persistent.contains(ROOT)) persistent.put(ROOT, new CompoundTag());
        return persistent.getCompound(ROOT);
    }

    private static void save(PathfinderMob mob, CompoundTag tag) {
        mob.getPersistentData().put(ROOT, tag);
    }

    public static boolean halted(PathfinderMob mob) {
        return data(mob).getBoolean(HALTED);
    }

    public static void halt(PathfinderMob mob) {
        CompoundTag tag = data(mob);
        tag.putBoolean(HALTED, true);
        save(mob, tag);
        mob.getNavigation().stop();
        mob.setTarget(null);
    }

    public static void ready(PathfinderMob mob) {
        CompoundTag tag = data(mob);
        tag.putBoolean(HALTED, false);
        save(mob, tag);
    }

    public static boolean targetFromCommandSword(PathfinderMob mob) {
        return data(mob).getBoolean(SWORD_TARGET);
    }

    public static void commandSwordAttack(PathfinderMob mob, net.minecraft.world.entity.LivingEntity target) {
        mob.getNavigation().stop();
        mob.setTarget(target);
        CompoundTag tag = data(mob);
        tag.putBoolean(SWORD_TARGET, true);
        save(mob, tag);
    }

    public static void commandSwordCancel(PathfinderMob mob) {
        if (!targetFromCommandSword(mob)) return;
        mob.getNavigation().stop();
        mob.setTarget(null);
        CompoundTag tag = data(mob);
        tag.putBoolean(SWORD_TARGET, false);
        save(mob, tag);
    }

    public static void clearSwordFlag(PathfinderMob mob) {
        CompoundTag tag = data(mob);
        tag.putBoolean(SWORD_TARGET, false);
        save(mob, tag);
    }
}
