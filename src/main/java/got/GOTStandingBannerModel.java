package got;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;

/** Separates the wooden frame and cloth so each can use its original texture. */
public final class GOTStandingBannerModel {
    private final ModelPart stand;
    private final ModelPart post;
    private final ModelPart lowerPost;
    private final ModelPart front;
    private final ModelPart back;

    public GOTStandingBannerModel(ModelPart root) {
        stand = root.getChild("stand");
        post = root.getChild("post");
        lowerPost = root.getChild("lower_post");
        front = root.getChild("banner_front");
        back = root.getChild("banner_back");
    }

    public void renderWorldFrame(PoseStack pose, VertexConsumer out, int light, int overlay) {
        stand.render(pose, out, light, overlay);
        post.render(pose, out, light, overlay);
    }

    public void renderHeldFrame(PoseStack pose, VertexConsumer out, int light, int overlay) {
        post.render(pose, out, light, overlay);
        lowerPost.render(pose, out, light, overlay);
    }

    public void renderCloth(PoseStack pose, VertexConsumer out, int light, int overlay) {
        front.render(pose, out, light, overlay);
        back.render(pose, out, light, overlay);
    }
}
