package got.mount;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
public final class GOTCamelEntity extends GOTMountEntity {
    private int carpetColor=-1;
    public GOTCamelEntity(EntityType<? extends Horse> type, Level level){super(type,level);}
    @Override protected double clampHealth(double v){return Math.max(12,Math.min(36,v));}
    @Override protected double clampJump(double v){return Math.max(.1,Math.min(.6,v));}
    @Override protected double clampSpeed(double v){return Math.max(.1,Math.min(.35,v));}
    @Override protected void applyLegacySpeciesStats(){super.applyLegacySpeciesStats();getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(getAttributeValue(Attributes.JUMP_STRENGTH)*.5D);}
    public int getCarpetColor(){return carpetColor;} public void setCarpetColor(int color){carpetColor=color<0?-1:color&15;}
    public int getCarpetRgb(){if(carpetColor<0)return 0xFFFFFF;float[] c=DyeColor.byId(carpetColor).getTextureDiffuseColors();return ((int)(c[0]*255)<<16)|((int)(c[1]*255)<<8)|(int)(c[2]*255);}
    @Override public void addAdditionalSaveData(CompoundTag tag){super.addAdditionalSaveData(tag);tag.putInt("GOTCamelCarpet",carpetColor);}
    @Override public void readAdditionalSaveData(CompoundTag tag){super.readAdditionalSaveData(tag);carpetColor=tag.contains("GOTCamelCarpet")?tag.getInt("GOTCamelCarpet"):-1;}
    @Override protected double mountedMoveSpeed(){return 1.0D;}
}
