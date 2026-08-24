package got;

import net.minecraft.world.effect.MobEffects;

import java.util.HashMap;
import java.util.Map;

/**
 * Central definitions for the 48 drinks restored in 4.5.4. Values mirror the
 * broad legacy categories: juices/soft drinks are non-alcoholic, beers and
 * wines are mild, and distilled spirits are strong. Exceptional drinks retain
 * their signature effects.
 */
public final class GOTDrinkDefinitions {
    private static final Map<String, GOTDrinkDefinition> DEFINITIONS = new HashMap<>();

    static {
        soft("mug_apple_juice", 2, 0.2F);
        soft("mug_blackberry_juice", 2, 0.2F);
        soft("mug_blueberry_juice", 2, 0.2F);
        soft("mug_chocolate", 4, 0.4F);
        soft("mug_cocoa", 4, 0.4F);
        soft("mug_cranberry_juice", 2, 0.2F);
        soft("mug_elderberry_juice", 2, 0.2F);
        soft("mug_lemonade", 2, 0.2F);
        soft("mug_mango_juice", 2, 0.2F);
        soft("mug_milk", 3, 0.3F);
        soft("mug_orange_juice", 2, 0.2F);
        soft("mug_pomegranate_juice", 2, 0.2F);
        soft("mug_raspberry_juice", 2, 0.2F);
        soft("mug_red_grape_juice", 2, 0.2F);
        soft("mug_sour_milk", 2, 0.2F);
        soft("mug_unsullied_tonic", 2, 0.2F);
        soft("mug_water", 0, 0.0F);
        soft("mug_white_grape_juice", 2, 0.2F);

        mild("mug_ale");
        mild("mug_banana_beer");
        mild("mug_cider");
        mild("mug_maple_beer");
        mild("mug_mead");
        mild("mug_perry");
        mild("mug_plantain_brew");
        mild("mug_plum_kvass");
        wine("mug_carrot_wine");
        wine("mug_pomegranate_wine");
        wine("mug_red_wine");
        wine("mug_white_wine");

        spirit("mug_araq");
        spirit("mug_brandy");
        spirit("mug_cactus_liqueur");
        spirit("mug_cherry_liqueur");
        spirit("mug_corn_liquor");
        spirit("mug_ethanol");
        spirit("mug_gin");
        spirit("mug_lemon_liqueur");
        spirit("mug_lime_liqueur");
        spirit("mug_melon_liqueur");
        spirit("mug_rum");
        spirit("mug_sambuca");
        spirit("mug_termite_tequila");
        spirit("mug_vodka");
        spirit("mug_whisky");

        define(new GOTDrinkDefinition("mug_poppy_milk", 0.0F, false, 1, 0.1F, false, 0,
                java.util.List.of(
                        new GOTDrinkDefinition.EffectEntry(MobEffects.REGENERATION, 20 * 20, 1),
                        new GOTDrinkDefinition.EffectEntry(MobEffects.MOVEMENT_SLOWDOWN, 30 * 20, 1),
                        new GOTDrinkDefinition.EffectEntry(MobEffects.CONFUSION, 20 * 20, 0))));
        define(new GOTDrinkDefinition("mug_shade_evening", 0.25F, false, 0, 0.0F, false, 0,
                java.util.List.of(
                        new GOTDrinkDefinition.EffectEntry(MobEffects.NIGHT_VISION, 90 * 20, 0),
                        new GOTDrinkDefinition.EffectEntry(MobEffects.CONFUSION, 25 * 20, 0))));
        define(new GOTDrinkDefinition("mug_wild_fire", 0.0F, false, 0, 0.0F, false, 8,
                java.util.List.of(new GOTDrinkDefinition.EffectEntry(MobEffects.CONFUSION, 10 * 20, 1))));
    }

    private static void soft(String id, int nutrition, float saturation) {
        define(new GOTDrinkDefinition(id, 0.0F, true, nutrition, saturation, false, 0, java.util.List.of()));
    }

    private static void mild(String id) {
        define(new GOTDrinkDefinition(id, 0.25F, true, 2, 0.2F, false, 0, java.util.List.of()));
    }

    private static void wine(String id) {
        define(new GOTDrinkDefinition(id, 0.45F, true, 2, 0.2F, false, 0, java.util.List.of()));
    }

    private static void spirit(String id) {
        define(new GOTDrinkDefinition(id, 0.8F, true, 1, 0.1F, false, 0, java.util.List.of()));
    }

    private static void define(GOTDrinkDefinition definition) {
        DEFINITIONS.put(definition.id(), definition);
    }

    public static GOTDrinkDefinition get(String id) {
        GOTDrinkDefinition definition = DEFINITIONS.get(id);
        if (definition == null) {
            throw new IllegalArgumentException("Unknown GOT drink definition: " + id);
        }
        return definition;
    }

    private GOTDrinkDefinitions() {}
}
