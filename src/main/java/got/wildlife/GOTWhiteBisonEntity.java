package got.wildlife;
import got.GOTItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
public final class GOTWhiteBisonEntity extends GOTBisonEntity {
    public GOTWhiteBisonEntity(EntityType<? extends GOTWhiteBisonEntity> type, Level level) { super(type, level); }
    @Override protected Item hornItem() { return GOTItems.WHITE_BISON_HORN.get(); }
}
