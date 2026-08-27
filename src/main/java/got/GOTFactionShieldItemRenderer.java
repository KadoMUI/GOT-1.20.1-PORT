package got;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

/**
 * Renders the original 64x32 shield sheets as a thin physical shield.
 * Legacy GOT shield sheets contain separate front/back halves. The renderer
 * assigns them to the physically correct outward/inward faces without changing
 * the reference-derived shield geometry or hand transforms.
 */
public final class GOTFactionShieldItemRenderer extends BlockEntityWithoutLevelRenderer {
    private static final Map<ResourceLocation, OutlineData> OUTLINE_CACHE = new ConcurrentHashMap<>();

    /**
     * One edge of an opaque texel on the FRONT/right-hand 32x32 shield half.
     * Coordinates are normalized 0..1 in that half's local texture space.
     */
    private record OutlineEdge(float x0, float y0, float x1, float y1, float nx, float ny) {}
    private record OutlineData(List<OutlineEdge> edges, float rimU, float rimV) {}

    public GOTFactionShieldItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet models) {
        super(dispatcher, models);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack pose,
                             MultiBufferSource buffers, int light, int overlay) {
        if (!(stack.getItem() instanceof GOTFactionShieldItem shield)) return;
        pose.pushPose();
        // Render in the same model space as vanilla ShieldRenderer.
        // Hand/GUI/ground transforms come from the item model JSON, exactly as
        // they do for minecraft:shield.  Do NOT apply a second hand transform here.
        pose.scale(1.0F, -1.0F, -1.0F);

        VertexConsumer vc = buffers.getBuffer(RenderType.entityCutoutNoCull(shield.texture()));
        PoseStack.Pose p = pose.last();
        Matrix4f m = p.pose();
        Matrix3f n = p.normal();

        // The legacy texture is square, but the painted shield occupies a tall
        // heraldic silhouette.  Render the physical plate with shield-like
        // proportions instead of as a 1x1 held-item card.
        float x0 = -0.96875F, x1 = 0.96875F, y0 = -1.0625F, y1 = 0.9375F;
        float zFront = -0.0625F, zBack = -0.125F;

        // Keep the working plate geometry/winding from the reference-derived pass.
        // Only swap which texture half is painted on each side: the previous
        // facing fix incorrectly flipped the mesh itself and broke the pose.
        // Front/outward face: RIGHT half of the legacy sheet.
        quad(vc, m, n, light, x0,y0,zFront, x1,y0,zFront, x1,y1,zFront, x0,y1,zFront,
                0.5F,0.0F, 1.0F,0.0F, 1.0F,1.0F, 0.5F,1.0F, 0,0,1);
        // Back/inside face: LEFT half, winding reversed so it faces the holder.
        quad(vc, m, n, light, x1,y0,zBack, x0,y0,zBack, x0,y1,zBack, x1,y1,zBack,
                0.0F,0.0F, 0.5F,0.0F, 0.5F,1.0F, 0.0F,1.0F, 0,0,-1);

        /*
         * Extrude the ACTUAL visible shield silhouette.
         *
         * The 64x32 legacy sheet contains alpha around the painted shield.  A
         * rectangular rim therefore connects mostly invisible pixels and does
         * not visually join the two faces.  Build the rim from opaque->transparent
         * alpha boundaries on the front/right-hand 32x32 half instead.
         */
        OutlineData outline = outlineFor(shield.texture());
        for (OutlineEdge edge : outline.edges()) {
            float ax = x0 + (x1 - x0) * edge.x0();
            float ay = y0 + (y1 - y0) * edge.y0();
            float bx = x0 + (x1 - x0) * edge.x1();
            float by = y0 + (y1 - y0) * edge.y1();

            /*
             * Use one guaranteed-opaque texel from THIS shield for every rim
             * vertex.  The previous pass sampled the sheet's far-left strip,
             * which is transparent padding on many legacy shield textures, so
             * the geometry existed but was invisible.
             */
            float ru = outline.rimU();
            float rv = outline.rimV();
            quad(vc, m, n, light,
                    ax,ay,zFront,
                    bx,by,zFront,
                    bx,by,zBack,
                    ax,ay,zBack,
                    ru,rv, ru,rv, ru,rv, ru,rv,
                    edge.nx(), edge.ny(), 0.0F);
        }


        pose.popPose();
    }


    private static OutlineData outlineFor(ResourceLocation texture) {
        return OUTLINE_CACHE.computeIfAbsent(texture, GOTFactionShieldItemRenderer::loadOutline);
    }

    private static OutlineData loadOutline(ResourceLocation texture) {
        List<OutlineEdge> edges = new ArrayList<>();
        float rimU = 0.75F;
        float rimV = 0.5F;

        try {
            Resource resource = Minecraft.getInstance().getResourceManager().getResource(texture).orElse(null);
            if (resource == null) {
                return new OutlineData(edges, rimU, rimV);
            }

            try (InputStream in = resource.open(); NativeImage image = NativeImage.read(in)) {
                int halfWidth = image.getWidth() / 2;
                int height = image.getHeight();
                int xOffset = halfWidth;

                /*
                 * Find a guaranteed opaque sample on the outward/right-hand
                 * half. The +0.5 samples the center of the texel so filtering
                 * cannot bleed in transparent neighboring pixels as easily.
                 */
                boolean foundOpaqueSample = false;
                for (int py = 0; py < height && !foundOpaqueSample; py++) {
                    for (int px = 0; px < halfWidth; px++) {
                        if (opaque(image, xOffset + px, py)) {
                            rimU = (xOffset + px + 0.5F) / (float) image.getWidth();
                            rimV = (py + 0.5F) / (float) height;
                            foundOpaqueSample = true;
                            break;
                        }
                    }
                }

                for (int py = 0; py < height; py++) {
                    for (int px = 0; px < halfWidth; px++) {
                        if (!opaque(image, xOffset + px, py)) {
                            continue;
                        }

                        float fx0 = px / (float) halfWidth;
                        float fx1 = (px + 1) / (float) halfWidth;
                        float fy0 = py / (float) height;
                        float fy1 = (py + 1) / (float) height;

                        if (px == 0 || !opaque(image, xOffset + px - 1, py)) {
                            edges.add(new OutlineEdge(fx0, fy1, fx0, fy0, -1.0F, 0.0F));
                        }
                        if (px == halfWidth - 1 || !opaque(image, xOffset + px + 1, py)) {
                            edges.add(new OutlineEdge(fx1, fy0, fx1, fy1, 1.0F, 0.0F));
                        }
                        if (py == 0 || !opaque(image, xOffset + px, py - 1)) {
                            edges.add(new OutlineEdge(fx0, fy0, fx1, fy0, 0.0F, -1.0F));
                        }
                        if (py == height - 1 || !opaque(image, xOffset + px, py + 1)) {
                            edges.add(new OutlineEdge(fx1, fy1, fx0, fy1, 0.0F, 1.0F));
                        }
                    }
                }
            }
        } catch (Exception ignored) {
            // Keep the primary front/back faces even if a resource pack gives
            // us an unreadable image. The fallback UV is harmless if no edges exist.
        }

        return new OutlineData(edges, rimU, rimV);
    }

    private static boolean opaque(NativeImage image, int x, int y) {
        if (x < 0 || y < 0 || x >= image.getWidth() || y >= image.getHeight()) {
            return false;
        }
        return ((image.getPixelRGBA(x, y) >>> 24) & 0xFF) > 8;
    }

    private static void quad(VertexConsumer vc, Matrix4f m, Matrix3f n, int light,
                             float ax,float ay,float az, float bx,float by,float bz,
                             float cx,float cy,float cz, float dx,float dy,float dz,
                             float au,float av, float bu,float bv, float cu,float cv, float du,float dv,
                             float nx,float ny,float nz) {
        v(vc,m,n,light,ax,ay,az,au,av,nx,ny,nz);
        v(vc,m,n,light,bx,by,bz,bu,bv,nx,ny,nz);
        v(vc,m,n,light,cx,cy,cz,cu,cv,nx,ny,nz);
        v(vc,m,n,light,dx,dy,dz,du,dv,nx,ny,nz);
    }

    private static void v(VertexConsumer vc, Matrix4f m, Matrix3f n, int light,
                          float x,float y,float z,float u,float vv,float nx,float ny,float nz) {
        vc.vertex(m,x,y,z).color(255,255,255,255).uv(u,vv)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(n,nx,ny,nz).endVertex();
    }


}
