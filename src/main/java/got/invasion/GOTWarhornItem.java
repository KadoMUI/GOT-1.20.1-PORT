package got.invasion;

import got.claim.GOTBannerProtection;
import got.faction.GOTFactionPlayerData;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import java.util.List;

/** 1.7.10 warhorn behavior: faction-bound horn, 500 alignment gate, persistent invasion. */
public final class GOTWarhornItem extends Item {
    public static final String TAG="InvasionType";
    public GOTWarhornItem(Properties p){super(p.stacksTo(1));}
    public static ItemStack create(GOTInvasionType type){ItemStack s=new ItemStack(got.GOTEquipment.WARHORN.get());setType(s,type);return s;}
    public static void setType(ItemStack s,GOTInvasionType type){s.getOrCreateTag().putString(TAG,type.name());}
    public static GOTInvasionType type(ItemStack s){CompoundTag t=s.getTag();return t==null?GOTInvasionType.NORTH:GOTInvasionType.byName(t.getString(TAG)).orElse(GOTInvasionType.NORTH);}
    public static int tint(ItemStack s,int layer){return layer==0?type(s).faction().color():0xFFFFFF;}
    @Override public Component getName(ItemStack s){return Component.translatable("got.invasion."+type(s).name()+".horn");}
    @Override public UseAnim getUseAnimation(ItemStack s){return UseAnim.BOW;}
    @Override public int getUseDuration(ItemStack s){return 40;}
    @Override public InteractionResultHolder<ItemStack> use(Level level, net.minecraft.world.entity.player.Player player, InteractionHand hand){ItemStack s=player.getItemInHand(hand); if(player instanceof ServerPlayer sp && !canUse((ServerLevel)level,sp,s,true))return InteractionResultHolder.fail(s);player.startUsingItem(hand);return InteractionResultHolder.consume(s);}
    @Override public ItemStack finishUsingItem(ItemStack s,Level level,net.minecraft.world.entity.LivingEntity living){if(!level.isClientSide&&living instanceof ServerPlayer p&&canUse((ServerLevel)level,p,s,true)){GOTInvasionData.get((ServerLevel)level).start((ServerLevel)level,type(s),p.blockPosition().above(3),-1,true,p.getUUID());if(!p.getAbilities().instabuild)s.shrink(1);}return s;}
    private boolean canUse(ServerLevel level,ServerPlayer p,ItemStack s,boolean notify){GOTInvasionType type=type(s);if(GOTFactionPlayerData.get(p).alignment(type.faction())<500F){if(notify)p.displayClientMessage(Component.translatable("got.invasion.horn.alignment",type.faction().displayName(),500).withStyle(ChatFormatting.RED),false);return false;}if(GOTBannerProtection.isFactionBlocked(level,p.blockPosition(),type.faction())){if(notify)p.displayClientMessage(Component.translatable("got.chat.conquestHornProtected",type.faction().displayName()).withStyle(ChatFormatting.RED),false);return false;}return true;}
    @Override public void appendHoverText(ItemStack s,Level level,List<Component> tip,TooltipFlag flag){tip.add(Component.translatable("got.invasion.horn.requires",500,type(s).faction().displayName()).withStyle(ChatFormatting.GRAY));}
}
