package got;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import got.world.structure.nightwatch.NightWatchStructureType;
import got.world.structure.north.NorthStructureType;
import got.world.structure.wildling.WildlingStructureType;

public final class GOTCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GOTMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> BLOCKS = TABS.register("blocks", () -> CreativeModeTab.builder()
            .title(Component.translatable("creativetab.got.blocks"))
            .icon(() -> GOTBlocks.BRONZE_BLOCK.get().asItem().getDefaultInstance())
            .displayItems((parameters, output) -> {
                GOTBlocks.MIGRATED_BLOCKS.forEach(block -> acceptBlock(output, block));
                GOTBlocks.BUILDING_BLOCKS.stream()
                        .filter(block -> !isMiscBlock(block))
                        .forEach(block -> acceptBlock(output, block));
            })
            .build());

    public static final RegistryObject<CreativeModeTab> UTILITIES = TABS.register("utilities", () -> CreativeModeTab.builder()
            .title(Component.translatable("creativetab.got.utilities"))
            .icon(() -> GOTBlocks.COMMAND_TABLE.get().asItem().getDefaultInstance())
            .displayItems((parameters, output) ->
                    GOTBlocks.UTILITY_BLOCKS.forEach(block -> acceptBlock(output, block)))
            .build());

    public static final RegistryObject<CreativeModeTab> DECORATIONS = TABS.register("decorations", () -> CreativeModeTab.builder()
            .title(Component.translatable("creativetab.got.decorations"))
            .icon(() -> firstValidBlockIcon(GOTBlocks.DECORATION_BLOCKS))
            .displayItems((parameters, output) ->
                    GOTBlocks.DECORATION_BLOCKS.forEach(block -> acceptBlock(output, block)))
            .build());

    public static final RegistryObject<CreativeModeTab> FOOD_AND_DRINK = TABS.register("food_and_drink", () -> CreativeModeTab.builder()
            .title(Component.translatable("creativetab.got.food_and_drink"))
            .icon(() -> GOTItems.CORN.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                GOTItems.FOOD_ITEMS.forEach(item -> acceptFoodAndDrinkItem(output, item));
                GOTItems.NATURE_ITEMS.forEach(item -> acceptItem(output, item));
                acceptItem(output, GOTItems.FLOUR);
                acceptItem(output, GOTItems.DOUGH);
                GOTBlocks.NATURE_BLOCKS.forEach(block -> acceptBlock(output, block));
            })
            .build());

    public static final RegistryObject<CreativeModeTab> MATERIALS = TABS.register("materials", () -> CreativeModeTab.builder()
            .title(Component.translatable("creativetab.got.materials"))
            .icon(() -> GOTItems.VALYRIAN_STEEL_INGOT.get().getDefaultInstance())
            .displayItems((parameters, output) -> GOTItems.MATERIAL_ITEMS.stream()
                    .filter(item -> !isCoin(item))
                    .filter(item -> item != GOTItems.FLOUR && item != GOTItems.DOUGH)
                    .forEach(item -> acceptItem(output, item)))
            .build());

    public static final RegistryObject<CreativeModeTab> MISC = TABS.register("misc", () -> CreativeModeTab.builder()
            .title(Component.translatable("creativetab.got.misc"))
            .icon(() -> GOTItems.COIN_1.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                GOTItems.MATERIAL_ITEMS.stream()
                        .filter(GOTCreativeTabs::isCoin)
                        .forEach(item -> acceptItem(output, item));
                GOTItems.MISC_ITEMS.forEach(item -> acceptItem(output, item));
                GOTBlocks.BUILDING_BLOCKS.stream()
                        .filter(GOTCreativeTabs::isMiscBlock)
                        .forEach(block -> acceptBlock(output, block));
            })
            .build());

    public static final RegistryObject<CreativeModeTab> TOOLS = TABS.register("tools", () -> CreativeModeTab.builder()
            .title(Component.translatable("creativetab.got.tools"))
            .icon(() -> GOTEquipment.VALYRIAN_MATTOCK.get().getDefaultInstance())
            .displayItems((parameters, output) -> GOTEquipment.ALL.stream()
                    .filter(GOTCreativeTabs::isTool)
                    .filter(item -> !GOTEquipment.LORE_ITEMS.contains(item))
                    .forEach(item -> acceptItem(output, item)))
            .build());

    public static final RegistryObject<CreativeModeTab> COMBAT = TABS.register("combat", () -> CreativeModeTab.builder()
            .title(Component.translatable("creativetab.got.combat"))
            .icon(() -> GOTEquipment.VALYRIAN_SWORD.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                GOTItems.BASIC_EQUIPMENT.forEach(item -> acceptItem(output, item));
                GOTEquipment.ALL.stream()
                        .filter(item -> !GOTEquipment.LORE_ITEMS.contains(item))
                        .filter(item -> !isTool(item))
                        .forEach(item -> acceptItem(output, item));
            })
            .build());

    public static final RegistryObject<CreativeModeTab> SMITHING = TABS.register("smithing", () -> CreativeModeTab.builder()
            .title(Component.translatable("creativetab.got.smithing"))
            .icon(() -> GOTSmithScrollItem.create(GOTSmithingModifier.STRONG_1))
            .displayItems((parameters, output) -> {
                for (GOTSmithingModifier modifier : GOTSmithingModifier.values()) {
                    output.accept(GOTSmithScrollItem.create(modifier));
                }
            })
            .build());

    public static final RegistryObject<CreativeModeTab> ARTIFACTS = TABS.register("artifacts", () -> CreativeModeTab.builder()
            .title(Component.translatable("creativetab.got.artifacts"))
            .icon(() -> GOTEquipment.LONGCLAW.get().getDefaultInstance())
            .displayItems((parameters, output) ->
                    GOTEquipment.LORE_ITEMS.forEach(item -> acceptItem(output, item)))
            .build());

    public static final RegistryObject<CreativeModeTab> SPAWNING = TABS.register("spawning", () -> CreativeModeTab.builder()
            .title(Component.translatable("creativetab.got.spawning"))
            .icon(() -> GOTStructureSpawnerItem.createStack(NorthStructureType.VILLAGE))
            .displayItems((parameters, output) -> {
                java.util.Arrays.stream(WildlingStructureType.values())
                        .forEach(type -> output.accept(GOTStructureSpawnerItem.createStack(type)));
                java.util.Arrays.stream(NightWatchStructureType.values())
                        .forEach(type -> output.accept(GOTStructureSpawnerItem.createStack(type)));
                java.util.Arrays.stream(NorthStructureType.values())
                        .forEach(type -> output.accept(GOTStructureSpawnerItem.createStack(type)));
                got.npc.NorthNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTNorthNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.WesterlandsNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTWesterlandsNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.RiverlandsNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTRiverlandsNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.ArrynNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTArrynNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.CrownlandsNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTCrownlandsNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.DragonstoneNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTDragonstoneNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.ReachNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTReachNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.StormlandsNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTStormlandsNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.DorneNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTDorneNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.IronbornNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTIronbornNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.WildlingNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTWildlingNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.NightWatchNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTNightWatchNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.WhiteWalkerNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTWhiteWalkerNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.BraavosNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTBraavosNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.PentosNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTPentosNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.VolantisNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTVolantisNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.LysNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTLysNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.MyrNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTMyrNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.TyroshNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTTyroshNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.GhiscarNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTGhiscarNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.DothrakiNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTDothrakiNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.YiTiNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTYiTiNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.AsshaiNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTAsshaiNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.IbbenNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTIbbenNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.JogosNhaiNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTJogosNhaiNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.QarthNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTQarthNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.LorathNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTLorathNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.QohorNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTQohorNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.LhazarNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTLhazarNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.NorvosNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTNorvosNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.MossovyNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTMossovyNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.GoldenCompanyNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTGoldenCompanyNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.SummerIslesNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTSummerIslesNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                got.npc.SothoryosNpcRole.spawnerOrder().stream()
                        .map(got.npc.GOTSothoryosNpcSpawnerItem::createStack)
                        .forEach(output::accept);
                java.util.Arrays.stream(got.npc.GOTUlthosCreatureSpawnerItem.UlthosCreature.values())
                        .map(got.npc.GOTUlthosCreatureSpawnerItem::createStack)
                        .forEach(output::accept);
            })
            .build());

    public static final RegistryObject<CreativeModeTab> BANNERS = TABS.register("banners", () -> CreativeModeTab.builder()
            .title(Component.translatable("creativetab.got.banners"))
            .icon(() -> GOTItems.ICON_HERALDRY.get().getDefaultInstance())
            .displayItems((parameters, output) ->
                    GOTBannerType.values().forEach(type -> output.accept(GOTBannerItem.createStack(type))))
            .build());

    private static boolean isCoin(RegistryObject<? extends Item> item) {
        return item.getId().getPath().startsWith("coin_");
    }

    private static boolean isTool(RegistryObject<? extends Item> item) {
        String id = item.getId().getPath();
        return id.endsWith("_mattock")
                || id.equals("alloy_steel_axe")
                || id.equals("bronze_axe")
                || id.equals("copper_axe")
                || id.equals("obsidian_axe")
                || id.equals("valyrian_axe")
                || id.endsWith("_pickaxe")
                || id.endsWith("_shovel")
                || id.endsWith("_hoe")
                || id.endsWith("_chisel")
                || id.equals("chisel")
                || id.equals("branding_iron");
    }

    private static boolean isMiscBlock(RegistryObject<? extends Block> block) {
        String id = block.getId().getPath();
        return id.contains("button")
                || id.contains("pressure_plate")
                || id.contains("pipe");
    }

    private static net.minecraft.world.item.ItemStack firstValidBlockIcon(
            Iterable<? extends RegistryObject<? extends Block>> blocks) {
        for (RegistryObject<? extends Block> block : blocks) {
            Item item = block.get().asItem();
            if (item != Items.AIR) {
                return item.getDefaultInstance();
            }
        }
        return Items.FLOWER_POT.getDefaultInstance();
    }

    private static void acceptItem(CreativeModeTab.Output output, RegistryObject<? extends Item> item) {
        Item value = item.get();
        if (value != Items.AIR) {
            output.accept(value);
        }
    }

    /**
     * Filled drinks use one registry item per liquid and store their vessel in
     * stack NBT. Add every supported vessel stack explicitly so the dedicated
     * Food and Drink tab exposes the complete set instead of only the default
     * mug. Non-drink food items retain their ordinary single entry.
     */
    private static void acceptFoodAndDrinkItem(
            CreativeModeTab.Output output,
            RegistryObject<? extends Item> item) {
        Item value = item.get();
        if (value == Items.AIR) {
            return;
        }

        if (value instanceof GOTDrinkItem drink) {
            for (GOTDrinkVessel vessel : GOTDrinkVessel.values()) {
                output.accept(drink.createFilled(vessel, 1.0F));
            }
            return;
        }

        output.accept(value);
    }

    private static void acceptBlock(CreativeModeTab.Output output, RegistryObject<? extends Block> block) {
        Item item = block.get().asItem();
        if (item != Items.AIR) {
            output.accept(item);
        }
    }

    private GOTCreativeTabs() {}

    public static void register(IEventBus modBus) {
        TABS.register(modBus);
    }
}
