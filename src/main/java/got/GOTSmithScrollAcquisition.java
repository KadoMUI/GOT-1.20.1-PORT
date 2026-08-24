package got;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.RandomSource;

/**
 * Smith Scroll acquisition routes for Project Thrones 1.0.
 *
 * - Military NPCs: very rare random scroll drop (1%)
 * - Blacksmith NPCs: rare random scroll drop (5%)
 * - Blacksmith trade lists: four weighted random Smith Scroll offers
 *
 * Role classification is intentionally based on the existing regional role
 * enums' combat()/trade() APIs so every current regional NPC implementation can
 * participate without maintaining a second faction-specific registry.
 */
@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTSmithScrollAcquisition {
    public static final float MILITARY_DROP_CHANCE = 0.01F;
    public static final float BLACKSMITH_DROP_CHANCE = 0.05F;
    public static final int BLACKSMITH_SCROLL_OFFERS = 4;

    private GOTSmithScrollAcquisition() {}

    @SubscribeEvent
    public static void livingDrops(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();
        Object role = roleOf(entity);
        if (role == null) return;

        boolean blacksmith = enumName(invoke(role, "trade")).equals("BLACKSMITH");
        boolean military = isMilitaryRole(role);

        float chance = blacksmith ? BLACKSMITH_DROP_CHANCE
                : military ? MILITARY_DROP_CHANCE
                : 0.0F;
        if (chance <= 0.0F || entity.getRandom().nextFloat() >= chance) return;

        GOTSmithingModifier modifier = weightedRandom(entity.getRandom());
        if (modifier == null) return;
        event.getDrops().add(new net.minecraft.world.entity.item.ItemEntity(
                entity.level(), entity.getX(), entity.getY(), entity.getZ(),
                GOTSmithScrollItem.create(modifier)));
    }

    /**
     * Called by every regional loadout builder after its ordinary offers are
     * assembled. Only roles whose trade enum is BLACKSMITH receive scrolls.
     */
    public static void addBlacksmithOffers(MerchantOffers offers, Object role) {
        if (offers == null || role == null) return;
        if (!enumName(invoke(role, "trade")).equals("BLACKSMITH")) return;

        RandomSource random = RandomSource.create(stableSeed(role, offers.size()));
        List<GOTSmithingModifier> chosen = new ArrayList<>();

        for (int i = 0; i < BLACKSMITH_SCROLL_OFFERS; i++) {
            GOTSmithingModifier modifier = weightedRandomAvoiding(random, chosen);
            if (modifier == null) break;
            chosen.add(modifier);

            ItemStack cost = new ItemStack(GOTItems.COIN_1.get(), purchasePrice(modifier));
            ItemStack result = GOTSmithScrollItem.create(modifier);
            offers.add(new MerchantOffer(cost, result, 3, 5, 0.08F));
        }
    }

    /**
     * Price is intentionally readable rather than arbitrary: stronger/skilful
     * scrolls cost more while preserving the old enchant weight as rarity.
     */
    public static int purchasePrice(GOTSmithingModifier modifier) {
        int price = 10 + modifier.materialCost() * 4;
        if (modifier.skilful()) price += 8;
        if (modifier.enchantWeight() <= 2) price += 8;
        else if (modifier.enchantWeight() <= 5) price += 4;
        return Math.min(48, Math.max(10, price));
    }

    private static GOTSmithingModifier weightedRandom(RandomSource random) {
        return weightedRandomAvoiding(random, List.of());
    }

    private static GOTSmithingModifier weightedRandomAvoiding(RandomSource random,
                                                              List<GOTSmithingModifier> excluded) {
        int total = 0;
        for (GOTSmithingModifier modifier : GOTSmithingModifier.values()) {
            if (excluded.contains(modifier) || modifier.enchantWeight() <= 0) continue;
            total += modifier.enchantWeight();
        }
        if (total <= 0) return null;

        int roll = random.nextInt(total);
        for (GOTSmithingModifier modifier : GOTSmithingModifier.values()) {
            if (excluded.contains(modifier) || modifier.enchantWeight() <= 0) continue;
            roll -= modifier.enchantWeight();
            if (roll < 0) return modifier;
        }
        return null;
    }

    private static boolean isMilitaryRole(Object role) {
        Object combat = invoke(role, "combat");
        String combatName = enumName(combat);
        if (combatName.isEmpty() || combatName.equals("PASSIVE") || combatName.equals("NONE")) return false;

        // Legendary named characters are not treated as ordinary military-drop
        // farms unless their role enum does not expose legendary().
        Object legendary = invoke(role, "legendary");
        if (legendary instanceof Boolean b && b) return false;
        return true;
    }

    private static Object roleOf(Object entity) {
        return invoke(entity, "getRole");
    }

    private static Object invoke(Object target, String methodName) {
        if (target == null) return null;
        try {
            Method m = target.getClass().getMethod(methodName);
            return m.invoke(target);
        } catch (ReflectiveOperationException ignored) {
            return null;
        }
    }

    private static String enumName(Object value) {
        return value instanceof Enum<?> e ? e.name() : "";
    }

    private static long stableSeed(Object role, int offerCount) {
        return 0x474F54534D495448L ^ role.toString().hashCode() * 31L ^ offerCount;
    }
}
