package got.conquest.pact;

import got.conquest.GOTConquestSavedData;
import got.faction.GOTFaction;
import got.faction.GOTFactionPlayerData;
import got.pact.GOTPact;
import got.pact.GOTPactSavedData;
import got.quest.GOTQuestGiver;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.Optional;
import java.util.UUID;

/** Player-facing political recruitment rules for Legendary NPCs. */
public final class GOTLegendaryRecruitmentService {
    /** Existing faction rank threshold used as the first-pass recruitment gate. */
    public static final float BASE_REQUIRED_ALIGNMENT = 500.0F;

    private GOTLegendaryRecruitmentService() {}

    public static RecruitResult recruit(ServerPlayer player, Entity target) {
        if (!(target instanceof GOTQuestGiver giver) || !GOTLegendaryNpcService.isLegendary(target)) {
            return RecruitResult.fail("That NPC is not a recruitable Legendary character.");
        }
        if (!target.isAlive()) return RecruitResult.fail("That character cannot be recruited right now.");
        if (target instanceof net.minecraft.world.entity.Mob mob && mob.getTarget() != null) {
            return RecruitResult.fail("You cannot negotiate recruitment while that character is in combat.");
        }

        String roleId = GOTCanonicalPactMember.normalizeRole(giver.getQuestRoleId());
        if (roleId.isBlank()) return RecruitResult.fail("That Legendary character has no stable role id.");

        GOTPactSavedData playerPacts = GOTPactSavedData.get(player.server);
        GOTPact pact = playerPacts.pactFor(player.getUUID()).orElse(null);
        if (pact == null) return RecruitResult.fail("You must belong to a player-run Pact before recruiting Legendary characters.");
        if (!pact.canManage(player.getUUID())) {
            return RecruitResult.fail("Only the Pact owner or an admin can recruit Legendary characters.");
        }

        GOTFaction faction = giver.getQuestFaction();
        float requiredAlignment = requiredAlignment(faction);
        float alignment = GOTFactionPlayerData.get(player).alignment(faction);
        if (faction.isPlayable() && alignment < requiredAlignment) {
            return RecruitResult.fail(target.getDisplayName().getString() + " requires "
                    + Math.round(requiredAlignment) + " alignment with " + faction.displayName().getString()
                    + " before joining your Pact. You currently have " + Math.round(alignment) + ".");
        }

        GOTConquestSavedData conquest = GOTConquestSavedData.get(player.server);
        Optional<GOTLegendaryPactMembership> existing = conquest.legendaryMembership(roleId);
        if (existing.isPresent() && existing.get().currentPactId().isPresent()) {
            UUID current = existing.get().currentPactId().get();
            if (current.equals(pact.id())) {
                return RecruitResult.fail(target.getDisplayName().getString() + " is already a member of " + pact.name() + ".");
            }
            boolean canonical = conquest.canonicalPact(current).isPresent();
            if (!canonical && playerPacts.pact(current).isPresent()) {
                String otherName = playerPacts.pact(current).map(GOTPact::name).orElse("another Pact");
                return RecruitResult.fail(target.getDisplayName().getString() + " is already sworn to " + otherName + ".");
            }
        }

        GOTLegendaryPactMembership membership = conquest.recruitLegendary(
                roleId,
                target.getDisplayName().getString(),
                faction,
                pact.id(),
                player.getUUID(),
                player.server.overworld().getGameTime());

        syncEntityTag(target, membership);
        Component message = Component.literal(target.getDisplayName().getString() + " has joined " + pact.name() + ".");
        for (UUID memberId : pact.members()) {
            ServerPlayer online = player.server.getPlayerList().getPlayer(memberId);
            if (online != null) online.sendSystemMessage(message);
        }
        return RecruitResult.ok(roleId, pact.id(), pact.name(), requiredAlignment);
    }

    public static float requiredAlignment(GOTFaction faction) {
        if (faction == null || !faction.isPlayable()) return 0.0F;
        return Math.max(BASE_REQUIRED_ALIGNMENT, faction.pledgeAlignment());
    }

    public static void syncEntityTag(Entity entity, GOTLegendaryPactMembership membership) {
        if (membership.currentPactId().isPresent()) {
            entity.getPersistentData().putUUID("GOTConquestPact", membership.currentPactId().get());
        } else {
            entity.getPersistentData().remove("GOTConquestPact");
        }
        entity.getPersistentData().putString("GOTConquestLegendaryRole", membership.roleId());
    }

    public record RecruitResult(boolean success, String error, String roleId,
                                UUID pactId, String pactName, float requiredAlignment) {
        public static RecruitResult ok(String roleId, UUID pactId, String pactName, float requiredAlignment) {
            return new RecruitResult(true, "", roleId, pactId, pactName, requiredAlignment);
        }
        public static RecruitResult fail(String error) {
            return new RecruitResult(false, error, "", null, "", 0.0F);
        }
    }
}
