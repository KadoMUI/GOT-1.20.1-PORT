package got;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;

/** Shared wrapper for the reconstructed 1.7.10 Java vessel models. */
public final class LegacyVesselModel {
    private final ModelPart root;

    public LegacyVesselModel(ModelPart root) {
        this.root = root;
    }

    public void render(PoseStack pose, VertexConsumer consumer, int light, int overlay) {
        root.render(pose, consumer, light, overlay);
    }
}
