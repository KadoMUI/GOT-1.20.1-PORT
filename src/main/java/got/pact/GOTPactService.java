package got.pact;

import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;
import java.util.UUID;

/** Mutation/query facade. Call this instead of editing GOTPact directly. */
public final class GOTPactService {
    private static final long INVITE_LIFETIME_TICKS = 20L * 60L * 5L;
    private GOTPactService() {}

    public static Optional<GOTPact> forPlayer(MinecraftServer server, UUID player) { return GOTPactSavedData.get(server).pactFor(player); }
    public static Optional<GOTPact> byId(MinecraftServer server, UUID pact) { return GOTPactSavedData.get(server).pact(pact); }
    public static boolean samePact(MinecraftServer server, UUID a, UUID b) {
        Optional<GOTPact> pact = forPlayer(server, a);
        return pact.isPresent() && pact.get().contains(b);
    }

    public static Result create(ServerPlayer owner, String name) {
        GOTPactSavedData data = GOTPactSavedData.get(owner.server);
        if (data.pactFor(owner.getUUID()).isPresent()) return Result.fail("You are already in a Pact.");
        GOTPact pact = new GOTPact(UUID.randomUUID(), owner.getUUID(), name);
        data.put(pact);
        notify(pact, owner.server, Component.literal(owner.getGameProfile().getName() + " founded the Pact " + pact.name() + "."));
        return Result.ok(pact);
    }

    public static Result invite(ServerPlayer actor, ServerPlayer target) {
        GOTPactSavedData data = GOTPactSavedData.get(actor.server);
        GOTPact pact = data.pactFor(actor.getUUID()).orElse(null);
        if (pact == null) return Result.fail("You are not in a Pact.");
        if (!pact.canManage(actor.getUUID())) return Result.fail("Only the Pact owner or an admin can invite players.");
        if (data.pactFor(target.getUUID()).isPresent()) return Result.fail("That player is already in a Pact.");
        if (pact.size() >= GOTPact.DEFAULT_MAX_MEMBERS) return Result.fail("That Pact is full.");
        long expiry = actor.server.overworld().getGameTime() + INVITE_LIFETIME_TICKS;
        data.invite(new GOTPactInvite(pact.id(), actor.getUUID(), target.getUUID(), expiry));
        target.sendSystemMessage(Component.literal(actor.getGameProfile().getName() + " invited you to join " + pact.name() + ". Use /pact accept or /pact decline."));
        return Result.ok(pact);
    }

    public static Result accept(ServerPlayer player) {
        GOTPactSavedData data = GOTPactSavedData.get(player.server);
        if (data.pactFor(player.getUUID()).isPresent()) return Result.fail("You are already in a Pact.");
        GOTPactInvite invite = data.inviteFor(player.getUUID()).orElse(null);
        if (invite == null) return Result.fail("You do not have a Pact invitation.");
        if (invite.expired(player.server.overworld().getGameTime())) { data.clearInvite(player.getUUID()); return Result.fail("That Pact invitation expired."); }
        GOTPact pact = data.pact(invite.pactId()).orElse(null);
        if (pact == null) { data.clearInvite(player.getUUID()); return Result.fail("That Pact no longer exists."); }
        if (!pact.addMember(player.getUUID())) return Result.fail("Could not join that Pact.");
        data.indexPlayer(player.getUUID(), pact.id());
        data.clearInvite(player.getUUID());
        data.setDirty();
        notify(pact, player.server, Component.literal(player.getGameProfile().getName() + " joined the Pact."));
        return Result.ok(pact);
    }

    public static Result decline(ServerPlayer player) {
        GOTPactSavedData data = GOTPactSavedData.get(player.server);
        if (data.inviteFor(player.getUUID()).isEmpty()) return Result.fail("You do not have a Pact invitation.");
        data.clearInvite(player.getUUID());
        return Result.ok(null);
    }

    public static Result leave(ServerPlayer player) {
        GOTPactSavedData data = GOTPactSavedData.get(player.server);
        GOTPact pact = data.pactFor(player.getUUID()).orElse(null);
        if (pact == null) return Result.fail("You are not in a Pact.");
        if (pact.isOwner(player.getUUID())) return Result.fail("The owner must transfer ownership or disband the Pact.");
        pact.removeMember(player.getUUID());
        data.unindexPlayer(player.getUUID());
        data.setDirty();
        notify(pact, player.server, Component.literal(player.getGameProfile().getName() + " left the Pact."));
        return Result.ok(pact);
    }

    public static Result kick(ServerPlayer actor, UUID target) {
        GOTPactSavedData data = GOTPactSavedData.get(actor.server);
        GOTPact pact = data.pactFor(actor.getUUID()).orElse(null);
        if (pact == null || !pact.canManage(actor.getUUID())) return Result.fail("You cannot manage that Pact.");
        if (!pact.contains(target)) return Result.fail("That player is not in your Pact.");
        if (pact.isOwner(target)) return Result.fail("The owner cannot be kicked.");
        if (pact.isAdmin(target) && !pact.isOwner(actor.getUUID())) return Result.fail("Only the owner can remove an admin.");
        pact.removeMember(target);
        data.unindexPlayer(target);
        data.setDirty();
        ServerPlayer online = actor.server.getPlayerList().getPlayer(target);
        if (online != null) online.sendSystemMessage(Component.literal("You were removed from " + pact.name() + "."));
        notify(pact, actor.server, Component.literal("A member was removed from the Pact."));
        return Result.ok(pact);
    }

    public static Result setAdmin(ServerPlayer actor, UUID target, boolean admin) {
        GOTPactSavedData data = GOTPactSavedData.get(actor.server);
        GOTPact pact = data.pactFor(actor.getUUID()).orElse(null);
        if (pact == null || !pact.isOwner(actor.getUUID())) return Result.fail("Only the Pact owner can change admins.");
        if (!pact.setAdmin(target, admin)) return Result.fail("Could not change that member's admin status.");
        data.setDirty();
        return Result.ok(pact);
    }

    public static Result transfer(ServerPlayer actor, UUID target) {
        GOTPactSavedData data = GOTPactSavedData.get(actor.server);
        GOTPact pact = data.pactFor(actor.getUUID()).orElse(null);
        if (pact == null || !pact.isOwner(actor.getUUID())) return Result.fail("Only the Pact owner can transfer ownership.");
        if (!pact.transferOwnership(target)) return Result.fail("Ownership can only be transferred to another member.");
        data.setDirty();
        return Result.ok(pact);
    }

    public static Result rename(ServerPlayer actor, String name) {
        GOTPactSavedData data = GOTPactSavedData.get(actor.server);
        GOTPact pact = data.pactFor(actor.getUUID()).orElse(null);
        if (pact == null || !pact.canManage(actor.getUUID())) return Result.fail("You cannot rename that Pact.");
        pact.setName(name); data.setDirty(); return Result.ok(pact);
    }

    /**
     * Legacy Fellowship parity: the Pact icon is the item currently held in the
     * actor's main hand. An empty hand clears the icon. The item is not consumed.
     */
    public static Result setIconFromHeldItem(ServerPlayer actor) {
        GOTPactSavedData data = GOTPactSavedData.get(actor.server);
        GOTPact pact = data.pactFor(actor.getUUID()).orElse(null);
        if (pact == null || !pact.canManage(actor.getUUID())) return Result.fail("You cannot change that Pact's icon.");

        ItemStack held = actor.getMainHandItem();
        ResourceLocation icon = held.isEmpty() ? null : BuiltInRegistries.ITEM.getKey(held.getItem());
        pact.setIcon(icon);
        data.setDirty();
        return Result.ok(pact);
    }

    public static Result togglePvp(ServerPlayer actor) { return toggle(actor, 0); }
    public static Result toggleHiredFriendlyFire(ServerPlayer actor) { return toggle(actor, 1); }
    public static Result toggleMap(ServerPlayer actor) { return toggle(actor, 2); }
    private static Result toggle(ServerPlayer actor, int kind) {
        GOTPactSavedData data = GOTPactSavedData.get(actor.server);
        GOTPact pact = data.pactFor(actor.getUUID()).orElse(null);
        if (pact == null || !pact.canManage(actor.getUUID())) return Result.fail("You cannot change Pact settings.");
        if (kind == 0) pact.setPreventPvp(!pact.preventPvp());
        if (kind == 1) pact.setPreventHiredFriendlyFire(!pact.preventHiredFriendlyFire());
        if (kind == 2) pact.setShowMapLocations(!pact.showMapLocations());
        data.setDirty(); return Result.ok(pact);
    }

    public static Result setMapSharing(ServerPlayer actor, boolean sharing) {
        GOTPactSavedData data = GOTPactSavedData.get(actor.server);
        GOTPact pact = data.pactFor(actor.getUUID()).orElse(null);
        if (pact == null || !pact.setMapSharing(actor.getUUID(), sharing)) return Result.fail("Could not change map sharing.");
        data.setDirty(); return Result.ok(pact);
    }

    public static Result disband(ServerPlayer actor) {
        GOTPactSavedData data = GOTPactSavedData.get(actor.server);
        GOTPact pact = data.pactFor(actor.getUUID()).orElse(null);
        if (pact == null || !pact.isOwner(actor.getUUID())) return Result.fail("Only the Pact owner can disband it.");
        notify(pact, actor.server, Component.literal(pact.name() + " has been disbanded."));
        data.removePact(pact.id());
        return Result.ok(null);
    }

    public static void message(ServerPlayer sender, Component message) {
        GOTPact pact = forPlayer(sender.server, sender.getUUID()).orElse(null);
        if (pact == null) { sender.sendSystemMessage(Component.literal("You are not in a Pact.")); return; }
        Component line = Component.literal("[Pact] <" + sender.getGameProfile().getName() + "> ").append(message);
        notify(pact, sender.server, line);
    }

    private static void notify(GOTPact pact, MinecraftServer server, Component message) {
        for (UUID id : pact.members()) {
            ServerPlayer p = server.getPlayerList().getPlayer(id);
            if (p != null) p.sendSystemMessage(message);
        }
    }

    public record Result(boolean success, String error, GOTPact pact) {
        public static Result ok(GOTPact pact) { return new Result(true, "", pact); }
        public static Result fail(String error) { return new Result(false, error, null); }
    }
}
