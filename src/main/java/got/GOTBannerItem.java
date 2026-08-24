package got;

import got.claim.GOTBannerClaim;
import got.claim.GOTBannerClaimConfig;
import got.claim.GOTBannerProtection;
import got.faction.GOTFaction;
import got.faction.GOTFactionPlayerData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

/** One registry item carrying all 605 legacy banner designs in stack NBT. */
public final class GOTBannerItem extends Item {
    public static final String TYPE_TAG = "BannerType";
    public static final String PROTECTION_TAG = "GOTBannerData";

    public GOTBannerItem(Properties properties) {
        super(properties);
    }

    public static ItemStack createStack(GOTBannerType type) {
        ItemStack stack = new ItemStack(GOTItems.BANNER.get());
        setBannerType(stack, type);
        return stack;
    }

    public static GOTBannerType getBannerType(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains(TYPE_TAG, Tag.TAG_ANY_NUMERIC)
                ? GOTBannerType.byLegacyId(tag.getInt(TYPE_TAG))
                : GOTBannerType.DEFAULT;
    }

    public static void setBannerType(ItemStack stack, GOTBannerType type) {
        stack.getOrCreateTag().putInt(TYPE_TAG, type.legacyId());
    }

    @Nullable
    public static CompoundTag getProtectionData(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains(PROTECTION_TAG, Tag.TAG_COMPOUND)
                ? tag.getCompound(PROTECTION_TAG).copy() : null;
    }

    public static void setProtectionData(ItemStack stack, @Nullable CompoundTag data) {
        if (data == null || data.isEmpty()) {
            if (stack.getTag() != null) stack.getTag().remove(PROTECTION_TAG);
        } else {
            stack.getOrCreateTag().put(PROTECTION_TAG, data.copy());
        }
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return getBannerType(stack).translationKey();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lines,
                                TooltipFlag flag) {
        CompoundTag protection = getProtectionData(stack);
        if (protection != null) {
            lines.add(Component.translatable("item.got.banner.protect").withStyle(ChatFormatting.GRAY));
        }
        if (flag.isAdvanced()) {
            GOTBannerType type = getBannerType(stack);
            lines.add(Component.literal("Legacy heraldry ID: " + type.legacyId())
                    .withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Direction face = context.getClickedFace();
        if (face == Direction.DOWN) return InteractionResult.FAIL;
        return face == Direction.UP ? placeStanding(context) : placeWall(context, face);
    }

    private InteractionResult placeStanding(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clicked = context.getClickedPos();
        BlockPos pos = level.getBlockState(clicked).canBeReplaced() ? clicked : clicked.above();
        BlockPos support = pos.below();
        if (!level.getBlockState(pos).canBeReplaced()
                || !level.getBlockState(support).isFaceSturdy(level, support, Direction.UP)) {
            return InteractionResult.FAIL;
        }

        Player placingPlayer = context.getPlayer();
        int protectionRange = GOTBannerProtection.rangeForSupport(level.getBlockState(support));
        CompoundTag carriedProtection = getProtectionData(context.getItemInHand());
        if (carriedProtection != null) {
            GOTBannerClaim carriedClaim = new GOTBannerClaim();
            carriedClaim.load(carriedProtection);
            if (carriedClaim.customRange() > 0) protectionRange = carriedClaim.customRange();
        }
        if (!GOTBannerClaimConfig.ENABLED.get()) protectionRange = 0;
        if (!level.isClientSide && protectionRange > 0 && placingPlayer instanceof ServerPlayer serverPlayer
                && !serverPlayer.getAbilities().instabuild) {
            GOTFaction faction = getBannerType(context.getItemInHand()).faction();
            float alignment = GOTFactionPlayerData.get(serverPlayer).alignment(faction);
            if (faction == GOTFaction.UNALIGNED || alignment < GOTBannerClaim.MIN_ALIGNMENT) {
                serverPlayer.displayClientMessage(Component.translatable(
                        "got.chat.bannerAlignmentRequired", GOTBannerClaim.MIN_ALIGNMENT,
                        faction.displayName()), false);
                return InteractionResult.FAIL;
            }
            if (!GOTBannerProtection.canCreateClaim((ServerLevel) level, pos,
                    protectionRange, serverPlayer, true)) {
                return InteractionResult.FAIL;
            }
        }

        GOTStandingBannerEntity banner = GOTEntities.STANDING_BANNER.get().create(level);
        if (banner == null) return InteractionResult.FAIL;
        Player player = placingPlayer;
        float yaw = player == null ? 0.0F
                : Direction.fromYRot(player.getYRot()).toYRot();
        banner.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
        banner.setYRot(yaw);
        banner.yRotO = yaw;
        applyStackData(context.getItemInHand(), player, banner);
        if (!level.noCollision(banner)
                || !level.getEntitiesOfClass(GOTAbstractBannerEntity.class,
                banner.getBoundingBox().inflate(0.05D)).isEmpty()) {
            return InteractionResult.FAIL;
        }

        return spawn(context, banner);
    }

    private InteractionResult placeWall(UseOnContext context, Direction face) {
        Level level = context.getLevel();
        BlockPos anchor = context.getClickedPos();
        if (!level.getBlockState(anchor).isFaceSturdy(level, anchor, face)) {
            return InteractionResult.FAIL;
        }

        GOTWallBannerEntity banner = GOTEntities.WALL_BANNER.get().create(level);
        if (banner == null) return InteractionResult.FAIL;
        banner.setAnchor(anchor, face);
        applyStackData(context.getItemInHand(), context.getPlayer(), banner);
        if (!level.noCollision(banner)
                || !level.getEntitiesOfClass(GOTAbstractBannerEntity.class,
                banner.getBoundingBox().inflate(0.05D)).isEmpty()) {
            return InteractionResult.FAIL;
        }

        return spawn(context, banner);
    }

    private static void applyStackData(ItemStack stack, @Nullable Player player,
                                       GOTAbstractBannerEntity banner) {
        banner.setBannerType(getBannerType(stack));
        banner.setProtectionData(getProtectionData(stack));
        boolean keepOriginalOwner = player != null && player.getAbilities().instabuild
                && player.isShiftKeyDown() && banner.getOwnerUUID() != null;
        if (player != null && !keepOriginalOwner) banner.setOwner(player);
        banner.claimChanged();
    }

    private static InteractionResult spawn(UseOnContext context, GOTAbstractBannerEntity banner) {
        Level level = context.getLevel();
        if (!level.isClientSide) {
            level.addFreshEntity(banner);
            level.playSound(null, banner.blockPosition(), SoundEvents.WOOD_PLACE,
                    SoundSource.BLOCKS, 1.0F, 0.9F + level.random.nextFloat() * 0.2F);
            Player player = context.getPlayer();
            if (player == null || !player.getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(GOTBannerItemClientExtensions.INSTANCE);
    }
}
