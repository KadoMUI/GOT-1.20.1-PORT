package got.npc;

import got.GOTMod;
import net.minecraft.util.RandomSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

/** Reads the original regional name banks already shipped by the mod. */
final class GOTNpcNames {
    private static final List<String> WESTEROS_MALE = load("westeros_male", List.of("Jon", "Robb", "Brandon"));
    private static final List<String> WESTEROS_FEMALE = load("westeros_female", List.of("Lyanna", "Sansa", "Arya"));
    private static final List<String> WILD_MALE = load("wild_male", WESTEROS_MALE);
    private static final List<String> WILD_FEMALE = load("wild_female", WESTEROS_FEMALE);
    private static final List<String> ESSOS_MALE = load("essos_male", List.of("Syrio", "Illyrio", "Varys"));
    private static final List<String> ESSOS_FEMALE = load("essos_female", List.of("Talisa", "Tyene", "Nymeria"));
    private static final List<String> GHISCAR_MALE = load("ghiscar_male", ESSOS_MALE);
    private static final List<String> GHISCAR_FEMALE = load("ghiscar_female", ESSOS_FEMALE);
    private static final List<String> DOTHRAKI_MALE = load("dothraki_male", ESSOS_MALE);
    private static final List<String> DOTHRAKI_FEMALE = load("dothraki_female", ESSOS_FEMALE);
    private static final List<String> YI_TI_MALE = load("yi_ti_male", ESSOS_MALE);
    private static final List<String> YI_TI_FEMALE = load("yi_ti_female", ESSOS_FEMALE);
    private static final List<String> ASSHAI_MALE = load("asshai_male", ESSOS_MALE);
    private static final List<String> ASSHAI_FEMALE = load("asshai_female", ESSOS_FEMALE);
    private static final List<String> JOGOS_NHAI_MALE = load("jogos_nhai_male", ESSOS_MALE);
    private static final List<String> JOGOS_NHAI_FEMALE = load("jogos_nhai_female", ESSOS_FEMALE);
    private static final List<String> LHAZAR_MALE = load("lhazar_male", ESSOS_MALE);
    private static final List<String> LHAZAR_FEMALE = load("lhazar_female", ESSOS_FEMALE);
    private static final List<String> MOSSOVY_MALE = load("mossovy_male", ESSOS_MALE);
    private static final List<String> MOSSOVY_FEMALE = load("mossovy_female", ESSOS_FEMALE);
    private static final List<String> SOTHORYOS_MALE = load("sothoryos_male", ESSOS_MALE);
    private static final List<String> SOTHORYOS_FEMALE = load("sothoryos_female", ESSOS_FEMALE);

    private GOTNpcNames() {}

    static String random(RandomSource random, boolean female, boolean wild) {
        List<String> names = wild
                ? (female ? WILD_FEMALE : WILD_MALE)
                : (female ? WESTEROS_FEMALE : WESTEROS_MALE);
        return names.get(random.nextInt(names.size()));
    }

    static String randomEssos(RandomSource random, boolean female) {
        List<String> names = female ? ESSOS_FEMALE : ESSOS_MALE;
        return names.get(random.nextInt(names.size()));
    }

    static String randomGhiscar(RandomSource random, boolean female) {
        List<String> names = female ? GHISCAR_FEMALE : GHISCAR_MALE;
        return names.get(random.nextInt(names.size()));
    }

    static String randomDothraki(RandomSource random, boolean female) {
        List<String> names = female ? DOTHRAKI_FEMALE : DOTHRAKI_MALE;
        return names.get(random.nextInt(names.size()));
    }

    static String randomYiTi(RandomSource random, boolean female) {
        List<String> names = female ? YI_TI_FEMALE : YI_TI_MALE;
        return names.get(random.nextInt(names.size()));
    }

    static String randomAsshai(RandomSource random, boolean female) {
        List<String> names = female ? ASSHAI_FEMALE : ASSHAI_MALE;
        return names.get(random.nextInt(names.size()));
    }

    /** The final Ibbenese class intentionally used the wild-name bank. */
    static String randomIbben(RandomSource random, boolean female) {
        List<String> names = female ? WILD_FEMALE : WILD_MALE;
        return names.get(random.nextInt(names.size()));
    }

    static String randomJogosNhai(RandomSource random, boolean female) {
        List<String> names = female ? JOGOS_NHAI_FEMALE : JOGOS_NHAI_MALE;
        return names.get(random.nextInt(names.size()));
    }

    static String randomLhazar(RandomSource random, boolean female) {
        List<String> names = female ? LHAZAR_FEMALE : LHAZAR_MALE;
        return names.get(random.nextInt(names.size()));
    }

    static String randomMossovy(RandomSource random, boolean female) {
        List<String> names = female ? MOSSOVY_FEMALE : MOSSOVY_MALE;
        return names.get(random.nextInt(names.size()));
    }

    /** Both Sothoryosi and Summer Islander classes used this shared legacy bank. */
    static String randomSothoryos(RandomSource random, boolean female) {
        List<String> names = female ? SOTHORYOS_FEMALE : SOTHORYOS_MALE;
        return names.get(random.nextInt(names.size()));
    }

    private static List<String> load(String bank, List<String> fallback) {
        String path = "/assets/got/texts/en/names/" + bank + ".txt";
        try (InputStream stream = GOTNpcNames.class.getResourceAsStream(path)) {
            if (stream == null) return fallback;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                List<String> values = reader.lines().map(String::trim).filter(line -> !line.isEmpty()).toList();
                return values.isEmpty() ? fallback : values;
            }
        } catch (Exception exception) {
            GOTMod.LOGGER.warn("Unable to read legacy NPC name bank {}", bank, exception);
            return fallback;
        }
    }
}
