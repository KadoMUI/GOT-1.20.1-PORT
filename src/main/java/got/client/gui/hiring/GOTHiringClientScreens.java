package got.client.gui.hiring;

import got.npc.hiring.GOTHireSnapshot;
import net.minecraft.client.Minecraft;

public final class GOTHiringClientScreens {
    private GOTHiringClientScreens() {}

    public static void open(GOTHireSnapshot snapshot) {
        Minecraft mc = Minecraft.getInstance();
        if (snapshot.hired()) {
            mc.setScreen(snapshot.task() == got.npc.hiring.GOTHiredTask.FARMER
                ? new GOTGuiHiredFarmer(snapshot)
                : new GOTGuiHiredWarrior(snapshot));
        } else {
            mc.setScreen(new GOTGuiHire(snapshot));
        }
    }
}
