package got.client.gui.hiring;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;

public final class GOTCommandHornClient {
    private GOTCommandHornClient() {}

    public static void open(InteractionHand hand) {
        Minecraft.getInstance().setScreen(new GOTGuiCommandHornSelect(hand));
    }
}
