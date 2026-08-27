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

/** Legacy Free Folk/Thenn weapons, armor and trades. */
final class GOTWildlingNpcLoadouts {
    private GOTWildlingNpcLoadouts() {}

    static void configure(GOTWildlingNpcEntity npc) {
        WildlingNpcRole role = npc.getRole();
        npc.clearLoadout();
        switch (role) {
            case WILDLING, THENN, CRASTER_WIFE -> npc.setCombatWeapon(stack("got:iron_dagger"));
            case WILDLING_WARRIOR, THENN_WARRIOR -> { npc.setCombatWeapon(primitiveWeapon(npc)); furArmor(npc, true); }
            case WILDLING_ARCHER, THENN_ARCHER, YGRITTE -> {
                ItemStack melee = role == WildlingNpcRole.YGRITTE ? stack("got:alloy_steel_dagger") : primitiveWeapon(npc);
                ItemStack bow = stack("got:longbow");
                npc.setWeapons(melee, bow); npc.setRangedWeapon(bow);
                if (role != WildlingNpcRole.YGRITTE) furArmor(npc, true);
            }
            case WILDLING_AXE_THROWER, THENN_AXE_THROWER -> {
                ItemStack melee = primitiveWeapon(npc); ItemStack axe = stack("got:iron_throwing_axe");
                npc.setWeapons(melee, axe); npc.setRangedWeapon(axe); furArmor(npc, true);
            }
            case WILDLING_BANNER_BEARER, THENN_BANNER_BEARER -> {
                ItemStack dagger = stack("got:iron_dagger"); npc.setWeapons(dagger, dagger); furArmor(npc, true);
                npc.setItemSlot(EquipmentSlot.OFFHAND, GOTBannerItem.createStack(
                        GOTBannerType.byName(role.thenn() ? "thenn" : "wildling")));
            }
            case WILDLING_CHIEFTAIN, THENN_CHIEFTAIN -> {
                ItemStack staff = stack("got:skull_staff"); npc.setWeapons(staff, staff); furArmor(npc, false);
            }
            case THENN_BLACKSMITH -> { ItemStack hammer=stack("got:blacksmith_hammer");npc.setWeapons(hammer,hammer); }
            case MANCE_RAYDER, TORMUND -> { ItemStack sword=stack("got:alloy_steel_sword");npc.setWeapons(sword,sword); }
            case CRASTER -> npc.setCombatWeapon(stack("got:iron_dagger"));
            case GIANT -> { }
        }
        npc.updateHeldItem();
    }

    static MerchantOffers createOffers(WildlingNpcRole role) {
        MerchantOffers legendary = got.economy.GOTLegendaryTraderOffers.forRole(role.id());
        if (legendary != null) return legendary;
        MerchantOffers offers = new MerchantOffers();
        switch (role.trade()) {
            case BLACKSMITH -> {
                sell(offers, 8, "got:blacksmith_hammer", 1); buy(offers,"minecraft:coal",16,2);buy(offers,"minecraft:iron_ingot",8,4);sell(offers,8,"minecraft:iron_axe",1);sell(offers,10,"got:iron_throwing_axe",3); }
            case UNITS -> {
                sell(offers, 16, "got:warhorn", 1);
                sell(offers, 12, "got:command_horn", 1); sell(offers,10,"got:fur_chestplate",1);sell(offers,8,"got:fur_helmet",1);sell(offers,12,"got:command_horn",1); }
            case THENN_UNITS -> { sell(offers,10,"got:fur_chestplate",1);sell(offers,10,"got:skull_staff",1);sell(offers,12,"got:command_horn",1); }
            case CRASTER -> { buy(offers,"minecraft:rabbit_hide",8,3);buy(offers,"minecraft:mutton",8,3);sell(offers,5,"got:fur",4);sell(offers,4,"minecraft:cooked_mutton",4); }
            default -> { }
        }
        got.GOTSmithScrollAcquisition.addBlacksmithOffers(offers, role);
        return offers;
    }
    private static void furArmor(GOTWildlingNpcEntity npc, boolean helmet){npc.setItemSlot(EquipmentSlot.FEET,stack("got:fur_boots"));npc.setItemSlot(EquipmentSlot.LEGS,stack("got:fur_leggings"));npc.setItemSlot(EquipmentSlot.CHEST,stack("got:fur_chestplate"));if(helmet)npc.setItemSlot(EquipmentSlot.HEAD,stack("got:fur_helmet"));}
    private static ItemStack primitiveWeapon(GOTWildlingNpcEntity npc){return switch(npc.getRandom().nextInt(4)){case 0->new ItemStack(Items.IRON_SWORD);case 1->new ItemStack(Items.IRON_AXE);case 2->stack("got:iron_spear");default->stack("got:iron_battleaxe");};}
    private static void buy(MerchantOffers o,String in,int count,int coins){add(o,in,count,"got:coin_1",coins);} private static void sell(MerchantOffers o,int coins,String out,int count){add(o,"got:coin_1",coins,out,count);}
    private static void add(MerchantOffers o,String in,int ic,String out,int oc){ItemStack a=stack(in,ic),b=stack(out,oc);if(!a.isEmpty()&&!b.isEmpty())o.add(new MerchantOffer(a,b,12,2,0.05F));}
    static ItemStack stack(String id){return stack(id,1);} static ItemStack stack(String id,int count){Item item=ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));return item==null||item==Items.AIR?ItemStack.EMPTY:new ItemStack(item,count);}
}
