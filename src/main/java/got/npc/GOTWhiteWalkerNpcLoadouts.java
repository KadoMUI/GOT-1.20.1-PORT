package got.npc;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraftforge.registries.ForgeRegistries;

/** Ice weapons and armor used by Walkers; Wights retain scavenged iron and fur. */
final class GOTWhiteWalkerNpcLoadouts {
    private GOTWhiteWalkerNpcLoadouts() {}
    static void configure(GOTWhiteWalkerNpcEntity npc){npc.clearLoadout();switch(npc.getRole()){
        case WIGHT->{npc.setCombatWeapon(stack("got:iron_dagger"));if(npc.getRandom().nextInt(6)==0){npc.setItemSlot(EquipmentSlot.FEET,stack("got:fur_boots"));npc.setItemSlot(EquipmentSlot.LEGS,stack("got:fur_leggings"));npc.setItemSlot(EquipmentSlot.CHEST,stack("got:fur_chestplate"));}}
        case WHITE_WALKER->{ItemStack w=switch(npc.getRandom().nextInt(3)){case 0->stack("got:ice_sword");case 1->stack("got:ice_heavy_sword");default->stack("got:ice_spear");};npc.setWeapons(w,w);walkerArmor(npc);}
        case NIGHT_KING->{ItemStack w=stack("got:night_king_sword");npc.setWeapons(w,w);walkerArmor(npc);}
        case WIGHT_GIANT->{}}
        npc.updateHeldItem();}
    static MerchantOffers createOffers(WhiteWalkerNpcRole role){return new MerchantOffers();}
    private static void walkerArmor(GOTWhiteWalkerNpcEntity n){n.setItemSlot(EquipmentSlot.FEET,stack("got:white_walkers_boots"));n.setItemSlot(EquipmentSlot.LEGS,stack("got:white_walkers_leggings"));n.setItemSlot(EquipmentSlot.CHEST,stack("got:white_walkers_chestplate"));}
    static ItemStack stack(String id){return stack(id,1);}static ItemStack stack(String id,int count){Item item=ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));return item==null||item==Items.AIR?ItemStack.EMPTY:new ItemStack(item,count);}
}
