package got.speech;

import net.minecraft.world.entity.Entity;

import java.lang.reflect.Method;

/** Common role adapter across the regional NPC role enums. */
public final class GOTSpeechRoleResolver {
    private GOTSpeechRoleResolver() {}

    public static String roleId(Entity entity) {
        try {
            Method getRole = entity.getClass().getMethod("getRole");
            Object role = getRole.invoke(entity);
            if (role == null) return "";
            Method id = role.getClass().getMethod("id");
            Object result = id.invoke(role);
            return result instanceof String s ? s : "";
        } catch (ReflectiveOperationException ignored) {
            return "";
        }
    }
}
