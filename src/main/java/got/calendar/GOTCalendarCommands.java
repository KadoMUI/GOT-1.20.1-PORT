package got.calendar;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Development/admin commands for validating the ported calendar. */
@Mod.EventBusSubscriber(modid = "got", bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTCalendarCommands {
    private GOTCalendarCommands() {}

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("gotdate")
                .executes(ctx -> queryDate(ctx.getSource(), false))
                .then(Commands.literal("long")
                        .executes(ctx -> queryDate(ctx.getSource(), true)))
                .then(Commands.literal("set")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("day", IntegerArgumentType.integer())
                                .executes(ctx -> setDate(
                                        ctx.getSource(),
                                        IntegerArgumentType.getInteger(ctx, "day")
                                )))));

        dispatcher.register(Commands.literal("gottime")
                .executes(ctx -> queryTime(ctx.getSource()))
                .then(Commands.literal("query")
                        .executes(ctx -> queryTime(ctx.getSource())))
                .then(Commands.literal("set")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("ticks", LongArgumentType.longArg(0L))
                                .executes(ctx -> setTime(
                                        ctx.getSource(),
                                        LongArgumentType.getLong(ctx, "ticks")
                                ))))
                .then(Commands.literal("add")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("ticks", LongArgumentType.longArg())
                                .executes(ctx -> addTime(
                                        ctx.getSource(),
                                        LongArgumentType.getLong(ctx, "ticks")
                                ))))
                .then(Commands.literal("morning")
                        .requires(source -> source.hasPermission(2))
                        .executes(ctx -> setTimeOfDay(ctx.getSource(), 0L, "morning")))
                .then(Commands.literal("noon")
                        .requires(source -> source.hasPermission(2))
                        .executes(ctx -> setTimeOfDay(ctx.getSource(), 12_000L, "noon")))
                .then(Commands.literal("night")
                        .requires(source -> source.hasPermission(2))
                        .executes(ctx -> setTimeOfDay(ctx.getSource(), 26_000L, "night"))));
    }

    private static int queryDate(CommandSourceStack source, boolean longForm) {
        GOTWorldTimeData data = GOTWorldTimeData.get(source.getServer());
        GOTAegonCalendar.Date date = data.getDate();
        source.sendSuccess(() -> Component.literal(
                date.displayName(longForm)
                        + " | Season: " + date.season().name()
                        + " | GOT day: " + data.getCalendarDay()
        ), false);
        return data.getCalendarDay();
    }

    private static int setDate(CommandSourceStack source, int day) {
        GOTWorldTimeData data = GOTWorldTimeData.get(source.getServer());
        data.setCalendarDay(day);
        source.sendSuccess(() -> Component.literal("GOT date set to " + data.getDate().displayName(false)), true);
        return 1;
    }

    private static int queryTime(CommandSourceStack source) {
        GOTWorldTimeData data = GOTWorldTimeData.get(source.getServer());
        long timeOfDay = Math.floorMod(data.getWorldTime(), GOTWorldTimeData.DAY_LENGTH);
        source.sendSuccess(() -> Component.literal(
                "GOT world time: " + data.getWorldTime()
                        + " | time-of-day: " + timeOfDay + "/" + GOTWorldTimeData.DAY_LENGTH
                        + " | total server ticks: " + data.getTotalTime()
        ), false);
        return (int) Math.min(Integer.MAX_VALUE, data.getWorldTime());
    }

    private static int setTime(CommandSourceStack source, long ticks) {
        GOTWorldTimeData data = GOTWorldTimeData.get(source.getServer());
        data.setWorldTime(ticks);
        source.sendSuccess(() -> Component.literal("GOT world time set to " + data.getWorldTime()), true);
        return 1;
    }

    private static int addTime(CommandSourceStack source, long ticks) {
        GOTWorldTimeData data = GOTWorldTimeData.get(source.getServer());
        data.addWorldTime(ticks);
        source.sendSuccess(() -> Component.literal("GOT world time is now " + data.getWorldTime()), true);
        return 1;
    }

    private static int setTimeOfDay(CommandSourceStack source, long gotTicks, String label) {
        GOTWorldTimeData data = GOTWorldTimeData.get(source.getServer());
        data.setTimeOfDay(gotTicks);
        source.sendSuccess(() -> Component.literal("GOT time set to " + label), true);
        return 1;
    }
}
