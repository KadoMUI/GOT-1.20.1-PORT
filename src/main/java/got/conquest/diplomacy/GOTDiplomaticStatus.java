package got.conquest.diplomacy;

import java.util.Locale;

/** Symmetric formal relationship shared by two Pacts. */
public enum GOTDiplomaticStatus {
    PEACE,
    ALLIED,
    AT_WAR;

    public static GOTDiplomaticStatus byName(String raw) {
        if (raw == null) return null;
        String normalized = raw.trim().toUpperCase(Locale.ROOT).replace('-', '_');
        try {
            return valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
