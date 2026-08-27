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

/** Exact Golden Company equipment and zero-alignment unit contracts. */
final class GOTGoldenCompanyNpcLoadouts {
    private GOTGoldenCompanyNpcLoadouts() {}

    static void configure(GOTGoldenCompanyNpcEntity npc) {
        npc.clearLoadout();
        switch (npc.getRole()) {
            case GOLDEN_COMPANY_WARRIOR -> {
                ItemStack sword = new ItemStack(Items.IRON_SWORD);
                npc.setWeapons(sword, sword);
                goldenArmor(npc, true);
            }
            case GOLDEN_COMPANY_SPEARMAN -> {
                ItemStack pike = stack("got:iron_pike");
                npc.setWeapons(pike, pike);
                goldenArmor(npc, true);
            }
            case GOLDEN_COMPANY_BANNER_BEARER -> {
                ItemStack dagger = stack("got:iron_dagger");
                npc.setWeapons(dagger, dagger);
                goldenArmor(npc, true);
                npc.setItemSlot(EquipmentSlot.OFFHAND,
                        GOTBannerItem.createStack(GOTBannerType.byName("golden_company")));
            }
            case GOLDEN_COMPANY_CAPTAIN -> {
                ItemStack sword = new ItemStack(Items.IRON_SWORD);
                npc.setWeapons(sword, sword);
                goldenArmor(npc, false);
            }
            case HARRY_STRICKLAND -> {
                ItemStack sword = stack("got:valyrian_sword");
                npc.setWeapons(sword, sword);
            }
        }
        GOTNpcShieldLoadouts.equip(npc, npc.getRole(), "got:golden_company_shield");
        npc.updateHeldItem();
    }

    static MerchantOffers createOffers(GoldenCompanyNpcRole role) {
        MerchantOffers offers = new MerchantOffers();
        if (role.trade() == GoldenCompanyNpcRole.Trade.UNITS) {
            contract(offers, GoldenCompanyNpcRole.GOLDEN_COMPANY_WARRIOR);
            contract(offers, GoldenCompanyNpcRole.GOLDEN_COMPANY_SPEARMAN);
            contract(offers, GoldenCompanyNpcRole.GOLDEN_COMPANY_BANNER_BEARER);
        }
        got.GOTSmithScrollAcquisition.addBlacksmithOffers(offers, role);
        return offers;
    }

    private static void contract(MerchantOffers offers, GoldenCompanyNpcRole role) {
        offers.add(new MerchantOffer(stack("got:coin_1", 10),
                GOTGoldenCompanyNpcSpawnerItem.createContract(role), 12, 2, 0.05F));
    }

    private static void goldenArmor(GOTGoldenCompanyNpcEntity npc, boolean helmet) {
        npc.setItemSlot(EquipmentSlot.FEET, stack("got:golden_company_boots"));
        npc.setItemSlot(EquipmentSlot.LEGS, stack("got:golden_company_leggings"));
        npc.setItemSlot(EquipmentSlot.CHEST, stack("got:golden_company_chestplate"));
        if (helmet) npc.setItemSlot(EquipmentSlot.HEAD, stack("got:golden_company_helmet"));
    }

    static ItemStack stack(String id) { return stack(id, 1); }
    static ItemStack stack(String id, int count) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
        return item == null ? ItemStack.EMPTY : new ItemStack(item, count);
    }
}
