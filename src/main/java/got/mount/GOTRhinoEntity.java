package got.mount;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.level.Level;
public class GOTRhinoEntity extends GOTMountEntity {
    public GOTRhinoEntity(EntityType<? extends Horse> type, Level level) { super(type, level); }
    @Override protected double clampHealth(double v){return Math.max(20, Math.min(50,v));}
    @Override protected double clampJump(double v){return Math.max(.2, Math.min(.8,v));}
    @Override protected double clampSpeed(double v){return Math.max(.12, Math.min(.42,v));}
    @Override protected void applyLegacySpeciesStats(){
        super.applyLegacySpeciesStats();
        getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4.0D);
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(Math.max(40.0D, getAttributeValue(Attributes.MAX_HEALTH)*1.5D));
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(getAttributeValue(Attributes.MOVEMENT_SPEED)*1.2D);
        getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(getAttributeValue(Attributes.JUMP_STRENGTH)*0.5D);
    }
    @Override protected boolean hasMountAttack(){return true;}
    @Override protected double mountAttackReachSqr(){return 9.0D;}
    @Override protected double mountedMoveSpeed(){return 1.05D;}
}
