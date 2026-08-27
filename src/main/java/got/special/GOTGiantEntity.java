package got.special;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/** Living Wildling-aligned Giant. Legacy: 100 HP and ten fur on death. */
public final class GOTGiantEntity extends GOTGiantBaseEntity {
    public GOTGiantEntity(EntityType<? extends net.minecraft.world.entity.monster.Monster> type, Level level) { super(type, level); }
    @Override protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        // The old Giant dropped exactly ten GOT fur. Until/where the fur item is mapped, leather is NOT substituted.
        var fur = net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(new net.minecraft.resources.ResourceLocation("got","fur"));
        if (fur != null && fur != Items.AIR) for (int i=0;i<10;i++) spawnAtLocation(fur);
    }
}
