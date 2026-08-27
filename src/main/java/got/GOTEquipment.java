package got;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collections;

public final class GOTEquipment {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, GOTMod.MOD_ID);

    // Shared regional faction-table weapons. Armor remains faction-specific.
    public static final RegistryObject<Item> WESTEROS_SWORD = ITEMS.register("westeros_sword", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> WESTEROS_SPEAR = ITEMS.register("westeros_spear", () -> new GOTRegionalSpearItem(Tiers.STONE, new Item.Properties()));
    public static final RegistryObject<Item> WESTEROS_HAMMER = ITEMS.register("westeros_hammer", () -> new SwordItem(Tiers.IRON, 7, -3.0F, new Item.Properties()));

    public static final RegistryObject<Item> ESSOS_SWORD = ITEMS.register("essos_sword", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> ESSOS_SPEAR = ITEMS.register("essos_spear", () -> new GOTRegionalSpearItem(Tiers.STONE, new Item.Properties()));
    public static final RegistryObject<Item> ESSOS_HAMMER = ITEMS.register("essos_hammer", () -> new SwordItem(Tiers.IRON, 7, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> ESSOS_POLEARM = ITEMS.register("essos_polearm", () -> new GOTRegionalSpearItem(Tiers.STONE, new Item.Properties()));

    // Real item-based GOT shields. These replace the legacy selector/menu system.
    public static final RegistryObject<Item> NORTH_SHIELD = ITEMS.register("north_shield", () -> new GOTFactionShieldItem("north"));
    public static final RegistryObject<Item> NORTHGUARD_SHIELD = ITEMS.register("northguard_shield", () -> new GOTFactionShieldItem("northguard"));
    public static final RegistryObject<Item> RIVERLANDS_SHIELD = ITEMS.register("riverlands_shield", () -> new GOTFactionShieldItem("riverlands"));
    public static final RegistryObject<Item> ARRYN_SHIELD = ITEMS.register("arryn_shield", () -> new GOTFactionShieldItem("arryn"));
    public static final RegistryObject<Item> ARRYNGUARD_SHIELD = ITEMS.register("arrynguard_shield", () -> new GOTFactionShieldItem("arrynguard"));
    public static final RegistryObject<Item> HILLMEN_SHIELD = ITEMS.register("hillmen_shield", () -> new GOTFactionShieldItem("hillmen"));
    public static final RegistryObject<Item> IRONBORN_SHIELD = ITEMS.register("ironborn_shield", () -> new GOTFactionShieldItem("ironborn"));
    public static final RegistryObject<Item> WESTERLANDS_SHIELD = ITEMS.register("westerlands_shield", () -> new GOTFactionShieldItem("westerlands"));
    public static final RegistryObject<Item> WESTERLANDSGUARD_SHIELD = ITEMS.register("westerlandsguard_shield", () -> new GOTFactionShieldItem("westerlandsguard"));
    public static final RegistryObject<Item> DRAGONSTONE_SHIELD = ITEMS.register("dragonstone_shield", () -> new GOTFactionShieldItem("dragonstone"));
    public static final RegistryObject<Item> CROWNLANDS_SHIELD = ITEMS.register("crownlands_shield", () -> new GOTFactionShieldItem("crownlands"));
    public static final RegistryObject<Item> STORMLANDS_SHIELD = ITEMS.register("stormlands_shield", () -> new GOTFactionShieldItem("stormlands"));
    public static final RegistryObject<Item> REACH_SHIELD = ITEMS.register("reach_shield", () -> new GOTFactionShieldItem("reach"));
    public static final RegistryObject<Item> REACHGUARD_SHIELD = ITEMS.register("reachguard_shield", () -> new GOTFactionShieldItem("reachguard"));
    public static final RegistryObject<Item> DORNE_SHIELD = ITEMS.register("dorne_shield", () -> new GOTFactionShieldItem("dorne"));
    public static final RegistryObject<Item> VOLANTIS_SHIELD = ITEMS.register("volantis_shield", () -> new GOTFactionShieldItem("volantis"));
    public static final RegistryObject<Item> PENTOS_SHIELD = ITEMS.register("pentos_shield", () -> new GOTFactionShieldItem("pentos"));
    public static final RegistryObject<Item> NORVOS_SHIELD = ITEMS.register("norvos_shield", () -> new GOTFactionShieldItem("norvos"));
    public static final RegistryObject<Item> BRAAVOS_SHIELD = ITEMS.register("braavos_shield", () -> new GOTFactionShieldItem("braavos"));
    public static final RegistryObject<Item> TYROSH_SHIELD = ITEMS.register("tyrosh_shield", () -> new GOTFactionShieldItem("tyrosh"));
    public static final RegistryObject<Item> LORATH_SHIELD = ITEMS.register("lorath_shield", () -> new GOTFactionShieldItem("lorath"));
    public static final RegistryObject<Item> QOHOR_SHIELD = ITEMS.register("qohor_shield", () -> new GOTFactionShieldItem("qohor"));
    public static final RegistryObject<Item> LYS_SHIELD = ITEMS.register("lys_shield", () -> new GOTFactionShieldItem("lys"));
    public static final RegistryObject<Item> MYR_SHIELD = ITEMS.register("myr_shield", () -> new GOTFactionShieldItem("myr"));
    public static final RegistryObject<Item> QARTH_SHIELD = ITEMS.register("qarth_shield", () -> new GOTFactionShieldItem("qarth"));
    public static final RegistryObject<Item> GHISCAR_SHIELD = ITEMS.register("ghiscar_shield", () -> new GOTFactionShieldItem("ghiscar"));
    public static final RegistryObject<Item> UNSULLIED_SHIELD = ITEMS.register("unsullied_shield", () -> new GOTFactionShieldItem("unsullied"));
    public static final RegistryObject<Item> YI_TI_SHIELD = ITEMS.register("yi_ti_shield", () -> new GOTFactionShieldItem("yi_ti"));
    public static final RegistryObject<Item> YI_TI_BOMBARDIER_SHIELD = ITEMS.register("yi_ti_bombardier_shield", () -> new GOTFactionShieldItem("yi_ti_bombardier"));
    public static final RegistryObject<Item> YI_TI_SAMURAI_SHIELD = ITEMS.register("yi_ti_samurai_shield", () -> new GOTFactionShieldItem("yi_ti_samurai"));
    public static final RegistryObject<Item> ASSHAI_SHIELD = ITEMS.register("asshai_shield", () -> new GOTFactionShieldItem("asshai"));
    public static final RegistryObject<Item> SUMMER_SHIELD = ITEMS.register("summer_shield", () -> new GOTFactionShieldItem("summer"));
    public static final RegistryObject<Item> SOTHORYOS_SHIELD = ITEMS.register("sothoryos_shield", () -> new GOTFactionShieldItem("sothoryos"));
    public static final RegistryObject<Item> GOLDEN_COMPANY_SHIELD = ITEMS.register("golden_company_shield", () -> new GOTFactionShieldItem("golden_company"));
    public static final RegistryObject<Item> TARGARYEN_SHIELD = ITEMS.register("targaryen_shield", () -> new GOTFactionShieldItem("targaryen"));
    public static final RegistryObject<Item> ALCOHOLIC_SHIELD = ITEMS.register("alcoholic_shield", () -> new GOTFactionShieldItem("alcoholic"));
    public static final RegistryObject<Item> ACHIEVEMENT_BRONZE_SHIELD = ITEMS.register("achievement_bronze_shield", () -> new GOTFactionShieldItem("achievement_bronze"));
    public static final RegistryObject<Item> ACHIEVEMENT_SILVER_SHIELD = ITEMS.register("achievement_silver_shield", () -> new GOTFactionShieldItem("achievement_silver"));
    public static final RegistryObject<Item> ACHIEVEMENT_GOLD_SHIELD = ITEMS.register("achievement_gold_shield", () -> new GOTFactionShieldItem("achievement_gold"));
    public static final RegistryObject<Item> ACHIEVEMENT_VALYRIAN_SHIELD = ITEMS.register("achievement_valyrian_shield", () -> new GOTFactionShieldItem("achievement_valyrian"));

    // Milestone 4.5.1: missing legacy tools
    public static final RegistryObject<Item> ALLOY_STEEL_AXE = ITEMS.register("alloy_steel_axe", () -> new AxeItem(Tiers.DIAMOND, 5.0F, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> ALLOY_STEEL_HOE = ITEMS.register("alloy_steel_hoe", () -> new HoeItem(Tiers.DIAMOND, -3, 0.0F, new Item.Properties()));
    public static final RegistryObject<Item> ALLOY_STEEL_PICKAXE = ITEMS.register("alloy_steel_pickaxe", () -> new PickaxeItem(Tiers.DIAMOND, 1, -2.8F, new Item.Properties()));
    public static final RegistryObject<Item> ALLOY_STEEL_SHOVEL = ITEMS.register("alloy_steel_shovel", () -> new ShovelItem(Tiers.DIAMOND, 1.5F, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> BRONZE_AXE = ITEMS.register("bronze_axe", () -> new AxeItem(Tiers.IRON, 5.0F, -3.1F, new Item.Properties()));
    public static final RegistryObject<Item> BRONZE_HOE = ITEMS.register("bronze_hoe", () -> new HoeItem(Tiers.IRON, -2, -1.0F, new Item.Properties()));
    public static final RegistryObject<Item> BRONZE_PICKAXE = ITEMS.register("bronze_pickaxe", () -> new PickaxeItem(Tiers.IRON, 1, -2.8F, new Item.Properties()));
    public static final RegistryObject<Item> BRONZE_SHOVEL = ITEMS.register("bronze_shovel", () -> new ShovelItem(Tiers.IRON, 1.5F, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> COPPER_AXE = ITEMS.register("copper_axe", () -> new AxeItem(Tiers.STONE, 5.0F, -3.1F, new Item.Properties()));
    public static final RegistryObject<Item> COPPER_HOE = ITEMS.register("copper_hoe", () -> new HoeItem(Tiers.STONE, -1, -2.0F, new Item.Properties()));
    public static final RegistryObject<Item> COPPER_PICKAXE = ITEMS.register("copper_pickaxe", () -> new PickaxeItem(Tiers.STONE, 1, -2.8F, new Item.Properties()));
    public static final RegistryObject<Item> COPPER_SHOVEL = ITEMS.register("copper_shovel", () -> new ShovelItem(Tiers.STONE, 1.5F, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_AXE = ITEMS.register("obsidian_axe", () -> new AxeItem(Tiers.DIAMOND, 5.0F, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_HOE = ITEMS.register("obsidian_hoe", () -> new HoeItem(Tiers.DIAMOND, -3, 0.0F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_PICKAXE = ITEMS.register("obsidian_pickaxe", () -> new PickaxeItem(Tiers.DIAMOND, 1, -2.8F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_SHOVEL = ITEMS.register("obsidian_shovel", () -> new ShovelItem(Tiers.DIAMOND, 1.5F, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> VALYRIAN_AXE = ITEMS.register("valyrian_axe", () -> new AxeItem(Tiers.NETHERITE, 5.0F, -3.0F, new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> VALYRIAN_HOE = ITEMS.register("valyrian_hoe", () -> new HoeItem(Tiers.NETHERITE, -4, 0.0F, new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> VALYRIAN_PICKAXE = ITEMS.register("valyrian_pickaxe", () -> new PickaxeItem(Tiers.NETHERITE, 1, -2.8F, new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> VALYRIAN_SHOVEL = ITEMS.register("valyrian_shovel", () -> new ShovelItem(Tiers.NETHERITE, 1.5F, -3.0F, new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> BRANDING_IRON = ITEMS.register("branding_iron", () -> new GOTBrandingIronItem(new Item.Properties()));
    public static final RegistryObject<Item> CHISEL = ITEMS.register("chisel", () -> new Item(new Item.Properties().stacksTo(1).durability(256)));
    public static final RegistryObject<Item> VALYRIAN_CHISEL = ITEMS.register("valyrian_chisel", () -> new Item(new Item.Properties().stacksTo(1).durability(2031).fireResistant()));


    // Milestone 4.5.2: missing legacy combat items.
    // Entity-dependent behavior (commanding NPCs, projectile entities, mount-specific armor)
    // is intentionally deferred; these registrations preserve the original items and presentation.
    public static final RegistryObject<Item> ALLOY_STEEL_HALBERD = ITEMS.register("alloy_steel_halberd", () -> new SwordItem(Tiers.DIAMOND, 6, -3.1F, new Item.Properties()));
    public static final RegistryObject<Item> ARMOR_STAND = ITEMS.register("armor_stand", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> COMMAND_HORN = ITEMS.register("command_horn", () -> new GOTCommandHornItem(new Item.Properties()));
    public static final RegistryObject<Item> DART = ITEMS.register("dart", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> DART_POISONED = ITEMS.register("dart_poisoned", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> HORSE_ARMOR_DIAMOND = ITEMS.register("horse_armor_diamond", () -> new HorseArmorItem(11, "diamond", new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HORSE_ARMOR_GOLD = ITEMS.register("horse_armor_gold", () -> new HorseArmorItem(7, "gold", new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HORSE_ARMOR_IRON = ITEMS.register("horse_armor_iron", () -> new HorseArmorItem(5, "iron", new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ICON_SWORD = ITEMS.register("icon_sword", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> RHINO_ARMOR = ITEMS.register("rhino_armor", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ROBES_HELMET = ITEMS.register("robes_helmet", () -> new ArmorItem(GOTArmorMaterials.of("robes"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> SLING = ITEMS.register("sling", () -> new GOTSlingItem(new Item.Properties()));
    public static final RegistryObject<Item> VALYRIAN_HORSE_ARMOR = ITEMS.register("valyrian_horse_armor", () -> new HorseArmorItem(15, "valyrian", new Item.Properties().stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> WARHORN = ITEMS.register("warhorn", () -> new got.invasion.GOTWarhornItem(new Item.Properties()));

    public static final RegistryObject<Item> ALLOY_STEEL_BATTLEAXE = ITEMS.register("alloy_steel_battleaxe", () -> new SwordItem(Tiers.DIAMOND, 6, -3.1F, new Item.Properties()));
    public static final RegistryObject<Item> ALLOY_STEEL_CROSSBOW = ITEMS.register("alloy_steel_crossbow", () -> new GOTLegacyCrossbowItem(new Item.Properties().durability(465)));
    public static final RegistryObject<Item> ALLOY_STEEL_DAGGER_POISONED = ITEMS.register("alloy_steel_dagger_poisoned", () -> new GOTPoisonedDaggerItem(Tiers.DIAMOND, new Item.Properties()));
    public static final RegistryObject<Item> ALLOY_STEEL_HAMMER = ITEMS.register("alloy_steel_hammer", () -> new SwordItem(Tiers.DIAMOND, 7, -3.2F, new Item.Properties()));
    public static final RegistryObject<Item> ALLOY_STEEL_MATTOCK = ITEMS.register("alloy_steel_mattock", () -> new SwordItem(Tiers.DIAMOND, 7, -3.2F, new Item.Properties()));
    public static final RegistryObject<Item> ALLOY_STEEL_PIKE = ITEMS.register("alloy_steel_pike", () -> new GOTLegacyPikeItem(Tiers.DIAMOND, new Item.Properties()));
    public static final RegistryObject<Item> ALLOY_STEEL_POLEARM = ITEMS.register("alloy_steel_polearm", () -> new GOTRegionalSpearItem(Tiers.DIAMOND, new Item.Properties()));
    public static final RegistryObject<Item> ALLOY_STEEL_SCIMITAR = ITEMS.register("alloy_steel_scimitar", () -> new SwordItem(Tiers.DIAMOND, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> ALLOY_STEEL_SPEAR = ITEMS.register("alloy_steel_spear", () -> new GOTLegacySpearItem(Tiers.DIAMOND, new Item.Properties()));
    public static final RegistryObject<Item> ALLOY_STEEL_SWORD = ITEMS.register("alloy_steel_sword", () -> new SwordItem(Tiers.DIAMOND, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> ALLOY_STEEL_THROWING_AXE = ITEMS.register("alloy_steel_throwing_axe", () -> new GOTThrowingAxeItem(Tiers.DIAMOND, new Item.Properties()));
    public static final RegistryObject<Item> ARDRIAN_CELTIGAR_AXE = ITEMS.register("ardrian_celtigar_axe", () -> new SwordItem(Tiers.IRON, 6, -3.1F, new Item.Properties()));
    public static final RegistryObject<Item> ARROW_FIRE = ITEMS.register("arrow_fire", () -> new GOTLegacyArrowItem(GOTLegacyArrowEntity.Kind.FIRE_ARROW, new Item.Properties()));
    public static final RegistryObject<Item> ARROW_POISONED = ITEMS.register("arrow_poisoned", () -> new GOTLegacyArrowItem(GOTLegacyArrowEntity.Kind.POISON_ARROW, new Item.Properties()));
    public static final RegistryObject<Item> ASSHAI_ARCHMAG_STAFF = ITEMS.register("asshai_archmag_staff", () -> new SwordItem(Tiers.IRON, 4, -2.8F, new Item.Properties()));
    public static final RegistryObject<Item> ASSHAI_SHADOWBINDER_STAFF = ITEMS.register("asshai_shadowbinder_staff", () -> new SwordItem(Tiers.IRON, 4, -2.8F, new Item.Properties()));
    public static final RegistryObject<Item> BANE = ITEMS.register("bane", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> BERIC_DONDARRION_SWORD = ITEMS.register("beric_dondarrion_sword", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> BLACK_ARAKH = ITEMS.register("black_arakh", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> BLACKFYRE = ITEMS.register("blackfyre", () -> new SwordItem(Tiers.NETHERITE, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> BLACKSMITH_HAMMER = ITEMS.register("blacksmith_hammer", () -> new SwordItem(Tiers.IRON, 7, -3.2F, new Item.Properties()));
    public static final RegistryObject<Item> BRIGHTROAR = ITEMS.register("brightroar", () -> new SwordItem(Tiers.NETHERITE, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> BRONZE_BATTLEAXE = ITEMS.register("bronze_battleaxe", () -> new SwordItem(Tiers.IRON, 6, -3.1F, new Item.Properties()));
    public static final RegistryObject<Item> BRONZE_CROSSBOW = ITEMS.register("bronze_crossbow", () -> new GOTLegacyCrossbowItem(new Item.Properties().durability(465)));
    public static final RegistryObject<Item> BRONZE_DAGGER_POISONED = ITEMS.register("bronze_dagger_poisoned", () -> new GOTPoisonedDaggerItem(Tiers.IRON, new Item.Properties()));
    public static final RegistryObject<Item> BRONZE_HAMMER = ITEMS.register("bronze_hammer", () -> new SwordItem(Tiers.IRON, 7, -3.2F, new Item.Properties()));
    public static final RegistryObject<Item> BRONZE_MATTOCK = ITEMS.register("bronze_mattock", () -> new SwordItem(Tiers.IRON, 7, -3.2F, new Item.Properties()));
    public static final RegistryObject<Item> BRONZE_PIKE = ITEMS.register("bronze_pike", () -> new GOTLegacyPikeItem(Tiers.IRON, new Item.Properties()));
    public static final RegistryObject<Item> BRONZE_SCIMITAR = ITEMS.register("bronze_scimitar", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> BRONZE_SPEAR = ITEMS.register("bronze_spear", () -> new GOTLegacySpearItem(Tiers.IRON, new Item.Properties()));
    public static final RegistryObject<Item> BRONZE_SWORD = ITEMS.register("bronze_sword", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> BRONZE_THROWING_AXE = ITEMS.register("bronze_throwing_axe", () -> new GOTThrowingAxeItem(Tiers.IRON, new Item.Properties()));
    public static final RegistryObject<Item> CLUB = ITEMS.register("club", () -> new SwordItem(Tiers.WOOD, 7, -3.2F, new Item.Properties()));
    public static final RegistryObject<Item> COMMAND_SWORD = ITEMS.register("command_sword", () -> new GOTCommandSwordItem(new Item.Properties()));
    public static final RegistryObject<Item> COPPER_BATTLEAXE = ITEMS.register("copper_battleaxe", () -> new SwordItem(Tiers.IRON, 6, -3.1F, new Item.Properties()));
    public static final RegistryObject<Item> COPPER_CROSSBOW = ITEMS.register("copper_crossbow", () -> new GOTLegacyCrossbowItem(new Item.Properties().durability(465)));
    public static final RegistryObject<Item> COPPER_DAGGER = ITEMS.register("copper_dagger", () -> new GOTLegacyDaggerItem(Tiers.IRON, new Item.Properties()));
    public static final RegistryObject<Item> COPPER_DAGGER_POISONED = ITEMS.register("copper_dagger_poisoned", () -> new GOTPoisonedDaggerItem(Tiers.IRON, new Item.Properties()));
    public static final RegistryObject<Item> COPPER_GREATSWORD = ITEMS.register("copper_greatsword", () -> new SwordItem(Tiers.IRON, 6, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> COPPER_HAMMER = ITEMS.register("copper_hammer", () -> new SwordItem(Tiers.IRON, 7, -3.2F, new Item.Properties()));
    public static final RegistryObject<Item> COPPER_LONGSWORD = ITEMS.register("copper_longsword", () -> new SwordItem(Tiers.IRON, 4, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> COPPER_MATTOCK = ITEMS.register("copper_mattock", () -> new SwordItem(Tiers.IRON, 7, -3.2F, new Item.Properties()));
    public static final RegistryObject<Item> COPPER_PIKE = ITEMS.register("copper_pike", () -> new GOTLegacyPikeItem(Tiers.IRON, new Item.Properties()));
    public static final RegistryObject<Item> COPPER_SCIMITAR = ITEMS.register("copper_scimitar", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> COPPER_SPEAR = ITEMS.register("copper_spear", () -> new GOTLegacySpearItem(Tiers.IRON, new Item.Properties()));
    public static final RegistryObject<Item> COPPER_SWORD = ITEMS.register("copper_sword", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> COPPER_THROWING_AXE = ITEMS.register("copper_throwing_axe", () -> new GOTThrowingAxeItem(Tiers.IRON, new Item.Properties()));
    public static final RegistryObject<Item> CROSSBOW_BOLT = ITEMS.register("crossbow_bolt", () -> new GOTLegacyArrowItem(GOTLegacyArrowEntity.Kind.BOLT, new Item.Properties()));
    public static final RegistryObject<Item> CROSSBOW_BOLT_POISONED = ITEMS.register("crossbow_bolt_poisoned", () -> new GOTLegacyArrowItem(GOTLegacyArrowEntity.Kind.POISON_BOLT, new Item.Properties()));
    public static final RegistryObject<Item> CROWBAR = ITEMS.register("crowbar", () -> new SwordItem(Tiers.IRON, 7, -3.2F, new Item.Properties()));
    public static final RegistryObject<Item> CUTWAVE = ITEMS.register("cutwave", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> DARK_SISTER = ITEMS.register("dark_sister", () -> new SwordItem(Tiers.NETHERITE, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> DARKSTAR = ITEMS.register("darkstar", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> DAWN = ITEMS.register("dawn", () -> new SwordItem(Tiers.NETHERITE, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> DIAMOND_MATTOCK = ITEMS.register("diamond_mattock", () -> new SwordItem(Tiers.DIAMOND, 7, -3.2F, new Item.Properties()));
    public static final RegistryObject<Item> FIN = ITEMS.register("fin", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> FIRE_POT = ITEMS.register("fire_pot", () -> new GOTFirePotItem(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_BATTLEAXE = ITEMS.register("gold_battleaxe", () -> new SwordItem(Tiers.GOLD, 6, -3.1F, new Item.Properties()));
    public static final RegistryObject<Item> GOLD_CROSSBOW = ITEMS.register("gold_crossbow", () -> new GOTLegacyCrossbowItem(new Item.Properties().durability(465)));
    public static final RegistryObject<Item> GOLD_DAGGER = ITEMS.register("gold_dagger", () -> new GOTLegacyDaggerItem(Tiers.GOLD, new Item.Properties()));
    public static final RegistryObject<Item> GOLD_DAGGER_POISONED = ITEMS.register("gold_dagger_poisoned", () -> new GOTPoisonedDaggerItem(Tiers.GOLD, new Item.Properties()));
    public static final RegistryObject<Item> GOLD_GREATSWORD = ITEMS.register("gold_greatsword", () -> new SwordItem(Tiers.GOLD, 6, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> GOLD_HAMMER = ITEMS.register("gold_hammer", () -> new SwordItem(Tiers.GOLD, 7, -3.2F, new Item.Properties()));
    public static final RegistryObject<Item> GOLD_LONGSWORD = ITEMS.register("gold_longsword", () -> new SwordItem(Tiers.GOLD, 4, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> GOLD_MATTOCK = ITEMS.register("gold_mattock", () -> new SwordItem(Tiers.GOLD, 7, -3.2F, new Item.Properties()));
    public static final RegistryObject<Item> GOLD_PIKE = ITEMS.register("gold_pike", () -> new GOTLegacyPikeItem(Tiers.GOLD, new Item.Properties()));
    public static final RegistryObject<Item> GOLD_SCIMITAR = ITEMS.register("gold_scimitar", () -> new SwordItem(Tiers.GOLD, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> GOLD_SPEAR = ITEMS.register("gold_spear", () -> new GOTLegacySpearItem(Tiers.GOLD, new Item.Properties()));
    public static final RegistryObject<Item> GOLD_THROWING_AXE = ITEMS.register("gold_throwing_axe", () -> new GOTThrowingAxeItem(Tiers.GOLD, new Item.Properties()));
    public static final RegistryObject<Item> GREGOR_CLEGANE_SWORD = ITEMS.register("gregor_clegane_sword", () -> new SwordItem(Tiers.IRON, 6, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> HARPOON = ITEMS.register("harpoon", () -> new GOTLegacySpearItem(Tiers.IRON, new Item.Properties()));
    public static final RegistryObject<Item> HEARTEATER = ITEMS.register("hearteater", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> HEARTSBANE = ITEMS.register("heartsbane", () -> new SwordItem(Tiers.NETHERITE, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> HONOR = ITEMS.register("honor", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> ICE = ITEMS.register("ice", () -> new SwordItem(Tiers.NETHERITE, 6, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> ICE_HEAVY_SWORD = ITEMS.register("ice_heavy_sword", () -> new SwordItem(Tiers.NETHERITE, 6, -3.1F, new Item.Properties()));
    public static final RegistryObject<Item> ICE_SPEAR = ITEMS.register("ice_spear", () -> new GOTLegacySpearItem(Tiers.NETHERITE, new Item.Properties()));
    public static final RegistryObject<Item> ICE_SWORD = ITEMS.register("ice_sword", () -> new SwordItem(Tiers.NETHERITE, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> INDOMITABLE = ITEMS.register("indomitable", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> IRON_BATTLEAXE = ITEMS.register("iron_battleaxe", () -> new SwordItem(Tiers.IRON, 6, -3.1F, new Item.Properties()));
    public static final RegistryObject<Item> IRON_CROSSBOW = ITEMS.register("iron_crossbow", () -> new GOTLegacyCrossbowItem(new Item.Properties().durability(465)));
    public static final RegistryObject<Item> IRON_DAGGER = ITEMS.register("iron_dagger", () -> new GOTLegacyDaggerItem(Tiers.IRON, new Item.Properties()));
    public static final RegistryObject<Item> IRON_DAGGER_POISONED = ITEMS.register("iron_dagger_poisoned", () -> new GOTPoisonedDaggerItem(Tiers.IRON, new Item.Properties()));
    public static final RegistryObject<Item> IRON_GREATSWORD = ITEMS.register("iron_greatsword", () -> new SwordItem(Tiers.IRON, 6, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> IRON_HAMMER = ITEMS.register("iron_hammer", () -> new SwordItem(Tiers.IRON, 7, -3.2F, new Item.Properties()));
    public static final RegistryObject<Item> IRON_LONGSWORD = ITEMS.register("iron_longsword", () -> new SwordItem(Tiers.IRON, 4, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> IRON_MATTOCK = ITEMS.register("iron_mattock", () -> new SwordItem(Tiers.IRON, 7, -3.2F, new Item.Properties()));
    public static final RegistryObject<Item> IRON_PIKE = ITEMS.register("iron_pike", () -> new GOTLegacyPikeItem(Tiers.IRON, new Item.Properties()));
    public static final RegistryObject<Item> IRON_POLEARM = ITEMS.register("iron_polearm", () -> new GOTRegionalSpearItem(Tiers.IRON, new Item.Properties()));
    public static final RegistryObject<Item> IRON_SCIMITAR = ITEMS.register("iron_scimitar", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> IRON_SPEAR = ITEMS.register("iron_spear", () -> new GOTLegacySpearItem(Tiers.IRON, new Item.Properties()));
    public static final RegistryObject<Item> IRON_THROWING_AXE = ITEMS.register("iron_throwing_axe", () -> new GOTThrowingAxeItem(Tiers.IRON, new Item.Properties()));
    public static final RegistryObject<Item> JOFFREY_BARATHEON_CROSSBOW = ITEMS.register("joffrey_baratheon_crossbow", () -> new GOTLegacyCrossbowItem(new Item.Properties().durability(465)));
    public static final RegistryObject<Item> JUST_MAID = ITEMS.register("just_maid", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> KATANA = ITEMS.register("katana", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> LADY_FORLORN = ITEMS.register("lady_forlorn", () -> new SwordItem(Tiers.NETHERITE, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> LAMENTATION = ITEMS.register("lamentation", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> LIGHTBRINGER = ITEMS.register("lightbringer", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> LINGERING_POTION = ITEMS.register("lingering_potion", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LONGBOW = ITEMS.register("longbow", () -> new BowItem(new Item.Properties().durability(384)));
    public static final RegistryObject<Item> LONGCLAW = ITEMS.register("longclaw", () -> new SwordItem(Tiers.NETHERITE, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> NEEDLE = ITEMS.register("needle", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> NIGHT_KING_SWORD = ITEMS.register("night_king_sword", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> NIGHTFALL = ITEMS.register("nightfall", () -> new SwordItem(Tiers.NETHERITE, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> OATHKEEPER = ITEMS.register("oathkeeper", () -> new SwordItem(Tiers.NETHERITE, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_BATTLEAXE = ITEMS.register("obsidian_battleaxe", () -> new SwordItem(Tiers.IRON, 6, -3.1F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_DAGGER = ITEMS.register("obsidian_dagger", () -> new GOTLegacyDaggerItem(Tiers.IRON, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_DAGGER_POISONED = ITEMS.register("obsidian_dagger_poisoned", () -> new GOTPoisonedDaggerItem(Tiers.IRON, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_HAMMER = ITEMS.register("obsidian_hammer", () -> new SwordItem(Tiers.IRON, 7, -3.2F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_MATTOCK = ITEMS.register("obsidian_mattock", () -> new SwordItem(Tiers.IRON, 7, -3.2F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_PIKE = ITEMS.register("obsidian_pike", () -> new GOTLegacyPikeItem(Tiers.IRON, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_SCIMITAR = ITEMS.register("obsidian_scimitar", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_SPEAR = ITEMS.register("obsidian_spear", () -> new GOTLegacySpearItem(Tiers.IRON, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_SWORD = ITEMS.register("obsidian_sword", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_THROWING_AXE = ITEMS.register("obsidian_throwing_axe", () -> new GOTThrowingAxeItem(Tiers.IRON, new Item.Properties()));
    public static final RegistryObject<Item> ORPHAN_MAKER = ITEMS.register("orphan_maker", () -> new SwordItem(Tiers.NETHERITE, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> PETYR_BAELISH_DAGGER = ITEMS.register("petyr_baelish_dagger", () -> new SwordItem(Tiers.IRON, 2, -1.7F, new Item.Properties()));

    // Special-mob spawn eggs used for testing and creative placement.
    public static final RegistryObject<Item> BARROW_WRAITH_SPAWN_EGG = ITEMS.register("barrow_wraith_spawn_egg",
            () -> new ForgeSpawnEggItem(GOTEntities.BARROW_WRAITH, 0x1B1B22, 0x7C7C8A, new Item.Properties()));
    public static final RegistryObject<Item> MARSH_WRAITH_SPAWN_EGG = ITEMS.register("marsh_wraith_spawn_egg",
            () -> new ForgeSpawnEggItem(GOTEntities.MARSH_WRAITH, 0x425C45, 0xA0B89A, new Item.Properties()));
    public static final RegistryObject<Item> PRUNER = ITEMS.register("pruner", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> RED_RAIN = ITEMS.register("red_rain", () -> new SwordItem(Tiers.NETHERITE, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> REMINDER = ITEMS.register("reminder", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> ROBERT_BARATHEON_HAMMER = ITEMS.register("robert_baratheon_hammer", () -> new SwordItem(Tiers.IRON, 7, -3.2F, new Item.Properties()));
    public static final RegistryObject<Item> ROLLING_PIN = ITEMS.register("rolling_pin", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> SANDOR_CLEGANE_SWORD = ITEMS.register("sandor_clegane_sword", () -> new SwordItem(Tiers.IRON, 4, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> SARBACANE = ITEMS.register("sarbacane", () -> new GOTSarbacaneItem(new Item.Properties()));
    public static final RegistryObject<Item> SKULL_STAFF = ITEMS.register("skull_staff", () -> new SwordItem(Tiers.IRON, 4, -2.8F, new Item.Properties()));
    public static final RegistryObject<Item> STONE_BATTLEAXE = ITEMS.register("stone_battleaxe", () -> new SwordItem(Tiers.STONE, 6, -3.1F, new Item.Properties()));
    public static final RegistryObject<Item> STONE_DAGGER = ITEMS.register("stone_dagger", () -> new GOTLegacyDaggerItem(Tiers.STONE, new Item.Properties()));
    public static final RegistryObject<Item> STONE_DAGGER_POISONED = ITEMS.register("stone_dagger_poisoned", () -> new GOTPoisonedDaggerItem(Tiers.STONE, new Item.Properties()));
    public static final RegistryObject<Item> STONE_HAMMER = ITEMS.register("stone_hammer", () -> new SwordItem(Tiers.STONE, 7, -3.2F, new Item.Properties()));
    public static final RegistryObject<Item> STONE_MATTOCK = ITEMS.register("stone_mattock", () -> new SwordItem(Tiers.STONE, 7, -3.2F, new Item.Properties()));
    public static final RegistryObject<Item> STONE_PIKE = ITEMS.register("stone_pike", () -> new GOTLegacyPikeItem(Tiers.STONE, new Item.Properties()));
    public static final RegistryObject<Item> STONE_SCIMITAR = ITEMS.register("stone_scimitar", () -> new SwordItem(Tiers.STONE, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> STONE_SPEAR = ITEMS.register("stone_spear", () -> new GOTLegacySpearItem(Tiers.STONE, new Item.Properties()));
    public static final RegistryObject<Item> STONE_THROWING_AXE = ITEMS.register("stone_throwing_axe", () -> new GOTThrowingAxeItem(Tiers.STONE, new Item.Properties()));
    public static final RegistryObject<Item> SUNSPEAR = ITEMS.register("sunspear", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> TERMITE = ITEMS.register("termite", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TIDEWINGS = ITEMS.register("tidewings", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> TRIDENT = ITEMS.register("trident", () -> new SwordItem(Tiers.IRON, 4, -2.8F, new Item.Properties()));
    public static final RegistryObject<Item> TRUTH = ITEMS.register("truth", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> VALYRIAN_BATTLEAXE = ITEMS.register("valyrian_battleaxe", () -> new SwordItem(Tiers.NETHERITE, 6, -3.1F, new Item.Properties()));
    public static final RegistryObject<Item> VALYRIAN_CROSSBOW = ITEMS.register("valyrian_crossbow", () -> new GOTLegacyCrossbowItem(new Item.Properties().durability(465)));
    public static final RegistryObject<Item> VALYRIAN_DAGGER = ITEMS.register("valyrian_dagger", () -> new GOTLegacyDaggerItem(Tiers.NETHERITE, new Item.Properties()));
    public static final RegistryObject<Item> VALYRIAN_DAGGER_POISONED = ITEMS.register("valyrian_dagger_poisoned", () -> new GOTPoisonedDaggerItem(Tiers.NETHERITE, new Item.Properties()));
    public static final RegistryObject<Item> VALYRIAN_GREATSWORD = ITEMS.register("valyrian_greatsword", () -> new SwordItem(Tiers.NETHERITE, 6, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> VALYRIAN_HAMMER = ITEMS.register("valyrian_hammer", () -> new SwordItem(Tiers.NETHERITE, 7, -3.2F, new Item.Properties()));
    public static final RegistryObject<Item> VALYRIAN_LONGSWORD = ITEMS.register("valyrian_longsword", () -> new SwordItem(Tiers.NETHERITE, 4, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> VALYRIAN_MATTOCK = ITEMS.register("valyrian_mattock", () -> new SwordItem(Tiers.NETHERITE, 7, -3.2F, new Item.Properties()));
    public static final RegistryObject<Item> VALYRIAN_PIKE = ITEMS.register("valyrian_pike", () -> new GOTLegacyPikeItem(Tiers.NETHERITE, new Item.Properties()));
    public static final RegistryObject<Item> VALYRIAN_SCIMITAR = ITEMS.register("valyrian_scimitar", () -> new SwordItem(Tiers.NETHERITE, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> VALYRIAN_SPEAR = ITEMS.register("valyrian_spear", () -> new GOTLegacySpearItem(Tiers.NETHERITE, new Item.Properties()));
    public static final RegistryObject<Item> VALYRIAN_SWORD = ITEMS.register("valyrian_sword", () -> new SwordItem(Tiers.NETHERITE, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> VALYRIAN_THROWING_AXE = ITEMS.register("valyrian_throwing_axe", () -> new GOTThrowingAxeItem(Tiers.NETHERITE, new Item.Properties()));
    public static final RegistryObject<Item> VIGILANCE = ITEMS.register("vigilance", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> WALKING_STICK = ITEMS.register("walking_stick", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> WIDOW_WAIL = ITEMS.register("widow_wail", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> WOOD_BATTLEAXE = ITEMS.register("wood_battleaxe", () -> new SwordItem(Tiers.WOOD, 6, -3.1F, new Item.Properties()));
    public static final RegistryObject<Item> WOOD_DAGGER = ITEMS.register("wood_dagger", () -> new GOTLegacyDaggerItem(Tiers.WOOD, new Item.Properties()));
    public static final RegistryObject<Item> WOOD_DAGGER_POISONED = ITEMS.register("wood_dagger_poisoned", () -> new GOTPoisonedDaggerItem(Tiers.WOOD, new Item.Properties()));
    public static final RegistryObject<Item> WOOD_HAMMER = ITEMS.register("wood_hammer", () -> new SwordItem(Tiers.WOOD, 7, -3.2F, new Item.Properties()));
    public static final RegistryObject<Item> WOOD_MATTOCK = ITEMS.register("wood_mattock", () -> new SwordItem(Tiers.WOOD, 7, -3.2F, new Item.Properties()));
    public static final RegistryObject<Item> WOOD_PIKE = ITEMS.register("wood_pike", () -> new GOTLegacyPikeItem(Tiers.WOOD, new Item.Properties()));
    public static final RegistryObject<Item> WOOD_SCIMITAR = ITEMS.register("wood_scimitar", () -> new SwordItem(Tiers.WOOD, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> WOOD_SPEAR = ITEMS.register("wood_spear", () -> new GOTLegacySpearItem(Tiers.WOOD, new Item.Properties()));
    public static final RegistryObject<Item> WOOD_THROWING_AXE = ITEMS.register("wood_throwing_axe", () -> new GOTThrowingAxeItem(Tiers.WOOD, new Item.Properties()));
    public static final RegistryObject<Item> ALLOY_STEEL_BOOTS = ITEMS.register("alloy_steel_boots", () -> new ArmorItem(GOTArmorMaterials.of("alloy_steel"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> ALLOY_STEEL_CHAINMAIL_BOOTS = ITEMS.register("alloy_steel_chainmail_boots", () -> new ArmorItem(GOTArmorMaterials.of("alloy_steel_chainmail"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> ALLOY_STEEL_CHAINMAIL_CHESTPLATE = ITEMS.register("alloy_steel_chainmail_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("alloy_steel_chainmail"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> ALLOY_STEEL_CHAINMAIL_HELMET = ITEMS.register("alloy_steel_chainmail_helmet", () -> new ArmorItem(GOTArmorMaterials.of("alloy_steel_chainmail"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> ALLOY_STEEL_CHAINMAIL_LEGGINGS = ITEMS.register("alloy_steel_chainmail_leggings", () -> new ArmorItem(GOTArmorMaterials.of("alloy_steel_chainmail"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> ALLOY_STEEL_CHESTPLATE = ITEMS.register("alloy_steel_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("alloy_steel"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> ALLOY_STEEL_HELMET = ITEMS.register("alloy_steel_helmet", () -> new ArmorItem(GOTArmorMaterials.of("alloy_steel"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> ALLOY_STEEL_LEGGINGS = ITEMS.register("alloy_steel_leggings", () -> new ArmorItem(GOTArmorMaterials.of("alloy_steel"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> ANONYMOUS_MASK = ITEMS.register("anonymous_mask", () -> new ArmorItem(GOTArmorMaterials.of("anonymous"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> ARRYN_BOOTS = ITEMS.register("arryn_boots", () -> new ArmorItem(GOTArmorMaterials.of("arryn"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> ARRYN_CHESTPLATE = ITEMS.register("arryn_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("arryn"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> ARRYN_HELMET = ITEMS.register("arryn_helmet", () -> new ArmorItem(GOTArmorMaterials.of("arryn"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> ARRYN_LEGGINGS = ITEMS.register("arryn_leggings", () -> new ArmorItem(GOTArmorMaterials.of("arryn"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> ARRYNGUARD_BOOTS = ITEMS.register("arrynguard_boots", () -> new ArmorItem(GOTArmorMaterials.of("arrynguard"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> ARRYNGUARD_CHESTPLATE = ITEMS.register("arrynguard_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("arrynguard"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> ARRYNGUARD_HELMET = ITEMS.register("arrynguard_helmet", () -> new ArmorItem(GOTArmorMaterials.of("arrynguard"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> ARRYNGUARD_LEGGINGS = ITEMS.register("arrynguard_leggings", () -> new ArmorItem(GOTArmorMaterials.of("arrynguard"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> ASSHAI_BOOTS = ITEMS.register("asshai_boots", () -> new ArmorItem(GOTArmorMaterials.of("asshai"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> ASSHAI_CHESTPLATE = ITEMS.register("asshai_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("asshai"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> ASSHAI_HELMET = ITEMS.register("asshai_helmet", () -> new ArmorItem(GOTArmorMaterials.of("asshai"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> ASSHAI_LEGGINGS = ITEMS.register("asshai_leggings", () -> new ArmorItem(GOTArmorMaterials.of("asshai"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> ASSHAI_MASK = ITEMS.register("asshai_mask", () -> new ArmorItem(GOTArmorMaterials.of("asshai"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> BONE_BOOTS = ITEMS.register("bone_boots", () -> new ArmorItem(GOTArmorMaterials.of("bone"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> BONE_CHESTPLATE = ITEMS.register("bone_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("bone"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> BONE_HELMET = ITEMS.register("bone_helmet", () -> new ArmorItem(GOTArmorMaterials.of("bone"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> BONE_LEGGINGS = ITEMS.register("bone_leggings", () -> new ArmorItem(GOTArmorMaterials.of("bone"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> BRAAVOS_BOOTS = ITEMS.register("braavos_boots", () -> new ArmorItem(GOTArmorMaterials.of("braavos"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> BRAAVOS_CHESTPLATE = ITEMS.register("braavos_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("braavos"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> BRAAVOS_HELMET = ITEMS.register("braavos_helmet", () -> new ArmorItem(GOTArmorMaterials.of("braavos"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> BRAAVOS_LEGGINGS = ITEMS.register("braavos_leggings", () -> new ArmorItem(GOTArmorMaterials.of("braavos"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> BRONZE_BOOTS = ITEMS.register("bronze_boots", () -> new ArmorItem(GOTArmorMaterials.of("bronze"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> BRONZE_CHAINMAIL_BOOTS = ITEMS.register("bronze_chainmail_boots", () -> new ArmorItem(GOTArmorMaterials.of("bronze_chainmail"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> BRONZE_CHAINMAIL_CHESTPLATE = ITEMS.register("bronze_chainmail_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("bronze_chainmail"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> BRONZE_CHAINMAIL_HELMET = ITEMS.register("bronze_chainmail_helmet", () -> new ArmorItem(GOTArmorMaterials.of("bronze_chainmail"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> BRONZE_CHAINMAIL_LEGGINGS = ITEMS.register("bronze_chainmail_leggings", () -> new ArmorItem(GOTArmorMaterials.of("bronze_chainmail"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> BRONZE_CHESTPLATE = ITEMS.register("bronze_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("bronze"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> BRONZE_HELMET = ITEMS.register("bronze_helmet", () -> new ArmorItem(GOTArmorMaterials.of("bronze"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> BRONZE_LEGGINGS = ITEMS.register("bronze_leggings", () -> new ArmorItem(GOTArmorMaterials.of("bronze"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> COPPER_BOOTS = ITEMS.register("copper_boots", () -> new ArmorItem(GOTArmorMaterials.of("copper"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> COPPER_CHAINMAIL_BOOTS = ITEMS.register("copper_chainmail_boots", () -> new ArmorItem(GOTArmorMaterials.of("copper_chainmail"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> COPPER_CHAINMAIL_CHESTPLATE = ITEMS.register("copper_chainmail_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("copper_chainmail"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> COPPER_CHAINMAIL_HELMET = ITEMS.register("copper_chainmail_helmet", () -> new ArmorItem(GOTArmorMaterials.of("copper_chainmail"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> COPPER_CHAINMAIL_LEGGINGS = ITEMS.register("copper_chainmail_leggings", () -> new ArmorItem(GOTArmorMaterials.of("copper_chainmail"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> COPPER_CHESTPLATE = ITEMS.register("copper_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("copper"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> COPPER_HELMET = ITEMS.register("copper_helmet", () -> new ArmorItem(GOTArmorMaterials.of("copper"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> COPPER_LEGGINGS = ITEMS.register("copper_leggings", () -> new ArmorItem(GOTArmorMaterials.of("copper"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> CROWNLANDS_BOOTS = ITEMS.register("crownlands_boots", () -> new ArmorItem(GOTArmorMaterials.of("crownlands"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> CROWNLANDS_CHESTPLATE = ITEMS.register("crownlands_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("crownlands"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> CROWNLANDS_HELMET = ITEMS.register("crownlands_helmet", () -> new ArmorItem(GOTArmorMaterials.of("crownlands"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> CROWNLANDS_LEGGINGS = ITEMS.register("crownlands_leggings", () -> new ArmorItem(GOTArmorMaterials.of("crownlands"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> DORNE_BOOTS = ITEMS.register("dorne_boots", () -> new ArmorItem(GOTArmorMaterials.of("dorne"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> DORNE_CHESTPLATE = ITEMS.register("dorne_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("dorne"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> DORNE_HELMET = ITEMS.register("dorne_helmet", () -> new ArmorItem(GOTArmorMaterials.of("dorne"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> DORNE_LEGGINGS = ITEMS.register("dorne_leggings", () -> new ArmorItem(GOTArmorMaterials.of("dorne"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> DOTHRAKI_BOOTS = ITEMS.register("dothraki_boots", () -> new ArmorItem(GOTArmorMaterials.of("dothraki"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> DOTHRAKI_CHESTPLATE = ITEMS.register("dothraki_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("dothraki"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> DOTHRAKI_HELMET = ITEMS.register("dothraki_helmet", () -> new ArmorItem(GOTArmorMaterials.of("dothraki"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> DOTHRAKI_LEGGINGS = ITEMS.register("dothraki_leggings", () -> new ArmorItem(GOTArmorMaterials.of("dothraki"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> DRAGONSTONE_BOOTS = ITEMS.register("dragonstone_boots", () -> new ArmorItem(GOTArmorMaterials.of("dragonstone"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> DRAGONSTONE_CHESTPLATE = ITEMS.register("dragonstone_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("dragonstone"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> DRAGONSTONE_HELMET = ITEMS.register("dragonstone_helmet", () -> new ArmorItem(GOTArmorMaterials.of("dragonstone"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> DRAGONSTONE_LEGGINGS = ITEMS.register("dragonstone_leggings", () -> new ArmorItem(GOTArmorMaterials.of("dragonstone"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> FUR_BOOTS = ITEMS.register("fur_boots", () -> new ArmorItem(GOTArmorMaterials.of("fur"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> FUR_CHESTPLATE = ITEMS.register("fur_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("fur"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> FUR_HELMET = ITEMS.register("fur_helmet", () -> new ArmorItem(GOTArmorMaterials.of("fur"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> FUR_LEGGINGS = ITEMS.register("fur_leggings", () -> new ArmorItem(GOTArmorMaterials.of("fur"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> GHISCAR_BOOTS = ITEMS.register("ghiscar_boots", () -> new ArmorItem(GOTArmorMaterials.of("ghiscar"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> GHISCAR_CHESTPLATE = ITEMS.register("ghiscar_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("ghiscar"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> GHISCAR_HELMET = ITEMS.register("ghiscar_helmet", () -> new ArmorItem(GOTArmorMaterials.of("ghiscar"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> GHISCAR_LEGGINGS = ITEMS.register("ghiscar_leggings", () -> new ArmorItem(GOTArmorMaterials.of("ghiscar"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> GIFT_BOOTS = ITEMS.register("gift_boots", () -> new ArmorItem(GOTArmorMaterials.of("gift"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> GIFT_CHESTPLATE = ITEMS.register("gift_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("gift"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> GIFT_HELMET = ITEMS.register("gift_helmet", () -> new ArmorItem(GOTArmorMaterials.of("gift"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> GIFT_LEGGINGS = ITEMS.register("gift_leggings", () -> new ArmorItem(GOTArmorMaterials.of("gift"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> GOLD_CHAINMAIL_BOOTS = ITEMS.register("gold_chainmail_boots", () -> new ArmorItem(GOTArmorMaterials.of("gold_chainmail"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> GOLD_CHAINMAIL_CHESTPLATE = ITEMS.register("gold_chainmail_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("gold_chainmail"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> GOLD_CHAINMAIL_HELMET = ITEMS.register("gold_chainmail_helmet", () -> new ArmorItem(GOTArmorMaterials.of("gold_chainmail"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> GOLD_CHAINMAIL_LEGGINGS = ITEMS.register("gold_chainmail_leggings", () -> new ArmorItem(GOTArmorMaterials.of("gold_chainmail"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> GOLDEN_COMPANY_BOOTS = ITEMS.register("golden_company_boots", () -> new ArmorItem(GOTArmorMaterials.of("golden_company"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> GOLDEN_COMPANY_CHESTPLATE = ITEMS.register("golden_company_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("golden_company"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> GOLDEN_COMPANY_HELMET = ITEMS.register("golden_company_helmet", () -> new ArmorItem(GOTArmorMaterials.of("golden_company"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> GOLDEN_COMPANY_LEGGINGS = ITEMS.register("golden_company_leggings", () -> new ArmorItem(GOTArmorMaterials.of("golden_company"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> HAND_GOLD = ITEMS.register("hand_gold", () -> new ArmorItem(GOTArmorMaterials.of("hand_gold"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> HAND_SILVER = ITEMS.register("hand_silver", () -> new ArmorItem(GOTArmorMaterials.of("hand_silver"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> HARPY = ITEMS.register("harpy", () -> new ArmorItem(GOTArmorMaterials.of("harpy"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> HILLMEN_BOOTS = ITEMS.register("hillmen_boots", () -> new ArmorItem(GOTArmorMaterials.of("hillmen"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> HILLMEN_CHESTPLATE = ITEMS.register("hillmen_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("hillmen"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> HILLMEN_HELMET = ITEMS.register("hillmen_helmet", () -> new ArmorItem(GOTArmorMaterials.of("hillmen"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> HILLMEN_LEGGINGS = ITEMS.register("hillmen_leggings", () -> new ArmorItem(GOTArmorMaterials.of("hillmen"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> IBBEN_BOOTS = ITEMS.register("ibben_boots", () -> new ArmorItem(GOTArmorMaterials.of("ibben"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> IBBEN_CHESTPLATE = ITEMS.register("ibben_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("ibben"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> IBBEN_LEGGINGS = ITEMS.register("ibben_leggings", () -> new ArmorItem(GOTArmorMaterials.of("ibben"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> IRONBORN_BOOTS = ITEMS.register("ironborn_boots", () -> new ArmorItem(GOTArmorMaterials.of("ironborn"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> IRONBORN_CHESTPLATE = ITEMS.register("ironborn_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("ironborn"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> IRONBORN_HELMET = ITEMS.register("ironborn_helmet", () -> new ArmorItem(GOTArmorMaterials.of("ironborn"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> IRONBORN_LEGGINGS = ITEMS.register("ironborn_leggings", () -> new ArmorItem(GOTArmorMaterials.of("ironborn"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> JOGOS_NHAI_BOOTS = ITEMS.register("jogos_nhai_boots", () -> new ArmorItem(GOTArmorMaterials.of("jogos_nhai"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> JOGOS_NHAI_CHESTPLATE = ITEMS.register("jogos_nhai_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("jogos_nhai"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> JOGOS_NHAI_HELMET = ITEMS.register("jogos_nhai_helmet", () -> new ArmorItem(GOTArmorMaterials.of("jogos_nhai"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> JOGOS_NHAI_LEGGINGS = ITEMS.register("jogos_nhai_leggings", () -> new ArmorItem(GOTArmorMaterials.of("jogos_nhai"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> KINGSGUARD_BOOTS = ITEMS.register("kingsguard_boots", () -> new ArmorItem(GOTArmorMaterials.of("kingsguard"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> KINGSGUARD_CHESTPLATE = ITEMS.register("kingsguard_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("kingsguard"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> KINGSGUARD_HELMET = ITEMS.register("kingsguard_helmet", () -> new ArmorItem(GOTArmorMaterials.of("kingsguard"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> KINGSGUARD_LEGGINGS = ITEMS.register("kingsguard_leggings", () -> new ArmorItem(GOTArmorMaterials.of("kingsguard"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> LHAZAR_BOOTS = ITEMS.register("lhazar_boots", () -> new ArmorItem(GOTArmorMaterials.of("lhazar"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> LHAZAR_CHESTPLATE = ITEMS.register("lhazar_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("lhazar"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> LHAZAR_HELMET = ITEMS.register("lhazar_helmet", () -> new ArmorItem(GOTArmorMaterials.of("lhazar"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> LHAZAR_LEGGINGS = ITEMS.register("lhazar_leggings", () -> new ArmorItem(GOTArmorMaterials.of("lhazar"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> LION_BOOTS = ITEMS.register("lion_boots", () -> new ArmorItem(GOTArmorMaterials.of("lion"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> LION_CHESTPLATE = ITEMS.register("lion_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("lion"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> LION_HELMET = ITEMS.register("lion_helmet", () -> new ArmorItem(GOTArmorMaterials.of("lion"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> LION_LEGGINGS = ITEMS.register("lion_leggings", () -> new ArmorItem(GOTArmorMaterials.of("lion"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> LORATH_BOOTS = ITEMS.register("lorath_boots", () -> new ArmorItem(GOTArmorMaterials.of("lorath"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> LORATH_CHESTPLATE = ITEMS.register("lorath_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("lorath"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> LORATH_HELMET = ITEMS.register("lorath_helmet", () -> new ArmorItem(GOTArmorMaterials.of("lorath"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> LORATH_LEGGINGS = ITEMS.register("lorath_leggings", () -> new ArmorItem(GOTArmorMaterials.of("lorath"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> LYS_BOOTS = ITEMS.register("lys_boots", () -> new ArmorItem(GOTArmorMaterials.of("lys"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> LYS_CHESTPLATE = ITEMS.register("lys_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("lys"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> LYS_HELMET = ITEMS.register("lys_helmet", () -> new ArmorItem(GOTArmorMaterials.of("lys"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> LYS_LEGGINGS = ITEMS.register("lys_leggings", () -> new ArmorItem(GOTArmorMaterials.of("lys"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> MONFORD_VELARYON_BROOCH = ITEMS.register("monford_velaryon_brooch", () -> new ArmorItem(GOTArmorMaterials.of("monford_velaryon_brooch"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> MOSSOVY_BOOTS = ITEMS.register("mossovy_boots", () -> new ArmorItem(GOTArmorMaterials.of("mossovy"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> MOSSOVY_CHESTPLATE = ITEMS.register("mossovy_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("mossovy"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> MOSSOVY_LEGGINGS = ITEMS.register("mossovy_leggings", () -> new ArmorItem(GOTArmorMaterials.of("mossovy"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> MYR_BOOTS = ITEMS.register("myr_boots", () -> new ArmorItem(GOTArmorMaterials.of("myr"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> MYR_CHESTPLATE = ITEMS.register("myr_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("myr"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> MYR_HELMET = ITEMS.register("myr_helmet", () -> new ArmorItem(GOTArmorMaterials.of("myr"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> MYR_LEGGINGS = ITEMS.register("myr_leggings", () -> new ArmorItem(GOTArmorMaterials.of("myr"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> NORTH_BOOTS = ITEMS.register("north_boots", () -> new ArmorItem(GOTArmorMaterials.of("north"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> NORTH_CHESTPLATE = ITEMS.register("north_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("north"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> NORTH_HELMET = ITEMS.register("north_helmet", () -> new GOTLegacyHelmetItem(GOTArmorMaterials.of("north"), new Item.Properties(), GOTLegacyHelmetItem.Shape.NORTH));
    public static final RegistryObject<Item> NORTH_LEGGINGS = ITEMS.register("north_leggings", () -> new ArmorItem(GOTArmorMaterials.of("north"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> NORTHGUARD_BOOTS = ITEMS.register("northguard_boots", () -> new ArmorItem(GOTArmorMaterials.of("northguard"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> NORTHGUARD_CHESTPLATE = ITEMS.register("northguard_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("northguard"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> NORTHGUARD_HELMET = ITEMS.register("northguard_helmet", () -> new ArmorItem(GOTArmorMaterials.of("northguard"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> NORTHGUARD_LEGGINGS = ITEMS.register("northguard_leggings", () -> new ArmorItem(GOTArmorMaterials.of("northguard"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> NORVOS_BOOTS = ITEMS.register("norvos_boots", () -> new ArmorItem(GOTArmorMaterials.of("norvos"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> NORVOS_CHESTPLATE = ITEMS.register("norvos_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("norvos"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> NORVOS_HELMET = ITEMS.register("norvos_helmet", () -> new ArmorItem(GOTArmorMaterials.of("norvos"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> NORVOS_LEGGINGS = ITEMS.register("norvos_leggings", () -> new ArmorItem(GOTArmorMaterials.of("norvos"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> ORYX_BOOTS = ITEMS.register("oryx_boots", () -> new ArmorItem(GOTArmorMaterials.of("oryx"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> ORYX_CHESTPLATE = ITEMS.register("oryx_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("oryx"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> ORYX_HELMET = ITEMS.register("oryx_helmet", () -> new ArmorItem(GOTArmorMaterials.of("oryx"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> ORYX_LEGGINGS = ITEMS.register("oryx_leggings", () -> new ArmorItem(GOTArmorMaterials.of("oryx"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> PENTOS_BOOTS = ITEMS.register("pentos_boots", () -> new ArmorItem(GOTArmorMaterials.of("pentos"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> PENTOS_CHESTPLATE = ITEMS.register("pentos_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("pentos"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> PENTOS_HELMET = ITEMS.register("pentos_helmet", () -> new ArmorItem(GOTArmorMaterials.of("pentos"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> PENTOS_LEGGINGS = ITEMS.register("pentos_leggings", () -> new ArmorItem(GOTArmorMaterials.of("pentos"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> PETYR_BAELISH_BROOCH = ITEMS.register("petyr_baelish_brooch", () -> new ArmorItem(GOTArmorMaterials.of("petyr_baelish_brooch"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> QARTH_BOOTS = ITEMS.register("qarth_boots", () -> new ArmorItem(GOTArmorMaterials.of("qarth"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> QARTH_CHESTPLATE = ITEMS.register("qarth_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("qarth"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> QARTH_HELMET = ITEMS.register("qarth_helmet", () -> new ArmorItem(GOTArmorMaterials.of("qarth"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> QARTH_LEGGINGS = ITEMS.register("qarth_leggings", () -> new ArmorItem(GOTArmorMaterials.of("qarth"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> QOHOR_BOOTS = ITEMS.register("qohor_boots", () -> new ArmorItem(GOTArmorMaterials.of("qohor"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> QOHOR_CHESTPLATE = ITEMS.register("qohor_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("qohor"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> QOHOR_HELMET = ITEMS.register("qohor_helmet", () -> new ArmorItem(GOTArmorMaterials.of("qohor"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> QOHOR_LEGGINGS = ITEMS.register("qohor_leggings", () -> new ArmorItem(GOTArmorMaterials.of("qohor"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> REACH_BOOTS = ITEMS.register("reach_boots", () -> new ArmorItem(GOTArmorMaterials.of("reach"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> REACH_CHESTPLATE = ITEMS.register("reach_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("reach"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> REACH_HELMET = ITEMS.register("reach_helmet", () -> new GOTLegacyHelmetItem(GOTArmorMaterials.of("reach"), new Item.Properties(), GOTLegacyHelmetItem.Shape.REACH));
    public static final RegistryObject<Item> REACH_LEGGINGS = ITEMS.register("reach_leggings", () -> new ArmorItem(GOTArmorMaterials.of("reach"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> REACHGUARD_BOOTS = ITEMS.register("reachguard_boots", () -> new ArmorItem(GOTArmorMaterials.of("reachguard"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> REACHGUARD_CHESTPLATE = ITEMS.register("reachguard_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("reachguard"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> REACHGUARD_HELMET = ITEMS.register("reachguard_helmet", () -> new ArmorItem(GOTArmorMaterials.of("reachguard"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> REACHGUARD_LEGGINGS = ITEMS.register("reachguard_leggings", () -> new ArmorItem(GOTArmorMaterials.of("reachguard"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> RIVERLANDS_BOOTS = ITEMS.register("riverlands_boots", () -> new ArmorItem(GOTArmorMaterials.of("riverlands"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> RIVERLANDS_CHESTPLATE = ITEMS.register("riverlands_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("riverlands"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> RIVERLANDS_HELMET = ITEMS.register("riverlands_helmet", () -> new ArmorItem(GOTArmorMaterials.of("riverlands"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> RIVERLANDS_LEGGINGS = ITEMS.register("riverlands_leggings", () -> new ArmorItem(GOTArmorMaterials.of("riverlands"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> ROBERT_BARATHEON_HELMET = ITEMS.register("robert_baratheon_helmet", () -> new ArmorItem(GOTArmorMaterials.of("robert_baratheon"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> ROBES_BOOTS = ITEMS.register("robes_boots", () -> new ArmorItem(GOTArmorMaterials.of("robes"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> ROBES_CHESTPLATE = ITEMS.register("robes_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("robes"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> ROBES_LEGGINGS = ITEMS.register("robes_leggings", () -> new ArmorItem(GOTArmorMaterials.of("robes"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> SANDOR_CLEGANE_HELMET = ITEMS.register("sandor_clegane_helmet", () -> new ArmorItem(GOTArmorMaterials.of("sandor_clegane"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> SOTHORYOS_BOOTS = ITEMS.register("sothoryos_boots", () -> new ArmorItem(GOTArmorMaterials.of("sothoryos"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> SOTHORYOS_CHESTPLATE = ITEMS.register("sothoryos_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("sothoryos"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> SOTHORYOS_HELMET = ITEMS.register("sothoryos_helmet", () -> new ArmorItem(GOTArmorMaterials.of("sothoryos"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> SOTHORYOS_HELMET_CHIEFTAIN = ITEMS.register("sothoryos_helmet_chieftain", () -> new ArmorItem(GOTArmorMaterials.of("sothoryos"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> SOTHORYOS_LEGGINGS = ITEMS.register("sothoryos_leggings", () -> new ArmorItem(GOTArmorMaterials.of("sothoryos"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> STORMLANDS_BOOTS = ITEMS.register("stormlands_boots", () -> new ArmorItem(GOTArmorMaterials.of("stormlands"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> STORMLANDS_CHESTPLATE = ITEMS.register("stormlands_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("stormlands"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> STORMLANDS_HELMET = ITEMS.register("stormlands_helmet", () -> new ArmorItem(GOTArmorMaterials.of("stormlands"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> STORMLANDS_LEGGINGS = ITEMS.register("stormlands_leggings", () -> new ArmorItem(GOTArmorMaterials.of("stormlands"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> SUMMER_BOOTS = ITEMS.register("summer_boots", () -> new ArmorItem(GOTArmorMaterials.of("summer"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> SUMMER_CHESTPLATE = ITEMS.register("summer_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("summer"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> SUMMER_HELMET = ITEMS.register("summer_helmet", () -> new ArmorItem(GOTArmorMaterials.of("summer"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> SUMMER_LEGGINGS = ITEMS.register("summer_leggings", () -> new ArmorItem(GOTArmorMaterials.of("summer"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> TARGARYEN_BOOTS = ITEMS.register("targaryen_boots", () -> new ArmorItem(GOTArmorMaterials.of("targaryen"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> TARGARYEN_CHESTPLATE = ITEMS.register("targaryen_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("targaryen"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> TARGARYEN_HELMET = ITEMS.register("targaryen_helmet", () -> new ArmorItem(GOTArmorMaterials.of("targaryen"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> TARGARYEN_LEGGINGS = ITEMS.register("targaryen_leggings", () -> new ArmorItem(GOTArmorMaterials.of("targaryen"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> TYRION_LANNISTER_BROOCH = ITEMS.register("tyrion_lannister_brooch", () -> new ArmorItem(GOTArmorMaterials.of("tyrion_lannister_brooch"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> TYROSH_BOOTS = ITEMS.register("tyrosh_boots", () -> new ArmorItem(GOTArmorMaterials.of("tyrosh"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> TYROSH_CHESTPLATE = ITEMS.register("tyrosh_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("tyrosh"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> TYROSH_HELMET = ITEMS.register("tyrosh_helmet", () -> new ArmorItem(GOTArmorMaterials.of("tyrosh"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> TYROSH_LEGGINGS = ITEMS.register("tyrosh_leggings", () -> new ArmorItem(GOTArmorMaterials.of("tyrosh"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> UNSULLIED_BOOTS = ITEMS.register("unsullied_boots", () -> new ArmorItem(GOTArmorMaterials.of("unsullied"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> UNSULLIED_CHESTPLATE = ITEMS.register("unsullied_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("unsullied"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> UNSULLIED_HELMET = ITEMS.register("unsullied_helmet", () -> new ArmorItem(GOTArmorMaterials.of("unsullied"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> UNSULLIED_LEGGINGS = ITEMS.register("unsullied_leggings", () -> new ArmorItem(GOTArmorMaterials.of("unsullied"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> VALYRIAN_BOOTS = ITEMS.register("valyrian_boots", () -> new ArmorItem(GOTArmorMaterials.of("valyrian"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> VALYRIAN_CHAINMAIL_BOOTS = ITEMS.register("valyrian_chainmail_boots", () -> new ArmorItem(GOTArmorMaterials.of("valyrian_chainmail"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> VALYRIAN_CHAINMAIL_CHESTPLATE = ITEMS.register("valyrian_chainmail_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("valyrian_chainmail"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> VALYRIAN_CHAINMAIL_HELMET = ITEMS.register("valyrian_chainmail_helmet", () -> new ArmorItem(GOTArmorMaterials.of("valyrian_chainmail"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> VALYRIAN_CHAINMAIL_LEGGINGS = ITEMS.register("valyrian_chainmail_leggings", () -> new ArmorItem(GOTArmorMaterials.of("valyrian_chainmail"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> VALYRIAN_CHESTPLATE = ITEMS.register("valyrian_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("valyrian"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> VALYRIAN_HELMET = ITEMS.register("valyrian_helmet", () -> new ArmorItem(GOTArmorMaterials.of("valyrian"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> VALYRIAN_LEGGINGS = ITEMS.register("valyrian_leggings", () -> new ArmorItem(GOTArmorMaterials.of("valyrian"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> VOLANTIS_BOOTS = ITEMS.register("volantis_boots", () -> new ArmorItem(GOTArmorMaterials.of("volantis"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> VOLANTIS_CHESTPLATE = ITEMS.register("volantis_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("volantis"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> VOLANTIS_HELMET = ITEMS.register("volantis_helmet", () -> new ArmorItem(GOTArmorMaterials.of("volantis"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> VOLANTIS_LEGGINGS = ITEMS.register("volantis_leggings", () -> new ArmorItem(GOTArmorMaterials.of("volantis"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> WESTERLANDS_BOOTS = ITEMS.register("westerlands_boots", () -> new ArmorItem(GOTArmorMaterials.of("westerlands"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> WESTERLANDS_CHESTPLATE = ITEMS.register("westerlands_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("westerlands"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> WESTERLANDS_HELMET = ITEMS.register("westerlands_helmet", () -> new ArmorItem(GOTArmorMaterials.of("westerlands"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> WESTERLANDS_LEGGINGS = ITEMS.register("westerlands_leggings", () -> new ArmorItem(GOTArmorMaterials.of("westerlands"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> WESTERLANDSGUARD_BOOTS = ITEMS.register("westerlandsguard_boots", () -> new ArmorItem(GOTArmorMaterials.of("westerlandsguard"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> WESTERLANDSGUARD_CHESTPLATE = ITEMS.register("westerlandsguard_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("westerlandsguard"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> WESTERLANDSGUARD_HELMET = ITEMS.register("westerlandsguard_helmet", () -> new ArmorItem(GOTArmorMaterials.of("westerlandsguard"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> WESTERLANDSGUARD_LEGGINGS = ITEMS.register("westerlandsguard_leggings", () -> new ArmorItem(GOTArmorMaterials.of("westerlandsguard"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> WHITE_WALKERS_BOOTS = ITEMS.register("white_walkers_boots", () -> new ArmorItem(GOTArmorMaterials.of("white_walkers"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> WHITE_WALKERS_CHESTPLATE = ITEMS.register("white_walkers_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("white_walkers"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> WHITE_WALKERS_LEGGINGS = ITEMS.register("white_walkers_leggings", () -> new ArmorItem(GOTArmorMaterials.of("white_walkers"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> YI_TI_BOMBARDIER_BOOTS = ITEMS.register("yi_ti_bombardier_boots", () -> new ArmorItem(GOTArmorMaterials.of("yi_ti_bombardier"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> YI_TI_BOMBARDIER_CHESTPLATE = ITEMS.register("yi_ti_bombardier_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("yi_ti_bombardier"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> YI_TI_BOMBARDIER_HELMET = ITEMS.register("yi_ti_bombardier_helmet", () -> new ArmorItem(GOTArmorMaterials.of("yi_ti_bombardier"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> YI_TI_BOMBARDIER_LEGGINGS = ITEMS.register("yi_ti_bombardier_leggings", () -> new ArmorItem(GOTArmorMaterials.of("yi_ti_bombardier"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> YI_TI_BOOTS = ITEMS.register("yi_ti_boots", () -> new ArmorItem(GOTArmorMaterials.of("yi_ti"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> YI_TI_CHESTPLATE = ITEMS.register("yi_ti_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("yi_ti"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> YI_TI_HELMET = ITEMS.register("yi_ti_helmet", () -> new ArmorItem(GOTArmorMaterials.of("yi_ti"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> YI_TI_HELMET_CAPTAIN = ITEMS.register("yi_ti_helmet_captain", () -> new ArmorItem(GOTArmorMaterials.of("yi_ti"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> YI_TI_LEGGINGS = ITEMS.register("yi_ti_leggings", () -> new ArmorItem(GOTArmorMaterials.of("yi_ti"), ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> YI_TI_SAMURAI_BOOTS = ITEMS.register("yi_ti_samurai_boots", () -> new ArmorItem(GOTArmorMaterials.of("yi_ti_samurai"), ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> YI_TI_SAMURAI_CHESTPLATE = ITEMS.register("yi_ti_samurai_chestplate", () -> new ArmorItem(GOTArmorMaterials.of("yi_ti_samurai"), ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> YI_TI_SAMURAI_HELMET = ITEMS.register("yi_ti_samurai_helmet", () -> new ArmorItem(GOTArmorMaterials.of("yi_ti_samurai"), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> YI_TI_SAMURAI_LEGGINGS = ITEMS.register("yi_ti_samurai_leggings", () -> new ArmorItem(GOTArmorMaterials.of("yi_ti_samurai"), ArmorItem.Type.LEGGINGS, new Item.Properties()));

    /**
     * Named story artifacts and character-specific equipment. These are kept out of the
     * general equipment tab and presented in the dedicated Lore tab.
     */
    public static final List<RegistryObject<? extends Item>> LORE_ITEMS = List.of(
            ARDRIAN_CELTIGAR_AXE,
            BANE,
            BERIC_DONDARRION_SWORD,
            BLACKFYRE,
            BRIGHTROAR,
            CUTWAVE,
            DARK_SISTER,
            DARKSTAR,
            DAWN,
            FIN,
            GREGOR_CLEGANE_SWORD,
            HEARTEATER,
            HEARTSBANE,
            HONOR,
            ICE,
            INDOMITABLE,
            JOFFREY_BARATHEON_CROSSBOW,
            JUST_MAID,
            LADY_FORLORN,
            LAMENTATION,
            LIGHTBRINGER,
            LONGCLAW,
            NEEDLE,
            NIGHT_KING_SWORD,
            NIGHTFALL,
            OATHKEEPER,
            ORPHAN_MAKER,
            PETYR_BAELISH_DAGGER,
            RED_RAIN,
            REMINDER,
            ROBERT_BARATHEON_HAMMER,
            SANDOR_CLEGANE_SWORD,
            SUNSPEAR,
            TIDEWINGS,
            TRUTH,
            VIGILANCE,
            WIDOW_WAIL,
            HAND_GOLD,
            HAND_SILVER,
            MONFORD_VELARYON_BROOCH,
            PETYR_BAELISH_BROOCH,
            ROBERT_BARATHEON_HELMET,
            SANDOR_CLEGANE_HELMET,
            TYRION_LANNISTER_BROOCH
    );

    public static final List<RegistryObject<? extends Item>> SHIELDS = List.of(
            NORTH_SHIELD,
            NORTHGUARD_SHIELD,
            RIVERLANDS_SHIELD,
            ARRYN_SHIELD,
            ARRYNGUARD_SHIELD,
            HILLMEN_SHIELD,
            IRONBORN_SHIELD,
            WESTERLANDS_SHIELD,
            WESTERLANDSGUARD_SHIELD,
            DRAGONSTONE_SHIELD,
            CROWNLANDS_SHIELD,
            STORMLANDS_SHIELD,
            REACH_SHIELD,
            REACHGUARD_SHIELD,
            DORNE_SHIELD,
            VOLANTIS_SHIELD,
            PENTOS_SHIELD,
            NORVOS_SHIELD,
            BRAAVOS_SHIELD,
            TYROSH_SHIELD,
            LORATH_SHIELD,
            QOHOR_SHIELD,
            LYS_SHIELD,
            MYR_SHIELD,
            QARTH_SHIELD,
            GHISCAR_SHIELD,
            UNSULLIED_SHIELD,
            YI_TI_SHIELD,
            YI_TI_BOMBARDIER_SHIELD,
            YI_TI_SAMURAI_SHIELD,
            ASSHAI_SHIELD,
            SUMMER_SHIELD,
            SOTHORYOS_SHIELD,
            GOLDEN_COMPANY_SHIELD,
            TARGARYEN_SHIELD,
            ALCOHOLIC_SHIELD,
            ACHIEVEMENT_BRONZE_SHIELD,
            ACHIEVEMENT_SILVER_SHIELD,
            ACHIEVEMENT_GOLD_SHIELD,
            ACHIEVEMENT_VALYRIAN_SHIELD
    );

    public static final List<RegistryObject<? extends Item>> ALL = List.of(
            WESTEROS_SWORD, WESTEROS_SPEAR, WESTEROS_HAMMER,
            ESSOS_SWORD, ESSOS_SPEAR, ESSOS_HAMMER, ESSOS_POLEARM,
            ALLOY_STEEL_HALBERD,
            ARMOR_STAND,
            COMMAND_HORN,
            DART,
            DART_POISONED,
            HORSE_ARMOR_DIAMOND,
            HORSE_ARMOR_GOLD,
            HORSE_ARMOR_IRON,
            RHINO_ARMOR,
            ROBES_HELMET,
            SLING,
            VALYRIAN_HORSE_ARMOR,
            WARHORN,
            ALLOY_STEEL_AXE,
            ALLOY_STEEL_HOE,
            ALLOY_STEEL_PICKAXE,
            ALLOY_STEEL_SHOVEL,
            BRONZE_AXE,
            BRONZE_HOE,
            BRONZE_PICKAXE,
            BRONZE_SHOVEL,
            COPPER_AXE,
            COPPER_HOE,
            COPPER_PICKAXE,
            COPPER_SHOVEL,
            OBSIDIAN_AXE,
            OBSIDIAN_HOE,
            OBSIDIAN_PICKAXE,
            OBSIDIAN_SHOVEL,
            VALYRIAN_AXE,
            VALYRIAN_HOE,
            VALYRIAN_PICKAXE,
            VALYRIAN_SHOVEL,
            BRANDING_IRON,
            CHISEL,
            VALYRIAN_CHISEL,
            ALLOY_STEEL_BATTLEAXE,
            ALLOY_STEEL_CROSSBOW,
            ALLOY_STEEL_DAGGER_POISONED,
            ALLOY_STEEL_HAMMER,
            ALLOY_STEEL_MATTOCK,
            ALLOY_STEEL_PIKE,
            ALLOY_STEEL_SCIMITAR,
            ALLOY_STEEL_SPEAR,
            ALLOY_STEEL_SWORD,
            ALLOY_STEEL_THROWING_AXE,
            ARDRIAN_CELTIGAR_AXE,
            ARROW_FIRE,
            ARROW_POISONED,
            ASSHAI_ARCHMAG_STAFF,
            ASSHAI_SHADOWBINDER_STAFF,
            BANE,
            BERIC_DONDARRION_SWORD,
            BLACK_ARAKH,
            BLACKFYRE,
            BLACKSMITH_HAMMER,
            BRIGHTROAR,
            BRONZE_BATTLEAXE,
            BRONZE_CROSSBOW,
            BRONZE_DAGGER_POISONED,
            BRONZE_HAMMER,
            BRONZE_MATTOCK,
            BRONZE_PIKE,
            BRONZE_SCIMITAR,
            BRONZE_SPEAR,
            BRONZE_SWORD,
            BRONZE_THROWING_AXE,
            CLUB,
            COMMAND_SWORD,
            COPPER_BATTLEAXE,
            COPPER_CROSSBOW,
            COPPER_DAGGER,
            COPPER_DAGGER_POISONED,
            COPPER_GREATSWORD,
            COPPER_HAMMER,
            COPPER_LONGSWORD,
            COPPER_MATTOCK,
            COPPER_PIKE,
            COPPER_SCIMITAR,
            COPPER_SPEAR,
            COPPER_SWORD,
            COPPER_THROWING_AXE,
            CROSSBOW_BOLT,
            CROSSBOW_BOLT_POISONED,
            CROWBAR,
            CUTWAVE,
            DARK_SISTER,
            DARKSTAR,
            DAWN,
            DIAMOND_MATTOCK,
            FIN,
            FIRE_POT,
            GOLD_BATTLEAXE,
            GOLD_CROSSBOW,
            GOLD_DAGGER,
            GOLD_DAGGER_POISONED,
            GOLD_GREATSWORD,
            GOLD_HAMMER,
            GOLD_LONGSWORD,
            GOLD_MATTOCK,
            GOLD_PIKE,
            GOLD_SCIMITAR,
            GOLD_SPEAR,
            GOLD_THROWING_AXE,
            GREGOR_CLEGANE_SWORD,
            HARPOON,
            HEARTEATER,
            HEARTSBANE,
            HONOR,
            ICE,
            ICE_HEAVY_SWORD,
            ICE_SPEAR,
            ICE_SWORD,
            INDOMITABLE,
            IRON_BATTLEAXE,
            IRON_CROSSBOW,
            IRON_DAGGER,
            IRON_DAGGER_POISONED,
            IRON_GREATSWORD,
            IRON_HAMMER,
            IRON_LONGSWORD,
            IRON_MATTOCK,
            IRON_PIKE,
            IRON_SCIMITAR,
            IRON_SPEAR,
            IRON_THROWING_AXE,
            JOFFREY_BARATHEON_CROSSBOW,
            JUST_MAID,
            KATANA,
            LADY_FORLORN,
            LAMENTATION,
            LIGHTBRINGER,
            LINGERING_POTION,
            LONGBOW,
            LONGCLAW,
            NEEDLE,
            NIGHT_KING_SWORD,
            NIGHTFALL,
            OATHKEEPER,
            OBSIDIAN_BATTLEAXE,
            OBSIDIAN_DAGGER,
            OBSIDIAN_DAGGER_POISONED,
            OBSIDIAN_HAMMER,
            OBSIDIAN_MATTOCK,
            OBSIDIAN_PIKE,
            OBSIDIAN_SCIMITAR,
            OBSIDIAN_SPEAR,
            OBSIDIAN_SWORD,
            OBSIDIAN_THROWING_AXE,
            ORPHAN_MAKER,
            PETYR_BAELISH_DAGGER,
            PRUNER,
            RED_RAIN,
            REMINDER,
            ROBERT_BARATHEON_HAMMER,
            ROLLING_PIN,
            SANDOR_CLEGANE_SWORD,
            SARBACANE,
            SKULL_STAFF,
            STONE_BATTLEAXE,
            STONE_DAGGER,
            STONE_DAGGER_POISONED,
            STONE_HAMMER,
            STONE_MATTOCK,
            STONE_PIKE,
            STONE_SCIMITAR,
            STONE_SPEAR,
            STONE_THROWING_AXE,
            SUNSPEAR,
            TERMITE,
            TIDEWINGS,
            TRIDENT,
            TRUTH,
            VALYRIAN_BATTLEAXE,
            VALYRIAN_CROSSBOW,
            VALYRIAN_DAGGER,
            VALYRIAN_DAGGER_POISONED,
            VALYRIAN_GREATSWORD,
            VALYRIAN_HAMMER,
            VALYRIAN_LONGSWORD,
            VALYRIAN_MATTOCK,
            VALYRIAN_PIKE,
            VALYRIAN_SCIMITAR,
            VALYRIAN_SPEAR,
            VALYRIAN_SWORD,
            VALYRIAN_THROWING_AXE,
            VIGILANCE,
            WALKING_STICK,
            WIDOW_WAIL,
            WOOD_BATTLEAXE,
            WOOD_DAGGER,
            WOOD_DAGGER_POISONED,
            WOOD_HAMMER,
            WOOD_MATTOCK,
            WOOD_PIKE,
            WOOD_SCIMITAR,
            WOOD_SPEAR,
            WOOD_THROWING_AXE,
            ALLOY_STEEL_BOOTS,
            ALLOY_STEEL_CHAINMAIL_BOOTS,
            ALLOY_STEEL_CHAINMAIL_CHESTPLATE,
            ALLOY_STEEL_CHAINMAIL_HELMET,
            ALLOY_STEEL_CHAINMAIL_LEGGINGS,
            ALLOY_STEEL_CHESTPLATE,
            ALLOY_STEEL_HELMET,
            ALLOY_STEEL_LEGGINGS,
            ANONYMOUS_MASK,
            ARRYN_BOOTS,
            ARRYN_CHESTPLATE,
            ARRYN_HELMET,
            ARRYN_LEGGINGS,
            ARRYNGUARD_BOOTS,
            ARRYNGUARD_CHESTPLATE,
            ARRYNGUARD_HELMET,
            ARRYNGUARD_LEGGINGS,
            ASSHAI_BOOTS,
            ASSHAI_CHESTPLATE,
            ASSHAI_HELMET,
            ASSHAI_LEGGINGS,
            ASSHAI_MASK,
            BONE_BOOTS,
            BONE_CHESTPLATE,
            BONE_HELMET,
            BONE_LEGGINGS,
            BRAAVOS_BOOTS,
            BRAAVOS_CHESTPLATE,
            BRAAVOS_HELMET,
            BRAAVOS_LEGGINGS,
            BRONZE_BOOTS,
            BRONZE_CHAINMAIL_BOOTS,
            BRONZE_CHAINMAIL_CHESTPLATE,
            BRONZE_CHAINMAIL_HELMET,
            BRONZE_CHAINMAIL_LEGGINGS,
            BRONZE_CHESTPLATE,
            BRONZE_HELMET,
            BRONZE_LEGGINGS,
            COPPER_BOOTS,
            COPPER_CHAINMAIL_BOOTS,
            COPPER_CHAINMAIL_CHESTPLATE,
            COPPER_CHAINMAIL_HELMET,
            COPPER_CHAINMAIL_LEGGINGS,
            COPPER_CHESTPLATE,
            COPPER_HELMET,
            COPPER_LEGGINGS,
            CROWNLANDS_BOOTS,
            CROWNLANDS_CHESTPLATE,
            CROWNLANDS_HELMET,
            CROWNLANDS_LEGGINGS,
            DORNE_BOOTS,
            DORNE_CHESTPLATE,
            DORNE_HELMET,
            DORNE_LEGGINGS,
            DOTHRAKI_BOOTS,
            DOTHRAKI_CHESTPLATE,
            DOTHRAKI_HELMET,
            DOTHRAKI_LEGGINGS,
            DRAGONSTONE_BOOTS,
            DRAGONSTONE_CHESTPLATE,
            DRAGONSTONE_HELMET,
            DRAGONSTONE_LEGGINGS,
            FUR_BOOTS,
            FUR_CHESTPLATE,
            FUR_HELMET,
            FUR_LEGGINGS,
            GHISCAR_BOOTS,
            GHISCAR_CHESTPLATE,
            GHISCAR_HELMET,
            GHISCAR_LEGGINGS,
            GIFT_BOOTS,
            GIFT_CHESTPLATE,
            GIFT_HELMET,
            GIFT_LEGGINGS,
            GOLD_CHAINMAIL_BOOTS,
            GOLD_CHAINMAIL_CHESTPLATE,
            GOLD_CHAINMAIL_HELMET,
            GOLD_CHAINMAIL_LEGGINGS,
            GOLDEN_COMPANY_BOOTS,
            GOLDEN_COMPANY_CHESTPLATE,
            GOLDEN_COMPANY_HELMET,
            GOLDEN_COMPANY_LEGGINGS,
            HAND_GOLD,
            HAND_SILVER,
            HARPY,
            HILLMEN_BOOTS,
            HILLMEN_CHESTPLATE,
            HILLMEN_HELMET,
            HILLMEN_LEGGINGS,
            IBBEN_BOOTS,
            IBBEN_CHESTPLATE,
            IBBEN_LEGGINGS,
            IRONBORN_BOOTS,
            IRONBORN_CHESTPLATE,
            IRONBORN_HELMET,
            IRONBORN_LEGGINGS,
            JOGOS_NHAI_BOOTS,
            JOGOS_NHAI_CHESTPLATE,
            JOGOS_NHAI_HELMET,
            JOGOS_NHAI_LEGGINGS,
            KINGSGUARD_BOOTS,
            KINGSGUARD_CHESTPLATE,
            KINGSGUARD_HELMET,
            KINGSGUARD_LEGGINGS,
            LHAZAR_BOOTS,
            LHAZAR_CHESTPLATE,
            LHAZAR_HELMET,
            LHAZAR_LEGGINGS,
            LION_BOOTS,
            LION_CHESTPLATE,
            LION_HELMET,
            LION_LEGGINGS,
            LORATH_BOOTS,
            LORATH_CHESTPLATE,
            LORATH_HELMET,
            LORATH_LEGGINGS,
            LYS_BOOTS,
            LYS_CHESTPLATE,
            LYS_HELMET,
            LYS_LEGGINGS,
            MONFORD_VELARYON_BROOCH,
            MOSSOVY_BOOTS,
            MOSSOVY_CHESTPLATE,
            MOSSOVY_LEGGINGS,
            MYR_BOOTS,
            MYR_CHESTPLATE,
            MYR_HELMET,
            MYR_LEGGINGS,
            NORTH_BOOTS,
            NORTH_CHESTPLATE,
            NORTH_HELMET,
            NORTH_LEGGINGS,
            NORTHGUARD_BOOTS,
            NORTHGUARD_CHESTPLATE,
            NORTHGUARD_HELMET,
            NORTHGUARD_LEGGINGS,
            NORVOS_BOOTS,
            NORVOS_CHESTPLATE,
            NORVOS_HELMET,
            NORVOS_LEGGINGS,
            ORYX_BOOTS,
            ORYX_CHESTPLATE,
            ORYX_HELMET,
            ORYX_LEGGINGS,
            PENTOS_BOOTS,
            PENTOS_CHESTPLATE,
            PENTOS_HELMET,
            PENTOS_LEGGINGS,
            PETYR_BAELISH_BROOCH,
            QARTH_BOOTS,
            QARTH_CHESTPLATE,
            QARTH_HELMET,
            QARTH_LEGGINGS,
            QOHOR_BOOTS,
            QOHOR_CHESTPLATE,
            QOHOR_HELMET,
            QOHOR_LEGGINGS,
            REACH_BOOTS,
            REACH_CHESTPLATE,
            REACH_HELMET,
            REACH_LEGGINGS,
            REACHGUARD_BOOTS,
            REACHGUARD_CHESTPLATE,
            REACHGUARD_HELMET,
            REACHGUARD_LEGGINGS,
            RIVERLANDS_BOOTS,
            RIVERLANDS_CHESTPLATE,
            RIVERLANDS_HELMET,
            RIVERLANDS_LEGGINGS,
            ROBERT_BARATHEON_HELMET,
            ROBES_BOOTS,
            ROBES_CHESTPLATE,
            ROBES_LEGGINGS,
            SANDOR_CLEGANE_HELMET,
            SOTHORYOS_BOOTS,
            SOTHORYOS_CHESTPLATE,
            SOTHORYOS_HELMET,
            SOTHORYOS_HELMET_CHIEFTAIN,
            SOTHORYOS_LEGGINGS,
            STORMLANDS_BOOTS,
            STORMLANDS_CHESTPLATE,
            STORMLANDS_HELMET,
            STORMLANDS_LEGGINGS,
            SUMMER_BOOTS,
            SUMMER_CHESTPLATE,
            SUMMER_HELMET,
            SUMMER_LEGGINGS,
            TARGARYEN_BOOTS,
            TARGARYEN_CHESTPLATE,
            TARGARYEN_HELMET,
            TARGARYEN_LEGGINGS,
            TYRION_LANNISTER_BROOCH,
            TYROSH_BOOTS,
            TYROSH_CHESTPLATE,
            TYROSH_HELMET,
            TYROSH_LEGGINGS,
            UNSULLIED_BOOTS,
            UNSULLIED_CHESTPLATE,
            UNSULLIED_HELMET,
            UNSULLIED_LEGGINGS,
            VALYRIAN_BOOTS,
            VALYRIAN_CHAINMAIL_BOOTS,
            VALYRIAN_CHAINMAIL_CHESTPLATE,
            VALYRIAN_CHAINMAIL_HELMET,
            VALYRIAN_CHAINMAIL_LEGGINGS,
            VALYRIAN_CHESTPLATE,
            VALYRIAN_HELMET,
            VALYRIAN_LEGGINGS,
            VOLANTIS_BOOTS,
            VOLANTIS_CHESTPLATE,
            VOLANTIS_HELMET,
            VOLANTIS_LEGGINGS,
            WESTERLANDS_BOOTS,
            WESTERLANDS_CHESTPLATE,
            WESTERLANDS_HELMET,
            WESTERLANDS_LEGGINGS,
            WESTERLANDSGUARD_BOOTS,
            WESTERLANDSGUARD_CHESTPLATE,
            WESTERLANDSGUARD_HELMET,
            WESTERLANDSGUARD_LEGGINGS,
            WHITE_WALKERS_BOOTS,
            WHITE_WALKERS_CHESTPLATE,
            WHITE_WALKERS_LEGGINGS,
            YI_TI_BOMBARDIER_BOOTS,
            YI_TI_BOMBARDIER_CHESTPLATE,
            YI_TI_BOMBARDIER_HELMET,
            YI_TI_BOMBARDIER_LEGGINGS,
            YI_TI_BOOTS,
            YI_TI_CHESTPLATE,
            YI_TI_HELMET,
            YI_TI_HELMET_CAPTAIN,
            YI_TI_LEGGINGS,
            YI_TI_SAMURAI_BOOTS,
            YI_TI_SAMURAI_CHESTPLATE,
            YI_TI_SAMURAI_HELMET,
            YI_TI_SAMURAI_LEGGINGS
    );

    private GOTEquipment() {}

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }
}
