package got.client.quest;

import got.client.gui.GOTGuiQuestOffer;
import got.network.S2CQuestDataPacket;
import got.network.S2CQuestOfferPacket;
import got.quest.GOTQuestView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

/** Client-only synchronized quest journal and HUD tracker state. */
public final class GOTClientQuestState {
    private static final ResourceLocation TRACKER =
            ResourceLocation.fromNamespaceAndPath("got", "textures/gui/quest/tracker.png");
    private static List<GOTQuestView> active = List.of();
    private static List<GOTQuestView> archive = List.of();
    private static UUID tracked;

    private GOTClientQuestState() {}

    public static void accept(S2CQuestDataPacket packet) {
        active = packet.active();
        archive = packet.archive();
        tracked = packet.tracked();
    }

    public static void openOffer(S2CQuestOfferPacket packet) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) minecraft.setScreen(new GOTGuiQuestOffer(packet.offer(), packet.giverId()));
    }

    public static List<GOTQuestView> active() { return active; }
    public static List<GOTQuestView> archive() { return archive; }

    @Nullable
    public static GOTQuestView trackedQuest() {
        if (tracked == null) return null;
        return active.stream().filter(view -> tracked.equals(view.instanceId())).findFirst().orElse(null);
    }

    public static void reset() {
        active = List.of();
        archive = List.of();
        tracked = null;
    }

    public static void renderTracker(Minecraft minecraft, GuiGraphics graphics) {
        GOTQuestView quest = trackedQuest();
        if (minecraft.player == null || quest == null || minecraft.options.hideGui) return;
        int x = 16;
        int y = 10;
        int barX = x + 24;
        int barWidth = 90;
        int barHeight = 15;
        graphics.blit(TRACKER, x, y, 0, 0, 20, 20, 256, 256);
        graphics.fill(barX + 2, y + 2, barX + 2 + Math.round(86 * quest.completion()),
                y + 13, 0xFF000000 | quest.color());
        graphics.blit(TRACKER, barX, y, 20, 0, barWidth, barHeight, 256, 256);

        Item icon = ForgeRegistries.ITEMS.getValue(quest.icon());
        if (icon != null) graphics.renderItem(new ItemStack(icon), x + 2, y + 2);
        GOTQuestView.ObjectiveView objective = quest.currentObjective();
        String progress = objective.progress() + " / " + objective.target();
        graphics.drawCenteredString(minecraft.font, progress, barX + barWidth / 2, y + 4, 0xFFFFFF);
        List<net.minecraft.util.FormattedCharSequence> lines = minecraft.font.split(
                Component.translatable(objective.labelKey(), objective.progress(), objective.target()), barWidth);
        for (int index = 0; index < Math.min(2, lines.size()); index++) {
            graphics.drawString(minecraft.font, lines.get(index), barX,
                    y + barHeight + 3 + index * minecraft.font.lineHeight, 0xFFFFFF, true);
        }
    }
}
