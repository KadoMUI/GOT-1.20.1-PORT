package got.speech;

import net.minecraft.world.entity.Entity;

import java.util.Map;

/**
 * Role-specific speech routing recovered from the legacy speech-bank names.
 *
 * Legendary NPCs are intentionally resolved before generic trader/combatant
 * routing so named characters keep their authored dialogue.
 */
public final class GOTSpeechSpecificSelector {
    private static final Map<String, String> LEGENDARY = Map.ofEntries(
        Map.entry("alliser_thorne", "alliser"),
        Map.entry("petyr_baelish", "baelish"),
        Map.entry("balon_greyjoy", "balon"),
        Map.entry("benjen_stark", "benjen"),
        Map.entry("bran_stark", "bran"),
        Map.entry("brienne_tarth", "brienne"),
        Map.entry("catelyn_stark", "catelyn"),
        Map.entry("cersei_lannister", "cersei"),
        Map.entry("craster", "craster"),
        Map.entry("craster_wife", "cwife"),
        Map.entry("daario_naharis", "daario"),
        Map.entry("daenerys_targaryen", "daenerys"),
        Map.entry("davos_seaworth", "davos"),
        Map.entry("doran_martell", "doran"),
        Map.entry("edd", "edd"),
        Map.entry("ellarya_sand", "ellarya"),
        Map.entry("euron_greyjoy", "euron"),
        Map.entry("gregor_clegane", "gregor"),
        Map.entry("young_griff", "griff"),
        Map.entry("hodor", "hodor"),
        Map.entry("illyrio_mopatis", "illyrio"),
        Map.entry("jaime_lannister", "jaime"),
        Map.entry("joffrey_baratheon", "joffrey"),
        Map.entry("jorah_mormont", "jorah"),
        Map.entry("kevan_lannister", "kevan"),
        Map.entry("margaery_tyrell", "margaery"),
        Map.entry("melisandra", "melisandra"),
        Map.entry("missandei", "missandei"),
        Map.entry("oberyn_martell", "oberyn"),
        Map.entry("olenna_tyrell", "olenna"),
        Map.entry("ramsay_bolton", "ramsay"),
        Map.entry("renly_baratheon", "renly"),
        Map.entry("robb_stark", "robb"),
        Map.entry("roose_bolton", "roose"),
        Map.entry("samwell_tarly", "sam"),
        Map.entry("sandor_clegane", "sandor"),
        Map.entry("sansa_stark", "sansa"),
        Map.entry("shireen_baratheon", "shireen"),
        Map.entry("stannis_baratheon", "stannis"),
        Map.entry("theon_greyjoy", "theon"),
        Map.entry("tyrion_lannister", "tyrion"),
        Map.entry("tywin_lannister", "tywin"),
        Map.entry("varys", "varys"),
        Map.entry("ygritte", "ygritte")
    );

    private GOTSpeechSpecificSelector() {}

    /**
     * Returns a dedicated bank when the role has one; otherwise null.
     * Hostile legendary dialogue is used only when an authored hostile bank
     * actually exists. Named NPCs with friendly-only dialogue fall back to the
     * normal hostile system rather than speaking a friendly line while hostile.
     */
    public static String dedicatedBank(Entity entity, boolean friendly) {
        String role = GOTSpeechRoleResolver.roleId(entity);
        if (role.isBlank()) return null;

        String legendaryStem = LEGENDARY.get(role);
        if (legendaryStem != null) {
            if (legendaryStem.equals("hodor")) {
                return GOTSpeechBankRegistry.has("legendary/hodor") ? "legendary/hodor" : null;
            }

            String wanted = "legendary/" + legendaryStem + (friendly ? "_friendly" : "_hostile");
            if (GOTSpeechBankRegistry.has(wanted)) return wanted;

            // Friendly-only authored legendary banks remain available in the
            // intended friendly state; hostile state continues to generic AI.
            if (friendly) {
                String fallback = "legendary/" + legendaryStem + "_friendly";
                if (GOTSpeechBankRegistry.has(fallback)) return fallback;
            }
        }

        return specialRoleBank(role, friendly);
    }

    private static String specialRoleBank(String role, boolean friendly) {
        if (role.contains("prostitute")) return existing("special/prostitute_" + side(friendly));
        if (role.contains("criminal")) return existing("special/criminal_" + side(friendly));
        if (role.contains("giant")) return existing("special/giant_" + side(friendly));
        if (role.equals("craster_wife")) return existing("legendary/cwife_" + side(friendly));
        if (role.contains("gladiator")) return existing("special/gladiator");
        if (role.contains("bandit")) return existing("special/bandit");
        if (role.contains("father")) return existing("special/father_" + side(friendly));
        return null;
    }

    private static String side(boolean friendly) {
        return friendly ? "friendly" : "hostile";
    }

    private static String existing(String bank) {
        return GOTSpeechBankRegistry.has(bank) ? bank : null;
    }
}
