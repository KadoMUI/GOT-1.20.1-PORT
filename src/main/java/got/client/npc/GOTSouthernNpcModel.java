package got.client.npc;

import got.npc.GOTNorvosNpcEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;

/** Shared legacy 64x64 human geometry for the Summer Isles and Sothoryos. */
public final class GOTSouthernNpcModel<T extends GOTNorvosNpcEntity> extends HumanoidModel<T> {
    private final ModelPart chest;

    public GOTSouthernNpcModel(ModelPart root) {
        super(root);
        chest = body.getChild("norvos_chest");
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        rightArmPose = ArmPose.EMPTY;
        leftArmPose = ArmPose.EMPTY;
        if (entity.isAimingBow()) {
            if (entity.getMainArm() == HumanoidArm.RIGHT) rightArmPose = ArmPose.BOW_AND_ARROW;
            else leftArmPose = ArmPose.BOW_AND_ARROW;
        }
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        chest.visible = entity.isFemale() && !entity.isBaby();
    }
}
