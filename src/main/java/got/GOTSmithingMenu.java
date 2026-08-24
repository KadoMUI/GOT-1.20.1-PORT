package got;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AnvilBlock;

public final class GOTSmithingMenu extends AbstractContainerMenu {
    public static final int EQUIPMENT_SLOT = 0;
    public static final int SCROLL_SLOT = 1;
    public static final int MATERIAL_SLOT = 2;
    public static final int OUTPUT_SLOT = 3;
    private static final int INTERNAL_SLOTS = 4;

    private final SimpleContainer smithing;
    private final BlockPos anvilPos;
    private boolean updating;
    private int materialCost;
    private boolean combiningScrolls;

    public GOTSmithingMenu(int id, Inventory inventory, FriendlyByteBuf buffer) {
        this(id, inventory, buffer.readBlockPos());
    }

    public GOTSmithingMenu(int id, Inventory inventory, BlockPos pos) {
        super(GOTMenus.GOT_SMITHING.get(), id);
        this.anvilPos = pos;
        this.smithing = new SimpleContainer(INTERNAL_SLOTS);
        this.smithing.addListener(this::slotsChanged);

        addSlot(new Slot(smithing, EQUIPMENT_SLOT, 26, 35) {
            @Override public boolean mayPlace(ItemStack stack) {
                return !stack.isEmpty();
            }
        });
        addSlot(new Slot(smithing, SCROLL_SLOT, 76, 35) {
            @Override public boolean mayPlace(ItemStack stack) {
                return stack.getItem() == GOTItems.SMITH_SCROLL.get()
                        || stack.getItem() instanceof GOTSpecialEnchantmentItem
                        || GOTAnvilNameColors.isNameColorGem(stack);
            }
        });
        addSlot(new Slot(smithing, MATERIAL_SLOT, 126, 35));
        addSlot(new Slot(smithing, OUTPUT_SLOT, 152, 35) {
            @Override public boolean mayPlace(ItemStack stack) { return false; }

            @Override
            public void onTake(Player player, ItemStack stack) {
                ItemStack first = smithing.getItem(EQUIPMENT_SLOT);
                ItemStack second = smithing.getItem(SCROLL_SLOT);
                ItemStack material = smithing.getItem(MATERIAL_SLOT);

                if (combiningScrolls) {
                    if (!first.isEmpty()) first.shrink(1);
                    if (!second.isEmpty()) second.shrink(1);
                } else {
                    if (!first.isEmpty()) first.shrink(1);
                    if (!second.isEmpty()) second.shrink(1);
                    if (!material.isEmpty()) material.shrink(Math.min(material.getCount(), materialCost));
                }

                smithing.setItem(OUTPUT_SLOT, ItemStack.EMPTY);
                smithing.setChanged();

                if (!player.level().isClientSide) {
                    player.level().playSound(null, anvilPos, SoundEvents.ANVIL_USE,
                            SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                super.onTake(player, stack);
            }
        });

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(inventory, col + row * 9 + 9,
                        8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(inventory, col, 8 + col * 18, 142));
        }
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        if (updating) return;
        updating = true;

        ItemStack equipment = smithing.getItem(EQUIPMENT_SLOT);
        ItemStack scroll = smithing.getItem(SCROLL_SLOT);
        ItemStack material = smithing.getItem(MATERIAL_SLOT);

        ItemStack result = ItemStack.EMPTY;
        materialCost = 0;
        combiningScrolls = false;

        // Vanilla enchanted-book style combining:
        // same Smith Scroll + same Smith Scroll -> next proven tier.
        if (GOTSmithScrollUpgrades.canCombine(equipment, scroll)) {
            result = GOTSmithScrollUpgrades.combine(equipment, scroll);
            combiningScrolls = !result.isEmpty();
        } else if (!equipment.isEmpty() && scroll.getItem() instanceof GOTSpecialEnchantmentItem specialItem) {
            GOTSmithingModifier enchant = specialItem.modifier();
            materialCost = enchant.materialCost();

            boolean validMaterial = !material.isEmpty()
                    && equipment.getItem().isValidRepairItem(equipment, material)
                    && material.getCount() >= materialCost;

            if (validMaterial && GOTSmithingModifierData.canAdd(equipment, enchant)) {
                result = equipment.copy();
                result.setCount(1);
                GOTSmithingModifierData.add(result, enchant);
            }
        } else if (!equipment.isEmpty() && scroll.getItem() == GOTItems.SMITH_SCROLL.get()) {
            var modifier = GOTSmithScrollItem.getModifier(scroll);
            if (modifier.isPresent()) {
                GOTSmithingModifier enchant = modifier.get();
                materialCost = enchant.materialCost();

                boolean validMaterial = !material.isEmpty()
                        && equipment.getItem().isValidRepairItem(equipment, material)
                        && material.getCount() >= materialCost;

                if (validMaterial && GOTSmithingModifierData.canAdd(equipment, enchant)) {
                    result = equipment.copy();
                    result.setCount(1);
                    GOTSmithingModifierData.add(result, enchant);
                    if (enchant.effect() == GOTSmithingEffect.SILK_TOUCH) {
                        result.enchant(net.minecraft.world.item.enchantment.Enchantments.SILK_TOUCH, 1);
                    }
                }
            }
        }

        smithing.setItem(OUTPUT_SLOT, result);
        updating = false;
        broadcastChanges();
    }



    public int engraveOwnerCost() {
        ItemStack equipment = smithing.getItem(EQUIPMENT_SLOT);
        if (equipment.isEmpty()) return 0;
        return 2;
    }

    public boolean canEngraveOwner(Player player) {
        ItemStack equipment = smithing.getItem(EQUIPMENT_SLOT);
        ItemStack material = smithing.getItem(MATERIAL_SLOT);
        int cost = engraveOwnerCost();
        return cost > 0
                && smithing.getItem(SCROLL_SLOT).isEmpty()
                && GOTItemOwnership.canEngraveNewOwner(equipment, player.getGameProfile().getName())
                && !material.isEmpty()
                && equipment.getItem().isValidRepairItem(equipment, material)
                && material.getCount() >= cost;
    }

    public void engraveOwner(Player player) {
        if (player.level().isClientSide || !canEngraveOwner(player)) return;

        ItemStack equipment = smithing.getItem(EQUIPMENT_SLOT);
        ItemStack material = smithing.getItem(MATERIAL_SLOT);
        GOTItemOwnership.setCurrentOwner(equipment, player.getGameProfile().getName());
        material.shrink(engraveOwnerCost());
        smithing.setChanged();
        broadcastChanges();

        if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            got.achievement.GOTAchievementHooks.award(serverPlayer, "ENGRAVE_OWNERSHIP");
        }

        player.level().playSound(null, anvilPos, SoundEvents.ANVIL_USE,
                SoundSource.BLOCKS, 1.0F, 1.05F);
    }

    public boolean canRename() {
        return !smithing.getItem(EQUIPMENT_SLOT).isEmpty();
    }

    public void rename(Player player, String rawName) {
        if (player.level().isClientSide || !canRename()) return;
        ItemStack equipment = smithing.getItem(EQUIPMENT_SLOT);

        String name = rawName == null ? "" : rawName.trim();
        if (name.length() > 50) name = name.substring(0, 50);

        net.minecraft.network.chat.MutableComponent component;
        if (name.isBlank()) {
            equipment.resetHoverName();
            component = null;
        } else {
            component = net.minecraft.network.chat.Component.literal(name);

            ItemStack colorGem = smithing.getItem(SCROLL_SLOT);
            net.minecraft.ChatFormatting color = GOTAnvilNameColors.colorFor(colorGem);
            if (color != null) {
                component.withStyle(color);
                colorGem.shrink(1);
            }
            equipment.setHoverName(component);
        }

        smithing.setChanged();
        broadcastChanges();
        player.level().playSound(null, anvilPos, SoundEvents.ANVIL_USE,
                SoundSource.BLOCKS, 0.8F, 1.2F);
    }

    public int reforgeCost() {
        ItemStack equipment = smithing.getItem(EQUIPMENT_SLOT);
        return GOTSmithingReforge.materialCost(equipment);
    }

    public boolean canReforge() {
        ItemStack equipment = smithing.getItem(EQUIPMENT_SLOT);
        ItemStack material = smithing.getItem(MATERIAL_SLOT);
        int cost = GOTSmithingReforge.materialCost(equipment);
        return cost > 0
                && smithing.getItem(SCROLL_SLOT).isEmpty()
                && !material.isEmpty()
                && equipment.getItem().isValidRepairItem(equipment, material)
                && material.getCount() >= cost;
    }

    public void reforge(Player player) {
        if (player.level().isClientSide || !canReforge()) return;

        ItemStack equipment = smithing.getItem(EQUIPMENT_SLOT);
        ItemStack material = smithing.getItem(MATERIAL_SLOT);
        int cost = GOTSmithingReforge.materialCost(equipment);

        ItemStack result = GOTSmithingReforge.reforge(equipment, player.getRandom());
        smithing.setItem(EQUIPMENT_SLOT, result);
        material.shrink(cost);
        smithing.setItem(OUTPUT_SLOT, ItemStack.EMPTY);
        smithing.setChanged();
        broadcastChanges();

        if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            got.achievement.GOTAchievementHooks.award(serverPlayer, "REFORGE");
        }

        player.level().playSound(null, anvilPos, SoundEvents.ANVIL_USE,
                SoundSource.BLOCKS, 1.0F, 0.9F + player.getRandom().nextFloat() * 0.2F);
    }

    public int materialCost() {
        return materialCost;
    }

    public boolean combiningScrolls() {
        return combiningScrolls;
    }

    @Override
    public boolean stillValid(Player player) {
        if (!(player.level().getBlockState(anvilPos).getBlock() instanceof AnvilBlock)) return false;
        return player.distanceToSqr(
                anvilPos.getX() + 0.5D,
                anvilPos.getY() + 0.5D,
                anvilPos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();
        int playerStart = INTERNAL_SLOTS;
        int playerEnd = playerStart + 36;

        if (index == OUTPUT_SLOT) {
            if (!moveItemStackTo(stack, playerStart, playerEnd, true)) return ItemStack.EMPTY;
            slot.onQuickCraft(stack, copy);
        } else if (index < INTERNAL_SLOTS) {
            if (!moveItemStackTo(stack, playerStart, playerEnd, false)) return ItemStack.EMPTY;
        } else if (stack.getItem() == GOTItems.SMITH_SCROLL.get()) {
            if (smithing.getItem(EQUIPMENT_SLOT).isEmpty()) {
                if (!moveItemStackTo(stack, EQUIPMENT_SLOT, EQUIPMENT_SLOT + 1, false)) return ItemStack.EMPTY;
            } else {
                if (!moveItemStackTo(stack, SCROLL_SLOT, SCROLL_SLOT + 1, false)) return ItemStack.EMPTY;
            }
        } else if (!smithing.getItem(EQUIPMENT_SLOT).isEmpty()
                && smithing.getItem(EQUIPMENT_SLOT).getItem()
                .isValidRepairItem(smithing.getItem(EQUIPMENT_SLOT), stack)) {
            if (!moveItemStackTo(stack, MATERIAL_SLOT, MATERIAL_SLOT + 1, false)) return ItemStack.EMPTY;
        } else if (!moveItemStackTo(stack, EQUIPMENT_SLOT, EQUIPMENT_SLOT + 1, false)) {
            if (index < playerStart + 27) {
                if (!moveItemStackTo(stack, playerStart + 27, playerEnd, false)) return ItemStack.EMPTY;
            } else if (!moveItemStackTo(stack, playerStart, playerStart + 27, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (stack.isEmpty()) slot.set(ItemStack.EMPTY); else slot.setChanged();
        if (stack.getCount() == copy.getCount()) return ItemStack.EMPTY;
        slot.onTake(player, stack);
        return copy;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!player.level().isClientSide) {
            clearContainer(player, smithing);
        }
    }
}
