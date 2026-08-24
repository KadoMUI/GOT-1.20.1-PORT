package got.npc;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraftforge.registries.ForgeRegistries;

/** Exact five-role Mossovy equipment and trade surface. */
final class GOTMossovyNpcLoadouts {
    private GOTMossovyNpcLoadouts() {}

    static void configure(GOTMossovyNpcEntity npc) {
        npc.clearLoadout();
        switch (npc.getRole()) {
            case MOSSOVY_MAN -> npc.setCombatWeapon(stack("got:iron_dagger"));
            case MOSSOVY_WITCHER -> {
                ItemStack sword = new ItemStack(Items.IRON_SWORD);
                ItemStack crossbow = stack("got:iron_crossbow");
                npc.setWeapons(sword, sword);
                npc.setRangedWeapon(crossbow);
                npc.setItemSlot(EquipmentSlot.FEET, stack("got:mossovy_boots"));
                npc.setItemSlot(EquipmentSlot.LEGS, stack("got:mossovy_leggings"));
                npc.setItemSlot(EquipmentSlot.CHEST, stack("got:mossovy_chestplate"));
            }
            case MOSSOVY_BLACKSMITH -> {
                ItemStack hammer = stack("got:blacksmith_hammer");
                npc.setWeapons(hammer, hammer);
            }
            case MOSSOVY_GOLDSMITH ->
                    npc.setWeapons(stack("got:iron_dagger"), stack("got:silver_ring"));
            case MOSSOVY_BARTENDER ->
                    npc.setWeapons(stack("got:iron_dagger"), stack("got:copper_goblet"));
        }
        npc.updateHeldItem();
    }

    static MerchantOffers createOffers(MossovyNpcRole role) {
        MerchantOffers offers = new MerchantOffers();
        switch (role.trade()) {
            case BARTENDER -> {
                buy(offers, "minecraft:wheat", 20, 2);
                sell(offers, 3, "got:mug_ale", 1);
                sell(offers, 5, "got:mug_mead", 1);
            }
            case BLACKSMITH -> {
                buy(offers, "minecraft:coal", 16, 2);
                buy(offers, "minecraft:iron_ingot", 8, 4);
                sell(offers, 8, "minecraft:iron_sword", 1);
                sell(offers, 10, "got:iron_crossbow", 1);
            }
            case GOLDSMITH -> {
                buy(offers, "minecraft:gold_ingot", 4, 5);
                buy(offers, "got:silver_ingot", 6, 5);
                sell(offers, 10, "got:silver_ring", 1);
                sell(offers, 12, "got:gold_ring", 1);
            }
            case MERCENARY -> offers.add(new MerchantOffer(
                    stack("got:coin_1", 50),
                    GOTMossovyNpcSpawnerItem.createContract(MossovyNpcRole.MOSSOVY_WITCHER),
                    12, 2, 0.05F));
            default -> { }
        }
        got.GOTSmithScrollAcquisition.addBlacksmithOffers(offers, role);
        return offers;
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
