package got.menu.hiring;

import got.npc.hiring.GOTHiredData;
import got.npc.hiring.GOTHiredTask;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

/**
 * Opens the correct hired inventory menu for an owned NPC.
 *
 * Replace the null MenuType placeholders with the project's registered menu
 * types when merging.
 */
public final class GOTHiredMenuProvider implements MenuProvider {
    private final Entity npc;

    public GOTHiredMenuProvider(Entity npc) {
        this.npc = npc;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal(npc.getDisplayName().getString() + " Inventory");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        if (!GOTHiredData.isOwner(npc, player.getUUID())) return null;

        if (GOTHiredData.task(npc) == GOTHiredTask.FARMER) {
            return new GOTContainerHiredFarmerInventory(id, inventory, npc);
        }
        return new GOTContainerHiredWarriorInventory(id, inventory, npc);
    }
}
