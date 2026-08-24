package got.npc;

import got.GOTBannerItem;
import got.GOTBannerType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraftforge.registries.ForgeRegistries;

/** Equipment and trade pools translated from the original Asshai classes. */
final class GOTAsshaiNpcLoadouts {
    private GOTAsshaiNpcLoadouts() {}

    static void configure(GOTAsshaiNpcEntity npc) {
        AsshaiNpcRole role = npc.getRole();
        npc.clearLoadout();
        switch (role) {
            case ASSHAI_MAN -> npc.setCombatWeapon(stack("got:iron_dagger"));
            case ASSHAI_WARRIOR -> {
                ItemStack weapon = ironWeapon(npc);
                npc.setWeapons(weapon, weapon);
                asshaiArmor(npc, true);
            }
            case ASSHAI_SHADOWBINDER -> {
                ItemStack staff = stack("got:asshai_shadowbinder_staff");
                npc.setWeapons(staff, staff);
                npc.setRangedWeapon(staff);
                asshaiArmor(npc, false);
                npc.setItemSlot(EquipmentSlot.HEAD, stack("got:asshai_mask"));
            }
            case ASSHAI_SPHEREBINDER -> {
                ItemStack torch = stack("got:asshai_torch");
                npc.setWeapons(torch, torch);
                asshaiArmor(npc, false);
                npc.setItemSlot(EquipmentSlot.HEAD, stack("got:asshai_mask"));
            }
            case ASSHAI_BANNER_BEARER -> {
                ItemStack dagger = stack("got:iron_dagger");
                npc.setWeapons(dagger, dagger);
                asshaiArmor(npc, true);
                npc.setItemSlot(EquipmentSlot.OFFHAND,
                        GOTBannerItem.createStack(GOTBannerType.byName("asshai")));
            }
            case ASSHAI_CAPTAIN -> {
                ItemStack sword = new ItemStack(Items.IRON_SWORD);
                npc.setWeapons(sword, sword);
                asshaiArmor(npc, false);
            }
            case ASSHAI_ALCHEMIST -> {
                ItemStack staff = stack("got:skull_staff");
                npc.setWeapons(staff, staff);
            }
            case ASSHAI_ARCHMAG -> {
                ItemStack staff = stack("got:asshai_archmag_staff");
                npc.setWeapons(staff, staff);
                npc.setRangedWeapon(staff);
                asshaiArmor(npc, false);
                npc.setItemSlot(EquipmentSlot.HEAD, stack("got:asshai_mask"));
            }
        }
        npc.updateHeldItem();
    }

    static MerchantOffers createOffers(AsshaiNpcRole role) {
        MerchantOffers offers = new MerchantOffers();
        switch (role.trade()) {
            case ALCHEMIST -> {
                buy(offers, "minecraft:blaze_powder", 6, 5);
                buy(offers, "minecraft:fermented_spider_eye", 4, 5);
                sell(offers, 8, "minecraft:glowstone_dust", 4);
                sell(offers, 10, "got:mug_poppy_milk", 1);
                sell(offers, 16, "got:skull_staff", 1);
            }
            case UNITS -> {
                sell(offers, 12, "got:command_horn", 1);
                sell(offers, 16, "got:asshai_chestplate", 1);
                sell(offers, 12, "got:asshai_helmet", 1);
                sell(offers, 10, "minecraft:iron_sword", 1);
            }
            default -> { }
        }
        got.GOTSmithScrollAcquisition.addBlacksmithOffers(offers, role);
        return offers;
    }

    private static void asshaiArmor(GOTAsshaiNpcEntity npc, boolean helmet) {
        npc.setItemSlot(EquipmentSlot.FEET, stack("got:asshai_boots"));
        npc.setItemSlot(EquipmentSlot.LEGS, stack("got:asshai_leggings"));
        npc.setItemSlot(EquipmentSlot.CHEST, stack("got:asshai_chestplate"));
        if (helmet) npc.setItemSlot(EquipmentSlot.HEAD, stack("got:asshai_helmet"));
    }

    private static ItemStack ironWeapon(GOTAsshaiNpcEntity npc) {
        return switch (npc.getRandom().nextInt(4)) {
            case 0 -> new ItemStack(Items.IRON_SWORD);
            case 1 -> stack("got:iron_spear");
            case 2 -> stack("got:iron_pike");
            default -> stack("got:iron_battleaxe");
        };
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
