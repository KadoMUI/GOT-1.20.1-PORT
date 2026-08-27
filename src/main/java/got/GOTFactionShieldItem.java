package got;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

/** A real, vanilla-mechanics shield backed by one of the legacy GOT shield textures. */
public final class GOTFactionShieldItem extends ShieldItem {
    private final ResourceLocation texture;

    public GOTFactionShieldItem(String textureName) {
        super(new Item.Properties().durability(336)); // vanilla shield durability
        this.texture = ResourceLocation.fromNamespaceAndPath(GOTMod.MOD_ID, "textures/shield/" + textureName + ".png");
    }

    public ResourceLocation texture() {
        return texture;
    }


    // Keep the vanilla shield interaction contract explicit.  This makes the item
    // enter BLOCK use-state, which drives both first-person and humanoid arm poses.
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(GOTFactionShieldItemClientExtensions.INSTANCE);
    }
}
