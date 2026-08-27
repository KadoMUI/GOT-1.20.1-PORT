package got.wildlife;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.level.Level;
/** Legacy ambient midge swarm represented as a tiny flying mob. */
public final class GOTMidgesEntity extends Bat {
    public GOTMidgesEntity(EntityType<? extends Bat> type, Level level){ super(type, level); xpReward = 0; }
    public static AttributeSupplier.Builder createAttributes(){ return Bat.createAttributes(); }
}
