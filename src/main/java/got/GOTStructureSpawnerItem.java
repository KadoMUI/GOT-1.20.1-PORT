package got;

import got.world.structure.GOTStructureSpawnerType;
import got.world.structure.nightwatch.NightWatchStructureType;
import got.world.structure.nightwatch.PlanetosNightWatchStructureGenerator;
import got.world.structure.north.NorthStructureType;
import got.world.structure.north.PlanetosNorthStructureGenerator;
import got.world.structure.wildling.PlanetosWildlingStructureGenerator;
import got.world.structure.wildling.WildlingStructureType;
import got.world.structure.schematic.AuthoredStructureType;
import got.world.structure.schematic.PlanetosAuthoredStructureSpawner;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

/** Creative/test spawner retaining the original SpawnedID NBT contract. */
public final class GOTStructureSpawnerItem extends Item {
    public static final String STRUCTURE_TAG = "SpawnedID";

    public GOTStructureSpawnerItem(Properties properties) {
        super(properties);
    }

    public static ItemStack createStack(GOTStructureSpawnerType type) {
        ItemStack stack = new ItemStack(GOTItems.STRUCTURE_SPAWNER.get());
        stack.getOrCreateTag().putInt(STRUCTURE_TAG, type.legacyId());
        if (type.settlement()) stack.getOrCreateTag().putInt("CustomModelData", 1);
        return stack;
    }

    public static GOTStructureSpawnerType getType(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains(STRUCTURE_TAG, Tag.TAG_ANY_NUMERIC)
                ? GOTStructureSpawnerType.byLegacyId(tag.getInt(STRUCTURE_TAG))
                : NorthStructureType.HOUSE;
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return "structure.got." + getType(stack).serializedName();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lines,
                                TooltipFlag flag) {
        GOTStructureSpawnerType type = getType(stack);
        lines.add(Component.translatable("item.got.structure_spawner.hint").withStyle(ChatFormatting.GRAY));
        if (flag.isAdvanced()) {
            lines.add(Component.literal("Legacy structure ID: " + type.legacyId())
                    .withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (!(level instanceof ServerLevel serverLevel)) return InteractionResult.FAIL;
        Player player = context.getPlayer();
        int rotation = player == null ? 0 : Math.floorMod((int)Math.floor((player.getYRot() + 45.0F) / 90.0F), 4);
        BlockPos origin = context.getClickedPos().relative(context.getClickedFace());
        GOTStructureSpawnerType type = getType(context.getItemInHand());
        boolean generated;
        if (type instanceof WildlingStructureType wildling) {
            generated = PlanetosWildlingStructureGenerator.spawn(serverLevel, origin, wildling, rotation);
        } else if (type instanceof NightWatchStructureType nightWatch) {
            generated = PlanetosNightWatchStructureGenerator.spawn(serverLevel, origin, nightWatch, rotation);
        } else if (type instanceof AuthoredStructureType authored) {
            generated = PlanetosAuthoredStructureSpawner.spawn(serverLevel, origin, authored, rotation);
        } else {
            generated = PlanetosNorthStructureGenerator.spawn(serverLevel, origin, (NorthStructureType)type, rotation);
        }
        if (generated && player != null && !player.getAbilities().instabuild) {
            context.getItemInHand().shrink(1);
        }
        return generated ? InteractionResult.CONSUME : InteractionResult.FAIL;
    }
}
