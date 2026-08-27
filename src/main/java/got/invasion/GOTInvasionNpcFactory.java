package got.invasion;

import got.GOTEntities;
import got.npc.GOTArrynNpcEntity;
import got.npc.ArrynNpcRole;
import got.npc.GOTBraavosNpcEntity;
import got.npc.BraavosNpcRole;
import got.npc.GOTDorneNpcEntity;
import got.npc.DorneNpcRole;
import got.npc.GOTDothrakiNpcEntity;
import got.npc.DothrakiNpcRole;
import got.npc.GOTDragonstoneNpcEntity;
import got.npc.DragonstoneNpcRole;
import got.npc.GOTGhiscarNpcEntity;
import got.npc.GhiscarNpcRole;
import got.npc.GOTIbbenNpcEntity;
import got.npc.IbbenNpcRole;
import got.npc.GOTIronbornNpcEntity;
import got.npc.IronbornNpcRole;
import got.npc.GOTJogosNhaiNpcEntity;
import got.npc.JogosNhaiNpcRole;
import got.npc.GOTLorathNpcEntity;
import got.npc.LorathNpcRole;
import got.npc.GOTLysNpcEntity;
import got.npc.LysNpcRole;
import got.npc.GOTMyrNpcEntity;
import got.npc.MyrNpcRole;
import got.npc.GOTNorthNpcEntity;
import got.npc.NorthNpcRole;
import got.npc.GOTNorvosNpcEntity;
import got.npc.NorvosNpcRole;
import got.npc.GOTPentosNpcEntity;
import got.npc.PentosNpcRole;
import got.npc.GOTReachNpcEntity;
import got.npc.ReachNpcRole;
import got.npc.GOTRiverlandsNpcEntity;
import got.npc.RiverlandsNpcRole;
import got.npc.GOTSothoryosNpcEntity;
import got.npc.SothoryosNpcRole;
import got.npc.GOTStormlandsNpcEntity;
import got.npc.StormlandsNpcRole;
import got.npc.GOTTyroshNpcEntity;
import got.npc.TyroshNpcRole;
import got.npc.GOTVolantisNpcEntity;
import got.npc.VolantisNpcRole;
import got.npc.GOTWesterlandsNpcEntity;
import got.npc.WesterlandsNpcRole;
import got.npc.GOTWildlingNpcEntity;
import got.npc.WildlingNpcRole;
import got.npc.GOTYiTiNpcEntity;
import got.npc.YiTiNpcRole;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import java.util.UUID;

public final class GOTInvasionNpcFactory {
    private GOTInvasionNpcFactory() {}
    public static Mob create(ServerLevel level, GOTInvasionType type, String roleId, BlockPos pos, UUID invasionId) {
        RandomSource random=level.random;
        Mob result = switch(type.region()) {
            case ARRYN -> {
                GOTArrynNpcEntity mob = GOTEntities.ARRYN_NPC.get().create(level);
                if (mob == null) yield null;
                ArrynNpcRole role = ArrynNpcRole.byId(roleId.toLowerCase(java.util.Locale.ROOT));
                mob.moveTo(pos, random.nextFloat()*360.0F, 0.0F);
                mob.prepareForSpawn(role, null, false, pos, 80, "invasion:" + invasionId);
                yield mob;
            }
            case BRAAVOS -> {
                GOTBraavosNpcEntity mob = GOTEntities.BRAAVOS_NPC.get().create(level);
                if (mob == null) yield null;
                BraavosNpcRole role = BraavosNpcRole.byId(roleId.toLowerCase(java.util.Locale.ROOT));
                mob.moveTo(pos, random.nextFloat()*360.0F, 0.0F);
                mob.prepareForSpawn(role, null, false, pos, 80, "invasion:" + invasionId);
                yield mob;
            }
            case DORNE -> {
                GOTDorneNpcEntity mob = GOTEntities.DORNE_NPC.get().create(level);
                if (mob == null) yield null;
                DorneNpcRole role = DorneNpcRole.byId(roleId.toLowerCase(java.util.Locale.ROOT));
                mob.moveTo(pos, random.nextFloat()*360.0F, 0.0F);
                mob.prepareForSpawn(role, null, false, pos, 80, "invasion:" + invasionId);
                yield mob;
            }
            case DOTHRAKI -> {
                GOTDothrakiNpcEntity mob = GOTEntities.DOTHRAKI_NPC.get().create(level);
                if (mob == null) yield null;
                DothrakiNpcRole role = DothrakiNpcRole.byId(roleId.toLowerCase(java.util.Locale.ROOT));
                mob.moveTo(pos, random.nextFloat()*360.0F, 0.0F);
                mob.prepareForSpawn(role, null, false, pos, 80, "invasion:" + invasionId);
                if (mob.rollWorldMount()) mob.requestDothrakiHorse();
                yield mob;
            }
            case DRAGONSTONE -> {
                GOTDragonstoneNpcEntity mob = GOTEntities.DRAGONSTONE_NPC.get().create(level);
                if (mob == null) yield null;
                DragonstoneNpcRole role = DragonstoneNpcRole.byId(roleId.toLowerCase(java.util.Locale.ROOT));
                mob.moveTo(pos, random.nextFloat()*360.0F, 0.0F);
                mob.prepareForSpawn(role, null, false, pos, 80, "invasion:" + invasionId);
                yield mob;
            }
            case GHISCAR -> {
                GOTGhiscarNpcEntity mob = GOTEntities.GHISCAR_NPC.get().create(level);
                if (mob == null) yield null;
                GhiscarNpcRole role = GhiscarNpcRole.byId(roleId.toLowerCase(java.util.Locale.ROOT));
                mob.moveTo(pos, random.nextFloat()*360.0F, 0.0F);
                mob.prepareForSpawn(role, null, false, pos, 80, "invasion:" + invasionId);
                yield mob;
            }
            case IBBEN -> {
                GOTIbbenNpcEntity mob = GOTEntities.IBBEN_NPC.get().create(level);
                if (mob == null) yield null;
                IbbenNpcRole role = IbbenNpcRole.byId(roleId.toLowerCase(java.util.Locale.ROOT));
                mob.moveTo(pos, random.nextFloat()*360.0F, 0.0F);
                mob.prepareForSpawn(role, null, false, pos, 80, "invasion:" + invasionId);
                yield mob;
            }
            case IRONBORN -> {
                GOTIronbornNpcEntity mob = GOTEntities.IRONBORN_NPC.get().create(level);
                if (mob == null) yield null;
                IronbornNpcRole role = IronbornNpcRole.byId(roleId.toLowerCase(java.util.Locale.ROOT));
                mob.moveTo(pos, random.nextFloat()*360.0F, 0.0F);
                mob.prepareForSpawn(role, null, false, pos, 80, "invasion:" + invasionId);
                yield mob;
            }
            case JOGOS_NHAI -> {
                GOTJogosNhaiNpcEntity mob = GOTEntities.JOGOS_NHAI_NPC.get().create(level);
                if (mob == null) yield null;
                JogosNhaiNpcRole role = JogosNhaiNpcRole.byId(roleId.toLowerCase(java.util.Locale.ROOT));
                mob.moveTo(pos, random.nextFloat()*360.0F, 0.0F);
                mob.prepareForSpawn(role, null, false, pos, 80, "invasion:" + invasionId);
                yield mob;
            }
            case LORATH -> {
                GOTLorathNpcEntity mob = GOTEntities.LORATH_NPC.get().create(level);
                if (mob == null) yield null;
                LorathNpcRole role = LorathNpcRole.byId(roleId.toLowerCase(java.util.Locale.ROOT));
                mob.moveTo(pos, random.nextFloat()*360.0F, 0.0F);
                mob.prepareForSpawn(role, null, false, pos, 80, "invasion:" + invasionId);
                yield mob;
            }
            case LYS -> {
                GOTLysNpcEntity mob = GOTEntities.LYS_NPC.get().create(level);
                if (mob == null) yield null;
                LysNpcRole role = LysNpcRole.byId(roleId.toLowerCase(java.util.Locale.ROOT));
                mob.moveTo(pos, random.nextFloat()*360.0F, 0.0F);
                mob.prepareForSpawn(role, null, false, pos, 80, "invasion:" + invasionId);
                yield mob;
            }
            case MYR -> {
                GOTMyrNpcEntity mob = GOTEntities.MYR_NPC.get().create(level);
                if (mob == null) yield null;
                MyrNpcRole role = MyrNpcRole.byId(roleId.toLowerCase(java.util.Locale.ROOT));
                mob.moveTo(pos, random.nextFloat()*360.0F, 0.0F);
                mob.prepareForSpawn(role, null, false, pos, 80, "invasion:" + invasionId);
                yield mob;
            }
            case NORTH -> {
                GOTNorthNpcEntity mob = GOTEntities.NORTH_NPC.get().create(level);
                if (mob == null) yield null;
                NorthNpcRole role = NorthNpcRole.byId(roleId.toLowerCase(java.util.Locale.ROOT));
                mob.moveTo(pos, random.nextFloat()*360.0F, 0.0F);
                mob.prepareForSpawn(role, null, false, pos, 80, "invasion:" + invasionId);
                yield mob;
            }
            case NORVOS -> {
                GOTNorvosNpcEntity mob = GOTEntities.NORVOS_NPC.get().create(level);
                if (mob == null) yield null;
                NorvosNpcRole role = NorvosNpcRole.byId(roleId.toLowerCase(java.util.Locale.ROOT));
                mob.moveTo(pos, random.nextFloat()*360.0F, 0.0F);
                mob.prepareForSpawn(role, null, false, pos, 80, "invasion:" + invasionId);
                yield mob;
            }
            case PENTOS -> {
                GOTPentosNpcEntity mob = GOTEntities.PENTOS_NPC.get().create(level);
                if (mob == null) yield null;
                PentosNpcRole role = PentosNpcRole.byId(roleId.toLowerCase(java.util.Locale.ROOT));
                mob.moveTo(pos, random.nextFloat()*360.0F, 0.0F);
                mob.prepareForSpawn(role, null, false, pos, 80, "invasion:" + invasionId);
                yield mob;
            }
            case REACH -> {
                GOTReachNpcEntity mob = GOTEntities.REACH_NPC.get().create(level);
                if (mob == null) yield null;
                ReachNpcRole role = ReachNpcRole.byId(roleId.toLowerCase(java.util.Locale.ROOT));
                mob.moveTo(pos, random.nextFloat()*360.0F, 0.0F);
                mob.prepareForSpawn(role, null, false, pos, 80, "invasion:" + invasionId);
                yield mob;
            }
            case RIVERLANDS -> {
                GOTRiverlandsNpcEntity mob = GOTEntities.RIVERLANDS_NPC.get().create(level);
                if (mob == null) yield null;
                RiverlandsNpcRole role = RiverlandsNpcRole.byId(roleId.toLowerCase(java.util.Locale.ROOT));
                mob.moveTo(pos, random.nextFloat()*360.0F, 0.0F);
                mob.prepareForSpawn(role, null, false, pos, 80, "invasion:" + invasionId);
                yield mob;
            }
            case SOTHORYOS -> {
                GOTSothoryosNpcEntity mob = GOTEntities.SOTHORYOS_NPC.get().create(level);
                if (mob == null) yield null;
                SothoryosNpcRole role = SothoryosNpcRole.byId(roleId.toLowerCase(java.util.Locale.ROOT));
                mob.moveTo(pos, random.nextFloat()*360.0F, 0.0F);
                mob.prepareForSpawn(role, null, false, pos, 80, "invasion:" + invasionId);
                yield mob;
            }
            case STORMLANDS -> {
                GOTStormlandsNpcEntity mob = GOTEntities.STORMLANDS_NPC.get().create(level);
                if (mob == null) yield null;
                StormlandsNpcRole role = StormlandsNpcRole.byId(roleId.toLowerCase(java.util.Locale.ROOT));
                mob.moveTo(pos, random.nextFloat()*360.0F, 0.0F);
                mob.prepareForSpawn(role, null, false, pos, 80, "invasion:" + invasionId);
                yield mob;
            }
            case TYROSH -> {
                GOTTyroshNpcEntity mob = GOTEntities.TYROSH_NPC.get().create(level);
                if (mob == null) yield null;
                TyroshNpcRole role = TyroshNpcRole.byId(roleId.toLowerCase(java.util.Locale.ROOT));
                mob.moveTo(pos, random.nextFloat()*360.0F, 0.0F);
                mob.prepareForSpawn(role, null, false, pos, 80, "invasion:" + invasionId);
                yield mob;
            }
            case VOLANTIS -> {
                GOTVolantisNpcEntity mob = GOTEntities.VOLANTIS_NPC.get().create(level);
                if (mob == null) yield null;
                VolantisNpcRole role = VolantisNpcRole.byId(roleId.toLowerCase(java.util.Locale.ROOT));
                mob.moveTo(pos, random.nextFloat()*360.0F, 0.0F);
                mob.prepareForSpawn(role, null, false, pos, 80, "invasion:" + invasionId);
                yield mob;
            }
            case WESTERLANDS -> {
                GOTWesterlandsNpcEntity mob = GOTEntities.WESTERLANDS_NPC.get().create(level);
                if (mob == null) yield null;
                WesterlandsNpcRole role = WesterlandsNpcRole.byId(roleId.toLowerCase(java.util.Locale.ROOT));
                mob.moveTo(pos, random.nextFloat()*360.0F, 0.0F);
                mob.prepareForSpawn(role, null, false, pos, 80, "invasion:" + invasionId);
                yield mob;
            }
            case WILDLING -> {
                GOTWildlingNpcEntity mob = GOTEntities.WILDLING_NPC.get().create(level);
                if (mob == null) yield null;
                WildlingNpcRole role = WildlingNpcRole.byId(roleId.toLowerCase(java.util.Locale.ROOT));
                mob.moveTo(pos, random.nextFloat()*360.0F, 0.0F);
                mob.prepareForSpawn(role, null, false, pos, 80, "invasion:" + invasionId);
                yield mob;
            }
            case YI_TI -> {
                GOTYiTiNpcEntity mob = GOTEntities.YI_TI_NPC.get().create(level);
                if (mob == null) yield null;
                YiTiNpcRole role = YiTiNpcRole.byId(roleId.toLowerCase(java.util.Locale.ROOT));
                mob.moveTo(pos, random.nextFloat()*360.0F, 0.0F);
                mob.prepareForSpawn(role, null, false, pos, 80, "invasion:" + invasionId);
                yield mob;
            }
        };
        if(result != null) { result.getPersistentData().putUUID("GOTInvasionId", invasionId); result.setPersistenceRequired(); }
        return result;
    }
}
