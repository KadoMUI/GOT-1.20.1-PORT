package got;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;

public final class GOTWallBannerModel {
    private final ModelPart post;
    private final ModelPart banner;

    public GOTWallBannerModel(ModelPart root) {
        post = root.getChild("post");
        banner = root.getChild("banner");
    }

    public void renderPost(PoseStack pose, VertexConsumer out, int light, int overlay) {
        post.render(pose, out, light, overlay);
    }

    public void renderBanner(PoseStack pose, VertexConsumer out, int light, int overlay) {
        banner.render(pose, out, light, overlay);
    }
}
