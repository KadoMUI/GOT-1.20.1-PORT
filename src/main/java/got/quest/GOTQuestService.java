package got.quest;

import got.GOTItems;
import got.faction.GOTFaction;
import got.faction.GOTFactionPlayerData;
import got.faction.GOTFactionService;
import got.network.GOTNetwork;
import got.network.S2CQuestDataPacket;
import got.network.S2CQuestOfferPacket;
import got.npc.GOTFactionNpc;
import got.npc.hiring.GOTHiredTask;
import got.npc.hiring.GOTHiringService;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/** Server-authoritative quest lifecycle, objective progress, turn-in, and rewards. */
public final class GOTQuestService {
    private static final Map<UUID, PendingOffer> PENDING_OFFERS = new HashMap<>();
    private static final int OFFER_LIFETIME = 20 * 60;

    private GOTQuestService() {}


    /** Server-authoritative query used by the legacy-style overhead quest marker. */
    public static boolean hasAvailableQuest(ServerPlayer player, Mob npc, GOTQuestGiver giver) {
        if (!npc.isAlive() || !giver.canOfferQuests()) return false;
        GOTQuestPlayerData data = GOTQuestPlayerData.get(player);
        if (data.findActiveByGiver(npc.getUUID()).isPresent()) return false;
        return !availableFor(player, npc, giver, data).isEmpty();
    }

    public static boolean interact(ServerPlayer player, Mob npc, GOTQuestGiver giver) {
        if (!npc.isAlive()) return false;
        GOTQuestPlayerData data = GOTQuestPlayerData.get(player);
        boolean talked = progressTalkObjectives(player, npc, giver, data);

        Optional<GOTQuestInstance> owned = data.findActiveByGiver(npc.getUUID());
        if (owned.isPresent()) {
            GOTQuestInstance instance = owned.get();
            Optional<GOTQuestDefinition> optionalDefinition = GOTQuestDefinitionManager.get(instance.definitionId());
            if (optionalDefinition.isEmpty()) {
                fail(player, data, instance, "got.quest.failure.definition_missing");
                return true;
            }
            GOTQuestDefinition definition = optionalDefinition.get();
            contributeCollectItems(player, definition, instance);
            refreshReady(definition, instance, player.level().getGameTime());
            data.update(instance);
            if (instance.state() == GOTQuestState.READY) {
                complete(player, data, definition, instance);
            } else {
                data.setTracked(instance.instanceId());
                GOTQuestView.ObjectiveView objective = view(definition, instance, true).currentObjective();
                player.displayClientMessage(Component.translatable(definition.progressKey(),
                        npc.getDisplayName(), Component.translatable(objective.labelKey()),
                        objective.progress(), objective.target()).withStyle(ChatFormatting.GOLD), false);
                sync(player);
            }
            return true;
        }

        if (!giver.canOfferQuests()) {
            if (talked) sync(player);
            return talked;
        }

        List<GOTQuestDefinition> candidates = availableFor(player, npc, giver, data);
        if (candidates.isEmpty()) {
            if (talked) sync(player);
            return talked;
        }

        GOTQuestDefinition offer = chooseOffer(player, npc, candidates);
        long expiry = serverGameTime(player) + OFFER_LIFETIME;
        PENDING_OFFERS.put(player.getUUID(), new PendingOffer(offer.id(), npc.getUUID(), expiry));
        GOTQuestView offerView = view(offer, null, false, npc.getDisplayName().getString());
        GOTNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                new S2CQuestOfferPacket(offerView, npc.getUUID()));
        return true;
    }

    public static boolean acceptOffer(ServerPlayer player, ResourceLocation questId, UUID giverId) {
        PendingOffer pending = PENDING_OFFERS.get(player.getUUID());
        if (pending == null || !pending.definitionId.equals(questId) || !pending.giverId.equals(giverId)
                || pending.expiresAt < serverGameTime(player)) {
            player.displayClientMessage(Component.translatable("got.quest.offer.expired")
                    .withStyle(ChatFormatting.RED), false);
            PENDING_OFFERS.remove(player.getUUID());
            return false;
        }

        Entity entity = findEntity(player, giverId);
        if (!(entity instanceof Mob npc) || !(entity instanceof GOTQuestGiver giver)
                || entity.level() != player.level() || entity.distanceToSqr(player) > 100.0D) {
            player.displayClientMessage(Component.translatable("got.quest.offer.too_far")
                    .withStyle(ChatFormatting.RED), false);
            return false;
        }
        GOTQuestDefinition definition = GOTQuestDefinitionManager.get(questId).orElse(null);
        GOTQuestPlayerData data = GOTQuestPlayerData.get(player);
        if (definition == null || !isAvailable(player, npc, giver, data, definition)) return false;

        GOTQuestInstance instance = GOTQuestInstance.create(definition, giverId,
                npc.getDisplayName().getString(), giver.getQuestFaction(), player.level().dimension(),
                npc.blockPosition(), player.level().getGameTime());
        data.add(instance);
        data.setTracked(instance.instanceId());
        PENDING_OFFERS.remove(player.getUUID());
        player.level().playSound(null, player.blockPosition(), SoundEvents.BOOK_PAGE_TURN,
                SoundSource.PLAYERS, 0.8F, 1.0F);
        player.displayClientMessage(Component.translatable("got.quest.accepted",
                Component.translatable(definition.titleKey())).withStyle(ChatFormatting.GOLD), false);
        sync(player);
        return true;
    }

    public static void declineOffer(ServerPlayer player, ResourceLocation questId, UUID giverId) {
        PendingOffer pending = PENDING_OFFERS.get(player.getUUID());
        if (pending != null && pending.definitionId.equals(questId) && pending.giverId.equals(giverId)) {
            PENDING_OFFERS.remove(player.getUUID());
        }
    }

    public static void clearPendingOffer(ServerPlayer player) {
        PENDING_OFFERS.remove(player.getUUID());
    }

    public static void track(ServerPlayer player, UUID instanceId) {
        GOTQuestPlayerData data = GOTQuestPlayerData.get(player);
        if (data.findActive(instanceId).isEmpty()) return;
        UUID current = data.tracked();
        data.setTracked(instanceId.equals(current) ? null : instanceId);
        sync(player);
    }

    public static void abandon(ServerPlayer player, UUID instanceId) {
        GOTQuestPlayerData data = GOTQuestPlayerData.get(player);
        GOTQuestInstance instance = data.findActive(instanceId).orElse(null);
        if (instance == null) return;
        instance.setState(GOTQuestState.ABANDONED, player.level().getGameTime());
        data.archive(instance);
        player.displayClientMessage(Component.translatable("got.quest.abandoned")
                .withStyle(ChatFormatting.RED), false);
        sync(player);
    }

    public static void onKill(ServerPlayer player, LivingEntity victim) {
        GOTQuestPlayerData data = GOTQuestPlayerData.get(player);
        boolean changed = false;
        for (GOTQuestInstance instance : data.active()) {
            GOTQuestDefinition definition = GOTQuestDefinitionManager.get(instance.definitionId()).orElse(null);
            if (definition == null) continue;
            boolean instanceChanged = false;
            for (int index = 0; index < definition.objectives().size(); index++) {
                GOTQuestDefinition.Objective objective = definition.objectives().get(index);
                if (!canProgress(definition, instance, index)) continue;
                if (objective.type() == GOTQuestObjectiveType.KILL_ENTITY
                        && matchesEntity(objective, victim)) {
                    instanceChanged |= instance.addProgress(index, 1, instance.target(index, objective));
                } else if (objective.type() == GOTQuestObjectiveType.KILL_FACTION
                        && victim instanceof GOTFactionNpc factionNpc
                        && factionNpc.getFactionId().equals(objective.target())) {
                    instanceChanged |= instance.addProgress(index, 1, instance.target(index, objective));
                }
            }
            if (instanceChanged) {
                refreshReady(definition, instance, player.level().getGameTime());
                data.update(instance);
                if (definition.autoComplete() && instance.state() == GOTQuestState.READY) {
                    complete(player, data, definition, instance);
                } else {
                    notifyProgress(player, definition, instance);
                }
                changed = true;
            }
        }
        if (changed) sync(player);
    }

    public static void onPlayerTick(ServerPlayer player) {
        if (player.tickCount % 20 != 0) return;
        long now = serverGameTime(player);
        PendingOffer pending = PENDING_OFFERS.get(player.getUUID());
        if (pending != null && pending.expiresAt < now) PENDING_OFFERS.remove(player.getUUID());

        GOTQuestPlayerData data = GOTQuestPlayerData.get(player);
        boolean changed = false;
        for (GOTQuestInstance instance : data.active()) {
            GOTQuestDefinition definition = GOTQuestDefinitionManager.get(instance.definitionId()).orElse(null);
            if (definition == null) continue;
            boolean instanceChanged = false;
            for (int index = 0; index < definition.objectives().size(); index++) {
                GOTQuestDefinition.Objective objective = definition.objectives().get(index);
                if (objective.type() != GOTQuestObjectiveType.VISIT_LOCATION
                        || !canProgress(definition, instance, index)) continue;
                if (!player.level().dimension().location().equals(objective.dimension())) continue;
                double radiusSquared = objective.radius() * objective.radius();
                if (player.blockPosition().distSqr(objective.position()) <= radiusSquared) {
                    instanceChanged |= instance.addProgress(index, instance.target(index, objective), instance.target(index, objective));
                }
            }
            if (instanceChanged) {
                refreshReady(definition, instance, player.level().getGameTime());
                data.update(instance);
                if (definition.autoComplete() && instance.state() == GOTQuestState.READY) {
                    complete(player, data, definition, instance);
                } else {
                    notifyProgress(player, definition, instance);
                }
                changed = true;
            }
        }
        if (changed) sync(player);
    }

    /** Public hook for story systems, bosses, dungeons, dialogue choices, and scripted events. */
    public static void triggerEvent(ServerPlayer player, ResourceLocation eventId, int amount) {
        if (amount <= 0) return;
        GOTQuestPlayerData data = GOTQuestPlayerData.get(player);
        boolean changed = false;
        for (GOTQuestInstance instance : data.active()) {
            GOTQuestDefinition definition = GOTQuestDefinitionManager.get(instance.definitionId()).orElse(null);
            if (definition == null) continue;
            boolean instanceChanged = false;
            for (int index = 0; index < definition.objectives().size(); index++) {
                GOTQuestDefinition.Objective objective = definition.objectives().get(index);
                if (objective.type() == GOTQuestObjectiveType.EVENT
                        && objective.target().equals(eventId.toString())
                        && canProgress(definition, instance, index)) {
                    instanceChanged |= instance.addProgress(index, amount, instance.target(index, objective));
                }
            }
            if (instanceChanged) {
                refreshReady(definition, instance, player.level().getGameTime());
                data.update(instance);
                if (definition.autoComplete() && instance.state() == GOTQuestState.READY) {
                    complete(player, data, definition, instance);
                } else {
                    notifyProgress(player, definition, instance);
                }
                changed = true;
            }
        }
        if (changed) sync(player);
    }

    public static void onQuestGiverDeath(Mob npc) {
        if (!(npc.level() instanceof ServerLevel level)) return;
        for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
            GOTQuestPlayerData data = GOTQuestPlayerData.get(player);
            GOTQuestInstance instance = data.findActiveByGiver(npc.getUUID()).orElse(null);
            if (instance != null) fail(player, data, instance, "got.quest.failure.giver_dead");
        }
    }

    public static void sync(ServerPlayer player) {
        GOTQuestPlayerData data = GOTQuestPlayerData.get(player);
        UUID tracked = data.tracked();
        List<GOTQuestView> active = new ArrayList<>();
        for (GOTQuestInstance instance : data.active()) {
            GOTQuestDefinitionManager.get(instance.definitionId()).ifPresent(definition -> {
                instance.ensureObjectiveCount(definition);
                active.add(view(definition, instance, instance.instanceId().equals(tracked)));
            });
        }
        List<GOTQuestView> archive = new ArrayList<>();
        for (GOTQuestInstance instance : data.archive()) {
            GOTQuestDefinitionManager.get(instance.definitionId()).ifPresent(definition ->
                    archive.add(view(definition, instance, false)));
        }
        GOTNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                new S2CQuestDataPacket(active, archive, tracked));
    }

    private static List<GOTQuestDefinition> availableFor(ServerPlayer player, Mob npc,
                                                          GOTQuestGiver giver,
                                                          GOTQuestPlayerData data) {
        List<GOTQuestDefinition> result = new ArrayList<>();
        for (GOTQuestDefinition definition : GOTQuestDefinitionManager.all()) {
            if (isAvailable(player, npc, giver, data, definition)) result.add(definition);
        }
        boolean hasLegendaryRoleQuest = result.stream().anyMatch(definition -> definition.legendary()
                && !definition.giver().roles().isEmpty()
                && definition.giver().roles().contains(giver.getQuestRoleId()));
        if (hasLegendaryRoleQuest) {
            result.removeIf(definition -> !definition.legendary()
                    || definition.giver().roles().isEmpty()
                    || !definition.giver().roles().contains(giver.getQuestRoleId()));
        }
        result.sort(Comparator.comparing(definition -> definition.id().toString()));
        return result;
    }

    private static boolean isAvailable(ServerPlayer player, Mob npc, GOTQuestGiver giver,
                                       GOTQuestPlayerData data, GOTQuestDefinition definition) {
        GOTQuestDefinition.Giver rules = definition.giver();
        ResourceLocation entityType = ForgeRegistries.ENTITY_TYPES.getKey(npc.getType());
        if (!rules.roles().isEmpty() && !rules.roles().contains(giver.getQuestRoleId())) return false;
        if (!rules.factions().isEmpty() && !rules.factions().contains(giver.getQuestFaction())) return false;
        if (!rules.entityTypes().isEmpty() && !rules.entityTypes().contains(entityType)) return false;
        if (data.active().stream().anyMatch(quest -> quest.definitionId().equals(definition.id()))) return false;
        for (ResourceLocation prerequisite : definition.prerequisites()) {
            if (data.completedCount(prerequisite) <= 0) return false;
        }
        int completions = data.completedCount(definition.id());
        if (!definition.repeatable() && completions > 0) return false;
        if (definition.repeatable() && definition.cooldownTicks() > 0 && completions > 0
                && serverGameTime(player) - data.lastCompletedTime(definition.id()) < definition.cooldownTicks()) {
            return false;
        }
        GOTFactionPlayerData factions = GOTFactionPlayerData.get(player);
        if (giver.getQuestFaction().isPlayable()
                && factions.alignment(giver.getQuestFaction()) < rules.minimumAlignment()) return false;
        if (rules.requiredMembership() != GOTFaction.UNALIGNED
                && factions.membership() != rules.requiredMembership()) return false;
        long activeForFaction = data.active().stream()
                .filter(quest -> quest.giverFaction() == giver.getQuestFaction()).count();
        return activeForFaction < rules.maximumActiveForFaction();
    }

    private static GOTQuestDefinition chooseOffer(ServerPlayer player, Mob npc,
                                                   List<GOTQuestDefinition> candidates) {
        int totalWeight = candidates.stream().mapToInt(GOTQuestDefinition::weight).sum();
        long day = serverGameTime(player) / 24_000L;
        long seed = player.getUUID().getMostSignificantBits() ^ npc.getUUID().getLeastSignificantBits() ^ day;
        int value = RandomSource.create(seed).nextInt(Math.max(1, totalWeight));
        for (GOTQuestDefinition candidate : candidates) {
            value -= candidate.weight();
            if (value < 0) return candidate;
        }
        return candidates.get(0);
    }

    private static boolean progressTalkObjectives(ServerPlayer player, Mob npc,
                                                  GOTQuestGiver giver, GOTQuestPlayerData data) {
        boolean changed = false;
        ResourceLocation type = ForgeRegistries.ENTITY_TYPES.getKey(npc.getType());
        for (GOTQuestInstance instance : data.active()) {
            GOTQuestDefinition definition = GOTQuestDefinitionManager.get(instance.definitionId()).orElse(null);
            if (definition == null) continue;
            boolean instanceChanged = false;
            for (int index = 0; index < definition.objectives().size(); index++) {
                GOTQuestDefinition.Objective objective = definition.objectives().get(index);
                if (objective.type() != GOTQuestObjectiveType.TALK_TO_NPC
                        || !canProgress(definition, instance, index)) continue;
                boolean roleMatches = objective.role().isBlank()
                        || objective.role().equals(giver.getQuestRoleId());
                boolean targetMatches = objective.target().isBlank()
                        || objective.target().equals(giver.getQuestFaction().id())
                        || (type != null && objective.target().equals(type.toString()));
                if (roleMatches && targetMatches) {
                    instanceChanged |= instance.addProgress(index, 1, instance.target(index, objective));
                }
            }
            if (instanceChanged) {
                refreshReady(definition, instance, player.level().getGameTime());
                data.update(instance);
                if (definition.autoComplete() && instance.state() == GOTQuestState.READY) {
                    complete(player, data, definition, instance);
                } else {
                    notifyProgress(player, definition, instance);
                }
                changed = true;
            }
        }
        return changed;
    }

    private static void contributeCollectItems(ServerPlayer player, GOTQuestDefinition definition,
                                               GOTQuestInstance instance) {
        Inventory inventory = player.getInventory();
        for (int index = 0; index < definition.objectives().size(); index++) {
            GOTQuestDefinition.Objective objective = definition.objectives().get(index);
            if (objective.type() != GOTQuestObjectiveType.COLLECT
                    || !canProgress(definition, instance, index)) continue;
            ResourceLocation itemId = ResourceLocation.tryParse(objective.target());
            Item wanted = itemId == null ? null : ForgeRegistries.ITEMS.getValue(itemId);
            if (wanted == null) continue;
            if (!objective.consume()) {
                int available = 0;
                for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
                    ItemStack stack = inventory.getItem(slot);
                    if (stack.is(wanted)) available += stack.getCount();
                }
                int credited = Math.min(instance.target(index, objective), available);
                int contribution = credited - instance.progress(index);
                if (contribution > 0) instance.addProgress(index, contribution, instance.target(index, objective));
                continue;
            }
            int remaining = instance.target(index, objective) - instance.progress(index);
            for (int slot = 0; slot < inventory.getContainerSize() && remaining > 0; slot++) {
                ItemStack stack = inventory.getItem(slot);
                if (!stack.is(wanted)) continue;
                int contribution = Math.min(remaining, stack.getCount());
                stack.shrink(contribution);
                instance.addProgress(index, contribution, instance.target(index, objective));
                remaining -= contribution;
            }
        }
        inventory.setChanged();
    }

    private static boolean matchesEntity(GOTQuestDefinition.Objective objective, LivingEntity victim) {
        ResourceLocation type = ForgeRegistries.ENTITY_TYPES.getKey(victim.getType());
        if (!objective.target().isBlank() && (type == null || !objective.target().equals(type.toString()))) {
            return false;
        }
        return objective.role().isBlank() || victim instanceof GOTQuestGiver giver
                && objective.role().equals(giver.getQuestRoleId());
    }

    private static boolean canProgress(GOTQuestDefinition definition, GOTQuestInstance instance,
                                       int objectiveIndex) {
        if (!instance.state().isActive()) return false;
        if (!definition.sequential()) return true;
        for (int previous = 0; previous < objectiveIndex; previous++) {
            GOTQuestDefinition.Objective previousObjective = definition.objectives().get(previous);
            if (instance.progress(previous) < instance.target(previous, previousObjective)) return false;
        }
        return true;
    }

    private static void refreshReady(GOTQuestDefinition definition, GOTQuestInstance instance,
                                     long gameTime) {
        for (int index = 0; index < definition.objectives().size(); index++) {
            GOTQuestDefinition.Objective objective = definition.objectives().get(index);
            if (instance.progress(index) < instance.target(index, objective)) {
                if (instance.state() == GOTQuestState.READY) instance.setState(GOTQuestState.ACTIVE, gameTime);
                return;
            }
        }
        instance.setState(GOTQuestState.READY, gameTime);
    }

    private static void complete(ServerPlayer player, GOTQuestPlayerData data,
                                 GOTQuestDefinition definition, GOTQuestInstance instance) {
        instance.setState(GOTQuestState.COMPLETED, serverGameTime(player));
        data.archive(instance);
        got.achievement.GOTAchievementHooks.award(player, definition.legendary() ? "DO_MINIQUEST_LEGENDARY" : "DO_MINIQUEST");
        for (Map.Entry<GOTFaction, Float> alignment : definition.reward().alignment().entrySet()) {
            GOTFactionService.addAlignment(player, alignment.getKey(), alignment.getValue());
        }
        int legacyCoins = 0;
        if (definition.legacyRewardFactor() >= 0.0F && !definition.objectives().isEmpty()) {
            GOTQuestDefinition.Objective objective = definition.objectives().get(0);
            int target = instance.target(0, objective);
            float alignmentBonus = objective.type() == GOTQuestObjectiveType.COLLECT
                    ? Math.max(target * definition.legacyRewardFactor(), 1.0F)
                    : target * definition.legacyRewardFactor();
            if (instance.giverFaction().isPlayable()) {
                GOTFactionService.addAlignment(player, instance.giverFaction(), alignmentBonus);
            }
            legacyCoins = Math.round(alignmentBonus * 2.0F);
        }
        for (GOTQuestDefinition.ItemReward reward : definition.reward().items()) {
            Item item = ForgeRegistries.ITEMS.getValue(reward.item());
            if (item != null) give(player, new ItemStack(item, reward.count()));
        }
        giveCoins(player, definition.reward().coins() + legacyCoins);
        // Legacy miniquests had a 1-in-10 chance to include a faction-appropriate lore book.
        if (player.getRandom().nextInt(10) == 0) {
            ItemStack lore = got.lore.GOTLoreBookService.randomForFaction(player, instance.giverFaction(), player.getRandom());
            if (!lore.isEmpty()) give(player, lore);
        }
        if (definition.reward().experience() > 0) player.giveExperiencePoints(definition.reward().experience());
        if (definition.reward().hireGiver()
                && (!instance.giverFaction().isPlayable()
                || GOTFactionPlayerData.get(player).alignment(instance.giverFaction()) >= definition.reward().hireAlignment())) {
            Entity giver = findEntity(player, instance.giverId());
            if (giver != null) GOTHiringService.hire(player, giver, GOTHiredTask.WARRIOR);
        }
        player.level().playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP,
                SoundSource.PLAYERS, 0.9F, 1.1F);
        player.displayClientMessage(Component.translatable(definition.completeKey(),
                instance.giverName()).withStyle(ChatFormatting.GREEN), false);
        sync(player);
    }

    private static void fail(ServerPlayer player, GOTQuestPlayerData data,
                             GOTQuestInstance instance, String messageKey) {
        instance.setState(GOTQuestState.FAILED, serverGameTime(player));
        data.archive(instance);
        player.displayClientMessage(Component.translatable(messageKey, instance.giverName())
                .withStyle(ChatFormatting.RED), false);
        sync(player);
    }

    private static void notifyProgress(ServerPlayer player, GOTQuestDefinition definition,
                                       GOTQuestInstance instance) {
        GOTQuestView.ObjectiveView objective = view(definition, instance, false).currentObjective();
        player.displayClientMessage(Component.translatable("got.quest.progress.toast",
                Component.translatable(definition.titleKey()), objective.progress(), objective.target())
                .withStyle(ChatFormatting.GOLD), true);
        if (instance.state() == GOTQuestState.READY) {
            player.displayClientMessage(Component.translatable("got.quest.ready", instance.giverName())
                    .withStyle(ChatFormatting.GREEN), false);
        }
    }

    private static GOTQuestView view(GOTQuestDefinition definition,
                                     @Nullable GOTQuestInstance instance, boolean tracked) {
        return view(definition, instance, tracked, instance == null ? "" : instance.giverName());
    }

    private static GOTQuestView view(GOTQuestDefinition definition,
                                     @Nullable GOTQuestInstance instance, boolean tracked,
                                     String giverName) {
        List<GOTQuestView.ObjectiveView> objectives = new ArrayList<>();
        for (int index = 0; index < definition.objectives().size(); index++) {
            GOTQuestDefinition.Objective objective = definition.objectives().get(index);
            objectives.add(new GOTQuestView.ObjectiveView(objective.labelKey(),
                    instance == null ? 0 : instance.progress(index),
                    instance == null ? objective.maximumCount() : instance.target(index, objective)));
        }
        return new GOTQuestView(instance == null ? null : instance.instanceId(), definition.id(),
                definition.titleKey(), definition.descriptionKey(), definition.offerKey(),
                definition.progressKey(), definition.completeKey(), definition.icon(),
                definition.color(), giverName,
                instance == null ? GOTQuestState.ACTIVE : instance.state(), tracked, objectives);
    }

    private static void give(ServerPlayer player, ItemStack stack) {
        if (!player.getInventory().add(stack)) player.drop(stack, false);
    }

    private static void giveCoins(ServerPlayer player, int value) {
        if (value <= 0) return;
        int[] values = {16_384, 4_096, 1_024, 256, 64, 16, 4, 1};
        Item[] coins = {GOTItems.COIN_16384.get(), GOTItems.COIN_4096.get(),
                GOTItems.COIN_1024.get(), GOTItems.COIN_256.get(), GOTItems.COIN_64.get(),
                GOTItems.COIN_16.get(), GOTItems.COIN_4.get(), GOTItems.COIN_1.get()};
        int remaining = value;
        for (int index = 0; index < values.length; index++) {
            int count = remaining / values[index];
            remaining %= values[index];
            while (count > 0) {
                int stackSize = Math.min(64, count);
                give(player, new ItemStack(coins[index], stackSize));
                count -= stackSize;
            }
        }
    }

    @Nullable
    private static Entity findEntity(ServerPlayer player, UUID id) {
        for (ServerLevel level : player.getServer().getAllLevels()) {
            Entity entity = level.getEntity(id);
            if (entity != null) return entity;
        }
        return null;
    }

    private static long serverGameTime(ServerPlayer player) {
        return player.getServer().overworld().getGameTime();
    }

    private record PendingOffer(ResourceLocation definitionId, UUID giverId, long expiresAt) {}
}
