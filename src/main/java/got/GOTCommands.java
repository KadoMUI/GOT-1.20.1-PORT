package got;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import got.claim.GOTBannerClaim;
import got.claim.GOTBannerClaimService;
import got.faction.GOTFaction;
import got.faction.GOTFactionPlayerData;
import got.faction.GOTFactionService;
import got.world.GOTDimensions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTCommands {
    private GOTCommands() {}

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("got")
                .then(Commands.literal("enter").requires(source -> source.hasPermission(0)).executes(ctx -> enter(ctx.getSource())))
                .then(Commands.literal("claim")
                        .then(Commands.literal("info").executes(ctx -> claimInfo(ctx.getSource())))
                        .then(Commands.literal("list")
                                .then(Commands.argument("radius", IntegerArgumentType.integer(1, 256))
                                        .executes(ctx -> claimList(ctx.getSource(),
                                                IntegerArgumentType.getInteger(ctx, "radius")))))
                        .then(Commands.literal("setrange").requires(source -> source.hasPermission(2))
                                .then(Commands.argument("range", IntegerArgumentType.integer(0, GOTBannerClaim.MAX_RANGE))
                                        .executes(ctx -> claimRange(ctx.getSource(),
                                                IntegerArgumentType.getInteger(ctx, "range")))))
                        .then(Commands.literal("structure").requires(source -> source.hasPermission(2))
                                .then(Commands.argument("enabled", BoolArgumentType.bool())
                                        .executes(ctx -> claimStructure(ctx.getSource(),
                                                BoolArgumentType.getBool(ctx, "enabled")))))
                        .then(Commands.literal("transfer").requires(source -> source.hasPermission(2))
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(ctx -> claimTransfer(ctx.getSource(),
                                                EntityArgument.getPlayer(ctx, "player"))))))
                .then(Commands.literal("quest")
                        .then(Commands.literal("status").executes(ctx -> questStatus(ctx.getSource())))
                        .then(Commands.literal("book").executes(ctx -> questBook(ctx.getSource())))
                        .then(Commands.literal("trigger").requires(source -> source.hasPermission(2))
                                .then(Commands.argument("event", ResourceLocationArgument.id())
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                                .executes(ctx -> triggerQuestEvent(ctx.getSource(),
                                                        ResourceLocationArgument.getId(ctx, "event"),
                                                        IntegerArgumentType.getInteger(ctx, "amount")))))))
                .then(Commands.literal("faction")
                        .then(Commands.literal("status").executes(ctx -> factionStatus(ctx.getSource())))
                        .then(Commands.literal("join")
                                .then(Commands.argument("faction", StringArgumentType.word())
                                        .suggests((ctx, builder) -> net.minecraft.commands.SharedSuggestionProvider.suggest(
                                                GOTFaction.playableFactions().stream().map(GOTFaction::id), builder))
                                        .executes(ctx -> joinFaction(ctx.getSource(),
                                                StringArgumentType.getString(ctx, "faction")))))
                        .then(Commands.literal("leave").executes(ctx -> leaveFaction(ctx.getSource())))
                        .then(Commands.literal("relation")
                                .then(Commands.argument("first", StringArgumentType.word())
                                        .then(Commands.argument("second", StringArgumentType.word())
                                                .executes(ctx -> relation(ctx.getSource(),
                                                        StringArgumentType.getString(ctx, "first"),
                                                        StringArgumentType.getString(ctx, "second"))))))
                        .then(Commands.literal("alignment")
                                .then(Commands.literal("get")
                                        .then(Commands.argument("faction", StringArgumentType.word())
                                                .executes(ctx -> getAlignment(ctx.getSource(),
                                                        StringArgumentType.getString(ctx, "faction")))))
                                .then(Commands.literal("set").requires(source -> source.hasPermission(2))
                                        .then(Commands.argument("faction", StringArgumentType.word())
                                                .then(Commands.argument("value", FloatArgumentType.floatArg(-1_000_000.0F, 1_000_000.0F))
                                                        .executes(ctx -> setAlignment(ctx.getSource(),
                                                                StringArgumentType.getString(ctx, "faction"),
                                                                FloatArgumentType.getFloat(ctx, "value"), false)))))
                                .then(Commands.literal("add").requires(source -> source.hasPermission(2))
                                        .then(Commands.argument("faction", StringArgumentType.word())
                                                .then(Commands.argument("value", FloatArgumentType.floatArg(-1_000_000.0F, 1_000_000.0F))
                                                        .executes(ctx -> setAlignment(ctx.getSource(),
                                                                StringArgumentType.getString(ctx, "faction"),
                                                                FloatArgumentType.getFloat(ctx, "value"), true))))))));
    }

    private static int questStatus(CommandSourceStack source)
            throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        got.quest.GOTQuestPlayerData data = got.quest.GOTQuestPlayerData.get(player);
        source.sendSuccess(() -> Component.literal("Active quests: " + data.active().size()
                + " | Archived quests: " + data.archive().size()), false);
        return data.active().size();
    }

    private static int claimInfo(CommandSourceStack source)
            throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        GOTAbstractBannerEntity banner = nearestBanner(player, 8);
        if (banner == null) {
            source.sendFailure(Component.translatable("got.command.claim.noneNearby"));
            return 0;
        }
        source.sendSuccess(() -> Component.translatable("got.command.claim.info",
                banner.getBannerType().name(), banner.getOwnerName(), banner.getClaimRange(),
                banner.getClaimFaction().id(), banner.getClaim().playerSpecific()
                        ? "whitelist" : "faction", banner.getClaim().entries().size()), false);
        if (banner.isClaimActive() && banner.canPlayerEditClaim(player)) {
            GOTBannerClaimService.open(player, banner);
        }
        return 1;
    }

    private static int claimList(CommandSourceStack source, int radius)
            throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        int count = player.serverLevel().getEntitiesOfClass(GOTAbstractBannerEntity.class,
                player.getBoundingBox().inflate(radius), GOTAbstractBannerEntity::isClaimActive).size();
        source.sendSuccess(() -> Component.translatable("got.command.claim.list", count, radius), false);
        return count;
    }

    private static int claimRange(CommandSourceStack source, int range)
            throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        GOTAbstractBannerEntity banner = nearestBanner(source.getPlayerOrException(), 8);
        if (banner == null) {
            source.sendFailure(Component.translatable("got.command.claim.noneNearby"));
            return 0;
        }
        banner.getClaim().setCustomRange(range);
        banner.claimChanged();
        source.sendSuccess(() -> Component.translatable("got.command.claim.range", range), true);
        return 1;
    }

    private static int claimStructure(CommandSourceStack source, boolean enabled)
            throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        GOTAbstractBannerEntity banner = nearestBanner(source.getPlayerOrException(), 8);
        if (banner == null) {
            source.sendFailure(Component.translatable("got.command.claim.noneNearby"));
            return 0;
        }
        banner.getClaim().setStructureProtection(enabled);
        banner.claimChanged();
        source.sendSuccess(() -> Component.translatable("got.command.claim.structure", enabled), true);
        return 1;
    }

    private static int claimTransfer(CommandSourceStack source, ServerPlayer target)
            throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        GOTAbstractBannerEntity banner = nearestBanner(source.getPlayerOrException(), 8);
        if (banner == null) {
            source.sendFailure(Component.translatable("got.command.claim.noneNearby"));
            return 0;
        }
        banner.setOwner(target);
        banner.claimChanged();
        source.sendSuccess(() -> Component.translatable("got.command.claim.transfer",
                target.getGameProfile().getName()), true);
        return 1;
    }

    private static GOTAbstractBannerEntity nearestBanner(ServerPlayer player, int radius) {
        return player.serverLevel().getEntitiesOfClass(GOTStandingBannerEntity.class,
                        new AABB(player.blockPosition()).inflate(radius))
                .stream().min(java.util.Comparator.comparingDouble(player::distanceToSqr)).orElse(null);
    }

    private static int questBook(CommandSourceStack source)
            throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        net.minecraft.world.item.ItemStack book = GOTItems.QUEST_BOOK.get().getDefaultInstance();
        if (!player.getInventory().add(book)) player.drop(book, false);
        source.sendSuccess(() -> Component.translatable("item.got.quest_book"), false);
        return 1;
    }

    private static int triggerQuestEvent(CommandSourceStack source,
                                         net.minecraft.resources.ResourceLocation eventId, int amount)
            throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        got.quest.GOTQuestService.triggerEvent(player, eventId, amount);
        source.sendSuccess(() -> Component.literal("Triggered quest event " + eventId + " x" + amount), false);
        return amount;
    }

    private static int enter(CommandSourceStack source) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        ServerLevel target = source.getServer().getLevel(GOTDimensions.PLANETOS);
        if (target == null) {
            source.sendFailure(Component.literal("The Planetos dimension is unavailable. Create a new world with the Planetos world type or enable the GOT datapack."));
            return 0;
        }
        double x = player.getX();
        double z = player.getZ();
        int topY = target.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (int)Math.floor(x), (int)Math.floor(z));
        double y = Math.max(target.getMinBuildHeight() + 2, topY + 1);
        player.teleportTo(target, x, y, z, player.getYRot(), player.getXRot());
        source.sendSuccess(() -> Component.literal("Entered Planetos."), false);
        return 1;
    }

    private static int factionStatus(CommandSourceStack source) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        GOTFactionPlayerData data = GOTFactionPlayerData.get(player);
        Component member = data.membership() == GOTFaction.UNALIGNED
                ? Component.literal("Unaligned") : data.membership().displayName();
        source.sendSuccess(() -> Component.literal("Faction: ").append(member)
                .append(Component.literal(" | North " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.NORTH))))
                .append(Component.literal(" | Westerlands " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.WESTERLANDS))))
                .append(Component.literal(" | Riverlands " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.RIVERLANDS))))
                .append(Component.literal(" | Vale " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.ARRYN))))
                .append(Component.literal(" | Crownlands " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.CROWNLANDS))))
                .append(Component.literal(" | Dragonstone " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.DRAGONSTONE))))
                .append(Component.literal(" | Reach " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.REACH))))
                .append(Component.literal(" | Stormlands " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.STORMLANDS))))
                .append(Component.literal(" | Dorne " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.DORNE))))
                .append(Component.literal(" | Iron Islands " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.IRONBORN))))
                .append(Component.literal(" | Wildlings " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.WILDLING))))
                .append(Component.literal(" | Night's Watch " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.NIGHT_WATCH))))
                .append(Component.literal(" | White Walkers " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.WHITE_WALKER))))
                .append(Component.literal(" | Braavos " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.BRAAVOS))))
                .append(Component.literal(" | Pentos " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.PENTOS))))
                .append(Component.literal(" | Volantis " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.VOLANTIS))))
                .append(Component.literal(" | Lys " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.LYS))))
                .append(Component.literal(" | Myr " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.MYR))))
                .append(Component.literal(" | Tyrosh " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.TYROSH))))
                .append(Component.literal(" | Ghiscar " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.GHISCAR))))
                .append(Component.literal(" | Dothraki " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.DOTHRAKI))))
                .append(Component.literal(" | Yi-Ti " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.YI_TI))))
                .append(Component.literal(" | Asshai " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.ASSHAI))))
                .append(Component.literal(" | Ibben " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.IBBEN))))
                .append(Component.literal(" | Jogos Nhai " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.JOGOS_NHAI))))
                .append(Component.literal(" | Qarth " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.QARTH))))
                .append(Component.literal(" | Lorath " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.LORATH))))
                .append(Component.literal(" | Qohor " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.QOHOR))))
                .append(Component.literal(" | Lhazar " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.LHAZAR))))
                .append(Component.literal(" | Norvos " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.NORVOS))))
                .append(Component.literal(" | Mossovy " + GOTFactionService.formatAlignment(data.alignment(GOTFaction.MOSSOVY)))), false);
        return 1;
    }

    private static int joinFaction(CommandSourceStack source, String name)
            throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        GOTFaction faction = faction(source, name);
        if (faction == null) return 0;
        return GOTFactionService.joinFaction(source.getPlayerOrException(), faction) ? 1 : 0;
    }

    private static int leaveFaction(CommandSourceStack source)
            throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        return GOTFactionService.leaveFaction(source.getPlayerOrException(), true) ? 1 : 0;
    }

    private static int relation(CommandSourceStack source, String firstName, String secondName) {
        GOTFaction first = GOTFaction.byId(firstName).orElse(null);
        GOTFaction second = GOTFaction.byId(secondName).orElse(null);
        if (first == null || second == null) {
            source.sendFailure(Component.literal("Unknown faction."));
            return 0;
        }
        source.sendSuccess(() -> first.displayName().copy().append(" / ").append(second.displayName())
                .append(": ").append(first.relationTo(second).displayName()), false);
        return 1;
    }

    private static int getAlignment(CommandSourceStack source, String name)
            throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        GOTFaction faction = faction(source, name);
        if (faction == null) return 0;
        float value = GOTFactionPlayerData.get(player).alignment(faction);
        source.sendSuccess(() -> faction.displayName().copy().append(": ")
                .append(GOTFactionService.formatAlignment(value)), false);
        return Math.round(value);
    }

    private static int setAlignment(CommandSourceStack source, String name, float value, boolean add)
            throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        GOTFaction faction = faction(source, name);
        if (faction == null) return 0;
        if (add) GOTFactionService.addAlignment(player, faction, value);
        else GOTFactionService.setAlignment(player, faction, value);
        float result = GOTFactionPlayerData.get(player).alignment(faction);
        source.sendSuccess(() -> faction.displayName().copy().append(": ")
                .append(GOTFactionService.formatAlignment(result)), false);
        return Math.round(result);
    }

    private static GOTFaction faction(CommandSourceStack source, String name) {
        GOTFaction faction = GOTFaction.byId(name).orElse(null);
        if (faction == null || !faction.isPlayable()) {
            source.sendFailure(Component.literal("Unknown or unplayable faction: " + name));
            return null;
        }
        return faction;
    }
}
