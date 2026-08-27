package got.special;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

/** Legacy Wight Giant: 1 HP behind the White-Walker damage gate, frost attack, no loot. */
public final class GOTWightGiantEntity extends GOTGiantBaseEntity {
    public GOTWightGiantEntity(EntityType<? extends net.minecraft.world.entity.monster.Monster> type, Level level) { super(type, level); xpReward=25; }
    public static AttributeSupplier.Builder createWightAttributes(){ return giantAttributes().add(Attributes.MAX_HEALTH,1.0D); }
    @Override public boolean hurt(DamageSource source,float amount){
        return GOTIceUndeadDamageRules.canDamageWightOrWalker(source) && super.hurt(source,amount);
    }
    @Override public boolean doHurtTarget(Entity target) { boolean hit=super.doHurtTarget(target); if(hit && target instanceof LivingEntity living) living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,100,1), this); return hit; }
    @Override protected void dropCustomDeathLoot(DamageSource source,int looting,boolean recentlyHit) {}
}
