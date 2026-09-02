package got.conquest.pact;

import got.GOTMod;
import got.faction.GOTFaction;
import net.minecraft.resources.ResourceLocation;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Baseline War of the Five Kings political camps used by the Conquest system.
 *
 * These are NOT player Pacts and are deliberately kept out of GOTPactSavedData.
 * Their UUIDs are deterministic so waypoint ownership remains valid across saves,
 * servers and NPC respawns.
 */
public final class GOTCanonicalPacts {
    public static final int DEFINITION_VERSION = 1;

    public static final ResourceLocation NORTH_AND_TRIDENT = key("north_and_trident");
    public static final ResourceLocation RENLY_TYRELL = key("renly_tyrell");
    public static final ResourceLocation STANNIS_VELARYON = key("stannis_velaryon");
    public static final ResourceLocation IRON_THRONE = key("iron_throne");
    public static final ResourceLocation IRON_ISLANDS = key("iron_islands");

    private static final List<GOTCanonicalPactDefinition> DEFINITIONS = List.of(
            pact(NORTH_AND_TRIDENT, "Kingdom of the North and Trident", "robb_stark",
                    member("robb_stark", "Robb Stark", GOTFaction.NORTH),
                    member("hoster_tully", "Hoster Tully", GOTFaction.RIVERLANDS),
                    member("edmure_tully", "Edmure Tully", GOTFaction.RIVERLANDS),
                    member("brynden_tully", "Brynden Tully", GOTFaction.RIVERLANDS),
                    member("roose_bolton", "Roose Bolton", GOTFaction.NORTH),
                    member("ramsay_bolton", "Ramsay Bolton", GOTFaction.NORTH),
                    member("rickard_karstark", "Rickard Karstark", GOTFaction.NORTH)),

            pact(RENLY_TYRELL, "Renly's Alliance", "renly_baratheon",
                    member("renly_baratheon", "Renly Baratheon", GOTFaction.STORMLANDS),
                    member("mace_tyrell", "Mace Tyrell", GOTFaction.REACH),
                    member("olenna_tyrell", "Olenna Tyrell", GOTFaction.REACH),
                    member("margaery_tyrell", "Margaery Tyrell", GOTFaction.REACH),
                    member("loras_tyrell", "Loras Tyrell", GOTFaction.REACH),
                    member("garlan_tyrell", "Garlan Tyrell", GOTFaction.REACH),
                    member("willas_tyrell", "Willas Tyrell", GOTFaction.REACH)),

            pact(STANNIS_VELARYON, "Stannis's Alliance", "stannis_baratheon",
                    member("stannis_baratheon", "Stannis Baratheon", GOTFaction.DRAGONSTONE),
                    member("monford_velaryon", "Monford Velaryon", GOTFaction.DRAGONSTONE)),

            pact(IRON_THRONE, "The Iron Throne", "joffrey_baratheon",
                    member("joffrey_baratheon", "Joffrey Baratheon", GOTFaction.CROWNLANDS),
                    member("cersei_lannister", "Cersei Lannister", GOTFaction.CROWNLANDS),
                    member("tywin_lannister", "Tywin Lannister", GOTFaction.WESTERLANDS),
                    member("jaime_lannister", "Jaime Lannister", GOTFaction.CROWNLANDS)),

            pact(IRON_ISLANDS, "Kingdom of the Iron Islands", "balon_greyjoy",
                    member("balon_greyjoy", "Balon Greyjoy", GOTFaction.IRONBORN),
                    member("yara_greyjoy", "Yara Greyjoy", GOTFaction.IRONBORN),
                    member("theon_greyjoy", "Theon Greyjoy", GOTFaction.IRONBORN))
    );

    private static final Map<ResourceLocation, GOTCanonicalPactDefinition> BY_KEY = indexByKey();
    private static final Map<UUID, GOTCanonicalPactDefinition> BY_ID = indexById();

    private GOTCanonicalPacts() {}

    public static List<GOTCanonicalPactDefinition> definitions() { return DEFINITIONS; }
    public static Optional<GOTCanonicalPactDefinition> byKey(ResourceLocation key) { return Optional.ofNullable(BY_KEY.get(key)); }
    public static Optional<GOTCanonicalPactDefinition> byId(UUID id) { return Optional.ofNullable(BY_ID.get(id)); }

    public static Optional<GOTCanonicalPactDefinition> byKey(String raw) {
        if (raw == null || raw.isBlank()) return Optional.empty();
        ResourceLocation parsed = ResourceLocation.tryParse(raw.contains(":") ? raw : GOTMod.MOD_ID + ":" + raw);
        return parsed == null ? Optional.empty() : byKey(parsed);
    }

    public static UUID deterministicId(ResourceLocation key) {
        return UUID.nameUUIDFromBytes((GOTMod.MOD_ID + ":canonical_pact:" + key).getBytes(StandardCharsets.UTF_8));
    }

    private static GOTCanonicalPactDefinition pact(ResourceLocation key, String name, String leader,
                                                     GOTCanonicalPactMember... members) {
        return new GOTCanonicalPactDefinition(key, deterministicId(key), name, leader, List.of(members));
    }

    private static GOTCanonicalPactMember member(String role, String name, GOTFaction faction) {
        return new GOTCanonicalPactMember(role, name, faction);
    }

    private static ResourceLocation key(String path) {
        return new ResourceLocation(GOTMod.MOD_ID, path);
    }

    private static Map<ResourceLocation, GOTCanonicalPactDefinition> indexByKey() {
        Map<ResourceLocation, GOTCanonicalPactDefinition> map = new LinkedHashMap<>();
        for (GOTCanonicalPactDefinition definition : DEFINITIONS) {
            if (map.put(definition.key(), definition) != null) {
                throw new IllegalStateException("Duplicate canonical Pact key " + definition.key());
            }
        }
        return Map.copyOf(map);
    }

    private static Map<UUID, GOTCanonicalPactDefinition> indexById() {
        Map<UUID, GOTCanonicalPactDefinition> map = new LinkedHashMap<>();
        for (GOTCanonicalPactDefinition definition : DEFINITIONS) {
            if (map.put(definition.id(), definition) != null) {
                throw new IllegalStateException("Duplicate canonical Pact UUID " + definition.id());
            }
        }
        return Map.copyOf(map);
    }
}
