package got;

import got.client.model.GOTLegacyHelmetModels;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

/** ArmorItem bridge for the two legacy helmets that used bespoke 1.7.10 geometry. */
public final class GOTLegacyHelmetItem extends ArmorItem {
    public enum Shape { NORTH, REACH }

    private final Shape shape;

    public GOTLegacyHelmetItem(ArmorMaterial material, Properties properties, Shape shape) {
        super(material, Type.HELMET, properties);
        this.shape = shape;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        Shape helmetShape = shape;
        consumer.accept(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity living, ItemStack stack,
                                                           EquipmentSlot slot, HumanoidModel<?> defaultModel) {
                if (slot != EquipmentSlot.HEAD) return defaultModel;
                HumanoidModel<?> model = GOTLegacyHelmetModels.model(helmetShape);
                model.crouching = defaultModel.crouching;
                model.riding = defaultModel.riding;
                model.young = defaultModel.young;
                model.attackTime = defaultModel.attackTime;
                model.leftArmPose = defaultModel.leftArmPose;
                model.rightArmPose = defaultModel.rightArmPose;
                return model;
            }
        });
    }
}
