package got.network;

import got.npc.GOTJaqenHgharEntity;
import got.quest.GOTJaqenQuestSequence;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record C2SJaqenTutorialPacket(Kind kind, int entityId, GOTJaqenQuestSequence.Action action) {
    public enum Kind { ACCEPT, DECLINE, ACTION }
    public static C2SJaqenTutorialPacket accept(int id) { return new C2SJaqenTutorialPacket(Kind.ACCEPT,id,null); }
    public static C2SJaqenTutorialPacket decline(int id) { return new C2SJaqenTutorialPacket(Kind.DECLINE,id,null); }
    public static C2SJaqenTutorialPacket action(GOTJaqenQuestSequence.Action a) { return new C2SJaqenTutorialPacket(Kind.ACTION,-1,a); }
    public static void encode(C2SJaqenTutorialPacket m, FriendlyByteBuf b) { b.writeEnum(m.kind); b.writeInt(m.entityId); b.writeBoolean(m.action!=null); if(m.action!=null)b.writeEnum(m.action); }
    public static C2SJaqenTutorialPacket decode(FriendlyByteBuf b) { Kind k=b.readEnum(Kind.class); int id=b.readInt(); var a=b.readBoolean()?b.readEnum(GOTJaqenQuestSequence.Action.class):null; return new C2SJaqenTutorialPacket(k,id,a); }
    public static void handle(C2SJaqenTutorialPacket m, Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context c=sup.get(); c.enqueueWork(() -> {
            ServerPlayer p=c.getSender(); if(p==null)return;
            if(m.kind==Kind.ACTION && m.action!=null) { GOTJaqenQuestSequence.clientAction(p,m.action); return; }
            Entity e=p.level().getEntity(m.entityId);
            if(!(e instanceof GOTJaqenHgharEntity j) || e.distanceToSqr(p)>100.0D)return;
            if(m.kind==Kind.ACCEPT) GOTJaqenQuestSequence.accept(p,j);
        }); c.setPacketHandled(true);
    }
}
