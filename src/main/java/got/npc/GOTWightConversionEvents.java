package got.npc;

import got.GOTEntities;
import got.GOTMod;
import got.special.GOTGiantEntity;
import got.special.GOTWightGiantEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Legacy IceUtils.createNewWight replacement.
 * Human faction NPCs killed by White-Walker-side entities rise as Wights;
 * living Giants rise as Wight Giants. Equipment is inherited from the corpse.
 */
@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID)
public final class GOTWightConversionEvents {
    private GOTWightConversionEvents() {}

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getEntity().level() instanceof ServerLevel level)) return;
        LivingEntity killer = event.getSource().getEntity() instanceof LivingEntity living ? living : null;
        if (!isIceUndeadKiller(killer)) return;

        LivingEntity victim = event.getEntity();
        if (victim instanceof GOTWhiteWalkerNpcEntity || victim instanceof GOTWightGiantEntity) return;

        if (victim instanceof GOTGiantEntity giant) {
            convertGiant(level, giant);
            return;
        }

        // Regional/faction NPCs are the modern equivalent of legacy GOTEntityHumanBase.
        if (victim instanceof Mob mob && victim instanceof GOTFactionNpc
                && !(victim instanceof GOTBlizzardEntity)
                && !(victim instanceof GOTUlthosSpiderEntity)) {
            convertHuman(level, mob, killer);
        }
    }

    private static boolean isIceUndeadKiller(LivingEntity killer) {
        if (killer instanceof GOTWightGiantEntity) return true;
        if (killer instanceof GOTWhiteWalkerNpcEntity ice) {
            WhiteWalkerNpcRole role = ice.getRole();
            return role == WhiteWalkerNpcRole.WIGHT
                    || role == WhiteWalkerNpcRole.WHITE_WALKER
                    || role == WhiteWalkerNpcRole.NIGHT_KING
                    || role == WhiteWalkerNpcRole.WIGHT_GIANT;
        }
        return false;
    }

    private static void convertHuman(ServerLevel level, Mob corpse, LivingEntity killer) {
        GOTWhiteWalkerNpcEntity wight = GOTEntities.WHITE_WALKER_NPC.get().create(level);
        if (wight == null) return;

        wight.moveTo(corpse.getX(), corpse.getY(), corpse.getZ(), corpse.getYRot(), corpse.getXRot());
        // Legacy IceUtils exception: a human personally slain by the Night King rises as a White Walker.
        WhiteWalkerNpcRole risenRole = killer instanceof GOTWhiteWalkerNpcEntity ice
                && ice.getRole() == WhiteWalkerNpcRole.NIGHT_KING
                ? WhiteWalkerNpcRole.WHITE_WALKER : WhiteWalkerNpcRole.WIGHT;
        wight.prepareForSpawn(risenRole, null, risenRole == WhiteWalkerNpcRole.WIGHT && corpse.isBaby(), corpse.blockPosition(), 24, "");

        ItemStack main = corpse.getItemBySlot(EquipmentSlot.MAINHAND).copy();
        wight.setWeapons(main, main);
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack inherited = corpse.getItemBySlot(slot).copy();
            if (!inherited.isEmpty()) wight.setItemSlot(slot, inherited);
            wight.setDropChance(slot, 0.0F);
        }
        level.addFreshEntity(wight);
    }

    private static void convertGiant(ServerLevel level, GOTGiantEntity corpse) {
        GOTWightGiantEntity wight = GOTEntities.WIGHT_GIANT.get().create(level);
        if (wight == null) return;
        wight.moveTo(corpse.getX(), corpse.getY(), corpse.getZ(), corpse.getYRot(), corpse.getXRot());
        wight.setPersistenceRequired();
        level.addFreshEntity(wight);
    }
}
