package got;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

/** Legacy pikes are melee-only sword-class weapons; unlike spears they have no throw action. */
public final class GOTLegacyPikeItem extends SwordItem {
    public GOTLegacyPikeItem(Tier tier, Properties props) {
        super(tier, 3, -2.8F, props);
    }
}
