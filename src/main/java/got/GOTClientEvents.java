package got;

import got.client.npc.GOTNorthNpcLayers;
import got.client.npc.GOTNorthNpcModel;
import got.client.npc.GOTNorthNpcRenderer;
import got.client.npc.GOTWesterlandsNpcLayers;
import got.client.npc.GOTWesterlandsNpcModel;
import got.client.npc.GOTWesterlandsNpcRenderer;
import got.client.npc.GOTRiverlandsNpcLayers;
import got.client.npc.GOTRiverlandsNpcModel;
import got.client.npc.GOTRiverlandsNpcRenderer;
import got.client.npc.GOTArrynNpcLayers;
import got.client.npc.GOTArrynNpcModel;
import got.client.npc.GOTArrynNpcRenderer;
import got.client.npc.GOTCrownlandsNpcLayers;
import got.client.npc.GOTCrownlandsNpcModel;
import got.client.npc.GOTCrownlandsNpcRenderer;
import got.client.npc.GOTDragonstoneNpcLayers;
import got.client.npc.GOTDragonstoneNpcModel;
import got.client.npc.GOTDragonstoneNpcRenderer;
import got.client.npc.GOTReachNpcLayers;
import got.client.npc.GOTReachNpcModel;
import got.client.npc.GOTReachNpcRenderer;
import got.client.npc.GOTStormlandsNpcLayers;
import got.client.npc.GOTStormlandsNpcModel;
import got.client.npc.GOTStormlandsNpcRenderer;
import got.client.npc.GOTDorneNpcLayers;
import got.client.npc.GOTDorneNpcModel;
import got.client.npc.GOTDorneNpcRenderer;
import got.client.npc.GOTIronbornNpcLayers;
import got.client.npc.GOTIronbornNpcModel;
import got.client.npc.GOTIronbornNpcRenderer;
import got.client.npc.GOTWildlingNpcLayers;
import got.client.npc.GOTWildlingNpcModel;
import got.client.npc.GOTWildlingNpcRenderer;
import got.client.npc.GOTNightWatchNpcLayers;
import got.client.npc.GOTNightWatchNpcModel;
import got.client.npc.GOTNightWatchNpcRenderer;
import got.client.npc.GOTWhiteWalkerNpcLayers;
import got.client.npc.GOTWhiteWalkerNpcModel;
import got.client.npc.GOTWhiteWalkerNpcRenderer;
import got.client.npc.GOTBraavosNpcLayers;
import got.client.npc.GOTBraavosNpcModel;
import got.client.npc.GOTBraavosNpcRenderer;
import got.client.npc.GOTPentosNpcLayers;
import got.client.npc.GOTPentosNpcModel;
import got.client.npc.GOTPentosNpcRenderer;
import got.client.npc.GOTVolantisNpcLayers;
import got.client.npc.GOTVolantisNpcModel;
import got.client.npc.GOTVolantisNpcRenderer;
import got.client.npc.GOTLysNpcLayers;
import got.client.npc.GOTLysNpcModel;
import got.client.npc.GOTLysNpcRenderer;
import got.client.npc.GOTMyrNpcLayers;
import got.client.npc.GOTMyrNpcModel;
import got.client.npc.GOTMyrNpcRenderer;
import got.client.npc.GOTTyroshNpcLayers;
import got.client.npc.GOTTyroshNpcModel;
import got.client.npc.GOTTyroshNpcRenderer;
import got.client.npc.GOTGhiscarNpcLayers;
import got.client.npc.GOTGhiscarNpcModel;
import got.client.npc.GOTGhiscarNpcRenderer;
import got.client.npc.GOTDothrakiNpcLayers;
import got.client.npc.GOTDothrakiNpcModel;
import got.client.npc.GOTDothrakiNpcRenderer;
import got.client.npc.GOTYiTiNpcLayers;
import got.client.npc.GOTYiTiNpcModel;
import got.client.npc.GOTYiTiNpcRenderer;
import got.client.npc.GOTAsshaiNpcLayers;
import got.client.npc.GOTAsshaiNpcModel;
import got.client.npc.GOTAsshaiNpcRenderer;
import got.client.npc.GOTIbbenNpcLayers;
import got.client.npc.GOTIbbenNpcModel;
import got.client.npc.GOTIbbenNpcRenderer;
import got.client.npc.GOTJogosNhaiNpcLayers;
import got.client.npc.GOTJogosNhaiNpcModel;
import got.client.npc.GOTJogosNhaiNpcRenderer;
import got.client.npc.GOTQarthNpcLayers;
import got.client.npc.GOTQarthNpcModel;
import got.client.npc.GOTQarthNpcRenderer;
import got.client.npc.GOTLorathNpcLayers;
import got.client.npc.GOTLorathNpcModel;
import got.client.npc.GOTLorathNpcRenderer;
import got.client.npc.GOTQohorNpcLayers;
import got.client.npc.GOTQohorNpcModel;
import got.client.npc.GOTQohorNpcRenderer;
import got.client.npc.GOTLhazarNpcLayers;
import got.client.npc.GOTLhazarNpcModel;
import got.client.npc.GOTLhazarNpcRenderer;
import got.client.npc.GOTNorvosNpcLayers;
import got.client.npc.GOTNorvosNpcModel;
import got.client.npc.GOTNorvosNpcRenderer;
import got.client.npc.GOTMossovyNpcLayers;
import got.client.npc.GOTMossovyNpcModel;
import got.client.npc.GOTMossovyNpcRenderer;
import got.client.npc.GOTGoldenCompanyNpcLayers;
import got.client.npc.GOTGoldenCompanyNpcModel;
import got.client.npc.GOTGoldenCompanyNpcRenderer;
import got.client.npc.GOTSummerIslesNpcRenderer;
import got.client.npc.GOTSothoryosNpcRenderer;
import got.client.npc.GOTUlthosSpiderRenderer;
import got.client.npc.GOTBlizzardRenderer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;

@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class GOTClientEvents {
    private GOTClientEvents() {}

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(LegacyVesselLayers.MUG, LegacyVesselGeometry::mug);
        event.registerLayerDefinition(LegacyVesselLayers.GOBLET, LegacyVesselGeometry::goblet);
        event.registerLayerDefinition(LegacyVesselLayers.ALE_HORN, LegacyVesselGeometry::aleHorn);
        event.registerLayerDefinition(LegacyVesselLayers.WINE_GLASS, LegacyVesselGeometry::wineGlass);
        event.registerLayerDefinition(LegacyVesselLayers.SKULL_CUP, LegacyVesselGeometry::skullCup);
        event.registerLayerDefinition(LegacyDecorLayers.BEACON, LegacyDecorGeometry::beacon);
        event.registerLayerDefinition(LegacyDecorLayers.UNSMELTERY, LegacyDecorGeometry::unsmeltery);
        event.registerLayerDefinition(LegacyDecorLayers.BEAR_RUG, LegacyDecorGeometry::bearRug);
        event.registerLayerDefinition(LegacyDecorLayers.GIRAFFE_RUG, LegacyDecorGeometry::giraffeRug);
        event.registerLayerDefinition(LegacyDecorLayers.LION_RUG, LegacyDecorGeometry::lionRug);
        event.registerLayerDefinition(GOTBannerLayers.STANDING, GOTBannerGeometry::standing);
        event.registerLayerDefinition(GOTBannerLayers.WALL, GOTBannerGeometry::wall);
        event.registerLayerDefinition(GOTNorthNpcLayers.BASE, () -> GOTNorthNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTNorthNpcLayers.OUTFIT, () -> GOTNorthNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTNorthNpcLayers.ARMOR_INNER, () -> GOTNorthNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTNorthNpcLayers.ARMOR_OUTER, () -> GOTNorthNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTWesterlandsNpcLayers.BASE,
                () -> GOTWesterlandsNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTWesterlandsNpcLayers.OUTFIT,
                () -> GOTWesterlandsNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTWesterlandsNpcLayers.ARMOR_INNER,
                () -> GOTWesterlandsNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTWesterlandsNpcLayers.ARMOR_OUTER,
                () -> GOTWesterlandsNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTRiverlandsNpcLayers.BASE,
                () -> GOTRiverlandsNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTRiverlandsNpcLayers.OUTFIT,
                () -> GOTRiverlandsNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTRiverlandsNpcLayers.ARMOR_INNER,
                () -> GOTRiverlandsNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTRiverlandsNpcLayers.ARMOR_OUTER,
                () -> GOTRiverlandsNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTArrynNpcLayers.BASE,
                () -> GOTArrynNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTArrynNpcLayers.OUTFIT,
                () -> GOTArrynNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTArrynNpcLayers.ARMOR_INNER,
                () -> GOTArrynNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTArrynNpcLayers.ARMOR_OUTER,
                () -> GOTArrynNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTCrownlandsNpcLayers.BASE,
                () -> GOTCrownlandsNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTCrownlandsNpcLayers.OUTFIT,
                () -> GOTCrownlandsNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTCrownlandsNpcLayers.ARMOR_INNER,
                () -> GOTCrownlandsNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTCrownlandsNpcLayers.ARMOR_OUTER,
                () -> GOTCrownlandsNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTDragonstoneNpcLayers.BASE,
                () -> GOTDragonstoneNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTDragonstoneNpcLayers.OUTFIT,
                () -> GOTDragonstoneNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTDragonstoneNpcLayers.ARMOR_INNER,
                () -> GOTDragonstoneNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTDragonstoneNpcLayers.ARMOR_OUTER,
                () -> GOTDragonstoneNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTReachNpcLayers.BASE,
                () -> GOTReachNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTReachNpcLayers.OUTFIT,
                () -> GOTReachNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTReachNpcLayers.ARMOR_INNER,
                () -> GOTReachNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTReachNpcLayers.ARMOR_OUTER,
                () -> GOTReachNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTStormlandsNpcLayers.BASE,
                () -> GOTStormlandsNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTStormlandsNpcLayers.OUTFIT,
                () -> GOTStormlandsNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTStormlandsNpcLayers.ARMOR_INNER,
                () -> GOTStormlandsNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTStormlandsNpcLayers.ARMOR_OUTER,
                () -> GOTStormlandsNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTDorneNpcLayers.BASE,
                () -> GOTDorneNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTDorneNpcLayers.OUTFIT,
                () -> GOTDorneNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTDorneNpcLayers.ARMOR_INNER,
                () -> GOTDorneNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTDorneNpcLayers.ARMOR_OUTER,
                () -> GOTDorneNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTIronbornNpcLayers.BASE,
                () -> GOTIronbornNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTIronbornNpcLayers.OUTFIT,
                () -> GOTIronbornNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTIronbornNpcLayers.ARMOR_INNER,
                () -> GOTIronbornNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTIronbornNpcLayers.ARMOR_OUTER,
                () -> GOTIronbornNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTWildlingNpcLayers.BASE, () -> GOTWildlingNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTWildlingNpcLayers.OUTFIT, () -> GOTWildlingNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTWildlingNpcLayers.ARMOR_INNER, () -> GOTWildlingNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTWildlingNpcLayers.ARMOR_OUTER, () -> GOTWildlingNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTNightWatchNpcLayers.BASE, () -> GOTNightWatchNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTNightWatchNpcLayers.OUTFIT, () -> GOTNightWatchNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTNightWatchNpcLayers.ARMOR_INNER, () -> GOTNightWatchNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTNightWatchNpcLayers.ARMOR_OUTER, () -> GOTNightWatchNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTWhiteWalkerNpcLayers.BASE, () -> GOTWhiteWalkerNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTWhiteWalkerNpcLayers.OUTFIT, () -> GOTWhiteWalkerNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTWhiteWalkerNpcLayers.ARMOR_INNER, () -> GOTWhiteWalkerNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTWhiteWalkerNpcLayers.ARMOR_OUTER, () -> GOTWhiteWalkerNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTBraavosNpcLayers.BASE, () -> GOTBraavosNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTBraavosNpcLayers.OUTFIT, () -> GOTBraavosNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTBraavosNpcLayers.ARMOR_INNER, () -> GOTBraavosNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTBraavosNpcLayers.ARMOR_OUTER, () -> GOTBraavosNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTPentosNpcLayers.BASE, () -> GOTPentosNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTPentosNpcLayers.OUTFIT, () -> GOTPentosNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTPentosNpcLayers.ARMOR_INNER, () -> GOTPentosNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTPentosNpcLayers.ARMOR_OUTER, () -> GOTPentosNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTVolantisNpcLayers.BASE, () -> GOTVolantisNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTVolantisNpcLayers.OUTFIT, () -> GOTVolantisNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTVolantisNpcLayers.ARMOR_INNER, () -> GOTVolantisNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTVolantisNpcLayers.ARMOR_OUTER, () -> GOTVolantisNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTLysNpcLayers.BASE, () -> GOTLysNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTLysNpcLayers.OUTFIT, () -> GOTLysNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTLysNpcLayers.ARMOR_INNER, () -> GOTLysNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTLysNpcLayers.ARMOR_OUTER, () -> GOTLysNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTMyrNpcLayers.BASE, () -> GOTMyrNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTMyrNpcLayers.OUTFIT, () -> GOTMyrNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTMyrNpcLayers.ARMOR_INNER, () -> GOTMyrNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTMyrNpcLayers.ARMOR_OUTER, () -> GOTMyrNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTTyroshNpcLayers.BASE, () -> GOTTyroshNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTTyroshNpcLayers.OUTFIT, () -> GOTTyroshNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTTyroshNpcLayers.ARMOR_INNER, () -> GOTTyroshNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTTyroshNpcLayers.ARMOR_OUTER, () -> GOTTyroshNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTGhiscarNpcLayers.BASE, () -> GOTGhiscarNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTGhiscarNpcLayers.OUTFIT, () -> GOTGhiscarNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTGhiscarNpcLayers.ARMOR_INNER, () -> GOTGhiscarNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTGhiscarNpcLayers.ARMOR_OUTER, () -> GOTGhiscarNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTDothrakiNpcLayers.BASE, () -> GOTDothrakiNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTDothrakiNpcLayers.OUTFIT, () -> GOTDothrakiNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTDothrakiNpcLayers.ARMOR_INNER, () -> GOTDothrakiNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTDothrakiNpcLayers.ARMOR_OUTER, () -> GOTDothrakiNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTYiTiNpcLayers.BASE, () -> GOTYiTiNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTYiTiNpcLayers.OUTFIT, () -> GOTYiTiNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTYiTiNpcLayers.ARMOR_INNER, () -> GOTYiTiNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTYiTiNpcLayers.ARMOR_OUTER, () -> GOTYiTiNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTAsshaiNpcLayers.BASE, () -> GOTAsshaiNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTAsshaiNpcLayers.OUTFIT, () -> GOTAsshaiNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTAsshaiNpcLayers.ARMOR_INNER, () -> GOTAsshaiNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTAsshaiNpcLayers.ARMOR_OUTER, () -> GOTAsshaiNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTIbbenNpcLayers.BASE, () -> GOTIbbenNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTIbbenNpcLayers.OUTFIT, () -> GOTIbbenNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTIbbenNpcLayers.ARMOR_INNER, () -> GOTIbbenNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTIbbenNpcLayers.ARMOR_OUTER, () -> GOTIbbenNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTJogosNhaiNpcLayers.BASE, () -> GOTJogosNhaiNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTJogosNhaiNpcLayers.OUTFIT, () -> GOTJogosNhaiNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTJogosNhaiNpcLayers.ARMOR_INNER, () -> GOTJogosNhaiNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTJogosNhaiNpcLayers.ARMOR_OUTER, () -> GOTJogosNhaiNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTQarthNpcLayers.BASE, () -> GOTQarthNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTQarthNpcLayers.OUTFIT, () -> GOTQarthNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTQarthNpcLayers.ARMOR_INNER, () -> GOTQarthNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTQarthNpcLayers.ARMOR_OUTER, () -> GOTQarthNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTLorathNpcLayers.BASE, () -> GOTLorathNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTLorathNpcLayers.OUTFIT, () -> GOTLorathNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTLorathNpcLayers.ARMOR_INNER, () -> GOTLorathNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTLorathNpcLayers.ARMOR_OUTER, () -> GOTLorathNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTQohorNpcLayers.BASE, () -> GOTQohorNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTQohorNpcLayers.OUTFIT, () -> GOTQohorNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTQohorNpcLayers.ARMOR_INNER, () -> GOTQohorNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTQohorNpcLayers.ARMOR_OUTER, () -> GOTQohorNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTLhazarNpcLayers.BASE, () -> GOTLhazarNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTLhazarNpcLayers.OUTFIT, () -> GOTLhazarNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTLhazarNpcLayers.ARMOR_INNER, () -> GOTLhazarNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTLhazarNpcLayers.ARMOR_OUTER, () -> GOTLhazarNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTNorvosNpcLayers.BASE, () -> GOTNorvosNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTNorvosNpcLayers.OUTFIT, () -> GOTNorvosNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTNorvosNpcLayers.ARMOR_INNER, () -> GOTNorvosNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTNorvosNpcLayers.ARMOR_OUTER, () -> GOTNorvosNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTMossovyNpcLayers.BASE, () -> GOTMossovyNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTMossovyNpcLayers.OUTFIT, () -> GOTMossovyNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTMossovyNpcLayers.ARMOR_INNER, () -> GOTMossovyNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTMossovyNpcLayers.ARMOR_OUTER, () -> GOTMossovyNpcModel.armorLayer(1.0F));
        event.registerLayerDefinition(GOTGoldenCompanyNpcLayers.BASE, () -> GOTGoldenCompanyNpcModel.bodyLayer(0.0F));
        event.registerLayerDefinition(GOTGoldenCompanyNpcLayers.OUTFIT, () -> GOTGoldenCompanyNpcModel.bodyLayer(0.6F));
        event.registerLayerDefinition(GOTGoldenCompanyNpcLayers.ARMOR_INNER, () -> GOTGoldenCompanyNpcModel.armorLayer(0.5F));
        event.registerLayerDefinition(GOTGoldenCompanyNpcLayers.ARMOR_OUTER, () -> GOTGoldenCompanyNpcModel.armorLayer(1.0F));
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> tintIndex == 0
                        ? ((net.minecraft.world.item.DyeableLeatherItem) stack.getItem()).getColor(stack)
                        : 0xFFFFFF,
                GOTItems.LEATHER_HAT.get());
        event.register(got.npc.GOTNorthNpcSpawnerItem::tint, GOTItems.NORTH_NPC_SPAWNER.get());
        event.register(got.npc.GOTWesterlandsNpcSpawnerItem::tint,
                GOTItems.WESTERLANDS_NPC_SPAWNER.get());
        event.register(got.npc.GOTRiverlandsNpcSpawnerItem::tint,
                GOTItems.RIVERLANDS_NPC_SPAWNER.get());
        event.register(got.npc.GOTArrynNpcSpawnerItem::tint,
                GOTItems.ARRYN_NPC_SPAWNER.get());
        event.register(got.npc.GOTCrownlandsNpcSpawnerItem::tint,
                GOTItems.CROWNLANDS_NPC_SPAWNER.get());
        event.register(got.npc.GOTDragonstoneNpcSpawnerItem::tint,
                GOTItems.DRAGONSTONE_NPC_SPAWNER.get());
        event.register(got.npc.GOTReachNpcSpawnerItem::tint,
                GOTItems.REACH_NPC_SPAWNER.get());
        event.register(got.npc.GOTStormlandsNpcSpawnerItem::tint,
                GOTItems.STORMLANDS_NPC_SPAWNER.get());
        event.register(got.npc.GOTDorneNpcSpawnerItem::tint,
                GOTItems.DORNE_NPC_SPAWNER.get());
        event.register(got.npc.GOTIronbornNpcSpawnerItem::tint,
                GOTItems.IRONBORN_NPC_SPAWNER.get());
        event.register(got.npc.GOTWildlingNpcSpawnerItem::tint, GOTItems.WILDLING_NPC_SPAWNER.get());
        event.register(got.npc.GOTNightWatchNpcSpawnerItem::tint, GOTItems.NIGHT_WATCH_NPC_SPAWNER.get());
        event.register(got.npc.GOTWhiteWalkerNpcSpawnerItem::tint, GOTItems.WHITE_WALKER_NPC_SPAWNER.get());
        event.register(got.npc.GOTBraavosNpcSpawnerItem::tint, GOTItems.BRAAVOS_NPC_SPAWNER.get());
        event.register(got.npc.GOTPentosNpcSpawnerItem::tint, GOTItems.PENTOS_NPC_SPAWNER.get());
        event.register(got.npc.GOTVolantisNpcSpawnerItem::tint, GOTItems.VOLANTIS_NPC_SPAWNER.get());
        event.register(got.npc.GOTLysNpcSpawnerItem::tint, GOTItems.LYS_NPC_SPAWNER.get());
        event.register(got.npc.GOTMyrNpcSpawnerItem::tint, GOTItems.MYR_NPC_SPAWNER.get());
        event.register(got.npc.GOTTyroshNpcSpawnerItem::tint, GOTItems.TYROSH_NPC_SPAWNER.get());
        event.register(got.npc.GOTGhiscarNpcSpawnerItem::tint, GOTItems.GHISCAR_NPC_SPAWNER.get());
        event.register(got.npc.GOTDothrakiNpcSpawnerItem::tint, GOTItems.DOTHRAKI_NPC_SPAWNER.get());
        event.register(got.npc.GOTYiTiNpcSpawnerItem::tint, GOTItems.YI_TI_NPC_SPAWNER.get());
        event.register(got.npc.GOTAsshaiNpcSpawnerItem::tint, GOTItems.ASSHAI_NPC_SPAWNER.get());
        event.register(got.npc.GOTIbbenNpcSpawnerItem::tint, GOTItems.IBBEN_NPC_SPAWNER.get());
        event.register(got.npc.GOTJogosNhaiNpcSpawnerItem::tint, GOTItems.JOGOS_NHAI_NPC_SPAWNER.get());
        event.register(got.npc.GOTQarthNpcSpawnerItem::tint, GOTItems.QARTH_NPC_SPAWNER.get());
        event.register(got.npc.GOTLorathNpcSpawnerItem::tint, GOTItems.LORATH_NPC_SPAWNER.get());
        event.register(got.npc.GOTQohorNpcSpawnerItem::tint, GOTItems.QOHOR_NPC_SPAWNER.get());
        event.register(got.npc.GOTLhazarNpcSpawnerItem::tint, GOTItems.LHAZAR_NPC_SPAWNER.get());
        event.register(got.npc.GOTNorvosNpcSpawnerItem::tint, GOTItems.NORVOS_NPC_SPAWNER.get());
        event.register(got.npc.GOTMossovyNpcSpawnerItem::tint, GOTItems.MOSSOVY_NPC_SPAWNER.get());
        event.register(got.npc.GOTGoldenCompanyNpcSpawnerItem::tint,
                GOTItems.GOLDEN_COMPANY_NPC_SPAWNER.get());
        event.register(got.npc.GOTSummerIslesNpcSpawnerItem::tint,
                GOTItems.SUMMER_ISLES_NPC_SPAWNER.get());
        event.register(got.npc.GOTSothoryosNpcSpawnerItem::tint,
                GOTItems.SOTHORYOS_NPC_SPAWNER.get());
        event.register(got.npc.GOTUlthosCreatureSpawnerItem::tint,
                GOTItems.ULTHOS_CREATURE_SPAWNER.get());
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(GOTMenus.OVEN.get(), OvenScreen::new);
            MenuScreens.register(GOTMenus.FERMENTATION_BARREL.get(), FermentationBarrelScreen::new);
            MenuScreens.register(GOTMenus.GOT_SMITHING.get(), GOTSmithingScreen::new);
            MenuScreens.register(GOTMenus.HIRED_WARRIOR_INVENTORY.get(), got.client.gui.hiring.GOTGuiHiredWarriorInventory::new);
            MenuScreens.register(GOTMenus.HIRED_FARMER_INVENTORY.get(), got.client.gui.hiring.GOTGuiHiredFarmerInventory::new);
            BlockEntityRenderers.register(GOTBlockEntities.PLACED_DRINK_VESSEL.get(), PlacedDrinkVesselRenderer::new);
            BlockEntityRenderers.register(GOTBlockEntities.WEAPON_RACK.get(), GOTWeaponRackRenderer::new);
            BlockEntityRenderers.register(GOTBlockEntities.CARVED_SIGN.get(), GOTCarvedSignRenderer::new);
            BlockEntityRenderers.register(GOTBlockEntities.LEGACY_DECOR.get(), GOTLegacyDecorRenderer::new);
            EntityRenderers.register(GOTEntities.STANDING_BANNER.get(), GOTStandingBannerRenderer::new);
            EntityRenderers.register(GOTEntities.WALL_BANNER.get(), GOTWallBannerRenderer::new);
            EntityRenderers.register(GOTEntities.NORTH_NPC.get(), GOTNorthNpcRenderer::new);
            EntityRenderers.register(GOTEntities.WESTERLANDS_NPC.get(), GOTWesterlandsNpcRenderer::new);
            EntityRenderers.register(GOTEntities.RIVERLANDS_NPC.get(), GOTRiverlandsNpcRenderer::new);
            EntityRenderers.register(GOTEntities.ARRYN_NPC.get(), GOTArrynNpcRenderer::new);
            EntityRenderers.register(GOTEntities.CROWNLANDS_NPC.get(), GOTCrownlandsNpcRenderer::new);
            EntityRenderers.register(GOTEntities.DRAGONSTONE_NPC.get(), GOTDragonstoneNpcRenderer::new);
            EntityRenderers.register(GOTEntities.REACH_NPC.get(), GOTReachNpcRenderer::new);
            EntityRenderers.register(GOTEntities.STORMLANDS_NPC.get(), GOTStormlandsNpcRenderer::new);
            EntityRenderers.register(GOTEntities.DORNE_NPC.get(), GOTDorneNpcRenderer::new);
            EntityRenderers.register(GOTEntities.IRONBORN_NPC.get(), GOTIronbornNpcRenderer::new);
            EntityRenderers.register(GOTEntities.WILDLING_NPC.get(), GOTWildlingNpcRenderer::new);
            EntityRenderers.register(GOTEntities.NIGHT_WATCH_NPC.get(), GOTNightWatchNpcRenderer::new);
            EntityRenderers.register(GOTEntities.WHITE_WALKER_NPC.get(), GOTWhiteWalkerNpcRenderer::new);
            EntityRenderers.register(GOTEntities.BRAAVOS_NPC.get(), GOTBraavosNpcRenderer::new);
            EntityRenderers.register(GOTEntities.PENTOS_NPC.get(), GOTPentosNpcRenderer::new);
            EntityRenderers.register(GOTEntities.VOLANTIS_NPC.get(), GOTVolantisNpcRenderer::new);
            EntityRenderers.register(GOTEntities.LYS_NPC.get(), GOTLysNpcRenderer::new);
            EntityRenderers.register(GOTEntities.MYR_NPC.get(), GOTMyrNpcRenderer::new);
            EntityRenderers.register(GOTEntities.TYROSH_NPC.get(), GOTTyroshNpcRenderer::new);
            EntityRenderers.register(GOTEntities.GHISCAR_NPC.get(), GOTGhiscarNpcRenderer::new);
            EntityRenderers.register(GOTEntities.DOTHRAKI_NPC.get(), GOTDothrakiNpcRenderer::new);
            EntityRenderers.register(GOTEntities.YI_TI_NPC.get(), GOTYiTiNpcRenderer::new);
            EntityRenderers.register(GOTEntities.ASSHAI_NPC.get(), GOTAsshaiNpcRenderer::new);
            EntityRenderers.register(GOTEntities.IBBEN_NPC.get(), GOTIbbenNpcRenderer::new);
            EntityRenderers.register(GOTEntities.JOGOS_NHAI_NPC.get(), GOTJogosNhaiNpcRenderer::new);
            EntityRenderers.register(GOTEntities.QARTH_NPC.get(), GOTQarthNpcRenderer::new);
            EntityRenderers.register(GOTEntities.LORATH_NPC.get(), GOTLorathNpcRenderer::new);
            EntityRenderers.register(GOTEntities.QOHOR_NPC.get(), GOTQohorNpcRenderer::new);
            EntityRenderers.register(GOTEntities.LHAZAR_NPC.get(), GOTLhazarNpcRenderer::new);
            EntityRenderers.register(GOTEntities.NORVOS_NPC.get(), GOTNorvosNpcRenderer::new);
            EntityRenderers.register(GOTEntities.MOSSOVY_NPC.get(), GOTMossovyNpcRenderer::new);
            EntityRenderers.register(GOTEntities.GOLDEN_COMPANY_NPC.get(), GOTGoldenCompanyNpcRenderer::new);
            EntityRenderers.register(GOTEntities.SUMMER_ISLES_NPC.get(), GOTSummerIslesNpcRenderer::new);
            EntityRenderers.register(GOTEntities.SOTHORYOS_NPC.get(), GOTSothoryosNpcRenderer::new);
            EntityRenderers.register(GOTEntities.ULTHOS_SPIDER.get(), GOTUlthosSpiderRenderer::new);
            EntityRenderers.register(GOTEntities.BLIZZARD.get(), GOTBlizzardRenderer::new);
            EntityRenderers.register(GOTEntities.THROWN_AXE.get(), ThrownItemRenderer::new);
            GOTDecorativeFunctionalBlocks.CUTOUT.forEach(block ->
                    ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutout()));
            GOTDecorativeFunctionalBlocks.TRANSLUCENT.forEach(block ->
                    ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.translucent()));
        });
    }
}
