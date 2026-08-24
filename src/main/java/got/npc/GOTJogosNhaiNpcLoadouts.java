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

/** Equipment and trade pools translated from the original JogosNhai classes. */
final class GOTJogosNhaiNpcLoadouts {
    private GOTJogosNhaiNpcLoadouts() {}

    static void configure(GOTJogosNhaiNpcEntity npc) {
        JogosNhaiNpcRole role = npc.getRole();
        npc.clearLoadout();
        switch (role) {
            case JOGOS_NHAI_MAN -> {
                ItemStack weapon = primitiveWeapon(npc);
                npc.setWeapons(weapon, weapon);
                jogosNhaiArmor(npc);
            }
            case JOGOS_NHAI_ARCHER -> {
                ItemStack melee = primitiveWeapon(npc);
                ItemStack bow = stack("got:longbow");
                npc.setWeapons(melee, bow);
                npc.setRangedWeapon(bow);
                jogosNhaiArmor(npc);
            }
            case JOGOS_NHAI_CHIEFTAIN -> {
                ItemStack scimitar = stack("got:iron_scimitar");
                npc.setWeapons(scimitar, scimitar);
                jogosNhaiArmor(npc);
                npc.setItemSlot(EquipmentSlot.HEAD, stack("got:jogos_nhai_helmet"));
            }
            case JOGOS_NHAI_SHAMAN -> npc.setWeapons(ItemStack.EMPTY, stack("got:mug_mead"));
            case TUGAR_KHAN -> {
                ItemStack sword = stack("got:alloy_steel_scimitar");
                npc.setWeapons(sword, sword);
            }
        }
        npc.updateHeldItem();
    }

    static MerchantOffers createOffers(JogosNhaiNpcRole role) {
        MerchantOffers offers = new MerchantOffers();
        switch (role.trade()) {
            case BUTCHER -> {
                buy(offers, "minecraft:beef", 12, 2);
                buy(offers, "minecraft:porkchop", 12, 2);
                sell(offers, 3, "minecraft:cooked_beef", 5);
                sell(offers, 3, "minecraft:cooked_porkchop", 5);
            }
            case UNITS -> {
                sell(offers, 12, "got:command_horn", 1);
                sell(offers, 16, "got:jogos_nhai_chestplate", 1);
                sell(offers, 10, "got:iron_scimitar", 1);
            }
            default -> { }
        }
        got.GOTSmithScrollAcquisition.addBlacksmithOffers(offers, role);
        return offers;
    }

    private static void jogosNhaiArmor(GOTJogosNhaiNpcEntity npc) {
        npc.setItemSlot(EquipmentSlot.FEET, stack("got:jogos_nhai_boots"));
        npc.setItemSlot(EquipmentSlot.LEGS, stack("got:jogos_nhai_leggings"));
        npc.setItemSlot(EquipmentSlot.CHEST, stack("got:jogos_nhai_chestplate"));
    }

    private static ItemStack primitiveWeapon(GOTJogosNhaiNpcEntity npc) {
        return switch (npc.getRandom().nextInt(4)) {
            case 0 -> stack("got:iron_scimitar");
            case 1 -> stack("got:iron_spear");
            case 2 -> stack("got:iron_dagger");
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
