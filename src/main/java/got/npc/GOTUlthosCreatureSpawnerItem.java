package got.npc;

import got.GOTEntities;
import got.GOTItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

/** One legacy-style egg with separate Ulthos Spider and Blizzard catalogue entries. */
public final class GOTUlthosCreatureSpawnerItem extends Item {
    private static final String TYPE_TAG = "UlthosCreature";

    public GOTUlthosCreatureSpawnerItem(Properties properties) { super(properties); }

    public static ItemStack createStack(UlthosCreature creature) {
        ItemStack stack = new ItemStack(GOTItems.ULTHOS_CREATURE_SPAWNER.get());
        stack.getOrCreateTag().putString(TYPE_TAG, creature.id());
        return stack;
    }

    public static UlthosCreature getCreature(ItemStack stack) {
        return stack.hasTag() ? UlthosCreature.byId(stack.getTag().getString(TYPE_TAG)) : UlthosCreature.SPIDER;
    }

    public static int tint(ItemStack stack, int tintIndex) {
        UlthosCreature creature = getCreature(stack);
        return tintIndex == 0 ? (creature == UlthosCreature.SPIDER ? 0x342A3B : 0xB9E8FF)
                : (creature == UlthosCreature.SPIDER ? 0x7A315B : 0xF5FBFF);
    }

    @Override public Component getName(ItemStack stack) {
        return Component.translatable("item.got.ulthos_creature_spawner.named",
                getCreature(stack).displayName());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(getCreature(stack) == UlthosCreature.SPIDER
                ? "item.got.ulthos_creature_spawner.ulthos"
                : "item.got.ulthos_creature_spawner.walker").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.got.ulthos_creature_spawner.hint").withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        if (level instanceof ServerLevel serverLevel) {
            Mob mob;
            if (getCreature(context.getItemInHand()) == UlthosCreature.BLIZZARD) {
                mob = GOTEntities.BLIZZARD.get().create(serverLevel);
            } else {
                GOTUlthosSpiderEntity spider = GOTEntities.ULTHOS_SPIDER.get().create(serverLevel);
                if (spider == null) return InteractionResult.FAIL;
                spider.randomizeLegacyVariant();
                mob = spider;
            }
            if (mob == null) return InteractionResult.FAIL;
            mob.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D,
                    context.getRotation() + 180.0F, 0.0F);
            mob.setPersistenceRequired();
            if (!serverLevel.noCollision(mob)) return InteractionResult.FAIL;
            serverLevel.addFreshEntity(mob);
            if (context.getPlayer() == null || !context.getPlayer().getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public enum UlthosCreature {
        SPIDER("spider", "Ulthos Spider"), BLIZZARD("blizzard", "Blizzard");
        private final String id;
        private final String displayName;
        UlthosCreature(String id, String displayName) { this.id = id; this.displayName = displayName; }
        public String id() { return id; }
        public String displayName() { return displayName; }
        public static UlthosCreature byId(String id) {
            return id != null && id.toLowerCase(Locale.ROOT).equals("blizzard") ? BLIZZARD : SPIDER;
        }
    }
}
