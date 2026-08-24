package got.client;

import com.mojang.blaze3d.vertex.PoseStack;
import got.GOTMod;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Keeps the compact legacy item sprites in inventories while selecting the
 * matching large-2x, large-3x, or vlarge-2x model only for hand transforms.
 */
@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT)
public final class GOTHeldWeaponModels {
    private static final String MANIFEST = "/assets/got/held_weapon_models.txt";
    private static final List<Spec> SPECS = loadManifest();

    private GOTHeldWeaponModels() {}

    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        for (Spec spec : SPECS) event.register(spec.heldModel());
    }

    @SubscribeEvent
    public static void replaceInventoryModels(ModelEvent.ModifyBakingResult event) {
        Map<ResourceLocation, BakedModel> models = event.getModels();
        int replaced = 0;
        for (Spec spec : SPECS) {
            ModelResourceLocation inventoryKey = new ModelResourceLocation(spec.item(), "inventory");
            BakedModel inventory = models.get(inventoryKey);
            BakedModel held = models.get(spec.heldModel());
            // Auxiliary legacy sprites may not have a registered item. They are
            // baked for compatibility but deliberately do not fabricate one.
            if (inventory == null || held == null) continue;
            models.put(inventoryKey, new HandContextModel(inventory, held, spec.scale()));
            replaced++;
        }
        GOTMod.LOGGER.info("Installed hand-only large textures for {} weapon models", replaced);
    }

    private static List<Spec> loadManifest() {
        List<Spec> result = new ArrayList<>();
        try (InputStream input = GOTHeldWeaponModels.class.getResourceAsStream(MANIFEST)) {
            if (input == null) throw new IOException("missing " + MANIFEST);
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(input, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String trimmed = line.trim();
                    if (trimmed.isEmpty() || trimmed.startsWith("#")) continue;
                    String[] fields = trimmed.split("\\|", -1);
                    if (fields.length != 3) throw new IOException("invalid line: " + trimmed);
                    ResourceLocation item = ResourceLocation.tryParse(fields[0]);
                    ResourceLocation heldModel = ResourceLocation.tryParse(fields[1]);
                    float scale = Float.parseFloat(fields[2]);
                    if (item == null || heldModel == null || scale <= 0.0F) {
                        throw new IOException("invalid line: " + trimmed);
                    }
                    result.add(new Spec(item, heldModel, scale));
                }
            }
        } catch (Exception exception) {
            GOTMod.LOGGER.error("Unable to load held weapon model manifest", exception);
        }
        return List.copyOf(result);
    }

    private record Spec(ResourceLocation item, ResourceLocation heldModel, float scale) {}

    private record HandContextModel(BakedModel inventory, BakedModel held,
                                    float heldScale) implements BakedModel {
        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
                                        RandomSource random) {
            return inventory.getQuads(state, side, random);
        }

        @Override public boolean useAmbientOcclusion() { return inventory.useAmbientOcclusion(); }
        @Override public boolean isGui3d() { return inventory.isGui3d(); }
        @Override public boolean usesBlockLight() { return inventory.usesBlockLight(); }
        @Override public boolean isCustomRenderer() { return inventory.isCustomRenderer(); }
        @Override public TextureAtlasSprite getParticleIcon() { return inventory.getParticleIcon(); }
        @Override public ItemTransforms getTransforms() { return inventory.getTransforms(); }
        @Override public ItemOverrides getOverrides() { return inventory.getOverrides(); }

        @Override
        public BakedModel applyTransform(ItemDisplayContext context, PoseStack poseStack,
                                         boolean leftHand) {
            if (isHandContext(context)) {
                BakedModel transformed = held.applyTransform(context, poseStack, leftHand);
                poseStack.scale(heldScale, heldScale, heldScale);
                return transformed;
            }
            return inventory.applyTransform(context, poseStack, leftHand);
        }

        private static boolean isHandContext(ItemDisplayContext context) {
            return context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND
                    || context == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
                    || context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND
                    || context == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
        }
    }
}
