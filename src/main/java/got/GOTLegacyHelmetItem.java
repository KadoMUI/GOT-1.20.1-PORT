package got;

import got.client.model.GOTLegacyHelmetModels;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
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
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        // These two recovered 1.7.10 helmet models use dedicated 64x32 UV
        // atlases. The normal faction layer texture is laid out for vanilla
        // humanoid armor and makes the custom model appear as a small cap.
        return shape == Shape.REACH
                ? "got:textures/armor/reach_helmet.png"
                : "got:textures/armor/north_helmet.png";
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

                // Forge asks for the custom armor model after the parent humanoid
                // model has already been posed. Copy the live part transforms so
                // bespoke helmet geometry follows the wearer's head instead of
                // remaining at its baked/default orientation.
                model.head.copyFrom(defaultModel.head);
                model.hat.copyFrom(defaultModel.hat);
                model.body.copyFrom(defaultModel.body);
                model.rightArm.copyFrom(defaultModel.rightArm);
                model.leftArm.copyFrom(defaultModel.leftArm);
                model.rightLeg.copyFrom(defaultModel.rightLeg);
                model.leftLeg.copyFrom(defaultModel.leftLeg);

                model.crouching = defaultModel.crouching;
                model.riding = defaultModel.riding;
                model.young = defaultModel.young;
                model.attackTime = defaultModel.attackTime;
                model.leftArmPose = defaultModel.leftArmPose;
                model.rightArmPose = defaultModel.rightArmPose;

                // This model is used only for a helmet slot. Keep the empty
                // humanoid placeholders hidden and render only the custom head.
                model.head.visible = true;
                model.hat.visible = false;
                model.body.visible = false;
                model.rightArm.visible = false;
                model.leftArm.visible = false;
                model.rightLeg.visible = false;
                model.leftLeg.visible = false;
                return model;
            }
        });
    }
}
