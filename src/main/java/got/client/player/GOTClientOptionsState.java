package got.client.player;

import got.player.GOTPlayerOptions;

public final class GOTClientOptionsState {
    private static GOTPlayerOptions.Snapshot state =
            new GOTPlayerOptions.Snapshot(true,true,true,true,true,false,true,false);

    private GOTClientOptionsState() {}
    public static void set(GOTPlayerOptions.Snapshot value) { state = value; }
    public static GOTPlayerOptions.Snapshot get() { return state; }
}
