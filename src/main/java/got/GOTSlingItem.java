package got;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;import net.minecraft.world.InteractionResultHolder;import net.minecraft.world.entity.player.Player;import net.minecraft.world.item.Item;import net.minecraft.world.item.ItemStack;import net.minecraft.world.level.Level;
public final class GOTSlingItem extends Item{
 public GOTSlingItem(Properties p){super(p.stacksTo(1).durability(250));}
 @Override public InteractionResultHolder<ItemStack> use(Level l,Player p,InteractionHand h){ItemStack sling=p.getItemInHand(h);boolean ammo=p.getAbilities().instabuild||p.getInventory().contains(new ItemStack(GOTItems.PEBBLE.get()));if(!ammo)return InteractionResultHolder.fail(sling);if(!l.isClientSide){if(!p.getAbilities().instabuild)consume(p,GOTItems.PEBBLE.get());sling.hurtAndBreak(1,p,x->x.broadcastBreakEvent(h));GOTPebbleEntity e=new GOTPebbleEntity(l,p);e.shootFromRotation(p,p.getXRot(),p.getYRot(),0,1.7F,1.0F);l.addFreshEntity(e);l.playSound(null,p.blockPosition(),SoundEvents.ARROW_SHOOT,SoundSource.PLAYERS,.5F,.9F+(p.getRandom().nextFloat()*.2F));}return InteractionResultHolder.sidedSuccess(sling,l.isClientSide);}
 private static void consume(Player p,Item item){for(int i=0;i<p.getInventory().getContainerSize();i++){ItemStack s=p.getInventory().getItem(i);if(s.is(item)){s.shrink(1);return;}}}
}
