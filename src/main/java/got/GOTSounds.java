package got;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class GOTSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, GOTMod.MOD_ID);

    public static final RegistryObject<SoundEvent> GATE_OPEN = sound("block.gate.open");
    public static final RegistryObject<SoundEvent> GATE_CLOSE = sound("block.gate.close");
    public static final RegistryObject<SoundEvent> STONE_GATE_OPEN = sound("block.gate.stone_open");
    public static final RegistryObject<SoundEvent> STONE_GATE_CLOSE = sound("block.gate.stone_close");
    public static final RegistryObject<SoundEvent> TREASURE_BREAK = sound("block.treasure.break");
    public static final RegistryObject<SoundEvent> TREASURE_STEP = sound("block.treasure.step");
    public static final RegistryObject<SoundEvent> TREASURE_PLACE = sound("block.treasure.place");
    public static final RegistryObject<SoundEvent> COMMAND_HORN = sound("item.horn");

    private static RegistryObject<SoundEvent> sound(String id) {
        return SOUNDS.register(id, () -> SoundEvent.createVariableRangeEvent(
                ResourceLocation.fromNamespaceAndPath(GOTMod.MOD_ID, id)));
    }

    public static void register(IEventBus bus) {
        SOUNDS.register(bus);
    }

    private GOTSounds() {}
}
