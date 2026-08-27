package got.client.gui;

import got.client.player.GOTClientOptionsState;
import got.network.C2SRequestOptionsPacket;
import got.network.C2SToggleOptionPacket;
import got.network.GOTNetwork;
import got.player.GOTPlayerOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.LanguageSelectScreen;
import net.minecraft.network.chat.Component;

import java.util.EnumMap;
import java.util.Map;

public final class GOTGuiSettings extends GOTGuiMenuBaseReturn {
    private final Map<GOTPlayerOptions.Option, Button> buttons =
            new EnumMap<>(GOTPlayerOptions.Option.class);

    public GOTGuiSettings() {
        super(Component.translatable("got.gui.settings"));
        sizeX=256; sizeY=258;
    }

    @Override
    protected void init() {
        super.init();
        buttons.clear();
        GOTPlayerOptions.Option[] options = {
                GOTPlayerOptions.Option.FRIENDLY_FIRE,
                GOTPlayerOptions.Option.HIRED_DEATH_MESSAGES,
                GOTPlayerOptions.Option.SHOW_ALIGNMENT,
                GOTPlayerOptions.Option.SHOW_MAP_LOCATION,
                GOTPlayerOptions.Option.CONQUEST_KILLS,
                GOTPlayerOptions.Option.FEMININE_RANKS,
                GOTPlayerOptions.Option.IMMERSIVE_SPEECH,
                GOTPlayerOptions.Option.IMMERSIVE_SPEECH_CHAT
        };
        int y=guiTop+30;
        for (GOTPlayerOptions.Option option : options) {
            Button b=addRenderableWidget(Button.builder(Component.empty(),
                    button -> {
                        GOTNetwork.CHANNEL.sendToServer(new C2SToggleOptionPacket(option));
                        updateLabels();
                    }).bounds(guiLeft+28,y,200,20).build());
            buttons.put(option,b);
            y+=23;
        }
        addRenderableWidget(Button.builder(Component.translatable("got.gui.settings.localization"),
                button -> {
                    Minecraft mc = Minecraft.getInstance();
                    mc.setScreen(new LanguageSelectScreen(this, mc.options, mc.getLanguageManager()));
                }).bounds(guiLeft + 28, y + 2, 200, 20).build());

        GOTNetwork.CHANNEL.sendToServer(new C2SRequestOptionsPacket());
        updateLabels();
    }

    @Override
    public void tick() {
        super.tick();
        updateLabels();
    }

    private boolean state(GOTPlayerOptions.Option option) {
        var s=GOTClientOptionsState.get();
        return switch(option) {
            case FRIENDLY_FIRE -> s.friendlyFire();
            case HIRED_DEATH_MESSAGES -> s.hiredDeathMessages();
            case SHOW_ALIGNMENT -> s.showAlignment();
            case SHOW_MAP_LOCATION -> s.showMapLocation();
            case CONQUEST_KILLS -> s.conquestKills();
            case FEMININE_RANKS -> s.feminineRanks();
            case IMMERSIVE_SPEECH -> s.immersiveSpeech();
            case IMMERSIVE_SPEECH_CHAT -> s.immersiveSpeechChat();
        };
    }

    private void updateLabels() {
        for (var e : buttons.entrySet()) {
            String key="got.gui.options."+e.getKey().name().toLowerCase();
            e.getValue().setMessage(Component.translatable(key)
                    .append(": ")
                    .append(Component.translatable(state(e.getKey()) ? "options.on" : "options.off")));
        }
    }

    @Override
    public void render(GuiGraphics g,int mouseX,int mouseY,float partialTick) {
        renderBackground(g);
        g.drawCenteredString(font,title,width/2,guiTop+8,0xFFFFFF);
        super.render(g,mouseX,mouseY,partialTick);
    }
}
