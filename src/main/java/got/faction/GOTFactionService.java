package got.faction;

import got.network.GOTNetwork;
import got.network.S2CFactionDataPacket;
import got.npc.GOTFactionNpc;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.PacketDistributor;

import java.util.EnumMap;
import java.util.Map;

public final class GOTFactionService {
    private GOTFactionService() {}

    public static void onNpcKilled(ServerPlayer player, LivingEntity victim, GOTFactionNpc npc) {
        GOTFaction slainFaction = npc.getFaction();
        if (!slainFaction.isPlayable()) return;

        float base = Math.abs(npc.getAlignmentBonus());
        if (base == 0.0F) return;
        GOTFactionPlayerData data = GOTFactionPlayerData.get(player);
        Map<GOTFaction, Float> changes = new EnumMap<>(GOTFaction.class);

        data.addNpcKill(slainFaction);
        applyChange(data, slainFaction, scalePenalty(-base, data.alignment(slainFaction)), changes);

        for (GOTFaction beneficiary : GOTFaction.playableFactions()) {
            if (beneficiary == slainFaction || !slainFaction.isBadRelation(beneficiary)) continue;
            data.addEnemyKill(beneficiary);
            if (npc.isCivilian() && !beneficiary.approvesCivilianEnemyKills()) continue;
            if (!GOTFactionControl.isInInfluence(player, beneficiary)) continue;

            float gain = base;
            if (data.alignment(beneficiary) >= beneficiary.pledgeAlignment()
                    && data.membership() != beneficiary) {
                gain *= 0.5F;
            }
            if (data.membership() != GOTFaction.UNALIGNED
                    && data.membership().relationTo(beneficiary) == GOTFactionRelation.MORTAL_ENEMY) {
                gain = Math.min(gain, Math.max(0.0F, -data.alignment(beneficiary)));
            }
            applyChange(data, beneficiary, gain, changes);
        }

        handleMembershipKill(player, data, slainFaction);
        GOTFaction membership = data.membership();
        if (membership != GOTFaction.UNALIGNED && data.alignment(membership) < membership.pledgeAlignment()) {
            leaveFaction(player, false);
        } else {
            notifyChanges(player, victim, changes);
            sync(player);
        }
    }

    public static boolean joinFaction(ServerPlayer player, GOTFaction faction) {
        GOTFactionPlayerData data = GOTFactionPlayerData.get(player);
        if (!faction.isPlayable()) {
            player.displayClientMessage(Component.translatable("got.faction.join.invalid").withStyle(ChatFormatting.RED), false);
            return false;
        }
        if (data.membership() != GOTFaction.UNALIGNED) {
            player.displayClientMessage(Component.translatable("got.faction.join.already",
                    data.membership().displayName()).withStyle(ChatFormatting.RED), false);
            return false;
        }
        if (data.pledgeBreakCooldown() > 0) {
            player.displayClientMessage(Component.translatable("got.faction.join.cooldown",
                    formatTicks(data.pledgeBreakCooldown())).withStyle(ChatFormatting.RED), false);
            return false;
        }
        if (data.alignment(faction) < faction.pledgeAlignment()) {
            player.displayClientMessage(Component.translatable("got.faction.join.requires",
                    formatAlignment(faction.pledgeAlignment()), faction.displayName()).withStyle(ChatFormatting.RED), false);
            return false;
        }

        data.setMembership(faction);
        player.level().playSound(null, player.blockPosition(),
                net.minecraft.sounds.SoundEvents.PLAYER_LEVELUP,
                net.minecraft.sounds.SoundSource.PLAYERS, 0.8F, 0.9F);
        player.displayClientMessage(Component.translatable("got.faction.join.success",
                faction.displayName()).withStyle(ChatFormatting.GOLD), false);
        sync(player);
        return true;
    }

    public static boolean leaveFaction(ServerPlayer player, boolean intentional) {
        GOTFactionPlayerData data = GOTFactionPlayerData.get(player);
        GOTFaction previous = data.membership();
        if (previous == GOTFaction.UNALIGNED) return false;

        float oldAlignment = data.alignment(previous);
        float replacement;
        if (previous == GOTFaction.WHITE_WALKER) replacement = 500.0F;
        else if (oldAlignment >= 1000.0F) replacement = 100.0F;
        else replacement = 50.0F;
        replacement = Math.min(oldAlignment, replacement);

        float excess = Math.max(0.0F, oldAlignment - previous.pledgeAlignment());
        float cooldownFraction = Math.max(0.0F, Math.min(1.0F, excess / 5000.0F));
        int cooldown = 36_000 + Math.round(cooldownFraction * 180_000.0F);

        data.setMembership(GOTFaction.UNALIGNED);
        data.setBrokenFaction(previous);
        data.setPledgeBreakCooldown(cooldown);
        if (replacement < oldAlignment) data.setAlignment(previous, replacement);

        player.level().playSound(null, player.blockPosition(),
                net.minecraft.sounds.SoundEvents.SHIELD_BREAK,
                net.minecraft.sounds.SoundSource.PLAYERS, 0.8F, 0.8F);
        player.displayClientMessage(Component.translatable(
                intentional ? "got.faction.leave.success" : "got.faction.leave.forced",
                previous.displayName()).withStyle(ChatFormatting.RED), false);
        sync(player);
        return true;
    }

    public static float setAlignment(ServerPlayer player, GOTFaction faction, float value) {
        GOTFactionPlayerData data = GOTFactionPlayerData.get(player);
        float before = data.alignment(faction);
        data.setAlignment(faction, value);
        if (data.membership() == faction && data.alignment(faction) < faction.pledgeAlignment()) {
            leaveFaction(player, false);
        } else {
            sync(player);
        }
        return data.alignment(faction) - before;
    }

    public static float addAlignment(ServerPlayer player, GOTFaction faction, float value) {
        return setAlignment(player, faction, GOTFactionPlayerData.get(player).alignment(faction) + value);
    }

    public static void sync(ServerPlayer player) {
        GOTNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                new S2CFactionDataPacket(GOTFactionPlayerData.get(player).snapshot()));
    }

    public static String formatAlignment(float value) {
        return String.format(java.util.Locale.ROOT, value >= 0.0F ? "+%,.1f" : "%,.1f", value);
    }

    public static String formatTicks(int ticks) {
        int seconds = Math.max(0, ticks) / 20;
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int remaining = seconds % 60;
        return hours > 0 ? String.format("%d:%02d:%02d", hours, minutes, remaining)
                : String.format("%d:%02d", minutes, remaining);
    }

    private static float scalePenalty(float penalty, float alignment) {
        if (alignment > 0.0F && penalty < 0.0F) {
            penalty *= Math.max(1.0F, Math.min(20.0F, alignment / 50.0F));
        }
        return penalty;
    }

    private static void applyChange(GOTFactionPlayerData data, GOTFaction faction, float requested,
                                    Map<GOTFaction, Float> changes) {
        if (requested == 0.0F) return;
        float actual = data.addAlignment(faction, requested);
        if (actual != 0.0F) changes.merge(faction, actual, Float::sum);
    }

    private static void handleMembershipKill(ServerPlayer player, GOTFactionPlayerData data,
                                             GOTFaction slainFaction) {
        GOTFaction membership = data.membership();
        if (membership == GOTFaction.UNALIGNED) return;
        if (membership != slainFaction && membership.relationTo(slainFaction) != GOTFactionRelation.ALLY) return;

        int newCooldown = data.pledgeKillCooldown() + 24_000;
        data.setPledgeKillCooldown(newCooldown);
        if (newCooldown > 24_000) {
            leaveFaction(player, false);
        } else {
            player.displayClientMessage(Component.translatable("got.chat.pledgeKillWarn",
                    membership.displayName()).withStyle(ChatFormatting.RED), false);
        }
    }

    private static void notifyChanges(ServerPlayer player, LivingEntity victim,
                                      Map<GOTFaction, Float> changes) {
        if (changes.isEmpty()) return;
        Component message = Component.translatable("got.alignment.kill", victim.getDisplayName()).withStyle(ChatFormatting.GRAY);
        for (Map.Entry<GOTFaction, Float> entry : changes.entrySet()) {
            ChatFormatting color = entry.getValue() >= 0.0F ? ChatFormatting.GREEN : ChatFormatting.RED;
            message = message.copy().append("  ").append(entry.getKey().displayName().copy().withStyle(color))
                    .append(" ").append(Component.literal(formatAlignment(entry.getValue())).withStyle(color));
        }
        player.displayClientMessage(message, true);
    }
}
