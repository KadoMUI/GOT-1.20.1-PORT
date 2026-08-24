package got;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

/** Renders both the physical legacy vessel and, when present, a separate liquid surface. */
public class PlacedDrinkVesselRenderer implements BlockEntityRenderer<PlacedDrinkVesselBlockEntity> {
    private static final ResourceLocation WHITE = ResourceLocation.fromNamespaceAndPath(GOTMod.MOD_ID, "textures/entity/vessel/liquid_white.png");
    private final LegacyVesselModel mug, goblet, horn, wineGlass, skullCup;

    public PlacedDrinkVesselRenderer(BlockEntityRendererProvider.Context context) {
        mug = bake(context, LegacyVesselLayers.MUG);
        goblet = bake(context, LegacyVesselLayers.GOBLET);
        horn = bake(context, LegacyVesselLayers.ALE_HORN);
        wineGlass = bake(context, LegacyVesselLayers.WINE_GLASS);
        skullCup = bake(context, LegacyVesselLayers.SKULL_CUP);
    }

    private static LegacyVesselModel bake(BlockEntityRendererProvider.Context context, ModelLayerLocation layer) {
        return new LegacyVesselModel(context.bakeLayer(layer));
    }

    @Override
    public void render(PlacedDrinkVesselBlockEntity entity, float partialTick, PoseStack pose,
                       MultiBufferSource buffers, int packedLight, int packedOverlay) {
        GOTDrinkVessel vessel = entity.getVessel();

        pose.pushPose();
        pose.translate(0.5D, 0.0125D, 0.5D);
        pose.mulPose(Axis.YP.rotationDegrees(-entity.getBlockState().getValue(PlacedDrinkVesselBlock.FACING).toYRot()));
        // ModelPart cube coordinates are already converted from model pixels to blocks
        // during baking. Applying the legacy 1/16 scale here shrank the vessel twice.
        pose.scale(1.0F, -1.0F, -1.0F);

        VertexConsumer consumer = buffers.getBuffer(RenderType.entityCutoutNoCull(texture(vessel)));
        model(vessel).render(pose, consumer, packedLight, OverlayTexture.NO_OVERLAY);

        if (entity.isFilled()) renderLiquid(entity.getDrink(), vessel, pose, buffers, packedLight);
        pose.popPose();
    }

    private static void renderLiquid(ItemStack drink, GOTDrinkVessel vessel, PoseStack pose,
                                     MultiBufferSource buffers, int light) {
        // Unlike ModelPart cubes, these vertices are submitted directly and therefore
        // must be converted from legacy model pixels to block units explicitly.
        float y = liquidY(vessel) / 16.0F;
        float r = liquidRadius(vessel) / 16.0F;
        int rgb = liquidColor(drink);
        float red = ((rgb >> 16) & 255) / 255.0F;
        float green = ((rgb >> 8) & 255) / 255.0F;
        float blue = (rgb & 255) / 255.0F;

        PoseStack.Pose last = pose.last();
        Matrix4f matrix = last.pose();
        Matrix3f normal = last.normal();
        VertexConsumer out = buffers.getBuffer(RenderType.entityTranslucent(WHITE));
        // Tiny upward offset avoids z-fighting with the rim.
        float yy = y - (0.02F / 16.0F);
        vertex(out, matrix, normal, -r, yy, -r, 0, 0, red, green, blue, light);
        vertex(out, matrix, normal, -r, yy,  r, 0, 1, red, green, blue, light);
        vertex(out, matrix, normal,  r, yy,  r, 1, 1, red, green, blue, light);
        vertex(out, matrix, normal,  r, yy, -r, 1, 0, red, green, blue, light);
    }

    private static void vertex(VertexConsumer out, Matrix4f pose, Matrix3f normal,
                               float x, float y, float z, float u, float v,
                               float r, float g, float b, int light) {
        out.vertex(pose, x, y, z).color(r, g, b, 0.88F).uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                .normal(normal, 0.0F, -1.0F, 0.0F).endVertex();
    }

    private static float liquidY(GOTDrinkVessel vessel) {
        return switch (vessel) {
            case GOLD_GOBLET, SILVER_GOBLET, COPPER_GOBLET, WOODEN_GOBLET, BRONZE_GOBLET, VALYRIAN_GOBLET -> -7.2F;
            case DRINKING_HORN, GOLD_DRINKING_HORN -> -5.2F;
            case WINE_GLASS -> -8.0F;
            case SKULL_CUP -> -6.7F;
            default -> -6.5F;
        };
    }

    private static float liquidRadius(GOTDrinkVessel vessel) {
        return switch (vessel) {
            case WINE_GLASS -> 1.45F;
            case DRINKING_HORN, GOLD_DRINKING_HORN -> 1.55F;
            case SKULL_CUP -> 2.2F;
            case GOLD_GOBLET, SILVER_GOBLET, COPPER_GOBLET, WOODEN_GOBLET, BRONZE_GOBLET, VALYRIAN_GOBLET -> 2.0F;
            default -> 2.25F;
        };
    }

    private static int liquidColor(ItemStack stack) {
        String id = net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(stack.getItem()).getPath();
        if (id.contains("milk")) return 0xF4F1DC;
        if (id.contains("water")) return 0x3F76E4;
        if (id.contains("wild_fire") || id.contains("wildfire")) return 0x69FF45;
        if (id.contains("wine") || id.contains("grape")) return 0x7A1832;
        if (id.contains("cider") || id.contains("apple")) return 0xD6A23A;
        if (id.contains("mead")) return 0xD8A52A;
        if (id.contains("ale") || id.contains("beer")) return 0xB56A24;
        if (id.contains("coffee")) return 0x4A2817;
        if (id.contains("tea")) return 0x8A542C;
        if (id.contains("juice")) return 0xC85B32;
        return 0xA94A32;
    }

    private LegacyVesselModel model(GOTDrinkVessel vessel) {
        return switch (vessel) {
            case GOLD_GOBLET, SILVER_GOBLET, COPPER_GOBLET, WOODEN_GOBLET, BRONZE_GOBLET, VALYRIAN_GOBLET -> goblet;
            case DRINKING_HORN, GOLD_DRINKING_HORN -> horn;
            case WINE_GLASS -> wineGlass;
            case SKULL_CUP -> skullCup;
            default -> mug;
        };
    }

    private static ResourceLocation texture(GOTDrinkVessel vessel) {
        String name = switch (vessel) {
            case CLAY_MUG -> "mug_clay";
            case GOLD_GOBLET -> "goblet_gold";
            case SILVER_GOBLET -> "goblet_silver";
            case COPPER_GOBLET -> "goblet_copper";
            case WOODEN_GOBLET -> "goblet_wood";
            case BRONZE_GOBLET -> "goblet_bronze";
            case VALYRIAN_GOBLET -> "goblet_valyrian";
            case DRINKING_HORN -> "ale_horn";
            case GOLD_DRINKING_HORN -> "ale_horn_gold";
            case WINE_GLASS -> "wine_glass";
            case SKULL_CUP -> "skull_cup";
            default -> "mug";
        };
        return ResourceLocation.fromNamespaceAndPath(GOTMod.MOD_ID, "textures/model/" + name + ".png");
    }
}
