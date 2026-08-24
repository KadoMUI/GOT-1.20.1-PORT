package got.world.genlayer;
public class GOTGenLayerZoom extends GOTGenLayer {
 public GOTGenLayerZoom(long seed,GOTGenLayer parent){super(seed);gotParent=parent;}
 public static GOTGenLayer magnify(long seed,GOTGenLayer layer,int times){for(int i=0;i<times;i++)layer=new GOTGenLayerZoom(seed+i,layer);return layer;}
 public int[] getInts(int x,int z,int w,int h){int px=x>>1,pz=z>>1,pw=(w>>1)+2,ph=(h>>1)+2;int[] p=gotParent.getInts(px,pz,pw,ph);int ew=(pw-1)*2,eh=(ph-1)*2;int[] e=new int[ew*eh];for(int dz=0;dz<ph-1;dz++){int a=p[dz*pw],c=p[(dz+1)*pw];for(int dx=0;dx<pw-1;dx++){initChunkSeed((dx+px)<<1,(dz+pz)<<1);int b=p[dx+1+dz*pw],d=p[dx+1+(dz+1)*pw];int i=(dx*2)+(dz*2)*ew;e[i]=a;e[i+ew]=selectRandom(a,c);e[i+1]=selectRandom(a,b);e[i+1+ew]=selectModeOrRandom(a,b,c,d);a=b;c=d;}}int[] out=new int[w*h];for(int dz=0;dz<h;dz++)System.arraycopy(e,(dz+(z&1))*ew+(x&1),out,dz*w,w);return out;}
}
