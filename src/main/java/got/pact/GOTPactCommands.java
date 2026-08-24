package got.pact;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "got")
public final class GOTPactCommands {
    private GOTPactCommands() {}

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) { register(event.getDispatcher()); }

    public static void register(CommandDispatcher<CommandSourceStack> d) {
        d.register(Commands.literal("pact")
            .then(Commands.literal("create").then(Commands.argument("name", StringArgumentType.greedyString()).executes(c -> run(c.getSource(), GOTPactService.create(c.getSource().getPlayerOrException(), StringArgumentType.getString(c, "name"))))))
            .then(Commands.literal("invite").then(Commands.argument("player", EntityArgument.player()).executes(c -> run(c.getSource(), GOTPactService.invite(c.getSource().getPlayerOrException(), EntityArgument.getPlayer(c, "player"))))))
            .then(Commands.literal("accept").executes(c -> run(c.getSource(), GOTPactService.accept(c.getSource().getPlayerOrException()))))
            .then(Commands.literal("decline").executes(c -> run(c.getSource(), GOTPactService.decline(c.getSource().getPlayerOrException()))))
            .then(Commands.literal("leave").executes(c -> run(c.getSource(), GOTPactService.leave(c.getSource().getPlayerOrException()))))
            .then(Commands.literal("disband").executes(c -> run(c.getSource(), GOTPactService.disband(c.getSource().getPlayerOrException()))))
            .then(Commands.literal("rename").then(Commands.argument("name", StringArgumentType.greedyString()).executes(c -> run(c.getSource(), GOTPactService.rename(c.getSource().getPlayerOrException(), StringArgumentType.getString(c, "name"))))))
            .then(Commands.literal("admin").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("value", BoolArgumentType.bool()).executes(c -> run(c.getSource(), GOTPactService.setAdmin(c.getSource().getPlayerOrException(), EntityArgument.getPlayer(c, "player").getUUID(), BoolArgumentType.getBool(c, "value")))))))
            .then(Commands.literal("transfer").then(Commands.argument("player", EntityArgument.player()).executes(c -> run(c.getSource(), GOTPactService.transfer(c.getSource().getPlayerOrException(), EntityArgument.getPlayer(c, "player").getUUID())))))
            .then(Commands.literal("kick").then(Commands.argument("player", EntityArgument.player()).executes(c -> run(c.getSource(), GOTPactService.kick(c.getSource().getPlayerOrException(), EntityArgument.getPlayer(c, "player").getUUID())))))
            .then(Commands.literal("pvp").executes(c -> run(c.getSource(), GOTPactService.togglePvp(c.getSource().getPlayerOrException()))))
            .then(Commands.literal("hiredff").executes(c -> run(c.getSource(), GOTPactService.toggleHiredFriendlyFire(c.getSource().getPlayerOrException()))))
            .then(Commands.literal("showmap").executes(c -> run(c.getSource(), GOTPactService.toggleMap(c.getSource().getPlayerOrException()))))
            .then(Commands.literal("sharemap").then(Commands.argument("value", BoolArgumentType.bool()).executes(c -> run(c.getSource(), GOTPactService.setMapSharing(c.getSource().getPlayerOrException(), BoolArgumentType.getBool(c, "value"))))))
            .then(Commands.literal("info").executes(c -> info(c.getSource())))
            .then(Commands.literal("msg").then(Commands.argument("message", StringArgumentType.greedyString()).executes(c -> { GOTPactService.message(c.getSource().getPlayerOrException(), Component.literal(StringArgumentType.getString(c, "message"))); return 1; })))
        );
        d.register(Commands.literal("p").then(Commands.argument("message", StringArgumentType.greedyString()).executes(c -> { GOTPactService.message(c.getSource().getPlayerOrException(), Component.literal(StringArgumentType.getString(c, "message"))); return 1; })));
    }

    private static int run(CommandSourceStack source, GOTPactService.Result result) {
        if (!result.success()) { source.sendFailure(Component.literal(result.error())); return 0; }
        source.sendSuccess(() -> Component.literal("Pact updated."), false); return 1;
    }

    private static int info(CommandSourceStack source) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        GOTPact pact = GOTPactService.forPlayer(player.server, player.getUUID()).orElse(null);
        if (pact == null) { source.sendFailure(Component.literal("You are not in a Pact.")); return 0; }
        source.sendSuccess(() -> Component.literal(pact.name() + " | " + pact.size() + " members | PvP protection: " + pact.preventPvp() + " | Hired FF protection: " + pact.preventHiredFriendlyFire() + " | Map: " + pact.showMapLocations()), false);
        return 1;
    }
}
