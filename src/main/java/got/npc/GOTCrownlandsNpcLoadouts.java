package got.npc;

import got.GOTBannerItem;
import got.GOTBannerType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraftforge.registries.ForgeRegistries;

/** Equipment and trade pools translated from the original Crownlands classes. */
final class GOTCrownlandsNpcLoadouts {
    private GOTCrownlandsNpcLoadouts() {}

    static void configure(GOTCrownlandsNpcEntity npc) {
        CrownlandsNpcRole role = npc.getRole();
        npc.clearLoadout();
        switch (role) {
            case CROWNLANDS_MAN -> npc.setCombatWeapon(stack("got:iron_dagger"));
            case CROWNLANDS_LEVYMAN -> {
                npc.setCombatWeapon(randomIronWeapon(npc));
                leatherArmor(npc);
            }
            case CROWNLANDS_LEVYMAN_ARCHER -> {
                ItemStack melee = randomIronWeapon(npc);
                ItemStack bow = stack("got:longbow");
                npc.setWeapons(melee, bow);
                npc.setRangedWeapon(bow);
                leatherArmor(npc);
            }
            case CROWNLANDS_GUARD -> {
                ItemStack weapon = randomIronWeapon(npc);
                npc.setWeapons(weapon, weapon);
                crownlandsArmor(npc, true);
            }
            case CROWNLANDS_CAPTAIN -> {
                ItemStack sword = new ItemStack(Items.IRON_SWORD);
                npc.setWeapons(sword, sword);
                crownlandsArmor(npc, false);
            }
            case CROWNLANDS_BLACKSMITH -> {
                ItemStack hammer = stack("got:blacksmith_hammer");
                npc.setWeapons(hammer, hammer);
            }
            case CROWNLANDS_ALCHEMIST ->
                    npc.setWeapons(stack("got:iron_dagger"), stack("minecraft:blaze_powder"));
            case CROWNLANDS_GOLDSMITH ->
                    npc.setWeapons(stack("got:iron_dagger"), stack("got:silver_ring"));
            case CROWNLANDS_FARMER -> {
                ItemStack hoe = new ItemStack(Items.IRON_HOE);
                npc.setWeapons(hoe, hoe);
                npc.setItemSlot(EquipmentSlot.HEAD, stack("got:leather_hat"));
            }
            case CROWNLANDS_FARMHAND -> {
                ItemStack hoe = new ItemStack(Items.IRON_HOE);
                npc.setWeapons(hoe, hoe);
            }
            case CROWNLANDS_BARTENDER ->
                    npc.setWeapons(stack("got:iron_dagger"), stack("got:copper_goblet"));
            case CROWNLANDS_MINER -> {
                ItemStack pickaxe = new ItemStack(Items.IRON_PICKAXE);
                npc.setWeapons(pickaxe, pickaxe);
            }
            case CROWNLANDS_LUMBERMAN -> {
                ItemStack axe = new ItemStack(Items.IRON_AXE);
                npc.setWeapons(axe, axe);
                npc.setItemSlot(EquipmentSlot.HEAD, stack("got:leather_hat"));
            }
            case CROWNLANDS_MASON ->
                    npc.setWeapons(new ItemStack(Items.IRON_PICKAXE), new ItemStack(Items.STONE));
            case CROWNLANDS_BREWER ->
                    npc.setWeapons(stack("got:iron_dagger"), stack("got:mug_ale"));
            case CROWNLANDS_FLORIST -> {
                npc.setWeapons(stack("got:iron_dagger"), new ItemStack(Items.POPPY));
                npc.setItemSlot(EquipmentSlot.HEAD, stack("got:leather_hat"));
            }
            case CROWNLANDS_BUTCHER ->
                    npc.setWeapons(stack("got:iron_dagger"), new ItemStack(Items.BEEF));
            case CROWNLANDS_FISHMONGER -> {
                npc.setWeapons(stack("got:iron_dagger"), new ItemStack(Items.COD));
                npc.setItemSlot(EquipmentSlot.HEAD, stack("got:leather_hat"));
            }
            case CROWNLANDS_BAKER ->
                    npc.setWeapons(stack("got:rolling_pin"), new ItemStack(Items.BREAD));

            case KINGSGUARD, MERYN_TRANT, BARRISTAN_SELMY -> {
                ItemStack sword = stack("got:alloy_steel_sword");
                npc.setWeapons(sword, sword);
                kingsguardArmor(npc, role == CrownlandsNpcRole.KINGSGUARD);
            }
            case SANDOR_CLEGANE -> {
                npc.setCombatWeapon(stack("got:sandor_clegane_sword"));
                npc.setItemSlot(EquipmentSlot.HEAD, stack("got:sandor_clegane_helmet"));
            }
            case JOFFREY_BARATHEON -> {
                ItemStack sword = stack("got:hearteater");
                ItemStack crossbow = stack("got:joffrey_baratheon_crossbow");
                npc.setWeapons(sword, sword);
                npc.setRangedWeapon(crossbow);
            }
            case PETYR_BAELISH -> {
                npc.setCombatWeapon(stack("got:petyr_baelish_dagger"));
                npc.setItemSlot(EquipmentSlot.CHEST, stack("got:petyr_baelish_brooch"));
            }
            case TYRION_LANNISTER -> {
                npc.setCombatWeapon(stack("got:alloy_steel_battleaxe"));
                npc.setItemSlot(EquipmentSlot.CHEST, stack("got:hand_gold"));
            }
            case JANOS_SLYNT -> {
                npc.setCombatWeapon(stack("got:alloy_steel_sword"));
                crownlandsArmor(npc, false);
            }
            case TOBHO_MOTT, GENDRY_BARATHEON -> npc.setCombatWeapon(stack("got:blacksmith_hammer"));
            case SANSA_STARK, SHAE, TOMMEN_BARATHEON, MYRCELLA_BARATHEON -> { }
            case CERSEI_LANNISTER, PYCELLE, VARYS, HIGH_SEPTON, LANCEL_LANNISTER ->
                    npc.setCombatWeapon(stack("got:alloy_steel_dagger"));
            case JAIME_LANNISTER, YOREN, BRONN, PODRICK_PAYNE ->
                    npc.setCombatWeapon(stack("got:alloy_steel_sword"));
            case ILYN_PAYNE -> npc.setCombatWeapon(new ItemStack(Items.IRON_AXE));
            default -> {
                ItemStack sword = stack("got:alloy_steel_sword");
                npc.setWeapons(sword, sword);
            }
        }
        GOTNpcShieldLoadouts.equip(npc, npc.getRole(), "got:crownlands_shield");
        npc.updateHeldItem();
    }

    static MerchantOffers createOffers(CrownlandsNpcRole role) {
        MerchantOffers legendary = got.economy.GOTLegendaryTraderOffers.forRole(role.id());
        if (legendary != null) return legendary;
        MerchantOffers offers = new MerchantOffers();
        switch (role.trade()) {
            case ALCHEMIST -> {
                buy(offers, "minecraft:redstone", 16, 3);
                buy(offers, "minecraft:gunpowder", 8, 4);
                sell(offers, 6, "minecraft:blaze_powder", 2);
                sell(offers, 8, "minecraft:fire_charge", 3);
            }
            case BAKER -> {
                buy(offers, "minecraft:wheat", 16, 2);
                sell(offers, 2, "minecraft:bread", 4);
                sell(offers, 5, "got:apple_crumble", 1);
            }
            case BARTENDER -> {
                buy(offers, "minecraft:wheat", 20, 2);
                sell(offers, 3, "got:mug_ale", 1);
                sell(offers, 4, "got:mug_cider", 1);
                sell(offers, 5, "got:mug_mead", 1);
            }
            case BLACKSMITH -> {
                sell(offers, 8, "got:blacksmith_hammer", 1);
                buy(offers, "minecraft:coal", 16, 2);
                buy(offers, "minecraft:iron_ingot", 8, 4);
                sell(offers, 8, "minecraft:iron_sword", 1);
                sell(offers, 10, "got:iron_pike", 1);
            }
            case BREWER -> {
                buy(offers, "minecraft:wheat", 20, 2);
                buy(offers, "minecraft:sugar", 16, 2);
                sell(offers, 3, "got:mug_ale", 1);
                sell(offers, 4, "got:mug_cider", 1);
            }
            case BUTCHER -> {
                buy(offers, "minecraft:beef", 12, 2);
                buy(offers, "minecraft:porkchop", 12, 2);
                sell(offers, 3, "minecraft:cooked_beef", 5);
                sell(offers, 3, "minecraft:cooked_porkchop", 5);
            }
            case FARMER -> {
                sell(offers, 4, "got:branding_iron", 1);
                // Guaranteed Farmer utility trade.
                sell(offers, 8, "got:millstone", 1);
                buy(offers, "minecraft:wheat", 20, 2);
                buy(offers, "minecraft:carrot", 18, 2);
                sell(offers, 2, "minecraft:wheat_seeds", 12);
                sell(offers, 3, "got:leek", 6);
            }
            case FISHMONGER -> {
                buy(offers, "minecraft:cod", 12, 2);
                buy(offers, "minecraft:salmon", 10, 2);
                sell(offers, 3, "minecraft:cooked_cod", 5);
                sell(offers, 3, "minecraft:cooked_salmon", 5);
            }
            case FLORIST -> {
                buy(offers, "minecraft:bone_meal", 16, 2);
                sell(offers, 2, "minecraft:poppy", 6);
                sell(offers, 2, "minecraft:dandelion", 6);
            }
            case GOLDSMITH -> {
                buy(offers, "minecraft:gold_ingot", 4, 5);
                buy(offers, "got:silver_ingot", 6, 5);
                sell(offers, 10, "got:silver_ring", 1);
                sell(offers, 12, "got:gold_ring", 1);
            }
            case LUMBERMAN -> {
                buy(offers, "minecraft:oak_log", 16, 2);
                sell(offers, 2, "minecraft:oak_planks", 24);
                sell(offers, 4, "minecraft:charcoal", 8);
            }
            case MASON -> {
                buy(offers, "minecraft:cobblestone", 32, 2);
                sell(offers, 2, "minecraft:stone_bricks", 16);
                sell(offers, 3, "minecraft:bricks", 12);
            }
            case MINER -> {
                buy(offers, "minecraft:coal", 16, 2);
                buy(offers, "minecraft:raw_iron", 8, 4);
                sell(offers, 4, "minecraft:torch", 16);
                sell(offers, 8, "minecraft:iron_pickaxe", 1);
            }
            case UNITS -> {
                sell(offers, 16, "got:warhorn", 1);
                sell(offers, 12, "got:command_horn", 1);
                sell(offers, 16, "got:crownlands_chestplate", 1);
                sell(offers, 12, "got:crownlands_helmet", 1);
                sell(offers, 10, "minecraft:iron_sword", 1);
            }
            case MAESTER -> {
                buy(offers, "minecraft:paper", 20, 3);
                sell(offers, 4, "minecraft:book", 3);
                sell(offers, 8, "minecraft:compass", 1);
                sell(offers, 8, "got:mug_poppy_milk", 1);
            }
            default -> { }
        }
        got.GOTSmithScrollAcquisition.addBlacksmithOffers(offers, role);
        return offers;
    }

    private static void crownlandsArmor(GOTCrownlandsNpcEntity npc, boolean helmet) {
        npc.setItemSlot(EquipmentSlot.FEET, stack("got:crownlands_boots"));
        npc.setItemSlot(EquipmentSlot.LEGS, stack("got:crownlands_leggings"));
        npc.setItemSlot(EquipmentSlot.CHEST, stack("got:crownlands_chestplate"));
        if (helmet) npc.setItemSlot(EquipmentSlot.HEAD, stack("got:crownlands_helmet"));
    }

    private static void kingsguardArmor(GOTCrownlandsNpcEntity npc, boolean helmet) {
        npc.setItemSlot(EquipmentSlot.FEET, stack("got:kingsguard_boots"));
        npc.setItemSlot(EquipmentSlot.LEGS, stack("got:kingsguard_leggings"));
        npc.setItemSlot(EquipmentSlot.CHEST, stack("got:kingsguard_chestplate"));
        if (helmet) npc.setItemSlot(EquipmentSlot.HEAD, stack("got:kingsguard_helmet"));
    }

    private static void leatherArmor(GOTCrownlandsNpcEntity npc) {
        npc.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
        npc.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
        npc.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
        npc.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
    }

    private static ItemStack randomIronWeapon(GOTCrownlandsNpcEntity npc) {
        return switch (npc.getRandom().nextInt(4)) {
            case 0 -> new ItemStack(Items.IRON_SWORD);
            case 1 -> stack("got:iron_spear");
            case 2 -> stack("got:iron_battleaxe");
            default -> stack("got:iron_pike");
        };
    }

    private static void buy(MerchantOffers offers, String input, int inputCount, int coins) {
        add(offers, input, inputCount, "got:coin_1", coins);
    }

    private static void sell(MerchantOffers offers, int coins, String output, int outputCount) {
        add(offers, "got:coin_1", coins, output, outputCount);
    }

    private static void add(MerchantOffers offers, String input, int inputCount,
                            String output, int outputCount) {
        ItemStack cost = stack(input, inputCount);
        ItemStack result = stack(output, outputCount);
        if (!cost.isEmpty() && !result.isEmpty()) {
            offers.add(new MerchantOffer(cost, result, 12, 2, 0.05F));
        }
    }

    static ItemStack stack(String id) { return stack(id, 1); }

    static ItemStack stack(String id, int count) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
        return item == null ? ItemStack.EMPTY : new ItemStack(item, count);
    }
}
