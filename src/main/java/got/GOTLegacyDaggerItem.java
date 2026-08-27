package got;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
/** Legacy dagger = sword-class weapon with three less damage than the material sword. */
public final class GOTLegacyDaggerItem extends SwordItem {
    public GOTLegacyDaggerItem(Tier tier, Properties props) { super(tier, 0, -1.7F, props); }
}
