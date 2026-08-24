package got;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTDrunkennessEvents {
    private GOTDrunkennessEvents() {}


    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) {
            return;
        }

        if (event.player.getPersistentData().getBoolean(GOTDrinkItem.TAG_WILDFIRE_BURN)) {
            // Reapply continuously so water, rain, cauldrons, and ordinary
            // extinguishing cannot end wildfire. Death creates a new player
            // entity, so the marker naturally disappears on respawn.
            event.player.setSecondsOnFire(2);
        }
    }

    @SubscribeEvent
    public static void onServerChat(ServerChatEvent event) {
        ServerPlayer player = event.getPlayer();
        if (!player.hasEffect(GOTEffects.DRUNKENNESS.get())) {
            return;
        }

        String original = event.getRawText();
        if (original == null || original.isBlank()) {
            return;
        }

        int amplifier = player.getEffect(GOTEffects.DRUNKENNESS.get()).getAmplifier();
        long seed = player.getUUID().getMostSignificantBits()
                ^ player.getUUID().getLeastSignificantBits()
                ^ player.level().getGameTime()
                ^ original.hashCode();
        Random random = new Random(seed);

        event.setMessage(Component.literal(slur(original, amplifier, random)));
    }

    static String slur(String message, int amplifier, Random random) {
        String[] words = message.trim().split("\\s+");
        List<String> output = new ArrayList<>();

        double cutoffChance = Math.min(0.48D, 0.20D + amplifier * 0.07D);
        double hicChance = Math.min(0.28D, 0.10D + amplifier * 0.04D);

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (word.length() >= 4 && random.nextDouble() < cutoffChance) {
                int minimum = Math.min(2, word.length() - 1);
                int maximum = Math.max(minimum + 1, word.length() - 1);
                int cutAt = minimum + random.nextInt(maximum - minimum);
                word = word.substring(0, cutAt) + "-";
            }

            output.add(word);

            if (i < words.length - 1 && random.nextDouble() < hicChance) {
                output.add("*hic*");
            }
        }

        if (random.nextDouble() < hicChance * 0.6D) {
            output.add("*hic*");
        }

        return String.join(" ", output);
    }
}
