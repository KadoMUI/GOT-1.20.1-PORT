package got;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class GOTArmorMaterials implements ArmorMaterial {
    private static final Map<String, GOTArmorMaterials> CACHE = new ConcurrentHashMap<>();
    private static final Map<ArmorItem.Type, Integer> BASE_DEFENSE = new EnumMap<>(ArmorItem.Type.class);

    static {
        BASE_DEFENSE.put(ArmorItem.Type.BOOTS, 2);
        BASE_DEFENSE.put(ArmorItem.Type.LEGGINGS, 5);
        BASE_DEFENSE.put(ArmorItem.Type.CHESTPLATE, 6);
        BASE_DEFENSE.put(ArmorItem.Type.HELMET, 2);
    }

    public static GOTArmorMaterials of(String textureName) {
        return CACHE.computeIfAbsent(textureName, GOTArmorMaterials::new);
    }

    private final String textureName;
    private final LazyLoadedValue<Ingredient> repairIngredient =
            new LazyLoadedValue<>(() -> Ingredient.of(GOTItems.ALLOY_STEEL_INGOT.get()));

    private GOTArmorMaterials(String textureName) {
        this.textureName = textureName;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return switch (type) {
            case BOOTS -> 286;
            case LEGGINGS -> 330;
            case CHESTPLATE -> 352;
            case HELMET -> 242;
        };
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return BASE_DEFENSE.get(type);
    }

    @Override
    public int getEnchantmentValue() {
        return 12;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_IRON;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient.get();
    }

    @Override
    public String getName() {
        return GOTMod.MOD_ID + ":" + textureName;
    }

    @Override
    public float getToughness() {
        return 1.0F;
    }

    @Override
    public float getKnockbackResistance() {
        return 0.0F;
    }
}
