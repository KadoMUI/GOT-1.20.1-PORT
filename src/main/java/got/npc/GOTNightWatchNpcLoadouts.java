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

/** Night's Watch equipment and service trades. */
final class GOTNightWatchNpcLoadouts {
    private GOTNightWatchNpcLoadouts() {}
    static void configure(GOTNightWatchNpcEntity npc){
        NightWatchNpcRole role=npc.getRole();npc.clearLoadout();
        switch(role){
            case GIFT_MAN -> npc.setCombatWeapon(stack("got:iron_dagger"));
            case GIFT_GUARD -> {ItemStack w=ironWeapon(npc);npc.setWeapons(w,w);giftArmor(npc,true);}
            case GIFT_ARCHER -> {ItemStack w=ironWeapon(npc),b=stack("got:longbow");npc.setWeapons(w,b);npc.setRangedWeapon(b);giftArmor(npc,true);}
            case GIFT_BANNER_BEARER -> {ItemStack d=stack("got:iron_dagger");npc.setWeapons(d,d);giftArmor(npc,true);npc.setItemSlot(EquipmentSlot.OFFHAND,GOTBannerItem.createStack(GOTBannerType.byName("night")));}
            case GIFT_BLACKSMITH,HARMUNE,MULLIN -> {ItemStack h=stack("got:blacksmith_hammer");npc.setWeapons(h,h);}
            case JEOR_MORMONT -> legendarySword(npc,"got:longclaw");
            case JON_SNOW -> legendarySword(npc,"got:longclaw");
            case BENJEN_STARK -> legendarySword(npc,"got:valyrian_sword");
            case AEMON_TARGARYEN,SAMWELL_TARLY -> { }
            default -> legendarySword(npc,"got:alloy_steel_sword");
        }
        npc.updateHeldItem();
    }
    static MerchantOffers createOffers(NightWatchNpcRole role){MerchantOffers legendary=got.economy.GOTLegendaryTraderOffers.forRole(role.id());if(legendary!=null)return legendary;MerchantOffers o=new MerchantOffers();switch(role.trade()){
        case BLACKSMITH->{buy(o,"minecraft:coal",16,2);buy(o,"minecraft:iron_ingot",8,4);sell(o,8,"minecraft:iron_sword",1);sell(o,10,"got:iron_spear",1);}
        case UNITS->{sell(o,16,"got:warhorn",1);sell(o,14,"got:gift_chestplate",1);sell(o,10,"got:gift_helmet",1);sell(o,12,"got:command_horn",1);}
        case MAESTER->{buy(o,"minecraft:paper",20,3);sell(o,4,"minecraft:book",3);sell(o,8,"got:mug_poppy_milk",1);}
        default->{}}
        return o;}
    private static void legendarySword(GOTNightWatchNpcEntity n,String id){ItemStack s=stack(id);n.setWeapons(s,s);}
    private static void giftArmor(GOTNightWatchNpcEntity n,boolean helmet){n.setItemSlot(EquipmentSlot.FEET,stack("got:gift_boots"));n.setItemSlot(EquipmentSlot.LEGS,stack("got:gift_leggings"));n.setItemSlot(EquipmentSlot.CHEST,stack("got:gift_chestplate"));if(helmet)n.setItemSlot(EquipmentSlot.HEAD,stack("got:gift_helmet"));}
    private static ItemStack ironWeapon(GOTNightWatchNpcEntity n){return switch(n.getRandom().nextInt(4)){case 0->new ItemStack(Items.IRON_SWORD);case 1->new ItemStack(Items.IRON_AXE);case 2->stack("got:iron_spear");default->stack("got:iron_battleaxe");};}
    private static void buy(MerchantOffers o,String in,int count,int coins){add(o,in,count,"got:coin_1",coins);}private static void sell(MerchantOffers o,int coins,String out,int count){add(o,"got:coin_1",coins,out,count);}private static void add(MerchantOffers o,String in,int ic,String out,int oc){ItemStack a=stack(in,ic),b=stack(out,oc);if(!a.isEmpty()&&!b.isEmpty())o.add(new MerchantOffer(a,b,12,2,0.05F));}
    static ItemStack stack(String id){return stack(id,1);}static ItemStack stack(String id,int count){Item item=ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));return item==null||item==Items.AIR?ItemStack.EMPTY:new ItemStack(item,count);}
}
