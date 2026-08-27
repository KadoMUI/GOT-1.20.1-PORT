package got.client.mount;

import got.mount.GOTMountEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

/** Reconstructed 1.7.10 box-model geometry for the non-horse GOT mounts. */
public final class GOTLegacyMountModel<T extends GOTMountEntity> extends HierarchicalModel<T> {
    public enum Kind { RHINO, CAMEL, BOAR }
    private final ModelPart root, head, leg1, leg2, leg3, leg4;
    public GOTLegacyMountModel(ModelPart root) {
        this.root=root; this.head=root.getChild("head"); this.leg1=root.getChild("leg1"); this.leg2=root.getChild("leg2"); this.leg3=root.getChild("leg3"); this.leg4=root.getChild("leg4");
    }
    @Override public ModelPart root(){ return root; }
    @Override public void setupAnim(T e,float limbSwing,float limbAmount,float age,float yaw,float pitch){
        head.yRot=yaw*Mth.DEG_TO_RAD; head.xRot=pitch*Mth.DEG_TO_RAD;
        float a=Mth.cos(limbSwing*0.6662F)*1.4F*limbAmount;
        leg1.xRot=a; leg4.xRot=a; leg2.xRot=-a; leg3.xRot=-a;
    }
    public static LayerDefinition layer(Kind kind){ return layer(kind, 0.0F); }
    public static LayerDefinition layer(Kind kind,float inflate){ return switch(kind){case RHINO->rhino(inflate); case CAMEL->camel(inflate); case BOAR->boar(inflate);}; }
    private static LayerDefinition rhino(float inflate){
        MeshDefinition m=new MeshDefinition(); PartDefinition r=m.getRoot();
        PartDefinition h=r.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0,0).addBox(-5,-2,-22,10,10,16).texOffs(0,0).addBox(-4,-4,-10,1,2,2).mirror().addBox(3,-4,-10,1,2,2), PartPose.offset(0,3,-12));
        h.addOrReplaceChild("horn1",CubeListBuilder.create().texOffs(36,0).addBox(-1,-14,-20,2,8,2),PartPose.rotation(0.2617994F,0,0));
        h.addOrReplaceChild("horn2",CubeListBuilder.create().texOffs(44,0).addBox(-1,-3,-17,2,4,2),PartPose.rotation(-0.17453292F,0,0));
        r.addOrReplaceChild("neck",CubeListBuilder.create().texOffs(52,0).addBox(-7,-4,-7,14,13,8),PartPose.offset(0,3,-12));
        r.addOrReplaceChild("body",CubeListBuilder.create().texOffs(0,26).addBox(-8,-7,-13,16,16,34),PartPose.offset(0,5,0));
        r.addOrReplaceChild("tail",CubeListBuilder.create().texOffs(100,63).addBox(-1.5F,-1,-1,3,8,2),PartPose.offset(0,7,21));
        rhinoLeg(r,"leg1",-8,3,14,false); rhinoLeg(r,"leg2",8,3,14,true); rhinoLeg(r,"leg3",-8,3,-9,false); rhinoLeg(r,"leg4",8,3,-9,true);
        return LayerDefinition.create(m,128,128);
    }
    private static void rhinoLeg(PartDefinition r,String n,float x,float y,float z,boolean mirror){ CubeListBuilder b=CubeListBuilder.create().texOffs(30,76); if(mirror)b=b.mirror(); b=b.addBox(mirror?0:-8,-3,-5,8,12,10).texOffs(0,95).addBox(mirror?1:-7,9,-3,6,12,6); r.addOrReplaceChild(n,b,PartPose.offset(x,y,z)); }
    private static LayerDefinition camel(float inflate){
        MeshDefinition m=new MeshDefinition(); PartDefinition r=m.getRoot();
        r.addOrReplaceChild("body",CubeListBuilder.create().texOffs(0,16).addBox(-4.5F,-5,-10,9,10,22),PartPose.offset(0,10,0));
        r.addOrReplaceChild("humps",CubeListBuilder.create().texOffs(34,0).addBox(-3,-9,-8,6,4,6).addBox(-3,-9,3,6,4,6),PartPose.offset(0,10,0));
        PartDefinition h=r.addOrReplaceChild("head",CubeListBuilder.create().texOffs(0,0).addBox(-3,-4,-8,6,6,8).texOffs(28,0).addBox(-2,-2,-13,4,4,5),PartPose.offset(0,3,-10));
        h.addOrReplaceChild("neck",CubeListBuilder.create().texOffs(42,16).addBox(-2,-1,0,4,13,5),PartPose.rotation(-0.35F,0,0));
        r.addOrReplaceChild("tail",CubeListBuilder.create().texOffs(0,48).addBox(-1,0,0,2,8,2),PartPose.offset(0,9,12));
        camelLeg(r,"leg1",-3,12,8); camelLeg(r,"leg2",3,12,8); camelLeg(r,"leg3",-3,12,-7); camelLeg(r,"leg4",3,12,-7);
        return LayerDefinition.create(m,64,64);
    }
    private static void camelLeg(PartDefinition r,String n,float x,float y,float z){r.addOrReplaceChild(n,CubeListBuilder.create().texOffs(0,48).addBox(-1.5F,0,-1.5F,3,12,3),PartPose.offset(x,y,z));}
    private static LayerDefinition boar(float inflate){
        MeshDefinition m=new MeshDefinition(); PartDefinition r=m.getRoot();
        PartDefinition h=r.addOrReplaceChild("head",CubeListBuilder.create().texOffs(0,0).addBox(-4,-4,-8,8,8,8).texOffs(24,0).addBox(-3,0,-10,6,4,2).texOffs(40,0).addBox(-5,-5,-6,1,2,2).mirror().addBox(4,-5,-6,1,2,2),PartPose.offset(0,12,-6));
        h.addOrReplaceChild("tusks",CubeListBuilder.create().texOffs(0,0).addBox(-4,2,-11,1,1,2).texOffs(1,1).addBox(-4,1,-11.5F,1,1,1).mirror().addBox(3,2,-11,1,1,2).texOffs(1,1).addBox(3,1,-11.5F,1,1,1),PartPose.ZERO);
        r.addOrReplaceChild("body",CubeListBuilder.create().texOffs(28,8).addBox(-5,-10,-7,10,16,8),PartPose.offsetAndRotation(0,11,2,Mth.HALF_PI,0,0));
        pigLeg(r,"leg1",-3,18,7); pigLeg(r,"leg2",3,18,7); pigLeg(r,"leg3",-3,18,-5); pigLeg(r,"leg4",3,18,-5);
        return LayerDefinition.create(m,64,32);
    }
    private static void pigLeg(PartDefinition r,String n,float x,float y,float z){r.addOrReplaceChild(n,CubeListBuilder.create().texOffs(0,16).addBox(-2,0,-2,4,6,4),PartPose.offset(x,y,z));}
}
