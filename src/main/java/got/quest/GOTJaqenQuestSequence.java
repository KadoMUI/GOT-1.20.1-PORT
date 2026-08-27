package got.quest;

import got.GOTEquipment;
import got.GOTItems;
import got.network.GOTNetwork;
import got.network.S2CJaqenTutorialOfferPacket;
import got.npc.GOTJaqenHgharEntity;
import got.speech.GOTSpeechService;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;

/** Faithful 1.20.1 bridge for the original GOTMiniQuestWelcome tutorial. */
public final class GOTJaqenQuestSequence {
    public static final String ID = "legendary/jaqen_welcome";
    private static final String FLAG_DONE = "done";
    private static final String FLAG_GIFT = "gift";
    private GOTJaqenQuestSequence() {}

    // Legacy WStage values are intentionally retained for parity and save readability.
    public static int stage(ServerPlayer p) { return GOTSpecialQuestState.stage(p, ID); }
    private static void stage(ServerPlayer p, int value) { GOTSpecialQuestState.setStage(p, ID, value); }
    public static boolean completed(ServerPlayer p) { return GOTSpecialQuestState.flag(p, ID, FLAG_DONE); }

    public static boolean interact(ServerPlayer player, GOTJaqenHgharEntity jaqen) {
        if (completed(player)) {
            GOTSpeechService.speak(jaqen, player, "legendary/jaqen_friendly");
            return true;
        }
        int s = stage(player);
        if (s == 0) {
            GOTSpeechService.speak(jaqen, player, "legendary/jaqen_welcome");
            GOTNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new S2CJaqenTutorialOfferPacket(jaqen.getId()));
            return true;
        }
        switch (s) {
            case 1 -> { give(player, GOTItems.QUEST_BOOK.get().getDefaultInstance()); quote(jaqen, player, 4); stage(player, 2); objective(player); }
            case 2 -> { quote(jaqen, player, 4); objective(player); }
            case 3 -> { quote(jaqen, player, 5); stage(player, 4); }
            case 4 -> { quote(jaqen, player, 6); stage(player, 5); objective(player); }
            case 5 -> { quote(jaqen, player, 6); objective(player); }
            case 6 -> { quote(jaqen, player, 7); stage(player, 7); }
            case 7 -> { quote(jaqen, player, 8); stage(player, 8); objective(player); }
            case 8, 9 -> objective(player);
            case 10 -> { quote(jaqen, player, 9); stage(player, 11); objective(player); }
            case 11 -> objective(player);
            case 12 -> { quote(jaqen, player, 10); stage(player, 13); }
            case 13 -> { giveFinalGift(player); quote(jaqen, player, 11); stage(player, 14); }
            case 14 -> complete(player, jaqen);
            default -> { }
        }
        return true;
    }

    public static void accept(ServerPlayer player, GOTJaqenHgharEntity jaqen) {
        if (completed(player) || stage(player) != 0) return;
        quote(jaqen, player, 2);
        stage(player, 1);
        player.displayClientMessage(Component.translatable("got.jaqen.tutorial.accepted").withStyle(ChatFormatting.GOLD), false);
        objective(player);
    }

    public static void clientAction(ServerPlayer player, Action action) {
        int s=stage(player);
        if (action == Action.OPEN_BOOK && s == 2) { stage(player, 3); objective(player); }
        else if (action == Action.VIEW_MAP && s == 5) { stage(player, 6); objective(player); }
        else if (action == Action.VIEW_ALIGNMENT && s == 8) { stage(player, 9); objective(player); }
        // Modern Alignment has no legacy region-cycle keys; first Factions visit stands in for region cycling.
        else if (action == Action.VIEW_FACTIONS && s == 9) { stage(player, 10); objective(player); }
        else if (action == Action.VIEW_FACTIONS && s == 11) { stage(player, 12); objective(player); }
    }

    private static void complete(ServerPlayer player, GOTJaqenHgharEntity jaqen) {
        quote(jaqen, player, 12);
        stage(player, 15);
        GOTSpecialQuestState.setFlag(player, ID, FLAG_DONE, true);
        player.displayClientMessage(Component.translatable("got.jaqen.tutorial.complete").withStyle(ChatFormatting.GREEN), false);
        jaqen.depart();
    }

    private static void giveFinalGift(ServerPlayer player) {
        if (GOTSpecialQuestState.flag(player, ID, FLAG_GIFT)) return;
        give(player, GOTItems.POUCH_SMALL.get().getDefaultInstance());
        give(player, GOTItems.POUCH_SMALL.get().getDefaultInstance());
        give(player, GOTEquipment.VALYRIAN_DAGGER.get().getDefaultInstance());
        GOTSpecialQuestState.setFlag(player, ID, FLAG_GIFT, true);
    }

    private static void give(ServerPlayer player, ItemStack stack) {
        if (!player.addItem(stack)) player.drop(stack, false);
    }

    private static void quote(GOTJaqenHgharEntity jaqen, ServerPlayer player, int line) {
        GOTSpeechService.speakLine(jaqen, player, "legendary/jaqen_quest", line);
    }

    private static void objective(ServerPlayer player) {
        String key = switch (stage(player)) {
            case 1,3,4,6,7,10,12,13,14 -> "got.miniquest.welcome.speak";
            case 2 -> "got.miniquest.welcome.book";
            case 5 -> "got.miniquest.welcome.map";
            case 8 -> "got.miniquest.welcome.align";
            case 9 -> "got.miniquest.welcome.alignRegions";
            case 11 -> "got.miniquest.welcome.factions";
            default -> null;
        };
        if (key != null) player.displayClientMessage(Component.translatable(key).withStyle(ChatFormatting.YELLOW), false);
    }

    public enum Action { OPEN_BOOK, VIEW_MAP, VIEW_ALIGNMENT, VIEW_FACTIONS }
}
