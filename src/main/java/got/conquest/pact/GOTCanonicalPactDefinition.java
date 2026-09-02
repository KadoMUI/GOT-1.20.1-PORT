package got.conquest.pact;

import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/** Immutable seed definition for one canonical NPC-controlled political Pact. */
public record GOTCanonicalPactDefinition(
        ResourceLocation key,
        UUID id,
        String name,
        String leaderRoleId,
        List<GOTCanonicalPactMember> members) {

    public Optional<GOTCanonicalPactMember> member(String roleId) {
        String normalized = GOTCanonicalPactMember.normalizeRole(roleId);
        return members.stream().filter(member -> member.roleId().equals(normalized)).findFirst();
    }

    public GOTCanonicalPactDefinition {
        Objects.requireNonNull(key);
        Objects.requireNonNull(id);
        name = name == null || name.isBlank() ? key.getPath() : name.trim();
        String normalizedLeaderRoleId = GOTCanonicalPactMember.normalizeRole(leaderRoleId);
        leaderRoleId = normalizedLeaderRoleId;
        members = List.copyOf(members);
        if (normalizedLeaderRoleId.isBlank()) throw new IllegalArgumentException("Canonical Pact leader cannot be blank: " + key);
        if (members.stream().noneMatch(member -> member.roleId().equals(normalizedLeaderRoleId))) {
            throw new IllegalArgumentException("Canonical Pact leader must also be a member: " + key + " / " + normalizedLeaderRoleId);
        }
    }
}
