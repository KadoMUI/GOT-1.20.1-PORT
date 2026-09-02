package got.conquest.pact;

import got.npc.GOTJaqenHgharEntity;
import got.quest.GOTQuestGiver;
import net.minecraft.world.entity.Entity;

import java.lang.reflect.Method;

/** Stable role/legendary detection bridge shared by Conquest recruitment and later execution logic. */
public final class GOTLegendaryNpcService {
    private GOTLegendaryNpcService() {}

    public static boolean isLegendary(Entity entity) {
        if (entity instanceof GOTJaqenHgharEntity) return true;
        if (!(entity instanceof GOTQuestGiver)) return false;
        try {
            Method getRole = entity.getClass().getMethod("getRole");
            Object role = getRole.invoke(entity);
            if (role == null) return false;
            Method legendary = role.getClass().getMethod("legendary");
            return Boolean.TRUE.equals(legendary.invoke(role));
        } catch (ReflectiveOperationException ignored) {
            return false;
        }
    }

    public static String roleId(Entity entity) {
        return entity instanceof GOTQuestGiver giver
                ? GOTCanonicalPactMember.normalizeRole(giver.getQuestRoleId())
                : "";
    }
}
