package got.compat.epicfight;

import got.GOTEntities;
import got.GOTMod;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import yesman.epicfight.api.forgeevent.EntityPatchRegistryEvent;
import yesman.epicfight.gameasset.Armatures;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Optional Epic Fight integration. This class is loaded only through GOTMod's
 * ModList + reflection gate; never reference it from unconditional GOT code.
 *
 * IMPORTANT: RegistryObject#get() must never be called from bootstrap(). GOT's
 * DeferredRegister entries are not populated during the mod CONSTRUCT phase.
 * The suppliers below are resolved only from later lifecycle/events.
 */
public final class GOTEpicFightCompat {
    private static boolean bootstrapped;

    private static final List<Supplier<? extends EntityType<?>>> TARGET_ENTITY_TYPES = List.of(
            GOTEntities.NORTH_NPC,
            GOTEntities.WESTERLANDS_NPC,
            GOTEntities.RIVERLANDS_NPC,
            GOTEntities.ARRYN_NPC,
            GOTEntities.CROWNLANDS_NPC,
            GOTEntities.DRAGONSTONE_NPC,
            GOTEntities.REACH_NPC,
            GOTEntities.STORMLANDS_NPC,
            GOTEntities.DORNE_NPC,
            GOTEntities.IRONBORN_NPC,
            GOTEntities.WILDLING_NPC,
            GOTEntities.NIGHT_WATCH_NPC,
            GOTEntities.WHITE_WALKER_NPC,
            GOTEntities.BRAAVOS_NPC,
            GOTEntities.JAQEN_HGHAR,
            GOTEntities.PENTOS_NPC,
            GOTEntities.VOLANTIS_NPC,
            GOTEntities.LYS_NPC,
            GOTEntities.MYR_NPC,
            GOTEntities.TYROSH_NPC,
            GOTEntities.GHISCAR_NPC,
            GOTEntities.DOTHRAKI_NPC,
            GOTEntities.YI_TI_NPC,
            GOTEntities.ASSHAI_NPC,
            GOTEntities.IBBEN_NPC,
            GOTEntities.JOGOS_NHAI_NPC,
            GOTEntities.QARTH_NPC,
            GOTEntities.LORATH_NPC,
            GOTEntities.QOHOR_NPC,
            GOTEntities.LHAZAR_NPC,
            GOTEntities.NORVOS_NPC,
            GOTEntities.MOSSOVY_NPC,
            GOTEntities.GOLDEN_COMPANY_NPC,
            GOTEntities.SUMMER_ISLES_NPC,
            GOTEntities.SOTHORYOS_NPC
    );

    private GOTEpicFightCompat() {}

    public static void bootstrap(IEventBus modBus) {
        if (bootstrapped) return;
        bootstrapped = true;

        modBus.addListener(GOTEpicFightCompat::registerEntityPatches);
        modBus.addListener(GOTEpicFightCompat::commonSetup);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            bootstrapClient(modBus);
        }

        // Do not resolve DeferredRegister objects here: Forge is still in CONSTRUCT.
        GOTMod.LOGGER.info("Registered optional Epic Fight hooks for {} GOT humanoid entity types",
                TARGET_ENTITY_TYPES.size());
    }

    private static void registerEntityPatches(EntityPatchRegistryEvent event) {
        try {
            for (EntityType<?> entityType : targetEntityTypes()) {
                event.getTypeEntry().put(entityType, ignoredEntity -> GOTHumanoidPatch::new);
            }
        } catch (RuntimeException | LinkageError exception) {
            // Epic Fight is optional. A bridge failure must not take down base GOT.
            GOTMod.LOGGER.error("Failed to register GOT Epic Fight entity patches; continuing without GOT NPC patches",
                    exception);
        }
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            try {
                for (EntityType<?> entityType : targetEntityTypes()) {
                    Armatures.registerEntityTypeArmature(entityType, Armatures.BIPED);
                }
            } catch (RuntimeException | LinkageError exception) {
                // Keep the base mod loadable even if an optional Epic Fight API changes.
                GOTMod.LOGGER.error("Failed to register GOT Epic Fight armatures; continuing without GOT armature bindings",
                        exception);
            }
        });
    }

    private static void bootstrapClient(IEventBus modBus) {
        try {
            Class<?> clientBootstrap = Class.forName("got.compat.epicfight.client.GOTEpicFightClient");
            clientBootstrap.getMethod("bootstrap", IEventBus.class).invoke(null, modBus);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException exception) {
            throw new IllegalStateException("Failed to initialize Conquest of Westeros Epic Fight client compatibility",
                    exception);
        }
    }

    /**
     * Resolves GOT entity RegistryObjects only after Forge registration has had a chance
     * to complete. Callers must therefore invoke this from lifecycle/event callbacks,
     * never during GOTMod construction.
     */
    public static List<EntityType<?>> targetEntityTypes() {
        List<EntityType<?>> resolved = new ArrayList<>(TARGET_ENTITY_TYPES.size());
        for (Supplier<? extends EntityType<?>> supplier : TARGET_ENTITY_TYPES) {
            resolved.add(supplier.get());
        }
        return List.copyOf(resolved);
    }
}
