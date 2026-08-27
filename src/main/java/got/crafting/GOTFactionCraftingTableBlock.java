package got.crafting;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Legacy GOT faction crafting table.
 *
 * The old mod gave each faction table its own recipe list.  The common iron
 * armour/tool/weapon silhouettes are intentionally reused, but the result is
 * swapped to the faction's registered equipment.  This keeps vanilla recipe
 * book/grid behaviour while restoring the table-sensitive output.
 */
public final class GOTFactionCraftingTableBlock extends CraftingTableBlock {
    private final String equipmentPrefix;

    public GOTFactionCraftingTableBlock(String equipmentPrefix, Properties properties) {
        super(properties);
        this.equipmentPrefix = equipmentPrefix;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        MenuProvider provider = getMenuProvider(state, level, pos);
        if (provider != null) {
            player.openMenu(provider);
            player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return GOTFactionCraftingTableBlock.this.getName();
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
                return new FactionMenu(id, inventory, ContainerLevelAccess.create(level, pos), equipmentPrefix, GOTFactionCraftingTableBlock.this);
            }
        };
    }

    private static final class FactionMenu extends CraftingMenu {
        private static final Map<Item, String> IRON_RESULT_SUFFIX = Map.ofEntries(
                Map.entry(Items.IRON_HELMET, "helmet"),
                Map.entry(Items.IRON_CHESTPLATE, "chestplate"),
                Map.entry(Items.IRON_LEGGINGS, "leggings"),
                Map.entry(Items.IRON_BOOTS, "boots")
        );

        private final String equipmentPrefix;
        private final ContainerLevelAccess access;
        private final Block expectedTable;
        private static final Map<String, String> ELITE_ARMOR_PREFIX = Map.ofEntries(
                Map.entry("north", "northguard"),
                Map.entry("arryn", "arrynguard"),
                Map.entry("crownlands", "kingsguard"),
                Map.entry("reach", "reachguard"),
                Map.entry("westerlands", "westerlandsguard"),
                Map.entry("qohor", "unsullied"),
                Map.entry("ghiscar", "unsullied")
        );

        private static final java.util.Set<String> WESTEROS_TABLES = java.util.Set.of(
                "north", "arryn", "crownlands", "dorne", "dragonstone", "gift",
                "hillmen", "ironborn", "reach", "riverlands", "stormlands",
                "westerlands", "fur"
        );

        private static final java.util.Set<String> ESSOS_TABLES = java.util.Set.of(
                "asshai", "braavos", "dothraki", "ghiscar", "ibben", "jogos_nhai",
                "lhazar", "lorath", "lys", "mossovy", "myr", "norvos", "pentos",
                "qarth", "qohor", "tyrosh", "volantis", "yi_ti"
        );


        private static final Map<String, String> BASE_SHIELD = Map.ofEntries(
                Map.entry("north", "north"), Map.entry("riverlands", "riverlands"),
                Map.entry("arryn", "arryn"), Map.entry("hillmen", "hillmen"),
                Map.entry("ironborn", "ironborn"), Map.entry("westerlands", "westerlands"),
                Map.entry("dragonstone", "dragonstone"), Map.entry("crownlands", "crownlands"),
                Map.entry("stormlands", "stormlands"), Map.entry("reach", "reach"),
                Map.entry("dorne", "dorne"), Map.entry("volantis", "volantis"),
                Map.entry("pentos", "pentos"), Map.entry("norvos", "norvos"),
                Map.entry("braavos", "braavos"), Map.entry("tyrosh", "tyrosh"),
                Map.entry("lorath", "lorath"), Map.entry("qohor", "qohor"),
                Map.entry("lys", "lys"), Map.entry("myr", "myr"),
                Map.entry("qarth", "qarth"), Map.entry("ghiscar", "ghiscar"),
                Map.entry("yi_ti", "yi_ti"), Map.entry("asshai", "asshai"),
                Map.entry("summer", "summer"), Map.entry("sothoryos", "sothoryos")
        );
        private static final Map<String, String> ALLOY_SHIELD = Map.ofEntries(
                Map.entry("north", "northguard"),
                Map.entry("arryn", "arrynguard"),
                Map.entry("reach", "reachguard"),
                Map.entry("westerlands", "westerlandsguard"),
                Map.entry("ghiscar", "unsullied"),
                Map.entry("qohor", "unsullied"),
                Map.entry("yi_ti", "yi_ti_samurai")
        );

        private boolean replacingResult;

        private FactionMenu(int id, Inventory inventory, ContainerLevelAccess access, String equipmentPrefix, Block expectedTable) {
            super(id, inventory, access);
            this.access = access;
            this.equipmentPrefix = equipmentPrefix;
            this.expectedTable = expectedTable;
        }

        @Override
        public boolean stillValid(Player player) {
            return access.evaluate((level, pos) ->
                    level.getBlockState(pos).getBlock() == expectedTable
                            && player.distanceToSqr(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D,
                    true);
        }

        @Override
        public void slotsChanged(net.minecraft.world.Container container) {
            if (replacingResult) return;

            MaterialMirror mirror = null;
            replacingResult = true;
            try {
                mirror = prepareLegacyArmorMaterialMirror();
                super.slotsChanged(container);
            } finally {
                if (mirror != null) restoreMaterialMirror(mirror);
                replacingResult = false;
            }

            ItemStack vanillaResult = getSlot(0).getItem();

            ItemStack factionShield = findFactionShieldRecipe();
            if (!factionShield.isEmpty()) {
                replacingResult = true;
                getSlot(0).set(factionShield);
                broadcastChanges();
                replacingResult = false;
                return;
            }
            // A faction table must never fall through to the generic vanilla shield.
            // Tables without a legacy shield simply do not offer a shield recipe.
            if (vanillaResult.is(Items.SHIELD)) {
                replacingResult = true;
                getSlot(0).set(ItemStack.EMPTY);
                broadcastChanges();
                replacingResult = false;
                return;
            }

            ItemStack regionalSword = findRegionalSwordRecipe();
            if (!regionalSword.isEmpty()) {
                replacingResult = true;
                getSlot(0).set(regionalSword);
                broadcastChanges();
                replacingResult = false;
                return;
            }

            ItemStack materialWeapon = findMaterialWeaponRecipe();
            if (!materialWeapon.isEmpty()) {
                replacingResult = true;
                getSlot(0).set(materialWeapon);
                broadcastChanges();
                replacingResult = false;
                return;
            }

            ItemStack legacySpecial = findLegacySpecialRecipe();
            if (!legacySpecial.isEmpty()) {
                replacingResult = true;
                getSlot(0).set(legacySpecial);
                broadcastChanges();
                replacingResult = false;
                return;
            }
            if (vanillaResult.isEmpty()) return;

            ItemStack regionalWeapon = regionalWeaponResult(vanillaResult);
            if (!regionalWeapon.isEmpty()) {
                replacingResult = true;
                getSlot(0).set(regionalWeapon);
                broadcastChanges();
                replacingResult = false;
                return;
            }

            String suffix = IRON_RESULT_SUFFIX.get(vanillaResult.getItem());
            if (suffix == null) return;

            String outputPrefix = equipmentPrefix;
            if (mirror != null) outputPrefix = mirror.outputPrefix;

            // Material-locked tables must not convert ordinary iron into their
            // regional armour. Legacy Wildling armour used fur and Dothraki
            // armour used dried reeds.
            if (("fur".equals(equipmentPrefix) || "dothraki".equals(equipmentPrefix)) && mirror == null) return;

            ResourceLocation id = new ResourceLocation("got", outputPrefix + "_" + suffix);
            Item factionItem = ForgeRegistries.ITEMS.getValue(id);
            if (factionItem == null || factionItem == Items.AIR) return;

            replacingResult = true;
            ItemStack replacement = new ItemStack(factionItem, vanillaResult.getCount());
            if (vanillaResult.hasTag()) replacement.setTag(vanillaResult.getTag().copy());
            getSlot(0).set(replacement);
            broadcastChanges();
            replacingResult = false;
        }



        private ItemStack findFactionShieldRecipe() {
            // All GOT shields use the vanilla shield silhouette:
            // X M X
            // X X X
            // . X .
            //
            // Normal faction shields: X = any planks, M = iron ingot.
            // Elite faction shields:  X = any planks, M = alloy steel ingot.
            // Alcoholic shield:        X = any planks, M = any alcoholic GOT drink in any vessel.
            // Targaryen shield:        X = any planks, M = Valyrian steel ingot.
            // Golden Company shield:  X = gold ingots, M = alloy steel ingot.
            int[] materialSlots = {0, 2, 3, 4, 5, 7};
            int[] emptySlots = {6, 8};
            for (int i : emptySlots) {
                if (!getSlot(i + 1).getItem().isEmpty()) return ItemStack.EMPTY;
            }

            ItemStack center = getSlot(2).getItem(); // top-middle crafting slot
            Item alloy = ForgeRegistries.ITEMS.getValue(new ResourceLocation("got", "alloy_steel_ingot"));
            Item valyrian = ForgeRegistries.ITEMS.getValue(new ResourceLocation("got", "valyrian_steel_ingot"));

            // Golden Company is the one shield that replaces the wooden body too:
            // six gold ingots around a central Alloy Steel ingot.
            if (alloy != null && alloy != Items.AIR && center.is(alloy)
                    && allSlotsAre(materialSlots, Items.GOLD_INGOT)) {
                return stack("got:golden_company_shield");
            }

            // Every other special shield keeps the ordinary wooden shield body.
            if (!allSlotsArePlanks(materialSlots)) return ItemStack.EMPTY;

            // Any alcoholic beverage works regardless of the vessel stored in its NBT.
            if (isAlcoholicDrink(center)) {
                return stack("got:alcoholic_shield");
            }

            if (valyrian != null && valyrian != Items.AIR && center.is(valyrian)) {
                return stack("got:targaryen_shield");
            }

            String shieldName;
            if (center.is(Items.IRON_INGOT)) {
                shieldName = BASE_SHIELD.get(equipmentPrefix);
            } else {
                if (alloy == null || alloy == Items.AIR || !center.is(alloy)) return ItemStack.EMPTY;
                shieldName = ALLOY_SHIELD.get(equipmentPrefix);
            }
            if (shieldName == null) return ItemStack.EMPTY;
            return stack("got:" + shieldName + "_shield");
        }

        private boolean allSlotsArePlanks(int[] slots) {
            for (int i : slots) {
                ItemStack st = getSlot(i + 1).getItem();
                if (st.isEmpty() || !st.is(ItemTags.PLANKS)) return false;
            }
            return true;
        }

        private boolean allSlotsAre(int[] slots, Item item) {
            for (int i : slots) {
                ItemStack st = getSlot(i + 1).getItem();
                if (st.isEmpty() || !st.is(item)) return false;
            }
            return true;
        }

        private static boolean isAlcoholicDrink(ItemStack stack) {
            return stack.getItem() instanceof got.GOTDrinkItem drink
                    && drink.definition().alcoholicity() > 0.0F;
        }

        /**
         * 1.0 regional sword recipe.
         *
         * Alloy Steel Ingot
         * Iron Ingot
         * Stick
         *
         * Westeros faction tables -> Westeros Sword
         * Essos faction tables -> Essos Sword
         *
         * This deliberately differs from the pure-iron Sword/Scimitar recipe so
         * the regional Diamond-tier swords cannot overlap the GOT Iron family.
         */
        private ItemStack findRegionalSwordRecipe() {
            boolean westeros = WESTEROS_TABLES.contains(equipmentPrefix);
            boolean essos = ESSOS_TABLES.contains(equipmentPrefix);
            if (!westeros && !essos) return ItemStack.EMPTY;

            if (!grid("A  ", "I  ", "S  ",
                    Map.of('A', "got:alloy_steel_ingot",
                           'I', "minecraft:iron_ingot",
                           'S', "minecraft:stick"), false)) {
                return ItemStack.EMPTY;
            }

            return stack(westeros ? "got:westeros_sword" : "got:essos_sword");
        }

        private ItemStack regionalWeaponResult(ItemStack vanillaResult) {
            boolean westeros = WESTEROS_TABLES.contains(equipmentPrefix);
            boolean essos = ESSOS_TABLES.contains(equipmentPrefix);
            String id = null;

            // Faction tables share regional weapon families. Their armor is still
            // faction-specific, but the common iron weapon/tool silhouettes map
            // to the Westeros or Essos weapon set.
            if (vanillaResult.is(Items.IRON_SWORD)) {
                // The base iron sword recipe is shared regionally: Westeros keeps
                // the normal iron sword, while Essos produces the GOT iron scimitar.
                if (westeros) return vanillaResult.copy();
                if (essos) id = "iron_scimitar";
                else return ItemStack.EMPTY;
            } else if (vanillaResult.is(Items.IRON_SHOVEL)) {
                id = westeros ? "westeros_spear" : "essos_spear";
            } else if (vanillaResult.is(Items.IRON_AXE)) {
                id = westeros ? "westeros_hammer" : "essos_hammer";
            } else if (!westeros && vanillaResult.is(Items.IRON_PICKAXE)) {
                id = "essos_polearm";
            }

            if (id == null) return ItemStack.EMPTY;
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("got", id));
            if (item == null || item == Items.AIR) return ItemStack.EMPTY;
            return new ItemStack(item, vanillaResult.getCount());
        }

        /**
         * 1.0 weapon recipe pass: GOT Iron and Alloy Steel weapon families.
         * These recipes intentionally live on GOT faction tables rather than the
         * generic vanilla crafting table. Sword/scimitar output is regional:
         * Westeros -> sword, Essos -> scimitar.
         */
        private ItemStack findMaterialWeaponRecipe() {
            boolean westeros = WESTEROS_TABLES.contains(equipmentPrefix);
            boolean essos = ESSOS_TABLES.contains(equipmentPrefix);

            ItemStack iron = findMaterialWeaponRecipeFor(
                    "minecraft:iron_ingot", "iron", westeros, essos, true);
            if (!iron.isEmpty()) return iron;

            ItemStack alloy = findMaterialWeaponRecipeFor(
                    "got:alloy_steel_ingot", "alloy_steel", westeros, essos, false);
            if (!alloy.isEmpty()) return alloy;

            // Pass 5: extend the established silhouettes to the remaining
            // material families. Scimitars are deliberately excluded.
            ItemStack r;
            r=extendedMaterial("got:bronze_ingot","bronze","got:bronze_sword","got:bronze_pickaxe","got:bronze_shovel",essos); if(!r.isEmpty()) return r;
            r=extendedMaterial("minecraft:copper_ingot","copper","got:copper_sword","got:copper_pickaxe","got:copper_shovel",essos); if(!r.isEmpty()) return r;
            r=extendedMaterial("minecraft:gold_ingot","gold","minecraft:golden_sword","minecraft:golden_pickaxe","minecraft:golden_shovel",essos); if(!r.isEmpty()) return r;
            r=extendedMaterial("got:obsidian_shard","obsidian","got:obsidian_sword","got:obsidian_pickaxe","got:obsidian_shovel",essos); if(!r.isEmpty()) return r;
            r=extendedMaterial("minecraft:cobblestone","stone","minecraft:stone_sword","minecraft:stone_pickaxe","minecraft:stone_shovel",essos); if(!r.isEmpty()) return r;
            r=extendedMaterial("#planks","wood","minecraft:wooden_sword","minecraft:wooden_pickaxe","minecraft:wooden_shovel",essos); if(!r.isEmpty()) return r;
            return extendedMaterial("got:valyrian_steel_ingot","valyrian","got:valyrian_sword","got:valyrian_pickaxe","got:valyrian_shovel",essos);
        }

        private ItemStack extendedMaterial(String mat,String tier,String sword,String pick,String shovel,boolean essos) {
            // Sword/Scimitar share the same recipe. Pass 6 adds the Scimitar
            // only to explicit Essos faction tables; other tables keep their own systems.
            if(mgrid("M  ","M  ","S  ",mat)) {
                if (essos) return registered("got:"+tier+"_scimitar");
                if (WESTEROS_TABLES.contains(equipmentPrefix)) return registered(sword);
                return ItemStack.EMPTY;
            }
            if(mgrid("MM ","MS "," S ",mat)) return registered("got:"+tier+"_axe");
            if(mgrid("MMM"," S "," S ",mat)) return registered(pick);
            if(mgrid(" M "," S "," S ",mat)) return registered(shovel);
            if(mgrid("MM "," S "," S ",mat)) return registered("got:"+tier+"_hoe");
            if(mgrid("SMS","THT"," S ",mat)) return registered("got:"+tier+"_crossbow");
            if(shapelessExact(pick,shovel)) return registered("got:"+tier+"_mattock");
            if(mgrid("MM ","MS ","   ",mat)) return registered("got:"+tier+"_throwing_axe");
            if(mgrid("MMM","MS "," S ",mat)) return registered("got:"+tier+"_battleaxe");
            if(mgrid("MMM","MSM"," S ",mat)) return registered("got:"+tier+"_hammer");
            if(mgrid("  M"," S ","S  ",mat)) return registered("got:"+tier+"_spear");
            if(mgrid("  M"," M ","S  ",mat)) return registered("got:"+tier+"_longsword");
            if(!mat.equals("#planks") && grid(" M "," M "," L ",Map.of('M',mat,'L',"got:"+tier+"_longsword"),false)) return registered("got:"+tier+"_greatsword");
            if(mgrid(" MM"," S ","S  ",mat)) return registered("got:"+tier+"_polearm");
            if(mgrid("  S"," S ","M  ",mat)) return registered("got:"+tier+"_pike");
            if(mgrid("   "," M "," S ",mat)) return registered("got:"+tier+"_dagger");
            if(!mat.equals("#planks") && grid(" P "," M "," S ",Map.of('P',"got:bottle_poison",'M',mat,'S',"minecraft:stick"),false)) return registered("got:"+tier+"_dagger_poisoned");
            if(shapelessExact("got:"+tier+"_dagger","got:bottle_poison")) return registered("got:"+tier+"_dagger_poisoned");
            return ItemStack.EMPTY;
        }

        private boolean mgrid(String a,String b,String c,String mat) {
            if(mat.equals("#planks")) {
                return grid(a.replace('M','P'),b.replace('M','P'),c.replace('M','P'),
                        Map.of('T',"minecraft:string",'H',"minecraft:tripwire_hook"),true);
            }
            return grid(a,b,c,Map.of('M',mat,'S',"minecraft:stick",'T',"minecraft:string",'H',"minecraft:tripwire_hook"),false);
        }

        private static ItemStack registered(String id) {
            Item i=ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
            return i==null||i==Items.AIR?ItemStack.EMPTY:new ItemStack(i);
        }

        private ItemStack findMaterialWeaponRecipeFor(String materialId, String tier, boolean westeros, boolean essos, boolean vanillaIronSword) {
            String sword = vanillaIronSword ? "minecraft:iron_sword" : "got:" + tier + "_sword";
            String scimitar = "got:" + tier + "_scimitar";
            String longsword = "got:" + tier + "_longsword";
            String dagger = "got:" + tier + "_dagger";
            String poisonedDagger = "got:" + tier + "_dagger_poisoned";

            // Sword / Scimitar: M / M / S
            if (grid("M  ", "M  ", "S  ", Map.of('M', materialId, 'S', "minecraft:stick"), false)) {
                if (westeros) return stack(sword);
                if (essos) return stack(scimitar);
                return ItemStack.EMPTY;
            }

            // Standard tools: same vanilla silhouettes for both GOT Iron and Alloy Steel.
            // Iron intentionally returns Minecraft's ordinary iron tools; Alloy Steel
            // returns the registered GOT Alloy Steel tool family.
            String axe = vanillaIronSword ? "minecraft:iron_axe" : "got:" + tier + "_axe";
            String pickaxe = vanillaIronSword ? "minecraft:iron_pickaxe" : "got:" + tier + "_pickaxe";
            String shovel = vanillaIronSword ? "minecraft:iron_shovel" : "got:" + tier + "_shovel";

            // Axe: MM. / MS. / .S. (grid matcher also accepts the horizontal mirror)
            if (grid("MM ", "MS ", " S ", Map.of('M', materialId, 'S', "minecraft:stick"), false))
                return stack(axe);

            // Pickaxe: MMM / .S. / .S.
            if (grid("MMM", " S ", " S ", Map.of('M', materialId, 'S', "minecraft:stick"), false))
                return stack(pickaxe);

            // Shovel: .M. / .S. / .S.
            if (grid(" M ", " S ", " S ", Map.of('M', materialId, 'S', "minecraft:stick"), false))
                return stack(shovel);

            // Hoe: standard vanilla silhouette, Alloy Steel only. Iron continues to use the vanilla iron hoe.
            if (!vanillaIronSword && grid("MM ", " S ", " S ", Map.of('M', materialId, 'S', "minecraft:stick"), false))
                return stack("got:alloy_steel_hoe");

            // Crossbow: vanilla crossbow silhouette, available at every GOT faction table.
            // The central top material selects Iron vs Alloy Steel.
            if (grid("SMS", "THT", " S ", Map.of(
                    'M', materialId,
                    'S', "minecraft:stick",
                    'T', "minecraft:string",
                    'H', "minecraft:tripwire_hook"), false))
                return stack("got:" + tier + "_crossbow");

            // Mattock: shapeless pickaxe + shovel of the exact same material tier.
            String mattockPickaxe = vanillaIronSword ? "minecraft:iron_pickaxe" : "got:alloy_steel_pickaxe";
            String mattockShovel = vanillaIronSword ? "minecraft:iron_shovel" : "got:alloy_steel_shovel";
            if (shapelessExact(mattockPickaxe, mattockShovel))
                return stack("got:" + tier + "_mattock");

            // Throwing Axe: MM. / MS. / ...
            if (grid("MM ", "MS ", "   ", Map.of('M', materialId, 'S', "minecraft:stick"), false))
                return stack("got:" + tier + "_throwing_axe");

            // Battle Axe: MMM / MS. / .S.
            if (grid("MMM", "MS ", " S ", Map.of('M', materialId, 'S', "minecraft:stick"), false))
                return stack("got:" + tier + "_battleaxe");

            // Hammer: MMM / MSM / .S.
            if (grid("MMM", "MSM", " S ", Map.of('M', materialId, 'S', "minecraft:stick"), false))
                return stack("got:" + tier + "_hammer");

            // Spear: ..M / .S. / S..
            if (grid("  M", " S ", "S  ", Map.of('M', materialId, 'S', "minecraft:stick"), false))
                return stack("got:" + tier + "_spear");

            // Longsword: ..M / .M. / S..
            if (grid("  M", " M ", "S  ", Map.of('M', materialId, 'S', "minecraft:stick"), false))
                return stack(longsword);

            // Greatsword: .M. / .M. / .L.
            if (grid(" M ", " M ", " L ", Map.of('M', materialId, 'L', longsword), false))
                return stack("got:" + tier + "_greatsword");

            // Polearm: .MM / .S. / S..
            if (grid(" MM", " S ", "S  ", Map.of('M', materialId, 'S', "minecraft:stick"), false))
                return stack("got:" + tier + "_polearm");

            // Pike: ..S / .S. / M..
            if (grid("  S", " S ", "M  ", Map.of('M', materialId, 'S', "minecraft:stick"), false))
                return stack("got:" + tier + "_pike");

            // Dagger: ... / .M. / .S.
            if (grid("   ", " M ", " S ", Map.of('M', materialId, 'S', "minecraft:stick"), false))
                return stack(dagger);

            // Poisoned dagger (direct shaped route): .P. / .M. / .S.
            if (grid(" P ", " M ", " S ", Map.of('P', "got:bottle_poison", 'M', materialId, 'S', "minecraft:stick"), false))
                return stack(poisonedDagger);

            return ItemStack.EMPTY;
        }

        private ItemStack findLegacySpecialRecipe() {
            // Pass 4: exact non-standard recipes recovered from GOTRecipe bytecode.
            if ("asshai".equals(equipmentPrefix)) {
                ItemStack result = findAsshaiRecipe();
                if (!result.isEmpty()) return result;
            }
            if ("sothoryos".equals(equipmentPrefix)) {
                ItemStack result = findSothoryosRecipe();
                if (!result.isEmpty()) return result;
            }
            if ("yi_ti".equals(equipmentPrefix)) {
                ItemStack result = findYiTiRecipe();
                if (!result.isEmpty()) return result;
            }

            // Pass 6: close the remaining legacy faction pools. Most of the
            // non-special tables only added their faction banner, their own
            // crafting table recipe, and (for Ghiscar) the Harpy headpiece.
            ItemStack common = findLegacyCommonFactionRecipe();
            if (!common.isEmpty()) return common;
            return ItemStack.EMPTY;
        }

        /**
         * Shared recipes present in the simple legacy GOTRecipe faction pools.
         * The old tables all used the same wool/stick/plank banner silhouette
         * and a 2x2 plank square for the faction table itself.
         */
        private ItemStack findLegacyCommonFactionRecipe() {
            // Ghiscar's one non-armour specialty: XXX / X X made from gold.
            if ("ghiscar".equals(equipmentPrefix)
                    && grid("GGG", "G G", "   ", Map.of('G', "minecraft:gold_ingot"), false)) {
                return stack("got:harpy");
            }

            // Wildling had a second banner recipe for the Thenns. Legacy shape:
            // "XA" / "Y" / "Z", where X=wool, A/Y=sticks, Z=planks.
            if ("fur".equals(equipmentPrefix) && thennBannerPattern()) {
                return got.GOTBannerItem.createStack(got.GOTBannerType.byName("thenn"));
            }

            String bannerName = legacyBannerName(equipmentPrefix);
            if (bannerName != null && factionBannerPattern()) {
                return got.GOTBannerItem.createStack(got.GOTBannerType.byName(bannerName));
            }

            String tableId = legacyTableId(equipmentPrefix);
            if (tableId != null && plankSquare()) return stack("got:" + tableId);
            return ItemStack.EMPTY;
        }

        @Nullable
        private static String legacyBannerName(String prefix) {
            return switch (prefix) {
                case "hillmen" -> "hillmen";
                case "arryn" -> "arryn";
                case "crownlands" -> "robert";
                case "dorne" -> "martell";
                case "dragonstone" -> "stannis";
                case "gift" -> "night";
                case "ironborn" -> "greyjoy";
                case "north" -> "robb";
                case "reach" -> "tyrell";
                case "riverlands" -> "tully";
                case "stormlands" -> "renly";
                case "westerlands" -> "lannister";
                case "lorath" -> "lorath";
                case "lys" -> "lys";
                case "myr" -> "myr";
                case "norvos" -> "norvos";
                case "pentos" -> "pentos";
                case "qarth" -> "qarth";
                case "qohor" -> "qohor";
                case "tyrosh" -> "tyrosh";
                case "volantis" -> "volantis";
                case "braavos" -> "braavos";
                case "ghiscar" -> "ghiscar";
                case "lhazar" -> "lhazar";
                case "summer" -> "summer";
                case "jogos_nhai" -> "jogos_nhai";
                case "mossovy" -> "mossovy";
                case "fur" -> "wildling";
                case "ibben" -> "ibben";
                case "sothoryos" -> "sothoryos";
                // Yi Ti and Asshai are already handled in their exact pools.
                default -> null;
            };
        }

        @Nullable
        private static String legacyTableId(String prefix) {
            return switch (prefix) {
                case "hillmen" -> "table_hill_tribes";
                case "fur" -> "table_wildling";
                default -> "table_" + prefix;
            };
        }

        private boolean factionBannerPattern() {
            // Legacy ShapedOreRecipe was 1x3 and could sit in any grid column.
            for (int col = 0; col < 3; col++) {
                boolean ok = true;
                for (int y = 0; y < 3; y++) for (int x = 0; x < 3; x++) {
                    ItemStack got = getSlot(1 + y * 3 + x).getItem();
                    if (x != col) { if (!got.isEmpty()) ok = false; continue; }
                    if (y == 0 && !got.is(ItemTags.WOOL)) ok = false;
                    if (y == 1 && !got.is(Items.STICK)) ok = false;
                    if (y == 2 && !got.is(ItemTags.PLANKS)) ok = false;
                }
                if (ok) return true;
            }
            return false;
        }

        private boolean thennBannerPattern() {
            // Legacy pattern XA / Y / Z is 2x3 and may be placed at x=0 or x=1.
            for (int ox = 0; ox <= 1; ox++) {
                boolean ok = true;
                for (int y = 0; y < 3; y++) for (int x = 0; x < 3; x++) {
                    ItemStack got = getSlot(1 + y * 3 + x).getItem();
                    boolean wool = x == ox && y == 0;
                    boolean stick = (x == ox + 1 && y == 0) || (x == ox && y == 1);
                    boolean plank = x == ox && y == 2;
                    if (wool) { if (!got.is(ItemTags.WOOL)) ok = false; }
                    else if (stick) { if (!got.is(Items.STICK)) ok = false; }
                    else if (plank) { if (!got.is(ItemTags.PLANKS)) ok = false; }
                    else if (!got.isEmpty()) ok = false;
                }
                if (ok) return true;
            }
            return false;
        }


        /** Exact Yi Ti-only recipes recovered from GOTRecipe.createYiTiRecipes(). */
        private ItemStack findYiTiRecipe() {
            ItemStack r;

            // Core Yi Ti masonry. Legacy brick5:11 was normal Yi Ti brick;
            // brick5:12/13/14/15 were carved, mossy, cracked and flowery.
            if (grid("SS ", "SS ", "   ", Map.of('S', "minecraft:stone"), false))
                return stackCount("got:yi_ti_bricks", 4);
            if (grid("BB ", "BB ", "   ", Map.of('B', "got:yi_ti_bricks"), false))
                return stack("got:carved_yi_ti_bricks");
            if (shapeless("got:yi_ti_bricks", "minecraft:vine"))
                return stack("got:mossy_yi_ti_bricks");
            if (shapeless("got:yi_ti_bricks", "got:chrysanthemum_orange"))
                return stack("got:yi_ti_flowers_bricks");

            // Gold-trimmed Yi Ti brick: four gold nuggets around a normal Yi Ti brick.
            if (grid(" G ", "GBG", " G ", Map.of('G', "minecraft:gold_nugget", 'B', "got:yi_ti_bricks"), false))
                return stack("got:yi_ti_gold_bricks");

            // Granite Yi Ti masonry. Legacy rock:4 was granite; brick6:1/2 were
            // granite Yi Ti brick and carved granite Yi Ti brick.
            if (grid("GG ", "GG ", "   ", Map.of('G', "got:granite_rock"), false))
                return stackCount("got:yi_ti_granite_bricks", 4);
            if (grid("BB ", "BB ", "   ", Map.of('B', "got:yi_ti_granite_bricks"), false))
                return stack("got:carved_yi_ti_granite_bricks");

            // Pillars came directly from three stone/granite blocks in a vertical line.
            if (grid("S  ", "S  ", "S  ", Map.of('S', "minecraft:stone"), false))
                return stackCount("got:yi_ti_pillar", 3);
            if (grid("G  ", "G  ", "G  ", Map.of('G', "got:granite_rock"), false))
                return stackCount("got:yi_ti_granite_pillar", 3);

            // Slabs, stairs and walls exactly mirror the five legacy trim families.
            if (!(r = stoneFamily("got:yi_ti_bricks", "got:yi_ti_brick_slab", "got:stairs_yi_ti_brick", "got:yi_ti_brick_wall")).isEmpty()) return r;
            if (!(r = stoneFamily("got:mossy_yi_ti_bricks", "got:mossy_yi_ti_brick_slab", "got:stairs_yi_ti_brick_mossy", "got:mossy_yi_ti_brick_wall")).isEmpty()) return r;
            if (!(r = stoneFamily("got:cracked_yi_ti_bricks", "got:cracked_yi_ti_brick_slab", "got:stairs_yi_ti_brick_cracked", "got:cracked_yi_ti_brick_wall")).isEmpty()) return r;
            if (!(r = stoneFamily("got:yi_ti_flowers_bricks", "got:flowery_yi_ti_brick_slab", "got:stairs_yi_ti_brick_flowers", "got:flowery_yi_ti_brick_wall")).isEmpty()) return r;
            if (!(r = stoneFamily("got:yi_ti_granite_bricks", "got:granite_yi_ti_brick_slab", "got:stairs_yi_ti_brick_red", "got:granite_yi_ti_brick_wall")).isEmpty()) return r;
            if (grid("PPP", "   ", "   ", Map.of('P', "got:yi_ti_pillar"), false))
                return stackCount("got:yi_ti_pillar_slab", 6);
            if (grid("PPP", "   ", "   ", Map.of('P', "got:yi_ti_granite_pillar"), false))
                return stackCount("got:yi_ti_granite_pillar_slab", 6);

            // Ordinary Yi Ti armour uses iron, as in the legacy pool.
            if (grid("III", "I I", "   ", Map.of('I', "minecraft:iron_ingot"), false)) return stack("got:yi_ti_helmet");
            if (grid("I I", "III", "III", Map.of('I', "minecraft:iron_ingot"), false)) return stack("got:yi_ti_chestplate");
            if (grid("III", "I I", "I I", Map.of('I', "minecraft:iron_ingot"), false)) return stack("got:yi_ti_leggings");
            if (grid("I I", "I I", "   ", Map.of('I', "minecraft:iron_ingot"), false)) return stack("got:yi_ti_boots");

            Map<Character, String> ai = Map.of('A', "got:alloy_steel_ingot", 'I', "minecraft:iron_ingot");

            // Bombardier armour: X=alloy steel, Y=iron in the original bytecode.
            if (grid("AIA", "I I", "   ", ai, false)) return stack("got:yi_ti_bombardier_helmet");
            if (grid("A A", "IAI", "AIA", ai, false)) return stack("got:yi_ti_bombardier_chestplate");
            if (grid("AAA", "I I", "A A", ai, false)) return stack("got:yi_ti_bombardier_leggings");
            if (grid("A A", "I I", "   ", ai, false)) return stack("got:yi_ti_bombardier_boots");

            // Samurai armour: the complementary alloy/iron layouts from legacy.
            if (grid("III", "A A", "   ", ai, false)) return stack("got:yi_ti_samurai_helmet");
            if (grid("A A", "AIA", "IAI", ai, false)) return stack("got:yi_ti_samurai_chestplate");
            if (grid("AAA", "A A", "I I", ai, false)) return stack("got:yi_ti_samurai_leggings");
            if (grid("I I", "A A", "   ", ai, false)) return stack("got:yi_ti_samurai_boots");

            // Captain helmet: horn / samurai helmet / horn.
            if (grid("HYH", "   ", "   ", Map.of('H', "got:white_bison_horn", 'Y', "got:yi_ti_samurai_helmet"), false))
                return stack("got:yi_ti_helmet_captain");

            if (yiTiBannerPattern())
                return got.GOTBannerItem.createStack(got.GOTBannerType.byName("yi_ti"));
            if (plankSquare()) return stack("got:table_yi_ti");
            return ItemStack.EMPTY;
        }

        private boolean yiTiBannerPattern() {
            return factionBannerPattern();
        }

        /** Exact Asshai-only recipes recovered from GOTRecipe.createAsshaiRecipes(). */
        private ItemStack findAsshaiRecipe() {
            ItemStack r;

            // Legacy brick1:0 was basalt brick. Asshai could manufacture it directly
            // from four basalt rocks, then shape the full local masonry family.
            if (grid("RR ", "RR ", "   ", Map.of('R', "got:basalt_rock"), false))
                return stackCount("got:basalt_bricks", 4);
            if (grid("R  ", "R  ", "R  ", Map.of('R', "got:basalt_rock"), false))
                return stackCount("got:asshai_basalt_pillar", 3);
            if (!(r = stoneFamily("got:basalt_bricks", "got:asshai_basalt_brick_slab",
                    "got:stairs_basalt_brick_asshai", "got:asshai_basalt_brick_wall")).isEmpty()) return r;

            // Legacy brick1:7 was cracked basalt and had its own Asshai trim set.
            if (!(r = stoneFamily("got:cracked_basalt_bricks", "got:asshai_cracked_basalt_brick_slab",
                    "got:stairs_basalt_brick_asshai_cracked", "got:asshai_cracked_basalt_brick_wall")).isEmpty()) return r;

            // Asshai chandelier: two output. Legacy pattern was " X" / "YZY":
            // X=stickWood, Y=fuse, Z=iron ingot.
            if (grid(" S ", "FIF", "   ", Map.of('F', "got:fuse", 'I', "minecraft:iron_ingot"), true))
                return stackCount("got:asshai_chandelier", 2);

            // Pillar slabs and carved basalt.
            if (grid("PPP", "   ", "   ", Map.of('P', "got:asshai_basalt_pillar"), false))
                return stackCount("got:asshai_basalt_pillar_slab", 6);
            if (grid("BB ", "BB ", "   ", Map.of('B', "got:basalt_bricks"), false))
                return stack("got:carved_basalt_bricks");

            // Asshai torch used the vanilla torch silhouette: coal above stick.
            if (grid("C  ", "S  ", "   ", Map.of('C', "minecraft:coal"), true))
                return stack("got:asshai_torch");

            // Six iron ingots -> sixteen Asshai bars, matching the legacy pool.
            if (grid("III", "III", "   ", Map.of('I', "minecraft:iron_ingot"), false))
                return stackCount("got:asshai_bars", 16);

            // Shadowbinder staff: "  X" / " Y" / "Y"; X=ruby, Y=stickWood.
            if (grid("  R", " S ", "S  ", Map.of('R', "got:ruby"), true))
                return stack("got:asshai_shadowbinder_staff");

            // Asshai mask: XXX / X X, X=plankWood.
            if (grid("PPP", "P P", "   ", Map.of(), true))
                return stack("got:asshai_mask");

            // Legacy faction banner: wool / stick / plank in a vertical line.
            if (asshaiBannerPattern())
                return got.GOTBannerItem.createStack(got.GOTBannerType.byName("asshai"));

            // The Asshai faction crafting table itself used a 2x2 plank square.
            if (plankSquare()) return stack("got:table_asshai");
            return ItemStack.EMPTY;
        }

        private boolean asshaiBannerPattern() {
            return factionBannerPattern();
        }

        /** Exact Sothoryos-only recipes recovered from GOTRecipe.createSothoryosRecipes(). */
        private ItemStack findSothoryosRecipe() {
            // Architectural family: legacy brick4 metadata 0-4.
            if (shapeless("got:sothoryos_bricks", "minecraft:vine")) return stack("got:mossy_sothoryos_bricks");
            if (grid("SS ", "SS ", "   ", Map.of('S', "minecraft:stone"), false)) return stackCount("got:sothoryos_bricks", 4);
            ItemStack r;
            if (!(r = stoneFamily("got:sothoryos_bricks", "got:sothoryos_brick_slab", "got:stairs_sothoryos_brick", "got:sothoryos_brick_wall")).isEmpty()) return r;
            if (!(r = stoneFamily("got:mossy_sothoryos_bricks", "got:mossy_sothoryos_brick_slab", "got:stairs_sothoryos_brick_mossy", "got:mossy_sothoryos_brick_wall")).isEmpty()) return r;
            if (!(r = stoneFamily("got:cracked_sothoryos_bricks", "got:cracked_sothoryos_brick_slab", "got:stairs_sothoryos_brick_cracked", "got:cracked_sothoryos_brick_wall")).isEmpty()) return r;
            if (grid("GG ", "GG ", "   ", Map.of('G', "minecraft:gold_ingot"), false)) return stackCount("got:sothoryos_gold_bricks", 4);
            if (!(r = stoneFamily("got:sothoryos_gold_bricks", "got:sothoryos_gold_brick_slab", "got:stairs_sothoryos_brick_gold", "got:sothoryos_gold_brick_wall")).isEmpty()) return r;
            if (grid("OO ", "OO ", "   ", Map.of('O', "got:obsidian_shard"), false)) return stackCount("got:sothoryos_obsidian_bricks", 4);
            if (!(r = stoneFamily("got:sothoryos_obsidian_bricks", "got:sothoryos_obsidian_brick_slab", "got:stairs_sothoryos_brick_obsidian", "got:sothoryos_obsidian_brick_wall")).isEmpty()) return r;

            // Pillars and pillar slabs.
            if (grid("S  ", "S  ", "S  ", Map.of('S', "minecraft:stone"), false)) return stackCount("got:sothoryos_pillar", 3);
            if (grid("PPP", "   ", "   ", Map.of('P', "got:sothoryos_pillar"), false)) return stackCount("got:sothoryos_pillar_slab", 6);
            if (grid("G  ", "G  ", "G  ", Map.of('G', "minecraft:gold_ingot"), false)) return stackCount("got:sothoryos_gold_pillar", 3);
            if (grid("PPP", "   ", "   ", Map.of('P', "got:sothoryos_gold_pillar"), false)) return stackCount("got:sothoryos_gold_pillar_slab", 6);
            if (grid("O  ", "O  ", "O  ", Map.of('O', "got:obsidian_shard"), false)) return stackCount("got:sothoryos_obsidian_pillar", 3);
            if (grid("PPP", "   ", "   ", Map.of('P', "got:sothoryos_obsidian_pillar"), false)) return stackCount("got:sothoryos_obsidian_pillar_slab", 6);

            // Sarbacane traps: eight matching bricks surrounding one sarbacane.
            if (grid("BBB", "BSB", "BBB", Map.of('B', "got:sothoryos_bricks", 'S', "got:sarbacane"), false)) return stack("got:sarbacane_trap");
            if (grid("BBB", "BSB", "BBB", Map.of('B', "got:sothoryos_gold_bricks", 'S', "got:sarbacane"), false)) return stack("got:sarbacane_trap_gold");
            if (grid("BBB", "BSB", "BBB", Map.of('B', "got:sothoryos_obsidian_bricks", 'S', "got:sarbacane"), false)) return stack("got:sarbacane_trap_obsidian");

            // Two legacy double torches: coal over two sticks.
            if (grid("C  ", "S  ", "S  ", Map.of('C', "minecraft:coal", 'S', "minecraft:stick"), false)) return stackCount("got:sothoryos_double_torch", 2);

            // Sothoryos iron armour and the Flame-of-East chieftain upgrade.
            if (grid("III", "I I", "   ", Map.of('I', "minecraft:iron_ingot"), false)) return stack("got:sothoryos_helmet");
            if (grid("I I", "III", "III", Map.of('I', "minecraft:iron_ingot"), false)) return stack("got:sothoryos_chestplate");
            if (grid("III", "I I", "I I", Map.of('I', "minecraft:iron_ingot"), false)) return stack("got:sothoryos_leggings");
            if (grid("I I", "I I", "   ", Map.of('I', "minecraft:iron_ingot"), false)) return stack("got:sothoryos_boots");
            if (grid("F  ", "H  ", "   ", Map.of('F', "got:flame_of_east", 'H', "got:sothoryos_helmet"), false)) return stack("got:sothoryos_helmet_chieftain");

            // Blowgun and ammunition.
            if (grid("SRR", "   ", "   ", Map.of('S', "minecraft:stick", 'R', "got:reeds"), false)) return stack("got:sarbacane");
            if (grid("O  ", "S  ", "F  ", Map.of('O', "got:obsidian_shard", 'S', "minecraft:stick", 'F', "minecraft:feather"), false)) return stackCount("got:dart", 4);
            if (shapeless("got:dart", "got:bottle_poison")) return stack("got:dart_poisoned");

            // The faction table itself used the old plankWood OreDictionary entry.
            if (plankSquare()) return stack("got:table_sothoryos");
            return ItemStack.EMPTY;
        }

        private ItemStack stoneFamily(String block, String slab, String stairs, String wall) {
            if (grid("BBB", "   ", "   ", Map.of('B', block), false)) return stackCount(slab, 6);
            if (grid("B  ", "BB ", "BBB", Map.of('B', block), false)) return stackCount(stairs, 4);
            if (grid("BBB", "BBB", "   ", Map.of('B', block), false)) return stackCount(wall, 6);
            return ItemStack.EMPTY;
        }

        private boolean plankSquare() {
            // Legacy 2x2 ShapedOreRecipe can be placed in any corner of the 3x3 grid.
            for (int oy = 0; oy <= 1; oy++) for (int ox = 0; ox <= 1; ox++) {
                boolean ok = true;
                for (int y = 0; y < 3; y++) for (int x = 0; x < 3; x++) {
                    ItemStack got = getSlot(1 + y * 3 + x).getItem();
                    boolean required = x >= ox && x < ox + 2 && y >= oy && y < oy + 2;
                    if (required ? !got.is(ItemTags.PLANKS) : !got.isEmpty()) ok = false;
                }
                if (ok) return true;
            }
            return false;
        }

        private boolean shapeless(String... ids) {
            java.util.List<Item> wanted = new java.util.ArrayList<>();
            for (String id : ids) {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
                if (item == null || item == Items.AIR) return false;
                wanted.add(item);
            }
            for (int i = 0; i < 9; i++) {
                ItemStack got = getSlot(i + 1).getItem();
                if (got.isEmpty()) continue;
                if (!wanted.remove(got.getItem())) return false;
            }
            return wanted.isEmpty();
        }

        private static ItemStack stackCount(String id, int count) {
            ItemStack stack = stack(id);
            if (!stack.isEmpty()) stack.setCount(count);
            return stack;
        }

        private boolean shapelessExact(String... itemIds) {
            java.util.List<Item> wanted = new java.util.ArrayList<>();
            for (String id : itemIds) {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
                if (item == null || item == Items.AIR) return false;
                wanted.add(item);
            }
            for (int i = 0; i < 9; i++) {
                ItemStack got = getSlot(i + 1).getItem();
                if (got.isEmpty()) continue;
                if (!wanted.remove(got.getItem())) return false;
            }
            return wanted.isEmpty();
        }

        private boolean grid(String r0, String r1, String r2, Map<Character, String> ids, boolean tagAware) {
            String[] rows = {r0, r1, r2};
            int minX = 3, minY = 3, maxX = -1, maxY = -1;
            for (int y = 0; y < 3; y++) for (int x = 0; x < 3; x++) {
                if (rows[y].charAt(x) == ' ') continue;
                minX = Math.min(minX, x); maxX = Math.max(maxX, x);
                minY = Math.min(minY, y); maxY = Math.max(maxY, y);
            }
            if (maxX < 0) return false;
            int width = maxX - minX + 1, height = maxY - minY + 1;

            // Forge ShapedOreRecipe supported grid offsets and horizontal mirroring.
            for (int oy = 0; oy <= 3 - height; oy++) for (int ox = 0; ox <= 3 - width; ox++) {
                for (boolean mirror : new boolean[]{false, true}) {
                    boolean ok = true;
                    for (int y = 0; y < 3 && ok; y++) for (int x = 0; x < 3; x++) {
                        ItemStack got = getSlot(1 + y * 3 + x).getItem();
                        int px = x - ox, py = y - oy;
                        char c = ' ';
                        if (px >= 0 && px < width && py >= 0 && py < height) {
                            int sx = mirror ? (maxX - px) : (minX + px);
                            c = rows[minY + py].charAt(sx);
                        }
                        if (c == ' ') { if (!got.isEmpty()) ok = false; continue; }
                        if (c == 'P' && tagAware) { if (!got.is(ItemTags.PLANKS)) ok = false; continue; }
                        if (c == 'S' && tagAware) { if (!got.is(Items.STICK)) ok = false; continue; }
                        String id = ids.get(c);
                        if (id == null) { ok = false; continue; }
                        Item want = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
                        if (want == null || want == Items.AIR || got.getItem() != want) ok = false;
                    }
                    if (ok) return true;
                }
            }
            return false;
        }

        private static ItemStack stack(String id) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
            return item == null || item == Items.AIR ? ItemStack.EMPTY : new ItemStack(item);
        }

        private record MaterialMirror(ItemStack[] original, String outputPrefix) {}

        /**
         * Legacy faction tables sometimes used a non-vanilla material with the
         * ordinary armour silhouettes. We temporarily mirror that material to
         * iron only for recipe matching, then restore the real grid before the
         * result can be taken. This means CraftingResultSlot consumes the real
         * legacy ingredient rather than a synthetic substitute.
         */
        @Nullable
        private MaterialMirror prepareLegacyArmorMaterialMirror() {
            boolean[] occupied = new boolean[9];
            for (int i = 0; i < 9; i++) occupied[i] = !getSlot(i + 1).getItem().isEmpty();
            if (!isArmorSilhouette(occupied)) return null;

            String ingredientId = null;
            String outputPrefix = null;
            if ("fur".equals(equipmentPrefix)) {
                ingredientId = "fur";
                outputPrefix = "fur";
            } else if ("dothraki".equals(equipmentPrefix)) {
                ingredientId = "dried_reeds";
                outputPrefix = "dothraki";
            } else if (ELITE_ARMOR_PREFIX.containsKey(equipmentPrefix)) {
                ingredientId = "alloy_steel_ingot";
                outputPrefix = ELITE_ARMOR_PREFIX.get(equipmentPrefix);
            }
            if (ingredientId == null) return null;

            Item ingredient = ForgeRegistries.ITEMS.getValue(new ResourceLocation("got", ingredientId));
            if (ingredient == null || ingredient == Items.AIR) return null;
            for (int i = 0; i < 9; i++) {
                ItemStack stack = getSlot(i + 1).getItem();
                if (!stack.isEmpty() && stack.getItem() != ingredient) return null;
            }

            ItemStack[] original = new ItemStack[9];
            for (int i = 0; i < 9; i++) {
                ItemStack stack = getSlot(i + 1).getItem();
                original[i] = stack.copy();
                if (!stack.isEmpty()) getSlot(i + 1).set(new ItemStack(Items.IRON_INGOT, stack.getCount()));
            }
            return new MaterialMirror(original, outputPrefix);
        }

        private void restoreMaterialMirror(MaterialMirror mirror) {
            for (int i = 0; i < 9; i++) getSlot(i + 1).set(mirror.original[i]);
        }

        private static boolean isArmorSilhouette(boolean[] s) {
            // Helmet: XXX / X X (either top two rows or bottom two rows)
            if (shape(s, "XXX", "X X", "   ") || shape(s, "   ", "XXX", "X X")) return true;
            // Chestplate: X X / XXX / XXX
            if (shape(s, "X X", "XXX", "XXX")) return true;
            // Leggings: XXX / X X / X X
            if (shape(s, "XXX", "X X", "X X")) return true;
            // Boots: X X / X X (either top two rows or bottom two rows)
            return shape(s, "X X", "X X", "   ") || shape(s, "   ", "X X", "X X");
        }

        private static boolean shape(boolean[] actual, String a, String b, String c) {
            String[] rows = {a, b, c};
            for (int y = 0; y < 3; y++) for (int x = 0; x < 3; x++) {
                if (actual[y * 3 + x] != (rows[y].charAt(x) == 'X')) return false;
            }
            return true;
        }
    }
}
