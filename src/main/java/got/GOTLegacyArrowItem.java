package got;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class GOTLegacyArrowItem extends ArrowItem {
    private final GOTLegacyArrowEntity.Kind kind;
    public GOTLegacyArrowItem(GOTLegacyArrowEntity.Kind kind, Properties props) { super(props); this.kind = kind; }
    @Override public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter) {
        return new GOTLegacyArrowEntity(level, shooter, ammo, kind);
    }
}
