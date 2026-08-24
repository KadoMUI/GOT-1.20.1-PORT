package got.claim;

import got.GOTAbstractBannerEntity;
import got.GOTMod;
import got.npc.GOTFactionNpc;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.CartographyTableBlock;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.GrindstoneBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.LoomBlock;
import net.minecraft.world.level.block.SmithingTableBlock;
import net.minecraft.world.level.block.StonecutterBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.event.level.PistonEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Iterator;

/** Full Forge event enforcement layer for player, mob, projectile, and explosion changes. */
@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTBannerProtectionEvents {
    private GOTBannerProtectionEvents() {}

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onBreak(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)
                || !(event.getPlayer() instanceof ServerPlayer player)) return;
        if (GOTBannerProtection.isDenied(level, event.getPos(), player,
                GOTBannerPermission.FULL, true)) event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlace(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        Entity actor = event.getEntity();
        if (actor instanceof ServerPlayer player) {
            if (GOTBannerProtection.isDenied(level, event.getPos(), player,
                    GOTBannerPermission.FULL, true)) event.setCanceled(true);
        } else if (actor != null && GOTBannerProtection.isClaimed(level, event.getPos())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onFarmlandTrample(BlockEvent.FarmlandTrampleEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        Entity actor = event.getEntity();
        if (actor instanceof ServerPlayer player) {
            if (GOTBannerProtection.isDenied(level, event.getPos(), player,
                    GOTBannerPermission.FULL, false)) event.setCanceled(true);
        } else if (GOTBannerProtection.isClaimed(level, event.getPos())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingDestroyBlock(LivingDestroyBlockEvent event) {
        if (event.getEntity().level() instanceof ServerLevel level
                && GOTBannerProtection.isClaimed(level, event.getPos())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onFluidPlace(BlockEvent.FluidPlaceBlockEvent event) {
        if (event.getLevel() instanceof ServerLevel level
                && GOTBannerProtection.isClaimed(level, event.getPos())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPiston(PistonEvent.Pre event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        net.minecraft.world.level.block.piston.PistonStructureResolver resolver = event.getStructureHelper();
        if (resolver == null || !resolver.resolve()) return;
        boolean touchesClaim = resolver.getToPush().stream().anyMatch(pos ->
                GOTBannerProtection.isClaimed(level, pos)
                        || GOTBannerProtection.isClaimed(level, pos.relative(event.getDirection())))
                || resolver.getToDestroy().stream().anyMatch(pos ->
                GOTBannerProtection.isClaimed(level, pos));
        if (touchesClaim) event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() != InteractionHand.MAIN_HAND
                || !(event.getEntity() instanceof ServerPlayer player)
                || !(event.getLevel() instanceof ServerLevel level)) return;
        GOTBannerPermission permission = classify(level, event.getPos(),
                event.getItemStack(), player.isShiftKeyDown());
        if (GOTBannerProtection.isDenied(level, event.getPos(), player, permission, true)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onBucket(FillBucketEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)
                || !(player.level() instanceof ServerLevel level)
                || !(event.getTarget() instanceof BlockHitResult hit)) return;
        if (GOTBannerProtection.isDenied(level, hit.getBlockPos(), player,
                GOTBannerPermission.FULL, true)) event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onAttackEntity(AttackEntityEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)
                || !(player.level() instanceof ServerLevel level)) return;
        Entity target = event.getTarget();
        if (target instanceof GOTAbstractBannerEntity banner) {
            if (!GOTBannerProtection.mayDamageBanner(banner, player)) event.setCanceled(true);
        } else if (isProtectedDecoration(target)
                && GOTBannerProtection.isDenied(level, target.blockPosition(), player,
                GOTBannerPermission.FULL, true)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onInteractEntity(PlayerInteractEvent.EntityInteract event) {
        if (denyEntityInteraction(event.getEntity(), event.getTarget())) event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onInteractEntitySpecific(PlayerInteractEvent.EntityInteractSpecific event) {
        if (denyEntityInteraction(event.getEntity(), event.getTarget())) event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onExplosion(ExplosionEvent.Detonate event) {
        if (!GOTBannerClaimConfig.PROTECT_FROM_EXPLOSIONS.get()) return;
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        Entity source = event.getExplosion().getDirectSourceEntity();
        ServerPlayer player = GOTBannerProtection.responsiblePlayer(source);
        Iterator<BlockPos> positions = event.getAffectedBlocks().iterator();
        while (positions.hasNext()) {
            BlockPos position = positions.next();
            boolean blocked = player == null
                    ? GOTBannerProtection.isClaimed(level, position)
                    : GOTBannerProtection.isDenied(level, position, player,
                    GOTBannerPermission.FULL, false);
            if (blocked) positions.remove();
        }
        event.getAffectedEntities().removeIf(entity -> {
            if (entity instanceof GOTAbstractBannerEntity banner && banner.isClaimActive()) {
                return player == null || !GOTBannerProtection.isAllowed(
                        banner, player, GOTBannerPermission.FULL);
            }
            if (!isProtectedInteractionTarget(entity)) return false;
            return player == null
                    ? GOTBannerProtection.isClaimed(level, entity.blockPosition())
                    : GOTBannerProtection.isDenied(level, entity.blockPosition(), player,
                    entity instanceof Container
                            ? GOTBannerPermission.CONTAINERS : GOTBannerPermission.FULL, false);
        });
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        if (!GOTBannerClaimConfig.PROTECT_FROM_PROJECTILES.get()) return;
        Projectile projectile = event.getProjectile();
        if (!(projectile.level() instanceof ServerLevel level)) return;
        ServerPlayer player = GOTBannerProtection.responsiblePlayer(projectile);
        boolean blocked = false;
        if (event.getRayTraceResult() instanceof BlockHitResult hit) {
            blocked = player == null
                    ? GOTBannerProtection.isClaimed(level, hit.getBlockPos())
                    : GOTBannerProtection.isDenied(level, hit.getBlockPos(), player,
                    GOTBannerPermission.FULL, true);
        } else if (event.getRayTraceResult() instanceof EntityHitResult hit) {
            Entity target = hit.getEntity();
            if (target instanceof GOTAbstractBannerEntity banner) {
                blocked = player == null ? banner.isClaimActive()
                        : !GOTBannerProtection.mayDamageBanner(banner, player);
            } else if (isProtectedInteractionTarget(target)) {
                blocked = player == null
                        ? GOTBannerProtection.isClaimed(level, target.blockPosition())
                        : GOTBannerProtection.isDenied(level, target.blockPosition(), player,
                        target instanceof Container
                                ? GOTBannerPermission.CONTAINERS : GOTBannerPermission.FULL, true);
            }
        }
        if (blocked) {
            event.setImpactResult(ProjectileImpactEvent.ImpactResult.STOP_AT_CURRENT_NO_DAMAGE);
            projectile.discard();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onMobSpawn(MobSpawnEvent.FinalizeSpawn event) {
        if (!GOTBannerClaimConfig.PREVENT_HOSTILE_SPAWNS.get()) return;
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        Entity entity = event.getEntity();
        if (entity instanceof Enemy && GOTBannerProtection.isClaimed(level, entity.blockPosition())) {
            event.setSpawnCancelled(true);
        } else if (entity instanceof GOTFactionNpc npc
                && GOTBannerProtection.isFactionBlocked(level, entity.blockPosition(), npc.getFaction())) {
            event.setSpawnCancelled(true);
        }
    }

    private static boolean isProtectedDecoration(Entity entity) {
        return entity instanceof HangingEntity || entity instanceof ArmorStand;
    }

    private static boolean isProtectedInteractionTarget(Entity entity) {
        return isProtectedDecoration(entity) || entity instanceof Container;
    }

    private static boolean denyEntityInteraction(Entity actor, Entity target) {
        if (!(actor instanceof ServerPlayer player)
                || !(player.level() instanceof ServerLevel level)
                || !isProtectedInteractionTarget(target)) return false;
        GOTBannerPermission permission = target instanceof Container
                ? GOTBannerPermission.CONTAINERS : GOTBannerPermission.FULL;
        return GOTBannerProtection.isDenied(level, target.blockPosition(), player,
                permission, true);
    }

    private static GOTBannerPermission classify(ServerLevel level, BlockPos position,
                                                ItemStack held, boolean sneaking) {
        if (sneaking && !held.isEmpty()) return GOTBannerPermission.FULL;
        BlockState state = level.getBlockState(position);
        net.minecraft.world.level.block.Block block = state.getBlock();
        if (block instanceof DoorBlock || block instanceof TrapDoorBlock
                || block instanceof FenceGateBlock) return GOTBannerPermission.DOORS;
        if (block instanceof BedBlock) return GOTBannerPermission.BEDS;
        if (block instanceof ButtonBlock || block instanceof LeverBlock
                || block instanceof BasePressurePlateBlock) return GOTBannerPermission.SWITCHES;
        if (block instanceof CraftingTableBlock || block instanceof AnvilBlock
                || block instanceof SmithingTableBlock || block instanceof CartographyTableBlock
                || block instanceof LoomBlock || block instanceof GrindstoneBlock
                || block instanceof StonecutterBlock || block instanceof EnchantmentTableBlock) {
            return GOTBannerPermission.TABLES;
        }
        if (block instanceof CakeBlock || idContains(block, "mug", "goblet", "ale_horn",
                "wine_glass", "skull_cup", "kebab", "fermentation_barrel")) {
            return GOTBannerPermission.FOOD;
        }
        if (idContains(block, "ender_chest")) return GOTBannerPermission.PERSONAL_CONTAINERS;
        BlockEntity blockEntity = level.getBlockEntity(position);
        if (blockEntity instanceof Container || idContains(block, "weapon_rack", "armor_stand",
                "bookshelf_storage")) return GOTBannerPermission.CONTAINERS;
        if (held.getItem() instanceof BlockItem || held.getItem() instanceof BucketItem) {
            return GOTBannerPermission.FULL;
        }
        return GOTBannerPermission.FULL;
    }

    private static boolean idContains(net.minecraft.world.level.block.Block block, String... needles) {
        ResourceLocation id = ForgeRegistries.BLOCKS.getKey(block);
        if (id == null) return false;
        String path = id.getPath();
        for (String needle : needles) if (path.contains(needle)) return true;
        return false;
    }
}
