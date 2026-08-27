package got.client.mount;

import got.mount.GOTMountEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public final class GOTLegacyMountRenderer<T extends GOTMountEntity> extends MobRenderer<T,GOTLegacyMountModel<T>> {
    private final ResourceLocation texture;
    public GOTLegacyMountRenderer(EntityRendererProvider.Context c, ModelLayerLocation layer, GOTLegacyMountModel.Kind kind, String texture, float shadow){
        super(c,new GOTLegacyMountModel<>(c.bakeLayer(layer)),shadow); this.texture=new ResourceLocation("got",texture);
        ModelLayerLocation saddleLayer = switch(kind){case RHINO->GOTMountModelLayers.RHINO_SADDLE; case CAMEL->GOTMountModelLayers.CAMEL_SADDLE; case BOAR->GOTMountModelLayers.BOAR_SADDLE;};
        String saddleTexture = switch(kind){case RHINO->"textures/entity/animal/rhino/saddle.png"; case CAMEL->"textures/entity/animal/camel/saddle.png"; case BOAR->"textures/entity/animal/boar/saddle.png";};
        addLayer(new GOTLegacyMountEquipmentLayer<>(this,new GOTLegacyMountModel<>(c.bakeLayer(saddleLayer)),saddleTexture,GOTLegacyMountEquipmentLayer.Mode.SADDLE));
        if(kind==GOTLegacyMountModel.Kind.CAMEL)
            addLayer(new GOTLegacyMountEquipmentLayer<>(this,new GOTLegacyMountModel<>(c.bakeLayer(GOTMountModelLayers.CAMEL_CARPET)),"textures/entity/animal/camel/carpet_overlay.png",GOTLegacyMountEquipmentLayer.Mode.CAMEL_CARPET));
    }
    @Override public ResourceLocation getTextureLocation(T e){return texture;}
}
