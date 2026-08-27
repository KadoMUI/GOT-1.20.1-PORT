package got.client.wildlife;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

/** Shared modern quadruped geometry for legacy GOT wildlife. */
public final class GOTWildlifeModel<T extends LivingEntity> extends HierarchicalModel<T> {
    public enum Kind { DEER, BEAR, BISON, DIREWOLF, ELEPHANT, MAMMOTH, GIRAFFE, LION, ORYX, DIKDIK, WALRUS, BEAVER, SHADOWCAT }
    private final ModelPart root, body, head, leg0, leg1, leg2, leg3;
    private final Kind kind;
    public GOTWildlifeModel(ModelPart root, Kind kind){this.root=root;this.kind=kind;body=root.getChild("body");head=root.getChild("head");leg0=root.getChild("leg0");leg1=root.getChild("leg1");leg2=root.getChild("leg2");leg3=root.getChild("leg3");}
    public static LayerDefinition deer(){return create(Kind.DEER);} public static LayerDefinition bear(){return create(Kind.BEAR);} public static LayerDefinition bison(){return create(Kind.BISON);} public static LayerDefinition direwolf(){return create(Kind.DIREWOLF);}
    public static LayerDefinition elephant(){return create(Kind.ELEPHANT);} public static LayerDefinition mammoth(){return create(Kind.MAMMOTH);} public static LayerDefinition giraffe(){return create(Kind.GIRAFFE);} public static LayerDefinition lion(){return create(Kind.LION);} public static LayerDefinition oryx(){return create(Kind.ORYX);} public static LayerDefinition dikdik(){return create(Kind.DIKDIK);} public static LayerDefinition walrus(){return create(Kind.WALRUS);} public static LayerDefinition beaver(){return create(Kind.BEAVER);} public static LayerDefinition shadowcat(){return create(Kind.SHADOWCAT);}
    private static LayerDefinition create(Kind kind){
        MeshDefinition mesh=new MeshDefinition(); PartDefinition r=mesh.getRoot();
        switch(kind){
            case DEER, ORYX, DIKDIK -> deerLike(r, kind==Kind.DIKDIK?0.72F:1F, kind==Kind.ORYX);
            case BEAR -> bearLike(r);
            case BISON -> bisonLike(r);
            case DIREWOLF, LION, SHADOWCAT -> predator(r, kind);
            case ELEPHANT, MAMMOTH -> elephantLike(r,kind==Kind.MAMMOTH);
            case GIRAFFE -> giraffeLike(r);
            case WALRUS -> walrusLike(r);
            case BEAVER -> beaverLike(r);
        }
        return LayerDefinition.create(mesh,128,128);
    }
    private static void deerLike(PartDefinition r,float s,boolean longHorn){
        r.addOrReplaceChild("body",CubeListBuilder.create().texOffs(0,16).addBox(-4*s,-5*s,-8*s,8*s,10*s,16*s),PartPose.offsetAndRotation(0,13,0,Mth.HALF_PI,0,0));
        PartDefinition h=r.addOrReplaceChild("head",CubeListBuilder.create().texOffs(0,0).addBox(-3*s,-5*s,-6*s,6*s,7*s,7*s).texOffs(26,0).addBox(-2*s,-2*s,-10*s,4*s,4*s,5*s),PartPose.offset(0,9,-8));
        if(longHorn){h.addOrReplaceChild("antlerL",CubeListBuilder.create().texOffs(52,0).addBox(-.5F,-10,-.5F,1,10,1),PartPose.offset(2,-4,-1));h.addOrReplaceChild("antlerR",CubeListBuilder.create().texOffs(52,0).addBox(-.5F,-10,-.5F,1,10,1),PartPose.offset(-2,-4,-1));}
        else {h.addOrReplaceChild("antlerL",CubeListBuilder.create().texOffs(12,34).addBox(-1,-8,-1,2,8,2),PartPose.offset(2,-4,-1));h.addOrReplaceChild("antlerR",CubeListBuilder.create().texOffs(12,34).addBox(-1,-8,-1,2,8,2),PartPose.offset(-2,-4,-1));}
        legs(r,2*s,8*s,2*s);
    }
    private static void bearLike(PartDefinition r){r.addOrReplaceChild("body",CubeListBuilder.create().texOffs(0,18).addBox(-6,-7,-8,12,14,16),PartPose.offsetAndRotation(0,13,2,Mth.HALF_PI,0,0));r.addOrReplaceChild("head",CubeListBuilder.create().texOffs(0,0).addBox(-5,-5,-5,10,9,8).texOffs(36,0).addBox(-3,-1,-8,6,4,4),PartPose.offset(0,10,-8));legs(r,3.5F,7,5);}
    private static void bisonLike(PartDefinition r){r.addOrReplaceChild("body",CubeListBuilder.create().texOffs(0,20).addBox(-7,-7,-9,14,14,18).texOffs(0,52).addBox(-8,-8,-9,16,11,10),PartPose.offsetAndRotation(0,12,2,Mth.HALF_PI,0,0));PartDefinition h=r.addOrReplaceChild("head",CubeListBuilder.create().texOffs(0,0).addBox(-5,-5,-6,10,10,8).texOffs(36,0).addBox(-3,-1,-10,6,5,5),PartPose.offset(0,10,-9));h.addOrReplaceChild("hornL",CubeListBuilder.create().texOffs(52,0).addBox(0,-1,-1,6,2,2),PartPose.offset(4,-3,-2));h.addOrReplaceChild("hornR",CubeListBuilder.create().texOffs(52,0).addBox(-6,-1,-1,6,2,2),PartPose.offset(-4,-3,-2));legs(r,3.5F,7,6);}
    private static void predator(PartDefinition r,Kind k){float w=k==Kind.DIREWOLF?4:4.5F;r.addOrReplaceChild("body",CubeListBuilder.create().texOffs(18,14).addBox(-w,-5,-7,w*2,10,14),PartPose.offsetAndRotation(0,14,1,Mth.HALF_PI,0,0));r.addOrReplaceChild("head",CubeListBuilder.create().texOffs(0,0).addBox(-4,-4,-5,8,8,7).texOffs(0,15).addBox(-2,-1,-9,4,4,5),PartPose.offset(0,11,-7));r.addOrReplaceChild("tail",CubeListBuilder.create().texOffs(52,14).addBox(-1,-1,0,2,2,10),PartPose.offsetAndRotation(0,12,8,-0.55F,0,0));legs(r,2.3F,7,4);}
    private static void elephantLike(PartDefinition r,boolean mammoth){r.addOrReplaceChild("body",CubeListBuilder.create().texOffs(0,28).addBox(-8,-9,-10,16,18,20),PartPose.offset(0,9,2));PartDefinition h=r.addOrReplaceChild("head",CubeListBuilder.create().texOffs(0,0).addBox(-6,-6,-7,12,12,10).texOffs(46,0).addBox(-2,-1,-14,4,4,10),PartPose.offset(0,7,-10));h.addOrReplaceChild("tuskL",CubeListBuilder.create().texOffs(70,0).addBox(-1,0,-8,2,2,8),PartPose.offset(4,2,-5));h.addOrReplaceChild("tuskR",CubeListBuilder.create().texOffs(70,0).addBox(-1,0,-8,2,2,8),PartPose.offset(-4,2,-5));legs(r,5,12,5);}
    private static void giraffeLike(PartDefinition r){r.addOrReplaceChild("body",CubeListBuilder.create().texOffs(0,24).addBox(-5,-5,-8,10,10,16),PartPose.offset(0,12,1));r.addOrReplaceChild("head",CubeListBuilder.create().texOffs(0,0).addBox(-3,-4,-5,6,6,7).texOffs(28,0).addBox(-2,-1,-9,4,3,5),PartPose.offset(0,-2,-7));r.addOrReplaceChild("neck",CubeListBuilder.create().texOffs(52,24).addBox(-3,-16,-3,6,18,6),PartPose.offset(0,10,-6));legs(r,3,14,3);}
    private static void walrusLike(PartDefinition r){r.addOrReplaceChild("body",CubeListBuilder.create().texOffs(0,24).addBox(-8,-6,-10,16,12,20),PartPose.offset(0,16,1));PartDefinition h=r.addOrReplaceChild("head",CubeListBuilder.create().texOffs(0,0).addBox(-6,-5,-7,12,10,9),PartPose.offset(0,15,-10));h.addOrReplaceChild("tuskL",CubeListBuilder.create().texOffs(50,0).addBox(-1,0,-1,2,9,2),PartPose.offset(3,3,-6));h.addOrReplaceChild("tuskR",CubeListBuilder.create().texOffs(50,0).addBox(-1,0,-1,2,9,2),PartPose.offset(-3,3,-6));legs(r,5,3,4);}
    private static void beaverLike(PartDefinition r){r.addOrReplaceChild("body",CubeListBuilder.create().texOffs(0,18).addBox(-5,-5,-7,10,10,14),PartPose.offset(0,17,1));r.addOrReplaceChild("head",CubeListBuilder.create().texOffs(0,0).addBox(-4,-4,-5,8,8,7),PartPose.offset(0,15,-7));r.addOrReplaceChild("tail",CubeListBuilder.create().texOffs(52,18).addBox(-4,-1,0,8,2,11),PartPose.offsetAndRotation(0,18,7,-0.25F,0,0));legs(r,3,4,3);}
    private static void legs(PartDefinition r,float x,float height,float width){CubeListBuilder b=CubeListBuilder.create().texOffs(0,64).addBox(-width/2,-1,-width/2,width,height,width);r.addOrReplaceChild("leg0",b,PartPose.offset(-x,18,-5));r.addOrReplaceChild("leg1",b,PartPose.offset(x,18,-5));r.addOrReplaceChild("leg2",b,PartPose.offset(-x,18,6));r.addOrReplaceChild("leg3",b,PartPose.offset(x,18,6));}
    @Override public ModelPart root(){return root;}
    @Override public void setupAnim(T e,float swing,float amount,float age,float yaw,float pitch){head.yRot=yaw*Mth.DEG_TO_RAD;head.xRot=pitch*Mth.DEG_TO_RAD;float a=Mth.cos(swing*.6662F)*1.4F*amount;leg0.xRot=a;leg3.xRot=a;leg1.xRot=-a;leg2.xRot=-a;if(kind==Kind.DIREWOLF||kind==Kind.LION||kind==Kind.SHADOWCAT||kind==Kind.BEAVER)root.getChild("tail").xRot=-.55F+Mth.cos(age*.12F)*.08F;}
}
