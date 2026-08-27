package got.npc;

import got.GOTBannerItem;
import got.GOTBannerType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraftforge.registries.ForgeRegistries;

/** Equipment and trade pools translated from the original Qohor classes. */
final class GOTQohorNpcLoadouts {
    private GOTQohorNpcLoadouts() {}

    static void configure(GOTQohorNpcEntity npc) {
        QohorNpcRole role = npc.getRole();
        npc.clearLoadout();
        switch (role) {
            case QOHOR_MAN -> npc.setCombatWeapon(stack("got:iron_dagger"));
            case QOHOR_LEVYMAN -> {
                npc.setCombatWeapon(randomIronWeapon(npc));
                levymanArmor(npc);
            }
            case QOHOR_LEVYMAN_ARCHER -> {
                ItemStack melee = randomIronWeapon(npc);
                ItemStack bow = stack("got:longbow");
                npc.setWeapons(melee, bow);
                npc.setRangedWeapon(bow);
                levymanArmor(npc);
            }
            case QOHOR_SOLDIER -> {
                ItemStack weapon = randomIronWeapon(npc);
                npc.setWeapons(weapon, weapon);
                qohorArmor(npc, true);
            }
            case QOHOR_SOLDIER_ARCHER -> {
                ItemStack melee = randomIronWeapon(npc);
                ItemStack bow = stack("got:longbow");
                npc.setWeapons(melee, bow);
                npc.setRangedWeapon(bow);
                qohorArmor(npc, true);
            }
            case QOHOR_BANNER_BEARER -> {
                ItemStack dagger = stack("got:iron_dagger");
                npc.setWeapons(dagger, dagger);
                qohorArmor(npc, true);
                npc.setItemSlot(EquipmentSlot.OFFHAND,
                        GOTBannerItem.createStack(GOTBannerType.byName("qohor")));
            }
            case QOHOR_CAPTAIN -> {
                ItemStack sword = new ItemStack(Items.IRON_SWORD);
                npc.setWeapons(sword, sword);
                qohorArmor(npc, false);
            }
            case QOHOR_BLACKSMITH -> {
                ItemStack hammer = stack("got:blacksmith_hammer");
                npc.setWeapons(hammer, hammer);
            }
            case QOHOR_GOLDSMITH ->
                    npc.setWeapons(stack("got:iron_dagger"), stack("got:silver_ring"));
            case QOHOR_FARMER -> {
                ItemStack hoe = new ItemStack(Items.IRON_HOE);
                npc.setWeapons(hoe, hoe);
            }
            case QOHOR_FARMHAND -> {
                ItemStack hoe = new ItemStack(Items.IRON_HOE);
                npc.setWeapons(hoe, hoe);
            }
            case QOHOR_BARTENDER ->
                    npc.setWeapons(stack("got:iron_dagger"), stack("got:copper_goblet"));
            case QOHOR_MINER -> {
                ItemStack pickaxe = new ItemStack(Items.IRON_PICKAXE);
                npc.setWeapons(pickaxe, pickaxe);
            }
            case QOHOR_LUMBERMAN -> {
                ItemStack axe = new ItemStack(Items.IRON_AXE);
                npc.setWeapons(axe, axe);
            }
            case QOHOR_MASON ->
                    npc.setWeapons(new ItemStack(Items.IRON_PICKAXE), new ItemStack(Items.STONE));
            case QOHOR_BREWER ->
                    npc.setWeapons(stack("got:iron_dagger"), stack("got:mug_ale"));
            case QOHOR_FLORIST -> {
                npc.setWeapons(stack("got:iron_dagger"), new ItemStack(Items.POPPY));
            }
            case QOHOR_BUTCHER ->
                    npc.setWeapons(stack("got:iron_dagger"), new ItemStack(Items.BEEF));
            case QOHOR_FISHMONGER -> {
                npc.setWeapons(stack("got:iron_dagger"), new ItemStack(Items.COD));
            }
            case QOHOR_BAKER ->
                    npc.setWeapons(stack("got:rolling_pin"), new ItemStack(Items.BREAD));
            case QOHOR_UNSULLIED -> {
                ItemStack pike = stack("got:iron_pike");
                npc.setWeapons(pike, pike);
                unsulliedArmor(npc);
            }
            default -> { }
        }
        switch (role) {
            case QOHOR_BAKER, QOHOR_BARTENDER, QOHOR_BLACKSMITH,
                    QOHOR_BREWER, QOHOR_BUTCHER, QOHOR_FARMER,
                    QOHOR_FISHMONGER, QOHOR_FLORIST, QOHOR_GOLDSMITH,
                    QOHOR_LUMBERMAN, QOHOR_MASON, QOHOR_MINER ->
                    npc.setItemSlot(EquipmentSlot.HEAD, stack("got:robes_helmet"));
            default -> { }
        }
        GOTNpcShieldLoadouts.equip(npc, npc.getRole(), "got:qohor_shield");
        npc.updateHeldItem();
    }

    static MerchantOffers createOffers(QohorNpcRole role) {
        MerchantOffers offers = new MerchantOffers();
        switch (role.trade()) {
            case BAKER -> {
                buy(offers, "minecraft:wheat", 16, 2);
                sell(offers, 2, "minecraft:bread", 4);
                sell(offers, 5, "got:apple_crumble", 1);
            }
            case BARTENDER -> {
                buy(offers, "minecraft:wheat", 20, 2);
                sell(offers, 3, "got:mug_ale", 1);
                sell(offers, 4, "got:mug_cider", 1);
                sell(offers, 5, "got:mug_mead", 1);
            }
            case BLACKSMITH -> {
                sell(offers, 8, "got:blacksmith_hammer", 1);
                buy(offers, "minecraft:coal", 16, 2);
                buy(offers, "minecraft:iron_ingot", 8, 4);
                sell(offers, 8, "minecraft:iron_sword", 1);
                sell(offers, 10, "got:iron_pike", 1);
            }
            case BREWER -> {
                buy(offers, "minecraft:wheat", 20, 2);
                buy(offers, "minecraft:sugar", 16, 2);
                sell(offers, 3, "got:mug_ale", 1);
                sell(offers, 4, "got:mug_cider", 1);
            }
            case BUTCHER -> {
                buy(offers, "minecraft:beef", 12, 2);
                buy(offers, "minecraft:porkchop", 12, 2);
                sell(offers, 3, "minecraft:cooked_beef", 5);
                sell(offers, 3, "minecraft:cooked_porkchop", 5);
            }
            case FARMER -> {
                sell(offers, 4, "got:branding_iron", 1);
                // Guaranteed Farmer utility trade.
                sell(offers, 8, "got:millstone", 1);
                buy(offers, "minecraft:wheat", 20, 2);
                buy(offers, "minecraft:carrot", 18, 2);
                sell(offers, 2, "minecraft:wheat_seeds", 12);
                sell(offers, 3, "got:leek", 6);
            }
            case FISHMONGER -> {
                buy(offers, "minecraft:cod", 12, 2);
                buy(offers, "minecraft:salmon", 10, 2);
                sell(offers, 3, "minecraft:cooked_cod", 5);
                sell(offers, 3, "minecraft:cooked_salmon", 5);
            }
            case FLORIST -> {
                buy(offers, "minecraft:bone_meal", 16, 2);
                sell(offers, 2, "minecraft:poppy", 6);
                sell(offers, 2, "minecraft:dandelion", 6);
            }
            case GOLDSMITH -> {
                buy(offers, "minecraft:gold_ingot", 4, 5);
                buy(offers, "got:silver_ingot", 6, 5);
                sell(offers, 10, "got:silver_ring", 1);
                sell(offers, 12, "got:gold_ring", 1);
            }
            case LUMBERMAN -> {
                buy(offers, "minecraft:oak_log", 16, 2);
                sell(offers, 2, "minecraft:oak_planks", 24);
                sell(offers, 4, "minecraft:charcoal", 8);
            }
            case MASON -> {
                buy(offers, "minecraft:cobblestone", 32, 2);
                sell(offers, 2, "minecraft:stone_bricks", 16);
                sell(offers, 3, "minecraft:bricks", 12);
            }
            case MINER -> {
                buy(offers, "minecraft:coal", 16, 2);
                buy(offers, "minecraft:raw_iron", 8, 4);
                sell(offers, 4, "minecraft:torch", 16);
                sell(offers, 8, "minecraft:iron_pickaxe", 1);
            }
            case UNITS -> {
                sell(offers, 16, "got:warhorn", 1);
                sell(offers, 12, "got:command_horn", 1);
                sell(offers, 16, "got:qohor_chestplate", 1);
                sell(offers, 12, "got:qohor_helmet", 1);
                sell(offers, 10, "minecraft:iron_sword", 1);
            }
            default -> { }
        }
        got.GOTSmithScrollAcquisition.addBlacksmithOffers(offers, role);
        return offers;
    }

    private static void qohorArmor(GOTQohorNpcEntity npc, boolean helmet) {
        npc.setItemSlot(EquipmentSlot.FEET, stack("got:qohor_boots"));
        npc.setItemSlot(EquipmentSlot.LEGS, stack("got:qohor_leggings"));
        npc.setItemSlot(EquipmentSlot.CHEST, stack("got:qohor_chestplate"));
        if (helmet) npc.setItemSlot(EquipmentSlot.HEAD, stack("got:qohor_helmet"));
    }

    private static void unsulliedArmor(GOTQohorNpcEntity npc) {
        npc.setItemSlot(EquipmentSlot.FEET, stack("got:unsullied_boots"));
        npc.setItemSlot(EquipmentSlot.LEGS, stack("got:unsullied_leggings"));
        npc.setItemSlot(EquipmentSlot.CHEST, stack("got:unsullied_chestplate"));
        npc.setItemSlot(EquipmentSlot.HEAD, stack("got:unsullied_helmet"));
    }

    private static void levymanArmor(GOTQohorNpcEntity npc) {
        npc.setItemSlot(EquipmentSlot.FEET, dyedLeather(Items.LEATHER_BOOTS, npc));
        int legsRoll = npc.getRandom().nextInt(10);
        boolean heavyLegs = legsRoll < 5;
        if (legsRoll < 2) {
            npc.setItemSlot(EquipmentSlot.LEGS, stack("got:bronze_chainmail_leggings"));
        } else if (legsRoll < 5) {
            npc.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
        } else {
            npc.setItemSlot(EquipmentSlot.LEGS, dyedLeather(Items.LEATHER_LEGGINGS, npc));
        }
        int chestRoll = npc.getRandom().nextInt(10);
        if (!heavyLegs && chestRoll < 2) {
            npc.setItemSlot(EquipmentSlot.CHEST, stack("got:bronze_chainmail_chestplate"));
        } else if (!heavyLegs && chestRoll < 5) {
            npc.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
        } else {
            npc.setItemSlot(EquipmentSlot.CHEST, dyedLeather(Items.LEATHER_CHESTPLATE, npc));
        }
        npc.setItemSlot(EquipmentSlot.HEAD, stack("got:robes_helmet"));
    }

    private static ItemStack dyedLeather(Item item, GOTQohorNpcEntity npc) {
        int[] colors = {0xA5A5A5, 0x7A7A7A, 0x545454, 0x383838, 0x7F6A59,
                0x9E826E, 0x493D33, 0x515B6B, 0x404499, 0x282B38};
        ItemStack stack = new ItemStack(item);
        if (stack.getItem() instanceof DyeableLeatherItem dyeable) {
            dyeable.setColor(stack, colors[npc.getRandom().nextInt(colors.length)]);
        }
        return stack;
    }

    private static ItemStack randomIronWeapon(GOTQohorNpcEntity npc) {
        ItemStack weapon = switch (npc.getRandom().nextInt(100)) {
            case 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                    10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
                    20, 21, 22, 23, 24, 25, 26, 27, 28, 29,
                    30, 31, 32, 33, 34, 35, 36, 37, 38, 39,
                    40, 41, 42, 43, 44, 45, 46, 47, 48, 49 -> new ItemStack(Items.IRON_SWORD);
            case 50, 51, 52, 53, 54, 55, 56, 57, 58, 59,
                    60, 61, 62, 63, 64, 65, 66, 67, 68, 69 -> stack("got:iron_battleaxe");
            case 70, 71, 72, 73, 74, 75, 76, 77, 78, 79,
                    80, 81, 82, 83, 84, 85, 86, 87, 88, 89 -> stack("got:iron_hammer");
            default -> stack("got:iron_pike");
        };
        return npc.getRandom().nextInt(5) == 0 ? stack("got:iron_spear") : weapon;
    }

    private static void buy(MerchantOffers offers, String input, int inputCount, int coins) {
        add(offers, input, inputCount, "got:coin_1", coins);
    }

    private static void sell(MerchantOffers offers, int coins, String output, int outputCount) {
        add(offers, "got:coin_1", coins, output, outputCount);
    }

    private static void add(MerchantOffers offers, String input, int inputCount,
                            String output, int outputCount) {
        ItemStack cost = stack(input, inputCount);
        ItemStack result = stack(output, outputCount);
        if (!cost.isEmpty() && !result.isEmpty()) {
            offers.add(new MerchantOffer(cost, result, 12, 2, 0.05F));
        }
    }

    static ItemStack stack(String id) { return stack(id, 1); }

    static ItemStack stack(String id, int count) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
        return item == null ? ItemStack.EMPTY : new ItemStack(item, count);
    }
}
