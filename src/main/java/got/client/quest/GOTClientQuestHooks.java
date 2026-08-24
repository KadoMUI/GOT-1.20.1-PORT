package got.client.quest;

import got.client.gui.GOTGuiQuestBook;
import net.minecraft.client.Minecraft;

public final class GOTClientQuestHooks {
    private GOTClientQuestHooks() {}

    public static void openBook() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) minecraft.setScreen(new GOTGuiQuestBook());
    }
}
