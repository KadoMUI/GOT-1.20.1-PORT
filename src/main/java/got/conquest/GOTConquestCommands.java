package got.conquest;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import got.GOTMod;
import got.common.world.map.GOTWaypoint;
import got.conquest.pact.GOTCanonicalPactService;
import got.conquest.pact.GOTCanonicalPactState;
import got.conquest.pact.GOTCanonicalPacts;
import got.conquest.pact.GOTLegendaryPactMembership;
import got.conquest.diplomacy.GOTCommunicationPolicy;
import got.conquest.diplomacy.GOTDiplomaticRelationState;
import got.conquest.diplomacy.GOTDiplomaticStatus;
import got.conquest.economy.GOTCanonicalEconomySeeds;
import got.conquest.economy.GOTPactEconomyService;
import got.conquest.economy.GOTPactTreasuryState;
import got.pact.GOTPactSavedData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/** Admin/debug command surface for Conquest persistence, Pact politics, and diplomacy foundations. */
public final class GOTConquestCommands {
    private GOTConquestCommands() {}

    public static LiteralArgumentBuilder<CommandSourceStack> build() {
        return Commands.literal("conquest")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("schema")
                        .executes(ctx -> schema(ctx.getSource())))
                .then(Commands.literal("pactid")
                        .executes(ctx -> pactId(ctx.getSource())))
                .then(Commands.literal("canonical")
                        .then(Commands.literal("list")
                                .executes(ctx -> listCanonicalPacts(ctx.getSource())))
                        .then(Commands.literal("info")
                                .then(Commands.argument("pact", StringArgumentType.word())
                                        .suggests((ctx, builder) -> net.minecraft.commands.SharedSuggestionProvider.suggest(
                                                GOTCanonicalPacts.definitions().stream().map(def -> def.key().getPath()), builder))
                                        .executes(ctx -> canonicalPactInfo(ctx.getSource(),
                                                StringArgumentType.getString(ctx, "pact"))))))
                .then(Commands.literal("treasury")
                        .then(Commands.literal("list")
                                .executes(ctx -> listTreasuries(ctx.getSource())))
                        .then(Commands.literal("info")
                                .then(Commands.argument("pact", StringArgumentType.word())
                                        .executes(ctx -> treasuryInfo(ctx.getSource(),
                                                StringArgumentType.getString(ctx, "pact")))))
                        .then(Commands.literal("set")
                                .then(Commands.argument("pact", StringArgumentType.word())
                                        .then(Commands.argument("value", LongArgumentType.longArg(0L))
                                                .executes(ctx -> setTreasury(ctx.getSource(),
                                                        StringArgumentType.getString(ctx, "pact"),
                                                        LongArgumentType.getLong(ctx, "value"))))))
                        .then(Commands.literal("add")
                                .then(Commands.argument("pact", StringArgumentType.word())
                                        .then(Commands.argument("value", LongArgumentType.longArg(1L))
                                                .executes(ctx -> addTreasury(ctx.getSource(),
                                                        StringArgumentType.getString(ctx, "pact"),
                                                        LongArgumentType.getLong(ctx, "value"))))))
                        .then(Commands.literal("spend")
                                .then(Commands.argument("pact", StringArgumentType.word())
                                        .then(Commands.argument("value", LongArgumentType.longArg(1L))
                                                .executes(ctx -> spendTreasury(ctx.getSource(),
                                                        StringArgumentType.getString(ctx, "pact"),
                                                        LongArgumentType.getLong(ctx, "value"))))))
                        .then(Commands.literal("process")
                                .executes(ctx -> processTreasuryIncome(ctx.getSource()))))
                .then(Commands.literal("diplomacy")
                        .then(Commands.literal("list")
                                .executes(ctx -> listDiplomacy(ctx.getSource())))
                        .then(Commands.literal("info")
                                .then(Commands.argument("pactA", StringArgumentType.word())
                                        .then(Commands.argument("pactB", StringArgumentType.word())
                                                .executes(ctx -> diplomacyInfo(ctx.getSource(),
                                                        StringArgumentType.getString(ctx, "pactA"),
                                                        StringArgumentType.getString(ctx, "pactB"))))))
                        .then(Commands.literal("opinion")
                                .then(Commands.argument("from", StringArgumentType.word())
                                        .then(Commands.argument("to", StringArgumentType.word())
                                                .then(Commands.argument("value", IntegerArgumentType.integer(-1000, 1000))
                                                        .executes(ctx -> setDiplomaticOpinion(ctx.getSource(),
                                                                StringArgumentType.getString(ctx, "from"),
                                                                StringArgumentType.getString(ctx, "to"),
                                                                IntegerArgumentType.getInteger(ctx, "value")))))))
                        .then(Commands.literal("trust")
                                .then(Commands.argument("from", StringArgumentType.word())
                                        .then(Commands.argument("to", StringArgumentType.word())
                                                .then(Commands.argument("value", IntegerArgumentType.integer(0, 1000))
                                                        .executes(ctx -> setDiplomaticTrust(ctx.getSource(),
                                                                StringArgumentType.getString(ctx, "from"),
                                                                StringArgumentType.getString(ctx, "to"),
                                                                IntegerArgumentType.getInteger(ctx, "value")))))))
                        .then(Commands.literal("fear")
                                .then(Commands.argument("from", StringArgumentType.word())
                                        .then(Commands.argument("to", StringArgumentType.word())
                                                .then(Commands.argument("value", IntegerArgumentType.integer(0, 1000))
                                                        .executes(ctx -> setDiplomaticFear(ctx.getSource(),
                                                                StringArgumentType.getString(ctx, "from"),
                                                                StringArgumentType.getString(ctx, "to"),
                                                                IntegerArgumentType.getInteger(ctx, "value")))))))
                        .then(Commands.literal("communication")
                                .then(Commands.argument("from", StringArgumentType.word())
                                        .then(Commands.argument("to", StringArgumentType.word())
                                                .then(Commands.argument("policy", StringArgumentType.word())
                                                        .executes(ctx -> setDiplomaticCommunication(ctx.getSource(),
                                                                StringArgumentType.getString(ctx, "from"),
                                                                StringArgumentType.getString(ctx, "to"),
                                                                StringArgumentType.getString(ctx, "policy")))))))
                        .then(Commands.literal("status")
                                .then(Commands.argument("pactA", StringArgumentType.word())
                                        .then(Commands.argument("pactB", StringArgumentType.word())
                                                .then(Commands.argument("status", StringArgumentType.word())
                                                        .executes(ctx -> setDiplomaticStatusCommand(ctx.getSource(),
                                                                StringArgumentType.getString(ctx, "pactA"),
                                                                StringArgumentType.getString(ctx, "pactB"),
                                                                StringArgumentType.getString(ctx, "status")))))))
                        .then(Commands.literal("trade")
                                .then(Commands.argument("pactA", StringArgumentType.word())
                                        .then(Commands.argument("pactB", StringArgumentType.word())
                                                .then(Commands.argument("enabled", BoolArgumentType.bool())
                                                        .executes(ctx -> setDiplomaticTrade(ctx.getSource(),
                                                                StringArgumentType.getString(ctx, "pactA"),
                                                                StringArgumentType.getString(ctx, "pactB"),
                                                                BoolArgumentType.getBool(ctx, "enabled")))))))
                        .then(Commands.literal("nonaggression")
                                .then(Commands.argument("pactA", StringArgumentType.word())
                                        .then(Commands.argument("pactB", StringArgumentType.word())
                                                .then(Commands.argument("enabled", BoolArgumentType.bool())
                                                        .executes(ctx -> setDiplomaticNonAggression(ctx.getSource(),
                                                                StringArgumentType.getString(ctx, "pactA"),
                                                                StringArgumentType.getString(ctx, "pactB"),
                                                                BoolArgumentType.getBool(ctx, "enabled")))))))
                        .then(Commands.literal("truce")
                                .then(Commands.argument("pactA", StringArgumentType.word())
                                        .then(Commands.argument("pactB", StringArgumentType.word())
                                                .then(Commands.argument("ticks", IntegerArgumentType.integer(0))
                                                        .executes(ctx -> setDiplomaticTruce(ctx.getSource(),
                                                                StringArgumentType.getString(ctx, "pactA"),
                                                                StringArgumentType.getString(ctx, "pactB"),
                                                                IntegerArgumentType.getInteger(ctx, "ticks"))))))))
                .then(Commands.literal("legendary")
                        .then(Commands.literal("list")
                                .executes(ctx -> listLegendaryMemberships(ctx.getSource())))
                        .then(Commands.literal("info")
                                .then(Commands.argument("role", StringArgumentType.word())
                                        .suggests((ctx, builder) -> net.minecraft.commands.SharedSuggestionProvider.suggest(
                                                data(ctx.getSource()).allLegendaryMemberships().stream().map(GOTLegendaryPactMembership::roleId), builder))
                                        .executes(ctx -> legendaryInfo(ctx.getSource(), StringArgumentType.getString(ctx, "role")))))
                        .then(Commands.literal("recruit")
                                .then(Commands.argument("role", StringArgumentType.word())
                                        .suggests((ctx, builder) -> net.minecraft.commands.SharedSuggestionProvider.suggest(
                                                data(ctx.getSource()).allLegendaryMemberships().stream().map(GOTLegendaryPactMembership::roleId), builder))
                                        .then(Commands.argument("pact", StringArgumentType.word())
                                                .executes(ctx -> forceRecruitLegendary(ctx.getSource(),
                                                        StringArgumentType.getString(ctx, "role"),
                                                        StringArgumentType.getString(ctx, "pact"))))))
                        .then(Commands.literal("release")
                                .then(Commands.argument("role", StringArgumentType.word())
                                        .suggests((ctx, builder) -> net.minecraft.commands.SharedSuggestionProvider.suggest(
                                                data(ctx.getSource()).allLegendaryMemberships().stream().map(GOTLegendaryPactMembership::roleId), builder))
                                        .executes(ctx -> releaseLegendary(ctx.getSource(), StringArgumentType.getString(ctx, "role")))))
                        .then(Commands.literal("restore")
                                .then(Commands.argument("role", StringArgumentType.word())
                                        .suggests((ctx, builder) -> net.minecraft.commands.SharedSuggestionProvider.suggest(
                                                data(ctx.getSource()).allLegendaryMemberships().stream().map(GOTLegendaryPactMembership::roleId), builder))
                                        .executes(ctx -> restoreLegendary(ctx.getSource(), StringArgumentType.getString(ctx, "role"))))))
                .then(Commands.literal("info")
                        .then(Commands.argument("waypoint", StringArgumentType.word())
                                .executes(ctx -> info(ctx.getSource(), StringArgumentType.getString(ctx, "waypoint")))))
                .then(Commands.literal("dump")
                        .executes(ctx -> dump(ctx.getSource())))
                .then(Commands.literal("setvalue")
                        .then(Commands.argument("waypoint", StringArgumentType.word())
                                .then(Commands.argument("value", IntegerArgumentType.integer(0))
                                        .executes(ctx -> setValue(ctx.getSource(),
                                                StringArgumentType.getString(ctx, "waypoint"),
                                                IntegerArgumentType.getInteger(ctx, "value"))))))
                .then(Commands.literal("setanchor")
                        .then(Commands.argument("waypoint", StringArgumentType.word())
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(ctx -> setAnchor(ctx.getSource(),
                                                StringArgumentType.getString(ctx, "waypoint"),
                                                BoolArgumentType.getBool(ctx, "value"))))))
                .then(Commands.literal("setstatus")
                        .then(Commands.argument("waypoint", StringArgumentType.word())
                                .then(Commands.argument("status", StringArgumentType.word())
                                        .executes(ctx -> setStatus(ctx.getSource(),
                                                StringArgumentType.getString(ctx, "waypoint"),
                                                StringArgumentType.getString(ctx, "status"))))))
                .then(Commands.literal("controller")
                        .then(Commands.argument("waypoint", StringArgumentType.word())
                                .then(Commands.argument("pact", StringArgumentType.word())
                                        .executes(ctx -> setController(ctx.getSource(),
                                                StringArgumentType.getString(ctx, "waypoint"),
                                                StringArgumentType.getString(ctx, "pact"))))))
                .then(Commands.literal("claimant")
                        .then(Commands.argument("waypoint", StringArgumentType.word())
                                .then(Commands.argument("pact", StringArgumentType.word())
                                        .executes(ctx -> setClaimant(ctx.getSource(),
                                                StringArgumentType.getString(ctx, "waypoint"),
                                                StringArgumentType.getString(ctx, "pact"))))))
                .then(Commands.literal("setscore")
                        .then(Commands.argument("waypoint", StringArgumentType.word())
                                .then(Commands.argument("pact", StringArgumentType.word())
                                        .then(Commands.argument("score", IntegerArgumentType.integer())
                                                .executes(ctx -> setScore(ctx.getSource(),
                                                        StringArgumentType.getString(ctx, "waypoint"),
                                                        StringArgumentType.getString(ctx, "pact"),
                                                        IntegerArgumentType.getInteger(ctx, "score")))))))
                .then(Commands.literal("clearscore")
                        .then(Commands.argument("waypoint", StringArgumentType.word())
                                .then(Commands.argument("pact", StringArgumentType.word())
                                        .executes(ctx -> clearScore(ctx.getSource(),
                                                StringArgumentType.getString(ctx, "waypoint"),
                                                StringArgumentType.getString(ctx, "pact"))))))
                .then(Commands.literal("setcontrol")
                        .then(Commands.argument("waypoint", StringArgumentType.word())
                                .then(Commands.argument("pact", StringArgumentType.word())
                                        .then(Commands.argument("score", IntegerArgumentType.integer())
                                                .executes(ctx -> setControl(ctx.getSource(),
                                                        StringArgumentType.getString(ctx, "waypoint"),
                                                        StringArgumentType.getString(ctx, "pact"),
                                                        IntegerArgumentType.getInteger(ctx, "score")))))))
                .then(Commands.literal("reset")
                        .then(Commands.argument("waypoint", StringArgumentType.word())
                                .executes(ctx -> reset(ctx.getSource(), StringArgumentType.getString(ctx, "waypoint")))))
                .then(Commands.literal("resetall")
                        .executes(ctx -> resetAll(ctx.getSource())));
    }

    private static int schema(CommandSourceStack source) {
        GOTConquestSavedData data = GOTConquestSavedData.get(source.getServer());
        source.sendSuccess(() -> Component.literal("Conquest schema=" + data.schemaVersion()
                + ", loadedSchema=" + data.loadedSchemaVersion()
                + ", records=" + data.allWaypoints().size()
                + ", canonicalPacts=" + data.allCanonicalPacts().size()
                + ", legendaryMemberships=" + data.allLegendaryMemberships().size()
                + ", diplomaticRelations=" + data.allDiplomaticRelations().size()
                + ", revision=" + data.revision()), false);
        return data.allWaypoints().size();
    }


    private static int pactId(CommandSourceStack source) {
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("This command requires a player."));
            return 0;
        }
        return GOTPactSavedData.get(source.getServer()).pactFor(player.getUUID())
                .map(pact -> {
                    source.sendSuccess(() -> Component.literal("Your Pact: " + pact.name() + " | " + pact.id()), false);
                    return 1;
                })
                .orElseGet(() -> {
                    source.sendFailure(Component.literal("You are not in a Pact."));
                    return 0;
                });
    }

    private static int listCanonicalPacts(CommandSourceStack source) {
        var pacts = GOTCanonicalPactService.all(source.getServer()).stream()
                .sorted(Comparator.comparing(pact -> pact.key().toString()))
                .toList();
        source.sendSuccess(() -> Component.literal("Canonical NPC Pacts: " + pacts.size()), false);
        for (GOTCanonicalPactState pact : pacts) {
            source.sendSuccess(() -> Component.literal(" - " + pact.key().getPath() + " | " + pact.name()
                    + " | leader=" + pact.leaderRoleId()
                    + " | members=" + pact.members().size()
                    + " | id=" + pact.id()), false);
        }
        return pacts.size();
    }

    private static int canonicalPactInfo(CommandSourceStack source, String rawPact) {
        GOTCanonicalPactState pact = GOTCanonicalPactService.byKey(source.getServer(), rawPact).orElse(null);
        if (pact == null) {
            source.sendFailure(Component.literal("Unknown canonical Pact: " + rawPact));
            return 0;
        }
        String members = pact.members().stream()
                .map(member -> member.displayName() + "[" + member.roleId() + "/" + member.faction().id() + "]")
                .collect(Collectors.joining(", "));
        long balance = GOTPactEconomyService.balance(source.getServer(), pact.id());
        long dailyIncome = GOTCanonicalEconomySeeds.byPactId(pact.id())
                .map(GOTCanonicalEconomySeeds.Seed::incomePerMinecraftDay).orElse(0L);
        source.sendSuccess(() -> Component.literal("[Canonical Pact] " + pact.name()
                + " key=" + pact.key()
                + " id=" + pact.id()
                + " active=" + pact.active()
                + " leader=" + pact.leaderRoleId()
                + " treasury=" + balance
                + " baseIncomePerDay=" + dailyIncome
                + " definitionVersion=" + pact.definitionVersion()
                + " members={" + members + "}"), false);
        return 1;
    }

    private static int listTreasuries(CommandSourceStack source) {
        var treasuries = data(source).allTreasuries().stream()
                .sorted(Comparator.comparing(treasury -> shortPact(source.getServer(), treasury.pactId())))
                .toList();
        source.sendSuccess(() -> Component.literal("Pact treasuries: " + treasuries.size()), false);
        for (GOTPactTreasuryState treasury : treasuries) {
            long daily = GOTCanonicalEconomySeeds.byPactId(treasury.pactId())
                    .map(GOTCanonicalEconomySeeds.Seed::incomePerMinecraftDay).orElse(0L);
            source.sendSuccess(() -> Component.literal(" - " + shortPact(source.getServer(), treasury.pactId())
                    + " balance=" + treasury.balance()
                    + " seeded=" + treasury.seededCapital()
                    + " playerDeposits=" + treasury.totalPlayerDeposits()
                    + " generated=" + treasury.totalGeneratedIncome()
                    + " spent=" + treasury.totalSpent()
                    + " baseIncomePerDay=" + daily
                    + " incomeClock=" + treasury.lastIncomeGameTime()), false);
        }
        return treasuries.size();
    }

    private static int treasuryInfo(CommandSourceStack source, String rawPact) {
        Optional<UUID> pact = pact(source, rawPact, false);
        if (pact == null || pact.isEmpty()) return 0;
        GOTPactTreasuryState treasury = data(source).treasury(pact.get(), gameTime(source));
        long daily = GOTCanonicalEconomySeeds.byPactId(pact.get())
                .map(GOTCanonicalEconomySeeds.Seed::incomePerMinecraftDay).orElse(0L);
        source.sendSuccess(() -> Component.literal("[Treasury] " + shortPact(source.getServer(), pact.get())
                + " balance=" + treasury.balance()
                + " seeded=" + treasury.seededCapital()
                + " playerDeposits=" + treasury.totalPlayerDeposits()
                + " generated=" + treasury.totalGeneratedIncome()
                + " spent=" + treasury.totalSpent()
                + " baseIncomePerDay=" + daily
                + " lastIncome=" + treasury.lastIncomeGameTime()
                + " updated=" + treasury.lastUpdatedGameTime()), false);
        return 1;
    }

    private static int setTreasury(CommandSourceStack source, String rawPact, long value) {
        Optional<UUID> pact = pact(source, rawPact, false);
        if (pact == null || pact.isEmpty()) return 0;
        data(source).setTreasuryBalance(pact.get(), value, gameTime(source));
        source.sendSuccess(() -> Component.literal("Set treasury for "
                + shortPact(source.getServer(), pact.get()) + " to " + value + "."), true);
        return 1;
    }

    private static int addTreasury(CommandSourceStack source, String rawPact, long value) {
        Optional<UUID> pact = pact(source, rawPact, false);
        if (pact == null || pact.isEmpty()) return 0;
        GOTConquestSavedData data = data(source);
        long current = data.treasury(pact.get(), gameTime(source)).balance();
        long updated = Long.MAX_VALUE - current < value ? Long.MAX_VALUE : current + value;
        data.setTreasuryBalance(pact.get(), updated, gameTime(source));
        source.sendSuccess(() -> Component.literal("Added " + value + " to "
                + shortPact(source.getServer(), pact.get()) + " treasury. Balance=" + updated + "."), true);
        return 1;
    }

    private static int spendTreasury(CommandSourceStack source, String rawPact, long value) {
        Optional<UUID> pact = pact(source, rawPact, false);
        if (pact == null || pact.isEmpty()) return 0;
        boolean spent = data(source).spendTreasury(pact.get(), value, gameTime(source));
        if (!spent) {
            long balance = data(source).treasury(pact.get()).map(GOTPactTreasuryState::balance).orElse(0L);
            source.sendFailure(Component.literal("Treasury only contains " + balance + "; cannot spend " + value + "."));
            return 0;
        }
        long balance = data(source).treasury(pact.get()).map(GOTPactTreasuryState::balance).orElse(0L);
        source.sendSuccess(() -> Component.literal("Spent " + value + " from "
                + shortPact(source.getServer(), pact.get()) + ". Balance=" + balance + "."), true);
        return 1;
    }

    private static int processTreasuryIncome(CommandSourceStack source) {
        long generated = GOTPactEconomyService.processCanonicalIncome(source.getServer());
        source.sendSuccess(() -> Component.literal("Processed canonical Pact income; generated " + generated
                + " total coin value currently due."), true);
        return 1;
    }

    private static int listDiplomacy(CommandSourceStack source) {
        var relations = data(source).allDiplomaticRelations().stream()
                .sorted(Comparator.comparing(relation -> shortPact(source.getServer(), relation.pactA())
                        + shortPact(source.getServer(), relation.pactB())))
                .toList();
        source.sendSuccess(() -> Component.literal("Diplomatic relations: " + relations.size()), false);
        for (GOTDiplomaticRelationState relation : relations) {
            String a = shortPact(source.getServer(), relation.pactA());
            String b = shortPact(source.getServer(), relation.pactB());
            source.sendSuccess(() -> Component.literal(" - " + a + " <-> " + b
                    + " status=" + relation.status()
                    + " opinions=" + relation.opinion(relation.pactA(), relation.pactB())
                    + "/" + relation.opinion(relation.pactB(), relation.pactA())
                    + " trade=" + relation.tradeAgreement()
                    + " NAP=" + relation.nonAggressionPact()), false);
        }
        return relations.size();
    }

    private static int diplomacyInfo(CommandSourceStack source, String rawA, String rawB) {
        UUID[] pair = diplomaticPair(source, rawA, rawB);
        if (pair == null) return 0;
        GOTDiplomaticRelationState relation = data(source).diplomacy(pair[0], pair[1]);
        String a = shortPact(source.getServer(), pair[0]);
        String b = shortPact(source.getServer(), pair[1]);
        long now = gameTime(source);
        source.sendSuccess(() -> Component.literal("[Diplomacy] " + a + " <-> " + b
                + " status=" + relation.status()
                + " trade=" + relation.tradeAgreement()
                + " NAP=" + relation.nonAggressionPact()
                + " truceUntil=" + relation.truceUntilGameTime()
                + (relation.truceUntilGameTime() > now ? " ACTIVE" : "")
                + " | " + a + " -> " + b
                + " opinion=" + relation.opinion(pair[0], pair[1]) + "(" + relation.attitude(pair[0], pair[1]) + ")"
                + " trust=" + relation.trust(pair[0], pair[1])
                + " fear=" + relation.fear(pair[0], pair[1])
                + " communication=" + relation.communication(pair[0], pair[1])
                + " | " + b + " -> " + a
                + " opinion=" + relation.opinion(pair[1], pair[0]) + "(" + relation.attitude(pair[1], pair[0]) + ")"
                + " trust=" + relation.trust(pair[1], pair[0])
                + " fear=" + relation.fear(pair[1], pair[0])
                + " communication=" + relation.communication(pair[1], pair[0])
                + " updated=" + relation.lastUpdatedGameTime()), false);
        return 1;
    }

    private static int setDiplomaticOpinion(CommandSourceStack source, String rawFrom, String rawTo, int value) {
        UUID[] pair = diplomaticPair(source, rawFrom, rawTo);
        if (pair == null) return 0;
        data(source).setDiplomaticOpinion(pair[0], pair[1], value, gameTime(source));
        source.sendSuccess(() -> Component.literal("Set opinion " + shortPact(source.getServer(), pair[0])
                + " -> " + shortPact(source.getServer(), pair[1]) + " = " + value + "."), true);
        return 1;
    }

    private static int setDiplomaticTrust(CommandSourceStack source, String rawFrom, String rawTo, int value) {
        UUID[] pair = diplomaticPair(source, rawFrom, rawTo);
        if (pair == null) return 0;
        data(source).setDiplomaticTrust(pair[0], pair[1], value, gameTime(source));
        source.sendSuccess(() -> Component.literal("Set trust " + shortPact(source.getServer(), pair[0])
                + " -> " + shortPact(source.getServer(), pair[1]) + " = " + value + "."), true);
        return 1;
    }

    private static int setDiplomaticFear(CommandSourceStack source, String rawFrom, String rawTo, int value) {
        UUID[] pair = diplomaticPair(source, rawFrom, rawTo);
        if (pair == null) return 0;
        data(source).setDiplomaticFear(pair[0], pair[1], value, gameTime(source));
        source.sendSuccess(() -> Component.literal("Set fear " + shortPact(source.getServer(), pair[0])
                + " -> " + shortPact(source.getServer(), pair[1]) + " = " + value + "."), true);
        return 1;
    }

    private static int setDiplomaticCommunication(CommandSourceStack source, String rawFrom, String rawTo, String rawPolicy) {
        UUID[] pair = diplomaticPair(source, rawFrom, rawTo);
        if (pair == null) return 0;
        GOTCommunicationPolicy policy = GOTCommunicationPolicy.byName(rawPolicy);
        if (policy == null) {
            source.sendFailure(Component.literal("Unknown communication policy '" + rawPolicy + "'. Use open, guarded, or closed."));
            return 0;
        }
        data(source).setDiplomaticCommunication(pair[0], pair[1], policy, gameTime(source));
        source.sendSuccess(() -> Component.literal("Set communication " + shortPact(source.getServer(), pair[0])
                + " -> " + shortPact(source.getServer(), pair[1]) + " = " + policy + "."), true);
        return 1;
    }

    private static int setDiplomaticStatusCommand(CommandSourceStack source, String rawA, String rawB, String rawStatus) {
        UUID[] pair = diplomaticPair(source, rawA, rawB);
        if (pair == null) return 0;
        GOTDiplomaticStatus status = GOTDiplomaticStatus.byName(rawStatus);
        if (status == null) {
            source.sendFailure(Component.literal("Unknown diplomatic status '" + rawStatus + "'. Use peace, allied, or at_war."));
            return 0;
        }
        data(source).setDiplomaticStatus(pair[0], pair[1], status, gameTime(source));
        source.sendSuccess(() -> Component.literal("Set " + shortPact(source.getServer(), pair[0]) + " <-> "
                + shortPact(source.getServer(), pair[1]) + " status=" + status + "."), true);
        return 1;
    }

    private static int setDiplomaticTrade(CommandSourceStack source, String rawA, String rawB, boolean enabled) {
        UUID[] pair = diplomaticPair(source, rawA, rawB);
        if (pair == null) return 0;
        try {
            data(source).setTradeAgreement(pair[0], pair[1], enabled, gameTime(source));
        } catch (IllegalStateException ex) {
            source.sendFailure(Component.literal(ex.getMessage()));
            return 0;
        }
        source.sendSuccess(() -> Component.literal("Set trade agreement " + shortPact(source.getServer(), pair[0])
                + " <-> " + shortPact(source.getServer(), pair[1]) + " = " + enabled + "."), true);
        return 1;
    }

    private static int setDiplomaticNonAggression(CommandSourceStack source, String rawA, String rawB, boolean enabled) {
        UUID[] pair = diplomaticPair(source, rawA, rawB);
        if (pair == null) return 0;
        try {
            data(source).setNonAggressionPact(pair[0], pair[1], enabled, gameTime(source));
        } catch (IllegalStateException ex) {
            source.sendFailure(Component.literal(ex.getMessage()));
            return 0;
        }
        source.sendSuccess(() -> Component.literal("Set non-aggression pact " + shortPact(source.getServer(), pair[0])
                + " <-> " + shortPact(source.getServer(), pair[1]) + " = " + enabled + "."), true);
        return 1;
    }

    private static int setDiplomaticTruce(CommandSourceStack source, String rawA, String rawB, int ticks) {
        UUID[] pair = diplomaticPair(source, rawA, rawB);
        if (pair == null) return 0;
        long now = gameTime(source);
        long until = ticks <= 0 ? 0L : now + (long) ticks;
        data(source).setTruceUntil(pair[0], pair[1], until, now);
        source.sendSuccess(() -> Component.literal("Set truce " + shortPact(source.getServer(), pair[0])
                + " <-> " + shortPact(source.getServer(), pair[1]) + " until gameTime=" + until + "."), true);
        return 1;
    }

    private static UUID[] diplomaticPair(CommandSourceStack source, String rawA, String rawB) {
        Optional<UUID> first = pact(source, rawA, false);
        if (first == null || first.isEmpty()) return null;
        Optional<UUID> second = pact(source, rawB, false);
        if (second == null || second.isEmpty()) return null;
        if (first.get().equals(second.get())) {
            source.sendFailure(Component.literal("A Pact cannot have a diplomatic relationship with itself."));
            return null;
        }
        return new UUID[]{first.get(), second.get()};
    }

    private static int listLegendaryMemberships(CommandSourceStack source) {
        var memberships = data(source).allLegendaryMemberships().stream()
                .sorted(Comparator.comparing(GOTLegendaryPactMembership::roleId))
                .toList();
        source.sendSuccess(() -> Component.literal("Legendary Pact memberships: " + memberships.size()), false);
        for (GOTLegendaryPactMembership membership : memberships) {
            source.sendSuccess(() -> Component.literal(" - " + membership.displayName() + " [" + membership.roleId() + "]"
                    + " faction=" + membership.faction().id()
                    + " pact=" + membership.currentPactId().map(id -> shortPact(source.getServer(), id)).orElse("none")
                    + " origin=" + membership.originCanonicalPact().map(Object::toString).orElse("none")
                    + (membership.isRecruited() ? " RECRUITED" : "")), false);
        }
        return memberships.size();
    }

    private static int legendaryInfo(CommandSourceStack source, String rawRole) {
        GOTLegendaryPactMembership membership = data(source).legendaryMembership(rawRole).orElse(null);
        if (membership == null) {
            source.sendFailure(Component.literal("Unknown Legendary role: " + rawRole));
            return 0;
        }
        source.sendSuccess(() -> Component.literal("[Legendary Pact] " + membership.displayName()
                + " role=" + membership.roleId()
                + " faction=" + membership.faction().id()
                + " currentPact=" + membership.currentPactId().map(id -> shortPact(source.getServer(), id)).orElse("none")
                + " origin=" + membership.originCanonicalPact().map(Object::toString).orElse("none")
                + " recruitedBy=" + membership.recruitedBy().map(UUID::toString).orElse("none")
                + " recruitedAt=" + membership.recruitedAtGameTime()
                + " updated=" + membership.lastUpdatedGameTime()), false);
        return 1;
    }

    private static int forceRecruitLegendary(CommandSourceStack source, String rawRole, String rawPact) {
        GOTLegendaryPactMembership membership = data(source).legendaryMembership(rawRole).orElse(null);
        if (membership == null) {
            source.sendFailure(Component.literal("Unknown Legendary role: " + rawRole));
            return 0;
        }
        Optional<UUID> pact = pact(source, rawPact, false);
        if (pact == null || pact.isEmpty()) return 0;
        if (GOTPactSavedData.get(source.getServer()).pact(pact.get()).isEmpty()) {
            source.sendFailure(Component.literal("Legendary recruitment debug command only targets player-run Pacts."));
            return 0;
        }
        UUID actor = source.getEntity() instanceof ServerPlayer player ? player.getUUID() : null;
        data(source).recruitLegendary(membership.roleId(), membership.displayName(), membership.faction(),
                pact.get(), actor, gameTime(source));
        source.sendSuccess(() -> Component.literal("Moved " + membership.displayName() + " into "
                + shortPact(source.getServer(), pact.get()) + "."), true);
        return 1;
    }

    private static int releaseLegendary(CommandSourceStack source, String rawRole) {
        GOTLegendaryPactMembership membership = data(source).legendaryMembership(rawRole).orElse(null);
        if (membership == null) {
            source.sendFailure(Component.literal("Unknown Legendary role: " + rawRole));
            return 0;
        }
        if (!data(source).releaseLegendary(membership.roleId(), gameTime(source))) {
            source.sendFailure(Component.literal(membership.displayName() + " is already unaffiliated."));
            return 0;
        }
        source.sendSuccess(() -> Component.literal("Released " + membership.displayName() + " from their current Pact."), true);
        return 1;
    }

    private static int restoreLegendary(CommandSourceStack source, String rawRole) {
        GOTLegendaryPactMembership membership = data(source).legendaryMembership(rawRole).orElse(null);
        if (membership == null) {
            source.sendFailure(Component.literal("Unknown Legendary role: " + rawRole));
            return 0;
        }
        if (!data(source).restoreLegendaryToOrigin(membership.roleId(), gameTime(source))) {
            source.sendFailure(Component.literal("No canonical origin is known for " + membership.displayName() + "."));
            return 0;
        }
        source.sendSuccess(() -> Component.literal("Restored " + membership.displayName() + " to their canonical Pact."), true);
        return 1;
    }

    private static int info(CommandSourceStack source, String rawWaypoint) {
        GOTWaypoint waypoint = waypoint(source, rawWaypoint);
        if (waypoint == null) return 0;
        GOTConquestSavedData data = GOTConquestSavedData.get(source.getServer());
        GOTWaypointConquestState state = data.state(waypoint);
        String regions = waypoint.getRegions().stream().map(Enum::name).collect(Collectors.joining(","));
        String scores = state.conquestScores().entrySet().stream()
                .sorted(Map.Entry.<UUID, Integer>comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .map(entry -> shortPact(source.getServer(), entry.getKey()) + "=" + entry.getValue())
                .collect(Collectors.joining(", "));
        if (scores.isBlank()) scores = "none";

        String finalScores = scores;
        source.sendSuccess(() -> Component.literal("[Conquest] " + waypoint.name()
                + " region=" + regions
                + " nativeFaction=" + waypoint.getFactionCode()
                + " pos=" + waypoint.getCoordX() + "," + waypoint.getCoordZ()
                + " value=" + state.strategicValue()
                + " anchor=" + state.anchor()
                + " state=" + state.controlState()
                + " controller=" + state.controllingPact().map(id -> shortPact(source.getServer(), id)).orElse("none")
                + " claimant=" + state.claimingPact().map(id -> shortPact(source.getServer(), id)).orElse("none")
                + " scores={" + finalScores + "}"
                + " updated=" + state.lastUpdatedGameTime()), false);
        return 1;
    }

    private static int dump(CommandSourceStack source) {
        GOTConquestSavedData data = GOTConquestSavedData.get(source.getServer());
        GOTMod.LOGGER.info("--- Conquest dump: schema={} revision={} records={} canonicalPacts={} legendaryMemberships={} diplomaticRelations={} ---",
                data.schemaVersion(), data.revision(), data.allWaypoints().size(), data.allCanonicalPacts().size(),
                data.allLegendaryMemberships().size(), data.allDiplomaticRelations().size());
        for (GOTCanonicalPactState pact : data.allCanonicalPacts()) {
            GOTMod.LOGGER.info("CANONICAL_PACT key={} id={} name={} active={} leader={} members={}",
                    pact.key(), pact.id(), pact.name(), pact.active(), pact.leaderRoleId(),
                    pact.members().stream().map(member -> member.roleId() + ":" + member.faction().id()).toList());
        }
        for (GOTLegendaryPactMembership membership : data.allLegendaryMemberships()) {
            GOTMod.LOGGER.info("LEGENDARY_PACT role={} name={} faction={} current={} origin={} recruitedBy={} recruitedAt={}",
                    membership.roleId(), membership.displayName(), membership.faction().id(),
                    membership.currentPactId().map(UUID::toString).orElse("none"),
                    membership.originCanonicalPact().map(Object::toString).orElse("none"),
                    membership.recruitedBy().map(UUID::toString).orElse("none"), membership.recruitedAtGameTime());
        }
        for (GOTDiplomaticRelationState relation : data.allDiplomaticRelations()) {
            GOTMod.LOGGER.info("DIPLOMACY a={} b={} status={} aToBOpinion={} bToAOpinion={} aToBTrust={} bToATrust={} aToBFear={} bToAFear={} aToBComm={} bToAComm={} trade={} nonAggression={} truceUntil={}",
                    relation.pactA(), relation.pactB(), relation.status(),
                    relation.opinion(relation.pactA(), relation.pactB()), relation.opinion(relation.pactB(), relation.pactA()),
                    relation.trust(relation.pactA(), relation.pactB()), relation.trust(relation.pactB(), relation.pactA()),
                    relation.fear(relation.pactA(), relation.pactB()), relation.fear(relation.pactB(), relation.pactA()),
                    relation.communication(relation.pactA(), relation.pactB()), relation.communication(relation.pactB(), relation.pactA()),
                    relation.tradeAgreement(), relation.nonAggressionPact(), relation.truceUntilGameTime());
        }
        for (GOTPactTreasuryState treasury : data.allTreasuries()) {
            GOTMod.LOGGER.info("TREASURY pact={} balance={} seeded={} playerDeposits={} generated={} spent={} incomeClock={} updated={}",
                    treasury.pactId(), treasury.balance(), treasury.seededCapital(), treasury.totalPlayerDeposits(),
                    treasury.totalGeneratedIncome(), treasury.totalSpent(), treasury.lastIncomeGameTime(),
                    treasury.lastUpdatedGameTime());
        }
        for (GOTWaypointConquestState state : data.allWaypoints()) {
            GOTMod.LOGGER.info("CONQUEST waypoint={} value={} anchor={} state={} controller={} claimant={} scores={}",
                    state.waypointKey(), state.strategicValue(), state.anchor(), state.controlState(),
                    state.controllingPact().map(UUID::toString).orElse("none"),
                    state.claimingPact().map(UUID::toString).orElse("none"),
                    state.conquestScores());
        }
        source.sendSuccess(() -> Component.literal("Dumped " + data.allWaypoints().size() + " conquest records to the server log."), false);
        return data.allWaypoints().size();
    }

    private static int setValue(CommandSourceStack source, String rawWaypoint, int value) {
        GOTWaypoint waypoint = waypoint(source, rawWaypoint);
        if (waypoint == null) return 0;
        data(source).setStrategicValue(waypoint, value, gameTime(source));
        source.sendSuccess(() -> Component.literal("Set " + waypoint.name() + " strategic value to " + value + "."), true);
        return 1;
    }

    private static int setAnchor(CommandSourceStack source, String rawWaypoint, boolean value) {
        GOTWaypoint waypoint = waypoint(source, rawWaypoint);
        if (waypoint == null) return 0;
        data(source).setAnchor(waypoint, value, gameTime(source));
        source.sendSuccess(() -> Component.literal("Set " + waypoint.name() + " anchor=" + value + "."), true);
        return 1;
    }

    private static int setStatus(CommandSourceStack source, String rawWaypoint, String rawState) {
        GOTWaypoint waypoint = waypoint(source, rawWaypoint);
        if (waypoint == null) return 0;
        GOTConquestControlState controlState = GOTConquestControlState.byName(rawState);
        if (controlState == null) {
            source.sendFailure(Component.literal("Unknown conquest state '" + rawState + "'. Use native, neutral, contested, or controlled."));
            return 0;
        }
        GOTConquestSavedData data = data(source);
        if (controlState == GOTConquestControlState.CONTROLLED && data.state(waypoint).controllingPact().isEmpty()) {
            source.sendFailure(Component.literal("CONTROLLED requires a controlling Pact. Use /got conquest controller or setcontrol."));
            return 0;
        }
        data.setControlState(waypoint, controlState, gameTime(source));
        source.sendSuccess(() -> Component.literal("Set " + waypoint.name() + " state=" + controlState + "."), true);
        return 1;
    }

    private static int setController(CommandSourceStack source, String rawWaypoint, String rawPact) {
        GOTWaypoint waypoint = waypoint(source, rawWaypoint);
        if (waypoint == null) return 0;
        Optional<UUID> pact = pact(source, rawPact, true);
        if (pact == null) return 0;
        data(source).setControllingPact(waypoint, pact.orElse(null), gameTime(source));
        source.sendSuccess(() -> Component.literal("Set " + waypoint.name() + " controller="
                + pact.map(id -> shortPact(source.getServer(), id)).orElse("none") + "."), true);
        return 1;
    }

    private static int setClaimant(CommandSourceStack source, String rawWaypoint, String rawPact) {
        GOTWaypoint waypoint = waypoint(source, rawWaypoint);
        if (waypoint == null) return 0;
        Optional<UUID> pact = pact(source, rawPact, true);
        if (pact == null) return 0;
        data(source).setClaimingPact(waypoint, pact.orElse(null), gameTime(source));
        source.sendSuccess(() -> Component.literal("Set " + waypoint.name() + " claimant="
                + pact.map(id -> shortPact(source.getServer(), id)).orElse("none") + "."), true);
        return 1;
    }

    private static int setScore(CommandSourceStack source, String rawWaypoint, String rawPact, int score) {
        GOTWaypoint waypoint = waypoint(source, rawWaypoint);
        if (waypoint == null) return 0;
        Optional<UUID> pact = pact(source, rawPact, false);
        if (pact == null || pact.isEmpty()) return 0;
        data(source).setConquestScore(waypoint, pact.get(), score, gameTime(source));
        source.sendSuccess(() -> Component.literal("Set " + waypoint.name() + " score for "
                + shortPact(source.getServer(), pact.get()) + " to " + score + "."), true);
        return 1;
    }

    private static int clearScore(CommandSourceStack source, String rawWaypoint, String rawPact) {
        GOTWaypoint waypoint = waypoint(source, rawWaypoint);
        if (waypoint == null) return 0;
        Optional<UUID> pact = pact(source, rawPact, false);
        if (pact == null || pact.isEmpty()) return 0;
        data(source).clearConquestScore(waypoint, pact.get(), gameTime(source));
        source.sendSuccess(() -> Component.literal("Cleared " + waypoint.name() + " score for "
                + shortPact(source.getServer(), pact.get()) + "."), true);
        return 1;
    }

    private static int setControl(CommandSourceStack source, String rawWaypoint, String rawPact, int score) {
        GOTWaypoint waypoint = waypoint(source, rawWaypoint);
        if (waypoint == null) return 0;
        Optional<UUID> pact = pact(source, rawPact, false);
        if (pact == null || pact.isEmpty()) return 0;
        GOTConquestSavedData data = data(source);
        long gameTime = gameTime(source);
        data.setConquestScore(waypoint, pact.get(), score, gameTime);
        data.setControllingPact(waypoint, pact.get(), gameTime);
        source.sendSuccess(() -> Component.literal("Set " + waypoint.name() + " controlled by "
                + shortPact(source.getServer(), pact.get()) + " with score " + score + "."), true);
        return 1;
    }

    private static int reset(CommandSourceStack source, String rawWaypoint) {
        GOTWaypoint waypoint = waypoint(source, rawWaypoint);
        if (waypoint == null) return 0;
        data(source).resetWaypoint(waypoint, gameTime(source));
        source.sendSuccess(() -> Component.literal("Reset conquest state for " + waypoint.name() + "."), true);
        return 1;
    }

    private static int resetAll(CommandSourceStack source) {
        data(source).resetAll(gameTime(source));
        source.sendSuccess(() -> Component.literal("Reset ALL conquest waypoint state to foundation defaults."), true);
        return 1;
    }

    private static GOTConquestSavedData data(CommandSourceStack source) {
        return GOTConquestSavedData.get(source.getServer());
    }

    private static long gameTime(CommandSourceStack source) {
        return source.getServer().overworld().getGameTime();
    }

    private static GOTWaypoint waypoint(CommandSourceStack source, String raw) {
        GOTWaypoint waypoint = GOTWaypoint.waypointForName(raw);
        if (waypoint == null) source.sendFailure(Component.literal("Unknown waypoint: " + raw));
        return waypoint;
    }

    /**
     * Returns null on parse failure, Optional.empty for the explicit word "none".
     * Canonical NPC Pact keys, player Pact UUIDs and arbitrary UUIDs are accepted.
     * The explicit word "self" resolves only the caller's existing player-created Pact.
     */
    private static Optional<UUID> pact(CommandSourceStack source, String raw, boolean allowNone) {
        if (allowNone && "none".equalsIgnoreCase(raw)) return Optional.empty();
        Optional<GOTCanonicalPactState> canonical = GOTCanonicalPactService.byKey(source.getServer(), raw);
        if (canonical.isPresent()) return Optional.of(canonical.get().id());
        if ("self".equalsIgnoreCase(raw) || "mine".equalsIgnoreCase(raw)) {
            if (!(source.getEntity() instanceof ServerPlayer player)) {
                source.sendFailure(Component.literal("The 'self' Pact shortcut requires a player."));
                return null;
            }
            Optional<got.pact.GOTPact> ownPact = GOTPactSavedData.get(source.getServer()).pactFor(player.getUUID());
            if (ownPact.isEmpty()) {
                source.sendFailure(Component.literal("You are not in a Pact."));
                return null;
            }
            return Optional.of(ownPact.get().id());
        }
        try {
            return Optional.of(UUID.fromString(raw));
        } catch (IllegalArgumentException ex) {
            source.sendFailure(Component.literal("Unknown Pact: " + raw + " (use a canonical key, UUID, or 'self')."));
            return null;
        }
    }

    private static String shortPact(MinecraftServer server, UUID pactId) {
        Optional<GOTCanonicalPactState> canonical = GOTCanonicalPactService.byId(server, pactId);
        if (canonical.isPresent()) {
            GOTCanonicalPactState pact = canonical.get();
            return pact.name() + "[" + pact.key().getPath() + "](" + pactId + ")";
        }
        return GOTPactSavedData.get(server).pact(pactId)
                .map(pact -> pact.name() + "(" + pactId + ")")
                .orElse(pactId.toString());
    }
}
