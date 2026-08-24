package got.quest;

import java.util.Map;

/**
 * Named-character factory IDs recovered from the original GOTMiniQuestFactory.
 * Values are stable modern binding IDs; content loaders may bind datapack
 * definitions to them without losing the original identity.
 */
public final class GOTLegendaryQuestBindings {
    private GOTLegendaryQuestBindings() {}

    public static final Map<String,String> LEGACY_TO_MODERN = Map.ofEntries(
        Map.entry("HOWLAND","howland"), Map.entry("BALON","balon"),
        Map.entry("DAENERYS","daenerys"), Map.entry("VARYS","varys"),
        Map.entry("OBERYN","oberyn"), Map.entry("STANNIS","stannis"),
        Map.entry("JON_SNOW","jon_snow"), Map.entry("RENLY","renly"),
        Map.entry("KITRA","kitra"), Map.entry("BUGAI","bugai"),
        Map.entry("TYRION","tyrion"), Map.entry("CERSEI","cersei"),
        Map.entry("RAMSAY","ramsay"), Map.entry("SANDOR","sandor"),
        Map.entry("MELISANDRA","melisandra"), Map.entry("DORAN","doran"),
        Map.entry("MARGAERY","margaery"), Map.entry("ELLARYA","ellarya"),
        Map.entry("ARYA","arya"), Map.entry("OLENNA","olenna"),
        Map.entry("SAMWELL","samwell"), Map.entry("LYSA","lysa"),
        Map.entry("CATELYN","catelyn"), Map.entry("DAVEN","daven"),
        Map.entry("ARIANNE","arianne"), Map.entry("MELLARIO","mellario")
    );
}
