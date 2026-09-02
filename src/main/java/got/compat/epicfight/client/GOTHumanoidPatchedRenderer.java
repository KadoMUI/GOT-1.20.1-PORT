package got.compat.epicfight.client;

import got.GOTMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.model.SkinnedMesh;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.api.client.model.transformer.HumanoidModelBaker;
import yesman.epicfight.client.mesh.HumanoidMesh;
import yesman.epicfight.client.renderer.patched.entity.PHumanoidRenderer;

import java.util.Locale;

/**
 * Uses the ACTUAL GOT HumanoidModel for the Epic Fight body mesh.
 * Using Epic Fight's stock biped here caused the oversized/"giant head" regression.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public final class GOTHumanoidPatchedRenderer extends PHumanoidRenderer {
    private final EntityType<?> entityType;
    private AssetAccessor<HumanoidMesh> gotMesh;

    public GOTHumanoidPatchedRenderer(EntityRendererProvider.Context context, EntityType<?> entityType) {
        // Default/fallback and armor-layer reference. The body is swapped in getMeshProvider().
        super(Meshes.BIPED, context, entityType);
        this.entityType = entityType;
    }

    @Override
    public AssetAccessor<HumanoidMesh> getMeshProvider(
            yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch entityPatch) {
        if (gotMesh != null) return gotMesh;

        try {
            PathfinderMob entity = (PathfinderMob) entityPatch.getOriginal();
            EntityRenderer<?> vanillaRenderer =
                    Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);

            if (vanillaRenderer instanceof LivingEntityRenderer livingRenderer
                    && livingRenderer.getModel() instanceof HumanoidModel humanoidModel) {
                SkinnedMesh baked =
                        HumanoidModelBaker.VANILLA_TRANSFORMER.transformArmorModel(humanoidModel);

                // HumanoidMesh can inherit the transformed SkinnedMesh arrays and parts.
                HumanoidMesh wrapped = new HumanoidMesh(
                        null, null, baked, baked.getRenderProperties());

                String path = EntityType.getKey(entityType).getPath()
                        .replaceAll("[^a-z0-9/._-]", "_")
                        .toLowerCase(Locale.ROOT);
                gotMesh = new FixedMeshAccessor(
                        new ResourceLocation(GOTMod.MOD_ID, "epicfight/runtime/" + path),
                        wrapped);
                return gotMesh;
            }
        } catch (RuntimeException exception) {
            GOTMod.LOGGER.error(
                    "Could not bake GOT model for Epic Fight entity type {}; using Epic Fight biped fallback",
                    EntityType.getKey(entityType), exception);
        }

        return Meshes.BIPED;
    }

    private record FixedMeshAccessor(ResourceLocation registryName, HumanoidMesh mesh)
            implements AssetAccessor<HumanoidMesh> {
        @Override public HumanoidMesh get() { return mesh; }
        @Override public boolean inRegistry() { return false; }
    }
}
