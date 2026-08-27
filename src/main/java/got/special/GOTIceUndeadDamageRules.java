package got.special;

import got.GOTEquipment;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;

/** Centralized vulnerability rules for Wights, White Walkers, Wight Giants, and the Night King. */
public final class GOTIceUndeadDamageRules {
    private static final Set<String> DRAGONGLASS_WEAPONS = Set.of(
            "got:obsidian_axe", "got:obsidian_battleaxe", "got:obsidian_dagger",
            "got:obsidian_dagger_poisoned", "got:obsidian_hammer", "got:obsidian_mattock",
            "got:obsidian_pike", "got:obsidian_scimitar", "got:obsidian_spear",
            "got:obsidian_sword", "got:obsidian_throwing_axe"
    );

    private static final Set<String> VALYRIAN_WEAPONS = Set.of(
            "got:valyrian_axe", "got:valyrian_battleaxe", "got:valyrian_crossbow",
            "got:valyrian_dagger", "got:valyrian_dagger_poisoned", "got:valyrian_greatsword",
            "got:valyrian_hammer", "got:valyrian_longsword", "got:valyrian_mattock",
            "got:valyrian_pike", "got:valyrian_scimitar", "got:valyrian_spear",
            "got:valyrian_sword", "got:valyrian_throwing_axe"
    );

    /** Legendary weapons whose 1.7.10 ToolMaterial was explicitly VALYRIAN_TOOL. */
    private static final Set<String> LEGENDARY_VALYRIAN = Set.of(
            "got:ardrian_celtigar_axe", "got:bane", "got:blackfyre", "got:brightroar",
            "got:cutwave", "got:dark_sister", "got:darkstar", "got:fin", "got:heartsbane",
            "got:honor", "got:ice", "got:indomitable", "got:just_maid", "got:lady_forlorn",
            "got:lamentation", "got:lightbringer", "got:longclaw", "got:nightfall",
            "got:oathkeeper", "got:orphan_maker", "got:red_rain", "got:reminder",
            "got:tidewings", "got:truth", "got:vigilance", "got:widow_wail"
    );

    private GOTIceUndeadDamageRules() {}

    /** Wights/Walkers: fire, Beric's flaming sword, dragonglass, Valyrian steel, or Petyr's dagger. */
    public static boolean canDamageWightOrWalker(DamageSource source) {
        if (source.is(DamageTypeTags.IS_FIRE)) return true;
        ItemStack weapon = attackingWeapon(source);
        if (weapon.isEmpty()) return false;
        var key = ForgeRegistries.ITEMS.getKey(weapon.getItem());
        if (key == null) return false;
        String id = key.toString();
        return id.equals("got:beric_dondarrion_sword")
                || id.equals("got:petyr_baelish_dagger")
                || DRAGONGLASS_WEAPONS.contains(id)
                || VALYRIAN_WEAPONS.contains(id)
                || LEGENDARY_VALYRIAN.contains(id);
    }

    /** Night King: intentionally stricter than every other ice-undead entity. */
    public static boolean canDamageNightKing(DamageSource source) {
        ItemStack weapon = attackingWeapon(source);
        return !weapon.isEmpty() && weapon.is(GOTEquipment.PETYR_BAELISH_DAGGER.get());
    }

    private static ItemStack attackingWeapon(DamageSource source) {
        Entity attacker = source.getEntity();
        return attacker instanceof LivingEntity living ? living.getMainHandItem() : ItemStack.EMPTY;
    }
}
