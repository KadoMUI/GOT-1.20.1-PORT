package got.achievement;

import got.GOTMod;
import got.npc.GOTFactionNpc;
import got.quest.GOTQuestGiver;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTAchievementEvents {
    private static final Set<String> CODES = GOTAchievementCatalog.all().stream().map(GOTAchievementCatalog.Entry::code).collect(Collectors.toUnmodifiableSet());
    private GOTAchievementEvents() {}

    @SubscribeEvent public static void login(PlayerEvent.PlayerLoggedInEvent e) {
        if (e.getEntity() instanceof ServerPlayer p) GOTAchievementHooks.sync(p);
    }
    @SubscribeEvent public static void clone(PlayerEvent.Clone e) { GOTAchievementHooks.copyPersisted(e.getOriginal(), e.getEntity()); }
    @SubscribeEvent public static void respawn(PlayerEvent.PlayerRespawnEvent e) {
        if (e.getEntity() instanceof ServerPlayer p) GOTAchievementHooks.sync(p);
    }

    @SubscribeEvent public static void pickup(EntityItemPickupEvent e) {
        if (!(e.getEntity() instanceof ServerPlayer p)) return;
        ResourceLocation id=ForgeRegistries.ITEMS.getKey(e.getItem().getItem().getItem());
        awardByRegistrySuffix(p,"GET_",id);
        if(id!=null && normalize(id.getPath()).contains("FOUR_LEAF_CLOVER")) GOTAchievementHooks.award(p,"FIND_FOUR_LEAF_CLOVER");
    }

    @SubscribeEvent public static void crafted(PlayerEvent.ItemCraftedEvent e) {
        if (!(e.getEntity() instanceof ServerPlayer p)) return;
        awardByRegistrySuffix(p, "CRAFT_", ForgeRegistries.ITEMS.getKey(e.getCrafting().getItem()));
        if (e.getCrafting().getItem() instanceof got.GOTPouchItem) GOTAchievementHooks.award(p, "CRAFT_POUCH");
    }

    @SubscribeEvent public static void useFinish(LivingEntityUseItemEvent.Finish e) {
        if (!(e.getEntity() instanceof ServerPlayer p)) return;
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(e.getItem().getItem());
        awardByRegistrySuffix(p, "DRINK_", id);
        awardByRegistrySuffix(p, "USE_", id);
    }

    @SubscribeEvent public static void rightClick(PlayerInteractEvent.RightClickItem e) {
        if (!(e.getEntity() instanceof ServerPlayer p) || e.getLevel().isClientSide) return;
        awardByRegistrySuffix(p, "USE_", ForgeRegistries.ITEMS.getKey(e.getItemStack().getItem()));
    }

    @SubscribeEvent public static void death(LivingDeathEvent e) {
        if (!(e.getSource().getEntity() instanceof ServerPlayer p)) return;
        LivingEntity victim = e.getEntity();
        if (victim instanceof GOTFactionNpc) GOTAchievementHooks.award(p, "KILL_NPC");
        if (victim instanceof GOTQuestGiver giver) {
            String role = normalize(giver.getQuestRoleId());
            String code = "KILL_" + role;
            if (CODES.contains(code)) {
                GOTAchievementHooks.award(p, code);
                if (GOTAchievementCatalog.category(GOTAchievementCatalog.Category.LEGENDARY).stream().anyMatch(a -> a.code().equals(code)))
                    GOTAchievementHooks.award(p, "KILL_LEGENDARY_NPC");
            }
        }
        ResourceLocation type = ForgeRegistries.ENTITY_TYPES.getKey(victim.getType());
        awardByRegistrySuffix(p, "KILL_", type);
    }

    @SubscribeEvent public static void tick(TickEvent.PlayerTickEvent e) {
        if (e.phase != TickEvent.Phase.END || !(e.player instanceof ServerPlayer p) || p.tickCount % 20 != 0) return;
        ResourceLocation dim = p.level().dimension().location();
        if (dim.getNamespace().equals(GOTMod.MOD_ID)) GOTAchievementHooks.award(p, "ENTER_KNOWN_WORLD");
        p.level().getBiome(p.blockPosition()).unwrapKey().ifPresent(key -> {
            String code = "ENTER_" + normalize(key.location().getPath());
            if (CODES.contains(code)) GOTAchievementHooks.award(p, code);
            if (key.location().getNamespace().equals(GOTMod.MOD_ID)) recordVisitedBiome(p, key.location());
        });
        checkRide(p);
        checkArmor(p);
    }

    private static void checkRide(ServerPlayer p) {
        if (!p.isPassenger()) return;
        ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(p.getVehicle().getType());
        if (id == null) return;
        String path = normalize(id.getPath());
        for (String code : CODES) {
            if (!code.startsWith("RIDE_")) continue;
            if (equivalent(code.substring(5), path)) GOTAchievementHooks.award(p, code);
        }
    }

    private static void checkArmor(ServerPlayer p) {
        ItemStack head=p.getItemBySlot(EquipmentSlot.HEAD), chest=p.getItemBySlot(EquipmentSlot.CHEST), legs=p.getItemBySlot(EquipmentSlot.LEGS), feet=p.getItemBySlot(EquipmentSlot.FEET);
        if (head.isEmpty() || chest.isEmpty() || legs.isEmpty() || feet.isEmpty()) return;
        String h=itemPath(head), c=itemPath(chest), l=itemPath(legs), f=itemPath(feet);
        for (String code : CODES) {
            if (!code.startsWith("WEAR_FULL_")) continue;
            String token = normalize(code.substring("WEAR_FULL_".length()));
            if (armorMatches(h,token,"HELMET") && armorMatches(c,token,"CHESTPLATE")
                    && armorMatches(l,token,"LEGGINGS") && armorMatches(f,token,"BOOTS")) GOTAchievementHooks.award(p,code);
        }
    }

    private static boolean armorMatches(String item, String token, String slot) {
        String n=normalize(item);
        if (token.equals("IRON") && n.equals("IRON_"+slot)) return true;
        if (token.equals("GOLD") && (n.equals("GOLDEN_"+slot)||n.equals("GOLD_"+slot))) return true;
        return n.contains(token) && n.endsWith("_"+slot);
    }
    private static String itemPath(ItemStack s){ResourceLocation id=ForgeRegistries.ITEMS.getKey(s.getItem()); return id==null?"":id.getPath();}
    private static void awardByRegistrySuffix(ServerPlayer p,String prefix,ResourceLocation id){
        if(id==null)return; String path=normalize(id.getPath());
        for(String code:CODES) if(code.startsWith(prefix)&&equivalent(code.substring(prefix.length()),path)) GOTAchievementHooks.award(p,code);
    }
    private static boolean equivalent(String a,String b){return normalize(a).replace("_","").equals(normalize(b).replace("_",""));}
    private static String normalize(String s){return s==null?"":s.toUpperCase(Locale.ROOT).replaceAll("[^A-Z0-9]+","_").replaceAll("^_|_$","");}

    private static void recordVisitedBiome(ServerPlayer p, ResourceLocation biome) {
        var root=p.getPersistentData().getCompound("GOTAchievementStats");
        var visited=root.getCompound("VisitedBiomes");
        String key=biome.toString();
        if (!visited.getBoolean(key)) { visited.putBoolean(key,true); root.put("VisitedBiomes",visited); p.getPersistentData().put("GOTAchievementStats",root); }
        int count=0; for(String k:visited.getAllKeys()) if(visited.getBoolean(k)) count++;
        for(int n:new int[]{20,40,60,80,100}) if(count>=n) GOTAchievementHooks.award(p,"TRAVEL"+n);
    }
}
