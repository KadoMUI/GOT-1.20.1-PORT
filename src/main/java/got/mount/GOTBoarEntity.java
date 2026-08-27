package got.mount;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.level.Level;
public final class GOTBoarEntity extends GOTMountEntity {
    public GOTBoarEntity(EntityType<? extends Horse> type, Level level){super(type,level);}
    @Override protected double clampHealth(double v){return Math.max(10,Math.min(30,v));}
    @Override protected double clampSpeed(double v){return Math.max(.08,Math.min(.35,v));}
    @Override protected void applyLegacySpeciesStats(){super.applyLegacySpeciesStats();getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3D);getAttribute(Attributes.MAX_HEALTH).setBaseValue(Math.min(25D,getAttributeValue(Attributes.MAX_HEALTH)));}
    @Override protected boolean hasMountAttack(){return true;}
    @Override protected double mountAttackReachSqr(){return 6.25D;}
    @Override protected int mountAttackInterval(){return 16;}
    @Override protected double mountedMoveSpeed(){return 1.2D;}
}
