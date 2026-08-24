package got.npc.hiring;

import net.minecraft.world.entity.Entity;

import java.lang.reflect.Method;

/**
 * Adapter for the current regional NPC role-enum design.
 *
 * Current 0.6.0 regional NPC entities expose getRole(), and each role enum
 * exposes id(). Reflection keeps this one bridge generic across all regional
 * entity classes without adding 30+ instanceof branches.
 *
 * This can later be replaced by a tiny common GOTNpcRole interface.
 */
public final class GOTHiringRoleResolver {
    private GOTHiringRoleResolver() {}

    public static String roleId(Entity entity) {
        try {
            Method getRole = entity.getClass().getMethod("getRole");
            Object role = getRole.invoke(entity);
            if (role == null) return null;

            Method id = role.getClass().getMethod("id");
            Object result = id.invoke(role);
            return result instanceof String s ? s : null;
        } catch (ReflectiveOperationException ignored) {
            return null;
        }
    }
}
