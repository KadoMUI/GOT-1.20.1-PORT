package got.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import got.faction.GOTFaction;
import got.faction.GOTFactionPlayerData;
import got.player.GOTPlayerTitleData;
import got.calendar.GOTCalendarApi;
import got.common.fasttravel.GOTFastTravelManager;
import got.common.world.map.GOTWaypoint;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public final class GOTLegacyCommands {
    private GOTLegacyCommands() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("got")
            .then(Commands.literal("alignment")
                .then(Commands.argument("faction", StringArgumentType.word())
                    .executes(ctx -> showAlignment(
                            ctx.getSource().getPlayerOrException(),
                            StringArgumentType.getString(ctx, "faction")))
                    .then(Commands.argument("value", FloatArgumentType.floatArg())
                        .requires(src -> src.hasPermission(2))
                        .executes(ctx -> setAlignment(
                            ctx.getSource(),
                            ctx.getSource().getPlayerOrException(),
                            StringArgumentType.getString(ctx, "faction"),
                            FloatArgumentType.getFloat(ctx, "value")))
                        .then(Commands.argument("player", EntityArgument.player())
                            .requires(src -> src.hasPermission(2))
                            .executes(ctx -> setAlignment(
                                ctx.getSource(),
                                EntityArgument.getPlayer(ctx, "player"),
                                StringArgumentType.getString(ctx, "faction"),
                                FloatArgumentType.getFloat(ctx, "value")))))))
            .then(Commands.literal("title")
                .then(Commands.literal("clear")
                    .executes(ctx -> {
                        ServerPlayer player = ctx.getSource().getPlayerOrException();
                        GOTPlayerTitleData.clear(player);
                        ctx.getSource().sendSuccess(() -> Component.translatable("commands.got.title.cleared"), false);
                        return 1;
                    }))
                .then(Commands.literal("color")
                    .then(Commands.argument("color", StringArgumentType.word())
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            String id = GOTPlayerTitleData.selectedId(player);
                            ChatFormatting color = ChatFormatting.getByName(
                                    StringArgumentType.getString(ctx, "color"));
                            if (color == null || !color.isColor()) {
                                ctx.getSource().sendFailure(Component.translatable("commands.got.title.bad_color"));
                                return 0;
                            }
                            GOTPlayerTitleData.select(player, id, color);
                            ctx.getSource().sendSuccess(
                                    () -> Component.translatable("commands.got.title.color", color.getName()), false);
                            return 1;
                        }))))
            .then(Commands.literal("waypoint")
                .then(Commands.argument("waypoint", StringArgumentType.word())
                    .executes(ctx -> {
                        ServerPlayer player = ctx.getSource().getPlayerOrException();
                        String raw = StringArgumentType.getString(ctx, "waypoint");
                        GOTWaypoint waypoint = GOTWaypoint.waypointForName(raw);
                        if (waypoint == null) {
                            ctx.getSource().sendFailure(Component.translatable("commands.got.waypoint.unknown", raw));
                            return 0;
                        }
                        GOTFastTravelManager.fastTravel(player, waypoint);
                        return 1;
                    })))
            .then(Commands.literal("calendar")
                .executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                    GOTCalendarApi.Snapshot calendar = GOTCalendarApi.snapshot(player.getServer());
                    ctx.getSource().sendSuccess(
                            () -> Component.translatable("commands.got.calendar.current", calendar.displayDate()), false);
                    return 1;
                }))
        );
    }

    private static int showAlignment(ServerPlayer player, String factionName) {
        GOTFaction faction = GOTFaction.byId(factionName).orElse(null);
        if (faction == null) {
            player.sendSystemMessage(Component.translatable("commands.got.alignment.unknown", factionName));
            return 0;
        }
        float value = GOTFactionPlayerData.get(player).alignment(faction);
        player.sendSystemMessage(Component.translatable(
                "commands.got.alignment.current", faction.displayName(), value));
        return 1;
    }

    private static int setAlignment(CommandSourceStack source, ServerPlayer player,
                                    String factionName, float value) {
        GOTFaction faction = GOTFaction.byId(factionName).orElse(null);
        if (faction == null) {
            source.sendFailure(Component.translatable("commands.got.alignment.unknown", factionName));
            return 0;
        }
        GOTFactionPlayerData.get(player).setAlignment(faction, value);
        source.sendSuccess(() -> Component.translatable(
                "commands.got.alignment.set", player.getDisplayName(), faction.displayName(), value), true);
        return 1;
    }
}
