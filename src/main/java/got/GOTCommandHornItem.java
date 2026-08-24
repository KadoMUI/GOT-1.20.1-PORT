package got;

import got.client.gui.hiring.GOTCommandHornClient;
import got.npc.hiring.command.GOTCommandHornMode;
import got.npc.hiring.command.GOTCommandHornService;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public final class GOTCommandHornItem extends Item {
    private static final String MODE_KEY = "GOTHornMode";

    public GOTCommandHornItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    public static GOTCommandHornMode getMode(ItemStack stack) {
        int id = stack.getOrCreateTag().getInt(MODE_KEY);
        if (id < 0 || id >= GOTCommandHornMode.values().length) id = 0;
        return GOTCommandHornMode.values()[id];
    }

    public static void setMode(ItemStack stack, GOTCommandHornMode mode) {
        stack.getOrCreateTag().putInt(MODE_KEY, mode.ordinal());
    }

    @Override
    public Component getName(ItemStack stack) {
        return switch (getMode(stack)) {
            case HALT -> Component.translatable("item.got.command_horn.halt");
            case READY -> Component.translatable("item.got.command_horn.ready");
            case SUMMON -> Component.translatable("item.got.command_horn.summon");
            default -> Component.translatable("item.got.command_horn");
        };
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 40;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        GOTCommandHornMode mode = getMode(stack);

        if (mode == GOTCommandHornMode.SELECT) {
            if (level.isClientSide) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> GOTCommandHornClient.open(hand));
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        if (!level.isClientSide && living instanceof ServerPlayer player) {
            GOTCommandHornMode mode = getMode(stack);
            int affected = GOTCommandHornService.execute(player, stack, mode);

            if (mode == GOTCommandHornMode.HALT) setMode(stack, GOTCommandHornMode.READY);
            else if (mode == GOTCommandHornMode.READY) setMode(stack, GOTCommandHornMode.HALT);

            level.playSound(null, player.blockPosition(), GOTSounds.COMMAND_HORN.get(),
                    SoundSource.PLAYERS, 4.0F, 1.0F);

            player.displayClientMessage(
                Component.literal("Horn command affected " + affected + " hired warrior" + (affected == 1 ? "" : "s") + "."),
                true
            );
        }

        return stack;
    }
}
