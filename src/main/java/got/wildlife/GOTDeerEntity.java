package got.wildlife;

import got.GOTItems;
import got.mount.GOTMountEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/** Legacy deer were members of the GOT horse/mount family, not cow-style passive mobs. */
public class GOTDeerEntity extends GOTMountEntity {
    public GOTDeerEntity(EntityType<? extends Horse> type, Level level) { super(type, level); }

    public static AttributeSupplier.Builder createAttributes() {
        return Horse.createBaseHorseAttributes().add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override protected double clampHealth(double value) { return Mth.clamp(value, 16.0D, 50.0D); }
    @Override protected double clampSpeed(double value) { return Mth.clamp(value, 0.08D, 0.34D); }
    @Override protected boolean hasMountAttack() { return true; }

    @Override public boolean isFood(ItemStack stack) { return stack.is(Items.WHEAT); }

    @Nullable @Override public AbstractHorse getBreedOffspring(ServerLevel level, AgeableMob mate) {
        return (GOTDeerEntity) getType().create(level);
    }

    @Override protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);
        int leather = random.nextInt(3) + random.nextInt(looting + 1);
        int meat = random.nextInt(3) + random.nextInt(looting + 1);
        for (int i=0;i<leather;i++) spawnAtLocation(Items.LEATHER);
        for (int i=0;i<meat;i++) spawnAtLocation(isOnFire() ? GOTItems.DEER_COOKED.get() : GOTItems.DEER_RAW.get());
    }
}
