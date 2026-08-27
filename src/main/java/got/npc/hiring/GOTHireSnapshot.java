package got.npc.hiring;

import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * Client-safe immutable view of one hireable/hired NPC.
 * No live Entity reference is retained by the GUI.
 */
public record GOTHireSnapshot(
    int entityId,
    String npcName,
    String npcType,
    String factionId,
    boolean hired,
    boolean owner,
    GOTHiredTask task,
    GOTHiredOrder order,
    String squadron,
    int guardRange,
    boolean autoTeleport,
    int level,
    int xp,
    int kills,
    boolean hireAllowed,
    boolean mountedVariant,
    boolean mountedHireAllowed,
    String rejectionReason,
    int requiredAlignment,
    String requirementText,
    List<CostLine> cost
) {
    public record CostLine(String itemId, int amount) {}

    public static void encode(GOTHireSnapshot s, FriendlyByteBuf b) {
        b.writeVarInt(s.entityId());
        b.writeUtf(s.npcName());
        b.writeUtf(s.npcType());
        b.writeUtf(s.factionId());
        b.writeBoolean(s.hired());
        b.writeBoolean(s.owner());
        b.writeEnum(s.task());
        b.writeEnum(s.order());
        b.writeUtf(s.squadron());
        b.writeVarInt(s.guardRange());
        b.writeBoolean(s.autoTeleport());
        b.writeVarInt(s.level());
        b.writeVarInt(s.xp());
        b.writeVarInt(s.kills());
        b.writeBoolean(s.hireAllowed());
        b.writeBoolean(s.mountedVariant());
        b.writeBoolean(s.mountedHireAllowed());
        b.writeUtf(s.rejectionReason());
        b.writeInt(s.requiredAlignment());
        b.writeUtf(s.requirementText());
        b.writeVarInt(s.cost().size());
        for (CostLine c : s.cost()) {
            b.writeUtf(c.itemId());
            b.writeVarInt(c.amount());
        }
    }

    public static GOTHireSnapshot decode(FriendlyByteBuf b) {
        int entityId=b.readVarInt();
        String npcName=b.readUtf();
        String npcType=b.readUtf();
        String factionId=b.readUtf();
        boolean hired=b.readBoolean();
        boolean owner=b.readBoolean();
        GOTHiredTask task=b.readEnum(GOTHiredTask.class);
        GOTHiredOrder order=b.readEnum(GOTHiredOrder.class);
        String squadron=b.readUtf();
        int guardRange=b.readVarInt();
        boolean autoTeleport=b.readBoolean();
        int level=b.readVarInt();
        int xp=b.readVarInt();
        int kills=b.readVarInt();
        boolean hireAllowed=b.readBoolean();
        boolean mountedVariant=b.readBoolean();
        boolean mountedHireAllowed=b.readBoolean();
        String rejectionReason=b.readUtf();
        int requiredAlignment=b.readInt();
        String requirementText=b.readUtf();
        int n=b.readVarInt();
        List<CostLine> cost=new ArrayList<>(n);
        for(int i=0;i<n;i++) cost.add(new CostLine(b.readUtf(), b.readVarInt()));
        return new GOTHireSnapshot(entityId,npcName,npcType,factionId,hired,owner,task,order,squadron,
            guardRange,autoTeleport,level,xp,kills,hireAllowed,mountedVariant,mountedHireAllowed,rejectionReason,requiredAlignment,requirementText,cost);
    }
}
